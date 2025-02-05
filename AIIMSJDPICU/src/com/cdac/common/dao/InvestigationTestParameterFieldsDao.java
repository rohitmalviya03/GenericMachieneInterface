/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package com.cdac.common.dao;

import java.util.List;

import com.cdac.common.GeneratedEntities.Investigationparameterfields;
import com.cdac.common.GeneratedEntities.Investigationstests;

public interface InvestigationTestParameterFieldsDao 
{
	/**
	 * This method returns list of test parameter fields corresponding to the test id provided by the user
	 * from Investigationparameterfields entity
	 * 
	 * 
	 * @param test
	 * - test whose paramters is to be retirieved
	 * 
	 * 
	 * 
	 * @param userName.
	 * 
	 * @return list of test parameter fields corresponding to the test id provided by the user
	 * 
	 */
	public List<Investigationparameterfields> getAllParameterField(Investigationstests test, String userName) throws Exception;
}
