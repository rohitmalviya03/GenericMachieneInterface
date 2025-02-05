/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package com.cdac.device.deviceInjectomat;

import java.util.List;

import org.apache.log4j.Logger;

import com.cdac.framework.CoreDevice;
import com.cdac.framework.IPacketMakerStrategy;
import com.cdac.framework.CoreDevice.Parsing;
import com.cdac.framework.datastructures.MedicalDeviceData;
import com.cdac.framework.datastructures.NodeBase;
import com.cdac.framework.datastructures.Parameter;
import com.cdac.framework.exceptions.CustomPacketMakerNotFoundException;
import com.cdac.framework.exceptions.StartBytesNotAvailableException;
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortEvent;

public class DeviceInjectomatParser extends CoreDevice
{
	private static final Logger LOG = Logger.getLogger(DeviceInjectomatParser.class);
	MedicalDeviceData<List<Byte>> medicalDeviceData;

	public static final String DEVICE_NAME = "DeviceInjectomat";
	public static final String INFUSION_CHANNEL1_POWERMODE = "InfusionPump.DeviceStatus.PowerMode";
	public static final String INFUSION_CHANNEL1_SERIALNUMBER = "InfusionPump.DeviceStatus.SerialNumber";
	public static final String INFUSION_CHANNEL2_INFUSIONMODE = "InfusionPump.Therapy.InfusionMode";
	public static final String INFUSION_CHANNEL2_DEVICESTEPS = "InfusionPump.Therapy.DeviceSteps";
	public static final String INFUSION_CHANNEL2_SYRINGENAME = "InfusionPump.Therapy.SyringeName";
	public static final String INFUSION_CHANNEL2_DRUGNAME = "InfusionPump.Therapy.DrugName";
	public static final String INFUSION_CHANNEL2_INFUSIONRATE = "InfusionPump.Therapy.InfusionRate";
	public static final String INFUSION_CHANNEL2_INFUSEDVOLUME = "InfusionPump.Therapy.InfusedVolume";
	public static final String INFUSION_CHANNEL2_VOLUMELEFT = "InfusionPump.Therapy.VolumeLeft";
	public static final String INFUSION_CHANNEL2_PRESSURE = "InfusionPump.Therapy.Pressure";
	public static final String INFUSION_CHANNEL2_ALARM = "InfusionPump.Therapy.Alarm";

	private Parameter parameterPowerMode;
	private Parameter parameterSerialNumber;
	private Parameter parameterInfusionMode;
	private Parameter parameterDeviceSteps;
	private Parameter parameterSyringeName;
	private Parameter parameterDrugName;
	private Parameter parameterInfusionRate;
	private Parameter parameterInfusedVolume;
	private Parameter parameterVolumeLeft;
	private Parameter parameterPressure;
	private Parameter<String> parameterAlarm;

	private InjectomatPacketMaker injectomatPacketMaker;

	public DeviceInjectomatParser(String workName, int messageLength) throws CustomPacketMakerNotFoundException
	{
		super(workName, messageLength, null, PacketingStrategy.MESSAGE_LENGTH);
		LOG.info("DeviceInjectomatParser constructor called");
		byte[] startByte = { 27, 96, 0, 0 };
		injectomatPacketMaker = new InjectomatPacketMaker(startByte, 123);
		medicalDeviceData = createIHEDataObject();
	}

	@Override
	public PacketingStrategy getPacketingStrategy()
	{
		return PacketingStrategy.CUSTOM;
	}

	@Override
	public IPacketMakerStrategy getCustomPacketMaker() throws CustomPacketMakerNotFoundException
	{
		return injectomatPacketMaker;
	}

	@Override
	public byte[] getStartBytes() throws StartBytesNotAvailableException
	{
		byte[] startByte = { 27, 96, 0, 0 };
		return startByte;
	}

	@Override
	public int getPacketSize()
	{
		return 123;
	}

	@Override
	public String getNameOfDevice()
	{
		return super.getNameOfDevice();
	}

	@Override
	protected MedicalDeviceData<List<Byte>> createIHEDataObject()
	{

		MedicalDeviceData deviceData = new MedicalDeviceData<List<Byte>>("DeviceInjectomat");

		parameterPowerMode = (Parameter) deviceData.createOrGetParameter(INFUSION_CHANNEL1_POWERMODE)[2];
		parameterSerialNumber = (Parameter) deviceData.createOrGetParameter(INFUSION_CHANNEL1_SERIALNUMBER)[2];
		parameterInfusionMode = (Parameter) deviceData.createOrGetParameter(INFUSION_CHANNEL2_INFUSIONMODE)[2];
		parameterDeviceSteps = (Parameter) deviceData.createOrGetParameter(INFUSION_CHANNEL2_DEVICESTEPS)[2];
		parameterSyringeName = (Parameter) deviceData.createOrGetParameter(INFUSION_CHANNEL2_SYRINGENAME)[2];
		parameterDrugName = (Parameter) deviceData.createOrGetParameter(INFUSION_CHANNEL2_DRUGNAME)[2];
		parameterInfusionRate = (Parameter) deviceData.createOrGetParameter(INFUSION_CHANNEL2_INFUSIONRATE)[2];
		parameterInfusedVolume = (Parameter) deviceData.createOrGetParameter(INFUSION_CHANNEL2_INFUSEDVOLUME)[2];
		parameterVolumeLeft = (Parameter) deviceData.createOrGetParameter(INFUSION_CHANNEL2_VOLUMELEFT)[2];
		parameterPressure = (Parameter) deviceData.createOrGetParameter(INFUSION_CHANNEL2_PRESSURE)[2];
		parameterAlarm = (Parameter) deviceData.createOrGetParameter(INFUSION_CHANNEL2_ALARM)[2];

		return deviceData;
	}

	@Override
	public NodeBase getNodeByIdentifier(String parameterIdentifier)
	{
		NodeBase param = null;
		switch (parameterIdentifier)
		{
		case INFUSION_CHANNEL1_POWERMODE:
			param = parameterPowerMode;
			break;

		case INFUSION_CHANNEL1_SERIALNUMBER:
			param = parameterSerialNumber;
			break;

		case INFUSION_CHANNEL2_INFUSIONMODE:
			param = parameterInfusionMode;
			break;

		case INFUSION_CHANNEL2_DEVICESTEPS:
			param = parameterDeviceSteps;
			break;

		case INFUSION_CHANNEL2_SYRINGENAME:
			param = parameterSyringeName;
			break;

		case INFUSION_CHANNEL2_DRUGNAME:
			param = parameterDrugName;
			break;

		case INFUSION_CHANNEL2_INFUSIONRATE:
			param = parameterInfusionRate;
			break;

		case INFUSION_CHANNEL2_INFUSEDVOLUME:
			param = parameterInfusedVolume;
			break;

		case INFUSION_CHANNEL2_VOLUMELEFT:
			param = parameterVolumeLeft;
			break;

		case INFUSION_CHANNEL2_PRESSURE:
			param = parameterPressure;
			break;

		case INFUSION_CHANNEL2_ALARM:
			param = parameterAlarm;
			break;

		default:
			param = null;

		}

		return param;
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

	public void parsingLogic(byte[] completeMessage)
	{
		byte[] drugName = new byte[20];
		byte[] syringeName = new byte[20];
		byte[] serialNumber = new byte[16];

		int drugNameCursor = 0;
		int syringeNameCursor = 0;
		int serialNumberCursor = 0;

		StringBuilder flow = new StringBuilder();
		StringBuilder infVolume = new StringBuilder();
		StringBuilder volLeft = new StringBuilder();
		StringBuilder pressure = new StringBuilder();

		String drug = null;
		String syringe = null;
		String rate = null;
		String infusedVolume = null;
		String volumeLeft = null;
		String pressureOfDevice = null;

		// ****** Power Mode ******//
		if (completeMessage[39] == 02)
			parameterPowerMode.setValue("Idle Mode");
		else
			parameterPowerMode.setValue("Switched On");

		// ****** Infusion Mode ******//
		if (completeMessage[43] == 01)
			parameterInfusionMode.setValue("Basic Infusion");
		else if (completeMessage[43] == 03)
			parameterInfusionMode.setValue("Simple Bolus");


		// ****** Device Steps ******//
		if (completeMessage[40] == 01)
			parameterDeviceSteps.setValue("Syringe Selection");
		else if (completeMessage[40] == 04)
			parameterDeviceSteps.setValue("Drug Selection");
		else if (completeMessage[40] == 13)
			parameterDeviceSteps.setValue("Waiting before launching infusion");
		else if (completeMessage[40] == 16)
		{
			parameterDeviceSteps.setValue("Infusion is in progess");
			// System.out.println("Infusion is in progess");
		}
		else if (completeMessage[40] == 32)
		{
			parameterDeviceSteps.setValue("Infusion Stopped");
			// System.out.println("Infusion stopped");

		}
		else if (completeMessage[40] == 48)
			parameterDeviceSteps.setValue("Booting Steps");

		// ****** Serial Number ******//
		for (int index = 12; index <= 26; index++)
			serialNumber[serialNumberCursor++] = completeMessage[index];
		String serialNo = new String(serialNumber);
		parameterSerialNumber.setValue(serialNo);
		System.out.println("**************************" + serialNo);

		// ****** Drug Name ******//
		for (int index = 50; index <= 69; index++)
			drugName[drugNameCursor++] = completeMessage[index];
		drug = new String(drugName);
		parameterDrugName.setValue(drug);
		System.out.println("!!!!!!!DRUG NAME!!!!!!" + drug);

		// ****** Syringe Name ******//
		for (int index = 91; index <= 110; index++)
			syringeName[syringeNameCursor++] = completeMessage[index];
		syringe = new String(syringeName);
		parameterSyringeName.setValue(syringe);

		// ****** Infusion Rate ******//
		for (int index = 46; index <= 49; index++)
			flow.append(String.format("%02X", completeMessage[index]));

		rate = flow.toString();
		parameterInfusionRate.setValue(InfusionUtility.getInstance().convertToDecimal(rate, rate.length()) + " "
		        + InfusionUtility.getInstance().unitEncoding(completeMessage[70], completeMessage[71]));

		// ****** Infused Volume ******//
		for (int index = 72; index <= 75; index++)
			infVolume.append(String.format("%02X", completeMessage[index]));

		infusedVolume = infVolume.toString();
		System.out
		        .println("***" + InfusionUtility.getInstance().convertToDecimal(infusedVolume, infusedVolume.length()));
		parameterInfusedVolume.setValue(InfusionUtility.getInstance().convertToDecimal(infusedVolume, infusedVolume.length()) + " ml");


		// ****** Volume Left ******//
		for (int index = 86; index <= 89; index++)
			volLeft.append(String.format("%02X", completeMessage[index]));

		volumeLeft = volLeft.toString();
		parameterVolumeLeft.setValue(InfusionUtility.getInstance().convertToDecimal(volumeLeft, volumeLeft.length())+ " ml");

		// ****** Pressure ******//
		for (int index = 76; index <= 77; index++)
			pressure.append(String.format("%02X", completeMessage[index]));

		pressureOfDevice = pressure.toString();
		parameterPressure.setValue(
		        InfusionUtility.getInstance().convertToDecimal(pressureOfDevice, pressureOfDevice.length()) + " mmHg");

		// ****** Alarm Status ******//
		parameterAlarm.setValue(InfusionUtility.getInstance().alarmStatus(completeMessage[28], completeMessage[29],
		        completeMessage[30], completeMessage[31]));
		if (!parameterAlarm.getParameterData().equals(""))
		{
			sendDeviceAlertsAndAlarmsNotifications(parameterAlarm.getParameterData());
		}

	}

	@Override
	public int getListeningEvent()
	{
		return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
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
