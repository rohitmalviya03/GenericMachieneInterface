/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package com.cdac.framework;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.commons.collections.buffer.CircularFifoBuffer;
import org.apache.log4j.Logger;

import com.cdac.framework.common.DeviceStatusNotifications;
import com.cdac.framework.common.Notification;
import com.cdac.framework.common.NotificationTypeConstants;
import com.cdac.framework.datastructures.MedicalDeviceData;
import com.cdac.framework.datastructures.NodeBase;
import com.cdac.framework.datastructures.Parameter;
import com.cdac.framework.datastructures.SnapshotDataSample;
import com.cdac.framework.exceptions.CustomPacketMakerNotFoundException;

/**
 * This class would call the workLogic method automatically whenever it has a
 * complete message available to process. Until then it will keep on buffering
 * the received data. The extending classes should:<br>
 * 1. Define the message size by passing the number to the constructor.<br>
 * 2. Define the parsing logic in workLogic().<br>
 * 3. Pass the incoming data from port to the feedData() method.
 *
 * @author Sahil_Jain08
 *
 * @param <>
 *            TODO: Identify approach to avoid data loss through packet
 *            synchronization.<br>
 *            Implementation of handling the parse data: Dump in filesystem or
 *            MongoDB etc.<br>
 *            XXX: A thorough testing should ensure that parsed data is in
 *            proper sequence and there is no data loss.
 */
public abstract class CoreDevice implements IMedicalDevice
{

	Queue<ArrayList<Byte>> completeMessages;
    protected InputStream inStreamSerialPort;

	private PacketingStrategy packetingStrategy;
	private String nameOfDevice;
	private SystemConfiguration configuration;
	private char dataDelimiter;
	private CircularFifoBuffer bufferedSamples;
	private SnapshotWorker snapshotWorker;
	private int snapshotFutureSeconds;
	private volatile boolean snapshotCaptureInProgress = false;
	private volatile Date lastDataProcessedTime;
	private int terminationTimeOut = 2;

	Logger LOG = Logger.getLogger(getClass());

	public enum Parsing
	{
		/** To Mark Successful Parsing **/
		SUCCESSFUL,
		/** To Mark Un-Successful parsing. Scenario - Garbled data received. **/
		NOT_SUCCESSFUL,
		/** To Mark an in progress parsing. Scenario - When more data is awaited from the device to complete the packet. **/
		IN_PROGRESS
	}

    /**
	 * Constructor.
	 *
	 * @param deviceName
	 *            name of the class.
	 * @param messageLength
	 *            is the expected length of each message.
	 * @throws CustomPacketMakerNotFoundException
	 */
	public CoreDevice(String deviceName, int messageLength, ArrayList<Byte> identifierStartBytes,
			PacketingStrategy packetingStrategy)
	{
		nameOfDevice = deviceName;
		this.packetingStrategy = packetingStrategy;

		configuration = SystemConfiguration.getInstance();
		dataDelimiter = configuration.getDelimiterForDataDump();
		String deviceType = nameOfDevice.split("-")[0];
		snapshotWorker = new SnapshotWorker();

		int totalSamples = SystemConfiguration.getInstance().getTotalSnapshotSamples(deviceType);
		snapshotFutureSeconds = SystemConfiguration.getInstance().getFutureTimeForSnapshotRecording(deviceType);
		bufferedSamples = new CircularFifoBuffer(totalSamples);
		lastDataProcessedTime = new Date(System.currentTimeMillis());
	}

	/**
	 * Use this constructor when message packets are to be formulated on the
	 * basis of Start byte(s).
	 *
	 * @param workName
	 *            name of the class
	 * @param identifierStartBytes
	 *            List of bytes that will be used to identify the start of the
	 *            message.
	 * @throws CustomPacketMakerNotFoundException
	 */
	public CoreDevice(String workName, ArrayList<Byte> identifierStartBytes)
	{
		this(workName, 0, identifierStartBytes, PacketingStrategy.START_BYTES);
	}

	@Override
	public PacketingStrategy getPacketingStrategy()
	{
		return packetingStrategy;
	}

	@Override
	public String getNameOfDevice()
	{
		return nameOfDevice;
	}

	/**
	 * This method would be repeatedly called after a particular interval to
	 * return the data at that particular point of time.
	 *
	 * @return data in IHE data structure format.
	 */
	abstract public MedicalDeviceData getDeviceData();

	/**
	 *{@inheritDoc}
	 */
	@Override
	public boolean requestSnapshot()
	{
		if(snapshotCaptureInProgress == false)
		{
			snapshotWorker.executeAfter(snapshotFutureSeconds * 1000);
			return true;
		}
		else
		{
			return false;
		}
	}

	protected void setDataProcessingStatus(Parsing status)
	{
		switch (status)
		{
			case SUCCESSFUL:
				// Data Processed successfully. Let's buffer the data and update the last data time stamp.
				// TODO: Revamp the Notification process to fire notification about parsed data from here instead of from IHE data.
				bufferDataForSnapshot();
				lastDataProcessedTime = new Date(System.currentTimeMillis());
				break;

			case NOT_SUCCESSFUL:
				LOG.warn("Data parsing was not successful for device:" + nameOfDevice);
				break;

			case IN_PROGRESS:
				LOG.info("Data parsing is in progress for device:" + nameOfDevice);
				break;

			default:
				break;
		}
	}

	/**
	 * Gets the time of latest successful data parsing.
	 * @return time of last successful data parsing.
	 */
	Date getLastDataProcessedTime()
	{
		return lastDataProcessedTime;
	}

	/**
	 * Buffers the data for Snapshot capturing. The number of samples to buffer is fetched from the config file.
	 */
	void bufferDataForSnapshot()
	{
		LOG.info("Buffering parsed data of device:" + getNameOfDevice());
		// Get parsed data in parameters and create a copy.
		MedicalDeviceData parsedData = getDeviceData();
		Map<String, Parameter> allParameters = parsedData.getParametersMap();

		// Create the data sample
		SnapshotDataSample dataSample = new SnapshotDataSample();
		for(Map.Entry<String, Parameter> entry : allParameters.entrySet())
		{
			dataSample.addSampleData(entry.getKey(), entry.getValue());
		}

		// Buffer this sample.
		bufferedSamples.add(dataSample);
		//LOG.info("Buffering of snapshot data complete for device:" + getNameOfDevice());
	}

	/**
	 * Sends a critical notification for a device.
	 *
	 * @param notificationMessage
	 *            message about the notification
	 */
	protected void sendDeviceAlertsAndAlarmsNotifications(String notificationMessage)
	{
		Notification notification = new Notification(NotificationTypeConstants.CRITICAL_EVENT_NOTIFICATIONS,
		        notificationMessage, this);
		CommunicationService.getInstance().sendNotification(notification);
	}

	/**
	 * Triggers parser termination and allows up to 2 seconds for clean up.
	 */
	void performTermination()
	{
		Future terminator = new Work("Device Terminator-" + getNameOfDevice())
		{
			@Override
			public Object workLogic()
			{
				terminate();
				return null;
			}
		}.execute();
		try
		{
			terminator.get(terminationTimeOut, TimeUnit.SECONDS);
		}
		catch (InterruptedException | ExecutionException | TimeoutException e)
		{
			terminator.cancel(true);
			LOG.info("Forced termination for:" + getNameOfDevice());
		}
	}

	abstract protected MedicalDeviceData createIHEDataObject();

	/**
	 * Getter that should expose parameters to UI.
	 *
	 * @param paramterIdentifier
	 * @return
	 */
	public abstract NodeBase getNodeByIdentifier(String paramterIdentifier);

	/**
	 * This parser is being terminated. Perform clean-up here.
	 * A timeout of {@value CoreDevice#terminationTimeOut} will be allowed for clean up.
	 */
	public abstract void terminate();

	/**
	 * Captures the snapshot.
	 * @author Sahil_Jain08
	 *
	 */
	private class SnapshotWorker
	extends Work
	{
		Date snapshotTriggerTime;
		public SnapshotWorker()
		{
			super("SnapshotWorker - " + CoreDevice.this.nameOfDevice);
		}

		public void setTriggerTime(Date triggerTime)
		{
			this.snapshotTriggerTime = triggerTime;
		}

		@Override
		public Object workLogic()
		{
			SnapshotDataSample [] snapshotData = (SnapshotDataSample[]) bufferedSamples.toArray();
			DeviceSnapshot snapshot = new DeviceSnapshot(nameOfDevice, snapshotTriggerTime, snapshotData);
			SnapshotList.getInstance().addSnapshot(snapshot);

			// Reset snapshotTriggerTime - Precautionary step.
			snapshotTriggerTime = null;

			// Send Notification
			DeviceStatusNotifications snapshotReadyNotification = new DeviceStatusNotifications(DeviceStatusNotifications.DEVICE_SNAPSHOT_READY);
			snapshotReadyNotification.setData(snapshot);
			CommunicationService.getInstance().sendNotification(snapshotReadyNotification);

			// Mark the capture as complete.
			snapshotCaptureInProgress = false;
			return snapshot;
		}

	}
}
