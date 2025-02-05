/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package services;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.cdac.common.GeneratedEntities.Entityvalues;
import com.cdac.common.util.DAOFactory;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class GetDeviceModel extends Service<Void> {
	
	private static final Logger LOG = Logger.getLogger(GetDeviceModel.class);

	private static String make;
	
	private static GetDeviceModel instance = null;

	public static GetDeviceModel getInstance(String makeVal)
	{
		if(instance == null)
		{
			instance = new GetDeviceModel();
		}
		make = makeVal;
		return instance;
	}

	private GetDeviceModel() {
		super();
	}

	private Map<String, List<Entityvalues>> deviceModel;

	public Map<String, List<Entityvalues>> getDeviceModel() {
		return deviceModel;
	}

	@Override
	protected Task<Void> createTask() {
		return new Task<Void>()
		{

		@Override
		protected Void call() throws Exception
		{
			try {
				deviceModel=DAOFactory.deviceConfigurationFetchlist().DeviceConfigurationfetchList(make);

			} catch (Exception e) {
				e.printStackTrace();
				LOG.error("## Exception occured:", e);
			}
			return null;
		}
		};



	}

}
