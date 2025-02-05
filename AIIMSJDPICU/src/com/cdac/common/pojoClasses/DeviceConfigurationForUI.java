/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package com.cdac.common.pojoClasses;

import java.util.Date;

public class DeviceConfigurationForUI 
{
	private int deviceCongigurationId;
	private int patientId;
	private long caseId;
	private Integer department;
	private String departmentName;
	private String wardNumber;
	private String model;
	private String make;
	private String serialNumber;
	private Integer ot;
	private String otName;
	/*private int deviceId;*/
	private String portNumber;
	private boolean deviceStatus;
	private boolean transportData;
	private String createdBy;
	private Date createdTime;
	private String updatedBy;
	private Date updatedTime;
	private String medicine;
	
	
	public DeviceConfigurationForUI() 
	{
		
	}
	public DeviceConfigurationForUI(int deviceCongigurationId, int patientId, long caseId, Integer department,
			String departmentName, String wardNumber, String model, String serialNumber, Integer ot, String otName,
			int deviceId, boolean deviceStatus, String createdBy, Date createdTime, String updatedBy, Date updatedTime,
			String medicine) {
		super();
		this.deviceCongigurationId = deviceCongigurationId;
		this.patientId = patientId;
		this.caseId = caseId;
		this.department = department;
		this.departmentName = departmentName;
		this.wardNumber = wardNumber;
		this.model = model;
		this.serialNumber = serialNumber;
		this.ot = ot;
		this.otName = otName;
		/*this.deviceId = deviceId;*/
		this.deviceStatus = deviceStatus;
		this.createdBy = createdBy;
		this.createdTime = createdTime;
		this.updatedBy = updatedBy;
		this.updatedTime = updatedTime;
		this.medicine = medicine;
	}
	public int getDeviceCongigurationId() {
		return deviceCongigurationId;
	}
	
	public DeviceConfigurationForUI(int deviceCongigurationId, int patientId, long caseId, Integer department,
			String departmentName, String wardNumber, String model, String serialNumber, Integer ot, String otName,
			int deviceId, String portNumber, boolean deviceStatus, String createdBy, Date createdTime, String updatedBy,
			Date updatedTime, String medicine) {
		super();
		this.deviceCongigurationId = deviceCongigurationId;
		this.patientId = patientId;
		this.caseId = caseId;
		this.department = department;
		this.departmentName = departmentName;
		this.wardNumber = wardNumber;
		this.model = model;
		this.serialNumber = serialNumber;
		this.ot = ot;
		this.otName = otName;
		/*this.deviceId = deviceId;*/
		this.portNumber = portNumber;
		this.deviceStatus = deviceStatus;
		this.createdBy = createdBy;
		this.createdTime = createdTime;
		this.updatedBy = updatedBy;
		this.updatedTime = updatedTime;
		this.medicine = medicine;
	}
	
	public DeviceConfigurationForUI(int deviceCongigurationId, int patientId, long caseId, Integer department,
			String departmentName, String wardNumber, String model, String make, String serialNumber, Integer ot,
			String otName, int deviceId, String portNumber, boolean deviceStatus, String createdBy, Date createdTime,
			String updatedBy, Date updatedTime, String medicine) {
		super();
		this.deviceCongigurationId = deviceCongigurationId;
		this.patientId = patientId;
		this.caseId = caseId;
		this.department = department;
		this.departmentName = departmentName;
		this.wardNumber = wardNumber;
		this.model = model;
		this.make = make;
		this.serialNumber = serialNumber;
		this.ot = ot;
		this.otName = otName;
		/*this.deviceId = deviceId;*/
		this.portNumber = portNumber;
		this.deviceStatus = deviceStatus;
		this.createdBy = createdBy;
		this.createdTime = createdTime;
		this.updatedBy = updatedBy;
		this.updatedTime = updatedTime;
		this.medicine = medicine;
	}
	
	public DeviceConfigurationForUI(int deviceCongigurationId, int patientId, long caseId, Integer department,
			String departmentName, String wardNumber, String model, String make, String serialNumber, Integer ot,
			String otName, int deviceId, String portNumber, boolean deviceStatus, boolean transportData,
			String createdBy, Date createdTime, String updatedBy, Date updatedTime, String medicine) {
		super();
		this.deviceCongigurationId = deviceCongigurationId;
		this.patientId = patientId;
		this.caseId = caseId;
		this.department = department;
		this.departmentName = departmentName;
		this.wardNumber = wardNumber;
		this.model = model;
		this.make = make;
		this.serialNumber = serialNumber;
		this.ot = ot;
		this.otName = otName;
		/*this.deviceId = deviceId;*/
		this.portNumber = portNumber;
		this.deviceStatus = deviceStatus;
		this.transportData = transportData;
		this.createdBy = createdBy;
		this.createdTime = createdTime;
		this.updatedBy = updatedBy;
		this.updatedTime = updatedTime;
		this.medicine = medicine;
	}
	public void setDeviceCongigurationId(int deviceCongigurationId) {
		this.deviceCongigurationId = deviceCongigurationId;
	}
	public int getPatientId() {
		return patientId;
	}
	public void setPatientId(int patientId) {
		this.patientId = patientId;
	}
	
	public String getPortNumber() {
		return portNumber;
	}
	public void setPortNumber(String portNumber) {
		this.portNumber = portNumber;
	}
	public long getCaseId() {
		return caseId;
	}
	public void setCaseId(long caseId) {
		this.caseId = caseId;
	}
	public Integer getDepartment() {
		return department;
	}
	public void setDepartment(Integer department) {
		this.department = department;
	}
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	public String getWardNumber() {
		return wardNumber;
	}
	public void setWardNumber(String wardNumber) {
		this.wardNumber = wardNumber;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	
	public String getMake() {
		return make;
	}
	public void setMake(String make) {
		this.make = make;
	}
	public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	public Integer getOt() {
		return ot;
	}
	public void setOt(Integer ot) {
		this.ot = ot;
	}
	public String getOtName() {
		return otName;
	}
	public void setOtName(String otName) {
		this.otName = otName;
	}
	/*public int getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(int deviceId) {
		this.deviceId = deviceId;
	}*/
	public boolean isDeviceStatus() {
		return deviceStatus;
	}
	public void setDeviceStatus(boolean deviceStatus) {
		this.deviceStatus = deviceStatus;
	}
	
	public boolean getTransportData() {
		return transportData;
	}
	public void setTransportData(boolean transportData) {
		this.transportData = transportData;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Date getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	public Date getUpdatedTime() {
		return updatedTime;
	}
	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}
	public String getMedicine() {
		return medicine;
	}
	public void setMedicine(String medicine) {
		this.medicine = medicine;
	}
	

}
