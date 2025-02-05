/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package com.cdac.device.patientMonitoring;

import java.util.List;

import org.apache.log4j.Logger;

import com.cdac.framework.CoreDevice;
import com.cdac.framework.CoreDevice.Parsing;
import com.cdac.framework.datastructures.MedicalDeviceData;
import com.cdac.framework.datastructures.NodeBase;
import com.cdac.framework.datastructures.Parameter;
import com.cdac.framework.exceptions.CustomPacketMakerNotFoundException;
import com.cdac.framework.exceptions.StartBytesNotAvailableException;
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortEvent;

public class PatientMonitoringParser extends CoreDevice
{
	public static final Logger LOG = Logger.getLogger(PatientMonitoringParser.class);
	MedicalDeviceData<List<Byte>> medicalDeviceData;

	public static final String PATIENTMONITOR_ECG_WAVE = "PatientMonitoring.ECG.WAVE";
	public static final String PATIENTMONITOR_ECG_WAVE1 = "PatientMonitoring.ECG.WAVE1";
	public static final String PATIENTMONITOR_ECG_WAVE2 = "PatientMonitoring.ECG.WAVE2";
	public static final String PATIENTMONITOR_ECG_WAVE3 = "PatientMonitoring.ECG.WAVE3";
	public static final String PATIENTMONITOR_ECG_RESPIRATIONWAVE = "PatientMonitoring.ECG.RespirationWave";
	public static final String PATIENTMONITOR_ECG_HEARTRATE = "PatientMonitoring.ECG.HeartRate";
	public static final String PATIENTMONITOR_ECG_RESPIRATORYRATE = "PatientMonitoring.ECG.RespiratoryRate";
	public static final String PATIENTMONITOR_ECG_TEMPERATURE1 = "PatientMonitoring.ECG.Temperature1";
	public static final String PATIENTMONITOR_ECG_TEMPERATURE2 = "PatientMonitoring.ECG.Temperature2";
	public static final String PATIENTMONITOR_SPO2_WAVE = "PatientMonitoring.SPO2.WAVE";
	public static final String PATIENTMONITOR_SPO2_OXYGENLEVEL = "PatientMonitoring.SPO2.OxygenLevel";
	public static final String PATIENTMONITOR_NIBP_SYSTOLICPRESSURE = "PatientMonitoring.NIBP.SystolicPressure";
	public static final String PATIENTMONITOR_NIBP_DIASTOLICPRESSURE = "PatientMonitoring.NIBP.DiastolicPressure";
	public static final String PATIENTMONITOR_IBP_WAVE = "PatientMonitoring.IBP.WAVE";
	public static final String PATIENTMONITOR_IBP_SYSTOLICPRESSURE = "PatientMonitoring.IBP.SystolicPressure";
	public static final String PATIENTMONITOR_IBP_DIASTOLICPRESSURE = "PatientMonitoring.IBP.DiastolicPressure";

	private Parameter ECGWave;
	private Parameter ECGWave1;
	private Parameter ECGWave2;
	private Parameter ECGWave3;
	private Parameter RespiratoryWave;
	private Parameter ECGHeartRate;
	private Parameter ECGRespiratoryRate;
	private Parameter Temperature1;
	private Parameter Temperature2;
	private Parameter SPO2Wave;
	private Parameter SPO2OxygenLevel;
	private Parameter NIBPSystolicPressure;
	private Parameter NIBPDiastolicPressure;
	private Parameter IBPWave;
	private Parameter IBPSystolicPressure;
	private Parameter IBPDiastolicPressure;

	public static final String DEVICE_NAME = "PatientMonitoring-0";

	public PatientMonitoringParser(String workName, int messageLength) throws CustomPacketMakerNotFoundException
	{
		super(workName, messageLength, null, PacketingStrategy.START_BYTES);
		LOG.info("PatientMonitoringParser constructor called");
		medicalDeviceData = createIHEDataObject();
	}

	@Override
	public int getPacketSize()
	{
		return 86;
	}

	@Override
	public PacketingStrategy getPacketingStrategy()
	{
		return PacketingStrategy.START_BYTES;
	}

	@Override
	public byte[] getStartBytes() throws StartBytesNotAvailableException
	{
		// TODO Auto-generated method stub
		byte[] arrayOfStartBytes = { 85, 85, 85, 85 };
		return arrayOfStartBytes;
	}

	@Override
	public void deviceEventOccurred(SerialPortEvent dataFromPort)
	{
		if (dataFromPort.getEventType() == SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
		{
			byte[] packet = dataFromPort.getReceivedData();
			parsingLogic(packet);
			setDataProcessingStatus(Parsing.SUCCESSFUL);
		}
	}

	@Override
	public int getListeningEvent()
	{
		return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
	}

	@Override
	protected MedicalDeviceData<List<Byte>> createIHEDataObject()
	{
		MedicalDeviceData deviceData = new MedicalDeviceData<List<Byte>>("PatientMonitoring");

		ECGWave = (Parameter) deviceData.createOrGetParameter(PATIENTMONITOR_ECG_WAVE)[2];
		ECGWave1 = (Parameter) deviceData.createOrGetParameter(PATIENTMONITOR_ECG_WAVE1)[2];
		ECGWave2 = (Parameter) deviceData.createOrGetParameter(PATIENTMONITOR_ECG_WAVE2)[2];
		ECGWave3 = (Parameter) deviceData.createOrGetParameter(PATIENTMONITOR_ECG_WAVE3)[2];
		RespiratoryWave = (Parameter) deviceData.createOrGetParameter(PATIENTMONITOR_ECG_RESPIRATIONWAVE)[2];
		ECGHeartRate = (Parameter) deviceData.createOrGetParameter(PATIENTMONITOR_ECG_HEARTRATE)[2];
		ECGRespiratoryRate = (Parameter) deviceData.createOrGetParameter(PATIENTMONITOR_ECG_RESPIRATORYRATE)[2];
		Temperature1 = (Parameter) deviceData.createOrGetParameter(PATIENTMONITOR_ECG_TEMPERATURE1)[2];
		Temperature2 = (Parameter) deviceData.createOrGetParameter(PATIENTMONITOR_ECG_TEMPERATURE2)[2];
		SPO2Wave = (Parameter) deviceData.createOrGetParameter(PATIENTMONITOR_SPO2_WAVE)[2];
		SPO2OxygenLevel = (Parameter) deviceData.createOrGetParameter(PATIENTMONITOR_SPO2_OXYGENLEVEL)[2];
		NIBPSystolicPressure = (Parameter) deviceData.createOrGetParameter(PATIENTMONITOR_NIBP_SYSTOLICPRESSURE)[2];
		NIBPDiastolicPressure = (Parameter) deviceData.createOrGetParameter(PATIENTMONITOR_NIBP_DIASTOLICPRESSURE)[2];
		IBPWave = (Parameter) deviceData.createOrGetParameter(PATIENTMONITOR_IBP_WAVE)[2];
		IBPSystolicPressure = (Parameter) deviceData.createOrGetParameter(PATIENTMONITOR_IBP_SYSTOLICPRESSURE)[2];
		IBPDiastolicPressure = (Parameter) deviceData.createOrGetParameter(PATIENTMONITOR_IBP_DIASTOLICPRESSURE)[2];

		return deviceData;
	}

	@Override
	public NodeBase getNodeByIdentifier(String parameterIdentifier)
	{
		NodeBase param = null;
		switch (parameterIdentifier)
		{

		case PATIENTMONITOR_ECG_WAVE:
			param = ECGWave;
			break;

		case PATIENTMONITOR_ECG_WAVE1:
			param = ECGWave1;
			break;

		case PATIENTMONITOR_ECG_WAVE2:
			param = ECGWave2;
			break;

		case PATIENTMONITOR_ECG_WAVE3:
			param = ECGWave3;
			break;

		case PATIENTMONITOR_ECG_RESPIRATIONWAVE:
			param = RespiratoryWave;
			break;

		case PATIENTMONITOR_ECG_HEARTRATE:
			param = ECGHeartRate;
			break;

		case PATIENTMONITOR_ECG_RESPIRATORYRATE:
			param = ECGRespiratoryRate;
			break;

		case PATIENTMONITOR_ECG_TEMPERATURE1:
			param = Temperature1;
			break;

		case PATIENTMONITOR_ECG_TEMPERATURE2:
			param = Temperature2;
			break;

		case PATIENTMONITOR_SPO2_WAVE:
			param = SPO2Wave;
			break;

		case PATIENTMONITOR_SPO2_OXYGENLEVEL:
			param = SPO2OxygenLevel;
			break;

		case PATIENTMONITOR_NIBP_SYSTOLICPRESSURE:
			param = NIBPSystolicPressure;
			break;

		case PATIENTMONITOR_NIBP_DIASTOLICPRESSURE:
			param = NIBPDiastolicPressure;
			break;

		case PATIENTMONITOR_IBP_WAVE:
			param = IBPWave;
			break;

		case PATIENTMONITOR_IBP_SYSTOLICPRESSURE:
			param = IBPSystolicPressure;
			break;

		case PATIENTMONITOR_IBP_DIASTOLICPRESSURE:
			param = IBPDiastolicPressure;
			break;

		default:
			param = null;
		}
		return param;
	}

	public void parsingLogic(byte[] completeMessage)
	{
		// ********PARAMETERS INITIALIZATION********//

		short respirationWave = 0;
		short ecgHeartRate = 0;
		float temperature1 = 0;
		float temperature2 = 0;
		int respirationRate = 0;
		short spo2Wave = 0;
		short oxygenLevel = 0;
		short nibpSystolicPressure = 0;
		short nibpDiastolicPressure = 0;
		short ibpWave = 0;
		short ibpSystolicPressure = 0;
		short ibpDiastolicPressure = 0;

		// *********ECG WAVE1********//

//		int[] ecgWave1 = { (short) (((completeMessage[1] & 0xFF) << 8) | completeMessage[0] & 0xFF),
//		        (short) (((completeMessage[3] & 0xFF) << 8) | completeMessage[2] & 0xFF),
//		        (short) (((completeMessage[5] & 0xFF) << 8) | completeMessage[4] & 0xFF) };

		int ecgWave = (short) (((completeMessage[4] & 0xFF) << 8) | completeMessage[3] & 0xFF);
		ECGWave.setValue(ecgWave);

		int[] ecgWave1 = { (short) (((completeMessage[4] & 0xFF) << 8) | completeMessage[3] & 0xFF),
		        (short) (((completeMessage[6] & 0xFF) << 8) | completeMessage[5] & 0xFF),
		        (short) (((completeMessage[8] & 0xFF) << 8) | completeMessage[7] & 0xFF) };

		ECGWave1.setValue(ecgWave1);

		// *********ECG WAVE2********//
		int[] ecgWave2 = { (short) (((completeMessage[10] & 0xFF) << 8) | completeMessage[9] & 0xFF),
		        (short) (((completeMessage[12] & 0xFF) << 8) | completeMessage[11] & 0xFF),
		        (short) (((completeMessage[14] & 0xFF) << 8) | completeMessage[13] & 0xFF) };

//		 int[] ecgWave2 = { (short) (((completeMessage[11] & 0xFF) << 8) |
//		 completeMessage[10] & 0xFF),
//		 (short) (((completeMessage[13] & 0xFF) << 8) | completeMessage[12] &
//		 0xFF),
//		 (short) (((completeMessage[15] & 0xFF) << 8) | completeMessage[14] &
//		 0xFF) };

		ECGWave2.setValue(ecgWave2);

		// *********ECG WAVE3********//
		int[] ecgWave3 = { (short) (((completeMessage[16] & 0xFF) << 8) | completeMessage[15] & 0xFF),
		        (short) (((completeMessage[18] & 0xFF) << 8) | completeMessage[17] & 0xFF),
		        (short) (((completeMessage[20] & 0xFF) << 8) | completeMessage[19] & 0xFF) };

//		int[] ecgWave3 = { (short) (((completeMessage[17] & 0xFF) << 8) | completeMessage[16] & 0xFF),
//		        (short) (((completeMessage[19] & 0xFF) << 8) | completeMessage[18] & 0xFF),
//				(short) (((completeMessage[21] & 0xFF) << 8) | completeMessage[20] & 0xFF) };


		ECGWave3.setValue(ecgWave3);

		// ********RESPIRATION WAVE********//
		respirationWave = (short) (((completeMessage[22] & 0xFF) << 8) | completeMessage[21] & 0xFF);
//		respirationWave = (short) (((completeMessage[23] & 0xFF) << 8) | completeMessage[22] & 0xFF);
		RespiratoryWave.setValue(Integer.valueOf(respirationWave));

		// *********HEART RATE********//
		ecgHeartRate = (short) (((completeMessage[24] & 0xFF) << 8) | completeMessage[23] & 0xFF);
//		ecgHeartRate = (short) (((completeMessage[25] & 0xFF) << 8) | completeMessage[24] & 0xFF);
		ECGHeartRate.setValue(Integer.valueOf(ecgHeartRate));

		// *********TEMPERATURE 1********//
		temperature1 = (short) (((completeMessage[26] & 0xFF) << 8) | completeMessage[25] & 0xFF);
//		temperature1 = (short) (((completeMessage[27] & 0xFF) << 8) | completeMessage[26] & 0xFF);
		Temperature1.setValue(temperature1 / 10);

		// *********TEMPERATURE 2********//
		temperature2 = (short) (((completeMessage[28] & 0xFF) << 8) | completeMessage[27] & 0xFF);
//		temperature2 = (short) (((completeMessage[29] & 0xFF) << 8) | completeMessage[28] & 0xFF);
		Temperature2.setValue(Integer.valueOf((int) (temperature2 / 10)));

		// *********RESPIRATION RATE********//
		respirationRate = (completeMessage[29] & 0xFF);
//		respirationRate = (completeMessage[30] & 0xFF);
		ECGRespiratoryRate.setValue(Integer.valueOf(respirationRate));

		// *********SPO2 WAVE********//
		spo2Wave = (short) (((completeMessage[36] & 0xFF) << 8) | completeMessage[35] & 0xFF);
//		spo2Wave = (short) (((completeMessage[37] & 0xFF) << 8) | completeMessage[36] & 0xFF);
		SPO2Wave.setValue(Integer.valueOf(spo2Wave));

		// *********OXYGEN LEVEL********//
		oxygenLevel = (short) (((completeMessage[42] & 0xFF) << 8) | completeMessage[41] & 0xFF);
//		oxygenLevel = (short) (((completeMessage[43] & 0xFF) << 8) | completeMessage[42] & 0xFF);
		SPO2OxygenLevel.setValue(Integer.valueOf(oxygenLevel));

		// *********NIBP SYSTOLIC PRESSURE********//
		nibpSystolicPressure = (short) (((completeMessage[48] & 0xFF) << 8) | completeMessage[47] & 0xFF);
//		nibpSystolicPressure = (short) (((completeMessage[49] & 0xFF) << 8) | completeMessage[48] & 0xFF);
		NIBPSystolicPressure.setValue(Integer.valueOf(nibpSystolicPressure));

		// *********NIBP DIASTOLIC PRESSURE********//
		nibpDiastolicPressure = (short) (((completeMessage[50] & 0xFF) << 8) | completeMessage[49] & 0xFF);
//		nibpDiastolicPressure = (short) (((completeMessage[51] & 0xFF) << 8) | completeMessage[50] & 0xFF);
		NIBPDiastolicPressure.setValue(Integer.valueOf(nibpDiastolicPressure));

		// *********IBP WAVE********//
		ibpWave = (short) (((completeMessage[58] & 0xFF) << 8) | completeMessage[57] & 0xFF);
//		ibpWave = (short) (((completeMessage[59] & 0xFF) << 8) | completeMessage[58] & 0xFF);
		IBPWave.setValue(Integer.valueOf(ibpWave));

		// *********IBP SYSTOLIC PRESSURE********//
		ibpSystolicPressure = (short) (((completeMessage[70] & 0xFF) << 8) | completeMessage[69] & 0xFF);
//		ibpSystolicPressure = (short) (((completeMessage[71] & 0xFF) << 8) | completeMessage[70] & 0xFF);
		IBPSystolicPressure.setValue(Integer.valueOf(ibpSystolicPressure));

		// *********IBP DIASTOLIC PRESSURE********//
		ibpDiastolicPressure = (short) (((completeMessage[72] & 0xFF) << 8) | completeMessage[71] & 0xFF);
//		ibpDiastolicPressure = (short) (((completeMessage[73] & 0xFF) << 8) | completeMessage[72] & 0xFF);
		IBPDiastolicPressure.setValue(Integer.valueOf(ibpDiastolicPressure));
	}

	@Override
	public MedicalDeviceData getDeviceData()
	{
		return medicalDeviceData;
	}

	@Override
	public void terminate()
	{
		// TODO Auto-generated method stub
		
	}
}
