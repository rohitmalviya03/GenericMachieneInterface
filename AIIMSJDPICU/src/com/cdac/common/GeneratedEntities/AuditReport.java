/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package com.cdac.common.GeneratedEntities;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@SuppressWarnings("serial")
@Entity
@Table(name = "auditreport", catalog = "intraop_dev")
public class AuditReport implements java.io.Serializable {

	private Integer auditReportID;
	private Long caseID;
	private String hemogram;
	private String coagulogram;
	private String biochemistry;
	private String abg;
	private String cxr;
	private String catheterisation;
	private String tmt;
	private String stressThallium;
	private String pulmonaryFunctionTest;
	private String comorbidities;
	private String ecg;
	private Float axcTime;
	private Float cpbTime;
	private Float anesthesiaTime;
	private Float surgeryTime;
	private Float handOverTime;
	private Float patientPreparationTime;
	private Float shiftingOutTime;
	//changes made by vivek
	private Float otTime;
	//changes end
	private Float medPropofol;
	private Float medEaca;

	private Float medFentanyl;
	private Float medKetamine;
	private Float medVecuronium;
	private Float medCefuroxime;
	private Float medHeparin;
	private Float medProtamine;
	private Float fluidNormalSaline;
	private Float fluidRingerLactate;
	private String createdBy;
	private Date createdTime;
	private String updatedBy;
	private Date updatedTime;

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "AuditReportID", unique = true, nullable = false)
	public Integer getauditReportID() {
		return auditReportID;
	}

	public void setauditReportID(Integer auditReportID) {
		this.auditReportID = auditReportID;
	}

	@Column(name = "CaseID")
	public Long getCaseID() {
		return caseID;
	}

	public void setCaseID(Long caseID) {
		this.caseID = caseID;
	}

	@Column(name = "Hemogram")
	public String getHemogram() {
		return hemogram;
	}

	public void setHemogram(String hemogram) {
		this.hemogram = hemogram;
	}

	@Column(name = "Coagulogram")
	public String getCoagulogram() {
		return coagulogram;
	}

	public void setCoagulogram(String coagulogram) {
		this.coagulogram = coagulogram;
	}

	@Column(name = "Biochemistry")
	public String getBiochemistry() {
		return biochemistry;
	}

	public void setBiochemistry(String biochemistry) {
		this.biochemistry = biochemistry;
	}

	@Column(name = "ABG")
	public String getABG() {
		return abg;
	}

	public void setABG(String abg) {
		this.abg = abg;
	}

	@Column(name = "Catheterisation")
	public String getCatheterisation() {
		return catheterisation;
	}

	public void setCatheterisation(String catheterisation) {
		this.catheterisation = catheterisation;
	}

	@Column(name = "StressThallium")
	public String getStressThallium() {
		return stressThallium;
	}

	public void setStressThallium(String stressThallium) {
		this.stressThallium = stressThallium;
	}

	@Column(name = "TMT")
	public String getTMT() {
		return tmt;
	}

	public void setTMT(String tmt) {
		this.tmt = tmt;
	}

	@Column(name = "PulmonaryFunctionTest")
	public String getPulmonaryFunctionTest() {
		return pulmonaryFunctionTest;
	}

	public void setPulmonaryFunctionTest(String pulmonaryFunctionTest) {
		this.pulmonaryFunctionTest = pulmonaryFunctionTest;
	}

	@Column(name = "ECG")
	public String getECG() {
		return ecg;
	}

	public void setECG(String ecg) {
		this.ecg = ecg;
	}

	@Column(name = "CXR")
	public String getCXR() {
		return cxr;
	}

	public void setCXR(String cxr) {
		this.cxr = cxr;
	}

	@Column(name = "Comorbidities")
	public String getComorbidities() {
		return comorbidities;
	}

	public void setComorbidities(String comorbidities) {
		this.comorbidities = comorbidities;
	}

	@Column(name = "AXCTime")
	public Float getAXCTime() {
		return axcTime;
	}

	public void setAXCTime(Float axcTime) {
		this.axcTime = axcTime;
	}

	@Column(name = "CPBTime")
	public Float getCPBTime() {
		return cpbTime;
	}

	public void setCPBTime(Float cpbTime) {
		this.cpbTime = cpbTime;
	}

	@Column(name = "AnesthesiaTime")
	public Float getAnesthesiaTime() {
		return anesthesiaTime;
	}

	public void setAnesthesiaTime(Float anesthesiaTime) {
		this.anesthesiaTime = anesthesiaTime;
	}

	@Column(name = "SurgeryTime")
	public Float getSurgeryTime() {
		return surgeryTime;
	}

	public void setSurgeryTime(Float surgeryTime) {
		this.surgeryTime = surgeryTime;
	}

	@Column(name = "HandOverTime")
	public Float getHandOverTime() {
		return handOverTime;
	}

	public void setHandOverTime(Float handOverTime) {
		this.handOverTime = handOverTime;
	}

	@Column(name = "PatientPreparationTime")
	public Float getPatientPreparationTime() {
		return patientPreparationTime;
	}

	public void setPatientPreparationTime(Float patientPreparationTime) {
		this.patientPreparationTime = patientPreparationTime;
	}

	@Column(name = "ShiftingOutTime")
	public Float getShiftingOutTime() {
		return shiftingOutTime;
	}

	public void setShiftingOutTime(Float shiftingOutTime) {
		this.shiftingOutTime = shiftingOutTime;
	}
	@Column(name = "OTTime")
	public Float getOtTime() {
		return otTime;
	}

	public void setOtTime(Float otTime) {
		this.otTime = otTime;
	}

	@Column(name = "CreatedBy", length = 500)
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

	@Column(name = "UpdatedBy", length = 500)
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
	@Column(name = "MedPropofol")
	public Float getMedPropofol() {
		return medPropofol;
	}

	public void setMedPropofol(Float medPropofol) {
		this.medPropofol = medPropofol;
	}
	@Column(name = "MedEaca")
	public Float getMedEaca() {
		return medEaca;
	}

	public void setMedEaca(Float medEaca) {
		this.medEaca = medEaca;
	}
	@Column(name = "FluidNormalSaline")
	public Float getFluidNormalSaline() {
		return fluidNormalSaline;
	}

	public void setFluidNormalSaline(Float fluidNormalSaline) {
		this.fluidNormalSaline = fluidNormalSaline;
	}
	@Column(name = "FluidRingerLactate")
	public Float getFluidRingerLactate() {
		return fluidRingerLactate;
	}

	public void setFluidRingerLactate(Float fluidRingerLactate) {
		this.fluidRingerLactate = fluidRingerLactate;
	}
	@Column(name = "MedFentanyl")
	public Float getMedFentanyl() {
		return medFentanyl;
	}

	public void setMedFentanyl(Float medFentanyl) {
		this.medFentanyl = medFentanyl;
	}
	@Column(name = "MedKetamine")
	public Float getMedKetamine() {
		return medKetamine;
	}

	public void setMedKetamine(Float medKetamine) {
		this.medKetamine = medKetamine;
	}
	@Column(name = "MedVecuronium")
	public Float getMedVecuronium() {
		return medVecuronium;
	}

	public void setMedVecuronium(Float medVecuronium) {
		this.medVecuronium = medVecuronium;
	}
	@Column(name = "MedCefuroxime")
	public Float getMedCefuroxime() {
		return medCefuroxime;
	}

	public void setMedCefuroxime(Float medCefuroxime) {
		this.medCefuroxime = medCefuroxime;
	}
	@Column(name = "MedHeparin")
	public Float getMedHeparin() {
		return medHeparin;
	}

	public void setMedHeparin(Float medHeparin) {
		this.medHeparin = medHeparin;
	}
	@Column(name = "MedProtamine")
	public Float getMedProtamine() {
		return medProtamine;
	}

	public void setMedProtamine(Float medProtamine) {
		this.medProtamine = medProtamine;
	}



}
