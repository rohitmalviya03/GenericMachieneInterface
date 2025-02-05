/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cdac.common.GeneratedEntities.Fdamedications;
import com.cdac.common.GeneratedEntities.Fluid;
import com.cdac.common.GeneratedEntities.Investigationparameterfields;

import javafx.collections.ObservableList;

public class MasterDataSession {

	public void setMasterDataMap(Map<String, List<String>> masterDataMap) {
		this.masterDataMap = masterDataMap;
	}

	public void setFavFluidDataMap(Map<String, List<Fluid>> favFluidDataMap) {
		this.favFluidDataMap = favFluidDataMap;
	}

	public void setFavMedDataMap(Map<String, List<Fdamedications>> favMedDataMap) {
		this.favMedDataMap = favMedDataMap;
	}

	public void setTestParamDataMap(Map<String, Map<String, List<Investigationparameterfields>>> testParamDataMap) {
		this.testParamDataMap = testParamDataMap;
	}
	private final static MasterDataSession instance = new MasterDataSession();

	public static MasterDataSession getInstance() {
		return instance;
	}
	private Map<String, List<String>> masterDataMap = new HashMap<String, List<String>>();
	private Map<String, List<Fluid>> favFluidDataMap = new HashMap<String, List<Fluid>>();
	private Map<String, List<Fdamedications>> favMedDataMap = new HashMap<String, List<Fdamedications>>();
	private Map<String,Map<String, List<Investigationparameterfields>>> testParamDataMap = new HashMap<String,Map<String, List<Investigationparameterfields>>>();


	public Map<String, Map<String, List<Investigationparameterfields>>> getTestParamDataMap() {
		return testParamDataMap;
	}

	public Map<String, List<Fluid>> getFavFluidDataMap() {
		return favFluidDataMap;
	}

	public Map<String, List<Fdamedications>> getFavMedDataMap() {
		return favMedDataMap;
	}



	public Map<String, List<String>> getMasterDataMap() {
		return masterDataMap;
	}




}
