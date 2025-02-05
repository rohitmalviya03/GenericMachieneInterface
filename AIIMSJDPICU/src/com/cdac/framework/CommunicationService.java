/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package com.cdac.framework;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.cdac.framework.common.IObserver;
import com.cdac.framework.common.Notification;
import com.cdac.framework.common.NotificationType;
import com.cdac.framework.common.ObserverAndTypeWrapper;

public class CommunicationService
{
	private static final Logger LOG = Logger.getLogger(CommunicationService.class);
	private static CommunicationService commService;
	private List<ObserverAndTypeWrapper> observers;

	static
	{
		commService = new CommunicationService();
	}

	private CommunicationService()
	{
		observers = new ArrayList<ObserverAndTypeWrapper>();
	}

	public static CommunicationService getInstance()
	{
		return commService;
	}

	public synchronized void registerObservers(IObserver observer, NotificationType notificationType)
	{
		try {
			observers.add(new ObserverAndTypeWrapper(observer, notificationType));
		} catch (Exception e) {
			LOG.error("## Exception occured:", e);
		}
	}

	public synchronized void sendNotification(Notification notification)
	{
		// XXX: The logic may be converted to a HashMap instead of a list where
		// in HashMap, the key will be the NotificationType and it's value can
		// be a List of observers for that NotificationType.

		try {
			NotificationType notificationType = notification.getNotificationType();
			LOG.info("Sending notification of type:" + notificationType);
			LOG.info("Observers:" + observers);
			for (ObserverAndTypeWrapper observerWrapper : observers)
			{
				if (observerWrapper.getNotificationType().equals(notificationType.getNotificationTypeString()))
				{
					// XXX: May need to execute this in a separate thread.
					observerWrapper.getObserver().processNotification(notification);
				}
			}
		} catch (Exception e) {
			LOG.error("## Exception occured:", e);
		}
	}
}
