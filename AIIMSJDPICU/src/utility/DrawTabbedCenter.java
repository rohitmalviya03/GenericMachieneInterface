/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package utility;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.cdac.common.GeneratedEntities.AnethesiaMachineData;
import com.cdac.common.GeneratedEntities.Fdamedications;
import com.cdac.common.GeneratedEntities.Fluid;
import com.cdac.common.GeneratedEntities.Fluidvalue;
import com.cdac.common.GeneratedEntities.IntraopMedicationlog;
import com.cdac.common.GeneratedEntities.IntraopOutput;
import com.cdac.common.GeneratedEntities.Investigationparameterfields;
import com.cdac.common.GeneratedEntities.Investigationstests;
import com.cdac.common.GeneratedEntities.Patient;
import com.cdac.common.GeneratedEntities.PatientMonitorData;
import com.cdac.common.dao.MedicationLogDao;
import com.cdac.common.dao.MedicationLogDaoImpl;
import com.cdac.common.pojoClasses.InvestigationMapperAndValue;
import com.cdac.common.pojoClasses.InvestigationSetValue;
import com.cdac.framework.SystemConfiguration;

import application.FxmlView;
import application.Main;
import controllers.HistoricalDataController;
import controllers.MedicationInfusionController;
import controllers.StopInfusionController;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.WorkerStateEvent;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TabPane;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;
import mediatorPattern.ControllerMediator;
import model.MasterDataSession;
import model.PatientSession;
import model.UserSession;
import services.GetAnesthesiaMachineDataTimeCaseService;
import services.GetIntraopMedicationlogService;
import services.GetInvestigationMapperAndValueService;
import services.GetMedAndFluidsService;
import services.GetPatientMonitorDataTimeCaseService;
import services.GetTestListService;
import services.GetTotalFluidVolumeService;
import services.GetTotalMedicationVolumeListService;
import services.GetTotalMedicationVolumeService;
import services.GetTotalOutputTypeVolumeService;
import services.SaveMedicationService;
import services.SearchFluidValueService;
import services.SearchIntraOpOutputService;

/**
 * DrawTabbedCenter.java <br>
 * Purpose: This class contains logic to create vitals and anesthesia chart
 * screens and live/Historical grid views
 *
 *
 * Steps to add a new Device Parameter to grid:<br>
 *
 * # Add new parameter details in ParamMinMaxRange.java - Name, Legend, Color,
 * MaxValue, Unit<br>
 * # Capture parsed parameter in the corresponding listener<br>
 * # Add new parameter to DeviceParamValMap in corresponding listener to show
 * its value on live grid<br>
 *
 *
 * 2)DrawTabbedCenter.java<br>
 * # Add new parameter to ptMntrTableRowHeader <br>
 * # Add new parameter to respective tempArr in setLegendsArray() method<br>
 * # Use appropriate scaling method (setScaleForAnesParameters() /
 * setScaleForPtMntrParameters()) to set scale for new parameter <br>
 * # Add condition for new parameter in saveDeviceData() and
 * fillDeviceGridCell() method<br>
 *
 * 3)Config file<br>
 * # Add new parameter under CSV_Param_Index section in config file and define
 * getter for the same in SystemConfiguration.java<br>
 *
 *
 * 4)MainController.java<br>
 * # Call appropriate scaling method to set scale for new parameter <br>
 * drawTabbedCenter.setScaleForAnesParameters(ParamMinMaxRange.NAME_OF_PARAM, 0,
 * ParamMinMaxRange.PARAM_MAX_VALUE);<br>
 * or use setScaleForPtMntrParameters()<br>
 *
 *
 * 5)HistoricalDataController.java<br>
 * # Use appropriate method(plotStaticPtMntrGraph / plotStaticAnesGraph) to add
 * plotting logic for new parameter for vitals chart<br>
 * # Add condition specific to new parameter in plotPoint() method<br>
 * # Add condition for new parameter in fetchAnesDBdata/fetchPtMntrDBdata as per
 * requirement<br>
 *
 *
 * 6)GridHistoricalViewController.java<br>
 * # Add new parameter to ptMntrTableRowHeader <br>
 * # Add conditions for new parameter in fillGrid and fillDeviceGridCell
 * methods<br>
 *
 * 7)Create a textfield in respective fxml (PatientMonitor/ AnesthesiaMashine)
 * and set values in the corresponding controller<br>
 *
 *
 * @author Rohit_Sharma41
 *
 */
public class DrawTabbedCenter
{

	private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
	private SimpleDateFormat sdfNew = new SimpleDateFormat("dd-MM-yyyy HH:mm");
	private static final String PIPE_SYMBOL="|";

	static final int MINUTES_PER_HOUR = 60;
	static final int SECONDS_PER_MINUTE = 60;
	static final int SECONDS_PER_HOUR = SECONDS_PER_MINUTE * MINUTES_PER_HOUR;

	public static double TIME_PIXELS_GAP = 0; // pixels
	public static final int TOTAL_TIME_MARKINGS = 10;
	private int MINS_GAP = 1;

	private static final int NO_OF_TABS = 4;
	private String tabNames[] = { "Anesthesia", "S5 PatientMonitor", "Medications", "TabularView" };
	private Integer timeLblsGapArr[] = { 1, 2, 5, 10, 15, 30 };

	private Pane centerPane;
	private TabPane baseTabPane;
	private VBox anesthesiaTabBody;
	private VBox s5TabBody;
	private VBox medTabBody;
	private VBox tablularBody;

	private String currentTab = "";
	private double calculatedGraphWidth = 0;


	// ---Y scale colors
	private String yscale4Color = "#000000";
	private String yscale3Color = "#9b1168";
	private String yscale2Color = "#27abc4";
	private String yscale1Color = "#e20d94";

	// ---Anesthesia
	private String anestimelineStartTime = "", anesFirstTimeLbl = "", anesLastTimeLbl = "";
	private String anesLegendsArr[];
	private Pane anesLegendsPanel;
	private Pane anesGraphPanel;
	private Pane anesBaseGraph;
	private GraphicsContext anesBaseGraphBrush;
	private Canvas anesGraphCanvas;
	private GraphicsContext anesGraphBrush;
	private GraphicsContext anesTimepaneBrush;
	private HBox anesTimeHBox;
	private Pane anesTimePane;
	private Button anesHistData;
	private Pane anesYScaleHolder1, anesYScaleHolder2, anesYScaleHolder3, anesYScaleHolder4;
	private Double anesBaseYScale[] = {}, anesYScale1[] = {}, anesYScale2[] = {}, anesYScale3[] = {};

	// ---S5 PTMonitor
	private String ptMntrtimelineStartTime = "", ptMntrFirstTimeLbl = "", ptMntrLastTimeLbl = "";
	private String ptMntrLegendsArr[];
	private Pane ptMntrLegendsPanel;
	private Pane ptMntrGraphPanel;
	private Pane ptMntrBaseGraph;
	private GraphicsContext ptMntrBaseGraphBrush;
	private Canvas ptMntrGraphCanvas;
	private GraphicsContext ptMntrGraphBrush;
	private GraphicsContext ptMntrTimepaneBrush;
	private HBox ptMntrTimeHBox;
	private Pane ptMntrTimePane;
	private Button ptMntrHistData;
	private Pane ptMntrYScaleHolder1, ptMntrYScaleHolder2, ptMntrYScaleHolder3, ptMntrYScaleHolder4;
	private Double ptMntrBaseYScale[] = {}, ptMntrYScale1[] = {}, ptMntrYScale2[] = {}, ptMntrYScale3[] = {};

	// ---Historical Data
	private int histMinsGap = 15;
	private Double histBaseYScale[] = {}, histYScale1[] = {}, histYScale2[] = {}, histYScale3[] = {};
	private String histLegendsArr[] = {};

	private String histStartDateTime = "", histEndDateTime = "", histFirstDateTimeLbl = "", histLastDateTimeLbl = "";
	private Pane histLegendsPanel;
	private Pane histGraphPanel;
	private Canvas histGraphCanvas;
	private GraphicsContext histGraphBrush;
	private HBox histTimeHBox;
	private Pane histTimePane;
	private Pane histYScaleHolder1, histYScaleHolder2, histYScaleHolder3, histYScaleHolder4;
	private ComboBox<Integer> timeDropdown;
	private Button start, end, next, prev;

	private String medFirstTimeLbl = "", medLastTimeLbl = "";
	private Pane medTimePane;
	private GraphicsContext medTimeBrush;
	private Button addmedBtn, addfluidBtn, addoutputBtn;

	private Canvas medCanvasBase, medCanvas;
	private GraphicsContext medBrushBase, medBrush;

	private double xValue = 0;
	private Map<String, Double[]> paramScaleMap = new HashMap<String, Double[]>();
	private List<Double[]> anesScalesList = new LinkedList<Double[]>();
	private List<Double[]> ptMntrScalesList = new LinkedList<Double[]>();
	private List<Double[]> histScalesList = new LinkedList<Double[]>();
	private List<Double> timeLblCoordinatesList = new LinkedList<Double>();
	private List<String> timeLblsList = new LinkedList<String>();

	// ---GridPane
	private static final String PTMNTR_GRID_NAME = "S5 Patient Monitor Vitals (Live)";
	private static final String ANES_GRID_NAME = "Anesthesia Vitals (Live)";
	public static final String MED_GRID_NAME = "Medication";
	private static final String TEST1_GRID_NAME = "ABG";
	private static final String TEST2_GRID_NAME = "Sugar";
	private static final String TEST3_GRID_NAME = "ACT";
	public static final String FLUID_GRID_NAME = "Fluid";
	public static final String OUTPUT_GRID_NAME = "Output";
	private Patient patient;

	private static final String ptMntrTableRowHeader[] = { "HR " + ParamMinMaxRange.HR_UNIT,
									        "IBPSys " + ParamMinMaxRange.IBP_UNIT,
									        "IBPDia " + ParamMinMaxRange.IBP_UNIT,
									        "SpO2 " + ParamMinMaxRange.SPO2_UNIT,
									        "EtCO2 " + ParamMinMaxRange.ETCO2_UNIT ,
									        "Temp1 "+ParamMinMaxRange.TEMP_UNIT,
									        "Temp2 " +ParamMinMaxRange.TEMP_UNIT,
									        "BIS " + ParamMinMaxRange.BIS_UNIT,
									        "ETAGENT" + ParamMinMaxRange.ETAGENT_UNIT,
									        "MAC" + ParamMinMaxRange.MAC_UNIT,
									        "IBPMean " + ParamMinMaxRange.IBP_UNIT,
									        "NIBPSys " + ParamMinMaxRange.NIBP_UNIT,
											"NIBPDia " + ParamMinMaxRange.NIBP_UNIT,
											"NIBPMean " + ParamMinMaxRange.NIBP_UNIT};
	private static final String anesTableRowHeader[] = { "Ppeak " + ParamMinMaxRange.PPEAK_UNIT,
									        "Pmean " + ParamMinMaxRange.PMEAN_UNIT,
									        "PEEP " + ParamMinMaxRange.PEEPEXT_UNIT,
									        "CircuitO2 " + ParamMinMaxRange.CIRCUITO2_UNIT,
									        "TVexp " + ParamMinMaxRange.TVEXP_UNIT,
									        "MVexp " + ParamMinMaxRange.MVEXP_UNIT,
									        "RR " + ParamMinMaxRange.RR_UNIT };
	private static final String[] outputsTableRowHeader = { "Blood", "Urine" };


	private final int NO_OF_NEW_COLUMNS = 5;
	private double columnPrefWidth;
	private static final double rowPrefHeight = 27;
	private int lastRemovedCol = 0;

	private List<Calendar> headerTimeLblsList = new LinkedList<Calendar>();
	private Calendar gridLastTimeStamp = Calendar.getInstance();
	private int lastGridColumn = 0;

	private TitledPane ptmntrGraphTitledPane, anesGraphTitledPane;

	private int lastMedRow = 0, lastFluidRow = 0;//, lastOutputrow=0;
	private SaveDevcieParamToDB saveDevcieParamToDB = new SaveDevcieParamToDB();
	private Map<String, Calendar> infusionStartedMap = new ConcurrentHashMap<String, Calendar>(); // ---(MedName,StartTime)
	private GetIntraopMedicationlogService getIntraopMedicationlogService;
	private List<String> medPrevCellsFilledList = new ArrayList<String>();
	private Set<String> medsFromInfPump = new HashSet<String>();
	private List<IntraopMedicationlog> historyMedList = new ArrayList<IntraopMedicationlog>();
	private List<Fluidvalue> historyFluidList = new ArrayList<Fluidvalue>();

	private Map<String,Integer> fluidIdsMap=new HashMap<String, Integer>();
	private Map<String, Calendar> fluidstartedMap = new HashMap<String, Calendar>();
	private List<String> fluidPrevCellsFilledList = new ArrayList<String>();

	private Investigationstests testObj = new Investigationstests();
	private List<Investigationstests> investigationTestsList = new ArrayList<Investigationstests>();
	private Map<String,Integer> testIdsMap=new HashMap<String, Integer>();
	private VBox testsTitledPanesHolder;


	// ---Dynamic grid creation
	private static int NO_OF_GRID_COLUMNS = 15;
	private Map<String, GridPane> paramGridMap = new HashMap<String, GridPane>();
	private Map<String, GridPane> totalsGridMap = new HashMap<String, GridPane>();
	private Map<String, GridPane> textboxGridMap = new HashMap<String, GridPane>();
	private Map<String, List<String>> paramListMap = new HashMap<String, List<String>>();
	private Map<String, List<String>> totalsListMap = new HashMap<String, List<String>>();
	private Map<String, List<Label>> textboxListMap = new HashMap<String, List<Label>>();
	private Map<String, Map<String, Label>> totalLblsMap = new HashMap<String, Map<String, Label>>();


	//---TimeLines
	private Timeline updateTimeline, checkTime;

	private String clickedGridCellParam = "", clickedTestGrid = "";
	private Calendar clickedGridCellTime;
	private ContextMenu ptmntrContextMenu, anesContextMenu, medContextMenu,fluidContextMenu,outputContextMenu,testContextMenu;

	// ---Pagination
	private Label currentPageLbl, totalPagesLbl;
	private int currentPgValue, totalPgValue;
	private double minsPerWindow, pageFactor;
	private long totalCSVrecords;


	private Map<String, Map<Calendar, String[]>> deviceParamValMap = new HashMap<String, Map<Calendar, String[]>>();

	private String syringePumpRecentInfRate="";
	private Map<String, Boolean> serialNoInfusionMap = new HashMap<String, Boolean>();

	private Button addNewGridBtn, medAddButton;
	private MenuItem ptmntrMenuItemEnter, anesMenuItemEnter, medMenuItemAdd, medMenuItemBolus, medMenuItemStart,
	        medMenuItemStop, medMenuItemEdit, fluidMenuItemAdd, fluidMenuItemStart, fluidMenuItemStop,
	        outputMenuItemEnter,
	        testMenuItemEnter;

	//---Event Handlers
	private EventHandler<MouseEvent> ptmntrGridClickHandler;
	private EventHandler<MouseEvent> anesGridClickHandler;
	private EventHandler<MouseEvent> medGridClickHandler;
	private EventHandler<MouseEvent> fluidGridClickHandler;
	private EventHandler<MouseEvent> outputGridClickHandler;
	private Map<GridPane, EventHandler<MouseEvent>> testsEventHandlersMap = new HashMap<GridPane, EventHandler<MouseEvent>>();
	private EventHandler<WorkerStateEvent> ptmntrTimeCaseServiceSuccessHandler;
	private EventHandler<WorkerStateEvent> ptmntrTimeCaseServiceFailedHandler;
	private EventHandler<WorkerStateEvent> anesTimeCaseServiceSuccessHandler;
	private EventHandler<WorkerStateEvent> anesTimeCaseServiceFailedHandler;
	private EventHandler<WorkerStateEvent> getMedlogServiceSuccessHandler;
	private EventHandler<WorkerStateEvent> getMedlogServiceFailedHandler;
	private EventHandler<WorkerStateEvent> getFluidTotalsServiceSuccessHandler;
	private EventHandler<WorkerStateEvent> getFluidTotalsServiceFailedHandler;
	private EventHandler<WorkerStateEvent> searchFluidServiceSuccessHandler;
	private EventHandler<WorkerStateEvent> searchFluidServiceFailedHandler;
	private EventHandler<WorkerStateEvent> getOutputTotalsServiceSuccessHandler;
	private EventHandler<WorkerStateEvent> getOutputTotalsServiceFailedHandler;
	private EventHandler<WorkerStateEvent> searchOutputServiceSuccessHandler;
	private EventHandler<WorkerStateEvent> searchOutputServiceFailedHandler;
	private EventHandler<WorkerStateEvent> investigationMapperAndValueServiceSuccessHandler;
	private EventHandler<WorkerStateEvent> investigationMapperAndValueServiceFailedHandler;
	private EventHandler<WorkerStateEvent> getMedTotalsServiceSuccessHandler;
	private EventHandler<WorkerStateEvent> getMedTotalsServiceFailedHandler;
	private EventHandler<WorkerStateEvent> getTestListServiceSuccessHandler;
	private EventHandler<WorkerStateEvent> getTestListServiceFailedHandler;
	private EventHandler<WorkerStateEvent> getTotalMedicationVolumeServiceSuccessHandler;
	private EventHandler<WorkerStateEvent> getTotalMedicationVolumeServiceFailedHandler;
	private EventHandler<WorkerStateEvent> saveMedicationServiceSuccessHandler;
	private EventHandler<WorkerStateEvent> saveMedicationServiceFailedHandler;
	private EventHandler<WorkerStateEvent> getMedAndFluidsServiceSuccessHandler;
	private EventHandler<WorkerStateEvent> getMedAndFluidsServiceFailedHandler;

	private EventHandler<ActionEvent> ptmntrMenuItemEnterHandler;
	private EventHandler<ActionEvent> anesMenuItemEnterHandler;
	private EventHandler<ActionEvent> medMenuItemAddHandler;
	private EventHandler<ActionEvent> medMenuItemBolusHandler;
	private EventHandler<ActionEvent> medMenuItemStartHandler;
	private EventHandler<ActionEvent> medMenuItemStopHandler;
	private EventHandler<ActionEvent> medMenuItemEditHandler;

	private EventHandler<ActionEvent> fluidMenuItemAddHandler;
	private EventHandler<ActionEvent> fluidMenuItemStartHandler;
	private EventHandler<ActionEvent> fluidMenuItemStopHandler;
	private EventHandler<ActionEvent> outputMenuItemEnterHandler;
	private EventHandler<ActionEvent> testMenuItemEnterHandler;

	private EventHandler<MouseEvent> anesHistDataHandler;
	private EventHandler<MouseEvent> ptMntrHistDataHandler;
	private EventHandler<MouseEvent> addNewGridBtnHandler;

	private MedicationLogDao  medicationLogDao =  MedicationLogDaoImpl.getInstance();
	private static final Logger LOG = Logger.getLogger(DrawTabbedCenter.class.getName());

	private static int updateTimelineCounter=0;
	
	private Map<String,IntraopMedicationlog > medicineRateMap = new HashMap<String,IntraopMedicationlog>();
	
	private Map<String,String > medicineTotalRateMap = new HashMap<String,String>();
	
	
	
	public Map<String, String> getMedicineTotalRateMap() {
		return medicineTotalRateMap;
	}
	public void setMedicineTotalRateMap(Map<String, String> medicineTotalRateMap) {
		this.medicineTotalRateMap = medicineTotalRateMap;
	}
	public Map<String, IntraopMedicationlog> getMedicineRateMap() {
		return medicineRateMap;
	}
	public void setMedicineRateMap(Map<String, IntraopMedicationlog> medicineRateMap) {
		this.medicineRateMap = medicineRateMap;
	}
	public Map<String, Boolean> getSerialNoInfusionMap()
	{
		return serialNoInfusionMap;
	}
	public String getSyringePumpRecentInfRate()
	{
		return syringePumpRecentInfRate;
	}
	public void setSyringePumpRecentInfRate(String syringePumpRecentInfRate)
	{
		this.syringePumpRecentInfRate = syringePumpRecentInfRate;
	}
	public Map<String, Integer> getFluidIdsMap()
	{
		return fluidIdsMap;
	}
	public Map<String, List<String>> getParamListMap()
	{
		return paramListMap;
	}
	public Pane getCenterPane()
	{
		return centerPane;
	}
	public void setCenterPane(Pane centerPane)
	{
		this.centerPane = centerPane;
	}
	public void setCurrentTab(String currentTab)
	{
		this.currentTab = currentTab;
	}
	public Timeline getCheckTime()
	{
		return checkTime;
	}
	public List<Investigationstests> getInvestigationTestsList()
	{
		return investigationTestsList;
	}
	public Map<String, Integer> getTestIdsMap()
	{
		return testIdsMap;
	}
	public Timeline getUpdateTimeline()
	{
		return updateTimeline;
	}

	public Map<String, Map<Calendar, String[]>> getDeviceParamValMap()
	{
		return deviceParamValMap;
	}
	public Set<String> getMedsFromInfPump()
	{
		return medsFromInfPump;
	}
	public Map<String, Calendar> getFluidstartedMap()
	{
		return fluidstartedMap;
	}
	public Map<String, Calendar> getInfusionStartedMap()
	{
		return infusionStartedMap;
	}
	public Button getAddmedBtn()
	{
		return addmedBtn;
	}
	public Button getAddfluidBtn()
	{
		return addfluidBtn;
	}
	public Button getAddoutputBtn()
	{
		return addoutputBtn;
	}
	public Canvas getMedCanvas()
	{
		return medCanvas;
	}
	public GraphicsContext getMedBrush()
	{
		return medBrush;
	}
	public Canvas getMedCanvasBase()
	{
		return medCanvasBase;
	}
	public GraphicsContext getMedBrushBase()
	{
		return medBrushBase;
	}
	public void setMedLastTimeLbl(String medLastTimeLbl)
	{
		this.medLastTimeLbl = medLastTimeLbl;
	}
	public String getMedFirstTimeLbl()
	{
		return medFirstTimeLbl;
	}
	public String getMedLastTimeLbl()
	{
		return medLastTimeLbl;
	}
	public Pane getMedTimePane()
	{
		return medTimePane;
	}
	public GraphicsContext getMedTimeBrush()
	{
		return medTimeBrush;
	}
	public void setTotalCSVrecords(long totalCSVrecords)
	{
		this.totalCSVrecords = totalCSVrecords;
	}
	public String getHistStartDateTime()
	{
		return histStartDateTime;
	}
	public void setHistStartDateTime(String histStartDateTime)
	{
		this.histStartDateTime = histStartDateTime;
	}
	public String getHistEndDateTime()
	{
		return histEndDateTime;
	}
	public void setHistEndDateTime(String histEndDateTime)
	{
		this.histEndDateTime = histEndDateTime;
	}
	public String getHistFirstDateTimeLbl()
	{
		return histFirstDateTimeLbl;
	}
	public String getHistLastDateTimeLbl()
	{
		return histLastDateTimeLbl;
	}
	public void setCurrentPgValue(int currentPgValue)
	{
		this.currentPgValue = currentPgValue;
	}
	public void setTotalPgValue(int totalPgValue)
	{
		this.totalPgValue = totalPgValue;
	}
	public double getPageFactor()
	{
		return pageFactor;
	}
	public Label getCurrentPageLbl()
	{
		return currentPageLbl;
	}
	public Label getTotalPagesLbl()
	{
		return totalPagesLbl;
	}
	public void setHistGraphBrush(GraphicsContext histGraphBrush)
	{
		this.histGraphBrush = histGraphBrush;
	}
	public Button getAnesHistData()
	{
		return anesHistData;
	}
	public Button getPtMntrHistData()
	{
		return ptMntrHistData;
	}
	public GraphicsContext getHistGraphBrush()
	{
		return histGraphBrush;
	}
	public Pane getHistGraphPanel()
	{
		return histGraphPanel;
	}
	public String getAnesFirstTimeLbl()
	{
		return anesFirstTimeLbl;
	}
	public String getPtMntrFirstTimeLbl()
	{
		return ptMntrFirstTimeLbl;
	}
	public void setAnesLegendsArr(String[] anesLegendsArr)
	{
		this.anesLegendsArr = anesLegendsArr;
	}
	public void setPtMntrLegendsArr(String[] ptMntrLegendsArr)
	{
		this.ptMntrLegendsArr = ptMntrLegendsArr;
	}
	public List<Double[]> getAnesScalesList()
	{
		return anesScalesList;
	}
	public void setAnesLastTimeLbl(String anesLastTimeLbl)
	{
		this.anesLastTimeLbl = anesLastTimeLbl;
	}
	public void setPtMntrLastTimeLbl(String ptMntrLastTimeLbl)
	{
		this.ptMntrLastTimeLbl = ptMntrLastTimeLbl;
	}
	public List<Double> getTimeLblCoordinatesList()
	{
		return timeLblCoordinatesList;
	}
	public List<Double[]> getPtMntrScalesList()
	{
		return ptMntrScalesList;
	}
	public double getxValue()
	{
		return xValue;
	}
	public void setxValue(double xValue)
	{
		this.xValue = xValue;
	}
	public void setTabNames(String[] tabNames)
	{
		this.tabNames = tabNames;
	}
	public String getCurrentTab()
	{
		return currentTab;
	}
	public GraphicsContext getAnesGraphBrush()
	{
		return anesGraphBrush;
	}
	public GraphicsContext getPtMntrGraphBrush()
	{
		return ptMntrGraphBrush;
	}
	public Pane getAnesGraphPanel()
	{
		return anesGraphPanel;
	}
	public Pane getPtMntrGraphPanel()
	{
		return ptMntrGraphPanel;
	}
	public double getCalculatedGraphWidth()
	{
		return calculatedGraphWidth;
	}
	public HBox getAnesTimeHBox()
	{
		return anesTimeHBox;
	}
	public Pane getAnesTimePane()
	{
		return anesTimePane;
	}
	public HBox getPtMntrTimeHBox()
	{
		return ptMntrTimeHBox;
	}
	public Pane getPtMntrTimePane()
	{
		return ptMntrTimePane;
	}
	public String getAnesLastTimeLbl()
	{
		return anesLastTimeLbl;
	}
	public String getPtMntrLastTimeLbl()
	{
		return ptMntrLastTimeLbl;
	}
	public Map<String, Double[]> getParamScaleMap()
	{
		return paramScaleMap;
	}
	public GraphicsContext getAnesTimepaneBrush()
	{
		return anesTimepaneBrush;
	}
	public GraphicsContext getPtMntrTimepaneBrush()
	{
		return ptMntrTimepaneBrush;
	}

	/**
	 * This method add components to the passed pane
	 *
	 * @param centerPane the outermost pane where all the titled panes and grid components are added
	 */
	public void createCenterPane(Pane centerPane)
	{
		try
		{
			this.centerPane = centerPane;

			TIME_PIXELS_GAP = centerPane.getPrefWidth() * 0.08;
			MINS_GAP = SystemConfiguration.getInstance().getMinsGap();
			/*addBaseTabPane();

			calculatedGraphWidth = calcGraphWidth();
			dividePaneInSections(anesthesiaTabBody);
			dividePaneInSections(s5TabBody);
			createMedTabBodyArea(medTabBody);
			createTableView(tablularBody);*/

			// setLegendsArray();
			calculatedGraphWidth = calcGraphWidth();
			tablularBody= new VBox();
			tablularBody.setPrefSize(centerPane.getPrefWidth(), centerPane.getPrefHeight());
			tablularBody.setId("tabularView");
			tablularBody.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
			createTableView(tablularBody);
			centerPane.getChildren().add(tablularBody);



			//updateTimeline = new Timeline(new KeyFrame(Duration.minutes(SystemConfiguration.getInstance().getMinsGap()), ae ->
			updateTimeline = new Timeline(new KeyFrame(Duration.seconds(SystemConfiguration.getInstance().getDataSaveTimeInterval()), ae ->
			{
				try
				{
					updateTimelineCounter+=SystemConfiguration.getInstance().getDataSaveTimeInterval();

					//if(SystemConfiguration.getInstance().getMinsGap() ==1)
						updateTimelineCounter=0;

					Calendar currentime = Calendar.getInstance();
					updaterFunc1(currentime);
				}
				catch (Exception e)
				{e.printStackTrace();
					LOG.error("## Exception occured:", e);
				}
			}));

			checkTime = new Timeline(new KeyFrame(Duration.seconds(1), ae ->
			{
				try
				{
					Calendar currentTime = Calendar.getInstance();

					String time = sdf.format(currentTime.getTime());
					int mins = Integer.parseInt(time.split(":")[1]);
					if (mins % SystemConfiguration.getInstance().getMinsGap() == 0 && currentTime.get(Calendar.SECOND) == 5)
					{
						updateTimeline.setCycleCount(Animation.INDEFINITE);
						updateTimeline.play();
						updaterFunc(currentTime);
						checkTime.stop();
					}
				}
				catch (Exception e)
				{
					LOG.error("## Exception occured:", e);
				}

			}));
			checkTime.setCycleCount(Animation.INDEFINITE);
			checkTime.play();
		}
		catch (Exception e)
		{
			LOG.error("## Exception occured:", e);
		}
	}

	/**
	 * This method removes any active event handlers
	 */
	public void removeHandlers()
	{
		try
		{
			for (Map.Entry<String, GridPane> entry : textboxGridMap.entrySet())
			{
				if (entry.getKey().equalsIgnoreCase(PTMNTR_GRID_NAME))
					entry.getValue().removeEventHandler(MouseEvent.MOUSE_PRESSED, ptmntrGridClickHandler);
				else if (entry.getKey().equalsIgnoreCase(ANES_GRID_NAME))
					entry.getValue().removeEventHandler(MouseEvent.MOUSE_PRESSED, anesGridClickHandler);
				else if (entry.getKey().equalsIgnoreCase(OUTPUT_GRID_NAME))
					entry.getValue().removeEventHandler(MouseEvent.MOUSE_PRESSED, outputGridClickHandler);
				else if (entry.getKey().equalsIgnoreCase(FLUID_GRID_NAME))
					entry.getValue().removeEventHandler(MouseEvent.MOUSE_PRESSED, fluidGridClickHandler);
				else if (entry.getKey().equalsIgnoreCase(MED_GRID_NAME))
					entry.getValue().removeEventHandler(MouseEvent.MOUSE_PRESSED, medGridClickHandler);
			}

			for (Map.Entry<GridPane, EventHandler<MouseEvent>> entry : testsEventHandlersMap.entrySet())
			{
				entry.getKey().removeEventHandler(MouseEvent.MOUSE_PRESSED, entry.getValue());
			}

			if (addNewGridBtn != null)
				addNewGridBtn.removeEventHandler(MouseEvent.MOUSE_PRESSED, addNewGridBtnHandler);
			if (medAddButton != null)
				medAddButton.removeEventHandler(ActionEvent.ACTION, medMenuItemAddHandler);
			if (anesHistData != null)
				anesHistData.removeEventHandler(MouseEvent.MOUSE_PRESSED, anesHistDataHandler);
			if (ptMntrHistData != null)
				ptMntrHistData.removeEventHandler(MouseEvent.MOUSE_PRESSED, ptMntrHistDataHandler);
			if (ptmntrMenuItemEnter != null)
				ptmntrMenuItemEnter.removeEventHandler(ActionEvent.ACTION, ptmntrMenuItemEnterHandler);
			if (anesMenuItemEnter != null)
				anesMenuItemEnter.removeEventHandler(ActionEvent.ACTION, anesMenuItemEnterHandler);
			if (medMenuItemAdd != null)
				medMenuItemAdd.removeEventHandler(ActionEvent.ACTION, medMenuItemAddHandler);
			if (medMenuItemBolus != null)
				medMenuItemBolus.removeEventHandler(ActionEvent.ACTION, medMenuItemBolusHandler);
			if (medMenuItemStart != null)
				medMenuItemStart.removeEventHandler(ActionEvent.ACTION, medMenuItemStartHandler);
			if (medMenuItemStop != null)
				medMenuItemStop.removeEventHandler(ActionEvent.ACTION, medMenuItemStopHandler);
			if (fluidMenuItemAdd != null)
				fluidMenuItemAdd.removeEventHandler(ActionEvent.ACTION, fluidMenuItemAddHandler);
			if (fluidMenuItemStart != null)
				fluidMenuItemStart.removeEventHandler(ActionEvent.ACTION, fluidMenuItemStartHandler);
			if (fluidMenuItemStop != null)
				fluidMenuItemStop.removeEventHandler(ActionEvent.ACTION, fluidMenuItemStopHandler);
			if (outputMenuItemEnter != null)
				outputMenuItemEnter.removeEventHandler(ActionEvent.ACTION, outputMenuItemEnterHandler);
			if (testMenuItemEnter != null)
				testMenuItemEnter.removeEventHandler(ActionEvent.ACTION, testMenuItemEnterHandler);
		}
		catch (Exception e)
		{
			LOG.error("## Exception occured:", e);

		}

	}

	/**
	 * This method calculates width for graph component
	 *
	 * @return calculated width
	 */
	private double calcGraphWidth()
	{
		return TIME_PIXELS_GAP * (TOTAL_TIME_MARKINGS - 1);
	}

	/**
	 * This method adds a TabPane to centerPane
	 */
	/*public void addBaseTabPane()
	{
		HBox base = new HBox();
		base.setPrefSize(centerPane.getPrefWidth(), centerPane.getPrefHeight() * 0.98);
		base.setAlignment(Pos.CENTER);

		baseTabPane = new TabPane();
		baseTabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
		baseTabPane.setPrefSize(base.getPrefWidth() * 0.99, base.getPrefHeight());

		for (int i = 0; i < NO_OF_TABS; i++)
		{
			Tab tab = new Tab();
			tab.setText(tabNames[i]);

			if (tabNames[i].toLowerCase().contains("anesthesia"))
			{
				anesthesiaTabBody = new VBox();
				anesthesiaTabBody.setId("anesthesia");
				anesthesiaTabBody.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
				tab.setContent(anesthesiaTabBody);
			}
			else if (tabNames[i].toLowerCase().contains("s5"))
			{
				s5TabBody = new VBox();
				s5TabBody.setId("s5ptmonitor");
				s5TabBody.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
				tab.setContent(s5TabBody);
			}
			else if (tabNames[i].toLowerCase().contains("med"))
			{
				medTabBody = new VBox();
				medTabBody.setId("medication");
				medTabBody.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
				tab.setContent(medTabBody);
			}
			else if (tabNames[i].toLowerCase().contains("tabularview"))
			{
				tablularBody= new VBox();
				tablularBody.setId("tabularView");
				tablularBody.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
				tab.setContent(tablularBody);
			}

			baseTabPane.getTabs().add(tab);
		}

		currentTab = baseTabPane.getSelectionModel().getSelectedItem().getText();
		addTabPaneListener(baseTabPane);

		base.getChildren().add(baseTabPane);
		centerPane.getChildren().add(base);
	}*/

	/**
	 * This method updates patient monitor and anesthesia grids with values from
	 * devices(if attached any) & medication and fluid grids with arrows marks
	 * for active medicine/fluid. Also save device data in database after 1
	 * minute interval
	 *
	 * @param currentTime current time value
	 */
	private void updaterFunc(Calendar currentTime)
	{
		highlightCurrentTimeCell(currentTime);
		if (updateTimelineCounter == 0 || updateTimelineCounter == 5)
		{
			currentTime.set(Calendar.SECOND, 0);
			currentTime.set(Calendar.MILLISECOND, 0);

			// ---Add new column to all grids if currenttime reaches lasttimeLbl
			Calendar secondLastTimeVal = Calendar.getInstance();
			/*secondLastTimeVal.setTime(gridLastTimeStamp.getTime());
			secondLastTimeVal.add(Calendar.MINUTE, -(SystemConfiguration.getInstance().getMinsGap()));
			secondLastTimeVal.set(Calendar.SECOND, 0);
			secondLastTimeVal.set(Calendar.MILLISECOND, 0);*/
			secondLastTimeVal.setTime(headerTimeLblsList.get(headerTimeLblsList.size()-2).getTime());

			if(currentTime.getTimeInMillis()-secondLastTimeVal.getTimeInMillis()>=0)
			{
		        addNewColumn();
	        }

			//---update cells with '>>' symbol for for started fluids
			if (fluidstartedMap != null)
			if (!fluidstartedMap.isEmpty())
			{
				addArrowSymbolToCell(currentTime, fluidstartedMap, FLUID_GRID_NAME, fluidPrevCellsFilledList);
			}

			//---update cells with '>>' symbol for medications with started infusions
			if (infusionStartedMap != null)
			if (!infusionStartedMap.isEmpty())
			{
				addArrowSymbolToCell(currentTime, infusionStartedMap, MED_GRID_NAME, medPrevCellsFilledList);
			}


			// ---Fill PTMonitor and Anesthesia grids with parameter values
			/*for (Map.Entry<String, Map<Calendar, String[]>> entry : deviceParamValMap.entrySet())
			{
				for (Map.Entry<Calendar, String[]> subEntry : entry.getValue().entrySet())
				{
					Calendar savedTime = Calendar.getInstance();
					savedTime.set(Calendar.HOUR_OF_DAY, Integer.valueOf(subEntry.getKey().split(":")[0]));
					savedTime.set(Calendar.MINUTE, Integer.valueOf(subEntry.getKey().split(":")[1]));
					savedTime.set(Calendar.SECOND, Integer.valueOf(subEntry.getKey().split(":")[2]));
					savedTime.set(Calendar.MILLISECOND, 0);
					subEntry.getKey().set(Calendar.MILLISECOND, 0);

					if ((currentTime.getTimeInMillis() - subEntry.getKey().getTimeInMillis())/ 1000 <= 30)
					{
						fillParamValInCells(currentTime, subEntry.getValue(), entry.getKey());
						//saveDeviceData(currentTime, subEntry.getValue(), entry.getKey());
					}

				}

			}*/

			updateTimelineCounter = 0;

		}

		for (Map.Entry<String, Map<Calendar, String[]>> entry : deviceParamValMap.entrySet())
		{
			for (Map.Entry<Calendar, String[]> subEntry : entry.getValue().entrySet())
			{
				subEntry.getKey().set(Calendar.MILLISECOND, 0);

				if ((currentTime.getTimeInMillis() - subEntry.getKey().getTimeInMillis()) / 1000 <= 30)
				{
					if (updateTimelineCounter == 0 || updateTimelineCounter == 5)
						fillParamValInCells(currentTime, subEntry.getValue(), entry.getKey());

					saveDeviceData(currentTime, subEntry.getValue(), entry.getKey());
				}

			}

		}
	}

	
	private void updaterFunc1(Calendar currentTime)
	{
		highlightCurrentTimeCell(currentTime);
		
		{
			//currentTime.set(Calendar.SECOND, 0);
			currentTime.set(Calendar.MILLISECOND, 0);

			// ---Add new column to all grids if currenttime reaches lasttimeLbl
			Calendar secondLastTimeVal = Calendar.getInstance();
			/*secondLastTimeVal.setTime(gridLastTimeStamp.getTime());
			secondLastTimeVal.add(Calendar.MINUTE, -(SystemConfiguration.getInstance().getMinsGap()));
			secondLastTimeVal.set(Calendar.SECOND, 0);
			secondLastTimeVal.set(Calendar.MILLISECOND, 0);*/
			secondLastTimeVal.setTime(headerTimeLblsList.get(headerTimeLblsList.size()-2).getTime());

			if(currentTime.getTimeInMillis()-secondLastTimeVal.getTimeInMillis()>=0)
			{
		        addNewColumn();
	        }

			//---update cells with '>>' symbol for for started fluids
			if (fluidstartedMap != null)
			if (!fluidstartedMap.isEmpty())
			{
				addArrowSymbolToCell(currentTime, fluidstartedMap, FLUID_GRID_NAME, fluidPrevCellsFilledList);
			}

			//---update cells with '>>' symbol for medications with started infusions
			if (infusionStartedMap != null)
			if (!infusionStartedMap.isEmpty())
			{
				addArrowSymbolToCell(currentTime, infusionStartedMap, MED_GRID_NAME, medPrevCellsFilledList);
				calculateTotalDosage();
			}


			// ---Fill PTMonitor and Anesthesia grids with parameter values
			/*for (Map.Entry<String, Map<Calendar, String[]>> entry : deviceParamValMap.entrySet())
			{
				for (Map.Entry<Calendar, String[]> subEntry : entry.getValue().entrySet())
				{
					Calendar savedTime = Calendar.getInstance();
					savedTime.set(Calendar.HOUR_OF_DAY, Integer.valueOf(subEntry.getKey().split(":")[0]));
					savedTime.set(Calendar.MINUTE, Integer.valueOf(subEntry.getKey().split(":")[1]));
					savedTime.set(Calendar.SECOND, Integer.valueOf(subEntry.getKey().split(":")[2]));
					savedTime.set(Calendar.MILLISECOND, 0);
					subEntry.getKey().set(Calendar.MILLISECOND, 0);

					if ((currentTime.getTimeInMillis() - subEntry.getKey().getTimeInMillis())/ 1000 <= 30)
					{
						fillParamValInCells(currentTime, subEntry.getValue(), entry.getKey());
						//saveDeviceData(currentTime, subEntry.getValue(), entry.getKey());
					}

				}

			}*/

			

		}
		
		if(currentTime.getTime().getSeconds() <30) {
			currentTime.set(Calendar.SECOND, 0);
		}
		
		else{
			
			currentTime.set(Calendar.SECOND, 30);
		};
		updateTimelineCounter = 0;
		for (Map.Entry<String, Map<Calendar, String[]>> entry : deviceParamValMap.entrySet())
		{
			for (Map.Entry<Calendar, String[]> subEntry : entry.getValue().entrySet())
			{
				subEntry.getKey().set(Calendar.MILLISECOND, 0);

				//if ((currentTime.getTimeInMillis() - subEntry.getKey().getTimeInMillis()) / 1000 <= 30)
				{
					
					fillParamValInCells(currentTime, subEntry.getValue(), entry.getKey());

					saveDeviceData(currentTime, subEntry.getValue(), entry.getKey());
				}

			}

		}
	}

	
	private void calculateTotalDosage() {
		// TODO Auto-generated method stub
		Calendar currentime = Calendar.getInstance();
		currentime.set(Calendar.MILLISECOND, 0);
		currentime.set(Calendar.SECOND, 0);
		for(Entry<String, Calendar> entry : infusionStartedMap.entrySet())
		{
			IntraopMedicationlog medicationLog=	medicineRateMap.get(entry.getKey());
			String total = calculateLiveDosage(medicationLog, currentime.getTime());
			if(infusionStartedMap.get(entry.getKey()) == null) {
				totalLblsMap.get(MED_GRID_NAME).get(entry.getKey()).setText(total + " (Stopped)");
				if(ControllerMediator.getInstance().getGridHistoricalViewController() != null)
				ControllerMediator.getInstance().getGridHistoricalViewController().getTotalLblsMap().get(MED_GRID_NAME).get(entry.getKey()).setText(total + " (Stopped)");

			}
			else {
			totalLblsMap.get(MED_GRID_NAME).get(entry.getKey()).setText(total + " (" + medicineTotalRateMap.get(entry.getKey())+")");
			if(ControllerMediator.getInstance().getGridHistoricalViewController() != null)
			ControllerMediator.getInstance().getGridHistoricalViewController().getTotalLblsMap().get(MED_GRID_NAME).get(entry.getKey()).setText(total + " (" + medicineTotalRateMap.get(entry.getKey())+")");
			}
			//totalsListMap.get(MED_GRID_NAME).add(entry.getValue());
		}
		
	}
	
	
	
	private String calculateLiveDosage(IntraopMedicationlog medicationLog,Date currentTime) {
		float dosage =0.0f;
		
		if(medsFromInfPump.contains(medicationLog.getMedicationName())) {
			if(medicationLog.getStartVol() !=null && medicationLog.getEndVol() !=null)
			dosage=((medicationLog.getEndVol()-medicationLog.getStartVol())*(Float.parseFloat(medicationLog.getConcentration())/1000));
		}
		else{
			 dosage = 	StopInfusionController.calculateInfusedDose(medicationLog.getStartTime(), currentTime, medicationLog.getRateOfInfusion(), medicationLog.getConcentration());

		if(medicationLog.getMedicationDosage() - dosage <=0 ) {
			medicationLog.setInfuseDosage(medicationLog.getMedicationDosage());
			dosage = medicationLog.getMedicationDosage();
			medicationLog.setEndTime(currentTime);
			Calendar c1 =Calendar.getInstance();
			c1.setTime(currentTime);
			if(ControllerMediator.getInstance().getGridHistoricalViewController() != null)
			ControllerMediator.getInstance().getGridHistoricalViewController().
			fillMedicationGridCell("infusion stop",
					medicationLog.getMedicationName(),c1,
	                MedicationInfusionController.calculateUnit((dosage)*1000), "");
			
			fillMedicationGridCell("infusion stop",
					medicationLog.getMedicationName(),c1,
	                MedicationInfusionController.calculateUnit((dosage)*1000), "");
			infusionStartedMap.remove(medicationLog.getMedicationName());
			try {
				
				medicationLogDao.saveMediactions(medicationLog, UserSession.getInstance().getUserWithrRolesForUserAuthentication().getUserID());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				LOG.error("## Exception occured:", e);
			}

		}
		}
	return 	MedicationInfusionController.calculateUnit((Float.parseFloat(totalsValuesMap.get(medicationLog.getMedicationName()).split(" ")[0]) + dosage)*1000 );
		

		
	}
	/**
	 * This method add arrow mark(>>>>>) to a particular cell of the specified
	 * grid
	 *
	 * @param currentTime current time value
	 * @param map map containing data regarding when a particular fluid/med was started
	 * @param gridName grid where cells need to be updated
	 * @param prevCellsFilledList list of cells already marked with arrow mark
	 */
	private void addArrowSymbolToCell(Calendar currentTime, Map<String, Calendar> map, String gridName,
	        List<String> prevCellsFilledList)
	{
		int x = 0, y = 0;
		for(Map.Entry<String, Calendar> entry:map.entrySet() )
		{
			Calendar infStartTime = Calendar.getInstance();
			/*infStartTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(entry.getValue().split(":")[0]));
			infStartTime.set(Calendar.MINUTE, Integer.parseInt(entry.getValue().split(":")[1]));
			infStartTime.set(Calendar.SECOND, 0);
			infStartTime.set(Calendar.MILLISECOND, 0);*/
			infStartTime.setTime(entry.getValue().getTime());

			if (prevCellsFilledList.contains(entry.getKey()))
			{
				currentTime.add(Calendar.MINUTE, -2);
				if (currentTime.getTimeInMillis() - infStartTime.getTimeInMillis() > 0)
				{
					/*infStartTime.set(Calendar.HOUR_OF_DAY, currentTime.get(Calendar.HOUR_OF_DAY));
					infStartTime.set(Calendar.MINUTE, currentTime.get(Calendar.MINUTE));*/
					infStartTime.setTime(currentTime.getTime());

				}
				currentTime.add(Calendar.MINUTE, 2);
			}
			infStartTime.set(Calendar.SECOND, 0);
			while (currentTime.getTimeInMillis() - infStartTime.getTimeInMillis() > 0)
			{
				x = 0;
				y = 0;
				
				
				x = paramListMap.get(gridName).indexOf(entry.getKey());
				y = headerTimeLblsList.indexOf(infStartTime);
				if (x >= 0 && y >= 0)
				{
					Label txt = getTextboxFromList(x, y, textboxListMap.get(gridName));
					if(txt.getText().contains("g")){
						
					}
					else if ((!(txt.getText().startsWith("(") && txt.getText().endsWith(")"))) || txt.getText().isEmpty())
					{
						txt.setText(">>>>>");
						txt.setStyle(txt.getStyle() + ";-fx-background-color:#60be98;");

						if (!prevCellsFilledList.contains(entry.getKey()))
						{
							prevCellsFilledList.add(entry.getKey());
						}
					}
				}
				infStartTime.add(Calendar.MINUTE, 1);
			}



		}
	}

	/**
	 * This method restarts running animation for active medicines (If
	 * application gets closed accidently)
	 *
	 * @param medsList list containing active medicine names
	 */
	private void restartMeds(List<IntraopMedicationlog> medsList)
	{
		if (PatientSession.getInstance().getPatient() != null)
				patient = PatientSession.getInstance().getPatient();
		
		if (medsList != null)
		for (IntraopMedicationlog obj : medsList)
		{
			if (obj.getStartTime() != null && obj.getEndTime() == null && obj.getBolusInfusion().equalsIgnoreCase("infusion") )
			{	medicineRateMap.put(obj.getMedicationName(), obj);
			
			Float infVal = Float.parseFloat(obj.getConcentration())
					* Float.parseFloat(obj.getRateOfInfusion().split(" ")[0]);
			infVal = (infVal) / patient.getWeight();
			infVal = (infVal)/60;
			
			medicineTotalRateMap.put( obj.getMedicationName(),MedicationInfusionController.calculateUnit(infVal));
			}
			if (!infusionStartedMap.containsKey(obj.getMedicationName()))
			if (obj.getStartTime() != null && obj.getEndTime() == null && obj.getBolusInfusion().equalsIgnoreCase("infusion") )
			{
				int x = -1;
				x = paramListMap.get(MED_GRID_NAME).indexOf(obj.getMedicationName());

				if (x >= 0)
				{
					Label lbl = getTextboxFromList(x, 0, textboxListMap.get(MED_GRID_NAME));
					lbl.setText(">>>>>");
					lbl.setStyle("-fx-background-color:#60be98;-fx-border-color:silver;-fx-font-size:12");
				}

				Calendar cal=Calendar.getInstance();
				cal.setTime(obj.getStartTime());
				infusionStartedMap.put(obj.getMedicationName(), cal);
			}
		}
	}

	/**
	 * This method restarts running animation for active fluids (If application
	 * gets closed accidently)
	 *
	 * @param fluidsList  list containing active fluid names
	 */
	private void restartFluids(List<Fluidvalue> fluidsList)
	{
		if (fluidsList != null)
		for (Fluidvalue obj : fluidsList)
		{
			if (!fluidstartedMap.containsKey(obj.getFluidName()))
			if (obj.getStartTime() != null && obj.getFinishTime() == null)
			{
				int x = -1;
				x = paramListMap.get(FLUID_GRID_NAME).indexOf(obj.getFluidName());

				if (x >= 0)
				{
					Label lbl = getTextboxFromList(x, 0, textboxListMap.get(FLUID_GRID_NAME));
					lbl.setText(">>>>>");
					lbl.setStyle("-fx-background-color:#60be98;-fx-border-color:silver;-fx-font-size:12");
				}
				Calendar cal=Calendar.getInstance();
				cal.setTime(obj.getStartTime());
				fluidstartedMap.put(obj.getFluidName(), cal);
			}
		}
	}

	/**
	 * This method creates dummy grid view for different sections
	 * (Medications,Tests, Fluids etc)
	 *
	 * @param vbox vbox where different sections are created
	 */
	public void createTableView(VBox vbox)
	{
		try
		{
			vbox.setPrefSize(centerPane.getPrefWidth(), centerPane.getPrefHeight());
			vbox.setStyle("-fx-border-color:silver");
			createTimeLblLists();

			ScrollPane baseScroller = new ScrollPane();
			baseScroller.setPrefWidth(vbox.getPrefWidth());
			VBox baseScrollVBox = new VBox();
			baseScrollVBox.setPadding(new Insets(0, 0, 0, baseScroller.getPrefWidth() * 0.01));
			baseScrollVBox.setPrefWidth(baseScroller.getPrefWidth() * 0.98);
			baseScrollVBox.setSpacing(5);
			baseScroller.setContent(baseScrollVBox);

			columnPrefWidth = ((baseScrollVBox.getPrefWidth() * 0.8) / (headerTimeLblsList.size()));
			// addS5TitledPane(baseScrollVBox);
			// addAnesthesiaTitledPane(baseScrollVBox);

			GetMedAndFluidsService getMedAndFluidsService = GetMedAndFluidsService
			        .getInstance(PatientSession.getInstance().getPatientCase().getCaseId());
			getMedAndFluidsService.restart();

			getMedAndFluidsServiceSuccessHandler = new EventHandler<WorkerStateEvent>()
			{
				@Override
				public void handle(WorkerStateEvent event)
				{

					try
					{
						historyMedList = getMedAndFluidsService.getMediactionsList();
						historyFluidList = getMedAndFluidsService.getFluidsList();

						createPtMntrGrid(baseScrollVBox);
						createAnesGrid(baseScrollVBox);
						createMedicationGrid(baseScrollVBox);
						createTestsGrid(baseScrollVBox);
						createFluidsGrid(baseScrollVBox);
						createOutputsGrid(baseScrollVBox);
						// createEventsGrid(baseScrollVBox);

						baseScrollVBox.getStylesheets().add(this.getClass().getResource("/styles/newStyles.css").toString());
						vbox.getChildren().addAll(baseScroller);

						getMedAndFluidsService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
						        getMedAndFluidsServiceSuccessHandler);
						getMedAndFluidsService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
						        getMedAndFluidsServiceFailedHandler);
					}
					catch (Exception e)
					{
						e.printStackTrace();
						LOG.error("## Exception occured:", e);
					}
				}
			};
			getMedAndFluidsService.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
			        getMedAndFluidsServiceSuccessHandler);

			getMedAndFluidsServiceFailedHandler = new EventHandler<WorkerStateEvent>()
			{
				@Override
				public void handle(WorkerStateEvent event)
				{

					try
					{
						Main.getInstance().getUtility().showNotification("Error", "Error",
						        getMedAndFluidsService.getException().getMessage());

						getMedAndFluidsService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
						        getMedAndFluidsServiceSuccessHandler);
						getMedAndFluidsService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
						        getMedAndFluidsServiceFailedHandler);
					}
					catch (Exception e)
					{
						LOG.error("## Exception occured:", e);
					}
				}
			};
			getMedAndFluidsService.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
			        getMedAndFluidsServiceFailedHandler);
		}
		catch (Exception e)
		{
			LOG.error("## Exception occured:", e);

		}
	}

	/**
	 * This method creates a list containing time labels to be used for timeline
	 * on top of every grid
	 */
	private void createTimeLblLists()
	{
		Calendar cal = Calendar.getInstance();
		String time = sdf.format(cal.getTime());
		int mins = Integer.parseInt(time.split(":")[1]);
		cal.set(Calendar.MINUTE, (mins - (mins % 5)));
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		for (int i = 0; i < NO_OF_GRID_COLUMNS; i++)
		{
			Calendar timeLbl = Calendar.getInstance();
			timeLbl.setTime(cal.getTime());

			// String gridLastTimeLbl = sdf.format(cal.getTime());
			headerTimeLblsList.add(timeLbl);

			if (i == NO_OF_GRID_COLUMNS - 1)
				gridLastTimeStamp.setTime(cal.getTime());

			cal.add(Calendar.MINUTE, MINS_GAP);
		}
	}

	/**
	 * This method compares time corresponding to clicked grid cell with current
	 * time
	 *
	 * @return true if current time is greater
	 */
	private boolean compareClickedcellTime()
	{
		boolean result = false;
		/*Calendar selectedTime = Calendar.getInstance();
		selectedTime.set(Calendar.HOUR_OF_DAY, Integer.valueOf(clickedGridCellTime.split(":")[0]));
		selectedTime.set(Calendar.MINUTE, Integer.valueOf(clickedGridCellTime.split(":")[1]));
		selectedTime.set(Calendar.SECOND, 0);
		selectedTime.set(Calendar.MILLISECOND, 0);*/

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		if (clickedGridCellTime.getTimeInMillis() - cal.getTimeInMillis() <= 0)
			result = true;

		return result;
	}

	/**
	 * This method creates grid view for PatientMonitor Section
	 *
	 * @param vbox vbox where grid need to be created
	 */
	private void createPtMntrGrid(VBox vbox)
	{
		createDynamicGrid(PTMNTR_GRID_NAME, false, vbox);
		paramListMap.put(PTMNTR_GRID_NAME, new ArrayList<String>(Arrays.asList(ptMntrTableRowHeader)));
		addLegendsToGrid(paramGridMap.get(PTMNTR_GRID_NAME), rowPrefHeight, ptMntrTableRowHeader);
		addGridHeader(headerTimeLblsList, textboxGridMap.get(PTMNTR_GRID_NAME), columnPrefWidth, rowPrefHeight);
		addGridRows(paramListMap.get(PTMNTR_GRID_NAME), headerTimeLblsList, textboxGridMap.get(PTMNTR_GRID_NAME),
		        textboxListMap.get(PTMNTR_GRID_NAME), columnPrefWidth, rowPrefHeight);
		addPtMntrContextMenu();

		ptmntrGridClickHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
			public void handle(javafx.scene.input.MouseEvent event)
			{
				try
				{
					for (Node node : textboxGridMap.get(PTMNTR_GRID_NAME).getChildren())
					{
						if (node.getBoundsInParent().contains(event.getX(), event.getY()))
						{
							Label lbl = (Label) node;
							if (lbl.getId() != null)
							{
								clickedGridCellTime = headerTimeLblsList.get(Integer.valueOf(lbl.getId().split(",")[1]));

								ptmntrContextMenu.show(lbl, event.getSceneX(), event.getSceneY());
								if (!compareClickedcellTime())
								{
									ptmntrContextMenu.getItems().get(0).setDisable(true);
								}
								else
								{
									ptmntrContextMenu.getItems().get(0).setDisable(false);
								}
							}
							break;
						}
					}
				}
				catch (Exception e)
				{
					LOG.error("## Exception occured:", e);
				}
			}
		};
		textboxGridMap.get(PTMNTR_GRID_NAME).addEventHandler(MouseEvent.MOUSE_PRESSED, ptmntrGridClickHandler);

	}

	/**
	 * This method adds contextmenu over PatientMonitor grid cells
	 */
	private void addPtMntrContextMenu()
	{
		ptmntrContextMenu = new ContextMenu();
		ptmntrContextMenu.setPrefSize(100, 100);

		ptmntrMenuItemEnter = new MenuItem("Enter Values");
		ptmntrMenuItemEnter.setStyle("-fx-font-size:12;");

		ptmntrContextMenu.getItems().addAll(ptmntrMenuItemEnter);

		ptmntrMenuItemEnterHandler = new EventHandler<ActionEvent>()
		{
			public void handle(ActionEvent event)
			{

				try
				{
					ControllerMediator.getInstance().getMainController().bindPopupFxml(FxmlView.PATIENT_MONITOR);
					ControllerMediator.getInstance().getPatientMonitorController().setSelectedTime(clickedGridCellTime);

					/*Calendar cal=Calendar.getInstance();
					//cal.set(Calendar.HOUR_OF_DAY, Integer.valueOf(headerTimeLblsList.get(Integer.valueOf(textbox.getId().split(",")[1])).split(":")[0]));
					//cal.set(Calendar.MINUTE, Integer.valueOf(headerTimeLblsList.get(Integer.valueOf(textbox.getId().split(",")[1])).split(":")[1]));
					cal.set(Calendar.HOUR_OF_DAY, Integer.valueOf(clickedGridCellTime.split(":")[0]));
					cal.set(Calendar.MINUTE, Integer.valueOf(clickedGridCellTime.split(":")[1]));
					cal.set(Calendar.SECOND	, 0);
					cal.set(Calendar.MILLISECOND, 0);*/

					GetPatientMonitorDataTimeCaseService ptmntrSearchService = GetPatientMonitorDataTimeCaseService.getInstance(
					        PatientSession.getInstance().getPatientCase().getCaseId(), clickedGridCellTime.getTime());
					ptmntrSearchService.restart();


					//---success
					ptmntrTimeCaseServiceSuccessHandler = new EventHandler<WorkerStateEvent>()
					{
						@Override
						public void handle(WorkerStateEvent event)
						{
							try
							{
								PatientMonitorData obj =  ptmntrSearchService.getPatientMonitorDataTimeStampCaseID();
								if (obj != null)
								{
									ControllerMediator.getInstance().getPatientMonitorController().autopopulateTextboxes(obj);
									//ControllerMediator.getInstance().getPatientMonitorController().getLblTime().setText(headerTimeLblsList.get(Integer.valueOf(textbox.getId().split(",")[1])));
									ControllerMediator.getInstance().getPatientMonitorController().getLblTime().setText(sdf.format(clickedGridCellTime.getTime()));
									ControllerMediator.getInstance().getPatientMonitorController().setPatientMonitorData(obj);
								}


								ptmntrSearchService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
										ptmntrTimeCaseServiceSuccessHandler);
								ptmntrSearchService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
										ptmntrTimeCaseServiceFailedHandler);
							}
							catch (Exception e)
							{
								LOG.error("## Exception occured:", e);

							}
						}
					};
					ptmntrSearchService.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
							ptmntrTimeCaseServiceSuccessHandler);


					//---failure
					ptmntrTimeCaseServiceFailedHandler = new EventHandler<WorkerStateEvent>()
					{
						@Override
						public void handle(WorkerStateEvent event)
						{
							try
							{
								Main.getInstance().getUtility().showNotification("Error", "Error",
										ptmntrSearchService.getException().getMessage());

								ptmntrSearchService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
										ptmntrTimeCaseServiceSuccessHandler);
								ptmntrSearchService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
										ptmntrTimeCaseServiceFailedHandler);
							}
							catch (Exception e)
							{
								LOG.error("## Exception occured:", e);

							}
						}
					};
					ptmntrSearchService.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
							ptmntrTimeCaseServiceFailedHandler);
				}
				catch (Exception e)
				{
					LOG.error("## Exception occured:", e);

				}


			}
		};
		ptmntrMenuItemEnter.addEventHandler(ActionEvent.ACTION,ptmntrMenuItemEnterHandler);

	}

	/**
	 * This method creates grid view for Anesthesia Section
	 *
	 * @param vbox  vbox where grid need to be created
	 */
	private void createAnesGrid(VBox vbox)
	{
		createDynamicGrid(ANES_GRID_NAME, false, vbox);
		paramListMap.put(ANES_GRID_NAME, new ArrayList<String>(Arrays.asList(anesTableRowHeader)));
		addLegendsToGrid(paramGridMap.get(ANES_GRID_NAME), rowPrefHeight, anesTableRowHeader);
		addGridHeader(headerTimeLblsList, textboxGridMap.get(ANES_GRID_NAME), columnPrefWidth, rowPrefHeight);
		addGridRows(paramListMap.get(ANES_GRID_NAME), headerTimeLblsList, textboxGridMap.get(ANES_GRID_NAME),
		        textboxListMap.get(ANES_GRID_NAME), columnPrefWidth, rowPrefHeight);
		addAnesContextMenu();

		anesGridClickHandler = new EventHandler<javafx.scene.input.MouseEvent>()
		{
			public void handle(javafx.scene.input.MouseEvent event)
			{
				try
				{
					for (Node node : textboxGridMap.get(ANES_GRID_NAME).getChildren())
					{
						if (node.getBoundsInParent().contains(event.getX(), event.getY()))
						{
							Label lbl = (Label) node;
							if (lbl.getId() != null)
							{
								clickedGridCellTime = headerTimeLblsList.get(Integer.valueOf(lbl.getId().split(",")[1]));

								anesContextMenu.show(lbl, event.getSceneX(), event.getSceneY());
								if (!compareClickedcellTime())
									anesContextMenu.getItems().get(0).setDisable(true);
								else
									anesContextMenu.getItems().get(0).setDisable(false);
							}
							break;
						}
					}
				}
				catch (Exception e)
				{
					LOG.error("## Exception occured:", e);

				}
			}
		};
		textboxGridMap.get(ANES_GRID_NAME).addEventHandler(MouseEvent.MOUSE_PRESSED, anesGridClickHandler);
	}

	/**
	 * This method adds contextmenu over Anesthesia grid cells
	 */
	private void addAnesContextMenu()
	{
		anesContextMenu = new ContextMenu();
		anesContextMenu.setPrefSize(100, 100);

		anesMenuItemEnter = new MenuItem("Enter Values");
		anesMenuItemEnter.setStyle("-fx-font-size:12;");

		anesContextMenu.getItems().addAll(anesMenuItemEnter);
		//textbox.setContextMenu(contextMenu);

		anesMenuItemEnterHandler = new EventHandler<ActionEvent>()
		{
			public void handle(ActionEvent event)
			{

				try
				{
					ControllerMediator.getInstance().getMainController().bindPopupFxml(FxmlView.DASHBOARD_ANESTHESIA);
					ControllerMediator.getInstance().getDashboardAnaestheiaController().setSelectedTime(clickedGridCellTime);

					/*Calendar cal = Calendar.getInstance();
					cal.set(Calendar.HOUR_OF_DAY, Integer.valueOf(clickedGridCellTime.split(":")[0]));
					cal.set(Calendar.MINUTE, Integer.valueOf(clickedGridCellTime.split(":")[1]));
					cal.set(Calendar.SECOND, 0);*/

					GetAnesthesiaMachineDataTimeCaseService anesSearchService = GetAnesthesiaMachineDataTimeCaseService
					        .getInstance(PatientSession.getInstance().getPatientCase().getCaseId(), clickedGridCellTime.getTime());
					anesSearchService.restart();

					anesTimeCaseServiceSuccessHandler = new EventHandler<WorkerStateEvent>()
					{
						@Override
						public void handle(WorkerStateEvent event)
						{
							try
							{
								AnethesiaMachineData obj = anesSearchService.getAnethesiaMachineDataTimeCase();
								if (obj != null)
								{
									ControllerMediator.getInstance().getDashboardAnaestheiaController().autopopulateTextboxes(obj);
									ControllerMediator.getInstance().getDashboardAnaestheiaController().getLblTime().setText(sdf.format(clickedGridCellTime.getTime()));
									ControllerMediator.getInstance().getDashboardAnaestheiaController().setAnethesiaDataObj(obj);
								}

								anesSearchService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
								        anesTimeCaseServiceSuccessHandler);
								anesSearchService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
								        anesTimeCaseServiceFailedHandler);
							}
							catch (Exception e)
							{
								LOG.error("## Exception occured:", e);

							}
						}
					};
					anesSearchService.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
							anesTimeCaseServiceSuccessHandler);

					anesTimeCaseServiceFailedHandler = new EventHandler<WorkerStateEvent>()
					{
						@Override
						public void handle(WorkerStateEvent event)
						{
							try
							{
								Main.getInstance().getUtility().showNotification("Error", "Error",
								        anesSearchService.getException().getMessage());

								anesSearchService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
								        anesTimeCaseServiceSuccessHandler);
								anesSearchService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
								        anesTimeCaseServiceFailedHandler);
							}
							catch (Exception e)
							{
								LOG.error("## Exception occured:", e);

							}
						}
					};
					anesSearchService.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
							anesTimeCaseServiceFailedHandler);
				}
				catch (Exception e)
				{
					LOG.error("## Exception occured:", e);

				}

			}
		};
		anesMenuItemEnter.addEventHandler(ActionEvent.ACTION, anesMenuItemEnterHandler);

	}

	/**
	 * This method creates grid view for Medication Section
	 *
	 * @param baseVBox  vbox where grid need to be created
	 */
	private void createMedicationGrid(VBox baseVBox)
	{
		createDynamicGrid(MED_GRID_NAME, true, baseVBox);
		paramListMap.put(MED_GRID_NAME, new ArrayList<String>());


		for (Fdamedications obj : MasterDataSession.getInstance().getFavMedDataMap().get("FavMedication"))
		{
			if (obj != null)
				paramListMap.get(MED_GRID_NAME).add(obj.getMedicationsName());
		}

		if (historyMedList != null)
		{
			for (IntraopMedicationlog obj : historyMedList)
			{
				if (!paramListMap.get(MED_GRID_NAME).contains(obj.getMedicationName()))
				{
					paramListMap.get(MED_GRID_NAME).add(obj.getMedicationName());
				}
			}
		}

		if (!paramListMap.get(MED_GRID_NAME).isEmpty())
		{
			String[] medNamesArr = paramListMap.get(MED_GRID_NAME).toArray(new String[paramListMap.get(MED_GRID_NAME).size()]);
			addLegendsToGrid(paramGridMap.get(MED_GRID_NAME), rowPrefHeight, medNamesArr);

			totalLblsMap.put(MED_GRID_NAME, new HashMap<String, Label>());
			createTotalsGrid(totalsGridMap.get(MED_GRID_NAME), rowPrefHeight, paramListMap.get(MED_GRID_NAME),totalLblsMap.get(MED_GRID_NAME));

			lastMedRow = paramListMap.get(MED_GRID_NAME).size() + 1;
			addGridHeader(headerTimeLblsList, textboxGridMap.get(MED_GRID_NAME), columnPrefWidth, rowPrefHeight);
			addGridRows(paramListMap.get(MED_GRID_NAME), headerTimeLblsList, textboxGridMap.get(MED_GRID_NAME),
			        textboxListMap.get(MED_GRID_NAME), columnPrefWidth, rowPrefHeight);
			addMedContextMenu();

			textboxGridMap.get(MED_GRID_NAME).setPrefHeight((paramListMap.get(MED_GRID_NAME).size() + 2) * rowPrefHeight);
			paramGridMap.get(MED_GRID_NAME).setPrefHeight(textboxGridMap.get(MED_GRID_NAME).getPrefHeight());
			totalsGridMap.get(MED_GRID_NAME).setPrefHeight(textboxGridMap.get(MED_GRID_NAME).getPrefHeight());

			if (PatientSession.getInstance().getPatient() != null
			        && PatientSession.getInstance().getPatientCase() != null)
			{
				if (PatientSession.getInstance().getPatient().getPatientId() != null
				        && PatientSession.getInstance().getPatientCase().getCaseId() != null)
				{
					getTotalMedicationVolumeList(paramListMap.get(MED_GRID_NAME));
				}

			}
		}

		
		restartMeds(historyMedList);
		
		
		highlightCurrentTimeCell(Calendar.getInstance());

		medGridClickHandler = new EventHandler<javafx.scene.input.MouseEvent>()
		{
			public void handle(javafx.scene.input.MouseEvent event)
			{
				try
				{
					for (Node node : textboxGridMap.get(MED_GRID_NAME).getChildren())
					{
						if (node.getBoundsInParent().contains(event.getX(), event.getY()))
						{
							Label lbl = (Label) node;
							if (lbl.getId() != null)
							{
								clickedGridCellTime = headerTimeLblsList.get(Integer.valueOf(lbl.getId().split(",")[1]));
								clickedGridCellParam = paramListMap.get(MED_GRID_NAME).get(Integer.valueOf(lbl.getId().split(",")[0]));

								medContextMenu.show(lbl, event.getSceneX(), event.getSceneY());
								if (compareClickedcellTime())
								{
									medContextMenu.getItems().get(0).setDisable(false);
									medContextMenu.getItems().get(1).setDisable(false);
									medContextMenu.getItems().get(2).setDisable(false);
									boolean flag = false, flag1 = false, flag2 = false;;
									if (ControllerMediator.getInstance().getMainController().getDrawTabbedCenter()
									        .getInfusionStartedMap().containsKey(clickedGridCellParam))
									{
										medContextMenu.getItems().get(2).setDisable(true);
										medContextMenu.getItems().get(3).setDisable(false);

										/*Calendar selectedTime=Calendar.getInstance();
										selectedTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(clickedGridCellTime.split(":")[0]));
										selectedTime.set(Calendar.MINUTE, Integer.parseInt(clickedGridCellTime.split(":")[1]));
										selectedTime.set(Calendar.SECOND,0);
										selectedTime.set(Calendar.MILLISECOND,0);*/

										/*String infStartedTime=ControllerMediadmin	ator.getInstance().getMainController().getDrawTabbedCenter().getInfusionStartedMap().get(clickedGridCellParam);
										Calendar infTime=Calendar.getInstance();
										infTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(infStartedTime.split(":")[0]));
										infTime.set(Calendar.MINUTE, Integer.parseInt(infStartedTime.split(":")[1]));
										infTime.set(Calendar.SECOND,0);
										infTime.set(Calendar.MILLISECOND,0);*/

										Calendar infTime=Calendar.getInstance();
										infTime.setTime(ControllerMediator.getInstance().getMainController().getDrawTabbedCenter().getInfusionStartedMap().get(clickedGridCellParam).getTime());
										
										flag =true;
										if(clickedGridCellTime.getTimeInMillis()-infTime.getTimeInMillis()>0)
										{	
											medContextMenu.getItems().get(3).setDisable(false);
										medContextMenu.getItems().get(4).setDisable(false);
									}
										else
											{
											
											int mingap = 	SystemConfiguration.getInstance().getMinsGap();
											
											Calendar currentime = Calendar.getInstance();
												currentime.set(Calendar.MILLISECOND, 0);
												currentime.set(Calendar.SECOND, 0);
												long diffTime = currentime.getTimeInMillis() - clickedGridCellTime.getTimeInMillis();
												long gapTime = mingap*60*1000;
												if( mingap >1 &&  diffTime<gapTime && diffTime >0 ) {
													
													medContextMenu.getItems().get(3).setDisable(false);
													medContextMenu.getItems().get(4).setDisable(false);
												}
												else
												{
													flag2=true;
											medContextMenu.getItems().get(3).setDisable(true);
											medContextMenu.getItems().get(4).setDisable(true);
												}
											}

										
											
										
									}
									else
									{
										medContextMenu.getItems().get(2).setDisable(false);
										medContextMenu.getItems().get(3).setDisable(true);
										medContextMenu.getItems().get(4).setDisable(true);
										
										int mingap = 	SystemConfiguration.getInstance().getMinsGap();
											Calendar realTime ;
													Calendar currentime = Calendar.getInstance();
														currentime.set(Calendar.MILLISECOND, 0);
														currentime.set(Calendar.SECOND, 0);
														long diffTime = currentime.getTimeInMillis() - clickedGridCellTime.getTimeInMillis();
														long gapTime = mingap*60*1000;
														if( mingap >1 &&  diffTime<gapTime && diffTime >0 ) {
															
															realTime= currentime;
														}
														else
															realTime = clickedGridCellTime;
										if(!medicationLogDao.getStartFlag(
										        clickedGridCellParam,
										        PatientSession.getInstance().getPatientCase().getCaseId(),
										        PatientSession.getInstance().getPatient().getPatientId(), realTime.getTime(), "Infusion",null))
										{ medContextMenu.getItems().get(2).setDisable(true);
										flag1 =true;
										}
									}

									if (!lbl.getText().isEmpty())
									{
//										if (lbl.getText().startsWith("(") && lbl.getText().endsWith(")"))
//										{
//											medContextMenu.getItems().get(2).setDisable(true);
//											medContextMenu.getItems().get(3).setDisable(true);
//										}
//										else 
										if (lbl.getText().startsWith(">") && lbl.getText().endsWith(">"))
										{
											//needs to code here for backDate scenarios in start time
											medicationLogDao =  MedicationLogDaoImpl.getInstance();
											if(medicationLogDao.getStartFlag(
											        clickedGridCellParam,
											        PatientSession.getInstance().getPatientCase().getCaseId(),
											        PatientSession.getInstance().getPatient().getPatientId(), clickedGridCellTime.getTime(), "Infusion",null))
												 medContextMenu.getItems().get(2).setDisable(false);
											else
											 medContextMenu.getItems().get(2).setDisable(true);
											//medContextMenu.getItems().get(4).setDisable(true);

											/*medContextMenu.getItems().get(0).setDisable(false);
											medContextMenu.getItems().get(1).setDisable(false);
											medContextMenu.getItems().get(2).setDisable(false);
											medContextMenu.getItems().get(3).setDisable(false);*/
										}
										else if (! lbl.getText().contains("g"))
										{
											medContextMenu.getItems().get(0).setDisable(false);
											medContextMenu.getItems().get(1).setDisable(false);
											medContextMenu.getItems().get(2).setDisable(false);
											medContextMenu.getItems().get(3).setDisable(true);
										//	medContextMenu.getItems().get(4).setDisable(true);
										}
										else if (lbl.getText().contains("g"))
										{
											medContextMenu.getItems().get(0).setDisable(false);
											medContextMenu.getItems().get(1).setDisable(false);
											medContextMenu.getItems().get(2).setDisable(true);
											if(flag ) {
												medContextMenu.getItems().get(3).setDisable(false);
												if(flag2)
												medContextMenu.getItems().get(3).setDisable(true);
											}
											else
											{	if(!flag1)
											medContextMenu.getItems().get(2).setDisable(false);
											medContextMenu.getItems().get(3).setDisable(true);
											
											}
										}
										/*else
										{
											medContextMenu.getItems().get(1).setDisable(true);
											medContextMenu.getItems().get(2).setDisable(true);
											medContextMenu.getItems().get(3).setDisable(true);
										}*/
									}
									if(medsFromInfPump.contains(clickedGridCellParam)) {
										medContextMenu.getItems().get(1).setDisable(false);
										//medContextMenu.getItems().get(1).setDisable(true);
										medContextMenu.getItems().get(2).setDisable(true);
										medContextMenu.getItems().get(3).setDisable(true);
										medContextMenu.getItems().get(4).setDisable(true);
									}
								}
								else
								{
									medContextMenu.getItems().get(0).setDisable(true);
									medContextMenu.getItems().get(1).setDisable(true);
									medContextMenu.getItems().get(2).setDisable(true);
									medContextMenu.getItems().get(3).setDisable(true);
									medContextMenu.getItems().get(4).setDisable(true);
								}
							}
							break;
						}
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
					LOG.error("## Exception occured:", e);

				}
			}
		};
		textboxGridMap.get(MED_GRID_NAME).addEventHandler(MouseEvent.MOUSE_PRESSED, medGridClickHandler);
	}

	/**
	 * This method adds contextmenu over Medication grid cells
	 */
	private void addMedContextMenu()
	{
		medContextMenu = new ContextMenu();
		medContextMenu.setPrefSize(100, 100);

		medMenuItemAdd = new MenuItem("Add Medicine");
		medMenuItemAdd.setStyle("-fx-font-size:12;");
		medMenuItemAdd.setVisible(false);
		medMenuItemBolus = new MenuItem("Bolus");
		medMenuItemBolus.setStyle("-fx-font-size:12");
		medMenuItemStart = new MenuItem("Start Infusion");
		medMenuItemStart.setStyle("-fx-font-size:12");
		medMenuItemStop = new MenuItem("Stop Infusion");
		medMenuItemStop.setStyle("-fx-font-size:12");
		medMenuItemStop.setDisable(true);

		medMenuItemEdit = new MenuItem("Edit Infusion");
		medMenuItemEdit.setStyle("-fx-font-size:12");
	//	medMenuItemEdit.setDisable(true);

		medContextMenu.getItems().addAll(medMenuItemAdd, medMenuItemBolus, medMenuItemStart, medMenuItemStop, medMenuItemEdit);

		// ---Add Medicine Button
		medMenuItemAddHandler = new EventHandler<ActionEvent>()
		{
			public void handle(ActionEvent event)
			{
				try
				{
					ControllerMediator.getInstance().getMainController().bindPopupFxml(FxmlView.SEARCH_MEDICINE);
				}
				catch (Exception e)
				{
					LOG.error("## Exception occured:", e);

				}
			}
		};
		medMenuItemAdd.addEventHandler(ActionEvent.ACTION, medMenuItemAddHandler);


		// ---Bolus Button
		medMenuItemBolusHandler = new EventHandler<ActionEvent>()
		{
			public void handle(ActionEvent event)
			{
				try
				{
					ControllerMediator.getInstance().getMainController().bindPopupFxml(FxmlView.MEDICATION_INFUSION);
					ControllerMediator.getInstance().getMedicationInfusionController().setSelectedTime(clickedGridCellTime);
					ControllerMediator.getInstance().getMedicationInfusionController().getLblSelectetMethod()
					        .setText("Bolus");
					ControllerMediator.getInstance().getMedicationInfusionController().checkMedType();
					ControllerMediator.getInstance().getMedicationInfusionController().getLblTime()
					        .setText(sdf.format(clickedGridCellTime.getTime()));
					ControllerMediator.getInstance().getMedicationInfusionController().getLblMedicineName()
					        .setText(clickedGridCellParam);

					getIntraopMedicationlogService = GetIntraopMedicationlogService.getInstance(clickedGridCellParam,
					        PatientSession.getInstance().getPatientCase().getCaseId(),
					        PatientSession.getInstance().getPatient().getPatientId(), clickedGridCellTime.getTime(), "Bolus");
					getIntraopMedicationlogService.restart();

					getMedlogServiceSuccessHandler = new EventHandler<WorkerStateEvent>()
					{
						@Override
						public void handle(WorkerStateEvent event)
						{
							IntraopMedicationlog obj = getIntraopMedicationlogService.getIntraopMedicationlog();
							if (obj == null)
								obj = new IntraopMedicationlog();
							ControllerMediator.getInstance().getMedicationInfusionController().setMedicationLog(obj);
							ControllerMediator.getInstance().getMedicationInfusionController().prePopulateTextfields();

							getIntraopMedicationlogService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
							        getMedlogServiceSuccessHandler);
							getIntraopMedicationlogService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
							        getMedlogServiceFailedHandler);
						}
					};
					getIntraopMedicationlogService.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
					        getMedlogServiceSuccessHandler);

					getMedlogServiceFailedHandler = new EventHandler<WorkerStateEvent>()
					{
						@Override
						public void handle(WorkerStateEvent event)
						{
							Main.getInstance().getUtility().showNotification("Error", "Error",
							        getIntraopMedicationlogService.getException().getMessage());

							getIntraopMedicationlogService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
							        getMedlogServiceSuccessHandler);
							getIntraopMedicationlogService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
							        getMedlogServiceFailedHandler);
						}
					};
					getIntraopMedicationlogService.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
					        getMedlogServiceFailedHandler);
				}
				catch (Exception e)
				{
					LOG.error("## Exception occured:", e);

				}
			}
		};
		medMenuItemBolus.addEventHandler(ActionEvent.ACTION, medMenuItemBolusHandler);



		// ---Start Infusion Button
		medMenuItemStartHandler = new EventHandler<ActionEvent>()
		{
			public void handle(ActionEvent event)
			{
				try
				{
					if (medsFromInfPump.contains(clickedGridCellParam))
					{
						Main.getInstance().getUtility().errorDialogue("Error", "Please start medication from Infusion Pump!");
					}
					else
					{
						
						
						int mingap = 	SystemConfiguration.getInstance().getMinsGap();
							Calendar realTime ;
									Calendar currentime = Calendar.getInstance();
										currentime.set(Calendar.MILLISECOND, 0);
										currentime.set(Calendar.SECOND, 0);
										long diffTime = currentime.getTimeInMillis() - clickedGridCellTime.getTimeInMillis();
										long gapTime = mingap*60*1000;
										if( mingap >1 &&  diffTime<gapTime && diffTime >0 ) {
											
											realTime= currentime;
										}
										else
											realTime = clickedGridCellTime;
										
											
						ControllerMediator.getInstance().getMainController().bindPopupFxml(FxmlView.MEDICATION_INFUSION);
						ControllerMediator.getInstance().getMedicationInfusionController().setSelectedTime(realTime);
						ControllerMediator.getInstance().getMedicationInfusionController().getLblSelectetMethod().setText("Infusion");
						ControllerMediator.getInstance().getMedicationInfusionController().setEditWindow(false);
						ControllerMediator.getInstance().getMedicationInfusionController().checkMedType();
						ControllerMediator.getInstance().getMedicationInfusionController().getLblTime().setText(sdf.format(realTime.getTime()));
						ControllerMediator.getInstance().getMedicationInfusionController().getLblMedicineName().setText(clickedGridCellParam);
						ControllerMediator.getInstance().getMedicationInfusionController().setMedicationLog(new IntraopMedicationlog());
						ControllerMediator.getInstance().getMedicationInfusionController().prePopulateTextfields();
						
						
					}
				}
				catch (Exception e)
				{
					LOG.error("## Exception occured:", e);

				}

			}
		};
		medMenuItemStart.addEventHandler(ActionEvent.ACTION, medMenuItemStartHandler);


		// ---Stop Infusion Button
		medMenuItemStopHandler = new EventHandler<ActionEvent>()
		{
			public void handle(ActionEvent event)
			{

				try
				{
					if (medsFromInfPump.contains(clickedGridCellParam))
					{
						Main.getInstance().getUtility().errorDialogue("Error", "Please stop medication from Infusion Pump!");
					}
					else
					{
						
							
					int mingap = 	SystemConfiguration.getInstance().getMinsGap();
						Calendar realTime ;
								Calendar currentime = Calendar.getInstance();
									currentime.set(Calendar.MILLISECOND, 0);
									currentime.set(Calendar.SECOND, 0);
									long diffTime = currentime.getTimeInMillis() - clickedGridCellTime.getTimeInMillis();
									long gapTime = mingap*60*1000;
									if( mingap >1 &&  diffTime<gapTime && diffTime >0 ) {
										
										realTime= currentime;
									}
									else
										realTime = clickedGridCellTime;
						ControllerMediator.getInstance().getMainController().bindPopupFxml(FxmlView.STOP_INFUSION);
						//String startTime = infusionStartedMap.get(clickedGridCellParam);
						//String endTime = clickedGridCellTime;

						ControllerMediator.getInstance().getStopInfusionController().getLblMedicineName()
						        .setText(clickedGridCellParam);
						ControllerMediator.getInstance().getStopInfusionController().getLblStartTime().setText(sdf.format(infusionStartedMap.get(clickedGridCellParam).getTime()));
						ControllerMediator.getInstance().getStopInfusionController().getLblStopTime().setText(sdf.format(realTime.getTime()));

						stopInfusion(realTime);

					}
				}
				catch (Exception e)
				{
					LOG.error("## Exception occured:", e);

				}


			}

			private void stopInfusion(Calendar realTime) {
				Calendar startCal = Calendar.getInstance();
				startCal=infusionStartedMap.get(clickedGridCellParam);

				ControllerMediator.getInstance().getStopInfusionController().setEndTime(realTime);

				getIntraopMedicationlogService = GetIntraopMedicationlogService.getInstance(
				        clickedGridCellParam,
				        PatientSession.getInstance().getPatientCase().getCaseId(),
				        PatientSession.getInstance().getPatient().getPatientId(), startCal.getTime(), "Infusion");
				getIntraopMedicationlogService.restart();

				getMedlogServiceSuccessHandler = new EventHandler<WorkerStateEvent>()
				{
					@Override
					public void handle(WorkerStateEvent event)
					{
						IntraopMedicationlog obj = getIntraopMedicationlogService.getIntraopMedicationlog();
						ControllerMediator.getInstance().getStopInfusionController().setMedicationLog(obj);
						ControllerMediator.getInstance().getStopInfusionController().setInfuseDosenRateText();

						getIntraopMedicationlogService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
								getMedlogServiceSuccessHandler);
						getIntraopMedicationlogService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
								getMedlogServiceFailedHandler);
					}
				};
				getIntraopMedicationlogService.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
						getMedlogServiceSuccessHandler);

				getMedlogServiceFailedHandler = new EventHandler<WorkerStateEvent>()
				{
					@Override
					public void handle(WorkerStateEvent event)
					{
						Main.getInstance().getUtility().showNotification("Error", "Error",
						        getIntraopMedicationlogService.getException().getMessage());

						getIntraopMedicationlogService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
								getMedlogServiceSuccessHandler);
						getIntraopMedicationlogService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
								getMedlogServiceFailedHandler);
					}
				};
				getIntraopMedicationlogService.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
						getMedlogServiceFailedHandler);
			}
		};
		medMenuItemStop.addEventHandler(ActionEvent.ACTION, medMenuItemStopHandler);

		// ---Edit Infusion Button
		medMenuItemEditHandler = new EventHandler<ActionEvent>()
		{
			public void handle(ActionEvent event)
			{
				try
				{
					
					
					ControllerMediator.getInstance().getMainController().bindPopupFxml(FxmlView.MEDICATION_INFUSION);
									

					int mingap = 	SystemConfiguration.getInstance().getMinsGap();
						Calendar realTime ;
								Calendar currentime = Calendar.getInstance();
									currentime.set(Calendar.MILLISECOND, 0);
									currentime.set(Calendar.SECOND, 0);
									long diffTime = currentime.getTimeInMillis() - clickedGridCellTime.getTimeInMillis();
									long gapTime = mingap*60*1000;
									if( mingap >1 &&  diffTime<gapTime && diffTime >0 ) {
										
										realTime= currentime;
									}
									else
										realTime = clickedGridCellTime;
									
					ControllerMediator.getInstance().getMedicationInfusionController().setSelectedTime(realTime);
					ControllerMediator.getInstance().getMedicationInfusionController().setEditWindow(true);
					ControllerMediator.getInstance().getMedicationInfusionController().getLblSelectetMethod().setText("Infusion");
					ControllerMediator.getInstance().getMedicationInfusionController().checkMedType();
					ControllerMediator.getInstance().getMedicationInfusionController().getLblTime().setText(sdf.format(realTime.getTime()));
					ControllerMediator.getInstance().getMedicationInfusionController().getLblMedicineName().setText(clickedGridCellParam);
					
				 	Calendar startCal = Calendar.getInstance();
					startCal=infusionStartedMap.get(clickedGridCellParam);

					getIntraopMedicationlogService = GetIntraopMedicationlogService.getInstance(
					        clickedGridCellParam,
					        PatientSession.getInstance().getPatientCase().getCaseId(),
					        PatientSession.getInstance().getPatient().getPatientId(), startCal.getTime(), "Infusion");
					getIntraopMedicationlogService.restart();

					getMedlogServiceSuccessHandler = new EventHandler<WorkerStateEvent>()
					{
						@Override
						public void handle(WorkerStateEvent event)
						{
							
						
							IntraopMedicationlog obj=getIntraopMedicationlogService.getIntraopMedicationlog();
							
							ControllerMediator.getInstance().getMedicationInfusionController().setMedicationLog(obj);
							ControllerMediator.getInstance().getMedicationInfusionController().prePopulateTextfields();

							Calendar startCal1 = Calendar.getInstance();
							startCal1 =infusionStartedMap.get(clickedGridCellParam);
							//ControllerMediator.getInstance().getMedicationInfusionController().get
							//startCal.getTime();
							//ControllerMediator.getInstance().getStopInfusionController().setEndTime(clickedGridCellTime);

							getIntraopMedicationlogService = GetIntraopMedicationlogService.getInstance(
							        clickedGridCellParam,
							        PatientSession.getInstance().getPatientCase().getCaseId(),
							        PatientSession.getInstance().getPatient().getPatientId(), startCal1.getTime(), "Infusion");
							getIntraopMedicationlogService.restart();
							getIntraopMedicationlogService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
									getMedlogServiceSuccessHandler);
							getIntraopMedicationlogService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
									getMedlogServiceFailedHandler);
						}
					};
					getIntraopMedicationlogService.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
							getMedlogServiceSuccessHandler);

					getMedlogServiceFailedHandler = new EventHandler<WorkerStateEvent>()
					{
						@Override
						public void handle(WorkerStateEvent event)
						{
							Main.getInstance().getUtility().showNotification("Error", "Error",
							        getIntraopMedicationlogService.getException().getMessage());

							getIntraopMedicationlogService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
									getMedlogServiceSuccessHandler);
							getIntraopMedicationlogService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
									getMedlogServiceFailedHandler);
						}
					};
					getIntraopMedicationlogService.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
							getMedlogServiceFailedHandler);

				}
				catch (Exception e)
				{
					LOG.error("## Exception occured:", e);

				}

			}
		};
		medMenuItemEdit.addEventHandler(ActionEvent.ACTION, medMenuItemEditHandler);

	}

	public void highlightCurrentTimeCell(Calendar cal)
	{
		List<Node> childrens = textboxGridMap.get(MED_GRID_NAME).getChildren();
		boolean found = false;
		Label previousLbl = new Label();

		for (int i = 0; i < lastGridColumn; i++)
		{
			for (Node node : childrens)
			{
				if (i > 0)
				{
					if (GridPane.getRowIndex(node) == 0 && GridPane.getColumnIndex(node) == i - 1)
						previousLbl = (Label) node;
				}

				if (GridPane.getRowIndex(node) == 0 && GridPane.getColumnIndex(node) == i)
				{
					Label lbl = (Label) node;
					if (sdf.format(cal.getTime()).equalsIgnoreCase(lbl.getText()))
					{
						lbl.setStyle(lbl.getStyle() + ";-fx-background-color: #53C86F");
						previousLbl.setStyle(previousLbl.getStyle() + ";-fx-background-color: #005d87");
						found = true;
					}
					break;
				}
			}

			if (found)
				break;
		}

	}

	/**
	 * This method creates grid view for Fluid Section
	 *
	 * @param vbox  vbox where grid need to be created
	 */
	private void createFluidsGrid(VBox vbox)
	{
		createDynamicGrid(FLUID_GRID_NAME, true, vbox);
		paramListMap.put(FLUID_GRID_NAME, new ArrayList<String>());

		for (Fluid obj : MasterDataSession.getInstance().getFavFluidDataMap().get("FavFluids"))
		{
			if (obj != null)
			{
				fluidIdsMap.put(obj.getFluidName(), obj.getFluidId());
				paramListMap.get(FLUID_GRID_NAME).add(obj.getFluidName());
			}
		}

		if (historyFluidList != null)
		{
			for (Fluidvalue obj : historyFluidList)
			{
				if (!paramListMap.get(FLUID_GRID_NAME).contains(obj.getFluidName()))
				{
					paramListMap.get(FLUID_GRID_NAME).add(obj.getFluidName());
				}
			}
		}

		if(!paramListMap.get(FLUID_GRID_NAME).isEmpty())
		{
			String[] fluidNamesArr=paramListMap.get(FLUID_GRID_NAME).toArray(new String[paramListMap.get(FLUID_GRID_NAME).size()]);
			addLegendsToGrid(paramGridMap.get(FLUID_GRID_NAME), rowPrefHeight, fluidNamesArr);

			totalLblsMap.put(FLUID_GRID_NAME,new HashMap<String, Label>());
			createTotalsGrid(totalsGridMap.get(FLUID_GRID_NAME), rowPrefHeight, paramListMap.get(FLUID_GRID_NAME), totalLblsMap.get(FLUID_GRID_NAME));
			lastFluidRow = paramListMap.get(FLUID_GRID_NAME).size() + 1;
			addGridHeader(headerTimeLblsList, textboxGridMap.get(FLUID_GRID_NAME), columnPrefWidth, rowPrefHeight);
			addGridRows(paramListMap.get(FLUID_GRID_NAME), headerTimeLblsList, textboxGridMap.get(FLUID_GRID_NAME),
			        textboxListMap.get(FLUID_GRID_NAME), columnPrefWidth, rowPrefHeight);

			addFluidContextMenu();

			textboxGridMap.get(FLUID_GRID_NAME).setPrefHeight((paramListMap.get(FLUID_GRID_NAME).size() + 2) * rowPrefHeight);
			paramGridMap.get(FLUID_GRID_NAME).setPrefHeight(textboxGridMap.get(FLUID_GRID_NAME).getPrefHeight());
			totalsGridMap.get(FLUID_GRID_NAME).setPrefHeight(textboxGridMap.get(FLUID_GRID_NAME).getPrefHeight());

			if (PatientSession.getInstance().getPatient() != null&& PatientSession.getInstance().getPatientCase() != null)
			{
				if(PatientSession.getInstance().getPatient().getPatientId() != null && PatientSession.getInstance().getPatientCase().getCaseId() != null)
				{
					getFluidTotalsListService(paramListMap.get(FLUID_GRID_NAME));
				}

			}
		}

		restartFluids(historyFluidList);

		fluidGridClickHandler = new EventHandler<javafx.scene.input.MouseEvent>()
		{
			public void handle(javafx.scene.input.MouseEvent event)
			{
				try
				{
					for (Node node : textboxGridMap.get(FLUID_GRID_NAME).getChildren())
					{
						if (node.getBoundsInParent().contains(event.getX(), event.getY()))
						{
							Label lbl = (Label) node;
							if (lbl.getId() != null)
							{
								clickedGridCellTime = headerTimeLblsList.get(Integer.valueOf(lbl.getId().split(",")[1]));
								clickedGridCellParam =paramListMap.get(FLUID_GRID_NAME).get(Integer.valueOf(lbl.getId().split(",")[0]));

								fluidContextMenu.show(lbl, event.getSceneX(), event.getSceneY());
								if (compareClickedcellTime())
								{
									fluidContextMenu.getItems().get(0).setDisable(false);
									fluidContextMenu.getItems().get(1).setDisable(false);

									if (ControllerMediator.getInstance().getMainController().getDrawTabbedCenter().getFluidstartedMap().containsKey(clickedGridCellParam))
									{
										fluidContextMenu.getItems().get(1).setDisable(true);
										//fluidContextMenu.getItems().get(2).setDisable(false);

										/*Calendar selectedTime=Calendar.getInstance();
										selectedTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(clickedGridCellTime.split(":")[0]));
										selectedTime.set(Calendar.MINUTE, Integer.parseInt(clickedGridCellTime.split(":")[1]));
										selectedTime.set(Calendar.SECOND,0);
										selectedTime.set(Calendar.MILLISECOND,0);*/

										/*String fldStartedTime=ControllerMediator.getInstance().getMainController().getDrawTabbedCenter().getFluidstartedMap().get(clickedGridCellParam);
										Calendar fldTime=Calendar.getInstance();
										fldTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(fldStartedTime.split(":")[0]));
										fldTime.set(Calendar.MINUTE, Integer.parseInt(fldStartedTime.split(":")[1]));
										fldTime.set(Calendar.SECOND,0);
										fldTime.set(Calendar.MILLISECOND,0);*/

										Calendar fldTime=Calendar.getInstance();
										fldTime.setTime(ControllerMediator.getInstance().getMainController().getDrawTabbedCenter().getFluidstartedMap().get(clickedGridCellParam).getTime());

										if(clickedGridCellTime.getTimeInMillis()-fldTime.getTimeInMillis()>0)
											fluidContextMenu.getItems().get(2).setDisable(false);
										else
											fluidContextMenu.getItems().get(2).setDisable(true);
									}
									else
									{
										fluidContextMenu.getItems().get(1).setDisable(false);
										fluidContextMenu.getItems().get(2).setDisable(true);
									}

									if (!lbl.getText().isEmpty())
									{
										if (lbl.getText().contains(">>>"))
											fluidContextMenu.getItems().get(1).setDisable(true);
									}

								}
								else
								{
									fluidContextMenu.getItems().get(0).setDisable(true);
									fluidContextMenu.getItems().get(1).setDisable(true);
									fluidContextMenu.getItems().get(2).setDisable(true);
								}
							}
							break;
						}
					}
				}
				catch (Exception e)
				{
					LOG.error("## Exception occured:", e);

				}
			}
		};
		textboxGridMap.get(FLUID_GRID_NAME).addEventHandler(MouseEvent.MOUSE_PRESSED, fluidGridClickHandler);

	}

	/**
	 * This method adds contextmenu over Fluid grid cells
	 */
	private void addFluidContextMenu()
	{
		fluidContextMenu = new ContextMenu();
		fluidContextMenu.setPrefSize(100, 100);

		fluidMenuItemAdd = new MenuItem("Add New Fluid");
		fluidMenuItemAdd.setStyle("-fx-font-size:12;");
		fluidMenuItemStart = new MenuItem("Start");
		fluidMenuItemStart.setStyle("-fx-font-size:12");
		fluidMenuItemStop = new MenuItem("Stop");
		fluidMenuItemStop.setStyle("-fx-font-size:12");
		fluidMenuItemStop.setDisable(true);

		fluidContextMenu.getItems().addAll(fluidMenuItemAdd, fluidMenuItemStart, fluidMenuItemStop);


		fluidMenuItemAddHandler = new EventHandler<ActionEvent>()
		{
			public void handle(ActionEvent event)
			{
				try
				{
					ControllerMediator.getInstance().getMainController().bindPopupFxml(FxmlView.DASHBOARD_FLUID_SEARCH);
				}
				catch (Exception e)
				{
					LOG.error("## Exception occured:", e);

				}
			}
		};
		fluidMenuItemAdd.addEventHandler(ActionEvent.ACTION, fluidMenuItemAddHandler);


		fluidMenuItemStartHandler = new EventHandler<ActionEvent>()
		{
			public void handle(ActionEvent event)
			{
				try
				{
					ControllerMediator.getInstance().getMainController().bindPopupFxml(FxmlView.DASHBOARD_FLUID_START);
					ControllerMediator.getInstance().getDashboardFluidController().setSelectedTime(clickedGridCellTime);
					ControllerMediator.getInstance().getDashboardFluidController().setIsStop(false);
					ControllerMediator.getInstance().getDashboardFluidController().checkStartOrStop();
					ControllerMediator.getInstance().getDashboardFluidController().getLblFluidName().setText(clickedGridCellParam);
					ControllerMediator.getInstance().getDashboardFluidController().getLblTime().setText(sdf.format(clickedGridCellTime.getTime()));
					ControllerMediator.getInstance().getDashboardFluidController().setSelectedFluidId(fluidIdsMap.get(clickedGridCellParam));
				}
				catch (Exception e)
				{
					LOG.error("## Exception occured:", e);

				}
			}
		};
		fluidMenuItemStart.addEventHandler(ActionEvent.ACTION, fluidMenuItemStartHandler);


		fluidMenuItemStopHandler = new EventHandler<ActionEvent>()
		{
			public void handle(ActionEvent event)
			{
				try
				{
					Calendar cal = Calendar.getInstance();
					cal = fluidstartedMap.get(clickedGridCellParam);

					SearchFluidValueService fluidSearchService = SearchFluidValueService.getInstance(
					        PatientSession.getInstance().getPatientCase().getCaseId(),
					        cal.getTime(),
					        clickedGridCellParam);
					fluidSearchService.restart();


					searchFluidServiceSuccessHandler = new EventHandler<WorkerStateEvent>()
					{
						@Override
						public void handle(WorkerStateEvent event)
						{
							try
							{
								ControllerMediator.getInstance().getMainController().bindPopupFxml(FxmlView.DASHBOARD_FLUID_START);
								ControllerMediator.getInstance().getDashboardFluidController().setSelectedTime(clickedGridCellTime);
								ControllerMediator.getInstance().getDashboardFluidController().setSelectedFluid(fluidSearchService.getSearchFluid());

								ControllerMediator.getInstance().getDashboardFluidController().setIsStop(true);
								ControllerMediator.getInstance().getDashboardFluidController().checkStartOrStop();
								ControllerMediator.getInstance().getDashboardFluidController().getLblFluidName().setText(clickedGridCellParam);
								ControllerMediator.getInstance().getDashboardFluidController().getLblTime().setText(sdf.format(clickedGridCellTime.getTime()));
								fluidSearchService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
										searchFluidServiceSuccessHandler);
								fluidSearchService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
										searchFluidServiceFailedHandler);
							}
							catch (Exception e)
							{
								LOG.error("## Exception occured:", e);

							}
						}
					};
					fluidSearchService.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
							searchFluidServiceSuccessHandler);

					searchFluidServiceFailedHandler = new EventHandler<WorkerStateEvent>()
					{
						@Override
						public void handle(WorkerStateEvent event)
						{
							try
							{
								Main.getInstance().getUtility().showNotification("Error", "Error",
										fluidSearchService.getException().getMessage());

								fluidSearchService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
										searchFluidServiceSuccessHandler);
								fluidSearchService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
										searchFluidServiceFailedHandler);
							}
							catch (Exception e)
							{
								LOG.error("## Exception occured:", e);

							}
						}
					};
					fluidSearchService.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
							searchFluidServiceFailedHandler);
				}
				catch (Exception e)
				{
					LOG.error("## Exception occured:", e);

				}

			}
		};
		fluidMenuItemStop.addEventHandler(ActionEvent.ACTION, fluidMenuItemStopHandler);

	}

	/**
	 * This method creates grid view for Output Section
	 *
	 * @param vbox  vbox where grid need to be created
	 */
	private void createOutputsGrid(VBox vbox)
	{
		createDynamicGrid(OUTPUT_GRID_NAME, true, vbox);
		paramListMap.put(OUTPUT_GRID_NAME, new ArrayList<String>(Arrays.asList(outputsTableRowHeader)));
		addLegendsToGrid(paramGridMap.get(OUTPUT_GRID_NAME), rowPrefHeight, outputsTableRowHeader);

		totalLblsMap.put(OUTPUT_GRID_NAME, new HashMap<String, Label>());
		createTotalsGrid(totalsGridMap.get(OUTPUT_GRID_NAME), rowPrefHeight, paramListMap.get(OUTPUT_GRID_NAME),
		        totalLblsMap.get(OUTPUT_GRID_NAME));
		addGridHeader(headerTimeLblsList, textboxGridMap.get(OUTPUT_GRID_NAME), columnPrefWidth, rowPrefHeight);
		addGridRows(paramListMap.get(OUTPUT_GRID_NAME), headerTimeLblsList, textboxGridMap.get(OUTPUT_GRID_NAME),
		        textboxListMap.get(OUTPUT_GRID_NAME), columnPrefWidth, rowPrefHeight);

		if (PatientSession.getInstance().getPatient() != null && PatientSession.getInstance().getPatientCase() != null)
		{
			if (PatientSession.getInstance().getPatient().getPatientId() != null
			        && PatientSession.getInstance().getPatientCase().getCaseId() != null)
			{
				getOutputTotalsListService(paramListMap.get(OUTPUT_GRID_NAME));
			}
		}

		addOutputContextMenu();


		textboxGridMap.get(OUTPUT_GRID_NAME).setPrefHeight((paramListMap.get(OUTPUT_GRID_NAME).size() + 2) * rowPrefHeight);
		paramGridMap.get(OUTPUT_GRID_NAME).setPrefHeight(textboxGridMap.get(OUTPUT_GRID_NAME).getPrefHeight());


		outputGridClickHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
			public void handle(javafx.scene.input.MouseEvent event)
			{
				try
				{
					for (Node node : textboxGridMap.get(OUTPUT_GRID_NAME).getChildren())
					{
						if (node.getBoundsInParent().contains(event.getX(), event.getY()))
						{
							Label lbl = (Label) node;
							if (lbl.getId() != null)
							{
								clickedGridCellTime = headerTimeLblsList.get(Integer.valueOf(lbl.getId().split(",")[1]));
								clickedGridCellParam = paramListMap.get(OUTPUT_GRID_NAME).get(Integer.valueOf(lbl.getId().split(",")[0]));

								outputContextMenu.show(lbl, event.getSceneX(), event.getSceneY());
								if (compareClickedcellTime())
									outputContextMenu.getItems().get(0).setDisable(false);
								else
									outputContextMenu.getItems().get(0).setDisable(true);
							}
							break;
						}
					}
				}
				catch (Exception e)
				{
					LOG.error("## Exception occured:", e);

				}
			}
		};
		textboxGridMap.get(OUTPUT_GRID_NAME).addEventHandler(MouseEvent.MOUSE_PRESSED, outputGridClickHandler);


	}

	/**
	 * This method adds contextmenu over Output grid cells
	 */
	private void addOutputContextMenu()
	{
		outputContextMenu = new ContextMenu();
		outputContextMenu.setPrefSize(100, 100);

		outputMenuItemEnter = new MenuItem("Enter value");
		outputMenuItemEnter.setStyle("-fx-font-size:12;");

		outputContextMenu.getItems().addAll(outputMenuItemEnter);

		outputMenuItemEnterHandler = new EventHandler<ActionEvent>()
		{
			public void handle(ActionEvent event)
			{

				try
				{
					ControllerMediator.getInstance().getMainController().bindPopupFxml(FxmlView.DASHBOARD_OUTPUT);
					ControllerMediator.getInstance().getDashboardOutputController().setSelectedTime(clickedGridCellTime);
					ControllerMediator.getInstance().getDashboardOutputController().getLblTime().setText(sdf.format(clickedGridCellTime.getTime()));
					ControllerMediator.getInstance().getDashboardOutputController().getLblOutputName().setText(clickedGridCellParam);

					SearchIntraOpOutputService searchOutputService = SearchIntraOpOutputService.getInstance(
					        PatientSession.getInstance().getPatientCase().getCaseId(), clickedGridCellTime.getTime(),
					        clickedGridCellParam);
					searchOutputService.restart();

					searchOutputServiceSuccessHandler = new EventHandler<WorkerStateEvent>()
					{
						@Override
						public void handle(WorkerStateEvent event)
						{
							try
							{
								IntraopOutput obj = searchOutputService.getIntraopOutputRecord();
								if (obj == null)
									obj = new IntraopOutput();
								else
									ControllerMediator.getInstance().getDashboardOutputController().getTxtVolume()
									        .setText(String.valueOf(obj.getVolume()));
								ControllerMediator.getInstance().getDashboardOutputController().setOutPutDetails(obj);

								searchOutputService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
										searchOutputServiceSuccessHandler);
								searchOutputService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
										searchOutputServiceFailedHandler);
							}
							catch (Exception e)
							{
								LOG.error("## Exception occured:", e);

							}
						}
					};
					searchOutputService.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
							searchOutputServiceSuccessHandler);

					searchOutputServiceFailedHandler = new EventHandler<WorkerStateEvent>()
					{
						@Override
						public void handle(WorkerStateEvent event)
						{
							try
							{
								Main.getInstance().getUtility().showNotification("Error", "Error",
								        searchOutputService.getException().getMessage());

								searchOutputService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
										searchOutputServiceSuccessHandler);
								searchOutputService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
										searchOutputServiceFailedHandler);
							}
							catch (Exception e)
							{
								LOG.error("## Exception occured:", e);

							}
						}
					};
					searchOutputService.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
							searchOutputServiceFailedHandler);
				}
				catch (Exception e)
				{
					LOG.error("## Exception occured:", e);

				}


			}
		};
		outputMenuItemEnter.addEventHandler(ActionEvent.ACTION, outputMenuItemEnterHandler);

	}

	/**
	 * This method creates grid view for Test Section
	 *
	 * @param vbox  vbox where grid need to be created
	 */
	private void createTestsGrid(VBox vbox)
	{
		testsTitledPanesHolder = new VBox();
		testsTitledPanesHolder.setPrefWidth(vbox.getPrefWidth());
		testsTitledPanesHolder.setSpacing(5);

		HBox newGridHBox = new HBox();
		newGridHBox.setPrefWidth(testsTitledPanesHolder.getPrefWidth());
		newGridHBox.setAlignment(Pos.CENTER_RIGHT);
		addNewGridBtn = new Button("Add New Test");
		addNewGridBtn.setFont(new Font(12));
		addNewGridBtn.setStyle(
		        "-fx-background-color:rgb(58.0, 163.0, 178.0);-fx-border-width:0px;-fx-text-fill:white;-fx-font-weight:bold ");
		addNewGridBtnHandler = new EventHandler<MouseEvent>()
		{
			public void handle(MouseEvent event)
			{
				try
				{
					ControllerMediator.getInstance().getMainController().bindPopupFxml(FxmlView.DASHBOARD_ADD_NEW_TEST);
				}
				catch (Exception e)
				{
					LOG.error("## Exception occured:", e);

				}
			}
		};
		addNewGridBtn.addEventHandler(MouseEvent.MOUSE_PRESSED, addNewGridBtnHandler);

		newGridHBox.getChildren().add(addNewGridBtn);
		testsTitledPanesHolder.getChildren().addAll(newGridHBox);

		TitledPane testsTitledPane = new TitledPane();
		testsTitledPane.setText("Tests");
		testsTitledPane.setContent(testsTitledPanesHolder);
		testsTitledPane.setExpanded(false);
		vbox.getChildren().addAll(testsTitledPane);

		createDynamicGrid(TEST1_GRID_NAME, false, testsTitledPanesHolder);
		paramListMap.put(TEST1_GRID_NAME, new ArrayList<String>());

		createDynamicGrid(TEST2_GRID_NAME, false, testsTitledPanesHolder);
		paramListMap.put(TEST2_GRID_NAME, new ArrayList<String>());

		createDynamicGrid(TEST3_GRID_NAME, false, testsTitledPanesHolder);
		paramListMap.put(TEST3_GRID_NAME, new ArrayList<String>());

		addTestContextMenu();
		getTestListandIDs();
	}

	/**
	 * This method adds contextmenu over Test grid cells
	 */
	private void addTestContextMenu()
	{
		testContextMenu = new ContextMenu();
		testContextMenu.setPrefSize(100, 100);

		testMenuItemEnter = new MenuItem("Enter Values");
		testMenuItemEnter.setStyle("-fx-font-size:12;");

		testContextMenu.getItems().addAll(testMenuItemEnter);

		testMenuItemEnterHandler = new EventHandler<ActionEvent>()
		{
			public void handle(ActionEvent event)
			{

				try
				{
					ControllerMediator.getInstance().getMainController().bindPopupFxml(FxmlView.DASHBOARD_TEST);
					ControllerMediator.getInstance().getDashboardTestController().setSelectedTime(clickedGridCellTime.getTime());
					ControllerMediator.getInstance().getDashboardTestController().getLblTime().setText(sdf.format(clickedGridCellTime.getTime()));
					ControllerMediator.getInstance().getDashboardTestController().getTxtTestName().setText(clickedTestGrid);

					GetInvestigationMapperAndValueService getTestsService = GetInvestigationMapperAndValueService.getInstance(
					        testIdsMap.get(clickedTestGrid.toLowerCase()), PatientSession.getInstance().getPatientCase().getCaseId(),
					        clickedGridCellTime.getTime());
					getTestsService.restart();


					investigationMapperAndValueServiceSuccessHandler = new EventHandler<WorkerStateEvent>()
					{
						@Override
						public void handle(WorkerStateEvent event)
						{
							try
							{
								InvestigationMapperAndValue mapperObj = getTestsService.getInvestigationMapperAndValue();
								ControllerMediator.getInstance().getDashboardTestController().setInvestigationMapperAndValue(mapperObj);

								for (Investigationstests obj : investigationTestsList)
								{
									if (obj.getTestName().equalsIgnoreCase(clickedTestGrid))
									{
										testObj = obj;
										break;
									}
								}

								ControllerMediator.getInstance().getDashboardTestController().setSelectedTestObj(testObj);
								ControllerMediator.getInstance().getDashboardTestController().getAllParam(testObj);
								// ControllerMediator.getInstance().getDashboardTestController().autoPopulateTextbox();

								getTestsService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
										investigationMapperAndValueServiceSuccessHandler);
								getTestsService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
										investigationMapperAndValueServiceFailedHandler);
							}
							catch (Exception e)
							{
								LOG.error("## Exception occured:", e);

							}
						}
					};
					getTestsService.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
							investigationMapperAndValueServiceSuccessHandler);

					investigationMapperAndValueServiceFailedHandler = new EventHandler<WorkerStateEvent>()
					{
						@Override
						public void handle(WorkerStateEvent event)
						{
							try
							{
								Main.getInstance().getUtility().showNotification("Error", "Error",
								        getTestsService.getException().getMessage());

								getTestsService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
										investigationMapperAndValueServiceSuccessHandler);
								getTestsService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
										investigationMapperAndValueServiceFailedHandler);
							}
							catch (Exception e)
							{
								LOG.error("## Exception occured:", e);

							}
						}
					};
					getTestsService.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
							investigationMapperAndValueServiceFailedHandler);
				}
				catch (Exception e)
				{
					LOG.error("## Exception occured:", e);

				}



			}
		};
		testMenuItemEnter.addEventHandler(ActionEvent.ACTION, testMenuItemEnterHandler);

	}

	/**
	 * This method creates a new grid view under Test section having name as
	 * gridName
	 *
	 * @param gridName name of the grid to be added
	 */
	public void addNewTestGrid(String gridName)
	{
		try
		{
			final String testName = gridName;
			//gridName = gridName.toLowerCase();
			createDynamicGrid(gridName, false, testsTitledPanesHolder);
			// getTestParamService(gridName, testIdsMap.get(gridName), "");


			String paramName = "";
			paramListMap.put(testName, new ArrayList<String>());

			for (Investigationparameterfields obj : MasterDataSession.getInstance().getTestParamDataMap().get("TestParam").get(gridName.toUpperCase()))
			{
				if (obj.getName() != null)
				{
					paramName = obj.getName();
					if (obj.getMeasuringUnit() != null)
					{
						if (!obj.getMeasuringUnit().getUnitSymbol().isEmpty())
							paramName = paramName + " (" + obj.getMeasuringUnit().getUnitSymbol() + ")";
					}
					else if (obj.getMeasuringUnitRatio() != null)
					{
						paramName = paramName + " (" + obj.getMeasuringUnitRatio().getNumeratorUnit().getUnitSymbol() + "/"
						        + obj.getMeasuringUnitRatio().getDenominatorUnit().getUnitSymbol() + ")";
					}

				}

				paramListMap.get(testName).add(paramName);
			}


			addLegendsToGrid(paramGridMap.get(testName), rowPrefHeight,paramListMap.get(testName).toArray(new String[paramListMap.get(testName).size()]));
			gridHeaderForNewTest(headerTimeLblsList, textboxGridMap.get(testName), columnPrefWidth, rowPrefHeight);
			gridRowsForNewTest(paramListMap.get(testName), headerTimeLblsList, textboxGridMap.get(testName),textboxListMap.get(testName), columnPrefWidth, rowPrefHeight);


			textboxGridMap.get(testName).setPrefHeight((paramListMap.get(testName).size() + 2) * rowPrefHeight);
			paramGridMap.get(testName).setPrefHeight(textboxGridMap.get(testName).getPrefHeight());

			EventHandler<MouseEvent> testGridClickHandler= new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent event)
				{
					try
					{
						for (Node node : textboxGridMap.get(testName).getChildren())
						{
							if (node.getBoundsInParent().contains(event.getX(), event.getY()))
							{
								Label lbl = (Label) node;
								if (lbl.getId() != null)
								{
									clickedGridCellTime = headerTimeLblsList.get(Integer.valueOf(lbl.getId().split(",")[1]));
									clickedTestGrid = testName;

									testContextMenu.show(lbl, event.getSceneX(), event.getSceneY());

									if (compareClickedcellTime())
										testContextMenu.getItems().get(0).setDisable(false);
									else
										testContextMenu.getItems().get(0).setDisable(true);
								}
								break;
							}
						}
					}
					catch (Exception e)
					{
						LOG.error("## Exception occured:", e);

					}
				}
			};
			textboxGridMap.get(testName).addEventHandler(MouseEvent.MOUSE_PRESSED, testGridClickHandler);

			testsEventHandlersMap.put(textboxGridMap.get(testName), testGridClickHandler);
		}
		catch (Exception e)
		{
			LOG.error("## Exception occured:", e);

		}

	}

	/**
	 * This method adds timeline to new grid view under Test section
	 *
	 * @param timeLblsList holds time labels
	 * @param grid newly added test grid
	 * @param colWidth column width
	 * @param rowHeight row height
	 */
	private void gridHeaderForNewTest(List<Calendar> timeLblsList, GridPane grid, double colWidth, double rowHeight)
	{
		for (int i = 0; i < timeLblsList.size(); i++)
		{
			if (i >= lastRemovedCol)
			{
				ColumnConstraints cc = new ColumnConstraints();
				cc.setMaxWidth(colWidth);
				grid.getColumnConstraints().add(cc);

				Label headerLbl = new Label(sdf.format(timeLblsList.get(i).getTime()));
				headerLbl.setAlignment(Pos.CENTER);
				headerLbl.setStyle("-fx-background-color: #005d87; -fx-text-fill: white;  -fx-border-color: silver;");

				headerLbl.setPrefSize(colWidth, rowHeight);
				grid.add(headerLbl, i, 0);

				lastGridColumn = i;
			}
		}
	}

	/**
	 * This method add rows to new grid view under Test section
	 *
	 * @param rowHeaderList list of specific test parameters
	 * @param timeLblsList holds time labels
	 * @param grid newly added test grid
	 * @param labelsList holds parameter labels
	 * @param colWidth column width
	 * @param rowHeight row height
	 */
	private void gridRowsForNewTest(List<String> rowHeaderList, List<Calendar> timeLblsList, GridPane grid,
	        List<Label> labelsList, double colWidth, double rowHeight)
	{
		for (int i = 0; i < rowHeaderList.size(); i++)
		{
			RowConstraints rc = new RowConstraints();
			rc.setPrefHeight(rowHeight);
			grid.getRowConstraints().add(rc);

			for (int j = 0; j < timeLblsList.size(); j++)
			{
				if (j >= lastRemovedCol)
				{
					Label lbl = new Label("");
					lbl.setId((i) + "," + (j));
					lbl.setPrefSize(colWidth, rowHeight);
					lbl.setStyle("-fx-font-size:12; -fx-border-color:silver");
					lbl.setAlignment(Pos.CENTER);

					grid.add(lbl, j, i + 1);
					labelsList.add(lbl);
				}
			}
		}
	}

	/**
	 * This is a generic method which creates partitions for parameters, totals and grid sections
	 *
	 * @param gridName name of the grid
	 * @param totalsGridReqd If totals section is required, it is true.
	 * @param baseVbox vbox where partitions need to be created
	 */
	private void createDynamicGrid(String gridName, boolean totalsGridReqd, VBox baseVbox)
	{
		VBox vboxGrid = new VBox();
		vboxGrid.setPrefWidth(baseVbox.getPrefWidth());
		vboxGrid.setSpacing(5);

		HBox hboxGrid = new HBox();
		hboxGrid.setPrefWidth(baseVbox.getPrefWidth());

		// ---PARAMETER SCROLLPANE
		ScrollPane paramScroller = new ScrollPane();
		paramScroller.setPrefWidth(hboxGrid.getPrefWidth() * 0.2);
		GridPane paramGrid = new GridPane();
		paramGrid.getStylesheets().add(this.getClass().getResource("/styles/newStyles.css").toString());
		paramGrid.getStyleClass().add("GridPane");
		paramGrid.setPrefWidth(paramScroller.getPrefWidth());
		paramScroller.setContent(paramGrid);
		paramScroller.setVbarPolicy(ScrollBarPolicy.NEVER);
		paramScroller.setHbarPolicy(ScrollBarPolicy.NEVER);
		paramScroller.addEventFilter(ScrollEvent.SCROLL, new EventHandler<ScrollEvent>()
		{
			@Override
			public void handle(ScrollEvent event)
			{
				if (event.getDeltaY() != 0)
				{
					event.consume();
				}
			}
		});
		hboxGrid.getChildren().add(paramScroller);

		// ---GRID TEXTBOX SCROLLPANE
		ScrollPane textboxScroller = new ScrollPane();
		textboxScroller.setPrefWidth(hboxGrid.getPrefWidth() * 0.8);
		GridPane textboxGrid = new GridPane();
		textboxGrid.getStylesheets().add(this.getClass().getResource("/styles/newStyles.css").toString());
		// textboxGrid.setStyle("-fx-grid-lines-visible: true");
		textboxGrid.getStyleClass().add("GridPane");
		textboxGrid.setPrefWidth(textboxScroller.getPrefWidth() * 0.98);
		textboxScroller.setContent(textboxGrid);
		textboxScroller.setVbarPolicy(ScrollBarPolicy.NEVER);
		textboxScroller.setHbarPolicy(ScrollBarPolicy.NEVER);
		textboxScroller.addEventFilter(ScrollEvent.SCROLL, new EventHandler<ScrollEvent>()
		{
			@Override
			public void handle(ScrollEvent event)
			{
				if (event.getDeltaY() != 0)
				{
					event.consume();
				}
			}
		});

		if (totalsGridReqd)
		{
			paramScroller.setPrefWidth(hboxGrid.getPrefWidth() * 0.13);
			paramGrid.setPrefWidth(paramScroller.getPrefWidth());

			// ---TOTALS SCROLLPANE
			ScrollPane totalsScroller = new ScrollPane();
			totalsScroller.setPrefWidth(hboxGrid.getPrefWidth() * 0.192);
			GridPane totalsGrid = new GridPane();
			totalsGrid.getStylesheets().add(this.getClass().getResource("/styles/newStyles.css").toString());
			totalsGrid.getStyleClass().add("GridPane");
			totalsGrid.setPrefWidth(totalsScroller.getPrefWidth());
			totalsScroller.setContent(totalsGrid);
			totalsScroller.setVbarPolicy(ScrollBarPolicy.NEVER);
			totalsScroller.setHbarPolicy(ScrollBarPolicy.NEVER);
			totalsScroller.addEventFilter(ScrollEvent.SCROLL, new EventHandler<ScrollEvent>()
			{
				@Override
				public void handle(ScrollEvent event)
				{
					if (event.getDeltaY() != 0)
					{
						event.consume();
					}
				}
			});

			hboxGrid.getChildren().add(totalsScroller);
			totalsGridMap.put(gridName, totalsGrid);
			totalsListMap.put(gridName, new ArrayList<String>());
		}
		hboxGrid.getChildren().add(textboxScroller);

		vboxGrid.getChildren().add(hboxGrid);

		if (gridName.equalsIgnoreCase(MED_GRID_NAME))
		{
			addNewMedButton(vboxGrid);
		}

		TitledPane titledPane = new TitledPane();
		titledPane.setText(gridName.substring(0, 1).toUpperCase() + gridName.substring(1, gridName.length()));
		titledPane.setContent(vboxGrid);
		titledPane.setExpanded(false);

		baseVbox.getChildren().add(titledPane);

		paramGridMap.put(gridName, paramGrid);
		textboxGridMap.put(gridName, textboxGrid);
		paramListMap.put(gridName, new ArrayList<String>());
		textboxListMap.put(gridName, new ArrayList<Label>());
	}

	public void addNewMedButton(VBox vbox)
	{
		medAddButton = new Button("Add New Medicine");
		medAddButton.setStyle("-fx-background-color: #005d87; -fx-text-fill: white; -fx-font-weight:bold ");
		medAddButton.setPrefSize(140, 40);
		vbox.getChildren().add(medAddButton);

		medMenuItemAddHandler = new EventHandler<ActionEvent>()
		{
			public void handle(ActionEvent event)
			{
				try
				{
					ControllerMediator.getInstance().getMainController().bindPopupFxml(FxmlView.SEARCH_MEDICINE);
				}
				catch (Exception e)
				{
					LOG.error("## Exception occured:", e);

				}
			}
		};
		medAddButton.addEventHandler(ActionEvent.ACTION, medMenuItemAddHandler);

	}

	/**
	 * This method adds parameters to parameter section
	 *
	 * @param grid grid holding parameters
	 * @param rowPrefHeight row height
	 * @param legendsArray array of parameters
	 */
	public void addLegendsToGrid(GridPane grid, Double rowPrefHeight, String[] legendsArray)
	{
		try
		{
			for (int i = 0; i < legendsArray.length + 1; i++)
			{
				RowConstraints rc = new RowConstraints();
				rc.setMaxHeight(rowPrefHeight);
				grid.getRowConstraints().add(rc);

				Label lbl;
				if (i == 0)
				{
					lbl = new Label("Parameters");
					lbl.setStyle("-fx-background-color: #005d87; -fx-text-fill: white;  -fx-border-color: silver;");
				}
				else
				{
					lbl = new Label(legendsArray[i - 1]);
					lbl.setStyle("-fx-background-color: rgb(58.0, 163.0, 178.0); -fx-text-fill: white;  -fx-border-color: white");
				}
				lbl.setPrefSize(grid.getPrefWidth(), rowPrefHeight);

				lbl.setAlignment(Pos.CENTER_LEFT);

				grid.add(lbl, 0, i);
			}
		}
		catch (Exception e)
		{
			LOG.error("## Exception occured:", e);

		}

	}

	/**
	 * This method creates totals section
	 *
	 * @param totalsGrid grid holding totals labels
	 * @param rowHeight row height
	 * @param paramList list containing parameters
	 * @param labelsMap map containing key-value pairs in format (Parameter Names - Their Label component)
	 */
	public void createTotalsGrid(GridPane totalsGrid, Double rowHeight, List<String> paramList,Map<String, Label> labelsMap)
	{
		try
		{
			for (int i = 0; i < paramList.size() + 1; i++)
			{
				RowConstraints rc = new RowConstraints();
				rc.setMaxHeight(rowHeight);
				
				totalsGrid.getRowConstraints().add(rc);

				Label lbl;
				if (i == 0)
				{
					lbl = new Label("T.Dose (Rate /kg/min)");
					lbl.setStyle("-fx-background-color: #005d87; -fx-text-fill: white;  -fx-border-color: silver;");
				}
				else
				{
					lbl = new Label();
					lbl.setStyle("-fx-background-color: rgb(58.0, 163.0, 178.0); -fx-text-fill: white;  -fx-border-color: white");
					labelsMap.put(paramList.get(i-1), lbl);
				}
				lbl.setPadding(new Insets(0, 0, 0, 5));
				double tt = totalsGrid.getPrefWidth();
				lbl.setPrefSize(tt, rowHeight);
				//lbl.setPr

				totalsGrid.add(lbl, 0, i);
			}
		}
		catch (Exception e)
		{
			LOG.error("## Exception occured:", e);

		}
	}


	/**
	 * This method adds timeline at the top of provided grid
	 *
	 * @param timeLblsList list holding time labels
	 * @param grid grid where timeline need to be added
	 * @param colWidth column width
	 * @param rowHeight row height
	 */
	private void addGridHeader(List<Calendar> timeLblsList, GridPane grid, double colWidth, double rowHeight)
	{
		for (int i = 0; i < timeLblsList.size(); i++)
		{
			ColumnConstraints cc = new ColumnConstraints();
			cc.setMaxWidth(colWidth);
			grid.getColumnConstraints().add(cc);

			Label headerLbl = new Label(sdf.format(timeLblsList.get(i).getTime()));
			headerLbl.setAlignment(Pos.CENTER);
			headerLbl.setStyle("-fx-background-color: #005d87; -fx-text-fill: white;  -fx-border-color: silver;");

			headerLbl.setPrefSize(colWidth, rowHeight);
			grid.add(headerLbl, i, 0);

			lastGridColumn=i;
		}
	}

	/**
	 * This method add rows in the provided grid
	 *
	 * @param rowHeaderList list containing parameter names
	 * @param timeLblsList list holding time labels
	 * @param grid grid where row need to be added
	 * @param labelsList list containing parameter label components
	 * @param colWidth column width
	 * @param rowHeight row height
	 */
	private void addGridRows(List<String> rowHeaderList, List<Calendar> timeLblsList, GridPane grid,
	        List<Label> labelsList, double colWidth, double rowHeight)
	{
		for (int i = 0; i < rowHeaderList.size(); i++)
		{
			RowConstraints rc = new RowConstraints();
			rc.setPrefHeight(rowHeight);
			grid.getRowConstraints().add(rc);

			for (int j = 0; j < timeLblsList.size(); j++)
			{
				Label lbl = new Label("");
				lbl.setId((i) + "," + (j));
				lbl.setPrefSize(colWidth, rowHeight);
				lbl.setStyle("-fx-font-size:12; -fx-border-color:silver");
				lbl.setAlignment(Pos.CENTER);

				grid.add(lbl, j, i + 1);
				labelsList.add(lbl);
			}
		}
	}

	/**
	 * This method adds a new grid row to an existing grid based on category passed
	 *
	 * @param rowHeader parameter name for the new row
	 * @param category type of grid where new row need to be added
	 */
	public void addNewGridRow(String rowHeader, String category)
	{
		try
		{
			if(category.equalsIgnoreCase(FLUID_GRID_NAME))
			{
				if (!paramListMap.get(FLUID_GRID_NAME).contains(rowHeader.split("\\|")[0]))
				{
					updateParamGrid(rowPrefHeight, paramGridMap.get(FLUID_GRID_NAME), rowHeader.split("\\|")[0], paramListMap.get(FLUID_GRID_NAME),lastFluidRow,false);
					Label totalsGridLbl=updateParamGrid(rowPrefHeight, totalsGridMap.get(FLUID_GRID_NAME), rowHeader.split("\\|")[1], totalsListMap.get(FLUID_GRID_NAME),lastFluidRow,true);
					totalLblsMap.get(FLUID_GRID_NAME).put(rowHeader.split("\\|")[0], totalsGridLbl);
					updateValuesGrid(rowPrefHeight, columnPrefWidth, headerTimeLblsList,  textboxGridMap.get(FLUID_GRID_NAME), textboxListMap.get(FLUID_GRID_NAME),lastFluidRow,FLUID_GRID_NAME);
					lastFluidRow += 1;
				}
			}
			else if(category.equalsIgnoreCase(MED_GRID_NAME))
			{
				if (!paramListMap.get(MED_GRID_NAME).contains(rowHeader.split("\\|")[0]))
				{
					updateParamGrid(rowPrefHeight, paramGridMap.get(MED_GRID_NAME), rowHeader.split("\\|")[0],paramListMap.get(MED_GRID_NAME), lastMedRow,false);
					Label totalsGridLbl=updateParamGrid(rowPrefHeight, totalsGridMap.get(MED_GRID_NAME), rowHeader.split("\\|")[1], totalsListMap.get(MED_GRID_NAME),lastMedRow,true);
					totalLblsMap.get(MED_GRID_NAME).put(rowHeader.split("\\|")[0], totalsGridLbl);
					updateValuesGrid(rowPrefHeight, columnPrefWidth, headerTimeLblsList,  textboxGridMap.get(MED_GRID_NAME), textboxListMap.get(MED_GRID_NAME),lastMedRow,MED_GRID_NAME);
					lastMedRow += 1;
				}
			}
		}
		catch (Exception e)
		{
			LOG.error("## Exception occured:", e);

		}

	}

	/**
	 * This method updates provided grid with a new component
	 *
	 * @param rowHeight row height
	 * @param grid grid to be updated
	 * @param lblText parameter name to be updated
	 * @param paramlist list holding parameter names
	 * @param lastrow last row number present in the grid
	 * @param isTotalGrid whether it is parameter grid or totals grid
	 * @return updated parameter label
	 */
	private Label updateParamGrid(double rowHeight, GridPane grid, String lblText, List<String> paramlist, int lastrow,
	        boolean isTotalGrid)
	{
		RowConstraints rc = new RowConstraints();
		rc.setMaxHeight(rowHeight);
		grid.getRowConstraints().add(rc);

		Label lbl = new Label(lblText);
		lbl.setStyle("-fx-background-color: rgb(58.0, 163.0, 178.0); -fx-text-fill: white; -fx-border-color: white;-fx-font-weight:bold");
		lbl.setPrefSize(grid.getPrefWidth(), rowHeight);
		if (isTotalGrid)
		{
			lbl.setAlignment(Pos.CENTER_LEFT);
			lbl.setText(lbl.getText());
			lbl.setPadding(new Insets(0, 0, 0, 5));
			double tt = grid.getPrefWidth();
			lbl.setPrefSize(tt, rowHeight);
		}
		else
			lbl.setAlignment(Pos.CENTER_LEFT);


		paramlist.add(lblText);
		grid.add(lbl, 0, lastrow);
		grid.setPrefHeight(grid.getPrefHeight() + rowHeight);

		return lbl;
	}

	/**
	 * This method updates grid view section with a new row
	 *
	 * @param rowHeight row height
	 * @param columnWidth column width
	 * @param timeLblsList list holding time labels
	 * @param grid grid which need to be updated
	 * @param gridTextboxList list holding textboxes present in provided grid
	 * @param lastrow last row number present in the grid
	 * @param category name of the section
	 */
	private void updateValuesGrid(double rowHeight, double columnWidth, List<Calendar> timeLblsList, GridPane grid,
	        List<Label> gridTextboxList, int lastrow, String category)
	{
		RowConstraints rc = new RowConstraints();
		rc.setPrefHeight(rowHeight);
		grid.getRowConstraints().add(rc);

		for (int j = 0; j < timeLblsList.size(); j++)
		{
			if (j >= lastRemovedCol)
			{
				Label lbl = new Label("");
				lbl.setId((lastrow - 1) + "," + (j));
				lbl.setPrefSize(columnWidth, rowHeight);
				grid.add(lbl, j, lastrow);
				gridTextboxList.add(lbl);
				lbl.setAlignment(Pos.CENTER);
				lbl.setStyle("-fx-font-size:12; -fx-border-color:silver");
			}

			/*if (j < lastRemovedCol)
			{
				Node node=getNodeByRowColumnIndex(lastrow, j, grid);
				grid.getChildren().remove(node);
			}*/
		}
		grid.setPrefHeight(grid.getPrefHeight() + rowHeight);
	}


	/**
	 * This method adds new column to the all grid views
	 */
	private void addNewColumn()
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(gridLastTimeStamp.getTime());

		for (int counter = 0; counter < NO_OF_NEW_COLUMNS; counter++)
		{
			cal.add(Calendar.MINUTE, MINS_GAP);

			for(Map.Entry<String, List<String>> entry:paramListMap.entrySet())
			{
				if (textboxGridMap.get(entry.getKey()) != null && textboxListMap.get(entry.getKey()) != null)
				{
					ColumnConstraints cc = new ColumnConstraints();
					cc.setMaxWidth(columnPrefWidth);
					textboxGridMap.get(entry.getKey()).getColumnConstraints().add(cc);

					addColumn(rowPrefHeight, columnPrefWidth, entry.getValue(), cal, lastGridColumn,
					        textboxGridMap.get(entry.getKey()), textboxListMap.get(entry.getKey()));
				}

				if(counter==0)
				{
					if (textboxGridMap.get(entry.getKey()) != null && entry.getValue() != null)
					{
						removeColumnFromGrid(textboxGridMap.get(entry.getKey()), entry.getValue(), entry.getKey());

					}
				}
			}
			lastGridColumn+=1;
			//String gridLastTimeLbl = sdf.format(cal.getTime());
			Calendar timeLbl = Calendar.getInstance();
			timeLbl.setTime(cal.getTime());
			headerTimeLblsList.add(timeLbl);
			gridLastTimeStamp.setTime(cal.getTime());
		}


		/*//---Remove first five columns from all grids
		for(Map.Entry<String, GridPane> entry:textboxGridMap.entrySet())
		{
			if (paramListMap.get(entry.getKey()) != null && entry.getValue() != null)
				removeColumnFromGrid(entry.getValue(), paramListMap.get(entry.getKey()));
		}*/
		lastRemovedCol += NO_OF_NEW_COLUMNS;

	}

	/**
	 * This method removes first five columns from the live grid component
	 *
	 * @param grid grid from which column need to be removed
	 * @param rowHeaderList list containing parameter names
	 * @param gridName name of the grid from which cloumns need to be removed
	 */
	private void removeColumnFromGrid(GridPane grid, List<String> rowHeaderList, String gridName)
	{
		for (int j = lastRemovedCol; j < lastRemovedCol + NO_OF_NEW_COLUMNS; j++)
		{
			for (int i = 0; i < rowHeaderList.size() + 1; i++)
			{
				// ---Remove labels from grid
				Node node = getNodeByRowColumnIndex(i, j, grid);
				grid.getChildren().remove(node);

				// ---Remove labels from lists
				textboxListMap.get(gridName).remove(getTextboxFromList(i, j, textboxListMap.get(gridName)));

			}
			// ---Remove grid column constraints
			if (grid.getColumnConstraints().size() >= j + 1)
				grid.getColumnConstraints().remove(j);

		}

	}

	/**
	 * This method searches for a node using row and column from the gridpane
	 *
	 * @param row row number
	 * @param column column number
	 * @param gridPane gridpane where node exists
	 * @return Returns the matching node
	 */
	private Node getNodeByRowColumnIndex(final int row, final int column, GridPane gridPane)
	{
		Node result = null;
		List<Node> childrens = gridPane.getChildren();

		for (Node node : childrens)
		{
			if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column)
			{
				result = node;
				break;
			}
		}

		return result;
	}

	/**
	 * This method adds a new column to passed grid
	 *
	 * @param rowHeight row height
	 * @param columnWidth column width
	 * @param paramList list of parameter names
	 * @param cal time value to be added to the column header
	 * @param lastColumn last column number present in the grid
	 * @param grid grid in which column need to be added
	 * @param textBoxList list holding textboxes present in provided grid
	 */
	private void addColumn(double rowHeight, double columnWidth, List<String> paramList, Calendar cal, int lastColumn,
	        GridPane grid, List<Label> textBoxList)
	{
		for (int i = 0; i < paramList.size() + 1; i++)
		{
			if (i == 0)
			{
				Label lbl = new Label(sdf.format(cal.getTime()));
				lbl.setPrefSize(columnWidth, rowHeight);
				lbl.setAlignment(Pos.CENTER);
				lbl.setStyle("-fx-background-color: #005d87; -fx-text-fill: white;  -fx-border-color: silver;");
				grid.add(lbl, lastColumn + 1, i);
			}
			else
			{
				Label lbl = new Label("");
				lbl.setStyle("-fx-font-size:12; -fx-border-color:silver");
				lbl.setAlignment(Pos.CENTER);
				lbl.setPrefSize(columnWidth, rowHeight);
				lbl.setId((i - 1) + "," + (lastColumn + 1));
				grid.add(lbl, lastColumn + 1, i);
				textBoxList.add(lbl);

				/*if (category.equalsIgnoreCase(PTMNTR_GRID_NAME))
					addPtMntrContextMenu();
				if (category.equalsIgnoreCase(ANES_GRID_NAME))
					addAnesContextMenu();
				if (category.equalsIgnoreCase(MED_GRID_NAME))
					addMedContextMenu();
				if (category.equalsIgnoreCase(FLUID_GRID_NAME))
					addFluidContextMenu(textbox);
				if (category.equalsIgnoreCase(OUTPUT_GRID_NAME))
					addOutputContextMenu(textbox);
				else if (category.equalsIgnoreCase(TEST1_GRID_NAME))
					addTestContextMenu(textbox, TEST1_GRID_NAME);
				else if (category.equalsIgnoreCase(TEST2_GRID_NAME))
					addTestContextMenu(textbox, TEST2_GRID_NAME);*/

			}
		}
		//preventTextboxInput(txtList);
	}


	/**
	 * This method fills data from S5PatientMonitor / Anesthesia devices into live grid cells in their respective sections
	 *
	 * @param time time label corresponding to which vales need to be populated in the grid
	 * @param paramVals array of values for specified parameters on grid
	 * @param type S5PatientMonitor / Anesthesia
	 */
	private void fillParamValInCells(Calendar time, String[] paramVals, String type)
	{
		/*Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, Integer.valueOf(time.split(":")[0]));
		cal.set(Calendar.MINUTE, Integer.valueOf(time.split(":")[1]));
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);*/

		List<Calendar> timeLblsList = new LinkedList<Calendar>();
		timeLblsList = headerTimeLblsList;
		String tableRowHeader[] = {};
		List<Label> gridTextBoxList = new ArrayList<Label>();

		if (type.equalsIgnoreCase("ptmonitor"))
		{
			tableRowHeader = ptMntrTableRowHeader;
			gridTextBoxList = textboxListMap.get(PTMNTR_GRID_NAME);
		}
		else if (type.equalsIgnoreCase("anes"))
		{
			tableRowHeader = anesTableRowHeader;
			gridTextBoxList = textboxListMap.get(ANES_GRID_NAME);
		}
		//time.set(Calendar.SECOND, 0);
		//time.getTime();
		if (timeLblsList.contains(time))
		{
			int y = timeLblsList.indexOf(time);
			for (String param : paramVals)
			{
				String paramName = param.split("\\|")[0];
				String paramVal = param.split("\\|")[1];

				try {
					if (paramVal.equalsIgnoreCase("null") || paramVal.contains("---")
					        || paramVal.contains("- - -") || Double.parseDouble(paramVal) <= 0)
						paramVal = "---";
				} catch (NumberFormatException e) {
					paramVal = "---";
				}

				int x = -1;
				for (int i = 0; i < tableRowHeader.length; i++)
				{
					if (tableRowHeader[i].split(" ")[0].equalsIgnoreCase(paramName))
					{
						x = i;
						break;
					}
				}
				if (x >= 0)
				{
					Label textbox = getTextboxFromList(x, y, gridTextBoxList);
					textbox.setText(paramVal);
				}
			}
		}
	}

	/**
	 * This method saves data from S5PatientMonitor / Anesthesia devices into database
	 *
	 * @param time time value corresponding to which data needs to be saved/updated
	 * @param paramVals array of values for specified parameters on grid
	 * @param type S5PatientMonitor / Anesthesia
	 */
	private void saveDeviceData(Calendar time, String[] paramVals, String type)
	{
		/*Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, Integer.valueOf(time.split(":")[0]));
		cal.set(Calendar.MINUTE, Integer.valueOf(time.split(":")[1]));*/
		//time.set(Calendar.SECOND, 0);
		time.set(Calendar.MILLISECOND, 0);
		

		PatientMonitorData ptmntrObj = new PatientMonitorData();
		AnethesiaMachineData anesObj = new AnethesiaMachineData();

		if (type.equalsIgnoreCase("ptmonitor"))
		{
			ptmntrObj.setCaseId(PatientSession.getInstance().getPatientCase().getCaseId());
			ptmntrObj.setDeviceId(null);
			ptmntrObj.setTimeStamp(time.getTime());
		}
		else if (type.equalsIgnoreCase("anes"))
		{
			anesObj.setCaseId(PatientSession.getInstance().getPatientCase().getCaseId());
			anesObj.setDeviceId(null);
			anesObj.setTimeStamp(time.getTime());
		}

		for (String param : paramVals)
		{
			String paramName = param.split("\\|")[0];
			String paramVal = param.split("\\|")[1];

			if (type.equalsIgnoreCase("ptmonitor"))
			{
				if (paramName.equalsIgnoreCase(ParamMinMaxRange.HR))
					ptmntrObj.setHr(paramVal);
				else if (paramName.equalsIgnoreCase(ParamMinMaxRange.IBPSYS))
					ptmntrObj.setiBPSys(paramVal);
				else if (paramName.equalsIgnoreCase(ParamMinMaxRange.IBPDIA))
					ptmntrObj.setiBPDia(paramVal);
				else if (paramName.equalsIgnoreCase(ParamMinMaxRange.SPO2))
					ptmntrObj.setSpO2(paramVal);
				else if (paramName.equalsIgnoreCase(ParamMinMaxRange.ETCO2))
					ptmntrObj.setEtCO2(paramVal);
				else if (paramName.equalsIgnoreCase(ParamMinMaxRange.TEMP1))
					ptmntrObj.setTemp1(paramVal);
				else if (paramName.equalsIgnoreCase(ParamMinMaxRange.TEMP2))
					ptmntrObj.setTemp2(paramVal);
				else if (paramName.equalsIgnoreCase(ParamMinMaxRange.BIS))
					ptmntrObj.setBIS(paramVal);
				else if (paramName.equalsIgnoreCase(ParamMinMaxRange.ETAGENT))
					ptmntrObj.setETAgent(paramVal);
				else if (paramName.equalsIgnoreCase(ParamMinMaxRange.MAC))
					ptmntrObj.setMAC(paramVal);
				else if (paramName.equalsIgnoreCase(ParamMinMaxRange.IBPMEAN))
					ptmntrObj.setiBPMean(paramVal);
				else if (paramName.equalsIgnoreCase(ParamMinMaxRange.NIBPMEAN))
					ptmntrObj.setNiBPMean(paramVal);
				else if (paramName.equalsIgnoreCase(ParamMinMaxRange.NIBPDIA))
					ptmntrObj.setNiBPDia(paramVal);
				else if (paramName.equalsIgnoreCase(ParamMinMaxRange.NIBPSYS))
					ptmntrObj.setNiBPSys(paramVal);
			}
			else if (type.equalsIgnoreCase("anes"))
			{
				if (paramName.equalsIgnoreCase(ParamMinMaxRange.PPEAK))
					anesObj.setpPeak(paramVal);
				else if (paramName.equalsIgnoreCase(ParamMinMaxRange.PMEAN))
					anesObj.setpMean(paramVal);
				else if (paramName.equalsIgnoreCase(ParamMinMaxRange.PEEP))
					anesObj.setPeep(paramVal);
				else if (paramName.equalsIgnoreCase(ParamMinMaxRange.CIRCUITO2))
					anesObj.setCircuitO2(paramVal);
				else if (paramName.equalsIgnoreCase(ParamMinMaxRange.TVEXP))
					anesObj.setTvExp(paramVal);
				else if (paramName.equalsIgnoreCase(ParamMinMaxRange.MVEXP))
					anesObj.setMvExp(paramVal);
				else if (paramName.equalsIgnoreCase(ParamMinMaxRange.RR))
					anesObj.setRr(paramVal);

			}

		}


		Platform.runLater(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					if (type.equalsIgnoreCase("ptmonitor"))
					{
						GetPatientMonitorDataTimeCaseService ptmntrSearchService = GetPatientMonitorDataTimeCaseService.getInstance(
						        PatientSession.getInstance().getPatientCase().getCaseId(), time.getTime());
						ptmntrSearchService.restart();


						//---success
						ptmntrTimeCaseServiceSuccessHandler = new EventHandler<WorkerStateEvent>()
						{
							@Override
							public void handle(WorkerStateEvent event)
							{
								try
								{
									PatientMonitorData obj = ptmntrSearchService.getPatientMonitorDataTimeStampCaseID();
									if (obj.getPatientMonitorDataID() > 0)
									{
										obj.setHr(ptmntrObj.getHr());
										obj.setiBPSys(ptmntrObj.getiBPSys());
										obj.setiBPDia(ptmntrObj.getiBPDia());
										obj.setSpO2(ptmntrObj.getSpO2());
										obj.setEtCO2(ptmntrObj.getEtCO2());
										obj.setTemp1(ptmntrObj.getTemp1());
										obj.setTemp2(ptmntrObj.getTemp2());
										obj.setBIS(ptmntrObj.getBIS());
										obj.setETAgent(ptmntrObj.getETAgent());
										obj.setMAC(ptmntrObj.getMAC());
										obj.setiBPMean(ptmntrObj.getiBPMean());
										obj.setNiBPMean(ptmntrObj.getNiBPMean());
										obj.setNiBPDia(ptmntrObj.getNiBPDia());
										obj.setNiBPSys(ptmntrObj.getNiBPSys());

										saveDevcieParamToDB.savePtMntrData(obj, false);
									}
									else
									{
										saveDevcieParamToDB.savePtMntrData(ptmntrObj, false);
									}

									ptmntrSearchService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
											ptmntrTimeCaseServiceSuccessHandler);
									ptmntrSearchService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
											ptmntrTimeCaseServiceFailedHandler);
								}
								catch (Exception e)
								{
									LOG.error("## Exception occured:", e);
								}
							}
						};
						ptmntrSearchService.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
								ptmntrTimeCaseServiceSuccessHandler);


						//---failure
						ptmntrTimeCaseServiceFailedHandler = new EventHandler<WorkerStateEvent>()
						{
							@Override
							public void handle(WorkerStateEvent event)
							{
								try
								{
									Main.getInstance().getUtility().showNotification("Error", "Error",
									        ptmntrSearchService.getException().getMessage());

									ptmntrSearchService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
											ptmntrTimeCaseServiceSuccessHandler);
									ptmntrSearchService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
											ptmntrTimeCaseServiceFailedHandler);
								}
								catch (Exception e)
								{
									LOG.error("## Exception occured:", e);
								}
							}
						};
						ptmntrSearchService.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
								ptmntrTimeCaseServiceFailedHandler);



					}
					else if (type.equalsIgnoreCase("anes"))
					{

						GetAnesthesiaMachineDataTimeCaseService anesSearchService = GetAnesthesiaMachineDataTimeCaseService.getInstance(
						        PatientSession.getInstance().getPatientCase().getCaseId(), time.getTime());
						anesSearchService.restart();


						anesTimeCaseServiceSuccessHandler = new EventHandler<WorkerStateEvent>()
						{
							@Override
							public void handle(WorkerStateEvent event)
							{
								try
								{
									AnethesiaMachineData obj = anesSearchService.getAnethesiaMachineDataTimeCase();
									if (obj.getAnesthesiaMachineDataID() > 0)
									{
										obj.setpPeak(anesObj.getpPeak());
										obj.setpMean(anesObj.getpMean());
										obj.setPeep(anesObj.getPeep());
										obj.setCircuitO2(anesObj.getCircuitO2());
										obj.setTvExp(anesObj.getTvExp());
										obj.setMvExp(anesObj.getMvExp());
										obj.setRr(anesObj.getRr());

										saveDevcieParamToDB.saveAnesData(obj, false);
									}
									else
									{
										saveDevcieParamToDB.saveAnesData(anesObj, false);
									}

									anesSearchService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
									        anesTimeCaseServiceSuccessHandler);
									anesSearchService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
									        anesTimeCaseServiceFailedHandler);
								}
								catch (Exception e)
								{
									LOG.error("## Exception occured:", e);

								}
							}
						};
						anesSearchService.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
								anesTimeCaseServiceSuccessHandler);

						anesTimeCaseServiceFailedHandler = new EventHandler<WorkerStateEvent>()
						{
							@Override
							public void handle(WorkerStateEvent event)
							{
								try
								{
									Main.getInstance().getUtility().showNotification("Error", "Error",
									        anesSearchService.getException().getMessage());

									anesSearchService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
									        anesTimeCaseServiceSuccessHandler);
									anesSearchService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
									        anesTimeCaseServiceFailedHandler);
								}
								catch (Exception e)
								{
									LOG.error("## Exception occured:", e);

								}
							}
						};
						anesSearchService.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
								anesTimeCaseServiceFailedHandler);

					}
				}
				catch (Exception e)
				{
					LOG.error("## Exception occured:", e);

				}
			}
		});

	}

	/**
	 * This method searches for a label from list of labels based on row(x) and column(y) number.
	 * Every label holds an Id in format x,y where x is its row number and y is its column number
	 *
	 * @param x row number
	 * @param y column number
	 * @param gridTextList list containing labels present in the grid
	 * @return the matching label
	 */
	public Label getTextboxFromList(int x, int y, List<Label> gridTextList)
	{
		Label textbox = new Label();
		try
		{
			for (Label txt : gridTextList)
			{
				String id = txt.getId();
				if (Integer.valueOf(id.split(",")[0]) == x && Integer.valueOf(id.split(",")[1]) == y)
				{
					textbox = txt;
					break;
				}
			}

		}
		catch (Exception e)
		{
			LOG.error("## Exception occured:", e);

		}
		return textbox;
	}

	/**
	 * This method fills cells in S5PAtientMonitor and Aensthesia live grids
	 *
	 * @param deviceType S5PAtientMonitor / Aensthesia
	 * @param obj Device specific object containing required parameter values
	 * @param time Timestamp corresponding to which column needs to be filled
	 */
	public void fillDeviceGridCell(String deviceType, Object obj, Calendar time)
	{
		try
		{
			if (deviceType.equalsIgnoreCase("ptmonitor"))
			{
				PatientMonitorData patientMonitorData = (PatientMonitorData) obj;
				int y = 0;
				if (headerTimeLblsList.contains(time))
				{
					y = headerTimeLblsList.indexOf(time);
					for (int i = 0; i < paramListMap.get(PTMNTR_GRID_NAME).size(); i++)
					{
						Label textbox = new Label();
						textbox = getTextboxFromList(i, y, textboxListMap.get(PTMNTR_GRID_NAME));

						if (paramListMap.get(PTMNTR_GRID_NAME).get(i).split(" ")[0].equalsIgnoreCase("hr"))
							textbox.setText(patientMonitorData.getHr());
						else if (paramListMap.get(PTMNTR_GRID_NAME).get(i).split(" ")[0].equalsIgnoreCase("ibpsys"))
							textbox.setText(patientMonitorData.getiBPSys());
						else if (paramListMap.get(PTMNTR_GRID_NAME).get(i).split(" ")[0].equalsIgnoreCase("ibpdia"))
							textbox.setText(patientMonitorData.getiBPDia());
						else if (paramListMap.get(PTMNTR_GRID_NAME).get(i).split(" ")[0].equalsIgnoreCase("spo2"))
							textbox.setText(patientMonitorData.getSpO2());
						else if (paramListMap.get(PTMNTR_GRID_NAME).get(i).split(" ")[0].equalsIgnoreCase("etco2"))
							textbox.setText(patientMonitorData.getEtCO2());
						else if (paramListMap.get(PTMNTR_GRID_NAME).get(i).split(" ")[0].equalsIgnoreCase("temp1"))
							textbox.setText(patientMonitorData.getTemp1());
						else if (paramListMap.get(PTMNTR_GRID_NAME).get(i).split(" ")[0].equalsIgnoreCase("temp2"))
							textbox.setText(patientMonitorData.getTemp2());
						else if (paramListMap.get(PTMNTR_GRID_NAME).get(i).split(" ")[0].equalsIgnoreCase("bis"))
							textbox.setText(patientMonitorData.getBIS());
						else if (paramListMap.get(PTMNTR_GRID_NAME).get(i).split(" ")[0].equalsIgnoreCase("etagent"))
							textbox.setText(patientMonitorData.getETAgent());
						else if (paramListMap.get(PTMNTR_GRID_NAME).get(i).split(" ")[0].equalsIgnoreCase("mac"))
							textbox.setText(patientMonitorData.getMAC());
						else if (paramListMap.get(PTMNTR_GRID_NAME).get(i).split(" ")[0].equalsIgnoreCase("ibpmean"))
							textbox.setText(patientMonitorData.getiBPMean());
						else if (paramListMap.get(PTMNTR_GRID_NAME).get(i).split(" ")[0].equalsIgnoreCase("nibpdia"))
							textbox.setText(patientMonitorData.getNiBPDia());
						else if (paramListMap.get(PTMNTR_GRID_NAME).get(i).split(" ")[0].equalsIgnoreCase("nibpsys"))
							textbox.setText(patientMonitorData.getNiBPSys());
						else if (paramListMap.get(PTMNTR_GRID_NAME).get(i).split(" ")[0].equalsIgnoreCase("nibpmean"))
							textbox.setText(patientMonitorData.getNiBPMean());
					}
				}

			}
			else if (deviceType.equalsIgnoreCase("anes"))
			{
				AnethesiaMachineData anethesiaMachineData = (AnethesiaMachineData) obj;
				int y = 0;
				if (headerTimeLblsList.contains(time))
				{
					y = headerTimeLblsList.indexOf(time);
					for (int i = 0; i <paramListMap.get(ANES_GRID_NAME).size(); i++)
					{
						Label textbox = new Label();
						textbox = getTextboxFromList(i, y, textboxListMap.get(ANES_GRID_NAME));

						if (paramListMap.get(ANES_GRID_NAME).get(i).split(" ")[0].equalsIgnoreCase("ppeak"))
							textbox.setText(anethesiaMachineData.getpPeak());
						else if (paramListMap.get(ANES_GRID_NAME).get(i).split(" ")[0].equalsIgnoreCase("pmean"))
							textbox.setText(anethesiaMachineData.getpMean());
						else if (paramListMap.get(ANES_GRID_NAME).get(i).split(" ")[0].equalsIgnoreCase("peep"))
							textbox.setText(anethesiaMachineData.getPeep());
						else if (paramListMap.get(ANES_GRID_NAME).get(i).split(" ")[0].equalsIgnoreCase("circuitO2"))
							textbox.setText(anethesiaMachineData.getCircuitO2());
						else if (paramListMap.get(ANES_GRID_NAME).get(i).split(" ")[0].equalsIgnoreCase("mvexp"))
							textbox.setText(anethesiaMachineData.getMvExp());
						else if (paramListMap.get(ANES_GRID_NAME).get(i).split(" ")[0].equalsIgnoreCase("tvexp"))
							textbox.setText(anethesiaMachineData.getTvExp());
						else if (paramListMap.get(ANES_GRID_NAME).get(i).split(" ")[0].equalsIgnoreCase("rr"))
							textbox.setText(anethesiaMachineData.getRr());

					}
				}
			}
		}
		catch (Exception e)
		{
			LOG.error("## Exception occured:", e);

		}

	}

	/**
	 * This method fills cells in Output live grid
	 *
	 * @param name name of output
	 * @param time Timestamp corresponding to which column needs to be filled
	 * @param value Value that need to be populated in the cell
	 * @param totalsValue value that needs to be updated in Totals column
	 */
	public void fillOutputGridCell(String name, Calendar time, String value, String totalsValue)
	{
		try
		{
			int x = 0, y = 0;
			if (headerTimeLblsList.contains(time))
			{
				y = headerTimeLblsList.indexOf(time);
				x = paramListMap.get(OUTPUT_GRID_NAME).indexOf(name);

				Label textbox = new Label();
				textbox = getTextboxFromList(x, y, textboxListMap.get(OUTPUT_GRID_NAME));
				textbox.setText(value);

			}

			if (!totalsValue.isEmpty())
				if (totalLblsMap.get(OUTPUT_GRID_NAME).get(name) != null)
					totalLblsMap.get(OUTPUT_GRID_NAME).get(name).setText(totalsValue);
		}
		catch (Exception e)
		{
			LOG.error("## Exception occured:", e);

		}
	}

	/**
	 * This method fills cells in Fluid live grid
	 *
	 * @param type whether fluid is started or stopped
	 * @param fluidName name of Fluid
	 * @param time timestamp corresponding to which column needs to be filled
	 * @param value value that need to be populated in the cell
	 * @param totalsValue value that needs to be updated in Totals column
	 */
	public void fillFluidGridCell(String type, String fluidName, Calendar time, String value, String totalsValue)
	{
		try
		{
			int x = 0, y = 0;
			if (headerTimeLblsList.contains(time))
			{
				y = headerTimeLblsList.indexOf(time);
				x =  paramListMap.get(FLUID_GRID_NAME).indexOf(fluidName);

				Label textbox = new Label();
				textbox = getTextboxFromList(x, y, textboxListMap.get(FLUID_GRID_NAME));
				textbox.setText(value);

				if (type.equalsIgnoreCase("start"))
				{
					textbox.setStyle(textbox.getStyle() + ";-fx-background-color:#60be98");
				}
				else if (type.equalsIgnoreCase("stop"))
				{
					textbox.setStyle(textbox.getStyle() + ";-fx-background-color:#d47f8e");
				}
			}

			if (!totalsValue.isEmpty())
				if (totalLblsMap.get(FLUID_GRID_NAME).get(fluidName) != null)
					totalLblsMap.get(FLUID_GRID_NAME).get(fluidName).setText(totalsValue);
		}
		catch (Exception e)
		{
			LOG.error("## Exception occured:", e);

		}
	}

	/**
	 * This method fills cells in Medication live grid
	 *
	 * @param type whether medication is bolus or infusion (started or stopped)
	 * @param medName name of medication
	 * @param time timestamp corresponding to which column needs to be filled
	 * @param value value that need to be populated in the cell
	 * @param totals value that needs to be updated in Totals column
	 */
	public void fillMedicationGridCell(String type, String medName, Calendar time, String value, String totals)
	{
		Calendar time1 = Calendar.getInstance();
		time1.setTime(time.getTime());
		try
		{
			int mingap = 	SystemConfiguration.getInstance().getMinsGap();
			int x = 0, y = 0;

				if (medsFromInfPump.contains(medName))
				{
					if (headerTimeLblsList.contains(time))
					{
						y = headerTimeLblsList.indexOf(time);
					}
					else
					{
						/*Calendar passedTime = Calendar.getInstance();
						passedTime.setTime(sdf.parse(time));*/

						for (Calendar timelbl : headerTimeLblsList)
						{
							/*Calendar cal = Calendar.getInstance();
							cal.setTime(sdf.parse(timelbl));*/

							if (timelbl.getTimeInMillis() - time.getTimeInMillis() > 0)
							{
								y = headerTimeLblsList.indexOf(timelbl);
								if (y != 0)
									y = y - 1;
								break;
							}
						}

					}
				}
				else
				{
					{
						
						while(time1.get(Calendar.MINUTE)%mingap!=0)
						{
							time1.add(Calendar.MINUTE, -1);
						}
					}
					
					y = headerTimeLblsList.indexOf(time1);
				
				}

				x = paramListMap.get(MED_GRID_NAME).indexOf(medName);
				Label textbox = new Label();
				textbox = getTextboxFromList(x, y, textboxListMap.get(MED_GRID_NAME));
				textbox.setText(value);
			textbox.setAlignment(Pos.CENTER);

				
				if (type.equalsIgnoreCase("bolus"))
				{
					textbox.setText("(" + textbox.getText() + ")");
					textbox.setStyle(textbox.getStyle() + ";-fx-background-color:white;-fx-border-color:silver");
					if (!totals.isEmpty())
					{
						String rate =medicineTotalRateMap.get(medName)==null?"Stopped":medicineTotalRateMap.get(medName);
						if (totalLblsMap.get(MED_GRID_NAME).get(medName) != null)
							totalLblsMap.get(MED_GRID_NAME).get(medName).setText(totals +   "  (" +rate +")");
				}
				}
				else if (type.equalsIgnoreCase("infusion start"))
				{
					textbox.setText(value);

					Tooltip tip = new Tooltip();
					tip.setText(textbox.getText());
					tip.setStyle("-fx-font-size:12");
					textbox.setTooltip(tip);

					textbox.setStyle(textbox.getStyle() + ";-fx-background-color:#60be98");
					Calendar currentTime = Calendar.getInstance();
					currentTime.set(Calendar.MILLISECOND, 0);
					addArrowSymbolToCell(currentTime, infusionStartedMap, MED_GRID_NAME, medPrevCellsFilledList);
					
					if (!totals.isEmpty())
						if (totalLblsMap.get(MED_GRID_NAME).get(medName) != null)
							totalLblsMap.get(MED_GRID_NAME).get(medName).setText(totals +  " (" +value +")");
				}
				else if (type.equalsIgnoreCase("infusion stop"))
				{
				textbox.setAlignment(Pos.CENTER);
					textbox.setText(value);
					textbox.setStyle(textbox.getStyle() + ";-fx-background-color:#d47f8e");
					textbox = getTextboxFromList(x, ++y, textboxListMap.get(MED_GRID_NAME));
					while (textbox.getText().contains(">>"))
					{	textbox.setStyle(("-fx-font-size:12; -fx-border-color:silver"));
						textbox.setText("");
					textbox = getTextboxFromList(x, ++y, textboxListMap.get(MED_GRID_NAME));
					
					}
					if (!totals.isEmpty())
						if (totalLblsMap.get(MED_GRID_NAME).get(medName) != null)
							totalLblsMap.get(MED_GRID_NAME).get(medName).setText(totals +  " " + "(Stopped)");
				}

				/*if (textbox.getId() != null)
					totalLblsMap.get(MED_GRID_NAME).get( paramListMap.get(MED_GRID_NAME).get(Integer.valueOf(textbox.getId().split(",")[0]))).setText(" " + totals);*/

			
		}
		catch (Exception e)
		{
			LOG.error("## Exception occured:", e);

		}


	}

	/**
	 * This method stops running medication animation when device is removed/disconnected/becomes unresponsive
	 *
	 * @param medName Medication that needs to be stopped
	 * @param serialNo Serial number of the unresponsive device
	 */
	public void stopInfusionWhenDeviceRemoved(String medName, String serialNo)
	{
		try
		{
			if(ControllerMediator.getInstance().getMainController().getDrawTabbedCenter().getInfusionStartedMap().containsKey(medName))
			{
				IntraopMedicationlog returnedObj =ControllerMediator.getInstance().getAssignInfusionPumpController().getConfiguredInfPumpMedMap().get(serialNo);

				 GetTotalMedicationVolumeService getTotalVolumeService = GetTotalMedicationVolumeService.getInstance(
					        returnedObj.getMedicationName(),
					        PatientSession.getInstance().getPatient().getPatientId(),
					        PatientSession.getInstance().getPatientCase().getCaseId());
				getTotalVolumeService.restart();

				getTotalMedicationVolumeServiceSuccessHandler = new EventHandler<WorkerStateEvent>()
				{
					@Override
					public void handle(WorkerStateEvent event)
					{

						try
						{
							ControllerMediator.getInstance().getMainController().getDrawTabbedCenter().getSerialNoInfusionMap().put(serialNo, false);
							//Date startTime=ControllerMediator.getInstance().getMainController().getDrawTabbedCenter().getInfusionStartedMap().get(returnedObj.getMedicationName()).getTime();

							ControllerMediator.getInstance().getMainController().getDrawTabbedCenter().getInfusionStartedMap().remove(returnedObj.getMedicationName());

							float totalDose = getTotalVolumeService.getTotalVolume().getValue();
							
							String totals = totalDose + " mg";
							 totals=MedicationInfusionController.calculateUnit(Float.parseFloat(totals.split(" ")[0])*1000);

							float infusedDosage=0;
							Calendar cal=Calendar.getInstance();
							cal.set(Calendar.SECOND, 0);
							cal.set(Calendar.MILLISECOND, 0);
							

							/*infusedDosage = calculateInfusedDose(startTime, cal.getTime(),
									ControllerMediator.getInstance().getMainController().getDrawTabbedCenter().getSyringePumpRecentInfRate(), returnedObj.getConcentration());*/

							infusedDosage=returnedObj.getInfuseDosage();

							returnedObj.setInfuseDosage(infusedDosage);
							returnedObj.setEndTime(cal.getTime());
							returnedObj.setRateOfInfusion(syringePumpRecentInfRate);

							SaveMedicationService saveMedicationService = SaveMedicationService.getInstance(returnedObj);
							saveMedicationService.restart();
							 String totals1 = totals;
							saveMedicationServiceSuccessHandler = new EventHandler<WorkerStateEvent>()
							{
								@Override
								public void handle(WorkerStateEvent event)
								{
									try
									{
										fillMedicationGridCell("infusion stop",returnedObj.getMedicationName(),cal,MedicationInfusionController.calculateUnit((returnedObj.getInfuseDosage())*1000),totals1);
										ControllerMediator.getInstance().getMainController().getDrawTabbedCenter().getMedsFromInfPump().remove(medName);

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
				getTotalVolumeService.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED, getTotalMedicationVolumeServiceFailedHandler);

			}
		}
		catch (Exception e)
		{
			LOG.error("## Exception occured:", e);

		}
	}

	/**
	 * This method calculates infused dosage value
	 *
	 * @param infusionStartTime timestamp when infusion start
	 * @param infusionStopTime timestamp when infusion end
	 * @param infusionRate rate at which infusion proceeds
	 * @param infusionConcentration concentration used in the infusion
	 * @return calculated dosage
	 */
	private float calculateInfusedDose(Date infusionStartTime, Date infusionStopTime, String infusionRate,
	        String infusionConcentration)
	{
		float infusedDose = 0f;

		infusionRate = infusionRate.split(" ")[0];
		Instant startInstant = infusionStartTime.toInstant();
		LocalDateTime startLocalDateTime = LocalDateTime.ofInstant(startInstant, ZoneId.systemDefault());
		Instant stopInstant = infusionStopTime.toInstant();
		LocalDateTime stopLocalDateTime = LocalDateTime.ofInstant(stopInstant, ZoneId.systemDefault());
		long time[] = getTime(startLocalDateTime, stopLocalDateTime);
		Period period = getPeriod(startLocalDateTime, stopLocalDateTime);

		Float diffHours = (period.getDays() * 24) + time[0] + (Float.valueOf(time[1]) / 60)
		        + (Float.valueOf(time[2]) / (60 * 60));

		Float volumeInjected = 0f;
		double dose = 0;
		double doseCal = 0;
		volumeInjected = Float.parseFloat(infusionRate) * diffHours;

		dose = volumeInjected * Float.parseFloat(infusionConcentration);
		doseCal = dose / 1000;
		if (doseCal != 0)
		{

			BigDecimal doseDecimal = new BigDecimal(doseCal);
			BigDecimal doseRounded = doseDecimal.setScale(3, BigDecimal.ROUND_HALF_UP);
			infusedDose = Float.parseFloat(doseRounded.toString());
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
	private static long[] getTime(LocalDateTime dob, LocalDateTime now)
	{
		LocalDateTime today = LocalDateTime.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth(), dob.getHour(),
		        dob.getMinute(), dob.getSecond());
		java.time.Duration duration = java.time.Duration.between(today, now);

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
	private static Period getPeriod(LocalDateTime dob, LocalDateTime now)
	{
		return Period.between(dob.toLocalDate(), now.toLocalDate());
	}

	/**
	 * This method fills cells in Tests live grid
	 *
	 * @param investigationValueList list of values for number of parameters specific to the given test
	 * @param time timestamp corresponding to which column needs to be filled
	 * @param testName name of the test
	 */
	public void fillTestsGridCell(List<InvestigationSetValue> investigationValueList, Calendar time, String testName)
	{
		try
		{
			List<String> paramList = paramListMap.get(testName);
			List<Label> gridTextboxList = textboxListMap.get(testName);

			int x = 0, y = 0;
			for (InvestigationSetValue testObj : investigationValueList)
			{
//			if (testObj.getValue() != null)
//			{
					y = headerTimeLblsList.indexOf(time);
					for (String val : paramList)
					{
						String paramName = val.split("\\(")[0].trim();
						if (testObj.getName().trim().equalsIgnoreCase(paramName))
						{
							x = paramList.indexOf(val);
							break;
						}
					}

					Label textbox = new Label();
					textbox = getTextboxFromList(x, y, gridTextboxList);
					if(testObj.getValue()!=null && !String.valueOf(testObj.getValue()).equals("") ){
					textbox.setText(String.valueOf(testObj.getValue()));
					}
					else{
						textbox.setText("");
					}

				}

//		}
		}
		catch (Exception e)
		{
			LOG.error("## Exception occured:", e);

		}
	}

	Map<String, String> totalsValuesMap;
	
	
	public Map<String, String> getTotalsValuesMap() {
		return totalsValuesMap;
	}
	public void setTotalsValuesMap(Map<String, String> totalsValuesMap) {
		this.totalsValuesMap = totalsValuesMap;
	}
	/**
	 * This method fetches totals values for a list of medicine names
	 *
	 * @param medNamesList list containing medicine names
	 */
	private void getTotalMedicationVolumeList(List<String> medNamesList)
	{
		GetTotalMedicationVolumeListService getTotalMedicationVolumeListService = GetTotalMedicationVolumeListService.getInstance(
		        medNamesList, PatientSession.getInstance().getPatient().getPatientId(),
		        PatientSession.getInstance().getPatientCase().getCaseId());
		getTotalMedicationVolumeListService.restart();


		getMedTotalsServiceSuccessHandler = new EventHandler<WorkerStateEvent>()
		{
			@Override
			public void handle(WorkerStateEvent event)
			{
				try
				{
					totalsValuesMap = getTotalMedicationVolumeListService.getTotalUsage();
					//DecimalFormat df = new DecimalFormat("###.##"); here
					
					for (Map.Entry<String, String> entry : totalsValuesMap.entrySet())
					{
						String rate = medicineTotalRateMap.get(entry.getKey());
								rate = rate == null ? "Stopped":  rate ;
					totalLblsMap.get(MED_GRID_NAME).get(entry.getKey()).setText(MedicationInfusionController.calculateUnit(Float.parseFloat(entry.getValue().split(" ")[0])*1000)
						 + " (" + rate+")");
						totalsListMap.get(MED_GRID_NAME).add(entry.getValue());
					}

					getTotalMedicationVolumeListService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
							getMedTotalsServiceSuccessHandler);
					getTotalMedicationVolumeListService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
							getMedTotalsServiceFailedHandler);
				}
				catch (Exception e)
				{
					LOG.error("## Exception occured:", e);

				}
			}
		};
		getTotalMedicationVolumeListService.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
				getMedTotalsServiceSuccessHandler);

		getMedTotalsServiceFailedHandler = new EventHandler<WorkerStateEvent>()
		{
			@Override
			public void handle(WorkerStateEvent event)
			{
				try
				{
					Main.getInstance().getUtility().showNotification("Error", "Error",
					        getTotalMedicationVolumeListService.getException().getMessage());

					getTotalMedicationVolumeListService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
							getMedTotalsServiceSuccessHandler);
					getTotalMedicationVolumeListService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
							getMedTotalsServiceFailedHandler);
				}
				catch (Exception e)
				{
					LOG.error("## Exception occured:", e);

				}
			}
		};
		getTotalMedicationVolumeListService.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
				getMedTotalsServiceFailedHandler);


	}

	/**
	 * This method fetches list of test and their respective IDs and create
	 * Tests live grid
	 */
	private void getTestListandIDs()
	{

		GetTestListService getTestListService = GetTestListService.getInstance();
		getTestListService.restart();

		getTestListServiceSuccessHandler = new EventHandler<WorkerStateEvent>()
		{
			@Override
			public void handle(WorkerStateEvent event)
			{

				try
				{
					investigationTestsList = getTestListService.getListOfTest();
					for (Investigationstests investigationstests : investigationTestsList)
					{
						testIdsMap.put(investigationstests.getTestName().toLowerCase(), investigationstests.getInvestigationsTestsId());
					}

					String testName="",paramName="";

					for(Map.Entry<String,List<Investigationparameterfields>> entry:MasterDataSession.getInstance().getTestParamDataMap().get("TestParam").entrySet())
					{
						testName=entry.getKey();
						if (testName.equalsIgnoreCase("abg") || testName.equalsIgnoreCase("sugar") || testName.equalsIgnoreCase("act"))
						{
							paramListMap.put(testName, new ArrayList<String>());
							for (Investigationparameterfields obj : entry.getValue())
							{
								if (obj.getName() != null)
									{
									paramName = obj.getName();
									if (obj.getMeasuringUnit() != null)
									{
										if (!obj.getMeasuringUnit().getUnitSymbol().isEmpty())
											paramName = paramName + " (" + obj.getMeasuringUnit().getUnitSymbol() + ")";
									}
									else if (obj.getMeasuringUnitRatio() != null)
									{
										paramName = paramName + " ("
										        + obj.getMeasuringUnitRatio().getNumeratorUnit().getUnitSymbol() + "/"
										        + obj.getMeasuringUnitRatio().getDenominatorUnit().getUnitSymbol() + ")";
									}

									}

								paramListMap.get(testName).add(paramName);
							}

							addLegendsToGrid(paramGridMap.get(testName), rowPrefHeight,
							        paramListMap.get(testName).toArray(new String[paramListMap.get(testName).size()]));
							addGridHeader(headerTimeLblsList, textboxGridMap.get(testName), columnPrefWidth, rowPrefHeight);
							addGridRows(paramListMap.get(testName), headerTimeLblsList, textboxGridMap.get(testName),
							        textboxListMap.get(testName), columnPrefWidth, rowPrefHeight);


							textboxGridMap.get(testName).setPrefHeight((paramListMap.get(testName).size() + 2) * rowPrefHeight);
							paramGridMap.get(testName).setPrefHeight(textboxGridMap.get(testName).getPrefHeight());

							EventHandler<MouseEvent> testGridClickHandler= new EventHandler<javafx.scene.input.MouseEvent>() {
								public void handle(javafx.scene.input.MouseEvent event)
								{
									for (Node node : textboxGridMap.get(entry.getKey()).getChildren())
									{
										if (node.getBoundsInParent().contains(event.getX(), event.getY()))
										{
											Label lbl = (Label) node;
											if (lbl.getId() != null)
											{
												clickedGridCellTime = headerTimeLblsList.get(Integer.valueOf(lbl.getId().split(",")[1]));
												clickedTestGrid = entry.getKey();

												testContextMenu.show(lbl, event.getSceneX(), event.getSceneY());
												if (compareClickedcellTime())
													testContextMenu.getItems().get(0).setDisable(false);
												else
													testContextMenu.getItems().get(0).setDisable(true);
											}
											break;
										}
									}
								}
							};
							textboxGridMap.get(testName).addEventHandler(MouseEvent.MOUSE_PRESSED, testGridClickHandler);

							testsEventHandlersMap.put(textboxGridMap.get(testName), testGridClickHandler);
						}

					}



					getTestListService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
							getTestListServiceSuccessHandler);
					getTestListService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
							getTestListServiceFailedHandler);
				}
				catch (Exception e)
				{
					LOG.error("## Exception occured:", e);

				}
			}
		};
		getTestListService.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
				getTestListServiceSuccessHandler);

		getTestListServiceFailedHandler = new EventHandler<WorkerStateEvent>()
		{
			@Override
			public void handle(WorkerStateEvent event)
			{
				try
				{
					Main.getInstance().getUtility().showNotification("Error", "Error",
					        getTestListService.getException().getMessage());

					getTestListService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
							getTestListServiceSuccessHandler);
					getTestListService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
							getTestListServiceFailedHandler);
				}
				catch (Exception e)
				{
					LOG.error("## Exception occured:", e);

				}
			}
		};
		getTestListService.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
				getTestListServiceFailedHandler);

	}



	/**
	 * This method fetches totals value for a list of fluids
	 *
	 * @param fluidList list containing fluid names
	 */
	private void getFluidTotalsListService(List<String> fluidList)
	{
		GetTotalFluidVolumeService getFluidTotalsListService = GetTotalFluidVolumeService.getInstance(
		        PatientSession.getInstance().getPatientCase().getCaseId(), fluidList);
		getFluidTotalsListService.restart();


		getFluidTotalsServiceSuccessHandler = new EventHandler<WorkerStateEvent>()
		{
			@Override
			public void handle(WorkerStateEvent event)
			{
				try
				{
					Map<String, String> returnTotalsMap = getFluidTotalsListService.getFluidVolume();

					for (Map.Entry<String, String> entry : returnTotalsMap.entrySet())
					{
						totalLblsMap.get(FLUID_GRID_NAME).get(entry.getKey()).setText(entry.getValue());
						totalsListMap.get(FLUID_GRID_NAME).add(entry.getValue());
					}

					getFluidTotalsListService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
							getFluidTotalsServiceSuccessHandler);
					getFluidTotalsListService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
							getFluidTotalsServiceFailedHandler);
				}
				catch (Exception e)
				{
					LOG.error("## Exception occured:", e);

				}
			}
		};
		getFluidTotalsListService.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
				getFluidTotalsServiceSuccessHandler);

		getFluidTotalsServiceFailedHandler = new EventHandler<WorkerStateEvent>()
		{
			@Override
			public void handle(WorkerStateEvent event)
			{
				try
				{
					Main.getInstance().getUtility().showNotification("Error", "Error",
					        getFluidTotalsListService.getException().getMessage());

					getFluidTotalsListService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
							getFluidTotalsServiceSuccessHandler);
					getFluidTotalsListService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
							getFluidTotalsServiceFailedHandler);
				}
				catch (Exception e)
				{
					LOG.error("## Exception occured:", e);

				}
			}
		};
		getFluidTotalsListService.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
				getFluidTotalsServiceFailedHandler);


	}

	/**
	 * This method fetches totals value for a list of outputs
	 *
	 * @param outputList list containing total names
	 */
	private void getOutputTotalsListService(List<String> outputList)
	{
		GetTotalOutputTypeVolumeService getOutputTotalsListService = GetTotalOutputTypeVolumeService.getInstance(
		        PatientSession.getInstance().getPatientCase().getCaseId(), outputList);
		getOutputTotalsListService.restart();


		getOutputTotalsServiceSuccessHandler = new EventHandler<WorkerStateEvent>()
		{
			@Override
			public void handle(WorkerStateEvent event)
			{
				try
				{
					Map<String, String> returnTotalsMap = getOutputTotalsListService.getTotalVolume();
					for (Map.Entry<String, String> entry : returnTotalsMap.entrySet())
					{
						totalLblsMap.get(OUTPUT_GRID_NAME).get(entry.getKey()).setText(entry.getValue());
						totalsListMap.get(OUTPUT_GRID_NAME).add(entry.getValue());
					}

					getOutputTotalsListService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
							getOutputTotalsServiceSuccessHandler);
					getOutputTotalsListService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
							getOutputTotalsServiceFailedHandler);
				}
				catch (Exception e)
				{
					LOG.error("## Exception occured:", e);

				}
			}
		};
		getOutputTotalsListService.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
				getOutputTotalsServiceSuccessHandler);

		getOutputTotalsServiceFailedHandler = new EventHandler<WorkerStateEvent>()
		{
			@Override
			public void handle(WorkerStateEvent event)
			{
				try
				{
					Main.getInstance().getUtility().showNotification("Error", "Error",
					        getOutputTotalsListService.getException().getMessage());

					getOutputTotalsListService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
							getOutputTotalsServiceSuccessHandler);
					getOutputTotalsListService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
							getOutputTotalsServiceFailedHandler);
				}
				catch (Exception e)
				{
					LOG.error("## Exception occured:", e);

				}
			}
		};
		getOutputTotalsListService.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
				getOutputTotalsServiceFailedHandler);
	}

	/**
	 * This method creates Header, Body and Footer section in the passed vbox
	 *
	 * @param vbox vbox where these sections are created
	 */
	private void dividePaneInSections(VBox vbox)
	{
		setInitialTimeStamps();
		// ---HEADER AREA
		HBox header = new HBox();
		header.setStyle("-fx-border-color: silver;");
		header.setPrefSize(vbox.getPrefWidth(), vbox.getPrefHeight() * 0.15);

		HBox leftPane = new HBox();
		leftPane.setAlignment(Pos.CENTER_LEFT);
		leftPane.setPrefSize(header.getPrefWidth() * 0.5, header.getPrefHeight());
		Label space1 = new Label();
		space1.setPrefWidth(leftPane.getPrefWidth() * 0.02);
		Label space2 = new Label();
		space2.setPrefWidth(leftPane.getPrefWidth() * 0.02);
		Label timerLbl = new Label("Timeline Preferences");
		Label timerVal = new Label("  " + MINS_GAP + " mins  ");
		timerVal.setStyle("-fx-border-color:silver;-fx-background-color:rgb(58.0, 163.0, 178.0)");
		timerVal.setTextFill(Color.WHITE);
		leftPane.getChildren().addAll(space1, timerLbl, space2, timerVal);

		HBox rightPane = new HBox();
		rightPane.setAlignment(Pos.CENTER_RIGHT);
		rightPane.setPrefSize(header.getPrefWidth() * 0.5, header.getPrefHeight());
		Label space3 = new Label();
		space3.setPrefWidth(rightPane.getPrefWidth() * 0.01);
		Label space4 = new Label();
		space4.setPrefWidth(rightPane.getPrefWidth() * 0.01);
		if (vbox.getId().equalsIgnoreCase("anesthesia"))
		{
			anesHistData = new Button("Trend Chart");
			anesHistData.setDisable(true);
			anesHistData.setFont(new Font(12));
			anesHistData.setStyle("-fx-background-color:rgb(58.0, 163.0, 178.0);-fx-border-width:0px;-fx-text-fill:white;-fx-font-weight:bold ");
			rightPane.getChildren().addAll(space3, anesHistData, space4);
			addAnesHistoryButtonAction();
		}
		else if(vbox.getId().equalsIgnoreCase("s5ptmonitor"))
		{
			ptMntrHistData = new Button("Trend Chart");
			ptMntrHistData.setFont(new Font(12));
			ptMntrHistData.setDisable(true);
			ptMntrHistData.setStyle("-fx-background-color:rgb(58.0, 163.0, 178.0);-fx-border-width:0px;-fx-text-fill:white;-fx-font-weight:bold ");
			rightPane.getChildren().addAll(space3, ptMntrHistData, space4);
			addPtMntrHistoryButtonAction();
		}
		header.getChildren().addAll(leftPane, rightPane);


		// ---CENTER BODY AREA
		VBox body = new VBox();
		body.setStyle("-fx-border-color: silver");
		body.setPrefSize(vbox.getPrefWidth(), vbox.getPrefHeight() * 0.85);
		if (vbox.getId().equalsIgnoreCase("anesthesia"))
		{
			createAnesBodyArea(body);
		}
		else if (vbox.getId().equalsIgnoreCase("s5ptmonitor"))
		{
			createPtMntrBodyArea(body);
		}

		vbox.getChildren().addAll(header, body);
	}

	/**
	 * This method sets starting time for Anesthesia and S5 PtMonitor graph windows
	 */
	private void setInitialTimeStamps()
	{
		anestimelineStartTime = sdfNew.format(Calendar.getInstance().getTime());
		ptMntrtimelineStartTime = anestimelineStartTime;
//		medtimelineStartTime = anestimelineStartTime;
	}

	/**
	 * This method returns number pixels covered in TrendGraphSpeed secs
	 *
	 * @return calculated pixels
	 */
	public double pixelsPerSec()
	{
		double d = 0;

		try
		{
			d = (calculatedGraphWidth * SystemConfiguration.getInstance().getTrendGraphSpeed())
			        / ((TOTAL_TIME_MARKINGS - 1) * MINS_GAP * 60);
		}
		catch (Exception e)
		{
			LOG.error("## Exception occured:", e);
		}
		return d;
	}


	/**
	 * This method returns number of pixels covered in 1 min for historical data graph
	 *
	 * @return
	 */
	/*private double pixelsPerMinHistGraph()
	{
		return calculatedGraphWidth / ((TOTAL_TIME_MARKINGS - 1) * histMinsGap);
	}*/

	/**
	 * This method returns number of pixels covered in 1 sec for historical data graph
	 *
	 * @return calculated pixels
	 */
	public double pixelsPerSecHistGraph()
	{
		double d = 0;

		try
		{
			d = calculatedGraphWidth / ((TOTAL_TIME_MARKINGS - 1) * histMinsGap);
		}
		catch (Exception e)
		{
			LOG.error("## Exception occured:", e);
		}
		return d;
	}


	/**
	 * Action handler for HistoricalData btn on Anesthesia window
	 */
	private void addAnesHistoryButtonAction()
	{
		anesHistDataHandler = new EventHandler<MouseEvent>()
		{
			public void handle(MouseEvent event)
			{
				currentTab = "Anesthesia";
				Main.getInstance().getStageManager().switchSceneAfterMain(FxmlView.HISTORICAL_DATA);
			}
		};
		anesHistData.addEventHandler(MouseEvent.MOUSE_PRESSED, anesHistDataHandler);
	}

	/**
	 * Action handler for HistoricalData btn on S5 PtMonitor window
	 */
	private void addPtMntrHistoryButtonAction()
	{
		ptMntrHistDataHandler = new EventHandler<MouseEvent>()
		{
			public void handle(MouseEvent event)
			{
				currentTab = "Patient Monitor";
				Main.getInstance().getStageManager().switchSceneAfterMain(FxmlView.HISTORICAL_DATA);
			}
		};
		ptMntrHistData.addEventHandler(MouseEvent.MOUSE_PRESSED, ptMntrHistDataHandler);
	}

	/**
	 * Action handler for selection of tab in TabPane
	 * @param pane
	 */
	/*private void addTabPaneListener(TabPane pane)
	{
		pane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>()
		{
			@Override
			public void changed(ObservableValue<? extends Tab> ov, Tab t, Tab t1)
			{
				currentTab = t1.getText();
			}
		});
	}*/


	/**
	 * This method creates center body for S5 PtMntr tab
	 *
	 * @param PtMntrbody pane where sections for graphs, legends and timeline are divided
	 */
	private void createPtMntrBodyArea(Pane PtMntrbody)
	{
		// -------new changes for screen resize fix
		HBox base = new HBox();
		base.setPrefSize(PtMntrbody.getPrefWidth(), PtMntrbody.getPrefHeight());
		base.setSpacing(5);

		VBox baseVboxForLegends = new VBox();
		baseVboxForLegends.setPrefSize(PtMntrbody.getPrefWidth() * 0.22,
		        PtMntrbody.getPrefHeight() * 0.92);

		HBox hboxForLegends = new HBox();
		hboxForLegends.setPrefSize(baseVboxForLegends.getPrefWidth(),baseVboxForLegends.getPrefHeight());
		Pane legendsHolder = new Pane();
		legendsHolder.setPrefSize(hboxForLegends.getPrefWidth() * 0.5, hboxForLegends.getPrefHeight());
		legendsHolder.setStyle(" -fx-border-color: silver;");
		Pane scaleHolder = new Pane();
		scaleHolder.setPrefSize(hboxForLegends.getPrefWidth() * 0.5, hboxForLegends.getPrefHeight());
		scaleHolder.setStyle(" -fx-border-color: silver;");

		VBox vboxForGraph = new VBox();
		vboxForGraph.setPrefSize(PtMntrbody.getPrefWidth() * 0.78, PtMntrbody.getPrefHeight() * 0.92);
		// ------------------------------

		ptMntrTimeHBox = new HBox();
		ptMntrTimeHBox.setPrefSize(PtMntrbody.getPrefWidth(), PtMntrbody.getPrefHeight() * 0.08);

		/*HBox centerHBox = new HBox();
		centerHBox.setPrefSize(PtMntrbody.getPrefWidth(), PtMntrbody.getPrefHeight() * 0.92);
		double remainingWidth = centerHBox.getPrefWidth() - calculatedGraphWidth;*/

		// ---Legends Area
		ptMntrLegendsPanel = new Pane();
		ptMntrLegendsPanel.setPrefSize(legendsHolder.getPrefWidth(), legendsHolder.getPrefHeight());

		// ---Y Axis Scale Area
		HBox ptMntrYscalePanel = new HBox();
		ptMntrYscalePanel.setPrefSize(scaleHolder.getPrefWidth(), scaleHolder.getPrefHeight());
		createPtMntrScaleHolders(ptMntrYscalePanel);

		// ---Graph Area
		StackPane graphArea = new StackPane();
		graphArea.setPrefSize(calculatedGraphWidth, vboxForGraph.getPrefHeight());

		ptMntrBaseGraph = new Pane();
		ptMntrBaseGraph.setId("BasePtMntrPanel");
		ptMntrBaseGraph.setStyle(" -fx-border-color: silver;");
		ptMntrBaseGraph.setPrefSize(graphArea.getPrefWidth(), graphArea.getPrefHeight() * 0.975);
		Canvas baseGraphCanvas = new Canvas(ptMntrBaseGraph.getPrefWidth(), ptMntrBaseGraph.getPrefHeight());
		ptMntrBaseGraphBrush = baseGraphCanvas.getGraphicsContext2D();
		ptMntrBaseGraphBrush.setStroke(Color.SILVER);
		ptMntrBaseGraphBrush.setLineWidth(1);
		ptMntrBaseGraph.getChildren().add(baseGraphCanvas);
		drawVerticalMarkings(ptMntrBaseGraph, ptMntrBaseGraphBrush);

		ptMntrGraphPanel = new Pane();
		ptMntrGraphPanel.setId("PtMntrPanel");
		ptMntrGraphPanel.setStyle(" -fx-border-color: silver;");
		ptMntrGraphPanel.setPrefSize(graphArea.getPrefWidth(), graphArea.getPrefHeight() * 0.975);
		ptMntrGraphCanvas = new Canvas(ptMntrGraphPanel.getPrefWidth(), ptMntrGraphPanel.getPrefHeight());
		ptMntrGraphBrush = ptMntrGraphCanvas.getGraphicsContext2D();
		ptMntrGraphBrush.setStroke(Color.SILVER);
		ptMntrGraphBrush.setLineWidth(1);
		ptMntrGraphPanel.getChildren().add(ptMntrGraphCanvas);
		graphArea.getChildren().addAll(ptMntrBaseGraph, ptMntrGraphPanel);

		// ---for timePane
		Pane horizotalSpace = new Pane();
		horizotalSpace.setPrefSize(ptMntrTimeHBox.getPrefWidth() - calculatedGraphWidth,ptMntrTimeHBox.getPrefHeight());
		ptMntrTimePane = new Pane();
		ptMntrTimePane.setPrefSize(calculatedGraphWidth, ptMntrTimeHBox.getPrefHeight());// 1.01

		Canvas ptMntrTimepaneCanvas = new Canvas(ptMntrTimePane.getPrefWidth(), ptMntrTimePane.getPrefHeight());
		ptMntrTimepaneBrush = ptMntrTimepaneCanvas.getGraphicsContext2D();
		ptMntrTimepaneBrush.setLineWidth(0.5);
		ptMntrTimePane.getChildren().add(ptMntrTimepaneCanvas);
		ptMntrTimeHBox.getChildren().addAll(horizotalSpace, ptMntrTimePane);
		addTimeLabels(ptMntrTimePane, ptMntrTimepaneBrush, ptMntrtimelineStartTime, MINS_GAP, "patient monitor");

		//centerHBox.getChildren().addAll(ptMntrLegendsPanel, verticalDivider1, ptMntrYscalePanel, verticalDivider2,graphArea);
		// PtMntrbody.getChildren().addAll(ptMntrTimeHBox, centerHBox);

		legendsHolder.getChildren().addAll(ptMntrLegendsPanel);
		scaleHolder.getChildren().addAll(ptMntrYscalePanel);

		hboxForLegends.getChildren().addAll(legendsHolder, scaleHolder);
		baseVboxForLegends.getChildren().addAll(horizotalSpace, hboxForLegends);
		vboxForGraph.getChildren().addAll(ptMntrTimePane, graphArea);
		base.getChildren().addAll(baseVboxForLegends, vboxForGraph);

		PtMntrbody.getChildren().addAll(base);
	}


	/**
	 * This method creates center body for Anesthesia tab
	 *
	 * @param Anesbody pane where sections for graphs, legends and timeline are divided
	 */
	private void createAnesBodyArea(Pane Anesbody)
	{
		// -------new changes for screen resize fix
		HBox base = new HBox();
		base.setPrefSize(Anesbody.getPrefWidth(), Anesbody.getPrefHeight());
		base.setSpacing(5);

		VBox baseVboxForLegends = new VBox();
		baseVboxForLegends.setPrefSize(Anesbody.getPrefWidth() * 0.22, Anesbody.getPrefHeight() * 0.92);

		HBox hboxForLegends = new HBox();
		hboxForLegends.setPrefSize(baseVboxForLegends.getPrefWidth(), baseVboxForLegends.getPrefHeight());
		Pane legendsHolder = new Pane();
		legendsHolder.setPrefSize(hboxForLegends.getPrefWidth() * 0.5, hboxForLegends.getPrefHeight());
		legendsHolder.setStyle(" -fx-border-color: silver;");
		Pane scaleHolder = new Pane();
		scaleHolder.setPrefSize(hboxForLegends.getPrefWidth() * 0.5, hboxForLegends.getPrefHeight());
		scaleHolder.setStyle(" -fx-border-color: silver;");

		VBox vboxForGraph = new VBox();
		vboxForGraph.setPrefSize(Anesbody.getPrefWidth() * 0.78, Anesbody.getPrefHeight() * 0.92);
		// ------------------------------

		anesTimeHBox = new HBox();
		anesTimeHBox.setPrefSize(Anesbody.getPrefWidth(), Anesbody.getPrefHeight() * 0.08);

		/*HBox centerHBox = new HBox();
		centerHBox.setPrefSize(Anesbody.getPrefWidth(), Anesbody.getPrefHeight() * 0.92);*/

		// ---Legends Area
		anesLegendsPanel = new Pane();
		anesLegendsPanel.setPrefSize(legendsHolder.getPrefWidth(), legendsHolder.getPrefHeight());

		// ---Y Axis Scale Area
		HBox anesYscalePanel = new HBox();
		anesYscalePanel.setPrefSize(scaleHolder.getPrefWidth(), scaleHolder.getPrefHeight());
		createAnesScaleHolders(anesYscalePanel);


		// ---Graph Area
		StackPane graphArea = new StackPane();
		graphArea.setPrefSize(calculatedGraphWidth, vboxForGraph.getPrefHeight());

		anesBaseGraph = new Pane();
		anesBaseGraph.setId("BaseAnesPanel");
		anesBaseGraph.setStyle(" -fx-border-color:silver;");
		anesBaseGraph.setPrefSize(graphArea.getPrefWidth(), graphArea.getPrefHeight() * 0.975);
		Canvas baseGraphCanvas = new Canvas(anesBaseGraph.getPrefWidth(), anesBaseGraph.getPrefHeight());
		anesBaseGraphBrush = baseGraphCanvas.getGraphicsContext2D();
		anesBaseGraphBrush.setStroke(Color.SILVER);
		anesBaseGraphBrush.setLineWidth(1);
		anesBaseGraph.getChildren().add(baseGraphCanvas);
		drawVerticalMarkings(anesBaseGraph, anesBaseGraphBrush);

		anesGraphPanel = new Pane();
		anesGraphPanel.setId("AnesPanel");
		anesGraphPanel.setStyle(" -fx-border-color:silver;");
		anesGraphPanel.setPrefSize(graphArea.getPrefWidth(), graphArea.getPrefHeight() * 0.975);
		anesGraphCanvas = new Canvas(anesGraphPanel.getPrefWidth(), anesGraphPanel.getPrefHeight());
		anesGraphBrush = anesGraphCanvas.getGraphicsContext2D();
		anesGraphBrush.setStroke(Color.SILVER);
		anesGraphBrush.setLineWidth(1);
		anesGraphPanel.getChildren().add(anesGraphCanvas);
		graphArea.getChildren().addAll(anesBaseGraph, anesGraphPanel);


		// ---for timePane
		Pane horizotalSpace = new Pane();
		horizotalSpace.setPrefSize(anesTimeHBox.getPrefWidth() - calculatedGraphWidth, anesTimeHBox.getPrefHeight());
		anesTimePane = new Pane();
		anesTimePane.setPrefSize(calculatedGraphWidth , anesTimeHBox.getPrefHeight());// * 1.01
		Canvas anesTimepaneCanvas = new Canvas(anesTimePane.getPrefWidth(), anesTimePane.getPrefHeight());
		anesTimepaneBrush = anesTimepaneCanvas.getGraphicsContext2D();
		anesTimepaneBrush.setLineWidth(0.5);
		anesTimePane.getChildren().add(anesTimepaneCanvas);
		anesTimeHBox.getChildren().addAll(horizotalSpace, anesTimePane);
		addTimeLabels(anesTimePane, anesTimepaneBrush, anestimelineStartTime, MINS_GAP, "anesthesia");

		// centerHBox.getChildren().addAll(anesLegendsPanel, verticalDivider1,
		// anesYscalePanel, verticalDivider2, graphArea);
		// Anesbody.getChildren().addAll(anesTimeHBox, centerHBox);

		legendsHolder.getChildren().addAll(anesLegendsPanel);
		scaleHolder.getChildren().addAll(anesYscalePanel);

		hboxForLegends.getChildren().addAll(legendsHolder, scaleHolder);
		baseVboxForLegends.getChildren().addAll(horizotalSpace, hboxForLegends);
		vboxForGraph.getChildren().addAll(anesTimePane, graphArea);
		base.getChildren().addAll(baseVboxForLegends, vboxForGraph);

		Anesbody.getChildren().addAll(base);

	}

	/**
	 * This method divides Anesthesia Y-Scale into multiple scale holders
	 *
	 * @param basePane pane which need to be divided into multiple scale holders
	 */
	private void createAnesScaleHolders(HBox basePane)
	{
		basePane.setPrefWidth(basePane.getPrefWidth() * 0.75);

		Pane verticalSpace = new Pane();
		verticalSpace.setPrefSize(basePane.getPrefWidth() * 0.08, basePane.getPrefHeight());

		anesYScaleHolder1 = new Pane();
		// anesYScaleHolder1.setStyle(" -fx-border-color:silver;");
		anesYScaleHolder1.setPrefSize(basePane.getPrefWidth() * 0.25, basePane.getPrefHeight());

		anesYScaleHolder2 = new Pane();
		// anesYScaleHolder2.setStyle(" -fx-border-color:silver;");
		anesYScaleHolder2.setPrefSize(basePane.getPrefWidth() * 0.25, basePane.getPrefHeight());

		anesYScaleHolder3 = new Pane();
		// anesYScaleHolder3.setStyle(" -fx-border-color:silver;");
		anesYScaleHolder3.setPrefSize(basePane.getPrefWidth() * 0.25, basePane.getPrefHeight());

		anesYScaleHolder4 = new Pane();
		// anesYScaleHolder4.setStyle(" -fx-border-color:silver;");
		anesYScaleHolder4.setPrefSize(basePane.getPrefWidth() * 0.25, basePane.getPrefHeight());

		basePane.getChildren().addAll(anesYScaleHolder1, anesYScaleHolder2,
		        anesYScaleHolder3, anesYScaleHolder4);
	}

	/**
	 * This method divides PtMonitor Y-Scale into multiple scale holders
	 *
	 * @param basePane pane which need to be divided into multiple scale holders
	 */
	private void createPtMntrScaleHolders(HBox basePane)
	{
		basePane.setPrefWidth(basePane.getPrefWidth() * 0.75);

		Pane verticalSpace = new Pane();
		verticalSpace.setPrefSize(basePane.getPrefWidth() * 0.08, basePane.getPrefHeight());

		ptMntrYScaleHolder1 = new Pane();
		// ptMntrYScaleHolder1.setStyle(" -fx-border-color:silver;");
		ptMntrYScaleHolder1.setPrefSize(basePane.getPrefWidth() * 0.25, basePane.getPrefHeight());

		ptMntrYScaleHolder2 = new Pane();
		// ptMntrYScaleHolder2.setStyle(" -fx-border-color:silver;");
		ptMntrYScaleHolder2.setPrefSize(basePane.getPrefWidth() * 0.25, basePane.getPrefHeight());

		ptMntrYScaleHolder3 = new Pane();
		// ptMntrYScaleHolder3.setStyle(" -fx-border-color:silver;");
		ptMntrYScaleHolder3.setPrefSize(basePane.getPrefWidth() * 0.25, basePane.getPrefHeight());

		ptMntrYScaleHolder4 = new Pane();
		// ptMntrYScaleHolder4.setStyle(" -fx-border-color:silver;");
		ptMntrYScaleHolder4.setPrefSize(basePane.getPrefWidth() * 0.25, basePane.getPrefHeight());

		basePane.getChildren().addAll(ptMntrYScaleHolder1, ptMntrYScaleHolder2, ptMntrYScaleHolder3,
		        ptMntrYScaleHolder4);
	}

	/**
	 * This method adds passed list of legends to legendsPanel
	 *
	 * @param legendsPanel pane where legends are added
	 * @param legendsList list of legend names
	 */
	private void addLegendLabels(Pane legendsPanel, List<String> legendsList)
	{
		VBox vbox = new VBox();
		// vbox.setStyle("-fx-border-color: red");
		vbox.setPrefSize(legendsPanel.getPrefWidth() * 0.75, legendsPanel.getPrefHeight());
		vbox.setAlignment(Pos.CENTER);

		for (String legend : legendsList)
		{
			HBox hbox = new HBox();
			hbox.setPrefWidth(vbox.getPrefWidth());
			hbox.setPrefHeight(vbox.getPrefHeight() * 0.08);
			hbox.setAlignment(Pos.CENTER_LEFT);
			Label spacing = new Label();
			spacing.setPrefWidth(vbox.getPrefWidth() * 0.15);
			Label lblIcon = new Label(legend.split("\\|")[0] + "  ");
			lblIcon.setTextFill(Color.web(legend.split("\\|")[2]));
			lblIcon.setStyle("-fx-font-size:" + vbox.getPrefWidth() * 0.13 + "px;-fx-font-weight:bold");
			Label lblName = new Label(legend.split("\\|")[1]);
			lblName.setStyle("-fx-font-size:" + vbox.getPrefWidth() * 0.13 + "px;-fx-font-family:Calibri");
			lblName.setTextFill(Color.web(legend.split("\\|")[2]));
			hbox.getChildren().addAll(spacing, lblIcon, lblName);
			vbox.getChildren().add(hbox);
		}
		legendsPanel.getChildren().add(vbox);
	}

	/**
	 * This method adds time labels to timePane
	 *
	 * @param timePane pane which holds time labels
	 * @param startTime timestamp from which timeline starts
	 */
	private void addTimeLabels(Pane timePane, GraphicsContext timePaneBrush, String startTime, int minGap,
	        String tabName)
	{
		double layoutX = 0;
		Calendar cal = Calendar.getInstance();
		try
		{
			cal.setTime(sdfNew.parse(startTime));
			//cal.set(Calendar.HOUR_OF_DAY, Integer.valueOf(startTime.split(" ")[1].split(":")[0]));
			//cal.set(Calendar.HOUR, Integer.valueOf(startTime.split(" ")[1].split(":")[0]));
		}
		catch (ParseException e){
			LOG.error("## Exception occured:", e);
		}

		int iEndVal = new Double(timePane.getPrefWidth() / TIME_PIXELS_GAP).intValue();
		for (int i = 0; i <= iEndVal; i++)
		{
			String timeVal = sdfNew.format(cal.getTime());
			Label lbl = new Label(timeVal.split(" ")[1]);
			lbl.setStyle("-fx-font-size:" + timePane.getPrefWidth() * 0.01 + "px;");
			lbl.setLayoutX(layoutX);

			if (!tabName.toLowerCase().contains("history"))
				timePaneBrush.strokeText(lbl.getText(), layoutX, timePane.getPrefHeight() / 2);

			timeLblCoordinatesList.add(layoutX);
			if (timeLblsList.size() < TOTAL_TIME_MARKINGS)
				timeLblsList.add(lbl.getText());
			layoutX += TIME_PIXELS_GAP;
			if (i == 0)
			{
				layoutX -= timePane.getPrefWidth() * 0.015; // * 0.012

				if (tabName.toLowerCase().contains("anesthesia"))
				{
					anesFirstTimeLbl = timeVal;
				}
				else if (tabName.toLowerCase().contains("patient") && tabName.toLowerCase().contains("monitor"))
				{
					ptMntrFirstTimeLbl = timeVal;
				}
				else if (tabName.toLowerCase().contains("history"))
				{
					histFirstDateTimeLbl = timeVal;
				}
				else if (tabName.toLowerCase().contains("med"))
				{
					medFirstTimeLbl = timeVal;
				}

			}
			else if (i == iEndVal - 1)
			{
				layoutX -= timePane.getPrefWidth() * 0.016;
			}
			else if (i == iEndVal)
			{

				if (tabName.toLowerCase().contains("anesthesia"))
				{
					anesLastTimeLbl = timeVal;
				}
				else if (tabName.toLowerCase().contains("patient") && tabName.toLowerCase().contains("monitor"))
				{
					ptMntrLastTimeLbl = timeVal;
				}
				else if (tabName.toLowerCase().contains("history"))
				{
					histLastDateTimeLbl = timeVal;
				}
				else if (tabName.toLowerCase().contains("med"))
				{
					medLastTimeLbl = timeVal;
				}
			}

			cal.add(Calendar.MINUTE, minGap);

			if (tabName.toLowerCase().contains("history"))
				timePane.getChildren().add(lbl);

		}

	}

	/**
	 * This method draws vertical lines on passed graphPanel
	 *
	 * @param graphPanel pane where graph lines are drawn
	 * @param graphBrush GraphicsContext component to stroke lines
	 */
	private void drawVerticalMarkings(Pane graphPanel, GraphicsContext graphBrush)
	{
		double xValue = 0, graphHeight = graphPanel.getPrefHeight();
		if (graphPanel.getId().equalsIgnoreCase("HistoryPanel"))
			graphHeight = graphPanel.getPrefHeight();
		for (int i = 0; i <= (TOTAL_TIME_MARKINGS - 1); i++)
		{
			graphBrush.strokeLine(xValue, 0, xValue, graphHeight);
			xValue += TIME_PIXELS_GAP;
		}
	}


	 /**
	  * This method adds passed Y-Scale array to scaleHolder pane. It also draws horizontal lines on graphPanel
	  *
	  * @param scaleArr array holding y scale values
	  * @param isBaseScale if it is a base scale or not
	  * @param scaleHolder pane where scale need to be added
	  * @param graphBrush GraphicsContext component to stroke lines
	  * @param graphWidth width of graph component
	  */
	private void addYScale(Double scaleArr[], boolean isBaseScale, Pane scaleHolder, GraphicsContext graphBrush,
	        double graphWidth, String ScaleColor)
	{
		if (scaleArr.length != 0)
		{
			scaleHolder.getChildren().clear();
			double max = scaleArr[0], gap = scaleArr[1];
			max -= gap;

			HBox lblBox = new HBox();
			Label ylbl = new Label();
			int yEndVal = new Double(max / gap).intValue();
			double ylblValue = max;
			double yIncrement = scaleHolder.getPrefHeight() / (yEndVal + 1), layoutY = yIncrement;

			for (int y = 0; y < yEndVal; y++)
			{
				lblBox = new HBox();
				lblBox.setPrefWidth(scaleHolder.getPrefWidth());
				lblBox.setAlignment(Pos.CENTER_RIGHT);

				if (String.valueOf(ylblValue).endsWith(".0"))
					ylbl = new Label(String.valueOf(new Double(ylblValue).intValue()));
				else
					ylbl = new Label(String.valueOf(ylblValue));

				ylbl.setStyle("-fx-font-size:" + scaleHolder.getPrefWidth() * 0.35 + "px;-fx-font-weight:bold");
				ylbl.setTextFill(Color.web(ScaleColor));
				lblBox.setLayoutY(layoutY - scaleHolder.getPrefHeight() * 0.03);

				lblBox.getChildren().add(ylbl);
				scaleHolder.getChildren().add(lblBox);

				if (isBaseScale)
				{
					graphBrush.strokeLine(0, layoutY, graphWidth, layoutY);
				}

				layoutY += yIncrement;
				ylblValue -= gap;
			}
		}

	}


	/**
	 * This method creates Header, Body and Footer section in the Historical data window
	 *
	 * @param vbox vbox where these sections are created
	 */
	public void createHistoricalDataBody(VBox vbox)
	{
		try
		{
			histMinsGap = 15;
			// ---Calculations for CurrentPage and TotalPages
			currentPgValue = 1;
			minsPerWindow = histMinsGap * (TOTAL_TIME_MARKINGS - 1);
			pageFactor=1/minsPerWindow;
			totalPgValue = new Double(Math.ceil(pageFactor * totalCSVrecords)).intValue();
			if (totalPgValue == 0)
				totalPgValue = 1;

			// ---HEADER AREA
			HBox header = new HBox();
			// header.setStyle("-fx-border-color: red;");
			header.setPrefSize(vbox.getPrefWidth(), vbox.getPrefHeight() * 0.08);

			HBox leftPane = new HBox();
			leftPane.setAlignment(Pos.CENTER_LEFT);
			leftPane.setPrefSize(header.getPrefWidth() * 0.5, header.getPrefHeight());
			Label space1 = new Label();
			space1.setPrefWidth(leftPane.getPrefWidth() * 0.02);
			Label space2 = new Label();
			space2.setPrefWidth(leftPane.getPrefWidth() * 0.02);
			Label timerLbl = new Label("Set Timeline Preferences");
			timeDropdown = new ComboBox<Integer>();
			timeDropdown.getItems().clear();
			timeDropdown.getItems().addAll(timeLblsGapArr);
			timeDropdown.getSelectionModel().select(4);
			leftPane.getChildren().addAll(space1, timerLbl, space2, timeDropdown);

			HBox rightPane = new HBox();
			rightPane.setAlignment(Pos.CENTER_RIGHT);
			rightPane.setPrefSize(header.getPrefWidth() * 0.5, header.getPrefHeight());
			Label lblPage = new Label("Page : ");
			currentPageLbl = new Label(String.valueOf(currentPgValue));
			totalPagesLbl = new Label(String.valueOf(totalPgValue));
			Label slashSymbol = new Label("/");
			slashSymbol.setPrefWidth(rightPane.getPrefWidth() * 0.01);
			Label blankSpace = new Label();
			blankSpace.setPrefWidth(rightPane.getPrefWidth() * 0.01);
			Label blankSpace1 = new Label();
			blankSpace1.setPrefWidth(rightPane.getPrefWidth() * 0.01);

			start = new Button("|\u25C0");
			start.setFont(new Font(15));
			start.setDisable(true);
			start.setStyle("-fx-background-color:rgb(58.0, 163.0, 178.0);-fx-border-width:0px;-fx-text-fill:white ");
			end = new Button("\u25B6|");
			end.setFont(new Font(15));
			end.setStyle("-fx-background-color:rgb(58.0, 163.0, 178.0);-fx-border-width:0px;-fx-text-fill:white ");
			next = new Button("\u25B6");
			next.setFont(new Font(15));
			next.setStyle("-fx-background-color:rgb(58.0, 163.0, 178.0);-fx-border-width:0px;-fx-text-fill:white ");
			prev = new Button("\u25C0");
			prev.setFont(new Font(15));
			prev.setDisable(true);
			prev.setStyle("-fx-background-color:rgb(58.0, 163.0, 178.0);-fx-border-width:0px;-fx-text-fill:white ");
			Label space3 = new Label();
			space3.setPrefWidth(rightPane.getPrefWidth() * 0.01);
			Label space4 = new Label();
			space4.setPrefWidth(rightPane.getPrefWidth() * 0.01);
			Label space5 = new Label();
			space5.setPrefWidth(rightPane.getPrefWidth() * 0.01);
			Label space6 = new Label();
			space6.setPrefWidth(rightPane.getPrefWidth() * 0.01);
			rightPane.getChildren().addAll(lblPage,currentPageLbl, slashSymbol, totalPagesLbl, blankSpace, start,
											space3, prev, space4,next, space5, end, space6);
			header.getChildren().addAll(leftPane, rightPane);



			// ---CENTER BODY AREA
			VBox body = new VBox();
			body.setStyle("-fx-border-color: silver");
			body.setPrefSize(vbox.getPrefWidth(), vbox.getPrefHeight() * 0.9);
			createHistoricalDataArea(body);

			addButtonActions(start, end, next, prev);

			vbox.getChildren().addAll(header, body);
		}
		catch (Exception e)
		{
			LOG.error("## Exception occured:", e);

		}
	}

	/**
	 * This method creates center body for Historical data window
	 *
	 * @param body pane in which section for graph, legend and timeline are divided
	 */
	private void createHistoricalDataArea(Pane body)
	{

		if (ControllerMediator.getInstance().getMainController().getDrawTabbedCenter().getCurrentTab().toLowerCase().contains("anesthesia"))
		{
			histLegendsArr = anesLegendsArr;
			histScalesList = anesScalesList;

		}
		else if (ControllerMediator.getInstance().getMainController().getDrawTabbedCenter().getCurrentTab().toLowerCase().contains("patient")
		        && ControllerMediator.getInstance().getMainController().getDrawTabbedCenter().getCurrentTab().toLowerCase().contains("monitor"))
		{
			histLegendsArr = ptMntrLegendsArr;
			histScalesList = ptMntrScalesList;
		}

		TIME_PIXELS_GAP = centerPane.getPrefWidth() * 0.0975;
		calculatedGraphWidth = calcGraphWidth();

		//---------------new changes for screen resize fix
		HBox base = new HBox();
		base.setPrefSize(body.getPrefWidth(), body.getPrefHeight());
		base.setSpacing(5);

		VBox baseVboxForLegends = new VBox();
		baseVboxForLegends.setPrefSize(body.getPrefWidth() * 0.2, body.getPrefHeight() * 0.96);


		HBox hboxForLegends = new HBox();
		hboxForLegends.setPrefSize(baseVboxForLegends.getPrefWidth(), baseVboxForLegends.getPrefHeight());
		Pane legendsHolder = new Pane();
		legendsHolder.setPrefSize(hboxForLegends.getPrefWidth() * 0.5, hboxForLegends.getPrefHeight());
		legendsHolder.setStyle(" -fx-border-color: silver;");
		Pane scaleHolder = new Pane();
		scaleHolder.setPrefSize(hboxForLegends.getPrefWidth() * 0.5, hboxForLegends.getPrefHeight());
		scaleHolder.setStyle(" -fx-border-color: silver;");

		VBox vboxForGraph = new VBox();
		vboxForGraph.setPrefSize(body.getPrefWidth() * 0.8, body.getPrefHeight() * 0.96);
		//--------------------------

		histTimeHBox = new HBox();
		histTimeHBox.setPrefSize(calculatedGraphWidth, body.getPrefHeight() * 0.04);


		// ---Legends Area
		histLegendsPanel = new Pane();
		histLegendsPanel.setPrefSize(legendsHolder.getPrefWidth(), legendsHolder.getPrefHeight());


		// ---Y Axis Scale Area
		HBox histYscalePanel = new HBox();
		histYscalePanel.setPrefSize(scaleHolder.getPrefWidth(), scaleHolder.getPrefHeight());
		createHistScaleHolders(histYscalePanel);


		// ---Graph Area
		histGraphPanel = new Pane();
		histGraphPanel.setId("HistoryPanel");
		histGraphPanel.setStyle(" -fx-border-color: silver;");
		histGraphPanel.setPrefSize(calculatedGraphWidth, vboxForGraph.getPrefHeight());
		histGraphCanvas = new Canvas(histGraphPanel.getPrefWidth(), histGraphPanel.getPrefHeight());
		histGraphBrush = histGraphCanvas.getGraphicsContext2D();
		histGraphBrush.setStroke(Color.SILVER);
		histGraphBrush.setLineWidth(1);
		histGraphPanel.getChildren().add(histGraphCanvas);
		drawVerticalMarkings(histGraphPanel, histGraphBrush);
		addHistYScales(histLegendsArr);

		// ---for timePane
		Pane horizotalSpace = new Pane();
		horizotalSpace.setPrefSize(histTimeHBox.getPrefWidth() - calculatedGraphWidth, histTimeHBox.getPrefHeight());
		histTimePane = new Pane();
		// histTimePane.setStyle(" -fx-border-color: blue;");
		histTimePane.setPrefSize(histTimeHBox.getPrefWidth(), histTimeHBox.getPrefHeight());
		histTimeHBox.getChildren().addAll(histTimePane);
		addTimeLabels(histTimePane, null, histStartDateTime, histMinsGap, "history");
		comboBoxListener(timeDropdown, histTimeHBox, histTimePane);

		legendsHolder.getChildren().addAll(histLegendsPanel);
		scaleHolder.getChildren().addAll(histYscalePanel);

		hboxForLegends.getChildren().addAll(legendsHolder, scaleHolder);
		baseVboxForLegends.getChildren().addAll(horizotalSpace, hboxForLegends);
		vboxForGraph.getChildren().addAll(histTimeHBox, histGraphPanel);
		base.getChildren().addAll(baseVboxForLegends, vboxForGraph);

		body.getChildren().addAll(base);

	}

	/**
	 * This method divides Historical Data Screen Y-Scale into multiple scale
	 * holders
	 *
	 * @param basePane hbox where multiple sclae holders are cerated
	 */
	private void createHistScaleHolders(HBox basePane)
	{
		Pane verticalSpace = new Pane();
		verticalSpace.setPrefSize(basePane.getPrefWidth() * 0.08, basePane.getPrefHeight());

		histYScaleHolder1 = new Pane();
		// histYScaleHolder1.setStyle(" -fx-border-color:silver;");
		histYScaleHolder1.setPrefSize(basePane.getPrefWidth() * 0.23, basePane.getPrefHeight());

		histYScaleHolder2 = new Pane();
		// histYScaleHolder2.setStyle(" -fx-border-color:silver;");
		histYScaleHolder2.setPrefSize(basePane.getPrefWidth() * 0.23, basePane.getPrefHeight());

		histYScaleHolder3 = new Pane();
		// histYScaleHolder3.setStyle(" -fx-border-color:silver;");
		histYScaleHolder3.setPrefSize(basePane.getPrefWidth() * 0.23, basePane.getPrefHeight());

		histYScaleHolder4 = new Pane();
		// histYScaleHolder4.setStyle(" -fx-border-color:silver;");
		histYScaleHolder4.setPrefSize(basePane.getPrefWidth() * 0.23, basePane.getPrefHeight());

		basePane.getChildren().addAll(histYScaleHolder1, histYScaleHolder2, histYScaleHolder3, histYScaleHolder4,
		        verticalSpace);
	}

	/**
	 * This method compares provided dates
	 *
	 * @param dateTime1 first date object
	 * @param dateTime2 second date object
	 * @return 0, 1 or -1 based on comparison
	 */
	private int compareDates(String dateTime1, String dateTime2)
	{
		int result = 0;

		try
		{
			Calendar date1 = Calendar.getInstance();
			Calendar date2 = Calendar.getInstance();

			date1.setTime(sdfNew.parse(dateTime1));
			date2.setTime(sdfNew.parse(dateTime2));

			if (date1.get(Calendar.YEAR) > date2.get(Calendar.YEAR))
			{
				result = 1;
			}
			else if (date1.get(Calendar.YEAR) < date2.get(Calendar.YEAR))
			{
				result = -1;
			}
			else if (date1.get(Calendar.YEAR) == date2.get(Calendar.YEAR))
			{
				if (date1.get(Calendar.MONTH) > date2.get(Calendar.MONTH))
				{
					result = 1;
				}
				else if (date1.get(Calendar.MONTH) < date2.get(Calendar.MONTH))
				{
					result = -1;
				}
				else if (date1.get(Calendar.MONTH) == date2.get(Calendar.MONTH))
				{
					if (date1.get(Calendar.DAY_OF_MONTH) > date2.get(Calendar.DAY_OF_MONTH))
					{
						result = 1;
					}
					else if (date1.get(Calendar.DAY_OF_MONTH) < date2.get(Calendar.DAY_OF_MONTH))
					{
						result = -1;
					}
					else if (date1.get(Calendar.DAY_OF_MONTH) == date2.get(Calendar.DAY_OF_MONTH))
					{
						result = 0;
					}
				}
			}
		}
		catch (ParseException e)
		{
			LOG.error("## Exception occured:", e);
		}

		return result;
	}

	/**
	 * This method compares provided time values
	 *
	 * @param dateTime1 first date object
	 * @param dateTime2 second date object
	 * @return 0, 1 or -1 based on comparison
	 */
	private int compareTime(String dateTime1, String dateTime2)
	{
		int result = 1;
		try
		{
			Calendar date1 = Calendar.getInstance();
			Calendar date2 = Calendar.getInstance();

			date1.setTime(sdfNew.parse(dateTime1));
			date2.setTime(sdfNew.parse(dateTime2));

			if (date1.get(Calendar.HOUR_OF_DAY) > date2.get(Calendar.HOUR_OF_DAY))
			{
				result = 1;
			}
			else if (date1.get(Calendar.HOUR_OF_DAY) < date2.get(Calendar.HOUR_OF_DAY))
			{
				result = -1;
			}
			if (date1.get(Calendar.HOUR_OF_DAY) == date2.get(Calendar.HOUR_OF_DAY))
			{
				if (date1.get(Calendar.MINUTE) > date2.get(Calendar.MINUTE))
				{
					result = 1;
				}
				else if (date1.get(Calendar.MINUTE) < date2.get(Calendar.MINUTE))
				{
					result = -1;
				}
				else if (date1.get(Calendar.MINUTE) == date2.get(Calendar.MINUTE))
				{
					result = 0;
				}

			}

		}
		catch (ParseException e)
		{
			LOG.error("## Exception occured:", e);
		}
		return result;
	}

	/**
	 * This method handles the event on TimelinePreferences ComboBox for
	 * Anesthesia and PtMonitor
	 *
	 * @param timeValues possible values of time gap
	 */
	private void comboBoxListener(ComboBox<Integer> timeValues, HBox timePaneHolder, Pane timePane)
	{
		/*comboboxchangeListener=new ChangeListener<Integer>()
		{
	        @Override
	        public void changed(ObservableValue obs, Integer oldItem, Integer newItem)
	        {
				histMinsGap = newItem;

				currentPgValue = 1;
				currentPageLbl.setText(String.valueOf(currentPgValue));

				minsPerWindow = histMinsGap * (TOTAL_TIME_MARKINGS - 1);
				pageFactor = 1 / minsPerWindow;
				totalPgValue = new Double(Math.ceil(pageFactor * totalCSVrecords)).intValue();
				if (totalPgValue == 0)
					totalPgValue = 1;
				totalPagesLbl.setText(String.valueOf(totalPgValue));

				// ---remove old time pane
				timePaneHolder.getChildren().remove(0);
				// ---clear time pane and add it again
				timePane.getChildren().clear();
				timePaneHolder.getChildren().add(timePane);
				// ---add labels to time pane
				addTimeLabels(timePane, null, histStartDateTime, histMinsGap, "history");

				clearGraphArea(histGraphPanel, histGraphCanvas, histGraphBrush, histBaseYScale, histYScaleHolder4);

				start.setDisable(true);
				prev.setDisable(true);
				end.setDisable(false);
				next.setDisable(false);

				disableAllBtns();

	        }
	    };
	    timeValues.valueProperty().addListener(comboboxchangeListener);*/

		timeValues.valueProperty().addListener((obs, oldItem, newItem) ->
		{
			try
			{
				histMinsGap = Integer.valueOf(newItem);

				currentPgValue = 1;
				currentPageLbl.setText(String.valueOf(currentPgValue));

				minsPerWindow = histMinsGap * (TOTAL_TIME_MARKINGS - 1);
				pageFactor = 1 / minsPerWindow;
				totalPgValue = new Double(Math.ceil(pageFactor * totalCSVrecords)).intValue();
				if (totalPgValue == 0)
					totalPgValue = 1;
				totalPagesLbl.setText(String.valueOf(totalPgValue));

				// ---remove old time pane
				timePaneHolder.getChildren().remove(0);
				// ---clear time pane and add it again
				timePane.getChildren().clear();
				timePaneHolder.getChildren().add(timePane);
				// ---add labels to time pane
				addTimeLabels(timePane, null, histStartDateTime, histMinsGap, "history");

				clearGraphArea(histGraphPanel, histGraphCanvas, histGraphBrush, histBaseYScale, histYScaleHolder4);

				start.setDisable(true);
				prev.setDisable(true);
				end.setDisable(false);
				next.setDisable(false);

				disableAllBtns();
			}
			catch (Exception e)
			{
				LOG.error("## Exception occured:", e);

			}

		});
	}


	/**
	 * This method clears graphPanel and redraws vertical and horizontal lines. It also resets base Y scale
	 *
	 * @param graphPanel pane which need to be cleared
	 * @param canvas canvas associated with this graphPanel
	 * @param graphBrush GraphicsContext component to stroke lines
	 * @param scale array of Y scale values
	 * @param scaleHolder pane which hold Y scales
	 */
	private void clearGraphArea(Pane graphPanel, Canvas canvas, GraphicsContext graphBrush, Double scale[],
	        Pane scaleHolder)
	{
		graphPanel.getChildren().clear();

		canvas = new Canvas(graphPanel.getPrefWidth(), graphPanel.getPrefHeight());
		graphBrush = canvas.getGraphicsContext2D();
		graphBrush.setStroke(Color.SILVER);
		graphBrush.setLineWidth(1);
		graphPanel.getChildren().add(canvas);
		ControllerMediator.getInstance().getMainController().getDrawTabbedCenter().setHistGraphBrush(graphBrush);

		drawVerticalMarkings(graphPanel, graphBrush);
		addYScale(scale, true, scaleHolder, graphBrush, graphPanel.getPrefWidth(), yscale4Color);


		HistoricalDataController obj = ControllerMediator.getInstance().getHistoricalDataController();
		Map<String, String[]> paramValsList = ExtractDataForTrends.getInstance().extract(obj.getCsvData(),
		        histFirstDateTimeLbl, histLastDateTimeLbl);
		if (ControllerMediator.getInstance().getMainController().getDrawTabbedCenter().getCurrentTab().toLowerCase()
		        .contains("anesthesia"))
		{
			 obj.plotStaticAnesGraph(paramValsList);
		}
		else if (ControllerMediator.getInstance().getMainController().getDrawTabbedCenter().getCurrentTab()
		        .toLowerCase().contains("patient")
		        && ControllerMediator.getInstance().getMainController().getDrawTabbedCenter().getCurrentTab()
		                .toLowerCase().contains("monitor"))
		{
			 obj.plotStaticPtMntrGraph(paramValsList);
		}
	}

	/**
	 * This method resets all time labels in timeline from provided starting time and clears entire graph area
	 *
	 * @param startingPt timestamp from which timeline starts
	 */
	private void recreateHistTimeLine(String startingPt)
	{
		// ---remove old time pane
		histTimeHBox.getChildren().remove(0);
		// ---add new time pane
		histTimePane.getChildren().clear();
		histTimeHBox.getChildren().add(histTimePane);
		// ---add labels to time pane
		addTimeLabels(histTimePane, null, startingPt, histMinsGap, "history");

		clearGraphArea(histGraphPanel, histGraphCanvas, histGraphBrush, histBaseYScale, histYScaleHolder4);

	}

	/**
	 * This method disables all start, end, next, prev controls
	 */
	private void disableAllBtns()
	{
		if (compareDates(histEndDateTime, histLastDateTimeLbl) < 0)
		{
			start.setDisable(true);
			end.setDisable(true);
			next.setDisable(true);
			prev.setDisable(true);
		}
		else if (compareDates(histEndDateTime, histLastDateTimeLbl) == 0)
		{
			if(compareTime(histEndDateTime, histLastDateTimeLbl) <= 0)
			{
				start.setDisable(true);
				end.setDisable(true);
				next.setDisable(true);
				prev.setDisable(true);
			}
		}
	}

	/**
	 * This method sets actions for passed buttons
	 */
	private void addButtonActions(Button start, Button end, Button next, Button prev)
	{
		disableAllBtns();
		start.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent arg0)
			{
				try
				{
					currentPgValue = 1;
					currentPageLbl.setText(String.valueOf(currentPgValue));
					start.setDisable(true);
					prev.setDisable(true);
					end.setDisable(false);
					next.setDisable(false);

					recreateHistTimeLine(histStartDateTime);
				}
				catch (Exception e)
				{
					LOG.error("## Exception occured:", e);

				}
			}

		});

		end.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent arg0)
			{
				try
				{
					currentPgValue = totalPgValue;
					currentPageLbl.setText(String.valueOf(currentPgValue));
					end.setDisable(true);
					next.setDisable(true);
					start.setDisable(false);
					prev.setDisable(false);


					try
					{
						Calendar cal = Calendar.getInstance();
						cal.setTime(sdfNew.parse(histEndDateTime));
						cal.add(Calendar.MINUTE, -(histMinsGap * (TOTAL_TIME_MARKINGS - 1)));
						cal.add(Calendar.MINUTE, 1);
						recreateHistTimeLine(sdfNew.format(cal.getTime()));
					}
					catch (ParseException e)
					{
						LOG.error("## Exception occured:", e);
					}
				}
				catch (Exception e)
				{
					LOG.error("## Exception occured:", e);

				}

			}

		});

		next.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent arg0)
			{
				try
				{
					prev.setDisable(false);
					start.setDisable(false);
					try
					{
						currentPgValue += 1;
						currentPageLbl.setText(String.valueOf(currentPgValue));
						recreateHistTimeLine(histLastDateTimeLbl);

						if (compareDates(histEndDateTime, histLastDateTimeLbl) < 0)
						{
							end.setDisable(true);
							next.setDisable(true);
						}
						else if (compareDates(histEndDateTime, histLastDateTimeLbl) == 0)
						{
							if (compareTime(histEndDateTime, histLastDateTimeLbl) <= 0)
							{
								end.setDisable(true);
								next.setDisable(true);
							}
						}

					}
					catch (Exception e)
					{
						LOG.error("## Exception occured:", e);
					}
				}
				catch (Exception e)
				{
					LOG.error("## Exception occured:", e);

				}
			}

		});

		prev.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent arg0)
			{
				try
				{
					next.setDisable(false);
					end.setDisable(false);

					try{
						currentPgValue -= 1;
						currentPageLbl.setText(String.valueOf(currentPgValue));
						Calendar cal = Calendar.getInstance();
						cal.setTime(sdfNew.parse(histFirstDateTimeLbl));
						cal.add(Calendar.MINUTE, -(histMinsGap * (TOTAL_TIME_MARKINGS - 1)));
						String tempFirstDateTimeLbl = sdfNew.format(cal.getTime());

						if(compareDates(histStartDateTime, tempFirstDateTimeLbl) > 0)
						{
							recreateHistTimeLine(histStartDateTime);
							start.setDisable(true);
							prev.setDisable(true);
						}
						else if (compareDates(histStartDateTime, tempFirstDateTimeLbl) == 0)
						{
							if (compareTime(histStartDateTime, tempFirstDateTimeLbl) >= 0)
							{
								recreateHistTimeLine(histStartDateTime);
								start.setDisable(true);
								prev.setDisable(true);
							}
							else if (compareTime(histStartDateTime, tempFirstDateTimeLbl) < 0)
							{
								recreateHistTimeLine(tempFirstDateTimeLbl);
							}
						}
						else if(compareDates(histStartDateTime, tempFirstDateTimeLbl) < 0)
						{
							recreateHistTimeLine(tempFirstDateTimeLbl);
						}
					}
					catch (ParseException e){
						LOG.error("## Exception occured:", e);
					}
				}
				catch (Exception e)
				{
					LOG.error("## Exception occured:", e);

				}

			}

		});
	}

	/**
	 * This method creates Y scale for the passed Anesthesia parameter
	 * and maintains a list of max four scales for Anesthesia Y-Axis
	 *
	 * @param param name of parameter
	 * @param min minimum value for parameter
	 * @param max maximum value for parameter
	 */
	public void setScaleForAnesParameters(String param, double min, double max)
	{
		try
		{
			if (param.equalsIgnoreCase(ParamMinMaxRange.PPEAK))
			{
				paramScaleMap.put(ParamMinMaxRange.PPEAK, new Double[] { max, max * 0.25 });
				anesScalesList.add(paramScaleMap.get(ParamMinMaxRange.PPEAK));
				ParamMinMaxRange.setPPEAK_COLOR(yscale4Color);
			}
			else if (param.equalsIgnoreCase(ParamMinMaxRange.PMEAN))
			{
				paramScaleMap.put(ParamMinMaxRange.PMEAN, new Double[] { max, max * 0.125 });
				anesScalesList.add(paramScaleMap.get(ParamMinMaxRange.PMEAN));
				ParamMinMaxRange.setPMEAN_COLOR(yscale3Color);
			}
			else if (param.equalsIgnoreCase(ParamMinMaxRange.PEEPEXT))
			{
				paramScaleMap.put(ParamMinMaxRange.PEEPEXT, paramScaleMap.get(ParamMinMaxRange.PMEAN));
				ParamMinMaxRange.setPEEPEXT_COLOR(ParamMinMaxRange.PMEAN_COLOR);
			}
			else if (param.equalsIgnoreCase(ParamMinMaxRange.CIRCUITO2))
			{
				paramScaleMap.put(ParamMinMaxRange.CIRCUITO2, paramScaleMap.get(ParamMinMaxRange.PPEAK));
				ParamMinMaxRange.setCIRCUITO2_COLOR(ParamMinMaxRange.PPEAK_COLOR);
			}
			else if (param.equalsIgnoreCase(ParamMinMaxRange.TVEXP))
			{
				paramScaleMap.put(ParamMinMaxRange.TVEXP, new Double[] { max, max * 0.25 });
				anesScalesList.add(paramScaleMap.get(ParamMinMaxRange.TVEXP));
				ParamMinMaxRange.setTVEXP_COLOR(yscale2Color);
			}
			else if (param.equalsIgnoreCase(ParamMinMaxRange.MVEXP))
			{
				paramScaleMap.put(ParamMinMaxRange.MVEXP, paramScaleMap.get(ParamMinMaxRange.PMEAN));
				ParamMinMaxRange.setMVEXP_COLOR(ParamMinMaxRange.PMEAN_COLOR);
			}
			else if (param.equalsIgnoreCase(ParamMinMaxRange.RR))
			{
				paramScaleMap.put(ParamMinMaxRange.RR, paramScaleMap.get(ParamMinMaxRange.PPEAK));
				ParamMinMaxRange.setRR_COLOR(ParamMinMaxRange.PPEAK_COLOR);
			}
		}
		catch (Exception e)
		{
			LOG.error("## Exception occured:", e);

		}
	}

	/**
	 * This method creates Y scale for the passed S5 PtMonitor parameter
	 * and maintains a list of max four scales for S5 PtMonitor Y-Axis
	 *
	 * @param param name of parameter
	 * @param min minimum value for parameter
	 * @param max maximum value for parameter
	 */
	public void setScaleForPtMntrParameters(String param, double min, double max)
	{
		try
		{
			if(param.equalsIgnoreCase(ParamMinMaxRange.HR))
			{
				paramScaleMap.put(ParamMinMaxRange.HR, new Double[] { max, max * 0.125 });
				ptMntrScalesList.add(paramScaleMap.get(ParamMinMaxRange.HR));
				ParamMinMaxRange.setHR_COLOR(yscale4Color);
			}
			else if (param.equalsIgnoreCase(ParamMinMaxRange.IBP))
			{
				paramScaleMap.put(ParamMinMaxRange.IBP, paramScaleMap.get(ParamMinMaxRange.HR));
				ParamMinMaxRange.setIBP_COLOR(ParamMinMaxRange.HR_COLOR);
			}
			else if (param.equalsIgnoreCase(ParamMinMaxRange.NIBP))
			{
				paramScaleMap.put(ParamMinMaxRange.NIBP, paramScaleMap.get(ParamMinMaxRange.HR));
				ParamMinMaxRange.setNIBP_COLOR(ParamMinMaxRange.HR_COLOR);
			}
			else if (param.equalsIgnoreCase(ParamMinMaxRange.SPO2))
			{
				paramScaleMap.put(ParamMinMaxRange.SPO2, new Double[] { max, max * 0.2 });
				ptMntrScalesList.add(paramScaleMap.get(ParamMinMaxRange.SPO2));
				ParamMinMaxRange.setSPO2_COLOR(yscale3Color);
			}
			else if (param.equalsIgnoreCase(ParamMinMaxRange.ETCO2))
			{
				paramScaleMap.put(ParamMinMaxRange.ETCO2, paramScaleMap.get(ParamMinMaxRange.SPO2));
				ParamMinMaxRange.setETCO2_COLOR(ParamMinMaxRange.SPO2_COLOR);
			}
			else if (param.equalsIgnoreCase(ParamMinMaxRange.TEMP))
			{
				paramScaleMap.put(ParamMinMaxRange.TEMP, paramScaleMap.get(ParamMinMaxRange.HR));
				ParamMinMaxRange.setTEMP_COLOR(ParamMinMaxRange.HR_COLOR);
			}
			else if (param.equalsIgnoreCase(ParamMinMaxRange.BIS))
			{
				paramScaleMap.put(ParamMinMaxRange.BIS, paramScaleMap.get(ParamMinMaxRange.SPO2));
				ParamMinMaxRange.setBIS_COLOR(ParamMinMaxRange.SPO2_COLOR);
			}
			else if (param.equalsIgnoreCase(ParamMinMaxRange.ETAGENT))
			{
				paramScaleMap.put(ParamMinMaxRange.ETAGENT, paramScaleMap.get(ParamMinMaxRange.HR));
				ParamMinMaxRange.setETAGENT_COLOR(ParamMinMaxRange.HR_COLOR);
			}
			else if (param.equalsIgnoreCase(ParamMinMaxRange.MAC))
			{
				paramScaleMap.put(ParamMinMaxRange.MAC, new Double[] { max, max * 0.25 });
				ptMntrScalesList.add(paramScaleMap.get(ParamMinMaxRange.MAC));
				ParamMinMaxRange.setMAC_COLOR(yscale2Color);

//				paramScaleMap.put(ParamMinMaxRange.MAC, paramScaleMap.get(ParamMinMaxRange.SPO2));
//				ParamMinMaxRange.setMAC_COLOR(ParamMinMaxRange.SPO2_COLOR);
			}

		}
		catch (Exception e)
		{
			LOG.error("## Exception occured:", e);

		}
	}

	/**
	 * This method sets inital legend values for S5PAtientMonitor and Anesthesia graphs legends
	 */
	public void setLegendsArray()
	{
		String[] tempArr = {
				ParamMinMaxRange.PPEAK_LEGEND+ PIPE_SYMBOL +ParamMinMaxRange.PPEAK+ PIPE_SYMBOL +ParamMinMaxRange.PPEAK_COLOR,
				ParamMinMaxRange.PMEAN_LEGEND+ PIPE_SYMBOL +ParamMinMaxRange.PMEAN+ PIPE_SYMBOL +ParamMinMaxRange.PMEAN_COLOR,
				ParamMinMaxRange.PEEPEXT_LEGEND+ PIPE_SYMBOL +ParamMinMaxRange.PEEPEXT+ PIPE_SYMBOL +ParamMinMaxRange.PEEPEXT_COLOR,
				ParamMinMaxRange.CIRCUITO2_LEGEND+ PIPE_SYMBOL +ParamMinMaxRange.CIRCUITO2+ PIPE_SYMBOL +ParamMinMaxRange.CIRCUITO2_COLOR,
				ParamMinMaxRange.MVEXP_LEGEND+ PIPE_SYMBOL +ParamMinMaxRange.MVEXP+ PIPE_SYMBOL +ParamMinMaxRange.MVEXP_COLOR,
				ParamMinMaxRange.TVEXP_LEGEND+ PIPE_SYMBOL +ParamMinMaxRange.TVEXP+ PIPE_SYMBOL +ParamMinMaxRange.TVEXP_COLOR,
				ParamMinMaxRange.RR_LEGEND+ PIPE_SYMBOL +ParamMinMaxRange.RR+ PIPE_SYMBOL +ParamMinMaxRange.RR_COLOR
				};

		anesLegendsArr = tempArr;


		String[] tempArr2 = {
			 	ParamMinMaxRange.HR_LEGEND+ PIPE_SYMBOL + ParamMinMaxRange.HR + PIPE_SYMBOL + ParamMinMaxRange.HR_COLOR,
	            ParamMinMaxRange.IBPSYS_LEGEND + PIPE_SYMBOL + ParamMinMaxRange.IBPSYS + PIPE_SYMBOL + ParamMinMaxRange.IBP_COLOR,
	            ParamMinMaxRange.IBPDIA_LEGEND + PIPE_SYMBOL + ParamMinMaxRange.IBPDIA + PIPE_SYMBOL + ParamMinMaxRange.IBP_COLOR,
	            ParamMinMaxRange.SPO2_LEGEND + PIPE_SYMBOL + ParamMinMaxRange.SPO2 + PIPE_SYMBOL + ParamMinMaxRange.SPO2_COLOR,
	            ParamMinMaxRange.ETCO2_LEGEND + PIPE_SYMBOL + ParamMinMaxRange.ETCO2 + PIPE_SYMBOL + ParamMinMaxRange.ETCO2_COLOR,
	            ParamMinMaxRange.TEMP1_LEGEND+PIPE_SYMBOL+ParamMinMaxRange.TEMP1+PIPE_SYMBOL+ParamMinMaxRange.TEMP_COLOR,
	            ParamMinMaxRange.TEMP2_LEGEND+PIPE_SYMBOL+ParamMinMaxRange.TEMP2+PIPE_SYMBOL+ParamMinMaxRange.TEMP_COLOR,
	            ParamMinMaxRange.BIS_LEGEND+PIPE_SYMBOL+ParamMinMaxRange.BIS+PIPE_SYMBOL+ParamMinMaxRange.BIS_COLOR,
	            ParamMinMaxRange.ETAGENT_LEGEND+PIPE_SYMBOL+ParamMinMaxRange.ETAGENT+PIPE_SYMBOL+ParamMinMaxRange.ETAGENT_COLOR,
	            ParamMinMaxRange.MAC_LEGEND+PIPE_SYMBOL+ParamMinMaxRange.MAC+PIPE_SYMBOL+ParamMinMaxRange.MAC_COLOR,
	            ParamMinMaxRange.IBPMEAN_LEGEND+PIPE_SYMBOL+ParamMinMaxRange.IBPMEAN+PIPE_SYMBOL+ParamMinMaxRange.IBP_COLOR,
	            ParamMinMaxRange.NIBPSYS_LEGEND + PIPE_SYMBOL + ParamMinMaxRange.NIBPSYS + PIPE_SYMBOL + ParamMinMaxRange.NIBP_COLOR,
	            ParamMinMaxRange.NIBPDIA_LEGEND + PIPE_SYMBOL + ParamMinMaxRange.NIBPDIA + PIPE_SYMBOL + ParamMinMaxRange.NIBP_COLOR,
	            ParamMinMaxRange.NIBPMEAN_LEGEND+PIPE_SYMBOL+ParamMinMaxRange.NIBPMEAN+PIPE_SYMBOL+ParamMinMaxRange.NIBP_COLOR,
				};

		ptMntrLegendsArr = tempArr2;
	}

	/**
	 * This method adds legend symbols and scales on Y-Axis for Anesthesia Window
	 */
	private void addAnesYScales()
	{
		try
		{
			/*String[] tempArr = {
					ParamMinMaxRange.PPEAK_LEGEND+ PIPE_SYMBOL +ParamMinMaxRange.PPEAK+ PIPE_SYMBOL +ParamMinMaxRange.PPEAK_COLOR,
					ParamMinMaxRange.PMEAN_LEGEND+ PIPE_SYMBOL +ParamMinMaxRange.PMEAN+ PIPE_SYMBOL +ParamMinMaxRange.PMEAN_COLOR,
					ParamMinMaxRange.PEEPEXT_LEGEND+ PIPE_SYMBOL +ParamMinMaxRange.PEEPEXT+ PIPE_SYMBOL +ParamMinMaxRange.PEEPEXT_COLOR,
					ParamMinMaxRange.CIRCUITO2_LEGEND+ PIPE_SYMBOL +ParamMinMaxRange.CIRCUITO2+ PIPE_SYMBOL +ParamMinMaxRange.CIRCUITO2_COLOR,
					ParamMinMaxRange.MVEXP_LEGEND+ PIPE_SYMBOL +ParamMinMaxRange.MVEXP+ PIPE_SYMBOL +ParamMinMaxRange.MVEXP_COLOR,
					ParamMinMaxRange.TVEXP_LEGEND+ PIPE_SYMBOL +ParamMinMaxRange.TVEXP+ PIPE_SYMBOL +ParamMinMaxRange.TVEXP_COLOR,
					ParamMinMaxRange.RR_LEGEND+ PIPE_SYMBOL +ParamMinMaxRange.RR+ PIPE_SYMBOL +ParamMinMaxRange.RR_COLOR
					};

			anesLegendsArr = tempArr;*/
			addLegendLabels(anesLegendsPanel, Arrays.asList(anesLegendsArr));

			if (anesScalesList != null)
			{
				for (Double[] array : anesScalesList)
				{
					if (anesBaseYScale.length == 0)
					{
						anesBaseYScale = array;
					}
					else if (anesYScale3.length == 0)
					{
						anesYScale3 = array;
					}
					else if (anesYScale2.length == 0)
					{
						anesYScale2 = array;
					}
					else if (anesYScale1.length == 0)
					{
						anesYScale1 = array;
					}
				}

				addYScale(anesBaseYScale, true, anesYScaleHolder4, anesBaseGraphBrush, anesBaseGraph.getPrefWidth(),yscale4Color);
				addYScale(anesYScale3, false, anesYScaleHolder3, null, 0,yscale3Color);
				addYScale(anesYScale2, false, anesYScaleHolder2, null, 0,yscale2Color);
				addYScale(anesYScale1, false, anesYScaleHolder1, null, 0,yscale1Color);
			}
		}
		catch (Exception e)
		{
			LOG.error("## Exception occured:", e);

		}
	}

	/**
	 * This method adds legend symbols and scales on Y-Axis for S5 PtMonitor Window
	 */
	private void addPtMntrYScales()
	{
		try
		{
			/*String[] tempArr = {
				 	ParamMinMaxRange.HR_LEGEND+ PIPE_SYMBOL + ParamMinMaxRange.HR + PIPE_SYMBOL + ParamMinMaxRange.HR_COLOR,
			        ParamMinMaxRange.IBPSYS_LEGEND + PIPE_SYMBOL + ParamMinMaxRange.IBPSYS + PIPE_SYMBOL + ParamMinMaxRange.IBP_COLOR,
			        ParamMinMaxRange.IBPDIA_LEGEND + PIPE_SYMBOL + ParamMinMaxRange.IBPDIA + PIPE_SYMBOL + ParamMinMaxRange.IBP_COLOR,
			        ParamMinMaxRange.SPO2_LEGEND + PIPE_SYMBOL + ParamMinMaxRange.SPO2 + PIPE_SYMBOL + ParamMinMaxRange.SPO2_COLOR,
			        ParamMinMaxRange.ETCO2_LEGEND + PIPE_SYMBOL + ParamMinMaxRange.ETCO2 + PIPE_SYMBOL + ParamMinMaxRange.ETCO2_COLOR,
					};

			ptMntrLegendsArr = tempArr;*/
			addLegendLabels(ptMntrLegendsPanel, Arrays.asList(ptMntrLegendsArr));
			if (ptMntrScalesList != null)
			{
				for (Double[] array : ptMntrScalesList)
				{
					if (ptMntrBaseYScale.length == 0)
					{
						ptMntrBaseYScale = array;
					}
					else if (ptMntrYScale3.length == 0)
					{
						ptMntrYScale3 = array;
					}
					else if (ptMntrYScale2.length == 0)
					{
						ptMntrYScale2 = array;
					}
					else if (ptMntrYScale1.length == 0)
					{
						ptMntrYScale1 = array;
					}
				}

				addYScale(ptMntrBaseYScale, true, ptMntrYScaleHolder4, ptMntrBaseGraphBrush,ptMntrBaseGraph.getPrefWidth(),yscale4Color);
				addYScale(ptMntrYScale3, false, ptMntrYScaleHolder3, null, 0,yscale3Color);
				addYScale(ptMntrYScale2, false, ptMntrYScaleHolder2, null, 0,yscale2Color);
				addYScale(ptMntrYScale1, false, ptMntrYScaleHolder1, null, 0,yscale1Color);
			}
		}
		catch (Exception e)
		{
			LOG.error("## Exception occured:", e);

		}
	}

	/**
	 * This method adds legend symbols and scales on Y-Axis for Historical Data Window
	 */
	private void addHistYScales(String[] legendArr)
	{
		addLegendLabels(histLegendsPanel, Arrays.asList(legendArr));
		histBaseYScale = new Double[] {};
		histYScale3 = new Double[] {};
		histYScale2 = new Double[] {};
		histYScale1 = new Double[] {};

		if (histScalesList != null)
		{
			for (Double[] array : histScalesList)
			{
				if (histBaseYScale.length == 0)
				{
					histBaseYScale = array;
				}
				else if (histYScale3.length == 0)
				{
					histYScale3 = array;
				}
				else if (histYScale2.length == 0)
				{
					histYScale2 = array;
				}
				else if (histYScale1.length == 0)
				{
					histYScale1 = array;
				}
			}
		}
		addYScale(histBaseYScale, true, histYScaleHolder4, histGraphBrush, histGraphPanel.getPrefWidth(), yscale4Color);
		addYScale(histYScale3, false, histYScaleHolder3, null, 0, yscale3Color);
		addYScale(histYScale2, false, histYScaleHolder2, null, 0, yscale2Color);
		addYScale(histYScale1, false, histYScaleHolder1, null, 0, yscale1Color);
	}


	// Changes Done By Sudeep
	/*private void addS5TitledPane(VBox vbox)
	{

		try
		{
			s5TabBody = new VBox();
			s5TabBody.setId("s5ptmonitor");
			s5TabBody.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));

			s5TabBody.setPrefSize(centerPane.getPrefWidth(), centerPane.getPrefHeight() * 0.5);
			dividePaneInSections(s5TabBody);

			ptmntrGraphTitledPane = new TitledPane();
			ptmntrGraphTitledPane.setText("S5 PatientMonitor Graphical Live View");
			ptmntrGraphTitledPane.setContent(s5TabBody);
			ptmntrGraphTitledPane.setExpanded(false);
			vbox.getChildren().add(ptmntrGraphTitledPane);
		}
		catch (Exception e)
		{
			LOG.error("## Exception occured:", e);

		}
	}

	private void addAnesthesiaTitledPane(VBox vbox)
	{

		try
		{
			anesthesiaTabBody = new VBox();
			anesthesiaTabBody.setId("anesthesia");
			anesthesiaTabBody.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));

			anesthesiaTabBody.setPrefSize(centerPane.getPrefWidth(), centerPane.getPrefHeight() * 0.5);
			dividePaneInSections(anesthesiaTabBody);

			anesGraphTitledPane = new TitledPane();
			anesGraphTitledPane.setText("Anesthesia Graphical Live View");
			anesGraphTitledPane.setContent(anesthesiaTabBody);
			anesGraphTitledPane.setExpanded(false);
			vbox.getChildren().add(anesGraphTitledPane);
		}
		catch (Exception e)
		{
			LOG.error("## Exception occured:", e);

		}
	}*/




}
