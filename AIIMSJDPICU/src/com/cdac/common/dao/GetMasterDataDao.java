/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package com.cdac.common.dao;

import java.util.List;
import java.util.Map;

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

// TODO: Auto-generated Javadoc
/**
 * The Interface GetMasterDataDao.
 */
public interface GetMasterDataDao {

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
	public Map<String, List<Entityvalues>> getEntityDetails(String entityName, String userName) throws Exception;

	/**
	 * Interface method to Get the master data for create case.
	 *
	 * @param userName
	 *            the logged in user
	 * @return the master data for create case
	 * @throws Exception
	 *             the exception
	 */
	public Map<String, List<Entityvalues>> getMasterDataForCreateCase(String userName) throws Exception;

	/**
	 * Interface method to Get the diagnosis favorite values.
	 *
	 * @param userName
	 *            the logged in user
	 * @return the diagnosis favorite values list
	 * @throws Exception
	 *             the exception
	 */
	public List<Diagnosis> getDiagnosisFavoriteValues(String userName) throws Exception;

	/**
	 * Interface method for Gets the planned procedure favorite values.
	 *
	 * @param userName
	 *            the logged in user
	 * @return the planned procedure favorite values list
	 * @throws Exception
	 *             the exception
	 */
	public List<Plannedprocedure> getPlannedProcedureFavoriteValues(String userName) throws Exception;

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
	public List<Diagnosiscategory> getDiagnosisCategory(String version, String userName) throws Exception;

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
	public List<Diagnosissubcategory> getDiagnosisSubCategory(Integer diagnosisCategoryID, String version,
	        String userName)
	        throws Exception;

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
	public List<Procedurecategory> getProcedureCategory(String version, String userName) throws Exception;

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
	public List<Proceduresubcategory> getProcedureSubCategory(String procedureCategoryCode, String userName)
	        throws Exception;

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
	public List<Proceduresubsubcategory> getProcedureSubSubCategory(String procedureSubCategoryCode, String userName)
	        throws Exception;

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
	public List<Diagnosis> searchDiagnosisDao(SearchDiagnosis searchDiagnosisEntity, String userName) throws Exception;

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
	public List<Plannedprocedure> searchProceduresDao(String id, String searchCriteria, String version, String userName)
	        throws Exception;

	/**
	 * Gets favorite medications list
	 * 
	 * @param userName
	 * @return List of medications
	 * @throws Exception
	 */
	public List<Fdamedications> getmedicationFavoriteValues(String userName) throws Exception;
	
	/**
	 * Gets favorite data including Fluids, Medications, Tests
	 * 
	 * @param userName
	 * @return List of medications
	 * @throws Exception
	 */
	public Map<String,List<Investigationparameterfields>> getFavoriteData(String userName) throws Exception;
	/**
	 * Search medications
	 * 
	 * @param searchMedication
	 * @param userName
	 * @return
	 * @throws Exception
	 */
	public List<Fdamedications> searchFDAMedications(String searchMedication, String userName) throws Exception;

	/**
	 * Gets master data for anesthesia
	 * 
	 * @param userName
	 * @return
	 * @throws Exception
	 */
	public Map<String, List<Entityvalues>> getMasterDataForAnesthesia(String userName) throws Exception;

	/**
	 * Gets master data for medication
	 * 
	 * @param userName
	 * @return
	 * @throws Exception
	 */
	public Map<String, List<Entityvalues>> getMasterDataForMedication(String userName) throws Exception;
	
	public Map<String,List<Investigationparameterfields>> getTestParametersForHistory(long caseId, String userName) throws Exception;
	
	public List<Fdamedications> getmedicationFavoriteEntityValues(String userName) throws Exception;
	
	public List<String> getMedicationsForHistory(long caseId, String userName) throws Exception;
	public List<String> getFluidsForHistory(long caseId, String userName) throws Exception;
}
