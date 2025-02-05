/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package services;

import java.util.List;
import java.util.Map;

import com.cdac.common.GeneratedEntities.Entityvalues;
import com.cdac.common.util.DAOFactory;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import model.UserSession;
public class GetMasterDataAnesthesiaService extends Service<Void>
{

	private Map<String, List<Entityvalues>> anesthesiaMasterDataList;

	public Map<String, List<Entityvalues>> getAnesthesiaMasterDataList()
	{
		return anesthesiaMasterDataList;
	}
	
	private static GetMasterDataAnesthesiaService instance = null;

	public static GetMasterDataAnesthesiaService getInstance() {
		if (instance == null) {
			instance = new GetMasterDataAnesthesiaService();
		}
		return instance;
	}

	private GetMasterDataAnesthesiaService() {
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
				try
				{
					anesthesiaMasterDataList = DAOFactory.getMasterDataDetails()
					        .getMasterDataForAnesthesia(UserSession.getInstance().getUserWithrRolesForUserAuthentication().getUserID());
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
