/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
/*
 * 
 */
package com.cdac.common.dao;

import java.util.Date;
import org.apache.log4j.Logger;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.cdac.common.GeneratedEntities.IntraopLocation;
import com.cdac.common.audit.interceptor.AuditLogInterceptor;
import com.cdac.common.util.HibernateUtilities;
import com.cdac.framework.exceptions.ServiceExceptionWrapper;

// TODO: Auto-generated Javadoc
/**
 * The Class CreateLocationDaoImpl.
 */
public class CreateLocationDaoImpl implements CreateLocationDao{

	/** The Constant LOGGER. */
	private static final Logger LOGGER = Logger.getLogger(CreateLocationDaoImpl.class.getName());

	/* (non-Javadoc)
	 * @see com.infosys.common.dao.CreateLocationDao#createLocation(com.infosys.common.GeneratedEntities.IntraopLocation)
	 */


	// ~ Methods
	// ------------------------------------------------------------------------------------

	private static CreateLocationDaoImpl instance = null;
	private CreateLocationDaoImpl()
	{

	}

	public static CreateLocationDaoImpl getInstance()
	{
		if(instance == null)
		{
			instance = new CreateLocationDaoImpl();
		}
		return instance;
	}

	/**
	 * This method returns string with success/error message for saving itraop location object
	 * from IntraopLocation entity
	 * 
	 * 
	 * @param intraopLocationEntity
	 * - intraopLocationEntity object containing the Location details which is to be saved
	 * 
	 * @param userName
	 * @return string with success/error message for saving itraop location object
	 * @throws Exception 
	 * 
	 */
	@Override
	public String createLocation(IntraopLocation intraopLocationEntity, String userName) throws Exception {

		String result="";
		Session session = null;
		try {

			// opens a new session from the session factory
			SessionFactory sessionFactory = HibernateUtilities.createSessionFactory();

			//session=sessionFactory.openSession();

			// For logging audit record
			AuditLogInterceptor interceptor = new AuditLogInterceptor();
			session = sessionFactory.withOptions().interceptor(interceptor).openSession();
			interceptor.setSession(session);
			interceptor.setUserName("ABC");
			session.getTransaction().begin();

			// for setting created time and created by in case of new record and updated time and updated by in case of exsisting record
			if(intraopLocationEntity.getLocationId()!=null)
			{
				Date updatedTime= new Date();
				intraopLocationEntity.setUpdatedBy(userName);
				intraopLocationEntity.setUpdatedTime(updatedTime);
			}
			else
			{
				Date createdTime= new Date();
				intraopLocationEntity.setCreatedBy(userName);
				intraopLocationEntity.setCreatedTime(createdTime);
			}

			// Save
			session.saveOrUpdate(intraopLocationEntity);
	 

			session.getTransaction().commit();
			result="succesfully added location"+intraopLocationEntity.getLocationId();
		}
		catch (Exception e)
		{
			if (session != null && session.getTransaction() != null)
			{
				// If transaction is open,rollback
				session.getTransaction().rollback();
			}
			LOGGER.error("## Exception occured:" + e.getMessage());
			throw new ServiceExceptionWrapper(e);
		}
		finally
		{
			try 
			{
				if(session != null)
				{
					session.close(); // Session closes
				}
			}
			catch (Exception e2) 
			{
				LOGGER.error("## Exception occured:" + e2.getMessage());
				throw new ServiceExceptionWrapper(e2);
			}
		}

		return result;
	}

}
