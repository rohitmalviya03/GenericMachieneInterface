/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package listeners;

import java.util.List;

import org.apache.log4j.Logger;

import com.cdac.device.anesthesia.AnesthesiaParser;
import com.cdac.framework.IMedicalDevice;
import com.cdac.framework.datastructures.IDataObserver;
import com.cdac.framework.datastructures.NodeBase;
import com.cdac.framework.datastructures.Parameter;

import application.Main;
import mediatorPattern.ControllerMediator;
import model.DevicesListenerStatusSession;

/**
 * AnesthesiaWaveformListener.java <br>
 * Purpose: This class captures frequent changes in waveform values from
 * Anesthesia machine
 *
 * @author Rohit_Sharma41
 *
 */
public class AnesthesiaWaveformListener
{
	private static final Logger LOG = Logger.getLogger(AnesthesiaWaveformListener.class.getName());

	private DataListener dataListener;
	private Parameter PressureWaveform;
	private static final String ANESTHESIA_WAVEFORM_TOPIC = "AnesthesiaWaveform";
	private String combinedData = "";

	public AnesthesiaWaveformListener()
	{
		dataListener = new DataListener();
	}

	/**
	 * This method gets the device parameters and add observer to them to
	 * monitor any modifications
	 *
	 * @param device connected Anesthesia device
	 */
	public void listenToTheDevice(IMedicalDevice device)
	{
		try
		{
			if (device.getNameOfDevice().equals(AnesthesiaParser.DEVICE_NAME))
			{
				AnesthesiaParser anesthesiaParser = (AnesthesiaParser) device;

				// Add listener to patientMonitoring parameters
				PressureWaveform = (Parameter) anesthesiaParser.getNodeByIdentifier(AnesthesiaParser.ANESTHESIA_WAVEFORM_PRESSURE);
				PressureWaveform.addObserver(dataListener);
			}
		}
		catch (Exception e)
		{
			LOG.error("## Exception occured:", e);
		}
	}

	/**
	 * update() method of this class is invoked when a parameter of device is
	 * modified
	 */
	private class DataListener implements IDataObserver
	{
		@Override
		public NotificationRule getNotificationRule()
		{
			return NotificationRule.EVERY_NODE;
		}

		@Override
		public List<NodeBase> getNodesUnderObservation()
		{
			// Not used in case of NotificationRule.EVERY_NODE
			return null;
		}

		@Override
		public void update(NodeBase notificationTriggeringNode, NodeBase nodeThatLeadToEventNotification)
		{
			try
			{
				combinedData = "";
				if(DevicesListenerStatusSession.getInstance().isAnesthesiaMachineAssigned()){
				for (String s : (String[]) PressureWaveform.getData())
				{
					Main.getInstance().getPlotAnesthesiaGraph().getPressWaveQueue().add(Integer.valueOf(s));
					combinedData = combinedData + s + " ";
				}
				combinedData = combinedData.trim();
				}

				// ---Send data over KAFKA
				if (ControllerMediator.getInstance().getMainController().isPublishMode())
				{
					Main.getInstance().getKafkaProducer().setTopic(ANESTHESIA_WAVEFORM_TOPIC);
					Main.getInstance().getKafkaProducer().setProducedData(combinedData);
					// Main.getInstance().getKafkaProducer().getProducerThread().execute();
					Main.getInstance().getKafkaProducer().callProducer();
				}

				if(DevicesListenerStatusSession.getInstance().isRemoveAnesthesiaMachineListener()){
					PressureWaveform.removeObserver(dataListener);

				}
			}
			catch (Exception e)
			{
				LOG.error("## Exception occured:", e);
			}

			/*Platform.runLater(new Runnable()
			{
				@Override
				public void run()
				{
					if(DevicesListenerStatusSession.getInstance().isAnesthesiaMachineAssigned()){
						// Main.getInstance().getPlotAnesthesiaGraph().initializeGraphTimeline();
					}
				}
			});*/

		}
	}
}
