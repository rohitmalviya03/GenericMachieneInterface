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

@SuppressWarnings("serial")
@Entity
@Table(name = "cpb_details", catalog = "intraop_dev")
public class CPBDetails implements java.io.Serializable {

	private Integer cPBDetailsID;
	private Long caseID;
	private String createdBy;
	private Date createdTime;
	private String updatedBy;
	private Date updatedTime;
	private String primeNS;
	private String primeBlood;
	private String primeAlbumin;
	private String primeMannitol;
	private String primeNaHCO3;
	private String primeCaCl2;
	private String primeHeparin;
	private String primeDetails;
	private String prePrime;
	private String preCrystalloidCPG;
	private String preNS;
	private String preRL;
	private String preBlood;
	private String preNaHCO3;
	private String preDetails;
	private String postIVF;
	private String postBlood;
	private String postFFP;
	private String postPC;
	private String postALB;
	private String postNetBalance;
	private String postDetails;

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "CPBDetailsID", unique = true, nullable = false)
	public Integer getcPBDetailsID() {
		return cPBDetailsID;
	}
	public void setcPBDetailsID(Integer cPBDetailsID) {
		this.cPBDetailsID = cPBDetailsID;
	}
	@Column(name = "CaseID")
	public Long getCaseID() {
		return caseID;
	}
	public void setCaseID(Long caseID) {
		this.caseID = caseID;
	}
	@Column(name = "CreatedBy")
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	@Column(name = "CreatedTime")
	public Date getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	@Column(name = "UpdatedBy")
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	@Column(name = "UpdatedTime")
	public Date getUpdatedTime() {
		return updatedTime;
	}
	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}
	@Column(name = "PrimeNS")
	public String getPrimeNS() {
		return primeNS;
	}
	public void setPrimeNS(String primeNS) {
		this.primeNS = primeNS;
	}
	@Column(name = "PrimeBlood")
	public String getPrimeBlood() {
		return primeBlood;
	}
	public void setPrimeBlood(String primeBlood) {
		this.primeBlood = primeBlood;
	}
	@Column(name = "PrimeAlbumin")
	public String getPrimeAlbumin() {
		return primeAlbumin;
	}
	public void setPrimeAlbumin(String primeAlbumin) {
		this.primeAlbumin = primeAlbumin;
	}
	@Column(name = "PrimeMannitol")
	public String getPrimeMannitol() {
		return primeMannitol;
	}
	public void setPrimeMannitol(String primeMannitol) {
		this.primeMannitol = primeMannitol;
	}
	@Column(name = "PrimeNaHCO3")
	public String getPrimeNaHCO3() {
		return primeNaHCO3;
	}
	public void setPrimeNaHCO3(String primeNaHCO3) {
		this.primeNaHCO3 = primeNaHCO3;
	}
	@Column(name = "PrimeCaCl2")
	public String getPrimeCaCl2() {
		return primeCaCl2;
	}
	public void setPrimeCaCl2(String primeCaCl2) {
		this.primeCaCl2 = primeCaCl2;
	}
	@Column(name = "PrimeHeparin")
	public String getPrimeHeparin() {
		return primeHeparin;
	}
	public void setPrimeHeparin(String primeHeparin) {
		this.primeHeparin = primeHeparin;
	}
	@Column(name = "PrimeDetails")
	public String getPrimeDetails() {
		return primeDetails;
	}
	public void setPrimeDetails(String primeDetails) {
		this.primeDetails = primeDetails;
	}
	@Column(name = "PrePrime")
	public String getPrePrime() {
		return prePrime;
	}
	public void setPrePrime(String prePrime) {
		this.prePrime = prePrime;
	}
	@Column(name = "PreCrystalloidCPG")
	public String getPreCrystalloidCPG() {
		return preCrystalloidCPG;
	}
	public void setPreCrystalloidCPG(String preCrystalloidCPG) {
		this.preCrystalloidCPG = preCrystalloidCPG;
	}
	@Column(name = "PreNS")
	public String getPreNS() {
		return preNS;
	}
	public void setPreNS(String preNS) {
		this.preNS = preNS;
	}
	@Column(name = "PreRL")
	public String getPreRL() {
		return preRL;
	}
	public void setPreRL(String preRL) {
		this.preRL = preRL;
	}
	@Column(name = "PreBlood")
	public String getPreBlood() {
		return preBlood;
	}
	public void setPreBlood(String preBlood) {
		this.preBlood = preBlood;
	}
	@Column(name = "PreNaHCO3")
	public String getPreNaHCO3() {
		return preNaHCO3;
	}
	public void setPreNaHCO3(String preNaHCO3) {
		this.preNaHCO3 = preNaHCO3;
	}
	@Column(name = "PreDetails")
	public String getPreDetails() {
		return preDetails;
	}
	public void setPreDetails(String preDetails) {
		this.preDetails = preDetails;
	}
	@Column(name = "PostIVF")
	public String getPostIVF() {
		return postIVF;
	}
	public void setPostIVF(String postIVF) {
		this.postIVF = postIVF;
	}
	@Column(name = "PostBlood")
	public String getPostBlood() {
		return postBlood;
	}
	public void setPostBlood(String postBlood) {
		this.postBlood = postBlood;
	}
	@Column(name = "PostFFP")
	public String getPostFFP() {
		return postFFP;
	}
	public void setPostFFP(String postFFP) {
		this.postFFP = postFFP;
	}
	@Column(name = "PostPC")
	public String getPostPC() {
		return postPC;
	}
	public void setPostPC(String postPC) {
		this.postPC = postPC;
	}
	@Column(name = "PostALB")
	public String getPostALB() {
		return postALB;
	}
	public void setPostALB(String postALB) {
		this.postALB = postALB;
	}
	@Column(name = "PostNetBalance")
	public String getPostNetBalance() {
		return postNetBalance;
	}
	public void setPostNetBalance(String postNetBalance) {
		this.postNetBalance = postNetBalance;
	}
	@Column(name = "PostDetails")
	public String getPostDetails() {
		return postDetails;
	}
	public void setPostDetails(String postDetails) {
		this.postDetails = postDetails;
	}

}
