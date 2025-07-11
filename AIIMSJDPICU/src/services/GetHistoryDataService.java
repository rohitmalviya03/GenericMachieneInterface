/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package services;

import java.util.Date;
import java.util.List;

import com.cdac.common.GeneratedEntities.AnethesiaMachineData;
import com.cdac.common.GeneratedEntities.Fluidvalue;
import com.cdac.common.GeneratedEntities.IntraopMedicationlog;
import com.cdac.common.GeneratedEntities.IntraopOutput;
import com.cdac.common.GeneratedEntities.PatientMonitorData;
import com.cdac.common.pojoClasses.InvestigationMapperAndValue;
import com.cdac.common.util.DAOFactory;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import model.UserSession;
public class GetHistoryDataService extends Service<Void>
{
	private List<PatientMonitorData> patientMonitorDataList;
	private List<AnethesiaMachineData> anethesiaMachineDataList;
	private List<IntraopMedicationlog> allMediactionsList;
	private List<IntraopOutput> listOfOutputRecords;
	private List<Fluidvalue> fluidMapDB;
	private static long caseId;
	private static Date startTime;
	private static Date endTime;
	private List<InvestigationMapperAndValue> testHistoryList;

	public List<PatientMonitorData> getPatientMonitorDataList() {
		return patientMonitorDataList;
	}

	public List<AnethesiaMachineData> getAnethesiaMachineDataList() {
		return anethesiaMachineDataList;
	}

	public List<IntraopMedicationlog> getAllMediactionsList()
	{
		return allMediactionsList;
	}

	public List<IntraopOutput> getListOfOutputRecords() {
		return listOfOutputRecords;
	}

	public List<Fluidvalue> getFluidMapDB()
	{
		return fluidMapDB;
	}

	public List<InvestigationMapperAndValue> getTestHistoryList() {
		return testHistoryList;
	}

	private static GetHistoryDataService instance = null;
	public static GetHistoryDataService getInstance(long caseIDVal, Date startTimeVal, Date endTimeVal) {
		if (instance == null) {
			instance = new GetHistoryDataService();
		}
		caseId = caseIDVal;
		startTime = startTimeVal;
		endTime = endTimeVal;

		return instance;
	}

	private GetHistoryDataService() {
		super();
	}

	@Override
	protected Task<Void> createTask()
	{
		return new Task<Void>()
		{

			@Override
			protected Void call() throws Exception
			{
				try
				{
					System.out.println("--");
					patientMonitorDataList=	DAOFactory.patientMonitorData().getTimedPatientMonitorData(caseId, startTime, endTime, UserSession.getInstance().getUserWithrRolesForUserAuthentication().getUserID());
					anethesiaMachineDataList=	DAOFactory.anethesiaMachineData().getTimedAnethesiaMachineData(caseId, startTime, endTime, UserSession.getInstance().getUserWithrRolesForUserAuthentication().getUserID());
					allMediactionsList = DAOFactory.medicationLog().getTimedMedicationsForPatientCase(caseId, startTime, endTime,
					        UserSession.getInstance().getUserWithrRolesForUserAuthentication().getUserID());
					listOfOutputRecords = DAOFactory.getOutputdetails().fetchTimedOutputList(caseId, startTime, endTime, UserSession.getInstance().getUserWithrRolesForUserAuthentication().getUserID());
					fluidMapDB = DAOFactory.getFluidList().viewTimedFluid(caseId, startTime, endTime, UserSession.getInstance().getUserWithrRolesForUserAuthentication().getUserID()).get("List of fluids");
					testHistoryList = DAOFactory.getInvestigationValueView().getInvestigationvaluecasemapperlist(caseId, startTime, endTime, UserSession.getInstance().getUserWithrRolesForUserAuthentication().getUserID());
				}
				catch (Exception e)
				{
					throw e;
				}

				return null;
			}

		};
	}

}
