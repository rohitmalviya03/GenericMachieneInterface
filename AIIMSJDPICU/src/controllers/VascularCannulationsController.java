/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.cdac.common.GeneratedEntities.Cannulations;
import com.cdac.common.GeneratedEntities.Patientcase;
import com.cdac.common.util.Validations;

import application.Main;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import model.MasterDataSession;
import model.PatientSession;
import services.CreateCannulationService;
import services.GetCannulationService;

/**
 * VascularCannulationsController.java<br>
 * Purpose : This class contains code to handle inputs from Vascular
 * Cannulations screen
 *
 * @author Rohit_Sharma41
 *
 */
public class VascularCannulationsController implements Initializable {

	private static final Logger LOGGER = Logger.getLogger(VascularCannulationsController.class.getName());

	@FXML
	private Button btnClose;

	@FXML
	private ChoiceBox<String> choiceSite;

	@FXML
	private ChoiceBox<String> choiceVein;

	@FXML
	private ChoiceBox<String> choiceVeinDetails;

	@FXML
	private TextField txtSize;

	@FXML
	private TextField txtLength;

	@FXML
	private TextArea txtAreaOtherDetails;

	@FXML
	private TextField txtArterialSite;

	@FXML
	private TextField txtArterialSize;

	@FXML
	private TextArea txtAreaArterialDetails;

	@FXML
	private TextField txtPeripheralSite;

	@FXML
	private TextField txtPeripheralSize;

	@FXML
	private TextArea txtAreaPeripheralDetails;

	@FXML
	private TextField txtPulmonarySite;

	@FXML
	private TextField txtPulmonarySize;

	@FXML
	private TextArea txtAreaPulmonaryDetails;

	@FXML
	private ChoiceBox<String> choicePatientPosition;

	@FXML
	private VBox vbComplications;

	@FXML
	private Label lblError;

	@FXML
	private Button btnSave;

	@FXML
	private AnchorPane disablePaneMainContent;

    @FXML
    private ChoiceBox<String> choiceCentralUltraSoundBlind;

    @FXML
    private ChoiceBox<String> choiceArterialUltraSoundBlind;

    @FXML
    private ChoiceBox<String> choicePeripheralUltraSoundBlind;

    @FXML
    private TextField txtInsertedBy;


	private Patientcase patientCase;
	private Long cannulationsId;
	private List<String> complicationListDropDown;
	private List<String> subclavianVeinDetailList;
	private List<String> jugularVeinDetailList;
	private StringBuilder selectedComplications = new StringBuilder("");
	private ChangeListener<String> choiceVeinChangeListener;
	private EventHandler<MouseEvent> btnSaveHandler;
	private EventHandler<MouseEvent> btnCloseHandler;

	private EventHandler<WorkerStateEvent> createCannulationServiceSuccessHandler;

	private EventHandler<WorkerStateEvent> createCannulationServiceFailedHandler;

	private EventHandler<WorkerStateEvent> getCannulationServiceSuccessHandler;

	private EventHandler<WorkerStateEvent> getCannulationServiceFailedHandler;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {

			List<String> listUltrasoundBlind = new ArrayList<String>();
			listUltrasoundBlind.add("Select");
			listUltrasoundBlind.add("Ultrasound");
			listUltrasoundBlind.add("Blind");
			choiceCentralUltraSoundBlind.getItems().clear();
			choiceArterialUltraSoundBlind.getItems().clear();
			choicePeripheralUltraSoundBlind.getItems().clear();
			choiceCentralUltraSoundBlind.getItems().addAll(listUltrasoundBlind);
			choiceArterialUltraSoundBlind.getItems().addAll(listUltrasoundBlind);
			choicePeripheralUltraSoundBlind.getItems().addAll(listUltrasoundBlind);
			choiceCentralUltraSoundBlind.getSelectionModel().select("Select");
			choiceArterialUltraSoundBlind.getSelectionModel().select("Select");
			choicePeripheralUltraSoundBlind.getSelectionModel().select("Select");


			if (PatientSession.getInstance().getPatientCase() != null)
				patientCase = PatientSession.getInstance().getPatientCase();

			List<String> siteList = new ArrayList<String>();// FXCollections.observableArrayList("Select",
															// "Left", "Right");
			siteList.add("Select");
			siteList.add("Left");
			siteList.add("Right");
			choiceSite.getItems().clear();
			choiceSite.getItems().addAll(siteList);
			choiceSite.setValue("Select");
			// radioArterial.setUserData("Arterial");
			// radioCentral.setUserData("Central");
			// radioArterial.setToggleGroup(type);
			// radioCentral.setToggleGroup(type);
			// choiceSite.getSelectionModel().select(0);

			choiceVeinChangeListener = new ChangeListener<String>() {

				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					if (newValue != null) {
						try {
							choiceVeinDetails.setDisable(false);
							if (choiceVein.getValue().equalsIgnoreCase("Subclavian vein")) {
								// choiceVeinDetails.setItems(subclavianVeinDetailList);
								choiceVeinDetails.getItems().clear();
								choiceVeinDetails.getItems().addAll(subclavianVeinDetailList);
							} else if (choiceVein.getValue().equalsIgnoreCase("Internal jugular vein")) {
								// choiceVeinDetails.setItems(jugularVeinDetailList);
								choiceVeinDetails.getItems().clear();
								choiceVeinDetails.getItems().addAll(jugularVeinDetailList);
							} else {
								choiceVeinDetails.setDisable(true);
							}
							choiceVeinDetails.setValue("Select");
							// choiceVeinDetails.getSelectionModel().select(0);
						} catch (Exception e) {
							LOGGER.error("## Exception occured:", e);

						}

					}
				}
			};

			choiceVein.getSelectionModel().selectedItemProperty().addListener(choiceVeinChangeListener);

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

			btnSaveHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent event) {
					try {
						complicationSelect();
						saveCannulation();
					} catch (Exception e1) {
						LOGGER.error("## Exception occured:", e1);
					}
				}
			};
			btnSave.addEventHandler(MouseEvent.MOUSE_CLICKED, btnSaveHandler);

			try {
				fetchMasterDataFromSession();
				// fetchMasterData();
			} catch (Exception e1) {
				LOGGER.error("## Exception occured:", e1);
			}
		} catch (Exception e) {
			LOGGER.error("## Exception occured:", e);

		}
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				try {
					choicePatientPosition.requestFocus();
				} catch (Exception e) {
					LOGGER.error("## Exception occured:", e);

				}
			}
		});

	}

	/**
	 * This method closes Fluid screen
	 */
	private void closePopup() {
		try {
			removeEventHandlers();
			Main.getInstance().getStageManager().closeSecondaryStage();
		} catch (Exception e) {
			LOGGER.error("## Exception occured:", e);
		}
	}

	/**
	 * This method fetch data lists from database for major dropdowns
	 * (speciality, operationTheater,surgeons, anesthesiologist)
	 */

	// public void fetchMasterData() throws Exception{
	// LOGGER.debug("Logger Name: " + LOGGER.getName());
	// try {
	// masterDataService = new GetMasterDataService();
	// masterDataService.start();
	//
	// masterDataService.setOnSucceeded(e -> {
	// System.out.println("javafx service was successfull----->>>");
	//
	// Map<String, List<Entityvalues>> entityDetailsList = new HashMap<String,
	// List<Entityvalues>>();
	// entityDetailsList = masterDataService.getEntityDetailsList();
	//
	// if (entityDetailsList != null) {
	// for (Map.Entry<String, List<Entityvalues>> entry :
	// entityDetailsList.entrySet()) {
	//
	// String mapKey = entry.getKey();
	// List<Entityvalues> mapValue = entry.getValue();
	//
	// System.out.println("Key-->" + mapKey);
	// for (Entityvalues obj : mapValue) {
	// System.out.println("Id -- " + obj.getEntityValueId() + " , Value --" +
	// obj.getEntityValue());
	// }
	//
	// if (mapKey.equalsIgnoreCase(ENTITY_COMPLICATION)) {
	// complicationList = mapValue;
	// } else if (mapKey.equalsIgnoreCase(ENTITY_PATIENTPOSITION)) {
	// patientPositionList = mapValue;
	// } else if (mapKey.equalsIgnoreCase(ENTITY_VEIN)) {
	// entityVeinList = mapValue;
	// } else if (mapKey.equalsIgnoreCase(ENTITY_SUBCLAVIAN_VEIN)) {
	// entitySubclavianVeilList = mapValue;
	// } else if (mapKey.equalsIgnoreCase(ENTITY_JAGULAR_VEIN)) {
	// entityJugularVeinList = mapValue;
	// }
	//
	// }
	//
	// }
	// complicationListDropDown = FXCollections.observableArrayList();
	// if (complicationList != null) {
	// for (Entityvalues obj : complicationList) {
	// if (obj != null) {
	// if (obj.getEntityValue() != null && !obj.getEntityValue().isEmpty())
	// complicationListDropDown.add(obj.getEntityValue());
	//
	// }
	// }
	// }
	// patientPositionListDropDown = FXCollections.observableArrayList();
	// patientPositionListDropDown.add("Select");
	// if (patientPositionList != null) {
	// for (Entityvalues obj : patientPositionList) {
	// if (obj != null) {
	// if (obj.getEntityValue() != null && !obj.getEntityValue().isEmpty())
	// patientPositionListDropDown.add(obj.getEntityValue());
	//
	// }
	// }
	// }
	// veinList = FXCollections.observableArrayList();
	// veinList.add("Select");
	// if (entityVeinList != null) {
	// for (Entityvalues obj : entityVeinList) {
	// if (obj != null) {
	// if (obj.getEntityValue() != null && !obj.getEntityValue().isEmpty())
	// veinList.add(obj.getEntityValue());
	//
	// }
	// }
	// }
	// subclavianVeinDetailList = FXCollections.observableArrayList();
	// subclavianVeinDetailList.add("Select");
	// if (entitySubclavianVeilList != null) {
	// for (Entityvalues obj : entitySubclavianVeilList) {
	// if (obj != null) {
	// if (obj.getEntityValue() != null && !obj.getEntityValue().isEmpty())
	// subclavianVeinDetailList.add(obj.getEntityValue());
	//
	// }
	// }
	// }
	// jugularVeinDetailList = FXCollections.observableArrayList();
	// jugularVeinDetailList.add("Select");
	// if (entityJugularVeinList != null) {
	// for (Entityvalues obj : entityJugularVeinList) {
	// if (obj != null) {
	// if (obj.getEntityValue() != null && !obj.getEntityValue().isEmpty())
	// jugularVeinDetailList.add(obj.getEntityValue());
	//
	// }
	// }
	// }
	// choicePatientPosition.setItems(patientPositionListDropDown);
	// choicePatientPosition.getSelectionModel().select(0);
	// choiceVein.setItems(veinList);
	// choiceVein.getSelectionModel().select(0);
	// try {
	// populateMultiSelectDropdowns();
	// } catch (Exception e1) {
	// LOGGER.error("## Exception occured:", e1);
	// }
	// try {
	// getCannulation();
	// } catch (Exception e1) {
	// LOGGER.error("## Exception occured:", e1);
	// }
	//
	// });
	//
	// System.out.println("---");
	// masterDataService.setOnFailed(e -> {
	// System.out.println("javafx service was failed----->>>");
	// Main.getInstance().getUtility().showNotification("Error", "Error",
	// masterDataService.getException().getMessage());
	//
	// });
	// }
	// catch (Exception e)
	// {
	// LOGGER.error("## Exception occured:" + e.getMessage());
	// Main.getInstance().getUtility().showNotification("Error",
	// "Error","Something went wrong");
	// }
	// }

	/**
	 * This method fetches master data available in the session
	 */
	private void fetchMasterDataFromSession() {

		try {
			if (MasterDataSession.getInstance().getMasterDataMap() != null) {

				complicationListDropDown = new ArrayList<>(
						MasterDataSession.getInstance().getMasterDataMap().get("CannulationsComplications"));
				// patientPositionListDropDown =
				// MasterDataSession.getInstance().getMasterDataMap().get("Patient
				// Position");
				List<String> patientPositionListDropDown = new ArrayList<>();
				patientPositionListDropDown.add("Select");
				patientPositionListDropDown
						.addAll(MasterDataSession.getInstance().getMasterDataMap().get("Patient Position"));
				List<String> veinList = new ArrayList<>();
				veinList.add("Select");
				veinList.addAll(MasterDataSession.getInstance().getMasterDataMap().get("Cannulation Vein"));
				subclavianVeinDetailList = new ArrayList<>();
				subclavianVeinDetailList.add("Select");
				subclavianVeinDetailList
						.addAll(MasterDataSession.getInstance().getMasterDataMap().get("Subclavian VeinDetail"));
				jugularVeinDetailList = new ArrayList<>();
				jugularVeinDetailList.add("Select");
				jugularVeinDetailList
						.addAll(MasterDataSession.getInstance().getMasterDataMap().get("Jugular VeinDetail"));

				// List<String> lstPP = new
				// ArrayList<>(patientPositionListDropDown);
				// List<String> lstVeins = new ArrayList<>(veinList);

				// choicePatientPosition.getItems().add("Select");
				// lstPP.add(0, "Select");
				choicePatientPosition.getItems().clear();
				choicePatientPosition.getItems().addAll(patientPositionListDropDown);
				choicePatientPosition.setValue("Select");
				// choicePatientPosition.getSelectionModel().select(0);
				// choicePatientPosition.getSelectionModel().select(0);
				// choiceSite.getItems().addAll(siteList);
				// choiceSite.getSelectionModel().select(0);
				// choiceVein.getItems().add("Select");
				// lstVeins.add(0, "Select");
				choiceVein.getItems().clear();
				choiceVein.getItems().addAll(veinList);
				choiceVein.setValue("Select");
				// choiceVein.getSelectionModel().select(0);
				// choiceVein.getSelectionModel().select(0);
				/*
				 * choicePatientPosition.setItems(patientPositionListDropDown);
				 * choicePatientPosition.getSelectionModel().select(0);
				 * choiceSite.setItems(siteList);
				 * choiceSite.getSelectionModel().select(0);
				 * choiceVein.setItems(veinList);
				 * choiceVein.getSelectionModel().select(0);
				 */

				populateMultiSelectDropdowns();
				getCannulation();
			}
		} catch (Exception e) {
			LOGGER.error("## Exception occured:", e);

		}
	}

	/**
	 * This method removes event handlers from all the components
	 */
	private void removeEventHandlers() {
		choiceVein.getSelectionModel().selectedItemProperty().removeListener(choiceVeinChangeListener);
		btnSave.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnSaveHandler);
		btnClose.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnCloseHandler);
		// for (int i = 0; i < complicationListDropDown.size(); i++) {
		// CheckBox CB = (CheckBox) vbComplications.getChildren().get(i);
		// CB.removeEventHandler(MouseEvent.MOUSE_CLICKED, cbHandler);
		// }
		patientCase = null;
		cannulationsId = null;
		complicationListDropDown = null;
		subclavianVeinDetailList = null;
		jugularVeinDetailList = null;
		selectedComplications = null;
		// listMenuComplication = null;
		choiceVeinChangeListener = null;
		btnSaveHandler = null;
		btnCloseHandler = null;
		// cbHandler = null;
		createCannulationServiceSuccessHandler = null;

		createCannulationServiceFailedHandler = null;

		getCannulationServiceSuccessHandler = null;

		getCannulationServiceFailedHandler = null;
	}

	/**
	 * This method popuplates complicationList dropDown
	 */
	private void populateMultiSelectDropdowns() {
		try {
			/*
			 * cbHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
			 * public void handle(javafx.scene.input.MouseEvent event) { try {
			 * complicationSelect(); } catch (Exception e1) {
			 * LOGGER.error("## Exception occured:", e1); } } };
			 */
			for (String item : complicationListDropDown) {
				CheckBox cb = new CheckBox(item);
				vbComplications.getChildren().add(cb);
				// cb.addEventHandler(MouseEvent.MOUSE_CLICKED, cbHandler);
			}
		} catch (Exception e) {
			LOGGER.error("## Exception occured:", e);

		}

	}

	/**
	 * This method alters selection from the complication dropdown
	 */
	private void complicationSelect() {
		try {
			selectedComplications = new StringBuilder("");
			for (int i = 0; i < complicationListDropDown.size(); i++) {
				CheckBox CB = (CheckBox) vbComplications.getChildren().get(i);

				if (CB.isSelected()) {

					if (selectedComplications.toString().equals("")) {
						selectedComplications.append(CB.getText().toString());
					} else {
						selectedComplications.append(",").append(CB.getText().toString());
					}

				}
			}
		} catch (Exception e) {
			LOGGER.error("## Exception occured:", e);

		}

	}

	/**
	 * This method saves cannulation details into the database
	 *
	 * @throws Exception
	 */
	private void saveCannulation() throws Exception {
		try {

			if (validateCentralForm()) {

				disablePaneMainContent.setVisible(true);

				lblError.setVisible(false);

				Cannulations cannulationsDetails = new Cannulations();
				cannulationsDetails.setCaseId(patientCase.getCaseId());
				if (!choicePatientPosition.getValue().equalsIgnoreCase("Select")) {
					cannulationsDetails.setPatientPosition(choicePatientPosition.getValue());
				}
				if (!choiceSite.getValue().equalsIgnoreCase("Select")) {
					cannulationsDetails.setSite(choiceSite.getValue());
				}
				cannulationsDetails.setComplications(selectedComplications.toString());
				if (!txtAreaOtherDetails.getText().trim().isEmpty()) {
					cannulationsDetails.setDetails(txtAreaOtherDetails.getText().trim());
				}
				if (!choiceVein.getValue().equalsIgnoreCase("Select")) {
					cannulationsDetails.setVein(choiceVein.getValue());
				}
				if (!choiceVeinDetails.getValue().equalsIgnoreCase("Select")) {
					cannulationsDetails.setVeinDetails(choiceVeinDetails.getValue());
				}
				if (!txtSize.getText().trim().isEmpty()) {
					cannulationsDetails.setInsertionSize(txtSize.getText().trim());
				}
				if (!txtInsertedBy.getText().trim().isEmpty()) {
					cannulationsDetails.setInsertedBy(txtInsertedBy.getText().trim());
				}
				if (!txtLength.getText().trim().isEmpty()) {
					cannulationsDetails.setInsertionLength(txtLength.getText().trim());
				}
				if (!txtArterialSite.getText().trim().isEmpty()) {
					cannulationsDetails.setArterySite(txtArterialSite.getText().trim());
				}
				if (!txtArterialSize.getText().trim().isEmpty()) {
					cannulationsDetails.setArterySize(txtArterialSize.getText().trim());
				}
				if (!txtAreaArterialDetails.getText().trim().isEmpty()) {
					cannulationsDetails.setArteryDetails(txtAreaArterialDetails.getText().trim());
				}
				if (!txtPeripheralSite.getText().trim().isEmpty()) {
					cannulationsDetails.setPeripheralSite(txtPeripheralSite.getText().trim());
				}
				if (!txtPeripheralSize.getText().trim().isEmpty()) {
					cannulationsDetails.setPeripheralSize(txtPeripheralSize.getText().trim());
				}
				if (!txtAreaPeripheralDetails.getText().trim().isEmpty()) {
					cannulationsDetails.setPeripheralDetails(txtAreaPeripheralDetails.getText().trim());
				}
				if (!txtPulmonarySite.getText().trim().isEmpty()) {
					cannulationsDetails.setPulmonarySite(txtPulmonarySite.getText().trim());
				}
				if (!txtPulmonarySize.getText().trim().isEmpty()) {
					cannulationsDetails.setPulmonarySize(txtPulmonarySize.getText().trim());
				}
				if (!txtAreaPulmonaryDetails.getText().trim().isEmpty()) {
					cannulationsDetails.setPulmonaryDetails(txtAreaPulmonaryDetails.getText().trim());
				}
				if (!choiceCentralUltraSoundBlind.getValue().equalsIgnoreCase("Select")) {
					cannulationsDetails.setCentralUltrasoundBlind(choiceCentralUltraSoundBlind.getValue());
				}
				if (!choiceArterialUltraSoundBlind.getValue().equalsIgnoreCase("Select")) {
					cannulationsDetails.setArterialUltrasoundBlind(choiceArterialUltraSoundBlind.getValue());
				}
				if (!choicePeripheralUltraSoundBlind.getValue().equalsIgnoreCase("Select")) {
					cannulationsDetails.setPeripheralUltrasoundBlind(choicePeripheralUltraSoundBlind.getValue());
				}

				if (cannulationsId != null) {
					cannulationsDetails.setCannulationsId(cannulationsId);
				}

				CreateCannulationService createCannulationService = CreateCannulationService
						.getInstance(cannulationsDetails);
				createCannulationService.restart();

				createCannulationServiceSuccessHandler = new EventHandler<WorkerStateEvent>() {

					@Override
					public void handle(WorkerStateEvent event) {

						try {
							disablePaneMainContent.setVisible(false);

							createCannulationService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
									createCannulationServiceSuccessHandler);
							createCannulationService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
									createCannulationServiceFailedHandler);
							try {
								closePopup();
							} catch (Exception e1) {
								LOGGER.error("## Exception occured:", e1);
							}
							if (cannulationsId != null) {
								Main.getInstance().getUtility().showNotification("Information", "Success",
										"Cannulation updated successfully!");
							} else {
								Main.getInstance().getUtility().showNotification("Information", "Success",
										"Cannulation saved successfully!");
							}
						} catch (Exception e) {
							LOGGER.error("## Exception occured:", e);

						}
					}

				};

				createCannulationService.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
						createCannulationServiceSuccessHandler);

				createCannulationServiceFailedHandler = new EventHandler<WorkerStateEvent>() {

					@Override
					public void handle(WorkerStateEvent event) {

						try {
							disablePaneMainContent.setVisible(false);

							createCannulationService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
									createCannulationServiceSuccessHandler);
							createCannulationService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
									createCannulationServiceFailedHandler);
							Main.getInstance().getUtility().showNotification("Error", "Error",
									createCannulationService.getException().getMessage());
						} catch (Exception e) {
							LOGGER.error("## Exception occured:", e);

						}
					}

				};

				createCannulationService.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
						createCannulationServiceFailedHandler);
			} else {
				lblError.setVisible(true);
			}

		} catch (Exception e) {
			LOGGER.error("## Exception occured:", e);
		}

	}

	/**
	 * This method fetches cannulation details from the database
	 */
	private void getCannulation() {
		try {
			disablePaneMainContent.setVisible(true);
			GetCannulationService getCannulationService = GetCannulationService.getInstance(patientCase.getCaseId());
			getCannulationService.restart();

			getCannulationServiceSuccessHandler = new EventHandler<WorkerStateEvent>() {

				@Override
				public void handle(WorkerStateEvent event) {
					try {
						Cannulations cannulationRecord = getCannulationService.getCannulationsRecord();
						if (cannulationRecord != null) {

							if (cannulationRecord.getSite() != null) {
								choiceSite.setValue(cannulationRecord.getSite());

							}
							if (cannulationRecord.getVein() != null) {
								choiceVein.setValue(cannulationRecord.getVein());

								if (cannulationRecord.getVein().equalsIgnoreCase("Femoral vein")) {
									choiceVeinDetails.setDisable(true);
								}
							}
							if (cannulationRecord.getVeinDetails() != null) {
								choiceVeinDetails.setValue(cannulationRecord.getVeinDetails());

							}
							if (cannulationRecord.getPatientPosition() != null) {
								choicePatientPosition.setValue(cannulationRecord.getPatientPosition());

							}
							if (cannulationRecord.getInsertionLength() != null) {
								txtLength.setText(cannulationRecord.getInsertionLength());
							}
							if (cannulationRecord.getInsertionSize() != null) {
								txtSize.setText(cannulationRecord.getInsertionSize());
							}
							if (cannulationRecord.getInsertedBy() != null) {
								txtInsertedBy.setText(cannulationRecord.getInsertedBy());
							}

							if (cannulationRecord.getArterySite() != null) {
								txtArterialSite.setText(cannulationRecord.getArterySite());
							}
							if (cannulationRecord.getArterySize() != null) {
								txtArterialSize.setText(cannulationRecord.getArterySize());
							}
							if (cannulationRecord.getArteryDetails() != null) {
								txtAreaArterialDetails.setText(cannulationRecord.getArteryDetails());
							}

							if (cannulationRecord.getPeripheralSite() != null) {
								txtPeripheralSite.setText(cannulationRecord.getPeripheralSite());
							}
							if (cannulationRecord.getPeripheralSize() != null) {
								txtPeripheralSize.setText(cannulationRecord.getPeripheralSize());
							}
							if (cannulationRecord.getPeripheralDetails() != null) {
								txtAreaPeripheralDetails.setText(cannulationRecord.getPeripheralDetails());
							}

							if (cannulationRecord.getPulmonarySite() != null) {
								txtPulmonarySite.setText(cannulationRecord.getPulmonarySite());
							}
							if (cannulationRecord.getPulmonarySize() != null) {
								txtPulmonarySize.setText(cannulationRecord.getPulmonarySize());
							}
							if (cannulationRecord.getPulmonaryDetails() != null) {
								txtAreaPulmonaryDetails.setText(cannulationRecord.getPulmonaryDetails());
							}

							if (cannulationRecord.getComplications() != null) {

								String complications[] = cannulationRecord.getComplications().split(",");
								for (int j = 0; j < complications.length; j++) {
									for (int i = 0; i < complicationListDropDown.size(); i++) {
										CheckBox CB = (CheckBox) vbComplications.getChildren().get(i);

										if (CB.getText().equalsIgnoreCase(complications[j])) {

											CB.setSelected(true);

										}
									}
								}

							}
							if (cannulationRecord.getDetails() != null) {
								txtAreaOtherDetails.setText(cannulationRecord.getDetails());
							}

							if(cannulationRecord.getArterialUltrasoundBlind()!=null){
								choiceArterialUltraSoundBlind.setValue(cannulationRecord.getArterialUltrasoundBlind());
							}

							if(cannulationRecord.getCentralUltrasoundBlind()!=null){
								choiceCentralUltraSoundBlind.setValue(cannulationRecord.getCentralUltrasoundBlind());
							}

							if(cannulationRecord.getPeripheralUltrasoundBlind()!=null){
								choicePeripheralUltraSoundBlind.setValue(cannulationRecord.getPeripheralUltrasoundBlind());
							}
							cannulationsId = cannulationRecord.getCannulationsId();
						}
						disablePaneMainContent.setVisible(false);
						getCannulationService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
								getCannulationServiceSuccessHandler);
						getCannulationService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
								getCannulationServiceFailedHandler);
					} catch (Exception e) {
						LOGGER.error("## Exception occured:", e);

					}
				}

			};

			getCannulationService.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
					getCannulationServiceSuccessHandler);

			getCannulationServiceFailedHandler = new EventHandler<WorkerStateEvent>() {

				@Override
				public void handle(WorkerStateEvent event) {
					try {
						disablePaneMainContent.setVisible(false);
						getCannulationService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
								getCannulationServiceSuccessHandler);
						getCannulationService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
								getCannulationServiceFailedHandler);
					} catch (Exception e) {
						LOGGER.error("## Exception occured:", e);
					}
				}

			};

			getCannulationService.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
					getCannulationServiceFailedHandler);
		} catch (Exception e) {
			LOGGER.error("## Exception occured:", e);
		}

	}

	/**
	 * This method validates input for Central form on the screen
	 *
	 * @return true or false depending on whether input is valid
	 */
	private boolean validateCentralForm() {
		if (choicePatientPosition.getValue().equalsIgnoreCase("Select")
				&& choiceSite.getValue().equalsIgnoreCase("Select") && choiceVein.getValue().equalsIgnoreCase("Select")
				&& txtLength.getText().trim().isEmpty() && txtSize.getText().trim().isEmpty() && txtInsertedBy.getText().trim().isEmpty()
				&& txtAreaOtherDetails.getText().trim().isEmpty() && txtArterialSite.getText().trim().isEmpty()
				&& txtArterialSize.getText().trim().isEmpty() && txtAreaArterialDetails.getText().trim().isEmpty()
				&& txtPeripheralSite.getText().trim().isEmpty() && txtPeripheralSize.getText().trim().isEmpty()
				&& txtAreaPeripheralDetails.getText().trim().isEmpty() && txtPulmonarySite.getText().trim().isEmpty()
				&& txtPulmonarySize.getText().trim().isEmpty() && txtAreaArterialDetails.getText().trim().isEmpty()
				&& selectedComplications.toString().isEmpty()
				&& choiceArterialUltraSoundBlind.getValue().equalsIgnoreCase("Select")
				&& choiceCentralUltraSoundBlind.getValue().equalsIgnoreCase("Select")
				&& choicePeripheralUltraSoundBlind.getValue().equalsIgnoreCase("Select")) {
			lblError.setText("*Please enter atlease one field to save.");
			return false;

		}
		if (!txtSize.getText().trim().isEmpty() && (!Validations.isAlphaNumericWithSpaceAndDot(txtSize.getText().trim())
				|| !Validations.maxLength(txtSize.getText(), 100))) {
			lblError.setText("*Please enter a valid value for Insertion Size less than 100 characters.");
			return false;
		}

		if (!txtInsertedBy.getText().trim().isEmpty() && (!Validations.isAlphaNumericWithSpaceAndDot(txtInsertedBy.getText().trim())
				|| !Validations.maxLength(txtInsertedBy.getText(), 500))) {
			lblError.setText("*Please enter a valid value for Inserted By less than 500 characters.");
			return false;
		}

		if (!txtLength.getText().trim().isEmpty() && (!Validations.isAlphaNumericWithSpace(txtLength.getText().trim())
				|| !Validations.maxLength(txtLength.getText(), 100))) {
			lblError.setText("*Please enter a valid value for Insertion Length less than 100 characters.");
			return false;
		}

		if (txtAreaOtherDetails.getText() != null && !txtAreaOtherDetails.getText().trim().isEmpty()
				&& !Validations.maxLength(txtAreaOtherDetails.getText(), 2000)) {
			lblError.setText("*Please enter Central Other Details less than 2000 characters");
			return false;
		}

		if (!txtArterialSite.getText().trim().isEmpty()
				&& (!Validations.isAlphaNumericWithSpace(txtArterialSite.getText().trim())
						|| !Validations.maxLength(txtArterialSite.getText(), 100))) {
			lblError.setText("*Please enter a valid value for Arterial Site less than 100 characters.");
			return false;
		}

		if (!txtArterialSize.getText().trim().isEmpty()
				&& (!Validations.isAlphaNumericWithSpace(txtArterialSize.getText().trim())
						|| !Validations.maxLength(txtArterialSize.getText(), 100))) {
			lblError.setText("*Please enter a valid value for Arterial Size less than 100 characters.");
			return false;
		}

		if (txtAreaArterialDetails.getText() != null && !txtAreaArterialDetails.getText().trim().isEmpty()
				&& !Validations.maxLength(txtAreaArterialDetails.getText(), 2000)) {
			lblError.setText("*Please enter Arterial Other Details less than 2000 characters");
			return false;
		}

		if (!txtPeripheralSite.getText().trim().isEmpty()
				&& (!Validations.isAlphaNumericWithSpace(txtPeripheralSite.getText().trim())
						|| !Validations.maxLength(txtPeripheralSite.getText(), 100))) {
			lblError.setText("*Please enter a valid value for Peripheral Site less than 100 characters.");
			return false;
		}

		if (!txtPeripheralSize.getText().trim().isEmpty()
				&& (!Validations.isAlphaNumericWithSpace(txtPeripheralSize.getText().trim())
						|| !Validations.maxLength(txtPeripheralSize.getText(), 100))) {
			lblError.setText("*Please enter a valid value for Peripheral Size less than 100 characters.");
			return false;
		}

		if (txtAreaPeripheralDetails.getText() != null && !txtAreaPeripheralDetails.getText().trim().isEmpty()
				&& !Validations.maxLength(txtAreaPeripheralDetails.getText(), 2000)) {
			lblError.setText("*Please enter Peripheral Other Details less than 2000 characters");
			return false;
		}

		if (!txtPulmonarySite.getText().trim().isEmpty()
				&& (!Validations.isAlphaNumericWithSpace(txtPulmonarySite.getText().trim())
						|| !Validations.maxLength(txtPulmonarySite.getText(), 100))) {
			lblError.setText("*Please enter a valid value for Pulmonary Site less than 100 characters.");
			return false;
		}

		if (!txtPulmonarySize.getText().trim().isEmpty()
				&& (!Validations.isAlphaNumericWithSpace(txtPulmonarySize.getText().trim())
						|| !Validations.maxLength(txtPulmonarySize.getText(), 100))) {
			lblError.setText("*Please enter a valid value for Pulmonary Size less than 100 characters.");
			return false;
		}

		if (txtAreaPulmonaryDetails.getText() != null && !txtAreaPulmonaryDetails.getText().trim().isEmpty()
				&& !Validations.maxLength(txtAreaPulmonaryDetails.getText(), 2000)) {
			lblError.setText("*Please enter Pulmonary Other Details less than 2000 characters");
			return false;
		}
		return true;
	}

	/**
	 * This method validates input for Artery form on the screen
	 *
	 * @return true or false depending on whether input is valid
	 */
	private boolean validateArteryForm() {

		if (!Validations.isDigit(txtArterialSize.getText().trim())) {
			lblError.setText("*Please enter a valid value for Cannula Size.");
			return false;
		}

		if (!Validations.decimalsUpto2placesAndLength3(txtArterialSize.getText().trim())) {
			lblError.setText("*Please enter Cannula Size upto 3 digits and decimal upto 2 digits only");
			return false;
		}
		if (!Validations.nonZeroBigDecimal(txtArterialSize.getText().trim())) {
			lblError.setText("*Please enter Cannula Size value more than 0");
			return false;
		}

		return true;
	}
}
