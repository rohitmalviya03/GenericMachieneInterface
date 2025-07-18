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
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.cdac.common.GeneratedEntities.Deviceconfiguration;
import com.cdac.common.GeneratedEntities.IntraopMedicationlog;
import com.cdac.common.GeneratedEntities.Patient;
import com.cdac.common.GeneratedEntities.Patientcase;
import com.cdac.common.pojoClasses.MedicationVolumeUpdation;
import com.cdac.common.pojoClasses.Volume;
import com.cdac.common.util.DAOFactory;
import com.cdac.framework.FwkServices;

import application.FxmlView;
import application.Main;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import mediatorPattern.ControllerMediator;
import model.ConfiguredInjectomartSession;
import model.DeviceModel;
import model.DevicesListenerStatusSession;
import model.PatientSession;
import model.UserSession;
import services.ConfigureDeviceService;
import services.DeviceStatusChangeService;
import services.GetTotalMedicationVolumeService;
import services.SaveMedicationService;

/**
 * ManageAvailableDevicesController.java<br>
 * Purpose:This class contains logic to scan and assign available devices
 *
 * @author Rohit_Sharma41
 *
 */
public class ManageAvailableDevicesController implements Initializable {

	@FXML
	private Button btnClose;

	@FXML
	private Button btnAssign;

	@FXML
	private Button btnScanInfusionPump;

	@FXML
	private StackPane stackPaneScan;

	@FXML
	private TableView<DeviceModel> tableDetectedDevices;

	@FXML
	private TableColumn<DeviceModel, String> clmnType;

	@FXML
	private TableColumn<DeviceModel, String> clmnSerialNo;

	@FXML
	private TableColumn<DeviceModel, String> clmnPortNo;

	@FXML
	private TableView<DeviceModel> tableAssignedDevices;

	@FXML
	private TableColumn<DeviceModel, String> clmnDeviceType;

	@FXML
	private TableColumn<DeviceModel, String> clmnDeviceSerialNo;

	@FXML
	private TableColumn<DeviceModel, String> ClmnDevicePortNo;

	@FXML
	private TableColumn<DeviceModel, String> clmnMedicineName;

	@FXML
	private FontAwesomeIconView iconDetected;

	@FXML
	private FontAwesomeIconView iconWarning;

	@FXML
	private FontAwesomeIconView iconError;

	@FXML
	private Label lblInfusion;

	@FXML
	private Button btnUnassign;

	@FXML
	private Button btnAbort;

	@FXML
	private Button btnScanS5Monitor;

	@FXML
	private Button btnScanAnesthesiaMachine;

	private DeviceModel selectedDevice = new DeviceModel();
	private DeviceModel selectedAssignedDevice = new DeviceModel();
//	private List<Label> deviceScannedList = new ArrayList<>();
	private Timeline timer;
	private List<DeviceModel> detectedDeviceList = new ArrayList<>();

	private int detectedDeviceCount = 0;
	private String selectedDeviceSerialNo;

	private Patient patient;
	private Patientcase patientCase;

	private EventHandler<MouseEvent> btnCloseHandler;

	private EventHandler<MouseEvent> btnAssignHandler;

	private EventHandler<MouseEvent> btnScanInfusionPumpHandler;

	private EventHandler<MouseEvent> btnScanS5MonitorHandler;

	private EventHandler<MouseEvent> btnScanAnesthesiaMachineHandler;

	private EventHandler<MouseEvent> btnAbortHandler;

	private EventHandler<MouseEvent> btnUnassignHandler;

	private ChangeListener<DeviceModel> tableDetectedDevicesChangeListener;

	private ChangeListener<DeviceModel> tableAssignedDevicesChangeListener;

	private EventHandler<WorkerStateEvent> configureDviceServiceSuccessHandler;

	private EventHandler<WorkerStateEvent> configureDviceServiceFailedHandler;

	private EventHandler<WorkerStateEvent> manageDeviceStatusServiceSuccessHandler;

	private EventHandler<WorkerStateEvent> manageDeviceStatusServiceFailedHandler;

	private final Logger LOG = Logger.getLogger(ManageAvailableDevicesController.class.getName());
	
	private EventHandler<WorkerStateEvent> getTotalMedicationVolumeServiceSuccessHandler;
	private EventHandler<WorkerStateEvent> getTotalMedicationVolumeServiceFailedHandler;
	private EventHandler<WorkerStateEvent> saveMedicationServiceSuccessHandler;
	private EventHandler<WorkerStateEvent> saveMedicationServiceFailedHandler;


	public String getSelectedDeviceSerialNo() {
		return selectedDeviceSerialNo;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try
		{
			clmnType.setCellValueFactory(new PropertyValueFactory<DeviceModel, String>("model"));
			clmnSerialNo.setCellValueFactory(new PropertyValueFactory<DeviceModel, String>("serialNumber"));
			clmnPortNo.setCellValueFactory(new PropertyValueFactory<DeviceModel, String>("portNumber"));
			clmnDeviceType.setCellValueFactory(new PropertyValueFactory<DeviceModel, String>("model"));
			clmnDeviceSerialNo.setCellValueFactory(new PropertyValueFactory<DeviceModel, String>("serialNumber"));
			ClmnDevicePortNo.setCellValueFactory(new PropertyValueFactory<DeviceModel, String>("portNumber"));
			clmnMedicineName.setCellValueFactory(new PropertyValueFactory<DeviceModel, String>("medicine"));

			ControllerMediator.getInstance().registerManageAvailableDevicesController(this);

			if (PatientSession.getInstance().getPatient() != null)
				patient = PatientSession.getInstance().getPatient();
			if (PatientSession.getInstance().getPatientCase() != null)
				patientCase = PatientSession.getInstance().getPatientCase();

			btnCloseHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent event) {
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

			btnAssignHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent event) {
					try
					{
						saveDeviceConfiguration();
					}
					catch (Exception e)
					{
						LOG.error("## Exception occured:", e);
					}
				}
			};
			btnAssign.addEventHandler(MouseEvent.MOUSE_CLICKED, btnAssignHandler);

			btnScanInfusionPumpHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent event) {
					try
					{
						startTimmer();

						FwkServices.getInstance()
								.setHandShakeClass("com.infosys.device.deviceInjectomat.DeviceInjectomatHandshake");
						FwkServices.getInstance().startIdentifyingDevicesAutomatically();

						Main.getInstance().getUtility().showNotification("Information", "Scanning",
								"Scanning for connected devices...");
					}
					catch (Exception e)
					{
						LOG.error("## Exception occured:", e);
					}
				}
			};
			btnScanInfusionPump.addEventHandler(MouseEvent.MOUSE_CLICKED, btnScanInfusionPumpHandler);

			btnScanS5MonitorHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent event) {
					try
					{
						startTimmer();
						DevicesListenerStatusSession.getInstance().setRemoveS5MonitorListener(false);
						// this.setRemoveS5MonitorListener(false);
						// removeS5MonitorListener = false;
						FwkServices.getInstance()
								.setHandShakeClass("com.infosys.device.s5PatientMonitor.S5PatientMonitorHandshake");
						FwkServices.getInstance().startIdentifyingDevicesAutomatically();

						Main.getInstance().getUtility().showNotification("Information", "Scanning",
								"Scanning for connected devices...");
					}
					catch (Exception e)
					{
						LOG.error("## Exception occured:", e);
					}
				}
			};
			btnScanS5Monitor.addEventHandler(MouseEvent.MOUSE_CLICKED, btnScanS5MonitorHandler);

			btnScanAnesthesiaMachineHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent event) {
					try
					{
						startTimmer();

						// removeAnesthesiaMachineListener=false;
						// this.setRemoveAnesthesiaMachineListener(false);
						DevicesListenerStatusSession.getInstance().setRemoveAnesthesiaMachineListener(false);
						FwkServices.getInstance().setHandShakeClass("com.infosys.device.anesthesia.AnesthesiaHandshake");
						FwkServices.getInstance().startIdentifyingDevicesAutomatically();

						Main.getInstance().getUtility().showNotification("Information", "Scanning",
								"Scanning for connected devices...");
					}
					catch (Exception e)
					{
						LOG.error("## Exception occured:", e);
					}
				}
			};
			btnScanAnesthesiaMachine.addEventHandler(MouseEvent.MOUSE_CLICKED, btnScanAnesthesiaMachineHandler);

			tableDetectedDevicesChangeListener = new ChangeListener<DeviceModel>() {

				@Override
				public void changed(ObservableValue<? extends DeviceModel> observable, DeviceModel oldValue,
						DeviceModel newSelection) {
					if (newSelection != null) {
						try
						{
							btnAssign.setDisable(false);
							selectedDeviceSerialNo = newSelection.getSerialNumber();
							selectedDevice = newSelection;
						}
						catch (Exception e)
						{
							LOG.error("## Exception occured:", e);
						}
					}

				}
			};

			tableAssignedDevicesChangeListener = new ChangeListener<DeviceModel>() {

				@Override
				public void changed(ObservableValue<? extends DeviceModel> observable, DeviceModel oldValue,
						DeviceModel newSelection) {
					if (newSelection != null) {
						try
						{
							btnUnassign.setDisable(false);
							selectedAssignedDevice = newSelection;
						}
						catch (Exception e)
						{
							LOG.error("## Exception occured:", e);
						}

					}

				}
			};

			// ---Enable buttons only when a table row is selected.
			tableDetectedDevices.getSelectionModel().selectedItemProperty().addListener(tableDetectedDevicesChangeListener);
			// ---Enable buttons only when a table row is selected.
			tableAssignedDevices.getSelectionModel().selectedItemProperty().addListener(tableAssignedDevicesChangeListener);

			btnAbortHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent event) {
					try
					{
						stackPaneScan.setVisible(false);
						FwkServices.getInstance().stopIdentifyingDevicesAutomatically();
						ControllerMediator.getInstance().getManageAvailableDevicesController().timer.stop();
						ControllerMediator.getInstance().getManageAvailableDevicesController().updateDetecteddeviceList();
					}
					catch (Exception e)
					{
						LOG.error("## Exception occured:", e);
					}
				}
			};
			btnAbort.addEventHandler(MouseEvent.MOUSE_CLICKED, btnAbortHandler);

			btnUnassignHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent event) {
					try
					{
						boolean status = Main.getInstance().getUtility()
								.confirmDialogue("Are you sure, you want to Unassign the selected Device ?");
						if (status) {

							List<DeviceModel> assignedDevicelist = new ArrayList<DeviceModel>();
							if (ConfiguredInjectomartSession.getInstance().getAssignedDeviceList() != null) {
								assignedDevicelist = ConfiguredInjectomartSession.getInstance().getAssignedDeviceList();
							}
							assignedDevicelist.remove(selectedAssignedDevice);
							ConfiguredInjectomartSession.getInstance().setAssignedDeviceList(assignedDevicelist);

							if (selectedAssignedDevice.getModel().equalsIgnoreCase("DeviceInjectomat")) {

								DevicesListenerStatusSession.getInstance().setRemoveInjectomatListener(true);
								DevicesListenerStatusSession.getInstance().setInjectomatassigned(false);
								IntraopMedicationlog returnedObj = new IntraopMedicationlog();
								 returnedObj=ControllerMediator.getInstance().getAssignInfusionPumpController().getConfiguredInfPumpMedMap().get(selectedAssignedDevice.getSerialNumber().trim());
								if(returnedObj != null && ControllerMediator.getInstance().getMainController().getDrawTabbedCenter().getInfusionStartedMap().containsKey( returnedObj.getMedicationName()))
								{
									 
								GetTotalMedicationVolumeService getTotalVolumeService = GetTotalMedicationVolumeService.getInstance(
								        returnedObj.getMedicationName(),
								        PatientSession.getInstance().getPatient().getPatientId(),
								        PatientSession.getInstance().getPatientCase().getCaseId());
								getTotalVolumeService.restart();

								IntraopMedicationlog returnedObj1 =returnedObj;
								getTotalMedicationVolumeServiceSuccessHandler = new EventHandler<WorkerStateEvent>()
								{
									@Override
									public void handle(WorkerStateEvent event)
									{
										try
										{
											ControllerMediator.getInstance().getMainController().getDrawTabbedCenter().getSerialNoInfusionMap().put(selectedAssignedDevice.getSerialNumber(), false);
											//Date startTime=ControllerMediator.getInstance().getMainController().getDrawTabbedCenter().getInfusionStartedMap().get(returnedObj.getMedicationName()).getTime();
											 
											ControllerMediator.getInstance().getMainController().getDrawTabbedCenter().getInfusionStartedMap().remove(returnedObj1.getMedicationName());

											float totalDose = getTotalVolumeService.getTotalVolume().getValue();
											DecimalFormat df = new DecimalFormat("###.##");
											String totals = df.format(totalDose) + " mg";

											//float infusedDosage;
											Calendar cal=Calendar.getInstance();
											cal.set(Calendar.SECOND, 0);
											cal.set(Calendar.MILLISECOND, 0);
										
											ControllerMediator.getInstance().getMainController().getDrawTabbedCenter().getTotalsValuesMap().put(returnedObj1.getMedicationName(),totals);
											
											/*infusedDosage = stopInfusionController.calculateInfusedDose(startTime, cal.getTime(),
											    infusionRate, returnedObj.getConcentration());*/
											/*ControllerMediator.getInstance().getMainController().getDrawTabbedCenter().fillMedicationGridCell(
													  "infusion stop", returnedObj.getMedicationName(),cal,String.valueOf(infusedDosage),totals);*/


											returnedObj1.setEndTime(cal.getTime());
										
											returnedObj1.setInfuseDosage((returnedObj1.getEndVol()-returnedObj1.getStartVol())*(Float.parseFloat(returnedObj1.getConcentration())/1000));

											//returnedObj.setRateOfInfusion(	ControllerMediator.getInstance().getMainController().getDrawTabbedCenter().getSyringePumpRecentInfRate());

											SaveMedicationService saveMedicationService = SaveMedicationService.getInstance(returnedObj1);
											saveMedicationService.restart();

											saveMedicationServiceSuccessHandler = new EventHandler<WorkerStateEvent>()
											{
												@Override
												public void handle(WorkerStateEvent event)
												{
													try
													{
														String totalDose;
														 Volume t1=null;
														
															t1 = DAOFactory.medicationLog().getTotalDosage(returnedObj1.getMedicationName(),returnedObj1.getPatient().getPatientId(), returnedObj1.getPatientcase().getCaseId(),  UserSession.getInstance().getUserWithrRolesForUserAuthentication().getUserID());
														
														 totalDose = t1.getValue() + t1.getUnit();
															ControllerMediator.getInstance().getMainController().getDrawTabbedCenter().getTotalsValuesMap().put(returnedObj1.getMedicationName(),totalDose);
														
														
														
															 totalDose= MedicationInfusionController.calculateUnit(Float.parseFloat(totalDose.split(" ")[0])*1000);
															 
														 ControllerMediator.getInstance().getMainController()
													        .getDrawTabbedCenter().fillMedicationGridCell("infusion stop",
													        		returnedObj1.getMedicationName(), cal,
													                MedicationInfusionController.calculateUnit((returnedObj1.getInfuseDosage())*1000), totalDose);

														ControllerMediator.getInstance().getMainController().getDrawTabbedCenter().getMedsFromInfPump()
														.remove( saveMedicationService.getReturnedMedicationLog().getMedicationName());
														

														saveMedicationService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
																saveMedicationServiceSuccessHandler);
														saveMedicationService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
																saveMedicationServiceFailedHandler);
													}
													catch (Exception e)
													{
																LOG.error("## Exception occured:", e);

													}
												}
											};
											saveMedicationService.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
													saveMedicationServiceSuccessHandler);

											saveMedicationServiceFailedHandler = new EventHandler<WorkerStateEvent>()
											{
												@Override
												public void handle(WorkerStateEvent event)
												{
													try
													{
														Main.getInstance().getUtility().showNotification("Error", "Error",
																saveMedicationService.getException().getMessage());

														saveMedicationService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
																saveMedicationServiceSuccessHandler);
														saveMedicationService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
																saveMedicationServiceFailedHandler);
													}
													catch (Exception e)
													{
																LOG.error("## Exception occured:", e);

													}
												}
											};
											saveMedicationService.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
													saveMedicationServiceFailedHandler);



											getTotalVolumeService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
													getTotalMedicationVolumeServiceSuccessHandler);
											getTotalVolumeService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
													getTotalMedicationVolumeServiceFailedHandler);
										}
										catch (Exception e)
										{
											LOG.error("## Exception occured:", e);

										}
									}
								};
								getTotalVolumeService.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
										getTotalMedicationVolumeServiceSuccessHandler);

								getTotalMedicationVolumeServiceFailedHandler = new EventHandler<WorkerStateEvent>()
								{
									@Override
									public void handle(WorkerStateEvent event)
									{
										try
										{
											Main.getInstance().getUtility().showNotification("Error", "Error",
											        getTotalVolumeService.getException().getMessage());

											getTotalVolumeService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
													getTotalMedicationVolumeServiceSuccessHandler);
											getTotalVolumeService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
													getTotalMedicationVolumeServiceFailedHandler);
										}
										catch (Exception e)
										{
													LOG.error("## Exception occured:", e);

										}
									}
								};
								getTotalVolumeService.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
										getTotalMedicationVolumeServiceFailedHandler);


								}
							
								
							} else if (selectedAssignedDevice.getModel().equalsIgnoreCase("S5PatientMonitor")) {

								DevicesListenerStatusSession.getInstance().setRemoveS5MonitorListener(true);
								DevicesListenerStatusSession.getInstance().setS5MonitorAssigned(false);
								DevicesListenerStatusSession.getInstance().setS5MonitorScanned(false);
								ControllerMediator.getInstance().getManageAvailableDevicesController().btnScanS5Monitor
										.setDisable(false);
							} else if (selectedAssignedDevice.getModel().equalsIgnoreCase("Anesthesia")) {

								DevicesListenerStatusSession.getInstance().setRemoveAnesthesiaMachineListener(true);
								DevicesListenerStatusSession.getInstance().setAnesthesiaMachineAssigned(false);
								DevicesListenerStatusSession.getInstance().setAnesthesiaMachineScanned(false);
								ControllerMediator.getInstance().getManageAvailableDevicesController().btnScanAnesthesiaMachine
										.setDisable(false);
							}
							FwkServices.getInstance().removeDeviceListenerForSelectedDevice(selectedAssignedDevice.getModel() + "-" + selectedAssignedDevice.getSerialNumber());
							disconnectDevice(selectedAssignedDevice.getDeviceCongigurationId() + "");
							upadateTables();
						}
							
					}
					catch (Exception e)
					{
						e.printStackTrace();
						LOG.error("## Exception occured:", e);
					}
				}
			};
			btnUnassign.addEventHandler(MouseEvent.MOUSE_CLICKED, btnUnassignHandler);

			if (DevicesListenerStatusSession.getInstance().isAnesthesiaMachineScanned()) {
				ControllerMediator.getInstance().getManageAvailableDevicesController().btnScanAnesthesiaMachine
						.setDisable(true);
			}
			if (DevicesListenerStatusSession.getInstance().isS5MonitorScanned()) {
				ControllerMediator.getInstance().getManageAvailableDevicesController().btnScanS5Monitor.setDisable(true);
			}
			upadateTables();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			LOG.error("## Exception occured:", e);
		}
	}

	/**
	 * This method start a scanning timeline
	 */
	private void startTimmer()
	{
		btnClose.setDisable(true);
		stackPaneScan.setVisible(true);
		timer = new Timeline(new KeyFrame(Duration.seconds(20), ae -> updateDetecteddeviceList()));
		timer.setCycleCount(Timeline.INDEFINITE);
		timer.play();
	}

	/**
	 * This method saves device configuration into database
	 */
	private void saveDeviceConfiguration()
	{

		if (selectedDevice.getModel().equalsIgnoreCase("DeviceInjectomat")) {
		//	ControllerMediator.getInstance().getMainController().bindPopupFxml(FxmlView.ASSIGN_INFUSION_PUMP);
			Main.getInstance().getStageManager().switchSceneAfterSecondary(FxmlView.ASSIGN_INFUSION_PUMP);
			ControllerMediator.getInstance().getAssignInfusionPumpController().setSerialNo(selectedDeviceSerialNo);
			ControllerMediator.getInstance().getAssignInfusionPumpController().setSelectedDevice(selectedDevice);


		} else {
			boolean status = Main.getInstance().getUtility()
					.confirmDialogue("Are you sure, you want to assign the selected Device ?");
			if (status) {
				Deviceconfiguration configureDeviceObj = new Deviceconfiguration();
				configureDeviceObj.setCaseId(patientCase.getCaseId());
				configureDeviceObj.setPatientId(patient.getPatientId());
				if (selectedDevice.getSerialNumber() != null) {
					configureDeviceObj.setSerialNumber(selectedDevice.getSerialNumber());
				}
				configureDeviceObj.setModel(selectedDevice.getModel());
				if (selectedDevice.getPortNumber() != null) {
					configureDeviceObj.setPortNumber(selectedDevice.getPortNumber());
				}

				ConfigureDeviceService configureDviceService = ConfigureDeviceService.getInstance(configureDeviceObj);

				configureDviceService.restart();

				configureDviceServiceSuccessHandler = new EventHandler<WorkerStateEvent>() {

					@Override
					public void handle(WorkerStateEvent event) {
						try
						{
							Map<String, Object> response = configureDviceService.getCunfiguredDevice();

							if (response != null) {
								String responseMsg = "";
								Main.getInstance().getUtility().showNotification("Information", "Success",
										"Device assigned successfully!");

								for (Map.Entry<String, Object> obj : response.entrySet()) {
									System.out.println(obj.getValue());
									System.out.println(obj.getKey());
									responseMsg = obj.getValue().toString();
								}

								String[] responseArr = responseMsg.split("\\:");

								if (selectedDevice.getModel().contains("S5PatientMonitor")) {

									DevicesListenerStatusSession.getInstance().setS5MonitorAssigned(true);
								} else {

									DevicesListenerStatusSession.getInstance().setAnesthesiaMachineAssigned(true);
								}

								List<DeviceModel> assignedDevicelist = new ArrayList<DeviceModel>();
								if (ConfiguredInjectomartSession.getInstance().getAssignedDeviceList() != null) {
									assignedDevicelist = ConfiguredInjectomartSession.getInstance().getAssignedDeviceList();
								}
								selectedDevice.setDeviceCongigurationId(Integer.valueOf(responseArr[1].trim()));
								assignedDevicelist.add(selectedDevice);
								ConfiguredInjectomartSession.getInstance().setAssignedDeviceList(assignedDevicelist);

								List<DeviceModel> devices = new ArrayList<DeviceModel>();
								if (ConfiguredInjectomartSession.getInstance().getDetectedDeviceList() != null) {
									devices = ConfiguredInjectomartSession.getInstance().getDetectedDeviceList();
								}
								devices.remove(selectedDevice);
								ConfiguredInjectomartSession.getInstance().setDetectedDeviceList(devices);

								upadateTables();

								selectedDevice = null;
							}
							configureDviceService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
									configureDviceServiceSuccessHandler);
							configureDviceService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
									configureDviceServiceFailedHandler);
						}
						catch (Exception e)
						{
							LOG.error("## Exception occured:", e);

						}
					}

				};

				configureDviceService.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
						configureDviceServiceSuccessHandler);

				configureDviceServiceFailedHandler = new EventHandler<WorkerStateEvent>() {

					@Override
					public void handle(WorkerStateEvent event) {
						try
						{
							Main.getInstance().getUtility().showNotification("Error", "Error",
									configureDviceService.getException().getMessage());
							configureDviceService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
									configureDviceServiceSuccessHandler);
							configureDviceService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
									configureDviceServiceFailedHandler);
						}
						catch (Exception e)
						{
							LOG.error("## Exception occured:", e);
						}
					}

				};

				configureDviceService.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
						configureDviceServiceFailedHandler);
			}
		}

	}

	/**
	 * This method updates the tabluar views visible on the screen
	 */
	public void upadateTables()
	{

		btnAssign.setDisable(true);
		btnUnassign.setDisable(true);
		tableAssignedDevices.getSelectionModel().clearSelection();
		tableDetectedDevices.getSelectionModel().clearSelection();

		List<DeviceModel> devices = new ArrayList<DeviceModel>();
		if (ConfiguredInjectomartSession.getInstance().getDetectedDeviceList() != null) {
			devices = ConfiguredInjectomartSession.getInstance().getDetectedDeviceList();
		}
		tableDetectedDevices.getItems().clear();
		tableDetectedDevices.getItems().addAll(devices);
//		tableDetectedDevices.setItems(FXCollections.observableArrayList(devices));
		List<DeviceModel> assignedDevicelist = new ArrayList<DeviceModel>();
		if (ConfiguredInjectomartSession.getInstance().getAssignedDeviceList() != null) {
			assignedDevicelist = ConfiguredInjectomartSession.getInstance().getAssignedDeviceList();
		}
//		tableAssignedDevices.setItems(FXCollections.observableArrayList(assignedDevicelist));
		tableAssignedDevices.getItems().clear();
		tableAssignedDevices.getItems().addAll(assignedDevicelist);
	}

	/**
	 * This method updates the listview of detected devices on the screen
	 */
	private void updateDetecteddeviceList()
	{

		try
		{
			btnClose.setDisable(false);
			FwkServices.getInstance().stopIdentifyingDevicesAutomatically();
			ControllerMediator.getInstance().getManageAvailableDevicesController().stackPaneScan.setVisible(false);
			Main.getInstance().getUtility().showNotification("Information", "Scanning", "Scanning Completed...");

			ControllerMediator.getInstance().getManageAvailableDevicesController().timer.stop();
			List<DeviceModel> devices = new ArrayList<DeviceModel>();
			if (ConfiguredInjectomartSession.getInstance().getDetectedDeviceList() != null) {
				devices = ConfiguredInjectomartSession.getInstance().getDetectedDeviceList();
			}
//		tableDetectedDevices.setItems(FXCollections.observableArrayList(devices));
			tableDetectedDevices.getItems().clear();
			tableDetectedDevices.getItems().addAll(devices);

			if (detectedDeviceCount != 0) {
				lblInfusion.setText(detectedDeviceCount + " device found.");
				iconDetected.setVisible(true);
				lblInfusion.setVisible(true);
			}
		}
		catch (Exception e)
		{
			LOG.error("## Exception occured:", e);
		}

	}

	/**
	 * This method fetches scanned devices
	 *
	 * @param deviceName connected device name
	 */
	public void getScannedDevices(String deviceName)
	{

		try
		{
			detectedDeviceCount += 1;

			System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$" + deviceName + "$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
			String deviceDetails[] = deviceName.split("-");
			DeviceModel device = new DeviceModel();
			device.setModel(deviceDetails[0]);
			device.setSerialNumber(deviceDetails[1]);
			device.setPortNumber("NA");
			ControllerMediator.getInstance().getManageAvailableDevicesController().detectedDeviceList.add(device);
			ConfiguredInjectomartSession.getInstance().setDetectedDeviceList(
					ControllerMediator.getInstance().getManageAvailableDevicesController().detectedDeviceList);
//		deviceScannedList.add(new Label(deviceName));
			if (!deviceName.contains("DeviceInjectomat")) {

				if (deviceName.contains("S5PatientMonitor")) {
					ControllerMediator.getInstance().getManageAvailableDevicesController().btnScanS5Monitor
							.setDisable(true);
					DevicesListenerStatusSession.getInstance().setS5MonitorScanned(true);
				} else {
					ControllerMediator.getInstance().getManageAvailableDevicesController().btnScanAnesthesiaMachine
							.setDisable(true);
					DevicesListenerStatusSession.getInstance().setAnesthesiaMachineScanned(true);
				}
				ControllerMediator.getInstance().getManageAvailableDevicesController().updateDetecteddeviceList();
			} else {

				try {

					ControllerMediator.getInstance().getManageAvailableDevicesController().timer.stop();
					ControllerMediator.getInstance().getManageAvailableDevicesController().timer
							.playFrom(new Duration(5000));

				} catch (Throwable th) {

					th.printStackTrace();
				}
			}
		}
		catch (Exception e)
		{
			LOG.error("## Exception occured:", e);

		}

	}

	/**
	 * This method disconnect a device and update its status in the database
	 *
	 * @param deviceConfigurationId configuration ID for the device
	 */
	private void disconnectDevice(String deviceConfigurationId)
	{
		DeviceStatusChangeService manageDeviceStatusService = DeviceStatusChangeService.getInstance(Integer.valueOf(deviceConfigurationId),
				false);
		manageDeviceStatusService.restart();

		manageDeviceStatusServiceSuccessHandler = new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent event) {
				try
				{
					try
					{
						List<MedicationVolumeUpdation> configuredDeviceList = new ArrayList<MedicationVolumeUpdation>();
						if (ConfiguredInjectomartSession.getInstance() != null) {

							configuredDeviceList = ConfiguredInjectomartSession.getInstance().getConfiguredDeviceList();
						}
						if (configuredDeviceList != null) {
							for (MedicationVolumeUpdation obj : configuredDeviceList) {
								if (obj.getConfigureDeviceId() == selectedAssignedDevice.getDeviceCongigurationId()) {
									configuredDeviceList.remove(obj);
									selectedAssignedDevice = null;

									break;
								}
							}
							ConfiguredInjectomartSession.getInstance().setConfiguredDeviceList(configuredDeviceList);
						}
						manageDeviceStatusService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
								manageDeviceStatusServiceSuccessHandler);
						manageDeviceStatusService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
								manageDeviceStatusServiceFailedHandler);
					}
					catch (Exception e)
					{
						e.printStackTrace();
						LOG.error("## Exception occured:", e);
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
					LOG.error("## Exception occured:", e);
				}
			}

		};

		manageDeviceStatusService.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
				manageDeviceStatusServiceSuccessHandler);

		manageDeviceStatusServiceFailedHandler = new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent event) {
				try
				{
					Main.getInstance().getUtility().showNotification("Error", "Error",
							manageDeviceStatusService.getException().getMessage());
					manageDeviceStatusService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
							manageDeviceStatusServiceSuccessHandler);
					manageDeviceStatusService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
							manageDeviceStatusServiceFailedHandler);
				}
				catch (Exception e)
				{
					LOG.error("## Exception occured:", e);
				}
			}

		};

		manageDeviceStatusService.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
				manageDeviceStatusServiceFailedHandler);
	}

	/**
	 * This method handles removed devices
	 *
	 * @param deviceName name of the device
	 */
	public void deviceRemoved(String deviceName)
	{
		try
		{
			Boolean isFound = false;
			String serialNo = "";
			long deviceConfigId = 0;
			String deviceDetails[] = deviceName.split("-");
			if (deviceName.contains("DeviceInjectomat")) {
				serialNo = deviceDetails[1];
			}

			List<DeviceModel> devices = new ArrayList<DeviceModel>();
			if (ConfiguredInjectomartSession.getInstance().getDetectedDeviceList() != null) {
				devices = ConfiguredInjectomartSession.getInstance().getDetectedDeviceList();
			}

			List<DeviceModel> assignedDevicelist = new ArrayList<DeviceModel>();
			if (ConfiguredInjectomartSession.getInstance().getAssignedDeviceList() != null) {
				assignedDevicelist = ConfiguredInjectomartSession.getInstance().getAssignedDeviceList();
			}

			if (serialNo != "" && serialNo != "0") {
				if (assignedDevicelist.size() != 0) {
					for (DeviceModel assignedDeviceObj : assignedDevicelist) {
						if (assignedDeviceObj.getSerialNumber().equalsIgnoreCase(serialNo)) {

							List<MedicationVolumeUpdation> configuredInjectomatDeviceList = new ArrayList<MedicationVolumeUpdation>();
							if (ConfiguredInjectomartSession.getInstance() != null) {

								configuredInjectomatDeviceList = ConfiguredInjectomartSession.getInstance()
										.getConfiguredDeviceList();
							}
							if (configuredInjectomatDeviceList != null) {
								for (MedicationVolumeUpdation injectomatObj : configuredInjectomatDeviceList) {
									if (injectomatObj.getSerialNo().equalsIgnoreCase(serialNo)) {
										configuredInjectomatDeviceList.remove(injectomatObj);
										break;
									}
								}
								ConfiguredInjectomartSession.getInstance()
										.setConfiguredDeviceList(configuredInjectomatDeviceList);
							}

							isFound = true;
							assignedDevicelist.remove(assignedDeviceObj);
							deviceConfigId = assignedDeviceObj.getDeviceCongigurationId();
							disconnectDevice(deviceConfigId + "");
							break;
						}
					}

				}
				if (!isFound) {
					if (devices.size() != 0) {
						for (DeviceModel detectedDeviceObj : devices) {
							if (detectedDeviceObj.getSerialNumber().equalsIgnoreCase(serialNo)) {
								isFound = true;
								devices.remove(detectedDeviceObj);
								deviceConfigId = detectedDeviceObj.getDeviceCongigurationId();
								break;
							}
						}
					}
				}

			} else {
				if (assignedDevicelist.size() != 0) {
					for (DeviceModel assignedDeviceObj : assignedDevicelist) {
						if (assignedDeviceObj.getModel().equalsIgnoreCase(deviceDetails[0])) {
							isFound = true;
							assignedDevicelist.remove(assignedDeviceObj);
							deviceConfigId = assignedDeviceObj.getDeviceCongigurationId();
							disconnectDevice(deviceConfigId + "");
							break;
						}
					}

				}
				if (!isFound) {
					if (devices.size() != 0) {
						for (DeviceModel detectedDeviceObj : devices) {
							if (detectedDeviceObj.getModel().equalsIgnoreCase(deviceDetails[0])) {
								isFound = true;
								devices.remove(detectedDeviceObj);
								deviceConfigId = detectedDeviceObj.getDeviceCongigurationId();
								break;
							}
						}
					}
				}
			}

			if (isFound) {
				if (deviceDetails[0].equalsIgnoreCase("DeviceInjectomat")) {

					DevicesListenerStatusSession.getInstance().setInjectomatassigned(false);
					DevicesListenerStatusSession.getInstance().setRemoveInjectomatListener(true);

				} else if (deviceDetails[0].equalsIgnoreCase("S5PatientMonitor")) {

					DevicesListenerStatusSession.getInstance().setS5MonitorAssigned(false);
					DevicesListenerStatusSession.getInstance().setRemoveS5MonitorListener(true);
					ControllerMediator.getInstance().getManageAvailableDevicesController().btnScanS5Monitor
							.setDisable(false);
					DevicesListenerStatusSession.getInstance().setS5MonitorScanned(false);
				} else if (deviceDetails[0].equalsIgnoreCase("Anesthesia")) {
					DevicesListenerStatusSession.getInstance().setAnesthesiaMachineAssigned(false);
					DevicesListenerStatusSession.getInstance().setRemoveAnesthesiaMachineListener(true);
					ControllerMediator.getInstance().getManageAvailableDevicesController().btnScanAnesthesiaMachine
							.setDisable(false);
					DevicesListenerStatusSession.getInstance().setAnesthesiaMachineScanned(false);
				}
				ConfiguredInjectomartSession.getInstance().setAssignedDeviceList(assignedDevicelist);
				ConfiguredInjectomartSession.getInstance().setDetectedDeviceList(devices);
				upadateTables();
			}
		}
		catch (Exception e)
		{
			LOG.error("## Exception occured:", e);

		}

	}

	/**
	 * This method removes any active event handlers from the components
	 */
	private void removeEventHandlers()
	{
		tableDetectedDevices.getSelectionModel().selectedItemProperty().removeListener(tableDetectedDevicesChangeListener);
		tableAssignedDevices.getSelectionModel().selectedItemProperty().removeListener(tableAssignedDevicesChangeListener);
		btnClose.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnCloseHandler);
		btnAssign.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnAssignHandler);
		btnScanInfusionPump.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnScanInfusionPumpHandler);
		btnScanS5Monitor.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnScanS5MonitorHandler);
		btnScanAnesthesiaMachine.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnScanAnesthesiaMachineHandler);
		btnAbort.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnAbortHandler);
		btnUnassign.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnUnassignHandler);
		timer=null;
	}

	/**
	 * This method closes current screen
	 */
	private void closePopup()
	{
		removeEventHandlers();
		Main.getInstance().getStageManager().closeSecondaryStage();
	}

}
