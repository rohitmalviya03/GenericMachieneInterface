/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package com.cdac.framework;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

import com.cdac.framework.common.DeviceStatusNotifications;
import com.cdac.framework.exceptions.CustomPacketMakerNotFoundException;
import com.cdac.framework.exceptions.ListeningEventNotAvailableException;
import com.cdac.framework.exceptions.PacketSizeNotDefinedException;
import com.cdac.framework.exceptions.StartBytesNotAvailableException;
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.fazecast.jSerialComm.SerialPortPacketListener;

public class PortManagerAndListener
{
	private static final Logger LOG = Logger.getLogger(PortManagerAndListener.class);
	private SerialPort serialPort;
	private IMedicalDevice device;
	private int packetSize;
	private SerialPortDataListener rawDataListener;
	private SerialPortPacketListener packetDataListener;
	private PacketMakerOnStartByteBasis packetMakerOnStartByteBasis;
	private ProcessData dataProcessor;
	private InputStream inputStream;
	private OutputStream outputStream;
	private int deviceTimeOut;

	public PortManagerAndListener(SerialPort port)
	{
		this.serialPort = port;

		dataProcessor = new ProcessData();
		rawDataListener = new SerialRawDataListener();
		packetDataListener = new SerialPacketListener();

		// Open the port.
		LOG.info("Opening port:" + serialPort.getDescriptivePortName());
		this.serialPort.openPort();
		LOG.info("Port opened:" + serialPort.getDescriptivePortName());
	}

	public void setComPortParameters(int baudRate, int dataBits, int stopBits, int parity)
	{
		serialPort.setComPortParameters(baudRate, dataBits, stopBits, parity);
	}

	/**
	 * Returns an java.io.InputStream object associated with this serial port.
	 *
	 * @return inputStream for reading data directly from the stream.
	 */
	public InputStream getInputStream()
	{
		inputStream = serialPort.getInputStream();
		return inputStream;
	}

	/**
	 * Returns an java.io.OutputStream object associated with this serial port.
	 *
	 * @return outputStream for sending commands in form of bytes to the device
	 *         attached to the port.
	 */
	public OutputStream getOutputStream()
	{
		outputStream = serialPort.getOutputStream();
		return outputStream;
	}

	public void setDeviceListeningToThisPort(IMedicalDevice device)
	{
		// Remove existing listener
		LOG.info("Removing existing listeners on port:" + serialPort.getDescriptivePortName());
		stopListening();

		LOG.info("Adding new listener on port:" + serialPort.getDescriptivePortName() + " for device:"
		        + device.getNameOfDevice());
		this.device = device;
		if(device instanceof CoreDevice)
		{
			String deviceType = ((CoreDevice)device).getNameOfDevice().split("-")[0];
			deviceTimeOut = SystemConfiguration.getInstance().getDeviceTimeOut(deviceType);
			LOG.info("Device timeout for " + deviceType + " is " + deviceTimeOut);
		}

		// Add appropriate listener for this device's packeting strategy.
		switch(device.getPacketingStrategy())
		{
			case MESSAGE_LENGTH:
				try
				{
					LOG.info("Setting packet size:" + device.getPacketSize());
					setPacketSize(device.getPacketSize());
				}
				catch (PacketSizeNotDefinedException e)
				{
					// TODO: Programming error. Log, Re-throw and halt
					e.printStackTrace();
					LOG.error("## Exception occured:", e);
				}
				serialPort.addDataListener(packetDataListener);
				break;

			case START_BYTES:
				try
				{
					LOG.info("Setting start bytes:" + device.getStartBytes());
					packetMakerOnStartByteBasis = new PacketMakerOnStartByteBasis(device.getStartBytes());
				System.out.println("!!!!!!!!!!PacketMaker!!!!!!!!" + packetMakerOnStartByteBasis);
				}
				catch (StartBytesNotAvailableException ex)
				{
					// TODO Programming error. Log, re-throw and halt.
					ex.printStackTrace();
				}
				// break not needed as rawDataListener is required.
			default:
				LOG.info("Adding raw data listener");
				serialPort.addDataListener(rawDataListener);
				break;

		}
	}

	void checkDeviceResponseTime()
	{
		if((device != null) && (device instanceof CoreDevice))
		{
			CoreDevice deviceParser = (CoreDevice) device;
			Date timeOfLastResponse = deviceParser.getLastDataProcessedTime();
			Date currentTime = new Date(System.currentTimeMillis());
			long timeDiff = (currentTime.getTime() - timeOfLastResponse.getTime())/1000 % 60;
			if (timeDiff > deviceTimeOut)
			{
				// The connection is dead.
				deviceParser.performTermination();
				DeviceStatusNotifications deviceTimeOut = new DeviceStatusNotifications(DeviceStatusNotifications.DEVICE_NOT_RESPONDING);
				deviceTimeOut.setData(device);
				CommunicationService.getInstance().sendNotification(deviceTimeOut);
				this.device = null;
			}
			else
			{
				// Do Nothing.
			}
		}
		else
		{
			// Do Nothing.
		}
	}

	public IMedicalDevice getDeviceListeningToThisPort()
	{
		return device;
	}

	SerialPort getThisPort()
	{
		return serialPort;
	}

	void stopListening()
	{
		LOG.info("Stopping device:" + device + " to listen on port:" + getPortName());
		if (inputStream != null)
		{
			try
			{
				inputStream.close();
			}
			catch (IOException e)
			{
				LOG.warn("Exception closing the input stream:" + e.getMessage());
			}
		}

		if (outputStream != null)
		{
			try
			{
				outputStream.close();
			}
			catch (IOException e)
			{
				LOG.warn("Exception closing the output stream:" + e.getMessage());
			}
		}
		serialPort.removeDataListener();
		device = null;
		packetMakerOnStartByteBasis = null; // Clean-up
	}

	public void setPacketSize(int size)
	{
		this.packetSize = size;
	}

	public String getPortName()
	{
		return serialPort.getDescriptivePortName();
	}

	private class SerialRawDataListener implements SerialPortDataListener
	{
		@Override
		public int getListeningEvents()
		{
			try
			{
				return device.getListeningEvent();
			}
			catch (ListeningEventNotAvailableException e)
			{
				// TODO Programmatic error. Log, re-throw and halt.
				e.printStackTrace();
				LOG.error("## Exception occured:", e);
			}
			return 0;
		}

		@Override
		public void serialEvent(SerialPortEvent arg0)
		{
			dataProcessor.addEvent(arg0);
			dataProcessor.execute();
		}
	}

	private class SerialPacketListener extends SerialRawDataListener implements SerialPortPacketListener
	{
		@Override
		public int getPacketSize()
		{
			return packetSize;
		}
	}

	public void closePort()
	{
		serialPort.closePort();
	}

	private class ProcessData extends Work
	{
		BlockingQueue<SerialPortEvent> eventsQueue;

		public ProcessData()
		{
			super("ProcessData");
			eventsQueue = new LinkedBlockingQueue<SerialPortEvent>();
		}

		public void addEvent(SerialPortEvent event)
		{
			try
			{
				eventsQueue.add(event);
			}
			catch (IllegalStateException ex)
			{
				// XXX: This LOG will be crucial for load testing and check the
				// breaking point of the system. Printing of this log means that
				// the system is not able to keep up with the speed at which the
				// device is throwing in the data.
				LOG.info("### Queue overload. For medical device:" + device.getNameOfDevice());
				ex.printStackTrace();
			}
		}

		@Override
		public synchronized Object workLogic()
		{
			if (device != null)
			{
				switch (device.getPacketingStrategy())
				{

				case START_BYTES:
					packetUsingStartBytes();
					break;

				case CUSTOM:
					packetUsingCustomMethod();
					break;

				case MESSAGE_LENGTH:
				default:
					packetUsingMessageLength();
					break;
				}
			}
			return null;
		}

		private void packetUsingStartBytes()
		{
			SerialPortEvent portEvent;
			while ((portEvent = eventsQueue.poll()) != null)
			{
				int bytesToRead = portEvent.getSerialPort().bytesAvailable();
				byte[] receivedBytes = new byte[bytesToRead];
				portEvent.getSerialPort().readBytes(receivedBytes, bytesToRead);

				if (SystemConfiguration.getInstance().getRawDataLogs())
				{
					StringBuilder rawData = new StringBuilder(
					        "Raw Data received for " + device.getNameOfDevice() + " is: ");
					for (byte a : receivedBytes)
					{
						rawData.append(a + " ");
					}
					LOG.info(rawData);
				}

				Queue<byte[]> dataPacketQueue = packetMakerOnStartByteBasis.makePacket(receivedBytes);
				byte[] dataPacket;
				while ((dataPacket = dataPacketQueue.poll()) != null)
				{
					if (SystemConfiguration.getInstance().getPacketLogs())
					{
						StringBuilder log = new StringBuilder("Packet Created for " + device.getNameOfDevice() + " is: ");
						for (byte a : dataPacket)
						{
							log.append(a + " ");
						}
						LOG.info(log);
					}

					SerialPortEvent updatedData = new SerialPortEvent(portEvent.getSerialPort(),
					        portEvent.getEventType(), dataPacket);

					device.deviceEventOccurred(updatedData);
				}
			}
		}

		private void packetUsingMessageLength()
		{
			SerialPortEvent portEvent;
			while ((portEvent = eventsQueue.poll()) != null)
			{
				if (SystemConfiguration.getInstance().getPacketLogs())
				{
					StringBuilder log = new StringBuilder("Packet Created for " + device.getNameOfDevice() + " is: ");
					for (byte a : portEvent.getReceivedData())
					{
						log.append(a + " ");
					}
					LOG.info(log);
				}

				// No extra processing needed.
				device.deviceEventOccurred(portEvent);
			}
		}

		private void packetUsingCustomMethod()
		{
			SerialPortEvent portEvent;
			while ((portEvent = eventsQueue.poll()) != null)
			{
				try
				{
					int availableBytes = serialPort.bytesAvailable();
						byte[] readData = new byte[availableBytes];
						serialPort.readBytes(readData, availableBytes);
						/*System.out.println("bytes "+availableBytes+" read");
						for (byte b : readData) {
							System.out.print(" "+b);
						}*/
					if (SystemConfiguration.getInstance().getRawDataLogs())
					{
						StringBuilder rawData = new StringBuilder(
						        "Raw Data received for " + device.getNameOfDevice() + " is: ");
						for (byte a : readData)
						{
							rawData.append(a + " ");
						}
						LOG.info(rawData);
					}

						Queue<byte[]> dataPacketQueue = device.getCustomPacketMaker().makePacket(readData);
						byte[] dataPacket;
						while ((dataPacket = dataPacketQueue.poll()) != null)
						{
						if (SystemConfiguration.getInstance().getPacketLogs())
							{
							StringBuilder log = new StringBuilder(
							        "Packet Created for " + device.getNameOfDevice() + " is: ");
							for (byte a : dataPacket)
							{
								log.append(a + " ");
							}
							LOG.info(log);
							}

							SerialPortEvent updatedData = new SerialPortEvent(portEvent.getSerialPort(),
							        portEvent.getEventType(), dataPacket);
							device.deviceEventOccurred(updatedData);
						}
					}
				catch (CustomPacketMakerNotFoundException e)
				{
					// TODO Programming error. Log, Re-Throw and Halt.
					e.printStackTrace();
					LOG.error("## Exception occured:", e);
				}
			}
		}
	}
}
