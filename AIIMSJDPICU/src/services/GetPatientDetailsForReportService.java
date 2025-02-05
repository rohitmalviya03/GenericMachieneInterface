/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package services;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.sql.Blob;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

import com.cdac.common.GeneratedEntities.CPBDetails;
import com.cdac.common.GeneratedEntities.Cannulations;
import com.cdac.common.GeneratedEntities.EchoDetails;
import com.cdac.common.GeneratedEntities.Fluidvalue;
import com.cdac.common.GeneratedEntities.IcuPlanEntity;
import com.cdac.common.GeneratedEntities.IntraopAnesthesiarecord;
import com.cdac.common.GeneratedEntities.IntraopEventlog;
import com.cdac.common.GeneratedEntities.IntraopMedicationlog;
import com.cdac.common.GeneratedEntities.IntraopOutput;
import com.cdac.common.GeneratedEntities.IntraopReportFile;
import com.cdac.common.GeneratedEntities.Patient;
import com.cdac.common.GeneratedEntities.Patientcase;
import com.cdac.common.GeneratedEntities.Recoveryroomreadings;
import com.cdac.common.pojoClasses.InvestigationMapperAndValue;
import com.cdac.common.pojoClasses.InvestigationSetValue;
import com.cdac.common.util.DAOFactory;
import com.cdac.framework.SystemConfiguration;
import com.cdac.reports.GeneratePatientReport;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import mediatorPattern.ControllerMediator;
import model.RADetailsModel;
import model.ChartImageModel;
import model.EventLogSession;
import model.GAReportModel;
import model.OtherTestModel;
import model.PatientSession;
import model.TestABGModel;
import model.UserSession;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

public class GetPatientDetailsForReportService extends Service<Void> {

	private static int patientID;
	private static Long caseID;
	private Map<String, Object> patientDetails;

	private static GetPatientDetailsForReportService instance = null;

	public static GetPatientDetailsForReportService getInstance(int patientIDVal, long caseIDVal) {
		if (instance == null) {
			instance = new GetPatientDetailsForReportService();
		}
		patientID = patientIDVal;
		caseID = caseIDVal;
		return instance;
	}

	private GetPatientDetailsForReportService() {
		super();
	}

	public Map<String, Object> getPatientDetails() {
		return patientDetails;
	}

	@Override
	protected Task<Void> createTask() {
		return new Task<Void>() {

			@Override
			protected Void call() throws Exception {

				try {
					patientDetails = DAOFactory.getPatientDetailsReport().getPatientAndCaseDetailsReport(caseID,
							patientID, UserSession.getInstance().getUserWithrRolesForUserAuthentication().getUserID());
					serviceStuff();
				} catch (Exception e) {
					System.out.println(e.getMessage());

					throw e;
				}

				return null;
			}

		};
	}

	private Patient patient;
	private Patientcase patientCase;
	private Map<String, Object> parameters = new HashMap<String, Object>();
	private List<IntraopMedicationlog> lstMedications = new ArrayList<IntraopMedicationlog>();
	private List<IntraopEventlog> lstEvents = new ArrayList<IntraopEventlog>();
	private List<Fluidvalue> lstFluids = new ArrayList<Fluidvalue>();
	private IntraopAnesthesiarecord intraopAnesthesiarecord = new IntraopAnesthesiarecord();
	private Cannulations cannulationRecord = new Cannulations();
	private Recoveryroomreadings recoveryroomreadings = new Recoveryroomreadings();
	private List<IntraopOutput> lstOutputs = new ArrayList<IntraopOutput>();
	private List<GAReportModel> gaReportDetailList = new ArrayList<GAReportModel>();
	private List<RADetailsModel> raReportDetailList = new ArrayList<RADetailsModel>();
	private List<RADetailsModel> maskList = new ArrayList<RADetailsModel>();
	private static final Logger LOG = Logger.getLogger(GetPatientDetailsForReportService.class);
	private List<InvestigationMapperAndValue> testHistoryList;
	private List<GAReportModel> cannulationDetailList = new ArrayList<GAReportModel>();
	private List<GAReportModel> cpbDetailList = new ArrayList<GAReportModel>();
	private CPBDetails cpbRecord = new CPBDetails();
	private List<GAReportModel> echoDetailList = new ArrayList<GAReportModel>();
	private EchoDetails echoRecord = new EchoDetails();
	private IcuPlanEntity icuPlanRecord = new IcuPlanEntity();
	private List<GAReportModel> shiftOutDetailsList = new ArrayList<GAReportModel>();
	String ralPath = "";
	String pdfPath = "";
	String imagePath = "";
	String medicationJasperPath = "";
	String eventsJasperPath = "";
	String raJasperPath = "";
	String gaJasperPath = "";
	String cannJasperPath = "";
	String fluidJasperPath = "";
	String graphJasperPath = "";
	String chartSubreportPath = "";
	String echoSubreportPath = "";
	String pdfFileName = "";
	String csvFileName = "";

	@SuppressWarnings({ "unchecked", "deprecation" })
	private void serviceStuff() {

		parameters = new HashMap<String, Object>();

		String path = "";
		String decodedPath = "";
		try {
			path = SystemConfiguration.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LOG.error("## Exception occured:", e);
		}

		try {
			decodedPath = URLDecoder.decode(path, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LOG.error("## Exception occured:", e);
		}

		decodedPath = (new File(decodedPath)).getParentFile().getPath() + File.separator + "jasper" + File.separator
				+ "IntraOpPatientReport.jrxml";

		ralPath = decodedPath;

		if (ralPath.contains("file:/")) {
			ralPath = ralPath.replaceAll("file:/", "");
		}

		imagePath = ralPath.replaceAll("IntraOpPatientReport.jrxml", "pgmerIcon.png").toString();
		parameters.put("imagePath", imagePath);
		System.out.println(imagePath);

		medicationJasperPath = ralPath.replaceAll("IntraOpPatientReport.jrxml", "IntraOpMedicatioSubReport.jasper");
		parameters.put("medicationJasperPath", medicationJasperPath);

		eventsJasperPath = ralPath.replaceAll("IntraOpPatientReport.jrxml", "IntraOpEventsSubreport.jasper");
		parameters.put("eventsJasperPath", eventsJasperPath);

		raJasperPath = ralPath.replaceAll("IntraOpPatientReport.jrxml", "RAIntraopSubReport.jasper");
		parameters.put("raJasperPath", raJasperPath);

		gaJasperPath = ralPath.replaceAll("IntraOpPatientReport.jrxml", "GAIntraOpSubReport.jasper");
		parameters.put("gaJasperPath", gaJasperPath);

		cannJasperPath = ralPath.replaceAll("IntraOpPatientReport.jrxml", "IntraOpCannulationSubReport.jasper");
		parameters.put("cannJasperPath", cannJasperPath);

		fluidJasperPath = ralPath.replaceAll("IntraOpPatientReport.jrxml", "IntraOpFluidsSubreport.jasper");
		parameters.put("fluidJasperPath", fluidJasperPath);

		// graphJasperPath = ralPath.replaceAll("IntraOpPatientReport.jrxml",
		// "multiAxisSubReport.jasper");
		// parameters.put("graphPath", graphJasperPath);

		chartSubreportPath = ralPath.replaceAll("IntraOpPatientReport.jrxml", "chartSubreport.jasper");
		parameters.put("chartSubreportPath", chartSubreportPath);

		// echoSubreportPath = ralPath.replaceAll("IntraOpPatientReport.jrxml",
		// "IntraOpEchoSubReport.jasper");
		// parameters.put("echoPath", echoSubreportPath);

		System.out.println(echoSubreportPath);
		String AbgTestpath = "";
		AbgTestpath = ralPath.replaceAll("IntraOpPatientReport.jrxml", "testABGSubReport.jasper");
		parameters.put("abgSubreportPath", AbgTestpath);

		String sugarTestpath = "";
		sugarTestpath = ralPath.replaceAll("IntraOpPatientReport.jrxml", "testSugarSubReport.jasper");
		parameters.put("SugarTestSubreportPath", sugarTestpath);

		String ACTTestpath = "";
		ACTTestpath = ralPath.replaceAll("IntraOpPatientReport.jrxml", "testACTSubReport.jasper");
		parameters.put("ACTSubreportPath", ACTTestpath);

		LOG.info("PATH##################"
				+ GeneratePatientReport.class.getResource("GeneratePatientReport.class").getPath()
				+ "PATH##################");
		LOG.info("PATH##################" + GeneratePatientReport.class.getResource("GeneratePatientReport.class")
				.getPath().replace("GeneratePatientReport.class", "") + "PATH##################");

		LOG.info("imagePath$$$$$$$$$$$$$$$" + ralPath + "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
		LOG.info("imagePath$$$$$$$$$$$$$$$" + imagePath);
		LOG.info("medicationJasperPath@@@@@@@@@@@@@@@@@@@@" + medicationJasperPath);
		LOG.info("eventsJasperPath&&&&&&&&&&&&&&&&&&&&&&" + eventsJasperPath);
		LOG.info("raJasperPath****************" + raJasperPath);
		LOG.info("gaJasperPath$$$$$$$$$$$$$$$$$" + gaJasperPath);
		LOG.info("cannJasperPath@@@@@@@@@@@@@@" + cannJasperPath);
		LOG.info("fluidJasperPath&&&&&&&&&&&&&" + fluidJasperPath);

		List<ChartImageModel> imageList = ControllerMediator.getInstance().getMainController()
				.getDrawTabbedCenterForSnapshot().getImageListForReport();

		if (imageList != null && imageList.size() != 0) {
			JRBeanCollectionDataSource imagesListBean = new JRBeanCollectionDataSource(imageList);

			parameters.put("chartImageList", imagesListBean);
		}
		// List<AnethesiaMachineData> anethesiaMachineDataList;

		System.out.println("Success");
		String diagValues = "";
		String procedureValues = "";
		for (Map.Entry<String, Object> entry : patientDetails.entrySet()) {
			if (entry.getKey().equalsIgnoreCase("name")) {
				String value = entry.getValue().toString().trim();
				parameters.put("name", value);
			} else if (entry.getKey().equalsIgnoreCase("cRNumber")) {

				parameters.put("cRNumber", entry.getValue());
			} else if (entry.getKey().equalsIgnoreCase("age")) {
				parameters.put("age", entry.getValue());
			} else if (entry.getKey().equalsIgnoreCase("ageUnit")) {
				parameters.put("ageUnit", entry.getValue());
			} else if (entry.getKey().equalsIgnoreCase("gender")) {
				parameters.put("gender", entry.getValue());
			} else if (entry.getKey().equalsIgnoreCase("height")) {
				parameters.put("height", entry.getValue());
			} else if (entry.getKey().equalsIgnoreCase("weight")) {
				parameters.put("weight", entry.getValue());
			} else if (entry.getKey().equalsIgnoreCase("bmi")) {
				parameters.put("bmi", entry.getValue());
			} else if (entry.getKey().equalsIgnoreCase("bsa")) {
				parameters.put("bsa", entry.getValue());
			} else if (entry.getKey().equalsIgnoreCase("speciality")) {
				parameters.put("specialities", entry.getValue());
			} else if (entry.getKey().equalsIgnoreCase("ot")) {
				if (entry.getValue() != null && !(entry.getValue().toString().trim().equals(""))) {
					parameters.put("ot", entry.getValue());
				}
			} else if (entry.getKey().equalsIgnoreCase("bloodGroup")) {
				if (entry.getValue() != null && !(entry.getValue().toString().trim().equals(""))) {
					parameters.put("bloodGroup", entry.getValue());
				}
			} else if (entry.getKey().equalsIgnoreCase("caseDate")) {
				if (entry.getValue() != null) {
					if (entry.getValue() != null) {
						SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
						String eventDateTime = formatter.format(entry.getValue());
						Date caseDate;
						try {
							caseDate = formatter.parse(eventDateTime);
							parameters.put("caseDate", caseDate);
						} catch (ParseException e1) {

							e1.printStackTrace();
						}

					}
				}
			} else if (entry.getKey().equalsIgnoreCase("plannedProcedureDate")) {
				if (entry.getValue() != null) {
					if (entry.getValue() != null) {
						SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
						String eventDateTime = formatter.format(entry.getValue());
						Date procedureDate;
						try {
							procedureDate = formatter.parse(eventDateTime);
							parameters.put("plannedProcedureDate", procedureDate);
						} catch (ParseException e1) {

							e1.printStackTrace();
						}

					}
				}
			} else if (entry.getKey().equalsIgnoreCase("caseNumber")) {
				if (entry.getValue() != null) {
					String value = entry.getValue().toString().trim();
					parameters.put("caseNo", value);
				}
			} else if (entry.getKey().equalsIgnoreCase("surgeryType")) {
				parameters.put("surgeryType", entry.getValue());
			} else if (entry.getKey().equalsIgnoreCase("surgeons")) {
				if (entry.getValue() != null && !(entry.getValue().toString().trim().equals(""))) {
					parameters.put("surgeons", entry.getValue());
				}
			} else if (entry.getKey().equalsIgnoreCase("anesthesiologists")) {
				if (entry.getValue() != null && !(entry.getValue().toString().trim().equals(""))) {
					parameters.put("anesthesiologists", entry.getValue());
				}
			} else if (entry.getKey().equalsIgnoreCase("diagnosis")) {
				if (entry.getValue() != null && !entry.getValue().toString().trim().equals("")) {
					if (!diagValues.trim().equals("")) {
						diagValues = diagValues + " | " + entry.getValue();
					} else {
						diagValues = diagValues + entry.getValue();
					}
				}

			} else if (entry.getKey().equalsIgnoreCase("diagnosisText")) {
				if (entry.getValue() != null && !entry.getValue().toString().trim().equals("")) {
					if (!diagValues.trim().equals("")) {
						diagValues = diagValues + " | " + entry.getValue();
					} else {
						diagValues = diagValues + entry.getValue();
					}
				}

			} else if (entry.getKey().equalsIgnoreCase("plannedProcedure")) {
				if (entry.getValue() != null && !entry.getValue().toString().trim().equals("")) {
					if (!procedureValues.trim().equals("")) {
						procedureValues = procedureValues + " | " + entry.getValue();
					} else {
						procedureValues = procedureValues + entry.getValue();
					}
				}
			} else if (entry.getKey().equalsIgnoreCase("plannedProcedureText")) {
				if (entry.getValue() != null && !entry.getValue().toString().trim().equals("")) {
					if (!procedureValues.trim().equals("")) {
						procedureValues = procedureValues + " | " + entry.getValue();
					} else {
						procedureValues = procedureValues + entry.getValue();
					}
				}

			} else if (entry.getKey().equalsIgnoreCase("DrugList")) {
				lstMedications = (List<IntraopMedicationlog>) entry.getValue();
				JRBeanCollectionDataSource beancollection = new JRBeanCollectionDataSource(lstMedications);

				parameters.put("medicationList", beancollection);
			} else if (entry.getKey().equalsIgnoreCase("EventsList")) {
				lstEvents = (List<IntraopEventlog>) entry.getValue();

				Date cpbOnTime = null;
				Date cpbOffTime = null;
				Date axcOnTime = null;
				Date axcOffTime = null;
				Date timeIn = null;
				Date handover = null;
				Date shiftOut = null;
				Date skinClosure = null;
				Date inductionTime = null;
				Date incision = null;

				for (IntraopEventlog eventsObj : lstEvents) {
					if (eventsObj.getCustomEventName().trim().equalsIgnoreCase("CPB On")) {
						cpbOnTime = eventsObj.getEventTime();
					} else if (eventsObj.getCustomEventName().trim().equalsIgnoreCase("CPB Off")) {
						cpbOffTime = eventsObj.getEventTime();
					} else if (eventsObj.getCustomEventName().trim().equalsIgnoreCase("AXC On")) {
						axcOnTime = eventsObj.getEventTime();
					} else if (eventsObj.getCustomEventName().trim().equalsIgnoreCase("AXC Off")) {
						axcOffTime = eventsObj.getEventTime();
					} else if (eventsObj.getCustomEventName().trim().equalsIgnoreCase("Time In")) {
						timeIn = eventsObj.getEventTime();
					} else if (eventsObj.getCustomEventName().trim().equalsIgnoreCase("Handover")) {
						handover = eventsObj.getEventTime();
					} else if (eventsObj.getCustomEventName().trim().equalsIgnoreCase("Shift Out")) {
						shiftOut = eventsObj.getEventTime();
					}
					else if (eventsObj.getCustomEventName().trim().equalsIgnoreCase("Skin Closure")) {
						skinClosure = eventsObj.getEventTime();
					}
					else if (eventsObj.getCustomEventName().trim().equalsIgnoreCase("Induction")) {
						inductionTime = eventsObj.getEventTime();
					}
					else if (eventsObj.getCustomEventName().trim().equalsIgnoreCase("Incision")) {
						incision = eventsObj.getEventTime();
					}

				}
				// CPB time
				if (cpbOnTime != null && cpbOffTime != null && (cpbOffTime.getTime() > cpbOnTime.getTime())) {
					long cpbTimeDiff = cpbOffTime.getTime() - cpbOnTime.getTime();
					long diffMinutes = cpbTimeDiff / (60 * 1000) % 60;
					long diffHours = cpbTimeDiff / (60 * 60 * 1000);
					String cpbTime = "";
					if (diffHours < 10) {
						cpbTime = "0" + diffHours + "hr ";
					} else {
						cpbTime = +diffHours + "hr ";
					}
					if (diffMinutes < 10) {
						cpbTime = cpbTime + "0" + diffMinutes + "min";
					} else {
						cpbTime = cpbTime + diffMinutes + "min";
					}
					parameters.put("cpbTime", cpbTime);
					System.out.println(cpbTime);
				}
				// AXC time
				if (axcOnTime != null && axcOffTime != null && (axcOffTime.getTime() > axcOnTime.getTime())) {
					long axcTimeDiff = axcOffTime.getTime() - axcOnTime.getTime();
					long diffMinutes = axcTimeDiff / (60 * 1000) % 60;
					long diffHours = axcTimeDiff / (60 * 60 * 1000);
					String axcTime = "";
					if (diffHours < 10) {
						axcTime = "0" + diffHours + "hr ";
					} else {
						axcTime = diffHours + "hr ";
					}
					if (diffMinutes < 10) {
						axcTime = axcTime + "0" + diffMinutes + "min";
					} else {
						axcTime = axcTime + diffMinutes + "min";
					}
					parameters.put("axcTime", axcTime);
					System.out.println(axcTime);
				}
				// Handover Time
				if (timeIn != null && handover != null && (handover.getTime() > timeIn.getTime())) {
					long handoverTimeDiff = handover.getTime() - timeIn.getTime();
					long diffMinutes = handoverTimeDiff / (60 * 1000) % 60;
					long diffHours = handoverTimeDiff / (60 * 60 * 1000);
					String handoverTime = "";
					if (diffHours < 10) {
						handoverTime = "0" + diffHours + "hr ";
					} else {
						handoverTime = +diffHours + "hr ";
					}
					if (diffMinutes < 10) {
						handoverTime = handoverTime + "0" + diffMinutes + "min";
					} else {
						handoverTime = handoverTime + diffMinutes + "min";
					}
					parameters.put("handoverTime", handoverTime);
					System.out.println(handoverTime);
				}
				// OT time
				if (timeIn != null && shiftOut != null && (shiftOut.getTime() > timeIn.getTime())) {
					long otTimeDiff = shiftOut.getTime() - timeIn.getTime();
					long diffMinutes = otTimeDiff / (60 * 1000) % 60;
					long diffHours = otTimeDiff / (60 * 60 * 1000);
					String otTime = "";
					if (diffHours < 10) {
						otTime = "0" + diffHours + "hr ";
					} else {
						otTime = +diffHours + "hr ";
					}
					if (diffMinutes < 10) {
						otTime = otTime + "0" + diffMinutes + "min";
					} else {
						otTime = otTime + diffMinutes + "min";
					}
					parameters.put("otTime", otTime);
					System.out.println(otTime);
				}
				// Surgery time
				if (handover != null && skinClosure != null && (skinClosure.getTime() > handover.getTime())) {
					long surgeryTimeDiff = skinClosure.getTime() - handover.getTime();
					long diffMinutes = surgeryTimeDiff / (60 * 1000) % 60;
					long diffHours = surgeryTimeDiff / (60 * 60 * 1000);
					String surgeryTime = "";
					if (diffHours < 10) {
						surgeryTime = "0" + diffHours + "hr ";
					} else {
						surgeryTime = +diffHours + "hr ";
					}
					if (diffMinutes < 10) {
						surgeryTime = surgeryTime + "0" + diffMinutes + "min";
					} else {
						surgeryTime = surgeryTime + diffMinutes + "min";
					}
					parameters.put("surgeryTime", surgeryTime);
					System.out.println(surgeryTime);
				}
				// Anesthesia time
				if (inductionTime != null && shiftOut != null && (shiftOut.getTime() > inductionTime.getTime())) {
					long anesthesiaTimeDiff = shiftOut.getTime() - inductionTime.getTime();
					long diffMinutes = anesthesiaTimeDiff / (60 * 1000) % 60;
					long diffHours = anesthesiaTimeDiff / (60 * 60 * 1000);
					String anesthesiaTime = "";
					if (diffHours < 10) {
						anesthesiaTime = "0" + diffHours + "hr ";
					} else {
						anesthesiaTime = +diffHours + "hr ";
					}
					if (diffMinutes < 10) {
						anesthesiaTime = anesthesiaTime + "0" + diffMinutes + "min";
					} else {
						anesthesiaTime = anesthesiaTime + diffMinutes + "min";
					}
					parameters.put("anesthesiaTime", anesthesiaTime);
					System.out.println(anesthesiaTime);
				}
				// Preparation time
				if (handover != null && incision != null && (incision.getTime() > handover.getTime())) {
					long preparationTimeDiff = incision.getTime() - handover.getTime();
					long diffMinutes = preparationTimeDiff / (60 * 1000) % 60;
					long diffHours = preparationTimeDiff / (60 * 60 * 1000);
					String preparationTime = "";
					if (diffHours < 10) {
						preparationTime = "0" + diffHours + "hr ";
					} else {
						preparationTime = +diffHours + "hr ";
					}
					if (diffMinutes < 10) {
						preparationTime = preparationTime + "0" + diffMinutes + "min";
					} else {
						preparationTime = preparationTime + diffMinutes + "min";
					}
					parameters.put("preparationTime", preparationTime);
					System.out.println(preparationTime);
				}


				JRBeanCollectionDataSource beancollection = new JRBeanCollectionDataSource(lstEvents);

				parameters.put("eventList", beancollection);
			}
			else if (entry.getKey().equalsIgnoreCase("FluidsList")) {
				lstFluids = (List<Fluidvalue>) entry.getValue();
				JRBeanCollectionDataSource beancollection = new JRBeanCollectionDataSource(lstFluids);

				parameters.put("fluidList", beancollection);
			}

			else if (entry.getKey().equalsIgnoreCase("OutputList")) {
				lstOutputs = (List<IntraopOutput>) entry.getValue();

				for (IntraopOutput obj : lstOutputs) {
					if (obj.getOutputType().equalsIgnoreCase("blood")) {
						if (obj.getVolume() != null) {
							parameters.put("blood", obj.getVolume().toString().trim() + " ml");
						}
					} else {
						if (obj.getVolume() != null) {
							parameters.put("urine", obj.getVolume().toString().trim() + " ml");
						}
					}

				}
			} else if (entry.getKey().equalsIgnoreCase("lstAnesthesiaRecord")) {
				intraopAnesthesiarecord = new IntraopAnesthesiarecord();
				intraopAnesthesiarecord = (IntraopAnesthesiarecord) entry.getValue();
				if (intraopAnesthesiarecord != null) {

					/*
					 * GA Details Starts
					 * ----------------------------------------------------
					 */

					/* Supraglottic Airway ############################### */

					if ((intraopAnesthesiarecord.getGaSupraglotticSize() != null
							&& !intraopAnesthesiarecord.getGaSupraglotticSize().toString().trim().equals(""))
							|| (intraopAnesthesiarecord.getGaSupraglotticAttempts() != null && !intraopAnesthesiarecord
									.getGaSupraglotticAttempts().toString().trim().equals(""))
							|| (intraopAnesthesiarecord.getGaSupraglotticComment() != null && !intraopAnesthesiarecord
									.getGaSupraglotticComment().toString().trim().equals(""))) {
						gaReportDetailList = new ArrayList<GAReportModel>();
						String GADetails = "";
						String GAComments = "";
						GAReportModel GAObj = new GAReportModel();
						if (intraopAnesthesiarecord.getGaSupraglotticSize() != null
								&& !intraopAnesthesiarecord.getGaSupraglotticSize().toString().trim().equals("")) {

							GADetails = "Size : " + intraopAnesthesiarecord.getGaSupraglotticSize().toString().trim()
									+ " , ";
						} else {
							GADetails = "Size : N/R" + " , ";
						}
						if (intraopAnesthesiarecord.getGaSupraglotticAttempts() != null
								&& !intraopAnesthesiarecord.getGaSupraglotticAttempts().toString().trim().equals("")) {
							GADetails = GADetails + "No of Attempts: "
									+ intraopAnesthesiarecord.getGaSupraglotticAttempts().toString().trim();
						} else {
							GADetails = GADetails + "No of Attempts : N/R";
						}

						if (intraopAnesthesiarecord.getGaSupraglotticComment() != null
								&& !intraopAnesthesiarecord.getGaSupraglotticComment().toString().trim().equals("")) {

							GAComments = "Comments : "
									+ intraopAnesthesiarecord.getGaSupraglotticComment().toString().trim();
						} else {
							GAComments = "Comments : " + "N/R";
						}

						GAObj.setComments(GAComments);
						GAObj.setDetails(GADetails);
						GAObj.setName("Supraglottic Airway:");
						gaReportDetailList.add(GAObj);

					}

					/*
					 * Supraglottic Airway Ends ###############################
					 */

					/* ETT Starts ############################### */

					if ((intraopAnesthesiarecord.getGaEttsize() != null
							&& !intraopAnesthesiarecord.getGaEttsize().toString().trim().equals(""))
							|| (intraopAnesthesiarecord.getGaEttattempts() != null
									&& !intraopAnesthesiarecord.getGaEttattempts().toString().trim().equals(""))
							|| (intraopAnesthesiarecord.getGaEttfixedAt() != null
									&& !intraopAnesthesiarecord.getGaEttfixedAt().toString().trim().equals(""))
							|| (intraopAnesthesiarecord.getGaSupraglotticComment() != null
									&& !intraopAnesthesiarecord.getGaSupraglotticComment().toString().trim().equals(""))
							|| intraopAnesthesiarecord.getcuffedUncuffed() != null) {
						String GADetails = "";
						String GAComments = "";
						GAReportModel GAObj = new GAReportModel();
						if (intraopAnesthesiarecord.getGaEttsize() != null
								&& !intraopAnesthesiarecord.getGaEttsize().toString().trim().equals("")) {

							GADetails = "Size : " + intraopAnesthesiarecord.getGaEttsize().toString().trim() + " , ";
						} else {
							GADetails = "Size : N/R" + " , ";
						}
						if (intraopAnesthesiarecord.getGaEttattempts() != null
								&& !intraopAnesthesiarecord.getGaEttattempts().toString().trim().equals("")) {
							GADetails = GADetails + "No of Attempts: "
									+ intraopAnesthesiarecord.getGaEttattempts().toString().trim() + " , ";
						} else {
							GADetails = GADetails + "No of Attempts : N/R , ";
						}
						if (intraopAnesthesiarecord.getGaEttfixedAt() != null
								&& !intraopAnesthesiarecord.getGaEttfixedAt().toString().trim().equals("")) {
							GADetails = GADetails + "Fixed at: "
									+ intraopAnesthesiarecord.getGaEttfixedAt().toString().trim() + " , ";
						} else {
							GADetails = GADetails + "Fixed at : N/R , ";
						}

						if (intraopAnesthesiarecord.getcuffedUncuffed() != null) {
							GADetails = GADetails + "Cuffed/Uncuffed : "
									+ intraopAnesthesiarecord.getcuffedUncuffed().toString().trim();
						} else {
							GADetails = GADetails + "Cuffed/Uncuffed : N/R ";
						}

						if (intraopAnesthesiarecord.getGaEttcomment() != null
								&& !intraopAnesthesiarecord.getGaEttcomment().toString().trim().equals("")) {

							GAComments = "Comments : " + intraopAnesthesiarecord.getGaEttcomment().toString().trim();
						} else {
							GAComments = "Comments : " + "N/R ";
						}

						GAObj.setComments(GAComments);
						GAObj.setDetails(GADetails);
						GAObj.setName("ETT/DLT:");
						gaReportDetailList.add(GAObj);

					}

					/* ETT Ends ############################### */

					/* Ventilation Starts ############################### */

					if (intraopAnesthesiarecord.getGaVentilation() != null
							&& !intraopAnesthesiarecord.getGaVentilation().toString().trim().equals("")) {
						String GADetails = "";
						String GAComments = "";
						GAReportModel GAObj = new GAReportModel();
						if (intraopAnesthesiarecord.getGaVentilation().toString().trim()
								.equalsIgnoreCase("Controlled")) {
							if (intraopAnesthesiarecord.getGaVentilation() != null
									&& !intraopAnesthesiarecord.getGaVentilation().toString().trim().equals("")) {

								GADetails = "Type : " + intraopAnesthesiarecord.getGaVentilation().toString().trim()
										+ " , ";
							} else {
								GADetails = "Type : N/R" + " , ";
							}
							if (intraopAnesthesiarecord.getGaAirPressure() != null
									&& !intraopAnesthesiarecord.getGaAirPressure().toString().trim().equals("")) {
								GADetails = GADetails + "Airway Pressure : "
										+ intraopAnesthesiarecord.getGaAirPressure().toString().trim() + " , ";
							} else {
								GADetails = GADetails + "Airway Pressure : N/R" + " , ";
							}
							if (intraopAnesthesiarecord.getGaRr() != null
									&& !intraopAnesthesiarecord.getGaRr().toString().trim().equals("")) {
								GADetails = GADetails + "RR : " + intraopAnesthesiarecord.getGaRr().toString().trim()
										+ " , ";
							} else {
								GADetails = GADetails + "RR : N/R" + " , ";
							}
							if (intraopAnesthesiarecord.getGaVt() != null
									&& !intraopAnesthesiarecord.getGaVt().toString().trim().equals("")) {
								GADetails = GADetails + "VT : " + intraopAnesthesiarecord.getGaVt().toString().trim()
										+ " , ";
							} else {
								GADetails = GADetails + "VT : N/R" + " , ";
							}
							if (intraopAnesthesiarecord.getGaIe() != null
									&& !intraopAnesthesiarecord.getGaIe().toString().trim().equals("")) {
								GADetails = GADetails + "I:E : " + intraopAnesthesiarecord.getGaIe().toString().trim()
										+ " , ";
							} else {
								GADetails = GADetails + "I:E : N/R" + " , ";
							}

						} else {
							if (intraopAnesthesiarecord.getGaVentilation() != null
									&& !intraopAnesthesiarecord.getGaVentilation().toString().trim().equals("")) {

								GADetails = "Type : " + intraopAnesthesiarecord.getGaVentilation().toString().trim();

							} else {
								GADetails = "Size : N/R";
							}
						}
						if (intraopAnesthesiarecord.getGaVentilationComment() != null
								&& !intraopAnesthesiarecord.getGaVentilationComment().toString().trim().equals("")) {

							GAComments = "Comments : "
									+ intraopAnesthesiarecord.getGaVentilationComment().toString().trim();
						} else {
							GAComments = "Comments : " + "N/R";
						}

						GAObj.setComments(GAComments);
						GAObj.setDetails(GADetails);
						GAObj.setName("Ventilation:");
						gaReportDetailList.add(GAObj);

					}

					/* Ventilation Ends ############################### */

					/* Mask Airway ############################### */

					if (intraopAnesthesiarecord.getGaMaskComment() != null
							&& !intraopAnesthesiarecord.getGaMaskComment().toString().trim().equals("")) {

						String GADetails = "";
						String GAComments = "";
						GAReportModel GAObj = new GAReportModel();

						GAComments = "Comments : " + intraopAnesthesiarecord.getGaMaskComment().toString().trim();
						GAObj.setComments(GAComments);
						GAObj.setDetails(GADetails);
						GAObj.setName("Mask:");
						gaReportDetailList.add(GAObj);
					} else {

						GAReportModel GAObj = new GAReportModel();
						String GADetails = "";
						GAObj.setComments("Comments : N/R");
						GAObj.setDetails(GADetails);
						GAObj.setName("Mask:");
						gaReportDetailList.add(GAObj);
					}

					/* Mask Airway Ends ############################### */

					/*
					 * GA Details Ends
					 * ----------------------------------------------------
					 */

					/*
					 * RA Details Starts
					 * ----------------------------------------------------
					 */

					/*
					 * RA Spinal Starts
					 * ----------------------------------------------------
					 */

					if ((intraopAnesthesiarecord.getRaSpinalSite() != null
							&& !intraopAnesthesiarecord.getRaSpinalSite().toString().trim().equals(""))
							|| (intraopAnesthesiarecord.getRaSpinalNeedle() != null
									&& !intraopAnesthesiarecord.getRaSpinalNeedle().toString().trim().equals(""))
							|| (intraopAnesthesiarecord.getRaSpinalHsl() != null
									&& !intraopAnesthesiarecord.getRaSpinalHsl().toString().trim().equals(""))) {
						raReportDetailList = new ArrayList<RADetailsModel>();
						String RADetails = "";

						RADetailsModel RAObj = new RADetailsModel();
						if (intraopAnesthesiarecord.getRaSpinalSite() != null
								&& !intraopAnesthesiarecord.getRaSpinalSite().toString().trim().equals("")) {

							RADetails = "Site : " + intraopAnesthesiarecord.getRaSpinalSite().toString().trim() + " , ";
						} else {
							RADetails = "Site : N/R" + " , ";
						}
						if (intraopAnesthesiarecord.getRaSpinalNeedle() != null
								&& !intraopAnesthesiarecord.getRaSpinalNeedle().toString().trim().equals("")) {
							RADetails = RADetails + "Needle : "
									+ intraopAnesthesiarecord.getRaSpinalNeedle().toString().trim() + " , ";
						} else {
							RADetails = RADetails + "Needle : N/R" + " , ";
						}
						if (intraopAnesthesiarecord.getRaSpinalHsl() != null
								&& !intraopAnesthesiarecord.getRaSpinalHsl().toString().trim().equals("")) {
							RADetails = RADetails + "Highest Sensary Level : "
									+ intraopAnesthesiarecord.getRaSpinalHsl().toString().trim();
						} else {
							RADetails = RADetails + "Highest Sensary Level : N/R";
						}

						RAObj.setDetails(RADetails);
						RAObj.setName("Spinal:");
						raReportDetailList.add(RAObj);

					}
					/*
					 * RA Spinal Ends
					 * ----------------------------------------------------
					 */

					/*
					 * RA Epidural Starts
					 * ----------------------------------------------------
					 */

					if ((intraopAnesthesiarecord.getRaEpiduralSite() != null
							&& !intraopAnesthesiarecord.getRaEpiduralSite().toString().trim().equals(""))
							|| (intraopAnesthesiarecord.getRaSpinalNeedle() != null
									&& !intraopAnesthesiarecord.getRaSpinalNeedle().toString().trim().equals(""))
							|| (intraopAnesthesiarecord.getRaSpinalHsl() != null
									&& !intraopAnesthesiarecord.getRaSpinalHsl().toString().trim().equals(""))) {
						String RADetails = "";

						RADetailsModel RAObj = new RADetailsModel();
						if (intraopAnesthesiarecord.getRaEpiduralSite() != null
								&& !intraopAnesthesiarecord.getRaEpiduralSite().toString().trim().equals("")) {

							RADetails = "Site : " + intraopAnesthesiarecord.getRaEpiduralSite().toString().trim()
									+ " , ";
						} else {
							RADetails = "Site : N/R" + " , ";
						}
						if (intraopAnesthesiarecord.getRaEpiduralNeedle() != null
								&& !intraopAnesthesiarecord.getRaEpiduralNeedle().toString().trim().equals("")) {
							RADetails = RADetails + "Needle : "
									+ intraopAnesthesiarecord.getRaEpiduralNeedle().toString().trim() + " , ";
						} else {
							RADetails = RADetails + "Needle : N/R" + " , ";
						}
						if (intraopAnesthesiarecord.getRaEpiduralCfixedAt() != null
								&& !intraopAnesthesiarecord.getRaEpiduralCfixedAt().toString().trim().equals("")) {
							RADetails = RADetails + "Catheter Fixed Value : "
									+ intraopAnesthesiarecord.getRaEpiduralCfixedAt().toString().trim();
						} else {
							RADetails = RADetails + "Catheter Fixed Value : N/R";
						}

						RAObj.setDetails(RADetails);
						RAObj.setName("Epidural:");
						raReportDetailList.add(RAObj);

					}
					/*
					 * RA Spinal Ends
					 * ----------------------------------------------------
					 */

					/*
					 * RA ULB Starts
					 * ----------------------------------------------------
					 */

					if ((intraopAnesthesiarecord.getRaUlbsite() != null
							&& !intraopAnesthesiarecord.getRaUlbsite().toString().trim().equals(""))
							|| (intraopAnesthesiarecord.getRaUlbBpbapproach() != null
									&& !intraopAnesthesiarecord.getRaUlbBpbapproach().toString().trim().equals(""))) {
						String RADetails = "";

						RADetailsModel RAObj = new RADetailsModel();
						if (intraopAnesthesiarecord.getRaUlbsite() != null
								&& !intraopAnesthesiarecord.getRaUlbsite().toString().trim().equals("")) {

							RADetails = "Type : " + intraopAnesthesiarecord.getRaUlbsite().toString().trim() + " , ";
						} else {
							RADetails = "Type : N/R" + " , ";
						}
						if (intraopAnesthesiarecord.getRaUlbsite().toString().trim()
								.equalsIgnoreCase("Peripheral Nerve Blocks")) {
							if (intraopAnesthesiarecord.getRaUlbPnbapproach() != null
									&& !intraopAnesthesiarecord.getRaUlbPnbapproach().toString().trim().equals("")) {

								RADetails = RADetails + "Sub-type : "
										+ intraopAnesthesiarecord.getRaUlbPnbapproach().toString().trim();
							}

						} else if (intraopAnesthesiarecord.getRaUlbsite().toString().trim()
								.equalsIgnoreCase("Brachial Plexus Blocks")) {
							if (intraopAnesthesiarecord.getRaUlbBpbapproach() != null
									&& !intraopAnesthesiarecord.getRaUlbBpbapproach().toString().trim().equals("")) {

								RADetails = RADetails + "Sub-type : "
										+ intraopAnesthesiarecord.getRaUlbBpbapproach().toString().trim();
							}
						} else {
							RADetails = RADetails + "Sub-type : N/R";
						}

						RAObj.setDetails(RADetails);
						RAObj.setName("Upper Limb Block:");
						raReportDetailList.add(RAObj);

					}
					/*
					 * RA ULB Ends
					 * ----------------------------------------------------
					 */

					/*
					 * RA LLB Starts
					 * ----------------------------------------------------
					 */

					if ((intraopAnesthesiarecord.getRaLlbSpnbapproach() != null
							&& !intraopAnesthesiarecord.getRaLlbSpnbapproach().toString().trim().equals(""))
							|| (intraopAnesthesiarecord.getRaLlbsite() != null
									&& !intraopAnesthesiarecord.getRaLlbsite().toString().trim().equals(""))) {
						String RADetails = "";

						RADetailsModel RAObj = new RADetailsModel();
						if (intraopAnesthesiarecord.getRaLlbsite() != null
								&& !intraopAnesthesiarecord.getRaLlbsite().toString().trim().equals("")) {

							RADetails = "Type : " + intraopAnesthesiarecord.getRaLlbsite().toString().trim() + " , ";
						} else {
							RADetails = "Type : N/R" + " , ";
						}
						if (intraopAnesthesiarecord.getRaLlbsite().toString().trim()
								.equalsIgnoreCase("Lumbar Plexus Nerve Blocks")) {
							if (intraopAnesthesiarecord.getRaLlbLpnbapproach() != null
									&& !intraopAnesthesiarecord.getRaLlbLpnbapproach().toString().trim().equals("")) {

								RADetails = RADetails + "Sub-type : "
										+ intraopAnesthesiarecord.getRaLlbLpnbapproach().toString().trim();
							}

						} else if (intraopAnesthesiarecord.getRaLlbsite().toString().trim()
								.equalsIgnoreCase("Sacral Plexus Nerve Blocks")) {
							if (intraopAnesthesiarecord.getRaLlbSpnbapproach() != null
									&& !intraopAnesthesiarecord.getRaLlbSpnbapproach().toString().trim().equals("")) {
								RADetails = RADetails + "Sub-type : "
										+ intraopAnesthesiarecord.getRaLlbSpnbapproach().toString().trim();
							}
						} else {
							RADetails = RADetails + "Sub-type : N/R";
						}

						RAObj.setDetails(RADetails);
						RAObj.setName("Lower Limb Block:");
						raReportDetailList.add(RAObj);

					}
					/*
					 * RA LLB Ends
					 * ----------------------------------------------------
					 */

					/*
					 * RA Details Ends
					 * ----------------------------------------------------
					 */

					/*
					 * Mask Starts
					 * ----------------------------------------------------
					 */

					if ((intraopAnesthesiarecord.getMacComment() != null
							&& !intraopAnesthesiarecord.getMacComment().toString().trim().equals(""))) {
						String maskDetails = "";
						maskList = new ArrayList<RADetailsModel>();
						RADetailsModel maskObj = new RADetailsModel();
						if (intraopAnesthesiarecord.getMacComment() != null
								&& !intraopAnesthesiarecord.getMacComment().toString().trim().equals("")) {

							maskDetails = "Comments : " + intraopAnesthesiarecord.getMacComment().toString().trim();
						} else {
							maskDetails = "Comments : " + " N/R";
						}

						maskObj.setDetails(maskDetails);
						maskObj.setName("Mask Details:");
						maskList.add(maskObj);

					}
					/*
					 * Mask Ends
					 * ----------------------------------------------------
					 */

					if (gaReportDetailList != null && gaReportDetailList.size() != 0) {
						JRBeanCollectionDataSource beancollection = new JRBeanCollectionDataSource(gaReportDetailList);
						parameters.put("gaList", beancollection);
						gaReportDetailList = new ArrayList<GAReportModel>();
					}
					if (raReportDetailList != null && raReportDetailList.size() != 0) {
						JRBeanCollectionDataSource beancollection = new JRBeanCollectionDataSource(raReportDetailList);
						parameters.put("raList", beancollection);
						raReportDetailList = new ArrayList<RADetailsModel>();

					}
					if (maskList != null && maskList.size() != 0) {
						JRBeanCollectionDataSource beancollection = new JRBeanCollectionDataSource(maskList);
						parameters.put("maskList", beancollection);
						maskList = new ArrayList<>();
					}

				}
			}
			else if (entry.getKey().equalsIgnoreCase("lstRecoveryRoom")) {
				recoveryroomreadings = (Recoveryroomreadings) entry.getValue();
				if (recoveryroomreadings != null) {
					shiftOutDetailsList = new ArrayList<GAReportModel>();

					if (recoveryroomreadings.getInVitalsBp() != null
							&& !recoveryroomreadings.getInVitalsBp().toString().trim().equals("")) {
						parameters.put("PACUBp", recoveryroomreadings.getInVitalsBp().toString().trim() + " mmHg");
					}
					if (recoveryroomreadings.getInVitalsPulse() != null
							&& !recoveryroomreadings.getInVitalsPulse().toString().trim().equals("")) {
						parameters.put("PACUPulse", recoveryroomreadings.getInVitalsPulse().toString().trim() + " bpm");
					}
					if (recoveryroomreadings.getInVitalsSpo2() != null
							&& !recoveryroomreadings.getInVitalsSpo2().toString().trim().equals("")) {
						parameters.put("PACUSpo2", recoveryroomreadings.getInVitalsSpo2().toString().trim() + " %");
					}
					if (recoveryroomreadings.getInVitalsResp() != null
							&& !recoveryroomreadings.getInVitalsResp().toString().trim().equals("")) {
						parameters.put("PACUResp", recoveryroomreadings.getInVitalsResp().toString().trim());
					}
					if (recoveryroomreadings.getInVitalsTemp() != null
							&& !recoveryroomreadings.getInVitalsTemp().toString().trim().equals("")) {
						parameters.put("PACUTemp", recoveryroomreadings.getInVitalsTemp().toString().trim() + " �C");
					}
					if (recoveryroomreadings.getRespSupport() != null
							&& !recoveryroomreadings.getRespSupport().toString().trim().equals("")) {
						parameters.put("PACURespSup", recoveryroomreadings.getRespSupport().toString().trim());
					}
					if (recoveryroomreadings.getIvlineInoTropsDetails() != null
							&& !recoveryroomreadings.getIvlineInoTropsDetails().toString().trim().equals("")) {

						GAReportModel details = new GAReportModel();
						details.setDetails(recoveryroomreadings.getIvlineInoTropsDetails().toString().trim());
						details.setName("Inotropes :");
						shiftOutDetailsList.add(details);
						// parameters.put("PACUInoTroops",
						// recoveryroomreadings.getIvlineInoTropsDetails().toString().trim());
					}
					if (recoveryroomreadings.getIvlineBloodDetails() != null
							&& !recoveryroomreadings.getIvlineBloodDetails().toString().trim().equals("")) {
						GAReportModel details = new GAReportModel();
						details.setDetails(recoveryroomreadings.getIvlineBloodDetails().toString().trim());
						details.setName("Blood :");
						shiftOutDetailsList.add(details);
						// parameters.put("PACUBlood",
						// recoveryroomreadings.getIvlineBloodDetails().toString().trim());
					}
					if (recoveryroomreadings.getIvlineIvfluidsDetails() != null
							&& !recoveryroomreadings.getIvlineIvfluidsDetails().toString().trim().equals("")) {
						GAReportModel details = new GAReportModel();
						details.setDetails(recoveryroomreadings.getIvlineIvfluidsDetails().toString().trim());
						details.setName("I/V Fluids :");
						shiftOutDetailsList.add(details);
						// parameters.put("PACUIVFluids",
						// recoveryroomreadings.getIvlineIvfluidsDetails().toString().trim());
					}
					if (recoveryroomreadings.getIvlineInfusionDetails() != null
							&& !recoveryroomreadings.getIvlineInfusionDetails().toString().trim().equals("")) {
						GAReportModel details = new GAReportModel();
						details.setDetails(recoveryroomreadings.getIvlineInfusionDetails().toString().trim());
						details.setName("Infusion :");
						shiftOutDetailsList.add(details);
						// parameters.put("PACUInfusion",
						// recoveryroomreadings.getIvlineInfusionDetails().toString().trim());
					}
					if (recoveryroomreadings.getAldreteScoreAtShiftingToPacu() != null
							&& !recoveryroomreadings.getAldreteScoreAtShiftingToPacu().toString().trim().equals("")) {

						parameters.put("PACUScore",
								recoveryroomreadings.getAldreteScoreAtShiftingToPacu().toString().trim());
					}
					if (recoveryroomreadings.getPacuTime() != null
							&& !recoveryroomreadings.getPacuTime().toString().trim().equals("")) {
						parameters.put("PACUDate", recoveryroomreadings.getPacuTime().toString().trim());
					}

					if (recoveryroomreadings.getOutVitalsBp() != null
							&& !recoveryroomreadings.getOutVitalsBp().toString().trim().equals("")) {
						parameters.put("OUTBp", recoveryroomreadings.getOutVitalsBp().toString().trim() + " mmHg");
					}
					if (recoveryroomreadings.getOutVitalsPulse() != null
							&& !recoveryroomreadings.getOutVitalsPulse().toString().trim().equals("")) {
						parameters.put("OUTPulse", recoveryroomreadings.getOutVitalsPulse().toString().trim() + " bpm");
					}
					if (recoveryroomreadings.getOutVitalsSpo2() != null
							&& !recoveryroomreadings.getOutVitalsSpo2().toString().trim().equals("")) {
						parameters.put("OUTSpo2", recoveryroomreadings.getOutVitalsSpo2().toString().trim() + " %");
					}
					if (recoveryroomreadings.getOutVitalsResp() != null
							&& !recoveryroomreadings.getOutVitalsResp().toString().trim().equals("")) {
						parameters.put("OUTResp", recoveryroomreadings.getOutVitalsResp().toString().trim());
					}
					if (recoveryroomreadings.getOutVitalsTemp() != null
							&& !recoveryroomreadings.getOutVitalsTemp().toString().trim().equals("")) {
						parameters.put("OUTTemp", recoveryroomreadings.getOutVitalsTemp().toString().trim() + " �C");
					}
					if (recoveryroomreadings.getSpecialInstructionsDetails() != null
							&& !recoveryroomreadings.getSpecialInstructionsDetails().toString().trim().equals("")) {
						GAReportModel details = new GAReportModel();
						details.setDetails(recoveryroomreadings.getSpecialInstructionsDetails().toString().trim());
						details.setName("Instructions :");
						shiftOutDetailsList.add(details);
						// parameters.put("OUTInstuction",
						// recoveryroomreadings.getSpecialInstructionsDetails().toString().trim());
					}
					// if (recoveryroomreadings.getInvestigationDetails() !=
					// null
					// &&
					// !recoveryroomreadings.getInvestigationDetails().toString().trim().equals(""))
					// {
					// parameters.put("OUTInstuction",
					// recoveryroomreadings.getInvestigationDetails().toString().trim());
					// }
					if (recoveryroomreadings.getConsultationDetails() != null
							&& !recoveryroomreadings.getConsultationDetails().toString().trim().equals("")) {
						parameters.put("OUTInvestigation",
								recoveryroomreadings.getConsultationDetails().toString().trim());
					}
					if (recoveryroomreadings.getDrugsDetails() != null
							&& !recoveryroomreadings.getDrugsDetails().toString().trim().equals("")) {
						parameters.put("OUTDrugs", recoveryroomreadings.getDrugsDetails().toString().trim());
					}
					if (recoveryroomreadings.getComplicationsDetails() != null
							&& !recoveryroomreadings.getComplicationsDetails().toString().trim().equals("")) {
						parameters.put("OUTCmplication",
								recoveryroomreadings.getComplicationsDetails().toString().trim());
					}
					if (recoveryroomreadings.getModifiedAldreteScoreAtDischarge() != null && !recoveryroomreadings
							.getModifiedAldreteScoreAtDischarge().toString().trim().equals("")) {
						parameters.put("OUTScore",
								recoveryroomreadings.getModifiedAldreteScoreAtDischarge().toString().trim());
					}
					if (recoveryroomreadings.getShiftingOutTime() != null
							&& !recoveryroomreadings.getShiftingOutTime().toString().trim().equals("")) {
						parameters.put("OUTDate", recoveryroomreadings.getShiftingOutTime().toString().trim());
					}
					if(recoveryroomreadings.getAreaUnderCurve()!=null && recoveryroomreadings.getLowerRange()!=null && recoveryroomreadings.getUpperRange()!=null)
                    {
                        parameters.put("AreaUnderCurve", recoveryroomreadings.getAreaUnderCurve().toString() + "(" + recoveryroomreadings.getUpperRange().toString().split("\\.")[0] + "/" + recoveryroomreadings.getLowerRange().toString().split("\\.")[0] + ")");
                    }
					if (shiftOutDetailsList != null && shiftOutDetailsList.size() != 0) {
						JRBeanCollectionDataSource beancollection = new JRBeanCollectionDataSource(shiftOutDetailsList);
						parameters.put("shiftOUTDetails", beancollection);
						shiftOutDetailsList = new ArrayList<GAReportModel>();
					}
				}

			}
			// ICU Plan
			else if (entry.getKey().equalsIgnoreCase("icuPlanDetails")) {
				icuPlanRecord = (IcuPlanEntity) entry.getValue();
				if (icuPlanRecord != null) {

					if (icuPlanRecord.getExtubation() != null
							&& !icuPlanRecord.getExtubation().toString().trim().equals("")) {
						parameters.put("extubation", icuPlanRecord.getExtubation().toString().trim());
					}
					if (icuPlanRecord.getPostExtubation() != null
							&& !icuPlanRecord.getPostExtubation().toString().trim().equals("")) {
						parameters.put("postExtubation", icuPlanRecord.getPostExtubation().toString().trim());
					}
					if (icuPlanRecord.getSedation() != null
							&& !icuPlanRecord.getSedation().toString().trim().equals("")) {
						parameters.put("sedation", icuPlanRecord.getSedation().toString().trim());
					}
					if (icuPlanRecord.getAnalgesia() != null
							&& !icuPlanRecord.getAnalgesia().toString().trim().equals("")) {
						parameters.put("analgesia", icuPlanRecord.getAnalgesia().toString().trim());
					}
					if (icuPlanRecord.getParalysis() != null
							&& !icuPlanRecord.getParalysis().toString().trim().equals("")) {
						parameters.put("paralysis", icuPlanRecord.getParalysis().toString().trim());
					}
					if (icuPlanRecord.getMao() != null
							&& !icuPlanRecord.getMao().toString().trim().equals("")) {
						parameters.put("mao", icuPlanRecord.getMao().toString().trim().split("\\.")[0] + " mmHg");
					}
					if (icuPlanRecord.getCvp() != null
							&& !icuPlanRecord.getCvp().toString().trim().equals("")) {
						parameters.put("cvp", icuPlanRecord.getCvp().toString().trim().split("\\.")[0] + " mmHg");
					}
					if (icuPlanRecord.getHr() != null
							&& !icuPlanRecord.getHr().toString().trim().equals("")) {
						parameters.put("hr", icuPlanRecord.getHr().toString().trim().split("\\.")[0] + " /min");
					}
					if (icuPlanRecord.getSpO2() != null
							&& !icuPlanRecord.getSpO2().toString().trim().equals("")) {
						parameters.put("spo2", icuPlanRecord.getSpO2().toString().trim().split("\\.")[0] + " %");
					}
					if (icuPlanRecord.getRlRate() != null
							&& !icuPlanRecord.getRlRate().toString().trim().equals("")) {
						parameters.put("rlRate", icuPlanRecord.getRlRate().toString().trim() + " ml/hr");
					}
					if (icuPlanRecord.getDnsRate() != null
							&& !icuPlanRecord.getDnsRate().toString().trim().equals("")) {
						parameters.put("dnsRate", icuPlanRecord.getDnsRate().toString().trim() + " ml/hr");
					}
					if (icuPlanRecord.getDrl() != null
							&& !icuPlanRecord.getDrl().toString().trim().equals("")) {
						parameters.put("drl", icuPlanRecord.getDrl().toString().trim());
					}
					if (icuPlanRecord.getInvestigations() != null
							&& !icuPlanRecord.getInvestigations().toString().trim().equals("")) {
						parameters.put("investigations", icuPlanRecord.getInvestigations().toString().trim());
					}
					if (icuPlanRecord.getNurseName() != null
							&& !icuPlanRecord.getNurseName().toString().trim().equals("")) {
						parameters.put("nurseName", icuPlanRecord.getNurseName().toString().trim());
					}
				}

			}
			else if (entry.getKey().equalsIgnoreCase("cannulations")) {
				cannulationRecord = (Cannulations) entry.getValue();

				if (cannulationRecord != null) {

					cannulationDetailList = new ArrayList<GAReportModel>();

					///// Cannulation Central Starts Here

					if (cannulationRecord.getSite() != null || cannulationRecord.getVein() != null
							|| cannulationRecord.getVeinDetails() != null
							|| cannulationRecord.getInsertionSize() != null
							|| cannulationRecord.getInsertionLength() != null || cannulationRecord.getDetails() != null
							|| cannulationRecord.getCentralUltrasoundBlind() != null || cannulationRecord.getInsertedBy() != null) {
						GAReportModel centralLineReportDetails = new GAReportModel();
						String CentralCanulaDetails = "";
						if (cannulationRecord.getSite() != null
								&& !cannulationRecord.getSite().toString().trim().equals("")) {
							CentralCanulaDetails = "Site : " + cannulationRecord.getSite().toString().trim() + " , ";

						} else {
							CentralCanulaDetails = "Site : N/R" + " , ";
						}
						if (cannulationRecord.getCentralUltrasoundBlind() != null
								&& !cannulationRecord.getCentralUltrasoundBlind().toString().trim().equals("")) {
							CentralCanulaDetails = CentralCanulaDetails + "Ultrasound/Blind : "
									+ cannulationRecord.getCentralUltrasoundBlind().toString().trim() + " , ";

						} else {
							CentralCanulaDetails = CentralCanulaDetails + "Ultrasound/Blind : N/R" + " , ";
						}
						if (cannulationRecord.getVein() != null
								&& !cannulationRecord.getVein().toString().trim().equals("")) {
							CentralCanulaDetails = CentralCanulaDetails + " Vein : "
									+ cannulationRecord.getVein().toString().trim() + " , ";
						} else {
							CentralCanulaDetails = CentralCanulaDetails + " Vein : N/R" + " , ";
						}
						if (cannulationRecord.getVeinDetails() != null
								&& !cannulationRecord.getVeinDetails().toString().trim().equals("")) {
							CentralCanulaDetails = CentralCanulaDetails + " Vein Details : "
									+ cannulationRecord.getVeinDetails().toString().trim() + " , ";
						} else {
							CentralCanulaDetails = CentralCanulaDetails + " Vein Details : N/R" + " , ";
						}
						if (cannulationRecord.getInsertionSize() != null
								&& !cannulationRecord.getInsertionSize().toString().trim().equals("")) {
							CentralCanulaDetails = CentralCanulaDetails + " Size : "
									+ cannulationRecord.getInsertionSize().toString().trim() + " , ";
						} else {
							CentralCanulaDetails = CentralCanulaDetails + " Size : N/R" + " , ";
						}
						if (cannulationRecord.getInsertionLength() != null
								&& !cannulationRecord.getInsertionLength().toString().trim().equals("")) {
							CentralCanulaDetails = CentralCanulaDetails + " Length : "
									+ cannulationRecord.getInsertionLength().toString().trim();
						} else {
							CentralCanulaDetails = CentralCanulaDetails + " Length : N/R";
						}
						if (cannulationRecord.getInsertedBy() != null
								&& !cannulationRecord.getInsertedBy().toString().trim().equals("")) {
							CentralCanulaDetails = CentralCanulaDetails + " Inserted By : "
									+ cannulationRecord.getInsertedBy().toString().trim();
						} else {
							CentralCanulaDetails = CentralCanulaDetails + " Inserted By : N/R";
						}
						centralLineReportDetails.setDetails(CentralCanulaDetails);
						centralLineReportDetails.setName("Central Line:");
						String centralComments = "";
						if (cannulationRecord.getDetails() != null
								&& !cannulationRecord.getDetails().toString().trim().equals("")) {
							centralComments = "Comments : " + cannulationRecord.getDetails().toString().trim();

						} else {
							centralComments = "Comments : " + " N/R";
						}
						centralLineReportDetails.setComments(centralComments);
						cannulationDetailList.add(centralLineReportDetails);
					}
					///// Cannulation Central Ends Here

					//// Cannulation Artery Starts Here
					if (cannulationRecord.getArterySite() != null || cannulationRecord.getArterySize() != null
							|| cannulationRecord.getArteryDetails() != null
							|| cannulationRecord.getArterialUltrasoundBlind() != null) {
						GAReportModel arteryReportDetails = new GAReportModel();
						String arteryDetails = "";
						String arteryComments = "";
						if (cannulationRecord.getArterySite() != null
								&& !cannulationRecord.getArterySite().toString().trim().equals("")) {
							arteryDetails = "Site : " + cannulationRecord.getArterySite().toString().trim() + " , ";

						} else {
							arteryDetails = "Site : N/R" + " , ";
						}
						if (cannulationRecord.getArterySize() != null
								&& !cannulationRecord.getArterySize().toString().trim().equals("")) {
							arteryDetails = arteryDetails + "Size : "
									+ cannulationRecord.getArterySize().toString().trim() + " , ";

						} else {
							arteryDetails = arteryDetails + " Size : N/R , ";
						}
						if (cannulationRecord.getArterialUltrasoundBlind() != null
								&& !cannulationRecord.getArterialUltrasoundBlind().toString().trim().equals("")) {
							arteryDetails = arteryDetails + "Ultrasound/Blind : "
									+ cannulationRecord.getArterialUltrasoundBlind().toString().trim() + " , ";

						} else {
							arteryDetails = arteryDetails + "Ultrasound/Blind : N/R" + " , ";
						}

						if (cannulationRecord.getArteryDetails() != null
								&& !cannulationRecord.getArteryDetails().toString().trim().equals("")) {
							arteryComments = "Comments : " + cannulationRecord.getArteryDetails().toString().trim();

						} else {
							arteryComments = "Comments : " + " N/R";
						}
						arteryReportDetails.setDetails(arteryDetails);
						arteryReportDetails.setName("Arterial Line:");
						arteryReportDetails.setComments(arteryComments);
						cannulationDetailList.add(arteryReportDetails);
					}
					//// Cannulation Artery Ends Here

					//// Peripheral Artery Starts Here
					if (cannulationRecord.getPeripheralSite() != null || cannulationRecord.getPeripheralSize() != null
							|| cannulationRecord.getPeripheralDetails() != null) {
						GAReportModel peripheralReportDetails = new GAReportModel();
						String peripheralDetails = "";
						String peripheralComments = "";
						if (cannulationRecord.getPeripheralSite() != null
								&& !cannulationRecord.getPeripheralSite().toString().trim().equals("")) {
							peripheralDetails = "Site : " + cannulationRecord.getPeripheralSite().toString().trim()
									+ " , ";

						} else {
							peripheralDetails = "Site : N/R" + " , ";
						}
						if (cannulationRecord.getPeripheralSize() != null
								&& !cannulationRecord.getPeripheralSize().toString().trim().equals("")) {
							peripheralDetails = peripheralDetails + " Size : "
									+ cannulationRecord.getPeripheralSize().toString().trim() + " , ";

						} else {
							peripheralDetails = peripheralDetails + " Size : N/R , ";
						}

						if (cannulationRecord.getPeripheralUltrasoundBlind() != null
								&& !cannulationRecord.getPeripheralUltrasoundBlind().toString().trim().equals("")) {
							peripheralDetails = peripheralDetails + "Ultrasound/Blind : "
									+ cannulationRecord.getPeripheralUltrasoundBlind().toString().trim() + " , ";

						} else {
							peripheralDetails = peripheralDetails + "Ultrasound/Blind : N/R" + " , ";
						}
						if (cannulationRecord.getPeripheralDetails() != null
								&& !cannulationRecord.getPeripheralDetails().toString().trim().equals("")) {
							peripheralComments = "Comments : "
									+ cannulationRecord.getPeripheralDetails().toString().trim();

						} else {
							peripheralComments = "Comments : " + " N/R";
						}
						peripheralReportDetails.setDetails(peripheralDetails);
						peripheralReportDetails.setName("Peripheral:");
						peripheralReportDetails.setComments(peripheralComments);
						cannulationDetailList.add(peripheralReportDetails);
					}
					//// Peripheral Artery Ends Here

					//// Pulmonary Artery Starts Here
					if (cannulationRecord.getPulmonarySite() != null || cannulationRecord.getPulmonarySize() != null
							|| cannulationRecord.getPulmonaryDetails() != null) {
						GAReportModel pulmonaryReportDetails = new GAReportModel();
						String pulmonaryDetails = "";
						String pulmonaryComments = "";
						if (cannulationRecord.getPulmonarySite() != null
								&& !cannulationRecord.getPulmonarySite().toString().trim().equals("")) {
							pulmonaryDetails = "Site : " + cannulationRecord.getPulmonarySite().toString().trim()
									+ " , ";

						} else {
							pulmonaryDetails = "Site : N/R" + " , ";
						}
						if (cannulationRecord.getPulmonarySize() != null
								&& !cannulationRecord.getPulmonarySize().toString().trim().equals("")) {
							pulmonaryDetails = pulmonaryDetails + " Size : "
									+ cannulationRecord.getPulmonarySize().toString().trim();

						} else {
							pulmonaryDetails = pulmonaryDetails + " Size : N/R";
						}
						if (cannulationRecord.getPulmonaryDetails() != null
								&& !cannulationRecord.getPulmonaryDetails().toString().trim().equals("")) {
							pulmonaryComments = "Comments : "
									+ cannulationRecord.getPulmonaryDetails().toString().trim();

						} else {
							pulmonaryComments = "Comments : " + " N/R";
						}
						pulmonaryReportDetails.setDetails(pulmonaryDetails);
						pulmonaryReportDetails.setName("Pulmonary Artery:");
						pulmonaryReportDetails.setComments(pulmonaryComments);
						cannulationDetailList.add(pulmonaryReportDetails);
					}
					//// Pulmonary Artery Ends Here

					if (cannulationDetailList != null && cannulationDetailList.size() != 0) {
						JRBeanCollectionDataSource beancollection = new JRBeanCollectionDataSource(
								cannulationDetailList);
						parameters.put("cannulationList", beancollection);
						cannulationDetailList = new ArrayList<GAReportModel>();
					}
					if (cannulationRecord.getPatientPosition() != null
							&& !cannulationRecord.getPatientPosition().toString().trim().equals("")) {
						parameters.put("patientPostion", cannulationRecord.getPatientPosition().toString().trim());
					}

					if (cannulationRecord.getComplications() != null
							&& !cannulationRecord.getComplications().toString().trim().equals("")) {
						parameters.put("cannulationComplications",
								cannulationRecord.getComplications().toString().trim());
					}

				}
			} else if (entry.getKey().equalsIgnoreCase("cpbDetails")) {
				cpbRecord = (CPBDetails) entry.getValue();

				if (cpbRecord != null) {
					cpbDetailList = new ArrayList<GAReportModel>();

					///// Prime Details Starts here
					if ((cpbRecord.getPrimeAlbumin() != null
							&& !cpbRecord.getPrimeAlbumin().toString().trim().equals(""))
							|| (cpbRecord.getPrimeBlood() != null
									&& !cpbRecord.getPrimeBlood().toString().trim().equals(""))
							|| (cpbRecord.getPrimeCaCl2() != null
									&& !cpbRecord.getPrimeCaCl2().toString().trim().equals(""))
							|| (cpbRecord.getPrimeHeparin() != null
									&& !cpbRecord.getPrimeHeparin().toString().trim().equals(""))
							|| (cpbRecord.getPrimeMannitol() != null
									&& !cpbRecord.getPrimeMannitol().toString().trim().equals(""))
							|| (cpbRecord.getPrimeNaHCO3() != null
									&& !cpbRecord.getPrimeNaHCO3().toString().trim().equals(""))
							|| (cpbRecord.getPrimeNS() != null && !cpbRecord.getPrimeNS().toString().trim().equals(""))
							|| (cpbRecord.getPrimeDetails() != null
									&& !cpbRecord.getPrimeDetails().toString().trim().equals(""))) {

						GAReportModel primeDetailsOBJ = new GAReportModel();
						String primeDetails = "";
						String primeComments = "";

						if (cpbRecord.getPrimeNS() != null && !cpbRecord.getPrimeNS().toString().trim().equals("")) {
							primeDetails = "Plasma-Lyte : " + cpbRecord.getPrimeNS().toString().trim() + " ml , ";

						} else {
							primeDetails = "Plasma-Lyte : N/R" + " , ";
						}
						if (cpbRecord.getPrimeBlood() != null
								&& !cpbRecord.getPrimeBlood().toString().trim().equals("")) {
							primeDetails = primeDetails + " Blood : " + cpbRecord.getPrimeBlood().toString().trim()
									+ " ml , ";

						} else {
							primeDetails = primeDetails + " Blood : N/R" + " , ";
						}
						if (cpbRecord.getPrimeAlbumin() != null
								&& !cpbRecord.getPrimeAlbumin().toString().trim().equals("")) {
							primeDetails = primeDetails + " Albumin : " + cpbRecord.getPrimeAlbumin().toString().trim()
									+ " ml , ";

						} else {
							primeDetails = primeDetails + " Albumin : N/R" + " , ";
						}
						if (cpbRecord.getPrimeMannitol() != null
								&& !cpbRecord.getPrimeMannitol().toString().trim().equals("")) {
							primeDetails = primeDetails + " Mannitol : "
									+ cpbRecord.getPrimeMannitol().toString().trim() + " ml , ";

						} else {
							primeDetails = primeDetails + " Mannitol : N/R" + " , ";
						}
						if (cpbRecord.getPrimeNaHCO3() != null
								&& !cpbRecord.getPrimeNaHCO3().toString().trim().equals("")) {
							primeDetails = primeDetails + " NaHCO3 : " + cpbRecord.getPrimeNaHCO3().toString().trim()
									+ " ml , ";

						} else {
							primeDetails = primeDetails + " NaHCO3 : N/R" + " , ";
						}
						if (cpbRecord.getPrimeCaCl2() != null
								&& !cpbRecord.getPrimeCaCl2().toString().trim().equals("")) {
							primeDetails = primeDetails + " CaCl2 : " + cpbRecord.getPrimeCaCl2().toString().trim()
									+ " ml , ";

						} else {
							primeDetails = primeDetails + " CaCl2 : N/R" + " , ";
						}
						if (cpbRecord.getPrimeHeparin() != null
								&& !cpbRecord.getPrimeHeparin().toString().trim().equals("")) {
							primeDetails = primeDetails + " Heparin : " + cpbRecord.getPrimeHeparin().toString().trim()
									+ " ml , ";

						} else {
							primeDetails = primeDetails + " Heparin : N/R";
						}

						if (cpbRecord.getPrimeDetails() != null
								&& !cpbRecord.getPrimeDetails().toString().trim().equals("")) {
							primeComments = " Details : " + cpbRecord.getPrimeDetails().toString().trim();

						} else {
							primeComments = " Details : N/R";
						}

						primeDetailsOBJ.setComments(primeComments);
						primeDetailsOBJ.setDetails(primeDetails);
						primeDetailsOBJ.setName("Prime :");
						cpbDetailList.add(primeDetailsOBJ);

					}

					///// Prime Details Ends here

					///// Pre Details Starts here
					if ((cpbRecord.getPreBlood() != null && !cpbRecord.getPreBlood().toString().trim().equals(""))
							|| (cpbRecord.getPreCrystalloidCPG() != null
									&& !cpbRecord.getPreCrystalloidCPG().toString().trim().equals(""))
							|| (cpbRecord.getPreDetails() != null
									&& !cpbRecord.getPreDetails().toString().trim().equals(""))
							|| (cpbRecord.getPreNaHCO3() != null
									&& !cpbRecord.getPreNaHCO3().toString().trim().equals(""))
							|| (cpbRecord.getPreNS() != null && !cpbRecord.getPreNS().toString().trim().equals(""))
							|| (cpbRecord.getPrePrime() != null
									&& !cpbRecord.getPrePrime().toString().trim().equals(""))
							|| (cpbRecord.getPreRL() != null && !cpbRecord.getPreRL().toString().trim().equals(""))) {

						GAReportModel preDetailsOBJ = new GAReportModel();
						String preDetails = "";
						String preComments = "";

						if (cpbRecord.getPrePrime() != null && !cpbRecord.getPrePrime().toString().trim().equals("")) {
							preDetails = "Prime : " + cpbRecord.getPrePrime().toString().trim() + " ml , ";

						} else {
							preDetails = "Prime : N/R" + " , ";
						}
						if (cpbRecord.getPreCrystalloidCPG() != null
								&& !cpbRecord.getPreCrystalloidCPG().toString().trim().equals("")) {
							preDetails = preDetails + " Crystalloid CPG : "
									+ cpbRecord.getPreCrystalloidCPG().toString().trim() + " ml , ";

						} else {
							preDetails = preDetails + " Crystalloid CPG : N/R" + " , ";
						}
						if (cpbRecord.getPreNS() != null && !cpbRecord.getPreNS().toString().trim().equals("")) {
							preDetails = preDetails + " NS : " + cpbRecord.getPreNS().toString().trim() + " ml , ";

						} else {
							preDetails = preDetails + " NS : N/R" + " , ";
						}
						if (cpbRecord.getPreRL() != null && !cpbRecord.getPreRL().toString().trim().equals("")) {
							preDetails = preDetails + " RL : " + cpbRecord.getPreRL().toString().trim() + " ml , ";

						} else {
							preDetails = preDetails + " RL : N/R" + " , ";
						}
						if (cpbRecord.getPreBlood() != null && !cpbRecord.getPreBlood().toString().trim().equals("")) {
							preDetails = preDetails + " Blood : " + cpbRecord.getPreBlood().toString().trim()
									+ " ml , ";

						} else {
							preDetails = preDetails + " Blood : N/R" + " , ";
						}
						if (cpbRecord.getPreNaHCO3() != null
								&& !cpbRecord.getPreNaHCO3().toString().trim().equals("")) {
							preDetails = preDetails + " NaHCO3 : " + cpbRecord.getPreNaHCO3().toString().trim()
									+ " ml ";

						} else {
							preDetails = preDetails + " NaHCO3 : N/R";
						}

						if (cpbRecord.getPreDetails() != null
								&& !cpbRecord.getPreDetails().toString().trim().equals("")) {
							preComments = " Details : " + cpbRecord.getPreDetails().toString().trim();

						} else {
							preComments = " Details : N/R";
						}

						preDetailsOBJ.setComments(preComments);
						preDetailsOBJ.setDetails(preDetails);
						preDetailsOBJ.setName("Pre :");
						cpbDetailList.add(preDetailsOBJ);

					}

					///// Pre Details Ends here

					///// Post Details Starts here
					if ((cpbRecord.getPostALB() != null && !cpbRecord.getPostALB().toString().trim().equals(""))
							|| (cpbRecord.getPostBlood() != null
									&& !cpbRecord.getPostBlood().toString().trim().equals(""))
							|| (cpbRecord.getPostDetails() != null
									&& !cpbRecord.getPostDetails().toString().trim().equals(""))
							|| (cpbRecord.getPostFFP() != null && !cpbRecord.getPostFFP().toString().trim().equals(""))
							|| (cpbRecord.getPostIVF() != null && !cpbRecord.getPostIVF().toString().trim().equals(""))
							|| (cpbRecord.getPostNetBalance() != null
									&& !cpbRecord.getPostNetBalance().toString().trim().equals(""))
							|| (cpbRecord.getPostPC() != null && !cpbRecord.getPostPC().toString().trim().equals(""))) {

						GAReportModel postDetailsOBJ = new GAReportModel();
						String postDetails = "";
						String postComments = "";

						if (cpbRecord.getPostIVF() != null && !cpbRecord.getPostIVF().toString().trim().equals("")) {
							postDetails = "IVF : " + cpbRecord.getPostIVF().toString().trim() + " ml , ";

						} else {
							postDetails = "IVF : N/R" + " , ";
						}
						if (cpbRecord.getPostBlood() != null
								&& !cpbRecord.getPostBlood().toString().trim().equals("")) {
							postDetails = postDetails + " Blood : " + cpbRecord.getPostBlood().toString().trim()
									+ " ml , ";

						} else {
							postDetails = postDetails + " Blood : N/R" + " , ";
						}
						if (cpbRecord.getPostFFP() != null && !cpbRecord.getPostFFP().toString().trim().equals("")) {
							postDetails = postDetails + " FFP : " + cpbRecord.getPostFFP().toString().trim() + " ml , ";

						} else {
							postDetails = postDetails + " FFP : N/R" + " , ";
						}
						if (cpbRecord.getPostPC() != null && !cpbRecord.getPostPC().toString().trim().equals("")) {
							postDetails = postDetails + " PC : " + cpbRecord.getPostPC().toString().trim() + " ml , ";

						} else {
							postDetails = postDetails + " PC : N/R" + " , ";
						}
						if (cpbRecord.getPostALB() != null && !cpbRecord.getPostALB().toString().trim().equals("")) {
							postDetails = postDetails + " ALB : " + cpbRecord.getPostALB().toString().trim() + " ml , ";

						} else {
							postDetails = postDetails + " ALB : N/R" + " , ";
						}
						if (cpbRecord.getPostNetBalance() != null
								&& !cpbRecord.getPostNetBalance().toString().trim().equals("")) {
							postDetails = postDetails + " Net Balance : "
									+ cpbRecord.getPostNetBalance().toString().trim() + " ml ";

						} else {
							postDetails = postDetails + " Net Balance : N/R";
						}

						if (cpbRecord.getPostDetails() != null
								&& !cpbRecord.getPostDetails().toString().trim().equals("")) {
							postComments = " Details : " + cpbRecord.getPostDetails().toString().trim();

						} else {
							postComments = " Details : N/R";
						}

						postDetailsOBJ.setComments(postComments);
						postDetailsOBJ.setDetails(postDetails);
						postDetailsOBJ.setName("Post :");
						cpbDetailList.add(postDetailsOBJ);

					}

					///// Post Details Ends here

					if (cpbDetailList != null && cpbDetailList.size() != 0) {
						JRBeanCollectionDataSource beancollection = new JRBeanCollectionDataSource(cpbDetailList);
						parameters.put("cPBDetailsList", beancollection);
						cpbDetailList = new ArrayList<GAReportModel>();
					}

				}
			}
			else if (entry.getKey().equalsIgnoreCase("echoDetails")) {
				echoRecord = (EchoDetails) entry.getValue();

				if (echoRecord != null) {
					echoDetailList = new ArrayList<GAReportModel>();

					if (echoRecord.getMachineName() != null
							&& !echoRecord.getMachineName().toString().trim().equals("")) {
						GAReportModel machineName = new GAReportModel();
						machineName.setDetails(echoRecord.getMachineName().toString().trim());
						machineName.setName("Machine Name :");
						echoDetailList.add(machineName);
					}

					if (echoRecord.getProbeSize() != null
							&& !echoRecord.getProbeSize().toString().trim().equals("")) {
						GAReportModel probeSize = new GAReportModel();
						probeSize.setDetails(echoRecord.getProbeSize().toString().trim());
						probeSize.setName("Probe Size :");
						echoDetailList.add(probeSize);
					}

					if (echoRecord.getBloodOnTip() != null
							&& !echoRecord.getBloodOnTip().toString().trim().equals("")) {
						GAReportModel bloodOnTip = new GAReportModel();
						bloodOnTip.setDetails(echoRecord.getBloodOnTip().toString().trim());
						bloodOnTip.setName("Blood On Tip :");
						echoDetailList.add(bloodOnTip);
					}

					if (echoRecord.getInsertedBy() != null
							&& !echoRecord.getInsertedBy().toString().trim().equals("")) {
						GAReportModel insertedBy = new GAReportModel();
						insertedBy.setDetails(echoRecord.getInsertedBy().toString().trim());
						insertedBy.setName("Inserted By :");
						echoDetailList.add(insertedBy);
					}

					if (echoRecord.getDifficultyInInsertion() != null
							&& !echoRecord.getDifficultyInInsertion().toString().trim().equals("")) {
						GAReportModel difficultyInInsertion = new GAReportModel();
						difficultyInInsertion.setDetails(echoRecord.getDifficultyInInsertion().toString().trim());
						difficultyInInsertion.setName("Insertion Difficulty :");
						echoDetailList.add(difficultyInInsertion);
					}

					if (echoRecord.getPreDetails() != null
							&& !echoRecord.getPreDetails().toString().trim().equals("")) {
						GAReportModel preEcho = new GAReportModel();
						preEcho.setDetails(echoRecord.getPreDetails().toString().trim());
						preEcho.setName("Pre Echo :");
						echoDetailList.add(preEcho);
					}

					if (echoRecord.getIntraOpDetails() != null
							&& !echoRecord.getIntraOpDetails().toString().trim().equals("")) {
						GAReportModel intraOpEcho = new GAReportModel();
						intraOpEcho.setDetails(echoRecord.getIntraOpDetails().toString().trim());
						intraOpEcho.setName("IntraOp Echo :");
						echoDetailList.add(intraOpEcho);
					}
					if (echoRecord.getNewFindings() != null
							&& !echoRecord.getNewFindings().toString().trim().equals("")) {
						GAReportModel postEcho = new GAReportModel();
						postEcho.setDetails(echoRecord.getNewFindings().toString().trim());
						postEcho.setName("New Findings :");
						echoDetailList.add(postEcho);
					}
					if (echoRecord.getChangeInDiagnosis() != null
							&& !echoRecord.getChangeInDiagnosis().toString().trim().equals("")) {
						GAReportModel postEcho = new GAReportModel();
						postEcho.setDetails(echoRecord.getChangeInDiagnosis().toString().trim());
						postEcho.setName("Change in Diagnosis :");
						echoDetailList.add(postEcho);
					}
					if (echoRecord.getPostDetails() != null
							&& !echoRecord.getPostDetails().toString().trim().equals("")) {
						GAReportModel postEcho = new GAReportModel();
						postEcho.setDetails(echoRecord.getPostDetails().toString().trim());
						postEcho.setName("Post Echo :");
						echoDetailList.add(postEcho);
					}

					if (echoDetailList != null && echoDetailList.size() != 0) {
						JRBeanCollectionDataSource echoData = new JRBeanCollectionDataSource(echoDetailList);
						parameters.put("echoList", echoData);
						echoDetailList = new ArrayList<GAReportModel>();
					}
				}
			}
		}
		if (!procedureValues.trim().equals("")) {
			parameters.put("plannedProcedure", procedureValues);
		}
		if (!diagValues.trim().equals("")) {
			parameters.put("diagnosis", diagValues);
		}

		try {
			testHistoryList = DAOFactory.getInvestigationValueView().getInvestigationvaluecasemapperlist(caseID,
					EventLogSession.getInstance().getEventTime(),
					ControllerMediator.getInstance().getMainController().getShiftOutTime().getTime(),
					UserSession.getInstance().getUserWithrRolesForUserAuthentication().getUserID());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LOG.error("## Exception occured:", e);
		}

		if (testHistoryList != null) {
			System.out.println(testHistoryList);

			List<TestABGModel> abgTestList = new ArrayList<TestABGModel>();
			List<OtherTestModel> sugarTestList = new ArrayList<OtherTestModel>();
			List<OtherTestModel> actTestList = new ArrayList<OtherTestModel>();
			for (InvestigationMapperAndValue mapperObj : testHistoryList) {

				System.out.println(mapperObj.getInvestigationvaluescasemapper().getinvestigationsTests().getTestName());
				if (mapperObj.getInvestigationvaluescasemapper().getinvestigationsTests().getTestName()
						.equalsIgnoreCase("ABG")) {
					TestABGModel abgTestObj = new TestABGModel();
					String time = "";
					if (mapperObj.getInvestigationvaluescasemapper().getDate().getHours() > 9) {
						time = mapperObj.getInvestigationvaluescasemapper().getDate().getHours() + "";
					} else {
						time = "0" + mapperObj.getInvestigationvaluescasemapper().getDate().getHours();
					}
					if (mapperObj.getInvestigationvaluescasemapper().getDate().getMinutes() > 9) {
						time = time + ":" + mapperObj.getInvestigationvaluescasemapper().getDate().getMinutes();
					} else {
						time = time + ":0" + mapperObj.getInvestigationvaluescasemapper().getDate().getMinutes();
					}

					abgTestObj.setTime(time);
					for (InvestigationSetValue testObj : mapperObj.getInvestigationSetValueList()) {

						if (testObj.getName().equalsIgnoreCase("FiO2") && testObj.getValue() != null) {
							abgTestObj.setFiO(testObj.getValue() + "");
						} else if (testObj.getName().trim().equalsIgnoreCase("pH") && testObj.getValue() != null) {
							abgTestObj.setpH(testObj.getValue() + "");
						} else if (testObj.getName().equalsIgnoreCase("pO2") && testObj.getValue() != null) {
							abgTestObj.setpO(testObj.getValue() + "");
						} else if (testObj.getName().equalsIgnoreCase("pCO2") && testObj.getValue() != null) {
							abgTestObj.setpCO(testObj.getValue() + "");
						} else if (testObj.getName().equalsIgnoreCase("HCO3-") && testObj.getValue() != null) {
							abgTestObj.sethCO(testObj.getValue() + "");
						}

						else if (testObj.getName().equalsIgnoreCase("BE/BD") && testObj.getValue() != null) {
							abgTestObj.setbEBD(testObj.getValue() + "");
						} else if (testObj.getName().equalsIgnoreCase("SpO2") && testObj.getValue() != null) {
							abgTestObj.setSpO(testObj.getValue() + "");
						} else if (testObj.getName().equalsIgnoreCase("Lac") && testObj.getValue() != null) {
							abgTestObj.setLac(testObj.getValue() + "");
						} else if (testObj.getName().equalsIgnoreCase("Hct") && testObj.getValue() != null) {
							abgTestObj.setHct(testObj.getValue() + "");
						}

						else if (testObj.getName().equalsIgnoreCase("Na+") && testObj.getValue() != null) {
							abgTestObj.setNa(testObj.getValue() + "");
						} else if (testObj.getName().equalsIgnoreCase("K+") && testObj.getValue() != null) {
							abgTestObj.setK(testObj.getValue() + "");
						} else if (testObj.getName().equalsIgnoreCase("Ca2+") && testObj.getValue() != null) {
							abgTestObj.setCa(testObj.getValue() + "");
						} else if (testObj.getName().equalsIgnoreCase("Cl-") && testObj.getValue() != null) {
							abgTestObj.setCl(testObj.getValue() + "");
						} else if (testObj.getName().equalsIgnoreCase("BB") && testObj.getValue() != null) {
							abgTestObj.setbB(testObj.getValue() + "");
						} else if (testObj.getName().equalsIgnoreCase("Others") && testObj.getValue() != null) {
							abgTestObj.setOthers(testObj.getValue() + "");
						}

					}

					abgTestList.add(abgTestObj);
				}
				else if (mapperObj.getInvestigationvaluescasemapper().getinvestigationsTests().getTestName()
						.equalsIgnoreCase("sugar")) {
					OtherTestModel sugarTestObj = new OtherTestModel();
					String time = "";
					if (mapperObj.getInvestigationvaluescasemapper().getDate().getHours() > 9) {
						time = mapperObj.getInvestigationvaluescasemapper().getDate().getHours() + "";
					} else {
						time = "0" + mapperObj.getInvestigationvaluescasemapper().getDate().getHours();
					}
					if (mapperObj.getInvestigationvaluescasemapper().getDate().getMinutes() > 9) {
						time = time + ":" + mapperObj.getInvestigationvaluescasemapper().getDate().getMinutes();
					} else {
						time = time + ":0" + mapperObj.getInvestigationvaluescasemapper().getDate().getMinutes();
					}
					sugarTestObj.setTime(time);
					for (InvestigationSetValue testObj : mapperObj.getInvestigationSetValueList()) {

						sugarTestObj.setMeasuredValue(testObj.getValue() + "");
					}
					sugarTestList.add(sugarTestObj);
				}
				else if (mapperObj.getInvestigationvaluescasemapper().getinvestigationsTests().getTestName()
						.equalsIgnoreCase("ACT")) {
					OtherTestModel actTestObj = new OtherTestModel();
					String time = "";
					if (mapperObj.getInvestigationvaluescasemapper().getDate().getHours() > 9) {
						time = mapperObj.getInvestigationvaluescasemapper().getDate().getHours() + "";
					} else {
						time = "0" + mapperObj.getInvestigationvaluescasemapper().getDate().getHours();
					}
					if (mapperObj.getInvestigationvaluescasemapper().getDate().getMinutes() > 9) {
						time = time + ":" + mapperObj.getInvestigationvaluescasemapper().getDate().getMinutes();
					} else {
						time = time + ":0" + mapperObj.getInvestigationvaluescasemapper().getDate().getMinutes();
					}
					actTestObj.setTime(time);
					for (InvestigationSetValue testObj : mapperObj.getInvestigationSetValueList()) {

						actTestObj.setMeasuredValue(testObj.getValue() + "");
					}
					actTestList.add(actTestObj);
				}

			}

			if (abgTestList != null && abgTestList.size() != 0) {
				JRBeanCollectionDataSource beancollection = new JRBeanCollectionDataSource(abgTestList);
				parameters.put("ABGTestList", beancollection);
			}
			if (sugarTestList != null && sugarTestList.size() != 0) {
				JRBeanCollectionDataSource beancollectionSugarTestList = new JRBeanCollectionDataSource(sugarTestList);
				parameters.put("SugarTestList", beancollectionSugarTestList);
			}
			if (actTestList != null && actTestList.size() != 0) {
				JRBeanCollectionDataSource beancollectionACTTestList = new JRBeanCollectionDataSource(actTestList);
				parameters.put("ACTList", beancollectionACTTestList);
			}

		}

		generatePDFReport("");

		// generateCSV();

	}

	// @SuppressWarnings("deprecation")
	// private void generateCSV() {
	//
	// List<PatientMonitorData> patientMonitorDataList = null;
	// try {
	// patientMonitorDataList =
	// DAOFactory.patientMonitorData().getAllPatientMonitorData(caseID,
	// UserSession.getInstance().getUserWithrRolesForUserAuthentication().getUserID());
	// } catch (Exception e1) {
	// // TODO Auto-generated catch block
	// e1.printStackTrace();
	// }
	// if (PatientSession.getInstance().getPatient() != null) {
	// patient = PatientSession.getInstance().getPatient();
	//
	// }
	// if (PatientSession.getInstance().getPatientCase() != null) {
	// patientCase = PatientSession.getInstance().getPatientCase();
	// }
	// String patientDetails = "";
	//
	// if (patient != null) {
	// patientDetails = "First Name:" + patient.getFirstName() + " Last Name:" +
	// patient.getLastName()
	// + " CR No:" + patient.getCrnumber() + " Case No:" + caseID;
	// }
	// if (patientCase != null) {
	// patientDetails = patientDetails + " Planned Procedure Date:" +
	// patientCase.getPlannedProcedureDateTime();
	// }
	//
	// XSSFWorkbook workbook = new XSSFWorkbook();
	// XSSFSheet sheet = workbook.createSheet("S5 Patient monitor data");
	//
	// CellStyle style = workbook.createCellStyle();
	// XSSFFont font = workbook.createFont();
	// font.setFontHeightInPoints((short) 10);
	// font.setFontName("Arial");
	// font.setBold(true);
	// style.setFont(font);
	//
	// CellStyle stylePatientDetails = workbook.createCellStyle();
	// XSSFFont fontDetails = workbook.createFont();
	// fontDetails.setFontHeightInPoints((short) 15);
	// XSSFRow patientDetailsRow = sheet.createRow((short) 0);
	// patientDetailsRow.setRowStyle(stylePatientDetails);
	//
	// XSSFCell patientDetailsCell = patientDetailsRow.createCell(0);
	// patientDetailsCell.setCellStyle(style);
	// patientDetailsCell.setCellValue(patientDetails);
	//
	// XSSFRow rowheadS5 = sheet.createRow((short) 2);
	// rowheadS5.setRowStyle(style);
	//
	// XSSFCell cell = rowheadS5.createCell(0);
	// cell.setCellStyle(style);
	// cell.setCellValue("Parameters");
	//
	// XSSFRow row0 = sheet.createRow((short) 3);
	// row0.createCell((short) 0).setCellValue("HR");
	// XSSFRow row1 = sheet.createRow((short) 4);
	// row1.createCell((short) 0).setCellValue("IBPSYS");
	// XSSFRow row2 = sheet.createRow((short) 5);
	// row2.createCell((short) 0).setCellValue("IBPDIA");
	// XSSFRow row3 = sheet.createRow((short) 6);
	// row3.createCell((short) 0).setCellValue("SPO2");
	// XSSFRow row4 = sheet.createRow((short) 7);
	// row4.createCell((short) 0).setCellValue("RTCO2");
	// XSSFRow row5 = sheet.createRow((short) 8);
	// row5.createCell((short) 0).setCellValue("TEMP1");
	// XSSFRow row6 = sheet.createRow((short) 9);
	// row6.createCell((short) 0).setCellValue("TEMP2");
	//
	// int i = 1;
	// String headerS5 = "";
	// if (patientMonitorDataList != null) {
	// System.out.println("Total Count#####" + patientMonitorDataList.size());
	// for (PatientMonitorData patientMonitorObj : patientMonitorDataList) {
	// if (i > 254) {
	// System.out.println();
	// }
	// XSSFCell cell1 = rowheadS5.createCell(i);
	// cell1.setCellStyle(style);
	// headerS5 = "";
	// if (patientMonitorObj.getTimeStamp().getHours() < 10) {
	// headerS5 = "0" + patientMonitorObj.getTimeStamp().getHours();
	// } else {
	// headerS5 = patientMonitorObj.getTimeStamp().getHours() + "";
	// }
	// if (patientMonitorObj.getTimeStamp().getMinutes() < 10) {
	// headerS5 = headerS5 + ":0" +
	// patientMonitorObj.getTimeStamp().getMinutes();
	// } else {
	// headerS5 = headerS5 + ":" +
	// patientMonitorObj.getTimeStamp().getMinutes();
	// }
	// cell1.setCellValue(headerS5);
	// i++;
	// System.out.println("Count#####" + i);
	// }
	//
	// i = 1;
	//
	// for (PatientMonitorData patientMonitorObj : patientMonitorDataList) {
	// row0.createCell((short) i).setCellValue(patientMonitorObj.getHr());
	// row1.createCell((short) i).setCellValue(patientMonitorObj.getiBPSys());
	// row2.createCell((short) i).setCellValue(patientMonitorObj.getiBPDia());
	// row3.createCell((short) i).setCellValue(patientMonitorObj.getSpO2());
	// row4.createCell((short) i).setCellValue(patientMonitorObj.getEtCO2());
	// row5.createCell((short) i).setCellValue(patientMonitorObj.getTemp1());
	// row6.createCell((short) i).setCellValue(patientMonitorObj.getTemp2());
	// i++;
	// }
	// }
	//
	// List<AnethesiaMachineData> anethesiaMachineDataList = null;
	//
	// try {
	// anethesiaMachineDataList =
	// DAOFactory.anethesiaMachineData().getAllAnethesiaMachineData(caseID,
	// UserSession.getInstance().getUserWithrRolesForUserAuthentication().getUserID());
	// } catch (Exception e1) {
	//
	// e1.printStackTrace();
	// }
	//
	// XSSFSheet sheet2 = workbook.createSheet("Anaethesia device data");
	//
	// XSSFRow patientDetailsRow2 = sheet2.createRow((short) 0);
	// patientDetailsRow2.setRowStyle(stylePatientDetails);
	//
	// XSSFCell patientDetailsCell2 = patientDetailsRow2.createCell(0);
	// patientDetailsCell2.setCellStyle(style);
	// patientDetailsCell2.setCellValue(patientDetails);
	//
	// font.setFontHeightInPoints((short) 10);
	// font.setFontName("Arial");
	// font.setBold(true);
	// style.setFont(font);
	// XSSFRow rowheadAnae = sheet2.createRow((short) 2);
	// rowheadAnae.setRowStyle(style);
	// XSSFCell cell2 = rowheadAnae.createCell(0);
	// cell2.setCellStyle(style);
	// cell2.setCellValue("Parameters");
	//
	// XSSFRow row20 = sheet2.createRow((short) 3);
	// row20.createCell((short) 0).setCellValue("PPEAK");
	// XSSFRow row21 = sheet2.createRow((short) 4);
	// row21.createCell((short) 0).setCellValue("PMEAN");
	// XSSFRow row22 = sheet2.createRow((short) 5);
	// row22.createCell((short) 0).setCellValue("PEEP");
	// XSSFRow row23 = sheet2.createRow((short) 6);
	// row23.createCell((short) 0).setCellValue("CIRCUITO2");
	// XSSFRow row24 = sheet2.createRow((short) 7);
	// row24.createCell((short) 0).setCellValue("TVEXP");
	// XSSFRow row25 = sheet2.createRow((short) 8);
	// row25.createCell((short) 0).setCellValue("MVEXP");
	// XSSFRow row26 = sheet2.createRow((short) 9);
	// row26.createCell((short) 0).setCellValue("RR");
	//
	// int j = 1;
	// String headerAnae = "";
	// if (anethesiaMachineDataList != null) {
	// for (AnethesiaMachineData anaethesiaObj : anethesiaMachineDataList) {
	// XSSFCell cell1 = rowheadAnae.createCell(j);
	//
	// cell1.setCellStyle(style);
	// headerAnae = "";
	// if (anaethesiaObj.getTimeStamp().getHours() < 10) {
	// headerAnae = "0" + anaethesiaObj.getTimeStamp().getHours();
	// } else {
	// headerAnae = anaethesiaObj.getTimeStamp().getHours() + "";
	// }
	// if (anaethesiaObj.getTimeStamp().getMinutes() < 10) {
	// headerAnae = headerAnae + ":0" +
	// anaethesiaObj.getTimeStamp().getMinutes();
	// } else {
	// headerAnae = headerAnae + ":" +
	// anaethesiaObj.getTimeStamp().getMinutes();
	// }
	// cell1.setCellValue(headerAnae);
	//
	// j++;
	// }
	// j = 1;
	//
	// for (AnethesiaMachineData anaethesiaObj : anethesiaMachineDataList) {
	// row20.createCell((short) j).setCellValue(anaethesiaObj.getpPeak());
	// row21.createCell((short) j).setCellValue(anaethesiaObj.getpMean());
	// row22.createCell((short) j).setCellValue(anaethesiaObj.getPeep());
	// row23.createCell((short) j).setCellValue(anaethesiaObj.getCircuitO2());
	// row24.createCell((short) j).setCellValue(anaethesiaObj.getTvExp());
	// row25.createCell((short) j).setCellValue(anaethesiaObj.getMvExp());
	// row26.createCell((short) j).setCellValue(anaethesiaObj.getRr());
	// j++;
	// }
	// }
	//
	// csvFileName = "";
	// if (patient != null) {
	//
	// csvFileName = patient.getFirstName() + "_" + caseID + "";
	// }
	// File directory = new
	// File(SystemConfiguration.getInstance().getLocationForPatientReport());
	// if (!directory.exists()) {
	// directory.mkdir();
	// // If you require it to make the entire directory path including
	// // parents,
	// // use directory.mkdirs(); here instead.
	// }
	// String path = directory + "\\" + csvFileName + ".xlsx";
	// // String path = ralPath.replaceAll("IntraOpPatientReport.jrxml",
	// // pdfFileName + ".xlsx").toString();
	// FileOutputStream fileOut;
	// try {
	// fileOut = new FileOutputStream(path);
	// workbook.write(fileOut);
	// fileOut.close();
	//
	// File myFile = new File(path);
	// try {
	// Desktop.getDesktop().open(myFile);
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// } catch (FileNotFoundException e) {
	//
	// e.printStackTrace();
	// } catch (IOException e) {
	//
	// e.printStackTrace();
	// }
	//
	// }

	@SuppressWarnings({ "deprecation", "rawtypes" })
	private void generatePDFReport(String patientReportDetails) {

		try {

			JasperDesign jasperDesign;

			jasperDesign = JRXmlLoader.load(ralPath);

			JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

			// ReportView reportView = new ReportView(jasperReport, parameters);
			if (PatientSession.getInstance().getPatient() != null) {
				patient = PatientSession.getInstance().getPatient();

			}
			if (patient != null) {

				pdfFileName = caseID + "_" + patient.getFirstName() + "_" + patient.getCrnumber();
			}
			String path = "";
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());
			String mode = "";
			mode = SystemConfiguration.getInstance().getLocationReportMode();
			if (!mode.equals("") && mode.trim().equalsIgnoreCase("1")) {
				String decodedPath = "";
				try {
					path = SystemConfiguration.class.getProtectionDomain().getCodeSource().getLocation().toURI()
							.getPath();
				} catch (URISyntaxException e) {

				}
				try {
					decodedPath = URLDecoder.decode(path, "UTF-8");
				} catch (UnsupportedEncodingException e) {

				}
				decodedPath = (new File(decodedPath)).getParentFile().getPath() + File.separator + "jasper"
						+ File.separator + "IntraOpPatientReport.jrxml";
				if (decodedPath.contains("file:/")) {
					decodedPath = decodedPath.replaceAll("file:/", "");
				}
				path = decodedPath.replaceAll("IntraOpPatientReport.jrxml", "patientReport.pdf").toString();

			} else {
				File directory = new File(SystemConfiguration.getInstance().getLocationForPatientReport());
				if (!directory.exists()) {
					directory.mkdir();
					// If you require it to make the entire directory path
					// including
					// parents,
					// use directory.mkdirs(); here instead.
				}

				path = directory + "\\" + pdfFileName + ".pdf";
			}

			try {
				JasperExportManager.exportReportToPdfFile(jasperPrint, path);
				File myFile = new File(path);
				Desktop.getDesktop().open(myFile);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				LOG.error("## Exception occured:", e);
			}

			// Report saving in Db starts here
			if (!mode.equals("") && mode.trim().equalsIgnoreCase("1")) {
				JSONObject dataJson = new JSONObject();
				IntraopReportFile intraopReportFile = new IntraopReportFile();
				String strData = pdfToBase64(path);

				byte[] buff = null;
				try {
					buff = strData.getBytes("UTF-8");
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Blob blob = null;
				try {
					blob = new SerialBlob(buff);
				} catch (SerialException e) {
					LOG.error("## Exception occured:", e);
					e.printStackTrace();
				} catch (SQLException e) {
					LOG.error("## Exception occured:", e);
					e.printStackTrace();
				}
				intraopReportFile.setCaseID(caseID);
				intraopReportFile.setPatientID(patient.getPatientId());
				intraopReportFile.setPatientReportData(blob);
				intraopReportFile.setFileName(pdfFileName);
				intraopReportFile.setFileType("pdf");

				dataJson = DAOFactory.getPatientDetailsReport().uploadFile(
						UserSession.getInstance().getUserWithrRolesForUserAuthentication().getUserID(),
						intraopReportFile);

			}
			// Report saving in Db ends here

			LOG.info("Reached -> " + LOG.getName());

		} catch (JRException e) {

			e.printStackTrace();
			LOG.error("## Exception occured:", e);
		}
	}

	public static String pdfToBase64(String path) {
		// Getting encoder
		Base64.Encoder encoder = Base64.getEncoder();
		// Creating byte array
		byte[] buf = fileToByteArray(path);
		// Encoding string
		String str = encoder.encodeToString(buf);
		return str;
	}

	public static byte[] fileToByteArray(String fileName) {
		try {
			File f = new File(fileName);
			FileInputStream in = new FileInputStream(f);
			byte[] bytes = new byte[(int) f.length()];
			int c = -1;
			int ix = 0;
			while ((c = in.read()) > -1) {
				bytes[ix] = (byte) c;
				ix++;
			}
			in.close();
			return bytes;
		} catch (IOException e) {
			LOG.error("## Exception occured:", e);
			e.printStackTrace();
			return null;
		}
	}

}
