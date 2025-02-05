/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package services;

import com.cdac.common.GeneratedEntities.Patientcase;
import com.cdac.common.util.DAOFactory;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class GetCaseDetailsService extends Service<Void>{

	private Patientcase caseDetails;
	private static long caseId;

	private static GetCaseDetailsService instance = null;

	public static GetCaseDetailsService getInstance(long caseIdVal)
	{
		if(instance == null)
		{
			instance = new GetCaseDetailsService();
		}
		caseId = caseIdVal;
		return instance;
	}

	private GetCaseDetailsService() {
		super();
	}

	public Patientcase getCaseDetails() {
		return caseDetails;
	}



	@Override
	protected Task<Void> createTask()
	{
		return new Task<Void>()
		{

			@Override
			protected Void call() throws Exception
			{
				try
				{
					caseDetails = DAOFactory.createCase().getCreateCaseEntity(caseId);

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
