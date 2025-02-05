/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package services;

import java.util.Map;

import com.cdac.common.util.DAOFactory;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class ViewAllCaseService extends Service<Void>
{

	private static int patientID;
	private static int pageNum;
	private Map<String, Object> caseDetails;


	public Map<String, Object> getCaseDetails()
	{
		return caseDetails;
	}
	private static ViewAllCaseService instance = null;

	public static ViewAllCaseService getInstance(int patientIDVal, int pageNumVal)
	{
		if(instance == null)
		{
			instance = new ViewAllCaseService();
		}
		patientID = patientIDVal;
		pageNum = pageNumVal;
		return instance;
	}

	private ViewAllCaseService() {
		super();
	}



	@Override
	protected Task<Void> createTask()
	{
		return new Task<Void>()
		{

			@Override
			protected Void call() throws Exception
			{
				// call DB method to save new patient details

				try
				{
					caseDetails = DAOFactory.viewAllCases().viewAllCases(patientID, System.getProperty("user.name"),
					        pageNum, 10, false);

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
