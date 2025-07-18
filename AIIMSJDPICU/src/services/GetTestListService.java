/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package services;

import java.util.ArrayList;
import java.util.List;

import com.cdac.common.GeneratedEntities.Investigationstests;
import com.cdac.common.util.DAOFactory;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import model.UserSession;

public class GetTestListService extends Service<Void> {

	List<Investigationstests> listOfTest=new ArrayList<Investigationstests>();


	public List<Investigationstests> getListOfTest() {
		return listOfTest;
	}


	private static GetTestListService instance = null;

	public static GetTestListService getInstance()
	{
		if(instance == null)
		{
			instance = new GetTestListService();
		}
		return instance;
	}

	private GetTestListService() {
		super();
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
					listOfTest = DAOFactory.getInvestigationTest().fetchListOfTest(UserSession.getInstance().getUserWithrRolesForUserAuthentication().getUserID());
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
