/*******************************************************************************
 * © 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package model;

public class InfusionPumpModel {

	private String deviceName;
	private String serialNo;
	private String infusionMode;
	private String deviceStep;
	private String drugName;
	private String infusionRate;
	private String portNo;
	private String volumeInfused;


	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public String getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
	public String getInfusionMode() {
		return infusionMode;
	}
	public void setInfusionMode(String infusionMode) {
		this.infusionMode = infusionMode;
	}
	public String getDeviceStep() {
		return deviceStep;
	}
	public void setDeviceStep(String deviceStep) {
		this.deviceStep = deviceStep;
	}
	public String getDrugName() {
		return drugName;
	}
	public void setDrugName(String drugName) {
		this.drugName = drugName;
	}
	public String getInfusionRate() {
		return infusionRate;
	}
	public void setInfusionRate(String infusionRate) {
		this.infusionRate = infusionRate;
	}
	public String getPortNo() {
		return portNo;
	}
	public void setPortNo(String portNo) {
		this.portNo = portNo;
	}
	public String getVolumeInfused() {
		return volumeInfused;
	}
	public void setVolumeInfused(String volumeInfused) {
		this.volumeInfused = volumeInfused;
	}





}
