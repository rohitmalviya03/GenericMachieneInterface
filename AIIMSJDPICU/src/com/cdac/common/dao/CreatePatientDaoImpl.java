/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package com.cdac.common.dao;

import java.util.Date;
import org.apache.log4j.Logger;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.cdac.common.GeneratedEntities.Patient;
import com.cdac.common.audit.interceptor.AuditLogInterceptor;
import com.cdac.common.util.HibernateUtilities;
import com.cdac.framework.exceptions.CRNumberExceptions;
import com.cdac.framework.exceptions.ServiceExceptionWrapper;


// TODO: Auto-generated Javadoc
/**
 * The Class CreatePatientDaoImpl.
 */
public class CreatePatientDaoImpl implements CreatePatientDao
{

	// ~ Static attributes/initializers
	/** The Constant LOGGER. */
	// -------------------------------------------------------------
	private static final Logger LOGGER = Logger.getLogger(CreatePatientDaoImpl.class.getName());
	
	private static CreatePatientDaoImpl instance = null;
	private CreatePatientDaoImpl()
	{

	}

	public static CreatePatientDaoImpl getInstance()
	{
		if(instance == null)
		{
			instance = new CreatePatientDaoImpl();
		}
		return instance;
	}
	/**
	 * This method takes Patient as input Persisting createPatient Entity for
	 * new patient persisted.
	 *
	 * @param createPatientEntity
	 *            the patient entity containing details of the patient to be
	 *            added
	 * @param userName
	 *            the logged in user
	 * @return Patient Entity having details of newly created Patient
	 * @throws Exception 
	 */
	@Override
	public Patient createPatient(Patient createPatientEntity, String userName) throws Exception {

		LOGGER.debug("Logger Name: " + LOGGER.getName());
		Boolean isCRNumThere=false;
		Session session = null;

		try {
			// opens a new session from the session factory

			SessionFactory sessionFactory = HibernateUtilities.createSessionFactory();

			// For logging audit record
			AuditLogInterceptor interceptor = new AuditLogInterceptor();
			session = sessionFactory.withOptions().interceptor(interceptor).openSession();
			interceptor.setSession(session);
			interceptor.setUserName(userName);
			session.getTransaction().begin();

			// for setting created time and created by in case of new record and updated time and updated by in case of exsisting record
			if(createPatientEntity.getPatientId()!=null)
			{
				Date updatedTime= new Date();
				createPatientEntity.setUpdatedBy(userName);
				createPatientEntity.setUpdatedTime(updatedTime);
			}

			else
			{
				isCRNumThere=checkCrNumber(createPatientEntity.getCrnumber());
				if(!isCRNumThere)
				{
					throw new CRNumberExceptions();
				}
				Date createdTime= new Date();
				createPatientEntity.setCreatedBy(userName);
				createPatientEntity.setCreatedTime(createdTime);
			}

			//System.out.println("Patient ID - " + createPatientEntity.getPatientId());
			session.saveOrUpdate(createPatientEntity); // Persisting
			//System.out.println("Patient ID - " + createPatientEntity.getPatientId());
			// createPatient Entity
			// in database

			session.getTransaction().commit(); // Session commit

		}
		catch (CRNumberExceptions ce)
		{
			if (session != null && session.getTransaction() != null)
			{
				// If transaction is open,rollback
				session.getTransaction().rollback();
			}
			LOGGER.error("## Exception occured:" + ce.getMessage());
			throw new CRNumberExceptions();
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
		return createPatientEntity;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.infosys.common.dao.CreatePatientDao#checkCrNumber(java.lang.Long)
	 */
	/**
	 * This method takes Long CRNumber as input to check if this exists already
	 * and returns boolean false if found this CR Number.
	 *
	 * @param crNumber
	 *            the CR number to be checked for if already present
	 * @return booelean false in case CR number already exists else
	 *         empty
	 * @throws Exception 
	 */
	@Override
	public boolean checkCrNumber(Long crNumber) throws Exception
	{
		LOGGER.debug("Logger Name: " + LOGGER.getName());
		boolean crNumberAvailable = false;
		Session session=null;
		try {
			SessionFactory sessionFactory = HibernateUtilities.createSessionFactory();
			session = sessionFactory.openSession(); 

			// opens a new session from the session factory
			session.getTransaction().begin();
			Query query = session.createQuery("SELECT COUNT(cpe.patientId) FROM Patient cpe where cpe.crnumber=?");
			query.setParameter(0,crNumber);
			Long count = (Long)query.uniqueResult();

			if (count>0) {
				crNumberAvailable = false;
			}
			else
			{
				crNumberAvailable = true;
			}

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
		return crNumberAvailable;
	}

}
