/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package services;

import java.util.ArrayList;
import java.util.List;

import com.cdac.common.GeneratedEntities.Investigationparameterfields;
import com.cdac.common.GeneratedEntities.Investigationstests;
import com.cdac.common.util.DAOFactory;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import model.UserSession;
public class GetAllTestParamService extends Service<Void> {


	List<Investigationparameterfields> listOfInvestigationParameterFields=new ArrayList<Investigationparameterfields>();
	private static Investigationstests test;

	private static GetAllTestParamService instance = null;

	public static GetAllTestParamService getInstance(Investigationstests testVal)
	{
		if(instance == null)
		{
			instance = new GetAllTestParamService();
		}
		test = testVal;
		return instance;
	}

	private GetAllTestParamService() {
		super();
	}


	public List<Investigationparameterfields> getListOfInvestigationParameterFields() {
		return listOfInvestigationParameterFields;
	}

	@Override
	protected Task<Void> createTask()
	{
		// TODO Auto-generated method stub
		return new Task<Void>()
		{

			@Override
			protected Void call() throws Exception
			{
				// TODO Auto-generated method stub

				try
				{
					listOfInvestigationParameterFields = DAOFactory.getInvestigationParameterFields().getAllParameterField(test, UserSession.getInstance().getUserWithrRolesForUserAuthentication().getUserID());
				}
				catch (Exception e) 
				{
					throw e;
				}
				return null;
			}

		};

	}

}
