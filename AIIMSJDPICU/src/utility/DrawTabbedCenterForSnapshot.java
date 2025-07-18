/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package utility;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

import com.cdac.common.GeneratedEntities.Investigationstests;
import com.cdac.common.pojoClasses.InvestigationMapperAndValue;
import com.cdac.framework.SystemConfiguration;

import application.FxmlView;
import application.Main;
import controllers.DashboardTestController;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import mediatorPattern.ControllerMediator;
import model.ChartImageModel;
import model.PatientSession;
import services.GetInvestigationMapperAndValueService;

/**
 * This Class is used to create trend graph for report
 *
 * @author Sudeep_Sahoo
 *
 */
public class DrawTabbedCenterForSnapshot {

	private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
	private SimpleDateFormat sdfNew = new SimpleDateFormat("dd-MM-yyyy HH:mm");
	private static final String PIPE_SYMBOL = "|";
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
	private Pane histYScaleHolder2, histYScaleHolder3, histYScaleHolder4;
	private ComboBox<Integer> timeDropdown;
	private Button start, end, next, prev;

	boolean isS5Called = false;
	List<ChartImageModel> imageListForReport = new ArrayList<ChartImageModel>();

	public List<ChartImageModel> getImageListForReport() {
		return imageListForReport;
	}

	public void setImageListForReport(List<ChartImageModel> imageListForReport) {
		this.imageListForReport = imageListForReport;
	}

	public boolean isS5Called() {
		return isS5Called;
	}

	public void setS5Called(boolean isS5Called) {
		this.isS5Called = isS5Called;
	}

	private String /* medtimelineStartTime = "", */ medFirstTimeLbl = "", medLastTimeLbl = "";
	// private HBox medTimeHBox;
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
	private List<String> headerTimeLblsList = new LinkedList<String>();
	private Calendar gridLastTimeStamp = Calendar.getInstance();
	private TitledPane ptmntrGraphTitledPane, anesGraphTitledPane;
	private Map<String, String> infusionStartedMap = new HashMap<String, String>(); // ---(MedName,StartTime)
	private List<String> medsFromInfPump = new ArrayList<String>();
	private Map<String, String> fluidstartedMap = new HashMap<String, String>();
	private Investigationstests testObj = new Investigationstests();
	private List<Investigationstests> investigationTestsList = new ArrayList<Investigationstests>();
	private Map<String, Integer> testIdsMap = new HashMap<String, Integer>();
	// ---Dynamic grid creation
	private static int NO_OF_GRID_COLUMNS = 15;
	private Map<String, GridPane> paramGridMap = new HashMap<String, GridPane>();
	private Map<String, GridPane> totalsGridMap = new HashMap<String, GridPane>();
	private Map<String, GridPane> textboxGridMap = new HashMap<String, GridPane>();
	private Map<String, List<String>> paramListMap = new HashMap<String, List<String>>();
	private Map<String, List<String>> totalsListMap = new HashMap<String, List<String>>();
	private Map<String, List<TextField>> textboxListMap = new HashMap<String, List<TextField>>();
	private static final Logger LOG = Logger.getLogger(DrawTabbedCenterForSnapshot.class.getName());
	// ---TimeLines
	private Timeline updateTimeline, checkTime;

	// ---Pagination
	private Label currentPageLbl, totalPagesLbl;
	private int currentPgValue, totalPgValue;

	public VBox getVbForSnapShotBody() {
		return VbForSnapShotBody;
	}

	public void setVbForSnapShotBody(VBox vbForSnapShotBody) {
		VbForSnapShotBody = vbForSnapShotBody;
	}

	private double minsPerWindow, pageFactor;
	private long totalCSVrecords;
	private VBox VbForSnapShotBody = new VBox();

	private Map<String, Map<String, String[]>> deviceParamValMap = new HashMap<String, Map<String, String[]>>();

	public void setCurrentTab(String currentTab) {
		this.currentTab = currentTab;
	}

	public Timeline getCheckTime() {
		return checkTime;
	}

	public List<Investigationstests> getInvestigationTestsList() {
		return investigationTestsList;
	}

	public Map<String, Integer> getTestIdsMap() {
		return testIdsMap;
	}

	public Timeline getUpdateTimeline() {
		return updateTimeline;
	}

	public Map<String, Map<String, String[]>> getDeviceParamValMap() {
		return deviceParamValMap;
	}

	public List<String> getMedsFromInfPump() {
		return medsFromInfPump;
	}

	public Map<String, String> getFluidstartedMap() {
		return fluidstartedMap;
	}

	public Map<String, String> getInfusionStartedMap() {
		return infusionStartedMap;
	}

	public Button getAddmedBtn() {
		return addmedBtn;
	}

	public Button getAddfluidBtn() {
		return addfluidBtn;
	}

	public Button getAddoutputBtn() {
		return addoutputBtn;
	}

	public Canvas getMedCanvas() {
		return medCanvas;
	}

	public GraphicsContext getMedBrush() {
		return medBrush;
	}

	public Canvas getMedCanvasBase() {
		return medCanvasBase;
	}

	public GraphicsContext getMedBrushBase() {
		return medBrushBase;
	}

	public void setMedLastTimeLbl(String medLastTimeLbl) {
		this.medLastTimeLbl = medLastTimeLbl;
	}

	public String getMedFirstTimeLbl() {
		return medFirstTimeLbl;
	}

	public String getMedLastTimeLbl() {
		return medLastTimeLbl;
	}

	public Pane getMedTimePane() {
		return medTimePane;
	}

	public GraphicsContext getMedTimeBrush() {
		return medTimeBrush;
	}

	public void setTotalCSVrecords(long totalCSVrecords) {
		this.totalCSVrecords = totalCSVrecords;
	}

	public String getHistStartDateTime() {
		return histStartDateTime;
	}

	public void setHistStartDateTime(String histStartDateTime) {
		this.histStartDateTime = histStartDateTime;
	}

	public String getHistEndDateTime() {
		return histEndDateTime;
	}

	public void setHistEndDateTime(String histEndDateTime) {
		this.histEndDateTime = histEndDateTime;
	}

	public String getHistFirstDateTimeLbl() {
		return histFirstDateTimeLbl;
	}

	public String getHistLastDateTimeLbl() {
		return histLastDateTimeLbl;
	}

	public void setCurrentPgValue(int currentPgValue) {
		this.currentPgValue = currentPgValue;
	}

	public void setTotalPgValue(int totalPgValue) {
		this.totalPgValue = totalPgValue;
	}

	public double getPageFactor() {
		return pageFactor;
	}

	public Label getCurrentPageLbl() {
		return currentPageLbl;
	}

	public Label getTotalPagesLbl() {
		return totalPagesLbl;
	}

	public void setHistGraphBrush(GraphicsContext histGraphBrush) {
		this.histGraphBrush = histGraphBrush;
	}

	public GraphicsContext getHistGraphBrush() {
		return histGraphBrush;
	}

	public Pane getHistGraphPanel() {
		return histGraphPanel;
	}

	public String getAnesFirstTimeLbl() {
		return anesFirstTimeLbl;
	}

	public String getPtMntrFirstTimeLbl() {
		return ptMntrFirstTimeLbl;
	}

	public void setAnesLegendsArr(String[] anesLegendsArr) {
		this.anesLegendsArr = anesLegendsArr;
	}

	public void setPtMntrLegendsArr(String[] ptMntrLegendsArr) {
		this.ptMntrLegendsArr = ptMntrLegendsArr;
	}

	public List<Double[]> getAnesScalesList() {
		return anesScalesList;
	}

	public void setAnesLastTimeLbl(String anesLastTimeLbl) {
		this.anesLastTimeLbl = anesLastTimeLbl;
	}

	public void setPtMntrLastTimeLbl(String ptMntrLastTimeLbl) {
		this.ptMntrLastTimeLbl = ptMntrLastTimeLbl;
	}

	public List<Double> getTimeLblCoordinatesList() {
		return timeLblCoordinatesList;
	}

	public List<Double[]> getPtMntrScalesList() {
		return ptMntrScalesList;
	}

	public double getxValue() {
		return xValue;
	}

	public void setxValue(double xValue) {
		this.xValue = xValue;
	}

	public void setTabNames(String[] tabNames) {
		this.tabNames = tabNames;
	}

	public String getCurrentTab() {
		return currentTab;
	}

	public GraphicsContext getAnesGraphBrush() {
		return anesGraphBrush;
	}

	public GraphicsContext getPtMntrGraphBrush() {
		return ptMntrGraphBrush;
	}

	public Pane getAnesGraphPanel() {
		return anesGraphPanel;
	}

	public Pane getPtMntrGraphPanel() {
		return ptMntrGraphPanel;
	}

	public double getCalculatedGraphWidth() {
		return calculatedGraphWidth;
	}

	public HBox getAnesTimeHBox() {
		return anesTimeHBox;
	}

	public Pane getAnesTimePane() {
		return anesTimePane;
	}

	public HBox getPtMntrTimeHBox() {
		return ptMntrTimeHBox;
	}

	public Pane getPtMntrTimePane() {
		return ptMntrTimePane;
	}

	public String getAnesLastTimeLbl() {
		return anesLastTimeLbl;
	}

	public String getPtMntrLastTimeLbl() {
		return ptMntrLastTimeLbl;
	}

	public Map<String, Double[]> getParamScaleMap() {
		return paramScaleMap;
	}

	public GraphicsContext getAnesTimepaneBrush() {
		return anesTimepaneBrush;
	}

	public GraphicsContext getPtMntrTimepaneBrush() {
		return ptMntrTimepaneBrush;
	}

	public void createCenterPane(Pane centerPane) {
		try {

			this.centerPane = centerPane;
			TIME_PIXELS_GAP = centerPane.getPrefWidth() * 0.08;
			MINS_GAP = SystemConfiguration.getInstance().getMinsGap();
			// setLegendsArray();
			calculatedGraphWidth = calcGraphWidth();
			tablularBody = new VBox();
			tablularBody.setPrefSize(centerPane.getPrefWidth(), centerPane.getPrefHeight());
			tablularBody.setId("tabularView");
			tablularBody.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
			createTableView(tablularBody);
			centerPane.getChildren().add(tablularBody);

		} catch (Exception e) {
			LOG.error("## Exception occured:", e);
		}
	}

	/**
	 * This method calculates width for graph component
	 *
	 * @return
	 */
	public double calcGraphWidth() {
		return TIME_PIXELS_GAP * (TOTAL_TIME_MARKINGS - 1);
	}

	public void createTableView(VBox vbox) {
		try {
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
			baseScrollVBox.getStylesheets().add(this.getClass().getResource("/styles/newStyles.css").toString());
			vbox.getChildren().addAll(baseScroller);

		} catch (Exception e) {
			LOG.error("## Exception occured:", e);
		}
	}

	public void createTimeLblLists() {
		try {

			Calendar cal = Calendar.getInstance();
			String time = sdf.format(cal.getTime());
			int mins = Integer.parseInt(time.split(":")[1]);
			cal.set(Calendar.MINUTE, (mins - (mins % 5)));

			for (int i = 0; i < NO_OF_GRID_COLUMNS; i++) {
				String gridLastTimeLbl = sdf.format(cal.getTime());
				headerTimeLblsList.add(gridLastTimeLbl);

				if (i == NO_OF_GRID_COLUMNS - 1) {
					gridLastTimeStamp.setTime(cal.getTime());
				}

				cal.add(Calendar.MINUTE, MINS_GAP);
			}

		} catch (Exception e) {
			LOG.error("## Exception occured:", e);
		}
	}

	public void createDynamicGrid(String gridName, boolean totalsGridReqd, VBox baseVbox) {
		try {

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
			paramScroller.addEventFilter(ScrollEvent.SCROLL, new EventHandler<ScrollEvent>() {
				@Override
				public void handle(ScrollEvent event) {
					if (event.getDeltaY() != 0) {
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
			textboxGrid.getStyleClass().add("GridPane");
			textboxGrid.setPrefWidth(textboxScroller.getPrefWidth() * 0.98);
			textboxScroller.setContent(textboxGrid);
			textboxScroller.setVbarPolicy(ScrollBarPolicy.NEVER);
			textboxScroller.addEventFilter(ScrollEvent.SCROLL, new EventHandler<ScrollEvent>() {
				@Override
				public void handle(ScrollEvent event) {
					if (event.getDeltaY() != 0) {
						event.consume();
					}
				}
			});

			if (totalsGridReqd) {
				paramScroller.setPrefWidth(hboxGrid.getPrefWidth() * 0.13);
				paramGrid.setPrefWidth(paramScroller.getPrefWidth());

				// ---TOTALS SCROLLPANE
				ScrollPane totalsScroller = new ScrollPane();
				totalsScroller.setPrefWidth(hboxGrid.getPrefWidth() * 0.092);
				GridPane totalsGrid = new GridPane();
				totalsGrid.getStylesheets().add(this.getClass().getResource("/styles/newStyles.css").toString());
				totalsGrid.getStyleClass().add("GridPane");
				totalsGrid.setPrefWidth(totalsScroller.getPrefWidth());
				totalsScroller.setContent(totalsGrid);
				totalsScroller.setVbarPolicy(ScrollBarPolicy.NEVER);
				totalsScroller.setHbarPolicy(ScrollBarPolicy.NEVER);
				totalsScroller.addEventFilter(ScrollEvent.SCROLL, new EventHandler<ScrollEvent>() {
					@Override
					public void handle(ScrollEvent event) {
						if (event.getDeltaY() != 0) {
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

			TitledPane titledPane = new TitledPane();
			titledPane.setText(gridName.substring(0, 1).toUpperCase() + gridName.substring(1, gridName.length()));
			titledPane.setContent(vboxGrid);
			titledPane.setExpanded(false);
			// titledPane.getStyleClass().add("nestedTitledPane");

			baseVbox.getChildren().add(titledPane);

			paramGridMap.put(gridName, paramGrid);
			textboxGridMap.put(gridName, textboxGrid);
			paramListMap.put(gridName, new ArrayList<String>());
			textboxListMap.put(gridName, new ArrayList<TextField>());

		} catch (Exception e) {
			LOG.error("## Exception occured:", e);
		}
	}

	public void addTestContextMenu(TextField textbox, String testName) {
		try {

			textbox.setStyle("-fx-display-caret: false;");
			ContextMenu contextMenu = new ContextMenu();
			contextMenu.setPrefSize(100, 100);

			MenuItem enter = new MenuItem("Enter Values");
			enter.setStyle("-fx-font-size:12;");

			contextMenu.getItems().addAll(enter);
			textbox.setContextMenu(contextMenu);

			enter.setOnAction(e -> {
				ControllerMediator.getInstance().getMainController().bindPopupFxml(FxmlView.DASHBOARD_TEST);

				String timeStamp = headerTimeLblsList.get(Integer.valueOf(textbox.getId().split(",")[1]));
				Calendar cal = Calendar.getInstance();
				cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeStamp.split(":")[0]));
				cal.set(Calendar.MINUTE, Integer.parseInt(timeStamp.split(":")[1]));
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.MILLISECOND, 0);
				ControllerMediator.getInstance().getDashboardTestController().setSelectedTime(cal.getTime());
				ControllerMediator.getInstance().getDashboardTestController().getLblTime().setText(timeStamp);
				ControllerMediator.getInstance().getDashboardTestController().getTxtTestName().setText(testName);

				GetInvestigationMapperAndValueService getTestsService = GetInvestigationMapperAndValueService
						.getInstance(testIdsMap.get(testName.toLowerCase()),
								PatientSession.getInstance().getPatientCase().getCaseId(), cal.getTime());
				getTestsService.restart();

				getTestsService.setOnSucceeded(ex -> {
					InvestigationMapperAndValue mapperObj = getTestsService.getInvestigationMapperAndValue();
					ControllerMediator.getInstance().getDashboardTestController()
							.setInvestigationMapperAndValue(mapperObj);

					for (Investigationstests obj : investigationTestsList) {
						if (obj.getTestName().equalsIgnoreCase(testName)) {
							testObj = obj;
							break;
						}
					}

					ControllerMediator.getInstance().getDashboardTestController().setSelectedTestObj(testObj);
					ControllerMediator.getInstance().getDashboardTestController().getAllParam(testObj);

				});

				getTestsService.setOnFailed(ex -> {
					Main.getInstance().getUtility().showNotification("Error", "Error",
							getTestsService.getException().getMessage());
				});

			});

			textbox.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					MouseButton button = event.getButton();
					if (button == MouseButton.SECONDARY) {
						String time = headerTimeLblsList.get(Integer.valueOf(textbox.getId().split(",")[1]));
						Calendar selectedTime = Calendar.getInstance();
						selectedTime.set(Calendar.HOUR_OF_DAY, Integer.valueOf(time.split(":")[0]));
						selectedTime.set(Calendar.MINUTE, Integer.valueOf(time.split(":")[1]));
						selectedTime.set(Calendar.SECOND, 0);
						selectedTime.set(Calendar.MILLISECOND, 0);

						Calendar cal = Calendar.getInstance();
						cal.set(Calendar.SECOND, 0);
						cal.set(Calendar.MILLISECOND, 0);

						if (selectedTime.getTimeInMillis() - cal.getTimeInMillis() > 0)
							enter.setDisable(true);
						else
							enter.setDisable(false);

					}
				}
			});

		} catch (Exception e) {
			LOG.error("## Exception occured:", e);
		}
	}

	/**
	 * This method sets starting time for Anesthesia and S5 PtMonitor graph
	 * windows
	 */
	public void setInitialTimeStamps() {
		try {

			anestimelineStartTime = sdfNew.format(Calendar.getInstance().getTime());
			ptMntrtimelineStartTime = anestimelineStartTime;

		} catch (Exception e) {
			LOG.error("## Exception occured:", e);
		}
	}

	/**
	 * This method returns number pixels covered in TrendGraphSpeed secs
	 *
	 * @return
	 */
	public double pixelsPerSec() {
		return (calculatedGraphWidth * SystemConfiguration.getInstance().getTrendGraphSpeed())
				/ ((TOTAL_TIME_MARKINGS - 1) * MINS_GAP * 60);
	}

	/**
	 * This method returns number of pixels covered in 1 min for historical data
	 * graph
	 *
	 * @return
	 */
	public double pixelsPerMinHistGraph() {
		return calculatedGraphWidth / ((TOTAL_TIME_MARKINGS - 1) * histMinsGap);
	}

	/**
	 * This method returns number of pixels covered in 1 sec for historical data
	 * graph
	 *
	 * @return
	 */
	public double pixelsPerSecHistGraph() {
		return calculatedGraphWidth / ((TOTAL_TIME_MARKINGS - 1) * histMinsGap);
	}

	/**
	 * This method creates center body for S5 PtMntr tab
	 *
	 * @param PtMntrbody
	 */
	public void createPtMntrBodyArea(Pane PtMntrbody) {
		try {

			// -------new changes for screen resize fix
			HBox base = new HBox();
			base.setPrefSize(PtMntrbody.getPrefWidth(), PtMntrbody.getPrefHeight());
			base.setSpacing(5);

			VBox baseVboxForLegends = new VBox();
			baseVboxForLegends.setPrefSize(PtMntrbody.getPrefWidth() * 0.22, PtMntrbody.getPrefHeight() * 0.92);

			HBox hboxForLegends = new HBox();
			hboxForLegends.setPrefSize(baseVboxForLegends.getPrefWidth(), baseVboxForLegends.getPrefHeight());
			Pane legendsHolder = new Pane();
			legendsHolder.setPrefSize(hboxForLegends.getPrefWidth() * 0.5, hboxForLegends.getPrefHeight());
			// legendsHolder.setStyle(" -fx-border-color: silver;");
			Pane scaleHolder = new Pane();
			scaleHolder.setPrefSize(hboxForLegends.getPrefWidth() * 0.5, hboxForLegends.getPrefHeight());
			// scaleHolder.setStyle(" -fx-border-color: silver;");

			VBox vboxForGraph = new VBox();
			vboxForGraph.setPrefSize(PtMntrbody.getPrefWidth() * 0.78, PtMntrbody.getPrefHeight() * 0.92);
			// ------------------------------

			ptMntrTimeHBox = new HBox();
			ptMntrTimeHBox.setPrefSize(PtMntrbody.getPrefWidth(), PtMntrbody.getPrefHeight() * 0.08);

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
			// ptMntrBaseGraph.setStyle(" -fx-border-color: silver;");
			ptMntrBaseGraph.setPrefSize(graphArea.getPrefWidth(), graphArea.getPrefHeight() * 0.975);
			Canvas baseGraphCanvas = new Canvas(ptMntrBaseGraph.getPrefWidth(), ptMntrBaseGraph.getPrefHeight());
			ptMntrBaseGraphBrush = baseGraphCanvas.getGraphicsContext2D();
			ptMntrBaseGraphBrush.setStroke(Color.SILVER);
			ptMntrBaseGraphBrush.setLineWidth(1);
			ptMntrBaseGraph.getChildren().add(baseGraphCanvas);
			drawVerticalMarkings(ptMntrBaseGraph, ptMntrBaseGraphBrush);

			ptMntrGraphPanel = new Pane();
			ptMntrGraphPanel.setId("PtMntrPanel");
			// ptMntrGraphPanel.setStyle(" -fx-border-color: silver;");
			ptMntrGraphPanel.setPrefSize(graphArea.getPrefWidth(), graphArea.getPrefHeight() * 0.975);
			ptMntrGraphCanvas = new Canvas(ptMntrGraphPanel.getPrefWidth(), ptMntrGraphPanel.getPrefHeight());
			ptMntrGraphBrush = ptMntrGraphCanvas.getGraphicsContext2D();
			ptMntrGraphBrush.setStroke(Color.SILVER);
			ptMntrGraphBrush.setLineWidth(1);
			ptMntrGraphPanel.getChildren().add(ptMntrGraphCanvas);
			graphArea.getChildren().addAll(ptMntrBaseGraph, ptMntrGraphPanel);

			// ---for timePane
			Pane horizotalSpace = new Pane();
			horizotalSpace.setPrefSize(ptMntrTimeHBox.getPrefWidth() - calculatedGraphWidth,
					ptMntrTimeHBox.getPrefHeight());
			ptMntrTimePane = new Pane();
			ptMntrTimePane.setPrefSize(calculatedGraphWidth, ptMntrTimeHBox.getPrefHeight());// 1.01

			Canvas ptMntrTimepaneCanvas = new Canvas(ptMntrTimePane.getPrefWidth(), ptMntrTimePane.getPrefHeight());
			ptMntrTimepaneBrush = ptMntrTimepaneCanvas.getGraphicsContext2D();
			ptMntrTimepaneBrush.setLineWidth(0.5);
			ptMntrTimePane.getChildren().add(ptMntrTimepaneCanvas);
			ptMntrTimeHBox.getChildren().addAll(horizotalSpace, ptMntrTimePane);
			addTimeLabels(ptMntrTimePane, ptMntrTimepaneBrush, ptMntrtimelineStartTime, MINS_GAP, "patient monitor");

			// centerHBox.getChildren().addAll(ptMntrLegendsPanel,
			// verticalDivider1,
			// ptMntrYscalePanel, verticalDivider2,graphArea);
			// PtMntrbody.getChildren().addAll(ptMntrTimeHBox, centerHBox);

			legendsHolder.getChildren().addAll(ptMntrLegendsPanel);
			scaleHolder.getChildren().addAll(ptMntrYscalePanel);

			hboxForLegends.getChildren().addAll(legendsHolder, scaleHolder);
			baseVboxForLegends.getChildren().addAll(horizotalSpace, hboxForLegends);
			vboxForGraph.getChildren().addAll(ptMntrTimePane, graphArea);
			base.getChildren().addAll(baseVboxForLegends, vboxForGraph);

			PtMntrbody.getChildren().addAll(base);

		} catch (Exception e) {
			LOG.error("## Exception occured:", e);
		}
	}

	/**
	 * This method creates center body for Anesthesia tab
	 *
	 * @param Anesbody
	 */
	public void createAnesBodyArea(Pane Anesbody) {
		try {

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
			scaleHolder.setPrefSize(hboxForLegends.getPrefWidth() * 0.7, hboxForLegends.getPrefHeight());
			scaleHolder.setStyle(" -fx-border-color: silver;");

			VBox vboxForGraph = new VBox();
			vboxForGraph.setPrefSize(Anesbody.getPrefWidth() * 0.78, Anesbody.getPrefHeight() * 0.92);
			// ------------------------------

			anesTimeHBox = new HBox();
			anesTimeHBox.setPrefSize(Anesbody.getPrefWidth(), Anesbody.getPrefHeight() * 0.08);

			/*
			 * HBox centerHBox = new HBox();
			 * centerHBox.setPrefSize(Anesbody.getPrefWidth(),
			 * Anesbody.getPrefHeight() * 0.92);
			 */

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
			horizotalSpace.setPrefSize(anesTimeHBox.getPrefWidth() - calculatedGraphWidth,
					anesTimeHBox.getPrefHeight());
			anesTimePane = new Pane();
			anesTimePane.setPrefSize(calculatedGraphWidth, anesTimeHBox.getPrefHeight());// *
																							// 1.01
			Canvas anesTimepaneCanvas = new Canvas(anesTimePane.getPrefWidth(), anesTimePane.getPrefHeight());
			anesTimepaneBrush = anesTimepaneCanvas.getGraphicsContext2D();
			anesTimepaneBrush.setLineWidth(0.5);
			anesTimePane.getChildren().add(anesTimepaneCanvas);
			anesTimeHBox.getChildren().addAll(horizotalSpace, anesTimePane);
			addTimeLabels(anesTimePane, anesTimepaneBrush, anestimelineStartTime, MINS_GAP, "anesthesia");

			// centerHBox.getChildren().addAll(anesLegendsPanel,
			// verticalDivider1,
			// anesYscalePanel, verticalDivider2, graphArea);
			// Anesbody.getChildren().addAll(anesTimeHBox, centerHBox);

			legendsHolder.getChildren().addAll(anesLegendsPanel);
			scaleHolder.getChildren().addAll(anesYscalePanel);

			hboxForLegends.getChildren().addAll(legendsHolder, scaleHolder);
			baseVboxForLegends.getChildren().addAll(horizotalSpace, hboxForLegends);
			vboxForGraph.getChildren().addAll(anesTimePane, graphArea);
			base.getChildren().addAll(baseVboxForLegends, vboxForGraph);

			Anesbody.getChildren().addAll(base);

		} catch (Exception e) {
			LOG.error("## Exception occured:", e);
		}
	}

	/**
	 * This method divides Anesthesia Y-Scale into multiple scale holders
	 *
	 * @param basePane
	 */
	public void createAnesScaleHolders(HBox basePane) {
		try {

			basePane.setPrefWidth(basePane.getPrefWidth() * 0.9);

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

			basePane.getChildren().addAll(anesYScaleHolder1, anesYScaleHolder2, anesYScaleHolder3, anesYScaleHolder4);

		} catch (Exception e) {
			LOG.error("## Exception occured:", e);
		}
	}

	/**
	 * This method divides PtMonitor Y-Scale into multiple scale holders
	 *
	 * @param basePane
	 */
	public void createPtMntrScaleHolders(HBox basePane) {
		try {

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

		} catch (Exception e) {
			LOG.error("## Exception occured:", e);
		}
	}

	/**
	 * This method adds passed list of legends to legendsPanel
	 *
	 * @param legendsPanel
	 * @param legendsList
	 */
	public void addLegendLabels(Pane legendsPanel, List<String> legendsList) {
		try {

			VBox vbox = new VBox();
			// vbox.setStyle("-fx-border-color: red");
			vbox.setPrefSize(legendsPanel.getPrefWidth() * 0.9, legendsPanel.getPrefHeight());
			vbox.setAlignment(Pos.CENTER);

			for (String legend : legendsList) {
				HBox hbox = new HBox();
				hbox.setPrefWidth(vbox.getPrefWidth());
				hbox.setPrefHeight(vbox.getPrefHeight() * 0.08);
				hbox.setAlignment(Pos.CENTER_LEFT);
				Label spacing = new Label();
				spacing.setPrefWidth(vbox.getPrefWidth() * 0.13);
				Label lblIcon = new Label(legend.split("\\|")[0] + "  ");
				lblIcon.setTextFill(Color.web(legend.split("\\|")[2]));
				lblIcon.setStyle("-fx-font-size:" + vbox.getPrefWidth() * 0.14 + "px;-fx-font-weight:bold");
				Label lblName = new Label(legend.split("\\|")[1]);
				lblName.setStyle("-fx-font-size:" + vbox.getPrefWidth() * 0.16 + "px;-fx-font-family:Calibri");
				lblName.setTextFill(Color.web(legend.split("\\|")[2]));
				hbox.getChildren().addAll(spacing, lblIcon, lblName);
				vbox.getChildren().add(hbox);
			}

			legendsPanel.getChildren().add(vbox);

		} catch (Exception e) {
			LOG.error("## Exception occured:", e);
		}
	}

	/**
	 * This method adds time labels to timePane
	 *
	 * @param timePane
	 * @param startTime
	 */
	public void addTimeLabels(Pane timePane, GraphicsContext timePaneBrush, String startTime, int minGap,
			String tabName) {
		try {

			double layoutX = 0;
			Calendar cal = Calendar.getInstance();
			try {
				cal.setTime(sdfNew.parse(startTime));

			} catch (ParseException e) {
				e.printStackTrace();
				LOG.error("## Exception occured:", e);
			}

			int iEndVal = new Double(timePane.getPrefWidth() / TIME_PIXELS_GAP).intValue();
			for (int i = 0; i <= iEndVal; i++) {
				String timeVal = sdfNew.format(cal.getTime());
				Label lbl = new Label(timeVal.split(" ")[1]);
				lbl.setStyle("-fx-font-family: Calibri");
				lbl.setStyle("-fx-font-size:" + timePane.getPrefWidth() * 0.019 + "px;");
				lbl.setStyle(" -fx-font-weight:bold");
				lbl.setLayoutX(layoutX);

				if (!tabName.toLowerCase().contains("history"))
					timePaneBrush.strokeText(lbl.getText(), layoutX, timePane.getPrefHeight() / 2);

				timeLblCoordinatesList.add(layoutX);
				if (timeLblsList.size() < TOTAL_TIME_MARKINGS)
					timeLblsList.add(lbl.getText());
				layoutX += TIME_PIXELS_GAP;
				if (i == 0) {
					layoutX -= timePane.getPrefWidth() * 0.015; // * 0.012

					if (tabName.toLowerCase().contains("anesthesia")) {
						anesFirstTimeLbl = timeVal;
					} else if (tabName.toLowerCase().contains("patient") && tabName.toLowerCase().contains("monitor")) {
						ptMntrFirstTimeLbl = timeVal;
					} else if (tabName.toLowerCase().contains("history")) {
						histFirstDateTimeLbl = timeVal;
					} else if (tabName.toLowerCase().contains("med")) {
						medFirstTimeLbl = timeVal;
					}

				} else if (i == iEndVal - 1) {
					layoutX -= timePane.getPrefWidth() * 0.016;
				} else if (i == iEndVal) {

					if (tabName.toLowerCase().contains("anesthesia")) {
						anesLastTimeLbl = timeVal;
					} else if (tabName.toLowerCase().contains("patient") && tabName.toLowerCase().contains("monitor")) {
						ptMntrLastTimeLbl = timeVal;
					} else if (tabName.toLowerCase().contains("history")) {
						histLastDateTimeLbl = timeVal;
					} else if (tabName.toLowerCase().contains("med")) {
						medLastTimeLbl = timeVal;
					}
				}

				cal.add(Calendar.MINUTE, minGap);

				if (tabName.toLowerCase().contains("history"))
					timePane.getChildren().add(lbl);

			}

		} catch (Exception e) {
			LOG.error("## Exception occured:", e);
		}
	}

	/**
	 * This method draws vertical lines on passed graphPanel
	 *
	 * @param graphPanel
	 * @param graphBrush
	 */
	public void drawVerticalMarkings(Pane graphPanel, GraphicsContext graphBrush) {
		try {

			double xValue = 0, graphHeight = graphPanel.getPrefHeight();
			if (graphPanel.getId().equalsIgnoreCase("HistoryPanel"))
				graphHeight = graphPanel.getPrefHeight();
			for (int i = 0; i <= (TOTAL_TIME_MARKINGS - 1); i++) {
				graphBrush.strokeLine(xValue, 0, xValue, graphHeight);
				xValue += TIME_PIXELS_GAP;
			}

		} catch (Exception e) {
			LOG.error("## Exception occured:", e);
		}
	}

	/**
	 * This method adds passed Y-Scale array to scaleHolder pane. It also draws
	 * horizontal lines on graphPanel
	 *
	 * @param scaleArr
	 * @param isBaseScale
	 * @param scaleHolder
	 * @param graphBrush
	 * @param graphWidth
	 */
	public void addYScale(Double scaleArr[], boolean isBaseScale, Pane scaleHolder, GraphicsContext graphBrush,
			double graphWidth, String ScaleColor) {
		try {

			if (scaleArr.length != 0) {
				scaleHolder.getChildren().clear();
				double max = scaleArr[0], gap = scaleArr[1];
				max -= gap;

				HBox lblBox = new HBox();
				Label ylbl = new Label();
				int yEndVal = new Double(max / gap).intValue();
				double ylblValue = max;
				double yIncrement = scaleHolder.getPrefHeight() / (yEndVal + 1), layoutY = yIncrement;

				for (int y = 0; y < yEndVal; y++) {
					lblBox = new HBox();
					lblBox.setPrefWidth(scaleHolder.getPrefWidth() * 1.2);
					lblBox.setAlignment(Pos.CENTER_RIGHT);

					if (String.valueOf(ylblValue).endsWith(".0"))
						ylbl = new Label(String.valueOf(new Double(ylblValue).intValue()));
					else
						ylbl = new Label(String.valueOf(ylblValue));

					ylbl.setStyle("-fx-font-size:" + scaleHolder.getPrefWidth() * 0.45 + "px;-fx-font-weight:bold");
					ylbl.setTextFill(Color.web(ScaleColor));
					lblBox.setLayoutY(layoutY - scaleHolder.getPrefHeight() * 0.03);

					lblBox.getChildren().add(ylbl);
					scaleHolder.getChildren().add(lblBox);

					if (isBaseScale) {
						graphBrush.strokeLine(0, layoutY, graphWidth, layoutY);
					}

					layoutY += yIncrement;
					ylblValue -= gap;
				}
			}

		} catch (Exception e) {
			LOG.error("## Exception occured:", e);
		}
	}

	// --- HISTORICAL DATA
	/**
	 * This method creates Header, Body and Footer section in the Historical
	 * data window
	 *
	 * @param vbox
	 */
	public void createHistoricalDataBody(VBox vbox) {
		try {

			// VbForSnapShotBody=vbox;
			histMinsGap = 30;
			// ---Calculations for CurrentPage and TotalPages
			currentPgValue = 1;
			minsPerWindow = histMinsGap * (TOTAL_TIME_MARKINGS - 1);
			pageFactor = 1 / minsPerWindow;
			totalPgValue = new Double(Math.ceil(pageFactor * totalCSVrecords)).intValue();
			// if (totalPgValue == 0)
			// totalPgValue = 1;

			// ---HEADER AREA
			HBox header = new HBox();
			// header.setStyle("-fx-border-color: red;");
			header.setPrefSize(vbox.getPrefWidth(), vbox.getPrefHeight() * 0.08);

			HBox leftPane = new HBox();
			leftPane.setAlignment(Pos.CENTER_LEFT);
			leftPane.setPrefSize(header.getPrefWidth() * 0.5, header.getPrefHeight());

			// Changes done By Sudeep For snapshot

			Button btnSnapshot = new Button(" Get Snapshot");
			btnSnapshot.setOnMouseClicked(e -> {

			});

			Label space1 = new Label();
			space1.setPrefWidth(leftPane.getPrefWidth() * 0.02);
			Label space2 = new Label();
			space2.setPrefWidth(leftPane.getPrefWidth() * 0.02);
			Label timerLbl = new Label("Set Timeline Preferences");
			timeDropdown = new ComboBox<Integer>();
			timeDropdown.getItems().clear();
			timeDropdown.getItems().addAll(timeLblsGapArr);
			timeDropdown.getSelectionModel().select(4);
			leftPane.getChildren().addAll(space1, timerLbl, space2, timeDropdown, btnSnapshot);

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
			rightPane.getChildren().addAll(lblPage, currentPageLbl, slashSymbol, totalPagesLbl, blankSpace, start,
					space3, prev, space4, next, space5, end, space6);
			header.getChildren().addAll(leftPane, rightPane);

			// ---CENTER BODY AREA

			VBox body = new VBox();
			body.setStyle("-fx-border-color: silver");
			body.setPrefSize(vbox.getPrefWidth() * 0.8, vbox.getPrefHeight() * 0.95);
			createHistoricalDataArea(body);

			/*
			 * // ---FOOTER AREA Pane footer = new Pane();
			 * footer.setStyle("-fx-border-color: silver");
			 * footer.setPrefSize(vbox.getPrefWidth(), vbox.getPrefHeight() *
			 * 0.15);
			 */

			vbox.getChildren().addAll(header, body);
			VbForSnapShotBody = body;

		} catch (Exception e) {
			LOG.error("## Exception occured:", e);
		}
	}

	public VBox getMainContainer() {
		return VbForSnapShotBody;
	}

	public void getSnapShot() {
		try {

			WritableImage wim = new WritableImage(100, 100);

			wim = VbForSnapShotBody.snapshot(new SnapshotParameters(), null);
			File file = new File("anesthesiaImage2.png");
			try {
				ImageIO.write(SwingFXUtils.fromFXImage(wim, null), "png", file);
			} catch (Exception s) {
			}

		} catch (Exception e) {
			LOG.error("## Exception occured:", e);
		}
	}

	/**
	 * This method creates center body for Historical data window
	 *
	 * @param Anesbody
	 */
	public void createHistoricalDataArea(Pane body) {
		try {

			if (ControllerMediator.getInstance().getMainController().getDrawTabbedCenterForSnapshot().getCurrentTab()
					.toLowerCase().contains("anesthesia")) {
				histLegendsArr = anesLegendsArr;
				histScalesList = anesScalesList;

			} else if (ControllerMediator.getInstance().getMainController().getDrawTabbedCenterForSnapshot()
					.getCurrentTab().toLowerCase().contains("patient")
					&& ControllerMediator.getInstance().getMainController().getDrawTabbedCenterForSnapshot()
							.getCurrentTab().toLowerCase().contains("monitor")) {
				histLegendsArr = ptMntrLegendsArr;
				histScalesList = ptMntrScalesList;
			}

			TIME_PIXELS_GAP = centerPane.getPrefWidth() * 0.0975;
			calculatedGraphWidth = calcGraphWidth();

			// ---------------new changes for screen resize fix
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
			// --------------------------

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
			histGraphPanel.setPrefSize(calculatedGraphWidth, vboxForGraph.getPrefHeight());
			// histGraphPanel.setStyle(" -fx-border-color: silver;");
			histGraphCanvas = new Canvas(histGraphPanel.getPrefWidth(), histGraphPanel.getPrefHeight());
			histGraphBrush = histGraphCanvas.getGraphicsContext2D();
			histGraphBrush.setStroke(Color.SILVER);
			histGraphBrush.setLineWidth(1);
			histGraphPanel.getChildren().add(histGraphCanvas);
			drawVerticalMarkings(histGraphPanel, histGraphBrush);
			addHistYScales(histLegendsArr);

			// ---for timePane
			Pane horizotalSpace = new Pane();
			horizotalSpace.setPrefSize(histTimeHBox.getPrefWidth() - calculatedGraphWidth,
					histTimeHBox.getPrefHeight());
			histTimePane = new Pane();
			// histTimePane.setStyle(" -fx-border-color: blue;");
			histTimePane.setPrefSize(histTimeHBox.getPrefWidth(), histTimeHBox.getPrefHeight());
			histTimeHBox.getChildren().addAll(histTimePane);
			addTimeLabels(histTimePane, null, histStartDateTime, histMinsGap, "history");
			// comboBoxListener(timeDropdown, histTimeHBox, histTimePane);

			legendsHolder.getChildren().addAll(histLegendsPanel);
			scaleHolder.getChildren().addAll(histYscalePanel);

			hboxForLegends.getChildren().addAll(legendsHolder, scaleHolder);
			baseVboxForLegends.getChildren().addAll(horizotalSpace, hboxForLegends);
			vboxForGraph.getChildren().addAll(histTimeHBox, histGraphPanel);
			base.getChildren().addAll(baseVboxForLegends, vboxForGraph);
			// base.getChildren().addAll(baseVboxForLegends);
			body.getChildren().addAll(base);

		} catch (Exception e) {
			LOG.error("## Exception occured:", e);
		}
	}

	/**
	 * This method divides Historical Data Screen Y-Scale into multiple scale
	 * holders
	 *
	 * @param basePane
	 */
	public void createHistScaleHolders(HBox basePane) {
		try {

			Pane verticalSpace = new Pane();
			verticalSpace.setPrefSize(basePane.getPrefWidth() * 0.08, basePane.getPrefHeight());
			histYScaleHolder2 = new Pane();
			// histYScaleHolder2.setStyle(" -fx-border-color:silver;");
			histYScaleHolder2.setPrefSize(basePane.getPrefWidth() * 0.3, basePane.getPrefHeight());

			histYScaleHolder3 = new Pane();
			// histYScaleHolder3.setStyle(" -fx-border-color:silver;");
			histYScaleHolder3.setPrefSize(basePane.getPrefWidth() * 0.3, basePane.getPrefHeight());

			histYScaleHolder4 = new Pane();
			// histYScaleHolder4.setStyle(" -fx-border-color:silver;");
			histYScaleHolder4.setPrefSize(basePane.getPrefWidth() * 0.3, basePane.getPrefHeight());

			basePane.getChildren().addAll(histYScaleHolder2, histYScaleHolder3, histYScaleHolder4, verticalSpace);

		} catch (Exception e) {
			LOG.error("## Exception occured:", e);
		}
	}

	/**
	 * This method compares provided dates
	 *
	 * @param dateTime1
	 * @param dateTime2
	 * @return
	 */
	public int compareDates(String dateTime1, String dateTime2) {
		int result = 0;

		try {
			Calendar date1 = Calendar.getInstance();
			Calendar date2 = Calendar.getInstance();

			date1.setTime(sdfNew.parse(dateTime1));
			date2.setTime(sdfNew.parse(dateTime2));

			if (date1.get(Calendar.YEAR) > date2.get(Calendar.YEAR)) {
				result = 1;
			} else if (date1.get(Calendar.YEAR) < date2.get(Calendar.YEAR)) {
				result = -1;
			} else if (date1.get(Calendar.YEAR) == date2.get(Calendar.YEAR)) {
				if (date1.get(Calendar.MONTH) > date2.get(Calendar.MONTH)) {
					result = 1;
				} else if (date1.get(Calendar.MONTH) < date2.get(Calendar.MONTH)) {
					result = -1;
				} else if (date1.get(Calendar.MONTH) == date2.get(Calendar.MONTH)) {
					if (date1.get(Calendar.DAY_OF_MONTH) > date2.get(Calendar.DAY_OF_MONTH)) {
						result = 1;
					} else if (date1.get(Calendar.DAY_OF_MONTH) < date2.get(Calendar.DAY_OF_MONTH)) {
						result = -1;
					} else if (date1.get(Calendar.DAY_OF_MONTH) == date2.get(Calendar.DAY_OF_MONTH)) {
						result = 0;
					}
				}
			}
		} catch (ParseException e) {
			LOG.error("## Exception occured:", e);
		}

		return result;
	}

	/**
	 * This method compares provided time values
	 *
	 * @param dateTime1
	 * @param dateTime2
	 * @return
	 */
	public int compareTime(String dateTime1, String dateTime2) {
		int result = 1;
		try {
			Calendar date1 = Calendar.getInstance();
			Calendar date2 = Calendar.getInstance();

			date1.setTime(sdfNew.parse(dateTime1));
			date2.setTime(sdfNew.parse(dateTime2));

			if (date1.get(Calendar.HOUR_OF_DAY) > date2.get(Calendar.HOUR_OF_DAY)) {
				result = 1;
			} else if (date1.get(Calendar.HOUR_OF_DAY) < date2.get(Calendar.HOUR_OF_DAY)) {
				result = -1;
			}
			if (date1.get(Calendar.HOUR_OF_DAY) == date2.get(Calendar.HOUR_OF_DAY)) {
				if (date1.get(Calendar.MINUTE) > date2.get(Calendar.MINUTE)) {
					result = 1;
				} else if (date1.get(Calendar.MINUTE) < date2.get(Calendar.MINUTE)) {
					result = -1;
				} else if (date1.get(Calendar.MINUTE) == date2.get(Calendar.MINUTE)) {
					result = 0;
				}

			}

		} catch (ParseException e) {
			LOG.error("## Exception occured:", e);
		}
		return result;
	}

	/**
	 * This method clears graphPanel and redraws vertical and horizontal lines.
	 * It also resets base Y scale
	 *
	 * @param graphPanel
	 * @param canvas
	 * @param graphBrush
	 * @param scale
	 * @param scaleHolder
	 */
	public void clearGraphArea(Pane graphPanel, Canvas canvas, GraphicsContext graphBrush, Double scale[],
			Pane scaleHolder) {
		try {

			graphPanel.getChildren().clear();

			canvas = new Canvas(graphPanel.getPrefWidth(), graphPanel.getPrefHeight());
			graphBrush = canvas.getGraphicsContext2D();
			graphBrush.setStroke(Color.SILVER);
			graphBrush.setLineWidth(1);
			graphPanel.getChildren().add(canvas);
			ControllerMediator.getInstance().getMainController().getDrawTabbedCenterForSnapshot()
					.setHistGraphBrush(graphBrush);

			drawVerticalMarkings(graphPanel, graphBrush);
			addYScale(scale, true, scaleHolder, graphBrush, graphPanel.getPrefWidth(), yscale4Color);

			GetSnapshot obj = ControllerMediator.getInstance().getGetSnapshot();
			Map<String, String[]> paramValsList = ExtractDataForTrends.getInstance().extract(obj.getCsvData(),
					histFirstDateTimeLbl, histLastDateTimeLbl);
			if (ControllerMediator.getInstance().getMainController().getDrawTabbedCenterForSnapshot().getCurrentTab()
					.toLowerCase().contains("anesthesia")) {
				obj.plotStaticAnesGraph(paramValsList);
			} else if (ControllerMediator.getInstance().getMainController().getDrawTabbedCenterForSnapshot()
					.getCurrentTab().toLowerCase().contains("patient")
					&& ControllerMediator.getInstance().getMainController().getDrawTabbedCenterForSnapshot()
							.getCurrentTab().toLowerCase().contains("monitor")) {
				obj.plotStaticPtMntrGraph(paramValsList);
			}

		} catch (Exception e) {
			LOG.error("## Exception occured:", e);
		}
	}

	/**
	 * This method resets all time labels in timeline from provided starting
	 * time and clears entire graph area
	 *
	 * @param startingPt
	 */
	public void recreateHistTimeLine(String startingPt) {
		try {

			// ---remove old time pane
			histTimeHBox.getChildren().remove(0);
			// ---add new time pane
			histTimePane.getChildren().clear();
			histTimeHBox.getChildren().add(histTimePane);
			// ---add labels to time pane
			addTimeLabels(histTimePane, null, startingPt, histMinsGap, "history");

			clearGraphArea(histGraphPanel, histGraphCanvas, histGraphBrush, histBaseYScale, histYScaleHolder4);

		} catch (Exception e) {
			LOG.error("## Exception occured:", e);
		}
	}

	public void getAllSnapshots() {
		try {

			if (ControllerMediator.getInstance().getMainController()
					.getDrawTabbedCenterForSnapshot().totalPgValue != 0) {
				currentPgValue = 1;
				histGraphPanel.setPrefWidth(VbForSnapShotBody.getPrefWidth());
				String path = "";
				String decodedPath = "";
				String realPath = "";
				try {
					path = SystemConfiguration.class.getProtectionDomain().getCodeSource().getLocation().toURI()
							.getPath();
				} catch (URISyntaxException e) {
					LOG.error("## Exception occured:", e);
					e.printStackTrace();
				}

				try {
					decodedPath = URLDecoder.decode(path, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					LOG.error("## Exception occured:", e);
					e.printStackTrace();
				}
				decodedPath = (new File(decodedPath)).getParentFile().getPath() + File.separator + "jasper"
						+ File.separator + "IntraOpPatientReport.jrxml";
				realPath = decodedPath;

				String selectedTab = "";
				selectedTab = ControllerMediator.getInstance().getMainController().getDrawTabbedCenterForSnapshot()
						.getCurrentTab();

				List<ChartImageModel> imageList = ControllerMediator.getInstance().getMainController()
						.getDrawTabbedCenterForSnapshot().getImageListForReport();

				for (int i = 0; i < ControllerMediator.getInstance().getMainController()
						.getDrawTabbedCenterForSnapshot().totalPgValue; i++) {

					WritableImage wim = new WritableImage(600, 500);
					VbForSnapShotBody.setRotate(90);
					wim = VbForSnapShotBody.snapshot(new SnapshotParameters(), null);

					String imagePath = realPath
							.replaceAll("IntraOpPatientReport.jrxml", selectedTab + currentPgValue + ".png").toString();
					File file = new File(imagePath);
					try {
						ImageIO.write(SwingFXUtils.fromFXImage(wim, null), "png", file);
					} catch (Exception s) {
					}
					if (ControllerMediator.getInstance().getMainController()
							.getDrawTabbedCenterForSnapshot().isS5Called) {
						if (imageList != null && imageList.size() != 0 && imageList.size()>i) {

							if (selectedTab.equalsIgnoreCase("anesthesia")) {
								imageList.get(i).setAnesthesiaImagePath(imagePath);
								imageList.get(i).setAnesthesiaText("Anesthesia-" + currentPgValue);
							} else {
								imageList.get(i).setS5MonitorImagePath(imagePath);
								imageList.get(i).setS5MonitorText("S5PatientMonitor-" + currentPgValue);
							}

						} else {
							ChartImageModel imageDetails = new ChartImageModel();
							if (selectedTab.equalsIgnoreCase("anesthesia")) {
								imageDetails.setAnesthesiaImagePath(imagePath);
								imageDetails.setAnesthesiaText("Anesthesia-" + currentPgValue);
							} else {
								imageDetails.setS5MonitorImagePath(imagePath);
								imageDetails.setS5MonitorText("S5PatientMonitor-" + currentPgValue);
							}
							imageList.add(imageDetails);

						}
					} else {
						ChartImageModel imageDetails = new ChartImageModel();
						if (selectedTab.equalsIgnoreCase("anesthesia")) {
							imageDetails.setAnesthesiaImagePath(imagePath);
							imageDetails.setAnesthesiaText("Anesthesia-" + currentPgValue);
						} else {
							imageDetails.setS5MonitorImagePath(imagePath);
							imageDetails.setS5MonitorText("S5PatientMonitor-" + currentPgValue);
						}
						imageList.add(imageDetails);
					}

					ControllerMediator.getInstance().getMainController().getDrawTabbedCenterForSnapshot()
							.setImageListForReport(imageList);

					recreateHistTimeLine(histLastDateTimeLbl);
					currentPgValue += 1;

				}
				if (ControllerMediator.getInstance().getMainController().getDrawTabbedCenterForSnapshot().isS5Called
						&& ControllerMediator.getInstance().GetSnapshotController().isReportGenerated()) {
					System.out.println("Generate Report is called 1st block");
					ControllerMediator.getInstance().GetSnapshotController().startGeneratingReport();
					ControllerMediator.getInstance().GetSnapshotController().setReportGenerated(false);
				}
				if (!ControllerMediator.getInstance().getMainController().getDrawTabbedCenterForSnapshot().isS5Called) {
					DrawTabbedCenterForSnapshot drawTabbedCenterObj = ControllerMediator.getInstance()
							.getMainController().getDrawTabbedCenterForSnapshot();
					drawTabbedCenterObj.setCurrentTab("patientMonitor");
					ControllerMediator.getInstance().GetSnapshotController().getGetSnapShot()
							.initializeContainer(drawTabbedCenterObj);
					ControllerMediator.getInstance().getMainController().getDrawTabbedCenterForSnapshot()
							.setS5Called(true);
				}

			} else {
				if (!ControllerMediator.getInstance().getMainController().getDrawTabbedCenterForSnapshot().isS5Called) {
					DrawTabbedCenterForSnapshot drawTabbedCenterObj = ControllerMediator.getInstance()
							.getMainController().getDrawTabbedCenterForSnapshot();
					drawTabbedCenterObj.setCurrentTab("patientMonitor");
					ControllerMediator.getInstance().GetSnapshotController().getGetSnapShot()
							.initializeContainer(drawTabbedCenterObj);

					ControllerMediator.getInstance().getMainController().getDrawTabbedCenterForSnapshot()
							.setS5Called(true);
				}else{
					if (ControllerMediator.getInstance().getMainController().getDrawTabbedCenterForSnapshot().isS5Called
							&& ControllerMediator.getInstance().GetSnapshotController().isReportGenerated()) {
					System.out.println("Generate Report is called 2nd block");
					ControllerMediator.getInstance().GetSnapshotController().startGeneratingReport();
					ControllerMediator.getInstance().GetSnapshotController().setReportGenerated(false);
					}
				}

			}

		} catch (Exception e) {
			LOG.error("## Exception occured:", e);
		}
	}

	/**
	 * This method creates Y scale for the passed Anesthesia parameter and
	 * maintains a list of max four scales for Anesthesia Y-Axis
	 *
	 * @param param
	 * @param min
	 * @param max
	 */
	public void setScaleForAnesParameters(String param, double min, double max) {
		try {

			if (param.equalsIgnoreCase(ParamMinMaxRange.PPEAK)) {
				paramScaleMap.put(ParamMinMaxRange.PPEAK, new Double[] { max, max * 0.25 });
				anesScalesList.add(paramScaleMap.get(ParamMinMaxRange.PPEAK));
				ParamMinMaxRange.setPPEAK_COLOR(yscale4Color);
			} else if (param.equalsIgnoreCase(ParamMinMaxRange.PMEAN)) {
				paramScaleMap.put(ParamMinMaxRange.PMEAN, new Double[] { max, max * 0.125 });
				anesScalesList.add(paramScaleMap.get(ParamMinMaxRange.PMEAN));
				ParamMinMaxRange.setPMEAN_COLOR(yscale3Color);
			} else if (param.equalsIgnoreCase(ParamMinMaxRange.PEEPEXT)) {
				paramScaleMap.put(ParamMinMaxRange.PEEPEXT, paramScaleMap.get(ParamMinMaxRange.PMEAN));
				ParamMinMaxRange.setPEEPEXT_COLOR(ParamMinMaxRange.PMEAN_COLOR);
			} else if (param.equalsIgnoreCase(ParamMinMaxRange.CIRCUITO2)) {
				paramScaleMap.put(ParamMinMaxRange.CIRCUITO2, paramScaleMap.get(ParamMinMaxRange.PPEAK));
				ParamMinMaxRange.setCIRCUITO2_COLOR(ParamMinMaxRange.PPEAK_COLOR);
			} else if (param.equalsIgnoreCase(ParamMinMaxRange.TVEXP)) {
				paramScaleMap.put(ParamMinMaxRange.TVEXP, new Double[] { max, max * 0.25 });
				anesScalesList.add(paramScaleMap.get(ParamMinMaxRange.TVEXP));
				ParamMinMaxRange.setTVEXP_COLOR(yscale2Color);
			} else if (param.equalsIgnoreCase(ParamMinMaxRange.MVEXP)) {
				paramScaleMap.put(ParamMinMaxRange.MVEXP, paramScaleMap.get(ParamMinMaxRange.PMEAN));
				ParamMinMaxRange.setMVEXP_COLOR(ParamMinMaxRange.PMEAN_COLOR);
			} else if (param.equalsIgnoreCase(ParamMinMaxRange.RR)) {
				paramScaleMap.put(ParamMinMaxRange.RR, paramScaleMap.get(ParamMinMaxRange.PPEAK));
				ParamMinMaxRange.setRR_COLOR(ParamMinMaxRange.PPEAK_COLOR);
			}

		} catch (Exception e) {
			LOG.error("## Exception occured:", e);
		}
	}

	/**
	 * This method creates Y scale for the passed S5 PtMonitor parameter and
	 * maintains a list of max four scales for S5 PtMonitor Y-Axis
	 *
	 * @param param
	 * @param min
	 * @param max
	 */
	public void setScaleForPtMntrParameters(String param, double min, double max) {
		try {
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
			else if (param.equalsIgnoreCase(ParamMinMaxRange.IBPMEAN))
			{
				paramScaleMap.put(ParamMinMaxRange.IBPMEAN, paramScaleMap.get(ParamMinMaxRange.HR));
				ParamMinMaxRange.setIBP_COLOR(ParamMinMaxRange.HR_COLOR);
			}
			else if (param.equalsIgnoreCase(ParamMinMaxRange.MAC))
			{
				paramScaleMap.put(ParamMinMaxRange.MAC, new Double[] { max, max * 0.25 });
				ptMntrScalesList.add(paramScaleMap.get(ParamMinMaxRange.MAC));
				ParamMinMaxRange.setMAC_COLOR(yscale2Color);

				/*paramScaleMap.put(ParamMinMaxRange.MAC, paramScaleMap.get(ParamMinMaxRange.SPO2));
				ParamMinMaxRange.setMAC_COLOR(ParamMinMaxRange.SPO2_COLOR);*/
			}
		} catch (Exception e) {
			LOG.error("## Exception occured:", e);
		}
	}

	public void setLegendsArray() {
		try {
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
		            ParamMinMaxRange.IBPMEAN_LEGEND+PIPE_SYMBOL+ParamMinMaxRange.IBPMEAN+PIPE_SYMBOL+ParamMinMaxRange.IBP_COLOR
					};

			ptMntrLegendsArr = tempArr2;
		} catch (Exception e) {
			LOG.error("## Exception occured:", e);
		}
	}

	/**
	 * This method adds legend symbols and scales on Y-Axis for Anesthesia
	 * Window
	 */
	public void addAnesYScales() {
		try {

			addLegendLabels(anesLegendsPanel, Arrays.asList(anesLegendsArr));

			if (anesScalesList != null) {
				for (Double[] array : anesScalesList) {
					if (anesBaseYScale.length == 0) {
						anesBaseYScale = array;
					} else if (anesYScale3.length == 0) {
						anesYScale3 = array;
					} else if (anesYScale2.length == 0) {
						anesYScale2 = array;
					} else if (anesYScale1.length == 0) {
						anesYScale1 = array;
					}
				}

				addYScale(anesBaseYScale, true, anesYScaleHolder4, anesBaseGraphBrush, anesBaseGraph.getPrefWidth(),
						yscale4Color);
				addYScale(anesYScale3, false, anesYScaleHolder3, null, 0, yscale3Color);
				addYScale(anesYScale2, false, anesYScaleHolder2, null, 0, yscale2Color);
				addYScale(anesYScale1, false, anesYScaleHolder1, null, 0, yscale1Color);
			}

		} catch (Exception e) {
			LOG.error("## Exception occured:", e);
		}
	}

	/**
	 * This method adds legend symbols and scales on Y-Axis for S5 PtMonitor
	 * Window
	 */
	public void addPtMntrYScales() {
		try {

			addLegendLabels(ptMntrLegendsPanel, Arrays.asList(ptMntrLegendsArr));
			if (ptMntrScalesList != null) {
				for (Double[] array : ptMntrScalesList) {
					if (ptMntrBaseYScale.length == 0) {
						ptMntrBaseYScale = array;
					} else if (ptMntrYScale3.length == 0) {
						ptMntrYScale3 = array;
					} else if (ptMntrYScale2.length == 0) {
						ptMntrYScale2 = array;
					} else if (ptMntrYScale1.length == 0) {
						ptMntrYScale1 = array;
					}
				}

				addYScale(ptMntrBaseYScale, true, ptMntrYScaleHolder4, ptMntrBaseGraphBrush,
						ptMntrBaseGraph.getPrefWidth(), yscale4Color);
				addYScale(ptMntrYScale3, false, ptMntrYScaleHolder3, null, 0, yscale3Color);
				addYScale(ptMntrYScale2, false, ptMntrYScaleHolder2, null, 0, yscale2Color);
				addYScale(ptMntrYScale1, false, ptMntrYScaleHolder1, null, 0, yscale1Color);
			}

		} catch (Exception e) {
			LOG.error("## Exception occured:", e);
		}
	}

	/**
	 * This method adds legend symbols and scales on Y-Axis for Historical Data
	 * Window
	 */
	public void addHistYScales(String[] legendArr) {
		try {

			addLegendLabels(histLegendsPanel, Arrays.asList(legendArr));
			histBaseYScale = new Double[] {};
			histYScale3 = new Double[] {};
			histYScale2 = new Double[] {};
			histYScale1 = new Double[] {};

			if (histScalesList != null) {
				for (Double[] array : histScalesList) {
					if (histBaseYScale.length == 0) {
						histBaseYScale = array;
					} else if (histYScale3.length == 0) {
						histYScale3 = array;
					} else if (histYScale2.length == 0) {
						histYScale2 = array;
					} else if (histYScale1.length == 0) {
						histYScale1 = array;
					}
				}
			}
			addYScale(histBaseYScale, true, histYScaleHolder4, histGraphBrush, histGraphPanel.getPrefWidth(),
					yscale4Color);
			addYScale(histYScale3, false, histYScaleHolder3, null, 0, yscale3Color);
			addYScale(histYScale2, false, histYScaleHolder2, null, 0, yscale2Color);
			// addYScale(histYScale1, false, histYScaleHolder1, null, 0,
			// yscale1Color);

		} catch (Exception e) {
			LOG.error("## Exception occured:", e);
		}
	}

	// Changes Done By Sudeep

}
