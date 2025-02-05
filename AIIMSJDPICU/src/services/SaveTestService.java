/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package services;

import java.util.Date;
import java.util.List;

import com.cdac.common.pojoClasses.InvestigationSetValue;
import com.cdac.common.util.DAOFactory;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import model.UserSession;
public class SaveTestService extends Service<Void> {

	private static Long caseID;
	private	static Integer testID;
	private static String comments;
	private static Date date;

	private	static List<InvestigationSetValue> investigationsValues;
	private static SaveTestService instance = null;

	public static SaveTestService getInstance(Long caseIDVal, Integer testIDVal, String commentsVal, Date dateVal,
			List<InvestigationSetValue> investigationsValuesVal)
	{
		if(instance == null)
		{
			instance = new SaveTestService();
		}
		caseID = caseIDVal;
		testID = testIDVal;
		comments = commentsVal;
		date = dateVal;
		investigationsValues = investigationsValuesVal;
		return instance;
	}

	private SaveTestService() {
		super();
	}


	private String result;



	public String getResult() {
		return result;
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
					result = DAOFactory.investigationValues().setInvestigationValues(caseID, testID, investigationsValues,date,comments,  UserSession.getInstance().getUserWithrRolesForUserAuthentication().getUserID());
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
