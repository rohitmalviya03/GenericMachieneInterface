/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package services;

import java.util.List;

import com.cdac.common.GeneratedEntities.Plannedprocedure;
import com.cdac.common.util.DAOFactory;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import model.UserSession;
public class GetPlannedProcedureFavoritesService extends Service<Void>
{

	private List<Plannedprocedure> plannedProcedureFavourites;
	
	private static GetPlannedProcedureFavoritesService instance = null;

	public static GetPlannedProcedureFavoritesService getInstance()
	{
		if(instance == null)
		{
			instance = new GetPlannedProcedureFavoritesService();
		}
		return instance;
	}

	private GetPlannedProcedureFavoritesService() {
		super();
	}

	
	public List<Plannedprocedure> getPlannedProcedureFavourites()
	{
		return plannedProcedureFavourites;
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
					plannedProcedureFavourites=DAOFactory.getMasterDataDetails().getPlannedProcedureFavoriteValues(UserSession.getInstance().getUserWithrRolesForUserAuthentication().getUserID());
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
