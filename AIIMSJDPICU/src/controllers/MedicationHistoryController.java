/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package controllers;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.cdac.common.GeneratedEntities.IntraopMedicationlog;

import application.FxmlView;
import application.Main;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import model.MedicationSession;
import model.PatientSession;
import model.totalMedicationModel;
import services.DeleteMedicationService;
import services.GetAllMedicationsService;
import services.GetMedForUpdateService;

/**
 * MedicationHistoryController.java<br>
 * Purpose: This class contains logic to handle MedicationHistory screen inputs
 *
 * @author Rohit_Sharma41
 *
 */
public class MedicationHistoryController implements Initializable {

	private static final Logger LOGGER = Logger.getLogger(MedicationHistoryController.class.getName());
	@FXML
	private TableView<IntraopMedicationlog> tableMedication;

	@FXML
	private TableColumn<IntraopMedicationlog, String> clmnMedicine;

	@FXML
	private TableColumn<IntraopMedicationlog, String> clmnType;

	@FXML
	private TableColumn<IntraopMedicationlog, String> clmnDose;

	@FXML
	private TableColumn<IntraopMedicationlog, String> clmnVolume;

	/*
	 * @FXML private TableColumn<IntraopMedicationlog, String> clmnUnit;
	 */

	@FXML
	private TableColumn<IntraopMedicationlog, String> clmnRate;

	@FXML
	private TableColumn<IntraopMedicationlog, Date> clmnStartTime;

	@FXML
	private TableColumn<IntraopMedicationlog, Date> clmnEndTime;

	@FXML
	private StackPane stackPaneLoader;

	@FXML
	private Button btnDelete;

	@FXML
	private Button btnUpdate;

	@FXML
	private Button btnClose;

	@FXML
	private TableView<totalMedicationModel> tableTotalDose;

	@FXML
	private TableColumn<totalMedicationModel, String> clmnMedicineName;

	@FXML
	private TableColumn<totalMedicationModel, Float> clmnTotal;

	private int patientId;
	private long caseId;
	private List<IntraopMedicationlog> allMediactionsListDB;
	private IntraopMedicationlog selectedTableRow;
	private EventHandler<MouseEvent> btnDeleteHandler;
	private EventHandler<MouseEvent> btnUpdateHandler;
	private EventHandler<MouseEvent> btnCloseHandler;
	private EventHandler<WorkerStateEvent> getAllMedicationsServiceSuccessHandler;
	private EventHandler<WorkerStateEvent> getAllMedicationsServiceFailedHandler;
	private EventHandler<WorkerStateEvent> deleteMedicationServiceSuccessHandler;
	private EventHandler<WorkerStateEvent> deleteMedicationServiceFailedHandler;
	private EventHandler<WorkerStateEvent> getMedForUpdateServiceSuccessHandler;
	private EventHandler<WorkerStateEvent> getMedForUpdateServiceFailedHandler;
	private ChangeListener<IntraopMedicationlog> tableMedicationChangeListener;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			// ---populating tableView
			clmnMedicine.setCellValueFactory(new PropertyValueFactory<IntraopMedicationlog, String>("medicationName"));
			clmnType.setCellValueFactory(new PropertyValueFactory<IntraopMedicationlog, String>("bolusInfusion"));

			/// changes from sahil.sharma08 start here
			// clmnStartTime.setCellValueFactory(new
			/// PropertyValueFactory<IntraopMedicationlog,
			/// String>("startTime"));
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
			clmnStartTime.setCellValueFactory(new PropertyValueFactory("startTime"));
			clmnStartTime.setCellFactory((TableColumn<IntraopMedicationlog, Date> column) -> {
				return new TableCell<IntraopMedicationlog, Date>() {
					@Override
					protected void updateItem(Date item, boolean empty) {
						super.updateItem(item, empty);
						if (item == null || empty) {
							setText(null);
						} else {
							setText(formatter.format(item));
						}
					}
				};
			});
			/// changes from sahil.sharma08 end here

			/// changes from sahil.sharma08 start here
			// clmnEndTime.setCellValueFactory(new
			/// PropertyValueFactory<IntraopMedicationlog, String>("endTime"));
			// SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy
			/// HH:mm");
			clmnEndTime.setCellValueFactory(new PropertyValueFactory("endTime"));
			clmnEndTime.setCellFactory((TableColumn<IntraopMedicationlog, Date> column) -> {
				return new TableCell<IntraopMedicationlog, Date>() {
					@Override
					protected void updateItem(Date item, boolean empty) {
						super.updateItem(item, empty);
						if (item == null || empty) {
							setText(null);
						} else {
							setText(formatter.format(item));
						}
					}
				};
			});
			/// changes from sahil.sharma08 end here

			clmnDose.setCellValueFactory(new PropertyValueFactory<IntraopMedicationlog, String>("infuseDosage"));
			clmnVolume.setCellValueFactory(new PropertyValueFactory<IntraopMedicationlog, String>("volume"));
			clmnRate.setCellValueFactory(new PropertyValueFactory<IntraopMedicationlog, String>("rateOfInfusion"));
			clmnMedicineName.setCellValueFactory(new PropertyValueFactory<totalMedicationModel, String>("name"));
			clmnTotal.setCellValueFactory(new PropertyValueFactory<totalMedicationModel, Float>("total"));
			tableMedication.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

			tableMedicationChangeListener = new ChangeListener<IntraopMedicationlog>() {

				@Override
				public void changed(ObservableValue<? extends IntraopMedicationlog> observable,
						IntraopMedicationlog oldValue, IntraopMedicationlog newSelection) {
					if (newSelection != null) {
						try
						{
							selectedTableRow = tableMedication.getSelectionModel().getSelectedItem();
							btnDelete.setDisable(false);
							btnUpdate.setDisable(false);
							if (selectedTableRow.isFromDevice())
							{
								btnUpdate.setText("View");
							}
							else
							{
								btnUpdate.setText("Edit");
							}
						}
						catch (Exception e)
						{
							LOGGER.error("## Exception occured:", e);
						}

					}

				}
			};

			tableMedication.getSelectionModel().selectedItemProperty().addListener(tableMedicationChangeListener);

			btnDeleteHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent event) {
					try
					{
						boolean result = Main.getInstance().getUtility().confirmDelete();
						if (result) {
							try {
								callDeleteMedicationService(selectedTableRow.getMedicationLogId());
							} catch (Exception e1) {
								LOGGER.error("## Exception occured:", e1);
							}
						}
					}
					catch (Exception e)
					{
						LOGGER.error("## Exception occured:", e);
					}
				}
			};
			btnDelete.addEventHandler(MouseEvent.MOUSE_CLICKED, btnDeleteHandler);

			btnUpdateHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent event) {
					try {
						callGetMedForUpdateService(selectedTableRow.getMedicationLogId());
					} catch (Exception e1) {
						LOGGER.error("## Exception occured:", e1);
					}
				}
			};
			btnUpdate.addEventHandler(MouseEvent.MOUSE_CLICKED, btnUpdateHandler);

			btnCloseHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent event) {
					try {
						closePopup();
					} catch (Exception e1) {
						LOGGER.error("## Exception occured:", e1);
					}
				}
			};
			btnClose.addEventHandler(MouseEvent.MOUSE_CLICKED, btnCloseHandler);

			// ---get all medications for loaded patient and case
			patientId = PatientSession.getInstance().getPatient().getPatientId();
			caseId = PatientSession.getInstance().getPatientCase().getCaseId();
			try {
				callGetAllMedicationsService(patientId, caseId);
			} catch (Exception e1) {
				LOGGER.error("## Exception occured:", e1);
			}
		} catch (Exception e) {
			LOGGER.error("## Exception occured:" + e.getMessage());
			Main.getInstance().getUtility().showNotification("Error", "Error", "Something went wrong");
		}

	}

	/**
	 * This method remove active event handlers from all the components
	 */
	private void removeEventHandlers() {
		tableMedication.getSelectionModel().selectedItemProperty().removeListener(tableMedicationChangeListener);
		btnDelete.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnDeleteHandler);
		btnUpdate.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnUpdateHandler);
		btnClose.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnCloseHandler);
	}

	/**
	 * This method closes MedicationHistory screen
	 */
	private void closePopup() throws Exception
	{
		try {
			removeEventHandlers();
//			Stage stage = Main.getInstance().getChildStage();
//			stage.close();
			Main.getInstance().getStageManager().closeSecondaryStage();
		} catch (Exception e) {
			LOGGER.error("## Exception occured:" + e.getMessage());
		}

	}

	/**
	 * This method call GetAllMedicationsService and fetches list of all
	 * medications for patientId and caseId
	 *
	 * @param patientId ID for logged in patient
	 * @param caseId ID for loaded case
	 */
	private void callGetAllMedicationsService(int patientId, long caseId) throws Exception
	{
		try {
			GetAllMedicationsService getAllMedicationsService = GetAllMedicationsService.getInstance(patientId, caseId);
			getAllMedicationsService.restart();

			getAllMedicationsServiceSuccessHandler = new EventHandler<WorkerStateEvent>() {

				@Override
				public void handle(WorkerStateEvent event) {
					try
					{
						allMediactionsListDB = getAllMedicationsService.getAllMediactionsList();
						stackPaneLoader.setVisible(false);

						if (allMediactionsListDB != null)
						{
							tableMedication.getItems().clear();
							tableMedication.getItems().addAll(allMediactionsListDB);
							btnDelete.setDisable(true);
							btnUpdate.setDisable(true);

							List<totalMedicationModel> medicationTotalDoseList = new ArrayList<totalMedicationModel>();

							Boolean isExist = false;

							totalMedicationModel totalDoseObj;

							for (IntraopMedicationlog medicationHistoryObj : allMediactionsListDB)
							{
								isExist = false;
								totalDoseObj = new totalMedicationModel();
								if (medicationTotalDoseList != null)
								{

									for (totalMedicationModel obj : medicationTotalDoseList)
									{

										if (medicationHistoryObj.getMedicationName().equalsIgnoreCase(obj.getName()))
										{
											isExist = true;
											break;
										}
									}
								}
								if (!isExist)
								{
									totalDoseObj.setName(medicationHistoryObj.getMedicationName());
									medicationTotalDoseList.add(totalDoseObj);
								}

							}
							Float total = 0f;
							for (totalMedicationModel obj : medicationTotalDoseList)
							{
								total = 0f;

								for (IntraopMedicationlog medicationHistoryObj : allMediactionsListDB)
								{
									if (obj.getName().equalsIgnoreCase(medicationHistoryObj.getMedicationName()))
									{
										if (medicationHistoryObj.getInfuseDosage() != null)
										{
											if (medicationHistoryObj.getDosageUnit().equalsIgnoreCase("mg"))
											{
												total = total + medicationHistoryObj.getInfuseDosage();
											}
											else if (medicationHistoryObj.getDosageUnit().equalsIgnoreCase("g"))
											{
												total = total + medicationHistoryObj.getInfuseDosage() * 1000;
											}
											else
											{
												total = total + (medicationHistoryObj.getInfuseDosage() / 1000);
											}

										}

									}

								}

								obj.setTotal(total);
							}
							tableTotalDose.getItems().clear();
							tableTotalDose.getItems().addAll(medicationTotalDoseList);

						}
						getAllMedicationsService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
						        getAllMedicationsServiceSuccessHandler);
						getAllMedicationsService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
						        getAllMedicationsServiceFailedHandler);
					}
					catch (Exception e)
					{
						LOGGER.error("## Exception occured:", e);
					}
				}

			};

			getAllMedicationsService.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
					getAllMedicationsServiceSuccessHandler);

			getAllMedicationsServiceFailedHandler = new EventHandler<WorkerStateEvent>() {

				@Override
				public void handle(WorkerStateEvent event) {
					try
					{
						getAllMedicationsService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
						        getAllMedicationsServiceSuccessHandler);
						getAllMedicationsService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
						        getAllMedicationsServiceFailedHandler);
					}
					catch (Exception e)
					{
						LOGGER.error("## Exception occured:", e);
					}
				}

			};

			getAllMedicationsService.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
					getAllMedicationsServiceFailedHandler);
		} catch (Exception e) {
			LOGGER.error("## Exception occured:" + e.getMessage());
		}

	}

	/**
	 * This method call DeleteMedicationService and deletes medication with
	 * provided medId
	 *
	 * @param medId medication ID
	 */
	private void callDeleteMedicationService(long medId) throws Exception
	{
		try {
			DeleteMedicationService deleteMedicationService = DeleteMedicationService.getInstance(medId);
			deleteMedicationService.restart();

			deleteMedicationServiceSuccessHandler = new EventHandler<WorkerStateEvent>() {

				@Override
				public void handle(WorkerStateEvent event) {
					try
					{
						String result = deleteMedicationService.getResult();
						if (result.equalsIgnoreCase("Medication Log Deleted"))
						{
							stackPaneLoader.setVisible(true);
							Main.getInstance().getUtility().showNotification("Information", "Success",
							        "Medication deleted successfully!");
							try
							{
								callGetAllMedicationsService(patientId, caseId);
							}
							catch (Exception e1)
							{
								LOGGER.error("## Exception occured:", e1);
							}
						}
						deleteMedicationService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
						        deleteMedicationServiceSuccessHandler);
						deleteMedicationService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
						        deleteMedicationServiceFailedHandler);
					}
					catch (Exception e)
					{
						LOGGER.error("## Exception occured:", e);
					}
				}

			};

			deleteMedicationService.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
					deleteMedicationServiceSuccessHandler);

			deleteMedicationServiceFailedHandler = new EventHandler<WorkerStateEvent>() {

				@Override
				public void handle(WorkerStateEvent event) {
					try
					{
						deleteMedicationService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
						        deleteMedicationServiceSuccessHandler);
						deleteMedicationService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
						        deleteMedicationServiceFailedHandler);
					}
					catch (Exception e)
					{
						LOGGER.error("## Exception occured:", e);
					}
				}

			};

			deleteMedicationService.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
					deleteMedicationServiceFailedHandler);
		} catch (Exception e) {
			LOGGER.error("## Exception occured:" + e.getMessage());
		}

	}

	/**
	 * This method call GetMedForUpdateService and fetches medication object
	 * corresponding to medId for update
	 *
	 * @param medId  medication ID
	 */
	private void callGetMedForUpdateService(long medId) throws Exception
	{
		try {
			GetMedForUpdateService getMedForUpdateService = GetMedForUpdateService.getInstance(medId);
			getMedForUpdateService.restart();

			getMedForUpdateServiceSuccessHandler = new EventHandler<WorkerStateEvent>() {

				@Override
				public void handle(WorkerStateEvent event) {
					try
					{
						MedicationSession.getInstance()
						        .setIntraopMedicationlog(getMedForUpdateService.getMedicationLog());

						// navigateToMedicationController();
						try
						{
							closePopup();
						}
						catch (Exception e1)
						{
							LOGGER.error("## Exception occured:", e1);
						}
						// ControllerMediator.getInstance().getMainController().bindPopupFxml(FxmlView.MEDICATION);
						Main.getInstance().getStageManager().switchSceneAfterMain(FxmlView.MEDICATION);
						getMedForUpdateService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
						        getMedForUpdateServiceSuccessHandler);
						getMedForUpdateService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
						        getMedForUpdateServiceFailedHandler);
					}
					catch (Exception e)
					{
						LOGGER.error("## Exception occured:", e);
					}
				}

			};

			getMedForUpdateService.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
					getMedForUpdateServiceSuccessHandler);

			getMedForUpdateServiceFailedHandler = new EventHandler<WorkerStateEvent>() {

				@Override
				public void handle(WorkerStateEvent event) {
					try
					{
						getMedForUpdateService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
						        getMedForUpdateServiceSuccessHandler);
						getMedForUpdateService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
						        getMedForUpdateServiceFailedHandler);
					}
					catch (Exception e)
					{
						LOGGER.error("## Exception occured:", e);
					}
				}

			};

			getMedForUpdateService.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
					getMedForUpdateServiceFailedHandler);
		} catch (Exception e) {
			LOGGER.error("## Exception occured:" + e.getMessage());
		}
	}

	/**
	 * This method navigates to Medication Screen
	 */
	// public void navigateToMedicationController() throws Exception {
	// try {
	// Main.getInstance().setLastFxmlScreen("MedicationHistory");
	//
	// Pane pane =
	// FXMLLoader.load(getClass().getResource("/View/Medication.fxml"));
	//
	// Scene scene = Main.getInstance().getParentScene();
	// StackPane root = (StackPane) scene.getRoot();
	// root.getChildren().add(pane);
	// root.getChildren().get(2).setVisible(false);
	// Stage stage = Main.getInstance().getParentStage();
	// stage.setScene(scene);
	// stage.show();
	// } catch (Exception e) {
	// LOGGER.error("## Exception occured:" + e.getMessage());
	// Main.getInstance().getUtility().showNotification("Error", "Error",
	// "Something went wrong");
	// }
	// }

}
