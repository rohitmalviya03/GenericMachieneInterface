/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package com.cdac.common.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.cdac.common.GeneratedEntities.Answerset;
import com.cdac.common.GeneratedEntities.Patientcase;
import com.cdac.common.util.HibernateUtilities;
import com.cdac.framework.exceptions.ServiceExceptionWrapper;

// TODO: Auto-generated Javadoc
/**
 * The Class ViewAllCasesDaoImpl.
 */
public class ViewAllCasesDaoImpl implements ViewAllCasesDao {

	// ~ Static attributes/initializers
	/** The Constant LOGGER. */
	// -------------------------------------------------------------
	private static final Logger LOGGER = Logger.getLogger(ViewAllCasesDaoImpl.class.getName());



	// ~ Methods
	// ------------------------------------------------------------------------------------

	private static ViewAllCasesDaoImpl instance = null;
	private ViewAllCasesDaoImpl()
	{

	}

	public static ViewAllCasesDaoImpl getInstance()
	{
		if(instance == null)
		{
			instance = new ViewAllCasesDaoImpl();
		}
		return instance;
	}

	/**
	 * This method returns Map for details of all cases from Patientcase,
	 * Entityvalues w.r.t patientId passed as parameter and status as 'Open'.
	 *
	 * @param patientId
	 *            the patient id for the he cases list is required
	 * @param userName
	 *            the logged in user
	 * @param pageNum
	 *            the page num for the results
	 * @param pageSize
	 *            the page size - no. of results to be displayed on a page
	 * @param isClosed
	 *            the is closed - if the case is closed or open
	 * @return the map - with details of patient case in of results found, else
	 *         error response
	 * @throws Exception
	 */

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Map<String, Object> viewAllCases(Integer patientId, String userName, Integer pageNum, Integer pageSize,
			Boolean isClosed) throws Exception
	{
		LOGGER.debug("Logger Name: " + LOGGER.getName());
		Session session = null;
		Map<String, Object> totalList = new HashMap<String, Object>();
		try
		{
			SessionFactory sessionFactory = HibernateUtilities.createSessionFactory();
			session = sessionFactory.openSession();

			List<Patientcase> caseList = new ArrayList<Patientcase>();

			List<Patientcase> totalResult = new ArrayList<Patientcase>();
			List<Patientcase> result = new ArrayList<Patientcase>();
			List<Answerset> result3 = new ArrayList<Answerset>();

			// opens a new session from the session factory
			session.getTransaction().begin();

			Query query = session.createQuery("SELECT COUNT(*) FROM Patient cpe where cpe.patientId=?");
			query.setParameter(0,patientId);
			Long count = (Long)query.uniqueResult();

			if (count == 0) {
				Map<String, Object> caseDetails = new HashMap<String, Object>();
				caseDetails.put("Error", "Patient ID does not exist");
				return caseDetails;
			}

			/*if (isClosed)
			{*/
				Query query1 = session.createQuery("FROM Patientcase pc  WHERE pc.patient.patientId=?");
				query1.setParameter(0, patientId);
				totalResult = query1.list();
				query1.setFirstResult((pageNum - 1) * pageSize);
				query1.setMaxResults(pageSize);
				result = query1.list();
			/*}

			else {
				Query query1 = session
						.createQuery("FROM Patientcase pc  WHERE pc.patient.patientId=?");
						//.createQuery("FROM Patientcase pc  WHERE pc.status ='Open' and pc.patient.patientId=?");
				query1.setParameter(0, patientId);
				totalResult = query1.list();
				query1.setFirstResult((pageNum - 1) * pageSize);
				query1.setMaxResults(pageSize);
				result = query1.list();
			}*/

			if (result.isEmpty()) {
				Map<String, Object> caseDetails = new HashMap<String, Object>();
				caseDetails.put("Error", "No cases found");
				return caseDetails;
			}

			/*for (Iterator iterator = result.iterator(); iterator.hasNext();) {

				Patientcase createCaseEntity = (Patientcase) iterator.next();

				List<Entityvalues> result2 = new ArrayList<Entityvalues>();
				String specialities = null;
				String oT = null;
				Query query2 = session.createQuery("select ve from Entityvalues ve,"
						+ "Casevalues cv where ve.entityValueId = cv.entityvalues.entityValueId and cv.patientcase.caseId=?");
				query2.setParameter(0, createCaseEntity.getCaseId());
				result2 = query2.list();
				for (Iterator iterator2 = result2.iterator(); iterator2.hasNext();) 
				{
					Entityvalues valuesEntity = (Entityvalues) iterator2.next();
					if (valuesEntity.getEntityId() == 3)
					{
						specialities = valuesEntity.getEntityValue();
					}
					if(valuesEntity.getEntityId() == 4)
					{
						oT=valuesEntity.getEntityValue();
					}
				}
				Query query3 = session.createQuery("from Answerset ase where ase.patientcase.caseId=?");
				query3.setParameter(0, createCaseEntity.getCaseId());
				result3 = query3.list();

				Integer answerSetID = null;

				if (result3.isEmpty())
				{
					answerSetID = null;
				}
				else
				{
					answerSetID = result3.get(0).getAnswerSetId();
				}

				Patientcase caseDetails = new Patientcase();

				caseDetails.setCaseDateTime(createCaseEntity.getCaseDateTime());
				caseDetails.setSurgeryType(createCaseEntity.getSurgeryType());
				caseDetails.setSpeciality(specialities);
				caseDetails.setoT(oT);
				caseDetails.setCaseId(createCaseEntity.getCaseId());
				caseDetails.setStatus(createCaseEntity.getStatus());

				Set<Answerset> caseDetailsAnswerSetList = new HashSet<Answerset>();
				Answerset caseDetailsAnswerSet = new Answerset();
				caseDetailsAnswerSet.setAnswerSetId(answerSetID);
				caseDetailsAnswerSetList.add(caseDetailsAnswerSet);
				caseDetails.setAnswersets(caseDetailsAnswerSetList);
				caseList.add(caseDetails);

			}*/
			totalList.put("size", totalResult.size());
			totalList.put("listOfCases", result);
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

	@Override
	public List<Object[]> viewAllPatientAndCases() throws Exception {
		LOGGER.debug("Logger Name: " + LOGGER.getName());
		Session session = null;
		List<Object[]> totalResult = new ArrayList<Object[]>();
		try
		{
			SessionFactory sessionFactory = HibernateUtilities.createSessionFactory();
			session = sessionFactory.openSession();
			
			
			// opens a new session from the session factory
			session.getTransaction().begin();

			Query query = session.createQuery("SELECT cpe.caseId,cpe.patient FROM Patientcase cpe order by  cpe.createdTime asc");
			

			
			 totalResult = (List<Object[]>) query.list();
			
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
		return totalResult;
	}
}
