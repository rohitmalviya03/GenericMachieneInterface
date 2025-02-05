/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package com.cdac.common.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.cdac.common.GeneratedEntities.Fluidvalue;
import com.cdac.common.GeneratedEntities.IntraopMedicationlog;
import com.cdac.common.audit.interceptor.AuditLogInterceptor;
import com.cdac.common.pojoClasses.MedicationVolumeUpdation;
import com.cdac.common.pojoClasses.Volume;
import com.cdac.common.util.HibernateUtilities;
import com.cdac.framework.exceptions.ServiceExceptionWrapper;

import controllers.StopInfusionController;

// TODO: Auto-generated Javadoc
/**
 * The Class EventLogDaoImpl.
 *
 * @author gursimratpreetkaur_d
 */
public class MedicationLogDaoImpl implements MedicationLogDao
{

	// ~ Static attributes/initializers
	/** The Constant LOGGER. */
	// -------------------------------------------------------------
	private static final Logger LOGGER = Logger.getLogger(MedicationLogDaoImpl.class.getName());

	/*
	 * (non-Javadoc)
	 *
	 * @see com.infosys.common.dao.EventLogDao#getAllSurgeryEvents(int,
	 * java.lang.String)
	 */


	// ~ Methods
	// ------------------------------------------------------------------------------------

	private static MedicationLogDaoImpl instance = null;
	private MedicationLogDaoImpl()
	{

	}

	public static MedicationLogDaoImpl getInstance()
	{
		if(instance == null)
		{
			instance = new MedicationLogDaoImpl();
		}
		return instance;
	}

	/**
	 * This method returns IntraopMedicationlog object after saving it into the database
	 * from IntraopMedicationlog entity
	 *
	 * @param medicationLog
	 * - the medication to be saved
	 *
	 * @param userName.
	 *
	 *
	 * @return IntraopMedicationlog object after saving it into the database
	 *
	 */
	@Override
	public IntraopMedicationlog saveMediactions(IntraopMedicationlog medicationLog, String userName) throws Exception
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
			if(medicationLog.getMedicationLogId()!=null)
			{
				Date updatedTime= new Date();
				medicationLog.setUpdatedBy(userName);
				medicationLog.setUpdatedTime(updatedTime);
			}
			else
			{
				Date createdTime= new Date();
				medicationLog.setCreatedBy(userName);
				medicationLog.setCreatedTime(createdTime);
			}

			// saving/updating  the medication in database
			session.saveOrUpdate(medicationLog);

			// commiting the session
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
			{e2.printStackTrace();
				LOGGER.error("## Exception occured:" + e2.getMessage());
				throw new ServiceExceptionWrapper(e2);
			}
		}

		// TODO Auto-generated method stub
		return medicationLog;
	}


	/**
	 * This method returns string containing success/error for deletion of medication
	 * from IntraopMedicationlog entity
	 *
	 * @param medicationLogID
	 * -ID of the the medication to be deleted
	 *
	 * @param userName.
	 *
	 *
	 * @return string containing success/error for deletion of medication
	 *
	 */
	@Override
	public String deleteMedicationLog(Long medicationLogID, String userName) throws Exception
	{
		LOGGER.debug("Logger Name: " + LOGGER.getName());
		String result = "";
		Session session = null;
		IntraopMedicationlog fromDB = new IntraopMedicationlog();
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

			// retrieving the medication to be deleted
			fromDB = (IntraopMedicationlog) session.get(IntraopMedicationlog.class, medicationLogID);
			if (fromDB == null)
			{
				result = "Medication Log ID does not exist";
			}
			else
			{
				session.delete(fromDB);
				result = "Medication Log Deleted";
			}

			// commiting the session
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
	 * This method returns list of medication corresponding to the patientID and caseID
	 * from IntraopMedicationlog entity
	 *
	 * @param patientId
	 *
	 * @param caseId
	 *
	 * @param userName.
	 *
	 *
	 * @return list of medication corresponding to the patientID and caseID
	 *
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<IntraopMedicationlog> getAllMedicationsForPatientCase(Long caseID, String userName)
			throws Exception
	{
		LOGGER.debug("Logger Name: " + LOGGER.getName());
		List<IntraopMedicationlog> mediactionLogList = new ArrayList<IntraopMedicationlog>();
		Session session = null;

		try
		{

			// opens a new session from the session factory
			SessionFactory sessionFactory = HibernateUtilities.createSessionFactory();

			session = sessionFactory.openSession();
			session.getTransaction().begin();

			// Get medication list
			Query query = session.createQuery("FROM IntraopMedicationlog iml "
					+ " where iml.patientcase.caseId=?");
			query.setParameter(0, caseID);
			mediactionLogList = query.list();

			// commiting the session
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

		return mediactionLogList;
	}

	/**
	 * This method returns list of medication corresponding to the patientID and caseID
	 * from IntraopMedicationlog entity
	 *
	 * @param patientId
	 *
	 * @param caseId
	 *
	 * @param userName.
	 *
	 *
	 * @return list of medication corresponding to the patientID and caseID
	 *
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<IntraopMedicationlog>  getMedicationsForPatientCase(Long caseID, String userName)
			throws Exception
	{
		LOGGER.debug("Logger Name: " + LOGGER.getName());
		List<IntraopMedicationlog> mediactionLogList = new ArrayList<IntraopMedicationlog>();
		Session session = null;
		//Map<String,Boolean> medicationMap = new HashMap<>();

		try
		{

			// opens a new session from the session factory
			SessionFactory sessionFactory = HibernateUtilities.createSessionFactory();

			session = sessionFactory.openSession();
			session.getTransaction().begin();

			// Get medication list
			Query query = session.createQuery("FROM IntraopMedicationlog iml "
					+ " where iml.patientcase.caseId=?");// and bolusInfusion=? and endTime=''");
			query.setParameter(0, caseID);
			// query.setParameter(1, "Infusion");
			mediactionLogList = query.list();

			/*for(IntraopMedicationlog log:mediactionLogList)
			{
				if(log.getAttachedToPump()!=null)
				{
					medicationMap.put(log.getMedicationName(), true);
				}
				else
				{
					medicationMap.put(log.getMedicationName(), false);
				}
			}*/

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

		return mediactionLogList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Fluidvalue> getFluidsForPatientCase(Long caseID, String userName)
			throws Exception
	{
		LOGGER.debug("Logger Name: " + LOGGER.getName());
		List<Fluidvalue> fluidList = new ArrayList<Fluidvalue>();
		Session session = null;
		//Map<String,Boolean> fluidMap = new HashMap<>();

		try
		{

			// opens a new session from the session factory
			SessionFactory sessionFactory = HibernateUtilities.createSessionFactory();

			session = sessionFactory.openSession();
			session.getTransaction().begin();

			// Get medication list
			Query query = session.createQuery("from Fluidvalue where caseId=?");// and isCompleted IS NULL");
			query.setParameter(0, caseID);
			fluidList = query.list();

			/*for(Fluidvalue log:fluidList)
			{
				fluidMap.put(log.getFluidName(), false);
			}*/

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

		return fluidList;
	}

	/**
	 * This method returns list of medication corresponding to the patientID and caseID
	 * from IntraopMedicationlog entity
	 *
	 * @param patientId
	 *
	 * @param caseId
	 *
	 * @param userName.
	 *
	 *
	 * @return list of medication corresponding to the patientID and caseID
	 *
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<IntraopMedicationlog> getTimedMedicationsForPatientCase(Long caseID, Date startTime, Date endTime, String userName)
			throws Exception
	{
		LOGGER.debug("Logger Name: " + LOGGER.getName());
		List<IntraopMedicationlog> mediactionLogList = new ArrayList<IntraopMedicationlog>();
		Session session = null;

		try
		{

			// opens a new session from the session factory
			SessionFactory sessionFactory = HibernateUtilities.createSessionFactory();

			session = sessionFactory.openSession();
			session.getTransaction().begin();

			// Get medication list
			Query query = session.createQuery("FROM IntraopMedicationlog iml "
			        + " where iml.patientcase.caseId=? and (iml.startTime between ? and ? or iml.endTime between ? and ?)");
			query.setParameter(0, caseID);
			query.setParameter(1, startTime);
			query.setParameter(2, endTime);
			query.setParameter(3, startTime);
			query.setParameter(4, endTime);

			mediactionLogList = query.list();

			// commiting the session
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

		return mediactionLogList;
	}


	/**
	 * This method returns medication which is to be updated
	 * from IntraopMedicationlog entity
	 *
	 * @param medicationLogId
	 * - ID of the medication to be updated
	 *
	 * @param userName.
	 *
	 *
	 * @return medication which is to be updated
	 *
	 */
	@Override
	public IntraopMedicationlog getMedicationLogForUpdate(Long medicationLogId, String userName) throws Exception
	{
		LOGGER.debug("Logger Name: " + LOGGER.getName());
		//String result = "";
		Session session = null;
		IntraopMedicationlog medicationLogfromDB = new IntraopMedicationlog();
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

			// retrieving the medication to be updated
			medicationLogfromDB = (IntraopMedicationlog) session.get(IntraopMedicationlog.class, medicationLogId);

			// commiting the session
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

		return medicationLogfromDB;
	}

	/**
	 * This method returns Volume object containing the total dosage of the medication given to the patient
	 * from IntraopMedicationlog entity
	 *
	 * @param medicationName
	 * - Name of the medication whose total dosage is to calculated
	 *
	 * @param patientId
	 *
	 *
	 * @param caseId
	 *
	 * @param userName.
	 *
	 *
	 * @return Volume object containing the total dosage of the medication given to the patient
	 *
	 */

	@SuppressWarnings("unchecked")
	@Override
	public Volume getTotalDosage(String medicationName, int patientID, Long caseID, String userName) throws Exception
	{

		LOGGER.debug("Logger Name: " + LOGGER.getName());
		Volume totalDosage=new Volume();
		Float dosage =0f;
		Session session = null;
		String dosageUnit =" mg";
		List<IntraopMedicationlog> medicationLogListForcalculatingDosage=new ArrayList<IntraopMedicationlog>();
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

			// Retrtieving the list of the medication
			Query query=session.createQuery("from IntraopMedicationlog where patientId=? and caseId=? and medicationName=?");
			query.setParameter(0, patientID);
			query.setParameter(1, caseID);
			query.setParameter(2,medicationName);
			medicationLogListForcalculatingDosage=query.list();

			if(!medicationLogListForcalculatingDosage.isEmpty())
			{
				for(IntraopMedicationlog object: medicationLogListForcalculatingDosage)
				{

//					if(object.getDosageUnit().equals("g"))
//					{
//						if(object.getInfuseDosage()!=null)
//						{
//							dosage=dosage+(object.getInfuseDosage()*1000);
//						}
//					}
//					else if(object.getDosageUnit().equals("mg"))
//					{
//						if(object.getInfuseDosage()!=null)
//						{
//							dosage=dosage+object.getInfuseDosage();
//						}
//					}
//
//					else
//					{
						if(object.getInfuseDosage()!=null)
						{
							dosage=dosage+object.getInfuseDosage();
						}
				//	}


				}
			}
			totalDosage.setUnit(dosageUnit);
			totalDosage.setValue(dosage);

			// commiting the session
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
		return totalDosage;
	}

	/**Arvind
	 * This method returns Volume object containing the total dosage of the medication given to the patient
	 * from IntraopMedicationlog entity
	 *
	 * @param medicationName
	 * - Name of the medication whose total dosage is to calculated
	 *
	 * @param patientId
	 *
	 *
	 * @param caseId
	 *
	 * @param userName.
	 *
	 *
	 * @return Volume object containing the total dosage of the medication given to the patient
	 *
	 */

	@SuppressWarnings("unchecked")
	@Override
	public HashMap<String,String> getTotalDosageListOfMedication(List<String> medicationName, int patientID, Long caseID, String userName) throws Exception
	{

		LOGGER.debug("Logger Name: " + LOGGER.getName());
//		List<Volume> totalDosage=new ArrayList<Volume>();
		HashMap<String, String> totalDosage = new HashMap<>();

		Float dosage =0f;
		String finalDosage="";
		Session session = null;
		String dosageUnit = " mg";// "\u00B5" + "g";
		List<IntraopMedicationlog> medicationLogListForcalculatingDosage=new ArrayList<IntraopMedicationlog>();
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

			// Retrtieving the list of the medication
			Query query=session.createQuery("from IntraopMedicationlog where patientId=? and caseId=? and medicationName in (:ids)");
			query.setParameter(0, patientID);
			query.setParameter(1, caseID);
			query.setParameterList("ids",medicationName);
			medicationLogListForcalculatingDosage=query.list();


				for(int i=0;i<medicationName.size();i++) {
					Volume volume = new Volume();
					dosage =0f;
				for(IntraopMedicationlog object: medicationLogListForcalculatingDosage)
				{

						if(object.getMedicationName().equals(medicationName.get(i))) {
							if(object.getInfuseDosage()!=null)
							{
								dosage=dosage+object.getInfuseDosage();
							}
						}


					}
				finalDosage = dosage+dosageUnit;
				totalDosage.put(medicationName.get(i),finalDosage);
					volume.setValue(dosage);
					volume.setUnit(dosageUnit);

				}



			// commiting the session
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
		return totalDosage;
	}


	/**
	 * This method returns success/error message for updation of medication record
	 * from IntraopMedicationlog entity
	 *
	 * @param listOfmedicationForUpdation
	 * - list of medication for updation object containing value to be updated

	 * @param userName.
	 *
	 *
	 * @return success/error message for updation of medication record
	 *
	 */
	@Override
	public String updateMedication(List<MedicationVolumeUpdation> listOfmedicationForUpdation, String userName)
			throws Exception
	{
		LOGGER.debug("Logger Name: " + LOGGER.getName());
		Session session = null;
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


			// updating the medication log record
			for(MedicationVolumeUpdation object : listOfmedicationForUpdation)
			{
				IntraopMedicationlog medicationlog=(IntraopMedicationlog)session.get(IntraopMedicationlog.class,object.getMedicationLogID());
				if(medicationlog!=null)
				{
					//medicationlog.setVolume(object.getVolume());

					medicationlog.setInfuseDosage(object.getDosage());
					if(object.getEndTime()!=null)
						medicationlog.setEndTime(object.getEndTime());
					if(object.getRateOfInfusion()!=null)
						medicationlog.setRateOfInfusion(object.getRateOfInfusion());

					medicationlog.setUpdatedBy(userName);
					Date date=new Date();
					medicationlog.setUpdatedTime(date);

				}
			}

			// commiting the session
			session.getTransaction().commit();
			result="succesfully updated the values";

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
	 * This method returns Volume object containing the total volume of the medication given to the patient
	 * from IntraopMedicationlog entity
	 *
	 * @param medicationName
	 * - Name of the medication whose total volume is to calculated
	 *
	 * @param patientId
	 *
	 *
	 * @param caseId
	 *
	 * @param userName.
	 *
	 *
	 * @return Volume object containing the total volume of the medication given to the patient
	 *
	 *//*
	@SuppressWarnings("unchecked")
	@Override
	public Volume getTotalVolume(String medicationName, int patientID, Long caseID, String userName) throws Exception
	{
		LOGGER.debug("Logger Name: " + LOGGER.getName());
		Volume totalVolume=new Volume();
		Float volume =0f;
		Session session = null;
		List<IntraopMedicationlog> medicationLogListForcalculatingVolume=new ArrayList<IntraopMedicationlog>();
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

			// Retrtieving the list of the medication
			Query query=session.createQuery("from IntraopMedicationlog where patientId=? and caseId=? and medicationName=?");
			query.setParameter(0, patientID);
			query.setParameter(1, caseID);
			query.setParameter(2,medicationName);
			medicationLogListForcalculatingVolume=query.list();

			if(!medicationLogListForcalculatingVolume.isEmpty())
			{
				for(IntraopMedicationlog object: medicationLogListForcalculatingVolume)
				{
					volume=volume+object.getVolume();
					if(object.getVolumeUnit()!=null || !"".equals(object.getVolumeUnit()))
					{
						totalVolume.setUnit(object.getVolumeUnit());
					}

				}
			}

			totalVolume.setValue(volume);

			// commiting the session
			session.getTransaction().commit();
		}
		catch (Exception e)
		{
			if (session != null && session.getTransaction() != null)
			{
				// If transaction is open,rollback
				session.getTransaction().rollback();
			}
			LOGGER.log(Level.debug, "Exception occur", e);
			throw e;
		}
		finally
		{
			session.close(); // Session close
		}
		return totalVolume;
	}*/
	/**
	 * This method returns list of medication corresponding to the patientID and caseID
	 * from IntraopMedicationlog entity
	 *
	 * @param patientId
	 *
	 * @param caseId
	 *
	 * @param userName.
	 *
	 *
	 * @return list of medication corresponding to the patientID and caseID
	 *
	 */
	@SuppressWarnings("unchecked")
	@Override
	public IntraopMedicationlog getIntraopMedicationlog(String medName,long caseId,int patientId,Date timeStamp, String type, String userName)
			throws Exception
	{
		LOGGER.debug("Logger Name: " + LOGGER.getName());
		IntraopMedicationlog mediactionLog = new IntraopMedicationlog();
		List<IntraopMedicationlog> mediactionLogList = new ArrayList<IntraopMedicationlog>();
		Session session = null;

		try
		{

			// opens a new session from the session factory
			SessionFactory sessionFactory = HibernateUtilities.createSessionFactory();

			session = sessionFactory.openSession();
			session.getTransaction().begin();

			// Get medication list
			Query query = session.createQuery("FROM IntraopMedicationlog iml "
					+ " where iml.patient.patientId=? and iml.patientcase.caseId=? and iml.medicationName=? and iml.startTime=? and iml.bolusInfusion=?");
			query.setParameter(0, patientId);
			query.setParameter(1, caseId);
			query.setParameter(2, medName);
			query.setParameter(3, timeStamp);
			query.setParameter(4, type);
			
			mediactionLog =(IntraopMedicationlog) query.uniqueResult();

			// commiting the session
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

		return mediactionLog;
	}

	
	/**
	 * This method returns list of medication corresponding to the patientID and caseID
	 * from IntraopMedicationlog entity
	 *
	 * @param patientId
	 *
	 * @param caseId
	 *
	 * @param userName.
	 *
	 *
	 * @return list of medication corresponding to the patientID and caseID
	 *
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Boolean getStartFlag(String medName,long caseId,int patientId,Date timeStamp, String type, String userName)
			throws Exception
	{
		LOGGER.debug("Logger Name: " + LOGGER.getName());
		Boolean flag   = false;
		List<IntraopMedicationlog> mediactionLogList = new ArrayList<IntraopMedicationlog>();
		Session session = null;

		try
		{

			// opens a new session from the session factory
			SessionFactory sessionFactory = HibernateUtilities.createSessionFactory();

			session = sessionFactory.openSession();
			session.getTransaction().begin();

			// Get medication list
			Query query = session.createQuery("FROM IntraopMedicationlog iml "
					+ " where  medicationLogId = "
					+ "( select max(medicationLogId)  FROM IntraopMedicationlog iml1  where iml1.patient.patientId=? "
					+ "and iml1.patientcase.caseId=? and iml1.medicationName=?  and iml1.bolusInfusion=?)  "
					+ " ");
			query.setParameter(0, patientId);
			query.setParameter(1, caseId);
			query.setParameter(2, medName);
		
			query.setParameter(3, type);

			IntraopMedicationlog mediactionLog =(IntraopMedicationlog) query.uniqueResult();
			if(mediactionLog !=null) {
			if(mediactionLog.getEndTime() == null) {
				
				return false;
			}
			
			if(timeStamp.getTime() - mediactionLog.getEndTime().getTime() >=0) {
				
				return true;
			}
			}
			else
				return true;
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

		return flag;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void settingEndTimeinLastDose(String medName,long caseId,int patientId,Date timeStamp, String type, String userName)
			throws Exception
	{
		LOGGER.debug("Logger Name: " + LOGGER.getName());
		Boolean flag   = false;
		List<IntraopMedicationlog> mediactionLogList = new ArrayList<IntraopMedicationlog>();
		Session session = null;

		try
		{

			// opens a new session from the session factory
			SessionFactory sessionFactory = HibernateUtilities.createSessionFactory();

			session = sessionFactory.openSession();
			session.getTransaction().begin();

			// Get medication list
			Query query = session.createQuery("FROM IntraopMedicationlog iml "
					+ " where  medicationLogId = "
					+ "( select max(medicationLogId)  FROM IntraopMedicationlog iml1  where iml1.patient.patientId=? "
					+ "and iml1.patientcase.caseId=? and iml1.medicationName=?  and iml1.bolusInfusion=?)  "
					+ " ");
			query.setParameter(0, patientId);
			query.setParameter(1, caseId);
			query.setParameter(2, medName);
		
			query.setParameter(3, type);

			IntraopMedicationlog mediactionLog =(IntraopMedicationlog) query.uniqueResult();
			if(mediactionLog != null) {
			
			Calendar currentime = Calendar.getInstance();
				currentime.set(Calendar.MILLISECOND, 0);
				currentime.set(Calendar.SECOND, 0);
			if(mediactionLog.getEndTime() == null) {
				
				mediactionLog.setEndTime(currentime.getTime());
				mediactionLog.setUpdatedTime(currentime.getTime());
				
				if(!mediactionLog.isFromDevice()) {
					
					float calcDose = StopInfusionController.calculateInfusedDose(mediactionLog.getStartTime(), currentime.getTime(),
							mediactionLog.getRateOfInfusion(), mediactionLog.getConcentration());
					// infusedDosage = String.valueOf(calcDose);
					 Float medicationDosage = mediactionLog.getMedicationDosage();
					  String unit = mediactionLog.getDosageUnit();
					  
					  if(unit.contentEquals("g")){
						  medicationDosage = medicationDosage*1000;
					  }
					  else if (unit.contentEquals("mg")) {
						  
					  }
					  else {
						  medicationDosage =medicationDosage/1000;
					  }
					if(calcDose>medicationDosage ) {
					mediactionLog.setInfuseDosage(medicationDosage);
				}
				else {
					mediactionLog.setInfuseDosage(calcDose);
				}
				}
			}
			}
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

		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getMedicationList(long caseId,int patientId,  String userName)
			throws Exception
	{
		LOGGER.debug("Logger Name: " + LOGGER.getName());
	
		Session session = null;
		List<String> medicationName = new LinkedList<String>();
		try
		{

			// opens a new session from the session factory
			SessionFactory sessionFactory = HibernateUtilities.createSessionFactory();
			
			session = sessionFactory.openSession();
			session.getTransaction().begin();

			// Get medication list
			Query query = session.createSQLQuery("select distinct medicationName from intraop_dev.intraop_medicationlog where patientId =? and caseId =?");
			query.setParameter(0, patientId);
			query.setParameter(1, caseId);

			medicationName =(List<String>) query.list();
			
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
		return medicationName;
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Date getStartTime(String medName,long caseId,int patientId, String type, String userName)
			throws Exception
	{
		LOGGER.debug("Logger Name: " + LOGGER.getName());
		Boolean flag   = false;
		List<IntraopMedicationlog> mediactionLogList = new ArrayList<IntraopMedicationlog>();
		Session session = null;

		try
		{

			// opens a new session from the session factory
			SessionFactory sessionFactory = HibernateUtilities.createSessionFactory();

			session = sessionFactory.openSession();
			session.getTransaction().begin();

			// Get medication list
			Query query = session.createQuery("FROM IntraopMedicationlog iml "
					+ " where  medicationLogId = "
					+ "( select max(medicationLogId)  FROM IntraopMedicationlog iml1  where iml1.patient.patientId=? "
					+ "and iml1.patientcase.caseId=? and iml1.medicationName=?  and iml1.bolusInfusion=?)  "
					+ " ");
			query.setParameter(0, patientId);
			query.setParameter(1, caseId);
			query.setParameter(2, medName);
		
			query.setParameter(3, type);

			IntraopMedicationlog mediactionLog =(IntraopMedicationlog) query.uniqueResult();
			if(mediactionLog !=null) {
			 return mediactionLog.getStartTime() ;
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

		return null;
	}


}
