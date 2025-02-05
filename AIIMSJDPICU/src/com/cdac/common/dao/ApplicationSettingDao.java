/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package com.cdac.common.dao;

import java.util.List;

import com.cdac.common.GeneratedEntities.Applicationsetting;

/**
 * @author sahil.sharma08
 *
 */
public interface ApplicationSettingDao 
{
	/**
	 * This method returns String  having success/failure message 
	 * from application setting entity
	 * 
	 * 
	 * @param applicationsettingObject
	 * - the setting which needs to be saved in the database.
	 * 
	 * @param userName.
	 * 
	 * 
	 * @return String  having success/failure message.
	 * 
	 */
	public String addSetting(Applicationsetting applicationsettingObject, String userName) throws Exception;
	
	
	/**
	 * This method returns list of application setting 
	 * from application setting entity
	 * 
	 * 
	 * @param userName.
	 * 
	 * 
	 * @return list of application setting.
	 * 
	 */
	
	public List<Applicationsetting> getListOfApplicationSettings(String userName) throws Exception;
	
	
	/**
	 * This method string containing the success message along with the delteted application setting 
	 * from application setting entity
	 * 
	 *  @param parameterID.
	 * - Setting ID which is to be deleted.
	 * 
	 * @param userName.
	 * 
	 * 
	 * @return string containing the success message along with the delteted application setting 
	 * 
	 */
	public String deleteSetting(Integer parameterID, String userName) throws Exception;
}
