/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package listeners;

import java.util.List;

import org.apache.log4j.Logger;

import com.cdac.device.s5PatientMonitor.S5PatientMonitorParser;
import com.cdac.framework.IMedicalDevice;
import com.cdac.framework.datastructures.IDataObserver;
import com.cdac.framework.datastructures.NodeBase;
import com.cdac.framework.datastructures.Parameter;

import application.Main;
import mediatorPattern.ControllerMediator;
import model.DevicesListenerStatusSession;

/**
 * S5PMWaveformListener.java <br>
 * Purpose: This class captures frequent changes in waveform values from S5
 * Patient Monitor machine
 *
 * @author Rohit_Sharma41
 *
 */
public class S5PMWaveformListener {

	private static final Logger LOG = Logger.getLogger(S5PMWaveformListener.class.getName());

	private DataListener dataListener;

	private static final String S5PTMONITOR_WAVEFORM_TOPIC = "S5PTMonitorWaveform";
	private String combinedData = "";

	private Parameter ecgWaveform;
	private Parameter ibpWaveform;
	private Parameter spo2Waveform;
	private Parameter etco2Waveform;

	public S5PMWaveformListener() {
		dataListener = new DataListener();
	}

	/**
	 * This method gets the device parameters and add observer to them to
	 * monitor any modifications
	 *
	 * @param device connected S5 Patient Monitor device
	 */
	public void listenToTheDevice(IMedicalDevice device) {
		try
		{
			if (device.getNameOfDevice().equals(S5PatientMonitorParser.DEVICE_NAME)) {
				S5PatientMonitorParser s5PatientMonitorParser = (S5PatientMonitorParser) device;

				// Add listener to patientMonitoring parameters
				ecgWaveform = (Parameter) s5PatientMonitorParser
						.getNodeByIdentifier(S5PatientMonitorParser.PATIENTMONITOR_WAVEFORM_ECG);
				ibpWaveform = (Parameter) s5PatientMonitorParser
						.getNodeByIdentifier(S5PatientMonitorParser.PATIENTMONITOR_WAVEFORM_INVP);
				spo2Waveform = (Parameter) s5PatientMonitorParser
						.getNodeByIdentifier(S5PatientMonitorParser.PATIENTMONITOR_WAVEFORM_PLETH);
				etco2Waveform = (Parameter) s5PatientMonitorParser
						.getNodeByIdentifier(S5PatientMonitorParser.PATIENTMONITOR_WAVEFORM_RESPIRATORY);

				etco2Waveform.addObserver(dataListener);
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
	private class DataListener implements IDataObserver {
		@Override
		public NotificationRule getNotificationRule() {
			return NotificationRule.EVERY_NODE;
		}

		@Override
		public List<NodeBase> getNodesUnderObservation() {
			// Not used in case of NotificationRule.EVERY_NODE
			return null;
		}

		@Override
		public void update(NodeBase notificationTriggeringNode, NodeBase nodeThatLeadToEventNotification) {
			try
			{
				if (DevicesListenerStatusSession.getInstance().isS5MonitorAssigned()) {

					combinedData = "";
					// ---1 ECG array
					for (int i : (int[]) ecgWaveform.getData()) {
						Main.getInstance().getPlotGraphs().getEcgWaveQueue().add(i);
						combinedData = combinedData + i + " ";
					}

					// ---2 SPO2 array
					combinedData = combinedData + "|";
					for (int i : (int[]) spo2Waveform.getData()) {
						Main.getInstance().getPlotGraphs().getSpo2WaveQueue().add(i);
						combinedData = combinedData + i + " ";
					}

					// ---3 IBP array
					combinedData = combinedData + "|";
					for (int i : (int[]) ibpWaveform.getData()) {
						Main.getInstance().getPlotGraphs().getIbpWaveQueue().add(i);
						combinedData = combinedData + i + " ";
					}

					// ---4 ETCO2 array
					combinedData = combinedData + "|";
					for (int i : (int[]) etco2Waveform.getData()) {
						Main.getInstance().getPlotGraphs().getEtcWaveQueue().add(i);
						combinedData = combinedData + i + " ";
					}
					combinedData = combinedData.trim();

					// ---Send data over KAFKA
					if (ControllerMediator.getInstance().getMainController().isPublishMode())
					{
						Main.getInstance().getKafkaProducer().setTopic(S5PTMONITOR_WAVEFORM_TOPIC);
						Main.getInstance().getKafkaProducer().setProducedData(combinedData);
						// Main.getInstance().getKafkaProducer().getProducerThread().execute();
						Main.getInstance().getKafkaProducer().callProducer();
					}

					if (DevicesListenerStatusSession.getInstance().isRemoveS5MonitorListener()) {
						etco2Waveform.removeObserver(dataListener);

					}
				}
				/*Platform.runLater(new Runnable() {
					@Override
					public void run() {
						if (DevicesListenerStatusSession.getInstance().isS5MonitorAssigned()) {
							Main.getInstance().getPlotGraphs().initializeEcgTimeline();
							Main.getInstance().getPlotGraphs().initializeSPO2Timeline();
							Main.getInstance().getPlotGraphs().initializeIBPTimeline();
							Main.getInstance().getPlotGraphs().initializeETCTimeline();
						}
					}
				});*/
			}
			catch (Exception e)
			{
				LOG.error("## Exception occured:", e);
			}

		}
	}
}
