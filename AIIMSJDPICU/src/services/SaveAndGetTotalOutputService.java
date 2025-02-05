/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package services;

import java.util.HashMap;
import java.util.List;

import com.cdac.common.GeneratedEntities.IntraopOutput;
import com.cdac.common.util.DAOFactory;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import model.UserSession;

public class SaveAndGetTotalOutputService extends Service<Void> {

	private static SaveAndGetTotalOutputService outputService= null;

	private static IntraopOutput outPutDetailsStatic;
	private static long caseIDStatic;
	private HashMap<String,String> totalVolume;
	private static List<String> outputTypeListStatic;

	private SaveAndGetTotalOutputService() {
		super();
	}
	
	public HashMap<String, String> getTotalVolume() {
		return totalVolume;
	}

	public static SaveAndGetTotalOutputService getInstance(IntraopOutput outPutDetails, long caseID,List<String> outputTypeList)
	{
		if(outputService == null)
		{
			outputService = new SaveAndGetTotalOutputService();
		}
		caseIDStatic = caseID;
		outputTypeListStatic=outputTypeList;
		outPutDetailsStatic = outPutDetails;
		return outputService;
	}

	@Override
	protected Task<Void> createTask() {
		return new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				try
				{
					DAOFactory.getOutputdetails().saveOrUpdate(outPutDetailsStatic, UserSession.getInstance().getUserWithrRolesForUserAuthentication().getUserID());
					totalVolume = DAOFactory.getOutputdetails().getTotalOutputTypeVolume(caseIDStatic,outputTypeListStatic,UserSession.getInstance().getUserWithrRolesForUserAuthentication().getUserID());
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
