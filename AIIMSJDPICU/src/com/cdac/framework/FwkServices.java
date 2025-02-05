/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package com.cdac.framework;

import java.util.ArrayList;
import java.util.List;

import com.cdac.appStartup.ApplicationStartup;
import com.cdac.framework.common.IObserver;
import com.cdac.framework.common.NotificationType;
import com.fazecast.jSerialComm.SerialPort;

/**
 * Front-end application can use this to access/query the back-end application.
 *
 * @author Sahil_Jain08
 *
 */
public class FwkServices
{
	private static FwkServices fwkService;
	private List<String> allSupportedDevices;

	static
	{
		fwkService = new FwkServices();
	}

	/**
	 * Singleton
	 */
	private FwkServices()
	{
		DeviceIdentificationService deviceIdentificationService = DeviceIdentificationService.getInstance();
		List<IDeviceHandshake> deviceHandshakes = deviceIdentificationService.getHandshakeList();
		allSupportedDevices = new ArrayList<String>();
		for (IDeviceHandshake deviceHandshake : deviceHandshakes)
		{
			allSupportedDevices.add(deviceHandshake.getNameOfDevice());
		}
	}

	/**
	 * Use this method to get a single instance of this class.
	 *
	 * @return
	 */
	public static FwkServices getInstance()
	{
		return fwkService;
	}

	/**
	 * Returns the names of all the devices which are supported.
	 *
	 * @return List of device names.
	 */
	public List<String> getAllSupportedDeviceNames()
	{
		return allSupportedDevices;
	}

	/**
	 * Returns a port-device mapping. Use
	 * {@link PortManagerAndListener#getThisPort()} to get the port to which
	 * this object belongs. Use
	 * {@link PortManagerAndListener#getDeviceListeningToThisPort()} to get the
	 * device which is listening to this port.
	 *
	 * @return List of PortManagerAndListener which contains all ports available
	 *         to the system and the devices currently listening to those ports.
	 */
	public List<PortManagerAndListener> getPortDeviceMapping()
	{
		return DeviceScanner.getInstance().getPortDeviceMapping();
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
	public void startDeviceManually(SerialPort port, String deviceName)
	{
		DeviceScanner.getInstance().submitManualDeviceCommunicationRequest(port, deviceName);
	}

	/**
	 * Frontend application can use this API to register for various types on
	 * notifications from the backend application. The frontend application must
	 * implement {@link IObserver} interface in order to become eligible for
	 * receiving notifications.
	 *
	 * @param observer
	 * @param notificationType
	 */
	public void registerForBackendEvents(IObserver observer, NotificationType notificationType)
	{
		CommunicationService.getInstance().registerObservers(observer, notificationType);
	}

	/**
	 * Starts the automatic device identification service.
	 */
	public void startIdentifyingDevicesAutomatically()
	{
		ApplicationStartup.startIdentifyingDevicesAutomatically();
	}

	/**
	 * Starts the automatic device identification service.
	 */
	public void setHandShakeClass(String handshakeClass)
	{
		ApplicationStartup.setHandshakeClass(handshakeClass);
	}

	/**
	 * Starts the automatic device identification service.
	 */
	public void removeDeviceListeners()
	{
		ApplicationStartup.stopDevices();
	}

	/**
	 * Starts the automatic device identification service.
	 */
	public void removeDeviceListenerForSelectedDevice(String deviceName)
	{
		ApplicationStartup.stopDevice(deviceName);
	}

	/**
	 * Stops the automatic device identification service.
	 */
	public void stopIdentifyingDevicesAutomatically()
	{
		ApplicationStartup.stopIdentifyingDevicesAutomatically();
	}

	/**
	 * Gets the status of the Automatic Device Identification Service.
	 *
	 * @return true if the service is actively running for identifying new
	 *         devices.
	 */
	public boolean getAutomaticDeviceIdentificationServiceStatus()
	{
		return ApplicationStartup.getAutoDeviceIdentificationStatus();
	}

	/**
	 * Returns a reference to the list which holds all the captured snapshots.
	 * @return List of snapshots. Any manipulations on the list (Example deleting a snapshot) will reflect back to the original list.
	 */
	public List getSnaphsotList()
	{
		return SnapshotList.getInstance().getSnapshotList();
	}
}
