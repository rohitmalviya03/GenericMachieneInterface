/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package com.cdac.device.deviceInjectomat;

import org.apache.log4j.Logger;

import com.cdac.device.deviceDemoA.DeviceDemoAHandshake;
import com.cdac.framework.AbstractDeviceHandshake;
import com.cdac.framework.CoreDevice;
import com.cdac.framework.IPacketMakerStrategy;
import com.cdac.framework.exceptions.CustomPacketMakerNotFoundException;
import com.cdac.framework.exceptions.ListeningEventNotAvailableException;
import com.cdac.framework.exceptions.StartBytesNotAvailableException;
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortEvent;

public class DeviceInjectomatHandshake extends AbstractDeviceHandshake
{
	private static final Logger LOG = Logger.getLogger(DeviceDemoAHandshake.class);

	static int deviceCounter;
	String serialNumber;
	DeviceInjectomatParser coreDev;
	private InjectomatPacketMaker injectomatPacketMaker;

	@Override
	public String getNameOfDevice()
	{
		return "DeviceInjectomatHandshake";
	}

	@Override
	public void initHandshakeLogic()
	{
		// System.out.println("handshakelogic of injectomat");
		// Opportunity to configure port. Example:
		setComPortParameters(115200, 8, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY);

		byte[] startByte = { 27, 96, 0, 0 };
		injectomatPacketMaker = new InjectomatPacketMaker(startByte, 123);
		// try
		// {
		// thePort = (SerialPort) port.open("DeviceInjectomat", 1000);
		// thePort.setSerialPortParams(115200, SerialPort.DATABITS_8,
		// SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
		// LOG.info("Identifying DeviceInjectomat on serial port:" + thePort);
			/**
			 * Logic for sending commands to the port, reading the output
			 * subsequently. The implementer can choose to read repeatedly or
			 * through listener registration. If listener is used, the
			 * implementation should make sure that it is removed also once the
			 * identification is done. If implementer chooses to repeatedly read
			 * from the port, he should make sure that he doesn't block the
			 * thread in doing so.
			 */

			/**
			 * If port identification is successful, set the identifiedPort such
			 * that it can be used by getExecutorInstance()
			 */
		// this.identifiedPort = thePort;
		// }
		// catch (PortInUseException e)
		// {
		// e.printStackTrace();
		// }
		// catch (UnsupportedCommOperationException e)
		// {
		// e.printStackTrace();
		// }

		/**
		 * Remove any listeners that may have been added to this port before
		 * returning.
		 */
		// return true;
	}

	@Override
	public CoreDevice getExecutorInstance()
	{
		try
		{
			coreDev = new DeviceInjectomatParser("DeviceInjectomat-" + serialNumber, 123);
			LOG.info("Device instance:" + coreDev);
			LOG.info("Serial No. is " + serialNumber);
			return coreDev;
		}
		catch (CustomPacketMakerNotFoundException e)
		{
			// The exception will not occur as CustomPacketMaker Strategy is not
			// used for this device.
			return null;
		}
	}

	@Override
	public void deviceEventOccurred(SerialPortEvent dataFromPort)
	{
		LOG.info("**Data in handshake:" + dataFromPort);
		// Analyze the data here and decide if the device is identified or not.
		if (dataFromPort.getEventType() == SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
		{
			// Indicate to the framework that handshake is successful.
			if (dataFromPort.getReceivedData()[0] == 27 && dataFromPort.getReceivedData()[7] == 66)
			{
				byte[] packet = dataFromPort.getReceivedData();
				byte[] serialNo = new byte[16];
				int serialNoCursor = 0;
				for (int index = 12; index <= 26; index++)
					serialNo[serialNoCursor++] = packet[index];

				serialNumber = new String(serialNo);
				setHandshakeStatus(HandshakeStatus.SUCCESSFUL);
			}
			else
			{
				setHandshakeStatus(HandshakeStatus.NOT_SUCCESSFUL);
			}
		}

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
	public int getListeningEvent() throws ListeningEventNotAvailableException
	{
		return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
	}
}

