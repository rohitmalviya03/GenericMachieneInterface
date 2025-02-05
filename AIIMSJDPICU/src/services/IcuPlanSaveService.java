/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package services;

import org.apache.log4j.Logger;

import com.cdac.common.GeneratedEntities.IcuPlanEntity;
import com.cdac.common.util.DAOFactory;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import model.UserSession;

/**
 * @author rohi.bhardwaj
 *
 */
public class IcuPlanSaveService extends Service<Void>{

	private static final Logger LOG = Logger.getLogger(IcuPlanSaveService.class);
	private static IcuPlanEntity readings ;
	private Integer icuPlanId =0;
	private static IcuPlanSaveService instance =null;


	private IcuPlanSaveService() {
		super();
	}



	public Integer getIcuPlanId() {
		return icuPlanId;
	}



	public static IcuPlanSaveService getInstance(IcuPlanEntity icuPlanReadings){
		if(instance==null){
			instance = new IcuPlanSaveService();
		}
		readings = icuPlanReadings;
		return instance ;
	}
	@Override
	protected Task<Void> createTask() {
		return new Task<Void>()
		{

			@Override
			protected Void call() throws Exception
			{
				// call DB method to save new ICU plan details

				try
				{
					DAOFactory.icuPlanReadings().saveOrUpdate(readings,
					        UserSession.getInstance().getUserWithrRolesForUserAuthentication().getUserID());
				}
				catch (Exception e)
				{
					e.printStackTrace();
					LOG.error("## Exception occured:", e);
				}

				return null;
			}

		};
	}


}
