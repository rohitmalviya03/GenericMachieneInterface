/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package com.cdac.common.dao;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.cdac.common.audit.interceptor.AuditLogInterceptor;
import com.cdac.common.util.HibernateUtilities;
import com.cdac.framework.exceptions.ServiceExceptionWrapper;

public class GetDefaultUnitDaoImpl implements GetDefaultUnitDao
{
	// ~ Static attributes/initializers
	private static GetDefaultUnitDaoImpl getDefaultUnitDaoImpl = null;
	/** The Constant LOGGER. */
	// -------------------------------------------------------------
	private static final Logger LOGGER = Logger.getLogger(GetDefaultUnitDaoImpl.class.getName());


	// ~ Methods
	// ------------------------------------------------------------------------------------

	private GetDefaultUnitDaoImpl()
	{

	}

	public static GetDefaultUnitDaoImpl getInstance()
	{
		if(getDefaultUnitDaoImpl == null)
		{
			getDefaultUnitDaoImpl = new GetDefaultUnitDaoImpl();
		}
		return getDefaultUnitDaoImpl;
	}


	/**
	 * This method returns the default Unit corresponding to the medication sent
	 *
	 * @param medicationName
	 *
	 * @param userName.
	 * @return Default Unit
	 *
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String getDefaultUnit(String medicationName, String userName) throws Exception
	{
		LOGGER.debug("Logger Name: " + LOGGER.getName());
		String unit = null;
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

			// retrieve all the fluids from database
			Query query=session.createQuery("SELECT defaultUnit from Fdamedications f WHERE f.medicationsName="+"'" + medicationName + "'");

			Object obj = query.uniqueResult();
			if(obj!=null)
				unit = (String) obj;
			System.out.println(unit);

			//commits the session
			session.getTransaction().commit();
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

		return unit;
	}

}
