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

public class DeleteInvestigationValueDaoImpl implements DeleteInvestigationValueDao
{

	// ~ Static attributes/initializers
	/** The Constant LOGGER. */
	// -------------------------------------------------------------
	private static final Logger LOGGER = Logger.getLogger(DeleteInvestigationValueDaoImpl.class.getName());

	// ~ Methods
	// ------------------------------------------------------------------------------------

	private static DeleteInvestigationValueDaoImpl instance = null;
	private DeleteInvestigationValueDaoImpl()
	{

	}

	public static DeleteInvestigationValueDaoImpl getInstance()
	{
		if(instance == null)
		{
			instance = new DeleteInvestigationValueDaoImpl();
		}
		return instance;
	}
	/**
	 * This method returns ID of the Investigationvaluescasemapper which has been deleted
	 * from Investigationvaluescasemapper entity
	 * 
	 * 
	 * @param investigationValuesCaseMapperID
	 * - ID of the Investigationvaluescasemapper which is to be deleted 
	 * 
	 * @param userName.
	 * 
	 * 
	 * @return ID of the Investigationvaluescasemapper which has been deleted
	 * 
	 */
	@Override
	public Integer deleteInvestionValues(Integer investigationValuesCaseMapperID, String userName) throws Exception {
		// TODO Auto-generated method stub
		LOGGER.debug("Logger Name: " + LOGGER.getName());
		Session session = null;
		Integer investigationValuesCaseMapperIDDeleted = null;
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

			// Query for deleting values from InvestigationsValues Table
			Query query=session.createQuery("DELETE FROM Investigationsvalues WHERE investigationValuesCaseMapper.investigationValuesCaseMapperId=?");
			query.setParameter(0, investigationValuesCaseMapperID);
			query.executeUpdate();

			// Query for deleting values from Investigationvaluescasemapper Table
			Query investigationValuesCaseMapperQuery = session.createQuery("delete FROM Investigationvaluescasemapper  where investigationValuesCaseMapperId = ?");
			investigationValuesCaseMapperQuery.setParameter(0, investigationValuesCaseMapperID);
			investigationValuesCaseMapperQuery.executeUpdate();
			investigationValuesCaseMapperIDDeleted = investigationValuesCaseMapperID;

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
		return investigationValuesCaseMapperIDDeleted;

	}

}
