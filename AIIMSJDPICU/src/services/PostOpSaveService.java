/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package services;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.cdac.common.GeneratedEntities.Recoveryroomreadings;
import com.cdac.common.util.DAOFactory;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import model.UserSession;

public class PostOpSaveService extends Service<Void> {

	private static Recoveryroomreadings readings;
	private static Integer upperRangeValue;
	private static Integer lowerRangeValue;
	List<Integer> recoveryroomreadingsId= new ArrayList<Integer>();
	private static PostOpSaveService instance = null;
	private static final Logger LOG = Logger.getLogger(PostOpSaveService.class);
	
	public static PostOpSaveService getInstance(Recoveryroomreadings readingsVal, Integer upperRange, Integer lowerRange) {
		if (instance == null) {
			instance = new PostOpSaveService();
		}
		readings = readingsVal;
		upperRangeValue = upperRange;
		lowerRangeValue = lowerRange;
		return instance;
	}

	private PostOpSaveService() {
		super();
	}

	public Integer getRecoveryroomreadingsId() {
		return recoveryroomreadingsId.get(0);
	}



	@Override
	protected Task<Void> createTask()
	{
		return new Task<Void>()
		{

			@Override
			protected Void call() throws Exception
			{
				// call DB method to save new patient details

				try
				{
					recoveryroomreadingsId = DAOFactory.recoveryRoomReading().saveOrUpdate(readings,
					        UserSession.getInstance().getUserWithrRolesForUserAuthentication().getUserID(), upperRangeValue, lowerRangeValue);
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
