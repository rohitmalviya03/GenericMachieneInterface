/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package com.cdac.device.anesthesia;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.log4j.Logger;

import com.cdac.framework.AbstractDeviceHandshake;
import com.cdac.framework.CoreDevice;
import com.cdac.framework.IPacketMakerStrategy;
import com.cdac.framework.exceptions.CustomPacketMakerNotFoundException;
import com.cdac.framework.exceptions.StartBytesNotAvailableException;
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortEvent;

public class AnesthesiaHandshake extends AbstractDeviceHandshake
{

	private static final Logger LOG = Logger.getLogger(AnesthesiaHandshake.class);
	static int deviceCounter;
	AnesthesiaParser coreDev;
	private PacketMakerOnStartByteAndEndByteBasis packetMakerOnStartByteAndEndByteBasis;
	static boolean isCommandToBeWritten = false;
	static int forSendingCommands = 0;
	private static final byte[] changeDatexVer = { 0x1b, 0x56, 0x54, 0x4f, 0x31, 0x32, 0x20, 0x0d };
	private static final byte[] enableAutoMode = { 0x1b, 0x56, 0x54, 0x58, 0x20, 0x0d };
	private static final byte[] reqWaveform = { 0x1b, 0x56, 0x54, 0x77, 0x50, 0x30, 0x30, 0x30, 0x30, 0x30, 0x20,
	        0x0d };


	@Override
	public String getNameOfDevice()
	{
		// TODO Auto-generated method stub
		return "AnesthesiaHandshake";
	}

	@Override
	public PacketingStrategy getPacketingStrategy()
	{
		// TODO Auto-generated method stub
		return PacketingStrategy.CUSTOM;
	}

	@Override
	public IPacketMakerStrategy getCustomPacketMaker() throws CustomPacketMakerNotFoundException
	{
		return packetMakerOnStartByteAndEndByteBasis;
	}

	@Override
	public void initHandshakeLogic()
	{
		// TODO Auto-generated method stub
		OutputStream out = getOutputStream();
		setComPortParameters(19200, 7, SerialPort.ONE_STOP_BIT, SerialPort.ODD_PARITY);
		packetMakerOnStartByteAndEndByteBasis = new PacketMakerOnStartByteAndEndByteBasis(new byte[] { 58, 86, 84 },
		        new byte[] { 13 });
		byte[] disableChecksum = new byte[] { 0x1b, 0x56, 0x54, 0x44, 0x77, 0x0d };
		System.out.println("!!!!!!!!!!!!!!!!!COMMAND!!!!!!!!!!!!");
		for (byte b : disableChecksum)
		{
			// System.out.print(" " + b);
		}
		try
		{
			if (out != null)
			{
				System.out.println("OUTPUT STREAM VALUE!!!!!!!!!!!!!!!!!" + out);
				out.write(disableChecksum);

				out.flush();
			}
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			LOG.error("## Exception occured:", e);
		}
	}

	@Override
	public void deviceEventOccurred(SerialPortEvent dataFromPort)
	{
		OutputStream out = getOutputStream();
		if (dataFromPort.getEventType() == SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
		{
			// Some complex/simple algorithm
			// Indicate to the framework that handshake is successful.
			if (dataFromPort.getReceivedData()[0] == 89)
			{
				if (forSendingCommands == 3)
				{
					System.out.println("recieved data!!!!!!!!!" + dataFromPort.getReceivedData()[0]);
					setHandshakeStatus(HandshakeStatus.SUCCESSFUL);
				}
				else if (forSendingCommands == 2)
				{
					forSendingCommands++;
					try
					{
						out.write(reqWaveform);
						out.flush();
						System.out.println("OUTPUT STREAM VALUE!!!!!!!!!!!!!!!!!" + out);
					}
					catch (IOException e)
					{
						e.printStackTrace();
						LOG.error("## Exception occured:", e);
					}
					System.out.println("!!!!!!!!!value!!!!! :" + forSendingCommands);

				}
				else if (forSendingCommands == 1)
				{
					forSendingCommands++;
					try
					{
						out.write(enableAutoMode);
						out.flush();
					}
					catch (IOException e)
					{
						e.printStackTrace();
						LOG.error("## Exception occured:", e);
					}
					System.out.println("!!!!!!!!!value!!!!! :" + forSendingCommands);
				}
				else if (forSendingCommands == 0)
				{
					forSendingCommands++;
					try
					{
						out.write(changeDatexVer);
						out.flush();
					}
					catch (IOException e)
					{
						e.printStackTrace();
						LOG.error("## Exception occured:", e);
					}
					System.out.println("!!!!!!!!!value!!!!! :" + forSendingCommands);
				}
				else
				{
					setHandshakeStatus(HandshakeStatus.NOT_SUCCESSFUL);
				}
			}
		}
	}

	@Override
	public CoreDevice getExecutorInstance()
	{
		try
		{
			coreDev = new AnesthesiaParser("Anesthesia-" + deviceCounter, 8);
			LOG.info("Device instance:" + coreDev);
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
	public byte[] getStartBytes() throws StartBytesNotAvailableException
	{
		// TODO Auto-generated method stub
		byte[] arrayOfStartBytes = { 58, 86, 84 };
		return arrayOfStartBytes;
	}

	@Override
	public int getListeningEvent()
	{
		return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
	}

}
