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
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.cdac.common.GeneratedEntities.AuditIntraOpReport1;
import com.cdac.common.GeneratedEntities.AuditIntraOpReport2;
import com.cdac.common.GeneratedEntities.AuditReport;
import com.cdac.common.GeneratedEntities.CPBDetails;
import com.cdac.common.GeneratedEntities.Cannulations;
import com.cdac.common.GeneratedEntities.EchoDetails;
import com.cdac.common.GeneratedEntities.Fluidvalue;
import com.cdac.common.GeneratedEntities.IcuPlanEntity;
import com.cdac.common.GeneratedEntities.IntraopAnesthesiamedicationrecord;
import com.cdac.common.GeneratedEntities.IntraopAnesthesiarecord;
import com.cdac.common.GeneratedEntities.IntraopEventlog;
import com.cdac.common.GeneratedEntities.IntraopOutput;
import com.cdac.common.GeneratedEntities.Patient;
import com.cdac.common.GeneratedEntities.Patientcase;
import com.cdac.common.GeneratedEntities.Recoveryroomreadings;
import com.cdac.common.dao.GetMasterDataDaoImpl;
import com.cdac.common.dao.MedicationLogDao;
import com.cdac.common.dao.MedicationLogDaoImpl;
import com.cdac.common.pojoClasses.AnesthesiaFetchListWithMedication;
import com.cdac.common.pojoClasses.InvestigationMapperAndValue;
import com.cdac.common.pojoClasses.InvestigationSetValue;
import com.cdac.common.util.AuditReportParams;
import com.cdac.common.util.DAOFactory;
import com.cdac.common.util.Validations;

import application.FxmlView;
import application.Main;
import javafx.application.Platform;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import mediatorPattern.ControllerMediator;
import model.AldreteScoreSession;
import model.ConfigureDeviceSession;
import model.ExecutedEventSession;
import model.OtherTestModel;
import model.PatientSession;
import model.UserSession;
import services.AddAuditReportService;
import services.AddEventService;
import services.GetAuditReportService;
import services.GetPostOpDetailsService;
import services.GetTotalFluidVolumeService;
import services.GetTotalMedicationVolumeListService;
import services.PostOpSaveService;

/**
 * PostOperativeController.java<br>
 * Purpose:This class contains logic to handle Shift Out screen inputs
 *
 * @author Rohit_Sharma41
 *
 */
public class PostOperativeController implements Initializable {

	private static final Logger LOGGER = Logger.getLogger(PostOperativeController.class.getName());

	@FXML
	private TextField txtLowerBp;

	@FXML
	private TextField txtUpperBp;

	@FXML
	private Button btnClose;

	@FXML
	private TextField txtPACUBp;

	@FXML
	private TextField txtPACUPulse;

	@FXML
	private TextField txtPACUSpO2;

	@FXML
	private TextField txtPACUResp;

	@FXML
	private TextField txtPACUTemp;

	@FXML
	private ChoiceBox<String> choiceRespSupport;

	@FXML
	private DatePicker datePACU;

	// @FXML
	// private CheckBox checkPACUInoTrops;

	@FXML
	private TextArea txtPACUInoTropDetails;

	// @FXML
	// private CheckBox checkPACUBlood;

	@FXML
	private TextArea txtPACUBloodDetails;

	// @FXML
	// private CheckBox checkPACUIvFluids;

	@FXML
	private TextArea txtPACUIVFluidsDetails;

	// @FXML
	// private CheckBox checkPACUInfusion;

	@FXML
	private TextArea txtPACUInfusionDetails;

	@FXML
	private TextField txtPACUAlderateScore;

	@FXML
	private Button btnPACUScore;

	private MedicationLogDao  medicationLogDao =  MedicationLogDaoImpl.getInstance();
	// @FXML
	// private Tab tabShiftingOut;

	// @FXML
	// private TextField txtOutBp;

	// @FXML
	// private TextField txtOutPulse;
	//
	// @FXML
	// private TextField txtOutSpO2;
	//
	// @FXML
	// private TextField txtOutResp;
	//
	// @FXML
	// private TextField txtOutTemp;

	// @FXML
	// private DatePicker dateOut;

	// @FXML
	// private TextField txtOutAlderateScore;

	// @FXML
	// private Button btnOutScore;

	// @FXML
	// private CheckBox checkOutInstruction;

	@FXML
	private TextArea txtOutInstructionDetails;

	// @FXML
	// private CheckBox checkOutInvestigation;
	//
	// @FXML
	// private TextField txtOutIninvestigationDeatils;
	//
	// @FXML
	// private CheckBox checkOutConsulation;
	//
	// @FXML
	// private TextField txtOutConsultaionDeatils;
	//
	// @FXML
	// private CheckBox checkOutDrug;
	//
	// @FXML
	// private TextField txtOutDrugDeatils;
	//
	// @FXML
	// private CheckBox checkOutComplication;
	//
	// @FXML
	// private TextField txtOutCompilationDeatils;

	@FXML
	private Button btnSave;

	@FXML
	private Label lblErrorMsg;

	// @FXML
	// private VBox vbOUTTimeControl;
	//
	// @FXML
	// private Button btnOutTime;

	@FXML
	private VBox vbPACUTimeControl;

	@FXML
	private Button btnTime;

	@FXML
	private Button btnCaptureDevice;
	@FXML
	private AnchorPane disablePaneMainContent;

	// changes done by Rohi
	@FXML
	private ChoiceBox<String> choicePacingSupport;
	@FXML
	private ChoiceBox<String> choiceLeads;

	@FXML
	private ChoiceBox<String> choiceMode;

	@FXML
	private ChoiceBox<String> choiceNegativeSuction;

	@FXML
	private ChoiceBox<String> choiceBleedingIssues;

	@FXML
	private ChoiceBox<String> choicePleural;

	@FXML
	private TextField textInotropicScore;

	@FXML
	private TextField textBeatsPerMinute;

	@FXML
	private ChoiceBox<String> choiceBA;

	@FXML
	private ChoiceBox<String> choiceRsPc;

	// change end
	public TextField getTxtPACUAlderateScore() {
		return txtPACUAlderateScore;
	}

	public void setTxtPACUAlderateScore(TextField txtPACUAlderateScore) {
		this.txtPACUAlderateScore = txtPACUAlderateScore;
	}

	private Recoveryroomreadings postOpReadings = new Recoveryroomreadings();
	private Recoveryroomreadings postOpDetails = new Recoveryroomreadings();;

	private List<String> respSupportList = new ArrayList<String>();

	// changes done by Rohi
	private List<String> pacingSupportList = new ArrayList<String>();

	private List<String> leadsList = new ArrayList<String>();
	private List<String> modeList = new ArrayList<String>();
	private List<String> negativeSuctionList = new ArrayList<String>();
	private List<String> bleedingIssuesList = new ArrayList<String>();
	private List<String> pleuralList = new ArrayList<String>();
	private List<String> back_actList = new ArrayList<String>();
	private List<String> rs_pcList = new ArrayList<String>();

	// changes end
	// ("Select", "Ventilator",;

	// "T piece", "Ventimask");

	private Patient patient;
	private Patientcase patientCase;
	private boolean checkForShiftingOut = true;
	private Button btnEvent;
	private Boolean isFromDasboard = false;

	// changes made by vivek
	
	// changes end

	public Boolean getIsFromDasboard() {
		return isFromDasboard;
	}

	public void setIsFromDasboard(Boolean isFromDasboard) {
		this.isFromDasboard = isFromDasboard;
	}

	public Button getBtnEvent() {
		return btnEvent;
	}

	public void setBtnEvent(Button btnEvent) {
		this.btnEvent = btnEvent;
	}

	Spinner<String> hourSpinnerPACU = new Spinner<>();
	Spinner<String> minuteSpinnerPACU = new Spinner<>();
	Spinner<String> meridiemSpinnerPACU = new Spinner<>();
	private HBox hBoxPACUTimeControl;

	Spinner<String> hourSpinnerOUT = new Spinner<>();
	Spinner<String> minuteSpinnerOUT = new Spinner<>();
	Spinner<String> meridiemSpinnerOUT = new Spinner<>();
	private HBox hBoxOUTTimeControl;

	private float AXCTime, CPBTime, SurgeryTime, Anesthesiatime, HandOverTime, PatientPrepationTime, ShiftingOutTime,
			OTTime;
	private float TotalFentanyl, TotalPropofol, TotalKetamine, TotalVecuronium, TotalCefuroxime, TotalHeparin,
			TotalProtamine, TotalEACA, TotalMeropenam, TotalVancomysin, TotalMorphine, TotalSufentanyl, TotalEtomidate,
			TotalAtrcurium, TotalCalciumGluconate, TotalCalciumChloride, TotalDexamethasone, TotalHydrocortisone,
			TotalMagnesium, TotalXylocard, TotalPhenylephrine, TotalAtropine, TotalAdrenaline;
	private float totalNormalSaline, totalRingerLactate;

	private EventHandler<MouseEvent> btnCloseHandler;

	private EventHandler<MouseEvent> btnSaveHandler;

	private EventHandler<MouseEvent> btnPACUScoreHandler;

	private EventHandler<MouseEvent> btnTimeHandler;

	private EventHandler<MouseEvent> btnCaptureDeviceHandler;

	private EventHandler<WorkerStateEvent> getPostOpDeatilsServiceSuccessHandler;

	private EventHandler<WorkerStateEvent> getPostOpDeatilsServiceFailedHandler;

	private EventHandler<WorkerStateEvent> postOpSaveServiceFailedHandler;

	private EventHandler<WorkerStateEvent> postOpSaveServiceSuccessHandler;

	private EventHandler<WorkerStateEvent> saveEventServiceSuccessHandler;
	private EventHandler<WorkerStateEvent> saveEventServiceFailedHandler;

	private EventHandler<WorkerStateEvent> getAuditReportServiceSuccessHandler;
	private EventHandler<WorkerStateEvent> getAuditReportServiceFailedHandler;

	// changes end

	private EventHandler<WorkerStateEvent> addAuditReportServiceSuccessHandler;
	private EventHandler<WorkerStateEvent> addAuditReportServiceFailedHandler;

	private EventHandler<WorkerStateEvent> getMedTotalsServiceSuccessHandler;
	private EventHandler<WorkerStateEvent> getMedTotalsServiceFailedHandler;

	private EventHandler<WorkerStateEvent> getFluidTotalsServiceSuccessHandler;
	private EventHandler<WorkerStateEvent> getFluidTotalsServiceFailedHandler;

	

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {

			if (UserSession.getInstance().getUserWithrRolesForUserAuthentication().getUserID().equals("admin")) {

				saveAllCasesData();

			}

			respSupportList.add("Select");
			respSupportList.add("Ventilator");
			respSupportList.add("T piece");
			respSupportList.add("Ventimask");
			// changes done by Rohi
			pacingSupportList.add("Select");
			pacingSupportList.add("Yes");
			pacingSupportList.add("No");

			leadsList.add("Select");
			leadsList.add("A");
			leadsList.add("V");
			leadsList.add("A+V");

			modeList.add("Select");
			modeList.add("Asynchronous");
			modeList.add("Demand");
			modeList.add("AV sequential");
			modeList.add("DDD");

			negativeSuctionList.add("Select");
			negativeSuctionList.add("Yes");
			negativeSuctionList.add("No");

			bleedingIssuesList.add("Select");
			bleedingIssuesList.add("Yes");
			bleedingIssuesList.add("No");

			pleuralList.add("Select");
			pleuralList.add("Right");
			pleuralList.add("Left");

			back_actList.add("Select");
			back_actList.add("Backup");
			back_actList.add("Active");

			rs_pcList.add("Select");
			rs_pcList.add("RS");
			rs_pcList.add("PC");

			ControllerMediator.getInstance().registerPostOperativeController(this);

			// ---- PACU Time control
			hBoxPACUTimeControl = Main.getInstance().getUtility().getTimeControl();
			vbPACUTimeControl.getChildren().add(hBoxPACUTimeControl);
			hourSpinnerPACU = (Spinner) hBoxPACUTimeControl.getChildren().get(0);
			minuteSpinnerPACU = (Spinner) hBoxPACUTimeControl.getChildren().get(2);
			meridiemSpinnerPACU = (Spinner) hBoxPACUTimeControl.getChildren().get(3);

			// ---- OUT Time control
			hBoxOUTTimeControl = Main.getInstance().getUtility().getTimeControl();
			// vbOUTTimeControl.getChildren().add(hBoxOUTTimeControl);
			hourSpinnerOUT = (Spinner) hBoxOUTTimeControl.getChildren().get(0);
			minuteSpinnerOUT = (Spinner) hBoxOUTTimeControl.getChildren().get(2);
			meridiemSpinnerOUT = (Spinner) hBoxOUTTimeControl.getChildren().get(3);

			lblErrorMsg.setVisible(false);
			choiceRespSupport.getItems().clear();
			choiceRespSupport.getItems().addAll(respSupportList);
			choiceRespSupport.getSelectionModel().select(0);
			// changes done by Rohi
			choicePacingSupport.getItems().clear();
			choicePacingSupport.getItems().addAll(pacingSupportList);
			choicePacingSupport.getSelectionModel().select(0);

			choiceLeads.getItems().clear();
			choiceLeads.getItems().addAll(leadsList);
			choiceLeads.getSelectionModel().select(0);

			choiceMode.getItems().clear();
			choiceMode.getItems().addAll(modeList);
			choiceMode.getSelectionModel().select(0);

			choiceNegativeSuction.getItems().clear();
			choiceNegativeSuction.getItems().addAll(negativeSuctionList);
			choiceNegativeSuction.getSelectionModel().select(0);

			choiceBleedingIssues.getItems().clear();
			choiceBleedingIssues.getItems().addAll(bleedingIssuesList);
			choiceBleedingIssues.getSelectionModel().select(0);

			choicePleural.getItems().clear();
			choicePleural.getItems().addAll(pleuralList);
			choicePleural.getSelectionModel().select(0);

			choiceBA.getItems().clear();
			choiceBA.getItems().addAll(back_actList);
			choiceBA.getSelectionModel().select(0);

			choiceRsPc.getItems().clear();
			choiceRsPc.getItems().addAll(rs_pcList);
			choiceRsPc.getSelectionModel().select(0);

			if (PatientSession.getInstance().getPatient() != null) {
				patient = PatientSession.getInstance().getPatient();
			}
			int upperRange = 0;
			int lowerRange = 0;
			int category = 0;
			try {
				category = calculateAgeCategory();
			} catch (Exception e) {

			}

			if (category == 0) // Adult
			{
				upperRange = 80;
				lowerRange = 65;
			} else if (category == 1) // Child
			{
				upperRange = 60;
				lowerRange = 50;
			} else if (category == 2) // Infant
			{
				upperRange = 55;
				lowerRange = 45;
			} else if (category == 3) // Neonate
			{
				upperRange = 50;
				lowerRange = 40;
			}

			txtUpperBp.setText(upperRange + "");
			txtLowerBp.setText(lowerRange + "");

			if (PatientSession.getInstance().getPatientCase() != null) {
				patientCase = PatientSession.getInstance().getPatientCase();
			}
			// postOpReadings = new Recoveryroomreadings();
			List<String> medList = GetMasterDataDaoImpl.getInstance().getMedicationsForHistory(patientCase.getCaseId(),
					UserSession.getInstance().getUserWithrRolesForUserAuthentication().getUserName());
//			medList.add("Fentanyl");
//			medList.add("Propofol");
//			medList.add("Ketamine");
//			medList.add("Vecuronium");
//			medList.add("Cefuroxime");
//			medList.add("Heparin");
//			medList.add("Protamine");
//			medList.add("EACA");
			if (medList.size() == 0)
				medList.add("testing");
			getTotalMedicationVolumeList(medList);

			List<String> fluidList = GetMasterDataDaoImpl.getInstance().getFluidsForHistory(patientCase.getCaseId(),
					UserSession.getInstance().getUserWithrRolesForUserAuthentication().getUserName());
			if (fluidList.size() == 0)
				fluidList.add("testing");
			getFluidTotalsListService(fluidList);

			btnCloseHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent event) {
					try {
						removeEventHandlers();
						Main.getInstance().getStageManager().closeSecondaryStage();
					} catch (Exception e1) {
						LOGGER.error("## Exception occured:", e1);
					}
				}
			};
			btnClose.addEventHandler(MouseEvent.MOUSE_CLICKED, btnCloseHandler);

			btnCaptureDeviceHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent event) {
					try {
						if (!ConfigureDeviceSession.getInstance().getLastDeviceReadings().isEmpty()) {
							txtPACUBp.setText(ConfigureDeviceSession.getInstance().getLastDeviceReadings().get("IBP"));
							txtPACUPulse
									.setText(ConfigureDeviceSession.getInstance().getLastDeviceReadings().get("HR"));
							txtPACUSpO2.setText(ConfigureDeviceSession.getInstance().getLastDeviceReadings().get("SP"));
							txtPACUResp.setText(ConfigureDeviceSession.getInstance().getLastDeviceReadings().get("RR"));
							txtPACUTemp
									.setText(ConfigureDeviceSession.getInstance().getLastDeviceReadings().get("TEMP"));
						} else {
							Main.getInstance().getUtility()
									.confirmDialogue("No device active. Please fill data manually!");
						}
					} catch (Exception e1) {
						LOGGER.error("## Exception occured:", e1);
					}
				}
			};
			btnCaptureDevice.addEventHandler(MouseEvent.MOUSE_CLICKED, btnCaptureDeviceHandler);

			btnSaveHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent event) {
					try {
						save();
					} catch (Exception e1) {
						LOGGER.error("## Exception occured:", e1);
					}
				}
			};
			btnSave.addEventHandler(MouseEvent.MOUSE_CLICKED, btnSaveHandler);

			try {
				getDetails();
			} catch (Exception e1) {
				LOGGER.error("## Exception occured:", e1);
			}

			btnPACUScoreHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent event) {
					try {
						if (AldreteScoreSession.getInstance() != null) {
							AldreteScoreSession.getInstance().setSelectedType("PACU");
							// AldreteScoreSession.getInstance().setPostOpStage(Main.getInstance().getChildStage());
						}

						Main.getInstance().getStageManager()
								.switchSceneAfterSecondary(FxmlView.CALCULATE_ALDERATE_SCORE);

						// ControllerMediator.getInstance().getMainController()
						// .bindPopupFxml(FxmlView.CALCULATE_ALDERATE_SCORE);
					} catch (Exception e) {
						LOGGER.error("## Exception occured:", e);
					}

				}
			};
			btnPACUScore.addEventHandler(MouseEvent.MOUSE_CLICKED, btnPACUScoreHandler);
			/*
			 * btnOutScore.setOnMouseClicked(e->{
			 * if(AldreteScoreSession.getInstance()!=null){
			 * AldreteScoreSession.getInstance().setSelectedType("ShiftingOut"); }
			 * ControllerMediator.getInstance().getMainController(). bindPopupFxml(
			 * "/View/CalculateAldreteScore.fxml");
			 *
			 * });
			 */
			try {
				resetTime();
			} catch (Exception e1) {
				LOGGER.error("## Exception occured:", e1);
			}

			btnTimeHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent event) {
					try {
						LocalTime timeNow = Main.getInstance().getUtility().getLocalSystemTime();
						try {
							setPACUTime(timeNow.toString());
						} catch (Exception e1) {
							LOGGER.error("## Exception occured:", e1);
						}
						Date input = new Date();
						LocalDate date = input.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
						datePACU.setValue(date);
					} catch (Exception e) {
						LOGGER.error("## Exception occured:", e);

					}
				}
			};
			btnTime.addEventHandler(MouseEvent.MOUSE_CLICKED, btnTimeHandler);
			/*
			 * btnOutTime.setOnMouseClicked(e -> { LocalTime timeNow =
			 * Main.getInstance().getUtility().getLocalSystemTime();
			 * setOUTTime(timeNow.toString()); Date input = new Date(); LocalDate date =
			 * input.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			 * dateOut.setValue(date); });
			 */
		} catch (Exception e) {
			LOGGER.error("## Exception occured:" + e.getMessage());
		}
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				try {
					txtPACUBp.requestFocus();
				} catch (Exception e) {
					LOGGER.error("## Exception occured:", e);
				}
			}
		});

	}

	private void saveAllCasesData() {
		// TODO Auto-generated method stub

		
		try {
			List<Object[]> caseList =	 DAOFactory.viewAllCases().viewAllPatientAndCases();
			
			caseList.parallelStream().forEach( obj -> {
				
				Object[] object = (Object[]) obj;
				Long caseId = (Long) object[0];
				Patient patient = (Patient)object[1];
				
				callIntraOpAuditReportTable1(patient.getPatientId(),caseId );
				
				
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LOGGER.error("## Exception occured:", e);
		}
	}

	/**
	 * This method remove active event handlers from all the components
	 */
	private void removeEventHandlers() {
		btnClose.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnCloseHandler);
		btnSave.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnSaveHandler);
		btnPACUScore.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnPACUScoreHandler);
		btnTime.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnTimeHandler);
		btnCaptureDevice.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnCaptureDeviceHandler);
	}

	/**
	 * This method fetches details from database
	 *
	 * @throws Exception
	 */
	private void getDetails() throws Exception {
		try {
			disablePaneMainContent.setVisible(true);
			lblErrorMsg.setVisible(false);
			GetPostOpDetailsService getPostOpDeatilsService = GetPostOpDetailsService
					.getInstance(patientCase.getCaseId());
			getPostOpDeatilsService.restart();

			getPostOpDeatilsServiceSuccessHandler = new EventHandler<WorkerStateEvent>() {

				@Override
				public void handle(WorkerStateEvent event) {
					try {
						postOpDetails = getPostOpDeatilsService.getrecoveryroomreadings();

						if (postOpDetails != null) {

							// tabShiftingOut.setDisable(false);

							postOpReadings
									.setRecoveryRoomReadingRecordId(postOpDetails.getRecoveryRoomReadingRecordId());

							if (postOpDetails.getInVitalsBp() != null) {
								txtPACUBp.setText(postOpDetails.getInVitalsBp() + "");
							}

							if (postOpDetails.getInVitalsPulse() != null) {
								txtPACUPulse.setText(postOpDetails.getInVitalsPulse() + "");
							}

							if (postOpDetails.getInVitalsSpo2() != null) {
								txtPACUSpO2.setText(postOpDetails.getInVitalsSpo2() + "");
							}

							if (postOpDetails.getInVitalsResp() != null) {
								txtPACUResp.setText(postOpDetails.getInVitalsResp() + "");
							}

							if (postOpDetails.getInVitalsTemp() != null) {
								txtPACUTemp.setText(postOpDetails.getInVitalsTemp() + "");
							}

							if (postOpDetails.getRespSupport() != null) {
								choiceRespSupport.getSelectionModel().select(postOpDetails.getRespSupport());
							}

							// changes done by Rohi
							if (postOpDetails.getPacingSupport() != null) {
								choicePacingSupport.getSelectionModel().select(postOpDetails.getPacingSupport());
							}

							if (postOpDetails.getLeads() != null) {
								choiceLeads.getSelectionModel().select(postOpDetails.getLeads());
							}
							if (postOpDetails.getMode() != null) {
								choiceMode.getSelectionModel().select(postOpDetails.getMode());
							}

							if (postOpDetails.getNegativeSuction() != null) {
								choiceNegativeSuction.getSelectionModel().select(postOpDetails.getNegativeSuction());
							}

							if (postOpDetails.getBleedingIssues() != null) {
								choiceBleedingIssues.getSelectionModel().select(postOpDetails.getBleedingIssues());
							}

							if (postOpDetails.getPleural() != null) {
								choicePleural.getSelectionModel().select(postOpDetails.getPleural());
							}

							if (postOpDetails.getBa() != null) {
								choiceBA.getSelectionModel().select(postOpDetails.getBa());
							}

							if (postOpDetails.getRs_pc() != null) {
								choiceRsPc.getSelectionModel().select(postOpDetails.getRs_pc());
							}

							if (postOpDetails.getBeatsPerMinutes() != null) {
								textBeatsPerMinute.setText(postOpDetails.getBeatsPerMinutes() + "");
							}

							if (postOpDetails.getInotropicScore() != null) {
								textInotropicScore.setText(postOpDetails.getInotropicScore() + "");
							}
							// end
							if (postOpDetails.getIvlineInoTropsDetails() != null) {
								txtPACUInoTropDetails.setText(postOpDetails.getIvlineInoTropsDetails());
							}

							// checkPACUInoTrops.setSelected(postOpDetails.getIvlineInoTrops());
							if (postOpDetails.getIvlineBloodDetails() != null) {
								txtPACUBloodDetails.setText(postOpDetails.getIvlineBloodDetails());
							}

							// checkPACUBlood.setSelected(postOpDetails.getIvlineBlood());
							if (postOpDetails.getIvlineIvfluidsDetails() != null) {
								txtPACUIVFluidsDetails.setText(postOpDetails.getIvlineIvfluidsDetails());
							}

							// checkPACUIvFluids.setSelected(postOpDetails.getIvlineIvfluids());
							if (postOpDetails.getIvlineInfusionDetails() != null) {
								txtPACUInfusionDetails.setText(postOpDetails.getIvlineInfusionDetails());
							}

							// checkPACUInfusion.setSelected(postOpDetails.getIvlineInfusion());
							if (postOpDetails.getAldreteScoreAtShiftingToPacu() != null) {
								txtPACUAlderateScore.setText(postOpDetails.getAldreteScoreAtShiftingToPacu() + "");
							}
							/*
							 * if (postOpDetails.getOutVitalsBp() != null) {
							 * txtOutBp.setText(postOpDetails.getOutVitalsBp() + ""); }
							 *
							 * if (postOpDetails.getOutVitalsPulse() != null) {
							 * txtOutPulse.setText(postOpDetails. getOutVitalsPulse() + ""); }
							 *
							 * if (postOpDetails.getOutVitalsSpo2() != null) {
							 * txtOutSpO2.setText(postOpDetails.getOutVitalsSpo2 () + ""); }
							 *
							 * if (postOpDetails.getOutVitalsResp() != null) {
							 * txtOutResp.setText(postOpDetails.getOutVitalsResp () + ""); }
							 *
							 * if (postOpDetails.getOutVitalsTemp() != null) {
							 * txtOutTemp.setText(postOpDetails.getOutVitalsTemp () + ""); }
							 *
							 * if (postOpDetails.getModifiedAldreteScoreAtDischarge () != null) {
							 * txtOutAlderateScore.setText(postOpDetails.
							 * getModifiedAldreteScoreAtDischarge() + ""); }
							 *
							 * checkOutInstruction.setSelected(postOpDetails. getSpecialInstructions());
							 *
							 *
							 * checkOutInvestigation.setSelected(postOpDetails. getInvestigation()); if
							 * (postOpDetails.getInvestigationDetails() != null) {
							 * txtOutIninvestigationDeatils.setText( postOpDetails.
							 * getInvestigationDetails()); }
							 *
							 * checkOutConsulation.setSelected(postOpDetails. getConsultation ()); if
							 * (postOpDetails.getConsultationDetails() != null) {
							 * txtOutConsultaionDeatils.setText(postOpDetails. getConsultationDetails()); }
							 *
							 * checkOutDrug.setSelected(postOpDetails.getDrugs() ); if
							 * (postOpDetails.getDrugsDetails() != null) {
							 * txtOutDrugDeatils.setText(postOpDetails. getDrugsDetails() ); }
							 *
							 * checkOutComplication.setSelected(postOpDetails. getComplications()); if
							 * (postOpDetails.getComplicationsDetails() != null) {
							 * txtOutCompilationDeatils.setText(postOpDetails. getComplicationsDetails()); }
							 */
							if (postOpDetails.getSpecialInstructionsDetails() != null) {
								txtOutInstructionDetails.setText(postOpDetails.getSpecialInstructionsDetails());
							}
							if (postOpDetails.getPacuTime() != null) {
								String dateTimeArr[] = postOpDetails.getPacuTime().toString().split(" ");

								String timearray[] = dateTimeArr[1].split("\\.");

								LocalDate localDate = LocalDate.parse(dateTimeArr[0]);
								datePACU.setValue(localDate);

								try {
									setPACUTime(timearray[0]);
								} catch (Exception e1) {
									LOGGER.error("## Exception occured:", e1);
								}
							}

							/*
							 * if (postOpDetails.getShiftingOutTime() != null) { String dateTimeArr[] =
							 * postOpDetails.getShiftingOutTime().toString(). split( " ");
							 *
							 * String timearray[] = dateTimeArr[1].split("\\.");
							 *
							 * LocalDate localDate = LocalDate.parse(dateTimeArr[0]);
							 * dateOut.setValue(localDate);
							 *
							 * LocalTime localTime = LocalTime.parse(timearray[0], timeFormatter);
							 *
							 * setOUTTime(timearray[0]); }
							 */
							if (postOpDetails.getPacuParams() != null) {
								AldreteScoreSession.getInstance().setPACUparams(postOpDetails.getPacuParams());
							}
							if (postOpDetails.getShiftingOutParams() != null) {
								AldreteScoreSession.getInstance()
										.setShiftingOutParams(postOpDetails.getShiftingOutParams());
							}
						} else {
							Date input = new Date();
							LocalDate date = input.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
							datePACU.setValue(date);
							try {
								resetTime();
							} catch (Exception e1) {
								LOGGER.error("## Exception occured:", e1);
							}
						}
						disablePaneMainContent.setVisible(false);
						getPostOpDeatilsService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
								getPostOpDeatilsServiceSuccessHandler);
						getPostOpDeatilsService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
								getPostOpDeatilsServiceFailedHandler);
					} catch (Exception e) {
						LOGGER.error("## Exception occured:", e);

					}
				}

			};

			getPostOpDeatilsService.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
					getPostOpDeatilsServiceSuccessHandler);

			getPostOpDeatilsServiceFailedHandler = new EventHandler<WorkerStateEvent>() {

				@Override
				public void handle(WorkerStateEvent event) {
					try {
						disablePaneMainContent.setVisible(false);
						getPostOpDeatilsService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
								getPostOpDeatilsServiceSuccessHandler);
						getPostOpDeatilsService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
								getPostOpDeatilsServiceFailedHandler);
						Main.getInstance().getUtility().showNotification("Error", "Error",
								getPostOpDeatilsService.getException().getMessage());
					} catch (Exception e) {
						LOGGER.error("## Exception occured:", e);

					}
				}

			};
		} catch (Exception e) {
			LOGGER.error("## Exception occured:" + e.getMessage());
		}
	}

	/**
	 * This method saves the input data into database
	 *
	 * @throws Exception
	 */
	private void save() throws Exception {
		try {
			if (postOpReadings != null) {
				if (postOpReadings.getRecoveryRoomReadingRecordId() != null) {
					checkForShiftingOut = validateForShiftingOut();
				}
			}
			/*
			 * if(!isEmptyForm()) {
			 */
			if (validateForPACU() && checkForShiftingOut) {
				disablePaneMainContent.setVisible(true);

				lblErrorMsg.setVisible(false);
				postOpReadings.setPatient(patient);
				postOpReadings.setPatientcase(patientCase);
				if (!txtPACUBp.getText().isEmpty()) {
					postOpReadings.setInVitalsBp(txtPACUBp.getText());
				}
				if (!txtPACUPulse.getText().isEmpty()) {
					postOpReadings.setInVitalsPulse(new BigDecimal(txtPACUPulse.getText()));
				}

				if (!txtPACUSpO2.getText().isEmpty()) {
					postOpReadings.setInVitalsSpo2(new BigDecimal(txtPACUSpO2.getText()));
				}

				if (!txtPACUResp.getText().isEmpty()) {
					postOpReadings.setInVitalsResp(new BigDecimal(txtPACUResp.getText()));
				}

				if (!txtPACUTemp.getText().isEmpty()) {
					postOpReadings.setInVitalsTemp(new BigDecimal(txtPACUTemp.getText()));
				}
				if (choiceRespSupport.getSelectionModel().getSelectedIndex() != 0) {
					postOpReadings.setRespSupport(choiceRespSupport.getSelectionModel().getSelectedItem());
				}

				// changes done by Rohi
				if (choicePacingSupport.getSelectionModel().getSelectedIndex() != 0) {
					postOpReadings.setPacingSupport(choicePacingSupport.getSelectionModel().getSelectedItem());
				}

				if (choiceLeads.getSelectionModel().getSelectedIndex() != 0) {
					postOpReadings.setLeads(choiceLeads.getSelectionModel().getSelectedItem());
				}

				if (choiceMode.getSelectionModel().getSelectedIndex() != 0) {
					postOpReadings.setMode(choiceMode.getSelectionModel().getSelectedItem());
				}

				if (choiceNegativeSuction.getSelectionModel().getSelectedIndex() != 0) {
					postOpReadings.setNegativeSuction(choiceNegativeSuction.getSelectionModel().getSelectedItem());
				}

				if (choiceBleedingIssues.getSelectionModel().getSelectedIndex() != 0) {
					postOpReadings.setBleedingIssues(choiceBleedingIssues.getSelectionModel().getSelectedItem());
				}

				if (choicePleural.getSelectionModel().getSelectedIndex() != 0) {
					postOpReadings.setPleural(choicePleural.getSelectionModel().getSelectedItem());
				}

				if (choiceBA.getSelectionModel().getSelectedIndex() != 0) {
					postOpReadings.setBa(choiceBA.getSelectionModel().getSelectedItem());
				}

				if (choiceRsPc.getSelectionModel().getSelectedIndex() != 0) {
					postOpReadings.setRs_pc(choiceRsPc.getSelectionModel().getSelectedItem());
				}

				if (!textInotropicScore.getText().isEmpty()) {
					postOpReadings.setInotropicScore(new BigDecimal(textInotropicScore.getText()));
				}

				if (!textBeatsPerMinute.getText().isEmpty()) {
					postOpReadings.setBeatsPerMinutes(new BigDecimal(textBeatsPerMinute.getText()));
				}
				// end
				if (!txtPACUInoTropDetails.getText().isEmpty()) {
					postOpReadings.setIvlineInoTropsDetails(txtPACUInoTropDetails.getText());
				}
				if (!txtPACUBloodDetails.getText().isEmpty()) {
					postOpReadings.setIvlineBloodDetails(txtPACUBloodDetails.getText());
				}
				if (!txtPACUIVFluidsDetails.getText().isEmpty()) {
					postOpReadings.setIvlineIvfluidsDetails(txtPACUIVFluidsDetails.getText());
				}
				if (!txtPACUInfusionDetails.getText().isEmpty()) {
					postOpReadings.setIvlineInfusionDetails(txtPACUInfusionDetails.getText());
				}
				if (!txtPACUAlderateScore.getText().isEmpty()) {
					postOpReadings.setAldreteScoreAtShiftingToPacu(Integer.valueOf(txtPACUAlderateScore.getText()));
				}
				if (!txtOutInstructionDetails.getText().isEmpty()) {
					postOpReadings.setSpecialInstructionsDetails(txtOutInstructionDetails.getText());
				}
				/*
				 * if (!txtOutBp.getText().isEmpty()) { postOpReadings.setOutVitalsBp(new
				 * BigDecimal(txtOutBp.getText())); }
				 *
				 * if (!txtOutPulse.getText().isEmpty()) { postOpReadings.setOutVitalsPulse(new
				 * BigDecimal(txtOutPulse.getText())); }
				 *
				 * if (!txtOutSpO2.getText().isEmpty()) { postOpReadings.setOutVitalsSpo2(new
				 * BigDecimal(txtOutSpO2.getText())); }
				 *
				 * if (!txtOutTemp.getText().isEmpty()) { postOpReadings.setOutVitalsTemp(new
				 * BigDecimal(txtOutTemp.getText())); } if
				 * (!txtOutAlderateScore.getText().isEmpty()) {
				 * postOpReadings.setModifiedAldreteScoreAtDischarge(Integer. valueOf
				 * (txtOutAlderateScore.getText())); }
				 *
				 *
				 * postOpReadings.setInvestigationDetails(
				 * txtOutIninvestigationDeatils.getText());
				 * postOpReadings.setConsultationDetails( txtOutConsultaionDeatils. getText());
				 * postOpReadings.setDrugsDetails(txtOutDrugDeatils.getText());
				 * postOpReadings.setComplicationsDetails( txtOutCompilationDeatils. getText());
				 */
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

				if (datePACU.getValue() != null) {

					Date PACUDateTime;
					try {
						PACUDateTime = simpleDateFormat.parse(datePACU.getValue() + " "
								+ Main.getInstance().getUtility().getTime(hBoxPACUTimeControl));
						postOpReadings.setPacuTime(PACUDateTime);
					} catch (ParseException e) {

						LOGGER.error("## Exception occured:", e);
					}
				}

				/*
				 * if ( dateOut.getValue() != null) {
				 *
				 * Date OutDateTime; try { OutDateTime =
				 * simpleDateFormat.parse(dateOut.getValue() + " " +
				 * Main.getInstance().getUtility().getTime(hBoxOUTTimeControl));
				 * postOpReadings.setShiftingOutTime(OutDateTime); } catch (ParseException e) {
				 * LOGGER.error("## Exception occured:", e); } }
				 */

				postOpReadings.setPacuParams(AldreteScoreSession.getInstance().getPACUparams());
				postOpReadings.setShiftingOutParams(AldreteScoreSession.getInstance().getShiftingOutParams());

				int upperRange = 0;
				int lowerRange = 0;
				int category = 0;
				try {
					category = calculateAgeCategory();
				} catch (Exception e) {
					LOGGER.error("## Exception occured:", e);
				}

				if (category == 0) // Adult
				{
					upperRange = 80;
					lowerRange = 65;
				} else if (category == 1) // Child
				{
					upperRange = 60;
					lowerRange = 50;
				} else if (category == 2) // Infant
				{
					upperRange = 55;
					lowerRange = 45;
				} else if (category == 3) // Neonate
				{
					upperRange = 50;
					lowerRange = 40;
				}

				if (!txtLowerBp.getText().isEmpty()) {
					lowerRange = Integer.parseInt(txtLowerBp.getText());
				}

				if (!txtUpperBp.getText().isEmpty()) {
					upperRange = Integer.parseInt(txtUpperBp.getText());
				}
				postOpReadings.setLowerRange(new BigDecimal(lowerRange));
				postOpReadings.setUpperRange(new BigDecimal(upperRange));

				PostOpSaveService postOpSaveService = PostOpSaveService.getInstance(postOpReadings, upperRange,
						lowerRange);
				postOpSaveService.restart();

				postOpSaveServiceSuccessHandler = new EventHandler<WorkerStateEvent>() {

					@Override
					public void handle(WorkerStateEvent event) {
						try {
							postOpReadings
									.setRecoveryRoomReadingRecordId(postOpSaveService.getRecoveryroomreadingsId());
							
						
							// tabShiftingOut.setDisable(false);

							if (isFromDasboard) {
								btnEvent.getStylesheets()
										.add(this.getClass().getResource("/styles/newStyles.css").toString());
								btnEvent.getStyleClass().add("sidebarBtnExecuted");
							}
							disablePaneMainContent.setVisible(false);
							postOpSaveService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
									postOpSaveServiceSuccessHandler);
							postOpSaveService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
									postOpSaveServiceFailedHandler);
							try {
								removeEventHandlers();
								Main.getInstance().getStageManager().closeSecondaryStage();
								Main.getInstance().getStageManager().switchScene(FxmlView.MAIN);
								Main.getInstance().getStageManager().switchSceneAfterMain(FxmlView.GET_SNAPSHOT);
							} catch (Exception e) {
								LOGGER.error("## Exception occured:", e);
							}
							Main.getInstance().getUtility().showNotification("Information", "Success",
									"Shift Out Details saved successfully!");

							// ---modified by Rohit on 9/19/2018
							calculateEventTimeDiff(postOpReadings.getPacuTime());

							Long caseId = PatientSession.getInstance().getPatientCase().getCaseId();
							Integer patientID = PatientSession.getInstance().getPatientCase().getPatient()
									.getPatientId();
							// changes by vivek

							// add shift out in Events Log table
							addEvent();
							String userID = UserSession.getInstance().getUserWithrRolesForUserAuthentication().getUserID();
							//closing Live Medications
						List<String> medicationList = 	medicationLogDao.getMedicationList(caseId, patientID, userID);
						
						if(medicationList !=null) {
							
							for (String medicationName: medicationList) {
								
								
								medicationLogDao.settingEndTimeinLastDose(medicationName,caseId,
										patientID, Calendar.getInstance().getTime(), "Infusion",null);
							}
						}
							
							callIntraOpAuditReportTable1(patientID, caseId);
							//callIntraOpAuditReportTable2(patientID, caseId, saveAllCases);

							// changes end here

							// ----------------------------------

						} catch (Exception e) {
							e.printStackTrace();
							LOGGER.error("## Exception occured:", e);

						}
					}

				};

				postOpSaveService.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
						postOpSaveServiceSuccessHandler);

				postOpSaveServiceFailedHandler = new EventHandler<WorkerStateEvent>() {

					@Override
					public void handle(WorkerStateEvent event) {

						try {
							disablePaneMainContent.setVisible(false);
							postOpSaveService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
									postOpSaveServiceSuccessHandler);
							postOpSaveService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
									postOpSaveServiceFailedHandler);
							Main.getInstance().getUtility().showNotification("Error", "Error",
									postOpSaveService.getException().getMessage());
						} catch (Exception e) {
							LOGGER.error("## Exception occured:", e);
						}
					}

				};

				postOpSaveService.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED, postOpSaveServiceFailedHandler);

			} else {
				lblErrorMsg.setVisible(true);
				disablePaneMainContent.setVisible(false);
			}
		} catch (Exception e) {
			LOGGER.error("## Exception occured:" + e.getMessage());
		}
	}

	private int calculateAgeCategory() {
		int category = 0; // Adult=0, Child=1, Infant=2, Neonate=3
		int ageInDays = -1;

		if (patient.getAge() != null) {
			if (patient.getAgeUnit().equalsIgnoreCase("Years")) {
				ageInDays = patient.getAge() * 365;
			} else if (patient.getAgeUnit().equalsIgnoreCase("Months")) {
				ageInDays = patient.getAge() * 30;
			} else {
				ageInDays = patient.getAge();
			}
		}

		if (ageInDays > 0 && ageInDays <= 28) {
			category = 3;
		} else if (ageInDays <= 365) {
			category = 2;
		} else if (ageInDays <= 6570) {
			category = 1;
		}

		return category;
	}

	// ---modified by Rohit on 9/19/2018
	public void calculateEventTimeDiff(Date outTime) {
		try {
			Calendar AXCOn = null;
			Calendar AXCOff = null;
			Calendar CPBOn = null;
			Calendar CPBOff = null;
			Calendar incesionTime = null;
			Calendar skinclosureTime = null;
			Calendar timeIn = null;
			Calendar handOverTime = null;
			// changes made by vivek
			Calendar inductionTime = null;
			// changes end
			Calendar shiftOutTime = Calendar.getInstance();

			shiftOutTime.setTime(outTime);

			for (IntraopEventlog eventObj : ExecutedEventSession.getInstance().getExecutedEventList()) {
				if (eventObj.getEventTime() != null) {
					if (eventObj.getCustomEventName().equalsIgnoreCase("axc on")) {
						AXCOn = Calendar.getInstance();
						AXCOn.setTime(eventObj.getEventTime());
					} else if (eventObj.getCustomEventName().equalsIgnoreCase("axc off")) {
						AXCOff = Calendar.getInstance();
						AXCOff.setTime(eventObj.getEventTime());
					} else if (eventObj.getCustomEventName().equalsIgnoreCase("cpb on")) {
						CPBOn = Calendar.getInstance();
						CPBOn.setTime(eventObj.getEventTime());
					} else if (eventObj.getCustomEventName().equalsIgnoreCase("cpb off")) {
						CPBOff = Calendar.getInstance();
						CPBOff.setTime(eventObj.getEventTime());
					} else if (eventObj.getCustomEventName().equalsIgnoreCase("incision")) {
						incesionTime = Calendar.getInstance();
						incesionTime.setTime(eventObj.getEventTime());
					} else if (eventObj.getCustomEventName().equalsIgnoreCase("skin closure ")) {
						skinclosureTime = Calendar.getInstance();
						skinclosureTime.setTime(eventObj.getEventTime());
					} else if (eventObj.getCustomEventName().equalsIgnoreCase("time in")) {
						timeIn = Calendar.getInstance();
						timeIn.setTime(eventObj.getEventTime());
					} else if (eventObj.getCustomEventName().equalsIgnoreCase("handover")) {
						handOverTime = Calendar.getInstance();
						handOverTime.setTime(eventObj.getEventTime());
					}
					// changes made by vivek
					else if (eventObj.getCustomEventName().equalsIgnoreCase("induction")) {
						inductionTime = Calendar.getInstance();
						inductionTime.setTime(eventObj.getEventTime());
					}
					// changes end

				}

			}

			DecimalFormat df = new DecimalFormat("#.00");

			if (AXCOn != null && AXCOff != null)
				AXCTime = Float
						.valueOf(df.format((AXCOff.getTimeInMillis() - AXCOn.getTimeInMillis()) * 0.00001 * 0.0278));

			if (CPBOn != null && CPBOff != null)
				CPBTime = Float
						.valueOf(df.format((CPBOff.getTimeInMillis() - CPBOn.getTimeInMillis()) * 0.00001 * 0.0278));

			if (skinclosureTime != null && handOverTime != null)
				SurgeryTime = Float.valueOf(df.format(
						(skinclosureTime.getTimeInMillis() - handOverTime.getTimeInMillis()) * 0.00001 * 0.0278));

			if (shiftOutTime != null && inductionTime != null)
				Anesthesiatime = Float.valueOf(df
						.format((shiftOutTime.getTimeInMillis() - inductionTime.getTimeInMillis()) * 0.00001 * 0.0278));

			if (timeIn != null && handOverTime != null) {
				HandOverTime = Float.valueOf(
						df.format((handOverTime.getTimeInMillis() - timeIn.getTimeInMillis()) * 0.00001 * 0.0278));
			}

			if (incesionTime != null && handOverTime != null) {
				PatientPrepationTime = Float.valueOf(df
						.format((incesionTime.getTimeInMillis() - handOverTime.getTimeInMillis()) * 0.00001 * 0.0278));
			}
			if (skinclosureTime != null && shiftOutTime != null) {
				ShiftingOutTime = Float.valueOf(df.format(
						(shiftOutTime.getTimeInMillis() - skinclosureTime.getTimeInMillis()) * 0.00001 * 0.0278));
			}
			// changes made by vivek
			if (shiftOutTime != null && timeIn != null) {
				OTTime = Float.valueOf(
						df.format((shiftOutTime.getTimeInMillis() - timeIn.getTimeInMillis()) * 0.00001 * 0.0278));
			}
			// changes end

			GetAuditReportService getAuditReportService = GetAuditReportService
					.getInstance(PatientSession.getInstance().getPatientCase().getCaseId());
			getAuditReportService.restart();

			// ---success
			getAuditReportServiceSuccessHandler = new EventHandler<WorkerStateEvent>() {
				@Override
				public void handle(WorkerStateEvent event) {
					try {
						AuditReport auditReport = getAuditReportService.getAuditReportService();
						auditReport.setCaseID(PatientSession.getInstance().getPatientCase().getCaseId());
						auditReport.setAXCTime(AXCTime);
						auditReport.setCPBTime(CPBTime);
						auditReport.setSurgeryTime(SurgeryTime);
						auditReport.setAnesthesiaTime(Anesthesiatime);
						auditReport.setHandOverTime(HandOverTime);
						auditReport.setPatientPreparationTime(PatientPrepationTime);
						auditReport.setShiftingOutTime(ShiftingOutTime);
						// changes made by vivek
						auditReport.setOtTime(OTTime);
						//
						auditReport.setMedFentanyl(TotalFentanyl);
						auditReport.setMedPropofol(TotalPropofol);
						auditReport.setMedKetamine(TotalKetamine);
						auditReport.setMedVecuronium(TotalVecuronium);
						auditReport.setMedCefuroxime(TotalCefuroxime);
						auditReport.setMedHeparin(TotalHeparin);
						auditReport.setMedProtamine(TotalProtamine);
						auditReport.setMedEaca(TotalEACA);
						auditReport.setFluidNormalSaline(totalNormalSaline);
						auditReport.setFluidRingerLactate(totalRingerLactate);

						AddAuditReportService addAuditReportService = AddAuditReportService.getInstance(auditReport);
						addAuditReportService.restart();

						addAuditReportServiceSuccessHandler = new EventHandler<WorkerStateEvent>() {
							@Override
							public void handle(WorkerStateEvent event) {
								try {
									System.out.println("success");
								} catch (Exception e) {
									LOGGER.error("## Exception occured:", e);

								}
							}
						};
						addAuditReportService.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
								addAuditReportServiceSuccessHandler);

						addAuditReportServiceFailedHandler = new EventHandler<WorkerStateEvent>() {
							@Override
							public void handle(WorkerStateEvent event) {
								try {
									Main.getInstance().getUtility().showNotification("Error", "Error",
											addAuditReportService.getException().getMessage());

									addAuditReportService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
											addAuditReportServiceSuccessHandler);
									addAuditReportService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
											addAuditReportServiceFailedHandler);
								} catch (Exception e) {
									LOGGER.error("## Exception occured:", e);

								}
							}
						};
						addAuditReportService.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
								addAuditReportServiceFailedHandler);

					} catch (Exception e) {
						LOGGER.error("## Exception occured:", e);

					}
				}
			};
			getAuditReportService.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
					getAuditReportServiceSuccessHandler);

			// ---failure
			getAuditReportServiceFailedHandler = new EventHandler<WorkerStateEvent>() {
				@Override
				public void handle(WorkerStateEvent event) {
					try {
						Main.getInstance().getUtility().showNotification("Error", "Error",
								getAuditReportService.getException().getMessage());

						getAuditReportService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
								getAuditReportServiceSuccessHandler);
						getAuditReportService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
								getAuditReportServiceFailedHandler);
					} catch (Exception e) {
						LOGGER.error("## Exception occured:", e);

					}
				}
			};
			getAuditReportService.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
					getAuditReportServiceFailedHandler);

		} catch (NumberFormatException e) {
			LOGGER.error("## Exception occured:" + e.getMessage());
		}

	}
	/*
	 * else { lblErrorMsg.setVisible(true); lblErrorMsg.setText(
	 * "*Please enter atleast 1 value"); }
	 */

	/*
	 * private boolean isEmptyForm() { /// for inVitalsBP
	 *
	 * if (txtPACUBp.getText() != null && !txtPACUBp.getText().trim().isEmpty()) {
	 * return false; } /// for inVitalsPulse
	 *
	 * if (txtPACUPulse.getText() != null &&
	 * !txtPACUPulse.getText().trim().isEmpty()) { return false; } /// for
	 * inVitalsSPO2
	 *
	 * if (txtPACUSpO2.getText() != null && !txtPACUSpO2.getText().trim().isEmpty())
	 * { return false; } /// for inVitalsResp
	 *
	 * if (txtPACUResp.getText() != null && !txtPACUResp.getText().trim().isEmpty())
	 * { return false; } /// for inVitalsTemp
	 *
	 * if (txtPACUTemp.getText() != null && !txtPACUTemp.getText().trim().isEmpty())
	 * { return false; }
	 *
	 * /// for PACU time
	 *
	 * if (datePACU.getValue() != null) { return false; }
	 *
	 * /// for ivlineINotropsDetails
	 *
	 * if (txtPACUInoTropDetails.getText() != null &&
	 * !txtPACUInoTropDetails.getText().trim().isEmpty()) { return false; } /// for
	 * ivlineBloodDetails
	 *
	 * if (txtPACUBloodDetails.getText() != null &&
	 * !txtPACUBloodDetails.getText().trim().isEmpty()) { return false; } /// for
	 * ivlineIVFluidsdetails
	 *
	 * if (txtPACUIVFluidsDetails.getText() != null &&
	 * !txtPACUIVFluidsDetails.getText().trim().isEmpty()) { return false; } /// for
	 * ivlineInfusionDetails
	 *
	 * if (txtPACUInfusionDetails.getText() != null &&
	 * !txtPACUInfusionDetails.getText().trim().isEmpty()) { return false; } /// for
	 * aldrete score first
	 *
	 * if (txtPACUAlderateScore.getText() != null &&
	 * !txtPACUAlderateScore.getText().trim().isEmpty()) { return false; }
	 *
	 * /// for outVitalsBP
	 *
	 *
	 * if (txtOutBp.getText() != null && !txtOutBp.getText().trim().isEmpty()) {
	 * return false; } /// for outVitalsPulse
	 *
	 *
	 * if (txtOutPulse.getText() != null && !txtOutPulse.getText().trim().isEmpty())
	 * { return false; } /// for outVitalsSPO2
	 *
	 *
	 * if (txtOutSpO2.getText() != null && !txtOutSpO2.getText().trim().isEmpty()) {
	 * return false; } /// for outVitalsResp
	 *
	 *
	 * if (txtOutResp.getText() != null && !txtOutResp.getText().trim().isEmpty()) {
	 * return false; }
	 *
	 * /// for outVitalsTemp
	 *
	 * if (txtOutTemp.getText() != null && !txtOutTemp.getText().trim().isEmpty()) {
	 * return false; } /// for shifting out time
	 *
	 *
	 * if (dateOut.getValue() !=null ) { return false; } /// for aldrete score
	 * second
	 *
	 *
	 * if (txtOutAlderateScore.getText() != null &&
	 * !txtOutAlderateScore.getText().trim().isEmpty()) { return false; } /// for
	 * special instructions details
	 *
	 *
	 * if (txtOutInstructionDetails.getText() != null &&
	 * !txtOutInstructionDetails.getText().trim().isEmpty()) { return false; }
	 *
	 * /// for Investigationdetails
	 *
	 *
	 * if (txtOutIninvestigationDeatils.getText() != null &&
	 * !txtOutIninvestigationDeatils.getText().trim().isEmpty()) { return false; }
	 * /// for ConsultationDetails
	 *
	 *
	 * if (txtOutConsultaionDeatils.getText() != null &&
	 * !txtOutConsultaionDeatils.getText().trim().isEmpty()) { return false; } ////
	 * for Drugs Details
	 *
	 *
	 * if (txtOutDrugDeatils.getText() != null &&
	 * !txtOutDrugDeatils.getText().trim().isEmpty()) { return false; } //// for
	 * complication Details
	 *
	 *
	 * if (txtOutCompilationDeatils.getText() != null &&
	 * !txtOutCompilationDeatils.getText().trim().isEmpty()) { return false; }
	 *
	 *
	 * return true; }
	 */

	/*
	 * public boolean isEmptyFormForShiftingOut() { /// for outVitalsBP
	 *
	 *
	 * if (txtOutBp.getText() != null && !txtOutBp.getText().trim().isEmpty()) {
	 * return false; } /// for outVitalsPulse
	 *
	 *
	 * if (txtOutPulse.getText() != null && !txtOutPulse.getText().trim().isEmpty())
	 * { return false; } /// for outVitalsSPO2
	 *
	 *
	 * if (txtOutSpO2.getText() != null && !txtOutSpO2.getText().trim().isEmpty()) {
	 * return false; } /// for outVitalsResp
	 *
	 *
	 * if (txtOutResp.getText() != null && !txtOutResp.getText().trim().isEmpty()) {
	 * return false; }
	 *
	 * /// for outVitalsTemp
	 *
	 * if (txtOutTemp.getText() != null && !txtOutTemp.getText().trim().isEmpty()) {
	 * return false; } /// for shifting out time
	 *
	 *
	 * if (dateOut.getValue() !=null && timeOut.getValue() != null) { return false;
	 * } /// for aldrete score second
	 *
	 *
	 * if (txtOutAlderateScore.getText() != null &&
	 * !txtOutAlderateScore.getText().trim().isEmpty()) { return false; } /// for
	 * special instructions details
	 *
	 *
	 * if (txtOutInstructionDetails.getText() != null &&
	 * !txtOutInstructionDetails.getText().trim().isEmpty()) { return false; }
	 *
	 * /// for Investigationdetails
	 *
	 *
	 * if (txtOutIninvestigationDeatils.getText() != null &&
	 * !txtOutIninvestigationDeatils.getText().trim().isEmpty()) { return false; }
	 * /// for ConsultationDetails
	 *
	 *
	 * if (txtOutConsultaionDeatils.getText() != null &&
	 * !txtOutConsultaionDeatils.getText().trim().isEmpty()) { return false; } ////
	 * for Drugs Details
	 *
	 *
	 * if (txtOutDrugDeatils.getText() != null &&
	 * !txtOutDrugDeatils.getText().trim().isEmpty()) { return false; } //// for
	 * complication Details
	 *
	 *
	 * if (txtOutCompilationDeatils.getText() != null &&
	 * !txtOutCompilationDeatils.getText().trim().isEmpty()) { return false; }
	 *
	 * return true; }
	 */

	/**
	 * This method validates textfields on the screen
	 *
	 * @return true/false depending upon whether the input is valid
	 */
	private boolean validateForPACU() {

		// for mandatory fields

		if (datePACU.getValue() == null) {
			lblErrorMsg.setText("*Please Fill the mandatory fields");
			return false;
		}

		/// for inVitalsBP

		if (txtPACUBp.getText() != null && !txtPACUBp.getText().trim().isEmpty()
				&& !Validations.numberWithSlash(txtPACUBp.getText())) {
			lblErrorMsg.setText("*Please enter IBP value in valid format(Sys/Dia) e.g 120/80.");
			return false;
		}

		/// for inVitalsPulse

		if (txtPACUPulse.getText() != null && !txtPACUPulse.getText().trim().isEmpty()
				&& !Validations.decimalsUpto2places3digit(txtPACUPulse.getText())) {
			lblErrorMsg.setText("*Please enter Pulse upto 3 digits and upto 2 decimal only.");
			return false;
		}

		if (textInotropicScore.getText() != null && !textInotropicScore.getText().trim().isEmpty()
				&& !Validations.decimalsUpto2places3digit(textInotropicScore.getText())) {
			lblErrorMsg.setText("*Please enter Inotropic Score upto 3 digits and upto 2 decimal only.");
			return false;
		}

		/*
		 * if (txtPACUPulse.getText() != null &&
		 * !txtPACUPulse.getText().trim().isEmpty() &&
		 * !Validations.decimalsUpto2places10(txtPACUPulse.getText())) { lblErrorMsg.
		 * setText("*Please enter pulse value upto 10 digits and decimal upto 3 digits only"
		 * ); return false; }
		 */
		if (txtPACUPulse.getText() != null && !txtPACUPulse.getText().trim().isEmpty()
				&& !Validations.nonZeroBigDecimal(txtPACUPulse.getText())) {
			lblErrorMsg.setText("*Please enter pulse value more than  0");
			return false;
		}

		/// for inVitalsSPO2

		// changes done by Rohi
		if (textBeatsPerMinute.getText() != null && !textBeatsPerMinute.getText().trim().isEmpty()
				&& !Validations.decimalsUpto2places3digit(textBeatsPerMinute.getText())) {
			lblErrorMsg.setText("*Please enter BPM upto 3 digits and upto 2 decimal only.");
			return false;
		}

		if (textBeatsPerMinute.getText() != null && !textBeatsPerMinute.getText().trim().isEmpty()
				&& !Validations.nonZeroBigDecimal(textBeatsPerMinute.getText())) {
			lblErrorMsg.setText("*Please enter BPM value more than  0");
			return false;
		}

		/*
		 * if (choicePacingSupport.getSelectionModel().getSelectedIndex() == 0) {
		 * lblErrorMsg.setText("*Please select Pacing Support"); return false;
		 * 
		 * }
		 * 
		 * if (choiceLeads.getSelectionModel().getSelectedIndex() == 0) {
		 * lblErrorMsg.setText("*Please select Leads"); return false;
		 * 
		 * }
		 * 
		 * if (choiceMode.getSelectionModel().getSelectedIndex() == 0) {
		 * lblErrorMsg.setText("*Please select Mode"); return false;
		 * 
		 * }
		 * 
		 * if (choiceNegativeSuction.getSelectionModel().getSelectedIndex() == 0) {
		 * lblErrorMsg.setText("*Please select Negative Suction"); return false;
		 * 
		 * }
		 * 
		 * if (choiceBleedingIssues.getSelectionModel().getSelectedIndex() == 0) {
		 * lblErrorMsg.setText("*Please select Any Beeding Issues"); return false;
		 * 
		 * }
		 * 
		 * if (choicePleural.getSelectionModel().getSelectedIndex() == 0) {
		 * lblErrorMsg.setText("*Please select Pleural"); return false;
		 * 
		 * }
		 * 
		 * if (choiceBA.getSelectionModel().getSelectedIndex() == 0) {
		 * lblErrorMsg.setText("*Please select Backup/Active"); return false;
		 * 
		 * }
		 * 
		 * if (choiceRsPc.getSelectionModel().getSelectedIndex() == 0) {
		 * lblErrorMsg.setText("*Please select Rs/Pc"); return false;
		 * 
		 * }
		 */
		// changes end
		if (txtPACUSpO2.getText() != null && !txtPACUSpO2.getText().trim().isEmpty()
				&& !Validations.decimalsUpto2places3digit(txtPACUSpO2.getText())) {
			lblErrorMsg.setText("*Please enter a valid SpO2 value upto 3 digits and upto 2 decimal only.");
			return false;
		}

		// if (txtPACUSpO2.getText() != null &&
		// !txtPACUSpO2.getText().trim().isEmpty()
		// && !Validations.decimalsUpto2places3digit(txtPACUSpO2.getText())) {
		// lblErrorMsg.setText("*Please enter spo2 value upto 10 digits and
		// decimal upto 3 digits only");
		// return false;
		// }
		if (txtPACUSpO2.getText() != null && !txtPACUSpO2.getText().trim().isEmpty()
				&& !Validations.nonZeroBigDecimal(txtPACUSpO2.getText())) {
			lblErrorMsg.setText("*Please enter SpO2 value more than  0");
			return false;
		}

		if (txtUpperBp.getText() != null && !txtUpperBp.getText().trim().isEmpty()
				&& !Validations.nonZeroBigDecimal(txtUpperBp.getText())) {
			lblErrorMsg.setText("*Please enter Upper BP value more than  0");
			return false;
		}

		if (txtLowerBp.getText() != null && !txtLowerBp.getText().trim().isEmpty()
				&& !Validations.nonZeroBigDecimal(txtLowerBp.getText())) {
			lblErrorMsg.setText("*Please enter Lower BP value more than  0");
			return false;
		}
		/// for inVitalsResp

		if (txtPACUResp.getText() != null && !txtPACUResp.getText().trim().isEmpty()
				&& !Validations.decimalsUpto2places3digit(txtPACUResp.getText())) {
			lblErrorMsg.setText("*Please enter a valid resp value upto 3 digits and upto 2 decimal only.");
			return false;
		}

		// if (txtPACUResp.getText() != null &&
		// !txtPACUResp.getText().trim().isEmpty()
		// && !Validations.decimalsUpto2places10(txtPACUResp.getText())) {
		// lblErrorMsg.setText("*Please enter resp value upto 10 digits and
		// decimal upto 3 digits only");
		// return false;
		// }
		if (txtPACUResp.getText() != null && !txtPACUResp.getText().trim().isEmpty()
				&& !Validations.nonZeroBigDecimal(txtPACUResp.getText())) {
			lblErrorMsg.setText("*Please enter resp value more than  0");
			return false;
		}
		/// for inVitalsTemp

		if (txtPACUTemp.getText() != null && !txtPACUTemp.getText().trim().isEmpty()
				&& !Validations.decimalsUpto2places3digit(txtPACUTemp.getText())) {
			lblErrorMsg.setText("*Please enter a valid temp value upto 3 digits and upto 2 decimal only.");
			return false;
		}

		// if (txtPACUTemp.getText() != null &&
		// !txtPACUTemp.getText().trim().isEmpty()
		// && !Validations.decimalsUpto2places10(txtPACUTemp.getText())) {
		// lblErrorMsg.setText("*Please enter temp value upto 10 digits and
		// decimal upto 3 digits only");
		// return false;
		// }
		if (txtPACUTemp.getText() != null && !txtPACUTemp.getText().trim().isEmpty()
				&& !Validations.nonZeroBigDecimal(txtPACUTemp.getText())) {
			lblErrorMsg.setText("*Please enter temp value more than  0");
			return false;
		}

		/// for checking whether or not the date entered is in future for pacu
		if (datePACU.getValue() != null) {
			try {
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				Date dateEntered = simpleDateFormat.parse(
						datePACU.getValue() + " " + Main.getInstance().getUtility().getTime(hBoxPACUTimeControl));
				if (Validations.futureDateCheck(dateEntered)) {
					lblErrorMsg.setText("*Future date is not allowed.");
					return false;
				}
			} catch (Exception e) {
				LOGGER.error("## Exception occured:", e);
			}
		}

		/// for ivlineINotropsDetails

		if (txtPACUInoTropDetails.getText() != null && !txtPACUInoTropDetails.getText().trim().isEmpty()
				&& !Validations.maxLength(txtPACUInoTropDetails.getText(), 2000)) {
			lblErrorMsg.setText("*Please enter Ino trops details less than 2000 characters");
			return false;
		}

		/// for ivlineBloodDetails

		if (txtPACUBloodDetails.getText() != null && !txtPACUBloodDetails.getText().trim().isEmpty()
				&& !Validations.maxLength(txtPACUBloodDetails.getText(), 2000)) {
			lblErrorMsg.setText("*Please enter Blood details less than 2000 characters");
			return false;
		}

		/// for ivlineIVFluidsdetails

		if (txtPACUIVFluidsDetails.getText() != null && !txtPACUIVFluidsDetails.getText().trim().isEmpty()
				&& !Validations.maxLength(txtPACUIVFluidsDetails.getText(), 2000)) {
			lblErrorMsg.setText("*Please enter I/V fluids details less than 2000 characters");
			return false;
		}

		/// for ivlineInfusionDetails

		if (txtPACUInfusionDetails.getText() != null && !txtPACUInfusionDetails.getText().trim().isEmpty()
				&& !Validations.maxLength(txtPACUInfusionDetails.getText(), 2000)) {
			lblErrorMsg.setText("*Please enter infusion details less than 2000 characters");
			return false;
		}

		/// for aldrete score first

		// Not Applicable

		return true;
	}

	/**
	 * This method validates input for shift out details textfield
	 *
	 * @return true/false depending upon input is valid
	 */
	private boolean validateForShiftingOut() {
		// for mandatory fields

		/*
		 * if(dateOut.getValue()==null ) { lblErrorMsg.setText(
		 * "*Please Fill the mandatory fields"); return false; }
		 */
		/// for outVitalsBP

		/*
		 * if(txtOutBp.getText() != null && !txtOutBp.getText().trim().isEmpty() &&
		 * !Validations.decimalsUpto2places(txtOutBp.getText())) {
		 * lblErrorMsg.setText("*Please enter a valid BP value"); return false; }
		 *
		 * if(txtOutBp.getText() != null && !txtOutBp.getText().trim().isEmpty() &&
		 * !Validations.decimalsUpto2places10(txtOutBp.getText())) { lblErrorMsg.
		 * setText(
		 * "*Please enter BP value upto 10 digits and decimal upto 3 digits only" );
		 * return false; } if(txtOutBp.getText() != null &&
		 * !txtOutBp.getText().trim().isEmpty() &&
		 * !Validations.nonZeroBigDecimal(txtOutBp.getText())) {
		 * lblErrorMsg.setText("*Please enter BP value more than  0"); return false; }
		 *
		 * /// for outVitalsPulse
		 *
		 * if(txtOutPulse.getText() != null && !txtOutPulse.getText().trim().isEmpty()
		 * && !Validations.decimalsUpto2places(txtOutPulse.getText())) {
		 * lblErrorMsg.setText("*Please enter a valid pulse value"); return false; }
		 *
		 * if(txtOutPulse.getText() != null && !txtOutPulse.getText().trim().isEmpty()
		 * && !Validations.decimalsUpto2places10(txtOutPulse.getText())) { lblErrorMsg.
		 * setText(
		 * "*Please enter pulse value upto 10 digits and decimal upto 3 digits only" );
		 * return false; } if(txtOutPulse.getText() != null &&
		 * !txtOutPulse.getText().trim().isEmpty() &&
		 * !Validations.nonZeroBigDecimal(txtOutPulse.getText())) {
		 * lblErrorMsg.setText("*Please enter pulse value more than  0"); return false;
		 * }
		 *
		 * /// for outVitalsSPO2
		 *
		 * if(txtOutSpO2.getText() != null && !txtOutSpO2.getText().trim().isEmpty() &&
		 * !Validations.decimalsUpto2places(txtOutSpO2.getText())) {
		 * lblErrorMsg.setText("*Please enter a valid spo2 value"); return false; }
		 *
		 * if(txtOutSpO2.getText() != null && !txtOutSpO2.getText().trim().isEmpty() &&
		 * !Validations.decimalsUpto2places10(txtOutSpO2.getText())) { lblErrorMsg.
		 * setText(
		 * "*Please enter spo2 value upto 10 digits and decimal upto 3 digits only" );
		 * return false; } if(txtOutSpO2.getText() != null &&
		 * !txtOutSpO2.getText().trim().isEmpty() &&
		 * !Validations.nonZeroBigDecimal(txtOutSpO2.getText())) {
		 * lblErrorMsg.setText("*Please enter spo2 value more than  0"); return false; }
		 *
		 * /// for outVitalsResp
		 *
		 * if(txtOutResp.getText() != null && !txtOutResp.getText().trim().isEmpty() &&
		 * !Validations.decimalsUpto2places(txtOutResp.getText())) {
		 * lblErrorMsg.setText("*Please enter a valid resp value"); return false; }
		 *
		 * if(txtOutResp.getText() != null && !txtOutResp.getText().trim().isEmpty() &&
		 * !Validations.decimalsUpto2places10(txtOutResp.getText())) { lblErrorMsg.
		 * setText(
		 * "*Please enter resp value upto 10 digits and decimal upto 3 digits only" );
		 * return false; } if(txtOutResp.getText() != null &&
		 * !txtOutResp.getText().trim().isEmpty() &&
		 * !Validations.nonZeroBigDecimal(txtOutResp.getText())) {
		 * lblErrorMsg.setText("*Please enter resp value more than  0"); return false; }
		 *
		 * /// for outVitalsTemp
		 *
		 * if(txtOutTemp.getText() != null && !txtOutTemp.getText().trim().isEmpty() &&
		 * !Validations.decimalsUpto2places(txtOutTemp.getText())) {
		 * lblErrorMsg.setText("*Please enter a valid temp value"); return false; }
		 *
		 * if(txtOutTemp.getText() != null && !txtOutTemp.getText().trim().isEmpty() &&
		 * !Validations.decimalsUpto2places10(txtOutTemp.getText())) { lblErrorMsg.
		 * setText(
		 * "*Please enter temp value upto 10 digits and decimal upto 3 digits only" );
		 * return false; } if(txtOutTemp.getText() != null &&
		 * !txtOutTemp.getText().trim().isEmpty() &&
		 * !Validations.nonZeroBigDecimal(txtOutTemp.getText())) {
		 * lblErrorMsg.setText("*Please enter temp value more than  0"); return false; }
		 *
		 * /// for checking whether or not the date entered is in future for shiftion
		 * out if(dateOut.getValue()!=null ) { try { SimpleDateFormat simpleDateFormat =
		 * new SimpleDateFormat("yyyy-MM-dd HH:mm"); Date dateEntered =
		 * simpleDateFormat.parse(dateOut.getValue() + " " +
		 * Main.getInstance().getUtility().getTime(hBoxOUTTimeControl)); if
		 * (Validations.futureDateCheck(dateEntered)) { lblErrorMsg.setText(
		 * "*Future date is not allowed."); return false; } } catch (Exception e) {
		 * LOGGER.error("## Exception occured:", e); } } if (datePACU.getValue() != null
		 * && dateOut.getValue() != null ) { try { SimpleDateFormat simpleDateFormat =
		 * new SimpleDateFormat("yyyy-MM-dd HH:mm");
		 *
		 * Date PACUTime = simpleDateFormat.parse(datePACU.getValue() + " " +
		 * Main.getInstance().getUtility().getTime(hBoxPACUTimeControl)); Date OUTTime =
		 * simpleDateFormat.parse(dateOut.getValue() + " " +
		 * Main.getInstance().getUtility().getTime(hBoxOUTTimeControl));
		 *
		 * Boolean result = Validations.startEndTimeCheck(PACUTime, OUTTime); if
		 * (!result) { lblErrorMsg.setText( "*Out time should be after PACU in time");
		 * return false; } } catch (Exception e) { LOGGER.error("## Exception occured:",
		 * e); } }
		 */

		/// for aldrete score second

		// Not Applicable

		/// for special instructions details
		if (txtOutInstructionDetails.getText() != null && !txtOutInstructionDetails.getText().trim().isEmpty()
				&& !Validations.maxLength(txtOutInstructionDetails.getText(), 2000)) {
			lblErrorMsg.setText("*Please enter special instruction details less than 2000 characters");
			return false;
		}
		/*
		 *
		 *
		 * /// for Investigationdetails
		 *
		 * if(txtOutIninvestigationDeatils.getText()!=null &&
		 * !txtOutIninvestigationDeatils.getText().trim().isEmpty() &&
		 * !Validations.maxLength(txtOutIninvestigationDeatils.getText(), 2000)) {
		 * lblErrorMsg. setText(
		 * "*Please enter investigation details less than 2000 characters" ); return
		 * false; }
		 *
		 * /// for ConsultationDetails
		 *
		 * if(txtOutConsultaionDeatils.getText()!=null &&
		 * !txtOutConsultaionDeatils.getText().trim().isEmpty() &&
		 * !Validations.maxLength(txtOutConsultaionDeatils.getText(), 2000)) {
		 * lblErrorMsg. setText(
		 * "*Please enter consultaion details less than 2000 characters" ); return
		 * false; }
		 *
		 * //// for Drugs Details
		 *
		 * if(txtOutDrugDeatils.getText()!=null &&
		 * !txtOutDrugDeatils.getText().trim().isEmpty() &&
		 * !Validations.maxLength(txtOutDrugDeatils.getText(), 2000)) { lblErrorMsg.
		 * setText( "*Please enter drugs details less than 2000 characters"); return
		 * false; }
		 *
		 * //// for complication Details
		 *
		 * if(txtOutCompilationDeatils.getText()!=null &&
		 * !txtOutCompilationDeatils.getText().trim().isEmpty() &&
		 * !Validations.maxLength(txtOutCompilationDeatils.getText(), 2000)) {
		 * lblErrorMsg. setText(
		 * "*Please enter complication details less than 2000 characters" ); return
		 * false; }
		 */
		return true;
	}

	/**
	 * This method is used to set time in time control
	 *
	 *
	 * @param time: local time in string
	 */
	private void setPACUTime(String time) throws Exception {
		try {
			String[] hourMinutearray = time.split(":");
			int hour = 0;
			if (Integer.valueOf(hourMinutearray[0].toString()) < 12) {
				meridiemSpinnerPACU.getValueFactory().setValue("AM");
				hour = Integer.valueOf(hourMinutearray[0].toString());
				if (hour == 0) {
					hour = 12;
				}
			} else {
				meridiemSpinnerPACU.getValueFactory().setValue("PM");
				hour = Integer.valueOf(hourMinutearray[0].toString());
				if (hour != 12) {
					hour = hour - 12;
				}

			}
			if (hour < 10) {
				hourSpinnerPACU.getValueFactory().setValue("0" + hour);
			} else {
				hourSpinnerPACU.getValueFactory().setValue("" + hour);
			}

			minuteSpinnerPACU.getValueFactory().setValue(hourMinutearray[1]);
		} catch (Exception e) {
			LOGGER.error("## Exception occured:" + e.getMessage());
		}
	}

	/**
	 * This method resets time control
	 *
	 * @throws Exception
	 */
	private void resetTime() throws Exception {
		try {
			LocalTime timeNow = Main.getInstance().getUtility().getLocalSystemTime();
			setPACUTime(timeNow.toString());
			// setOUTTime(timeNow.toString());
		} catch (Exception e) {
			LOGGER.error("## Exception occured:" + e.getMessage());
		}

	}

	/**
	 * This method is used to set time in time control
	 *
	 *
	 * @param time: local time in string
	 */
	/*
	 * private void setOUTTime(String time) throws Exception { try { String[]
	 * hourMinutearray = time.split(":"); int hour = 0; if
	 * (Integer.valueOf(hourMinutearray[0].toString()) < 13) {
	 * meridiemSpinnerOUT.getValueFactory().setValue("AM"); hour =
	 * Integer.valueOf(hourMinutearray[0].toString()); if (hour == 0) { hour = 12; }
	 * } else { meridiemSpinnerOUT.getValueFactory().setValue("PM"); hour =
	 * Integer.valueOf(hourMinutearray[0].toString()) - 12;
	 *
	 * } if (hour < 10) { hourSpinnerOUT.getValueFactory().setValue("0" + hour); }
	 * else { hourSpinnerOUT.getValueFactory().setValue("" + hour); }
	 *
	 * minuteSpinnerOUT.getValueFactory().setValue(hourMinutearray[1]); } catch
	 * (Exception e) { LOGGER.error("## Exception occured:" + e.getMessage()); } }
	 */

	/**
	 * This method adds an event
	 */
	private void addEvent() {

		IntraopEventlog intraopEventLog = new IntraopEventlog();

		intraopEventLog.setCustomEventName("Shift Out");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

		if (datePACU.getValue() != null) {

			Date PACUDateTime;
			try {
				PACUDateTime = simpleDateFormat.parse(
						datePACU.getValue() + " " + Main.getInstance().getUtility().getTime(hBoxPACUTimeControl));
				intraopEventLog.setEventTime(PACUDateTime);
			} catch (ParseException e) {
				LOGGER.error("## Exception occured:", e);
			}
		}
		intraopEventLog.setPatientcase(patientCase);
		intraopEventLog.setPatient(patient);
		intraopEventLog.setSurgeryType(patientCase.getSpeciality());

		// ---calling service
		AddEventService addEventService = AddEventService.getInstance(intraopEventLog);
		addEventService.restart();

		// ---on service success

		saveEventServiceSuccessHandler = new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent event) {

				try {
					addEventService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
							saveEventServiceSuccessHandler);
					addEventService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
							saveEventServiceFailedHandler);
				} catch (Exception e) {
					LOGGER.error("## Exception occured:", e);

				}
			}
		};

		addEventService.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, saveEventServiceSuccessHandler);

		saveEventServiceFailedHandler = new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent event) {

				try {
					addEventService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
							saveEventServiceSuccessHandler);
					addEventService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
							saveEventServiceFailedHandler);
				} catch (Exception e) {
					LOGGER.error("## Exception occured:", e);

				}
			}
		};

		addEventService.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED, saveEventServiceFailedHandler);

	}

	/**
	 * This method fetches totals values for a list of medicine names
	 *
	 * @param medNamesList list containing medicine names
	 */
	private void getTotalMedicationVolumeList(List<String> medNamesList) {
		GetTotalMedicationVolumeListService getTotalMedicationVolumeListService = GetTotalMedicationVolumeListService
				.getInstance(medNamesList, PatientSession.getInstance().getPatient().getPatientId(),
						PatientSession.getInstance().getPatientCase().getCaseId());
		getTotalMedicationVolumeListService.restart();

		getMedTotalsServiceSuccessHandler = new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				try {
					Map<String, String> totalsValuesMap = getTotalMedicationVolumeListService.getTotalUsage();
					

					for (Map.Entry<String, String> entry : totalsValuesMap.entrySet()) {
						// totalLblsMap.get(MED_GRID_NAME).get(entry.getKey()).setText("
						// "+df.format(Double.parseDouble(entry.getValue().split("
						// ")[0]))
						// + " " + entry.getValue().split(" ")[1]);
						// totalsListMap.get(MED_GRID_NAME).add(entry.getValue());

						System.out.println(entry.getKey() + "#############" + entry.getValue());

						if (entry.getKey().equalsIgnoreCase("Fentanyl") && entry.getValue() != null) {
							TotalFentanyl = Float.valueOf(entry.getValue().split(" ")[0]);
						} else if (entry.getKey().equalsIgnoreCase("Propofol") && entry.getValue() != null) {
							TotalPropofol = Float.valueOf(entry.getValue().split(" ")[0]);
						} else if (entry.getKey().equalsIgnoreCase("Ketamine") && entry.getValue() != null) {
							TotalKetamine = Float.valueOf(entry.getValue().split(" ")[0]);
						} else if (entry.getKey().equalsIgnoreCase("Vecuronium") && entry.getValue() != null) {
							TotalVecuronium = Float.valueOf(entry.getValue().split(" ")[0]);
						} else if (entry.getKey().equalsIgnoreCase("Cefuroxime") && entry.getValue() != null) {
							TotalCefuroxime = Float.valueOf(entry.getValue().split(" ")[0]);
						} else if (entry.getKey().equalsIgnoreCase("Heparin") && entry.getValue() != null) {
							TotalHeparin = Float.valueOf(entry.getValue().split(" ")[0]);
						} else if (entry.getKey().equalsIgnoreCase("Protamine") && entry.getValue() != null) {
							TotalProtamine = Float.valueOf(entry.getValue().split(" ")[0]);
						} else if (entry.getKey().equalsIgnoreCase("EACA") && entry.getValue() != null) {
							TotalEACA = Float.valueOf(entry.getValue().split(" ")[0]);
						}
						// changes made by vivek
						else if (entry.getKey().equalsIgnoreCase("Meropenam") && entry.getValue() != null) {
							TotalMeropenam = Float.valueOf(entry.getValue().split(" ")[0]);
						} else if (entry.getKey().equalsIgnoreCase("Vancomysin") && entry.getValue() != null) {
							TotalVancomysin = Float.valueOf(entry.getValue().split(" ")[0]);
						} else if (entry.getKey().equalsIgnoreCase("Morphine") && entry.getValue() != null) {
							TotalMorphine = Float.valueOf(entry.getValue().split(" ")[0]);
						} else if (entry.getKey().equalsIgnoreCase("Sufentanyl") && entry.getValue() != null) {
							TotalSufentanyl = Float.valueOf(entry.getValue().split(" ")[0]);
						} else if (entry.getKey().equalsIgnoreCase("Etomidate") && entry.getValue() != null) {
							TotalEtomidate = Float.valueOf(entry.getValue().split(" ")[0]);
						} else if (entry.getKey().equalsIgnoreCase("Atrcurium") && entry.getValue() != null) {
							TotalAtrcurium = Float.valueOf(entry.getValue().split(" ")[0]);
						} else if (entry.getKey().equalsIgnoreCase("CalciumGluconate") && entry.getValue() != null) {
							TotalCalciumGluconate = Float.valueOf(entry.getValue().split(" ")[0]);
						} else if (entry.getKey().equalsIgnoreCase("CalciumChloride") && entry.getValue() != null) {
							TotalCalciumChloride = Float.valueOf(entry.getValue().split(" ")[0]);
						} else if (entry.getKey().equalsIgnoreCase("Dexamethasone") && entry.getValue() != null) {
							TotalDexamethasone = Float.valueOf(entry.getValue().split(" ")[0]);
						} else if (entry.getKey().equalsIgnoreCase("Hydrocortisone") && entry.getValue() != null) {
							TotalHydrocortisone = Float.valueOf(entry.getValue().split(" ")[0]);
						} else if (entry.getKey().equalsIgnoreCase("Magnesium") && entry.getValue() != null) {
							TotalMagnesium = Float.valueOf(entry.getValue().split(" ")[0]);
						} else if (entry.getKey().equalsIgnoreCase("Xylocard") && entry.getValue() != null) {
							TotalXylocard = Float.valueOf(entry.getValue().split(" ")[0]);
						} else if (entry.getKey().equalsIgnoreCase("Phenylephrine") && entry.getValue() != null) {
							TotalPhenylephrine = Float.valueOf(entry.getValue().split(" ")[0]);
						} else if (entry.getKey().equalsIgnoreCase("Atropine") && entry.getValue() != null) {
							TotalAtropine = Float.valueOf(entry.getValue().split(" ")[0]);
						} else if (entry.getKey().equalsIgnoreCase("Adrenaline") && entry.getValue() != null) {
							TotalAdrenaline = Float.valueOf(entry.getValue().split(" ")[0]);
						}
						// changes end
					}

					getTotalMedicationVolumeListService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
							getMedTotalsServiceSuccessHandler);
					getTotalMedicationVolumeListService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
							getMedTotalsServiceFailedHandler);
				} catch (NumberFormatException e) {
					LOGGER.error("## Exception occured:", e);
				}
			}
		};
		getTotalMedicationVolumeListService.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
				getMedTotalsServiceSuccessHandler);

		getMedTotalsServiceFailedHandler = new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				try {
					Main.getInstance().getUtility().showNotification("Error", "Error",
							getTotalMedicationVolumeListService.getException().getMessage());

					getTotalMedicationVolumeListService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
							getMedTotalsServiceSuccessHandler);
					getTotalMedicationVolumeListService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
							getMedTotalsServiceFailedHandler);
				} catch (Exception e) {
					LOGGER.error("## Exception occured:", e);
				}
			}
		};
		getTotalMedicationVolumeListService.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
				getMedTotalsServiceFailedHandler);

	}

	/**
	 * This method fetches totals value for a list of fluids
	 *
	 * @param fluidList list containing fluid names
	 */
	private void getFluidTotalsListService(List<String> fluidList) {
		GetTotalFluidVolumeService getFluidTotalsListService = GetTotalFluidVolumeService
				.getInstance(PatientSession.getInstance().getPatientCase().getCaseId(), fluidList);
		getFluidTotalsListService.restart();

		getFluidTotalsServiceSuccessHandler = new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				try {
					Map<String, String> returnTotalsMap = getFluidTotalsListService.getFluidVolume();

					for (Map.Entry<String, String> entry : returnTotalsMap.entrySet()) {
						// totalLblsMap.get(FLUID_GRID_NAME).get(entry.getKey()).setText(entry.getValue());
						// totalsListMap.get(FLUID_GRID_NAME).add(entry.getValue());

						if (entry.getKey().equalsIgnoreCase("Normal Saline") && entry.getValue() != null) {
							totalNormalSaline = Float.valueOf(entry.getValue().split(" ")[0]);
						} else if (entry.getKey().equalsIgnoreCase("Ringer lactate") && entry.getValue() != null) {
							totalRingerLactate = Float.valueOf(entry.getValue().split(" ")[0]);
						}
					}

					getFluidTotalsListService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
							getFluidTotalsServiceSuccessHandler);
					getFluidTotalsListService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
							getFluidTotalsServiceFailedHandler);
				} catch (Exception e) {
					LOGGER.error("## Exception occured:", e);
				}
			}
		};
		getFluidTotalsListService.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
				getFluidTotalsServiceSuccessHandler);

		getFluidTotalsServiceFailedHandler = new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				try {
					Main.getInstance().getUtility().showNotification("Error", "Error",
							getFluidTotalsListService.getException().getMessage());

					getFluidTotalsListService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
							getFluidTotalsServiceSuccessHandler);
					getFluidTotalsListService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
							getFluidTotalsServiceFailedHandler);
				} catch (Exception e) {
					LOGGER.error("## Exception occured:", e);
				}
			}
		};
		getFluidTotalsListService.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
				getFluidTotalsServiceFailedHandler);

	}

	/**
	 * This method fetches totals value for a list of outputs
	 * @param saveAllCases2 
	 * @param caseId2 
	 * @param patientID2 
	 *
	 * @param outputList
	 *            list containing output names
	 */
	//created by vivek
		public void callIntraOpAuditReportTable1(Integer patientID, Long caseId){
		 
			Calendar AXCOn = null;
			Calendar AXCOff = null;
			Calendar CPBOn = null;
			Calendar CPBOff = null;
			Calendar incesionTime = null;
			Calendar skinclosureTime = null;
			Calendar timeIn = null;
			Calendar handOverTime = null;
			//changes made by vivek
			Calendar inductionTime = null;
			//changes end
			Calendar shiftOutTime = null;
			try {
							Recoveryroomreadings recoveryroomreadings = new Recoveryroomreadings();
							Cannulations cannulations = new Cannulations();
							CPBDetails cpbDetails = new CPBDetails();
							EchoDetails echoDetails = new EchoDetails();
							IntraopAnesthesiarecord intraopAnesthesiarecord = new IntraopAnesthesiarecord();
							IcuPlanEntity icuPlanEntity = new IcuPlanEntity();
							
							
							String userID = UserSession.getInstance().getUserWithrRolesForUserAuthentication().getUserID();
							
							AnesthesiaFetchListWithMedication anesthesiaRecordfromDB=DAOFactory.anesthesiaDetails().getAnesthesiaRecord(patientID,caseId,userID );
												
							Map<String, Object>	patientDetails = DAOFactory.getPatientDetailsReport().getPatientAndCaseDetailsReport(caseId,patientID,userID);
							
								AuditIntraOpReport1 auditIntraOpReport = DAOFactory.AuditReport1().getAuditReport(caseId, userID);
								AuditIntraOpReport1 auditIntraOpReport1 = new AuditIntraOpReport1();
								if(auditIntraOpReport.getAuditReportID()!= null && auditIntraOpReport.getAuditReportID() >0)
								{
									auditIntraOpReport1.setAuditReportID(auditIntraOpReport.getAuditReportID());
									auditIntraOpReport1.setCreatedTime(auditIntraOpReport.getCreatedTime());
									auditIntraOpReport1.setCreatedBy(auditIntraOpReport.getCreatedBy());
									
								}
								if(patientDetails!=null){
								for (Map.Entry<String, Object> entry : patientDetails.entrySet()) {
									
								if(entry.getKey().equalsIgnoreCase("ot")){
									String ot = (String) entry.getValue();
									auditIntraOpReport1.setOt(ot);
								}
								else if(entry.getKey().equalsIgnoreCase("anesthesiologists")){
									String anesthesiologists = (String) entry.getValue();
									auditIntraOpReport1.setAnesthesiologists(anesthesiologists);
								}
								else if(entry.getKey().equalsIgnoreCase("otherAnesthesiologists")){
									String otherAnesthesiologists = (String) entry.getValue();
									auditIntraOpReport1.setOtherAnesthesiologists(otherAnesthesiologists);
								}
								else if(entry.getKey().equalsIgnoreCase("surgeons")){
									String surgeons = (String) entry.getValue();
									auditIntraOpReport1.setSurgeons(surgeons);
								}														
								else if(entry.getKey().equalsIgnoreCase("lstRecoveryRoom")){
									recoveryroomreadings = (Recoveryroomreadings) entry.getValue();
									if (recoveryroomreadings != null) {
									BigDecimal auc = (BigDecimal) recoveryroomreadings.getAreaUnderCurve();
									auditIntraOpReport1.setAuc(auc); 
									}
								}
								else if(entry.getKey().equalsIgnoreCase("cannulations")){
									cannulations = (Cannulations) entry.getValue();
									if (cannulations != null) {										
									String patientPosition = cannulations.getPatientPosition();
									auditIntraOpReport1.setPatientPosition(patientPosition);									
									String peripheralSite = cannulations.getPeripheralSite();
									auditIntraOpReport1.setPeripheralSite(peripheralSite); 									
									String peripheralSize = cannulations.getPeripheralSize();
									auditIntraOpReport1.setPeripheralSize(peripheralSize);									
									String peripheralUltrasoundBlind = cannulations.getPeripheralUltrasoundBlind();
									auditIntraOpReport1.setPeripheralUltrasoundBlind(peripheralUltrasoundBlind); 								
									String arterySite = cannulations.getArterySite();
									auditIntraOpReport1.setArterySite(arterySite); 									
									String arterySize = cannulations.getArterySize();
									auditIntraOpReport1.setArterySize(arterySize); 									
									String arterialUltrasoundBlind = cannulations.getArterialUltrasoundBlind();
									auditIntraOpReport1.setArterialUltrasoundBlind(arterialUltrasoundBlind); 									
									String centralLineSite = cannulations.getSite();
									auditIntraOpReport1.setCentralLineSite(centralLineSite);									
									String vein = cannulations.getVein();
									auditIntraOpReport1.setVein(vein);									
									String methodOfInsertion = cannulations.getMethodOfInsertion();
									auditIntraOpReport1.setMethodOfInsertion(methodOfInsertion);									
									String centralUltrasoundBlind = cannulations.getCentralUltrasoundBlind();
									auditIntraOpReport1.setCentralUltrasoundBlind(centralUltrasoundBlind);								
									String insertionSize = cannulations.getInsertionSize();
									auditIntraOpReport1.setInsertionSize(insertionSize);								
									String insertionLength = cannulations.getInsertionLength();
									auditIntraOpReport1.setInsertionLength(insertionLength);								
									String cannulationsInsertedBy = cannulations.getInsertedBy();
									auditIntraOpReport1.setCannulationsInsertedBy(cannulationsInsertedBy);								
									String complications = cannulations.getComplications();
									auditIntraOpReport1.setComplications(complications);
									}
								}
								if(entry.getKey().equalsIgnoreCase("cpbDetails")){
									cpbDetails = (CPBDetails) entry.getValue();
									if (cpbDetails != null) {
									String primeNS = cpbDetails.getPrimeNS();
									auditIntraOpReport1.setPrimeNS(primeNS);									
									String primeBlood = cpbDetails.getPrimeBlood();
									auditIntraOpReport1.setPrimeBlood(primeBlood);									
									String primeAlbumin = cpbDetails.getPrimeAlbumin();
									auditIntraOpReport1.setPrimeAlbumin(primeAlbumin);									
									String primeMannitol = cpbDetails.getPrimeMannitol();
									auditIntraOpReport1.setPrimeMannitol(primeMannitol);									
									String primeNaHCO3 = cpbDetails.getPrimeNaHCO3();
									auditIntraOpReport1.setPrimeNaHCO3(primeNaHCO3);									
									String primeCaCl2 = cpbDetails.getPrimeCaCl2();
									auditIntraOpReport1.setPrimeCaCl2(primeCaCl2);									
									String primeHeparin = cpbDetails.getPrimeHeparin();
									auditIntraOpReport1.setPrimeHeparin(primeHeparin);									
									String primeDetails = cpbDetails.getPrimeDetails();
									auditIntraOpReport1.setPrimeDetails(primeDetails);									
									String preCrystalloidCPG = cpbDetails.getPreCrystalloidCPG();
									auditIntraOpReport1.setPreCrystalloidCPG(preCrystalloidCPG);									
									String preNS = cpbDetails.getPreNS();
									auditIntraOpReport1.setPreNS(preNS);								
									String preBlood = cpbDetails.getPreBlood();
									auditIntraOpReport1.setPreBlood(preBlood);									
									String prePrime = cpbDetails.getPrePrime();
									auditIntraOpReport1.setPrePrime(prePrime);									
									String preRL = cpbDetails.getPreRL();
									auditIntraOpReport1.setPreRL(preRL);									
									String preNaHCO3 = cpbDetails.getPreNaHCO3();
									auditIntraOpReport1.setPreNaHCO3(preNaHCO3);									
									String preDetails = cpbDetails.getPreDetails();
									auditIntraOpReport1.setPreDetails(preDetails);									
									String postIVF = cpbDetails.getPostIVF();
									auditIntraOpReport1.setPostIVF(postIVF);									
									String postBlood = cpbDetails.getPostBlood();
									auditIntraOpReport1.setPostBlood(postBlood);									
									String postFFP = cpbDetails.getPostFFP();
									auditIntraOpReport1.setPostFFP(postFFP);									
									String postPC = cpbDetails.getPostPC();
									auditIntraOpReport1.setPostPC(postPC);									
									String postALB = cpbDetails.getPostALB();
									auditIntraOpReport1.setPostALB(postALB);									
									String postNetBalance = cpbDetails.getPostNetBalance();
									auditIntraOpReport1.setPostNetBalance(postNetBalance);									
									String postDetails = cpbDetails.getPostDetails();
									auditIntraOpReport1.setPostDetails(postDetails);;
									}
								}
								if(entry.getKey().equalsIgnoreCase("echoDetails")){
									echoDetails = (EchoDetails) entry.getValue();
									if (echoDetails != null) {
									String machineName = echoDetails.getMachineName();
									auditIntraOpReport1.setMachineName(machineName);									
									String probeSize = echoDetails.getProbeSize();
									auditIntraOpReport1.setProbeSize(probeSize);									
									String echoInsertedBy = echoDetails.getInsertedBy();
									auditIntraOpReport1.setEchoInsertedBy(echoInsertedBy);									
									String bloodOnTip = echoDetails.getBloodOnTip();
									auditIntraOpReport1.setBloodOnTip(bloodOnTip);									
									String preEchoDetails = echoDetails.getPreDetails();
									auditIntraOpReport1.setPreEchoDetails(preEchoDetails);									
									String intraOPDetails = echoDetails.getIntraOpDetails();
									auditIntraOpReport1.setIntraOPDetails(intraOPDetails);									
									String changeInDiagnosis = echoDetails.getChangeInDiagnosis();
									auditIntraOpReport1.setChangeInDiagnosis(changeInDiagnosis);									
									String newFindings = echoDetails.getNewFindings();
									auditIntraOpReport1.setNewFindings(newFindings);									
									String postEchoDetails = echoDetails.getPostDetails();
									auditIntraOpReport1.setPostEchoDetails(postEchoDetails);
									}
								}
								if(entry.getKey().equalsIgnoreCase("lstAnesthesiaRecord")){
									intraopAnesthesiarecord = (IntraopAnesthesiarecord) entry.getValue();
									if (intraopAnesthesiarecord != null) {
									String GA_ETTSize = intraopAnesthesiarecord.getGaEttsize();
									auditIntraOpReport1.setGA_ETTSize(GA_ETTSize);									
									String GA_ETTAttempts = intraopAnesthesiarecord.getGaEttattempts();
									auditIntraOpReport1.setGA_ETTAttempts(GA_ETTAttempts);									
									String GA_ETTFixedAt = intraopAnesthesiarecord.getGaEttfixedAt();
									auditIntraOpReport1.setGA_ETTFixedAt(GA_ETTFixedAt);									
									String coughedUncoughed = intraopAnesthesiarecord.getcuffedUncuffed();
									auditIntraOpReport1.setCoughedUncoughed(coughedUncoughed);									
									String GA_SupraglotticSize = intraopAnesthesiarecord.getGaSupraglotticSize();
									auditIntraOpReport1.setGA_SupraglotticSize(GA_SupraglotticSize);									
									String GA_SupraglotticAttempts = intraopAnesthesiarecord.getGaSupraglotticAttempts();
									auditIntraOpReport1.setGA_SupraglotticAttempts(GA_SupraglotticAttempts);								
									String GA_SupraglotticComment = intraopAnesthesiarecord.getGaSupraglotticComment();
									auditIntraOpReport1.setGA_SupraglotticComment(GA_SupraglotticComment);									
									String GA_Ventilation = intraopAnesthesiarecord.getGaVentilation();
									auditIntraOpReport1.setGA_Ventilation(GA_Ventilation);									
									String GA_AirPressure = intraopAnesthesiarecord.getGaAirPressure();
									auditIntraOpReport1.setGA_AirPressure(GA_AirPressure);									
									String GA_FiO2 = intraopAnesthesiarecord.getGaFiO2();
									auditIntraOpReport1.setGA_FiO2(GA_FiO2);									
									String gaVt = intraopAnesthesiarecord.getGaVt();
									auditIntraOpReport1.setGaVt(gaVt);									
									String gaRr = intraopAnesthesiarecord.getGaRr();
									auditIntraOpReport1.setGaRr(gaRr);									
									String gaIe = intraopAnesthesiarecord.getGaIe();
									auditIntraOpReport1.setGaIe(gaIe);									
									String GA_VentilationComment = intraopAnesthesiarecord.getGaVentilationComment();
									auditIntraOpReport1.setGA_VentilationComment(GA_VentilationComment);									
									String RA_SpinalSite = intraopAnesthesiarecord.getRaSpinalSite();
									auditIntraOpReport1.setRA_SpinalSite(RA_SpinalSite);									
									String RA_SpinalNeedle = intraopAnesthesiarecord.getRaSpinalNeedle();
									auditIntraOpReport1.setRA_SpinalNeedle(RA_SpinalNeedle);									
									String RA_SpinalHsl = intraopAnesthesiarecord.getRaSpinalHsl();
									auditIntraOpReport1.setRA_SpinalHsl(RA_SpinalHsl);									
									String RA_EpiduralSite = intraopAnesthesiarecord.getRaEpiduralSite();
									auditIntraOpReport1.setRA_EpiduralSite(RA_EpiduralSite);									
									String RA_EpiduralNeedle = intraopAnesthesiarecord.getRaEpiduralNeedle();
									auditIntraOpReport1.setRA_EpiduralNeedle(RA_EpiduralNeedle);									
									String RA_EpiduralCfixedAt = intraopAnesthesiarecord.getRaEpiduralCfixedAt();
									auditIntraOpReport1.setRA_EpiduralCfixedAt(RA_EpiduralCfixedAt);									
									String RA_Ulbsite = intraopAnesthesiarecord.getRaUlbsite();
									auditIntraOpReport1.setRA_Ulbsite(RA_Ulbsite);									
									String RA_UlbBpbapproach = intraopAnesthesiarecord.getRaUlbBpbapproach();
									auditIntraOpReport1.setRA_UlbBpbapproach(RA_UlbBpbapproach);									
									String RA_UlbPnbapproach = intraopAnesthesiarecord.getRaUlbPnbapproach();
									auditIntraOpReport1.setRA_UlbPnbapproach(RA_UlbPnbapproach);									
									String RA_Llbsite = intraopAnesthesiarecord.getRaLlbsite();
									auditIntraOpReport1.setRA_Llbsite(RA_Llbsite);									
									String rA_LlbLpnbapproach = intraopAnesthesiarecord.getRaLlbLpnbapproach();
									auditIntraOpReport1.setRA_LlbLpnbapproach(rA_LlbLpnbapproach);									
									String RA_LlbSpnbapproach = intraopAnesthesiarecord.getRaLlbSpnbapproach();
									auditIntraOpReport1.setRA_LlbSpnbapproach(RA_LlbSpnbapproach);									
									String MAC_Comment = intraopAnesthesiarecord.getMacComment();
									auditIntraOpReport1.setMAC_Comment(MAC_Comment);
									}
								}
								if(entry.getKey().equalsIgnoreCase("lstRecoveryRoom")){
									recoveryroomreadings = (Recoveryroomreadings) entry.getValue();
									if (recoveryroomreadings != null) {
									String bp = recoveryroomreadings.getInVitalsBp();
									if(bp != null){
										
									String [] lstBp = bp.split("/");
									
									if(lstBp.length ==2) {
										String soSbp=lstBp[0];
										String soDbp=lstBp[1];
										int Mbp = (Integer.parseInt(soSbp.trim())+Integer.parseInt(soDbp.trim()))/2;
										String soMbp = Integer.toString(Mbp);
										auditIntraOpReport1.setSoSbp(soSbp);
										auditIntraOpReport1.setSoDbp(soDbp);
										auditIntraOpReport1.setSoMbp(soMbp);
										}							
									}									
									BigDecimal soPr = recoveryroomreadings.getInVitalsPulse();
									auditIntraOpReport1.setSoPr(soPr);									
									BigDecimal soSpo2 = recoveryroomreadings.getInVitalsSpo2();
									auditIntraOpReport1.setSoSpo2(soSpo2);									
									BigDecimal soRr = recoveryroomreadings.getInVitalsResp();
									auditIntraOpReport1.setSoRr(soRr);									
									BigDecimal soTemp = recoveryroomreadings.getInVitalsTemp();
									auditIntraOpReport1.setSoTemp(soTemp);									
									String SO_RespSupport = recoveryroomreadings.getRespSupport();
									auditIntraOpReport1.setSO_RespSupport(SO_RespSupport);									
									String IVLineIVFluidsDetails = recoveryroomreadings.getIvlineIvfluidsDetails();
									auditIntraOpReport1.setIVLineIVFluidsDetails(IVLineIVFluidsDetails);									
									String IVLineInoTropsDetails = recoveryroomreadings.getIvlineInoTropsDetails();
									auditIntraOpReport1.setIVLineInoTropsDetails(IVLineInoTropsDetails);									
									String IVLineBloodDetails = recoveryroomreadings.getIvlineBloodDetails();
									auditIntraOpReport1.setIVLineBloodDetails(IVLineBloodDetails);								
									String IVLineInfusionDetails = recoveryroomreadings.getIvlineInfusionDetails();
									auditIntraOpReport1.setIVLineInfusionDetails(IVLineInfusionDetails);									
									String pacingSupport = recoveryroomreadings.getPacingSupport();
									auditIntraOpReport1.setPacingSupport(pacingSupport);									
									BigDecimal beatsPerMinutes = recoveryroomreadings.getBeatsPerMinutes();
									auditIntraOpReport1.setBeatsPerMinutes(beatsPerMinutes);									
									String backup_Active = recoveryroomreadings.getBa();
									auditIntraOpReport1.setBackup_Active(backup_Active);									
									String leads = recoveryroomreadings.getLeads();
									auditIntraOpReport1.setLeads(leads);									
									String mode = recoveryroomreadings.getMode();
									auditIntraOpReport1.setMode(mode);									
									String negativeSuction = recoveryroomreadings.getNegativeSuction();
									auditIntraOpReport1.setNegativeSuction(negativeSuction);									
									String bleedingIssues = recoveryroomreadings.getBleedingIssues();
									auditIntraOpReport1.setBleedingIssues(bleedingIssues);									
									String pleural = recoveryroomreadings.getPleural();
									if(pleural!=null){
									if(pleural.contains("Right")){
										auditIntraOpReport1.setRTPleural(pleural);
									}
									else if(pleural.contains("Left")){
										auditIntraOpReport1.setLTPleural(pleural);
									}
									}
									String rs_pc = recoveryroomreadings.getRs_pc();
									if(rs_pc!=null){
									if(rs_pc.contains("RS")){
										auditIntraOpReport1.setRs(rs_pc);;
									}
									else if(rs_pc.contains("PC")){
										auditIntraOpReport1.setPc(rs_pc);;
									}	
									}
									Integer aldreteScore = recoveryroomreadings.getAldreteScoreAtShiftingToPacu();
									auditIntraOpReport1.setAldreteScore(aldreteScore);							
									BigDecimal inotropicScore = recoveryroomreadings.getInotropicScore();
									auditIntraOpReport1.setInotropicScore(inotropicScore);									
									String specialInstructions = recoveryroomreadings.getSpecialInstructionsDetails();
									auditIntraOpReport1.setSpecialInstructions(specialInstructions);
									
									SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
									Date pacuTime= recoveryroomreadings.getPacuTime();
									if(pacuTime!=null){
									String time = dateFormat.format(pacuTime);
									auditIntraOpReport1.setPACUTime(time);
									}
									SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
									Date pacuDate= recoveryroomreadings.getPacuTime();	
									if(pacuDate!=null){
									String date= dateFormat1.format(pacuDate);
									auditIntraOpReport1.setPACUDate(date);
									}
									}
								}
								if(entry.getKey().equalsIgnoreCase("icuPlanDetails")){
									icuPlanEntity = (IcuPlanEntity) entry.getValue();
									if (icuPlanEntity != null) {
									String ICU_Extubation = icuPlanEntity.getExtubation();
									auditIntraOpReport1.setICU_Extubation(ICU_Extubation);									
									String ICU_Analgesia = icuPlanEntity.getAnalgesia();
									auditIntraOpReport1.setICU_Analgesia(ICU_Analgesia);									
									String ICU_Paralysis = icuPlanEntity.getParalysis();
									auditIntraOpReport1.setICU_Paralysis(ICU_Paralysis);									
									String iCU_Sedation = icuPlanEntity.getSedation();
									auditIntraOpReport1.setICU_Sedation(iCU_Sedation);									
									String icuNiv = icuPlanEntity.getPostExtubation();
									auditIntraOpReport1.setIcuNiv(icuNiv);
									BigDecimal mao = icuPlanEntity.getMao();
									auditIntraOpReport1.setMao(mao);								
									BigDecimal cvp = icuPlanEntity.getCvp();
									auditIntraOpReport1.setCvp(cvp);								
									BigDecimal hr = icuPlanEntity.getHr();
									auditIntraOpReport1.setHr(hr);								
									BigDecimal spo2 = icuPlanEntity.getSpO2();
									auditIntraOpReport1.setSpo2(spo2);							
									BigDecimal rL_Rate = icuPlanEntity.getRlRate();
									auditIntraOpReport1.setRlRate(rL_Rate);								
									String nS_Rate = icuPlanEntity.getDrl();
									auditIntraOpReport1.setNsRate(nS_Rate);								
									String investigations = icuPlanEntity.getInvestigations();
									auditIntraOpReport1.setInvestigations(investigations);
									}
								}							
								if(entry.getKey().equalsIgnoreCase("EventsList")){
								List<IntraopEventlog>	intraopEventlogList = (List<IntraopEventlog>) entry.getValue();
								int index =0;
								for (IntraopEventlog intraopEventlog:intraopEventlogList){			
								if(intraopEventlog.getCustomEventName().equals("Time In")){
									auditIntraOpReport1.setTime_In(intraopEventlog.getEventTime());
									auditIntraOpReport1.setTime_In_Comment(intraopEventlog.getComments());
									timeIn= Calendar.getInstance();
									timeIn.setTime(intraopEventlog.getEventTime());
								}
								else if(intraopEventlog.getCustomEventName().equals("Induction")){
									auditIntraOpReport1.setInduction_Time(intraopEventlog.getEventTime());
									auditIntraOpReport1.setInduction_Time_Comment(intraopEventlog.getComments());
									inductionTime= Calendar.getInstance();
									inductionTime.setTime(intraopEventlog.getEventTime());
								}
								else if(intraopEventlog.getCustomEventName().equals("Intubation")){
									auditIntraOpReport1.setIntubation_Time(intraopEventlog.getEventTime());
									auditIntraOpReport1.setIntubation_Time_Comment(intraopEventlog.getComments());	
								}
								else if(intraopEventlog.getCustomEventName().equals("Handover")){
									auditIntraOpReport1.setHand_Over_Time(intraopEventlog.getEventTime());
									auditIntraOpReport1.setHand_Over_Time_Comment(intraopEventlog.getComments());	
									handOverTime= Calendar.getInstance();
									handOverTime.setTime(intraopEventlog.getEventTime());
								}
								else if(intraopEventlog.getCustomEventName().equals("Incision")){
									auditIntraOpReport1.setIncision(intraopEventlog.getEventTime());
									auditIntraOpReport1.setIncisionComment(intraopEventlog.getComments());	
									incesionTime= Calendar.getInstance();
									incesionTime.setTime(intraopEventlog.getEventTime());
								}
								else if(intraopEventlog.getCustomEventName().equals("Cannulation")){
									auditIntraOpReport1.setCannulation(intraopEventlog.getEventTime());
									auditIntraOpReport1.setCannulationComment(intraopEventlog.getComments());	
								}
								else if(intraopEventlog.getCustomEventName().equals("CPB On")){
									auditIntraOpReport1.setCpbOn(intraopEventlog.getEventTime());
									auditIntraOpReport1.setCpbOnComment(intraopEventlog.getComments());	
									CPBOn= Calendar.getInstance();
									CPBOn.setTime(intraopEventlog.getEventTime());
								}
								else if(intraopEventlog.getCustomEventName().equals("CPB Off")){
									auditIntraOpReport1.setCpbOff(intraopEventlog.getEventTime());
									auditIntraOpReport1.setCpbOffComment(intraopEventlog.getComments());
									CPBOff= Calendar.getInstance();
									CPBOff.setTime(intraopEventlog.getEventTime());
									
								}else if(intraopEventlog.getCustomEventName().equals("AXC On")){
									auditIntraOpReport1.setAxcOn(intraopEventlog.getEventTime());
									auditIntraOpReport1.setAxcOnComment(intraopEventlog.getComments());	
									AXCOn = Calendar.getInstance();
									AXCOn.setTime(intraopEventlog.getEventTime());
								}
								else if(intraopEventlog.getCustomEventName().equals("AXC Off")){
									auditIntraOpReport1.setAxcOff(intraopEventlog.getEventTime());
									auditIntraOpReport1.setAxcOffComment(intraopEventlog.getComments());
									AXCOff= Calendar.getInstance();
									AXCOff.setTime(intraopEventlog.getEventTime());
								}
								else if(intraopEventlog.getCustomEventName().equals("Decannulation")){
									auditIntraOpReport1.setDecannulation(intraopEventlog.getEventTime());
									auditIntraOpReport1.setDecannulationComment(intraopEventlog.getComments());	
								}
								else if(intraopEventlog.getCustomEventName().equals("Skin Closure ")){
									auditIntraOpReport1.setSkinClosure(intraopEventlog.getEventTime());
									auditIntraOpReport1.setSkinClosureComment(intraopEventlog.getComments());
									skinclosureTime= Calendar.getInstance();
									skinclosureTime.setTime(intraopEventlog.getEventTime());
								}
								else if(intraopEventlog.getCustomEventName().equals("Shift Out")){
									auditIntraOpReport1.setShiftoutTime(intraopEventlog.getEventTime());
									auditIntraOpReport1.setShiftoutTimeComment("ShiftOut Time Event");	
									shiftOutTime= Calendar.getInstance();
									shiftOutTime.setTime(intraopEventlog.getEventTime());
								}
								else {
									if(index ==0){
									auditIntraOpReport1.setNewEvent1(intraopEventlog.getEventTime());
									auditIntraOpReport1.setNewEvent1Comment(intraopEventlog.getComments());	
								
									}
									else if (index ==1){
									auditIntraOpReport1.setNewEvent2(intraopEventlog.getEventTime());
									auditIntraOpReport1.setNewEvent2Comment(intraopEventlog.getComments());	
								}
									else if (index ==2){
									auditIntraOpReport1.setNewEvent3(intraopEventlog.getEventTime());
									auditIntraOpReport1.setNewEvent3Comment(intraopEventlog.getComments());	
								}
									else if (index ==3){
										auditIntraOpReport1.setNewEvent4(intraopEventlog.getEventTime());
										auditIntraOpReport1.setNewEvent4Comment(intraopEventlog.getComments());	
									}
									else if (index ==4){
										auditIntraOpReport1.setNewEvent5(intraopEventlog.getEventTime());
										auditIntraOpReport1.setNewEvent5Comment(intraopEventlog.getComments());	
									}
									++index;
								}
									
							}
						}
								
								AuditReportParams auditReportParams = new AuditReportParams();
								DecimalFormat df = new DecimalFormat("#.00");

								if (AXCOn != null && AXCOff != null)
									auditReportParams.setAXCTime(Float
											.valueOf(df.format((AXCOff.getTimeInMillis() - AXCOn.getTimeInMillis()) * 0.00001 * 0.0278)));
								
								if (CPBOn != null && CPBOff != null)
									auditReportParams.setCPBTime( Float
											.valueOf(df.format((CPBOff.getTimeInMillis() - CPBOn.getTimeInMillis()) * 0.00001 * 0.0278)));

								if (skinclosureTime != null && handOverTime != null)
									auditReportParams.setSurgeryTime( Float.valueOf(df.format(
											(skinclosureTime.getTimeInMillis() - handOverTime.getTimeInMillis()) * 0.00001 * 0.0278)));

								if (shiftOutTime != null && inductionTime != null)
									auditReportParams.setAnesthesiatime( Float.valueOf(
											df.format((shiftOutTime.getTimeInMillis() - inductionTime.getTimeInMillis()) * 0.00001 * 0.0278)));

								if (timeIn != null && handOverTime != null) {
									auditReportParams.setHandOverTime(Float.valueOf(
											df.format((handOverTime.getTimeInMillis() - timeIn.getTimeInMillis()) * 0.00001 * 0.0278)));
								}

								if (incesionTime != null && handOverTime != null) {
									auditReportParams.setPatientPrepationTime(Float.valueOf(df
											.format((incesionTime.getTimeInMillis() - handOverTime.getTimeInMillis()) * 0.00001 * 0.0278)));
								}
								if (skinclosureTime != null && shiftOutTime != null) {
									auditReportParams.setShiftingOutTime(Float.valueOf(df.format(
											(shiftOutTime.getTimeInMillis() - skinclosureTime.getTimeInMillis()) * 0.00001 * 0.0278)));
								}
								//changes made by vivek
								if (shiftOutTime != null && timeIn != null) {
									auditReportParams.setOTTime( Float.valueOf(df.format(
											(shiftOutTime.getTimeInMillis() - timeIn.getTimeInMillis()) * 0.00001 * 0.0278)));
								}			
						
								
								List<String> medList = GetMasterDataDaoImpl.getInstance().getMedicationsForHistory(caseId,
										UserSession.getInstance().getUserWithrRolesForUserAuthentication().getUserName());
								if (medList.size() == 0)
									medList.add("testing");

								HashMap<String,String>	totalUsage = DAOFactory.medicationLog().getTotalDosageListOfMedication(medList, patientID, caseId,  UserSession.getInstance().getUserWithrRolesForUserAuthentication().getUserID());

								for (Map.Entry<String, String> entry1 : totalUsage.entrySet()) {
									// totalLblsMap.get(MED_GRID_NAME).get(entry.getKey()).setText("
									// "+df.format(Double.parseDouble(entry.getValue().split("
									// ")[0]))
									// + " " + entry.getValue().split(" ")[1]);
									// totalsListMap.get(MED_GRID_NAME).add(entry.getValue());

									System.out.println(entry1.getKey() + "#############" + entry1.getValue());

									if (entry1.getKey().equalsIgnoreCase("Fentanyl") && entry1.getValue() != null) {
										auditReportParams.setTotalFentanyl(Float.valueOf(entry1.getValue().split(" ")[0]));
									} else if (entry1.getKey().equalsIgnoreCase("Propofol") && entry1.getValue() != null) {
										auditReportParams.setTotalPropofol(Float.valueOf(entry1.getValue().split(" ")[0]));
									} else if (entry1.getKey().equalsIgnoreCase("Ketamine") && entry1.getValue() != null) {
										auditReportParams.setTotalKetamine(Float.valueOf(entry1.getValue().split(" ")[0]));
									} else if (entry1.getKey().equalsIgnoreCase("Vecuronium") && entry1.getValue() != null) {
										auditReportParams.setTotalVecuronium(Float.valueOf(entry1.getValue().split(" ")[0]));
									} else if (entry1.getKey().equalsIgnoreCase("Cefuroxime") && entry1.getValue() != null) {
										auditReportParams.setTotalCefuroxime(Float.valueOf(entry1.getValue().split(" ")[0]));
									} else if (entry1.getKey().equalsIgnoreCase("Heparin") && entry1.getValue() != null) {
										auditReportParams.setTotalHeparin( Float.valueOf(entry1.getValue().split(" ")[0]));
									} else if (entry1.getKey().equalsIgnoreCase("Protamine") && entry1.getValue() != null) {
										auditReportParams.setTotalProtamine(Float.valueOf(entry1.getValue().split(" ")[0]));
									} else if (entry1.getKey().equalsIgnoreCase("EACA") && entry1.getValue() != null) {
										auditReportParams.setTotalEACA( Float.valueOf(entry1.getValue().split(" ")[0]));
									}
									// changes made by vivek
									else if (entry1.getKey().equalsIgnoreCase("Meropenam") && entry1.getValue() != null) {
										auditReportParams.setTotalMeropenam(Float.valueOf(entry1.getValue().split(" ")[0]));
									} else if (entry1.getKey().equalsIgnoreCase("Vancomysin") && entry1.getValue() != null) {
										auditReportParams.setTotalVancomysin(Float.valueOf(entry1.getValue().split(" ")[0]));
									} else if (entry1.getKey().equalsIgnoreCase("Morphine") && entry1.getValue() != null) {
										auditReportParams.setTotalMorphine( Float.valueOf(entry1.getValue().split(" ")[0]));
									} else if (entry1.getKey().equalsIgnoreCase("Sufentanyl") && entry1.getValue() != null) {
										auditReportParams.setTotalSufentanyl( Float.valueOf(entry1.getValue().split(" ")[0]));
									} else if (entry1.getKey().equalsIgnoreCase("Etomidate") && entry1.getValue() != null) {
										auditReportParams.setTotalEtomidate( Float.valueOf(entry1.getValue().split(" ")[0]));
									} else if (entry1.getKey().equalsIgnoreCase("Atrcurium") && entry1.getValue() != null) {
										auditReportParams.setTotalAtrcurium( Float.valueOf(entry1.getValue().split(" ")[0]));
									} else if (entry1.getKey().equalsIgnoreCase("CalciumGluconate") && entry1.getValue() != null) {
										auditReportParams.setTotalCalciumGluconate(Float.valueOf(entry1.getValue().split(" ")[0]));
									} else if (entry1.getKey().equalsIgnoreCase("CalciumChloride") && entry1.getValue() != null) {
										auditReportParams.setTotalCalciumChloride( Float.valueOf(entry1.getValue().split(" ")[0]));
									} else if (entry1.getKey().equalsIgnoreCase("Dexamethasone") && entry1.getValue() != null) {
										auditReportParams.setTotalDexamethasone(Float.valueOf(entry1.getValue().split(" ")[0]));
									} else if (entry1.getKey().equalsIgnoreCase("Hydrocortisone") && entry1.getValue() != null) {
										auditReportParams.setTotalHydrocortisone( Float.valueOf(entry1.getValue().split(" ")[0]));
									} else if (entry1.getKey().equalsIgnoreCase("Magnesium") && entry1.getValue() != null) {
										auditReportParams.setTotalMagnesium(Float.valueOf(entry1.getValue().split(" ")[0]));
									} else if (entry1.getKey().equalsIgnoreCase("Xylocard") && entry1.getValue() != null) {
										auditReportParams.setTotalXylocard(Float.valueOf(entry1.getValue().split(" ")[0]));
									} else if (entry1.getKey().equalsIgnoreCase("Phenylephrine") && entry1.getValue() != null) {
										auditReportParams.setTotalPhenylephrine( Float.valueOf(entry1.getValue().split(" ")[0]));
									} else if (entry1.getKey().equalsIgnoreCase("Atropine") && entry1.getValue() != null) {
										auditReportParams.setTotalAtropine( Float.valueOf(entry1.getValue().split(" ")[0]));
									} else if (entry1.getKey().equalsIgnoreCase("Adrenaline") && entry1.getValue() != null) {
										auditReportParams.setTotalAdrenaline( Float.valueOf(entry1.getValue().split(" ")[0]));
									}
									// changes end
								}

								List<String> fluidList = GetMasterDataDaoImpl.getInstance().getFluidsForHistory(caseId,
										UserSession.getInstance().getUserWithrRolesForUserAuthentication().getUserName());
								if (fluidList.size() == 0)
									fluidList.add("testing");
								HashMap<String,String> returnTotalsMap  = DAOFactory.getFluidList().getTotalFluidNameVolume(caseId,fluidList,UserSession.getInstance().getUserWithrRolesForUserAuthentication().getUserID());

								for (Map.Entry<String, String> entry1 : returnTotalsMap.entrySet()) {
									// totalLblsMap.get(FLUID_GRID_NAME).get(entry.getKey()).setText(entry.getValue());
									// totalsListMap.get(FLUID_GRID_NAME).add(entry.getValue());

									if (entry1.getKey().equalsIgnoreCase("Normal Saline") && entry1.getValue() != null) {
										auditReportParams.setTotalNormalSaline( Float.valueOf(entry1.getValue().split(" ")[0]));
									} else if (entry1.getKey().equalsIgnoreCase("Ringer lactate") && entry1.getValue() != null) {
										auditReportParams.setTotalRingerLactate( Float.valueOf(entry1.getValue().split(" ")[0]));
									}
								}

								
								//changes end}							
					
								//changes made by vivek
								auditIntraOpReport1.setCaseID(caseId);
								auditIntraOpReport1.setAXCTime(auditReportParams.getAXCTime());
								auditIntraOpReport1.setCPBTime(auditReportParams.getCPBTime());						
								auditIntraOpReport1.setSurgeryTime(auditReportParams.getSurgeryTime());
								auditIntraOpReport1.setAnesthesiaTime(auditReportParams.getAnesthesiatime());
								auditIntraOpReport1.setHandOverTime(auditReportParams.getHandOverTime());
								auditIntraOpReport1.setPatientPreparationTime(auditReportParams.getPatientPrepationTime());
								auditIntraOpReport1.setOTTime(auditReportParams.getOTTime());
								auditIntraOpReport1.setShiftingOutTime(auditReportParams.getShiftingOutTime());
								auditIntraOpReport1.setMedFentanyl(auditReportParams.getTotalFentanyl());
								auditIntraOpReport1.setMedPropofol(auditReportParams.getTotalPropofol());
								auditIntraOpReport1.setMedKetamine(auditReportParams.getTotalKetamine());
								auditIntraOpReport1.setMedVecuronium(auditReportParams.getTotalVecuronium());
								auditIntraOpReport1.setMedCefuroxime(auditReportParams.getTotalCefuroxime());
								auditIntraOpReport1.setMedHeparin(auditReportParams.getTotalHeparin());
								auditIntraOpReport1.setMedProtamine(auditReportParams.getTotalProtamine());
								auditIntraOpReport1.setMedEaca(auditReportParams.getTotalEACA());
								auditIntraOpReport1.setFluidNormalSaline(auditReportParams.getTotalNormalSaline());
								auditIntraOpReport1.setFluidRingerLactate(auditReportParams.getTotalRingerLactate());
								auditIntraOpReport1.setMedMeropenam(auditReportParams.getTotalMeropenam());
								auditIntraOpReport1.setMedVancomysin(auditReportParams.getTotalVancomysin());
								auditIntraOpReport1.setMedMorphine(auditReportParams.getTotalMorphine());
								auditIntraOpReport1.setMedSufentanyl(auditReportParams.getTotalSufentanyl());
								auditIntraOpReport1.setMedEtomidate(auditReportParams.getTotalEtomidate());
								auditIntraOpReport1.setMedAtrcurium(auditReportParams.getTotalAtrcurium());
								auditIntraOpReport1.setMedCalciumGluconate(auditReportParams.getTotalCalciumGluconate());
								auditIntraOpReport1.setMedCalciumChloride(auditReportParams.getTotalCalciumChloride());
								auditIntraOpReport1.setMedDexamethasone(auditReportParams.getTotalDexamethasone());
								auditIntraOpReport1.setMedHydrocortisone(auditReportParams.getTotalHydrocortisone());
								auditIntraOpReport1.setMedMagnesium(auditReportParams.getTotalMagnesium());
								auditIntraOpReport1.setMedXylocard(auditReportParams.getTotalXylocard());
								auditIntraOpReport1.setMedPhenylephrine(auditReportParams.getTotalPhenylephrine());
								auditIntraOpReport1.setMedAtropine(auditReportParams.getTotalAtropine());
								auditIntraOpReport1.setMedAdrenaline(auditReportParams.getTotalAdrenaline());								
								//changes end							
							}
						}
								if(anesthesiaRecordfromDB!=null){
									if(anesthesiaRecordfromDB.getListOfAnesthesiaMedication()!=null){
									for(IntraopAnesthesiamedicationrecord intraopAnesthesiamedicationrecord:anesthesiaRecordfromDB.getListOfAnesthesiaMedication()){
										if(intraopAnesthesiamedicationrecord.getAnesthesiaSubType()!=null){
										if(intraopAnesthesiamedicationrecord.getAnesthesiaSubType().equals("Spinal")){											
											auditIntraOpReport1.setSpinalMedication(intraopAnesthesiamedicationrecord.getMedicine());
											auditIntraOpReport1.setSpinalVolume(intraopAnesthesiamedicationrecord.getVolume());
											auditIntraOpReport1.setSpinalAdjustment(intraopAnesthesiamedicationrecord.getAdjustment());
										} 
										else if(intraopAnesthesiamedicationrecord.getAnesthesiaSubType().equals("Epidural")){											
											auditIntraOpReport1.setEpiduralMedication(intraopAnesthesiamedicationrecord.getMedicine());
											auditIntraOpReport1.setEpiduralVolume(intraopAnesthesiamedicationrecord.getVolume());
											auditIntraOpReport1.setEpiduralAdjustment(intraopAnesthesiamedicationrecord.getAdjustment());
										}
										else if(intraopAnesthesiamedicationrecord.getAnesthesiaSubType().equals("Upper Limb Block")){											
											auditIntraOpReport1.setUlbMedication(intraopAnesthesiamedicationrecord.getMedicine());
											auditIntraOpReport1.setUlbVolume(intraopAnesthesiamedicationrecord.getVolume());
											auditIntraOpReport1.setUlbAdjustment(intraopAnesthesiamedicationrecord.getAdjustment());
										}
										else if(intraopAnesthesiamedicationrecord.getAnesthesiaSubType().equals("Lower Limb Block")){											
											auditIntraOpReport1.setLlbMedication(intraopAnesthesiamedicationrecord.getMedicine());
											auditIntraOpReport1.setLlbVolume(intraopAnesthesiamedicationrecord.getVolume());
											auditIntraOpReport1.setLlbAdjustment(intraopAnesthesiamedicationrecord.getAdjustment());
										}
									}
								}
									}
									}
								DAOFactory.AuditReport1().createAuditReport(auditIntraOpReport1, userID);
								
								callIntraOpAuditReportTable2(patientID,caseId,auditIntraOpReport1.getTime_In(),auditIntraOpReport1.getShiftoutTime());

						
						} catch (Exception e) {
							e.printStackTrace();
							LOGGER.error("## Exception occured:", e);

						}
					
				// ---failure
			
		}

	// created by Vivek
	public void callIntraOpAuditReportTable2(Integer patientID, Long caseId, Date startDate, Date endDate) {
		
			// ---success
			String userID = UserSession.getInstance().getUserWithrRolesForUserAuthentication().getUserID();
					try {
						AuditIntraOpReport2 auditIntraOpReport = DAOFactory.AuditReport2().getAuditReport(caseId, userID);
						AuditIntraOpReport2 auditIntraOpReport2 = new AuditIntraOpReport2();
						if(auditIntraOpReport.getAuditReportID()!= null && auditIntraOpReport.getAuditReportID() >0)
						{
							auditIntraOpReport2.setAuditReportID(auditIntraOpReport.getAuditReportID());
							auditIntraOpReport2.setCreatedTime(auditIntraOpReport.getCreatedTime());
							auditIntraOpReport2.setCreatedBy(auditIntraOpReport.getCreatedBy());
							
						}
						
						
						

						List<InvestigationMapperAndValue>	testHistoryList = DAOFactory.getInvestigationValueView()
								.getInvestigationvaluecasemapperlist(caseId, startDate, endDate, userID);

						if (testHistoryList != null) {
							int i = 0, j = 0, k = 0;
							for (InvestigationMapperAndValue mapperObj : testHistoryList) {
								if(i >9 && j >9 && k>9) {
									break;
								}
								if (mapperObj.getInvestigationvaluescasemapper() != null && mapperObj
										.getInvestigationvaluescasemapper().getinvestigationsTests() != null) {
									if (i<10 && mapperObj.getInvestigationvaluescasemapper().getinvestigationsTests()
											.getTestName().equalsIgnoreCase("ABG")) {
										
										
										
										String time = "";
										if (mapperObj.getInvestigationvaluescasemapper().getDate().getHours() > 9) {
											time = mapperObj.getInvestigationvaluescasemapper().getDate().getHours()
													+ "";
										} else {
											time = "0"
													+ mapperObj.getInvestigationvaluescasemapper().getDate().getHours();
										}
										if (mapperObj.getInvestigationvaluescasemapper().getDate().getMinutes() > 9) {
											time = time + ":" + mapperObj.getInvestigationvaluescasemapper().getDate()
													.getMinutes();
										} else {
											time = time + ":0" + mapperObj.getInvestigationvaluescasemapper().getDate()
													.getMinutes();
										}
										if (mapperObj.getInvestigationSetValueList() != null) {
											if (i == 0) {
												{
													for (InvestigationSetValue testObj : mapperObj
															.getInvestigationSetValueList()) {
														auditIntraOpReport2.setAbg_1_time(time);
														if (testObj.getName().equalsIgnoreCase("FiO2")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_1_fio2(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("pH")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_1_ph(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("pO2")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_1_po2(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("pCO2")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_1_pco2(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("HCO3-")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_1_hco3(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("BE/BD")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_1_be(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("SpO2")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_1_spo2(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("Lac")
																&& testObj.getValue() != null) {
															auditIntraOpReport2
																	.setAbg_1_lactate(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("Hct")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_1_hct(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("Na+")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_1_na(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("K+")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_1_k(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("Ca2+")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_1_ca2(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("Cl-")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_1_cl(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("BB")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_1_bb(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("Others")
																&& testObj.getValue() != null) {
															auditIntraOpReport2
																	.setAbg_1_others(testObj.getValue() + "");
														}
													}
												}
											} else if (i == 1) {
												{
													for (InvestigationSetValue testObj : mapperObj
															.getInvestigationSetValueList()) {
														auditIntraOpReport2.setAbg_2_time(time);
														if (testObj.getName().trim().equalsIgnoreCase("FiO2")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_2_fio2(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("pH")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_2_ph(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("pO2")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_2_po2(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("pCO2")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_2_pco2(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("HCO3-")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_2_hco3(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("BE/BD")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_2_be(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("SpO2")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_2_spo2(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("Lac")
																&& testObj.getValue() != null) {
															auditIntraOpReport2
																	.setAbg_2_lactate(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("Hct")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_2_hct(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("Na+")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_2_na(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("K+")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_2_k(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("Ca2+")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_2_ca2(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("Cl-")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_2_cl(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("BB")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_2_bb(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("Others")
																&& testObj.getValue() != null) {
															auditIntraOpReport2
																	.setAbg_2_others(testObj.getValue() + "");
														}
													}
												}
											} else if (i == 2) {
												{
													for (InvestigationSetValue testObj : mapperObj
															.getInvestigationSetValueList()) {
														auditIntraOpReport2.setAbg_3_time(time);
														if (testObj.getName().trim().equalsIgnoreCase("FiO2")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_3_fio2(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("pH")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_3_ph(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("pO2")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_3_po2(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("pCO2")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_3_pco2(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("HCO3-")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_3_hco3(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("BE/BD")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_3_be(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("SpO2")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_3_spo2(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("Lac")
																&& testObj.getValue() != null) {
															auditIntraOpReport2
																	.setAbg_3_lactate(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("Hct")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_3_hct(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("Na+")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_3_na(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("K+")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_3_k(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("Ca2+")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_3_ca2(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("Cl-")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_3_cl(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("BB")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_3_bb(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("Others")
																&& testObj.getValue() != null) {
															auditIntraOpReport2
																	.setAbg_3_others(testObj.getValue() + "");
														}
													}
												}
											} else if (i == 3) {
												{
													for (InvestigationSetValue testObj : mapperObj
															.getInvestigationSetValueList()) {
														auditIntraOpReport2.setAbg_4_time(time);
														if (testObj.getName().trim().equalsIgnoreCase("FiO2")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_4_fio2(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("pH")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_4_ph(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("pO2")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_4_po2(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("pCO2")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_4_pco2(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("HCO3-")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_4_hco3(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("BE/BD")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_4_be(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("SpO2")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_4_spo2(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("Lac")
																&& testObj.getValue() != null) {
															auditIntraOpReport2
																	.setAbg_4_lactate(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("Hct")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_4_hct(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("Na+")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_4_na(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("K+")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_4_k(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("Ca2+")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_4_ca2(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("Cl-")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_4_cl(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("BB")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_4_bb(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("Others")
																&& testObj.getValue() != null) {
															auditIntraOpReport2
																	.setAbg_4_others(testObj.getValue() + "");
														}
													}
												}
											} else if (i == 4) {
												{
													for (InvestigationSetValue testObj : mapperObj
															.getInvestigationSetValueList()) {
														auditIntraOpReport2.setAbg_5_time(time);
														if (testObj.getName().trim().equalsIgnoreCase("FiO2")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_5_fio2(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("pH")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_5_ph(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("pO2")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_5_po2(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("pCO2")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_5_pco2(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("HCO3-")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_5_hco3(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("BE/BD")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_5_be(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("SpO2")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_5_spo2(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("Lac")
																&& testObj.getValue() != null) {
															auditIntraOpReport2
																	.setAbg_5_lactate(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("Hct")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_5_hct(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("Na+")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_5_na(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("K+")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_5_k(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("Ca2+")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_5_ca2(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("Cl-")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_5_cl(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("BB")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_5_bb(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("Others")
																&& testObj.getValue() != null) {
															auditIntraOpReport2
																	.setAbg_5_others(testObj.getValue() + "");
														}
													}
												}
											}

											else if (i == 5) {
												{
													for (InvestigationSetValue testObj : mapperObj
															.getInvestigationSetValueList()) {
														auditIntraOpReport2.setAbg_6_time(time);
														if (testObj.getName().trim().equalsIgnoreCase("FiO2")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_6_fio2(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("pH")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_6_ph(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("pO2")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_6_po2(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("pCO2")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_6_pco2(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("HCO3-")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_6_hco3(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("BE/BD")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_6_be(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("SpO2")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_6_spo2(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("Lac")
																&& testObj.getValue() != null) {
															auditIntraOpReport2
																	.setAbg_6_lactate(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("Hct")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_6_hct(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("Na+")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_6_na(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("K+")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_6_k(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("Ca2+")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_6_ca2(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("Cl-")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_6_cl(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("BB")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_6_bb(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("Others")
																&& testObj.getValue() != null) {
															auditIntraOpReport2
																	.setAbg_6_others(testObj.getValue() + "");
														}
													}
												}
											} else if (i == 6) {
												{
													for (InvestigationSetValue testObj : mapperObj
															.getInvestigationSetValueList()) {
														auditIntraOpReport2.setAbg_7_time(time);
														if (testObj.getName().trim().equalsIgnoreCase("FiO2")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_7_fio2(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("pH")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_7_ph(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("pO2")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_7_po2(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("pCO2")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_7_pco2(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("HCO3-")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_7_hco3(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("BE/BD")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_7_be(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("SpO2")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_7_spo2(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("Lac")
																&& testObj.getValue() != null) {
															auditIntraOpReport2
																	.setAbg_7_lactate(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("Hct")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_7_hct(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("Na+")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_7_na(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("K+")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_7_k(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("Ca2+")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_7_ca2(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("Cl-")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_7_cl(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("BB")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_7_bb(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("Others")
																&& testObj.getValue() != null) {
															auditIntraOpReport2
																	.setAbg_7_others(testObj.getValue() + "");
														}
													}
												}
											} else if (i == 7) {
												{
													for (InvestigationSetValue testObj : mapperObj
															.getInvestigationSetValueList()) {
														auditIntraOpReport2.setAbg_8_time(time);
														if (testObj.getName().trim().equalsIgnoreCase("FiO2")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_8_fio2(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("pH")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_8_ph(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("pO2")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_8_po2(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("pCO2")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_8_pco2(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("HCO3-")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_8_hco3(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("BE/BD")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_8_be(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("SpO2")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_8_spo2(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("Lac")
																&& testObj.getValue() != null) {
															auditIntraOpReport2
																	.setAbg_8_lactate(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("Hct")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_8_hct(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("Na+")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_8_na(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("K+")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_8_k(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("Ca2+")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_8_ca2(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("Cl-")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_8_cl(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("BB")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_8_bb(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("Others")
																&& testObj.getValue() != null) {
															auditIntraOpReport2
																	.setAbg_8_others(testObj.getValue() + "");
														}
													}
												}
											} else if (i == 8) {
												{
													for (InvestigationSetValue testObj : mapperObj
															.getInvestigationSetValueList()) {
														auditIntraOpReport2.setAbg_9_time(time);
														if (testObj.getName().trim().equalsIgnoreCase("FiO2")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_9_fio2(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("pH")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_9_ph(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("pO2")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_9_po2(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("pCO2")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_9_pco2(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("HCO3-")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_9_hco3(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("BE/BD")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_9_be(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("SpO2")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_9_spo2(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("Lac")
																&& testObj.getValue() != null) {
															auditIntraOpReport2
																	.setAbg_9_lactate(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("Hct")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_9_hct(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("Na+")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_9_na(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("K+")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_9_k(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("Ca2+")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_9_ca2(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("Cl-")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_9_cl(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("BB")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_9_bb(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("Others")
																&& testObj.getValue() != null) {
															auditIntraOpReport2
																	.setAbg_9_others(testObj.getValue() + "");
														}
													}
												}
											} else if (i == 9) {
												{
													for (InvestigationSetValue testObj : mapperObj
															.getInvestigationSetValueList()) {
														auditIntraOpReport2.setAbg_10_time(time);
														if (testObj.getName().trim().equalsIgnoreCase("FiO2")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_10_fio2(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("pH")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_10_ph(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("pO2")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_10_po2(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("pCO2")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_10_pco2(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("HCO3-")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_10_hco3(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("BE/BD")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_10_be(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("SpO2")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_10_spo2(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("Lac")
																&& testObj.getValue() != null) {
															auditIntraOpReport2
																	.setAbg_10_lactate(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("Hct")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_10_hct(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("Na+")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_10_na(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("K+")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_10_k(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("Ca2+")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_10_ca2(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("Cl-")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_10_cl(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("BB")
																&& testObj.getValue() != null) {
															auditIntraOpReport2.setAbg_10_bb(testObj.getValue() + "");
														} else if (testObj.getName().trim().equalsIgnoreCase("Others")
																&& testObj.getValue() != null) {
															auditIntraOpReport2
																	.setAbg_10_others(testObj.getValue() + "");
														}
													}
												}
											}
										}
										i++;
									}

									else if (j <10 && mapperObj.getInvestigationvaluescasemapper().getinvestigationsTests()
											.getTestName().equalsIgnoreCase("sugar")) {
										OtherTestModel sugarTestObj = new OtherTestModel();
										String time = "";
										if (mapperObj.getInvestigationvaluescasemapper().getDate().getHours() > 9) {
											time = mapperObj.getInvestigationvaluescasemapper().getDate().getHours()
													+ "";
										} else {
											time = "0"
													+ mapperObj.getInvestigationvaluescasemapper().getDate().getHours();
										}
										if (mapperObj.getInvestigationvaluescasemapper().getDate().getMinutes() > 9) {
											time = time + ":" + mapperObj.getInvestigationvaluescasemapper().getDate()
													.getMinutes();
										} else {
											time = time + ":0" + mapperObj.getInvestigationvaluescasemapper().getDate()
													.getMinutes();
										}

										if (mapperObj.getInvestigationSetValueList() != null) {
											for (InvestigationSetValue testObj : mapperObj
													.getInvestigationSetValueList()) {
												if (j == 0) {
													auditIntraOpReport2.setSugar_1_time(time);
													auditIntraOpReport2.setSugar_1_value(testObj.getValue());
												} else if (j == 1) {
													auditIntraOpReport2.setSugar_2_time(time);
													auditIntraOpReport2.setSugar_2_value(testObj.getValue());
												} else if (j == 2) {
													auditIntraOpReport2.setSugar_3_time(time);
													auditIntraOpReport2.setSugar_3_value(testObj.getValue());
												} else if (j == 3) {
													auditIntraOpReport2.setSugar_4_time(time);
													auditIntraOpReport2.setSugar_4_value(testObj.getValue());
												} else if (j == 4) {
													auditIntraOpReport2.setSugar_5_time(time);
													auditIntraOpReport2.setSugar_5_value(testObj.getValue());
												} else if (j == 5) {
													auditIntraOpReport2.setSugar_6_time(time);
													auditIntraOpReport2.setSugar_6_value(testObj.getValue());
												} else if (j == 6) {
													auditIntraOpReport2.setSugar_7_time(time);
													auditIntraOpReport2.setSugar_7_value(testObj.getValue());
												} else if (j == 7) {
													auditIntraOpReport2.setSugar_8_time(time);
													auditIntraOpReport2.setSugar_8_value(testObj.getValue());
												} else if (j == 8) {
													auditIntraOpReport2.setSugar_9_time(time);
													auditIntraOpReport2.setSugar_9_value(testObj.getValue());
												} else if (j == 9) {
													auditIntraOpReport2.setSugar_10_time(time);
													auditIntraOpReport2.setSugar_10_value(testObj.getValue());
												}
											}
											j++;
										}
									} else if (k< 10 && mapperObj.getInvestigationvaluescasemapper().getinvestigationsTests()
											.getTestName().equalsIgnoreCase("ACT")) {
										
										String time = "";
										if (mapperObj.getInvestigationvaluescasemapper().getDate().getHours() > 9) {
											time = mapperObj.getInvestigationvaluescasemapper().getDate().getHours()
													+ "";
										} else {
											time = "0"
													+ mapperObj.getInvestigationvaluescasemapper().getDate().getHours();
										}
										if (mapperObj.getInvestigationvaluescasemapper().getDate().getMinutes() > 9) {
											time = time + ":" + mapperObj.getInvestigationvaluescasemapper().getDate()
													.getMinutes();
										} else {
											time = time + ":0" + mapperObj.getInvestigationvaluescasemapper().getDate()
													.getMinutes();
										}
										if (mapperObj.getInvestigationSetValueList() != null) {
											for (InvestigationSetValue testObj : mapperObj
													.getInvestigationSetValueList()) {
												if (k == 0) {
													auditIntraOpReport2.setAct_1_time(time);
													auditIntraOpReport2.setAct_1_value(testObj.getValue());
												} else if (k == 1) {
													auditIntraOpReport2.setAct_2_time(time);
													auditIntraOpReport2.setAct_2_value(testObj.getValue());
												} else if (k == 2) {
													auditIntraOpReport2.setAct_3_time(time);
													auditIntraOpReport2.setAct_3_value(testObj.getValue());
												} else if (k == 3) {
													auditIntraOpReport2.setAct_4_time(time);
													auditIntraOpReport2.setAct_4_value(testObj.getValue());
												} else if (k == 4) {
													auditIntraOpReport2.setAct_5_time(time);
													auditIntraOpReport2.setAct_5_value(testObj.getValue());
												} else if (k == 5) {
													auditIntraOpReport2.setAct_6_time(time);
													auditIntraOpReport2.setAct_6_value(testObj.getValue());
												} else if (k == 6) {
													auditIntraOpReport2.setAct_7_time(time);
													auditIntraOpReport2.setAct_7_value(testObj.getValue());
												} else if (k == 7) {
													auditIntraOpReport2.setAct_8_time(time);
													auditIntraOpReport2.setAct_8_value(testObj.getValue());
												} else if (k == 8) {
													auditIntraOpReport2.setAct_9_time(time);
													auditIntraOpReport2.setAct_9_value(testObj.getValue());
												} else if (k == 9) {
													auditIntraOpReport2.setAct_10_time(time);
													auditIntraOpReport2.setAct_10_value(testObj.getValue());
												}
											}
											k++;

										}

									}
								}
								
								
							}
						}
						Map<String, Object>	patientDetails = DAOFactory.getPatientDetailsReport().getPatientAndCaseDetailsReport(caseId,
								patientID, userID);

						auditIntraOpReport2.setCaseID(caseId);

						if (patientDetails != null) {
							if (patientDetails.entrySet() != null) {
								for (Map.Entry<String, Object> entry : patientDetails.entrySet()) {
									if (entry.getKey().equalsIgnoreCase("FluidsList")) {
										List<Fluidvalue> lstFluid = new ArrayList<>();
										lstFluid = (List<Fluidvalue>) entry.getValue();
										if (lstFluid != null) {
											for (Fluidvalue fluidObject : lstFluid) {
												if (fluidObject.getFluidName().equals("Ringer lactate")) {
													Float fluid_RL = fluidObject.getVolume();
													auditIntraOpReport2.setFluid_RL(fluid_RL);
												} else if (fluidObject.getFluidName().equals("Normal Saline")) {
													Float fluid_NS = fluidObject.getVolume();
													auditIntraOpReport2.setFluid_NS(fluid_NS);
												} else {
													Float fluid_Other = fluidObject.getVolume();
													auditIntraOpReport2.setFluid_Other(fluid_Other);
												}
											}
										}
									}
									if (entry.getKey().equalsIgnoreCase("OutputList")) {
										List<IntraopOutput> lstOutput = new ArrayList<>();
										lstOutput = (List<IntraopOutput>) entry.getValue();
										if (lstOutput != null) {
											for (IntraopOutput outputObject : lstOutput) {
												if (outputObject.getOutputType().equals("Blood")) {
													String output_Blood = outputObject.getOutputType();
													auditIntraOpReport2.setOutput_Blood(output_Blood);
												} else if (outputObject.getOutputType().equals("Urine")) {
													String output_Urine = outputObject.getOutputType();
													auditIntraOpReport2.setOutput_Urine(output_Urine);
												}
											}
										}
									}
								}
							}
						}
						DAOFactory.AuditReport2().createAuditReport(auditIntraOpReport2, userID);
				} catch (Exception e) {
			LOGGER.error("## Exception occured:" + e.getMessage());

		}
	}
}
