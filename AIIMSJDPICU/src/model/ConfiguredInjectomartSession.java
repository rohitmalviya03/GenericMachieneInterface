/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package model;

import java.util.List;

import com.cdac.common.pojoClasses.MedicationVolumeUpdation;

public class ConfiguredInjectomartSession {

	private final static ConfiguredInjectomartSession instance = new ConfiguredInjectomartSession();

	public static ConfiguredInjectomartSession getInstance()
	{
		return instance;
	}

	private List<MedicationVolumeUpdation> configuredDeviceList;
	private List<DeviceModel> assignedDeviceList;
	private List<DeviceModel> detectedDeviceList;

	public List<MedicationVolumeUpdation> getConfiguredDeviceList() {
		return configuredDeviceList;
	}

	public void setConfiguredDeviceList(List<MedicationVolumeUpdation> configuredDeviceList) {
		this.configuredDeviceList = configuredDeviceList;
	}

	public List<DeviceModel> getAssignedDeviceList() {
		return assignedDeviceList;
	}

	public void setAssignedDeviceList(List<DeviceModel> assignedDeviceList) {
		this.assignedDeviceList = assignedDeviceList;
	}

	public List<DeviceModel> getDetectedDeviceList() {
		return detectedDeviceList;
	}

	public void setDetectedDeviceList(List<DeviceModel> detectedDeviceList) {
		this.detectedDeviceList = detectedDeviceList;
	}



}
