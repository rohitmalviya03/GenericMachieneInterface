/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package com.cdac.common.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.cdac.common.GeneratedEntities.Patientcase;
import com.cdac.common.GeneratedEntities.Recoveryroomreadings;
import com.cdac.common.audit.interceptor.AuditLogInterceptor;
import com.cdac.common.util.HibernateUtilities;
import com.cdac.framework.exceptions.ServiceExceptionWrapper;

public class RecoveryRoomReadingDaoImpl implements RecoveryRoomReadingDao{

	// ~ Static attributes/initializers
	/** The Constant LOGGER. */
	// -------------------------------------------------------------
	private static final Logger LOGGER = Logger.getLogger(RecoveryRoomReadingDaoImpl.class.getName());


	// ~ Methods
	// ------------------------------------------------------------------------------------

	private static RecoveryRoomReadingDaoImpl instance = null;
	private RecoveryRoomReadingDaoImpl()
	{

	}

	public static RecoveryRoomReadingDaoImpl getInstance()
	{
		if(instance == null)
		{
			instance = new RecoveryRoomReadingDaoImpl();
		}
		return instance;
	}


	/**
	 * This method returns ID of the Recoveryroomreadings record which is saved or updated
	 * from Recoveryroomreadings entity
	 *
	 *
	 * @param recoveryroomreadings
	 * - recoveryroomreadings object containing the recoveryroomreadings details which is to be saved
	 *
	 *
	 *
	 * @param userName.
	 *
	 * @return ID of the Recoveryroomreadings record which is saved or updated
	 *
	 */
	@Override
	public List<Integer> saveOrUpdate(Recoveryroomreadings recoveryroomreadings, String userName, Integer upperRange, Integer lowerRange) throws Exception
	{
		LOGGER.debug("Logger Name: " + LOGGER.getName());
		Session session = null;
		List<Integer> recoveryroomreadingsId= new ArrayList<Integer>();
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
			if(recoveryroomreadings.getRecoveryRoomReadingRecordId()!=null)
			{
				Date updatedTime= new Date();
				recoveryroomreadings.setUpdatedBy(userName);
				recoveryroomreadings.setUpdatedTime(updatedTime);
			}
			else
			{
				Date createdTime= new Date();
				recoveryroomreadings.setCreatedBy(userName);
				recoveryroomreadings.setCreatedTime(createdTime);
			}

			// saving/ updating the Recoveryroomreadings details
			Patientcase patientcase = recoveryroomreadings.getPatientcase();
			String status = patientcase.getStatus();
			Query query= session.createQuery("SELECT COUNT(pmd.caseId) from PatientMonitorData pmd where pmd.caseId=? AND (pmd.iBPDia !='---' AND pmd.iBPSys!='---')");
			query.setParameter(0, patientcase.getCaseId());
			Long countTotalValidValues = (Long)query.uniqueResult();
			
			Query query2= session.createQuery("SELECT (1/3* pmd.iBPSys + 2/3* pmd.iBPDia) from PatientMonitorData pmd where pmd.caseId=? AND (pmd.iBPDia !='---' AND pmd.iBPSys!='---') AND ((2/3* pmd.iBPDia + 1/3 * pmd.iBPSys) < " + lowerRange + " OR (2/3* pmd.iBPDia + 1/3 * pmd.iBPSys) > "+ upperRange +")");
			List<Integer> meanValues=new ArrayList<Integer>();
//			Query query2= session.createQuery("SELECT 1/3* pmd.iBPSys + 2/3* pmd.iBPDia from PatientMonitorData pmd where pmd.caseId=? AND (pmd.iBPDia !='---' AND pmd.iBPSys!='---') AND ((2/3*(pmd.iBPDia) + 1/3 * (pmd.iBPSys) <"+ lowerRange + ") OR pmd.iBPSys > " + upperRange + ")");
			query2.setParameter(0, patientcase.getCaseId());
			meanValues = query2.list();			
			int AUC = 0;
			
			for(int i = 0; i<meanValues.size();i++)
			{
				int value = meanValues.get(i).intValue();
				if(value > upperRange)
				{
					AUC = AUC + (value - upperRange);
				}
				else if (value < lowerRange)
				{
					AUC = AUC + (lowerRange - value);
				}
			}
			Double aucTotal = null;
			if(countTotalValidValues!=0){
			aucTotal = (AUC*1.0) / (countTotalValidValues*1.0/60);			
			}
			
			 

			/*if(countTotalValidValues!=null && countOORValues!=null && countTotalValidValues!=0)
			{
				double areaUnderCurve = (countTotalValidValues.doubleValue() - countOORValues.doubleValue()/countTotalValidValues.doubleValue()) * 100;
				recoveryroomreadings.setAreaUnderCurve(new BigDecimal(areaUnderCurve));
			}*/
			if(aucTotal != null){
				recoveryroomreadings.setAreaUnderCurve(new BigDecimal(new Double(aucTotal)));
			}	
			session.saveOrUpdate(recoveryroomreadings);
			patientcase.setStatus("Closed");
			session.saveOrUpdate(patientcase);
			recoveryroomreadingsId.add(0, recoveryroomreadings.getRecoveryRoomReadingRecordId());
			recoveryroomreadingsId.add(1, countTotalValidValues.intValue());
//			recoveryroomreadingsId.add(2, countOORValues.intValue());



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
				e2.printStackTrace();
				LOGGER.error("## Exception occured:" + e2.getMessage());
				throw new ServiceExceptionWrapper(e2);
			}
		}

		return recoveryroomreadingsId;
	}



	/**
	 * This method returns String containing success/error message for deleting the Recoveryroomreadings record
	 * from Recoveryroomreadings entity
	 *
	 *
	 * @param recoveryroomreadingsrecordID
	 * - ID of Recoveryroomreadings record to be deleted
	 *
	 *
	 *
	 * @param userName.
	 *
	 * @return String containing success/error message for deleting the Recoveryroomreadings record
	 *
	 */
	@Override
	public String deleteRecord(int recoveryroomreadingsrecordID, String userName) throws Exception
	{
		LOGGER.debug("Logger Name: " + LOGGER.getName());
		Session session = null;
		String result = "";
		Recoveryroomreadings recoveryroomreadings= new Recoveryroomreadings();
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

			/// retrieves the Recoveryroomreadings record to be deleted
			recoveryroomreadings=(Recoveryroomreadings)session.get(Recoveryroomreadings.class,recoveryroomreadingsrecordID);
			session.delete(recoveryroomreadings);
			result = "Recoveryroomreadings Entry Deleted";

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
		return result;
	}

	/**
	 * This method returns Recoveryroomreadings record corresponding to the caseID provided by the user
	 * from Recoveryroomreadings entity
	 *
	 *
	 * @param caseID
	 * - ID of case corresponding to which Recoveryroomreadings record to be retrieved
	 *
	 *
	 *
	 * @param userName.
	 *
	 * @return Recoveryroomreadings record corresponding to the caseID provided by the user
	 *
	 */

	@SuppressWarnings("unchecked")
	@Override
	public Recoveryroomreadings getRecoveryRoomReadingRecord(long caseID, String userName) throws Exception
	{
		LOGGER.debug("Logger Name: " + LOGGER.getName());
		Session session = null;
		List<Recoveryroomreadings> listOfrecRecoveryroomreadings=new ArrayList<Recoveryroomreadings>();
		Recoveryroomreadings recoveryroomreadings= null;
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

			// retrieves the Recoveryroomreadings record
			Query query= session.createQuery("from Recoveryroomreadings where patientcase.caseId=?");
			query.setParameter(0, caseID);
			listOfrecRecoveryroomreadings=query.list();
			if(listOfrecRecoveryroomreadings.isEmpty())
			{
				return recoveryroomreadings;
			}
			else
			{
				recoveryroomreadings=listOfrecRecoveryroomreadings.get(0);
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
		return recoveryroomreadings;
	}

}
