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
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.cdac.common.GeneratedEntities.Deviceconfiguration;
import com.cdac.common.GeneratedEntities.Fdamedications;
import com.cdac.common.GeneratedEntities.IntraopMedicationlog;
import com.cdac.common.GeneratedEntities.Patient;
import com.cdac.common.GeneratedEntities.Patientcase;
import com.cdac.common.dao.MedicationLogDao;
import com.cdac.common.dao.MedicationLogDaoImpl;
import com.cdac.common.pojoClasses.MedicationVolumeUpdation;
import com.cdac.common.util.Validations;

import application.Main;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import mediatorPattern.ControllerMediator;
import model.ConfiguredInjectomartSession;
import model.DeviceModel;
import model.DevicesListenerStatusSession;
import model.InfusionPumpModel;
import model.InjectomartSession;
import model.PatientSession;
import services.ConfigureDeviceService;
import services.GetTotalMedicationVolumeService;
import services.SaveMedicationService;
import services.SearchFDAMedicationsService;
import services.UpdateInfusedVolumeService;
import utility.DrawTabbedCenter;

/**
 * This controller is used to assign medicine to selected Infusion pump
 *
 * @author Sudeep_Sahoo
 *
 */
public class AssignInfusionPumpController implements Initializable {

	private static final Logger LOGGER = Logger.getLogger(AssignInfusionPumpController.class.getName());

	@FXML
	private Button btnClose;

	@FXML
	private TextField txtMedicine;

	@FXML
	private TextField txtDose;

	@FXML
	private ChoiceBox<String> choiceUnit;

	@FXML
	private TextField txtVolume;

	@FXML
	private TextField txtConcentration;

	@FXML
	private Button btnSave;

	@FXML
	private ListView<String> medSearchList;

	@FXML
	private Label lblError;

	@FXML
	private Label lblSelectedDevice;

	private String selectedSearchVal = "";
	private Patient patient;
	private Patientcase patientCase;
	private Timeline timer;
	public String serialNo;
	private DeviceModel selectedDevice = new DeviceModel();
	private Map<String, IntraopMedicationlog> configuredInfPumpMedMap = new HashMap<String, IntraopMedicationlog>();
	private MedicationLogDao  medicationLogDao =  MedicationLogDaoImpl.getInstance();
	private EventHandler<MouseEvent> btnCloseHandler;

	private ChangeListener<Boolean> txtMedicineChangeListener;

	private ChangeListener<Boolean> txtVolumeChangeListener;

	private ChangeListener<Boolean> txtDoseChangeListener;

	private ChangeListener<Boolean> choiceUnitChangeListener;

	private EventHandler<MouseEvent> btnSaveHandler;

	private ChangeListener<String> medSearchListChangeListener;

	private ChangeListener<String> txtMedicine2ChangeListener;

	private EventHandler<WorkerStateEvent> searchFDAMedicationsServiceFailedHandler;

	private EventHandler<WorkerStateEvent> searchFDAMedicationsServiceSuccessHandler;

	private EventHandler<WorkerStateEvent> configureDviceServiceSuccessHandler;

	private EventHandler<WorkerStateEvent> configureDviceServiceFailedHandler;

	private EventHandler<WorkerStateEvent> getTotalVolumeServiceFailedHandler;

	private EventHandler<WorkerStateEvent> getTotalVolumeServiceSuccessHandler;

	private EventHandler<WorkerStateEvent> saveMedicationServiceFailedHandler;

	private EventHandler<WorkerStateEvent> saveMedicationServiceSuccessHandler;

	public Map<String, IntraopMedicationlog> getConfiguredInfPumpMedMap() {
		return configuredInfPumpMedMap;
	}

	public TextField getTxtMedicine() {
		return txtMedicine;
	}

	public Timeline getTimer() {
		return timer;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public DeviceModel getSelectedDevice() {
		return selectedDevice;
	}

	public void setSelectedDevice(DeviceModel selectedDevice) {
		this.selectedDevice = selectedDevice;
	}

	/**
	 * This method initializes the all Event handlers and Default values on page
	 * load
	 */

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		try {

			if (ControllerMediator.getInstance().getAssignInfusionPumpController() == null) {
				ControllerMediator.getInstance().registerAssignInfusionPumpController(this);
			}
			lblSelectedDevice.setText("Fresenius Infusion Pump");

			if (PatientSession.getInstance().getPatient() != null)
				patient = PatientSession.getInstance().getPatient();
			if (PatientSession.getInstance().getPatientCase() != null)
				patientCase = PatientSession.getInstance().getPatientCase();

			List<String> units = new ArrayList<>();
			units.add("Select");
			units.add("g");
			units.add("mg");
			String unit = "\u00B5" + "g";

			units.add(unit);
			choiceUnit.getItems().clear();
			choiceUnit.getItems().addAll(units);
			choiceUnit.getSelectionModel().select("mg");

			btnCloseHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent event) {
					closePopup();
				}
			};
			btnClose.addEventHandler(MouseEvent.MOUSE_CLICKED, btnCloseHandler);

			medSearchListChangeListener = new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldPropertyValue,
						String newValue) {
					if (newValue != null) {

						selectedSearchVal = newValue;
						txtMedicine.setText(selectedSearchVal);
						medSearchList.setVisible(false);

					}
				}
			};

			txtMedicine2ChangeListener = new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldPropertyValue,
						String newValue) {
					if (newValue.length() >= 5 && !newValue.isEmpty() && !newValue.equals("")) {
						if (!newValue.equalsIgnoreCase(selectedSearchVal)) {
							if (!Validations.maxLength(txtMedicine.getText(), 999)) {
								lblError.setText("*Please enter Medicine Name less than 1000 characters.");
								lblError.setVisible(true);

							} else {
								lblError.setVisible(false);
								medSearchList.getItems().clear();
								medSearchList.getItems().addAll(new ArrayList<>());
								callSearchFDAMEdService(newValue);
							}
						}

					} else if (newValue.length() < 5) {
						medSearchList.getSelectionModel().clearSelection();
						medSearchList.setVisible(false);
					}
				}
			};

			// ---show selected medicationSearchList item on lblSelectedMed
			// label
			medSearchList.getSelectionModel().selectedItemProperty().addListener(medSearchListChangeListener);

			txtMedicine.textProperty().addListener(txtMedicine2ChangeListener);

			txtMedicineChangeListener = new ChangeListener<Boolean>() {
				@Override
				public void changed(ObservableValue<? extends Boolean> observable, Boolean oldPropertyValue,
						Boolean newPropertyValue) {
					if (!newPropertyValue) {
						medSearchList.setVisible(false);
					}
				}
			};

			txtVolumeChangeListener = new ChangeListener<Boolean>() {
				@Override
				public void changed(ObservableValue<? extends Boolean> observable, Boolean oldPropertyValue,
						Boolean newPropertyValue) {
					if (!newPropertyValue) {

						calculateConcentration();
					}
				}
			};

			txtDoseChangeListener = new ChangeListener<Boolean>() {
				@Override
				public void changed(ObservableValue<? extends Boolean> observable, Boolean oldPropertyValue,
						Boolean newPropertyValue) {
					if (!newPropertyValue) {

						calculateConcentration();

					}
				}
			};

			choiceUnitChangeListener = new ChangeListener<Boolean>() {
				@Override
				public void changed(ObservableValue<? extends Boolean> observable, Boolean oldPropertyValue,
						Boolean newPropertyValue) {
					if (!newPropertyValue) {

						calculateConcentration();
					}
				}
			};

			txtMedicine.focusedProperty().addListener(txtMedicineChangeListener);
			txtVolume.focusedProperty().addListener(txtVolumeChangeListener);
			txtDose.focusedProperty().addListener(txtDoseChangeListener);
			choiceUnit.focusedProperty().addListener(choiceUnitChangeListener);

			btnSaveHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent event) {
					assignInfusionPump();
				}
			};
			btnSave.addEventHandler(MouseEvent.MOUSE_CLICKED, btnSaveHandler);

			if (ControllerMediator.getInstance().getAssignInfusionPumpController().getTimer() == null) {
				ControllerMediator.getInstance().getAssignInfusionPumpController().timer = new Timeline(
						new KeyFrame(Duration.seconds(60), ae -> updateDosage()));
				ControllerMediator.getInstance().getAssignInfusionPumpController().timer
						.setCycleCount(Animation.INDEFINITE);

			}
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					txtMedicine.requestFocus();
				}
			});

		} catch (Exception e) {
			LOGGER.error("## Exception occured:", e);
		}

	}

	/**
	 * This method is used to calculate concentration of doses based on volume,
	 * dose and unit
	 */
	private void calculateConcentration() {

		try {

			txtConcentration.setText("");
			if (choiceUnit.getSelectionModel().getSelectedIndex() != 0 && !txtVolume.getText().isEmpty()
					&& !txtDose.getText().isEmpty()) {
				if (validateMedicationForm()) {
					lblError.setVisible(false);
					float concentration = 0;

					if (choiceUnit.getSelectionModel().getSelectedItem().equalsIgnoreCase("mg")) {
						concentration = (Float.parseFloat(txtDose.getText()) * 1000)
								/ Float.parseFloat(txtVolume.getText());
					} else if (choiceUnit.getSelectionModel().getSelectedItem().equalsIgnoreCase("g")) {
						concentration = (Float.parseFloat(txtDose.getText()) * 1000000)
								/ Float.parseFloat(txtVolume.getText());
					} else {

						concentration = Float.parseFloat(txtDose.getText()) / Float.parseFloat(txtVolume.getText());
					}
					txtConcentration.setText(concentration + "");

				} else {
					lblError.setVisible(true);
				}
			}

		} catch (Exception e) {
			LOGGER.error("## Exception occured:", e);
		}

	}

	/**
	 * This method is used to call SearchFDAMEdService and fetches list of
	 * favorite medications
	 *
	 * @param searchVal
	 */
	private void callSearchFDAMEdService(String searchVal) {

		try {

			SearchFDAMedicationsService searchFDAMedicationsService = SearchFDAMedicationsService
					.getInstance(searchVal);
			searchFDAMedicationsService.restart();
			medSearchList.setVisible(false);
			searchFDAMedicationsServiceSuccessHandler = new EventHandler<WorkerStateEvent>() {

				@Override
				public void handle(WorkerStateEvent event) {
					List<Fdamedications> fdamedicationsSearchListDB = new ArrayList<>();
					List<String> fdamedicationsSearchList = new ArrayList<String>();
					fdamedicationsSearchListDB = searchFDAMedicationsService.getFdaMedicationsList();
					if (fdamedicationsSearchListDB != null) {
						for (Fdamedications obj : fdamedicationsSearchListDB) {

							fdamedicationsSearchList.add(obj.getMedicationsName());
						}
						if (fdamedicationsSearchList.size() != 0) {
							medSearchList.getItems().clear();
							medSearchList.getItems().addAll(fdamedicationsSearchList);
							medSearchList.setVisible(true);
						}
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

			LOGGER.error("## Exception occured:", e);
		}
	}

	/**
	 * This method is used to log Configure Device in Database
	 */
	private void assignInfusionPump() {

		try {

			if (txtMedicine.getText() != null && !txtMedicine.getText().trim().equalsIgnoreCase("")
					&& txtVolume.getText() != null && !txtVolume.getText().trim().equalsIgnoreCase("")
					&& txtDose.getText() != null && !txtDose.getText().trim().equalsIgnoreCase("")
					&& choiceUnit.getSelectionModel().getSelectedIndex() != 0) {

				if (validateMedicationForm()) {

					lblError.setVisible(false);

					Deviceconfiguration configureDeviceObj = new Deviceconfiguration();
					configureDeviceObj.setCaseId(patientCase.getCaseId());
					configureDeviceObj.setPatientId(patient.getPatientId());
					if (ControllerMediator.getInstance().getAssignInfusionPumpController().getSelectedDevice()
							.getSerialNumber() != null) {
						configureDeviceObj.setSerialNumber(ControllerMediator.getInstance()
								.getAssignInfusionPumpController().getSelectedDevice().getSerialNumber());
					}
					configureDeviceObj.setModel(ControllerMediator.getInstance().getAssignInfusionPumpController()
							.getSelectedDevice().getModel());
					if (selectedDevice.getPortNumber() != null) {
						configureDeviceObj.setPortNumber(ControllerMediator.getInstance()
								.getAssignInfusionPumpController().getSelectedDevice().getPortNumber());
					}
					if (patientCase.getSpeciality() != null) {
						configureDeviceObj.setDepartment(patientCase.getSpeciality());
					}
					if (patientCase.getoT() != null) {
						configureDeviceObj.setOt(patientCase.getoT());
					}
					configureDeviceObj.setDeviceStatus(true);
					configureDeviceObj.setMedicine(txtMedicine.getText());
					ConfigureDeviceService configureDviceService = ConfigureDeviceService
							.getInstance(configureDeviceObj);

					configureDviceService.restart();

					configureDviceServiceSuccessHandler = new EventHandler<WorkerStateEvent>() {

						@Override
						public void handle(WorkerStateEvent event) {
							String responseMsg = "";
							Map<String, Object> response = configureDviceService.getCunfiguredDevice();

							if (response != null) {

								for (Map.Entry<String, Object> obj : response.entrySet()) {

									responseMsg = obj.getValue().toString();
								}

								String[] responseArr = responseMsg.split("\\:");

								Main.getInstance().getUtility().showNotification("Information", "Success",
										"Device assigned successfully!");
								saveMedication(Integer.valueOf(responseArr[1].trim()));
							}
							configureDviceService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
									configureDviceServiceSuccessHandler);
							configureDviceService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
									configureDviceServiceFailedHandler);
						}

					};

					configureDviceService.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
							configureDviceServiceSuccessHandler);

					configureDviceServiceFailedHandler = new EventHandler<WorkerStateEvent>() {

						@Override
						public void handle(WorkerStateEvent event) {
							Main.getInstance().getUtility().showNotification("Error", "Error",
									configureDviceService.getException().getMessage());
							configureDviceService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
									configureDviceServiceSuccessHandler);
							configureDviceService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
									configureDviceServiceFailedHandler);
						}

					};

					configureDviceService.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
							configureDviceServiceFailedHandler);

					// Boolean isDrugExist = false;

					if (InjectomartSession.getInstance() != null) {

						List<InfusionPumpModel> configuredInfusionPumpList = new ArrayList<InfusionPumpModel>();

						configuredInfusionPumpList = InjectomartSession.getInstance().getConfiguredInfusionPumpList();
						if (configuredInfusionPumpList != null) {
							for (InfusionPumpModel obj : configuredInfusionPumpList) {
								if (txtMedicine.getText().equals(obj.getDrugName())) {
									lblError.setVisible(true);
									lblError.setText("* Selecetd medicine infusion is already in progress .");
									// isDrugExist = true;
									break;
								}

							}
						}

					}
				} else {
					lblError.setVisible(true);
				}
			} else {
				lblError.setVisible(true);
				lblError.setText(" * Please enter all mandatory fields. ");
			}

		} catch (Exception e) {
			LOGGER.error("## Exception occured:", e);
		}
	}

	/**
	 * This method is used to add medication to the live grid in Main Dashboard
	 *
	 * @param returnedObj
	 */

	private void addMedicationRowToGrid(IntraopMedicationlog returnedObj) {

		try {

			GetTotalMedicationVolumeService getTotalVolumeService = GetTotalMedicationVolumeService
					.getInstance(returnedObj.getMedicationName(), patient.getPatientId(), patientCase.getCaseId());
			getTotalVolumeService.restart();

			getTotalVolumeServiceSuccessHandler = new EventHandler<WorkerStateEvent>() {

				@Override
				public void handle(WorkerStateEvent event) {
					float totalDose = getTotalVolumeService.getTotalVolume().getValue();
					DecimalFormat df = new DecimalFormat("###.##");
					String totals = df.format(totalDose) + " mg";
					totals= MedicationInfusionController.calculateUnit(Float.parseFloat(totals.split(" ")[0])*1000);
					ControllerMediator.getInstance().getMainController().getDrawTabbedCenter().addNewGridRow(
							returnedObj.getMedicationName() + "|" + totals, DrawTabbedCenter.MED_GRID_NAME);
					ControllerMediator.getInstance().getMainController().getDrawTabbedCenter().getMedsFromInfPump()
							.add(returnedObj.getMedicationName());
					ControllerMediator.getInstance().getMainController().getDrawTabbedCenter().getTotalsValuesMap().put(returnedObj.getMedicationName(),totals);
					ControllerMediator.getInstance().getAssignInfusionPumpController()
							.getConfiguredInfPumpMedMap().put(ControllerMediator.getInstance()
									.getManageAvailableDevicesController().getSelectedDeviceSerialNo().trim(),
									returnedObj);

					getTotalVolumeService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
							getTotalVolumeServiceSuccessHandler);
					getTotalVolumeService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
							getTotalVolumeServiceFailedHandler);
				}

			};

			getTotalVolumeService.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
					getTotalVolumeServiceSuccessHandler);

			getTotalVolumeServiceFailedHandler = new EventHandler<WorkerStateEvent>() {

				@Override
				public void handle(WorkerStateEvent event) {
					Main.getInstance().getUtility().showNotification("Error", "Error",
							getTotalVolumeService.getException().getMessage());
					getTotalVolumeService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
							getTotalVolumeServiceSuccessHandler);
					getTotalVolumeService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
							getTotalVolumeServiceFailedHandler);
				}

			};

			getTotalVolumeService.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
					getTotalVolumeServiceFailedHandler);

		} catch (Exception e) {

			LOGGER.error("## Exception occured:", e);

		}
	}

	/**
	 * This method is used to add medication log in database . The selected
	 * infusion pump is added to configured infusion pump list session and
	 * removed from unassigned Infusion pump list session
	 *
	 * @param ConfigId
	 */
	private void saveMedication(int ConfigId) {

		try {

			if (lblSelectedDevice.getText().toString().equalsIgnoreCase("Fresenius Infusion Pump")) {
				
				medicationLogDao.settingEndTimeinLastDose(txtMedicine.getText(),  PatientSession.getInstance().getPatientCase().getCaseId(),
				        PatientSession.getInstance().getPatient().getPatientId(), Calendar.getInstance().getTime(), "Infusion",null);

				ControllerMediator.getInstance().getMainController().getDrawTabbedCenter().getInfusionStartedMap().remove(txtMedicine.getText());
				IntraopMedicationlog medicationLog = new IntraopMedicationlog();
				Calendar currentTime = Calendar.getInstance();
				currentTime.set(Calendar.SECOND, 0);
				currentTime.set(Calendar.MILLISECOND, 0);
				

				medicationLog.setPatient(patient);
				medicationLog.setPatientcase(patientCase);
				medicationLog.setMedicationDosage((Float.parseFloat(txtDose.getText())));
				medicationLog.setDosageUnit(choiceUnit.getSelectionModel().getSelectedItem());
				medicationLog.setVolume(Float.parseFloat(txtVolume.getText()));
				medicationLog.setBolusInfusion("Infusion");
				medicationLog.setMedicationName(txtMedicine.getText());
				medicationLog.setFromDevice(true);
				medicationLog.setDeviceSerialNumber(ControllerMediator.getInstance().getAssignInfusionPumpController()
						.getSelectedDevice().getSerialNumber());
				medicationLog.setConcentration(txtConcentration.getText());
				medicationLog.setRateOfInfusion(ControllerMediator.getInstance().getMainController()
						.getDrawTabbedCenter().getSyringePumpRecentInfRate());
				medicationLog.setStartTime(currentTime.getTime());
				medicationLog.setAttachedToPump(ControllerMediator.getInstance().getAssignInfusionPumpController()
						.getSelectedDevice().getModel()
						+ ControllerMediator.getInstance().getAssignInfusionPumpController().getSelectedDevice()
								.getSerialNumber());

				SaveMedicationService saveMedicationService = SaveMedicationService.getInstance(medicationLog);
				saveMedicationService.restart();

				saveMedicationServiceSuccessHandler = new EventHandler<WorkerStateEvent>() {

					@Override
					public void handle(WorkerStateEvent event) {
						IntraopMedicationlog returnedMedicationLog = new IntraopMedicationlog();
						returnedMedicationLog = saveMedicationService.getReturnedMedicationLog();
						Float infVal = Float.parseFloat(returnedMedicationLog.getConcentration())
								* Float.parseFloat(returnedMedicationLog.getRateOfInfusion().split(" ")[0]);
						infVal = (infVal) / patient.getWeight();
						infVal = (infVal)/60;
						
						String value = MedicationInfusionController.calculateUnit(infVal);
						//String rateVal[] = value.split(" ");
						ControllerMediator.getInstance().getMainController().getDrawTabbedCenter().getMedicineTotalRateMap().put(returnedMedicationLog.getMedicationName(),value);

						
						ControllerMediator.getInstance().getMainController().getDrawTabbedCenter().getMedicineRateMap().put(returnedMedicationLog.getMedicationName(), returnedMedicationLog);

						// drawMedicationPlot(returnedMedicationLog);
						addMedicationRowToGrid(returnedMedicationLog);

						if (InjectomartSession.getInstance() != null) {

							List<InfusionPumpModel> configuredInfusionPumpList = new ArrayList<InfusionPumpModel>();

							configuredInfusionPumpList = InjectomartSession.getInstance()
									.getConfiguredInfusionPumpList();
							MedicationVolumeUpdation infusionPump = new MedicationVolumeUpdation();

							if (configuredInfusionPumpList != null) {

								for (InfusionPumpModel obj : configuredInfusionPumpList) {
									if (obj.getSerialNo().equalsIgnoreCase(ControllerMediator.getInstance()
											.getAssignInfusionPumpController().getSerialNo())) {

										obj.setDrugName(txtMedicine.getText());

										infusionPump.setMedicationLogID(returnedMedicationLog.getMedicationLogId());
										infusionPump.setSerialNo(obj.getSerialNo());
										infusionPump.setConcentration(txtConcentration.getText());
										infusionPump.setDosageUnit(choiceUnit.getSelectionModel().getSelectedItem());
										infusionPump.setConfigureDeviceId(ConfigId);

									}
								}

							}

							if (ConfiguredInjectomartSession.getInstance() != null) {

								List<MedicationVolumeUpdation> configuredDeviceList = new ArrayList<MedicationVolumeUpdation>();

								configuredDeviceList = ConfiguredInjectomartSession.getInstance()
										.getConfiguredDeviceList();

								if (configuredDeviceList != null) {
									configuredDeviceList.add(infusionPump);
								} else {
									configuredDeviceList = new ArrayList<MedicationVolumeUpdation>();
									configuredDeviceList.add(infusionPump);
								}

								ConfiguredInjectomartSession.getInstance()
										.setConfiguredDeviceList(configuredDeviceList);

							}

						}

						ControllerMediator.getInstance().getAssignInfusionPumpController().timer.play();
						closePopup();

						List<DeviceModel> assigneDevicelist = new ArrayList<DeviceModel>();
						if (ConfiguredInjectomartSession.getInstance().getAssignedDeviceList() != null) {
							assigneDevicelist = ConfiguredInjectomartSession.getInstance().getAssignedDeviceList();
						}
						ControllerMediator.getInstance().getAssignInfusionPumpController().getSelectedDevice()
								.setDeviceCongigurationId(ConfigId);
						ControllerMediator.getInstance().getAssignInfusionPumpController().getSelectedDevice()
								.setMedicine(txtMedicine.getText());

						assigneDevicelist.add(
								ControllerMediator.getInstance().getAssignInfusionPumpController().getSelectedDevice());
						ConfiguredInjectomartSession.getInstance().setAssignedDeviceList(assigneDevicelist);

						List<DeviceModel> devices = new ArrayList<DeviceModel>();
						if (ConfiguredInjectomartSession.getInstance().getDetectedDeviceList() != null) {
							devices = ConfiguredInjectomartSession.getInstance().getDetectedDeviceList();
						}
						DevicesListenerStatusSession.getInstance().setInjectomatassigned(true);
						devices.remove(
								ControllerMediator.getInstance().getAssignInfusionPumpController().getSelectedDevice());
						ConfiguredInjectomartSession.getInstance().setDetectedDeviceList(devices);

						ControllerMediator.getInstance().getManageAvailableDevicesController().upadateTables();
						saveMedicationService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
								saveMedicationServiceSuccessHandler);
						saveMedicationService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
								saveMedicationServiceFailedHandler);
					}

				};

				saveMedicationService.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
						saveMedicationServiceSuccessHandler);

				saveMedicationServiceFailedHandler = new EventHandler<WorkerStateEvent>() {

					@Override
					public void handle(WorkerStateEvent event) {
						Main.getInstance().getUtility().showNotification("Error", "Error",
								saveMedicationService.getException().getMessage());
						saveMedicationService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
								saveMedicationServiceSuccessHandler);
						saveMedicationService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
								saveMedicationServiceFailedHandler);
					}

				};

				saveMedicationService.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
						saveMedicationServiceFailedHandler);
			}

		} catch (Exception e) {

			LOGGER.error("## Exception occured:", e);
		}
	}

	/**
	 * This method is used to update dosage for selected infusion pump
	 */
	public void updateDosage() {

		try {

			List<MedicationVolumeUpdation> configuredDeviceList = new ArrayList<MedicationVolumeUpdation>();
			if (ConfiguredInjectomartSession.getInstance() != null) {

				configuredDeviceList = ConfiguredInjectomartSession.getInstance().getConfiguredDeviceList();
			}
			if (configuredDeviceList != null) {
				UpdateInfusedVolumeService updateInfusedVolumeService = UpdateInfusedVolumeService
						.getInstance(configuredDeviceList);
				updateInfusedVolumeService.restart();

			}

		} catch (Exception e) {

			LOGGER.error("## Exception occured:", e);
		}
	}

	/**
	 * This method is used to remove listeners
	 */
	private void removeEventHandlers() {

		try {
			btnClose.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnCloseHandler);
			medSearchList.getSelectionModel().selectedItemProperty().removeListener(medSearchListChangeListener);
			txtMedicine.textProperty().removeListener(txtMedicine2ChangeListener);
			txtMedicine.focusedProperty().removeListener(txtMedicineChangeListener);
			txtVolume.focusedProperty().removeListener(txtVolumeChangeListener);
			txtDose.focusedProperty().removeListener(txtDoseChangeListener);
			choiceUnit.focusedProperty().removeListener(choiceUnitChangeListener);
			btnSave.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnSaveHandler);
		} catch (Exception e) {
			LOGGER.error("## Exception occured:", e);
		}
	}

	/**
	 * This method is used to close the Assign Infusion Pump Window
	 */
	private void closePopup() {
		try {
			removeEventHandlers();
			Main.getInstance().getStageManager().closeTertiaryStage();
		} catch (Exception e) {
			LOGGER.error("## Exception occured:", e);
		}

	}

	/**
	 * This method is used to validate madication form
	 *
	 * @return
	 */
	private boolean validateMedicationForm() {
		try {

			if (txtDose.getText().isEmpty() || choiceUnit.getSelectionModel().getSelectedItem() == null
					|| txtVolume.getText().isEmpty()) {
				lblError.setText("*Please fill all the mandatory fields");
				return false;
			}
			if (!Validations.maxLength(txtMedicine.getText(), 999)) {
				lblError.setText("*Please enter Medicine Name less than 1000 characters.");
				lblError.setVisible(true);
				return false;
			}
			if (!validateDoseAndVolume()) {
				return false;
			}
			return true;

		} catch (Exception e) {
			LOGGER.error("## Exception occured:", e);
			return false;
		}

	}

	/**
	 * This method is used to validate Dose and Volume entered
	 *
	 * @return
	 */
	private boolean validateDoseAndVolume() {
		try {

			if (!Validations.isDigit(txtDose.getText())) {
				lblError.setText("*Please enter a valid value for dosage");
				return false;
			}

			if (!Validations.decimalsUpto2places(txtDose.getText())) {
				lblError.setText("*Please enter dosage upto 4 digits and decimal " + "\n" + "upto 2 digits only");
				return false;
			}

			if (!Validations.nonZeroBigDecimal(txtDose.getText())) {
				lblError.setText("*Please enter dosage value more than 0");
				return false;
			}
			if (!Validations.isDigit(txtVolume.getText())) {
				lblError.setText("*Volume can only have digits");
				return false;
			}

			if (!Validations.decimalsUpto2places(txtVolume.getText())) {
				lblError.setText("*Please enter volume upto 4 digits and decimal " + "\n" + " upto 2 digits only");
				return false;
			}

			if (!Validations.nonZeroBigDecimal(txtVolume.getText())) {
				lblError.setText("*Please enter volume value more than 0");
				return false;
			}
			return true;

		} catch (Exception e) {
			LOGGER.error("## Exception occured:", e);
			return false;
		}

	}
}
