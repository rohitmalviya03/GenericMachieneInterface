/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package com.cdac.common.dao;

import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.cdac.common.GeneratedEntities.Investigationparameterfields;
import com.cdac.common.GeneratedEntities.Investigationstests;
import com.cdac.common.GeneratedEntities.Investigationsvalues;
import com.cdac.common.GeneratedEntities.Investigationvaluescasemapper;
import com.cdac.common.GeneratedEntities.Patientcase;
import com.cdac.common.audit.interceptor.AuditLogInterceptor;
import com.cdac.common.pojoClasses.InvestigationSetValue;
import com.cdac.common.util.HibernateUtilities;
import com.cdac.framework.exceptions.ServiceExceptionWrapper;

public class SetInvestigationValuesDaoImpl implements SetInvestigationValuesDao
{
	// ~ Static attributes/initializers
	/** The Constant LOGGER. */
	// -------------------------------------------------------------
	private static final Logger LOGGER = Logger.getLogger(SetInvestigationValuesDaoImpl.class.getName());



	// ~ Methods
	// ------------------------------------------------------------------------------------

	private static SetInvestigationValuesDaoImpl instance = null;
	private SetInvestigationValuesDaoImpl()
	{

	}

	public static SetInvestigationValuesDaoImpl getInstance()
	{
		if(instance == null)
		{
			instance = new SetInvestigationValuesDaoImpl();
		}
		return instance;
	}


	/**
	 * This method returns string containing success/error message for saving the test value
	 * from Investigationsvalues entity
	 *
	 * @param caseId
	 *
	 * @param testID
	 * -ID of the test whose values is to be saved
	 *
	 * @param investigationsValues
	 * - list of values of the test
	 *
	 * @param testDate
	 *
	 * @param details
	 * @param userName.
	 *
	 * @return map list of devices configured w.r.t. patientID and caseID.
	 *
	 */
	@Override
	public String setInvestigationValues(Long caseID, Integer testID, List<InvestigationSetValue> investigationsValues,Date testDate,String details,String userName) throws Exception
	{
		LOGGER.debug("Logger Name: " + LOGGER.getName());
		Session session = null;
		Session session2=null;
		Investigationvaluescasemapper investigationValueCaseMapperObject = new Investigationvaluescasemapper();
		String result="";
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



			// for creating a new Investigation value Case Mapper

			Investigationvaluescasemapper investigationvaluescasemapper=new Investigationvaluescasemapper();
			Patientcase patientcase = (Patientcase)session.get(Patientcase.class,caseID);
			investigationvaluescasemapper.setPatientcase(patientcase);
			/*Date date = new Date();
			investigationvaluescasemapper.setDate(date);*/
			investigationvaluescasemapper.setDate(testDate);
			investigationvaluescasemapper.setDetails(details);
			Investigationstests investigationstests = (Investigationstests)session.get(Investigationstests.class,testID);
			investigationvaluescasemapper.setinvestigationsTests(investigationstests);
			investigationvaluescasemapper.setCreatedBy(userName);
			Date createdTime= new Date();
			investigationvaluescasemapper.setCreatedTime(createdTime);

			session.saveOrUpdate(investigationvaluescasemapper);

			//commiting the session
			session.getTransaction().commit();

			//closing the session
			session.close();
			investigationValueCaseMapperObject=investigationvaluescasemapper;



			// opens a new session from the session factory
			AuditLogInterceptor interceptor1 = new AuditLogInterceptor();
			session2 = sessionFactory.withOptions().interceptor(interceptor1).openSession();
			interceptor1.setSession(session2);
			interceptor1.setUserName(userName);
			session2.getTransaction().begin();

			/// for creating new entry into InvestigationValues Table
			if(!investigationsValues.isEmpty())
			{
				for(InvestigationSetValue investigationSetValue : investigationsValues)
				{
					Investigationsvalues investigationsValuesObject = new Investigationsvalues();
					if(investigationSetValue.getValue()!=null)
					{
						investigationsValuesObject.setValue(investigationSetValue.getValue());
					}
					if(investigationSetValue.getExponentValue()!=null){
						investigationsValuesObject.setExponent(investigationSetValue.getExponentValue());
					}
					Investigationparameterfields investigationparameterfields=(Investigationparameterfields)session2.get(Investigationparameterfields.class,investigationSetValue.getInvestigationParameterFieldID());

					investigationsValuesObject.setMeasuringUnit(investigationparameterfields.getMeasuringUnit());
					investigationsValuesObject.setMeasuringUnitRatio(investigationparameterfields.getMeasuringUnitRatio());
					investigationsValuesObject.setInvestigationTestsParameters(investigationparameterfields.getInvestigationTestsParameters());
					investigationsValuesObject.setInvestigationValuesCaseMapper(investigationValueCaseMapperObject);
					investigationsValuesObject.setCreatedBy(userName);
					investigationsValuesObject.setCreatedTime(createdTime);
					session2.saveOrUpdate(investigationsValuesObject);
				}
			}
			result="Succesfully saved the test values";
		}
		catch (Exception e)
		{
			if (session2 != null && session2.getTransaction() != null)
			{
				// If transaction is open,rollback
				session2.getTransaction().rollback();
			}
			LOGGER.error("## Exception occured:" + e.getMessage());
			throw new ServiceExceptionWrapper(e);
		}
		finally
		{
			try
			{
				if(session2 != null)
				{
					session2.close(); // Session closes
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


