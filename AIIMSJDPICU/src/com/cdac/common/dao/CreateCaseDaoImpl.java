/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package com.cdac.common.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.cdac.common.GeneratedEntities.Casevalues;
import com.cdac.common.GeneratedEntities.Entityvalues;
import com.cdac.common.GeneratedEntities.Patient;
import com.cdac.common.GeneratedEntities.Patientcase;
import com.cdac.common.audit.interceptor.AuditLogInterceptor;
import com.cdac.common.util.HibernateUtilities;
import com.cdac.framework.exceptions.ServiceExceptionWrapper;

// TODO: Auto-generated Javadoc
/**
 * The class CreateCaseDaoImpl
 */
public class CreateCaseDaoImpl implements CreateCaseDao {

	// ~ Static attributes/initializers
	/** The Constant LOGGER. */
	// -------------------------------------------------------------
	private static final Logger LOGGER = Logger.getLogger(CreateCaseDaoImpl.class.getName());

	private static CreateCaseDaoImpl instance = null;
	private CreateCaseDaoImpl()
	{

	}

	public static CreateCaseDaoImpl getInstance()
	{
		if(instance == null)
		{
			instance = new CreateCaseDaoImpl();
		}
		return instance;
	}

	/**
	 * This method takes Patientcase entity, List of surgeonsID, List of
	 * anesthesiologistID, specialityID and operationTheaterNoID as input
	 * Persisting Patientcase Entity and pac entity for new case persisted And
	 * returns Map for case details having patientId and caseId value generated
	 * in case of successfully created a case / error response if not created.
	 *
	 * @param createCase
	 *            the details of the case to be created
	 * @param patientID
	 *            the patient ID for the case
	 * @param userName
	 *            the logged in user
	 * @param surgeonsID
	 *            the surgeons ID for the case
	 * @param anesthesiologistID
	 *            the anesthesiologist ID for the case
	 * @param specialityID
	 *            the speciality ID for the case
	 * @param operationTheaterNoID
	 *            the operation theater no ID for the case
	 * @return the map - contains case details in case of successfully created a
	 *         case / error response if not created
	 * @throws Exception
	 */

	@Override
	public Patientcase createCase(Patientcase createCase, Integer patientID, String userName
			// changes by sahil.sharma08 start here
			/* ,List<Long> surgeonsID,
	        List<Long> anesthesiologistID, Integer specialityID, Integer operationTheaterNoID
	        // changes by sahil.sharma08 start here
			 */) throws Exception
	{

		LOGGER.debug("Logger Name: " + LOGGER.getName());
		Session session = null;
		Map<String, String> result = new HashMap<String, String>();
		Patient createPatientEntity;
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

			createPatientEntity = (Patient) session.get(Patient.class, patientID);
			createCase.setPatient(createPatientEntity);

			/*if (createPatientEntity == null)
			{
				result.put("Error", "Patient ID does not Exist");
				return result;
			}*/

			// for setting created time and created by in case of new record and updated time and updated by in case of exsisting record
			if(createCase.getCaseId()!=null)
			{
				Date updatedTime= new Date();
				createCase.setUpdatedBy(userName);
				createCase.setUpdatedTime(updatedTime);
			}
			else
			{
				Date createdTime= new Date();
				createCase.setCreatedBy(userName);
				createCase.setCreatedTime(createdTime);
			}



			// changes by sahil.sharma08 start here
			//Long caseID = createCase.getCaseId();
			// changes by sahil.sharma08 end here

			Query query = session.createQuery("delete FROM Casevalues cve WHERE cve.patientcase.caseId = ?");
			// changes by sahil.sharma08 start here
			//query.setParameter(0, caseID);
			query.setParameter(0, createCase.getCaseId());
			// changes by sahil.sharma08 end here
			query.executeUpdate();

			// changes by sahil.sharma08 start here


			/*for (int i = 0; i < surgeonsID.size(); i++) {
				Casevalues createCaseValuesEntity = new Casevalues();
				createCaseValuesEntity.setPatientcase(createCase);
				valuesEntity = (Entityvalues) session.get(Entityvalues.class, surgeonsID.get(i).intValue());
				createCaseValuesEntity.setEntityvalues(valuesEntity);
				session.saveOrUpdate(createCaseValuesEntity);
			}

			for (int i = 0; i < anesthesiologistID.size(); i++) {
				Casevalues createCaseValuesEntity = new Casevalues();
				createCaseValuesEntity.setPatientcase(createCase);
				valuesEntity = (Entityvalues) session.get(Entityvalues.class, anesthesiologistID.get(i).intValue());
				createCaseValuesEntity.setEntityvalues(valuesEntity);
				session.saveOrUpdate(createCaseValuesEntity);
			}*/


			/*

				Casevalues createCaseValuesEntity = new Casevalues();
				createCaseValuesEntity.setPatientcase(createCase);
				valuesEntity = (Entityvalues) session.get(Entityvalues.class, specialityID.intValue());
				createCaseValuesEntity.setEntityvalues(valuesEntity);
				session.saveOrUpdate(createCaseValuesEntity);
				createCase.setSpeciality(valuesEntity.getEntityValue());

			 */

			if(createCase.getSpeciality()!=null && !createCase.getSpeciality().equals(""))
			{
				Casevalues createCaseValuesEntity = new Casevalues();
				createCaseValuesEntity.setPatientcase(createCase);
				Entityvalues valuesEntity =(Entityvalues) session.get(Entityvalues.class, new Integer(createCase.getSpeciality()));
				createCaseValuesEntity.setCreatedBy(userName);
				Date createdTime = new Date();
				createCaseValuesEntity.setCreatedTime(createdTime);
				createCaseValuesEntity.setEntityvalues(valuesEntity);
				session.saveOrUpdate(createCaseValuesEntity);

				createCase.setSpeciality(valuesEntity.getEntityValue());

			}



			// changes by sahil.sharma08 end here


			session.saveOrUpdate(createCase); // Persisting createCase Entity in database

			// changes by sahil.sharma08 start here

			/*Casevalues createCaseValuesEntityOT = new Casevalues();
			createCaseValuesEntityOT.setPatientcase(createCase);
			valuesEntity = (Entityvalues) session.get(Entityvalues.class, operationTheaterNoID.intValue());
			createCaseValuesEntityOT.setEntityvalues(valuesEntity);
			session.saveOrUpdate(createCaseValuesEntityOT);*/

			// changes by sahil.sharma08 end here


			/*result.put("patientId", createCase.getPatient().getPatientId().toString());
			result.put("caseId", createCase.getCaseId().toString());*/


			// changes by sahil.sharma08 start here
			/// this part is not required
			/*if (createCase.getCaseId() != null)
			{
				Query countQuery = session.createQuery(
						"SELECT COUNT(*) FROM Pac pe where pe.patientcase.caseId=? and pe.patient.patientId=?");
				countQuery.setParameter(0, caseID);
				countQuery.setParameter(1, createPatientEntity.getPatientId());
				Long count = (Long) countQuery.uniqueResult();

				if (count <= 0)
				{

					Pac pacEntity = null;
					Date createdDate = new Date();
					pacEntity = new Pac();

					pacEntity.setPatientcase(createCase);
					pacEntity.setPatient(createPatientEntity);
					pacEntity.setCreatedBy(userName);
					pacEntity.setCreatedDateTime(createdDate);
					session.save(pacEntity);
				}
			}*/

			// changes by sahil.sharma08 end here


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
		return createCase;
	}

	/**
	 * This method returns CreateCaseEntity w.r.t caseId
	 *
	 * @param caseId
	 *            the case id of the required case
	 * @return Patientcase - the patient case entity for given case id.
	 * @throws Exception
	 */
	@Override
	public Patientcase getCreateCaseEntity(Long caseId) throws Exception
	{
		LOGGER.debug("Logger Name: " + LOGGER.getName());

		Session session=null;
		Patientcase createCaseEntity = null;
		try {
			SessionFactory sessionFactory = HibernateUtilities.createSessionFactory();

			session = sessionFactory.openSession();

			// opens a new session from the session factory
			session.getTransaction().begin();

			createCaseEntity = (Patientcase) session.get(Patientcase.class, caseId);

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
		return createCaseEntity;
	}

}
