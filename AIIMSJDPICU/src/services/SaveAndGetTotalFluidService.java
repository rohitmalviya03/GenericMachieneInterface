/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cdac.common.GeneratedEntities.Fluidvalue;
import com.cdac.common.util.DAOFactory;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import model.UserSession;

public class SaveAndGetTotalFluidService extends Service<Void>
{
	private static SaveAndGetTotalFluidService addFluidService = null;
	private static boolean isStop= false;
	private static Fluidvalue fluidvalueStatic;
	private Map<String, Fluidvalue> returnMap;

private static long caseID;
	
	private HashMap<String,String> fluidVolume;
	private static List<String> fluidNameList;

	public Map<String, Fluidvalue> getReturnMap()
	{
		return returnMap;
	}

	private SaveAndGetTotalFluidService() {
		super();
	}
	
	public HashMap<String, String> getFluidVolume() {
		return fluidVolume;
	}

	public static SaveAndGetTotalFluidService getInstance(Fluidvalue fluidvalue, long caseIDVal,List<String> fluidNameListVal, boolean stopVal)
	{
		if(addFluidService == null)
		{
			addFluidService = new SaveAndGetTotalFluidService();
		}
		fluidvalueStatic = fluidvalue;
		fluidNameList=fluidNameListVal;
		caseID = caseIDVal;
		isStop = stopVal;
		return addFluidService;
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
					returnMap = DAOFactory.getFluidList().addFluid(fluidvalueStatic, UserSession.getInstance().getUserWithrRolesForUserAuthentication().getUserID());
					if(isStop)
						fluidVolume = DAOFactory.getFluidList().getTotalFluidNameVolume(caseID,fluidNameList,UserSession.getInstance().getUserWithrRolesForUserAuthentication().getUserID());
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
