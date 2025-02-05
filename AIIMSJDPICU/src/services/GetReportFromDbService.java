/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package services;

import org.json.simple.JSONObject;

import com.cdac.common.GeneratedEntities.IntraopReportFile;
import com.cdac.common.util.DAOFactory;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import model.UserSession;

public class GetReportFromDbService extends Service<Void> {

	private static GetReportFromDbService getReportFromDbService= null;
	private IntraopReportFile reportData = new IntraopReportFile();


	public IntraopReportFile getReportData() {
		return reportData;
	}

	private static long caseId;


	private GetReportFromDbService() {
		super();
	}

	public static GetReportFromDbService getInstance(long caseID)
	{
		if(getReportFromDbService == null)
		{
			getReportFromDbService = new GetReportFromDbService();
		}
		caseId=caseID;
		return getReportFromDbService;
	}

	@Override
	protected Task<Void> createTask() {
		// TODO Auto-generated method stub
		return new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				// TODO Auto-generated method stub

				try
				{
					reportData = DAOFactory.getPatientDetailsReport().getFile(caseId);
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
