/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package services;

import com.cdac.common.GeneratedEntities.Cannulations;
import com.cdac.common.util.DAOFactory;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import model.UserSession;

public class CreateCannulationService extends Service<Void> {

	private static Cannulations cannulationsRecordStatic;
	
	private static CreateCannulationService instance = null;
	private CreateCannulationService() {
		super();
	}

	public static CreateCannulationService getInstance(Cannulations cannulationsRecord)
	{
		if(instance == null)
		{
			instance = new CreateCannulationService();
		}
		cannulationsRecordStatic = cannulationsRecord;
		return instance;
	}

	@Override
	protected Task<Void> createTask() {
		return new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				try
				{
					DAOFactory.cannulations().createCannulationRecord(cannulationsRecordStatic, UserSession.getInstance().getUserWithrRolesForUserAuthentication().getUserID());
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
