/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package controllers;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.cdac.common.GeneratedEntities.IntraopEventlog;
import com.cdac.common.GeneratedEntities.IntraopReportFile;
import com.cdac.common.GeneratedEntities.Patient;
import com.cdac.common.GeneratedEntities.Patientcase;
import com.cdac.common.GeneratedEntities.Recoveryroomreadings;
import com.cdac.framework.FwkServices;
import com.cdac.framework.SystemConfiguration;
import com.jfoenix.controls.JFXToggleButton;

import application.FxmlView;
import application.Main;
import javafx.animation.Animation;
import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.util.Duration;
import mediatorPattern.ControllerMediator;
import model.AldreteScoreSession;
import model.ConfigureDeviceSession;
import model.ConfiguredInjectomartSession;
import model.DashboardTestSession;
import model.DevicesListenerStatusSession;
import model.EditCaseFlagSession;
import model.EditPatientSession;
import model.EventLogSession;
import model.ExecutedEventSession;
import model.FluidSession;
import model.InjectomartSession;
import model.MasterDataSession;
import model.MedicationSession;
import model.OTSession;
import model.OutputSession;
import model.PatientSession;
import model.UserSession;
import services.GetPostOpDetailsService;
import services.GetReportFromDbService;
import sun.misc.BASE64Decoder;
import utility.DrawDashboardSideBar;
import utility.DrawNewGraphs;
import utility.DrawTabbedCenter;
import utility.DrawTabbedCenterForSnapshot;
import utility.GetSnapshot;
import utility.ParamMinMaxRange;
import utility.SendS5PMWaveformData;

/**
 * MainController.java<br>
 * Purpose:This class contains logic to handle functionalities on main dashboard
 * screen
 *
 * @author Rohit_Sharma41
 *
 */
public class MainController implements Initializable {

	@FXML
	private Button eventsBtn;

	@FXML
	private Button AnesthesiaBtn;

	// @FXML
	// private Button fluidsBtn;

	@FXML
	private Button btnSearchPatient;

	@FXML
	private Label lblName;

	@FXML
	private Label lblCrNum;

	@FXML
	private Label lblAge;

	@FXML
	private Label lblGender;

	@FXML
	private Label lblHeight;

	@FXML
	private Label lblWeight;

	@FXML
	private Label lblBMI;

	@FXML
	private Label lblBSA;

	@FXML
	private BorderPane baseBorderPane;

	@FXML
	private Label date;

	@FXML
	private Label time;

	@FXML
	private Button close;

	@FXML
	private VBox leftSideTab;

	@FXML
	private ListView<String> listViewExecuted;

	@FXML
	private ListView<String> listViewToDo;

	@FXML
	private Pane centerPane;
	@FXML
	private Pane sidePane;

	@FXML
	private Label lblOt;

	@FXML
	private Label lblLoggedUser;

	@FXML
	private Button btnLogout;

	@FXML
	private Label lblSelectedEvent;

	// @FXML
	// private Button btnOutput;

	@FXML
	private Label lblHR;

	@FXML
	private Label lblReadingHR;

	@FXML
	private Label lblRR;

	@FXML
	private Label lblReadingRR;

	@FXML
	private Label lblIBP;

	@FXML
	private Label lblReadingIBPSys;

	@FXML
	private Label lblTemp;

	@FXML
	private Label lblReadingTemp;

	@FXML
	private JFXToggleButton toggleButtonPublish;

	@FXML
	private ScrollPane leftScrollPane;

	@FXML
	private VBox headerVbox;

	@FXML
	private HBox ptDetailsHeader;

	@FXML
	private HBox controlsHeader;

	@FXML
	private HBox headerCornerHbox;

	@FXML
	private HBox headerStartHbox;

	@FXML
	private VBox BoxHR;

	@FXML
	private VBox BoxRR;

	@FXML
	private VBox BoxIBP;

	@FXML
	private VBox BoxTemp;

	@FXML
	private Label lblSelectedOT;

	@FXML
	private Button btnExitCase;

	// @FXML
	// private Button btnMedicationHistory;

	@FXML
	private Button btnPostOp;

	@FXML
	private Label lblCaseNo;

	@FXML
	private Button btnReport;

	@FXML
	private Button btnVascularCannulations;

	@FXML
	private AnchorPane disablePaneMainContent;

	@FXML
	private Label lblBloodGroup;

	//changes done by Rohi
    @FXML
    private Button btnIcuPlan;

	public AnchorPane getDisablePaneMainContent() {
		return disablePaneMainContent;
	}

	public void setDisablePaneMainContent(AnchorPane disablePaneMainContent) {
		this.disablePaneMainContent = disablePaneMainContent;
	}

	@FXML
	private Button btnManageDevices;

	@FXML
	private Button btnAddPatient;

	@FXML
	private Button btnEditCase;

	@FXML
	private StackPane pageMainContent;

	@FXML
	private Button btnS5Chart;

	@FXML
	private Button btnAnesthesiaChart;

	@FXML
	private Button btnHistoricalView;

	// @FXML
	// private HBox hbExitBtnHolder;

	// @FXML
	// private Button btnFluidsLog;

	// @FXML
	// private Button btnMedicationLog;

	@FXML
	private Button btnSearchCase;

	@FXML
	private Button helpIcon;

	@FXML
	private Button btnPAC;

	@FXML
	private Label lblCopyright;

	@FXML
	private Button btnCPBDetails;

	@FXML
	private Button btnEchoDetails;

	/*
	 * @FXML private Button btnGraphs;
	 */

	private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
	private SimpleDateFormat sdfNew = new SimpleDateFormat("dd-MM-yyyy HH:mm");
	private ImageView imgMainLogo = new ImageView();

	private final Logger LOG = Logger.getLogger(MainController.class.getName());
	private static MainController mainController;

	private DrawNewGraphs drawNewGraphs;

	public void setDrawNewGraphs(DrawNewGraphs drawNewGraphs) {
		this.drawNewGraphs = drawNewGraphs;
	}

	private DrawTabbedCenter drawTabbedCenter;
	private DrawDashboardSideBar drawSidePane;
	// private SendWaveformData sendWaveformData;

	public DrawDashboardSideBar getDrawSidePane() {
		return drawSidePane;
	}

	/*
	 * private static final String PATIENT_DETAILS_TOPIC = "PatientDetails";
	 * private static final String SCALING_VARS_TOPIC
	 * ="WaveformScalingVariables"; private final double
	 * PTDETAILS_PUBLISH_FREQUENCY = 5000; // In millisecs private static final
	 * double SCALINGVARS_PUBLISH_FREQUENCY = 5000; // In millisecs
	 */
	private boolean publishMode = false;

	private Timeline xValueTrendTimeline, ptDataTimeline, eventsTimeline, ecgTimeline;// ,
																						// scalingFactorTimeline;
	private boolean trendTimelineStarted = false;
	private int timeLblCounter = -1;
	private int timePaneColorCounter = 1;
	private int patientId = -1;
	private boolean patientSessionAvailable = false, caseSessionAvailable = false;

	private Calendar shiftOutTime;

	public Calendar getShiftOutTime() {
		return shiftOutTime;
	}

	private EventHandler<MouseEvent> eventsBtnHandler;
	private EventHandler<MouseEvent> AnesthesiaBtnHandler;
	private EventHandler<MouseEvent> fluidsBtnHandler;
	private EventHandler<MouseEvent> btnSearchPatientHandler;
	private EventHandler<MouseEvent> btnLogoutHandler;
	private EventHandler<MouseEvent> btnExitCaseHandler;
	private EventHandler<MouseEvent> btnMedicationHistoryHandler;
	private EventHandler<MouseEvent> btnPostOpHandler;
	private EventHandler<MouseEvent> btnReportHandler;
	private EventHandler<MouseEvent> btnManageDevicesHandler;
	private EventHandler<MouseEvent> btnVascularCannulationsHandler;
	private EventHandler<MouseEvent> btnAddPatientHandler;
	private EventHandler<MouseEvent> btnEditCaseHandler;
	// private EventHandler<MouseEvent> btnOutputCaseHandler;
	private EventHandler<MouseEvent> btnS5ChartHandler;
	private EventHandler<MouseEvent> btnAnesthesiaChartHandler;
	private EventHandler<MouseEvent> btnHistoricalViewHandler;
	private EventHandler<MouseEvent> btnSearchCaseHandler;
	private EventHandler<MouseEvent> btnHelpHandler;
	private EventHandler<MouseEvent> btnPACHandler;
	private EventHandler<MouseEvent> btnCPBHandler;
	private EventHandler<MouseEvent> btnEchoHandler;
	//changes done by Rohi
	private EventHandler<MouseEvent> btnIcuPlanHandler;
	// By Sudeep for testing(Snapshot)

	private DrawTabbedCenterForSnapshot drawTabbedCenterForSnapshot;

	public DrawTabbedCenterForSnapshot getDrawTabbedCenterForSnapshot() {
		return drawTabbedCenterForSnapshot;
	}

	public void setDrawTabbedCenterForSnapshot(DrawTabbedCenterForSnapshot drawTabbedCenterForSnapshot) {
		this.drawTabbedCenterForSnapshot = drawTabbedCenterForSnapshot;
	}

	private GetSnapshot getSnapshot = new GetSnapshot();

	private ChangeListener<Boolean> publishToggleListener;

	private EventHandler<WorkerStateEvent> getPostOpDeatilsServiceSuccessHandler;
	private EventHandler<WorkerStateEvent> getPostOpDeatilsServiceFailedHandler;

	private EventHandler<WorkerStateEvent> getReportFromDbServiceSuccessHandler;
	private EventHandler<WorkerStateEvent> getReportFromDbServiceFailedHandler;
	private Recoveryroomreadings postOpDetails = new Recoveryroomreadings();;

	public Timeline getxValueTrendTimeline() {
		return xValueTrendTimeline;
	}

	public static MainController getInstance() {
		return mainController;
	}

	public DrawNewGraphs getDrawNewGraphs() {
		return drawNewGraphs;
	}

	public boolean isPublishMode() {
		return publishMode;
	}

	public JFXToggleButton getToggleButtonPublish() {
		return toggleButtonPublish;
	}

	public DrawTabbedCenter getDrawTabbedCenter() {
		return drawTabbedCenter;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			LOG.info("Reached -> " + LOG.getName());

			// centerPane.setBackground(new Background(new
			// BackgroundFill(Color.GRAY, null, null)));
			centerPane.setStyle("-fx-border-color:gray");
			// toggleLiveGraphs.setText("Live\nGraphs");

			// ---Register this controller
			ControllerMediator.getInstance().registerMainController(this);
			// plotAnesthesiaGraph = new PlotAnesthesiaGraph();
			// sendWaveformData = new SendWaveformData();
			// sendWaveformData.createCombinedList();

			// ---set % size (height/width) for all dashboard components
			setComponentsize();

			if (ControllerMediator.getInstance().getEventsController() == null) {
				ControllerMediator.getInstance().registerEventsController(new EventsController());
			}
			// ---start Date & Time update timeline
			Timeline timeandDate = new Timeline(new KeyFrame(Duration.millis(1000), ae -> updateTimeAndDate()));
			timeandDate.setCycleCount(Animation.INDEFINITE);
			if (timeandDate.getStatus() != Status.RUNNING)
				timeandDate.play();

			// ---populate patient info in top banner
			fillDashboardPatientBanner();

			// ---create center TabPane area
			drawTabbedCenter = new DrawTabbedCenter();
			centerPane.getChildren().clear();
			drawTabbedCenter.setCenterPane(centerPane);

			drawSidePane = new DrawDashboardSideBar();
			sidePane.getChildren().clear();

			// drawTabbedCenter.getAnesScalesList().clear();
			drawTabbedCenter.setScaleForAnesParameters(ParamMinMaxRange.PPEAK, 0, ParamMinMaxRange.PPEAK_MAX);
			drawTabbedCenter.setScaleForAnesParameters(ParamMinMaxRange.PMEAN, 0, ParamMinMaxRange.PMEAN_MAX);
			drawTabbedCenter.setScaleForAnesParameters(ParamMinMaxRange.PEEPEXT, 0, ParamMinMaxRange.PEEP_EXT_MAX);
			drawTabbedCenter.setScaleForAnesParameters(ParamMinMaxRange.CIRCUITO2, 0, ParamMinMaxRange.CIRCUITO2_MAX);
			drawTabbedCenter.setScaleForAnesParameters(ParamMinMaxRange.MVEXP, 0, ParamMinMaxRange.MVEXP_MAX);
			drawTabbedCenter.setScaleForAnesParameters(ParamMinMaxRange.TVEXP, 0, ParamMinMaxRange.TVEXP_MAX);
			drawTabbedCenter.setScaleForAnesParameters(ParamMinMaxRange.RR, 0, ParamMinMaxRange.RR_MAX);
			// drawTabbedCenter.addAnesYScales();

			// drawTabbedCenter.getPtMntrScalesList().clear();
			drawTabbedCenter.setScaleForPtMntrParameters(ParamMinMaxRange.HR, 0, ParamMinMaxRange.HR_MAX);
			drawTabbedCenter.setScaleForPtMntrParameters(ParamMinMaxRange.IBP, 0, ParamMinMaxRange.IBP_MAX);
			drawTabbedCenter.setScaleForPtMntrParameters(ParamMinMaxRange.NIBP, 0, ParamMinMaxRange.NIBP_MAX);
			drawTabbedCenter.setScaleForPtMntrParameters(ParamMinMaxRange.SPO2, 0, ParamMinMaxRange.SPO2_MAX);
			drawTabbedCenter.setScaleForPtMntrParameters(ParamMinMaxRange.ETCO2, 0, ParamMinMaxRange.ETCO2_MAX);
			drawTabbedCenter.setScaleForPtMntrParameters(ParamMinMaxRange.TEMP, 0, ParamMinMaxRange.TEMP_MAX);
			drawTabbedCenter.setScaleForPtMntrParameters(ParamMinMaxRange.BIS, 0, ParamMinMaxRange.BIS_MAX);
			drawTabbedCenter.setScaleForPtMntrParameters(ParamMinMaxRange.ETAGENT, 0, ParamMinMaxRange.ETAGENT_MAX);
			drawTabbedCenter.setScaleForPtMntrParameters(ParamMinMaxRange.MAC, 0, ParamMinMaxRange.MAC_MAX);
			// drawTabbedCenter.addPtMntrYScales();

			drawTabbedCenter.setLegendsArray();

			if (PatientSession.getInstance().getPatient() != null
					&& PatientSession.getInstance().getPatientCase() != null) {
				if (PatientSession.getInstance().getPatient().getPatientId() != null
						&& PatientSession.getInstance().getPatientCase().getCaseId() != null) {

					GetPostOpDetailsService getPostOpDeatilsService = GetPostOpDetailsService
							.getInstance(PatientSession.getInstance().getPatientCase().getCaseId());
					getPostOpDeatilsService.restart();

					getPostOpDeatilsServiceSuccessHandler = new EventHandler<WorkerStateEvent>() {

						@Override
						public void handle(WorkerStateEvent event) {

							try {
								postOpDetails = getPostOpDeatilsService.getrecoveryroomreadings();

								if (postOpDetails == null || postOpDetails.getPacuTime() == null) {
									btnAddPatient.setDisable(false);
									btnEditCase.setDisable(false);
									btnManageDevices.setDisable(false);
									btnVascularCannulations.setDisable(false);
									AnesthesiaBtn.setDisable(false);
									btnCPBDetails.setDisable(false);
									btnEchoDetails.setDisable(false);
									eventsBtn.setDisable(false);
									btnPostOp.setDisable(false);
									btnExitCase.setVisible(false);
									btnPostOp.setVisible(true);
									//changes done by Rohi
									btnIcuPlan.setDisable(false);
									//changes End
									drawTabbedCenter.createCenterPane(centerPane);

									// ---Timeline for running a red line
									// indicator over
									// live
									// TrendGraph plot and updating X value
									xValueTrendTimeline = new Timeline(new KeyFrame(
											Duration.seconds(SystemConfiguration.getInstance().getTrendGraphSpeed()),
											ae -> {
												drawTabbedCenter.setxValue(
														drawTabbedCenter.getxValue() + drawTabbedCenter.pixelsPerSec());

												// ---Rubb all graphs
												// simultaneously
												drawTabbedCenter.getAnesGraphBrush().clearRect(
														drawTabbedCenter.getxValue() + 1, 0, 15,
														drawTabbedCenter.getAnesGraphPanel().getPrefHeight());
												drawTabbedCenter.getPtMntrGraphBrush().clearRect(
														drawTabbedCenter.getxValue() + 1, 0, 15,
														drawTabbedCenter.getPtMntrGraphPanel().getPrefHeight());
												/*
												 * drawTabbedCenter.getMedBrush(
												 * ). clearRect(
												 * drawTabbedCenter.getxValue()
												 * + 1, 0, 15,
												 * drawTabbedCenter.getMedCanvas
												 * (). getHeight());
												 */

												// ---if polt reaches graph end
												// width
												if (drawTabbedCenter.getxValue() > new Double(
														drawTabbedCenter.getCalculatedGraphWidth()).intValue()) {
													if (timePaneColorCounter % 2 == 0) {
														drawTabbedCenter.getAnesTimepaneBrush().setStroke(Color.BLACK);
														drawTabbedCenter.getPtMntrTimepaneBrush()
																.setStroke(Color.BLACK);
														// drawTabbedCenter.getMedTimeBrush().setStroke(Color.BLACK);
													} else {
														drawTabbedCenter.getAnesTimepaneBrush().setStroke(Color.RED);
														drawTabbedCenter.getPtMntrTimepaneBrush().setStroke(Color.RED);
														// drawTabbedCenter.getMedTimeBrush().setStroke(Color.RED);
													}
													timePaneColorCounter++;

													// ---Reset last timeLabel
													drawTabbedCenter.getAnesTimepaneBrush().clearRect(0, 0, 30,
															drawTabbedCenter.getAnesTimePane().getPrefHeight());
													drawTabbedCenter.getAnesTimepaneBrush().strokeText(
															drawTabbedCenter.getAnesLastTimeLbl().split(" ")[1], 0,
															drawTabbedCenter.getAnesTimePane().getPrefHeight() * 0.5);

													drawTabbedCenter.getPtMntrTimepaneBrush().clearRect(0, 0, 30,
															drawTabbedCenter.getPtMntrTimePane().getPrefHeight());
													drawTabbedCenter.getPtMntrTimepaneBrush().strokeText(
															drawTabbedCenter.getPtMntrLastTimeLbl().split(" ")[1], 0,
															drawTabbedCenter.getPtMntrTimePane().getPrefHeight() * 0.5);

													/*
													 * drawTabbedCenter.
													 * getMedTimeBrush(
													 * ).clearRect( 0, 0,
													 * 30,drawTabbedCenter.
													 * getMedTimePane().
													 * getPrefHeight());
													 * drawTabbedCenter.
													 * getMedTimeBrush(
													 * ).strokeText
													 * (drawTabbedCenter.
													 * getMedLastTimeLbl().
													 * split(" ")[1], 0,
													 * drawTabbedCenter.
													 * getMedTimePane() .
													 * getPrefHeight()* 0.5);
													 */

													// ---reset x value
													drawTabbedCenter.setxValue(0);
													Main.getInstance().getPlotTrendGraph().setIterations(1);
													Main.getInstance().getPlotTrendGraph().setAnesIterations(1);
													timeLblCounter = 1;

													drawTabbedCenter.getAnesGraphBrush().clearRect(
															drawTabbedCenter.getxValue(), 0, 15,
															drawTabbedCenter.getAnesGraphPanel().getPrefHeight());
													drawTabbedCenter.getPtMntrGraphBrush().clearRect(
															drawTabbedCenter.getxValue(), 0, 15,
															drawTabbedCenter.getPtMntrGraphPanel().getPrefHeight());
													/*
													 * drawTabbedCenter.
													 * getMedBrush(). clearRect(
													 * drawTabbedCenter.
													 * getxValue(), 0, 15,
													 * drawTabbedCenter.
													 * getMedCanvas().
													 * getHeight());
													 */

												} else {
													SimpleDateFormat sdfNew = new SimpleDateFormat("dd-MM-yyyy HH:mm");
													if (timeLblCounter > 0) {
														// ---Change only next
														// time
														// label
														if (drawTabbedCenter
																.getxValue() > ((DrawTabbedCenter.TIME_PIXELS_GAP
																		* timeLblCounter) - 5)) {
															Calendar anesTime = Calendar.getInstance();
															Calendar ptMntrTime = Calendar.getInstance();
															// Calendar medTime
															// =
															// Calendar.getInstance();
															try {
																anesTime.setTime(sdfNew
																		.parse(drawTabbedCenter.getAnesLastTimeLbl()));
																anesTime.add(Calendar.MINUTE,
																		SystemConfiguration.getInstance().getMinsGap());
																drawTabbedCenter.setAnesLastTimeLbl(
																		sdfNew.format(anesTime.getTime()));

																ptMntrTime.setTime(sdfNew.parse(
																		drawTabbedCenter.getPtMntrLastTimeLbl()));
																ptMntrTime.add(Calendar.MINUTE,
																		SystemConfiguration.getInstance().getMinsGap());
																drawTabbedCenter.setPtMntrLastTimeLbl(
																		sdfNew.format(ptMntrTime.getTime()));

																/*
																 * medTime.
																 * setTime(
																 * sdfNew.parse(
																 * drawTabbedCenter.
																 * getMedLastTimeLbl
																 * ())) ;
																 * medTime.add(
																 * Calendar.
																 * MINUTE,
																 * drawTabbedCenter.
																 * getMinsGap())
																 * ;
																 * drawTabbedCenter.
																 * setMedLastTimeLbl(
																 * sdfNew.
																 * format(
																 * medTime.
																 * getTime()));
																 */
															} catch (ParseException e1) {
																// TODO
																// Auto-generated
																// catch block
																e1.printStackTrace();
															}

															drawTabbedCenter.getAnesTimepaneBrush().clearRect(
																	drawTabbedCenter.getTimeLblCoordinatesList()
																			.get(timeLblCounter),
																	0, 30,
																	drawTabbedCenter.getAnesTimePane().getPrefHeight());
															drawTabbedCenter.getAnesTimepaneBrush().strokeText(
																	drawTabbedCenter.getAnesLastTimeLbl().split(" ")[1],
																	drawTabbedCenter.getTimeLblCoordinatesList()
																			.get(timeLblCounter),
																	drawTabbedCenter.getAnesTimePane().getPrefHeight()
																			* 0.5);

															drawTabbedCenter.getPtMntrTimepaneBrush().clearRect(
																	drawTabbedCenter.getTimeLblCoordinatesList()
																			.get(timeLblCounter),
																	0, 30, drawTabbedCenter.getPtMntrTimePane()
																			.getPrefHeight());
															drawTabbedCenter.getPtMntrTimepaneBrush().strokeText(
																	drawTabbedCenter.getPtMntrLastTimeLbl()
																			.split(" ")[1],
																	drawTabbedCenter.getTimeLblCoordinatesList()
																			.get(timeLblCounter),
																	drawTabbedCenter.getPtMntrTimePane().getPrefHeight()
																			* 0.5);

															/*
															 * drawTabbedCenter.
															 * getMedTimeBrush()
															 * . clearRect(
															 * drawTabbedCenter.
															 * getTimeLblCoordinatesList
															 * ().get(
															 * timeLblCounter),
															 * 0, 30,
															 * drawTabbedCenter.
															 * getMedTimePane().
															 * getPrefHeight());
															 * drawTabbedCenter.
															 * getMedTimeBrush()
															 * . strokeText(
															 * drawTabbedCenter.
															 * getMedLastTimeLbl
															 * ().
															 * split(" ")[1],
															 * drawTabbedCenter.
															 * getTimeLblCoordinatesList
															 * ().get(
															 * timeLblCounter),
															 * drawTabbedCenter.
															 * getMedTimePane().
															 * getPrefHeight() *
															 * 0.5);
															 */

															timeLblCounter++;
														}
													}

													// ---Continuously run a red
													// line as
													// current
													// index indicator
													drawTabbedCenter.getAnesGraphBrush().setStroke(Color.RED);
													drawTabbedCenter.getPtMntrGraphBrush().setStroke(Color.RED);
													// drawTabbedCenter.getMedBrush().setStroke(Color.RED);

													drawTabbedCenter.getAnesGraphBrush().strokeLine(
															drawTabbedCenter.getxValue(), 0,
															drawTabbedCenter.getxValue(),
															drawTabbedCenter.getAnesGraphPanel().getPrefHeight());
													drawTabbedCenter.getAnesGraphBrush().clearRect(
															drawTabbedCenter.getxValue()
																	- drawTabbedCenter.pixelsPerSec() - 1,
															0, 1.5,
															drawTabbedCenter.getAnesGraphPanel().getPrefHeight());

													drawTabbedCenter.getPtMntrGraphBrush().strokeLine(
															drawTabbedCenter.getxValue(), 0,
															drawTabbedCenter.getxValue(),
															drawTabbedCenter.getPtMntrGraphPanel().getPrefHeight());
													drawTabbedCenter.getPtMntrGraphBrush().clearRect(
															drawTabbedCenter.getxValue()
																	- drawTabbedCenter.pixelsPerSec() - 1,
															0, 1.5,
															drawTabbedCenter.getPtMntrGraphPanel().getPrefHeight());

													/*
													 * drawTabbedCenter.
													 * getMedBrush().
													 * strokeLine(
													 * drawTabbedCenter.
													 * getxValue(), 0,
													 * drawTabbedCenter.
													 * getxValue(),
													 * drawTabbedCenter.
													 * getMedCanvas().
													 * getHeight());
													 * drawTabbedCenter.
													 * getMedBrush(). clearRect(
													 * drawTabbedCenter.
													 * getxValue() -
													 * drawTabbedCenter.
													 * pixelsPerSec(), 0, 1,
													 * drawTabbedCenter.
													 * getMedCanvas().
													 * getHeight());
													 */
													if (Main.getInstance().getPlotTrendGraph() != null) {
														Main.getInstance().getPlotTrendGraph().drawLiveGraph();
														Main.getInstance().getPlotTrendGraph().drawAnesGraph();
													}

												}
											}));
									xValueTrendTimeline.setCycleCount(Animation.INDEFINITE);
									/*
									 * if (ControllerMediator.getInstance().
									 * getMainController().
									 * getxValueTrendTimeline().getStatus() !=
									 * Status.RUNNING)
									 * ControllerMediator.getInstance().
									 * getMainController().
									 * initializeXValTrendTimeline();
									 */

								} else {
									shiftOutTime = Calendar.getInstance();
									shiftOutTime.setTime(postOpDetails.getPacuTime());

									btnAddPatient.setDisable(true);
									btnEditCase.setDisable(true);
									btnManageDevices.setDisable(true);
									btnVascularCannulations.setDisable(true);
									AnesthesiaBtn.setDisable(true);
									btnCPBDetails.setDisable(true);
									btnEchoDetails.setDisable(true);
									//changes done by ROhi
									btnIcuPlan.setDisable(true);
									//changes end
									eventsBtn.setDisable(true);
									btnPostOp.setDisable(true);
									// btnFluidsLog.setDisable(true);
									btnExitCase.setVisible(true);
									toggleButtonPublish.setDisable(true);

									// btnMedicationLog.setDisable(true);
									// btnReport.setDisable(false);
									// HBox hbox=new HBox();
									// hbox.setPadding(new Insets(10));
									// Label lbl = new Label("This case is
									// closed!
									// Please click RECORDED DATA button to view
									// historical data.");
									// lbl.setFont(new Font("Arial",30));
									// hbox.getChildren().add(lbl);
									// centerPane.getChildren().add(hbox);
									//

									centerPane.getChildren().clear();
									double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
									double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();
									VBox vBPageBlock = new VBox();
									Image imageLogo = new Image(this.getClass().getResourceAsStream("/res/logo.png"));
									vBPageBlock.setStyle("-fx-background-color:#505050");
									vBPageBlock.setOpacity(0.5);
									imgMainLogo.setImage(imageLogo);
									imgMainLogo.prefHeight(screenHeight * 0.4);
									imgMainLogo.prefWidth(screenWidth * 0.5);
									vBPageBlock.getChildren().add(imgMainLogo);
									vBPageBlock.setAlignment(Pos.CENTER);
									vBPageBlock.setSpacing(10);
									Label lbl = new Label(
											"This case is closed! Please click RECORDED DATA button to view historical data.");
									lbl.setStyle("-fx-font-size:29 px");
									lbl.setStyle("-fx-font-weight:bold");
									lbl.setStyle("-fx-text-fill: #ffffff");
									vBPageBlock.getChildren().add(lbl);
									vBPageBlock.setPrefSize(screenWidth, screenHeight * 0.86);

									pageMainContent.getChildren().add(vBPageBlock);

								}

								getPostOpDeatilsService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
										getPostOpDeatilsServiceSuccessHandler);
								getPostOpDeatilsService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
										getPostOpDeatilsServiceFailedHandler);
							} catch (Exception e) {
								LOG.error("## Exception occured:", e);
							}
						}
					};
					getPostOpDeatilsService.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
							getPostOpDeatilsServiceSuccessHandler);

					getPostOpDeatilsServiceFailedHandler = new EventHandler<WorkerStateEvent>() {
						@Override
						public void handle(WorkerStateEvent event) {
							try {
								Main.getInstance().getUtility().showNotification("Error", "Error",
										getPostOpDeatilsService.getException().getMessage());
								getPostOpDeatilsService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
										getPostOpDeatilsServiceSuccessHandler);
								getPostOpDeatilsService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
										getPostOpDeatilsServiceFailedHandler);
							} catch (Exception e) {
								LOG.error("## Exception occured:", e);
							}
						}
					};
					getPostOpDeatilsService.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
							getPostOpDeatilsServiceFailedHandler);

				}

			}

			SendS5PMWaveformData sendS5PMWaveformData = new SendS5PMWaveformData();
			ecgTimeline = new Timeline(new KeyFrame(Duration.millis(1000), ae -> {
				String combinedData = "";
				for (Integer i : sendS5PMWaveformData.ecgArr) {
					combinedData = combinedData + i + " ";
				}
				combinedData = combinedData + "|";
				for (Integer i : sendS5PMWaveformData.spo2Arr) {
					combinedData = combinedData + i + " ";
				}
				combinedData = combinedData + "|";
				for (Integer i : sendS5PMWaveformData.ibpArr) {
					combinedData = combinedData + i + " ";
				}

				if (ControllerMediator.getInstance().getMainController().isPublishMode()) {
					// Main.getInstance().getKafkaProducer().setSelectedOT("Main_OT___URO___II");
					
					Main.getInstance().getKafkaProducer().setTopic("S5PTMonitorWaveform");
					Main.getInstance().getKafkaProducer().setProducedData(combinedData);
					Main.getInstance().getKafkaProducer().callProducer();
					
				}
			}));
			ecgTimeline.setCycleCount(Animation.INDEFINITE);

			// ---set logged in user session
			if (UserSession.getInstance().getUserWithrRolesForUserAuthentication() != null) {
				lblLoggedUser.setText(UserSession.getInstance().getUserWithrRolesForUserAuthentication().getUserName());
			}

			if (OTSession.getInstance().getSelectedOT() != null) {
				lblSelectedOT.setText(OTSession.getInstance().getSelectedOT());
			}

			// ---Check if patient and patientCase sessions are not null and
			// make
			// ---different controls visible

			try {
				if (PatientSession.getInstance().getPatient().getPatientId() != null)
					patientSessionAvailable = true;
			} catch (Exception e) {
				patientSessionAvailable = false;
			}

			try {
				if (PatientSession.getInstance().getPatientCase().getCaseId() != null)
					caseSessionAvailable = true;
			} catch (Exception e) {
				caseSessionAvailable = false;
			}

			if (DashboardTestSession.getInstance() != null) {
				if (DashboardTestSession.getInstance().getTestList() == null
						|| DashboardTestSession.getInstance().getTestList().size() == 0) {
					List<String> testListAdded = new ArrayList<String>();
					testListAdded.add("ABG");
					testListAdded.add("Sugar");
					DashboardTestSession.getInstance().setTestList(testListAdded);
				}
			}

			if (patientSessionAvailable && caseSessionAvailable) {
				if (ControllerMediator.getInstance().getMainController().isPublishMode()) {
					sendPatientInfoToKafka();
				}
				// sendScalingInfoToKafka();
				// fetchMasterData();
				// medicationBtn.setDisable(false);
				AnesthesiaBtn.setDisable(false);
				btnCPBDetails.setDisable(false);
				btnEchoDetails.setDisable(false);
				//changes done by Rohi
				btnIcuPlan.setDisable(false);
				//changes end
				eventsBtn.setDisable(false);
				// fluidsBtn.setDisable(false);
				// btnOutput.setDisable(false);
				// btnTest.setDisable(false);
				// btnMedicationHistory.setDisable(false);
				controlsHeader.getChildren().remove(0);
				controlsHeader.getChildren().remove(0);
				// Edit patient button removed
				controlsHeader.getChildren().remove(0);
				btnExitCase.setVisible(true);
				btnPostOp.setDisable(false);
				btnManageDevices.setDisable(false);
				btnVascularCannulations.setDisable(false);
				btnReport.setDisable(false);
				btnEditCase.setDisable(false);
				btnS5Chart.setDisable(false);
				btnAnesthesiaChart.setDisable(false);
				btnHistoricalView.setDisable(false);
				btnAddPatient.setText("Edit Patient");
				toggleButtonPublish.setDisable(false);
				// btnFluidsLog.setDisable(false);
				// btnMedicationLog.setDisable(false);
				loadSideBarEvents();
				pageMainContent.getChildren().remove(1);

				/*
				 *
				 * #############################################################
				 * ####
				 *
				 *
				 * #############################################################
				 * #### By Sudeep For testing getSnapshot
				 */
				drawTabbedCenterForSnapshot = new DrawTabbedCenterForSnapshot();
				drawTabbedCenterForSnapshot.createCenterPane(centerPane);

				drawTabbedCenterForSnapshot.setScaleForAnesParameters(ParamMinMaxRange.PPEAK, 0,
						ParamMinMaxRange.PPEAK_MAX);
				drawTabbedCenterForSnapshot.setScaleForAnesParameters(ParamMinMaxRange.PMEAN, 0,
						ParamMinMaxRange.PMEAN_MAX);
				drawTabbedCenterForSnapshot.setScaleForAnesParameters(ParamMinMaxRange.PEEPEXT, 0,
						ParamMinMaxRange.PEEP_EXT_MAX);
				drawTabbedCenterForSnapshot.setScaleForAnesParameters(ParamMinMaxRange.CIRCUITO2, 0,
						ParamMinMaxRange.CIRCUITO2_MAX);
				drawTabbedCenterForSnapshot.setScaleForAnesParameters(ParamMinMaxRange.MVEXP, 0,
						ParamMinMaxRange.MVEXP_MAX);
				drawTabbedCenterForSnapshot.setScaleForAnesParameters(ParamMinMaxRange.TVEXP, 0,
						ParamMinMaxRange.TVEXP_MAX);
				drawTabbedCenterForSnapshot.setScaleForAnesParameters(ParamMinMaxRange.RR, 0, ParamMinMaxRange.RR_MAX);
				drawTabbedCenterForSnapshot.setScaleForPtMntrParameters(ParamMinMaxRange.HR, 0,
						ParamMinMaxRange.HR_MAX);
				drawTabbedCenterForSnapshot.setScaleForPtMntrParameters(ParamMinMaxRange.IBP, 0,
						ParamMinMaxRange.IBP_MAX);
				drawTabbedCenterForSnapshot.setScaleForPtMntrParameters(ParamMinMaxRange.SPO2, 0,
						ParamMinMaxRange.SPO2_MAX);
				drawTabbedCenterForSnapshot.setScaleForPtMntrParameters(ParamMinMaxRange.ETCO2, 0,
						ParamMinMaxRange.ETCO2_MAX);
				drawTabbedCenterForSnapshot.setScaleForPtMntrParameters(ParamMinMaxRange.TEMP, 0,
						ParamMinMaxRange.TEMP_MAX);
				drawTabbedCenterForSnapshot.setScaleForPtMntrParameters(ParamMinMaxRange.MAC, 0,
						ParamMinMaxRange.MAC_MAX);
				drawTabbedCenterForSnapshot.setScaleForPtMntrParameters(ParamMinMaxRange.ETAGENT, 0,
						ParamMinMaxRange.ETAGENT_MAX);
				drawTabbedCenterForSnapshot.setScaleForPtMntrParameters(ParamMinMaxRange.BIS, 0,
						ParamMinMaxRange.BIS_MAX);
				drawTabbedCenterForSnapshot.setScaleForPtMntrParameters(ParamMinMaxRange.IBPMEAN, 0,
						ParamMinMaxRange.IBP_MAX);
				drawTabbedCenterForSnapshot.setLegendsArray();

				/*
				 *
				 * #############################################################
				 * ####
				 *
				 * #############################################################
				 * #### By Sudeep For testing getSnapshot
				 */

			}
			btnSearchPatientHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent event) {

					try {
						Main.getInstance().setLastFxmlScreen("Main");
						Main.getInstance().getStageManager().switchSceneAfterMain(FxmlView.SEARCH_PATIENT);
						// bindPopupFxml("/View/searchPatient.fxml");
					} catch (Exception e) {
						LOG.error("## Exception occured:", e);
					}

				}
			};
			btnSearchPatient.addEventHandler(MouseEvent.MOUSE_CLICKED, btnSearchPatientHandler);

			AnesthesiaBtnHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent event) {
					try {
						// bindPopupFxml("/View/AnaethesiaMultiTab.fxml");
						Main.getInstance().getStageManager().switchSceneAfterMain(FxmlView.ANESTHESIA_DETAILS);
					} catch (Exception e) {
						LOG.error("## Exception occured:", e);
					}
				}
			};
			AnesthesiaBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, AnesthesiaBtnHandler);

			eventsBtnHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent event) {
					try {
						// bindPopupFxml("/View/Events.fxml");
						Main.getInstance().getStageManager().switchSceneAfterMain(FxmlView.EVENTS_LOG);
					} catch (Exception e) {
						LOG.error("## Exception occured:", e);
					}
				}
			};
			eventsBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, eventsBtnHandler);

			// fluidsBtnHandler = new
			// EventHandler<javafx.scene.input.MouseEvent>() {
			// public void handle(javafx.scene.input.MouseEvent event) {
			// try {
			// Main.getInstance().getStageManager().switchSceneAfterMain(FxmlView.FLUIDS);
			// } catch (Exception e) {
			// LOG.error("## Exception occured:", e);
			// }
			// }
			// };
			// btnFluidsLog.addEventHandler(MouseEvent.MOUSE_CLICKED,
			// fluidsBtnHandler);

			// btnMedicationHistoryHandler = new
			// EventHandler<javafx.scene.input.MouseEvent>() {
			// public void handle(javafx.scene.input.MouseEvent event) {
			// Main.getInstance().getStageManager().switchSceneAfterMain(FxmlView.MEDICATION_HISTORY);
			// }
			// };
			// btnMedicationLog.addEventHandler(MouseEvent.MOUSE_CLICKED,
			// btnMedicationHistoryHandler);

			btnSearchCaseHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent event) {
					try {
						Main.getInstance().setLastFxmlScreen("Main");
						Main.getInstance().getStageManager().switchSceneAfterMain(FxmlView.SEARCH_CASE);
					} catch (Exception e) {
						LOG.error("## Exception occured:", e);
					}
				}
			};
			btnSearchCase.addEventHandler(MouseEvent.MOUSE_CLICKED, btnSearchCaseHandler);

			btnHelpHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent event) {

					String path = "";
					String decodedPath = "";
					try {
						path = SystemConfiguration.class.getProtectionDomain().getCodeSource().getLocation().toURI()
								.getPath();
						try {
							decodedPath = URLDecoder.decode(path, "UTF-8");
						} catch (UnsupportedEncodingException e) {

							LOG.error("## Exception occured:", e);
						}

						decodedPath = (new File(decodedPath)).getParentFile().getPath() + File.separator + "jasper"
								+ File.separator + "Quick_Start_Guide.pdf";
						File myFile = new File(decodedPath);
						try {
							Desktop.getDesktop().open(myFile);
						} catch (IOException e) {

							LOG.error("## Exception occured:", e);
						}
					} catch (Exception e) {

						LOG.error("## Exception occured:", e);
					}
				}
			};
			helpIcon.addEventHandler(MouseEvent.MOUSE_CLICKED, btnHelpHandler);

			btnPACHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent event) {
					try {
						String url = SystemConfiguration.getInstance().getPacUrl();

						Desktop.getDesktop().browse(new URI(url));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						LOG.error("## Exception occured:", e);
					} catch (URISyntaxException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						LOG.error("## Exception occured:", e);
					}

				}
			};
			btnPAC.addEventHandler(MouseEvent.MOUSE_CLICKED, btnPACHandler);

			btnCPBHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent event) {
					try {

						Main.getInstance().getStageManager().switchSceneAfterMain(FxmlView.CPB_DETAILS);
					} catch (Exception e) {
						LOG.error("## Exception occured:", e);
					}
				}
			};
			btnCPBDetails.addEventHandler(MouseEvent.MOUSE_CLICKED, btnCPBHandler);

			btnEchoHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent event) {
					try {

						Main.getInstance().getStageManager().switchSceneAfterMain(FxmlView.ECHO_DETAILS);
					} catch (Exception e) {
						LOG.error("## Exception occured:", e);
					}
				}
			};
			btnEchoDetails.addEventHandler(MouseEvent.MOUSE_CLICKED, btnEchoHandler);


			//changes done By Rohi
			btnIcuPlanHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent event) {
					try {

						Main.getInstance().getStageManager().switchSceneAfterMain(FxmlView.ICU_PLAN_DETAILS);
					} catch (Exception e) {
						LOG.error("## Exception occured:", e);
					}
				}
			};
			btnIcuPlan.addEventHandler(MouseEvent.MOUSE_CLICKED, btnIcuPlanHandler);
			//changes end

			btnPostOpHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent event) {

					try {
						boolean isTimeIn = false;
						if (EventLogSession.getInstance().getEventTime() != null) {
							isTimeIn = true;
						}
						if (isTimeIn) {

							boolean dialogueResult = Main.getInstance().getUtility()
									.confirmDialogue("Do you really want to proceed with closure of the case ?");
							if (dialogueResult)
								Main.getInstance().getStageManager().switchSceneAfterMain(FxmlView.SHIFT_OUT);
						} else {
							Main.getInstance().getUtility().warningDialogue("Information",
									"Please mark Patient Time In before Shift Out.");
						}
					} catch (Exception e) {
						LOG.error("## Exception occured:", e);
					}
				}
			};
			btnPostOp.addEventHandler(MouseEvent.MOUSE_CLICKED, btnPostOpHandler);

			btnManageDevicesHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent event) {
					try {
						// bindPopupFxml("/View/ManageAvailableDevice.fxml");
						Main.getInstance().getStageManager().switchSceneAfterMain(FxmlView.MANAGE_DEVICES);
					} catch (Exception e) {
						LOG.error("## Exception occured:", e);
					}
				}
			};
			btnManageDevices.addEventHandler(MouseEvent.MOUSE_CLICKED, btnManageDevicesHandler);

			btnReportHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent event) {
					try {
						if (postOpDetails == null || postOpDetails.getPacuTime() == null) {
							Main.getInstance().getUtility().warningDialogue("Information",
									"Report can only be generated after Shift Out.");
						} else {
							disablePaneMainContent.setVisible(true);
							String mode = "";
							mode = SystemConfiguration.getInstance().getLocationReportMode();
							if (!mode.equals("") && mode.trim().equalsIgnoreCase("1")) {
								GetReportFromDbService getReportFromDbService = GetReportFromDbService
										.getInstance(PatientSession.getInstance().getPatientCase().getCaseId());
								getReportFromDbService.restart();

								getReportFromDbServiceSuccessHandler = new EventHandler<WorkerStateEvent>() {

									@Override
									public void handle(WorkerStateEvent event) {

										try {
											IntraopReportFile reportData = new IntraopReportFile();
											reportData = getReportFromDbService.getReportData();
											if (reportData != null && reportData.getReportID() != null
													&& reportData.getReportID() != 0) {
												String str = new String(reportData.getPatientReportData().getBytes(1l,
														(int) reportData.getPatientReportData().length()));
												System.out.println("Report Data###########################" + str);
												String path = "";
												String decodedPath = "";
												try {
													path = SystemConfiguration.class.getProtectionDomain()
															.getCodeSource().getLocation().toURI().getPath();
												} catch (URISyntaxException e) {

												}
												try {
													decodedPath = URLDecoder.decode(path, "UTF-8");
												} catch (UnsupportedEncodingException e) {

												}
												decodedPath = (new File(decodedPath)).getParentFile().getPath()
														+ File.separator + "jasper" + File.separator
														+ "IntraOpPatientReport.jrxml";
												if (decodedPath.contains("file:/")) {
													decodedPath = decodedPath.replaceAll("file:/", "");
												}
												path = decodedPath
														.replaceAll("IntraOpPatientReport.jrxml", "patientReport.pdf")
														.toString();

												BASE64Decoder decoder = new BASE64Decoder();
												byte[] decodedBytes = decoder.decodeBuffer(str);

												File file = new File(path);
												FileOutputStream fop = new FileOutputStream(file);

												fop.write(decodedBytes);
												fop.flush();
												fop.close();
												Desktop.getDesktop().open(file);
												disablePaneMainContent.setVisible(false);
											} else {
												disablePaneMainContent.setVisible(false);
												Main.getInstance().getStageManager()
														.switchSceneAfterMain(FxmlView.GET_SNAPSHOT);
											}

										} catch (Exception e) {
											disablePaneMainContent.setVisible(false);
											if(e.getMessage().contains("The process cannot access the file because it is being used by another process")){

												Main.getInstance().getUtility().warningDialogue("Information", "Patient Report is already open.Please close it and Generate Report agian.");

												}

										}
										getReportFromDbService.removeEventHandler(
												WorkerStateEvent.WORKER_STATE_SUCCEEDED,
												getReportFromDbServiceSuccessHandler);
										getReportFromDbService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
												getReportFromDbServiceFailedHandler);
									}

								};

								getReportFromDbService.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
										getReportFromDbServiceSuccessHandler);

								getReportFromDbServiceFailedHandler = new EventHandler<WorkerStateEvent>() {

									@Override
									public void handle(WorkerStateEvent event) {
										try {
											Main.getInstance().getUtility().showNotification("Error", "Error",
													getReportFromDbService.getException().getMessage());
											getReportFromDbService.removeEventHandler(
													WorkerStateEvent.WORKER_STATE_SUCCEEDED,
													getReportFromDbServiceSuccessHandler);
											getReportFromDbService.removeEventHandler(
													WorkerStateEvent.WORKER_STATE_FAILED,
													getReportFromDbServiceFailedHandler);

										} catch (Exception e) {
											LOG.error("## Exception occured:", e);
										}
									}

								};

								getReportFromDbService.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
										getReportFromDbServiceFailedHandler);
							} else {
								Patient patient = null;
								String pdfFileName = "";
								if (PatientSession.getInstance().getPatient() != null) {
									patient = PatientSession.getInstance().getPatient();
								}
								if (patient != null) {
									pdfFileName = PatientSession.getInstance().getPatientCase().getCaseId() + "_"
											+ patient.getFirstName() + "_" + patient.getCrnumber();
								}
								File directory = new File(
										SystemConfiguration.getInstance().getLocationForPatientReport());
								String path = directory + "\\" + pdfFileName + ".pdf";

								File myFile = new File(path);
								if (myFile.exists()) {
									Desktop.getDesktop().open(myFile);
								} else {
									Main.getInstance().getStageManager().switchSceneAfterMain(FxmlView.GET_SNAPSHOT);
								}
							}

						}

					} catch (Exception e) {
						LOG.error("## Exception occured:", e);
					}

				}
			};
			btnReport.addEventHandler(MouseEvent.MOUSE_CLICKED, btnReportHandler);

			btnVascularCannulationsHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent event) {
					try {
						// bindPopupFxml("/View/VascularCannulations.fxml");
						Main.getInstance().getStageManager().switchSceneAfterMain(FxmlView.VASCULAR_CANNULATIONS);
					} catch (Exception e) {
						LOG.error("## Exception occured:", e);
					}
				}
			};
			btnVascularCannulations.addEventHandler(MouseEvent.MOUSE_CLICKED, btnVascularCannulationsHandler);

			btnAddPatientHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent event) {
					try {
						if (patientSessionAvailable && caseSessionAvailable) {
							if (PatientSession.getInstance().getPatient() != null) {

								if (PatientSession.getInstance().getPatient().getPatientId() != null) {
									patientId = PatientSession.getInstance().getPatient().getPatientId();
									EditPatientSession.getInstance().setIsEdit(true);
									EditPatientSession.getInstance().setIsFromDashboard(true);
									EditPatientSession.getInstance().setPatientId(patientId);
								}

							}
						} else {
							PatientSession.getInstance().setPatient(null);
							PatientSession.getInstance().setPatientCase(null);
						}
						// bindPopupFxml("/View/createPatient.fxml");
						Main.getInstance().getStageManager().switchSceneAfterMain(FxmlView.CREATE_PATIENT);
					} catch (Exception e) {
						LOG.error("## Exception occured:", e);
					}
				}
			};
			btnAddPatient.addEventHandler(MouseEvent.MOUSE_CLICKED, btnAddPatientHandler);

			btnEditCaseHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent event) {
					try {
						if (PatientSession.getInstance().getPatientCase() != null) {
							EditCaseFlagSession.getInstance()
									.setCaseId(PatientSession.getInstance().getPatientCase().getCaseId());

						}
						EditCaseFlagSession.getInstance().setIsEdit(true);
						EditCaseFlagSession.getInstance().setIsEditFromDashboard(true);
						// bindPopupFxml("/View/createCase.fxml");
						Main.getInstance().getStageManager().switchSceneAfterMain(FxmlView.CREATE_CASE);
					} catch (Exception e) {
						LOG.error("## Exception occured:", e);
					}
				}
			};
			btnEditCase.addEventHandler(MouseEvent.MOUSE_CLICKED, btnEditCaseHandler);

			btnS5ChartHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent event) {
					try {
						drawTabbedCenter.setCurrentTab("Patient Monitor");
						// bindPopupFxml("/View/HistoricalData.fxml");
						Main.getInstance().getStageManager().switchSceneAfterMain(FxmlView.HISTORICAL_DATA);
					} catch (Exception e) {
						LOG.error("## Exception occured:", e);
					}
				}
			};
			btnS5Chart.addEventHandler(MouseEvent.MOUSE_CLICKED, btnS5ChartHandler);

			btnAnesthesiaChartHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent event) {
					try {
						drawTabbedCenter.setCurrentTab("Anesthesia");
						// bindPopupFxml("/View/HistoricalData.fxml");
						Main.getInstance().getStageManager().switchSceneAfterMain(FxmlView.HISTORICAL_DATA);
					} catch (Exception e) {
						LOG.error("## Exception occured:", e);
					}
				}
			};
			btnAnesthesiaChart.addEventHandler(MouseEvent.MOUSE_CLICKED, btnAnesthesiaChartHandler);

			btnHistoricalViewHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent event) {
					// bindPopupFxml("/View/GridsHistoricalView.fxml");

					try {
						if (EventLogSession.getInstance().getEventTime() == null)
							Main.getInstance().getUtility().warningDialogue("Information",
									"Time in not recorded for this patient! Please mark Time in event.");
						else
							Main.getInstance().getStageManager().switchSceneAfterMain(FxmlView.GRID_HISTORICAL);
					} catch (Exception e) {
						LOG.error("## Exception occured:", e);
					}
				}
			};
			btnHistoricalView.addEventHandler(MouseEvent.MOUSE_CLICKED, btnHistoricalViewHandler);

			/*
			 * btnGraphs.setOnAction(e -> {
			 * Main.getInstance().getStageManager().switchSceneAfterMain(
			 * FxmlView.DUMMY_GRAPHS); });
			 */

			btnLogoutHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent event) {
					try {

						boolean status = Main.getInstance().getUtility()
								.confirmDialogue("Are you sure, you want to logout ?");
						if (status) {

							stopRunningTimelines();

							toggleButtonPublish.setDisable(true);
							// ---Hide dashboard screen
							Main.getInstance().getParentStage().hide();

							// ---logged in user object set to null
							UserSession.getInstance().setUserWithrRolesForUserAuthentication(null);

							// ---nullify patient and case objects
							PatientSession.getInstance().setPatient(null);
							PatientSession.getInstance().setPatientCase(null);

							// ---nullify pmaster data session

							MasterDataSession.getInstance().getFavFluidDataMap().clear();
							MasterDataSession.getInstance().getFavMedDataMap().clear();
							MasterDataSession.getInstance().getMasterDataMap().clear();
							MasterDataSession.getInstance().getTestParamDataMap().clear();

							AldreteScoreSession.getInstance().setPACUparams(null);
							AldreteScoreSession.getInstance().setSelectedType(null);
							AldreteScoreSession.getInstance().setShiftingOutParams(null);

							ConfigureDeviceSession.getInstance().setDevice(null);

							ConfiguredInjectomartSession.getInstance().setAssignedDeviceList(null);
							ConfiguredInjectomartSession.getInstance().setConfiguredDeviceList(null);
							ConfiguredInjectomartSession.getInstance().setDetectedDeviceList(null);

							DashboardTestSession.getInstance().setTestList(null);

							EventLogSession.getInstance().setEventLog(null);
							EventLogSession.getInstance().setEventTime(null);

							FluidSession.getInstance().setFluidValue(null);
							InjectomartSession.getInstance().setConfiguredInfusionPumpList(null);

							MedicationSession.getInstance().setIntraopMedicationlog(null);
							OTSession.getInstance().setSelectedOT(null);
							OutputSession.getInstance().setOutput(null);

							UserSession.getInstance().setUserWithrRolesForUserAuthentication(null);

							// ConfigureDevice.isPtMonitorSaveClicked = false;
							// if
							// (ControllerMediator.getInstance().getConfigureDevice()
							// != null)
							// ControllerMediator.getInstance().getConfigureDevice().setPtMonitorSaveClicked(false);

							Main.getInstance().getPlotGraphs().setGraphRunning(false);
							Main.getInstance().getPlotGraphs().setFirstRun(false);

							// ---Stop Graph plotting transition
							if (Main.getInstance().getPlotGraphs().getGraphTimeline() != null) {
								if (Main.getInstance().getPlotGraphs().getGraphTimeline()
										.getStatus() == Status.RUNNING) {
									Main.getInstance().getPlotGraphs().getGraphTimeline().stop();
								}
							}

							if (FwkServices.getInstance().getAutomaticDeviceIdentificationServiceStatus() == true)
								FwkServices.getInstance().stopIdentifyingDevicesAutomatically();

							// ---Redirect to Login Page
							// Main.getInstance().openLoginPage(new Stage());

							Main.getInstance().getStageManager().switchScene(FxmlView.LOGIN);

							// Stage stage =
							// Main.getInstance().getParentStage();
							// if (stage != null) {
							// stage.close();
							// }
							//
							// FXMLLoader fxmlLoader = new
							// FXMLLoader(getClass().getResource("/View/Login.fxml"));
							// Pane pane = fxmlLoader.load();
							//
							// StackPane stackPane = new StackPane();
							// stackPane.getChildren().add(pane);
							//
							// Scene scene = new Scene(stackPane);
							// scene.getStylesheets().add(this.getClass().getResource("/styles/newStyles.css").toString());
							//
							// Main.getInstance().getParentStage().setScene(scene);
							// Main.getInstance().getParentStage().show();
							//
							// Main.getInstance().setParentScene(scene);
							ControllerMediator.getInstance().registerGetSnapshot(null);
							ControllerMediator.getInstance().registerGetSnapshotController(null);
							EditCaseFlagSession.getInstance().setIsEditFromDashboard(false);
							EditPatientSession.getInstance().setIsFromDashboard(false);
							// Remove scanned devices memory
							DevicesListenerStatusSession.getInstance().setRemoveS5MonitorListener(true);
							DevicesListenerStatusSession.getInstance().setS5MonitorAssigned(false);
							DevicesListenerStatusSession.getInstance().setS5MonitorScanned(false);

							DevicesListenerStatusSession.getInstance().setRemoveAnesthesiaMachineListener(true);
							DevicesListenerStatusSession.getInstance().setAnesthesiaMachineAssigned(false);
							DevicesListenerStatusSession.getInstance().setAnesthesiaMachineScanned(false);

							DevicesListenerStatusSession.getInstance().setRemoveInjectomatListener(true);
							DevicesListenerStatusSession.getInstance().setInjectomatassigned(false);

							removeListeners();
						}
					} catch (Exception e) {
						LOG.error("## Exception occured:", e);
					}

				}
			};
			btnLogout.addEventHandler(MouseEvent.MOUSE_CLICKED, btnLogoutHandler);

			btnExitCaseHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent event) {
					try {
						boolean status = Main.getInstance().getUtility()
								.confirmDialogue("Are you sure, you want to exit case?");
						if (status) {
							// DeviceDataSaveService.getInstance().setIsCaseLoaded(false);
							// DeviceDataSaveService.getInstance().closeFile();

							lblName.setText("");

							lblName.setText("");

							lblCrNum.setText("");

							lblGender.setText("");

							lblAge.setText("");

							lblHeight.setText("");

							lblWeight.setText("");

							lblBMI.setText("");

							lblBSA.setText("");
							lblCaseNo.setText("");
							btnExitCase.setVisible(false);

							PatientSession.getInstance().setPatient(null);
							PatientSession.getInstance().setPatientCase(null);

							AldreteScoreSession.getInstance().setPACUparams(null);
							AldreteScoreSession.getInstance().setSelectedType(null);
							AldreteScoreSession.getInstance().setShiftingOutParams(null);

							ConfigureDeviceSession.getInstance().setDevice(null);

							ConfiguredInjectomartSession.getInstance().setAssignedDeviceList(null);
							ConfiguredInjectomartSession.getInstance().setConfiguredDeviceList(null);
							ConfiguredInjectomartSession.getInstance().setDetectedDeviceList(null);

							// DevicesListenerStatusSession.getInstance().setAnesthesiaMachineScanned(false);
							// DevicesListenerStatusSession.getInstance().setAnesthesiaMachineAssigned(false);
							// DevicesListenerStatusSession.getInstance().setInjectomatassigned(false);
							// DevicesListenerStatusSession.getInstance().setRemoveAnesthesiaMachineListener(true);
							// DevicesListenerStatusSession.getInstance().setRemoveS5MonitorListener(true);
							// DevicesListenerStatusSession.getInstance().setRemoveInjectomatListener(true);
							// DevicesListenerStatusSession.getInstance().setS5MonitorAssigned(false);
							// DevicesListenerStatusSession.getInstance().setS5MonitorScanned(false);

							DashboardTestSession.getInstance().setTestList(null);

							EventLogSession.getInstance().setEventLog(null);
							EventLogSession.getInstance().setEventTime(null);

							FluidSession.getInstance().setFluidValue(null);
							InjectomartSession.getInstance().setConfiguredInfusionPumpList(null);

							MedicationSession.getInstance().setIntraopMedicationlog(null);

							OutputSession.getInstance().setOutput(null);

							FwkServices.getInstance().removeDeviceListeners();

							try {
								ControllerMediator.getInstance().getLoginController().navigateToMainDashboard();
							} catch (Exception e1) {
								LOG.error("## Exception occured:", e1);
							}
							EditCaseFlagSession.getInstance().setIsEditFromDashboard(false);
							EditPatientSession.getInstance().setIsFromDashboard(false);
							ControllerMediator.getInstance().registerGetSnapshot(null);
							ControllerMediator.getInstance().registerGetSnapshotController(null);
							// ---Stop running timelines
							stopRunningTimelines();
							removeListeners();

						}
					} catch (Exception e) {
						LOG.error("## Exception occured:", e);
					}
				}
			};
			btnExitCase.addEventHandler(MouseEvent.MOUSE_CLICKED, btnExitCaseHandler);

			publishToggleListener = new ChangeListener<Boolean>() {

				@Override
				public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {

					try {
						if (newValue) {
							publishMode = true;

							if (Main.getInstance().getKafkaProducer().getProducer() == null) {
								if (SystemConfiguration.getInstance().getKafkaIp().isEmpty()) {
									Main.getInstance().getUtility().errorDialogue("Error",
											"Settings not configured! Please contact administrator");
									toggleButtonPublish.setSelected(false);
								} else {
									Main.getInstance().getKafkaProducer().initializeProducer();
								}
							}

							if (!SystemConfiguration.getInstance().getKafkaIp().isEmpty()) {
								sendPatientInfoToKafka();
								sendExecutedEventsInfoToKafka();
								// sendScalingInfoToKafka();
							}

							if (ecgTimeline != null)
								if (ecgTimeline.getStatus() != Status.RUNNING)
									ecgTimeline.play();
						} else {
							publishMode = false;

							if (ptDataTimeline != null)
								if (ptDataTimeline.getStatus() == Status.RUNNING)
									ptDataTimeline.stop();

							if (eventsTimeline != null)
								if (eventsTimeline.getStatus() == Status.RUNNING)
									eventsTimeline.stop();

							/*
							 * if (scalingFactorTimeline != null) if
							 * (scalingFactorTimeline.getStatus() ==
							 * Status.RUNNING) scalingFactorTimeline.stop();
							 */
							if (ecgTimeline != null)
								if (ecgTimeline.getStatus() == Status.RUNNING)
									ecgTimeline.stop();
						}
					} catch (Exception e) {
						LOG.error("## Exception occured:", e);
					}
				}

			};
			toggleButtonPublish.selectedProperty().addListener(publishToggleListener);

			/*
			 * btnDemoControls.setOnAction(new EventHandler<ActionEvent>() {
			 *
			 * @Override public void handle(ActionEvent arg0) {
			 * bindPopupFxml("/View/DemoButtons.fxml"); }
			 *
			 * });
			 *
			 * toggleLiveGraphs.selectedProperty().addListener(((observable,
			 * oldValue, newValue) -> {
			 *
			 * centerPane.getChildren().clear(); if (newValue) { // ---add
			 * components to centerPane drawNewGraphs = new DrawNewGraphs();
			 * drawNewGraphs.createCenterPane(centerPane);
			 * System.out.println("xValueTimelineSpeed---->" +
			 * drawNewGraphs.xValueTimelineSpeed()); xValueTimeline = new
			 * Timeline(new
			 * KeyFrame(Duration.millis(drawNewGraphs.xValueTimelineSpeed()), ae
			 * -> { drawNewGraphs.setxValue(drawNewGraphs.getxValue() + 1);
			 *
			 *
			 * // ---Rubb all graphs simultaneously
			 * drawNewGraphs.getEcgBrush().clearRect(drawNewGraphs.getxValue() +
			 * 1, 0, 5, drawNewGraphs.getECGGraphHeight());
			 * drawNewGraphs.getIbpBrush().clearRect(drawNewGraphs.getxValue() +
			 * 1, 0, 5, drawNewGraphs.getIbpGraphHeight());
			 * drawNewGraphs.getSpo2Brush().clearRect(drawNewGraphs.getxValue()
			 * + 1, 0, 5, drawNewGraphs.getSPO2GraphHeight());
			 *
			 * if (drawNewGraphs.getxValue() > new
			 * Double(drawNewGraphs.getCalcGraphWidth()).intValue()) { try { //
			 * ---Erase entire GraphPane
			 * drawNewGraphs.getGraphBrush().clearRect(0, 0,
			 * drawNewGraphs.getGraphPaneWidth(),
			 * drawNewGraphs.getGraphPaneHeight()); // ---reset x value
			 * drawNewGraphs.setxValue(0); } catch (Exception e1) {
			 * e1.printStackTrace(); }
			 *
			 * } else { // ---Continuously run a red line as current index
			 * indicator
			 * drawNewGraphs.getGraphBrush().strokeLine(drawNewGraphs.getxValue(
			 * ), drawNewGraphs.getTimePaneHeight(), drawNewGraphs.getxValue(),
			 * drawNewGraphs.getECGGraphHeight() +
			 * drawNewGraphs.getSPO2GraphHeight() +
			 * drawNewGraphs.getSPO2GraphHeight()+drawNewGraphs.
			 * getGraphPaneHeight() *0.07);
			 * drawNewGraphs.getGraphBrush().clearRect(drawNewGraphs.getxValue()
			 * - 1, 0, 1, drawNewGraphs.getGraphPaneHeight()); } }));
			 * xValueTimeline.setCycleCount(Animation.INDEFINITE);
			 * xValueTimeline.play();
			 *
			 *
			 * secondXValueTimeline = new Timeline(new
			 * KeyFrame(Duration.millis(drawNewGraphs.xValueTimelineSpeed() *
			 * 4), ae -> {
			 * drawNewGraphs.setSecondXValue(drawNewGraphs.getSecondXValue() +
			 * 1);
			 *
			 *
			 * // ---Rubb all graphs simultaneously
			 * drawNewGraphs.getEtcBrush().clearRect(drawNewGraphs.
			 * getSecondXValue() + 1, 0, 5, drawNewGraphs.getEtcGraphHeight());
			 * drawNewGraphs.getPressureBrush().clearRect(drawNewGraphs.
			 * getSecondXValue() + 1, 0, 5,
			 * drawNewGraphs.getPressureGraphHeight());
			 *
			 * if (drawNewGraphs.getSecondXValue() > new
			 * Double(drawNewGraphs.getCalcGraphWidth()).intValue()) { try { //
			 * ---Erase entire GraphPane
			 * drawNewGraphs.getGraphBrush().clearRect(0, 0,
			 * drawNewGraphs.getGraphPaneWidth(),
			 * drawNewGraphs.getGraphPaneHeight()); // ---reset x value
			 * drawNewGraphs.setSecondXValue(0); } catch (Exception e1) {
			 * e1.printStackTrace(); }
			 *
			 * } else { // ---Continuously run a red line as current index
			 * indicator drawNewGraphs.getGraphBrush().strokeLine(drawNewGraphs.
			 * getSecondXValue(), drawNewGraphs.getEtcGraphYLayout(),
			 * drawNewGraphs.getSecondXValue(),
			 * drawNewGraphs.getSecondXLineHeight());
			 * drawNewGraphs.getGraphBrush().clearRect(drawNewGraphs.
			 * getSecondXValue () - 1, drawNewGraphs.getEtcGraphYLayout() - 1,
			 * 1, drawNewGraphs.getSecondXLineHeight()); } }));
			 * secondXValueTimeline.setCycleCount(Animation.INDEFINITE);
			 * secondXValueTimeline.play(); } else { // ---stop live grpah
			 * plotting if (xValueTimeline != null) if
			 * (xValueTimeline.getStatus() == Status.RUNNING)
			 * xValueTimeline.stop(); if (secondXValueTimeline != null) if
			 * (secondXValueTimeline.getStatus() == Status.RUNNING)
			 * secondXValueTimeline.stop(); }
			 *
			 * }));
			 */
		} catch (Exception e) {
			LOG.error("## Exception occured:", e);
		}
	}

	/**
	 * This method loads events for side bar
	 */
	public void loadSideBarEvents() {
		try {
			sidePane.getChildren().clear();
			drawSidePane.createSidePane(sidePane, disablePaneMainContent);
		} catch (Exception e) {
			LOG.error("## Exception occured:", e);
		}
	}

	/**
	 * This method update date and time labels on the top patient banner
	 */
	private void updateTimeAndDate() {
		try {
			String dateString = new SimpleDateFormat("dd-MM-yyy", Locale.ENGLISH)
					.format(Calendar.getInstance().getTime());
			String timeString = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH)
					.format(Calendar.getInstance().getTime());
			time.setText(timeString);
			date.setText(dateString);
		} catch (Exception e) {
			LOG.error("## Exception occured:", e);
		}
	}

	/**
	 * This method fills the patient info in the top banner using Patient Object
	 */
	public void fillDashboardPatientBanner() {

		Patient patientObj = PatientSession.getInstance().getPatient();

		if (patientObj != null) {
			if ((patientObj.getFirstName() != null && !patientObj.getFirstName().isEmpty())
					|| (patientObj.getLastName() != null && !patientObj.getLastName().isEmpty())
					|| patientObj.getCrnumber() > 0) {
				if (patientObj.getFirstName() != null)
					lblName.setText(patientObj.getFirstName());
				if (patientObj.getLastName() != null)
					lblName.setText(lblName.getText() + " " + patientObj.getLastName());
				if (patientObj.getCrnumber() > 0)
					lblCrNum.setText(patientObj.getCrnumber() + "");
				if (patientObj.getGender() != null)
					lblGender.setText(patientObj.getGender());
				if (patientObj.getAge() != null)
					lblAge.setText("" + patientObj.getAge() + " " + patientObj.getAgeUnit());
				if (patientObj.getHeight() > 0)
					lblHeight.setText(patientObj.getHeight() + " cm");
				if (patientObj.getWeight() > 0)
					lblWeight.setText(patientObj.getWeight() + " kg");
				if (patientObj.getBmi() > 0)
					lblBMI.setText(patientObj.getBmi() + " kg/m2");
				if (patientObj.getBsa() > 0)
					lblBSA.setText(patientObj.getBsa() + " m2");
				if (PatientSession.getInstance().getPatientCase().getCaseId() != null) {
					lblCaseNo.setText(PatientSession.getInstance().getPatientCase().getCaseId() + "");
					/*
					 * lblSelectedOT.setText(PatientSession.getInstance().
					 * getPatientCase().getoT());
					 * OTSession.getInstance().setSelectedOT(PatientSession.
					 * getInstance().getPatientCase().getoT());
					 */
				}
				if (patientObj.getBloodGroup() != null && !patientObj.getBloodGroup().trim().equals("")) {
					lblBloodGroup.setText(patientObj.getBloodGroup());
				} else {
					lblBloodGroup.setText("N/R");
				}

			}
		}
	}

	/**
	 * This method initializes a timeline to update X value for live TrendGraph
	 */
	/*
	 * private void initializeXValTrendTimeline() { if (!trendTimelineStarted) {
	 * drawTabbedCenter.getAnesGraphBrush().setStroke(Color.RED);
	 * drawTabbedCenter.getPtMntrGraphBrush().setStroke(Color.RED); //
	 * drawTabbedCenter.getMedBrush().setStroke(Color.RED);
	 * drawTabbedCenter.getAnesGraphBrush().strokeLine(0, 0, 0,
	 * drawTabbedCenter.getAnesGraphPanel().getPrefHeight());
	 * drawTabbedCenter.getPtMntrGraphBrush().strokeLine(0, 0, 0,
	 * drawTabbedCenter.getPtMntrGraphPanel().getPrefHeight()); //
	 * drawTabbedCenter.getMedBrush().strokeLine(0, 0, 0, //
	 * drawTabbedCenter.getMedCanvas().getHeight());
	 *
	 * if (xValueTrendTimeline.getStatus() != Status.RUNNING)
	 * xValueTrendTimeline.play();
	 *
	 * trendTimelineStarted = true; }
	 *
	 * }
	 */
	/**
	 * This method sends patient information(name,CrNum,age etc) to KAFKA
	 */
	private void sendPatientInfoToKafka() {

		try {
			ptDataTimeline = new Timeline(
					new KeyFrame(Duration.millis(SystemConfiguration.getInstance().getPtmntrPublishFrequency()), ae -> {
						try {
							Patient patientObj = PatientSession.getInstance().getPatient();
							Patientcase caseObj = PatientSession.getInstance().getPatientCase();
							String timeIn = "NA";
							if (EventLogSession.getInstance().getEventTime() != null)
								timeIn = sdfNew.format(EventLogSession.getInstance().getEventTime());

							String procedure = "NA";

					        if (caseObj.getPlannedProcedureText() != null
					                && !caseObj.getPlannedProcedureText().trim().isEmpty())
								procedure = caseObj.getPlannedProcedureText().trim();

							String diagnosis = "NA";
					        if (caseObj.getDiagnosisText() != null && !caseObj.getDiagnosisText().trim().isEmpty())
								diagnosis = caseObj.getDiagnosisText().trim();

							String age = patientObj.getAge() + " " + patientObj.getAgeUnit().toLowerCase();

							String ptDetailsPublishData = patientObj.getFirstName() + "_" + patientObj.getLastName()
									+ "|" + patientObj.getCrnumber() + "|" + patientObj.getGender() + "|" + age + "|"
									+ patientObj.getHeight() + "|" + patientObj.getWeight() + "|" + patientObj.getBmi()
									+ "|" + patientObj.getBsa() + "|" + OTSession.getInstance().getSelectedOT().trim()
									+ "|" + timeIn + "|" + diagnosis + "|" + procedure;

							// ---Send data over KAFKA
							Main.getInstance().getKafkaProducer()
									.setTopic(SystemConfiguration.getInstance().getPatientDetailsTopic());
							Main.getInstance().getKafkaProducer().setProducedData(ptDetailsPublishData);
							// Main.getInstance().getKafkaProducer().getProducerThread().execute();
							Main.getInstance().getKafkaProducer().callProducer();
						} catch (Exception e) {
							LOG.error("## Exception occured:", e);
						}
					}));
			ptDataTimeline.setCycleCount(Animation.INDEFINITE);
			if (ptDataTimeline.getStatus() != Status.RUNNING)
				ptDataTimeline.play();
		} catch (Exception e) {
			LOG.error("## Exception occured:", e);
		}

	}

	/**
	 * This method sends information related to executed events to KAFKA
	 */
	private void sendExecutedEventsInfoToKafka() {

		try {
			eventsTimeline = new Timeline(
					new KeyFrame(Duration.millis(SystemConfiguration.getInstance().getPtmntrPublishFrequency()), ae -> {
						try {
							StringBuilder publishData = new StringBuilder();
							if (ExecutedEventSession.getInstance().getExecutedEventList() != null)
								for (IntraopEventlog obj : ExecutedEventSession.getInstance().getExecutedEventList()) {
									if (obj != null)
										if (obj.getEventTime() != null) {
											publishData.append(obj.getCustomEventName() + "-"
													+ sdf.format(obj.getEventTime()) + " |");
										}
								}

							// ---Send data over KAFKA
							Main.getInstance().getKafkaProducer()
									.setTopic(SystemConfiguration.getInstance().getExecEventsTopic());
							
							Main.getInstance().getKafkaProducer().setProducedData(publishData.toString());
							Main.getInstance().getKafkaProducer().callProducer();
						} catch (Exception e) {
							LOG.error("## Exception occured:", e);
						}
					}));
			eventsTimeline.setCycleCount(Animation.INDEFINITE);
			if (eventsTimeline.getStatus() != Status.RUNNING)
				eventsTimeline.play();
		} catch (Exception e) {
			LOG.error("## Exception occured:", e);
		}
	}

	/**
	 * This method sends waveform scaling info to KAFKA
	 */
	/*
	 * public void sendScalingInfoToKafka() { scalingFactorTimeline = new
	 * Timeline(new KeyFrame(Duration.millis(SystemConfiguration.getInstance().
	 * getScalingvarsPublishFrequency()), ae -> { try { String scalingVars =
	 * Main.getInstance().getPlotGraphs().getEcgScalingVar() + "|" +
	 * drawNewGraphs.getIbpYAxisLbl() + "|"
	 * +Main.getInstance().getPlotGraphs().getSpScalingVar() + "|"
	 * +drawNewGraphs.getEtcYAxisLbl() + "|" +drawNewGraphs.getPressYAxisLbl() +
	 * "|" +Main.getInstance().getPlotGraphs().getEcgScalingVar() + "|" +
	 * Main.getInstance().getPlotGraphs().getIbpScalingVar() + "|"
	 * +Main.getInstance().getPlotGraphs().getSpScalingVar() + "|" +
	 * Main.getInstance().getPlotGraphs().getEtcScalingVar() + "|" +
	 * Main.getInstance().getPlotAnesthesiaGraph().getPeakScalingVar();
	 *
	 *
	 * String scalingVars = "1|" + "200|" + "1|" + "40|" + "40|" + "1|" +
	 * "20000|" + "1|" + "400|" + "1";
	 *
	 * Main.getInstance().getKafkaProducer().setTopic(SystemConfiguration.
	 * getInstance().getScalingVarsTopic());
	 * Main.getInstance().getKafkaProducer().setProducedData(scalingVars); //
	 * Main.getInstance().getKafkaProducer().getProducerThread().execute();
	 * Main.getInstance().getKafkaProducer().callProducer(); } catch (Exception
	 * e) { LOG.error("## Exception occured:", e); }
	 *
	 * })); scalingFactorTimeline.setCycleCount(Animation.INDEFINITE); if
	 * (scalingFactorTimeline.getStatus() != Status.RUNNING)
	 * scalingFactorTimeline.play(); }
	 */

	/**
	 * This method take care of any running timeline and stops it
	 */
	private void stopRunningTimelines() {
		try {
			if (drawTabbedCenter != null) {
				if (drawTabbedCenter.getUpdateTimeline() != null)
					drawTabbedCenter.getUpdateTimeline().stop();

				if (drawTabbedCenter.getCheckTime() != null)
					drawTabbedCenter.getCheckTime().stop();
			}

			if (ptDataTimeline != null)
				if (ptDataTimeline.getStatus() == Status.RUNNING)
					ptDataTimeline.stop();

			if (eventsTimeline != null)
				if (eventsTimeline.getStatus() == Status.RUNNING)
					eventsTimeline.stop();
		} catch (Exception e) {
			LOG.error("## Exception occured:", e);
		}
	}

	/**
	 * This method loads the FXML passed to it as argument
	 *
	 * @param path
	 * @throws IOException
	 */
	public void bindPopupFxml(FxmlView path) {
		try {
			Main.getInstance().getStageManager().switchSceneAfterMain(path);
		} catch (Exception e) {
			LOG.error("## Exception occured:", e);

		}
		// try {
		// Pane pane = FXMLLoader.load(getClass().getResource(path));
		// Pane blurPane = new Pane();
		// blurPane.setStyle("-fx-background-color: #505050");
		// blurPane.setOpacity(0.5);
		// Stage primaryStage = Main.getInstance().getParentStage();
		// Stage stage = new Stage();
		// stage.initModality(Modality.WINDOW_MODAL);
		// stage.initOwner(primaryStage);
		// Main.getInstance().setChildStage(stage);
		// StackPane stackPane = new StackPane();
		// stackPane.getChildren().add(pane);
		// Main.getInstance().setChildStackPane(stackPane);
		// Scene newScene = new Scene(stackPane);
		// newScene.getStylesheets()
		// .add(this.getClass().getResource("/styles/newStyles.css").toString());
		// stage.setScene(newScene);
		// stage.initStyle(StageStyle.UNDECORATED);
		// stage.show();
		// } catch (Exception e) {
		//
		// e.printStackTrace();
		// }

	}

	/**
	 * This method contains code to exit from application
	 */
	@FXML
	private void closeApplication() {
		boolean status = Main.getInstance().getUtility().confirmDialogue("Are you sure, you want to close?");
		if (status) {

			// ---logged in user object set to null
			UserSession.getInstance().setUserWithrRolesForUserAuthentication(null);

			// ---Stop Graph plotting transition
			if (Main.getInstance().getPlotGraphs().getGraphTimeline() != null) {
				if (Main.getInstance().getPlotGraphs().getGraphTimeline().getStatus() == Status.RUNNING) {
					Main.getInstance().getPlotGraphs().getGraphTimeline().stop();
				}
			}

			if (FwkServices.getInstance().getAutomaticDeviceIdentificationServiceStatus() == true)
				FwkServices.getInstance().stopIdentifyingDevicesAutomatically();
			removeListeners();
			Platform.exit();
			System.exit(0);
		}
	}

	/**
	 * This method sets percentage width for header, footer and center pane
	 * based on screen resolution
	 */
	private void setComponentsize() {
		double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
		double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();
		baseBorderPane.setPrefSize(screenWidth, screenHeight);
		ptDetailsHeader.setPrefSize(screenWidth, screenHeight * 0.07);
		controlsHeader.setPrefSize(screenWidth * 0.92, screenHeight * 0.08);
		centerPane.setPrefSize(screenWidth * 0.88, screenHeight * 0.82);
		lblCopyright.setPrefWidth(screenWidth * 0.88);
		lblCopyright.setAlignment(Pos.CENTER);
		sidePane.setPrefSize(screenWidth * 0.12, screenHeight * 0.87);
		headerVbox.setPrefSize(screenWidth, screenHeight * 0.14);
		headerStartHbox.setPrefWidth(screenWidth * 0.7);
		headerCornerHbox.setPrefWidth(screenWidth * 0.3);
		double controllHeadrerWidth = screenWidth * 0.07;
		double controllHeadrerHeight = screenHeight * 0.07;
		// changes by Sudeep
		double widthOFButtonholder = 0;
		for (int i = 0; i < controlsHeader.getChildren().size() - 2; i++) {
			if (i != controlsHeader.getChildren().size() - 2) {
				Button controlBtn = (Button) controlsHeader.getChildren().get(i);
				controlBtn.setStyle(String.format("-fx-font-size: %dpx;", (int) (0.115 * controllHeadrerWidth)));
				controlBtn.setPrefWidth(controllHeadrerWidth * 0.99);
				widthOFButtonholder = widthOFButtonholder + controllHeadrerWidth * 0.99;

			}
		}

		close.setPrefWidth(controllHeadrerWidth * 0.6);
		close.setPrefHeight(controllHeadrerHeight * 0.9);
		close.setStyle(String.format("-fx-font-size: %dpx;", (int) (0.12 * controllHeadrerWidth)));
		toggleButtonPublish.setPrefHeight(controllHeadrerHeight);
		// toggleButtonPublish.setPrefWidth(controllHeadrerWidth * 0.1);
		// if (widthOFButtonholder != 0) {
		// hbExitBtnHolder.setPrefWidth(screenWidth - widthOFButtonholder);
		// }
		// toggleButtonPublish.setStyle(String.format("-fx-font-size: %dpx;",
		// (int) (0.09 * controllHeadrerWidth)));
		date.setStyle(String.format("-fx-font-size: %dpx;", (int) (0.16 * controllHeadrerWidth)));
		time.setStyle(String.format("-fx-font-size: %dpx;", (int) (0.16 * controllHeadrerWidth)));
		lblOt.setStyle(String.format("-fx-font-size: %dpx;", (int) (0.16 * controllHeadrerWidth)));
		lblSelectedOT.setStyle(String.format("-fx-font-size: %dpx;", (int) (0.16 * controllHeadrerWidth)));

		VBox vBPageBlock = new VBox();
		Image imageLogo = new Image(this.getClass().getResourceAsStream("/res/logo.png"));
		vBPageBlock.setStyle("-fx-background-color:#505050");
		vBPageBlock.setOpacity(0.5);
		imgMainLogo.setImage(imageLogo);
		imgMainLogo.prefHeight(screenHeight * 0.4);
		imgMainLogo.prefWidth(screenWidth * 0.5);
		vBPageBlock.getChildren().add(imgMainLogo);
		vBPageBlock.setAlignment(Pos.CENTER);
		vBPageBlock.setSpacing(10);
		Label lbl = new Label("Please Search or Add Patient to continue.");
		lbl.setStyle("-fx-font-size:25 px");
		lbl.setStyle("-fx-font-weight:bold");
		lbl.setStyle("-fx-text-fill: #ffffff");
		vBPageBlock.getChildren().add(lbl);
		vBPageBlock.setPrefSize(screenWidth, screenHeight * 0.86);
		pageMainContent.getChildren().add(vBPageBlock);

	}

	/**
	 * This method removes listeners active on all components of main dashboard
	 */
	private void removeListeners() {

		try {
			eventsBtn.removeEventHandler(MouseEvent.MOUSE_CLICKED, eventsBtnHandler);
			// fluidsBtn.removeEventHandler(MouseEvent.MOUSE_CLICKED,
			// fluidsBtnHandler);
			AnesthesiaBtn.removeEventHandler(MouseEvent.MOUSE_CLICKED, AnesthesiaBtnHandler);
			btnSearchPatient.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnSearchPatientHandler);
			btnLogout.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnLogoutHandler);
			btnExitCase.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnExitCaseHandler);
			// btnMedicationHistory.removeEventHandler(MouseEvent.MOUSE_CLICKED,
			// btnMedicationHistoryHandler);
			btnPostOp.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnPostOpHandler);
			btnReport.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnReportHandler);
			btnManageDevices.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnManageDevicesHandler);
			btnVascularCannulations.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnVascularCannulationsHandler);
			btnAddPatient.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnAddPatientHandler);
			btnEditCase.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnEditCaseHandler);
			btnS5Chart.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnS5ChartHandler);
			btnAnesthesiaChart.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnAnesthesiaChartHandler);
			btnHistoricalView.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnHistoricalViewHandler);
			btnPAC.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnPACHandler);
			btnCPBDetails.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnCPBHandler);
			btnEchoDetails.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnEchoHandler);
			//changes done By Rohi
			btnIcuPlan.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnIcuPlanHandler);
			//changes end
			// btnOutput.removeEventHandler(MouseEvent.MOUSE_CLICKED,
			// btnOutputCaseHandler);

			toggleButtonPublish.selectedProperty().removeListener(publishToggleListener);

			if (drawTabbedCenter != null)
				drawTabbedCenter.removeHandlers();
		} catch (Exception e) {
			LOG.error("## Exception occured:", e);
		}

	}
}
