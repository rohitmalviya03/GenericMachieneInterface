/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package controllers;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.cdac.common.GeneratedEntities.Fdamedications;

import application.Main;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import mediatorPattern.ControllerMediator;
import model.PatientSession;
import services.GetTotalMedicationVolumeService;
import services.SearchFDAMedicationsService;
import utility.DrawTabbedCenter;

/**
 * SearchMedicineController.java<br>
 * Purpose:This class contains logic to handle Search Medicine screen inputs
 *
 * @author Rohit_Sharma41
 *
 */
public class SearchMedicineController implements Initializable {

	@FXML
	private Button btnClose;

	@FXML
	private TextField txtMedicine;

	@FXML
	private Button btnSave;

	@FXML
	private ListView<String> medSearchList;

	private String selectedSearchVal = "";

	private EventHandler<MouseEvent> btnCloseHandler;

	private EventHandler<MouseEvent> btnSaveHandler;

	private ChangeListener<String> medSearchListChangeListener;

	private ChangeListener<String> txtMedicineChangeListener;

	private ChangeListener<Boolean> txtMedicine2ChangeListener;

	private EventHandler<WorkerStateEvent> searchFDAMedicationsServiceSuccessHandler;

	private EventHandler<WorkerStateEvent> searchFDAMedicationsServiceFailedHandler;

	private EventHandler<WorkerStateEvent> getTotalVolumeServiceSuccessHandler;

	private EventHandler<WorkerStateEvent> getTotalVolumeServiceFailedHandler;

	private EventHandler<javafx.scene.input.KeyEvent> enterKeyPressEventHandler;

	private static final Logger LOG = Logger.getLogger(SearchMedicineController.class.getName());

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try
		{
			btnCloseHandler = new EventHandler<javafx.scene.input.MouseEvent>()
			{
				public void handle(javafx.scene.input.MouseEvent event)
				{
					try
					{
						closePopup();
					}
					catch (Exception e)
					{
						LOG.error("## Exception occured:", e);
					}
				}
			};
			btnClose.addEventHandler(MouseEvent.MOUSE_CLICKED, btnCloseHandler);
			btnSaveHandler = new EventHandler<javafx.scene.input.MouseEvent>()
			{
				public void handle(javafx.scene.input.MouseEvent event)
				{
					try
					{
						closePopup();
						if (!txtMedicine.getText().trim().isEmpty())
							getTotalMedicationVolume(txtMedicine.getText());
					}
					catch (Exception e)
					{
						LOG.error("## Exception occured:", e);
						Main.getInstance().getUtility().showNotification("Error", "Error", "Something went wrong");

					}
				}
			};
			btnSave.addEventHandler(MouseEvent.MOUSE_CLICKED, btnSaveHandler);

			enterKeyPressEventHandler = new EventHandler<javafx.scene.input.KeyEvent>() {

				@Override
				public void handle(javafx.scene.input.KeyEvent event) {

					if (event.getCode().equals(KeyCode.ENTER)) {

						try
						{
							closePopup();
							if (!txtMedicine.getText().trim().isEmpty())
								getTotalMedicationVolume(txtMedicine.getText());
						}
						catch (Exception e)
						{
							LOG.error("## Exception occured:", e);
							Main.getInstance().getUtility().showNotification("Error", "Error", "Something went wrong");

						}

					}
				}
			};
			txtMedicine.addEventHandler(javafx.scene.input.KeyEvent.KEY_RELEASED, enterKeyPressEventHandler);
			medSearchListChangeListener = new ChangeListener<String>()
			{

				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
				{
					try
					{
						if (newValue != null)
						{

							selectedSearchVal = newValue;
							txtMedicine.setText(selectedSearchVal);
							medSearchList.setVisible(false);

						}
					}
					catch (Exception e)
					{
						LOG.error("## Exception occured:", e);
					}
				}

			};
			// ---show selected medicationSearchList item on lblSelectedMed
			// label
			medSearchList.getSelectionModel().selectedItemProperty().addListener(medSearchListChangeListener);
			txtMedicineChangeListener = new ChangeListener<String>()
			{

				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
				{
					try
					{
						if (newValue.length() >= 5 && !newValue.isEmpty() && !newValue.equals(""))
						{
							if (!newValue.equalsIgnoreCase(selectedSearchVal))
							{

								medSearchList.getItems().clear();
								medSearchList.getItems().addAll(new ArrayList<>());
								callSearchFDAMEdService(newValue);
							}

						}
						else if (newValue.length() < 5)
						{
							medSearchList.getSelectionModel().clearSelection();
							medSearchList.setVisible(false);
						}
					}
					catch (Exception e)
					{
						LOG.error("## Exception occured:", e);
					}
				}

			};
			txtMedicine.textProperty().addListener(txtMedicineChangeListener);
			txtMedicine2ChangeListener = new ChangeListener<Boolean>()
			{

				@Override
				public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
				{
					try
					{
						if (!newValue)
						{
							medSearchList.setVisible(false);
						}
					}
					catch (Exception e)
					{
						LOG.error("## Exception occured:", e);
					}
				}
			};
			txtMedicine.focusedProperty().addListener(txtMedicine2ChangeListener);
			Platform.runLater(new Runnable()
			{
				@Override
				public void run()
				{
					try
					{
						txtMedicine.requestFocus();
					}
					catch (Exception e)
					{
						LOG.error("## Exception occured:", e);
					}
				}
			});
		}
		catch (Exception e)
		{
			LOG.error("## Exception occured:", e);
		}


	}

	/**
	 * This method closes the popup window
	 */
	private void closePopup()
	{
		removeEventHandlers();
		Main.getInstance().getStageManager().closeSecondaryStage();
	}

	/**
	 * This method remove active event handlers from all the components
	 */
	private void removeEventHandlers() {
		txtMedicine.focusedProperty().removeListener(txtMedicine2ChangeListener);
		txtMedicine.textProperty().removeListener(txtMedicineChangeListener);
		medSearchList.getSelectionModel().selectedItemProperty().removeListener(medSearchListChangeListener);
		btnSave.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnSaveHandler);
		btnClose.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnCloseHandler);
		txtMedicine.removeEventHandler(javafx.scene.input.KeyEvent.KEY_RELEASED, enterKeyPressEventHandler);
	}

	/**
	 * This method call SearchFDAMEdService and fetches list of favorite
	 * medications
	 *
	 * @param searchVal searched string
	 */
	private void callSearchFDAMEdService(String searchVal)
	{
		SearchFDAMedicationsService searchFDAMedicationsService = SearchFDAMedicationsService.getInstance(searchVal);
		searchFDAMedicationsService.restart();



		searchFDAMedicationsServiceSuccessHandler = new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent event) {
				try
				{
					List<String> fdamedicationsSearchList = new ArrayList<>();
					List<Fdamedications> fdamedicationsSearchListDB = new ArrayList<Fdamedications>();
					fdamedicationsSearchListDB = searchFDAMedicationsService.getFdaMedicationsList();
					if (fdamedicationsSearchListDB != null)
					{
						for (Fdamedications obj : fdamedicationsSearchListDB)
						{

							fdamedicationsSearchList.add(obj.getMedicationsName());
						}
						medSearchList.getItems().clear();
						medSearchList.getItems().addAll(fdamedicationsSearchList);
						if(fdamedicationsSearchList.size()!=0){
							medSearchList.setVisible(true);
						}

					}
					searchFDAMedicationsService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
					        searchFDAMedicationsServiceSuccessHandler);
					searchFDAMedicationsService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
					        searchFDAMedicationsServiceFailedHandler);
				}
				catch (Exception e)
				{
					LOG.error("## Exception occured:", e);
				}
			}

		};

		searchFDAMedicationsService.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
				searchFDAMedicationsServiceSuccessHandler);

		searchFDAMedicationsServiceFailedHandler = new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent event) {
				try
				{
					Main.getInstance().getUtility().showNotification("Error", "Error",
					        searchFDAMedicationsService.getException().getMessage());
					searchFDAMedicationsService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
					        searchFDAMedicationsServiceSuccessHandler);
					searchFDAMedicationsService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
					        searchFDAMedicationsServiceFailedHandler);
				}
				catch (Exception e)
				{
					LOG.error("## Exception occured:", e);
				}
			}

		};
	}

	/**
	 * This method fetches total medication volume from database for the passed medicine
	 * @param medName name of the medicine
	 */
	private void getTotalMedicationVolume(String medName)
	{
		GetTotalMedicationVolumeService getTotalVolumeService = GetTotalMedicationVolumeService.getInstance(medName,
				PatientSession.getInstance().getPatient().getPatientId(),
				PatientSession.getInstance().getPatientCase().getCaseId());
		getTotalVolumeService.restart();

		getTotalVolumeServiceSuccessHandler = new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent event) {
				try
				{
					float totalDose = getTotalVolumeService.getTotalVolume().getValue();
					DecimalFormat df = new DecimalFormat("###.##");
					// ControllerMediator.getInstance().getMainController().getDrawTabbedCenter().addNewRow(txtMedicine.getText(),df.format(totalDose)
					// + " mg");
					ControllerMediator.getInstance().getMainController().getDrawTabbedCenter().addNewGridRow(
					        txtMedicine.getText() + "|" + df.format(totalDose) + " mg", DrawTabbedCenter.MED_GRID_NAME);
					getTotalVolumeService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
					        getTotalVolumeServiceSuccessHandler);
					getTotalVolumeService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
					        getTotalVolumeServiceFailedHandler);
				}
				catch (Exception e)
				{
					LOG.error("## Exception occured:", e);
				}
			}

		};

		getTotalVolumeService.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
				getTotalVolumeServiceSuccessHandler);

		getTotalVolumeServiceFailedHandler = new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent event) {
				try
				{
					Main.getInstance().getUtility().showNotification("Error", "Error",
					        getTotalVolumeService.getException().getMessage());
					getTotalVolumeService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
					        getTotalVolumeServiceSuccessHandler);
					getTotalVolumeService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
					        getTotalVolumeServiceFailedHandler);
				}
				catch (Exception e)
				{
					LOG.error("## Exception occured:", e);
				}
			}

		};
	}

}
