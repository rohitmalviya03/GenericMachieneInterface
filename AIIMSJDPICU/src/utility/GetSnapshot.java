/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package utility;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.cdac.common.GeneratedEntities.AnethesiaMachineData;
import com.cdac.common.GeneratedEntities.PatientMonitorData;
import com.cdac.framework.SystemConfiguration;

import application.CSVReader;
import application.Main;
import controllers.DashboardTestController;
import javafx.application.Platform;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import mediatorPattern.ControllerMediator;
import model.PatientSession;
import services.GetAllAnesthesiaDataService;
import services.GetAllPatientMonitorDataService;

/**
 * This class is used to get historical data for trend chart from data base
 *
 * @author Sudeep_Sahoo
 *
 */
public class GetSnapshot {

	private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
	private static final int NUMBER_OF_PARAMERTERS = 18;

	public DrawTabbedCenterForSnapshot drawTabbedCenterForSnapshot;
	private CSVReader csvReader = new CSVReader();

	private Map<String, String[]> csvData = new LinkedHashMap<>();
	private Map<String, String[]> paramValuesMap;

	private double X, Y1;
	private double scalingFactor, graphHeight, pixelPerMin = 0;
	private String symbol;
	private Double scaleArr[] = {};
	private Map<String, Double[]> lastCoordinatesMap = new HashMap<String, Double[]>();
	private int iterations = 1;
	private AnchorPane anchorPane = new AnchorPane();
	private Label titleLbl = new Label();
	private VBox vboxBody = new VBox();
	private StackPane header = new StackPane();

	private EventHandler<WorkerStateEvent> getAllAnesthesiaDataServiceSuccessHandler;
	private EventHandler<WorkerStateEvent> getAllAnesthesiaDataServiceFailedHandler;

	private EventHandler<WorkerStateEvent> getAllPatientMonitorDataServiceSuccessHandler;
	private EventHandler<WorkerStateEvent> getAllPatientMonitorDataServiceFailedHandler;

	private static final Logger LOG = Logger.getLogger(GetSnapshot.class.getName());

	public Map<String, String[]> getCsvData() {
		return csvData;
	}

	public CSVReader getCsvReader() {
		return csvReader;
	}

	/**
	 * This method is usd to initialize trend chart containers
	 *
	 * @param tabbedCentre
	 */
	public void initializeContainer(DrawTabbedCenterForSnapshot tabbedCentre) {
		try {

			if (ControllerMediator.getInstance().getGetSnapshot() == null) {
				ControllerMediator.getInstance().registerGetSnapshot(this);
			}

			anchorPane.getStylesheets().add(this.getClass().getResource("/styles/newStyles.css").toString());
			setComponentsSize();

			drawTabbedCenterForSnapshot = tabbedCentre;

			paramValuesMap = new LinkedHashMap<String, String[]>();
			titleLbl.setText("anesthesia");

			if (drawTabbedCenterForSnapshot.getCurrentTab().toLowerCase().contains("anesthesia")) {
				fetchAnesDBdata();
			} else if (drawTabbedCenterForSnapshot.getCurrentTab().toLowerCase().contains("patient")
					&& drawTabbedCenterForSnapshot.getCurrentTab().toLowerCase().contains("monitor")) {
				fetchPtMntrDBdata();
				ControllerMediator.getInstance().GetSnapshotController().setReportGenerated(true);
			}
			csvData = new LinkedHashMap<>();

		} catch (Exception e) {
			LOG.error("## Exception occured:", e);
		}
	}

	/**
	 * This method is used to process the device data for trend chart plotting
	 */
	public void processDataSet() {
		try {
			if (csvData != null && csvData.size() != 0) {
				if (!csvData.isEmpty()) {
					List<Map.Entry<String, String[]>> entryList = new ArrayList<Map.Entry<String, String[]>>(
							csvData.entrySet());
					Map.Entry<String, String[]> firstEntry = entryList.get(0);
					Map.Entry<String, String[]> lastEntry = entryList.get(entryList.size() - 1);

					String startTime = firstEntry.getKey();
					// startTime = startTime.substring(0,
					// startTime.lastIndexOf(":"));
					startTime = startTime.replaceAll("/", "-");

					String endTime = lastEntry.getKey();
					// endTime = endTime.substring(0, endTime.lastIndexOf(":"));
					endTime = endTime.replaceAll("/", "-");

					drawTabbedCenterForSnapshot.setHistStartDateTime(startTime);
					drawTabbedCenterForSnapshot.setHistEndDateTime(endTime);

					Calendar startCal = Calendar.getInstance();
					Calendar endCal = Calendar.getInstance();
					startCal.setTime(sdf.parse(startTime));
					endCal.setTime(sdf.parse(endTime));
					long totalCSVMins=0;
					if(endCal.getTimeInMillis()==startCal.getTimeInMillis()){
						totalCSVMins=1;
					}else{
						 totalCSVMins = TimeUnit.MILLISECONDS
									.toMinutes(endCal.getTimeInMillis() - startCal.getTimeInMillis());
					}

					drawTabbedCenterForSnapshot.setTotalCSVrecords(totalCSVMins);
				} else {
					Calendar startCal = Calendar.getInstance();
					Calendar endCal = Calendar.getInstance();
					endCal.setTime(startCal.getTime());
					endCal.add(Calendar.MINUTE, SystemConfiguration.getInstance().getMinsGap()
							* (drawTabbedCenterForSnapshot.TOTAL_TIME_MARKINGS - 1));
					long totalCSVMins = TimeUnit.MILLISECONDS
							.toMinutes(endCal.getTimeInMillis() - startCal.getTimeInMillis());
					drawTabbedCenterForSnapshot.setTotalCSVrecords(totalCSVMins);

					drawTabbedCenterForSnapshot.setHistStartDateTime(sdf.format(startCal.getTime()));
					drawTabbedCenterForSnapshot.setHistEndDateTime(sdf.format(endCal.getTime()));
				}
				drawTabbedCenterForSnapshot.createHistoricalDataBody(vboxBody);
				graphHeight = drawTabbedCenterForSnapshot.getHistGraphPanel().getPrefHeight();
			}

			if (csvData != null && csvData.size() != 0) {
				// ---Extract required CSV data for current time frame
				// (firstTimeLbl to endTimeLbl)
				paramValuesMap = ExtractDataForTrends.getInstance().extract(csvData,
						drawTabbedCenterForSnapshot.getHistFirstDateTimeLbl(),
						drawTabbedCenterForSnapshot.getHistLastDateTimeLbl());

				if (drawTabbedCenterForSnapshot.getCurrentTab().toLowerCase().contains("anesthesia")) {
					plotStaticAnesGraph(paramValuesMap);
				} else if (drawTabbedCenterForSnapshot.getCurrentTab().toLowerCase().contains("patient")
						&& drawTabbedCenterForSnapshot.getCurrentTab().toLowerCase().contains("monitor")) {
					plotStaticPtMntrGraph(paramValuesMap);
				}
			}

			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					ControllerMediator.getInstance().GetSnapshotController().initializeConatiner();

				}
			});

		} catch (Exception e) {
			LOG.error("## Exception occured:", e);
		}
	}

	/**
	 * This method sets percentage width and height to screen components
	 */
	public void setComponentsSize() {
		try {
			double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
			double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();
			anchorPane.setPrefSize(screenWidth * 0.995, screenHeight * 0.85);
			header.setPrefSize(anchorPane.getPrefWidth() * 0.999, anchorPane.getPrefHeight() * 0.06);
			vboxBody.setPrefSize(anchorPane.getPrefWidth(), anchorPane.getPrefHeight() * 0.94);

		} catch (Exception e) {
			LOG.error("## Exception occured:", e);
		}

	}

	/**
	 * This method is used to fetch patient monitor device data from data base
	 */
	public void fetchPtMntrDBdata() {
		try {

			GetAllPatientMonitorDataService getAllPatientMonitorDataService = GetAllPatientMonitorDataService
					.getInstance(PatientSession.getInstance().getPatientCase().getCaseId());
			getAllPatientMonitorDataService.restart();

			getAllPatientMonitorDataServiceSuccessHandler = new EventHandler<WorkerStateEvent>() {
				@Override
				public void handle(WorkerStateEvent event) {
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
						if (ptObj.getMAC() == null || ptObj.getMAC().isEmpty() || ptObj.getMAC().equals("")
						        || ptObj.getMAC().contains("--") || Double.parseDouble(ptObj.getMAC()) < -32000
						        || Double.parseDouble(ptObj.getMAC()) > 32000)
							ptObj.setMAC("---");
						if (ptObj.getiBPMean() == null || ptObj.getiBPMean().isEmpty() || ptObj.getiBPMean().equals("")
						        || ptObj.getiBPMean().contains("--") || Double.parseDouble(ptObj.getiBPMean()) < -32000
						        || Double.parseDouble(ptObj.getiBPMean()) > 32000)
							ptObj.setiBPMean("---");


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
						csvData.put(sdf.format(ptObj.getTimeStamp()), valuesArr);
					}

					processDataSet();

					getAllPatientMonitorDataService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
							getAllPatientMonitorDataServiceSuccessHandler);
					getAllPatientMonitorDataService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
							getAllPatientMonitorDataServiceFailedHandler);
				}
			};
			getAllPatientMonitorDataService.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
					getAllPatientMonitorDataServiceSuccessHandler);

			getAllPatientMonitorDataServiceFailedHandler = new EventHandler<WorkerStateEvent>() {
				@Override
				public void handle(WorkerStateEvent event) {
					Main.getInstance().getUtility().showNotification("Error", "Error",
							getAllPatientMonitorDataService.getException().getMessage());

					getAllPatientMonitorDataService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
							getAllPatientMonitorDataServiceSuccessHandler);
					getAllPatientMonitorDataService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
							getAllPatientMonitorDataServiceFailedHandler);
				}
			};
			getAllPatientMonitorDataService.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
					getAllPatientMonitorDataServiceFailedHandler);

		} catch (Exception e) {
			LOG.error("## Exception occured:", e);
		}
	}

	/**
	 * This method is used to fetch Anaesthesia Device data from data base
	 */
	public void fetchAnesDBdata() {
		try {

			// ---Anesthesia objects list
			GetAllAnesthesiaDataService getAllAnesthesiaDataService = GetAllAnesthesiaDataService
					.getInstance(PatientSession.getInstance().getPatientCase().getCaseId());
			getAllAnesthesiaDataService.restart();

			getAllAnesthesiaDataServiceSuccessHandler = new EventHandler<WorkerStateEvent>() {
				@Override
				public void handle(WorkerStateEvent event) {

					List<AnethesiaMachineData> anethesiaMachineDataList = getAllAnesthesiaDataService
							.getAnethesiaMachineDataList();

					for (AnethesiaMachineData anesObj : anethesiaMachineDataList) {
						String anesValsArr[] = new String[NUMBER_OF_PARAMERTERS];

						if (anesObj.getpPeak() == null || anesObj.getpPeak().isEmpty() || anesObj.getpPeak().equals("")
								|| anesObj.getpPeak().contains("--") || Double.parseDouble(anesObj.getpPeak()) < -32000
								|| Double.parseDouble(anesObj.getpPeak()) > 32000)
							anesObj.setpPeak("---");

						if (anesObj.getpMean() == null || anesObj.getpMean().isEmpty() || anesObj.getpMean().equals("")
								|| anesObj.getpMean().contains("--") || Double.parseDouble(anesObj.getpMean()) < -32000
								|| Double.parseDouble(anesObj.getpMean()) > 32000)
							anesObj.setpMean("---");

						if (anesObj.getPeep() == null || anesObj.getPeep().isEmpty() || anesObj.getPeep().equals("")
								|| anesObj.getPeep().contains("--") || Double.parseDouble(anesObj.getPeep()) < -32000
								|| Double.parseDouble(anesObj.getPeep()) > 32000)
							anesObj.setPeep("---");

						if (anesObj.getCircuitO2() == null || anesObj.getCircuitO2().isEmpty()
								|| anesObj.getCircuitO2().equals("") || anesObj.getCircuitO2().contains("--")
								|| Double.parseDouble(anesObj.getCircuitO2()) < -32000
								|| Double.parseDouble(anesObj.getCircuitO2()) > 32000)
							anesObj.setCircuitO2("---");

						if (anesObj.getTvExp() == null || anesObj.getTvExp().isEmpty() || anesObj.getTvExp().equals("")
								|| anesObj.getTvExp().contains("--") || Double.parseDouble(anesObj.getTvExp()) < -32000
								|| Double.parseDouble(anesObj.getTvExp()) > 32000)
							anesObj.setTvExp("---");

						if (anesObj.getMvExp() == null || anesObj.getMvExp().isEmpty() || anesObj.getMvExp().equals("")
								|| anesObj.getMvExp().contains("--") || Double.parseDouble(anesObj.getMvExp()) < -32000
								|| Double.parseDouble(anesObj.getMvExp()) > 32000)
							anesObj.setMvExp("---");

						if (anesObj.getRr() == null || anesObj.getRr().isEmpty() || anesObj.getRr().equals("")
								|| anesObj.getRr().contains("--") || Double.parseDouble(anesObj.getRr()) < -32000
								|| Double.parseDouble(anesObj.getRr()) > 32000)
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
			};
			getAllAnesthesiaDataService.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
					getAllAnesthesiaDataServiceSuccessHandler);

			getAllAnesthesiaDataServiceFailedHandler = new EventHandler<WorkerStateEvent>() {
				@Override
				public void handle(WorkerStateEvent event) {

					Main.getInstance().getUtility().showNotification("Error", "Error",
							getAllAnesthesiaDataService.getException().getMessage());

					getAllAnesthesiaDataService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
							getAllAnesthesiaDataServiceSuccessHandler);
					getAllAnesthesiaDataService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
							getAllAnesthesiaDataServiceFailedHandler);

				}
			};
			getAllAnesthesiaDataService.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
					getAllAnesthesiaDataServiceFailedHandler);

		} catch (Exception e) {
			LOG.error("## Exception occured:", e);
		}
	}

	String HRval = "", IBPsysVal = "", IBPdiaVal = "", SPO2val = "", ETCO2val = "", Temp1Val = "", Temp2Val = "", BISVal = "", ETAGENTVal = "", MACVal = "",IBPmeanVal = "";

	/**
	 * This method sets every point for plotting from provided paramMap for S5
	 * PtMonitorGraph
	 *
	 * @param paramMap
	 */
	public void plotStaticPtMntrGraph(Map<String, String[]> paramMap) {
		try {
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

			iterations = 1;
			X = 0;
			pixelPerMin = drawTabbedCenterForSnapshot.pixelsPerSecHistGraph();
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

					X += pixelPerMin;
					iterations++;

				}
			}
		} catch (Exception e) {
			LOG.error("## Exception occured:", e);
		}
	}

	String Ppeakval = "", PpmeanVal = "", PEEPExtVal = "", CircuitO2val = "", MVexpval = "", TVexpval = "", RRval = "";

	/**
	 * This method sets every point for plotting from provided paramMap for
	 * AnesthesiaGraph
	 *
	 * @param paramMap
	 */
	public void plotStaticAnesGraph(Map<String, String[]> paramMap) {
		try {

			lastCoordinatesMap.put(ParamMinMaxRange.PPEAK, null);
			lastCoordinatesMap.put(ParamMinMaxRange.PMEAN, null);
			lastCoordinatesMap.put(ParamMinMaxRange.PEEPEXT, null);
			lastCoordinatesMap.put(ParamMinMaxRange.CIRCUITO2, null);
			lastCoordinatesMap.put(ParamMinMaxRange.MVEXP, null);
			lastCoordinatesMap.put(ParamMinMaxRange.TVEXP, null);
			lastCoordinatesMap.put(ParamMinMaxRange.RR, null);

			iterations = 1;
			X = 0;
			pixelPerMin = drawTabbedCenterForSnapshot.pixelsPerSecHistGraph();
			if (paramMap != null) {
				for (Map.Entry<String, String[]> entry : paramMap.entrySet()) {
					if (entry.getValue() != null) {
						Ppeakval = entry.getValue()[SystemConfiguration.getInstance().getCsvPpeakIndex()];
						PpmeanVal = entry.getValue()[SystemConfiguration.getInstance().getCsvPmeanIndex()];
						PEEPExtVal = entry.getValue()[SystemConfiguration.getInstance().getCsvPeepextIndex()];
						CircuitO2val = entry.getValue()[SystemConfiguration.getInstance().getCsvCicuito2Index()];
						MVexpval = entry.getValue()[SystemConfiguration.getInstance().getCsvMvexpIndex()];
						TVexpval = entry.getValue()[SystemConfiguration.getInstance().getCsvTvexpIndex()];
						RRval = entry.getValue()[SystemConfiguration.getInstance().getCsvRrIndex()];
					} else {
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
							plotPoint(Double.parseDouble(Ppeakval), ParamMinMaxRange.PPEAK);

					if (PpmeanVal != null)
						if (!PpmeanVal.contains("---"))
							plotPoint(Double.parseDouble(PpmeanVal), ParamMinMaxRange.PMEAN);

					if (PEEPExtVal != null)
						if (!PEEPExtVal.contains("---"))
							plotPoint(Double.parseDouble(PEEPExtVal), ParamMinMaxRange.PEEPEXT);

					if (CircuitO2val != null)
						if (!CircuitO2val.contains("---"))
							plotPoint(Double.parseDouble(CircuitO2val), ParamMinMaxRange.CIRCUITO2);

					if (MVexpval != null)
						if (!MVexpval.contains("---"))
							plotPoint(Double.parseDouble(MVexpval), ParamMinMaxRange.MVEXP);

					if (TVexpval != null)
						if (!TVexpval.contains("---"))
							plotPoint(Double.parseDouble(TVexpval), ParamMinMaxRange.TVEXP);

					if (RRval != null)
						if (!RRval.contains("---"))
							plotPoint(Double.parseDouble(RRval), ParamMinMaxRange.RR);

					X += pixelPerMin;
					iterations++;
				}
			}

		} catch (Exception e) {
			LOG.error("## Exception occured:", e);
		}
	}

	/**
	 * This method strokes legend for provided parameter at specified value
	 *
	 * @param val
	 * @param paramName
	 */
	public void plotPoint(double val, String paramName) {
		try {

			if (paramName.equalsIgnoreCase(ParamMinMaxRange.HR)) {
				symbol = ParamMinMaxRange.HR_LEGEND;
				drawTabbedCenterForSnapshot.getHistGraphBrush().setStroke(Color.web(ParamMinMaxRange.HR_COLOR));
				scaleArr = drawTabbedCenterForSnapshot.getParamScaleMap().get(ParamMinMaxRange.HR);
			} else if (paramName.equalsIgnoreCase(ParamMinMaxRange.IBPSYS)) {
				symbol = ParamMinMaxRange.IBPSYS_LEGEND;
				drawTabbedCenterForSnapshot.getHistGraphBrush().setStroke(Color.web(ParamMinMaxRange.IBP_COLOR));
				scaleArr = drawTabbedCenterForSnapshot.getParamScaleMap().get(ParamMinMaxRange.IBP);
			} else if (paramName.equalsIgnoreCase(ParamMinMaxRange.IBPDIA)) {
				symbol = ParamMinMaxRange.IBPDIA_LEGEND;
				drawTabbedCenterForSnapshot.getHistGraphBrush().setStroke(Color.web(ParamMinMaxRange.IBP_COLOR));
				scaleArr = drawTabbedCenterForSnapshot.getParamScaleMap().get(ParamMinMaxRange.IBP);
			} else if (paramName.equalsIgnoreCase(ParamMinMaxRange.SPO2)) {
				symbol = ParamMinMaxRange.SPO2_LEGEND;
				drawTabbedCenterForSnapshot.getHistGraphBrush().setStroke(Color.web(ParamMinMaxRange.SPO2_COLOR));
				scaleArr = drawTabbedCenterForSnapshot.getParamScaleMap().get(ParamMinMaxRange.SPO2);
			} else if (paramName.equalsIgnoreCase(ParamMinMaxRange.ETCO2)) {
				symbol = ParamMinMaxRange.HR_LEGEND;
				drawTabbedCenterForSnapshot.getHistGraphBrush().setStroke(Color.web(ParamMinMaxRange.ETCO2_COLOR));
				scaleArr = drawTabbedCenterForSnapshot.getParamScaleMap().get(ParamMinMaxRange.ETCO2);
			} else if (paramName.equalsIgnoreCase(ParamMinMaxRange.TEMP1)) {
				symbol = ParamMinMaxRange.TEMP1_LEGEND;
				drawTabbedCenterForSnapshot.getHistGraphBrush().setStroke(Color.web(ParamMinMaxRange.TEMP_COLOR));
				scaleArr = drawTabbedCenterForSnapshot.getParamScaleMap().get(ParamMinMaxRange.TEMP);
			} else if (paramName.equalsIgnoreCase(ParamMinMaxRange.TEMP2)) {
				symbol = ParamMinMaxRange.TEMP2_LEGEND;
				drawTabbedCenterForSnapshot.getHistGraphBrush().setStroke(Color.web(ParamMinMaxRange.TEMP_COLOR));
				scaleArr = drawTabbedCenterForSnapshot.getParamScaleMap().get(ParamMinMaxRange.TEMP);
			}else if (paramName.equalsIgnoreCase(ParamMinMaxRange.BIS))
			{
				symbol = ParamMinMaxRange.BIS_LEGEND;
				drawTabbedCenterForSnapshot.getHistGraphBrush().setStroke(Color.web(ParamMinMaxRange.BIS_COLOR));
				scaleArr = drawTabbedCenterForSnapshot.getParamScaleMap().get(ParamMinMaxRange.BIS);
			}
			else if (paramName.equalsIgnoreCase(ParamMinMaxRange.ETAGENT))
			{
				symbol = ParamMinMaxRange.ETAGENT_LEGEND;
				drawTabbedCenterForSnapshot.getHistGraphBrush().setStroke(Color.web(ParamMinMaxRange.ETAGENT_COLOR));
				scaleArr = drawTabbedCenterForSnapshot.getParamScaleMap().get(ParamMinMaxRange.ETAGENT);
			}
			else if (paramName.equalsIgnoreCase(ParamMinMaxRange.MAC))
			{
				symbol = ParamMinMaxRange.MAC_LEGEND;
				drawTabbedCenterForSnapshot.getHistGraphBrush().setStroke(Color.web(ParamMinMaxRange.MAC_COLOR));
				scaleArr = drawTabbedCenterForSnapshot.getParamScaleMap().get(ParamMinMaxRange.MAC);
			}
			else if (paramName.equalsIgnoreCase(ParamMinMaxRange.IBPMEAN))
			{
				symbol = ParamMinMaxRange.IBPMEAN_LEGEND;
				drawTabbedCenterForSnapshot.getHistGraphBrush().setStroke(Color.web(ParamMinMaxRange.IBP_COLOR));
				scaleArr = drawTabbedCenterForSnapshot.getParamScaleMap().get(ParamMinMaxRange.IBPMEAN);
			}
			else if (paramName.equalsIgnoreCase(ParamMinMaxRange.PPEAK)) {
				symbol = ParamMinMaxRange.PPEAK_LEGEND;
				drawTabbedCenterForSnapshot.getHistGraphBrush().setStroke(Color.web(ParamMinMaxRange.PPEAK_COLOR));
				scaleArr = drawTabbedCenterForSnapshot.getParamScaleMap().get(ParamMinMaxRange.PPEAK);
			} else if (paramName.equalsIgnoreCase(ParamMinMaxRange.PMEAN)) {
				symbol = ParamMinMaxRange.PMEAN_LEGEND;
				drawTabbedCenterForSnapshot.getHistGraphBrush().setStroke(Color.web(ParamMinMaxRange.PMEAN_COLOR));
				scaleArr = drawTabbedCenterForSnapshot.getParamScaleMap().get(ParamMinMaxRange.PMEAN);
			} else if (paramName.equalsIgnoreCase(ParamMinMaxRange.PEEPEXT)) {
				symbol = ParamMinMaxRange.PEEPEXT_LEGEND;
				drawTabbedCenterForSnapshot.getHistGraphBrush().setStroke(Color.web(ParamMinMaxRange.PEEPEXT_COLOR));
				scaleArr = drawTabbedCenterForSnapshot.getParamScaleMap().get(ParamMinMaxRange.PEEPEXT);
			} else if (paramName.equalsIgnoreCase(ParamMinMaxRange.CIRCUITO2)) {
				symbol = ParamMinMaxRange.CIRCUITO2_LEGEND;
				drawTabbedCenterForSnapshot.getHistGraphBrush().setStroke(Color.web(ParamMinMaxRange.CIRCUITO2_COLOR));
				scaleArr = drawTabbedCenterForSnapshot.getParamScaleMap().get(ParamMinMaxRange.CIRCUITO2);
			} else if (paramName.equalsIgnoreCase(ParamMinMaxRange.TVEXP)) {
				symbol = ParamMinMaxRange.TVEXP_LEGEND;
				drawTabbedCenterForSnapshot.getHistGraphBrush().setStroke(Color.web(ParamMinMaxRange.TVEXP_COLOR));
				scaleArr = drawTabbedCenterForSnapshot.getParamScaleMap().get(ParamMinMaxRange.TVEXP);
			} else if (paramName.equalsIgnoreCase(ParamMinMaxRange.MVEXP)) {
				symbol = ParamMinMaxRange.MVEXP_LEGEND;
				drawTabbedCenterForSnapshot.getHistGraphBrush().setStroke(Color.web(ParamMinMaxRange.MVEXP_COLOR));
				scaleArr = drawTabbedCenterForSnapshot.getParamScaleMap().get(ParamMinMaxRange.MVEXP);
			} else if (paramName.equalsIgnoreCase(ParamMinMaxRange.RR)) {
				symbol = ParamMinMaxRange.RR_LEGEND;
				drawTabbedCenterForSnapshot.getHistGraphBrush().setStroke(Color.web(ParamMinMaxRange.RR_COLOR));
				scaleArr = drawTabbedCenterForSnapshot.getParamScaleMap().get(ParamMinMaxRange.RR);
			}

			scalingFactor = graphHeight / scaleArr[0];

			Y1 = graphHeight - (val * scalingFactor);

			if (iterations > 1) {
				Double[] lastPts = lastCoordinatesMap.get(paramName);
				if (lastPts != null)
					if (lastPts[0] < X) {
						drawTabbedCenterForSnapshot.getHistGraphBrush().strokeLine(lastPts[0] + 2, lastPts[1] - 3,
								X - 2, Y1 - 3);
					}

			}

			drawTabbedCenterForSnapshot.getHistGraphBrush().strokeText(symbol, X - 4, Y1);

			lastCoordinatesMap.put(paramName, new Double[] { X - 4, Y1 });

		} catch (Exception e) {
			LOG.error("## Exception occured:", e);
		}
	}

}
