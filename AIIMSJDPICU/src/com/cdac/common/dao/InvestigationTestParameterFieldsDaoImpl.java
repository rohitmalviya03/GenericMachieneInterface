/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package com.cdac.common.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.cdac.common.GeneratedEntities.Investigationparameterfields;
import com.cdac.common.GeneratedEntities.Investigationstests;
import com.cdac.common.audit.interceptor.AuditLogInterceptor;
import com.cdac.common.util.HibernateUtilities;
import com.cdac.framework.exceptions.ServiceExceptionWrapper;

public class InvestigationTestParameterFieldsDaoImpl implements InvestigationTestParameterFieldsDao
{

	// ~ Static attributes/initializers
	/** The Constant LOGGER. */
	// -------------------------------------------------------------

	private static final Logger LOGGER = Logger.getLogger(InvestigationTestParameterFieldsDaoImpl.class.getName());


	// ~ Methods
	// ------------------------------------------------------------------------------------

	private static InvestigationTestParameterFieldsDaoImpl instance = null;
	private InvestigationTestParameterFieldsDaoImpl()
	{

	}

	public static InvestigationTestParameterFieldsDaoImpl getInstance()
	{
		if(instance == null)
		{
			instance = new InvestigationTestParameterFieldsDaoImpl();
		}
		return instance;
	}


	/**
	 * This method returns list of test parameter fields corresponding to the test id provided by the user
	 * from Investigationparameterfields entity
	 *
	 *
	 * @param test
	 * - test whose paramters is to be retirieved
	 *
	 *
	 *
	 * @param userName.
	 *
	 * @return list of test parameter fields corresponding to the test id provided by the user
	 *
	 */
	/*@SuppressWarnings("unchecked")
	@Override
	public List<Investigationparameterfields> getAllParameterField(Investigationstests test, String userName)
			throws Exception
	{

		LOGGER.info("Logger Name: " + LOGGER.getName());
		List<Investigationparameterfields> listOfInvestigationParameterFields=new ArrayList<Investigationparameterfields>();
		List<Investigationtestsparameters> listOfInvestigationTestParameters=new ArrayList<Investigationtestsparameters>();
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

			/// retrieves the list of parameters for the provided test ID
			Query query=session.createQuery("from Investigationtestsparameters where investigationTest.investigationsTestsId=?");
			query.setParameter(0, test.getInvestigationsTestsId());
			listOfInvestigationTestParameters=query.list();
			for(Investigationtestsparameters object : listOfInvestigationTestParameters)
			{
				Query query2=session.createQuery("from Investigationparameterfields where investigationTestsParameters.investigationTestsParametersId=?");
				query2.setParameter(0,object.getInvestigationTestsParametersId());
				List<Investigationparameterfields> listOfInvestigationParameterFields2=new ArrayList<Investigationparameterfields>();
				listOfInvestigationParameterFields2=query2.list();

				for(Investigationparameterfields object2 : listOfInvestigationParameterFields2)
				{
					listOfInvestigationParameterFields.add(object2);
				}
			}

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
			LOGGER.log(Level.INFO, "Exception occur", e);
			throw e;
		}
		finally
		{
			session.close(); // Session close
		}

		return listOfInvestigationParameterFields;
	}*/


	/**
	 * This method returns list of test parameter fields corresponding to the test id provided by the user
	 * from Investigationparameterfields entity
	 *
	 *
	 * @param test
	 * - test whose paramters is to be retirieved
	 *
	 *
	 *
	 * @param userName.
	 *
	 * @return list of test parameter fields corresponding to the test id provided by the user
	 *
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Investigationparameterfields> getAllParameterField(Investigationstests test, String userName)
			throws Exception
	{

		LOGGER.debug("Logger Name: " + LOGGER.getName());
		List<Investigationparameterfields> listOfInvestigationParameterFields=new ArrayList<Investigationparameterfields>();
		List<Object> InvestigationTestParameterList = new ArrayList<Object>();
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

			/// retrieves the list of parameters fields for the provided test ID
			Query query=session.createQuery("from Investigationtestsparameters IP inner join Investigationparameterfields IPF on IP.investigationTestsParametersId=IPF.investigationTestsParameters.investigationTestsParametersId where IP.investigationTest.investigationsTestsId=? order by IP.testParameterOrder");
			query.setParameter(0, test.getInvestigationsTestsId());
			InvestigationTestParameterList=query.list();
			for(Iterator<Object> iterator = InvestigationTestParameterList.iterator(); iterator.hasNext();)
			{
				Object[] resultset=(Object[]) iterator.next();
				Investigationparameterfields investigationparameterfields=(Investigationparameterfields) resultset[1];
				listOfInvestigationParameterFields.add(investigationparameterfields);

			}

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

		return listOfInvestigationParameterFields;
	}

}
