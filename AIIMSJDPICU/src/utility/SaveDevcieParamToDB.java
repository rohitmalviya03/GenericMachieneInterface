/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package utility;

import org.apache.log4j.Logger;

import com.cdac.common.GeneratedEntities.AnethesiaMachineData;
import com.cdac.common.GeneratedEntities.PatientMonitorData;

import application.Main;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import mediatorPattern.ControllerMediator;
import services.SaveAnesthesiaDataService;
import services.SavePatientMonitorDataService;

/**
 * SaveDevcieParamToDB.java<br>
 * Purpose: This method contains logic to save data from S5PAtientMonitor and Anesthesia devices to database
 *
 * @author Rohit_Sharma41
 *
 */

public class SaveDevcieParamToDB {

	private EventHandler<WorkerStateEvent> savePatientMonitorDataServiceSuccessHandler;
	private EventHandler<WorkerStateEvent> savePatientMonitorDataServiceFailedHandler;
	private EventHandler<WorkerStateEvent> saveAnesthesiaDataServiceSuccessHandler;
	private EventHandler<WorkerStateEvent> saveAnesthesiaDataServiceFailedHandler;

	private static final Logger LOG = Logger.getLogger(SaveDevcieParamToDB.class.getName());

	/**
	 * This method save S5PAtientMonitor device data to database
	 *
	 * @param obj object possessing S5PAtientMonitor device data
	 * @param showNotification whether save notification need to be displayed or not
	 */
	public void savePtMntrData(PatientMonitorData obj, boolean showNotification)
	{
		SavePatientMonitorDataService savePatientMonitorDataService = SavePatientMonitorDataService.getInstance(obj);
		savePatientMonitorDataService.restart();

		savePatientMonitorDataServiceSuccessHandler = new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent event) {
				try
				{
					if (showNotification)
						Main.getInstance().getUtility().showNotification("Information", "Success",
						        "Patient Monitor data saved successfully!");
					if (ControllerMediator.getInstance().getPatientMonitorController() != null)
						ControllerMediator.getInstance().getPatientMonitorController().hideDisablePane();
					savePatientMonitorDataService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
					        savePatientMonitorDataServiceSuccessHandler);
					savePatientMonitorDataService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
					        savePatientMonitorDataServiceFailedHandler);
				}
				catch (Exception e)
				{
					LOG.error("## Exception occured:", e);

				}
			}

		};

		savePatientMonitorDataService.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
				savePatientMonitorDataServiceSuccessHandler);

		savePatientMonitorDataServiceFailedHandler = new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent event) {
				try
				{
					Main.getInstance().getUtility().showNotification("Error", "Error",
					        savePatientMonitorDataService.getException().getMessage());
					if (ControllerMediator.getInstance().getPatientMonitorController() != null)
						ControllerMediator.getInstance().getPatientMonitorController().hideDisablePane();
					savePatientMonitorDataService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
					        savePatientMonitorDataServiceSuccessHandler);
					savePatientMonitorDataService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
					        savePatientMonitorDataServiceFailedHandler);
				}
				catch (Exception e)
				{
					LOG.error("## Exception occured:", e);
				}
			}

		};

		savePatientMonitorDataService.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
				savePatientMonitorDataServiceFailedHandler);
	}

	/**
	 * This method save Anesthesia device data to database
	 *
	 * @param obj  object possessing Anesthesia device data
	 * @param showNotification whether save notification need to be displayed or not
	 */
	public void saveAnesData(AnethesiaMachineData obj, boolean showNotification) {
		SaveAnesthesiaDataService saveAnesthesiaDataService = SaveAnesthesiaDataService.getInstance(obj);
		saveAnesthesiaDataService.restart();

		saveAnesthesiaDataServiceSuccessHandler = new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent event) {
				try
				{
					if (showNotification)
						Main.getInstance().getUtility().showNotification("Information", "Success",
						        "Anesthesia data saved successfully!");
					if (ControllerMediator.getInstance().getDashboardAnaestheiaController() != null)
						ControllerMediator.getInstance().getDashboardAnaestheiaController().hideDisablePane();
					saveAnesthesiaDataService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
					        saveAnesthesiaDataServiceSuccessHandler);
					saveAnesthesiaDataService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
					        saveAnesthesiaDataServiceFailedHandler);
				}
				catch (Exception e)
				{
					LOG.error("## Exception occured:", e);

				}
			}

		};

		saveAnesthesiaDataService.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
				saveAnesthesiaDataServiceSuccessHandler);

		saveAnesthesiaDataServiceFailedHandler = new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent event) {
				try
				{
					Main.getInstance().getUtility().showNotification("Error", "Error",
					        saveAnesthesiaDataService.getException().getMessage());
					if (ControllerMediator.getInstance().getDashboardAnaestheiaController() != null)
						ControllerMediator.getInstance().getDashboardAnaestheiaController().hideDisablePane();
					saveAnesthesiaDataService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
					        saveAnesthesiaDataServiceSuccessHandler);
					saveAnesthesiaDataService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
					        saveAnesthesiaDataServiceFailedHandler);
				}
				catch (Exception e)
				{
					LOG.error("## Exception occured:", e);
				}
			}

		};

		saveAnesthesiaDataService.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
				saveAnesthesiaDataServiceFailedHandler);

	}
}
