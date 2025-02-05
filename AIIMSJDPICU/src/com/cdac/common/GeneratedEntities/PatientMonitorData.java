/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package com.cdac.common.GeneratedEntities;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
@Entity
@Table(name="patientmonitordata", catalog = "intraop_dev",uniqueConstraints = { @UniqueConstraint(columnNames = "PatientMonitorDataID")})
public class PatientMonitorData {

	private int patientMonitorDataID;
	private Long caseId;
	private String hr;
	private String iBPSys;
	private Deviceconfiguration deviceId;
	private String createdBy;
	private Date createdTime;
	private String updatedBy;
	private Date updatedTime;
	private String iBPDia;
	private String iBPMean;
	private String spO2;
	private String etCO2;
	private String temp1;
	private String temp2;
	private String bis;
	private String MAC;
	private String ETAgent;
	private Date timeStamp;
	private String niBPSys;
	private String niBPDia;
	private String niBPMean;
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "PatientMonitorDataID", length = 11,unique = true, nullable = false)
	public int getPatientMonitorDataID() {
		return patientMonitorDataID;
	}

	public void setPatientMonitorDataID(int patientMonitorDataID) {
		this.patientMonitorDataID = patientMonitorDataID;
	}



	@Column(name="CaseID")
	public Long getCaseId() {
		return caseId;
	}

	public void setCaseId(Long caseId) {
		this.caseId = caseId;
	}

	@Column(name = "HR", length = 50)
	public String getHr() {
		return hr;
	}

	public void setHr(String hr) {
		this.hr = hr;
	}
	@Column(name = "BIS", length = 45)
	public String getBIS() {
		return bis;
	}

	public void setBIS(String bis) {
		this.bis = bis;
	}


	@Column(name = "MAC", length = 45)
	public String getMAC() {
		return MAC;
	}

	public void setMAC(String MAC) {
		this.MAC = MAC;
	}

	@Column(name = "ET_Agent", length = 45)
	public String getETAgent() {
		return ETAgent;
	}

	public void setETAgent(String ET_Agent) {
		this.ETAgent = ET_Agent;
	}


	@Column(name = "IBPSys", length = 50)
	public String getiBPSys() {
		return iBPSys;
	}

	public void setiBPSys(String iBPSys) {
		this.iBPSys = iBPSys;
	}

	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="DeviceID")
	public Deviceconfiguration getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(Deviceconfiguration deviceId) {
		this.deviceId = deviceId;
	}

	@Column(name = "CreatedBy", length = 45)
	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CreatedTime", length = 19)
	public Date getCreatedTime() {
		return this.createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	@Column(name = "UpdatedBy", length = 45)
	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UpdatedTime", length = 19)
	public Date getUpdatedTime() {
		return this.updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	@Column(name = "IBPDia", length = 45)
	public String getiBPDia() {
		return iBPDia;
	}

	public void setiBPDia(String iBPDia) {
		this.iBPDia = iBPDia;
	}
	@Column(name = "IBPMean", length = 45)
	public String getiBPMean() {
		return iBPMean;
	}

	public void setiBPMean(String iBPMean) {
		this.iBPMean = iBPMean;
	}
	@Column(name = "SpO2", length = 45)
	public String getSpO2() {
		return spO2;
	}

	public void setSpO2(String spO2) {
		this.spO2 = spO2;
	}

	@Column(name = "EtCO2", length = 45)
	public String getEtCO2() {
		return etCO2;
	}

	public void setEtCO2(String etCO2) {
		this.etCO2 = etCO2;
	}

	@Column(name = "Temp1", length = 45)
	public String getTemp1() {
		return temp1;
	}

	public void setTemp1(String temp1) {
		this.temp1 = temp1;
	}

	@Column(name = "Temp2", length = 45)
	public String getTemp2() {
		return temp2;
	}

	public void setTemp2(String temp2) {
		this.temp2 = temp2;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TimeStamp", length = 19)
	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}
	@Column(name = "NIBPSYS", length = 45)
	public String getNiBPSys() {
		return niBPSys;
	}

	public void setNiBPSys(String niBPSys) {
		this.niBPSys = niBPSys;
	}
	@Column(name = "NIBPDIA", length = 45)
	public String getNiBPDia() {
		return niBPDia;
	}

	public void setNiBPDia(String niBPDia) {
		this.niBPDia = niBPDia;
	}
	@Column(name = "NIBPMean", length = 45)
	public String getNiBPMean() {
		return niBPMean;
	}

	public void setNiBPMean(String niBPMean) {
		this.niBPMean = niBPMean;
	}
	
	
}


