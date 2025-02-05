/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package com.cdac.common.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.cdac.common.GeneratedEntities.Investigationparameterfields;
import com.cdac.common.GeneratedEntities.Investigationsvalues;
import com.cdac.common.GeneratedEntities.Investigationvaluescasemapper;
import com.cdac.common.audit.interceptor.AuditLogInterceptor;
import com.cdac.common.pojoClasses.InvestigationMapperAndValue;
import com.cdac.common.pojoClasses.InvestigationSetValue;
import com.cdac.common.util.HibernateUtilities;
import com.cdac.framework.exceptions.ServiceExceptionWrapper;


public class GetInvestigationValueViewDaoImpl implements GetInvestigationValueViewDao
{

	// ~ Static attributes/initializers
	// -------------------------------------------------------------
	private static final Logger LOGGER = Logger.getLogger(GetInvestigationValueViewDaoImpl.class.getName());




	// ~ Methods
	// ------------------------------------------------------------------------------------

	private static GetInvestigationValueViewDaoImpl instance = null;
	private GetInvestigationValueViewDaoImpl()
	{

	}

	public static GetInvestigationValueViewDaoImpl getInstance()
	{
		if(instance == null)
		{
			instance = new GetInvestigationValueViewDaoImpl();
		}
		return instance;
	}


	/*@SuppressWarnings("unchecked")
	@Override
	public List<Investigationsvalues> getInvestigationValuesView(Integer patientID, Long caseID, String userName) throws Exception
	{
		LOGGER.debug("Logger Name: " + LOGGER.getName());




		LOGGER.debug("Logger Name: " + LOGGER.getName());
		List<Investigationsvalues> listOfInvestigationValues=new ArrayList<Investigationsvalues>();
		Session session = null;
		new ArrayList<Investigationtestsparameters>();
		List<Investigationsvalues> result = new ArrayList<Investigationsvalues>();
		new ArrayList<Investigationparameterfields>();
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


//			Query query=session.createQuery("FROM Investigationtestsparameters where investigationTest.investigationsTestsId=?");
//			query.setParameter(0,investigationsTestsID );
//			testParameterResult=query.list();
			for(Investigationtestsparameters ObjectTestParameter : testParameterResult)
			{



			Query query = session.createQuery("from Investigationsvalues where investigationValuesPacMapper.pac.patient.patientId=? and investigationValuesPacMapper.pac.patientcase.caseId=?");
			query.setParameter(0, patientID);
			query.setParameter(1, caseID);
			result=query.list();



			for(Investigationsvalues objectInvestigationsvalues : result)
			{
				listOfInvestigationValues.add(objectInvestigationsvalues);
			}

			//}

			Query query = session.createQuery("FROM Investigationtestsparameters");
			testParameterResult=query.list();
			for(Iterator<Investigationtestsparameters> iterator = testParameterResult.iterator();iterator.hasNext();)
			{
				Investigationtestsparameters investigationtestsparameters=(Investigationtestsparameters)iterator.next();
				Query query2 = session.createQuery(
						"FROM Investigationsvalues ive where ive.investigationValuesPacMapper.pac.createPatient.patientID=? and ive.investigationValuesPacMapper.pac.createCase.caseID=? and "
								+ "ive.investigationValuesPacMapper.investigationsTests.investigationsTestsId=? and ive.investigationTestsParameters.investigationTestsParametersId=? "
								+ "ORDER BY ive.investigationValuesPacMapper.date DESC");
				query2.setParameter(0, patientID);
				query2.setParameter(1, caseID);
				query2.setParameter(2, investigationsTestsID);
				query2.setParameter(3, investigationtestsparameters.getInvestigationTestsParametersId());
				result = query2.list();
				if (!result.isEmpty())
				{
					Query query3 = session.createQuery("FROM Investigationparameterfields ipfe where ipfe.investigationTestsParameters.investigationTestsParametersId=?");
					query3.setParameter(0,result.get(0).getInvestigationTestsParameters().getInvestigationTestsParametersId());
					investigationFieldResult = query3.list();
				}


			}
		}
		catch (Exception e)
		{

		}
		return listOfInvestigationValues;
	}*/
	/*@SuppressWarnings("unchecked")
	@Override
	public List<Investigationvaluescasemapper> getInvestigationvaluecasemapperlist(Long caseID, Integer testID,
			String userName) throws Exception
	{
		LOGGER.debug("Logger Name: " + LOGGER.getName());
		List<Investigationvaluescasemapper> listOfTestRecord = new ArrayList<Investigationvaluescasemapper>();
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

			// retrieving the list of test record

			Query query = session.createQuery("from Investigationvaluescasemapper where patientcase.caseId=? and investigationsTests.investigationsTestsId=?");
			query.setParameter(0, caseID);
			query.setParameter(1, testID);
			listOfTestRecord=query.list();

			session.getTransaction().commit(); //commiting the session
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
		return listOfTestRecord;
	}*/


	/**
	 * This method returns list containing test case mapper object and its related test values
	 *
	 * @param caseID
	 *
	 * @param testID
	 *
	 * @param userName
	 *
	 * @return list containing test case mapper object and its related test values
	 *
	 */

	@SuppressWarnings("unchecked")
	@Override
	public List<InvestigationMapperAndValue> getInvestigationvaluecasemapperlist(Long caseID, Date startDate, Date endDate, String userName) throws Exception
	{
		LOGGER.debug("Logger Name: " + LOGGER.getName());
		//List<InvestigationMapperAndValue> listOfInvestigationMapperAndValue = new ArrayList<InvestigationMapperAndValue>();
		//List<Investigationvaluescasemapper> listOfMapperRecord = new ArrayList<Investigationvaluescasemapper>();
		Session session = null;
		List<InvestigationSetValue> listInvestigationSetValues= new ArrayList<InvestigationSetValue>();
		List<InvestigationMapperAndValue> listInvestigationMapperAndValue= new ArrayList<InvestigationMapperAndValue>();
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

			Query query=session.createQuery("SELECT investigationsTestsId from Investigationstests where isAvailableInIntraOp=1");
			List<Integer> tests=query.list();

//			StringBuilder bldr = new StringBuilder("");
//
//			for(int i=0;i<tests.size();i++)
//			{
//				bldr.append(tests.get(i));
//				bldr.append(",");
//			}
//			String testsIDs = bldr.substring(0, bldr.length()-1);

			// retrieving the list of test record

//			query = session.createQuery("from Investigationvaluescasemapper m inner join  Investigationsvalues v  on m.investigationValuesCaseMapperId=v.investigationValuesCaseMapper.investigationValuesCaseMapperId inner join  Investigationparameterfields f on v.investigationTestsParameters.investigationTestsParametersId = f.investigationTestsParameters.investigationTestsParametersId where m.patientcase.caseId=? and m.investigationsTests.investigationsTestsId=? and m.date=?");
			query = session.createQuery("from Investigationvaluescasemapper m inner join  Investigationsvalues v  on m.investigationValuesCaseMapperId=v.investigationValuesCaseMapper.investigationValuesCaseMapperId inner join  Investigationparameterfields f on v.investigationTestsParameters.investigationTestsParametersId = f.investigationTestsParameters.investigationTestsParametersId where m.patientcase.caseId=:case and m.investigationsTests.investigationsTestsId IN(:names) AND m.date BETWEEN :start and :end");
			query.setParameter("case", caseID);
			query.setParameterList("names", tests);
			query.setParameter("start", startDate);
			query.setParameter("end", endDate);
			List<Object> listOfRecordFromDB=query.list();

			if(!listOfRecordFromDB.isEmpty())
			{
				/// getting the first Mapper Object
				Object[] dummyResultSet=(Object[])listOfRecordFromDB.get(0);
				Investigationvaluescasemapper LastMapperObject=(Investigationvaluescasemapper)dummyResultSet[0];
				Integer lastID=LastMapperObject.getInvestigationValuesCaseMapperId();


				for(Iterator<Object> iterator = listOfRecordFromDB.iterator(); iterator.hasNext();)
				{
					Object[] resultset=(Object[]) iterator.next();
					Investigationvaluescasemapper mapper=(Investigationvaluescasemapper)resultset[0];
					Investigationsvalues values= (Investigationsvalues) resultset[1];
					Investigationparameterfields parameter=(Investigationparameterfields) resultset[2];

					Integer currentMapperID=mapper.getInvestigationValuesCaseMapperId();

					if(currentMapperID.equals(lastID) && iterator.hasNext())
					{
						InvestigationSetValue investigationSetValue = new InvestigationSetValue();
						investigationSetValue.setInvestigationParameterFieldID(parameter.getInvestigationParameterFieldsId());
						investigationSetValue.setName(parameter.getName());
						if(values.getValue()!=null)
						{
							investigationSetValue.setValue(values.getValue());
						}
						if(values.getExponent()!=null)
						{
							investigationSetValue.setExponentValue(values.getExponent());
						}
						investigationSetValue.setMeasuringUnit(parameter.getMeasuringUnit());
						listInvestigationSetValues.add(investigationSetValue);

						lastID=mapper.getInvestigationValuesCaseMapperId();
						LastMapperObject=mapper;
					}
					else if (!iterator.hasNext())
					{
						if(currentMapperID!=lastID)
						{

							InvestigationMapperAndValue investigationMapperAndValue = new InvestigationMapperAndValue();

							investigationMapperAndValue.setInvestigationSetValueList(listInvestigationSetValues);
							investigationMapperAndValue.setInvestigationvaluescasemapper(LastMapperObject);

							listInvestigationMapperAndValue.add(investigationMapperAndValue);
							listInvestigationSetValues = new ArrayList<InvestigationSetValue>();
						}

						InvestigationSetValue investigationSetValue = new InvestigationSetValue();
						investigationSetValue.setInvestigationParameterFieldID(parameter.getInvestigationParameterFieldsId());
						investigationSetValue.setName(parameter.getName());
						if(values.getValue()!=null)
						{
							investigationSetValue.setValue(values.getValue());
						}
						if(values.getExponent()!=null)
						{
							investigationSetValue.setExponentValue(values.getExponent());
						}
						investigationSetValue.setMeasuringUnit(parameter.getMeasuringUnit());
						listInvestigationSetValues.add(investigationSetValue);

						InvestigationMapperAndValue investigationMapperAndValue1 = new InvestigationMapperAndValue();

						investigationMapperAndValue1.setInvestigationSetValueList(listInvestigationSetValues);
						investigationMapperAndValue1.setInvestigationvaluescasemapper(mapper);

						listInvestigationMapperAndValue.add(investigationMapperAndValue1);
					}
					else
					{
						InvestigationMapperAndValue investigationMapperAndValue = new InvestigationMapperAndValue();

						investigationMapperAndValue.setInvestigationSetValueList(listInvestigationSetValues);
						investigationMapperAndValue.setInvestigationvaluescasemapper(LastMapperObject);

						listInvestigationMapperAndValue.add(investigationMapperAndValue);

						lastID=mapper.getInvestigationValuesCaseMapperId();
						listInvestigationSetValues = new ArrayList<InvestigationSetValue>();

						InvestigationSetValue investigationSetValue = new InvestigationSetValue();
						investigationSetValue.setInvestigationParameterFieldID(parameter.getInvestigationParameterFieldsId());
						investigationSetValue.setName(parameter.getName());
						if(values.getValue()!=null)
						{
							investigationSetValue.setValue(values.getValue());
						}
						investigationSetValue.setMeasuringUnit(parameter.getMeasuringUnit());
						listInvestigationSetValues.add(investigationSetValue);

						LastMapperObject=mapper;
					}
				}
			}


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
		return listInvestigationMapperAndValue;
	}

	/**
	 * This method returns list containing test case mapper object and its related test values
	 *
	 * @param caseID
	 *
	 * @param testID
	 *
	 * @param userName
	 *
	 * @return list containing test case mapper object and its related test values
	 *
	 */

	@SuppressWarnings("unchecked")
	@Override
	public InvestigationMapperAndValue getInvestigationvaluecasemapper(Long caseID, Integer testID,Date timeStamp,
			String userName) throws Exception
	{
		LOGGER.debug("Logger Name: " + LOGGER.getName());
		//List<InvestigationMapperAndValue> listOfInvestigationMapperAndValue = new ArrayList<InvestigationMapperAndValue>();
		//List<Investigationvaluescasemapper> listOfMapperRecord = new ArrayList<Investigationvaluescasemapper>();
		Session session = null;
		List<InvestigationSetValue> listInvestigationSetValues= new ArrayList<InvestigationSetValue>();
		InvestigationMapperAndValue investigationMapperAndValue1= new InvestigationMapperAndValue();
		List<InvestigationMapperAndValue> listInvestigationMapperAndValue= new ArrayList<InvestigationMapperAndValue>();
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

			// retrieving the list of test record

			Query query = session.createQuery("from Investigationvaluescasemapper m inner join  Investigationsvalues v  on m.investigationValuesCaseMapperId=v.investigationValuesCaseMapper.investigationValuesCaseMapperId inner join  Investigationparameterfields f on v.investigationTestsParameters.investigationTestsParametersId = f.investigationTestsParameters.investigationTestsParametersId where m.patientcase.caseId=? and m.investigationsTests.investigationsTestsId=? and m.date=?");
			query.setParameter(0, caseID);
			query.setParameter(1, testID);
			query.setParameter(2, timeStamp);
	//		investigationMapperAndValue1=(InvestigationMapperAndValue) query.uniqueResult();
			List<Object> listOfRecordFromDB=query.list();

			if(!listOfRecordFromDB.isEmpty())
			{
				/// getting the first Mapper Object
				Object[] dummyResultSet=(Object[])listOfRecordFromDB.get(0);
				Investigationvaluescasemapper LastMapperObject=(Investigationvaluescasemapper)dummyResultSet[0];
				Integer lastID=LastMapperObject.getInvestigationValuesCaseMapperId();


				for(Iterator<Object> iterator = listOfRecordFromDB.iterator(); iterator.hasNext();)
				{
					Object[] resultset=(Object[]) iterator.next();
					Investigationvaluescasemapper mapper=(Investigationvaluescasemapper)resultset[0];
					Investigationsvalues values= (Investigationsvalues) resultset[1];
					Investigationparameterfields parameter=(Investigationparameterfields) resultset[2];

					Integer currentMapperID=mapper.getInvestigationValuesCaseMapperId();

					if(currentMapperID.equals(lastID) && iterator.hasNext())
					{
						InvestigationSetValue investigationSetValue = new InvestigationSetValue();
						investigationSetValue.setInvestigationParameterFieldID(parameter.getInvestigationParameterFieldsId());
						investigationSetValue.setName(parameter.getName());
						if(values.getValue()!=null)
						{
							investigationSetValue.setValue(values.getValue());
						}
						investigationSetValue.setMeasuringUnit(parameter.getMeasuringUnit());
						listInvestigationSetValues.add(investigationSetValue);

						lastID=mapper.getInvestigationValuesCaseMapperId();
						LastMapperObject=mapper;
					}
					else if (!iterator.hasNext())
					{
						if(currentMapperID!=lastID)
						{

							InvestigationMapperAndValue investigationMapperAndValue = new InvestigationMapperAndValue();

							investigationMapperAndValue.setInvestigationSetValueList(listInvestigationSetValues);
							investigationMapperAndValue.setInvestigationvaluescasemapper(LastMapperObject);

							listInvestigationMapperAndValue.add(investigationMapperAndValue);
							listInvestigationSetValues = new ArrayList<InvestigationSetValue>();
						}

						InvestigationSetValue investigationSetValue = new InvestigationSetValue();
						investigationSetValue.setInvestigationParameterFieldID(parameter.getInvestigationParameterFieldsId());
						investigationSetValue.setName(parameter.getName());
						if(values.getValue()!=null)
						{
							investigationSetValue.setValue(values.getValue());
						}
						investigationSetValue.setMeasuringUnit(parameter.getMeasuringUnit());
						listInvestigationSetValues.add(investigationSetValue);

						InvestigationMapperAndValue investigationMapperAndValue2 = new InvestigationMapperAndValue();

						investigationMapperAndValue1.setInvestigationSetValueList(listInvestigationSetValues);
						investigationMapperAndValue1.setInvestigationvaluescasemapper(mapper);

						listInvestigationMapperAndValue.add(investigationMapperAndValue1);
					}
					else
					{
						InvestigationMapperAndValue investigationMapperAndValue = new InvestigationMapperAndValue();

						investigationMapperAndValue.setInvestigationSetValueList(listInvestigationSetValues);
						investigationMapperAndValue.setInvestigationvaluescasemapper(LastMapperObject);

						listInvestigationMapperAndValue.add(investigationMapperAndValue);

						lastID=mapper.getInvestigationValuesCaseMapperId();
						listInvestigationSetValues = new ArrayList<InvestigationSetValue>();

						InvestigationSetValue investigationSetValue = new InvestigationSetValue();
						investigationSetValue.setInvestigationParameterFieldID(parameter.getInvestigationParameterFieldsId());
						investigationSetValue.setName(parameter.getName());
						if(values.getValue()!=null)
						{
							investigationSetValue.setValue(values.getValue());
						}
						investigationSetValue.setMeasuringUnit(parameter.getMeasuringUnit());
						listInvestigationSetValues.add(investigationSetValue);

						LastMapperObject=mapper;
					}
				}
			}


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
		return investigationMapperAndValue1;
	}



	/**
	 * This method returns list containing test values corresponding to test case mapper ID
	 *
	 * @param investigationTestCasemapeperID
	 *
	 *
	 * @param userName
	 *
	 *
	 * @return list containing test values corresponding to test case mapper ID
	 *
	 */

	/*@SuppressWarnings("unchecked")
	@Override
	public List<InvestigationSetValue> getInvestigationValueslist(Integer investigationTestCasemapeperID,String userName) throws Exception
	{
		LOGGER.debug("Logger Name: " + LOGGER.getName());
		List<InvestigationSetValue> investigationValueslist = new ArrayList<InvestigationSetValue>();
		List<Investigationsvalues> listOfInvestigationRecordFromDB= new ArrayList<Investigationsvalues>();
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

			// retrieving the list of investigation value list


			Query query= session.createQuery("from Investigationsvalues where investigationValuesCaseMapper.investigationValuesCaseMapperId = ?");
			query.setParameter(0, investigationTestCasemapeperID);
			listOfInvestigationRecordFromDB=query.list();

			if(!listOfInvestigationRecordFromDB.isEmpty())
			{
				for(Investigationsvalues DBEntry : listOfInvestigationRecordFromDB)
				{
					InvestigationSetValue invSetValue = new InvestigationSetValue();
					Query query2= session.createQuery("from Investigationparameterfields where investigationTestsParameters.investigationTestsParametersId=?");
					query2.setParameter(0, DBEntry.getInvestigationTestsParameters().getInvestigationTestsParametersId());
					List<Investigationparameterfields> investigationParameterObject = new ArrayList<Investigationparameterfields>();
					investigationParameterObject=query2.list();

					invSetValue.setName(investigationParameterObject.get(0).getName());
					if(DBEntry.getValue()!=null)
					{
						invSetValue.setValue(DBEntry.getValue());
					}
					invSetValue.setInvestigationParameterFieldID(investigationParameterObject.get(0).getInvestigationParameterFieldsId());
					investigationValueslist.add(invSetValue);
				}
			}
			session.getTransaction().commit(); //commiting the session
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
		return investigationValueslist;
	}*/


	/**
	 * This method returns list containing test values corresponding to test case mapper ID
	 *
	 * @param investigationTestCasemapeperID
	 *
	 *
	 * @param userName
	 *
	 *
	 * @return list containing test values corresponding to test case mapper ID
	 *
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<InvestigationSetValue> getInvestigationValueslist(Integer investigationTestCasemapeperID,String userName) throws Exception
	{
		LOGGER.debug("Logger Name: " + LOGGER.getName());
		List<InvestigationSetValue> investigationValueslist = new ArrayList<InvestigationSetValue>();
		Session session = null;
		List<Object> listOfValuesAndParameters= new ArrayList<Object>();
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

			// retrieving the list of investigation value list
			Query query= session.createQuery("from Investigationsvalues IV inner join Investigationtestsparameters ITP on IV.investigationTestsParameters.investigationTestsParametersId=ITP.investigationTestsParametersId inner join  Investigationparameterfields IPF on ITP.investigationTestsParametersId=IPF.investigationTestsParameters.investigationTestsParametersId    where IV.investigationValuesCaseMapper.investigationValuesCaseMapperId = ?");
			query.setParameter(0, investigationTestCasemapeperID);
			listOfValuesAndParameters=query.list();


			for(Iterator<Object> iterator=listOfValuesAndParameters.iterator(); iterator.hasNext();)
			{

				Object[] resultset=(Object[]) iterator.next();
				Investigationsvalues valuesObject=(Investigationsvalues) resultset[0];
				Investigationparameterfields fieldObject=(Investigationparameterfields) resultset[2];

				InvestigationSetValue invSetValue = new InvestigationSetValue();
				invSetValue.setName(fieldObject.getName());
				invSetValue.setValue(valuesObject.getValue());
				invSetValue.setMeasuringUnit(fieldObject.getMeasuringUnit());
				invSetValue.setInvestigationParameterFieldID(fieldObject.getInvestigationParameterFieldsId());

				investigationValueslist.add(invSetValue);


			}

			//commiting the session
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
		return investigationValueslist;
	}
	/**
	 * This method returns list containing test values corresponding to test case mapper ID
	 *
	 * @param investigationTestCasemapeperID
	 *
	 *
	 * @param userName
	 *
	 *
	 * @return list containing test values corresponding to test case mapper ID
	 *
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<InvestigationSetValue> getInvestigationSetValueslist(String userName) throws Exception
	{
		LOGGER.debug("Logger Name: " + LOGGER.getName());
		List<InvestigationSetValue> investigationValueslist = new ArrayList<InvestigationSetValue>();
		List<Investigationparameterfields> investigationparameterfields = new ArrayList<Investigationparameterfields>();
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

			// retrieving the list of investigation value list

			Query query1= session.createQuery("from Investigationparameterfields");
			investigationparameterfields= query1.list();

			for (Iterator iterator = investigationparameterfields.iterator(); iterator.hasNext();) {
				Object object = (Object) iterator.next();
				InvestigationSetValue investigationSetValue = new InvestigationSetValue();
				Investigationparameterfields fieldObject=(Investigationparameterfields) object;
				investigationSetValue.setName(fieldObject.getName());
				investigationSetValue.setMeasuringUnit(fieldObject.getMeasuringUnit());
				investigationSetValue.setInvestigationParameterFieldID(fieldObject.getInvestigationParameterFieldsId());
				investigationValueslist.add(investigationSetValue);
			}


			//commiting the session
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
		return investigationValueslist;
	}

}
