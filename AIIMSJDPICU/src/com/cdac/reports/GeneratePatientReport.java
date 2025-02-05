/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package com.cdac.reports;

import java.io.File;
import java.util.List;

import com.cdac.common.GeneratedEntities.Patient;
import com.cdac.common.GeneratedEntities.Patientcase;

import application.Main;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.AnchorPane;
import mediatorPattern.ControllerMediator;
import model.ChartImageModel;
import model.PatientSession;
import services.GetPatientDetailsForReportService;

public class GeneratePatientReport {

	private GetPatientDetailsForReportService getPatientDetailsForReportService;

	private Patient patient;
	private Patientcase patientCase;
	private EventHandler<WorkerStateEvent> getPatientDetailsForReportServiceSuccessHandler;
	private EventHandler<WorkerStateEvent> getPatientDetailsForReportServiceFailedHandler;

	public void getPatientDetails() {

		if (PatientSession.getInstance().getPatient() != null)
			patient = PatientSession.getInstance().getPatient();
		if (PatientSession.getInstance().getPatientCase() != null)
			patientCase = PatientSession.getInstance().getPatientCase();
		getPatientDetailsForReportService = GetPatientDetailsForReportService.getInstance(patient.getPatientId(),
				patientCase.getCaseId());
		getPatientDetailsForReportService.restart();


		getPatientDetailsForReportServiceSuccessHandler = new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent event) {



				AnchorPane disablePane = ControllerMediator.getInstance().getMainController().getDisablePaneMainContent();
				disablePane.setVisible(false);
				ControllerMediator.getInstance().GetSnapshotController().closePopup();
				AnchorPane disablePaneSnapShot= ControllerMediator.getInstance().GetSnapshotController().getDisablePane();
				disablePaneSnapShot.setVisible(false);

				System.out.println("Report Genarated Successfully");
				Main.getInstance().getUtility().showNotification("Information", "Success",
						"Report generated successfully");
				List<ChartImageModel> imageList = ControllerMediator.getInstance().getMainController()
						.getDrawTabbedCenterForSnapshot().getImageListForReport();
				if(imageList!=null && imageList.size()!=0){
				for(ChartImageModel obj:imageList){
					if(obj.getAnesthesiaImagePath()!=null){
					File anesthesiaImage = new File(obj.getAnesthesiaImagePath());
					if(anesthesiaImage.exists()){
						anesthesiaImage.delete();
					}
					}
					if(obj.getS5MonitorImagePath()!=null){
					File patientMotitorImage = new File(obj.getS5MonitorImagePath());
					if(patientMotitorImage.exists()){
						patientMotitorImage.delete();
					}

					}


				}
				}
				ControllerMediator.getInstance().getMainController()
				.getDrawTabbedCenterForSnapshot().setImageListForReport(null);
				getPatientDetailsForReportService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
						getPatientDetailsForReportServiceSuccessHandler);
				getPatientDetailsForReportService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
						getPatientDetailsForReportServiceFailedHandler);
			}

		};

		getPatientDetailsForReportService.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
				getPatientDetailsForReportServiceSuccessHandler);

		getPatientDetailsForReportServiceFailedHandler = new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent event) {

				if(getPatientDetailsForReportService.getException().getMessage().contains("The process cannot access the file because it is being used by another process")){

				Main.getInstance().getUtility().warningDialogue("Information", "Patient Report is already open.Please close it and Generate Report agian.");

				}else{
				Main.getInstance().getUtility().showNotification("Error", "Error",
						getPatientDetailsForReportService.getException().getMessage());
				}


				ControllerMediator.getInstance().GetSnapshotController().closePopup();
				AnchorPane disablePaneSnapShot= ControllerMediator.getInstance().GetSnapshotController().getDisablePane();
				disablePaneSnapShot.setVisible(false);



				List<ChartImageModel> imageList = ControllerMediator.getInstance().getMainController()
						.getDrawTabbedCenterForSnapshot().getImageListForReport();
				if(imageList!=null && imageList.size()!=0){
				for(ChartImageModel obj:imageList){
					if(obj.getAnesthesiaImagePath()!=null){

					File anesthesiaImage = new File(obj.getAnesthesiaImagePath());

					if(anesthesiaImage.exists()){
						anesthesiaImage.delete();
					}

					}
					if(obj.getS5MonitorImagePath()!=null){
					File patientMotitorImage = new File(obj.getS5MonitorImagePath());
					if(patientMotitorImage.exists()){
						patientMotitorImage.delete();
					}
					}


				}
				}
				ControllerMediator.getInstance().getMainController()
				.getDrawTabbedCenterForSnapshot().setImageListForReport(null);
				getPatientDetailsForReportService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
						getPatientDetailsForReportServiceSuccessHandler);
				getPatientDetailsForReportService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
						getPatientDetailsForReportServiceFailedHandler);
			}

		};

		getPatientDetailsForReportService.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
				getPatientDetailsForReportServiceFailedHandler);

	}


}
