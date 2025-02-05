/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package com.cdac.common.util;

import java.util.Date;

import org.hibernate.Session;

import com.cdac.common.GeneratedEntities.Auditlog;
import com.cdac.common.audit.interceptor.IAuditLog;

// TODO: Auto-generated Javadoc
/**
 * The Class AuditLogUtil.
 */
public class AuditLogUtil {

	/**
	 * Log it.
	 *
	 * @param action
	 *            the action to be performed - Save/Update/Delete
	 * @param userId
	 *            the logged in user
	 * @param entity
	 *            the entity detail to be saved asaudit log
	 * @param session
	 *            the session
	 */
	public static void logIt(String action, String userId, IAuditLog entity, Session session) {

			Auditlog auditRecord = new Auditlog(userId, action,entity.getLogDeatil(), new Date(),entity.getId(),
					entity.getClass().toString());
		try
		{
			session.saveOrUpdate(auditRecord);
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}

	}
}