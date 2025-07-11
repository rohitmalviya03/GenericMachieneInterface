/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package com.cdac.framework.common;

import com.cdac.framework.CoreDevice;

public class DeviceStatusNotifications extends Notification
{
	public static NotificationType DEVICE_STATUS_NOTIFICATION = NotificationTypeConstants.DEVICE_STATUS_NOTIFICATIONS;

	public static String NEW_DEVICE_FOUND = "New device found";
	public static String DEVICE_REMOVED = "A device was removed";
	public static String DEVICE_NOT_RESPONDING = "A device has stopped responding. The data parser is removed. Please Re-Scan.";
	public static String DEVICE_SNAPSHOT_READY = "Snapshot of a device is ready";

	public DeviceStatusNotifications(String notificationMessage)
	{
		super(DEVICE_STATUS_NOTIFICATION, notificationMessage);
	}

	public void setData(CoreDevice device)
	{
		super.setData(device);
	}

	public CoreDevice getData()
	{
		return (CoreDevice) super.getData();
	}
}
