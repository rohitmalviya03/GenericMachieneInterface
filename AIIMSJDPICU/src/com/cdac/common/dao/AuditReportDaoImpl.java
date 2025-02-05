/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package com.cdac.common.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.cdac.common.GeneratedEntities.AuditReport;
import com.cdac.common.audit.interceptor.AuditLogInterceptor;
import com.cdac.common.util.HibernateUtilities;
import com.cdac.framework.exceptions.ServiceExceptionWrapper;

public class AuditReportDaoImpl implements AuditReportDao {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = Logger.getLogger(AuditReportDaoImpl.class.getName());


	// ~ Methods
	// ------------------------------------------------------------------------------------

	private static AuditReportDaoImpl instance = null;
	private AuditReportDaoImpl()
	{

	}

	public static AuditReportDaoImpl getInstance()
	{
		if(instance == null)
		{
			instance = new AuditReportDaoImpl();
		}
		return instance;
	}


	public void createAuditReport(AuditReport auditReport,String userName)
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


			// for setting created time and created by in case of new record and updated time and updated by in case of exsisting record
			if(auditReport.getauditReportID()!=null)
			{
				Date updatedTime= new Date();
				auditReport.setUpdatedBy(userName);
				auditReport.setUpdatedTime(updatedTime);
			}
			else
			{
				Date createdTime= new Date();
				auditReport.setCreatedBy(userName);
				auditReport.setCreatedTime(createdTime);
			}


			// saves/updates the Audit Report
			session.saveOrUpdate(auditReport);

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

	public AuditReport getAuditReport(Long patientCaseId, String userName)
			throws Exception
	{
		LOGGER.debug("Logger Name: " + LOGGER.getName());
		Session session = null;
		List<AuditReport> auditReport=new ArrayList<>();

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
			Query query = session.createQuery("FROM AuditReport c "
			        + " where c.caseID=?");
			query.setParameter(0, patientCaseId);
			auditReport = query.list();
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

		if(auditReport.size()>0)
		{
			return auditReport.get(0);
		}
		else
		{
			return new AuditReport();
		}

	}

}
