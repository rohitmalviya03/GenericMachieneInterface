/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package com.cdac.common.dao;

import java.util.List;
import java.util.Map;

import com.cdac.common.GeneratedEntities.Entityvalues;

public interface DeviceConfigurationFetchListDao 
{

	/**
	 * This method returns map containg list of models for a specific make.
	 *from IntraopDevices entity
	 * @param make
	 * name of the maker corresponding to which models list is required
	 * @return map containg list of models for a specific make.
	 *
	 */
	public Map<String,List<Entityvalues>> DeviceConfigurationfetchList(String make)  throws Exception;
	
	/**
	 * This method returns map containg list of serial numbers for a specific model.
	 *from IntraopDevices entity
	 * @param model
	 * name of the model corresponding to which serial numerbers  is required
	 * @return map containg list of serial numbers for a specific model.
	 *
	 */
	public Map<String,List<Entityvalues>> serialNumberFetchList(String model)  throws Exception;
}
