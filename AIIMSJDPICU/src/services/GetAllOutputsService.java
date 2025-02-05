/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package services;

import java.util.List;

import com.cdac.common.GeneratedEntities.IntraopOutput;
import com.cdac.common.util.DAOFactory;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import model.UserSession;
public class GetAllOutputsService extends Service<Void> {


	private static int patientID;

	private static long caseID;

	private List<IntraopOutput> listOfOutputRecords;

	private static GetAllOutputsService instance = null;

	public static GetAllOutputsService getInstance(int patientIDVal, Long caseIDVal)
	{
		if(instance == null)
		{
			instance = new GetAllOutputsService();
		}
		patientID = patientIDVal;
		caseID = caseIDVal;
		return instance;
	}

	private GetAllOutputsService() {
		super();
	}

	public List<IntraopOutput> getListOfOutputRecords() {
		return listOfOutputRecords;
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
					listOfOutputRecords = DAOFactory.getOutputdetails().fetchOutputList(caseID, UserSession.getInstance().getUserWithrRolesForUserAuthentication().getUserID());
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
