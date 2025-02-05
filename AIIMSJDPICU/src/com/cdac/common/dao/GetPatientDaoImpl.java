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

import com.cdac.common.GeneratedEntities.Patient;
import com.cdac.common.audit.interceptor.AuditLogInterceptor;
import com.cdac.common.util.HibernateUtilities;
import com.cdac.framework.exceptions.ServiceExceptionWrapper;

public class GetPatientDaoImpl implements GetPatientDao{

	// ~ Static attributes/initializers
	/** The Constant LOGGER. */
	// -------------------------------------------------------------
	private static final Logger LOGGER = Logger.getLogger(GetPatientDaoImpl.class.getName());


	// ~ Methods
	// ------------------------------------------------------------------------------------

	private static GetPatientDaoImpl instance = null;
	private GetPatientDaoImpl()
	{

	}

	public static GetPatientDaoImpl getInstance()
	{
		if(instance == null)
		{
			instance = new GetPatientDaoImpl();
		}
		return instance;
	}

	/**
	 * This method returns patient object
	 * from Patient entity
	 * 
	 * 
	 * @param patientID
	 * 
	 * 
	 * @param userName.
	 * 
	 * 
	 * @return patient object
	 * 
	 */
	@Override
	public Patient getPatient(int patientID, String userName) throws Exception 
	{
		LOGGER.debug("Logger Name: " + LOGGER.getName());
		Patient patient;
		Session session = null;
		try 
		{

			// opens a new session from the session factory
			SessionFactory sessionFactory = HibernateUtilities.createSessionFactory();
			AuditLogInterceptor interceptor = new AuditLogInterceptor();
			session = sessionFactory.withOptions().interceptor(interceptor).openSession();
			interceptor.setSession(session);
			interceptor.setUserName(userName);
			session.getTransaction().begin();

			/// retrieve the patient object
			patient=(Patient)session.get(Patient.class,patientID);


			session.getTransaction().commit(); // Session commit

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
		return patient;
	}

}
