/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package com.cdac.common.pojoClasses;

import java.util.Date;

public class MedicationVolumeUpdation
{

	private Long medicationLogID;
	private float volume;
	private float dosage;
	private String rateOfInfusion;
	private String dosageUnit;
	private String concentration;
	private Date endTime;
	private String serialNo;
	private int configureDeviceId;


	public MedicationVolumeUpdation() {
		super();
		// TODO Auto-generated constructor stub
	}


	public MedicationVolumeUpdation(Long medicationLogID, float volume, Date endTime, String serialNo) {
		super();
		this.medicationLogID = medicationLogID;
		this.volume = volume;
		this.endTime = endTime;
		this.serialNo = serialNo;
	}







	public MedicationVolumeUpdation(Long medicationLogID, float volume, float dosage, String rateOfInfusion,
			String dosageUnit, String concentration, Date endTime, String serialNo, int configureDeviceId) {
		super();
		this.medicationLogID = medicationLogID;
		this.volume = volume;
		this.dosage = dosage;
		this.rateOfInfusion = rateOfInfusion;
		this.dosageUnit = dosageUnit;
		this.concentration = concentration;
		this.endTime = endTime;
		this.serialNo = serialNo;
		this.configureDeviceId = configureDeviceId;
	}


	public String getRateOfInfusion() {
		return rateOfInfusion;
	}


	public void setRateOfInfusion(String rateOfInfusion) {
		this.rateOfInfusion = rateOfInfusion;
	}


	public int getConfigureDeviceId() {
		return configureDeviceId;
	}


	public void setConfigureDeviceId(int configureDeviceId) {
		this.configureDeviceId = configureDeviceId;
	}


	public Long getMedicationLogID() {
		return medicationLogID;
	}


	public void setMedicationLogID(Long medicationLogID) {
		this.medicationLogID = medicationLogID;
	}


	public float getVolume() {
		return volume;
	}


	public void setVolume(float volume) {
		this.volume = volume;
	}


	public Date getEndTime() {
		return endTime;
	}


	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}


	public String getSerialNo() {
		return serialNo;
	}


	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}


	public float getDosage() {
		return dosage;
	}


	public void setDosage(float dosage) {
		this.dosage = dosage;
	}


	public String getDosageUnit() {
		return dosageUnit;
	}


	public void setDosageUnit(String dosageUnit) {
		this.dosageUnit = dosageUnit;
	}


	public String getConcentration() {
		return concentration;
	}


	public void setConcentration(String concentration) {
		this.concentration = concentration;
	}





}
