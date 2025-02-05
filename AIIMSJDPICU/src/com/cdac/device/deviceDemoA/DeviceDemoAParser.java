/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package com.cdac.device.deviceDemoA;

import java.util.List;

import org.apache.log4j.Logger;

import com.cdac.framework.CoreDevice;
import com.cdac.framework.datastructures.Channel;
import com.cdac.framework.datastructures.MedicalDeviceData;
import com.cdac.framework.datastructures.NodeBase;
import com.cdac.framework.datastructures.Parameter;
import com.cdac.framework.exceptions.CustomPacketMakerNotFoundException;
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortEvent;

@Deprecated
public class DeviceDemoAParser extends CoreDevice
{

	public static final Logger LOG = Logger.getLogger(DeviceDemoAParser.class);
	public static final String VMD_VENTILATOR = "Ventilator";
	public static final String VMD_HEART_MONITOR = "HeartRateMonitor";
	public static final String VENTILATOR_CHANNEL1 = "Channel1";
	public static final String VENTILATOR_CHANNEL2 = "Channel2";
	public static final String HEART_RATE_CHANNEL1 = "Channel1";
	public static final String HEART_RATE_CHANNEL2 = "Channel2";
	public static final String VENT_CHANNEL1_PARAM1 = "VentChannelParameter1";
	public static final String VENT_CHANNEL1_PARAM2 = "Parameter2";
	public static final String VENT_CHANNEL2_PARAM1 = "Parameter1";
	public static final String HR_CHANNEL1_PARAM1 = "Parameter1";
	public static final String HR_CHANNEL2_PARAM1 = "Parameter1";
	public static final String HR_CHANNEL2_PARAM2 = "Parameter2";

	public static final String BRANCH_EXAMPLE1 = "FetalMonitor.Channel1.Parameter1";
	public static final String BRANCH_EXAMPLE2 = "FetalMonitor.Channel1.Parameter2";
	public static final String BRANCH_EXAMPLE3 = VMD_VENTILATOR + "." + VENTILATOR_CHANNEL2 + ".Parameter3";

	private Parameter ventChannel1_param1;
	private Parameter ventChannel1_param2;
	private Parameter ventChannel2_param1;
	private Parameter hrChannel1_param1;
	private Parameter hrChannel2_param1;
	private Parameter hrChannel2_param2;
	private Parameter branchParameter1;
	private Parameter branchParameter2;
	private Parameter branchParameter3;

	private Channel ventChannel1;
	private boolean toggle;

	SerialPort portForThisDevice; // May be used to send commands to this
									// device.
	MedicalDeviceData<List<Byte>> medicalDeviceData;

	public DeviceDemoAParser(String workName, int messageLength)
			throws CustomPacketMakerNotFoundException
	{
		super(workName, messageLength, null, PacketingStrategy.MESSAGE_LENGTH);
		// new TestUI(this); // This usually will not be done like this and UI
							// should access this through a frwk service.
		LOG.info("DeviceDemoAParser constructor called");
	}

	@Override
	public int getPacketSize()
	{
		return 8;
	}

	@Override
	protected MedicalDeviceData<List<Byte>> createIHEDataObject()
	{
		medicalDeviceData = new MedicalDeviceData<List<Byte>>("DeviceDemoA");
		
		// try
		// {
			//XXX: Following mechanisms of creating VMDs have been removed.
			// Create VMDs
//			VirtualMedicalDevice<List<Byte>> ventilator = medicalDeviceData.addVMD(VMD_VENTILATOR, "Ventilator");
//			VirtualMedicalDevice<List<Byte>> heartRate = medicalDeviceData.addVMD(VMD_HEART_MONITOR,
			// "Heart Rate Monitor");

			// Create VMD specific channels
			// ventChannel1 = ventilator.addChannel(VENTILATOR_CHANNEL1,
			// "Channel1");
			// Channel ventChannel2 = ventilator.addChannel(VENTILATOR_CHANNEL2,
			// "Channel2");
			// Channel heartRateChannel1 =
			// heartRate.addChannel(HEART_RATE_CHANNEL1, "Channel1");
			// HeartRateChannel heartRateCustomChannel2 = (HeartRateChannel)
			// heartRate.addChannel(HEART_RATE_CHANNEL2,
			// new HeartRateChannel("Channel2", heartRate));

			// Create parameters
			// ventChannel1_param1 =
			// ventChannel1.addParameter(VENT_CHANNEL1_PARAM1, "OxygenLevel");
			// ventChannel1_param2 =
			// ventChannel1.addParameter(VENT_CHANNEL1_PARAM2, "CO2_Level");
			//
			// ventChannel2_param1 =
			// ventChannel1.addParameter(VENT_CHANNEL2_PARAM1, "Status");

			// Both parameters below are basically same.
			// AtriumParameter atParam = new AtriumParameter("A",
			// heartRateChannel1);
			// hrChannel1_param1 =
			// heartRateChannel1.addParameter(HR_CHANNEL1_PARAM1, atParam);
			//
			// hrChannel2_param1 =
			// heartRateCustomChannel2.addParameter(HR_CHANNEL2_PARAM1, "LV");
			// hrChannel2_param2 =
			// heartRateCustomChannel2.addParameter(HR_CHANNEL2_PARAM2, "RV");

			branchParameter1 = (Parameter) medicalDeviceData.createOrGetParameter(BRANCH_EXAMPLE1)[2];
			branchParameter2 = (Parameter) medicalDeviceData.createOrGetParameter(BRANCH_EXAMPLE2)[2];
			branchParameter3 = (Parameter) medicalDeviceData.createOrGetParameter(BRANCH_EXAMPLE3)[2];
		// }
		// catch (KeyExistsException exception)
		// {
		// LOG.error(exception.getMessage());
		// }

		return medicalDeviceData;
	}

	@Override
	public NodeBase getNodeByIdentifier(String parameterIdentifier)
	{
		NodeBase param = null;
		switch (parameterIdentifier)
		{
		case VENT_CHANNEL1_PARAM1:
			param = ventChannel1_param1;
			break;

		case VENT_CHANNEL1_PARAM2:
			param = ventChannel1_param2;
			break;

		case VENT_CHANNEL2_PARAM1:
			param = ventChannel2_param1;
			break;
			
		case VENTILATOR_CHANNEL1:
			param = ventChannel1;

		default:
			param = null;
		}

		return param;
	}

	@Override
	public void deviceEventOccurred(SerialPortEvent dataFromPort)
	{
		// Set the data in parameters here.
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
