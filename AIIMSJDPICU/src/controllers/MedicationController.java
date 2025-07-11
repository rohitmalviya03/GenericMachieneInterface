/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package controllers;

import java.math.BigDecimal;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.cdac.common.GeneratedEntities.Fdamedications;
import com.cdac.common.GeneratedEntities.IntraopMedicationlog;
import com.cdac.common.GeneratedEntities.Patient;
import com.cdac.common.GeneratedEntities.Patientcase;
import com.cdac.common.util.Validations;
import com.jfoenix.controls.JFXToggleButton;

import application.FxmlView;
import application.Main;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import model.MedicationSession;
import model.PatientSession;
import model.UserSession;
import services.GetTotalMedicationVolumeService;
import services.SaveMedicationService;
import services.SearchFDAMedicationsService;

/**
 * MedicationController.java<br>
 * Purpose:This class contains logic to handle Medication screen inputs
 *
 * @author Rohit_Sharma41
 *
 */
public class MedicationController implements Initializable {

	private static final Logger LOGGER = Logger.getLogger(MedicationController.class.getName());

	@FXML
	private AnchorPane topBar;

	@FXML
	private Button btnClose;

	@FXML
	private AnchorPane mainContainer;

	// @FXML
	// private AnchorPane sideBar;

	@FXML
	private TextField textSearchMedication;

	// @FXML
	// private ListView<String> medicationList;

	@FXML
	private ListView<String> medSearchList;

	@FXML
	private Label lblSelectedMed;

	@FXML
	private RadioButton typeBolus;

	@FXML
	private ToggleGroup medicationType;

	@FXML
	private RadioButton typeInfusion;

	@FXML
	private TitledPane bolusPane;

	@FXML
	private TextField bolus_dose;

	@FXML
	private TextArea txtareaBolus;

	@FXML
	private TitledPane infusionPane;

	@FXML
	private TextField infusion_dose;

	@FXML
	private TextField infusion_volume;

	@FXML
	private TextField infusion_rate;

	@FXML
	private TextArea txtareaInfusion;

	@FXML
	private HBox btnHolder;

	@FXML
	private Label lblError;

	@FXML
	private Label lblSaving;

	@FXML
	private Button btnSave;

	@FXML
	private ChoiceBox<String> choiceUnit;

	@FXML
	private TextField txtConcetrInfusion;

	@FXML
	private ChoiceBox<String> choiceUnitBolus;

	@FXML
	private Label lblTotalDose;

	@FXML
	private VBox pageContent;

	@FXML
	private DatePicker datePickerStartDate;

	@FXML
	private DatePicker datePickerStopDate;

	@FXML
	private DatePicker datePickerBolusDate;

	@FXML
	private VBox vbInfusionStopTimeControl;

	@FXML
	private Button btnStopInfusionTime;

	@FXML
	private VBox vbInfusionStartTimeControl;

	@FXML
	private Button btnStartInfusionTime;

	@FXML
	private VBox vbBolusStartTimeControl;

	@FXML
	private Button btnBolusTime;

	@FXML
	private Label lblInProgress;

	@FXML
	private JFXToggleButton toggleBtnStop;

	@FXML
	private VBox vbDoseInfused;

	@FXML
	private Label lblDoseInfused;

	// @FXML
	// private AnchorPane disablePaneMedicationList;

	@FXML
	private AnchorPane disablePaneMainContent;
	@FXML
	private AnchorPane anchorLoader;

	// private final Logger LOG = Logger.getLogger(MedicationController.class);

	private String medicationTypeValue = "Bolus";
	private String selectedSearchVal = "";
	// private String savedRouteId = "";

	// private GetFavoriteMedicationsService getFavoriteMedicationsService;
	// private SaveMedicationService saveMedicationService;
	private SearchFDAMedicationsService searchFDAMedicationsService;
	// private GetMasterDataMedService getMasterDataMedService;
	// private GetTotalMedicationVolumeService getTotalVolumeService;

	// private List<Fdamedications> fdamedicationsListDB;
	//
	private List<Fdamedications> fdamedicationsSearchListDB;
	//
	//
	// private ObservableList<String> fdamedicationsList;

	private ObservableList<String> units;
	private ObservableList<String> fdamedicationsSearchList;

	private IntraopMedicationlog medicationLog;
	private Long medicationLogId;
	private Patient patient;
	private Patientcase patientCase;
	private Boolean isValidForm = true;
	private Boolean fromSearch = false;
	private boolean medSessionAvailable = false;

	Spinner<String> hourSpinnerInfStart = new Spinner<>();
	Spinner<String> minuteSpinnerInfStart = new Spinner<>();
	Spinner<String> meridiemSpinnerInfStart = new Spinner<>();
	private HBox hBoxInfStartTimeControl;

	Spinner<String> hourSpinnerInfStop = new Spinner<>();
	Spinner<String> minuteSpinnerInfStop = new Spinner<>();
	Spinner<String> meridiemSpinnerInfStop = new Spinner<>();
	private HBox hBoxInfStopTimeControl;

	Spinner<String> hourSpinnerBolus = new Spinner<>();
	Spinner<String> minuteSpinnerBolus = new Spinner<>();
	Spinner<String> meridiemSpinnerBolus = new Spinner<>();
	private HBox hBoxBolusTimeControl;
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	static final int MINUTES_PER_HOUR = 60;
	static final int SECONDS_PER_MINUTE = 60;
	static final int SECONDS_PER_HOUR = SECONDS_PER_MINUTE * MINUTES_PER_HOUR;

	// private boolean infusionStopped = false;
	// private String infusedDoseFinal;

	private ChangeListener<String> medSearchListChangeListener;

	private ChangeListener<String> textSearchMedicationChangeListener;

	private ChangeListener<Boolean> textSearchMedication2ChangeListener;

	private EventHandler<MouseEvent> btnCloseHandler;

	private EventHandler<MouseEvent> btnSaveHandler;

	private ChangeListener<Boolean> infusion_volumeChangeListener;

	private ChangeListener<Boolean> infusion_doseChangeListener;

	private ChangeListener<Boolean> choiceUnitChangeListener;

	private EventHandler<MouseEvent> btnBolusTimeHandler;

	private EventHandler<MouseEvent> btnStartInfusionTimeHandler;

	private EventHandler<MouseEvent> btnStopInfusionTimeHandler;

	private ChangeListener<Boolean> toggleBtnStopChangeListener;

	private EventHandler<WorkerStateEvent> getTotalVolumeServiceSuccessHandler;

	private EventHandler<WorkerStateEvent> getTotalVolumeServiceFailedHandler;

	private EventHandler<WorkerStateEvent> saveMedicationServiceSuccessHandler;

	private EventHandler<WorkerStateEvent> saveMedicationServiceFailedHandler;

	private EventHandler<WorkerStateEvent> searchFDAMedicationsServiceSuccessHandler;

	private EventHandler<WorkerStateEvent> searchFDAMedicationsServiceFailedHandler;

	private ChangeListener<Toggle> medicationTypeChangeListener;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			hBoxInfStartTimeControl = Main.getInstance().getUtility().getTimeControl();
			vbInfusionStartTimeControl.getChildren().add(hBoxInfStartTimeControl);
			hourSpinnerInfStart = (Spinner) hBoxInfStartTimeControl.getChildren().get(0);
			minuteSpinnerInfStart = (Spinner) hBoxInfStartTimeControl.getChildren().get(2);
			meridiemSpinnerInfStart = (Spinner) hBoxInfStartTimeControl.getChildren().get(3);

			// ----Infusion Stop Time control

			hBoxInfStopTimeControl = Main.getInstance().getUtility().getTimeControl();
			vbInfusionStopTimeControl.getChildren().add(hBoxInfStopTimeControl);
			hourSpinnerInfStop = (Spinner) hBoxInfStopTimeControl.getChildren().get(0);
			minuteSpinnerInfStop = (Spinner) hBoxInfStopTimeControl.getChildren().get(2);
			meridiemSpinnerInfStop = (Spinner) hBoxInfStopTimeControl.getChildren().get(3);

			// ---- Bolus Time control

			hBoxBolusTimeControl = Main.getInstance().getUtility().getTimeControl();
			vbBolusStartTimeControl.getChildren().add(hBoxBolusTimeControl);
			hourSpinnerBolus = (Spinner) hBoxBolusTimeControl.getChildren().get(0);
			minuteSpinnerBolus = (Spinner) hBoxBolusTimeControl.getChildren().get(2);
			meridiemSpinnerBolus = (Spinner) hBoxBolusTimeControl.getChildren().get(3);

			if (PatientSession.getInstance().getPatient() != null)
				patient = PatientSession.getInstance().getPatient();
			if (PatientSession.getInstance().getPatientCase() != null)
				patientCase = PatientSession.getInstance().getPatientCase();

			btnSave.setText("Save");

			units = FXCollections.observableArrayList("Select", "g", "mg");
			String unit = "\u00B5" + "g";

			units.add(unit);
			choiceUnit.setItems(units);
			choiceUnit.getSelectionModel().select("mg");
			choiceUnitBolus.setItems(units);
			choiceUnitBolus.getSelectionModel().select("mg");
			// ---populate left panel with favorite medication list
			// medicationList.setItems(FXCollections.observableArrayList());
			// try {
			//
			// if(MasterDataSession.getInstance().getMasterDataMap().get("FavMedication")!=null){
			// medicationList.setItems(MasterDataSession.getInstance().getMasterDataMap().get("FavMedication"));
			// }else{
			// callFavMedService();
			// }
			// } catch (Exception e2) {
			// LOGGER.error("## Exception occured:", e2);
			// }

			// ---set RadioGroup for Bolus and Infusion radiobuttons
			try {
				setRadioButtons();
			} catch (Exception e2) {
				LOGGER.error("## Exception occured:", e2);
			}

			try {
				if (MedicationSession.getInstance().getIntraopMedicationlog().getMedicationLogId() != null) {
					medSessionAvailable = true;
				}
			} catch (Exception e) {
				medSessionAvailable = false;
			}

			if (medSessionAvailable) {
				anchorLoader.setVisible(true);

				// textSearchMedication.setDisable(true);
				// disablePaneMedicationList.setVisible(true);
				// medicationList.setDisable(true);
				btnSave.setText("Update");
				IntraopMedicationlog medicationObj = MedicationSession.getInstance().getIntraopMedicationlog();

				if (medicationObj.isFromDevice()) {
					// pageContent.setDisable(true);
					disablePaneMainContent.setVisible(true);

				} else {
					// pageContent.setDisable(false);
					disablePaneMainContent.setVisible(false);
				}

				medicationLogId = medicationObj.getMedicationLogId();
				medicationObj
						.setUpdatedBy(UserSession.getInstance().getUserWithrRolesForUserAuthentication().getUserID());
				medicationObj.setUpdatedTime(new Date());

				lblSelectedMed.setText(medicationObj.getMedicationName());
				textSearchMedication.setText(medicationObj.getMedicationName());

				if (medicationObj.getBolusInfusion().equalsIgnoreCase("bolus")) {
					medicationTypeValue = "Bolus";

					bolusPane.setVisible(true);
					infusionPane.setVisible(false);
					typeInfusion.setDisable(true);
					medicationType.selectToggle(typeBolus);

					choiceUnitBolus.getSelectionModel().select(medicationObj.getDosageUnit());
					float dose = 0.0f;
					if (choiceUnitBolus.getSelectionModel().getSelectedItem().equalsIgnoreCase("g")) {
						dose = medicationObj.getInfuseDosage() / 1000;
					} else if (choiceUnitBolus.getSelectionModel().getSelectedItem().equalsIgnoreCase("mg")) {
						dose = medicationObj.getInfuseDosage();
					} else {
						dose = medicationObj.getInfuseDosage() * 1000;
					}
					bolus_dose.setText(dose + "");

					if (medicationObj.getStartTime() != null) {
						String dateTimeArr[] = medicationObj.getStartTime().toString().split(" ");
						String timearray[] = dateTimeArr[1].split("\\.");
						LocalDate localStartDate = LocalDate.parse(dateTimeArr[0]);
						datePickerBolusDate.setValue(localStartDate);
						try {
							setBolusTime(timearray[0]);
						} catch (Exception e1) {
							LOGGER.error("## Exception occured:", e1);
						}
					}

					if (medicationObj.getComments() != null && !medicationObj.getComments().trim().isEmpty()) {
						txtareaBolus.setText(medicationObj.getComments().trim());
					}
				} else if (medicationObj.getBolusInfusion().equalsIgnoreCase("infusion")) {
					medicationTypeValue = "Infusion";
					if (medicationObj.getMedicationDosage() != null) {
						infusion_dose.setText(medicationObj.getMedicationDosage() + "");
					}
					bolusPane.setVisible(false);
					infusionPane.setVisible(true);
					typeBolus.setDisable(true);
					medicationType.selectToggle(typeInfusion);
					infusion_dose.setText(medicationObj.getMedicationDosage() + "");
					infusion_volume.setText(medicationObj.getVolume().toString());

					if (medicationObj.getRateOfInfusion() != null
							&& !medicationObj.getRateOfInfusion().trim().isEmpty()) {
						infusion_rate.setText(medicationObj.getRateOfInfusion().trim());
					}
					if (medicationObj.getComments() != null && !medicationObj.getComments().trim().isEmpty()) {
						txtareaInfusion.setText(medicationObj.getComments().trim());
					}
					choiceUnit.getSelectionModel().select(medicationObj.getDosageUnit());
					txtConcetrInfusion.setText(medicationObj.getConcentration());

					if (medicationObj.getStartTime() != null) {
						String dateTimeArr[] = medicationObj.getStartTime().toString().split(" ");
						String timearray[] = dateTimeArr[1].split("\\.");
						LocalDate localStartDate = LocalDate.parse(dateTimeArr[0]);
						datePickerStartDate.setValue(localStartDate);
						try {
							setInfusionStartTime(timearray[0]);
						} catch (Exception e1) {
							LOGGER.error("## Exception occured:", e1);
						}

					}
					if (medicationObj.getEndTime() != null) {
						String dateTimeArr[] = medicationObj.getEndTime().toString().split(" ");
						String timearray[] = dateTimeArr[1].split("\\.");
						LocalDate localStoptDate = LocalDate.parse(dateTimeArr[0]);
						datePickerStopDate.setValue(localStoptDate);

						try {
							setInfusionStopTime(timearray[0]);
						} catch (Exception e1) {
							LOGGER.error("## Exception occured:", e1);
						}
						toggleBtnStop.setSelected(true);
						try {
							stopInfusion();
						} catch (Exception e1) {
							LOGGER.error("## Exception occured:", e1);
						}

					} else {
						lblInProgress.setVisible(true);
					}

				}

				// savedRouteId = medicationObj.getRouteId();
				MedicationSession.getInstance().setIntraopMedicationlog(null);
				try {
					getTotalMedicationVolume();
				} catch (Exception e1) {
					LOGGER.error("## Exception occured:", e1);
				}
				btnSave.setVisible(true);
				anchorLoader.setVisible(false);
			} else {
				lblSelectedMed.setText("");
				Date input = new Date();
				LocalDate date = input.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				datePickerStartDate.setValue(date);
				datePickerBolusDate.setValue(date);

				try {
					resetTime();
				} catch (Exception e1) {
					LOGGER.error("## Exception occured:", e1);
				}
			}

			medicationTypeChangeListener = new ChangeListener<Toggle>() {
				@Override
				public void changed(ObservableValue<? extends Toggle> observable, Toggle oldPropertyValue,
						Toggle new_toggle) {
					if (new_toggle != null) {
						try
						{
							medicationTypeValue = new_toggle.getUserData().toString();
							if (medicationTypeValue.equalsIgnoreCase("Bolus")) {
								bolusPane.setVisible(true);
								infusionPane.setVisible(false);
							} else if (medicationTypeValue.equalsIgnoreCase("Infusion")) {
								infusionPane.setVisible(true);
								bolusPane.setVisible(false);
							}
							btnSave.setVisible(true);
						}
						catch (Exception e)
						{
							LOGGER.error("## Exception occured:", e);
						}
					}
				}
			};

			// ---Onclick of radiobuttons, show respective tab
			medicationType.selectedToggleProperty().addListener(medicationTypeChangeListener);

			// ---show selected medicationList item on lblSelectedMed label
			// medicationList.getSelectionModel().selectedItemProperty()
			// .addListener((ObservableValue<? extends String> observable,
			// String oldValue, String newValue) -> {
			// if (newValue != null) {
			// fromSearch = true;
			// lblError.setVisible(false);
			// System.out.println("Selected Favourite Medication : ***" +
			// newValue + "***");
			// lblSelectedMed.setText(newValue);
			// textSearchMedication.setText(newValue);
			// try {
			// getTotalMedicationVolume();
			// } catch (Exception e1) {
			// LOGGER.error("## Exception occured:", e1);
			// }
			// }
			// });

			// ---show selected medicationSearchList item on lblSelectedMed
			// label

			medSearchListChangeListener = new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldPropertyValue,
						String newValue) {
					if (newValue != null) {
						try
						{
							fromSearch = true;
							lblError.setVisible(false);
							selectedSearchVal = newValue;
							lblSelectedMed.setText(newValue);
							medSearchList.setVisible(false);
							// medicationList.getSelectionModel().clearSelection();
							textSearchMedication.setText(newValue);
							try {
								getTotalMedicationVolume();
							} catch (Exception e1) {
								LOGGER.error("## Exception occured:", e1);
							}
						}
						catch (Exception e)
						{
							LOGGER.error("## Exception occured:", e);
						}
					}
				}
			};

			medSearchList.getSelectionModel().selectedItemProperty().addListener(medSearchListChangeListener);

			textSearchMedicationChangeListener = new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldPropertyValue,
						String newValue) {
					try
					{
						if (!fromSearch)
						{
							lblError.setVisible(false);
							if (Validations.isAlphaNumericWithSpaceAndSpecialCharacter(textSearchMedication.getText()))
							{
								if (newValue.length() >= 5 && !newValue.isEmpty() && !newValue.equals(""))
								{

									if (!newValue.equalsIgnoreCase(selectedSearchVal))
									{
										fdamedicationsSearchList = FXCollections.observableArrayList();
										medSearchList.setItems(fdamedicationsSearchList);
										medSearchList.setVisible(false);
										try
										{
											callSearchFDAMEdService(newValue);
										}
										catch (Exception e1)
										{
											LOGGER.error("## Exception occured:", e1);
										}
									}

								}
								else if (newValue.length() < 5)
								{
									medSearchList.getSelectionModel().clearSelection();
									medSearchList.setVisible(false);
								}
							}
							else
							{
								lblError.setVisible(true);
								lblError.setText("Please enter a valid search entry");
							}
						} else {
							fromSearch = false;
						}
					}
					catch (Exception e)
					{
						LOGGER.error("## Exception occured:", e);
					}
				}
			};

			textSearchMedication.textProperty().addListener(textSearchMedicationChangeListener);

			textSearchMedication2ChangeListener = new ChangeListener<Boolean>() {
				@Override
				public void changed(ObservableValue<? extends Boolean> observable, Boolean oldPropertyValue,
						Boolean newPropertyValue) {
					try
					{
						if (!newPropertyValue) {
							lblError.setVisible(false);
							medSearchList.setVisible(false);
						}
					}
					catch (Exception e)
					{
						LOGGER.error("## Exception occured:", e);
					}
				}
			};

			textSearchMedication.focusedProperty().addListener(textSearchMedication2ChangeListener);

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
					try
					{
						if (medicationTypeValue.equalsIgnoreCase("Infusion")) {
							if (validateForInfusion()) {
								lblError.setVisible(false);

								// ---create MedicationLog Object
								try {
									createMedicationLogObject();
								} catch (Exception e2) {
									LOGGER.error("## Exception occured:", e2);
								}

								// ---pass object to saveMedicationService
								try {
									callSaveMedicationService(medicationLog);
								} catch (Exception e1) {
									LOGGER.error("## Exception occured:", e1);
								}

							} else {
								lblError.setVisible(true);
							}

						}

						if (medicationTypeValue.equalsIgnoreCase("Bolus")) {
							if (validateForBolus()) {
								lblError.setVisible(false);

								// ---create MedicationLog Object
								try {
									createMedicationLogObject();
								} catch (Exception e2) {
									LOGGER.error("## Exception occured:", e2);
								}

								// ---pass object to saveMedicationService
								try {
									callSaveMedicationService(medicationLog);
								} catch (Exception e1) {
									LOGGER.error("## Exception occured:", e1);
								}
							} else {
								lblError.setVisible(true);
							}
						}
					}
					catch (Exception e)
					{
						LOGGER.error("## Exception occured:", e);
					}
				}
			};
			btnSave.addEventHandler(MouseEvent.MOUSE_CLICKED, btnSaveHandler);

			infusion_volumeChangeListener = new ChangeListener<Boolean>() {
				@Override
				public void changed(ObservableValue<? extends Boolean> observable, Boolean oldPropertyValue,
						Boolean newPropertyValue) {
					try
					{
						if (!newPropertyValue) {
							if (infusion_volume.getText() != null && infusion_volume.getText().trim() != ""
									&& Validations.decimalsUpto2places(infusion_volume.getText())) {

								lblError.setVisible(false);
								try {
									calculateConcentration();
								} catch (Exception e) {
									LOGGER.error("## Exception occured:", e);
								}
							}

							else {

								infusion_volume.setText("");
								lblError.setVisible(true);
								lblError.setText("Please enter valid volume upto 5 digits and decimal upto 2 digits only");
							}
						}
					}
					catch (Exception e)
					{
						LOGGER.error("## Exception occured:", e);
					}
				}
			};

			infusion_doseChangeListener = new ChangeListener<Boolean>() {
				@Override
				public void changed(ObservableValue<? extends Boolean> observable, Boolean oldPropertyValue,
						Boolean newPropertyValue) {
					try
					{
						if (!newPropertyValue) {
							if (infusion_dose.getText() != null && infusion_dose.getText() != ""
									&& Validations.decimalsUpto2places(infusion_dose.getText())) {

								lblError.setVisible(false);

								try {
									calculateConcentration();
								} catch (Exception e) {
									LOGGER.error("## Exception occured:", e);
								}
							}

							else {

								infusion_dose.setText("");
								lblError.setText("Please enter valid Dose upto 4 digits and decimal upto 2 digits only");
								lblError.setVisible(true);
							}

						}
					}
					catch (Exception e)
					{
						LOGGER.error("## Exception occured:", e);
					}
				}
			};

			choiceUnitChangeListener = new ChangeListener<Boolean>() {
				@Override
				public void changed(ObservableValue<? extends Boolean> observable, Boolean oldPropertyValue,
						Boolean newPropertyValue) {
					try
					{
						if (!newPropertyValue) {

							try {
								calculateConcentration();
							} catch (Exception e) {
								LOGGER.error("## Exception occured:", e);
							}
						}
					}
					catch (Exception e)
					{
						LOGGER.error("## Exception occured:", e);
					}
				}
			};

			infusion_volume.focusedProperty().addListener(infusion_volumeChangeListener);
			infusion_dose.focusedProperty().addListener(infusion_doseChangeListener);
			choiceUnit.focusedProperty().addListener(choiceUnitChangeListener);

			btnStartInfusionTimeHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent event) {
					try
					{
						LocalTime timeNow = Main.getInstance().getUtility().getLocalSystemTime();
						try {
							setInfusionStartTime(timeNow.toString());
						} catch (Exception e1) {
							LOGGER.error("## Exception occured:", e1);
						}
						Date input = new Date();
						LocalDate date = input.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
						datePickerStartDate.setValue(date);
					}
					catch (Exception e)
					{
						LOGGER.error("## Exception occured:", e);
					}
				}
			};
			btnStartInfusionTime.addEventHandler(MouseEvent.MOUSE_CLICKED, btnStartInfusionTimeHandler);

			btnStopInfusionTimeHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent event) {
					try
					{
						LocalTime timeNow = Main.getInstance().getUtility().getLocalSystemTime();
						try {
							setInfusionStopTime(timeNow.toString());
						} catch (Exception e1) {
							LOGGER.error("## Exception occured:", e1);
						}
						Date input = new Date();
						LocalDate date = input.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
						datePickerStopDate.setValue(date);
					}
					catch (Exception e)
					{
						LOGGER.error("## Exception occured:", e);
					}
				}
			};
			btnStopInfusionTime.addEventHandler(MouseEvent.MOUSE_CLICKED, btnStopInfusionTimeHandler);

			btnBolusTimeHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent event) {
					try
					{
						LocalTime timeNow = Main.getInstance().getUtility().getLocalSystemTime();
						try {
							setBolusTime(timeNow.toString());
						} catch (Exception e1) {
							LOGGER.error("## Exception occured:", e1);
						}
						Date input = new Date();
						LocalDate date = input.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
						datePickerBolusDate.setValue(date);
					}
					catch (Exception e)
					{
						LOGGER.error("## Exception occured:", e);
					}
				}
			};
			btnBolusTime.addEventHandler(MouseEvent.MOUSE_CLICKED, btnBolusTimeHandler);

			toggleBtnStopChangeListener = new ChangeListener<Boolean>() {
				@Override
				public void changed(ObservableValue<? extends Boolean> observable, Boolean oldPropertyValue,
						Boolean newValue) {
					try
					{
						if (newValue) {
							try {
								stopInfusion();
							} catch (Exception e1) {
								LOGGER.error("## Exception occured:", e1);
							}
						} else {
							if (medSessionAvailable) {
								lblInProgress.setVisible(true);
							}
							datePickerStartDate.setDisable(false);
							datePickerStopDate.setDisable(false);
							vbInfusionStartTimeControl.setDisable(false);
							vbInfusionStopTimeControl.setDisable(false);

							datePickerStopDate.setValue(null);
							lblDoseInfused.setText("");
							vbDoseInfused.setVisible(false);
						}
					}
					catch (Exception e)
					{
						LOGGER.error("## Exception occured:", e);
					}
				}
			};

			toggleBtnStop.selectedProperty().addListener(toggleBtnStopChangeListener);
		} catch (Exception e) {
			LOGGER.error("## Exception occured:" + e.getMessage());
			Main.getInstance().getUtility().showNotification("Error", "Error", "Something went wrong");
		}

	}

	/**
	 * This method stops running infusion
	 *
	 * @throws Exception
	 */
	public void stopInfusion() throws Exception {
		try {
			LocalTime timeNow = Main.getInstance().getUtility().getLocalSystemTime();
			setInfusionStopTime(timeNow.toString());
			Date input = new Date();
			LocalDate date = input.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			datePickerStopDate.setValue(date);
			if (!textSearchMedication.getText().isEmpty() && !infusion_dose.getText().isEmpty()
					&& !infusion_volume.getText().isEmpty() && choiceUnit.getSelectionModel().getSelectedIndex() != 0) {
				if (validateForInfusion()) {
					lblError.setVisible(false);
					if (medSessionAvailable) {
						lblInProgress.setVisible(false);
					}

					float infusedDose = calculateInfusedDose();
					if (infusedDose != 0) {

						lblDoseInfused.setText(infusedDose + " " + "mg");
					}
					// infusedDoseFinal = String.valueOf(infusedDose);

					vbDoseInfused.setVisible(true);
					datePickerStartDate.setDisable(true);
					datePickerStopDate.setDisable(true);
					vbInfusionStartTimeControl.setDisable(true);
					vbInfusionStopTimeControl.setDisable(true);

					// infusionStopped = true;
				} else {
					lblError.setVisible(true);
					toggleBtnStop.setSelected(false);
				}
			} else {
				lblError.setText("Please enter mandatory fields.");
				lblError.setVisible(true);
				toggleBtnStop.setSelected(false);

			}
		} catch (Exception e) {
			LOGGER.error("## Exception occured:" + e.getMessage());
		}
	}

	/**
	 * This method remove active event handlers from all the components
	 */
	private void removeEventHandlers() {
		medicationType.selectedToggleProperty().removeListener(medicationTypeChangeListener);
		medSearchList.getSelectionModel().selectedItemProperty().removeListener(medSearchListChangeListener);
		textSearchMedication.textProperty().removeListener(textSearchMedicationChangeListener);
		textSearchMedication.focusedProperty().removeListener(textSearchMedication2ChangeListener);
		btnClose.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnCloseHandler);
		btnSave.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnSaveHandler);
		infusion_volume.focusedProperty().removeListener(infusion_volumeChangeListener);
		infusion_dose.focusedProperty().removeListener(infusion_doseChangeListener);
		choiceUnit.focusedProperty().removeListener(choiceUnitChangeListener);
		btnStartInfusionTime.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnStartInfusionTimeHandler);
		btnStopInfusionTime.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnStopInfusionTimeHandler);
		btnBolusTime.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnBolusTimeHandler);
		toggleBtnStop.selectedProperty().removeListener(toggleBtnStopChangeListener);
	}

	/**
	 * This method closes Medication screen
	 */
	private void closePopup() throws Exception
	{
		try {
			removeEventHandlers();
			Main.getInstance().getStageManager().closeSecondaryStage();
		} catch (Exception e) {
			LOGGER.error("## Exception occured:" + e.getMessage());
		}

	}

	/**
	 * This method call GetTotalMedicationVolumeService and get total medication
	 * volume
	 */
	private void getTotalMedicationVolume() throws Exception
	{
		try {
			GetTotalMedicationVolumeService getTotalVolumeService = GetTotalMedicationVolumeService
					.getInstance(textSearchMedication.getText(), patient.getPatientId(), patientCase.getCaseId());
			getTotalVolumeService.restart();

			getTotalVolumeServiceSuccessHandler = new EventHandler<WorkerStateEvent>() {

				@Override
				public void handle(WorkerStateEvent event) {
					try
					{
						float totalDose = getTotalVolumeService.getTotalVolume().getValue();
						DecimalFormat df = new DecimalFormat("###.##");

						lblTotalDose.setText(df.format(totalDose) + " mg");
						getTotalVolumeService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
								getTotalVolumeServiceSuccessHandler);
						getTotalVolumeService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
								getTotalVolumeServiceFailedHandler);
					}
					catch (Exception e)
					{
						LOGGER.error("## Exception occured:", e);
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
						LOGGER.error("## Exception occured:", e);
					}
				}

			};

			getTotalVolumeService.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
					getTotalVolumeServiceFailedHandler);
		} catch (Exception e) {
			LOGGER.error("## Exception occured:" + e.getMessage());
		}
	}

	/**
	 * This method calculates Infusion concentration
	 */
	private void calculateConcentration() throws Exception
	{
		try {
			txtConcetrInfusion.setText("");

			if (choiceUnit.getSelectionModel().getSelectedIndex() != 0 && !infusion_volume.getText().isEmpty()
					&& !infusion_dose.getText().isEmpty()) {

				float concentration = 0;

				if (choiceUnit.getSelectionModel().getSelectedItem().equalsIgnoreCase("mg")) {
					concentration = (Float.parseFloat(infusion_dose.getText()) * 1000)
							/ Float.parseFloat(infusion_volume.getText());
				} else if (choiceUnit.getSelectionModel().getSelectedItem().equalsIgnoreCase("g")) {
					concentration = (Float.parseFloat(infusion_dose.getText()) * 1000000)
							/ Float.parseFloat(infusion_volume.getText());
				} else {
					concentration = Float.parseFloat(infusion_dose.getText())
							/ Float.parseFloat(infusion_volume.getText());
				}
				BigDecimal concentrationDecimal = new BigDecimal(concentration);
				BigDecimal concentrationRounded = concentrationDecimal.setScale(3, BigDecimal.ROUND_HALF_UP);
				txtConcetrInfusion.setText(concentrationRounded + "");

			}
		} catch (Exception e) {
			LOGGER.error("## Exception occured:" + e.getMessage());
		}

	}

	/**
	 * This method sets toggle group for radio buttons
	 */
	private void setRadioButtons() throws Exception
	{
		try {
			medicationType = new ToggleGroup();

			typeBolus.setUserData("Bolus");
			typeBolus.setToggleGroup(medicationType);

			typeInfusion.setUserData("Infusion");
			typeInfusion.setToggleGroup(medicationType);
		} catch (Exception e) {
			LOGGER.error("## Exception occured:" + e.getMessage());
		}
	}

	/**
	 * This method call FavMedService and fetches all favorite medicines
	 */
	// public void callFavMedService() throws Exception{
	// LOGGER.debug("Logger Name: " + LOGGER.getName());
	// try {
	// getFavoriteMedicationsService =
	// GetFavoriteMedicationsService.getInstance();
	// getFavoriteMedicationsService.restart();
	//
	// fdamedicationsList = FXCollections.observableArrayList();
	// getFavoriteMedicationsService.setOnSucceeded(e -> {
	// System.out.println("getFavoriteMedicationsService service was
	// success----->>>");
	//
	// fdamedicationsListDB =
	// getFavoriteMedicationsService.getFdamedicationsList();
	// if (fdamedicationsListDB != null) {
	// for (Fdamedications obj : fdamedicationsListDB) {
	// if (obj != null)
	// fdamedicationsList.add(obj.getMedicationsName());
	// }
	//
	// medicationList.setItems(fdamedicationsList);
	//
	// }
	//
	// });
	//
	// getFavoriteMedicationsService.setOnFailed(e -> {
	// System.out.println("getFavoriteMedicationsService service was
	// failed----->>>");
	// Main.getInstance().getUtility().showNotification("Error", "Error",
	// getFavoriteMedicationsService.getException().getMessage());
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
	 * This method creates MedicationLog Object using the fields input on UI
	 */
	private void createMedicationLogObject() throws Exception
	{

		try {
			anchorLoader.setVisible(true);
			isValidForm = true;
			lblError.setVisible(false);
			medicationLog = new IntraopMedicationlog();
			if (medicationLogId != null)
				medicationLog.setMedicationLogId(medicationLogId);

			medicationLog.setPatient(PatientSession.getInstance().getPatient());
			medicationLog.setPatientcase(PatientSession.getInstance().getPatientCase());

			medicationLog.setBolusInfusion(medicationTypeValue);
			medicationLog.setMedicationName(textSearchMedication.getText());

			if (medicationTypeValue.equalsIgnoreCase("Bolus")) {
				if (!textSearchMedication.getText().isEmpty() && !bolus_dose.getText().isEmpty()
						&& choiceUnitBolus.getSelectionModel().getSelectedIndex() != 0) {
					medicationLog.setDosageUnit(choiceUnitBolus.getSelectionModel().getSelectedItem());

					Float infusedDose = 0f;
					if (choiceUnitBolus.getSelectionModel().getSelectedItem().equalsIgnoreCase("g")) {
						infusedDose = Float.parseFloat(bolus_dose.getText()) * 1000;
					} else if (choiceUnitBolus.getSelectionModel().getSelectedItem().equalsIgnoreCase("mg")) {
						infusedDose = Float.parseFloat(bolus_dose.getText());
					} else {
						infusedDose = Float.parseFloat(bolus_dose.getText()) / 1000;
					}
					medicationLog.setInfuseDosage(infusedDose);

					if (datePickerBolusDate.getValue() != null) {
						LocalDate localDate = datePickerBolusDate.getValue();
						LocalTime bolusTime = Main.getInstance().getUtility().getTime(hBoxBolusTimeControl);
						String dateTime = localDate + " " + bolusTime;
						Date bolusDateTime = simpleDateFormat.parse(dateTime);
						medicationLog.setStartTime(bolusDateTime);
					}

					if (txtareaBolus.getText() != null && !txtareaBolus.getText().trim().isEmpty()) {
						medicationLog.setComments(txtareaBolus.getText().trim());
					}

				} else {
					lblError.setText(" *Please enter mandatory fields.");
					lblError.setVisible(true);
					isValidForm = false;
				}

			} else if (medicationTypeValue.equalsIgnoreCase("Infusion")) {

				if (!textSearchMedication.getText().isEmpty() && !infusion_dose.getText().isEmpty()
						&& !infusion_volume.getText().isEmpty()
						&& choiceUnit.getSelectionModel().getSelectedIndex() != 0) {

					medicationLog.setVolume(Float.valueOf(infusion_volume.getText()));

					if (datePickerStartDate.getValue() != null) {
						LocalDate localStartDate = datePickerStartDate.getValue();
						Date infusionStartTime = simpleDateFormat.parse(localStartDate + " "
								+ Main.getInstance().getUtility().getTime(hBoxInfStartTimeControl));
						medicationLog.setStartTime(infusionStartTime);
					}

					if (datePickerStopDate.getValue() != null) {
						LocalDate localStopDate = datePickerStopDate.getValue();
						Date infusionStopTime = simpleDateFormat.parse(
								localStopDate + " " + Main.getInstance().getUtility().getTime(hBoxInfStopTimeControl));
						medicationLog.setEndTime(infusionStopTime);
					}

					if (medicationLog.getStartTime() != null && medicationLog.getEndTime() != null) {

						medicationLog.setInfuseDosage(calculateInfusedDose());
					}
					medicationLog.setMedicationDosage(Float.valueOf(infusion_dose.getText()));

					if (txtareaInfusion.getText() != null && !txtareaInfusion.getText().trim().isEmpty()) {
						medicationLog.setComments(txtareaInfusion.getText().trim());
					}
					if (infusion_rate.getText() != null & !infusion_rate.getText().trim().isEmpty()) {
						medicationLog.setRateOfInfusion(infusion_rate.getText().trim());
					}

					medicationLog.setConcentration(txtConcetrInfusion.getText());
					medicationLog.setDosageUnit(choiceUnit.getSelectionModel().getSelectedItem());

				} else {
					lblError.setText("Please enter mandatory fields.");
					lblError.setVisible(true);
					isValidForm = false;
				}
			}

			if (medicationLog.getMedicationLogId() == null) {
				medicationLog
						.setCreatedBy(UserSession.getInstance().getUserWithrRolesForUserAuthentication().getUserID());
				medicationLog.setCreatedTime(new Date());
			}
		} catch (Exception e) {
			anchorLoader.setVisible(false);
			LOGGER.error("## Exception occured:" + e.getMessage());
		}
	}

	/**
	 * This method calculates final infused dosage
	 *
	 * @return calculated infused dosage
	 */
	private float calculateInfusedDose()
	{

		Date infusionStartTime = null;
		Date infusionStopTime = null;
		float infusedDose = 0f;
		if (datePickerStartDate.getValue() != null) {
			LocalDate localStartDate = datePickerStartDate.getValue();

			try {
				infusionStartTime = simpleDateFormat
						.parse(localStartDate + " " + Main.getInstance().getUtility().getTime(hBoxInfStartTimeControl));
			} catch (ParseException e) {
				LOGGER.error("## Exception occured:", e);
			}

		}

		if (datePickerStopDate.getValue() != null) {
			LocalDate localStopDate = datePickerStopDate.getValue();

			try {
				infusionStopTime = simpleDateFormat
						.parse(localStopDate + " " + Main.getInstance().getUtility().getTime(hBoxInfStopTimeControl));
			} catch (ParseException e) {
				LOGGER.error("## Exception occured:", e);
			}

		}
		if (infusionStopTime != null && infusionStopTime != null) {

			Instant startInstant = infusionStartTime.toInstant();
			LocalDateTime startLocalDateTime = LocalDateTime.ofInstant(startInstant, ZoneId.systemDefault());
			Instant stopInstant = infusionStopTime.toInstant();
			LocalDateTime stopLocalDateTime = LocalDateTime.ofInstant(stopInstant, ZoneId.systemDefault());
			long time[] = getTime(startLocalDateTime, stopLocalDateTime);
			Period period = getPeriod(startLocalDateTime, stopLocalDateTime);

			Float diffHours = (period.getDays() * 24) + time[0] + (Float.valueOf(time[1]) / 60)
					+ (Float.valueOf(time[2]) / (60 * 60));

			// Long diff = infusionStopTime.getTime() -
			// infusionStartTime.getTime();
			// Float timeDiff = Float.parseFloat(diff.toString());
			// Float diffMinutes = timeDiff / (60 * 1000) % 60;
			// Float diffHours = timeDiff / (60 * 60 * 1000) % 24;
			// Float diffDays = timeDiff / (24 * 60 * 60 * 1000);
			// diffHours = diffDays * 24 + diffHours + (diffMinutes / 60);
			Float volumeInjected = 0f;
			double dose = 0;
			double doseCal = 0;
			volumeInjected = Float.parseFloat(infusion_rate.getText()) * diffHours;

			dose = volumeInjected * Float.parseFloat(txtConcetrInfusion.getText());
			// if
			// (choiceUnit.getSelectionModel().getSelectedItem().equalsIgnoreCase("mg"))
			// {
			// doseCal = dose / 1000;
			// } else if
			// (choiceUnit.getSelectionModel().getSelectedItem().equalsIgnoreCase("g"))
			// {
			// doseCal = dose / 1000000;
			// }
			// else{
			// doseCal=dose;
			// }
			doseCal = dose / 1000;
			if (doseCal != 0) {

				BigDecimal doseDecimal = new BigDecimal(doseCal);
				BigDecimal doseRounded = doseDecimal.setScale(3, BigDecimal.ROUND_HALF_UP);
				infusedDose = Float.parseFloat(doseRounded.toString());
			}
		}

		return infusedDose;
	}

	/**
	 * This method return an array containing hours, minutes & secs
	 * between dob and now LocalDateTime objects
	 *
	 * @param dob start timestamp
	 * @param now end timestamp
	 * @return array containing hours, minutes & secs
	 */
	private static long[] getTime(LocalDateTime dob, LocalDateTime now) {
		LocalDateTime today = LocalDateTime.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth(), dob.getHour(),
				dob.getMinute(), dob.getSecond());
		Duration duration = Duration.between(today, now);

		long seconds = duration.getSeconds();

		long hours = seconds / SECONDS_PER_HOUR;
		long minutes = ((seconds % SECONDS_PER_HOUR) / SECONDS_PER_MINUTE);
		long secs = (seconds % SECONDS_PER_MINUTE);

		return new long[] { hours, minutes, secs };
	}

	/**
	 * This method returns period between dob and now LocalDateTime objects
	 *
	 * @param dob start timestamp
	 * @param now  end timestamp
	 * @return period
	 */
	private static Period getPeriod(LocalDateTime dob, LocalDateTime now) {
		return Period.between(dob.toLocalDate(), now.toLocalDate());
	}

	// public void drawMedicationPlot(IntraopMedicationlog returnedObj)
	// {
	// getTotalVolumeService = new
	// GetTotalMedicationVolumeService(textSearchMedication.getText(),
	// patient.getPatientId(), patientCase.getCaseId());
	// getTotalVolumeService.restart();
	//
	// getTotalVolumeService.setOnSucceeded(e ->
	// {
	// float totalDose = getTotalVolumeService.getTotalVolume().getValue();
	// DecimalFormat df = new DecimalFormat("###.##");
	// String totals = df.format(totalDose) + " mg";
	//
	// ControllerMediator.getInstance().getMainController().getDrawTabbedCenter().drawMedPlot(returnedObj,
	// totals,
	// returnedObj.getBolusInfusion(), infusionStopped, infusedDoseFinal);
	//
	// });
	// getTotalVolumeService.setOnFailed(e ->
	// {
	// Main.getInstance().getUtility().showNotification("Error", "Error",
	// getTotalVolumeService.getException().getMessage());
	// });
	// }

	/**
	 * This method call SaveMedicationService and saves the provided
	 * medicationLogObj
	 *
	 * @param medicationLogObj
	 */
	private void callSaveMedicationService(IntraopMedicationlog medicationLogObj) throws Exception
	{
		try {
			if (isValidForm) {

				SaveMedicationService saveMedicationService = SaveMedicationService.getInstance(medicationLogObj);
				saveMedicationService.restart();

				saveMedicationServiceSuccessHandler = new EventHandler<WorkerStateEvent>() {

					@Override
					public void handle(WorkerStateEvent event) {
						try
						{
							IntraopMedicationlog returnedObj = saveMedicationService.getReturnedMedicationLog();

							long medLogId = 0;
							try {
								if (returnedObj != null) {
									medLogId = returnedObj.getMedicationLogId();

									// drawMedicationPlot(returnedObj);
								}
							} catch (Exception e2) {
								medLogId = 0;
							}

							if (medLogId > 0) {

								lblError.setVisible(false);

								Timer timer = new Timer();
								timer.schedule(new TimerTask() {
									@Override
									public void run() {
										Platform.runLater(() -> {

											if (medSessionAvailable) {

												Main.getInstance().getUtility().showNotification("Information", "Success",
														"Medication updated successfully!");

												// Stage stage =
												// Main.getInstance().getChildStage();
												// if (stage != null) {
												// stage.close();
												// }
												Main.getInstance().getStageManager().closeSecondaryStage();
												// ControllerMediator.getInstance().getMainController()
												// .bindPopupFxml(FxmlView.MEDICATION_HISTORY);
												Main.getInstance().getStageManager()
														.switchSceneAfterMain(FxmlView.MEDICATION_HISTORY);

											} else {
												Main.getInstance().getUtility().showNotification("Information", "Success",
														"Medication saved successfully!");
											}

										});
									}
								}, 500);

							} else if (medLogId <= 0) {
								lblError.setVisible(true);
								lblError.setTextFill(Color.web("#7f0000"));
							}
							try {
								reset();
							} catch (Exception e1) {
								LOGGER.error("## Exception occured:", e1);
							}
							anchorLoader.setVisible(false);
							saveMedicationService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
									saveMedicationServiceSuccessHandler);
							saveMedicationService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
									saveMedicationServiceFailedHandler);
						}
						catch (Exception e)
						{
							LOGGER.error("## Exception occured:", e);
						}
					}

				};

				saveMedicationService.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
						saveMedicationServiceSuccessHandler);

				saveMedicationServiceFailedHandler = new EventHandler<WorkerStateEvent>() {

					@Override
					public void handle(WorkerStateEvent event) {
						try
						{
							Main.getInstance().getUtility().showNotification("Error", "Error",
									saveMedicationService.getException().getMessage());
							anchorLoader.setVisible(false);
							saveMedicationService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
									saveMedicationServiceSuccessHandler);
							saveMedicationService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
									saveMedicationServiceFailedHandler);
						}
						catch (Exception e)
						{
							LOGGER.error("## Exception occured:", e);
						}
					}

				};

				saveMedicationService.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
						saveMedicationServiceFailedHandler);
			} else {
				anchorLoader.setVisible(false);
			}
		} catch (Exception e) {
			LOGGER.error("## Exception occured:" + e.getMessage());
		}
	}

	/**
	 * This method call SearchFDAMEdService and searches for Medicine starting
	 * with searchVal
	 *
	 * @param searchVal
	 */
	private void callSearchFDAMEdService(String searchVal) throws Exception
	{
		try {
			searchFDAMedicationsService = SearchFDAMedicationsService.getInstance(searchVal);
			searchFDAMedicationsService.restart();

			searchFDAMedicationsServiceSuccessHandler = new EventHandler<WorkerStateEvent>() {

				@Override
				public void handle(WorkerStateEvent event) {
					fdamedicationsSearchListDB = searchFDAMedicationsService.getFdaMedicationsList();
					if (fdamedicationsSearchListDB.size() != 0) {
						for (Fdamedications obj : fdamedicationsSearchListDB) {

							fdamedicationsSearchList.add(obj.getMedicationsName());
						}

						medSearchList.setItems(fdamedicationsSearchList);
						medSearchList.setVisible(true);
					}
					searchFDAMedicationsService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
							searchFDAMedicationsServiceSuccessHandler);
					searchFDAMedicationsService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
							searchFDAMedicationsServiceFailedHandler);
				}

			};

			searchFDAMedicationsService.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
					searchFDAMedicationsServiceSuccessHandler);

			searchFDAMedicationsServiceFailedHandler = new EventHandler<WorkerStateEvent>() {

				@Override
				public void handle(WorkerStateEvent event) {
					Main.getInstance().getUtility().showNotification("Error", "Error",
							searchFDAMedicationsService.getException().getMessage());
					searchFDAMedicationsService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
							searchFDAMedicationsServiceSuccessHandler);
					searchFDAMedicationsService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
							searchFDAMedicationsServiceFailedHandler);
				}

			};

			searchFDAMedicationsService.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
					searchFDAMedicationsServiceFailedHandler);
		} catch (Exception e) {
			LOGGER.error("## Exception occured:" + e.getMessage());

		}
	}

	/**
	 * This method navigates to Medication History screen
	 */
//	public void navigateToMedicationHistory() throws Exception {
//		LOGGER.debug("Logger Name: " + LOGGER.getName());
//		try {
//			Scene scene = Main.getInstance().getParentScene();
//			StackPane root = (StackPane) scene.getRoot();
//			root.getChildren().remove(3);
//			root.getChildren().get(2).setVisible(true);
//
//			Stage stage = Main.getInstance().getParentStage();
//			stage.setScene(scene);
//			stage.show();
//		} catch (Exception e) {
//			LOGGER.error("## Exception occured:" + e.getMessage());
//			Main.getInstance().getUtility().showNotification("Error", "Error", "Something went wrong");
//		}
//	}

	/**
	 * This method validates the input for bolus
	 *
	 * @return true/false depending upon input satisfies all conditions
	 */
	private boolean validateForBolus()
	{
		if (bolus_dose.getText().equals("") || choiceUnitBolus.getSelectionModel().getSelectedItem() == null) {
			lblError.setText("*Please fill all the mandatory fields");
			return false;
		}
		if (!Validations.maxLength(textSearchMedication.getText(), 1999)) {
			lblError.setText("*Please enter a medication name less than 2000");
			return false;
		}
		if (!Validations.isDigit(bolus_dose.getText())) {
			lblError.setText("*Please enter a valid value for dosage");
			return false;
		}

		if (!Validations.decimalsUpto2places(bolus_dose.getText())) {
			lblError.setText("*Please enter dosage upto 4 digits and decimal upto 2 digits only");
			return false;
		}
		if (!Validations.nonZeroBigDecimal(bolus_dose.getText())) {
			lblError.setText("*Please enter dosage value more than 0");
			return false;
		}

		if (txtareaBolus.getText() != null && !txtareaBolus.getText().trim().isEmpty()
				&& !Validations.maxLength(txtareaBolus.getText(), 2000)) {
			lblError.setText("*Please enter Comments less than 2000 characters");
			return false;
		}

		/// for checking whether or not the date entered is in future
		if (datePickerBolusDate.getValue() != null) {
			try {
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				Date dateEntered = simpleDateFormat.parse(datePickerBolusDate.getValue() + " "
						+ Main.getInstance().getUtility().getTime(hBoxBolusTimeControl));
				if (Validations.futureDateCheck(dateEntered)) {
					lblError.setText("Future date is not allowed.");
					return false;
				}
			} catch (Exception e) {
				LOGGER.error("## Exception occured:", e);
			}

		}
		return true;
	}

	/**
	 * This method validates the input for infusion
	 *
	 * @return true/false depending upon input satisfies all conditions
	 */
	private boolean validateForInfusion()
	{

		if (infusion_dose.getText().equals("") || infusion_rate.getText().equals("")
		        || choiceUnit.getSelectionModel().getSelectedItem() == null || infusion_volume.getText().equals(""))
		{
			lblError.setText("*Please fill all the mandatory fields");
			return false;
		}

		if (!Validations.isDigit(infusion_dose.getText()))
		{
			lblError.setText("*Please enter a valid value for dosage");
			return false;
		}

		if (!Validations.decimalsUpto2places(infusion_dose.getText()))
		{
			lblError.setText("*Please enter dosage upto 4 digits and decimal upto 2 digits only");
			return false;
		}

		if (!Validations.nonZeroBigDecimal(infusion_dose.getText()))
		{
			lblError.setText("*Please enter dosage value more than 0");
			return false;
		}
		if (!Validations.isDigit(infusion_volume.getText()))
		{
			lblError.setText("*Volume can only have digits");
			return false;
		}
		if (Float.valueOf(infusion_rate.getText()) > 1200)
		{
			lblError.setText("*Maximum rate of infusion allowed is 1200 ml/hr.");
			return false;
		}

		if (!Validations.decimalsUpto2places(infusion_volume.getText()))
		{
			lblError.setText("*Please enter volume upto 4 digits and decimal upto 2 digits only");
			return false;
		}

		if (!Validations.nonZeroBigDecimal(infusion_volume.getText()))
		{
			lblError.setText("*Please enter volume value more than 0");
			return false;
		}

		if ((datePickerStartDate.getValue() == null)
		        || (Main.getInstance().getUtility().getTime(hBoxInfStartTimeControl) == null
		                && datePickerStartDate.getValue() != null))
		{
			lblError.setText("*Please Enter start date and time.");
			return false;
		}

		if (Main.getInstance().getUtility().getTime(hBoxBolusTimeControl) == null
		        && Main.getInstance().getUtility().getTime(hBoxInfStopTimeControl) != null)
		{

			lblError.setText("*Please Enter start date and time.");
			return false;
		}
		if (datePickerStartDate.getValue() != null && datePickerStopDate.getValue() != null)
		{
			try
			{
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

				Date infusionStartTime = simpleDateFormat.parse(datePickerStartDate.getValue() + " "
				        + Main.getInstance().getUtility().getTime(hBoxInfStartTimeControl));
				Date infusionStopTime = simpleDateFormat.parse(datePickerStopDate.getValue() + " "
				        + Main.getInstance().getUtility().getTime(hBoxInfStopTimeControl));

				Boolean result = Validations.startEndTimeCheck(infusionStartTime, infusionStopTime);
				if (!result)
				{
					lblError.setText("*Stop time should be after start time");
					return false;
				}
			}
			catch (Exception e)
			{
				LOGGER.error("## Exception occured:", e);
			}
		}

		if (Main.getInstance().getUtility().getTime(hBoxInfStartTimeControl) == null
		        && Main.getInstance().getUtility().getTime(hBoxInfStopTimeControl) != null)
		{
			lblError.setText("*Please enter start time");
			return false;
		}

		/// for checking whether or not the date entered is in future for start
		/// time
		if (datePickerStartDate.getValue() != null)
		{
			try
			{
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				Date dateEntered = simpleDateFormat.parse(datePickerStartDate.getValue() + " "
				        + Main.getInstance().getUtility().getTime(hBoxInfStartTimeControl));
				if (Validations.futureDateCheck(dateEntered))
				{
					lblError.setText("*Start date time should not be in future");
					return false;
				}
			}
			catch (Exception e)
			{
				LOGGER.error("## Exception occured:", e);
			}
		}

		/// for checking whether or not the date entered is in future for End
		/// time
		if (datePickerStopDate.getValue() != null)
		{
			try
			{
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				Date dateEntered = simpleDateFormat.parse(datePickerStopDate.getValue() + " "
				        + Main.getInstance().getUtility().getTime(hBoxInfStopTimeControl));
				if (Validations.futureDateCheck(dateEntered))
				{
					lblError.setText("*Stop date time should not be in future");
					return false;
				}
			}
			catch (Exception e)
			{
				LOGGER.error("## Exception occured:", e);
			}
		}
		if (infusion_rate.getText() != null && !infusion_rate.getText().trim().equals(""))
		{
			if (!Validations.isDigit(infusion_rate.getText()))
			{
				lblError.setText("*Rate can only have digits");
				return false;
			}

			if (!Validations.decimalsUpto2places(infusion_rate.getText()))
			{
				lblError.setText("*Please enter rate upto 4 digits and decimal upto 2 digits only");
				return false;
			}

			if (!Validations.nonZeroBigDecimal(infusion_rate.getText()))
			{
				lblError.setText("*Please enter rate value more than 0");
				return false;
			}
		}

		if (txtareaInfusion.getText() != null && !txtareaInfusion.getText().trim().isEmpty()
		        && !Validations.maxLength(txtareaInfusion.getText(), 2000))
		{
			lblError.setText("*Please enter Comments less than 2000 characters");
			return false;
		}
		return true;
	}

	/**
	 * This method resets all the textfields on the screen
	 *
	 * @throws Exception
	 */
	private void reset() throws Exception
	{
		try {
			textSearchMedication.setText("");
			bolus_dose.setText("");
			infusion_dose.setText("");
			lblTotalDose.setText("0.00 mg");
			lblSelectedMed.setText("");
			Date input = new Date();
			LocalDate date = input.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			datePickerStartDate.setValue(date);
			datePickerBolusDate.setValue(date);
			txtareaBolus.setText("");
			txtareaInfusion.setText("");
			choiceUnit.getSelectionModel().select("mg");
			choiceUnitBolus.getSelectionModel().select("mg");
			txtConcetrInfusion.setText("");
			infusion_volume.setText("");
			datePickerStopDate.setValue(null);
			infusion_rate.setText("");
			lblError.setVisible(false);
			// medicationList.getSelectionModel().clearSelection();
			resetTime();
		} catch (Exception e) {
			LOGGER.error("## Exception occured:" + e.getMessage());
		}

	}

	/**
	 * This method is used to set time in time control
	 *
	 *
	 * @param time:
	 *            local time in string
	 */
	private void setInfusionStartTime(String time) throws Exception
	{
		try {
			String[] hourMinutearray = time.split(":");
			int hour = 0;
			if (Integer.valueOf(hourMinutearray[0].toString()) < 12) {
				meridiemSpinnerInfStart.getValueFactory().setValue("AM");
				hour = Integer.valueOf(hourMinutearray[0].toString());
				if (hour == 0) {
					hour = 12;
				}
			} else {
				meridiemSpinnerInfStart.getValueFactory().setValue("PM");
				hour = Integer.valueOf(hourMinutearray[0].toString());
				if (hour != 12) {
					hour = hour - 12;
				}

			}
			if (hour < 10) {
				hourSpinnerInfStart.getValueFactory().setValue("0" + hour);
			} else {
				hourSpinnerInfStart.getValueFactory().setValue("" + hour);
			}

			minuteSpinnerInfStart.getValueFactory().setValue(hourMinutearray[1]);
		} catch (Exception e) {
			LOGGER.error("## Exception occured:" + e.getMessage());
		}
	}

	/**
	 * This method is used to set time in time control
	 *
	 *
	 * @param time:
	 *            local time in string
	 */
	private void setInfusionStopTime(String time) throws Exception
	{
		try {
			String[] hourMinutearray = time.split(":");
			int hour = 0;
			if (Integer.valueOf(hourMinutearray[0].toString()) < 12) {
				meridiemSpinnerInfStop.getValueFactory().setValue("AM");
				hour = Integer.valueOf(hourMinutearray[0].toString());
				if (hour == 0) {
					hour = 12;
				}
			} else {
				meridiemSpinnerInfStop.getValueFactory().setValue("PM");
				hour = Integer.valueOf(hourMinutearray[0].toString());
				if (hour != 12) {
					hour = hour - 12;
				}

			}
			if (hour < 10) {
				hourSpinnerInfStop.getValueFactory().setValue("0" + hour);
			} else {
				hourSpinnerInfStop.getValueFactory().setValue("" + hour);
			}

			minuteSpinnerInfStop.getValueFactory().setValue(hourMinutearray[1]);
		} catch (Exception e) {
			LOGGER.error("## Exception occured:" + e.getMessage());
		}
	}

	/**
	 * This method sets the time when bolus was provided
	 *
	 * @param time the actual time
	 * @throws Exception
	 */
	private void setBolusTime(String time) throws Exception
	{
		try {
			String[] hourMinutearray = time.split(":");
			int hour = 0;
			if (Integer.valueOf(hourMinutearray[0].toString()) < 12) {
				meridiemSpinnerBolus.getValueFactory().setValue("AM");
				hour = Integer.valueOf(hourMinutearray[0].toString());
				if (hour == 0) {
					hour = 12;
				}
			} else {
				meridiemSpinnerBolus.getValueFactory().setValue("PM");
				hour = Integer.valueOf(hourMinutearray[0].toString());
				if (hour != 12) {
					hour = hour - 12;
				}

			}
			if (hour < 10) {
				hourSpinnerBolus.getValueFactory().setValue("0" + hour);
			} else {
				hourSpinnerBolus.getValueFactory().setValue("" + hour);
			}

			minuteSpinnerBolus.getValueFactory().setValue(hourMinutearray[1]);
		} catch (Exception e) {
			LOGGER.error("## Exception occured:" + e.getMessage());
		}
	}

	/**
	 * This method reset time fields
	 *
	 * @throws Exception
	 */
	private void resetTime() throws Exception
	{
		try {
			LocalTime timeNow = Main.getInstance().getUtility().getLocalSystemTime();
			setInfusionStartTime(timeNow.toString());
			setInfusionStopTime(timeNow.toString());
			setBolusTime(timeNow.toString());
		} catch (Exception e) {
			LOGGER.error("## Exception occured:" + e.getMessage());
		}
	}

}
