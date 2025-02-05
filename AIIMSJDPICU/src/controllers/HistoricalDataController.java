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
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.cdac.common.GeneratedEntities.AnethesiaMachineData;
import com.cdac.common.GeneratedEntities.PatientMonitorData;
import com.cdac.framework.SystemConfiguration;

import application.CSVReader;
import application.Main;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import mediatorPattern.ControllerMediator;
import model.PatientSession;
import services.GetAllAnesthesiaDataService;
import services.GetAllPatientMonitorDataService;
import utility.DrawTabbedCenter;
import utility.ExtractDataForTrends;
import utility.ParamMinMaxRange;

/**
 * HistoricalDataController.java<br>
 * Purpose: This class contains logic to create and draw vitals/Anesthesia
 * charts
 *
 * @author Rohit_Sharma41
 *
 */
public class HistoricalDataController implements Initializable
{
	@FXML
	private AnchorPane anchorPane;

	@FXML
	private StackPane header;

	@FXML
	private VBox vboxBody;

	@FXML
	private Button btnClose;

	@FXML
	private Label titleLbl;

	private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
	private static final int NUMBER_OF_PARAMERTERS = 21;

	public DrawTabbedCenter drawTabbedCenter;
	private CSVReader csvReader = new CSVReader();

	private Map<String, String[]> csvData = new LinkedHashMap<>();
	private Map<String, String[]> paramValuesMap;

	private double X, Y1;
	private double scalingFactor, graphHeight, pixelPerMin = 0;
	private String symbol;
	private Double scaleArr[] = {};
	private Map<String, Double[]> lastCoordinatesMap = new HashMap<String, Double[]>();
	private int iterations = 1;

	private ImageView lodingImg;


	private EventHandler<WorkerStateEvent> getAllPatientMonitorDataServiceSuccessHandler;
	private EventHandler<WorkerStateEvent> getAllPatientMonitorDataServiceFailedHandler;
	private EventHandler<WorkerStateEvent> getAllAnesthesiaDataServiceSuccessHandler;
	private EventHandler<WorkerStateEvent> getAllAnesthesiaDataServiceFailedHandler;

	private EventHandler<MouseEvent> btnCloseHandler;

	private static final Logger LOG = Logger.getLogger(HistoricalDataController.class.getName());

	public Map<String, String[]> getCsvData()
	{
		return csvData;
	}

	public CSVReader getCsvReader()
	{
		return csvReader;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		// TODO Auto-generated method stub
		try
		{
			ControllerMediator.getInstance().registerHistoricalDataController(this);
			anchorPane.getStylesheets().add(this.getClass().getResource("/styles/newStyles.css").toString());

			lodingImg = new ImageView();
			Image spinnerImg = new Image(this.getClass().getResourceAsStream("/res/Spinner.gif"));
			lodingImg.setImage(spinnerImg);

			setComponentsSize();

			drawTabbedCenter = ControllerMediator.getInstance().getMainController().getDrawTabbedCenter();
			paramValuesMap = new LinkedHashMap<String, String[]>();
			titleLbl.setText(drawTabbedCenter.getCurrentTab()+ " " + titleLbl.getText());

			// ---Read data from CSV file to extract start and end time
			/*csvReader = new CSVReader();
			csvData = csvReader.parseCsvAsString(new File(SystemConfiguration.getInstance().getLocationForDeviceDataDump()
			                + "\\DeviceData_" + PatientSession.getInstance().getPatientCase().getCaseId() + ".csv"));*/

			if (drawTabbedCenter.getCurrentTab().toLowerCase().contains("anesthesia"))
			{
				fetchAnesDBdata();
			}
			else if (drawTabbedCenter.getCurrentTab().toLowerCase().contains("patient")
			        && drawTabbedCenter.getCurrentTab().toLowerCase().contains("monitor"))
			{
				fetchPtMntrDBdata();
			}
		}
		catch (Exception e)
		{
			LOG.error("## Exception occured:", e);
		}

	}

	/**
	 * This method processes data and calculates total number of pages for pagination.
	 * It also creates UI view of historical data screen and then call for graph drawing logic
	 */
	@SuppressWarnings("static-access")
	private void processDataSet()
	{
		try
		{
			if (csvData != null)
			{
				if (!csvData.isEmpty())
				{
					List<Map.Entry<String, String[]>> entryList = new ArrayList<Map.Entry<String, String[]>>(
					        csvData.entrySet());
					Map.Entry<String, String[]> firstEntry = entryList.get(0);
					Map.Entry<String, String[]> lastEntry = entryList.get(entryList.size() - 1);

					String startTime = firstEntry.getKey();
					//startTime = startTime.substring(0, startTime.lastIndexOf(":"));
					startTime = startTime.replaceAll("/", "-");

					String endTime = lastEntry.getKey();
					//endTime = endTime.substring(0, endTime.lastIndexOf(":"));
					endTime = endTime.replaceAll("/", "-");

					drawTabbedCenter.setHistStartDateTime(startTime);
					drawTabbedCenter.setHistEndDateTime(endTime);

					Calendar startCal = Calendar.getInstance();
					Calendar endCal = Calendar.getInstance();
					startCal.setTime(sdf.parse(startTime));
					endCal.setTime(sdf.parse(endTime));
					long totalCSVMins = TimeUnit.MILLISECONDS
					        .toMinutes(endCal.getTimeInMillis() - startCal.getTimeInMillis());
					drawTabbedCenter.setTotalCSVrecords(totalCSVMins);
				}
				else
				{
					Calendar startCal = Calendar.getInstance();
					Calendar endCal = Calendar.getInstance();
					endCal.setTime(startCal.getTime());
					endCal.add(Calendar.MINUTE,SystemConfiguration.getInstance().getMinsGap() * (drawTabbedCenter.TOTAL_TIME_MARKINGS - 1));
					long totalCSVMins = TimeUnit.MILLISECONDS
					        .toMinutes(endCal.getTimeInMillis() - startCal.getTimeInMillis());
					drawTabbedCenter.setTotalCSVrecords(totalCSVMins);

					drawTabbedCenter.setHistStartDateTime(sdf.format(startCal.getTime()));
					drawTabbedCenter.setHistEndDateTime(sdf.format(endCal.getTime()));
				}
			}
			drawTabbedCenter.createHistoricalDataBody(vboxBody);

			graphHeight = drawTabbedCenter.getHistGraphPanel().getPrefHeight();

			if (csvData != null)
			{
				// ---Extract required CSV data for current time frame (firstTimeLbl to endTimeLbl)
				paramValuesMap = ExtractDataForTrends.getInstance().extract(csvData, drawTabbedCenter.getHistFirstDateTimeLbl(),
				        drawTabbedCenter.getHistLastDateTimeLbl());

				if (drawTabbedCenter.getCurrentTab().toLowerCase().contains("anesthesia"))
				{
					plotStaticAnesGraph(paramValuesMap);
				}
				else if (drawTabbedCenter.getCurrentTab().toLowerCase().contains("patient")
				        && drawTabbedCenter.getCurrentTab().toLowerCase().contains("monitor"))
				{
					plotStaticPtMntrGraph(paramValuesMap);
				}
			}


			/*btnClose.setOnMouseClicked(e ->
			{
				try
				{
					Main.getInstance().getStageManager().closeSecondaryStage();
				}
				catch (Exception e1)
				{
					LOG.error("## Exception occured:", e1);
				}

			});
*/
			btnCloseHandler = new EventHandler<MouseEvent>()
			{
				public void handle(MouseEvent event)
				{
					try
					{
						Main.getInstance().getStageManager().closeSecondaryStage();
						btnClose.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnCloseHandler);
					}
					catch (Exception e)
					{
						LOG.error("## Exception occured:", e);
					}
				}
			};
			btnClose.addEventHandler(MouseEvent.MOUSE_CLICKED, btnCloseHandler);

			vboxBody.getChildren().remove(0);
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
		anchorPane.setPrefSize(screenWidth * 0.995, screenHeight * 0.85);
		header.setPrefSize(anchorPane.getPrefWidth() * 0.999, anchorPane.getPrefHeight() * 0.06);
		vboxBody.setPrefSize(anchorPane.getPrefWidth(), anchorPane.getPrefHeight() * 0.94);

		vboxBody.getChildren().add(lodingImg);
		vboxBody.setAlignment(Pos.CENTER);
	}


	/**
	 * This method fetches S5PatientMonitor data from the database corresponding to the loaded caseID
	 */
	private void fetchPtMntrDBdata()
	{
		GetAllPatientMonitorDataService getAllPatientMonitorDataService = GetAllPatientMonitorDataService.getInstance(
		        PatientSession.getInstance().getPatientCase().getCaseId());
		getAllPatientMonitorDataService.restart();


		getAllPatientMonitorDataServiceSuccessHandler = new EventHandler<WorkerStateEvent>()
		{
			@Override
			public void handle(WorkerStateEvent event)
			{

				try
				{
					List<PatientMonitorData> patientMonitorDataList = getAllPatientMonitorDataService.getPatientMonitorDataList();

					for (PatientMonitorData ptObj : patientMonitorDataList)
					{
						String valuesArr[] = new String[NUMBER_OF_PARAMERTERS];

						if (ptObj.getHr() ==null || ptObj.getHr().isEmpty() || ptObj.getHr().equals("") || ptObj.getHr().contains("--")
						        || Double.parseDouble(ptObj.getHr()) < -32000 || Double.parseDouble(ptObj.getHr()) > 32000)
							ptObj.setHr("---");
						if (ptObj.getiBPSys()==null || ptObj.getiBPSys().isEmpty() || ptObj.getiBPSys().equals("") || ptObj.getiBPSys().contains("--")
						        || Double.parseDouble(ptObj.getiBPSys()) < -32000 || Double.parseDouble(ptObj.getiBPSys()) > 32000)
							ptObj.setiBPSys("---");
						if (ptObj.getiBPDia()==null||ptObj.getiBPDia().isEmpty() || ptObj.getiBPDia().equals("") || ptObj.getiBPDia().contains("--")
						        || Double.parseDouble(ptObj.getiBPDia()) < -32000 || Double.parseDouble(ptObj.getiBPDia()) > 32000)
							ptObj.setiBPDia("---");
						if (ptObj.getSpO2()==null||ptObj.getSpO2().isEmpty() || ptObj.getSpO2().equals("") || ptObj.getSpO2().contains("--")
						        || Double.parseDouble(ptObj.getSpO2()) < -32000 || Double.parseDouble(ptObj.getSpO2()) > 32000)
							ptObj.setSpO2("---");
						if (ptObj.getEtCO2()==null||ptObj.getEtCO2().isEmpty() || ptObj.getEtCO2().equals("") || ptObj.getEtCO2().contains("--")
						        || Double.parseDouble(ptObj.getEtCO2()) < -32000 || Double.parseDouble(ptObj.getEtCO2()) > 32000)
							ptObj.setEtCO2("---");
						if (ptObj.getTemp1()==null||ptObj.getTemp1().isEmpty() || ptObj.getTemp1().equals("") || ptObj.getTemp1().contains("--")
						        || Double.parseDouble(ptObj.getTemp1()) < -32000 || Double.parseDouble(ptObj.getTemp1()) > 32000)
							ptObj.setTemp1("---");
						if (ptObj.getTemp2()==null||ptObj.getTemp2().isEmpty() || ptObj.getTemp2().equals("") || ptObj.getTemp2().contains("--")
						        || Double.parseDouble(ptObj.getTemp2()) < -32000 || Double.parseDouble(ptObj.getTemp2()) > 32000)
							ptObj.setTemp2("---");
						if (ptObj.getBIS() == null || ptObj.getBIS().isEmpty() || ptObj.getBIS().equals("")|| ptObj.getBIS().contains("--")
								|| Double.parseDouble(ptObj.getBIS()) < -32000 || Double.parseDouble(ptObj.getBIS()) > 32000)
							ptObj.setBIS("---");
						if (ptObj.getETAgent() == null || ptObj.getETAgent().isEmpty() || ptObj.getETAgent().equals("")|| ptObj.getETAgent().contains("--")
								|| Double.parseDouble(ptObj.getETAgent()) < -32000 || Double.parseDouble(ptObj.getETAgent()) > 32000)
							ptObj.setETAgent("---");
						if (ptObj.getMAC() == null || ptObj.getMAC().isEmpty() || ptObj.getMAC().equals("") || ptObj.getMAC().contains("--")
								|| Double.parseDouble(ptObj.getMAC()) < -32000 || Double.parseDouble(ptObj.getMAC()) > 32000)
							ptObj.setMAC("---");
						if (ptObj.getiBPMean() == null || ptObj.getiBPMean().isEmpty() || ptObj.getiBPMean().equals("") || ptObj.getiBPMean().contains("--")
								|| Double.parseDouble(ptObj.getiBPMean()) < -32000 || Double.parseDouble(ptObj.getiBPMean()) > 32000)
							ptObj.setiBPMean("---");
						if (ptObj.getNiBPMean() == null || ptObj.getNiBPMean().isEmpty() || ptObj.getNiBPMean().equals("") || ptObj.getNiBPMean().contains("--")
								|| Double.parseDouble(ptObj.getNiBPMean()) < -32000 || Double.parseDouble(ptObj.getNiBPMean()) > 32000)
							ptObj.setNiBPMean("---");
						
						if (ptObj.getNiBPSys()==null || ptObj.getNiBPSys().isEmpty() || ptObj.getNiBPSys().equals("") || ptObj.getNiBPSys().contains("--")
						        || Double.parseDouble(ptObj.getNiBPSys()) < -32000 || Double.parseDouble(ptObj.getNiBPSys()) > 32000)
							ptObj.setNiBPSys("---");
						
						if (ptObj.getNiBPDia()==null||ptObj.getNiBPDia().isEmpty() || ptObj.getNiBPDia().equals("") || ptObj.getNiBPDia().contains("--")
						        || Double.parseDouble(ptObj.getNiBPDia()) < -32000 || Double.parseDouble(ptObj.getNiBPDia()) > 32000)
							ptObj.setNiBPDia("---");
						
						
						valuesArr[SystemConfiguration.getInstance().getCsvHrIndex()] = ptObj.getHr();
						valuesArr[SystemConfiguration.getInstance().getCsvIbpsysIndex()] = ptObj.getiBPSys();
						valuesArr[SystemConfiguration.getInstance().getCsvIbpdiaIndex()] = ptObj.getiBPDia();
						valuesArr[SystemConfiguration.getInstance().getCsvSpo2Index()] = ptObj.getSpO2();
						valuesArr[SystemConfiguration.getInstance().getCsvEtc02Index()] = ptObj.getEtCO2();
						valuesArr[SystemConfiguration.getInstance().getCsvTemp1Index()] = ptObj.getTemp1();
						valuesArr[SystemConfiguration.getInstance().getCsvTemp2Index()] = ptObj.getTemp2();
						valuesArr[SystemConfiguration.getInstance().getCsvBisIndex()] = ptObj.getBIS();
						valuesArr[SystemConfiguration.getInstance().getCsvEtagentIndex()] = ptObj.getETAgent();
						valuesArr[SystemConfiguration.getInstance().getCsvMacIndex()] = ptObj.getMAC();
						valuesArr[SystemConfiguration.getInstance().getCsvIbpmeanIndex()] = ptObj.getiBPMean();
						valuesArr[SystemConfiguration.getInstance().getCsvIbpsysIndex()] = ptObj.getNiBPSys();
						valuesArr[SystemConfiguration.getInstance().getCsvIbpdiaIndex()] = ptObj.getNiBPDia();
						valuesArr[SystemConfiguration.getInstance().getCsvNIbpmeanIndex()] = ptObj.getNiBPMean();
						csvData.put(sdf.format(ptObj.getTimeStamp()), valuesArr);
					}

					processDataSet();

					getAllPatientMonitorDataService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
							getAllPatientMonitorDataServiceSuccessHandler);
					getAllPatientMonitorDataService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
							getAllPatientMonitorDataServiceFailedHandler);
				}
				catch (Exception e)
				{
					LOG.error("## Exception occured:", e);
				}
			}
		};
		getAllPatientMonitorDataService.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
				getAllPatientMonitorDataServiceSuccessHandler);

		getAllPatientMonitorDataServiceFailedHandler = new EventHandler<WorkerStateEvent>()
		{
			@Override
			public void handle(WorkerStateEvent event)
			{
				try
				{
					Main.getInstance().getUtility().showNotification("Error", "Error",
					        getAllPatientMonitorDataService.getException().getMessage());

					getAllPatientMonitorDataService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
					        getAllPatientMonitorDataServiceSuccessHandler);
					getAllPatientMonitorDataService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
					        getAllPatientMonitorDataServiceFailedHandler);
				}
				catch (Exception e)
				{
					LOG.error("## Exception occured:", e);
				}
			}
		};
		getAllPatientMonitorDataService.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
				getAllPatientMonitorDataServiceFailedHandler);

	}

	/**
	 * This method fetches Anesthesia data from the database corresponding to the loaded caseID
	 */
	private void fetchAnesDBdata()
	{

		//---Anesthesia objects list
		GetAllAnesthesiaDataService getAllAnesthesiaDataService = GetAllAnesthesiaDataService.getInstance(
		        PatientSession.getInstance().getPatientCase().getCaseId());
		getAllAnesthesiaDataService.restart();


		getAllAnesthesiaDataServiceSuccessHandler = new EventHandler<WorkerStateEvent>()
		{
			@Override
			public void handle(WorkerStateEvent event)
			{

				try
				{
					List<AnethesiaMachineData> anethesiaMachineDataList=getAllAnesthesiaDataService.getAnethesiaMachineDataList();

					for(AnethesiaMachineData  anesObj:anethesiaMachineDataList)
					{
						String anesValsArr[] = new String[NUMBER_OF_PARAMERTERS];

						if (anesObj.getpPeak()==null||anesObj.getpPeak().isEmpty() || anesObj.getpPeak().equals("") || anesObj.getpPeak().contains("--")
						        || Double.parseDouble(anesObj.getpPeak()) < -32000 || Double.parseDouble(anesObj.getpPeak()) > 32000)
							anesObj.setpPeak("---");

						if (anesObj.getpMean()==null||anesObj.getpMean().isEmpty() || anesObj.getpMean().equals("") || anesObj.getpMean().contains("--")
						        || Double.parseDouble(anesObj.getpMean()) < -32000 || Double.parseDouble(anesObj.getpMean()) > 32000)
							anesObj.setpMean("---");

						if (anesObj.getPeep()==null||anesObj.getPeep().isEmpty() || anesObj.getPeep().equals("") || anesObj.getPeep().contains("--")
						        || Double.parseDouble(anesObj.getPeep()) < -32000 || Double.parseDouble(anesObj.getPeep()) > 32000)
							anesObj.setPeep("---");

						if (anesObj.getCircuitO2()==null||anesObj.getCircuitO2().isEmpty() || anesObj.getCircuitO2().equals("") || anesObj.getCircuitO2().contains("--")
						        || Double.parseDouble(anesObj.getCircuitO2()) < -32000 || Double.parseDouble(anesObj.getCircuitO2()) > 32000)
							anesObj.setCircuitO2("---");

						if (anesObj.getTvExp()==null||anesObj.getTvExp().isEmpty() || anesObj.getTvExp().equals("") || anesObj.getTvExp().contains("--")
						        || Double.parseDouble(anesObj.getTvExp()) < -32000 || Double.parseDouble(anesObj.getTvExp()) > 32000)
							anesObj.setTvExp("---");

						if (anesObj.getMvExp()==null||anesObj.getMvExp().isEmpty() || anesObj.getMvExp().equals("") || anesObj.getMvExp().contains("--")
						        || Double.parseDouble(anesObj.getMvExp()) < -32000 || Double.parseDouble(anesObj.getMvExp()) > 32000)
							anesObj.setMvExp("---");

						if (anesObj.getRr()==null||anesObj.getRr().isEmpty() || anesObj.getRr().equals("") || anesObj.getRr().contains("--")
						        || Double.parseDouble(anesObj.getRr()) < -32000 || Double.parseDouble(anesObj.getRr()) > 32000)
							anesObj.setRr("---");




						anesValsArr[SystemConfiguration.getInstance().getCsvPpeakIndex()] = anesObj.getpPeak();
						anesValsArr[SystemConfiguration.getInstance().getCsvPmeanIndex()] = anesObj.getpMean();
						anesValsArr[SystemConfiguration.getInstance().getCsvPeepextIndex()] = anesObj.getPeep();
						anesValsArr[SystemConfiguration.getInstance().getCsvCicuito2Index()] = anesObj.getCircuitO2();
						anesValsArr[SystemConfiguration.getInstance().getCsvTvexpIndex()] = anesObj.getTvExp();
						anesValsArr[SystemConfiguration.getInstance().getCsvMvexpIndex()] = anesObj.getMvExp();
						anesValsArr[SystemConfiguration.getInstance().getCsvRrIndex()] = anesObj.getRr();
						csvData.put(sdf.format(anesObj.getTimeStamp()), anesValsArr);

					}

					processDataSet();



					getAllAnesthesiaDataService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
							getAllAnesthesiaDataServiceSuccessHandler);
					getAllAnesthesiaDataService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
							getAllAnesthesiaDataServiceFailedHandler);
				}
				catch (Exception e)
				{
					LOG.error("## Exception occured:", e);
				}
			}
		};
		getAllAnesthesiaDataService.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
				getAllAnesthesiaDataServiceSuccessHandler);

		getAllAnesthesiaDataServiceFailedHandler = new EventHandler<WorkerStateEvent>()
		{
			@Override
			public void handle(WorkerStateEvent event)
			{
				try
				{
					Main.getInstance().getUtility().showNotification("Error", "Error",
					        getAllAnesthesiaDataService.getException().getMessage());

					getAllAnesthesiaDataService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
							getAllAnesthesiaDataServiceSuccessHandler);
					getAllAnesthesiaDataService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
							getAllAnesthesiaDataServiceFailedHandler);
				}
				catch (Exception e)
				{
					LOG.error("## Exception occured:", e);
				}
			}
		};
		getAllAnesthesiaDataService.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
				getAllAnesthesiaDataServiceFailedHandler);

	}

	String HRval = "", IBPsysVal = "", IBPdiaVal = "", SPO2val = "", ETCO2val = "", Temp1Val = "", Temp2Val = "",
	        BISVal = "", ETAGENTVal = "", MACVal = "", IBPmeanVal = "", NIBPsysVal = "", NIBPdiaVal = "", NIBPmeanVal ="";

	/**
	 * This method sets every point for plotting from provided paramMap for S5
	 * PtMonitorGraph
	 *
	 * @param paramMap map containing timestamp at which parameter is captured and parameter values array as key-value pairs
	 */
	public void plotStaticPtMntrGraph(Map<String, String[]> paramMap)
	{
		try
		{
			lastCoordinatesMap.put(ParamMinMaxRange.HR, null);
			lastCoordinatesMap.put(ParamMinMaxRange.IBPSYS, null);
			lastCoordinatesMap.put(ParamMinMaxRange.IBPDIA, null);
			lastCoordinatesMap.put(ParamMinMaxRange.SPO2, null);
			lastCoordinatesMap.put(ParamMinMaxRange.ETCO2, null);
			lastCoordinatesMap.put(ParamMinMaxRange.TEMP1, null);
			lastCoordinatesMap.put(ParamMinMaxRange.TEMP2, null);
			lastCoordinatesMap.put(ParamMinMaxRange.TEMP2, null);
			lastCoordinatesMap.put(ParamMinMaxRange.BIS, null);
			lastCoordinatesMap.put(ParamMinMaxRange.ETAGENT, null);
			lastCoordinatesMap.put(ParamMinMaxRange.MAC, null);
			lastCoordinatesMap.put(ParamMinMaxRange.IBPMEAN, null);
			lastCoordinatesMap.put(ParamMinMaxRange.NIBPSYS, null);
			lastCoordinatesMap.put(ParamMinMaxRange.NIBPDIA, null);
		    lastCoordinatesMap.put(ParamMinMaxRange.NIBPMEAN, null);

			iterations = 1;
			X = 0;
			pixelPerMin = drawTabbedCenter.pixelsPerSecHistGraph();
			if (paramMap != null)
			{
				for (Map.Entry<String, String[]> entry : paramMap.entrySet())	
				{
					if(entry.getValue()!=null)
					{
						HRval = entry.getValue()[SystemConfiguration.getInstance().getCsvHrIndex()];
						IBPsysVal = entry.getValue()[SystemConfiguration.getInstance().getCsvIbpsysIndex()];
						IBPdiaVal = entry.getValue()[SystemConfiguration.getInstance().getCsvIbpdiaIndex()];
						SPO2val = entry.getValue()[SystemConfiguration.getInstance().getCsvSpo2Index()];
						ETCO2val = entry.getValue()[SystemConfiguration.getInstance().getCsvEtc02Index()];
						Temp1Val = entry.getValue()[SystemConfiguration.getInstance().getCsvTemp1Index()];
						Temp2Val = entry.getValue()[SystemConfiguration.getInstance().getCsvTemp2Index()];
						BISVal = entry.getValue()[SystemConfiguration.getInstance().getCsvBisIndex()];
						ETAGENTVal = entry.getValue()[SystemConfiguration.getInstance().getCsvEtagentIndex()];
						MACVal = entry.getValue()[SystemConfiguration.getInstance().getCsvMacIndex()];
						IBPmeanVal = entry.getValue()[SystemConfiguration.getInstance().getCsvIbpmeanIndex()];
					 	NIBPmeanVal = entry.getValue()[SystemConfiguration.getInstance().getCsvNIbpmeanIndex()];
					 	NIBPsysVal = entry.getValue()[SystemConfiguration.getInstance().getCsvNibpsysIndex()];
						NIBPdiaVal = entry.getValue()[SystemConfiguration.getInstance().getCsvNibpdiaIndex()];
					}
					else
					{
						HRval = "---";
						IBPsysVal = "---";
						IBPdiaVal = "---";
						SPO2val = "---";
						ETCO2val = "---";
						Temp1Val = "---";
						Temp2Val = "---";
						BISVal = "---";
						ETAGENTVal = "---";
						MACVal = "---";
						IBPmeanVal = "---";
						NIBPmeanVal = "---";
						NIBPsysVal = "---";
						NIBPdiaVal = "---";
					}

					if (HRval != null)
						if (!HRval.contains("---"))
						plotPoint(Double.parseDouble(HRval),ParamMinMaxRange.HR);

					if (IBPsysVal != null)
						if (!IBPsysVal.contains("---"))
						plotPoint(Double.parseDouble(IBPsysVal), ParamMinMaxRange.IBPSYS);

					if (IBPdiaVal != null)
						if (!IBPdiaVal.contains("---"))
						plotPoint(Double.parseDouble(IBPdiaVal), ParamMinMaxRange.IBPDIA);

					if (SPO2val != null)
						if (!SPO2val.contains("---"))
						plotPoint(Double.parseDouble(SPO2val),ParamMinMaxRange.SPO2);

					if (ETCO2val != null)
						if (!ETCO2val.contains("---"))
						plotPoint(Double.parseDouble(ETCO2val), ParamMinMaxRange.ETCO2);

					if (Temp1Val != null)
						if (!Temp1Val.contains("---"))
							plotPoint(Double.parseDouble(Temp1Val), ParamMinMaxRange.TEMP1);

					if (Temp2Val != null)
						if (!Temp2Val.contains("---"))
							plotPoint(Double.parseDouble(Temp2Val), ParamMinMaxRange.TEMP2);

					if (BISVal != null)
						if (!BISVal.contains("---"))
							plotPoint(Double.parseDouble(BISVal), ParamMinMaxRange.BIS);

					if (ETAGENTVal != null)
						if (!ETAGENTVal.contains("---"))
							plotPoint(Double.parseDouble(ETAGENTVal), ParamMinMaxRange.ETAGENT);

					if (MACVal != null)
						if (!MACVal.contains("---"))
							plotPoint(Double.parseDouble(MACVal), ParamMinMaxRange.MAC);

					if (IBPmeanVal != null)
						if (!IBPmeanVal.contains("---"))
							plotPoint(Double.parseDouble(IBPmeanVal), ParamMinMaxRange.IBPMEAN);
					
					
					
					if (NIBPsysVal != null)
						if (!NIBPsysVal.contains("---"))
						plotPoint(Double.parseDouble(NIBPsysVal), ParamMinMaxRange.NIBPSYS);
					
					if (NIBPmeanVal != null)
						if (!NIBPmeanVal.contains("---"))
							plotPoint(Double.parseDouble(NIBPmeanVal), ParamMinMaxRange.NIBPMEAN);

					if (NIBPdiaVal != null)
						if (!NIBPdiaVal.contains("---"))
						plotPoint(Double.parseDouble(NIBPdiaVal), ParamMinMaxRange.NIBPDIA);

					X += pixelPerMin;
					iterations++;

				}
			}
		}
		catch (Exception e)
		{
			LOG.error("## Exception occured:", e);
		}
	}

	String Ppeakval = "", PpmeanVal = "", PEEPExtVal = "", CircuitO2val = "", MVexpval = "", TVexpval = "", RRval = "";

	/**
	 * This method sets every point for plotting from provided paramMap for
	 * AnesthesiaGraph
	 *
	 * @param paramMap  map containing timestamp at which parameter is captured and parameter values array as key-value pairs
	 */
	public void plotStaticAnesGraph(Map<String, String[]> paramMap)
	{
		try
		{
			lastCoordinatesMap.put(ParamMinMaxRange.PPEAK, null);
			lastCoordinatesMap.put(ParamMinMaxRange.PMEAN, null);
			lastCoordinatesMap.put(ParamMinMaxRange.PEEPEXT, null);
			lastCoordinatesMap.put(ParamMinMaxRange.CIRCUITO2, null);
			lastCoordinatesMap.put(ParamMinMaxRange.MVEXP, null);
			lastCoordinatesMap.put(ParamMinMaxRange.TVEXP, null);
			lastCoordinatesMap.put(ParamMinMaxRange.RR, null);

			iterations = 1;
			X = 0;
			pixelPerMin = drawTabbedCenter.pixelsPerSecHistGraph();
			if (paramMap != null)
			{
				for (Map.Entry<String, String[]> entry : paramMap.entrySet())
				{
					if(entry.getValue()!=null)
					{
						Ppeakval = entry.getValue()[SystemConfiguration.getInstance().getCsvPpeakIndex()];
						PpmeanVal = entry.getValue()[SystemConfiguration.getInstance().getCsvPmeanIndex()];
						PEEPExtVal = entry.getValue()[SystemConfiguration.getInstance().getCsvPeepextIndex()];
						CircuitO2val = entry.getValue()[SystemConfiguration.getInstance().getCsvCicuito2Index()];
						MVexpval = entry.getValue()[SystemConfiguration.getInstance().getCsvMvexpIndex()];
						TVexpval = entry.getValue()[SystemConfiguration.getInstance().getCsvTvexpIndex()];
						RRval = entry.getValue()[SystemConfiguration.getInstance().getCsvRrIndex()];
					}
					else
					{
						Ppeakval = "---";
						PpmeanVal = "---";
						PEEPExtVal = "---";
						CircuitO2val = "---";
						MVexpval = "---";
						TVexpval = "---";
						RRval = "---";

					}

					if (Ppeakval != null)
						if (!Ppeakval.contains("---"))
						plotPoint(Double.parseDouble(Ppeakval),ParamMinMaxRange.PPEAK);

					if (PpmeanVal != null)
						if (!PpmeanVal.contains("---"))
						plotPoint(Double.parseDouble(PpmeanVal),ParamMinMaxRange.PMEAN);

					if (PEEPExtVal != null)
						if (!PEEPExtVal.contains("---"))
						plotPoint(Double.parseDouble(PEEPExtVal),ParamMinMaxRange.PEEPEXT);

					if (CircuitO2val != null)
						if (!CircuitO2val.contains("---"))
						plotPoint(Double.parseDouble(CircuitO2val),ParamMinMaxRange.CIRCUITO2);

					if (MVexpval != null)
						if (!MVexpval.contains("---"))
						plotPoint(Double.parseDouble(MVexpval),ParamMinMaxRange.MVEXP);

					if (TVexpval != null)
						if (!TVexpval.contains("---"))
						plotPoint(Double.parseDouble(TVexpval),ParamMinMaxRange.TVEXP);

					if (RRval != null)
						if (!RRval.contains("---"))
						plotPoint(Double.parseDouble(RRval),ParamMinMaxRange.RR);

					X += pixelPerMin;
						iterations++;
				}
			}
		}
		catch (Exception e)
		{
			LOG.error("## Exception occured:", e);
		}
	}

	/**
	 * This method strokes legend for provided parameter at specified value
	 *
	 * @param val actual value to be plotted
	 * @param paramName parameter to which value belongs
	 */
	private void plotPoint(double val, String paramName)
	{
		try
		{
			if (paramName.equalsIgnoreCase(ParamMinMaxRange.HR))
			{
				symbol = ParamMinMaxRange.HR_LEGEND;
				drawTabbedCenter.getHistGraphBrush().setStroke(Color.web(ParamMinMaxRange.HR_COLOR));
				scaleArr = drawTabbedCenter.getParamScaleMap().get(ParamMinMaxRange.HR);
			}
			else if (paramName.equalsIgnoreCase(ParamMinMaxRange.IBPSYS))
			{
				symbol = ParamMinMaxRange.IBPSYS_LEGEND;
				drawTabbedCenter.getHistGraphBrush().setStroke(Color.web(ParamMinMaxRange.IBP_COLOR));
				scaleArr = drawTabbedCenter.getParamScaleMap().get(ParamMinMaxRange.IBP);
			}
			else if (paramName.equalsIgnoreCase(ParamMinMaxRange.IBPDIA))
			{
				symbol = ParamMinMaxRange.IBPDIA_LEGEND;
				drawTabbedCenter.getHistGraphBrush().setStroke(Color.web(ParamMinMaxRange.IBP_COLOR));
				scaleArr = drawTabbedCenter.getParamScaleMap().get(ParamMinMaxRange.IBP);
			}
			else if (paramName.equalsIgnoreCase(ParamMinMaxRange.SPO2))
			{
				symbol = ParamMinMaxRange.SPO2_LEGEND;
				drawTabbedCenter.getHistGraphBrush().setStroke(Color.web(ParamMinMaxRange.SPO2_COLOR));
				scaleArr = drawTabbedCenter.getParamScaleMap().get(ParamMinMaxRange.SPO2);
			}
			else if (paramName.equalsIgnoreCase(ParamMinMaxRange.ETCO2))
			{
				symbol = ParamMinMaxRange.HR_LEGEND;
				drawTabbedCenter.getHistGraphBrush().setStroke(Color.web(ParamMinMaxRange.ETCO2_COLOR));
				scaleArr = drawTabbedCenter.getParamScaleMap().get(ParamMinMaxRange.ETCO2);
			}
			else if (paramName.equalsIgnoreCase(ParamMinMaxRange.TEMP1))
			{
				symbol = ParamMinMaxRange.TEMP1_LEGEND;
				drawTabbedCenter.getHistGraphBrush().setStroke(Color.web(ParamMinMaxRange.TEMP_COLOR));
				scaleArr = drawTabbedCenter.getParamScaleMap().get(ParamMinMaxRange.TEMP);
			}
			else if (paramName.equalsIgnoreCase(ParamMinMaxRange.TEMP2))
			{
				symbol = ParamMinMaxRange.TEMP2_LEGEND;
				drawTabbedCenter.getHistGraphBrush().setStroke(Color.web(ParamMinMaxRange.TEMP_COLOR));
				scaleArr = drawTabbedCenter.getParamScaleMap().get(ParamMinMaxRange.TEMP);
			}
			else if (paramName.equalsIgnoreCase(ParamMinMaxRange.BIS))
			{
				symbol = ParamMinMaxRange.BIS_LEGEND;
				drawTabbedCenter.getHistGraphBrush().setStroke(Color.web(ParamMinMaxRange.BIS_COLOR));
				scaleArr = drawTabbedCenter.getParamScaleMap().get(ParamMinMaxRange.BIS);
			}
			else if (paramName.equalsIgnoreCase(ParamMinMaxRange.ETAGENT))
			{
				symbol = ParamMinMaxRange.ETAGENT_LEGEND;
				drawTabbedCenter.getHistGraphBrush().setStroke(Color.web(ParamMinMaxRange.ETAGENT_COLOR));
				scaleArr = drawTabbedCenter.getParamScaleMap().get(ParamMinMaxRange.ETAGENT);
			}
			else if (paramName.equalsIgnoreCase(ParamMinMaxRange.MAC))
			{
				symbol = ParamMinMaxRange.MAC_LEGEND;
				drawTabbedCenter.getHistGraphBrush().setStroke(Color.web(ParamMinMaxRange.MAC_COLOR));
				scaleArr = drawTabbedCenter.getParamScaleMap().get(ParamMinMaxRange.MAC);
			}
			else if (paramName.equalsIgnoreCase(ParamMinMaxRange.IBPMEAN))
			{
				symbol = ParamMinMaxRange.IBPMEAN_LEGEND;
				drawTabbedCenter.getHistGraphBrush().setStroke(Color.web(ParamMinMaxRange.IBP_COLOR));
				scaleArr = drawTabbedCenter.getParamScaleMap().get(ParamMinMaxRange.IBP);
			}
			
			else if (paramName.equalsIgnoreCase(ParamMinMaxRange.NIBPMEAN))
			{
				symbol = ParamMinMaxRange.NIBPMEAN_LEGEND;
				drawTabbedCenter.getHistGraphBrush().setStroke(Color.web(ParamMinMaxRange.NIBP_COLOR));
				scaleArr = drawTabbedCenter.getParamScaleMap().get(ParamMinMaxRange.NIBP);
			}
			else if (paramName.equalsIgnoreCase(ParamMinMaxRange.NIBPSYS))
			{
				symbol = ParamMinMaxRange.NIBPSYS_LEGEND;
				drawTabbedCenter.getHistGraphBrush().setStroke(Color.web(ParamMinMaxRange.NIBP_COLOR));
				scaleArr = drawTabbedCenter.getParamScaleMap().get(ParamMinMaxRange.NIBP);
			}
			else if (paramName.equalsIgnoreCase(ParamMinMaxRange.NIBPDIA))
			{
				symbol = ParamMinMaxRange.NIBPDIA_LEGEND;
				drawTabbedCenter.getHistGraphBrush().setStroke(Color.web(ParamMinMaxRange.NIBP_COLOR));
				scaleArr = drawTabbedCenter.getParamScaleMap().get(ParamMinMaxRange.NIBP);
			}

			else if (paramName.equalsIgnoreCase(ParamMinMaxRange.PPEAK))
			{
				symbol = ParamMinMaxRange.PPEAK_LEGEND;
				drawTabbedCenter.getHistGraphBrush().setStroke(Color.web(ParamMinMaxRange.PPEAK_COLOR));
				scaleArr = drawTabbedCenter.getParamScaleMap().get(ParamMinMaxRange.PPEAK);
			}
			else if (paramName.equalsIgnoreCase(ParamMinMaxRange.PMEAN))
			{
				symbol = ParamMinMaxRange.PMEAN_LEGEND;
				drawTabbedCenter.getHistGraphBrush().setStroke(Color.web(ParamMinMaxRange.PMEAN_COLOR));
				scaleArr = drawTabbedCenter.getParamScaleMap().get(ParamMinMaxRange.PMEAN);
			}
			else if (paramName.equalsIgnoreCase(ParamMinMaxRange.PEEPEXT))
			{
				symbol = ParamMinMaxRange.PEEPEXT_LEGEND;
				drawTabbedCenter.getHistGraphBrush().setStroke(Color.web(ParamMinMaxRange.PEEPEXT_COLOR));
				scaleArr = drawTabbedCenter.getParamScaleMap().get(ParamMinMaxRange.PEEPEXT);
			}
			else if (paramName.equalsIgnoreCase(ParamMinMaxRange.CIRCUITO2))
			{
				symbol = ParamMinMaxRange.CIRCUITO2_LEGEND;
				drawTabbedCenter.getHistGraphBrush().setStroke(Color.web(ParamMinMaxRange.CIRCUITO2_COLOR));
				scaleArr = drawTabbedCenter.getParamScaleMap().get(ParamMinMaxRange.CIRCUITO2);
			}
			else if (paramName.equalsIgnoreCase(ParamMinMaxRange.TVEXP))
			{
				symbol = ParamMinMaxRange.TVEXP_LEGEND;
				drawTabbedCenter.getHistGraphBrush().setStroke(Color.web(ParamMinMaxRange.TVEXP_COLOR));
				scaleArr = drawTabbedCenter.getParamScaleMap().get(ParamMinMaxRange.TVEXP);
			}
			else if (paramName.equalsIgnoreCase(ParamMinMaxRange.MVEXP))
			{
				symbol = ParamMinMaxRange.MVEXP_LEGEND;
				drawTabbedCenter.getHistGraphBrush().setStroke(Color.web(ParamMinMaxRange.MVEXP_COLOR));
				scaleArr = drawTabbedCenter.getParamScaleMap().get(ParamMinMaxRange.MVEXP);
			}
			else if (paramName.equalsIgnoreCase(ParamMinMaxRange.RR))
			{
				symbol = ParamMinMaxRange.RR_LEGEND;
				drawTabbedCenter.getHistGraphBrush().setStroke(Color.web(ParamMinMaxRange.RR_COLOR));
				scaleArr = drawTabbedCenter.getParamScaleMap().get(ParamMinMaxRange.RR);
			}

			scalingFactor = graphHeight / scaleArr[0];

			Y1 = graphHeight - (val * scalingFactor);

			if (iterations > 1)
			{
				Double[] lastPts = lastCoordinatesMap.get(paramName);
				if (lastPts != null)
					if (lastPts[0] < X)
					{
						drawTabbedCenter.getHistGraphBrush().strokeLine(lastPts[0] + 2, lastPts[1] - 3, X - 2, Y1 - 3);
					}

			}

			drawTabbedCenter.getHistGraphBrush().strokeText(symbol, X - 4, Y1);
			lastCoordinatesMap.put(paramName, new Double[] { X - 4, Y1 });
		}
		catch (Exception e)
		{
			LOG.error("## Exception occured:", e);
		}

	}




}
