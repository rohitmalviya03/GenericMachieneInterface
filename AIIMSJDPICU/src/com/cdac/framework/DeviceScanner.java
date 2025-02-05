/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package com.cdac.framework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;

import com.cdac.framework.common.DeviceStatusNotifications;
import com.cdac.framework.common.Notification;
import com.cdac.framework.common.NotificationTypeConstants;
import com.fazecast.jSerialComm.SerialPort;

public class DeviceScanner
{
	private static final Logger LOG = Logger.getLogger(DeviceScanner.class);
	static DeviceScanner singleInstance;

	// The comPorts are being mapped to the work executor that will be working
	// on them.
	// AutoWorkExecutor will remain null for ports not identified yet.
	SerialPort[] allPorts;
	Queue<HandshakePortWrapper> manualSetupRequests;
	Map<PortManagerAndListener, Future> allPortManagerAndListeners;

	static
	{
		singleInstance = new DeviceScanner();
	}

	private DeviceScanner()
	{
		allPortManagerAndListeners = new HashMap<PortManagerAndListener, Future>();
//		generateListOfAvailablePorts();
		refreshListOfAvailablePorts();
		manualSetupRequests = new LinkedList<HandshakePortWrapper>();
	}

	public static DeviceScanner getInstance()
	{
		return singleInstance;
	}

	/**
	 * Essentially be called only once on startup. This lists all the available
	 * ports in the system and adds an application level listener to each of
	 * them.
	 */
	/*public void generateListOfAvailablePorts()
	{
		LOG.info("Generating list of available ports.");

		allPorts = SerialPort.getCommPorts();

		LOG.info("Ports available:" + allPorts);

		for (SerialPort port : allPorts)
		{
			allPortManagerAndListeners.add(new PortManagerAndListener(port));
		}
	}*/

	/**
	 * Essentially be called only once on startup. This lists all the available
	 * ports in the system and adds an application level listener to each of
	 * them.
	 */
	public void refreshListOfAvailablePorts()
	{
		LOG.info("Generating list of available ports.");

		allPorts = SerialPort.getCommPorts();

		LOG.info("Ports available:" + allPorts);

		for (SerialPort port : allPorts)
		{
			boolean portFound = false;
			for (PortManagerAndListener managedPort : allPortManagerAndListeners.keySet())
			{
				if (port.getDescriptivePortName().equals(managedPort.getPortName()))
				{
					portFound = true;
					break;
				}
			}
			if (!portFound)
			{
				LOG.info("### New port found:" + port.getDescriptivePortName());
				allPortManagerAndListeners.put(new PortManagerAndListener(port), null);
			}
		}

		for (PortManagerAndListener managedPort : allPortManagerAndListeners.keySet())
		{
			if (managedPort.getDeviceListeningToThisPort() == null)
			{
				boolean portFound = false;
				// Free port. Let's verify if it still exists.
				for (SerialPort newPort : allPorts)
				{
					if(managedPort.getPortName().equals(newPort.getDescriptivePortName()))
					{
						portFound = true;
						break;
					}
				}

				if(!portFound)
				{
					// Port no longer available. Remove it.
					allPortManagerAndListeners.remove(managedPort);
					LOG.info("### Removed obsolete port:" + managedPort.getPortName());
					Notification portRemoved = new Notification(NotificationTypeConstants.CRITICAL_EVENT_NOTIFICATIONS,
					        "Port no longer available", managedPort.getPortName());
					CommunicationService.getInstance().sendNotification(portRemoved);
				}
			}
		}
	}

	public void removeListeners()
	{
		for (PortManagerAndListener portManager : allPortManagerAndListeners.keySet())
		{
			if (portManager.getDeviceListeningToThisPort() != null)
			{
				portManager.stopListening();
			}
		}
	}

	public void removeListener(String deviceName)
	{
		for (PortManagerAndListener portManager : allPortManagerAndListeners.keySet())
		{
			if (portManager.getDeviceListeningToThisPort() !=null && portManager.getDeviceListeningToThisPort().getNameOfDevice() !=null && portManager.getDeviceListeningToThisPort().getNameOfDevice().equals(deviceName) && portManager.getDeviceListeningToThisPort() != null)
			{
				portManager.stopListening();
				break;
			}
		}
	}

	/**
	 * Entry point on port scanning and device identification.
	 */
	public void identifyDeviceAndRegisterPorts()
	{
		LOG.info("Scanning for devices...");
		for (PortManagerAndListener portManager : allPortManagerAndListeners.keySet())
		{
			if (portManager.getDeviceListeningToThisPort() == null)
			{
				// No existing device registered on this port.

				LOG.info("Begin identification chain.");
				LOG.info("Scanning on port:" + portManager.getPortName());

				CoreDevice workExecutor = DeviceIdentificationService.getInstance().getFirstHandshake()
				        .identifyDevice(portManager);

				LOG.info("Executor: " + workExecutor + " active on port " + portManager.getPortName());
				// Device identified on a given port.
				if (workExecutor != null)
				{
					// Send Notification
					dispatchNewDeviceNotification(workExecutor);
				}
				else
				{
					// TODO: Log or ignore.
				}

			}
			else
			{
				LOG.info(
				        portManager.getDeviceListeningToThisPort() + " is active on port:" + portManager.getPortName());
			}
		}
		LOG.info("Exit scanning");
	}

	public void executeManualConfigurationRequests()
	{
		LOG.info("Initiating manual setup");

		for (HandshakePortWrapper handshakePortWrapper = manualSetupRequests
		        .poll(); handshakePortWrapper != null; handshakePortWrapper = manualSetupRequests.poll())
		{
			AbstractDeviceHandshake deviceHandshake = (AbstractDeviceHandshake) handshakePortWrapper.getDevice();
			SerialPort requestedPort = handshakePortWrapper.getPort();
			CoreDevice workExecutor = deviceHandshake.getExecutorInstance();
			LOG.info("Request for manual setup of device - " + workExecutor.getNameOfDevice() + " submitted on port "
			        + requestedPort.getDescriptivePortName());

			for (PortManagerAndListener managerAndListener : allPortManagerAndListeners.keySet())
			{
				if (managerAndListener.getThisPort() == requestedPort)
				{
					if (managerAndListener.getDeviceListeningToThisPort() != null)
					{
						// TODO: Shoot a notification informing the
						// device/front-end about device being removed from
						// listening to this port.
						dispatchStoppedListeningToDeviceNotification(managerAndListener.getDeviceListeningToThisPort());
					}

					managerAndListener.setDeviceListeningToThisPort(workExecutor);
					// Send Notification
					dispatchNewDeviceNotification(workExecutor);
				}
			}

		}

		LOG.info("Manual device setup complete");
	}

	/**
	 * Identifies and removes the Non-Responding device parsers.
	 */
	public void checkDeviceHeartbeat()
	{
		LOG.info("Checking for devices that are not responding.");
		for (PortManagerAndListener portManager : allPortManagerAndListeners.keySet())
		{
			portManager.checkDeviceResponseTime();
		}
	}

	/**
	 * Dispatches new device notifications
	 *
	 * @param device
	 *            is the device that was just found.
	 */
	public void dispatchNewDeviceNotification(IMedicalDevice device)
	{
		LOG.info("Sending new device notification.");
		// Dispatch notification to Front-end
		DeviceStatusNotifications newDeviceNotification = new DeviceStatusNotifications(
		        DeviceStatusNotifications.NEW_DEVICE_FOUND);
		newDeviceNotification.setData(device);
		LOG.info("Notification:" + newDeviceNotification);
		CommunicationService.getInstance().sendNotification(newDeviceNotification);
	}

	public void dispatchStoppedListeningToDeviceNotification(IMedicalDevice medicalDevice)
	{
		LOG.info("Sending a notification about not listening to device:" + medicalDevice.getNameOfDevice());
		// Dispatch notification to Front-end
		DeviceStatusNotifications deviceRemovedNotification = new DeviceStatusNotifications(
		        DeviceStatusNotifications.DEVICE_REMOVED);
		deviceRemovedNotification.setData(medicalDevice);
		LOG.info("Notification:" + deviceRemovedNotification);
		CommunicationService.getInstance().sendNotification(deviceRemovedNotification);
	}

	public List<PortManagerAndListener> getPortDeviceMapping()
	{
		return new ArrayList(allPortManagerAndListeners.keySet());
	}

	/**
	 * Submit a request to directly instantiate a device on the given port. The
	 * handshake logic will be skipped and the corresponding device will be
	 * instantiated direclty.<br>
	 * <b>Note:</b> This request is queued and is executed once the
	 * auto-identification process completes.
	 *
	 * @param port
	 * @param deviceName
	 */
	void submitManualDeviceCommunicationRequest(SerialPort port, String deviceName)
	{
		for (IDeviceHandshake deviceHandshake : DeviceIdentificationService.getInstance().getHandshakeList())
		{
			if (deviceHandshake.getNameOfDevice().equals(deviceName))
			{
				manualSetupRequests.add(new HandshakePortWrapper(port, deviceHandshake));
			}
		}
	}
}
