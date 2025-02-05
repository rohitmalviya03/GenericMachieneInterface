/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package com.cdac.common.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.cdac.common.GeneratedEntities.Patient;
import com.cdac.common.GeneratedEntities.Patientcase;
import com.cdac.common.GeneratedEntities.SearchPatientEntity;
import com.cdac.common.util.HibernateUtilities;
import com.cdac.framework.exceptions.ServiceExceptionWrapper;

// TODO: Auto-generated Javadoc
/**
 * The Class SearchPatientDAOImpl.
 */
public class SearchPatientDAOImpl implements SearchPatientDao {

	// ~ Static attributes/initializers
	/** The Constant LOGGER. */
	// -------------------------------------------------------------
	private static final Logger LOGGER = Logger.getLogger(SearchPatientDAOImpl.class.getName());

	// ~ Instance attributes
	// ------------------------------------------------------------------------

	// ~ Constructors
	// -------------------------------------------------------------------------------

	// ~ Methods
	// ------------------------------------------------------------------------------------

	private static SearchPatientDAOImpl instance = null;
	private SearchPatientDAOImpl()
	{

	}

	public static SearchPatientDAOImpl getInstance()
	{
		if(instance == null)
		{
			instance = new SearchPatientDAOImpl();
		}
		return instance;
	}
	/**
	 * This method returns Map having patientList/error messgae from
	 * PatientEntity w.r.t different values passed in SearchPatientEntity
	 *
	 * @param searchPatient
	 *            - the entity which will be populated with search criteria
	 * @param userName
	 *            - the logged in user
	 * @param pageNum
	 *            - page number for the results
	 * @param pageSize
	 *            - number of results to be displayed on the page
	 * @return the map - containing total results and list of patients for
	 *         matches found / contains error message for no match found
	 * @throws Exception
	 */

	@Override
	public Map<String, Object> searchPatient(SearchPatientEntity searchPatient, String userName, int pageNum,
	        int pageSize) throws Exception
	{

		LOGGER.debug("Logger Name: " + LOGGER.getName());
		// Main.LOGGER.info("In search patient DB service ");
		Map<String,Object> patientListDetails = new HashMap<String, Object>();
		Session session=null;
		try {
		SessionFactory sessionFactory = HibernateUtilities.createSessionFactory();
		session = sessionFactory.openSession();

			// opens a new session from the session factory
			session.getTransaction().begin();

			// Main methods

			if (searchPatient.getCaseID() == null)
			{
				Long cRNumber = searchPatient.getcRNumber();

				patientListDetails = getPatientList(searchPatient, cRNumber, pageNum, pageSize);
			}
			else
			{
				Patientcase caseEntity = (Patientcase) session.get(Patientcase.class,
						Long.parseLong(searchPatient.getCaseID().toString()));

				if (caseEntity == null) {
					Map<String,Object> patientList=new HashMap<String,Object>();
					patientList.put("Error", "Case ID does not exist");
					return patientList;
				}

				Patient patientEntity = (Patient) session.get(Patient.class,
						caseEntity.getPatient().getPatientId());

				if (searchPatient.getcRNumber() == null)
				{
					patientListDetails = getPatientList(searchPatient, patientEntity.getCrnumber(), pageNum, pageSize);
				}
				else
				{

					if (patientEntity.getCrnumber()==searchPatient.getcRNumber()) {
						patientListDetails = getPatientList(searchPatient, patientEntity.getCrnumber(), pageNum, pageSize);
					} else {
						Map<String,Object> patientList=new HashMap<String,Object>();
						patientList.put("Error", "Case ID does not exist");
						return patientList;
					}

				}
			}
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
		return patientListDetails;
	}
	/**
	 * This method returns JSONObject having patientList from PatientEntity
	 * w.r.t different values passed in SearchPatientEntity
	 *
	 * @param searchPatient
	 *            the search patient containing values to be searched
	 * @param cRNumber
	 *            the CR number to be searched
	 * @param pageNum
	 *            page number for the results
	 * @param pageSize
	 *            number of results to be displayed on the page
	 * @return Map - having list of patients in case of success response/error
	 *         detail in case of no match found.
	 * @throws Exception
	 */
	@Override
	public Map<String, Object> searchCases(String OTName, Date startDate, Date endDate, int pageNum,
	        int pageSize) throws Exception
	{
		LOGGER.debug("Logger Name: " + LOGGER.getName());
		Map<String,Object> totalList=new HashMap<String, Object>();
		Session session=null;

		try {
		SessionFactory sessionFactory = HibernateUtilities.createSessionFactory();
		session = sessionFactory.openSession();

			// opens a new session from the session factory
			session.getTransaction().begin();
			Query query = null;
			String stringQuery = "";
			Query countQuery =null;

			if(OTName!=null && !OTName.equalsIgnoreCase("") && startDate!=null)
			{
				stringQuery = "from Patientcase pc where pc.oT=? AND pc.plannedProcedureDateTime between ? AND ?";
				countQuery = session.createQuery("SELECT COUNT(*) "+stringQuery);
				query = session.createQuery(stringQuery);
				query.setParameter(0,OTName);
				query.setParameter(1,startDate);
				query.setParameter(2,endDate);

				countQuery.setParameter(0,OTName);
				countQuery.setParameter(1,startDate);
				countQuery.setParameter(2,endDate);
			}
			else if((OTName==null ||OTName.equalsIgnoreCase("")) && startDate!=null)
			{
				stringQuery = "from Patientcase pc where pc.plannedProcedureDateTime between ? AND ?";
				countQuery = session.createQuery("SELECT COUNT(*) "+stringQuery);
				query = session.createQuery(stringQuery);
				query.setParameter(0,startDate);
				query.setParameter(1,endDate);

				countQuery.setParameter(0,startDate);
				countQuery.setParameter(1,endDate);
			}
			else if(OTName!=null && OTName.equals(""))
			{
				stringQuery = "from Patientcase pc where pc.oT=?";
				countQuery = session.createQuery("SELECT COUNT(*) "+stringQuery);
				query = session.createQuery(stringQuery);
				query.setParameter(0,OTName);

				countQuery.setParameter(0,OTName);
			}



//			if (cRNumber != null) {
//				if (andCheck) {
//
//					andCheck = false;
//				} else {
//					query = query + " and";
//				}
//				query = query + " pe.crnumber = " + cRNumber.toString();
//			}
//			if (searchPatient.getFirstName() != null) {
//				if (andCheck) {
//
//					andCheck = false;
//				} else {
//					query = query + " and";
//				}
//				query = query + " pe.firstName like '%" + searchPatient.getFirstName().toString() + "%'";
//			}
//			if (searchPatient.getLastName() != null) {
//				if (andCheck) {
//
//					andCheck = false;
//				} else {
//					query = query + " and";
//				}
//				query = query + " pe.lastName like '%" + searchPatient.getLastName().toString() + "%'";
//			}
//			if (searchPatient.getMobilePhone() != null) {
//				if (andCheck) {
//
//					andCheck = false;
//				} else {
//					query = query + " and";
//				}
//				query = query + " pe.mobilephone = " + searchPatient.getMobilePhone().toString();
//			}
			Long count = (Long) countQuery.uniqueResult();
//
//			Query queryExecute = session.createQuery(query);
//
			query.setFirstResult((pageNum - 1) * pageSize);
			query.setMaxResults(pageSize);
//
			List<Patient> result = query.list();
			if (result.isEmpty()) {
				Map<String,Object> patientDetails=new HashMap<String, Object>();
				patientDetails.put("Error", "No patient found");
				return patientDetails;
			}
//
			totalList.put("size", count);
			totalList.put("listOfCase", result);
//			session.getTransaction().commit(); // Session commit

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
		return totalList;
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> getPatientList(SearchPatientEntity searchPatient, Long cRNumber, int pageNum,
	        int pageSize) throws Exception
	{
		LOGGER.debug("Logger Name: " + LOGGER.getName());
		Map<String,Object> totalList=new HashMap<String, Object>();
		Session session=null;

		try {
		SessionFactory sessionFactory = HibernateUtilities.createSessionFactory();
		session = sessionFactory.openSession();

			// opens a new session from the session factory
			session.getTransaction().begin();
			boolean andCheck = true;

			String query = "from Patient pe where pe.isDeleted=0 and";

			if (cRNumber != null) {
				if (andCheck) {

					andCheck = false;
				} else {
					query = query + " and";
				}
				query = query + " pe.crnumber = " + cRNumber.toString();
			}
			if (searchPatient.getFirstName() != null) {
				if (andCheck) {

					andCheck = false;
				} else {
					query = query + " and";
				}
				query = query + " pe.firstName like '%" + searchPatient.getFirstName().toString() + "%'";
			}
			if (searchPatient.getLastName() != null) {
				if (andCheck) {

					andCheck = false;
				} else {
					query = query + " and";
				}
				query = query + " pe.lastName like '%" + searchPatient.getLastName().toString() + "%'";
			}
			if (searchPatient.getMobilePhone() != null) {
				if (andCheck) {

					andCheck = false;
				} else {
					query = query + " and";
				}
				query = query + " pe.mobilephone = " + searchPatient.getMobilePhone().toString();
			}
			String countQuery = "SELECT COUNT(*) "+query;
			Query countQueryExecute = session.createQuery(countQuery);
			Long count = (Long) countQueryExecute.uniqueResult();

			Query queryExecute = session.createQuery(query);

			queryExecute.setFirstResult((pageNum - 1) * pageSize);
			queryExecute.setMaxResults(pageSize);

			List<Patient> result = queryExecute.list();
			if (result.isEmpty()) {
				Map<String,Object> patientDetails=new HashMap<String, Object>();
				patientDetails.put("Error", "No patient found");
				return patientDetails;
			}

			totalList.put("size", count);
			totalList.put("listOfPatients", result);
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
		return totalList;
	}
}
