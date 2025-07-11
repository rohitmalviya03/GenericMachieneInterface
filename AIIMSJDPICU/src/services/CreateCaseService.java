/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package services;

import java.util.Map;

import com.cdac.common.GeneratedEntities.Patientcase;
import com.cdac.common.util.DAOFactory;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class CreateCaseService extends Service<Void>
{
	private static Patientcase createCase;
	private static int patientID;

	private Patientcase result;

	private static CreateCaseService instance = null;

	public static CreateCaseService getInstance(Patientcase createCaseVal, int patientIDVal)
	{
		if(instance == null)
		{
			instance = new CreateCaseService();
		}
		createCase = createCaseVal;
		patientID = patientIDVal;
		return instance;
	}

	private CreateCaseService() {
		super();
	}

	public Patientcase getResult()
	{
		return result;
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
					result = DAOFactory.createCase().createCase(createCase, patientID,System.getProperty("user.name"));

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
