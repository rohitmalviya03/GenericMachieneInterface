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

import com.cdac.common.GeneratedEntities.IntraopAnesthesiamedicationrecord;
import com.cdac.common.GeneratedEntities.IntraopAnesthesiarecord;
import com.cdac.common.audit.interceptor.AuditLogInterceptor;
import com.cdac.common.pojoClasses.AnesthesiaFetchListWithMedication;
import com.cdac.common.util.HibernateUtilities;
import com.cdac.framework.exceptions.ServiceExceptionWrapper;

public class AnesthesiaDetailsDaoImpl implements AnesthesiaDetailsDao
{

	
	/** The Constant LOGGER. */
	private static final Logger LOGGER = Logger.getLogger(AnesthesiaDetailsDaoImpl.class.getName());


	// ~ Methods
	// ------------------------------------------------------------------------------------
	
	private static AnesthesiaDetailsDaoImpl instance = null;
	private AnesthesiaDetailsDaoImpl()
	{

		
	}

	public static AnesthesiaDetailsDaoImpl getInstance()
	{
		if(instance == null)
		{
			instance = new AnesthesiaDetailsDaoImpl();
		}
		return instance;
	}
	
	/**
	 * Inserts a new record for anesthesia
	 * 
	 * @param anesthesiaRecord
	 * @param userName
	 * @return Anesthesia record entity
	 * @throws Exception
	 */
	//@SuppressWarnings("unchecked")
	@Override
	public IntraopAnesthesiarecord createAnesthesiaRecord(IntraopAnesthesiarecord anesthesiaRecord,List<IntraopAnesthesiamedicationrecord> listOfMediacation, String userName)
			throws Exception
	{
		LOGGER.debug("Logger Name: " + LOGGER.getName());
		Session session = null;
		Session session2=null;
		//List<IntraopAnesthesiamedicationrecord> listOfMediactionCorresponding = new ArrayList<IntraopAnesthesiamedicationrecord>();
		ArrayList<Integer> duplicateValues=new ArrayList<Integer>();
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
			if(anesthesiaRecord.getAnesthesiaRecordId()!=null)
			{
				Date updatedTime= new Date();
				anesthesiaRecord.setUpdatedBy(userName);
				anesthesiaRecord.setUpdatedTime(updatedTime);
			}
			else
			{
				Date createdTime= new Date();
				anesthesiaRecord.setCreatedBy(userName);
				anesthesiaRecord.setCreatedTime(createdTime);
			}


			// saves/updates the anesthesia record
			session.saveOrUpdate(anesthesiaRecord);

			session.getTransaction().commit();
			session.close();

			// opens a new session from the session factory
			session2 = sessionFactory.openSession();
			session2.getTransaction().begin();
			//Query query=session2.createQuery("from IntraopAnesthesiamedicationrecord where AnesthesiaRecordID=?");
			Query query=session2.createQuery("delete from IntraopAnesthesiamedicationrecord where AnesthesiaRecordID=?");
			query.setParameter(0,anesthesiaRecord.getAnesthesiaRecordId());
			query.executeUpdate();
			//listOfMediactionCorresponding=query.list();
			/*if(listOfMediactionCorresponding!=null)
			{
				for(IntraopAnesthesiamedicationrecord fromDB : listOfMediactionCorresponding)
				{
					session2.delete(fromDB);
				}
			}*/

			for (int i = 0; i < listOfMediacation.size(); i++)
			{
				for (int j = 0; j < listOfMediacation.size(); j++)
				{
					if(i!=j)
					{
						if(!duplicateValues.contains(i))
						{
							if(listOfMediacation.get(i).getAnesthesiaSubType().toLowerCase().equals(listOfMediacation.get(j).getAnesthesiaSubType().toLowerCase()))
							{
								if(listOfMediacation.get(i).getMedicine().toLowerCase().equals(listOfMediacation.get(j).getMedicine().toLowerCase()))
								{
									{
										duplicateValues.add(j);

									}
								}
							}
						}
					}
				}
			}
			if(duplicateValues!=null) 
			{
				listOfMediacation.removeAll(duplicateValues);
				/*for(Integer place: duplicateValues)
				{
					listOfMediacation.remove(listOfMediacation.get(place));

				}*/
			}
			for(IntraopAnesthesiamedicationrecord anesthesiamedicationrecord : listOfMediacation)
			{
				Date createdTime= new Date();
				anesthesiamedicationrecord.setAnesthesiaRecordId(anesthesiaRecord);
				anesthesiamedicationrecord.setCreatedBy(userName);
				anesthesiamedicationrecord.setCreatedTime(createdTime);

				session2.merge(anesthesiamedicationrecord);

			}
			session2.getTransaction().commit();	
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

		return anesthesiaRecord;
	}


	/**
	 * Get anesthesia record details
	 * 
	 * @param patientId
	 * @param patientCaseId
	 * @param userName
	 * @return List of anesthesia record
	 * @throws Exception
	 */

	@SuppressWarnings("unchecked")
	@Override
	public AnesthesiaFetchListWithMedication getAnesthesiaRecord(int patientId, Long patientCaseId, String userName)
			throws Exception
	{
		LOGGER.debug("Logger Name: " + LOGGER.getName());
		Session session = null;
		AnesthesiaFetchListWithMedication anesthesiaFetchListWithMedication=new AnesthesiaFetchListWithMedication();
		IntraopAnesthesiarecord anesthesiarecord=new IntraopAnesthesiarecord();
		List<IntraopAnesthesiarecord> anesthesiaRecordfromDB = new ArrayList<IntraopAnesthesiarecord>();
		List<IntraopAnesthesiamedicationrecord> anesthesiaMedicationRecordfromDB = new ArrayList<IntraopAnesthesiamedicationrecord>();

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
			Query query = session.createQuery("FROM IntraopAnesthesiarecord iar "
					+ " where iar.patient.patientId=? and iar.patientcase.caseId=?");
			query.setParameter(0, patientId);
			query.setParameter(1, patientCaseId);
			anesthesiaRecordfromDB = query.list();
			if(!anesthesiaRecordfromDB.isEmpty())
			{
				/// this loop will only iterate once
				for(IntraopAnesthesiarecord anesthesiaRecord : anesthesiaRecordfromDB)
				{
					anesthesiarecord=anesthesiaRecord;
					Query query1=session.createQuery("from IntraopAnesthesiamedicationrecord where AnesthesiaRecordID=?");
					query1.setParameter(0,anesthesiaRecord.getAnesthesiaRecordId());
					anesthesiaMedicationRecordfromDB=query1.list();

				}
			}
			anesthesiaFetchListWithMedication.setListOfAnesthesiaMedication(anesthesiaMedicationRecordfromDB);
			anesthesiaFetchListWithMedication.setAnesthesiaRecord(anesthesiarecord);
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

		return anesthesiaFetchListWithMedication;
	}






}


