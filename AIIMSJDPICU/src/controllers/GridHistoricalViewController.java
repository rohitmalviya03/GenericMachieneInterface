/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package controllers;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.cdac.common.GeneratedEntities.AnethesiaMachineData;
import com.cdac.common.GeneratedEntities.Fluidvalue;
import com.cdac.common.GeneratedEntities.IntraopMedicationlog;
import com.cdac.common.GeneratedEntities.IntraopOutput;
import com.cdac.common.GeneratedEntities.Investigationparameterfields;
import com.cdac.common.GeneratedEntities.Investigationstests;
import com.cdac.common.GeneratedEntities.PatientMonitorData;
import com.cdac.common.dao.MedicationLogDao;
import com.cdac.common.dao.MedicationLogDaoImpl;
import com.cdac.common.pojoClasses.InvestigationMapperAndValue;
import com.cdac.common.pojoClasses.InvestigationSetValue;
import com.cdac.framework.SystemConfiguration;

import application.FxmlView;
import application.Main;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import mediatorPattern.ControllerMediator;
import model.EventLogSession;
import model.MasterDataSession;
import model.PatientSession;
import services.GetAnesthesiaMachineDataTimeCaseService;
import services.GetHistoryDataService;
import services.GetHistoryParametersService;
import services.GetIntraopMedicationlogService;
import services.GetInvestigationMapperAndValueService;
import services.GetPatientMonitorDataTimeCaseService;
import services.GetTotalFluidVolumeService;
import services.GetTotalMedicationVolumeListService;
import services.GetTotalOutputTypeVolumeService;
import services.SearchFluidValueService;
import services.SearchIntraOpOutputService;
import utility.DrawTabbedCenter;
import utility.ParamMinMaxRange;

/**
 * GridHistoricalViewController.java <br>
 * Purpose: This class contains logic for creating historical grid view for
 * multiple sections(S5PatientMonitor, Anesthesia, Medication etc)
 *
 * @author Rohit_Sharma41
 *
 */
public class GridHistoricalViewController implements Initializable
{

	@FXML
	private VBox baseVbox;

	@FXML
	private StackPane header;

	@FXML
	private Button btnClose;

	@FXML
	private VBox centerVBox;

	private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

	static final int MINUTES_PER_HOUR = 60;
	static final int SECONDS_PER_MINUTE = 60;
	static final int SECONDS_PER_HOUR = SECONDS_PER_MINUTE * MINUTES_PER_HOUR;

	private static final String PTMNTR_GRID_NAME = "S5 Patient Monitor Vitals (Live)";
	private static final String ANES_GRID_NAME = "Anesthesia Vitals (Live)";
	public static final String MED_GRID_NAME = "Medication";
	public static final String FLUID_GRID_NAME = "Fluid";
	public static final String OUTPUT_GRID_NAME = "Output";

	private String ptMntrTableRowHeader[] = { "HR " + ParamMinMaxRange.HR_UNIT,
	        "IBPSys " + ParamMinMaxRange.IBP_UNIT,
	        "IBPDia " + ParamMinMaxRange.IBP_UNIT,
	        "SpO2 " + ParamMinMaxRange.SPO2_UNIT,
	        "EtCO2 " + ParamMinMaxRange.ETCO2_UNIT ,
	        "Temp1 "+ParamMinMaxRange.TEMP_UNIT,
	        "Temp2 " +ParamMinMaxRange.TEMP_UNIT,
	        "BIS " + ParamMinMaxRange.BIS_UNIT,
	        "ETAGENT"+ParamMinMaxRange.ETAGENT_UNIT,
	        "MAC"+ParamMinMaxRange.MAC_UNIT,
	        "IBPMean " + ParamMinMaxRange.IBP_UNIT,
	        "NIBPSys " + ParamMinMaxRange.NIBP_UNIT,
	        "NIBPDia " + ParamMinMaxRange.NIBP_UNIT,
	        "NIBPMean " + ParamMinMaxRange.NIBP_UNIT};
	private String anesTableRowHeader[] = { "Ppeak " + ParamMinMaxRange.PPEAK_UNIT,
		        "Pmean " + ParamMinMaxRange.PMEAN_UNIT,
		        "PEEP " + ParamMinMaxRange.PEEPEXT_UNIT,
		        "CircuitO2 " + ParamMinMaxRange.CIRCUITO2_UNIT,
		        "TVexp " + ParamMinMaxRange.TVEXP_UNIT,
		        "MVexp " + ParamMinMaxRange.MVEXP_UNIT,
		        "RR " + ParamMinMaxRange.RR_UNIT };
	private String[] outputsTableRowHeader = { "Blood", "Urine" };

	private double columnPrefWidth;
	private double rowPrefHeight = 40;
	private static int NO_OF_GRID_COLUMNS = 15;

	private List<Calendar> headerTimeLblsList = new LinkedList<Calendar>();
	
	int mingap = 	SystemConfiguration.getInstance().getMinsGap();

	// ---Dynamic grid creation
	private Map<String, GridPane> paramGridMap = new HashMap<String, GridPane>();
	private Map<String, GridPane> totalsGridMap = new HashMap<String, GridPane>();
	private Map<String, GridPane> textboxGridMap = new HashMap<String, GridPane>();
	private Map<String, List<String>> paramListMap = new HashMap<String, List<String>>();
	private Map<String, List<String>> totalsListMap = new HashMap<String, List<String>>();
	private Map<String, List<Label>> textboxListMap = new HashMap<String, List<Label>>();
	private Map<String, Map<String, Label>> totalLblsMap = new HashMap<String, Map<String, Label>>();

	private DrawTabbedCenter drawTabbedCenter;

	//private List<String> performedTestList = new ArrayList<String>();

	private VBox testsHolder;

	private Calendar gridStartTimeStamp, gridEndTimeStamp, gridFirstTimeStamp, gridLastTimeStamp;
	private Label currentPageLbl, totalPagesLbl;
	private int currentPgValue, totalPgValue;

	private Map<String,List<Investigationparameterfields>> listOfTestParameters;
	private List<String> medNamesList;
	private List<String> fluidNamesList;

	private List<PatientMonitorData> patientMonitorDataList;
	private List<AnethesiaMachineData> anethesiaMachineDataList;
	private List<IntraopMedicationlog> allMedicationsList;
	private List<IntraopOutput> allOutputsList;
	private List<Fluidvalue> allfluidList;
	private Map<String, Map<Date, List<InvestigationSetValue>>> allTestValueMap = new HashMap<>();

	private String clickedGridCellParam = "", clickedTestGrid = "";
	private Calendar clickedGridCellTime;
	private ContextMenu ptmntrContextMenu, anesContextMenu, medContextMenu, fluidContextMenu, outputContextMenu,testContextMenu;
	private Map<GridPane, EventHandler<MouseEvent>> testsEventHandlersMap = new HashMap<GridPane, EventHandler<MouseEvent>>();


	private EventHandler<MouseEvent> ptmntrGridClickHandler;
	private EventHandler<MouseEvent> anesGridClickHandler;
	private EventHandler<MouseEvent> medGridClickHandler;
	private EventHandler<MouseEvent> fluidGridClickHandler;
	private EventHandler<MouseEvent> outputGridClickHandler;

	private EventHandler<WorkerStateEvent> ptmntrTimeCaseServiceSuccessHandler;
	private EventHandler<WorkerStateEvent> ptmntrTimeCaseServiceFailedHandler;
	private EventHandler<WorkerStateEvent> anesTimeCaseServiceSuccessHandler;
	private EventHandler<WorkerStateEvent> anesTimeCaseServiceFailedHandler;
	private EventHandler<WorkerStateEvent> getMedlogServiceSuccessHandler;
	private EventHandler<WorkerStateEvent> getMedlogServiceFailedHandler;
	private EventHandler<WorkerStateEvent> getFluidTotalsServiceSuccessHandler;
	private EventHandler<WorkerStateEvent> getFluidTotalsServiceFailedHandler;
	private EventHandler<WorkerStateEvent> getOutputTotalsServiceSuccessHandler;
	private EventHandler<WorkerStateEvent> getOutputTotalsServiceFailedHandler;
	private EventHandler<WorkerStateEvent> searchOutputServiceSuccessHandler;
	private EventHandler<WorkerStateEvent> searchOutputServiceFailedHandler;
	private EventHandler<WorkerStateEvent> investigationMapperAndValueServiceSuccessHandler;
	private EventHandler<WorkerStateEvent> investigationMapperAndValueServiceFailedHandler;
	private EventHandler<WorkerStateEvent> getMedTotalsServiceSuccessHandler;
	private EventHandler<WorkerStateEvent> getMedTotalsServiceFailedHandler;
	private EventHandler<WorkerStateEvent> historyDataServiceSuccessHandler;
	private EventHandler<WorkerStateEvent> historyDataServiceFailedHandler;
	private EventHandler<WorkerStateEvent> historyParamServiceSuccessHandler;
	private EventHandler<WorkerStateEvent> historyParamServiceFailedHandler;
	private EventHandler<WorkerStateEvent> searchFluidServiceSuccessHandler;
	private EventHandler<WorkerStateEvent> searchFluidServiceFailedHandler;

	private EventHandler<MouseEvent> btnStartHandler;

	private EventHandler<MouseEvent> btnEndHandler;

	private EventHandler<MouseEvent> btnNextHandler;

	private EventHandler<MouseEvent> btnPrevHandler;

	private EventHandler<MouseEvent> btnCloseHandler;

	private EventHandler<ActionEvent> item1Handler;

	private EventHandler<ActionEvent> item12Handler;

	private EventHandler<ActionEvent> item2Handler;

	private EventHandler<ActionEvent> startInfItemHandler;

	private EventHandler<ActionEvent> stopInfItemHandler;

	private EventHandler<ActionEvent> editInfItemHandler;

	private EventHandler<ActionEvent> enterHandler;

	private EventHandler<ActionEvent> enter2Handler;

	private EventHandler<ActionEvent> startFluidHandler;

	private EventHandler<ActionEvent> stopFluidHandler;

	private Button start, end, next, prev;
	private MedicationLogDao  medicationLogDao =  MedicationLogDaoImpl.getInstance();

	private MenuItem item1, item12, item2, startInfItem, stopInfItem, editInfItem, startFluid, stopFluid, enter2, enter;

	private ImageView lodingImg;

	private static final Logger LOG = Logger.getLogger(GridHistoricalViewController.class.getName());

	
	public Map<String, Map<String, Label>> getTotalLblsMap() {
		return totalLblsMap;
	}

	public void setTotalLblsMap(Map<String, Map<String, Label>> totalLblsMap) {
		this.totalLblsMap = totalLblsMap;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{

		try
		{
			ControllerMediator.getInstance().registerGridHistoricalViewController(this);
			baseVbox.setStyle("-fx-background-color:white;-fx-border-color:gray; -fx-border-width:5px");
			baseVbox.getStylesheets().add(this.getClass().getResource("/styles/newStyles.css").toString());

			drawTabbedCenter = ControllerMediator.getInstance().getMainController().getDrawTabbedCenter();

			setComponentsSize();
			setTimestamps();

			lodingImg = new ImageView();
			Image spinnerImg = new Image(this.getClass().getResourceAsStream("/res/Spinner.gif"));
			lodingImg.setImage(spinnerImg);

			createHistoricalDataBody(centerVBox);

			btnCloseHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent event) {
					try
					{
						removeHandlers();
						Main.getInstance().getStageManager().closeSecondaryStage();
					}
					catch (Exception e)
					{
						LOG.error("## Exception occured:", e);
					}
				}
			};
			btnClose.addEventHandler(MouseEvent.MOUSE_CLICKED, btnCloseHandler);
		}
		catch (Exception e)
		{
			LOG.error("## Exception occured:", e);

		}
	}

	/**
	 * This method sets percentage width and height to screen components
	 */
	private void setComponentsSize()
	{
		double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
		double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();
		baseVbox.setPrefSize(screenWidth * 0.995, screenHeight * 0.85);
		header.setPrefSize(baseVbox.getPrefWidth() * 0.999, baseVbox.getPrefHeight() * 0.06);
		centerVBox.setPrefSize(baseVbox.getPrefWidth(), baseVbox.getPrefHeight() * 0.94);
	}

	/**
	 * This method removes all event handlers associated with various components
	 */
	private void removeHandlers()
	{
		if (start != null)
			start.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnStartHandler);
		if (end != null)
			end.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnEndHandler);
		if (prev != null)
			prev.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnPrevHandler);
		if (next != null)
			next.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnNextHandler);
		if (item1 != null)
			item1.removeEventHandler(ActionEvent.ACTION, item1Handler);
		if (item12 != null)
			item12.removeEventHandler(ActionEvent.ACTION, item12Handler);
		if (item2 != null)
			item2.removeEventHandler(ActionEvent.ACTION, item2Handler);
		if (enter != null)
			enter.removeEventHandler(ActionEvent.ACTION, enterHandler);
		if (enter2 != null)
			enter2.removeEventHandler(ActionEvent.ACTION, enter2Handler);
		if (startInfItem != null)
			startInfItem.removeEventHandler(ActionEvent.ACTION, startInfItemHandler);
		if (stopInfItem != null)
			stopInfItem.removeEventHandler(ActionEvent.ACTION, stopInfItemHandler);
		if (startFluid != null)
			startFluid.removeEventHandler(ActionEvent.ACTION, startFluidHandler);
		if (stopFluid != null)
			stopFluid.removeEventHandler(ActionEvent.ACTION, stopFluidHandler);
		if (btnClose != null)
			btnClose.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnCloseHandler);

		for (Map.Entry<String, GridPane> entry : textboxGridMap.entrySet())
		{
			if (entry.getKey().equalsIgnoreCase(PTMNTR_GRID_NAME))
				entry.getValue().removeEventHandler(MouseEvent.MOUSE_CLICKED, ptmntrGridClickHandler);
			else if (entry.getKey().equalsIgnoreCase(ANES_GRID_NAME))
				entry.getValue().removeEventHandler(MouseEvent.MOUSE_CLICKED, anesGridClickHandler);
			else if (entry.getKey().equalsIgnoreCase(OUTPUT_GRID_NAME))
				entry.getValue().removeEventHandler(MouseEvent.MOUSE_CLICKED, outputGridClickHandler);
			else if (entry.getKey().equalsIgnoreCase(FLUID_GRID_NAME))
				entry.getValue().removeEventHandler(MouseEvent.MOUSE_CLICKED, fluidGridClickHandler);
			else if (entry.getKey().equalsIgnoreCase(MED_GRID_NAME))
				entry.getValue().removeEventHandler(MouseEvent.MOUSE_CLICKED, medGridClickHandler);
		}

		for (Map.Entry<GridPane, EventHandler<MouseEvent>> entry : testsEventHandlersMap.entrySet())
		{
			entry.getKey().removeEventHandler(MouseEvent.MOUSE_CLICKED, entry.getValue());
		}
	}

	/*public void bindPopupFxml(String path)
	{

		try
		{
			Pane pane = FXMLLoader.load(getClass().getResource(path));

			Stage primaryStage = Main.getInstance().getChildStage();
			Stage stage = new Stage();
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(primaryStage);
			Main.getInstance().setChildStage(stage);
			Scene newScene= new Scene(pane);
			stage.setScene(newScene);
			stage.initStyle(StageStyle.UNDECORATED);
			stage.show();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}*/

	/**
	 * This method creates Header, Body and Footer sections in the Historical
	 * grid window
	 *
	 * @param vbox vbox in which these sections are created
	 */
	private void createHistoricalDataBody(VBox vbox)
	{
		currentPgValue = 1;
		totalPgValue = calculateTotalPg();

		// ---HEADER AREA
		HBox header = new HBox();
		header.setPrefSize(vbox.getPrefWidth(), vbox.getPrefHeight() * 0.08);

		HBox rightPane = new HBox();
		rightPane.setAlignment(Pos.CENTER);
		rightPane.setSpacing(10);
		rightPane.setPrefSize(header.getPrefWidth(), header.getPrefHeight());
		Label lblPage = new Label("Page :");
		currentPageLbl = new Label(String.valueOf(currentPgValue));// currentPgValue
		totalPagesLbl = new Label(String.valueOf(totalPgValue));// totalPgValue
		Label slashSymbol = new Label("/");

		String dashboardBtnStyle="-fx-background-color:rgb(58.0, 163.0, 178.0);-fx-border-width:0px;-fx-text-fill:white ";
		start = new Button("|\u25C0");
		start.setFont(new Font(15));
		start.setDisable(true);
		start.setStyle(dashboardBtnStyle);
		end = new Button("\u25B6|");
		end.setFont(new Font(15));
		end.setStyle(dashboardBtnStyle);
		next = new Button("\u25B6");
		next.setFont(new Font(15));
		next.setStyle(dashboardBtnStyle);
		prev = new Button("\u25C0");
		prev.setFont(new Font(15));
		prev.setDisable(true);
		prev.setStyle(dashboardBtnStyle);

		rightPane.getChildren().addAll(lblPage, currentPageLbl, slashSymbol, totalPagesLbl, start, prev, next, end);
		header.getChildren().addAll(rightPane);


		// ---CENTER BODY AREA
		VBox body = new VBox();
		body.setStyle("-fx-border-color: gray");
		body.setPrefSize(vbox.getPrefWidth(), vbox.getPrefHeight() * 0.92);

		ScrollPane sp = new ScrollPane();
		sp.setPrefWidth(body.getPrefWidth());
		VBox scrollPaneHolder = new VBox();
		scrollPaneHolder.setPrefWidth(sp.getPrefWidth());
		scrollPaneHolder.setSpacing(10);
		scrollPaneHolder.setPadding(new Insets(10, 30, 10, 30));
		sp.setContent(scrollPaneHolder);
		scrollPaneHolder.setAlignment(Pos.CENTER);
		sp.setHbarPolicy(ScrollBarPolicy.NEVER);
		body.getChildren().add(sp);

		createTimeLblLists(gridStartTimeStamp);
		columnPrefWidth = ((body.getPrefWidth() * 0.8) / (headerTimeLblsList.size()));

		scrollPaneHolder.getChildren().add(lodingImg);

		//---This ervice fetches list of configured tests, medicines and fluids for specific case ID
		getParamsService(PatientSession.getInstance().getPatientCase().getCaseId(), scrollPaneHolder);


		addButtonActions();

		vbox.getChildren().addAll(header, body);

		fetchHistoryData(PatientSession.getInstance().getPatientCase().getCaseId(), gridFirstTimeStamp.getTime(),
		        gridLastTimeStamp.getTime());
	}

	/**
	 * This method sets Initial and last timestamps which is used in the
	 * timeline for grids
	 */
	private void setTimestamps()
	{
		gridStartTimeStamp = Calendar.getInstance();
		if (EventLogSession.getInstance().getEventTime() != null)
		{
			gridStartTimeStamp.setTime(EventLogSession.getInstance().getEventTime());
		}
		gridStartTimeStamp.set(Calendar.SECOND, 0);
		gridStartTimeStamp.set(Calendar.MILLISECOND, 0);
		String time = sdf.format(gridStartTimeStamp.getTime());
		int mins = Integer.parseInt(time.split(":")[1]);
		gridStartTimeStamp.set(Calendar.MINUTE, (mins - (mins % 5)));

		/*gridStartTimeStamp.set(Calendar.HOUR_OF_DAY, 18);
		gridStartTimeStamp.set(Calendar.MINUTE, 45);
		gridStartTimeStamp.set(Calendar.SECOND, 0);
		gridStartTimeStamp.set(Calendar.MILLISECOND, 0);
		 */

		gridEndTimeStamp = Calendar.getInstance();
		gridEndTimeStamp.set(Calendar.SECOND, 0);
		gridEndTimeStamp.set(Calendar.MILLISECOND, 0);
		/*gridEndTimeStamp.set(Calendar.HOUR_OF_DAY, 18);
		gridEndTimeStamp.set(Calendar.MINUTE, 0);*/


		gridFirstTimeStamp = Calendar.getInstance();
		gridFirstTimeStamp.set(Calendar.SECOND, 0);
		gridFirstTimeStamp.set(Calendar.MILLISECOND, 0);

		gridLastTimeStamp = Calendar.getInstance();
		gridLastTimeStamp.set(Calendar.SECOND, 0);
		gridLastTimeStamp.set(Calendar.MILLISECOND, 0);
	}

	/**
	 * This method creates a list of time labels to be added in timeline for
	 * every grid using startTime
	 *
	 * @param startTime : starting time for the timeline
	 */
	private void createTimeLblLists(Calendar startTime)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(startTime.getTime());
		for (int i = 0; i < NO_OF_GRID_COLUMNS; i++)
		{

			Calendar timeLbl = Calendar.getInstance();
			timeLbl.setTime(cal.getTime());
			/*String gridLastTimeLbl = sdf.format(cal.getTime());
			headerTimeLblsList.add(gridLastTimeLbl);*/
			headerTimeLblsList.add(timeLbl);

			if (i == 0)
			{
				gridFirstTimeStamp.setTime(cal.getTime());
			}
			else if (i == NO_OF_GRID_COLUMNS - 1)
			{
				gridLastTimeStamp.setTime(cal.getTime());
			}
			cal.add(Calendar.MINUTE, SystemConfiguration.getInstance().getMinsGap());
		}

	}

	/**
	 * This method calculates total number of pages for pagination
	 *
	 * @return total number of pages
	 */
	private int calculateTotalPg()
	{
		int totalPgs = 0;

		Calendar currentTime = Calendar.getInstance();
		if (ControllerMediator.getInstance().getMainController().getShiftOutTime() != null)
			currentTime.setTime(ControllerMediator.getInstance().getMainController().getShiftOutTime().getTime());

		Calendar cal=Calendar.getInstance();
		cal.setTime(gridStartTimeStamp.getTime());

		do
		{
			totalPgs+=1;
			cal.add(Calendar.MINUTE, SystemConfiguration.getInstance().getMinsGap() * (NO_OF_GRID_COLUMNS - 1));

		}
		while (cal.getTimeInMillis() - currentTime.getTimeInMillis() <= 0);

		gridEndTimeStamp.setTime(cal.getTime());

		if (totalPgs == 0)
			totalPgs = 1;
		return totalPgs;
	}

	/**
	 * This method add actions to paginator buttons
	 */
	private void addButtonActions()
	{
		if (gridEndTimeStamp.getTimeInMillis() - gridLastTimeStamp.getTimeInMillis() <= 0)
		{
			start.setDisable(true);
			prev.setDisable(true);
			end.setDisable(true);
			next.setDisable(true);
		}

		btnStartHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
			public void handle(javafx.scene.input.MouseEvent event) {
				try
				{
					start.setDisable(true);
					prev.setDisable(true);
					end.setDisable(false);
					next.setDisable(false);

					currentPgValue = 1;
					currentPageLbl.setText(String.valueOf(currentPgValue));

					headerTimeLblsList.clear();
					createTimeLblLists(gridStartTimeStamp);

					fetchHistoryData(PatientSession.getInstance().getPatientCase().getCaseId(), gridFirstTimeStamp.getTime(),
					        gridLastTimeStamp.getTime());
				}
				catch (Exception e)
				{
					LOG.error("## Exception occured:", e);
					Main.getInstance().getUtility().showNotification("Error", "Error", "Something went wrong");
				}
			}
		};
		start.addEventHandler(MouseEvent.MOUSE_CLICKED, btnStartHandler);

		btnEndHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
			public void handle(javafx.scene.input.MouseEvent event) {

				try
				{
					end.setDisable(true);
					next.setDisable(true);
					start.setDisable(false);
					prev.setDisable(false);

					currentPgValue = totalPgValue;
					currentPageLbl.setText(String.valueOf(currentPgValue));

					headerTimeLblsList.clear();
					Calendar cal=Calendar.getInstance();
					cal.setTime(gridEndTimeStamp.getTime());
					cal.add(Calendar.MINUTE, -(SystemConfiguration.getInstance().getMinsGap() * (NO_OF_GRID_COLUMNS - 1)));
					createTimeLblLists(cal);

					fetchHistoryData(PatientSession.getInstance().getPatientCase().getCaseId(), gridFirstTimeStamp.getTime(),
					        gridLastTimeStamp.getTime());
				}
				catch (Exception e)
				{
					LOG.error("## Exception occured:", e);
					Main.getInstance().getUtility().showNotification("Error", "Error", "Something went wrong");
				}
			}
		};
		end.addEventHandler(MouseEvent.MOUSE_CLICKED, btnEndHandler);

		btnNextHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
			public void handle(javafx.scene.input.MouseEvent event) {
				try
				{
					start.setDisable(false);
					prev.setDisable(false);

					currentPgValue += 1;
					currentPageLbl.setText(String.valueOf(currentPgValue));

					headerTimeLblsList.clear();
					createTimeLblLists(gridLastTimeStamp);
					if (gridLastTimeStamp.getTimeInMillis() - gridEndTimeStamp.getTimeInMillis() >= 0)
					{
						end.setDisable(true);
						next.setDisable(true);
					}

					fetchHistoryData(PatientSession.getInstance().getPatientCase().getCaseId(), gridFirstTimeStamp.getTime(),
					        gridLastTimeStamp.getTime());
				}
				catch (Exception e)
				{
					LOG.error("## Exception occured:", e);
					Main.getInstance().getUtility().showNotification("Error", "Error", "Something went wrong");
				}
			}
		};
		next.addEventHandler(MouseEvent.MOUSE_CLICKED, btnNextHandler);

		btnPrevHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
			public void handle(javafx.scene.input.MouseEvent event) {
				try
				{
					end.setDisable(false);
					next.setDisable(false);

					currentPgValue -= 1;
					currentPageLbl.setText(String.valueOf(currentPgValue));

					headerTimeLblsList.clear();
					Calendar cal=Calendar.getInstance();
					cal.setTime(gridFirstTimeStamp.getTime());
					cal.add(Calendar.MINUTE, -(SystemConfiguration.getInstance().getMinsGap() * (NO_OF_GRID_COLUMNS - 1)));
					if (cal.getTimeInMillis() - gridStartTimeStamp.getTimeInMillis() <= 0)
					{
						cal.setTime(gridStartTimeStamp.getTime());
						start.setDisable(true);
						prev.setDisable(true);
					}
					createTimeLblLists(cal);

					fetchHistoryData(PatientSession.getInstance().getPatientCase().getCaseId(), gridFirstTimeStamp.getTime(),
					        gridLastTimeStamp.getTime());
				}
				catch (Exception e)
				{
					LOG.error("## Exception occured:", e);
					Main.getInstance().getUtility().showNotification("Error", "Error", "Something went wrong");
				}
			}
		};
		prev.addEventHandler(MouseEvent.MOUSE_CLICKED, btnPrevHandler);
	}

	/**
	 * This method erases and refill new data in all the grids
	 */
	private void recreateAllGrids()
	{
		for(Map.Entry<String, GridPane> entry :textboxGridMap.entrySet())
		{
			entry.getValue().getChildren().clear();
			entry.getValue().getRowConstraints().clear();
			textboxListMap.get(entry.getKey()).clear();
			


			addGridHeader(headerTimeLblsList, entry.getValue(), columnPrefWidth, rowPrefHeight);
			addGridRows(paramListMap.get(entry.getKey()), headerTimeLblsList,
					entry.getValue(), textboxListMap.get(entry.getKey()), columnPrefWidth,rowPrefHeight);

			/*
			 * for (Label txtbox : textboxListMap.get(entry.getKey())) { if
			 * (entry.getKey().equalsIgnoreCase(PTMNTR_GRID_NAME))
			 * addPtMntrContextMenu(txtbox); else if
			 * (entry.getKey().equalsIgnoreCase(ANES_GRID_NAME))
			 * addAnesContextMenu(txtbox); else if
			 * (entry.getKey().equalsIgnoreCase(MED_GRID_NAME))
			 * addMedContextMenu(txtbox); else if
			 * (entry.getKey().equalsIgnoreCase(FLUID_GRID_NAME))
			 * addFluidContextMenu(txtbox); else if
			 * (entry.getKey().equalsIgnoreCase(OUTPUT_GRID_NAME))
			 * addOutputContextMenu(txtbox); else addTestContextMenu(txtbox,
			 * entry.getKey()); }
			 */

			if(entry.getKey().equalsIgnoreCase(PTMNTR_GRID_NAME))
				fillGrid(entry.getKey(), patientMonitorDataList, textboxListMap.get(entry.getKey()), paramListMap.get(entry.getKey()));
			else if(entry.getKey().equalsIgnoreCase(ANES_GRID_NAME))
				fillGrid(entry.getKey(), anethesiaMachineDataList, textboxListMap.get(entry.getKey()), paramListMap.get(entry.getKey()));
			else if(entry.getKey().equalsIgnoreCase(OUTPUT_GRID_NAME))
				fillGrid(entry.getKey(), allOutputsList, textboxListMap.get(entry.getKey()), paramListMap.get(entry.getKey()));
			else if(entry.getKey().equalsIgnoreCase(FLUID_GRID_NAME))
				fillGrid(entry.getKey(), allfluidList, textboxListMap.get(entry.getKey()), paramListMap.get(entry.getKey()));
			else if(entry.getKey().equalsIgnoreCase(MED_GRID_NAME))
				fillGrid(entry.getKey(), allMedicationsList, textboxListMap.get(entry.getKey()), paramListMap.get(entry.getKey()));

		}

		fillTestsGrid(allTestValueMap);
	}

	/**
	 * This is a generic method which creates partitions for parameters, totals and grid sections
	 *
	 * @param gridName Name of the grid
	 * @param totalsGridReqd If totals section is required, it is true.
	 * @param baseVbox Vbox where partitions need to be created
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
		textboxGrid.getStyleClass().add("GridPane");
		textboxGrid.setPrefWidth(textboxScroller.getPrefWidth() * 0.97);
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

		TitledPane titledPane = new TitledPane();
		titledPane.setText(gridName.substring(0, 1).toUpperCase() + gridName.substring(1, gridName.length()));
		titledPane.setContent(vboxGrid);
		titledPane.setExpanded(false);
		// titledPane.getStyleClass().add("nestedTitledPane");

		baseVbox.getChildren().add(titledPane);

		paramGridMap.put(gridName, paramGrid);
		textboxGridMap.put(gridName, textboxGrid);
		paramListMap.put(gridName, new ArrayList<String>());
		textboxListMap.put(gridName, new ArrayList<Label>());
	}

	/**
	 * This method compares current time with the timestamp corresponding to the clicked grid cell
	 *
	 * @return if current time is greater, returns true else false
	 */
	private boolean compareClickedcellTime()
	{
		boolean result = false;
		/*Calendar selectedTime = Calendar.getInstance();
		selectedTime.set(Calendar.HOUR_OF_DAY, Integer.valueOf(clickedGridCellTime.split(":")[0]));
		selectedTime.set(Calendar.MINUTE, Integer.valueOf(clickedGridCellTime.split(":")[1]));
		selectedTime.set(Calendar.SECOND, 0);
		selectedTime.set(Calendar.MILLISECOND, 0);*/

		Calendar currentTime = Calendar.getInstance();
		currentTime.set(Calendar.SECOND, 0);
		currentTime.set(Calendar.MILLISECOND, 0);

		if (clickedGridCellTime.getTimeInMillis() - currentTime.getTimeInMillis() <= 0)
			result = true;

		return result;
	}

	/**
	 * This method creates grid view for PatientMonitor Section
	 *
	 * @param vbox vbox where grid is created
	 */
	private void createPtMntrGrid(VBox vbox)
	{
		createDynamicGrid(PTMNTR_GRID_NAME, false, vbox);
		paramListMap.put(PTMNTR_GRID_NAME, new ArrayList<String>(Arrays.asList(ptMntrTableRowHeader)));
		drawTabbedCenter.addLegendsToGrid(paramGridMap.get(PTMNTR_GRID_NAME), rowPrefHeight, ptMntrTableRowHeader);
		addGridHeader(headerTimeLblsList, textboxGridMap.get(PTMNTR_GRID_NAME), columnPrefWidth, rowPrefHeight);
		addGridRows(paramListMap.get(PTMNTR_GRID_NAME), headerTimeLblsList,
		        textboxGridMap.get(PTMNTR_GRID_NAME), textboxListMap.get(PTMNTR_GRID_NAME),columnPrefWidth,rowPrefHeight);
		addPtMntrContextMenu();

		/*
		 * for (Label txtbox : textboxListMap.get(PTMNTR_GRID_NAME)) {
		 * addPtMntrContextMenu(txtbox); }
		 */

		ptmntrGridClickHandler = new EventHandler<javafx.scene.input.MouseEvent>()
		{
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

								ptmntrContextMenu.show(lbl, event.getSceneX(), event.getSceneY() + 50);
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
		textboxGridMap.get(PTMNTR_GRID_NAME).addEventHandler(MouseEvent.MOUSE_CLICKED, ptmntrGridClickHandler);
	}

	/**
	 * This method creates grid view for Anesthesia Section
	 *
	 * @param vbox vbox where grid is created
	 */
	private void createAnesGrid(VBox vbox)
	{
		createDynamicGrid(ANES_GRID_NAME, false, vbox);
		paramListMap.put(ANES_GRID_NAME, new ArrayList<String>(Arrays.asList(anesTableRowHeader)));
		drawTabbedCenter.addLegendsToGrid(paramGridMap.get(ANES_GRID_NAME), rowPrefHeight, anesTableRowHeader);
		addGridHeader(headerTimeLblsList, textboxGridMap.get(ANES_GRID_NAME), columnPrefWidth, rowPrefHeight);
		addGridRows(paramListMap.get(ANES_GRID_NAME), headerTimeLblsList,
		        textboxGridMap.get(ANES_GRID_NAME), textboxListMap.get(ANES_GRID_NAME), columnPrefWidth, rowPrefHeight);

		addAnesContextMenu();
		/*for (Label txtbox : textboxListMap.get(ANES_GRID_NAME))
		{
			addAnesContextMenu(txtbox);
		}*/

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

								anesContextMenu.show(lbl, event.getSceneX(), event.getSceneY() + 50);
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
		textboxGridMap.get(ANES_GRID_NAME).addEventHandler(MouseEvent.MOUSE_CLICKED, anesGridClickHandler);
	}

	/**
	 * This method creates grid view for Medication Section
	 *
	 * @param vbox vbox where grid is created
	 */
	private void createMedGrid(VBox vbox)
	{
		List<String> favMedList = new ArrayList<>();
		favMedList = medNamesList;

		/*for (Fdamedications obj : MasterDataSession.getInstance().getFavMedDataMap().get("FavMedication"))
		{
			if (obj != null)
				favMedList.add(obj.getMedicationsName());
		}*/


		if (favMedList != null)
		if(!favMedList.isEmpty())
		{
			createDynamicGrid(MED_GRID_NAME, true, vbox);
			paramListMap.put(MED_GRID_NAME, favMedList);
			drawTabbedCenter.addLegendsToGrid(paramGridMap.get(MED_GRID_NAME), rowPrefHeight, favMedList.toArray(new String[favMedList.size()]));

			totalLblsMap.put(MED_GRID_NAME,new HashMap<String, Label>());
			drawTabbedCenter.createTotalsGrid(totalsGridMap.get(MED_GRID_NAME), rowPrefHeight, paramListMap.get(MED_GRID_NAME), totalLblsMap.get(MED_GRID_NAME));
				addGridHeader(headerTimeLblsList, textboxGridMap.get(MED_GRID_NAME), columnPrefWidth, rowPrefHeight);
				addGridRows(paramListMap.get(MED_GRID_NAME), headerTimeLblsList, textboxGridMap.get(MED_GRID_NAME),
			        textboxListMap.get(MED_GRID_NAME), columnPrefWidth, rowPrefHeight);

			if (PatientSession.getInstance().getPatient() != null&& PatientSession.getInstance().getPatientCase() != null)
			{
				if(PatientSession.getInstance().getPatient().getPatientId() != null && PatientSession.getInstance().getPatientCase().getCaseId() != null)
				{
					getTotalMedicationVolumeList(paramListMap.get(MED_GRID_NAME));
				}
			}

			addMedContextMenu();

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

									medContextMenu.show(lbl, event.getSceneX(), event.getSceneY() + 50);

									if (compareClickedcellTime())
									{
										medContextMenu.getItems().get(0).setDisable(false);
										medContextMenu.getItems().get(1).setDisable(false);

										if (drawTabbedCenter.getInfusionStartedMap().containsKey(clickedGridCellParam))
										{
											medContextMenu.getItems().get(0).setDisable(false);
											medContextMenu.getItems().get(1).setDisable(true);
											medContextMenu.getItems().get(2).setDisable(false);
											medContextMenu.getItems().get(3).setDisable(false);

											/*Calendar selectedTime=Calendar.getInstance();
											selectedTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(clickedGridCellTime.split(":")[0]));
											selectedTime.set(Calendar.MINUTE, Integer.parseInt(clickedGridCellTime.split(":")[1]));
											selectedTime.set(Calendar.SECOND,0);
											selectedTime.set(Calendar.MILLISECOND,0);*/

											/*String infStartedTime=drawTabbedCenter.getInfusionStartedMap().get(clickedGridCellParam);
											Calendar infTime=Calendar.getInstance();
											infTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(infStartedTime.split(":")[0]));
											infTime.set(Calendar.MINUTE, Integer.parseInt(infStartedTime.split(":")[1]));
											infTime.set(Calendar.SECOND,0);
											infTime.set(Calendar.MILLISECOND,0);*/

											Calendar infTime=Calendar.getInstance();
											infTime.setTime(drawTabbedCenter.getInfusionStartedMap().get(clickedGridCellParam).getTime());
											while(infTime.get(Calendar.MINUTE)%mingap!=0)
											{
												infTime.add(Calendar.MINUTE, -1);
											}
											if(clickedGridCellTime.getTimeInMillis()-infTime.getTimeInMillis()>=0)
											{	medContextMenu.getItems().get(2).setDisable(false);
												medContextMenu.getItems().get(3).setDisable(false);
											}
											else
											{	medContextMenu.getItems().get(2).setDisable(true);
											medContextMenu.getItems().get(3).setDisable(true);
											}
											
										}
										else
										{
										Date startDate=	medicationLogDao.getStartTime(clickedGridCellParam,
											        PatientSession.getInstance().getPatientCase().getCaseId(),
											        PatientSession.getInstance().getPatient().getPatientId(),  "Infusion",null);
											if(startDate ==null) {
												medContextMenu.getItems().get(1).setDisable(false);
												
											}
											else {
												Calendar infTime=Calendar.getInstance();
												infTime.setTime(startDate);
												while(infTime.get(Calendar.MINUTE)%mingap!=0)
												{
													infTime.add(Calendar.MINUTE, -1);
												}
												if(clickedGridCellTime.getTimeInMillis()-infTime.getTimeInMillis()<0)
												{	medContextMenu.getItems().get(1).setDisable(true);
													
												}
											}
											
											medContextMenu.getItems().get(0).setDisable(false);
											
											
											medContextMenu.getItems().get(2).setDisable(true);
											medContextMenu.getItems().get(3).setDisable(true);

										}

										if(ControllerMediator.getInstance().getMainController()
										        .getDrawTabbedCenter().getMedsFromInfPump().contains(clickedGridCellParam)) {
											medContextMenu.getItems().get(0).setDisable(false);
											//medContextMenu.getItems().get(1).setDisable(true);
											medContextMenu.getItems().get(1).setDisable(true);
											medContextMenu.getItems().get(2).setDisable(true);
											medContextMenu.getItems().get(3).setDisable(true);
										}
//
//										if (!lbl.getText().isEmpty())
//										{
//											if (lbl.getText().startsWith("(") && lbl.getText().endsWith(")"))
//											{
//												medContextMenu.getItems().get(0).setDisable(false);
//												medContextMenu.getItems().get(1).setDisable(true);
//												medContextMenu.getItems().get(2).setDisable(true);
//											}
//											else if (! lbl.getText().contains("g"))
//											{
//												medContextMenu.getItems().get(0).setDisable(false);
//
//												if (!drawTabbedCenter.getInfusionStartedMap().containsKey(clickedGridCellParam))
//													medContextMenu.getItems().get(1).setDisable(false);
//
//												medContextMenu.getItems().get(2).setDisable(true);
//												medContextMenu.getItems().get(3).setDisable(true);
//											}
//											else if (lbl.getText().contains("g"))
//											{
//												medContextMenu.getItems().get(0).setDisable(false);
//												medContextMenu.getItems().get(1).setDisable(true);
//												medContextMenu.getItems().get(2).setDisable(true);
//													// medContextMenu.getItems().get(3).setDisable(false);
//											}
//											/*else
//											{
//												medContextMenu.getItems().get(0).setDisable(true);
//												medContextMenu.getItems().get(1).setDisable(true);
//												medContextMenu.getItems().get(2).setDisable(true);
//											}*/
//										}
//										else
//										{


											//medContextMenu.getItems().get(0).setDisable(false);
											//medContextMenu.getItems().get(1).setDisable(false);
//										}
									}
									else
									{
										medContextMenu.getItems().get(0).setDisable(true);
										medContextMenu.getItems().get(1).setDisable(true);
										medContextMenu.getItems().get(2).setDisable(true);
										medContextMenu.getItems().get(3).setDisable(true);
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
			textboxGridMap.get(MED_GRID_NAME).addEventHandler(MouseEvent.MOUSE_CLICKED, medGridClickHandler);

		}
		else
		{
			HBox hboxGrid = new HBox();
			hboxGrid.setPrefWidth(baseVbox.getPrefWidth());
			hboxGrid.setAlignment(Pos.CENTER_LEFT);
			Label lbl = new Label("No data available!");
			hboxGrid.getChildren().add(lbl);

			TitledPane titledPane = new TitledPane();
			titledPane.setText(MED_GRID_NAME);
			titledPane.setContent(hboxGrid);
				titledPane.setExpanded(false);

			vbox.getChildren().add(titledPane);
		}

	}

	/**
	 * This method creates grid view for Fluid Section
	 *
	 * @param vbox vbox where grid is created
	 */
	private void createFluidsGrid(VBox vbox)
	{
		List<String> favFluidsList  = new ArrayList<>();
		favFluidsList = fluidNamesList;
		/*for(Fluid obj:MasterDataSession.getInstance().getFavFluidDataMap().get("FavFluids"))
		{
			if(obj!=null)
				favFluidsList.add(obj.getFluidName());
		}*/



		if (favFluidsList != null)
		if(!favFluidsList.isEmpty())
		{
			createDynamicGrid(FLUID_GRID_NAME, true, vbox);
			paramListMap.put(FLUID_GRID_NAME, favFluidsList);
			drawTabbedCenter.addLegendsToGrid(paramGridMap.get(FLUID_GRID_NAME), rowPrefHeight, favFluidsList.toArray(new String[favFluidsList.size()]));

			totalLblsMap.put(FLUID_GRID_NAME,new HashMap<String, Label>());
			drawTabbedCenter.createTotalsGrid(totalsGridMap.get(FLUID_GRID_NAME), rowPrefHeight, paramListMap.get(FLUID_GRID_NAME), totalLblsMap.get(FLUID_GRID_NAME));
				addGridHeader(headerTimeLblsList, textboxGridMap.get(FLUID_GRID_NAME), columnPrefWidth, rowPrefHeight);
				addGridRows(paramListMap.get(FLUID_GRID_NAME), headerTimeLblsList, textboxGridMap.get(FLUID_GRID_NAME),
			        textboxListMap.get(FLUID_GRID_NAME), columnPrefWidth, rowPrefHeight);

			if (PatientSession.getInstance().getPatient() != null&& PatientSession.getInstance().getPatientCase() != null)
			{
				if(PatientSession.getInstance().getPatient().getPatientId() != null && PatientSession.getInstance().getPatientCase().getCaseId() != null)
				{
					getFluidTotalsListService(paramListMap.get(FLUID_GRID_NAME));
				}

			}
			addFluidContextMenu();

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
										if (!lbl.getText().isEmpty())
										{
											fluidContextMenu.getItems().get(0).setDisable(true);
											fluidContextMenu.getItems().get(1).setDisable(true);
										}
										else
										{
											if (drawTabbedCenter.getFluidstartedMap().containsKey(clickedGridCellParam))
											{
												fluidContextMenu.getItems().get(0).setDisable(true);
												//fluidContextMenu.getItems().get(1).setDisable(false);

												/*Calendar selectedTime=Calendar.getInstance();
												selectedTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(clickedGridCellTime.split(":")[0]));
												selectedTime.set(Calendar.MINUTE, Integer.parseInt(clickedGridCellTime.split(":")[1]));
												selectedTime.set(Calendar.SECOND,0);
												selectedTime.set(Calendar.MILLISECOND,0);*/

												/*String fldStartedTime=drawTabbedCenter.getFluidstartedMap().get(clickedGridCellParam);
												Calendar fldTime=Calendar.getInstance();
												fldTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(fldStartedTime.split(":")[0]));
												fldTime.set(Calendar.MINUTE, Integer.parseInt(fldStartedTime.split(":")[1]));
												fldTime.set(Calendar.SECOND,0);
												fldTime.set(Calendar.MILLISECOND,0);*/

												Calendar fldTime=Calendar.getInstance();
												fldTime.setTime(drawTabbedCenter.getFluidstartedMap().get(clickedGridCellParam).getTime());

												if(clickedGridCellTime.getTimeInMillis()-fldTime.getTimeInMillis()>0)
													fluidContextMenu.getItems().get(1).setDisable(false);
												else
													fluidContextMenu.getItems().get(1).setDisable(true);
											}
											else
											{
												fluidContextMenu.getItems().get(0).setDisable(false);
												fluidContextMenu.getItems().get(1).setDisable(true);
											}
										}
									}
									else
									{
										fluidContextMenu.getItems().get(0).setDisable(true);
										fluidContextMenu.getItems().get(1).setDisable(true);
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
			textboxGridMap.get(FLUID_GRID_NAME).addEventHandler(MouseEvent.MOUSE_CLICKED, fluidGridClickHandler);

		}
		else
		{
				HBox hboxGrid = new HBox();
				hboxGrid.setPrefWidth(baseVbox.getPrefWidth());
				hboxGrid.setAlignment(Pos.CENTER_LEFT);
				Label lbl = new Label("No data available!");
				hboxGrid.getChildren().add(lbl);

				TitledPane titledPane = new TitledPane();
				titledPane.setText(FLUID_GRID_NAME);
				titledPane.setContent(hboxGrid);
				titledPane.setExpanded(false);

				vbox.getChildren().add(titledPane);
		}
	}

	/**
	 * This method creates grid view for Output Section
	 *
	 * @param vbox vbox where grid is created
	 */
	private void createOutputGrid(VBox vbox)
	{
		createDynamicGrid(OUTPUT_GRID_NAME, true, vbox);
		paramListMap.put(OUTPUT_GRID_NAME, new ArrayList<String>(Arrays.asList(outputsTableRowHeader)));
		drawTabbedCenter.addLegendsToGrid(paramGridMap.get(OUTPUT_GRID_NAME), rowPrefHeight, outputsTableRowHeader);

		totalLblsMap.put(OUTPUT_GRID_NAME, new HashMap<String, Label>());
		drawTabbedCenter.createTotalsGrid(totalsGridMap.get(OUTPUT_GRID_NAME), rowPrefHeight,paramListMap.get(OUTPUT_GRID_NAME), totalLblsMap.get(OUTPUT_GRID_NAME));
		addGridHeader(headerTimeLblsList, textboxGridMap.get(OUTPUT_GRID_NAME), columnPrefWidth, rowPrefHeight);
		addGridRows(paramListMap.get(OUTPUT_GRID_NAME), headerTimeLblsList,
		        textboxGridMap.get(OUTPUT_GRID_NAME), textboxListMap.get(OUTPUT_GRID_NAME), columnPrefWidth, rowPrefHeight);

		if (PatientSession.getInstance().getPatient() != null && PatientSession.getInstance().getPatientCase() != null)
		{
			if (PatientSession.getInstance().getPatient().getPatientId() != null
			        && PatientSession.getInstance().getPatientCase().getCaseId() != null)
			{
				getOutputTotalsListService(paramListMap.get(OUTPUT_GRID_NAME));
			}
		}
		addOutputContextMenu();

		outputGridClickHandler = new EventHandler<javafx.scene.input.MouseEvent>()
		{
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
								clickedGridCellParam = paramListMap.get(OUTPUT_GRID_NAME)
								        .get(Integer.valueOf(lbl.getId().split(",")[0]));

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
		textboxGridMap.get(OUTPUT_GRID_NAME).addEventHandler(MouseEvent.MOUSE_CLICKED, outputGridClickHandler);
	}


	/**
	 * This method creates grid view for Test Section
	 *
	 * @param vbox vbox where grid is created
	 */
	private void createTestGrid(VBox vbox)
	{
		TitledPane testsTitledPane = new TitledPane();
		testsTitledPane.setText("Tests");

		testsHolder = new VBox();
		testsHolder.setPrefWidth(vbox.getPrefWidth());
		testsHolder.setSpacing(10);
		testsHolder.setPadding(new Insets(10, 30, 10, 30));
		testsTitledPane.setContent(testsHolder);
		testsTitledPane.setExpanded(false);
		vbox.getChildren().addAll(testsTitledPane);


		if (listOfTestParameters.isEmpty())
		{
			HBox hboxGrid = new HBox();
			hboxGrid.setPrefWidth(baseVbox.getPrefWidth());
			hboxGrid.setAlignment(Pos.CENTER_LEFT);
			Label lbl = new Label("No data available!");
			hboxGrid.getChildren().add(lbl);
			testsHolder.getChildren().add(hboxGrid);
		}
		else
		{
			addTestContextMenu();

			for(Map.Entry<String,List<Investigationparameterfields>> entry:listOfTestParameters.entrySet())
			{

				String testName=entry.getKey();
				String paramName="";

				createDynamicGrid(testName, false, testsHolder);

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
						else if (obj.getName() != null)
						{
							paramName = obj.getName();
						}
					}
					paramListMap.get(testName).add(paramName);
				}


				drawTabbedCenter.addLegendsToGrid(paramGridMap.get(testName), rowPrefHeight, paramListMap.get(testName).toArray(new String[paramListMap.get(testName).size()]));
				addGridHeader(headerTimeLblsList, textboxGridMap.get(testName), columnPrefWidth, rowPrefHeight);
				addGridRows(paramListMap.get(testName), headerTimeLblsList,
					        textboxGridMap.get(testName),
				        textboxListMap.get(testName), columnPrefWidth, rowPrefHeight);


				textboxGridMap.get(testName).setPrefHeight((paramListMap.get(testName).size() + 2) * rowPrefHeight);
				paramGridMap.get(testName).setPrefHeight(textboxGridMap.get(testName).getPrefHeight());

				EventHandler<MouseEvent> testGridClickHandler= new EventHandler<javafx.scene.input.MouseEvent>() {
					public void handle(javafx.scene.input.MouseEvent event)
					{
						try
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
						catch (Exception e)
						{
							LOG.error("## Exception occured:", e);
						}
					}
				};
				textboxGridMap.get(testName).addEventHandler(MouseEvent.MOUSE_CLICKED, testGridClickHandler);

				testsEventHandlersMap.put(textboxGridMap.get(testName), testGridClickHandler);

			}
		}

	}

	/**
	 * This method adds contextmenu over PatientMonitor grid cells
	 */
	private void addPtMntrContextMenu()
	{
		ptmntrContextMenu = new ContextMenu();
		ptmntrContextMenu.setPrefSize(100, 100);

		item1 = new MenuItem("Update Values");
		item1.setStyle("-fx-font-size:12;");

		ptmntrContextMenu.getItems().addAll(item1);


		item1Handler = new EventHandler<ActionEvent>()
		{
			public void handle(ActionEvent event)
			{

				try
				{
					Main.getInstance().getStageManager().switchSceneAfterSecondary(FxmlView.PATIENT_MONITOR);
					ControllerMediator.getInstance().getPatientMonitorController().setTertiaryWindow(true);
					ControllerMediator.getInstance().getPatientMonitorController().setFromHistoryScreen(true);
					ControllerMediator.getInstance().getPatientMonitorController().setSelectedTime(clickedGridCellTime);
					//bindPopupFxml("/View/PatientMonitor.fxml");

					/*Calendar cal = Calendar.getInstance();
					cal.set(Calendar.HOUR_OF_DAY, Integer.valueOf(clickedGridCellTime.split(":")[0]));
					cal.set(Calendar.MINUTE, Integer.valueOf(clickedGridCellTime.split(":")[1]));
					cal.set(Calendar.SECOND, 0);
					cal.set(Calendar.MILLISECOND, 0);*/

					GetPatientMonitorDataTimeCaseService ptmntrSearchService = GetPatientMonitorDataTimeCaseService
					        .getInstance(PatientSession.getInstance().getPatientCase().getCaseId(), clickedGridCellTime.getTime());
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
								if (obj != null)
								{
									ControllerMediator.getInstance().getPatientMonitorController().autopopulateTextboxes(obj);
									ControllerMediator.getInstance().getPatientMonitorController().getLblTime()
									        .setText(sdf.format(clickedGridCellTime.getTime()));
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
		item1.addEventHandler(ActionEvent.ACTION, item1Handler);

	}

	/**
	 * This method adds contextmenu over Anesthesia grid cells
	 */
	private void addAnesContextMenu()
	{
		anesContextMenu = new ContextMenu();
		anesContextMenu.setPrefSize(100, 100);

		item12 = new MenuItem("Update Values");
		item12.setStyle("-fx-font-size:12;");

		anesContextMenu.getItems().addAll(item12);

		item12Handler = new EventHandler<ActionEvent>()
		{
			public void handle(ActionEvent event)
			{

				try
				{
					Main.getInstance().getStageManager().switchSceneAfterSecondary(FxmlView.DASHBOARD_ANESTHESIA);
					ControllerMediator.getInstance().getDashboardAnaestheiaController().setTertiaryWindow(true);
					ControllerMediator.getInstance().getDashboardAnaestheiaController().setFromHistoryScreen(true);
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
									ControllerMediator.getInstance().getDashboardAnaestheiaController().getLblTime()
									        .setText(sdf.format(clickedGridCellTime.getTime()));
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
		item12.addEventHandler(ActionEvent.ACTION, item12Handler);
	}

	/**
	 * This method adds contextmenu over Medication grid cells
	 */
	private void addMedContextMenu()
	{
		medContextMenu = new ContextMenu();
		medContextMenu.setPrefSize(100, 100);

		item2 = new MenuItem("Update Bolus");
		item2.setStyle("-fx-font-size:12");
		startInfItem = new MenuItem("Start Infusion");
		startInfItem.setStyle("-fx-font-size:12");
		stopInfItem = new MenuItem("Stop Infusion");
		stopInfItem.setStyle("-fx-font-size:12");
		stopInfItem.setDisable(true);

		editInfItem = new MenuItem("Edit Infusion");
		editInfItem.setStyle("-fx-font-size:12");
		editInfItem.setDisable(true);

		medContextMenu.getItems().addAll(item2, startInfItem, stopInfItem, editInfItem);

		item2Handler = new EventHandler<ActionEvent>()
		{
			public void handle(ActionEvent event)
			{
				try
				{
					//bindPopupFxml("/View/MedicationInfusion.fxml");
					Main.getInstance().getStageManager().switchSceneAfterSecondary(FxmlView.MEDICATION_INFUSION);
					ControllerMediator.getInstance().getMedicationInfusionController().setTertiaryWindow(true);
					ControllerMediator.getInstance().getMedicationInfusionController().setFromHistoryScreen(true);
					ControllerMediator.getInstance().getMedicationInfusionController().setSelectedTime(clickedGridCellTime);

					ControllerMediator.getInstance().getMedicationInfusionController().getLblSelectetMethod().setText("Bolus");
					ControllerMediator.getInstance().getMedicationInfusionController().checkMedType();
					ControllerMediator.getInstance().getMedicationInfusionController().getLblTime()
					        .setText(sdf.format(clickedGridCellTime.getTime()));
					ControllerMediator.getInstance().getMedicationInfusionController().getLblMedicineName().setText(clickedGridCellParam);

					/*Calendar cal = Calendar.getInstance();
					cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(clickedGridCellTime.split(":")[0]));
					cal.set(Calendar.MINUTE, Integer.parseInt(clickedGridCellTime.split(":")[1]));
					cal.set(Calendar.SECOND, 0);*/

					GetIntraopMedicationlogService getIntraopMedicationlogService = GetIntraopMedicationlogService.getInstance(
							clickedGridCellParam,
					        PatientSession.getInstance().getPatientCase().getCaseId(),
					        PatientSession.getInstance().getPatient().getPatientId(), clickedGridCellTime.getTime(), "Bolus");

					getIntraopMedicationlogService.restart();


					getMedlogServiceSuccessHandler = new EventHandler<WorkerStateEvent>()
					{
						@Override
						public void handle(WorkerStateEvent event)
						{
							try
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
							catch (Exception e)
							{
								LOG.error("## Exception occured:", e);
							}
						}
					};
					getIntraopMedicationlogService.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
							getMedlogServiceSuccessHandler);

					getMedlogServiceFailedHandler = new EventHandler<WorkerStateEvent>()
					{
						@Override
						public void handle(WorkerStateEvent event)
						{
							try
							{
								Main.getInstance().getUtility().showNotification("Error", "Error",
								        getIntraopMedicationlogService.getException().getMessage());

								getIntraopMedicationlogService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
										getMedlogServiceSuccessHandler);
								getIntraopMedicationlogService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
										getMedlogServiceFailedHandler);
							}
							catch (Exception e)
							{
								LOG.error("## Exception occured:", e);
							}
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
		item2.addEventHandler(ActionEvent.ACTION, item2Handler);

		startInfItemHandler = new EventHandler<ActionEvent>()
		{
			public void handle(ActionEvent event)
			{
				try
				{
					if (drawTabbedCenter.getMedsFromInfPump().contains(clickedGridCellParam))
					{
						Main.getInstance().getUtility().errorDialogue("Error",
						        "Please start medication from Infusion Pump!");
					}
					else
					{
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
							
						Main.getInstance().getStageManager().switchSceneAfterSecondary(FxmlView.MEDICATION_INFUSION);
						ControllerMediator.getInstance().getMedicationInfusionController().setTertiaryWindow(true);
						ControllerMediator.getInstance().getMedicationInfusionController().setFromHistoryScreen(true);
						ControllerMediator.getInstance().getMedicationInfusionController()
						        .setSelectedTime(realTime);

						ControllerMediator.getInstance().getMedicationInfusionController().getLblSelectetMethod()
						        .setText("Infusion");
						ControllerMediator.getInstance().getMedicationInfusionController().setEditWindow(false);
						ControllerMediator.getInstance().getMedicationInfusionController().checkMedType();
						ControllerMediator.getInstance().getMedicationInfusionController().getLblTime().setText(sdf.format(realTime.getTime()));
						ControllerMediator.getInstance().getMedicationInfusionController().getLblMedicineName()
						        .setText(clickedGridCellParam);
						ControllerMediator.getInstance().getMedicationInfusionController()
						        .setMedicationLog(new IntraopMedicationlog());
						ControllerMediator.getInstance().getMedicationInfusionController().prePopulateTextfields();
					}
				}
				catch (Exception e)
				{
					LOG.error("## Exception occured:", e);
				}

			}
		};
		startInfItem.addEventHandler(ActionEvent.ACTION, startInfItemHandler);

		stopInfItemHandler = new EventHandler<ActionEvent>()
		{
			public void handle(ActionEvent event)
			{

				try
				{
					if (drawTabbedCenter.getMedsFromInfPump().contains(clickedGridCellParam))
					{
						Main.getInstance().getUtility().errorDialogue("Error",
						        "Please stop medication from Infusion Pump!");
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
						Main.getInstance().getStageManager().switchSceneAfterSecondary(FxmlView.STOP_INFUSION);
						ControllerMediator.getInstance().getStopInfusionController().setTertiaryWindow(true);
						ControllerMediator.getInstance().getStopInfusionController().setFromHistoryScreen(true);

						//String startTime = drawTabbedCenter.getInfusionStartedMap().get(clickedGridCellParam);
						//String endTime = clickedGridCellTime;

						ControllerMediator.getInstance().getStopInfusionController().getLblMedicineName()
						        .setText(clickedGridCellParam);
						ControllerMediator.getInstance().getStopInfusionController().getLblStartTime().setText(
						        sdf.format(drawTabbedCenter.getInfusionStartedMap().get(clickedGridCellParam).getTime()));
						ControllerMediator.getInstance().getStopInfusionController().getLblStopTime().setText(sdf.format(realTime.getTime()));

						Calendar startCal = Calendar.getInstance();
						startCal.setTime(drawTabbedCenter.getInfusionStartedMap().get(clickedGridCellParam).getTime());
						/*startCal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(startTime.split(":")[0]));
						startCal.set(Calendar.MINUTE, Integer.parseInt(startTime.split(":")[1]));
						startCal.set(Calendar.SECOND, 0);*/

						/*Calendar endCal = Calendar.getInstance();
						endCal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(endTime.split(":")[0]));
						endCal.set(Calendar.MINUTE, Integer.parseInt(endTime.split(":")[1]));
						endCal.set(Calendar.SECOND, 0);*/
						ControllerMediator.getInstance().getStopInfusionController().setEndTime(realTime);

						GetIntraopMedicationlogService getIntraopMedicationlogService = GetIntraopMedicationlogService
						        .getInstance(clickedGridCellParam,
						                PatientSession.getInstance().getPatientCase().getCaseId(),
						                PatientSession.getInstance().getPatient().getPatientId(), startCal.getTime(), "Infusion");
						getIntraopMedicationlogService.restart();

						getMedlogServiceSuccessHandler = new EventHandler<WorkerStateEvent>()
						{
							@Override
							public void handle(WorkerStateEvent event)
							{
								try
								{
									IntraopMedicationlog obj = getIntraopMedicationlogService.getIntraopMedicationlog();
									ControllerMediator.getInstance().getStopInfusionController().setMedicationLog(obj);
									ControllerMediator.getInstance().getStopInfusionController().setInfuseDosenRateText();

									getIntraopMedicationlogService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
									        getMedlogServiceSuccessHandler);
									getIntraopMedicationlogService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
									        getMedlogServiceFailedHandler);
								}
								catch (Exception e)
								{
									LOG.error("## Exception occured:", e);
								}
							}
						};
						getIntraopMedicationlogService.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
						        getMedlogServiceSuccessHandler);

						getMedlogServiceFailedHandler = new EventHandler<WorkerStateEvent>()
						{
							@Override
							public void handle(WorkerStateEvent event)
							{
								try
								{
									Main.getInstance().getUtility().showNotification("Error", "Error",
									        getIntraopMedicationlogService.getException().getMessage());

									getIntraopMedicationlogService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
									        getMedlogServiceSuccessHandler);
									getIntraopMedicationlogService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
									        getMedlogServiceFailedHandler);
								}
								catch (Exception e)
								{
									LOG.error("## Exception occured:", e);
								}
							}
						};
						getIntraopMedicationlogService.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
						        getMedlogServiceFailedHandler);

					}
				}
				catch (Exception e)
				{
					LOG.error("## Exception occured:", e);
				}

			}
		};
		stopInfItem.addEventHandler(ActionEvent.ACTION, stopInfItemHandler);


		editInfItemHandler = new EventHandler<ActionEvent>()
		{
			public void handle(ActionEvent event)
			{
				try
				{
					Calendar realTime =null ;
					Calendar currentime = Calendar.getInstance();
						currentime.set(Calendar.MILLISECOND, 0);
						currentime.set(Calendar.SECOND, 0);
						long diffTime = currentime.getTimeInMillis() - clickedGridCellTime.getTimeInMillis();
						long gapTime = mingap*60*1000;
						if( mingap >1 &&  diffTime<gapTime && diffTime >0 ) {
							
							realTime= currentime;
						}
						else {
							if(clickedGridCellTime.getTimeInMillis()>drawTabbedCenter.getInfusionStartedMap().get(clickedGridCellParam).getTimeInMillis())
							realTime = clickedGridCellTime;
							else {
								realTime=drawTabbedCenter.getInfusionStartedMap().get(clickedGridCellParam);
							}
							
						}
						
					Main.getInstance().getStageManager().switchSceneAfterSecondary(FxmlView.MEDICATION_INFUSION);
					ControllerMediator.getInstance().getMedicationInfusionController().setTertiaryWindow(true);
					ControllerMediator.getInstance().getMedicationInfusionController().setEditWindow(true);
					ControllerMediator.getInstance().getMedicationInfusionController().setFromHistoryScreen(true);
					ControllerMediator.getInstance().getMedicationInfusionController().setSelectedTime(realTime);
					ControllerMediator.getInstance().getMedicationInfusionController().getLblSelectetMethod().setText("Infusion");
					ControllerMediator.getInstance().getMedicationInfusionController().checkMedType();
					ControllerMediator.getInstance().getMedicationInfusionController().getLblTime().setText(sdf.format(realTime.getTime()));
					ControllerMediator.getInstance().getMedicationInfusionController().getLblMedicineName().setText(clickedGridCellParam);
					
					Calendar startCal = Calendar.getInstance();
					startCal=ControllerMediator.getInstance().getMainController().getDrawTabbedCenter()
							.getInfusionStartedMap().get(clickedGridCellParam);
					

					GetIntraopMedicationlogService getIntraopMedicationlogService = GetIntraopMedicationlogService.getInstance(
					        clickedGridCellParam,
					        PatientSession.getInstance().getPatientCase().getCaseId(),
					        PatientSession.getInstance().getPatient().getPatientId(), startCal.getTime(), "Infusion");
					getIntraopMedicationlogService.restart();
					
					getMedlogServiceSuccessHandler = new EventHandler<WorkerStateEvent>()
					{
						@Override
						public void handle(WorkerStateEvent event)
						{
							GetIntraopMedicationlogService getIntraopMedicationlogService1 =getIntraopMedicationlogService;
							IntraopMedicationlog obj = getIntraopMedicationlogService.getIntraopMedicationlog();
							ControllerMediator.getInstance().getMedicationInfusionController().setMedicationLog(obj);
							ControllerMediator.getInstance().getMedicationInfusionController().prePopulateTextfields();

							Calendar startCal1 = Calendar.getInstance();
							startCal1 =ControllerMediator.getInstance().getMainController().getDrawTabbedCenter()
									.getInfusionStartedMap().get(clickedGridCellParam);
							getIntraopMedicationlogService1 = GetIntraopMedicationlogService.getInstance(
							        clickedGridCellParam,
							        PatientSession.getInstance().getPatientCase().getCaseId(),
							        PatientSession.getInstance().getPatient().getPatientId(), startCal1.getTime(), "Infusion");
							getIntraopMedicationlogService1.restart();
							
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
					e.printStackTrace();
					LOG.error("## Exception occured:", e);
				}

			}
		};
		editInfItem.addEventHandler(ActionEvent.ACTION, editInfItemHandler);
	}

	/**
	 * This method adds contextmenu over Fluid grid cells
	 */
	private void addFluidContextMenu()
	{
		fluidContextMenu = new ContextMenu();
		fluidContextMenu.setPrefSize(100, 100);

		startFluid = new MenuItem("Start fluid");
		startFluid.setStyle("-fx-font-size:12");
		stopFluid = new MenuItem("Stop fluid");
		stopFluid.setStyle("-fx-font-size:12");


		fluidContextMenu.getItems().addAll(startFluid,stopFluid);

		startFluidHandler = new EventHandler<ActionEvent>()
		{
			public void handle(ActionEvent event)
			{
				try
				{
					Main.getInstance().getStageManager().switchSceneAfterSecondary(FxmlView.DASHBOARD_FLUID_START);
					ControllerMediator.getInstance().getDashboardFluidController().setTertiaryWindow(true);
					ControllerMediator.getInstance().getDashboardFluidController().setFromHistoryScreen(true);
					ControllerMediator.getInstance().getDashboardFluidController().setSelectedTime(clickedGridCellTime);

					ControllerMediator.getInstance().getDashboardFluidController().setIsStop(false);
					ControllerMediator.getInstance().getDashboardFluidController().checkStartOrStop();
					ControllerMediator.getInstance().getDashboardFluidController().getLblFluidName().setText(clickedGridCellParam);
					ControllerMediator.getInstance().getDashboardFluidController().getLblTime()
					        .setText(sdf.format(clickedGridCellTime.getTime()));
					ControllerMediator.getInstance().getDashboardFluidController().setSelectedFluidId(drawTabbedCenter.getFluidIdsMap().get(clickedGridCellParam));
				}
				catch (Exception e)
				{
					LOG.error("## Exception occured:", e);
				}
			}
		};
		startFluid.addEventHandler(ActionEvent.ACTION, startFluidHandler);


		stopFluidHandler = new EventHandler<ActionEvent>()
		{
			public void handle(ActionEvent event)
			{
				try
				{
					/*String timeStamp = drawTabbedCenter.getFluidstartedMap().get(clickedGridCellParam);
					Calendar cal = Calendar.getInstance();
					cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeStamp.split(":")[0]));
					cal.set(Calendar.MINUTE, Integer.parseInt(timeStamp.split(":")[1]));
					cal.set(Calendar.SECOND, 0);
					cal.set(Calendar.MILLISECOND, 0);*/

					Calendar cal = Calendar.getInstance();
					cal.setTime( drawTabbedCenter.getFluidstartedMap().get(clickedGridCellParam).getTime());

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
								Main.getInstance().getStageManager().switchSceneAfterSecondary(FxmlView.DASHBOARD_FLUID_START);
								ControllerMediator.getInstance().getDashboardFluidController().setTertiaryWindow(true);
								ControllerMediator.getInstance().getDashboardFluidController().setFromHistoryScreen(true);
								ControllerMediator.getInstance().getDashboardFluidController().setSelectedTime(clickedGridCellTime);
								ControllerMediator.getInstance().getDashboardFluidController().setSelectedFluid(fluidSearchService.getSearchFluid());

								ControllerMediator.getInstance().getDashboardFluidController().setIsStop(true);
								ControllerMediator.getInstance().getDashboardFluidController().checkStartOrStop();
								ControllerMediator.getInstance().getDashboardFluidController().getLblFluidName().setText(clickedGridCellParam);
								ControllerMediator.getInstance().getDashboardFluidController().getLblTime()
								        .setText(sdf.format(clickedGridCellTime.getTime()));
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
		stopFluid.addEventHandler(ActionEvent.ACTION, stopFluidHandler);

	}

	/**
	 * This method adds contextmenu over Output grid cells
	 */
	private void addOutputContextMenu()
	{
		outputContextMenu = new ContextMenu();
		outputContextMenu.setPrefSize(100, 100);

		enter = new MenuItem("Update value");
		enter.setStyle("-fx-font-size:12;");

		outputContextMenu.getItems().addAll(enter);

		enterHandler = new EventHandler<ActionEvent>()
		{
			public void handle(ActionEvent event)
			{
				try
				{
					//bindPopupFxml("/View/DashboardOutput.fxml");
					Main.getInstance().getStageManager().switchSceneAfterSecondary(FxmlView.DASHBOARD_OUTPUT);
					ControllerMediator.getInstance().getDashboardOutputController().setTertiaryWindow(true);
					ControllerMediator.getInstance().getDashboardOutputController().setFromHistoryScreen(true);
					ControllerMediator.getInstance().getDashboardOutputController().setSelectedTime(clickedGridCellTime);

					ControllerMediator.getInstance().getDashboardOutputController().getLblTime()
					        .setText(sdf.format(clickedGridCellTime.getTime()));
					ControllerMediator.getInstance().getDashboardOutputController().getLblOutputName().setText(clickedGridCellParam);

					/*Calendar cal = Calendar.getInstance();
					cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(clickedGridCellTime.split(":")[0]));
					cal.set(Calendar.MINUTE, Integer.parseInt(clickedGridCellTime.split(":")[1]));
					cal.set(Calendar.SECOND, 0);
					cal.set(Calendar.MILLISECOND, 0);*/

					SearchIntraOpOutputService searchOutputService = SearchIntraOpOutputService.getInstance(
					        PatientSession.getInstance().getPatientCase().getCaseId(), clickedGridCellTime.getTime(),
					        clickedGridCellParam);
					searchOutputService.restart();

					/*searchOutputService.setOnSucceeded(ex ->
					{
						IntraopOutput obj = searchOutputService.getIntraopOutputRecord();
						if (obj == null)
							obj = new IntraopOutput();
						else
							ControllerMediator.getInstance().getDashboardOutputController().getTxtVolume()
							        .setText(String.valueOf(obj.getVolume()));
						ControllerMediator.getInstance().getDashboardOutputController().setOutPutDetails(obj);
					});

					searchOutputService.setOnFailed(ex ->
					{
						Main.getInstance().getUtility().showNotification("Error", "Error",
						        searchOutputService.getException().getMessage());
					});*/


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
		enter.addEventHandler(ActionEvent.ACTION, enterHandler);
	}

	/**
	 * This method adds contextmenu over Test grid cells
	 */
	private void addTestContextMenu()
	{
		testContextMenu = new ContextMenu();
		testContextMenu.setPrefSize(100, 100);

		enter2 = new MenuItem("Update Values");
		enter2.setStyle("-fx-font-size:12;");

		testContextMenu.getItems().addAll(enter2);

		enter2Handler = new EventHandler<ActionEvent>()
		{
			public void handle(ActionEvent event)
			{
				try
				{
					//bindPopupFxml("/View/DashboardTest.fxml");
					Main.getInstance().getStageManager().switchSceneAfterSecondary(FxmlView.DASHBOARD_TEST);
					ControllerMediator.getInstance().getDashboardTestController().setTertiaryWindow(true);
					ControllerMediator.getInstance().getDashboardTestController().setFromHistoryScreen(true);

					/*Calendar cal = Calendar.getInstance();
					cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(clickedGridCellTime.split(":")[0]));
					cal.set(Calendar.MINUTE, Integer.parseInt(clickedGridCellTime.split(":")[1]));
					cal.set(Calendar.SECOND, 0);
					cal.set(Calendar.MILLISECOND, 0);*/
					ControllerMediator.getInstance().getDashboardTestController().setSelectedTime(clickedGridCellTime.getTime());
					ControllerMediator.getInstance().getDashboardTestController().getLblTime()
					        .setText(sdf.format(clickedGridCellTime.getTime()));
					ControllerMediator.getInstance().getDashboardTestController().getTxtTestName().setText(clickedTestGrid);


					GetInvestigationMapperAndValueService getTestsService = GetInvestigationMapperAndValueService.getInstance(
					                drawTabbedCenter.getTestIdsMap().get(clickedTestGrid.toLowerCase()),
							PatientSession.getInstance().getPatientCase().getCaseId(), clickedGridCellTime.getTime());
					getTestsService.restart();

					/*getTestsService.setOnSucceeded(ex ->
					{
						Investigationstests testObj = new Investigationstests();

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
					});

					getTestsService.setOnFailed(ex ->
					{
						Main.getInstance().getUtility().showNotification("Error", "Error",
						        getTestsService.getException().getMessage());
					});
					*/

					investigationMapperAndValueServiceSuccessHandler = new EventHandler<WorkerStateEvent>()
					{
						@Override
						public void handle(WorkerStateEvent event)
						{
							try
							{
								Investigationstests testObj = new Investigationstests();

								InvestigationMapperAndValue mapperObj = getTestsService.getInvestigationMapperAndValue();
								ControllerMediator.getInstance().getDashboardTestController().setInvestigationMapperAndValue(mapperObj);

								for (Investigationstests obj : drawTabbedCenter.getInvestigationTestsList())
								{
									if (obj.getTestName().equalsIgnoreCase(clickedTestGrid))
									{
										testObj = obj;
										break;
									}
								}

								ControllerMediator.getInstance().getDashboardTestController().setSelectedTestObj(testObj);
								ControllerMediator.getInstance().getDashboardTestController().getAllParam(testObj);

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
		enter2.addEventHandler(ActionEvent.ACTION, enter2Handler);
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
						HashMap<String, String> totalsValuesMap = getTotalMedicationVolumeListService.getTotalUsage();
						//DecimalFormat df = new DecimalFormat("###.##"); here
						
						for (Map.Entry<String, String> entry : totalsValuesMap.entrySet())
						{
							String rate = ControllerMediator.getInstance().getMainController()
							        .getDrawTabbedCenter().getMedicineTotalRateMap().get(entry.getKey());
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
	 * This method fetches totals value for a list of fluids
	 *
	 * @param fluidList list containing fluid names
	 */
	private void getFluidTotalsListService(List<String> fluidList)
		{
			GetTotalFluidVolumeService getFluidTotalsListService = GetTotalFluidVolumeService.getInstance(
			        PatientSession.getInstance().getPatientCase().getCaseId(), fluidList);
			getFluidTotalsListService.restart();

			/*getFluidTotalsListService.setOnSucceeded(ex ->
			{
				Map<String, String> returnTotalsMap = getFluidTotalsListService.getFluidVolume();

				for (Map.Entry<String, String> entry : returnTotalsMap.entrySet())
				{
					totalLblsMap.get(FLUID_GRID_NAME).get(entry.getKey()).setText(entry.getValue());
					totalsListMap.get(FLUID_GRID_NAME).add(entry.getValue());
				}
			});

			getFluidTotalsListService.setOnFailed(ex ->
			{
				Main.getInstance().getUtility().showNotification("Error", "Error",
				        getFluidTotalsListService.getException().getMessage());
			});*/



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
	 * @param outputList list containing output names
	 */
	private void getOutputTotalsListService(List<String> outputList)
	{
		GetTotalOutputTypeVolumeService getOutputTotalsListService = GetTotalOutputTypeVolumeService
		        .getInstance(PatientSession.getInstance().getPatientCase().getCaseId(), outputList);
		getOutputTotalsListService.restart();

		/*getOutputTotalsListService.setOnSucceeded(ex ->
		{

			Map<String, String> returnTotalsMap = getOutputTotalsListService.getTotalVolume();
			for (Map.Entry<String, String> entry : returnTotalsMap.entrySet())
			{
				totalLblsMap.get(OUTPUT_GRID_NAME).get(entry.getKey()).setText(entry.getValue());
				totalsListMap.get(OUTPUT_GRID_NAME).add(entry.getValue());
			}
		});

		getOutputTotalsListService.setOnFailed(ex ->
		{
			Main.getInstance().getUtility().showNotification("Error", "Error",
			        getOutputTotalsListService.getException().getMessage());
		});*/



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
	 * This method fetches list of parameters for various sections <br>
	 * and then call suitable method for creating their grids
	 *
	 * @param caseID ID of current loaded case
	 * @param scrollPaneHolder base scrollpane which holds all the grids
	 */
	private void getParamsService(long caseID, VBox scrollPaneHolder)
	{
		GetHistoryParametersService getHistoryParametersService = GetHistoryParametersService
		        .getInstance(caseID);
		getHistoryParametersService.restart();

		/*getHistoryParametersService.setOnSucceeded(e ->
		{
			listOfTestParameters=getHistoryParametersService.getTestParameters();
			medNamesList = getHistoryParametersService.getMedications();
			fluidNamesList = getHistoryParametersService.getFluids();

			createPtMntrGrid(scrollPaneHolder);
			createAnesGrid(scrollPaneHolder);
			createMedGrid(scrollPaneHolder);
			createTestGrid(scrollPaneHolder);
			createFluidsGrid(scrollPaneHolder);
			createOutputGrid(scrollPaneHolder);
		});

		getHistoryParametersService.setOnFailed(e ->
		{
			Main.getInstance().getUtility().showNotification("Error", "Error",
					getHistoryParametersService.getException().getMessage());
		});*/



		historyParamServiceSuccessHandler = new EventHandler<WorkerStateEvent>()
		{
			@Override
			public void handle(WorkerStateEvent event)
			{
				try
				{
					listOfTestParameters=getHistoryParametersService.getTestParameters();
					fluidNamesList = getHistoryParametersService.getFluids();
					medNamesList = getHistoryParametersService.getMedications();

					if(ControllerMediator.getInstance().getMainController().getDrawTabbedCenter().getParamListMap().get(MED_GRID_NAME)!=null)
					for(String medLiveGrid: ControllerMediator.getInstance().getMainController().getDrawTabbedCenter().getParamListMap().get(MED_GRID_NAME))
					{
						if(!medNamesList.contains(medLiveGrid))
							medNamesList.add(medLiveGrid);
					}
					if(ControllerMediator.getInstance().getMainController().getDrawTabbedCenter().getParamListMap().get(FLUID_GRID_NAME)!=null)
					for(String fluidLiveGrid: ControllerMediator.getInstance().getMainController().getDrawTabbedCenter().getParamListMap().get(FLUID_GRID_NAME))
					{
						if(!fluidNamesList.contains(fluidLiveGrid))
							fluidNamesList.add(fluidLiveGrid);
					}

					if(ControllerMediator.getInstance().getMainController().getDrawTabbedCenter().getParamListMap()!=null)
					for(Map.Entry<String, List<String>> entry:ControllerMediator.getInstance().getMainController().getDrawTabbedCenter().getParamListMap().entrySet())
					{
							if (!entry.getKey().equalsIgnoreCase(PTMNTR_GRID_NAME)
							        && !entry.getKey().equalsIgnoreCase(ANES_GRID_NAME)
							        && !entry.getKey().equalsIgnoreCase(MED_GRID_NAME)
							        && !entry.getKey().equalsIgnoreCase(FLUID_GRID_NAME)
							        && !entry.getKey().equalsIgnoreCase(OUTPUT_GRID_NAME))
						{
							listOfTestParameters.put(entry.getKey(), MasterDataSession.getInstance().getTestParamDataMap().get("TestParam").get(entry.getKey()));
						}
					}

					/*if(!listOfTestParameters.keySet().contains("ABG"))
					{
						listOfTestParameters.put("ABG", MasterDataSession.getInstance().getTestParamDataMap().get("TestParam").get("ABG"));
					}
					if(!listOfTestParameters.keySet().contains("Sugar"))
					{
						listOfTestParameters.put("Sugar", MasterDataSession.getInstance().getTestParamDataMap().get("TestParam").get("Sugar"));
					}*/

					createPtMntrGrid(scrollPaneHolder);
					createAnesGrid(scrollPaneHolder);
					createMedGrid(scrollPaneHolder);
					createTestGrid(scrollPaneHolder);
					createFluidsGrid(scrollPaneHolder);
					createOutputGrid(scrollPaneHolder);

					scrollPaneHolder.getChildren().remove(0);
					// lodingImg.setVisible(false);
					getHistoryParametersService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
							historyParamServiceSuccessHandler);
					getHistoryParametersService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
							historyParamServiceFailedHandler);
				}
				catch (Exception e)
				{
					LOG.error("## Exception occured:", e);
				}
			}
		};
		getHistoryParametersService.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
				historyParamServiceSuccessHandler);

		historyParamServiceFailedHandler = new EventHandler<WorkerStateEvent>()
		{
			@Override
			public void handle(WorkerStateEvent event)
			{
				try
				{
					Main.getInstance().getUtility().showNotification("Error", "Error",
							getHistoryParametersService.getException().getMessage());

					getHistoryParametersService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
							historyParamServiceSuccessHandler);
					getHistoryParametersService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
							historyParamServiceFailedHandler);
				}
				catch (Exception e)
				{
					LOG.error("## Exception occured:", e);
				}
			}
		};
		getHistoryParametersService.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
				historyParamServiceFailedHandler);


	}




	/**
	 * This method fetch saved data from database for all grids
	 *
	 * @param caseID ID of current loaded case
	 * @param startTime timestamp starting from which data is required
	 * @param endTime timestamp till which data is required
	 */
	private void fetchHistoryData(long caseID, Date startTime, Date endTime)
	{
		GetHistoryDataService historyDataService = GetHistoryDataService.getInstance(caseID,startTime,endTime);
		historyDataService.restart();


		historyDataServiceSuccessHandler = new EventHandler<WorkerStateEvent>()
		{
			@Override
			public void handle(WorkerStateEvent event)
			{
				try
				{
					patientMonitorDataList = historyDataService.getPatientMonitorDataList();
					anethesiaMachineDataList = historyDataService.getAnethesiaMachineDataList();
					allMedicationsList=historyDataService.getAllMediactionsList();
					allOutputsList=historyDataService.getListOfOutputRecords();
					allfluidList=historyDataService.getFluidMapDB();
					List<InvestigationMapperAndValue> testHistoryList=historyDataService.getTestHistoryList();

					for(InvestigationMapperAndValue mapperObj:testHistoryList)
					{

						if (allTestValueMap.keySet().contains(mapperObj.getInvestigationvaluescasemapper().getinvestigationsTests().getTestName()))
						{
							allTestValueMap.get(mapperObj.getInvestigationvaluescasemapper().getinvestigationsTests().getTestName())
							        .put(mapperObj.getInvestigationvaluescasemapper().getDate(),mapperObj.getInvestigationSetValueList());
						}
						else
						{
							Map<Date, List<InvestigationSetValue>> valueMap=new HashMap<>();
							valueMap.put(mapperObj.getInvestigationvaluescasemapper().getDate(), mapperObj.getInvestigationSetValueList());
							allTestValueMap.put(mapperObj.getInvestigationvaluescasemapper().getinvestigationsTests().getTestName(),valueMap);
						}
					}

					recreateAllGrids();

					historyDataService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
							historyDataServiceSuccessHandler);
					historyDataService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
							historyDataServiceFailedHandler);
				}
				catch (Exception e)
				{
					LOG.error("## Exception occured:", e);
				}
			}
		};
		historyDataService.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
				historyDataServiceSuccessHandler);

		historyDataServiceFailedHandler = new EventHandler<WorkerStateEvent>()
		{
			@Override
			public void handle(WorkerStateEvent event)
			{
				try
				{
					Main.getInstance().getUtility().showNotification("Error", "Error",
							historyDataService.getException().getMessage());

					historyDataService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
							historyDataServiceSuccessHandler);
					historyDataService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
							historyDataServiceFailedHandler);
				}
				catch (Exception e)
				{
					LOG.error("## Exception occured:", e);
				}
			}
		};
		historyDataService.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
				historyDataServiceFailedHandler);

	}



	/**
	 * This method fill grid cells based on the gridname provided
	 *
	 * @param gridName name of the grid to be filled
	 * @param valuesList list containing values to be entered in the grid cells
	 * @param textboxList list containing labels shown on grid
	 * @param paramList list containing parameter names specific to grid
	 */
	@SuppressWarnings("unchecked")
	private void fillGrid(String gridName, Object valuesList, List<Label> textboxList, List<String> paramList)
	{
		int x = 0;
		String paramName = "";

		if (valuesList != null)
		if(gridName.equalsIgnoreCase(PTMNTR_GRID_NAME))
		{
			for (PatientMonitorData obj :(List<PatientMonitorData>) valuesList)
			{
				Calendar cal=Calendar.getInstance();
				cal.setTime(obj.getTimeStamp());
				if (headerTimeLblsList.contains(cal))
				{
					x = headerTimeLblsList.indexOf(cal);

					for(int y=0;y<paramList.size();y++)
					{
							Label txtBox = drawTabbedCenter.getTextboxFromList(y, x, textboxList);
						paramName = paramList.get(y).split(" ")[0].toLowerCase().trim();
						if (paramName.equalsIgnoreCase("hr"))
							txtBox.setText(obj.getHr());
						else if (paramName.equalsIgnoreCase("ibpsys"))
							txtBox.setText(obj.getiBPSys());
						else if (paramName.equalsIgnoreCase("ibpdia"))
							txtBox.setText(obj.getiBPDia());
						else if (paramName.equalsIgnoreCase("spo2"))
							txtBox.setText(obj.getSpO2());
						else if (paramName.equalsIgnoreCase("etco2"))
							txtBox.setText(obj.getEtCO2());
						else if (paramName.equalsIgnoreCase("temp1"))
							txtBox.setText(obj.getTemp1());
						else if (paramName.equalsIgnoreCase("temp2"))
							txtBox.setText(obj.getTemp2());
						else if (paramName.equalsIgnoreCase("bis"))
							txtBox.setText(obj.getBIS());
						else if (paramName.equalsIgnoreCase("etagent"))
							txtBox.setText(obj.getETAgent());
						else if (paramName.equalsIgnoreCase("mac"))
							txtBox.setText(obj.getMAC());
							else if (paramName.equalsIgnoreCase("ibpmean"))
								txtBox.setText(obj.getiBPMean());
						
							else if (paramName.equalsIgnoreCase("nibpsys"))
								txtBox.setText(obj.getNiBPSys());
							else if (paramName.equalsIgnoreCase("nibpdia"))
								txtBox.setText(obj.getNiBPDia());
							else if (paramName.equalsIgnoreCase("nibpmean"))
								txtBox.setText(obj.getNiBPMean());
						

					}
				}
			}
		}
		else if(gridName.equalsIgnoreCase(ANES_GRID_NAME))
		{
			for (AnethesiaMachineData obj :(List<AnethesiaMachineData>) valuesList)
			{
				Calendar cal=Calendar.getInstance();
				cal.setTime(obj.getTimeStamp());

				if (headerTimeLblsList.contains(cal))
				{
					x = headerTimeLblsList.indexOf(cal);

					for(int y=0;y<paramList.size();y++)
					{
							Label txtBox = drawTabbedCenter.getTextboxFromList(y, x, textboxList);
						paramName = paramList.get(y).split(" ")[0].trim();

						if (paramName.equalsIgnoreCase("Ppeak"))
							txtBox.setText(obj.getpPeak());
						else if (paramName.equalsIgnoreCase("Pmean"))
							txtBox.setText(obj.getpMean());
						else if (paramName.equalsIgnoreCase("PEEP"))
							txtBox.setText(obj.getPeep());
						else if (paramName.equalsIgnoreCase("CircuitO2"))
							txtBox.setText(obj.getCircuitO2());
						else if (paramName.equalsIgnoreCase("TVexp"))
							txtBox.setText(obj.getTvExp());
						else if (paramName.equalsIgnoreCase("MVexp"))
							txtBox.setText(obj.getMvExp());
						else if (paramName.equalsIgnoreCase("RR"))
							txtBox.setText(obj.getRr());

					}
				}
			}
		}
		else if (gridName.equalsIgnoreCase(OUTPUT_GRID_NAME))
		{
			for (IntraopOutput obj : (List<IntraopOutput>) valuesList)
			{
				Calendar cal=Calendar.getInstance();
				cal.setTime(obj.getTime());

				if (headerTimeLblsList.contains(cal))
				{
					x = headerTimeLblsList.indexOf(cal);

					for (int y = 0; y < paramList.size(); y++)
					{
							Label txtBox = drawTabbedCenter.getTextboxFromList(y, x, textboxList);
						paramName = paramList.get(y).trim();

						if (paramName.equalsIgnoreCase(obj.getOutputType()))
						txtBox.setText(obj.getVolume().toString());

					}
				}
			}
		}
		else if (gridName.equalsIgnoreCase(FLUID_GRID_NAME))
		{
			for (Fluidvalue obj : (List<Fluidvalue>)valuesList)
			{

				if(obj.getStartTime()!=null)
				{
					Calendar cal=Calendar.getInstance();
					cal.setTime(obj.getStartTime());
					if (headerTimeLblsList.contains(cal))
					{
						int y=paramListMap.get(FLUID_GRID_NAME).indexOf(obj.getFluidName().trim());
						x = headerTimeLblsList.indexOf(cal);

						Label txtBox = drawTabbedCenter.getTextboxFromList(y, x, textboxList);
						//txtBox.setFont(new Font(12));
						txtBox.setStyle("-fx-font-size:12;-fx-background-color:#60be98");
						String value=obj.getInitialVolume().toString();
						txtBox.setText(value);
					}
				}

				if(obj.getFinishTime()!=null)
				{
					Calendar cal=Calendar.getInstance();
					cal.setTime(obj.getFinishTime());
					if (headerTimeLblsList.contains(cal))
					{
						int y=paramListMap.get(FLUID_GRID_NAME).indexOf(obj.getFluidName().trim());
						x = headerTimeLblsList.indexOf(cal);

						Label txtBox = drawTabbedCenter.getTextboxFromList(y, x, textboxList);
						//txtBox.setFont(new Font(12));
						txtBox.setStyle("-fx-font-size:12;-fx-background-color:#d47f8e");
						String value=obj.getVolume().toString();
						txtBox.setText(value);

					}
				}

				/*
				int x2=-1,y=0;
				String startLbl = ">>>>>", endLbl = ">>>>>";
				if(obj.getStartTime()!=null && obj.getFinishTime()!=null)
				{
					y=paramListMap.get(FLUID_GRID_NAME).indexOf(obj.getFluidName());

					Calendar fluidStartTime=Calendar.getInstance();
					fluidStartTime.setTime(obj.getStartTime());
					Calendar fluidFinishTime=Calendar.getInstance();
					fluidFinishTime.setTime(obj.getFinishTime());


					if (headerTimeLblsList.contains(sdf.format(obj.getStartTime())) &&
							headerTimeLblsList.contains(sdf.format(obj.getFinishTime())))
					{
						x = headerTimeLblsList.indexOf(sdf.format(obj.getStartTime()));
						x2= headerTimeLblsList.indexOf(sdf.format(obj.getFinishTime()));

						startLbl=obj.getInitialVolume().toString();
						endLbl=obj.getVolume().toString();
					}
					else if (headerTimeLblsList.contains(sdf.format(obj.getStartTime())))
					{
						x = headerTimeLblsList.indexOf(sdf.format(obj.getStartTime()));
						x2= headerTimeLblsList.size()-1;

						startLbl=obj.getInitialVolume().toString();
					}
					else if(headerTimeLblsList.contains(sdf.format(obj.getFinishTime())))
					{
						x = 0;
						x2= headerTimeLblsList.indexOf(sdf.format(obj.getFinishTime()));

						endLbl=obj.getVolume().toString();
					}
					else if(fluidStartTime.getTimeInMillis()-gridFirstTimeStamp.getTimeInMillis()<0
							&& fluidFinishTime.getTimeInMillis()-gridLastTimeStamp.getTimeInMillis()>0)
					{
						x = 0;
						x2= headerTimeLblsList.size()-1;
					}

					for(int i=x;i<=x2;i++)
					{
							Label txtBox = drawTabbedCenter.getTextboxFromList(y, i, textboxList);
						txtBox.setFont(new Font(12));
						txtBox.setStyle("-fx-background-color:#60be98");

						if (i == x)
							txtBox.setText(startLbl);
						else if (i == x2)
						{
							txtBox.setText(endLbl);
							txtBox.setStyle("-fx-background-color:#d47f8e");
						}
						else
							txtBox.setText(">>>>>");

						if(txtBox.getText().equalsIgnoreCase(">>>>>"))
							txtBox.setStyle("-fx-background-color:#60be98");

					}




				}

			*/}
		}

		else if (gridName.equalsIgnoreCase(MED_GRID_NAME))
		{

			for (IntraopMedicationlog obj : (List<IntraopMedicationlog>) valuesList)
			{
				Calendar cal=Calendar.getInstance();
				cal.setTime(obj.getStartTime());
				if(obj.getBolusInfusion().equalsIgnoreCase("bolus"))
				{
					
					while(cal.get(Calendar.MINUTE)%mingap!=0) 
					{
						cal.add(Calendar.MINUTE, -1);
					}
					if(obj.getStartTime()!=null )
					if (headerTimeLblsList.contains(cal))
					{
						int y=paramListMap.get(MED_GRID_NAME).indexOf(obj.getMedicationName().trim());
						x = headerTimeLblsList.indexOf(cal);

								Label txtBox = drawTabbedCenter.getTextboxFromList(y, x, textboxList);
								// txtBox.setFont(new Font(11));
						String value="("+MedicationInfusionController.calculateUnit(obj.getInfuseDosage()*1000)+")";
						txtBox.setText(value);
					}

				}
				else if(obj.getBolusInfusion().equalsIgnoreCase("infusion"))
				{
					if(obj.getStartTime()!=null)
					{
						while(cal.get(Calendar.MINUTE)%mingap!=0) 
						{
							cal.add(Calendar.MINUTE, -1);
						}
						

						if (headerTimeLblsList.contains(cal))
						{
							int y=paramListMap.get(MED_GRID_NAME).indexOf(obj.getMedicationName().trim());
							x = headerTimeLblsList.indexOf(cal);

							Label txtBox = drawTabbedCenter.getTextboxFromList(y, x, textboxList);
							txtBox.setStyle("-fx-font-size:12;-fx-background-color:#60be98");
							String value=calInfStartValue(obj);
							//txtBox.setText(df.format(Double.parseDouble(value)));
							txtBox.setText(value);

							Tooltip tip = new Tooltip();
							tip.setText(value);
							tip.setStyle("-fx-font-size:12");
							txtBox.setTooltip(tip);
						}
					}

					if(obj.getEndTime()!=null)
					{
							
						 cal=Calendar.getInstance();
						cal.setTime(obj.getEndTime());
						while(cal.get(Calendar.MINUTE)%mingap!=0) 
						{
							cal.add(Calendar.MINUTE, -1);
						}

						if (headerTimeLblsList.contains(cal))
						{
							int y=paramListMap.get(MED_GRID_NAME).indexOf(obj.getMedicationName());
							
							x = headerTimeLblsList.indexOf(cal);

							Label txtBox = drawTabbedCenter.getTextboxFromList(y, x, textboxList);
							txtBox.setStyle("-fx-font-size:12;-fx-background-color:#d47f8e");
							String value="";

							if(obj.isFromDevice())
								value=MedicationInfusionController.calculateUnit((obj.getInfuseDosage()*1000));
							else
								value=calInfEndValue(obj);

								txtBox.setText(value);

						}
					}

				}
				/*else if(obj.getBolusInfusion().equalsIgnoreCase("infusion"))
				{
					int x2 = -1, y = 0;
					String startLbl = ">>>>>", endLbl = ">>>>>";

					if(obj.getStartTime()!=null && obj.getEndTime()!=null)
					{
						y=paramListMap.get(MED_GRID_NAME).indexOf(obj.getMedicationName());

						Calendar medStartTime=Calendar.getInstance();
						medStartTime.setTime(obj.getStartTime());
						Calendar medEndTime=Calendar.getInstance();
						medEndTime.setTime(obj.getEndTime());

						if (headerTimeLblsList.contains(sdf.format(obj.getStartTime())) &&
								headerTimeLblsList.contains(sdf.format(obj.getEndTime())))
						{
							x = headerTimeLblsList.indexOf(sdf.format(obj.getStartTime()));
							x2= headerTimeLblsList.indexOf(sdf.format(obj.getEndTime()));

							startLbl=calInfStartValue(obj);
							endLbl=calInfEndValue(obj);
						}
						else if (headerTimeLblsList.contains(sdf.format(obj.getStartTime())))
						{
							x = headerTimeLblsList.indexOf(sdf.format(obj.getStartTime()));
							x2= headerTimeLblsList.size()-1;

							startLbl=calInfStartValue(obj);
						}
						else if(headerTimeLblsList.contains(sdf.format(obj.getEndTime())))
						{
							x = 0;
							x2= headerTimeLblsList.indexOf(sdf.format(obj.getEndTime()));

							endLbl=calInfEndValue(obj);
						}
						else if(medStartTime.getTimeInMillis()-gridFirstTimeStamp.getTimeInMillis()<0
								&& medEndTime.getTimeInMillis()-gridLastTimeStamp.getTimeInMillis()>0)
						{
							x = 0;
							x2= headerTimeLblsList.size()-1;
						}

						for(int i=x;i<=x2;i++)
						{
								Label txtBox = drawTabbedCenter.getTextboxFromList(y, i, textboxList);
							txtBox.setFont(new Font(12));
							txtBox.setStyle("-fx-background-color:#60be98");
							if (i == x)
								txtBox.setText(startLbl);
							else if (i == x2)
							{
								txtBox.setText(endLbl);
								txtBox.setStyle("-fx-background-color:#d47f8e");
							}
							else
								txtBox.setText(">>>>>");

							if(txtBox.getText().equalsIgnoreCase(">>>>>"))
								txtBox.setStyle("-fx-background-color:#60be98");
						}

					}
				}*/
			}
		}
	}


	/**
	 * This method fill Test grid cells
	 *
	 * @param valuesMap map containing values that need to be filled in grid cells
	 */
	private void fillTestsGrid(Map<String, Map<Date, List<InvestigationSetValue>>> valuesMap)
	{
		for (Map.Entry<String, Map<Date, List<InvestigationSetValue>>> entry : valuesMap.entrySet())
		{
			String testName=entry.getKey();
			List<String> paramList = new ArrayList<String>();
			List<Label> gridTextboxList = textboxListMap.get(testName);


			int x, y;
			for (Map.Entry<Date, List<InvestigationSetValue>> timeEntry : entry.getValue().entrySet())
			{
				x = -1;
				Calendar cal=Calendar.getInstance();
				cal.setTime(timeEntry.getKey());

				if (headerTimeLblsList.contains(cal))
				{
					if (paramList.isEmpty())
					for (String name : paramListMap.get(testName))
					{
							paramList.add(name.split(" ")[0].trim());
					}

					x = headerTimeLblsList.indexOf(cal);

					for (InvestigationSetValue obj : timeEntry.getValue())
					{
						y = -1;
						if (paramList.contains(obj.getName().trim()))
						{
							y = paramList.indexOf(obj.getName().trim());
							Label txtBox = drawTabbedCenter.getTextboxFromList(y, x, gridTextboxList);
							if(obj.getValue()!=null && !obj.getValue().toString().equals("")){
							txtBox.setText(obj.getValue().toString());
							}
						}
					}

				}
			}

		}

	}

	/**
	 * This method calculates dosage while infusion is started
	 *
	 * @param obj Medication object containing infusion details
	 * @return calculated dosage
	 */
	private String calInfStartValue(IntraopMedicationlog obj)
	{
		float infVal = 0;

		if (obj.getConcentration() == null || obj.getRateOfInfusion() == null)
			infVal = 0;
		else
			infVal = Float.parseFloat(obj.getConcentration()) * Float.parseFloat(obj.getRateOfInfusion().split(" ")[0]);


		infVal = (infVal) / PatientSession.getInstance().getPatient().getWeight();
		infVal = (infVal)/60;
		String value = MedicationInfusionController.calculateUnit(infVal);
		

		return value;
	}

	/**
	 *  This method calculates dosage when infusion ends
	 *
	 * @param obj Medication object containing infusion details
	 * @return calculated dosage
	 */
	private String calInfEndValue(IntraopMedicationlog obj)
	{
		float endValue = 0;

		if (obj.getConcentration() == null || obj.getRateOfInfusion() == null)
			endValue = 0;
		else
			endValue=StopInfusionController.calculateInfusedDose(obj.getStartTime(), obj.getEndTime(),obj.getRateOfInfusion(), obj.getConcentration());


		
		return MedicationInfusionController.calculateUnit(endValue*1000);
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
	private static Period getPeriod(LocalDateTime dob, LocalDateTime now)
	{
		return Period.between(dob.toLocalDate(), now.toLocalDate());
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
						textbox = drawTabbedCenter.getTextboxFromList(i, y, textboxListMap.get(PTMNTR_GRID_NAME));

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
					for (int i = 0; i < paramListMap.get(ANES_GRID_NAME).size(); i++)
					{
						Label textbox = new Label();
						textbox = drawTabbedCenter.getTextboxFromList(i, y, textboxListMap.get(ANES_GRID_NAME));

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
				textbox = drawTabbedCenter.getTextboxFromList(x, y, textboxListMap.get(OUTPUT_GRID_NAME));
				textbox.setText(value);

				totalLblsMap.get(OUTPUT_GRID_NAME).get(name).setText(totalsValue);
			}
		}
		catch (Exception e)
		{
			LOG.error("## Exception occured:", e);
		}
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
					textbox = drawTabbedCenter.getTextboxFromList(x, y, gridTextboxList);
					if(testObj.getValue()!=null && !String.valueOf(testObj.getValue()).equals("") ){
						textbox.setText(String.valueOf(testObj.getValue()));
						}
						else{
							textbox.setText("");
						}
				}

			//}
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
		int mingap = 	SystemConfiguration.getInstance().getMinsGap();
		try
		{
			int x = 0, y = 0;

			if (drawTabbedCenter.getMedsFromInfPump().contains(medName))
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
			textbox = drawTabbedCenter.getTextboxFromList(x, y, textboxListMap.get(MED_GRID_NAME));
			textbox.setText(value);
			textbox.setAlignment(Pos.CENTER);

			
			if (type.equalsIgnoreCase("bolus"))
			{
				textbox.setText("(" + textbox.getText() + ")");
				textbox.setStyle(textbox.getStyle() + ";-fx-background-color:white;-fx-border-color:silver");
				
				String rate =drawTabbedCenter.getMedicineTotalRateMap().get(medName)==null?"Stopped":drawTabbedCenter.getMedicineTotalRateMap().get(medName);
				if (totalLblsMap.get(MED_GRID_NAME).get(medName) != null)
					totalLblsMap.get(MED_GRID_NAME).get(medName).setText(totals +   "  (" +rate +")");
			}
			else if (type.equalsIgnoreCase("infusion start"))
			{
				textbox.setText(value);

				Tooltip tip = new Tooltip();
				tip.setText(textbox.getText());
				tip.setStyle("-fx-font-size:12");
				textbox.setTooltip(tip);

				textbox.setStyle(textbox.getStyle() + ";-fx-background-color:#60be98");
				
				if (!totals.isEmpty())
					if (totalLblsMap.get(MED_GRID_NAME).get(medName) != null)
						totalLblsMap.get(MED_GRID_NAME).get(medName).setText(totals +  " (" +value +")");
			}
			else if (type.equalsIgnoreCase("infusion stop"))
			{
				textbox.setText(value);
				textbox.setStyle(textbox.getStyle() + ";-fx-background-color:#d47f8e");
				
				if (!totals.isEmpty())
				if (totalLblsMap.get(MED_GRID_NAME).get(medName) != null)
					totalLblsMap.get(MED_GRID_NAME).get(medName).setText(totals +  " " + "(Stopped)");
			}

			
		}
		catch (NumberFormatException e)
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
				x = paramListMap.get(FLUID_GRID_NAME).indexOf(fluidName);

				Label textbox = new Label();
				textbox = drawTabbedCenter.getTextboxFromList(x, y, textboxListMap.get(FLUID_GRID_NAME));
				textbox.setText(value);

				if (type.equalsIgnoreCase("start"))
				{
					textbox.setStyle(textbox.getStyle() + ";-fx-background-color:#60be98;-fx-font-size:12");
				}
				else if (type.equalsIgnoreCase("stop"))
				{
					textbox.setStyle(textbox.getStyle() + ";-fx-background-color:#d47f8e;-fx-font-size:12");
					totalLblsMap.get(FLUID_GRID_NAME).get(fluidName).setText(totalsValue);
				}

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
	 * @param grid grid in which row need to be added
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

			// lastGridColumn=i;
		}
	}

	/**
	 * This method add rows in the provided grid
	 *
	 * @param rowHeaderList list containing parameter names
	 * @param timeLblsList list holding time labels
	 * @param grid grid in which row need to be added
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
}
