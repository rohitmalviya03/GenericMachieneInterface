/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package com.cdac.common.pojoClasses;

import java.util.List;

import com.cdac.common.GeneratedEntities.IntraopAnesthesiamedicationrecord;
import com.cdac.common.GeneratedEntities.IntraopAnesthesiarecord;

public class AnesthesiaFetchListWithMedication 
{

	private List<IntraopAnesthesiamedicationrecord> listOfAnesthesiaMedication;
	private IntraopAnesthesiarecord AnesthesiaRecord;
	
	
	public AnesthesiaFetchListWithMedication() 
	{
		super();
	}


	public AnesthesiaFetchListWithMedication(List<IntraopAnesthesiamedicationrecord> listOfAnesthesiaMedication,
			IntraopAnesthesiarecord AnesthesiaRecord) 
	{
		super();
		this.listOfAnesthesiaMedication = listOfAnesthesiaMedication;
		this.AnesthesiaRecord = AnesthesiaRecord;
	}


	public List<IntraopAnesthesiamedicationrecord> getListOfAnesthesiaMedication() {
		return listOfAnesthesiaMedication;
	}


	public void setListOfAnesthesiaMedication(List<IntraopAnesthesiamedicationrecord> listOfAnesthesiaMedication) {
		this.listOfAnesthesiaMedication = listOfAnesthesiaMedication;
	}


	public IntraopAnesthesiarecord getAnesthesiaRecord() {
		return AnesthesiaRecord;
	}


	public void setAnesthesiaRecord(IntraopAnesthesiarecord AnesthesiaRecord) {
		this.AnesthesiaRecord = AnesthesiaRecord;
	}
	
	
	
	
}
