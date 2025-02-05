/*******************************************************************************
3 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package com.cdac.common.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.cdac.common.GeneratedEntities.PatientMonitorData;
import com.cdac.common.audit.interceptor.AuditLogInterceptor;
import com.cdac.common.util.HibernateUtilities;
import com.cdac.framework.exceptions.ServiceExceptionWrapper;

public class PatientMonitorDataDaoImpl implements PatientMonitorDataDao
{

	/** The Constant LOGGER. */
	private static final Logger LOGGER = Logger.getLogger(PatientMonitorDataDaoImpl.class.getName());


	// ~ Methods
	// ------------------------------------------------------------------------------------
	
	private static PatientMonitorDataDaoImpl instance = null;
	private PatientMonitorDataDaoImpl()
	{

	}

	public static PatientMonitorDataDaoImpl getInstance()
	{
		if(instance == null)
		{
			instance = new PatientMonitorDataDaoImpl();
		}
		return instance;
	}
	/**
	 * Inserts a new record for PatientMonitorData
	 *
	 * @param patientMonitorDataList
	 * @param userName
	 * @return PatientMonitorData record entity
	 * @throws Exception
	 */
	//@SuppressWarnings("unchecked")
	@Override
	public void createPatientMonitorData(PatientMonitorData patientMonitorData,String userName)
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
			
			if(patientMonitorData.getPatientMonitorDataID() >0)
			{
				Date updatedTime= new Date();
				patientMonitorData.setUpdatedBy(userName);
				patientMonitorData.setUpdatedTime(updatedTime);
			}
			else
			{
				Date createdTime= new Date();
				patientMonitorData.setCreatedBy(userName);
				patientMonitorData.setCreatedTime(createdTime);
			}
			// saves/updates the anesthesia record 
		
				session.saveOrUpdate(patientMonitorData);
			
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
			e.printStackTrace();
			LOGGER.error("## Exception occured:" + e.getMessage());
			throw new ServiceExceptionWrapper(e);
		}
	}


	/**
	 * Get PatientMonitorData record details
	 *
	 * @param patientMonitorDataID
	 * @param patientCaseId
	 * @param userName
	 * @return List of PatientMonitorData record
	 * @throws Exception
	 */

	@SuppressWarnings("unchecked")
	@Override
	public PatientMonitorData getPatientMonitorData(Long patientMonitorDataId, String userName)
			throws Exception
	{
		LOGGER.debug("Logger Name: " + LOGGER.getName());
		Session session = null;
		List<PatientMonitorData> patientMonitorData=new ArrayList<>();

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

			// Get event log
			Query query = session.createQuery("FROM PatientMonitorData p "
					+ " where p.patientMonitorDataID=?");
			query.setParameter(0, patientMonitorDataId);
			patientMonitorData = query.list();
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
				e2.printStackTrace();
				LOGGER.error("## Exception occured:" + e2.getMessage());
				throw new ServiceExceptionWrapper(e2);
			}
		}

		if(patientMonitorData.size()>0)
		{
			return patientMonitorData.get(0);
		}
		else
		{
			return new PatientMonitorData();
		}

	}

	/**
	 * Get PatientMonitorData record details
	 *
	 * @param patientMonitorDataID
	 * @param patientCaseId
	 * @param userName
	 * @return List of PatientMonitorData record
	 * @throws Exception
	 */

	@SuppressWarnings("unchecked")
	@Override
	public PatientMonitorData getPatientMonitorDataTimeStampCaseID(Long caseId, Date timeStamp,String userName)
			throws Exception
	{
		LOGGER.debug("Logger Name: " + LOGGER.getName());
		Session session = null;
		List<PatientMonitorData> patientMonitorData=new ArrayList<>();

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
			Date startDate = timeStamp;
			 Calendar cal = Calendar.getInstance();
			 cal.setTime(startDate);
			
			 Date endDate = cal.getTime();
			 endDate.setSeconds(59);
			
		endDate.getMinutes();
			
			// Get event log
			Query query = session.createQuery("FROM PatientMonitorData p "
					+ " where p.caseId=? and p.timeStamp between ? and ? ");
			query.setParameter(0, caseId);
			query.setParameter(1, startDate);
			query.setParameter(2, endDate);
			patientMonitorData = query.list();
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

		if(patientMonitorData.size()>0)
		{
			return patientMonitorData.get(0);
		}
		else
		{
			return new PatientMonitorData();
		}

	}

	/**
	 * Get PatientMonitorData record details
	 *
	 * @param patientMonitorDataID
	 * @param patientCaseId
	 * @param userName
	 * @return List of PatientMonitorData record
	 * @throws Exception
	 */

	@SuppressWarnings("unchecked")
	@Override
	public List<PatientMonitorData> getAllPatientMonitorData(Long caseId,String userName)
			throws Exception
	{
		LOGGER.debug("Logger Name: " + LOGGER.getName());
		Session session = null;
		List<PatientMonitorData> patientMonitorDataList=new ArrayList<>();

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
			
		
			
			// Get event log
			Query query = session.createQuery("FROM PatientMonitorData p where p.caseId=?");
			query.setParameter(0, caseId);
			patientMonitorDataList = query.list();
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
				e2.printStackTrace();
				LOGGER.error("## Exception occured:" + e2.getMessage());
				throw new ServiceExceptionWrapper(e2);
			}
		}

		
			return patientMonitorDataList;
	}

	/**
	 * Get PatientMonitorData record details
	 *
	 * @param patientMonitorDataID
	 * @param patientCaseId
	 * @param userName
	 * @return List of PatientMonitorData record
	 * @throws Exception
	 */

	@SuppressWarnings("unchecked")
	@Override
	public List<PatientMonitorData> getTimedPatientMonitorData(Long caseId, Date startTime, Date endTime, String userName)
			throws Exception
	{
		LOGGER.debug("Logger Name: " + LOGGER.getName());
		Session session = null;
		List<PatientMonitorData> patientMonitorDataList=new ArrayList<>();

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
			
			// Get event log
			Query query = session.createQuery("FROM PatientMonitorData p where p.caseId=? AND p.timeStamp between ? and ?");
			query.setParameter(0, caseId);
			query.setParameter(1, startTime);
			query.setParameter(2, endTime);
			patientMonitorDataList = query.list();
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
				e2.printStackTrace();
				LOGGER.error("## Exception occured:" + e2.getMessage());
				throw new ServiceExceptionWrapper(e2);
			}
		}

		
			return patientMonitorDataList;
	}



}


