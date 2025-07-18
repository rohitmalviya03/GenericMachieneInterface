/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package model;

import com.cdac.common.GeneratedEntities.Patient;
import com.cdac.common.GeneratedEntities.Patientcase;

public class PatientSession
{
	private final static PatientSession instance = new PatientSession();

	public static PatientSession getInstance()
	{
		return instance;
	}


	private Patient patient = new Patient();
	private Patientcase patientCase = new Patientcase();

	public Patient getPatient()
	{
		return patient;
	}

	public void setPatient(Patient patient)
	{
		this.patient = patient;
	}

	public Patientcase getPatientCase()
	{
		return patientCase;
	}

	public void setPatientCase(Patientcase patientCase)
	{
		this.patientCase = patientCase;
	}

}
