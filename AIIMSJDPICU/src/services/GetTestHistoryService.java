/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package services;

import java.util.Date;
import java.util.List;

import com.cdac.common.pojoClasses.InvestigationMapperAndValue;
import com.cdac.common.util.DAOFactory;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import model.UserSession;
public class GetTestHistoryService extends Service<Void> {

	private List<InvestigationMapperAndValue> testHistoryList;
	private static int testId;
	private static long caseID;
	private static Date startDate;
	private static Date endDate;


	public List<InvestigationMapperAndValue> getTestHistoryList() {
		return testHistoryList;
	}

	private static GetTestHistoryService instance = null;

	public static GetTestHistoryService getInstance(int testIdVal, long caseIDVal, Date startDateVal, Date endDateVal)
	{
		if(instance == null)
		{
			instance = new GetTestHistoryService();
		}
		testId = testIdVal;
		caseID = caseIDVal;
		startDate = startDateVal;
		endDate = endDateVal;
		return instance;
	}

	private GetTestHistoryService() {
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
					testHistoryList = DAOFactory.getInvestigationValueView().getInvestigationvaluecasemapperlist(caseID,
					        startDate, endDate,
					        UserSession.getInstance().getUserWithrRolesForUserAuthentication().getUserID());
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
