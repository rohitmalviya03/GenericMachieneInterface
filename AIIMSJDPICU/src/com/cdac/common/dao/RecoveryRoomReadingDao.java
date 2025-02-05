/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package com.cdac.common.dao;

import java.util.List;

import com.cdac.common.GeneratedEntities.Recoveryroomreadings;

public interface RecoveryRoomReadingDao
{
	/**
	 * This method returns ID of the Recoveryroomreadings record which is saved or updated
	 * from Recoveryroomreadings entity
	 *
	 *
	 * @param recoveryroomreadings
	 * - recoveryroomreadings object containing the recoveryroomreadings details which is to be saved
	 *
	 *
	 *
	 * @param userName.
	 *
	 * @return ID of the Recoveryroomreadings record which is saved or updated
	 *
	 */
	public List<Integer> saveOrUpdate(Recoveryroomreadings recoveryroomreadings,String userName, Integer upperRange, Integer lowerRange) throws Exception;


	/**
	 * This method returns String containing success/error message for deleting the Recoveryroomreadings record
	 * from Recoveryroomreadings entity
	 *
	 *
	 * @param recoveryroomreadingsrecordID
	 * - ID of Recoveryroomreadings record to be deleted
	 *
	 *
	 *
	 * @param userName.
	 *
	 * @return String containing success/error message for deleting the Recoveryroomreadings record
	 *
	 */
	public String deleteRecord(int recoveryroomreadingsrecordID, String userName) throws Exception;


	/**
	 * This method returns Recoveryroomreadings record corresponding to the caseID provided by the user
	 * from Recoveryroomreadings entity
	 *
	 *
	 * @param caseID
	 * - ID of case corresponding to which Recoveryroomreadings record to be retrieved
	 *
	 *
	 *
	 * @param userName.
	 *
	 * @return Recoveryroomreadings record corresponding to the caseID provided by the user
	 *
	 */
	public Recoveryroomreadings getRecoveryRoomReadingRecord(long caseID, String userName) throws Exception;

}
