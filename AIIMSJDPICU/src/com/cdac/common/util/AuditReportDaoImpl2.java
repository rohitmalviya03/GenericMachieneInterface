/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package com.cdac.common.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.cdac.common.GeneratedEntities.AuditIntraOpReport2;
import com.cdac.common.audit.interceptor.AuditLogInterceptor;
import com.cdac.framework.exceptions.ServiceExceptionWrapper;

public class AuditReportDaoImpl2 {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = Logger.getLogger(AuditReportDaoImpl2.class.getName());


	// ~ Methods
	// ------------------------------------------------------------------------------------

	private static AuditReportDaoImpl2 instance = null;
	private AuditReportDaoImpl2()
	{

	}

	public static AuditReportDaoImpl2 getInstance()
	{
		if(instance == null)
		{
			instance = new AuditReportDaoImpl2();
		}
		return instance;
	}


	public void createAuditReport(AuditIntraOpReport2 auditIntraOpReport2,String userName)
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
			if(auditIntraOpReport2.getAuditReportID()!=null)
			{
				Date updatedTime= new Date();
				auditIntraOpReport2.setUpdatedBy(userName);
				auditIntraOpReport2.setUpdatedTime(updatedTime);
			}
			else
			{
				Date createdTime= new Date();
				auditIntraOpReport2.setCreatedBy(userName);
				auditIntraOpReport2.setCreatedTime(createdTime);
			}


			// saves/updates the Audit Report
			session.saveOrUpdate(auditIntraOpReport2);

			session.getTransaction().commit();
			session.close();
		}

		catch (Exception e)
		{e.printStackTrace();
			if (session != null)
			{
				// If transaction is open,rollback
				session.getTransaction().rollback();
			}
			LOGGER.error("## Exception occured:" + e.getMessage());
			throw new ServiceExceptionWrapper(e);
		}
	}

	public AuditIntraOpReport2 getAuditReport(Long patientCaseId, String userName)
			throws Exception
	{
		LOGGER.debug("Logger Name: " + LOGGER.getName());
		Session session = null;
		List<AuditIntraOpReport2> auditIntraOpReport2=new ArrayList<>();

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
			Query query = session.createQuery("FROM AuditIntraOpReport2 c "
			        + " where c.caseID=?");
			query.setParameter(0, patientCaseId);
			auditIntraOpReport2 = query.list();
			session.getTransaction().commit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
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

		if(auditIntraOpReport2.size()>0)
		{
			return auditIntraOpReport2.get(0);
		}
		else
		{
			return new AuditIntraOpReport2();
		}

	}

}


