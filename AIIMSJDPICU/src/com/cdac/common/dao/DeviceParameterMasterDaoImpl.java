/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package com.cdac.common.dao;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.cdac.common.GeneratedEntities.DeviceParameterMaster;
import com.cdac.common.audit.interceptor.AuditLogInterceptor;
import com.cdac.common.util.HibernateUtilities;
import com.cdac.framework.exceptions.ServiceExceptionWrapper;

public class DeviceParameterMasterDaoImpl implements DeviceParameterMasterDao
{

	/** The Constant LOGGER. */
	private static final Logger LOGGER = Logger.getLogger(DeviceParameterMasterDaoImpl.class.getName());


	// ~ Methods
	// ------------------------------------------------------------------------------------
	/**
	 * Inserts a new record for DeviceParameter
	 *
	 * @param deviceParameterMasterData
	 * @param userName
	 * @return DeviceParameter
	 *  record entity
	 * @throws Exception
	 */
	//@SuppressWarnings("unchecked")
	@Override
	public void createDeviceParameter(DeviceParameterMaster deviceParameterMasterData,String userName)
			throws Exception
	{
		LOGGER.debug("Logger Name: " + LOGGER.getName());
		Session session = null;
		try
		{
			// opens a new session from the session factory
			SessionFactory sessionFactory = HibernateUtilities.createSessionFactory();

			// For logging audit record
			AuditLogInterceptor interceptor = new AuditLogInterceptor();
			session = sessionFactory.withOptions().interceptor(interceptor).openSession();
			interceptor.setSession(session);
			interceptor.setUserName(userName);
			session.getTransaction().begin();


			// saves/updates the anesthesia record
			session.saveOrUpdate(deviceParameterMasterData);

			session.getTransaction().commit();
			session.close();
		}

		catch (Exception e)
		{
			if (session != null)
			{
				// If transaction is open,rollback
				session.getTransaction().rollback();
			}
			LOGGER.error("## Exception occured:" + e.getMessage());
			throw new ServiceExceptionWrapper(e);
		}
	}

}


