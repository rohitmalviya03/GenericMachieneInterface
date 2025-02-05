/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package com.cdac.common.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.cdac.common.GeneratedEntities.AdminEntity;
import com.cdac.common.GeneratedEntities.Diagnosis;
import com.cdac.common.GeneratedEntities.Diagnosiscategory;
import com.cdac.common.GeneratedEntities.Diagnosissubcategory;
import com.cdac.common.GeneratedEntities.Entityvalues;
import com.cdac.common.GeneratedEntities.Fdamedications;
import com.cdac.common.GeneratedEntities.Investigationparameterfields;
import com.cdac.common.GeneratedEntities.Plannedprocedure;
import com.cdac.common.GeneratedEntities.Procedurecategory;
import com.cdac.common.GeneratedEntities.Proceduresubcategory;
import com.cdac.common.GeneratedEntities.Proceduresubsubcategory;
import com.cdac.common.GeneratedEntities.SearchDiagnosis;
import com.cdac.common.util.HibernateUtilities;
import com.cdac.framework.exceptions.ServiceExceptionWrapper;

// TODO: Auto-generated Javadoc
/**
 * The Class GetMasterDataDaoImpl.
 */
public class GetMasterDataDaoImpl implements GetMasterDataDao {

	// ~ Static attributes/initializers
	/** The Constant LOGGER. */
	// -------------------------------------------------------------
	private static final Logger LOGGER = Logger.getLogger(GetMasterDataDaoImpl.class.getName());

	private static GetMasterDataDaoImpl instance = null;
	private GetMasterDataDaoImpl()
	{

	}

	public static GetMasterDataDaoImpl getInstance()
	{
		if(instance == null)
		{
			instance = new GetMasterDataDaoImpl();
		}
		return instance;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.infosys.common.dao.GetMasterDataDao#getEntityDetails(java.lang.
	 * String, java.lang.String)
	 */


	// ~ Methods
	// ------------------------------------------------------------------------------------



	/**
	 * Interface method to Get the entity details.
	 *
	 * @param entityName
	 *            the entity name for which the values need to be populated
	 * @param userName
	 *            the logged-in user
	 * @return the entity value details
	 * @throws Exception
	 *             the exception
	 */
	@SuppressWarnings({ "unchecked", "null" })
	@Override
	public Map<String, List<Entityvalues>> getEntityDetails(String entityName, String userName) throws Exception
	{
		LOGGER.debug("Logger Name: " + LOGGER.getName());

		SessionFactory sessionFactory = HibernateUtilities.createSessionFactory();
		Session session = sessionFactory.openSession();

		Map<String, List<Entityvalues>> entityDetailsObject = null;
		//List<Entityvalues> result = new ArrayList<Entityvalues>();
		List<AdminEntity> adminResult = null;//new ArrayList<AdminEntity>();
		try {
			// opens a new session from the session factory
			session.getTransaction().begin();
			if ("all".equals(entityName))
			{
				Query queryForAdminValues = session.createQuery("FROM AdminEntity ae");
				adminResult = queryForAdminValues.list();
				for (Iterator<AdminEntity> iteratorForAdmin = adminResult.iterator(); iteratorForAdmin.hasNext();) {
					AdminEntity adminEntity = (AdminEntity) iteratorForAdmin.next();
					entityDetailsObject = new HashMap<String, List<Entityvalues>>();
					Query query = session.createQuery(
							"FROM Entityvalues ve where ve.entityId = (select ae.entityId from AdminEntity ae where ae.entityName= ?) and ve.isDeleted=?");
					query.setParameter(0, adminEntity.getEntityName());
					query.setParameter(1, false);
					List<Entityvalues> entityDetails = query.list();
					//List<Entityvalues> entityDetails = new ArrayList<Entityvalues>();
					/*for (Iterator<Entityvalues> iterator = result.iterator(); iterator.hasNext();)
					{

						Entityvalues valuesEntity = (Entityvalues) iterator.next();
						Entityvalues values = new Entityvalues();
						values.setEntityValueId(valuesEntity.getEntityValueId());
						values.setEntityValue(valuesEntity.getEntityValue());
						entityDetails.add(values);
					}*/
					entityDetailsObject.put(adminEntity.getEntityName(), entityDetails);
				}

			} else {
				Query query = session.createQuery(
						"FROM Entityvalues ve where ve.entityId = (select ae.entityId from AdminEntity ae where ae.entityName= ?) and ve.isDeleted=?");
				query.setParameter(0, entityName);
				query.setParameter(1, false);
				List<Entityvalues> entityDetails = query.list();

				//List<Entityvalues> entityDetails = new ArrayList<Entityvalues>();
				/*for (Iterator<Entityvalues> iterator = result.iterator(); iterator.hasNext();)
				{
					Entityvalues valuesEntity = (Entityvalues) iterator.next();
					Entityvalues values = new Entityvalues();
					values.setEntityValueId(valuesEntity.getEntityValueId());
					values.setEntityValue(valuesEntity.getEntityValue());
					entityDetails.add(values);
				}*/
				entityDetailsObject.put(entityName, entityDetails);
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
		return entityDetailsObject;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.infosys.common.dao.GetMasterDataDao#getMasterDataForCreateCase(java.
	 * lang.String)
	 */

	/**
	 * Interface method to Get the master data for create case.
	 *
	 * @param userName
	 *            the logged in user
	 * @return the master data for create case
	 * @throws Exception
	 *             the exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, List<Entityvalues>> getMasterDataForCreateCase(String userName) throws Exception
	{
		LOGGER.debug("Logger Name: " + LOGGER.getName());

		SessionFactory sessionFactory = HibernateUtilities.createSessionFactory();
		Session session = sessionFactory.openSession();

		Map<String, List<Entityvalues>> entityDetailsObject = null;
		//List<Entityvalues> result = new ArrayList<Entityvalues>();
		List<AdminEntity> adminResult = null;// = new ArrayList<AdminEntity>();
		try
		{
			// opens a new session from the session factory
			session.getTransaction().begin();
			Query queryForAdminValues = session.createQuery("FROM AdminEntity ae ");
			
			
			adminResult = queryForAdminValues.list();
			entityDetailsObject = new HashMap<String, List<Entityvalues>>();
			for (Iterator<AdminEntity> iteratorForAdmin = adminResult.iterator(); iteratorForAdmin.hasNext();)
			{
				AdminEntity adminEntity = (AdminEntity) iteratorForAdmin.next();
				Query query = session.createQuery(
						"FROM Entityvalues ve where ve.entityId = (select ae.entityId from AdminEntity ae where ae.entityName= ?) and ve.isDeleted=? ORDER BY entityValue");
				query.setParameter(0, adminEntity.getEntityName());
				query.setParameter(1, false);
				List<Entityvalues> entityDetails = query.list();
				//List<Entityvalues> entityDetails = new ArrayList<Entityvalues>();
				/*for (Iterator<Entityvalues> iterator = result.iterator(); iterator.hasNext();)
				{

					Entityvalues valuesEntity = (Entityvalues) iterator.next();
					Entityvalues values = new Entityvalues();
					values.setEntityValueId(valuesEntity.getEntityValueId());
					values.setEntityValue(valuesEntity.getEntityValue());
					entityDetails.add(values);
				}*/
				entityDetailsObject.put(adminEntity.getEntityName(), entityDetails);
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
		return entityDetailsObject;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.infosys.common.dao.GetMasterDataDao#getDiagnosisFavoriteValues(java.
	 * lang.String)
	 */

	/**
	 * Interface method to Get the diagnosis favorite values.
	 *
	 * @param userName
	 *            the logged in user
	 * @return the diagnosis favorite values list
	 * @throws Exception
	 *             the exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Diagnosis> getDiagnosisFavoriteValues(String userName) throws Exception
	{
		LOGGER.debug("Logger Name: " + LOGGER.getName());

		SessionFactory sessionFactory = HibernateUtilities.createSessionFactory();
		Session session = sessionFactory.openSession();

		//List<Diagnosis> diagnosisResult = new ArrayList<Diagnosis>();
		List<Diagnosis> diagnosisValuesList = null;//new ArrayList<Diagnosis>();

		try {
			// opens a new session from the session factory
			session.getTransaction().begin();

			Query query = session.createQuery("FROM Diagnosis de WHERE de.isFavourite=?");
			query.setParameter(0,true);
			diagnosisValuesList = query.list();

			/*for (Iterator<Diagnosis> iterator = diagnosisResult.iterator(); iterator.hasNext();)
			{
				Diagnosis diagnosisEntity = (Diagnosis) iterator.next();
				Diagnosis diagnosisDetails = new Diagnosis();

				diagnosisDetails.setDiagnosisId(diagnosisEntity.getDiagnosisId());
				diagnosisDetails.setDiagnosisName(diagnosisEntity.getDiagnosisName());
				diagnosisValuesList.add(diagnosisDetails);
			}*/

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
		return diagnosisValuesList;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.infosys.common.dao.GetMasterDataDao#getPlannedProcedureFavoriteValues
	 * (java.lang.String)
	 */

	/**
	 * Interface method for Gets the planned procedure favorite values.
	 *
	 * @param userName
	 *            the logged in user
	 * @return the planned procedure favorite values list
	 * @throws Exception
	 *             the exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Plannedprocedure> getPlannedProcedureFavoriteValues(String userName) throws Exception
	{
		LOGGER.debug("Logger Name: " + LOGGER.getName());

		SessionFactory sessionFactory = HibernateUtilities.createSessionFactory();
		Session session = sessionFactory.openSession();

		List<Plannedprocedure> plannedProcedureValuesList = null;// = new ArrayList<Plannedprocedure>();
		//List<Plannedprocedure> result = new ArrayList<Plannedprocedure>();

		try {
			// opens a new session from the session factory
			session.getTransaction().begin();

			Query query = session.createQuery("FROM Plannedprocedure ppe WHERE ppe.isFavourite=?");
			query.setParameter(0,true);
			plannedProcedureValuesList = query.list();

			/*for (Iterator<Plannedprocedure> iterator = result.iterator(); iterator.hasNext();)
			{

				Plannedprocedure plannedProcedureEntity = (Plannedprocedure) iterator.next();
				Plannedprocedure plannedProcedureDetails = new Plannedprocedure();

				plannedProcedureDetails.setProcedureId(plannedProcedureEntity.getProcedureId());
				plannedProcedureDetails.setProcedureName(plannedProcedureEntity.getProcedureName());
				plannedProcedureValuesL1ist.add(plannedProcedureDetails);
			}*/

			session.getTransaction().commit(); // Session commit

		} catch (Exception e)
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
		return plannedProcedureValuesList;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.infosys.common.dao.GetMasterDataDao#getDiagnosisCategory(java.lang.
	 * String, java.lang.String)
	 */

	/**
	 * Interface method to Get the diagnosis category.
	 *
	 * @param version
	 *            the version
	 * @param userName
	 *            the logged in user
	 * @return the diagnosis category list
	 * @throws Exception
	 *             the exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<Diagnosiscategory> getDiagnosisCategory(String version, String userName) throws Exception
	{

		LOGGER.debug("Logger Name: " + LOGGER.getName());

		SessionFactory sessionFactory = HibernateUtilities.createSessionFactory();
		Session session = sessionFactory.openSession();

		List<Diagnosiscategory> diagnosisCategoryList = null;//new ArrayList<Diagnosiscategory>();
		//List<Diagnosiscategory> result = new ArrayList<Diagnosiscategory>();
		try
		{
			// opens a new session from the session factory
			session.getTransaction().begin();

			Query query = session.createQuery("FROM Diagnosiscategory dce where dce.icdversion=?");
			query.setParameter(0, version);
			diagnosisCategoryList = query.list();
			/*for (Iterator iterator = result.iterator(); iterator.hasNext();)
			{
				Diagnosiscategory diagnosisCategoryEntity = (Diagnosiscategory) iterator.next();
				Diagnosiscategory obj = new Diagnosiscategory();
				obj.setDiagnosisCategoryId(diagnosisCategoryEntity.getDiagnosisCategoryId());
				obj.setDiagnosisCategoryValue(diagnosisCategoryEntity.getDiagnosisCategoryValue());
				diagnosisCategoryList.add(obj);
			}*/
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
		return diagnosisCategoryList;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.infosys.common.dao.GetMasterDataDao#getDiagnosisSubCategory(java.lang
	 * .Integer, java.lang.String, java.lang.String)
	 */

	/**
	 * Interface method to Get the diagnosis sub category.
	 *
	 * @param diagnosisCategoryID
	 *            the diagnosis category ID
	 * @param version
	 *            the version
	 * @param userName
	 *            the logged in user
	 * @return the diagnosis sub category list
	 * @throws Exception
	 *             the exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<Diagnosissubcategory> getDiagnosisSubCategory(Integer diagnosisCategoryID, String version,
			String userName)
					throws Exception
	{

		LOGGER.debug("Logger Name: " + LOGGER.getName());

		SessionFactory sessionFactory = HibernateUtilities.createSessionFactory();
		Session session = sessionFactory.openSession();

		//List<Diagnosissubcategory> result = new ArrayList<Diagnosissubcategory>();
		List<Diagnosissubcategory> diagnosisSubcategoryList = null;//new ArrayList<Diagnosissubcategory>();
		try
		{
			// opens a new session from the session factory
			session.getTransaction().begin();

			Query query = session.createQuery(
					"FROM Diagnosissubcategory dsce where dsce.diagnosiscategory.diagnosisCategoryId=? and dsce.icdversion=?");
			query.setParameter(0, diagnosisCategoryID);
			query.setParameter(1, version);
			diagnosisSubcategoryList = query.list();

			/*for (Iterator iterator = result.iterator(); iterator.hasNext();)
			{
				Diagnosissubcategory diagnosisSubCategoryEntity = (Diagnosissubcategory) iterator
						.next();
				Diagnosissubcategory obj = new Diagnosissubcategory();
				obj.setDiagnosisSubCategory(diagnosisSubCategoryEntity.getDiagnosisSubCategory());
				obj.setDiagnosisSubcategoryValue(diagnosisSubCategoryEntity.getDiagnosisSubcategoryValue());
				diagnosisSubcategoryList.add(obj);
			}*/
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
		return diagnosisSubcategoryList;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.infosys.common.dao.GetMasterDataDao#getProcedureCategory(java.lang.
	 * String, java.lang.String)
	 */

	/**
	 * Interface method for Gets the procedure category.
	 *
	 * @param version
	 *            the version
	 * @param userName
	 *            the logged in user
	 * @return the procedure category list
	 * @throws Exception
	 *             the exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<Procedurecategory> getProcedureCategory(String version, String userName) throws Exception
	{

		LOGGER.debug("Logger Name: " + LOGGER.getName());

		SessionFactory sessionFactory = HibernateUtilities.createSessionFactory();
		Session session = sessionFactory.openSession();

		//List<Procedurecategory> result = new ArrayList<Procedurecategory>();
		List<Procedurecategory> procedureCategoryList = null;//new ArrayList<Procedurecategory>();
		try
		{
			// opens a new session from the session factory
			session.getTransaction().begin();

			Query query = session.createQuery("FROM Procedurecategory pce where pce.icdversion=?");
			query.setParameter(0, version);
			procedureCategoryList = query.list();

			/*for (Iterator iterator = result.iterator(); iterator.hasNext();)
			{
				Procedurecategory procedureCategoryEntity = (Procedurecategory) iterator.next();
				Procedurecategory obj = new Procedurecategory();
				obj.setProcedureCategoryId(procedureCategoryEntity.getProcedureCategoryId());
				obj.setProcedureCategoryCode(procedureCategoryEntity.getProcedureCategoryCode());
				obj.setProcedureCategoryValue(procedureCategoryEntity.getProcedureCategoryValue());
				procedureCategoryList.add(obj);
			}*/
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
		return procedureCategoryList;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.infosys.common.dao.GetMasterDataDao#getProcedureSubCategory(java.lang
	 * .String, java.lang.String)
	 */

	/**
	 * Interface method for Gets the procedure sub category.
	 *
	 * @param procedureCategoryCode
	 *            the procedure category code
	 * @param userName
	 *            the logged in user
	 * @return the procedure sub category list
	 * @throws Exception
	 *             the exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<Proceduresubcategory> getProcedureSubCategory(String procedureCategoryCode, String userName)
			throws Exception
	{

		LOGGER.debug("Logger Name: " + LOGGER.getName());

		SessionFactory sessionFactory = HibernateUtilities.createSessionFactory();
		Session session = sessionFactory.openSession();

		//List<Proceduresubcategory> result = new ArrayList<Proceduresubcategory>();
		List<Proceduresubcategory> procedureSubCategoryList =null;// new ArrayList<Proceduresubcategory>();
		try
		{
			// opens a new session from the session factory
			session.getTransaction().begin();
			Query query = session
					.createQuery("FROM Proceduresubcategory psce where psce.procedureSubCategoryCode like '"
							+ procedureCategoryCode.toString() + "%'");
			procedureSubCategoryList = query.list();

			/*for (Iterator iterator = result.iterator(); iterator.hasNext();)
			{
				Proceduresubcategory procedureSubCategoryEntity = (Proceduresubcategory) iterator
						.next();
				Proceduresubcategory obj = new Proceduresubcategory();
				obj.setProcedureSubCategoryId(procedureSubCategoryEntity.getProcedureSubCategoryId());
				obj.setProcedureSubCategoryCode(procedureSubCategoryEntity.getProcedureSubCategoryCode());
				obj.setProcedureSubCategoryValue(procedureSubCategoryEntity.getProcedureSubCategoryValue());
				procedureSubCategoryList.add(obj);
			}*/
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
		return procedureSubCategoryList;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.infosys.common.dao.GetMasterDataDao#getProcedureSubSubCategory(java.
	 * lang.String, java.lang.String)
	 */

	/**
	 * Interface method for Gets the procedure sub sub category.
	 *
	 * @param procedureSubCategoryCode
	 *            the procedure sub category code
	 * @param userName
	 *            the logged in user
	 * @return the procedure sub sub category list
	 * @throws Exception
	 *             the exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<Proceduresubsubcategory> getProcedureSubSubCategory(String procedureSubCategoryCode, String userName)
			throws Exception
	{
		LOGGER.debug("Logger Name: " + LOGGER.getName());

		SessionFactory sessionFactory = HibernateUtilities.createSessionFactory();
		Session session = sessionFactory.openSession();

		//List<Proceduresubsubcategory> result = new ArrayList<Proceduresubsubcategory>();
		List<Proceduresubsubcategory> procedureSubSubCategoryList = null;//new ArrayList<Proceduresubsubcategory>();
		try
		{
			// opens a new session from the session factory
			session.getTransaction().begin();
			Query query = session.createQuery(
					"FROM Proceduresubsubcategory pssce where pssce.procedureSubSubCategoryCode like '"
							+ procedureSubCategoryCode.toString() + "%'");
			procedureSubSubCategoryList = query.list();

			/*for (Iterator iterator = result.iterator(); iterator.hasNext();)
			{
				Proceduresubsubcategory procedureSubSubCategoryEntity = (Proceduresubsubcategory) iterator
						.next();
				Proceduresubsubcategory obj = new Proceduresubsubcategory();
				obj.setProcedureSubSubCategoryId(procedureSubSubCategoryEntity.getProcedureSubSubCategoryId());
				obj.setProcedureSubSubCategoryCode(procedureSubSubCategoryEntity.getProcedureSubSubCategoryCode());
				obj.setProcedureSubSubCategoryValue(
						procedureSubSubCategoryEntity.getProcedureSubSubCategoryValue());
				procedureSubSubCategoryList.add(obj);
			}*/
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
		return procedureSubSubCategoryList;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.infosys.common.dao.GetMasterDataDao#searchDiagnosisDao(com.infosys.
	 * common.GeneratedEntities.SearchDiagnosis, java.lang.String)
	 */

	/**
	 * Interface method for Search diagnosis dao.
	 *
	 * @param searchDiagnosisEntity
	 *            the search diagnosis entity
	 * @param userName
	 *            the logged in user
	 * @return the diagnosis list
	 * @throws Exception
	 *             the exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Diagnosis> searchDiagnosisDao(SearchDiagnosis searchDiagnosisEntity, String userName) throws Exception
	{
		// TODO Auto-generated method stub
		LOGGER.debug("Logger Name: " + LOGGER.getName());

		SessionFactory sessionFactory = HibernateUtilities.createSessionFactory();
		Session session = sessionFactory.openSession();
		List<Diagnosis> diagnosisList = null;//new ArrayList<Diagnosis>();
		try
		{
			// opens a new session from the session factory
			session.getTransaction().begin();
			//boolean andCheck = true;
			//List<Diagnosis> result = new ArrayList<Diagnosis>();
			String query = "from Diagnosis dve where";

			if (searchDiagnosisEntity.getDiagnosisValue() != null)
			{
				query = query + " dve.diagnosisName like '%" + searchDiagnosisEntity.getDiagnosisValue().toString()
						+ "%'";

				if (searchDiagnosisEntity.getDiagnosisCategoryID() != null)
				{
					query = query + " and dve.diagnosiscategory.diagnosisCategoryId ="
							+ searchDiagnosisEntity.getDiagnosisCategoryID().toString();
					if (searchDiagnosisEntity.getDiagnosisSubCategoryID() != null)
					{
						query = query + " and dve.diagnosissubcategory.diagnosisSubCategory = "
								+ searchDiagnosisEntity.getDiagnosisSubCategoryID().toString();
					}
				}
			}

			if (searchDiagnosisEntity.getVersion() != null)
			{
				query = query + " and dve.icdversion = '" + searchDiagnosisEntity.getVersion().toString() + "'";
			}
			System.out.println("-----");
			Query queryExecute = session.createQuery(query);
			diagnosisList = queryExecute.list();

			/*for (Diagnosis dve : result)
			{

				Diagnosis diagnosisObject = new Diagnosis();
				diagnosisObject.setDiagnosisId(dve.getDiagnosisId());
				diagnosisObject.setDiagnosisName(dve.getDiagnosisName());
				diagnosisList.add(diagnosisObject);
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
		return diagnosisList;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.infosys.common.dao.GetMasterDataDao#searchProceduresDao(java.lang.
	 * String, java.lang.String, java.lang.String)
	 */

	/**
	 * Interface method for Search procedures dao.
	 *
	 * @param id
	 *            the id
	 * @param searchCriteria
	 *            the search criteria for procedures
	 * @param userName
	 *            the logged in user
	 * @return the planned procedure list
	 * @throws Exception
	 *             the exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Plannedprocedure> searchProceduresDao(String id, String searchCriteria, String version, String userName)
			throws Exception
	{
		LOGGER.debug("Logger Name: " + LOGGER.getName());

		SessionFactory sessionFactory = HibernateUtilities.createSessionFactory();
		Session session = sessionFactory.openSession();
		List<Plannedprocedure> procedureList = null;//new ArrayList<Plannedprocedure>();
		try
		{
			// opens a new session from the session factory
			session.getTransaction().begin();
			//List<Plannedprocedure> result = new ArrayList<Plannedprocedure>();
			String query = "from Plannedprocedure ppe where ppe.procedureName like '%" + searchCriteria + "%'";

			if ((id != null && !id.isEmpty()))
			{
				query = query + " and ppe.procedureId like '" + id.toString() + "%'";
			}
			Query queryExecute = session.createQuery(query);
			procedureList = queryExecute.list();

			/*for (Plannedprocedure ppe : result)
			{
				Plannedprocedure obj = new Plannedprocedure();
				obj.setProcedureId(ppe.getProcedureId());
				obj.setProcedureName(ppe.getProcedureName());
				procedureList.add(obj);
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
		return procedureList;
	}

	/**
	 * Gets master data for anesthesia
	 *
	 * @param userName
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Map<String, List<Entityvalues>> getMasterDataForAnesthesia(String userName) throws Exception
	{
		LOGGER.debug("Logger Name: " + LOGGER.getName());

		SessionFactory sessionFactory = HibernateUtilities.createSessionFactory();
		Session session = sessionFactory.openSession();

		Map<String, List<Entityvalues>> entityDetailsObject = null;
		//List<Entityvalues> result = new ArrayList<Entityvalues>();
		List<AdminEntity> adminResult = null;//new ArrayList<AdminEntity>();
		try
		{
			// opens a new session from the session factory
			session.getTransaction().begin();
			Query queryForAdminValues = session.createQuery("FROM AdminEntity ae where ae.entityName in (?,?,?,?)");
			queryForAdminValues.setParameter(0, "Lumbar Plexus Nerve Blocks");
			queryForAdminValues.setParameter(1, "Peripheral Nerve Blocks");
			queryForAdminValues.setParameter(2, "Truncal and Cutaneous Nerve Blocks");
			queryForAdminValues.setParameter(3, "Brachial Plexus Blocks");
			adminResult = queryForAdminValues.list();
			entityDetailsObject = new HashMap<String, List<Entityvalues>>();
			for (Iterator<AdminEntity> iteratorForAdmin = adminResult.iterator(); iteratorForAdmin.hasNext();)
			{
				AdminEntity adminEntity = (AdminEntity) iteratorForAdmin.next();
				Query query = session.createQuery(
						"FROM Entityvalues ve where ve.entityId = (select ae.entityId from AdminEntity ae where ae.entityName= ?) and ve.isDeleted=?");
				query.setParameter(0, adminEntity.getEntityName());
				query.setParameter(1, false);
				List<Entityvalues> entityDetails = query.list();
				//List<Entityvalues> entityDetails = new ArrayList<Entityvalues>();
				/*for (Iterator<Entityvalues> iterator = result.iterator(); iterator.hasNext();)
				{

					Entityvalues valuesEntity = (Entityvalues) iterator.next();
					Entityvalues values = new Entityvalues();
					values.setEntityValueId(valuesEntity.getEntityValueId());
					values.setEntityValue(valuesEntity.getEntityValue());
					entityDetails.add(values);
				}*/
				entityDetailsObject.put(adminEntity.getEntityName(), entityDetails);
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
		return entityDetailsObject;
	}

	/**
	 * Gets favorite medications list
	 *
	 * @param userName
	 * @return List of medications
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<String,List<Investigationparameterfields>> getFavoriteData(String userName) throws Exception
	{
		LOGGER.debug("Logger Name: " + LOGGER.getName());

		Map<String,List<Investigationparameterfields>> mapOfFavoriteData = new HashMap<>();

		SessionFactory sessionFactory = HibernateUtilities.createSessionFactory();
		Session session = sessionFactory.openSession();

		try
		{
			// opens a new session from the session factory
			session.getTransaction().begin();

			Query query=session.createQuery("SELECT testName from Investigationstests where isAvailableInIntraOp=1");
			List<String> tests=query.list();

			for(int i=0;i<tests.size();i++)
			{
				List<Investigationparameterfields> result = new ArrayList<Investigationparameterfields>();
				query=session.createQuery("from Investigationtestsparameters IP inner join Investigationparameterfields IPF on IP.investigationTestsParametersId=IPF.investigationTestsParameters.investigationTestsParametersId where IP.investigationTest.investigationsTestsId IN(SELECT te.investigationsTestsId FROM Investigationstests te WHERE te.testName=?) order by IP.testParameterOrder");
				query.setParameter(0, tests.get(i));
				for(Iterator<Object> iterator = query.list().iterator(); iterator.hasNext();)
				{
					Object[] resultset=(Object[]) iterator.next();
					Investigationparameterfields investigationparameterfields=(Investigationparameterfields) resultset[1];
					result.add(investigationparameterfields);
				}
				mapOfFavoriteData.put(tests.get(i), result);
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
		return mapOfFavoriteData;
	}

	/**
	 * Gets favorite medications list
	 *
	 * @param userName
	 * @return List of medications
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<String,List<Investigationparameterfields>> getTestParametersForHistory(long caseId, String userName) throws Exception
	{
		LOGGER.debug("Logger Name: " + LOGGER.getName());

		Map<String,List<Investigationparameterfields>> mapOfFavoriteData = new HashMap<>();

		SessionFactory sessionFactory = HibernateUtilities.createSessionFactory();
		Session session = sessionFactory.openSession();

		try
		{
			// opens a new session from the session factory
			session.getTransaction().begin();

			Query query = session.createQuery("SELECT DISTINCT investigationsTests.testName from Investigationvaluescasemapper iv where iv.patientcase.caseId=?");
			query.setParameter(0, caseId);
			List<String> listOfTests=query.list();

			for(int i=0;i<listOfTests.size();i++)
			{
				List<Investigationparameterfields> result = new ArrayList<Investigationparameterfields>();
				query=session.createQuery("from Investigationtestsparameters IP inner join Investigationparameterfields IPF on IP.investigationTestsParametersId=IPF.investigationTestsParameters.investigationTestsParametersId where IP.investigationTest.investigationsTestsId IN(SELECT te.investigationsTestsId FROM Investigationstests te WHERE te.testName=?) order by IP.testParameterOrder");
				query.setParameter(0, listOfTests.get(i));
				for(Iterator<Object> iterator = query.list().iterator(); iterator.hasNext();)
				{
					Object[] resultset=(Object[]) iterator.next();
					Investigationparameterfields investigationparameterfields=(Investigationparameterfields) resultset[1];
					result.add(investigationparameterfields);
				}
				mapOfFavoriteData.put(listOfTests.get(i), result);
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
		return mapOfFavoriteData;
	}

	/**
	 * Gets favorite medications list
	 *
	 * @param userName
	 * @return List of medications
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getMedicationsForHistory(long caseId, String userName) throws Exception
	{
		LOGGER.debug("Logger Name: " + LOGGER.getName());
		List<String> listOfMedications = null;

		SessionFactory sessionFactory = HibernateUtilities.createSessionFactory();
		Session session = sessionFactory.openSession();

		try
		{
			// opens a new session from the session factory
			session.getTransaction().begin();

			Query query = session.createQuery("SELECT DISTINCT medicationName from IntraopMedicationlog where caseId=?");
			query.setParameter(0, caseId);
			listOfMedications=query.list();
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
		return listOfMedications;
	}

	/**
	 * Gets favorite medications list
	 *
	 * @param userName
	 * @return List of medications
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getFluidsForHistory(long caseId, String userName) throws Exception
	{
		LOGGER.debug("Logger Name: " + LOGGER.getName());
		List<String> listOfFluids = null;

		SessionFactory sessionFactory = HibernateUtilities.createSessionFactory();
		Session session = sessionFactory.openSession();

		try
		{
			// opens a new session from the session factory
			session.getTransaction().begin();

			Query query = session.createQuery("SELECT DISTINCT fluidName from Fluidvalue fv where fv.caseId=?");
			query.setParameter(0, caseId);
			listOfFluids=query.list();
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
		return listOfFluids;
	}

	/**
	 * Gets favorite medications list
	 *
	 * @param userName
	 * @return List of medications
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Fdamedications> getmedicationFavoriteValues(String userName) throws Exception
	{
		LOGGER.debug("Logger Name: " + LOGGER.getName());

		SessionFactory sessionFactory = HibernateUtilities.createSessionFactory();
		Session session = sessionFactory.openSession();

		List<Fdamedications> fdamedicationsResult = null;//new ArrayList<Fdamedications>();

		try
		{
			// opens a new session from the session factory
			session.getTransaction().begin();

			Query query = session.createQuery("FROM Fdamedications de WHERE de.isFavourite=? order by de.order");
			query.setParameter(0, true);
			fdamedicationsResult = query.list();
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
		return fdamedicationsResult;
	}

	/**
	 * Gets favorite medications list
	 *
	 * @param userName
	 * @return List of medications
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Fdamedications> getmedicationFavoriteEntityValues(String userName) throws Exception
	{
		LOGGER.debug("Logger Name: " + LOGGER.getName());

		SessionFactory sessionFactory = HibernateUtilities.createSessionFactory();
		Session session = sessionFactory.openSession();

		List<Fdamedications> fdamedicationsResult = null;//new ArrayList<Fdamedications>();

		try
		{
			// opens a new session from the session factory
			session.getTransaction().begin();

			Query query = session.createQuery("FROM Fdamedications de WHERE de.isFavourite=? order by de.order");
			query.setParameter(0, true);
			fdamedicationsResult = query.list();
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
		return fdamedicationsResult;
	}

	/**
	 * Search medications
	 *
	 * @param searchMedication
	 * @param userName
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Fdamedications> searchFDAMedications(String searchMedication, String userName) throws Exception
	{
		LOGGER.debug("Logger Name: " + LOGGER.getName());

		SessionFactory sessionFactory = HibernateUtilities.createSessionFactory();
		Session session = sessionFactory.openSession();

		List<Fdamedications> fdaMedicationsList = null;//new ArrayList<Fdamedications>();
		String query;

		try
		{
			// opens a new session from the session factory
			session.getTransaction().begin();
			query = "FROM Fdamedications fme where fme.medicationsName like'%" + searchMedication + "%'";
			Query queryExecute = session.createQuery(query);
			fdaMedicationsList = queryExecute.list();

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
		return fdaMedicationsList;
	}

	/**
	 * Gets master data for medication
	 *
	 * @param userName
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, List<Entityvalues>> getMasterDataForMedication(String userName) throws Exception
	{
		LOGGER.debug("Logger Name: " + LOGGER.getName());

		SessionFactory sessionFactory = HibernateUtilities.createSessionFactory();
		Session session = sessionFactory.openSession();

		Map<String, List<Entityvalues>> entityDetailsObject = null;
//		List<Entityvalues> result = new ArrayList<Entityvalues>();
		List<AdminEntity> adminResult = null;//new ArrayList<AdminEntity>();
		try
		{
			// opens a new session from the session factory
			session.getTransaction().begin();
			Query queryForAdminValues = session.createQuery("FROM AdminEntity ae where ae.entityName in (?)");
			queryForAdminValues.setParameter(0, "Route Id");
			adminResult = queryForAdminValues.list();
			entityDetailsObject = new HashMap<String, List<Entityvalues>>();
			for (Iterator<AdminEntity> iteratorForAdmin = adminResult.iterator(); iteratorForAdmin.hasNext();)
			{
				AdminEntity adminEntity = (AdminEntity) iteratorForAdmin.next();
				Query query = session.createQuery(
						"FROM Entityvalues ve where ve.entityId = (select ae.entityId from AdminEntity ae where ae.entityName= ?) and ve.isDeleted=?");
				query.setParameter(0, adminEntity.getEntityName());
				query.setParameter(1, false);
				List<Entityvalues> entityDetails = query.list();
				//List<Entityvalues> entityDetails = new ArrayList<Entityvalues>();
				/*for (Iterator<Entityvalues> iterator = result.iterator(); iterator.hasNext();)
				{

					Entityvalues valuesEntity = (Entityvalues) iterator.next();
					Entityvalues values = new Entityvalues();
					values.setEntityValueId(valuesEntity.getEntityValueId());
					values.setEntityValue(valuesEntity.getEntityValue());
					entityDetails.add(values);
				}*/
				entityDetailsObject.put(adminEntity.getEntityName(), entityDetails);
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
		return entityDetailsObject;
	}

}
