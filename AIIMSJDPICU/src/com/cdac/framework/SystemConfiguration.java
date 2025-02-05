/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package com.cdac.framework;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.configuration.AbstractConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalINIConfiguration;
import org.apache.log4j.Logger;

public class SystemConfiguration extends HierarchicalINIConfiguration
{
	private static SystemConfiguration configuration;
	private static final String INI_FILENAME = "config.ini";
	private static final Logger LOG = Logger.getLogger(SystemConfiguration.class);

	private static final String DATA_SAVING = "Data_Saving";
	private static final String WAVEFORM_PARSING_FLAG = "Waveform_Parsing_Flag";
	private static final String LOGS_SECTION = "LOGS";
	private static final String VERSION = "Version";
	private static final String DEVICE_IDENTIFICATION = "Device_Identification";
	private static final String DOT = ".";
	private static final String PARSE_S5_WAVEFORM = WAVEFORM_PARSING_FLAG + DOT + "ParseS5Waveform";
	private static final String PARSE_ANESTHESIA_WAVEFORM = WAVEFORM_PARSING_FLAG + DOT + "ParseAnesthesiaWaveform";
	private static final String DATA_DUMP_INTERVAL = DATA_SAVING + DOT + "Data_Dump_Interval";
	private static final String HANDSHAKE_CLASSES = DEVICE_IDENTIFICATION + DOT + "Handshake_Classes";
	private static final String DEVICE_DATA_DUMP = DATA_SAVING + DOT + "Data_Dump_Location";
	private static final String DATA_DUMP_DELIMITER = DATA_SAVING + DOT + "Data_Dump_Delimiter";
	private static final String DEVICE_IDENTIFICATION_SERVICE_INTERVAL = DEVICE_IDENTIFICATION + DOT + "Identification_Service_Interval";
	private static final String SAVE_PARAMETERS = DATA_SAVING + DOT + "Save_Parameters";
	private static final String PATIENT_REPORT = DATA_SAVING + DOT + "Report_Location";
	private static final String PATIENT_REPORT_MODE = DATA_SAVING + DOT + "Report_Save_Mode";
	private static final String APP_VERSION = VERSION + DOT + "Version_Details";
	private static final String PAC_URL = VERSION + DOT + "Pac_Url";
	private static final String TOTAL_SNAPSHOT_SAMPLES = "Total_Snapshot_Samples";
	private static final String SNAPSHOT_FUTURE_TIME = "Snapshot_Future_Time";
	private static final String DEVICE_TIMEOUT = "Device_Timeout";
	private static final String SHOW_RAW_DATA_LOGS = LOGS_SECTION + DOT + "ShowRawDataLogs";
	private static final String SHOW_PACKET_LOGS = LOGS_SECTION + DOT + "ShowPacketLogs";
	
	//MAIN CATEGORY  --> Changes by vivek
	private static final String MAIN_CATEGORY = "Main_Category";
	private static final String MAIN_CAT = MAIN_CATEGORY + DOT + "Main_Cat";
	//changes end
	
	// ---CSV PARMETER INDEXES
	private static final String CSV_INDEX = "CSV_Param_Index";
	private static final String CSV_HR_INDEX = CSV_INDEX + DOT + "HR";
	private static final String CSV_IBPSYS_INDEX = CSV_INDEX + DOT + "IBPSYS";
	private static final String CSV_IBPDIA_INDEX = CSV_INDEX + DOT + "IBPDIA";
	private static final String CSV_ETC02_INDEX = CSV_INDEX + DOT + "ETC02";
	private static final String CSV_SPO2_INDEX = CSV_INDEX + DOT + "SPO2";
	private static final String CSV_TEMP1_INDEX = CSV_INDEX + DOT + "TEMP1";
	private static final String CSV_TEMP2_INDEX = CSV_INDEX + DOT + "TEMP2";
	private static final String CSV_BIS_INDEX = CSV_INDEX + DOT + "BIS";
	private static final String CSV_ETAGENT_INDEX = CSV_INDEX + DOT + "ETAGENT";
	private static final String CSV_MAC_INDEX = CSV_INDEX + DOT + "MAC";
	private static final String CSV_IBPMEAN_INDEX = CSV_INDEX + DOT + "IBPMEAN";
	private static final String CSV_NIBPMEAN_INDEX = CSV_INDEX + DOT + "NIBPMEAN";
	private static final String CSV_NIBPSYS_INDEX = CSV_INDEX + DOT + "NIBPSYS";
	private static final String CSV_NIBPDIA_INDEX = CSV_INDEX + DOT + "NIBPDIA";


	private static final String CSV_PPEAK_INDEX = CSV_INDEX + DOT + "PPEAK";
	private static final String CSV_PMEAN_INDEX = CSV_INDEX + DOT + "PMEAN";
	private static final String CSV_PEEPEXT_INDEX = CSV_INDEX + DOT + "PEEPEXT";
	private static final String CSV_CICUITO2_INDEX = CSV_INDEX + DOT + "CICUITO2";
	private static final String CSV_MVEXP_INDEX = CSV_INDEX + DOT + "MVEXP";
	private static final String CSV_TVEXP_INDEX = CSV_INDEX + DOT + "TVEXP";
	private static final String CSV_RR_INDEX = CSV_INDEX + DOT + "RR";

	private static final String GRAPH_PARSING = "Graph_Parsing";
	private static final String ANES_GRAPH_PARSING = GRAPH_PARSING + DOT + "AnesGraph";
	private static final String S5_GRAPH_PARSING = GRAPH_PARSING + DOT + "S5Graph";

	// ---Trend Graph
	private static final String TREND_GRAPH = "Trend_Graph";
	private static final String TREND_GRAPH_SPEED = TREND_GRAPH + DOT + "TrendGraphSpeed";

	private static final String GRID_GRAPH_TIME = "Grid_Graph_Time";
	private static final String MINS_GAP = GRID_GRAPH_TIME + DOT + "MinsGap";
	private static final String DATA_SAVE_TIME_INTERVAL = GRID_GRAPH_TIME + DOT + "DataSaveTimeInterval";

	// ---Kafka IP
	private static final String KAFKA = "Kafka";
	private static final String KAFKA_IP = KAFKA + DOT + "IP";
	private static final String PTMNTR_PUBLISH_FREQUENCY = KAFKA + DOT + "PTMNTR_PUBLISH_FREQUENCY";
	private static final String PATIENT_DETAILS_TOPIC = KAFKA + DOT + "PATIENT_DETAILS_TOPIC";
	private static final String SCALINGVARS_PUBLISH_FREQUENCY = KAFKA + DOT + "SCALINGVARS_PUBLISH_FREQUENCY";
	private static final String SCALING_VARS_TOPIC = KAFKA + DOT + "SCALING_VARS_TOPIC";
	private static final String EXEC_EVENTS_TOPIC = KAFKA + DOT + "EXEC_EVENTS_TOPIC";

	static
	{
		try
		{
			String path = SystemConfiguration.class.getProtectionDomain().getCodeSource().getLocation().toURI()
			        .getPath();
			String decodedPath = URLDecoder.decode(path, "UTF-8");

			configuration = new SystemConfiguration((new File(decodedPath)).getParentFile().getPath() + File.separator
			        + "config" + File.separator + INI_FILENAME);
		}
		catch (URISyntaxException e)
		{
			LOG.error("Error 1" + e.getMessage());
			e.printStackTrace();
		}
		catch (UnsupportedEncodingException e)
		{
			LOG.error("Error 2" + e.getMessage());
			e.printStackTrace();
		}
		catch (ConfigurationException e)
		{
			LOG.error("Error opening or reading from config file:" + e.getMessage());
			e.printStackTrace();
		}
	}

	private SystemConfiguration(String path) throws ConfigurationException
	{
		super(path);
	}

	/**
	 * QuickFix as the library is not able to convert to list via getList() API.
	 * FIXME: Debug why Library's getList() API is not working.
	 *
	 * @param valueToConvert
	 *            value to convert to the list
	 * @return ArrayList.
	 */
	private List convertStringToList(String valueToConvert)
	{
		return new ArrayList(Arrays.asList(valueToConvert.split("" + AbstractConfiguration.getDefaultListDelimiter())));
	}

	public static SystemConfiguration getInstance()
	{
		return configuration;
	}
	//Changes by vivek
	public String getMainCat()
	{
		return getString(MAIN_CAT);
	}
	//changes end

	public int getDataDumpTime()
	{
		return getInt(DATA_DUMP_INTERVAL);
	}

	public List getListOfHandshakeClasses()
	{
		String value = getString(HANDSHAKE_CLASSES);
		return convertStringToList(value);
	}

	public List getListOfParametersToSave()
	{
		String value = getString(SAVE_PARAMETERS);
		return convertStringToList(value);
	}

	public String getLocationForDeviceDataDump()
	{
		return getString(DEVICE_DATA_DUMP);
	}

	public void setLocationForDeviceDataDump(String location)
	{
		setProperty(DEVICE_DATA_DUMP, location);
	}
	public String getLocationForPatientReport()
	{
		return getString(PATIENT_REPORT);
	}

	public void setLocationForPatientReport(String location)
	{
		setProperty(PATIENT_REPORT, location);
	}
	public String getLocationReportMode()
	{
		return getString(PATIENT_REPORT_MODE);
	}

	public void setLocationReportMode(String location)
	{
		setProperty(PATIENT_REPORT_MODE, location);
	}
	public String getAppVersion()
	{
		return getString(APP_VERSION);
	}

	public void setAppVersion(String location)
	{
		setProperty(APP_VERSION, location);
	}


	public String getPacUrl()
	{
		return getString(PAC_URL);
	}

	public void setPacUrl(String url)
	{
		setProperty(PAC_URL, url);
	}

	public char getDelimiterForDataDump()
	{
		return getString(DATA_DUMP_DELIMITER).charAt(0);
	}

	public int getRepeatIntervalOfIdentificationService()
	{
		return getInt(DEVICE_IDENTIFICATION_SERVICE_INTERVAL);
	}

	public int getTotalSnapshotSamples(String deviceNameInConfig)
	{
		return getInt(deviceNameInConfig + "." + TOTAL_SNAPSHOT_SAMPLES);
	}

	public int getFutureTimeForSnapshotRecording(String deviceNameInConfig)
	{
		return getInt(deviceNameInConfig + "." + SNAPSHOT_FUTURE_TIME);
	}

	public int getDeviceTimeOut(String deviceNameInConfig)
	{
		return getInt(deviceNameInConfig + "." + DEVICE_TIMEOUT);
	}
	public int getCsvHrIndex()
	{
		return getInt(CSV_HR_INDEX);
	}

	public int getCsvIbpsysIndex()
	{
		return getInt(CSV_IBPSYS_INDEX);
	}

	public int getCsvIbpdiaIndex()
	{
		return getInt(CSV_IBPDIA_INDEX);
	}

	public int getCsvEtc02Index()
	{
		return getInt(CSV_ETC02_INDEX);
	}

	public int getCsvSpo2Index()
	{
		return getInt(CSV_SPO2_INDEX);
	}

	public int getCsvTemp1Index()
	{
		return getInt(CSV_TEMP1_INDEX);
	}

	public int getCsvTemp2Index()
	{
		return getInt(CSV_TEMP2_INDEX);
	}

	public int getCsvBisIndex()
	{
		return getInt(CSV_BIS_INDEX);
	}

	public int getCsvEtagentIndex()
	{
		return getInt(CSV_ETAGENT_INDEX);
	}

	public int getCsvMacIndex()
	{
		return getInt(CSV_MAC_INDEX);
	}

	public int getCsvIbpmeanIndex()
	{
		return getInt(CSV_IBPMEAN_INDEX);
	}
	public int getCsvNIbpmeanIndex()
	{
		return getInt(CSV_NIBPMEAN_INDEX);
	}
	public int getCsvPpeakIndex()
	{
		return getInt(CSV_PPEAK_INDEX);
	}

	public int getCsvPmeanIndex()
	{
		return getInt(CSV_PMEAN_INDEX);
	}

	public int getCsvPeepextIndex()
	{
		return getInt(CSV_PEEPEXT_INDEX);
	}

	public int getCsvCicuito2Index()
	{
		return getInt(CSV_CICUITO2_INDEX);
	}

	public int getCsvMvexpIndex()
	{
		return getInt(CSV_MVEXP_INDEX);
	}

	public int getCsvTvexpIndex()
	{
		return getInt(CSV_TVEXP_INDEX);
	}

	public int getCsvRrIndex()
	{
		return getInt(CSV_RR_INDEX);
	}

	public int getTrendGraphSpeed()
	{
		return getInt(TREND_GRAPH_SPEED);
	}

	public boolean getRawDataLogs()
	{
		return getBoolean(SHOW_RAW_DATA_LOGS);
	}

	public boolean getPacketLogs()
	{
		return getBoolean(SHOW_PACKET_LOGS);
	}

	public boolean getParseS5Waveform()
	{
		return getBoolean(PARSE_S5_WAVEFORM);
	}

	public boolean getParseAnesthesiaWaveform()
	{
		return getBoolean(PARSE_ANESTHESIA_WAVEFORM);
	}

	public int getMinsGap()
	{
		return getInt(MINS_GAP);
	}

	public int getAnesGraphParsing()
	{
		return getInt(ANES_GRAPH_PARSING);
	}

	public int getS5GraphParsing()
	{
		return getInt(S5_GRAPH_PARSING);
	}

	public String getKafkaIp()
	{
		return getString(KAFKA_IP);
	}

	public int getPtmntrPublishFrequency()
	{
		return getInt(PTMNTR_PUBLISH_FREQUENCY);
	}

	public String getPatientDetailsTopic()
	{
		return getString(PATIENT_DETAILS_TOPIC);
	}

	public int getScalingvarsPublishFrequency()
	{
		return getInt(SCALINGVARS_PUBLISH_FREQUENCY);
	}

	public String getScalingVarsTopic()
	{
		return getString(SCALING_VARS_TOPIC);
	}

	public String getExecEventsTopic()
	{
		return getString(EXEC_EVENTS_TOPIC);
	}

	public int getDataSaveTimeInterval()
	{
		return getInt(DATA_SAVE_TIME_INTERVAL);
	}

	public static String getCsvNibpmeanIndex() {
		return CSV_NIBPMEAN_INDEX;
	}

	public  int getCsvNibpsysIndex() {
		return getInt( CSV_NIBPSYS_INDEX) ;
	}

	public  int getCsvNibpdiaIndex() {
		return getInt( CSV_NIBPDIA_INDEX);
	}
	
	

}
