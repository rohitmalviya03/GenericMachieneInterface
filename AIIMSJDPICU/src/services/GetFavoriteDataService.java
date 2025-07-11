/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cdac.common.GeneratedEntities.Fdamedications;
import com.cdac.common.GeneratedEntities.Fluid;
import com.cdac.common.GeneratedEntities.Investigationparameterfields;
import com.cdac.common.util.DAOFactory;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import model.UserSession;

public class GetFavoriteDataService extends Service<Void> {

	private List<Fdamedications> fdamedicationsList;
	private List<Fluid> allFluidslist;
//	List<String> listOfTest;
	Map<String, List<Investigationparameterfields>> listOfTestParameters = new HashMap<>();

	public List<Fdamedications> getFdamedicationsList() {
		return fdamedicationsList;
	}

//	public List<String> getListOfTest() {
//		return listOfTest;
//	}

	public List<Fluid> getAllFluidslist() {
		return allFluidslist;
	}

	public Map<String, List<Investigationparameterfields>> getTestParameters() {
		return listOfTestParameters;
	}

	private static GetFavoriteDataService instance = null;

	public static GetFavoriteDataService getInstance() {
		if (instance == null) {
			instance = new GetFavoriteDataService();
		}

		return instance;
	}

	private GetFavoriteDataService() {
		super();
	}

	@Override
	protected Task<Void> createTask() {
		return new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				// call DB method to save new patient details

				try {
					fdamedicationsList = DAOFactory.getMasterDataDetails().getmedicationFavoriteValues(
							UserSession.getInstance().getUserWithrRolesForUserAuthentication().getUserID());
					/*listOfTest = DAOFactory.getInvestigationTest().fetchListOfTest(
							UserSession.getInstance().getUserWithrRolesForUserAuthentication().getUserID());*/
					allFluidslist = DAOFactory.getFluidList().getAllFluid(
							UserSession.getInstance().getUserWithrRolesForUserAuthentication().getUserID());
					listOfTestParameters = DAOFactory.getMasterDataDetails().getFavoriteData(
							UserSession.getInstance().getUserWithrRolesForUserAuthentication().getUserID());
				} catch (Exception e) {
					throw e;
				}

				return null;
			}

		};
	}

}
