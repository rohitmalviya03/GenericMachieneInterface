/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package com.cdac.framework;

import com.cdac.framework.exceptions.CustomPacketMakerNotFoundException;
import com.cdac.framework.exceptions.ListeningEventNotAvailableException;
import com.cdac.framework.exceptions.PacketSizeNotDefinedException;
import com.cdac.framework.exceptions.StartBytesNotAvailableException;
import com.fazecast.jSerialComm.SerialPortEvent;

public interface IMedicalDevice
{
	enum PacketingStrategy
	{
		MESSAGE_LENGTH, START_BYTES, CUSTOM
	}

	public String getNameOfDevice();

	public void deviceEventOccurred(SerialPortEvent dataFromPort);

	public PacketingStrategy getPacketingStrategy();
	
	/**
	 * Request Snapshot Capture.
	 * @return true if the request is placed successfully. 
	 * Returns false when the selected device is not a parser or if another request is already in progress.
	 */
	default boolean requestSnapshot()
	{
		return false;
	}

	/**
	 * Devices must override this method if they are providing (through
	 * {@link PacketingStrategy#CUSTOM} their own implementation for packet
	 * maker.
	 *
	 * @return a reference to packet maker that the framework will use to create
	 *         a data packet for the devices.
	 * @throws CustomPacketMakerNotFoundException
	 */
	default IPacketMakerStrategy getCustomPacketMaker() throws CustomPacketMakerNotFoundException
	{
		throw new CustomPacketMakerNotFoundException(getNameOfDevice());
	}

	/**
	 * Override this to provide start bytes which will be used to identify the
	 * data packet in raw stream of data.
	 *
	 * @return an array of start bytes.
	 * @throws StartBytesNotAvailableException
	 */
	default byte[] getStartBytes() throws StartBytesNotAvailableException
	{
		throw new StartBytesNotAvailableException(getNameOfDevice());
	}

	default int getPacketSize() throws PacketSizeNotDefinedException
	{
		throw new PacketSizeNotDefinedException(getNameOfDevice());
	}

	default int getListeningEvent() throws ListeningEventNotAvailableException
	{
		throw new ListeningEventNotAvailableException(getNameOfDevice());
	}
}
