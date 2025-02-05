/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package services;

import java.util.List;

import com.cdac.common.pojoClasses.InvestigationSetValue;
import com.cdac.common.util.DAOFactory;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import model.UserSession;
public class FetchTestHistoryService extends Service<Void>{


	List<InvestigationSetValue> investigationValueslist;
	private static int testId;


	public List<InvestigationSetValue> getInvestigationValueslist() {
		return investigationValueslist;
	}
	
	private static FetchTestHistoryService instance = null;

	public static FetchTestHistoryService getInstance(int testIdVal)
	{
		if(instance == null)
		{
			instance = new FetchTestHistoryService();
		}
		testId = testIdVal;
		return instance;
	}

	private FetchTestHistoryService() {
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

				investigationValueslist = DAOFactory.getInvestigationValueView().getInvestigationValueslist( testId, UserSession.getInstance().getUserWithrRolesForUserAuthentication().getUserID());
				return null;
			}

		};

	}

}
