/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.cdac.framework.FwkServices;
import com.cdac.framework.IMedicalDevice;
import com.cdac.framework.PortManagerAndListener;
import com.cdac.framework.common.DeviceStatusNotifications;
import com.cdac.framework.common.IObserver;
import com.cdac.framework.common.Notification;
import com.cdac.framework.common.NotificationTypeConstants;

import application.Main;
import javafx.application.Platform;
import mediatorPattern.ControllerMediator;
import model.ConfigureDeviceSession;

public class BackendObserver implements IObserver
{
	private final Logger LOG = Logger.getLogger(BackendObserver.class);
	private Map<String, String> scannedDevicesMap = new HashMap<String, String>();

	public Map<String, String> getScannedDevicesMap()
	{
		return scannedDevicesMap;
	}

	@Override
	public void processNotification(Notification notification)
	{
		LOG.info("Application level notification received:" + notification);
		ControllerMediator.getInstance().registerBackendObserver(this);

		Platform.runLater(new Runnable()
		{
			@Override
			public void run()
			{

				if (notification.getNotificationType() == NotificationTypeConstants.CRITICAL_EVENT_NOTIFICATIONS)
				{
					String deviceName = ((IMedicalDevice) notification.getData()).getNameOfDevice();
					Main.getInstance().getUtility().showNotification("Warning", deviceName,
					        notification.getNotificationMessage());
				}
				else if (notification.getNotificationType() == NotificationTypeConstants.DEVICE_STATUS_NOTIFICATIONS
		                && notification.getNotificationMessage().equals(DeviceStatusNotifications.NEW_DEVICE_FOUND))
				{
					IMedicalDevice device = (IMedicalDevice) notification.getData();
					Main.getInstance().getUtility().showNotification("Information", device.getNameOfDevice(),
		                    notification.getNotificationMessage());


					String deviceName = device.getNameOfDevice();

					/*if (ControllerMediator.getInstance().getConfigureDevice() != null)
					if (ControllerMediator.getInstance().getConfigureDevice().getScannedDevicesMap() != null)
					if (!ControllerMediator.getInstance().getConfigureDevice().getScannedDevicesMap().keySet().contains(deviceName))*/
					if(!scannedDevicesMap.keySet().contains(deviceName))
					{
						String devicePort = "";
						//---Get list of ports along with devices connected to them (port-device mapping)
						List<PortManagerAndListener> devicePortMapping = FwkServices.getInstance().getPortDeviceMapping();
						for (PortManagerAndListener obj : devicePortMapping)
						{
								if (obj.getDeviceListeningToThisPort() != null)
								{
									//---Get the port for newly found device
									if (obj.getDeviceListeningToThisPort().getNameOfDevice().equalsIgnoreCase(deviceName))
									{
										devicePort = obj.getPortName();
										break;
									}
							}

						}

						//ControllerMediator.getInstance().getConfigureDevice().getScannedDevicesMap().put(deviceName, devicePort);
						scannedDevicesMap.put(deviceName, devicePort);
					}

					// Call the controllers for this device and have them listen to this device's parameters.
					Main.getInstance().getInfPumpListener().listenToTheDevice(device);
					Main.getInstance().getPTMonitorListener().listenToTheDevice(device);
					Main.getInstance().getAnesthesiaSettingsListener().listenToTheDevice(device);
					Main.getInstance().getAnesthesiaParametersListener().listenToTheDevice(device);
					Main.getInstance().getAnesthesiaWaveformListener().listenToTheDevice(device);
					Main.getInstance().getS5pmListener().listenToTheDevice(device);
					Main.getInstance().getS5PMWaveformListener().listenToTheDevice(device);
				}

				else if (notification.getNotificationType() == NotificationTypeConstants.DEVICE_STATUS_NOTIFICATIONS
		                && notification.getNotificationMessage().equals(DeviceStatusNotifications.DEVICE_NOT_RESPONDING))
				{
					String deviceName = ((IMedicalDevice) notification.getData()).getNameOfDevice();
					Main.getInstance().getUtility().errorDialogue(deviceName, notification.getNotificationMessage());
					ControllerMediator.getInstance().getManageAvailableDevicesController().deviceRemoved(deviceName);
					if(deviceName.contains("DeviceInjectomat"))
					{
						String medName=ControllerMediator.getInstance().getAssignInfusionPumpController()
				                .getConfiguredInfPumpMedMap().get(deviceName.split("-")[1].trim())
				                .getMedicationName();
						String serialNo=deviceName.split("-")[1].trim();
						ControllerMediator.getInstance().getMainController().getDrawTabbedCenter()
						        .stopInfusionWhenDeviceRemoved(medName,serialNo);

					}
					ConfigureDeviceSession.getInstance().getLastDeviceReadings().clear();
				}
				else if (notification.getNotificationType() == NotificationTypeConstants.DEVICE_STATUS_NOTIFICATIONS
		                && notification.getNotificationMessage().equals(DeviceStatusNotifications.DEVICE_REMOVED))
				{
					String deviceName = ((IMedicalDevice) notification.getData()).getNameOfDevice();
					Main.getInstance().getUtility().errorDialogue( deviceName,
					        notification.getNotificationMessage());
					ControllerMediator.getInstance().getManageAvailableDevicesController().deviceRemoved(deviceName);
					if(deviceName.contains("DeviceInjectomat"))
					{
						/*ControllerMediator.getInstance().getMainController().getDrawTabbedCenter().getMedsFromInfPump()
						        .remove(ControllerMediator.getInstance().getAssignInfusionPumpController()
						                .getConfiguredInfPumpMedMap().get(deviceName.split("-")[1].trim())
						                .getMedicationName());*/

						String medName=ControllerMediator.getInstance().getAssignInfusionPumpController()
				                .getConfiguredInfPumpMedMap().get(deviceName.split("-")[1].trim())
				                .getMedicationName();
						String serialNo=deviceName.split("-")[1].trim();
						ControllerMediator.getInstance().getMainController().getDrawTabbedCenter()
						        .stopInfusionWhenDeviceRemoved(medName,serialNo);
					}
					ConfigureDeviceSession.getInstance().getLastDeviceReadings().clear();
				}
			}
		});
	}
}
