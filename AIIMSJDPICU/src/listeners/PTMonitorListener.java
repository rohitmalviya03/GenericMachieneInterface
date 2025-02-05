/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package listeners;

import java.util.List;

import com.cdac.device.patientMonitoring.PatientMonitoringParser;
import com.cdac.framework.IMedicalDevice;
import com.cdac.framework.datastructures.IDataObserver;
import com.cdac.framework.datastructures.NodeBase;
import com.cdac.framework.datastructures.Parameter;

import application.Main;
import javafx.application.Platform;
import mediatorPattern.ControllerMediator;

public class PTMonitorListener
{
	private DataListener dataListener;

	private Parameter heartRate;
	private Parameter ibpDiastolicPressure;
	private Parameter ibpSystolicPressure;
	private Parameter spo2Param;
	private Parameter ecgWave;
	private Parameter spo2Wave;
	private Parameter ibpWave;

	private int ecg;
	private int spo2;
	private int ibp;

	private String ibpSys = "";
	private String ibpDia = "";
	private String ecgWaveVal = "";
	private String spo2WaveVal = "";
	private String ibpWaveVal = "";


	public PTMonitorListener()
	{
		dataListener = new DataListener();
	}

	/**
	 * This method gets the device parameters and add observer to them to monitor any modifications
	 * @param device
	 */
	public void listenToTheDevice(IMedicalDevice device)
	{
		if (device.getNameOfDevice().equals(PatientMonitoringParser.DEVICE_NAME))
		{
			PatientMonitoringParser patientMonitoring = (PatientMonitoringParser) device;

			// Add listener to patientMonitoring parameters
			ecgWave = (Parameter) patientMonitoring
			        .getNodeByIdentifier(PatientMonitoringParser.PATIENTMONITOR_ECG_WAVE);

			spo2Wave = (Parameter) patientMonitoring
			        .getNodeByIdentifier(PatientMonitoringParser.PATIENTMONITOR_SPO2_WAVE);

			ibpWave = (Parameter) patientMonitoring
			        .getNodeByIdentifier(PatientMonitoringParser.PATIENTMONITOR_IBP_WAVE);

			ibpSystolicPressure = (Parameter) patientMonitoring
			        .getNodeByIdentifier(PatientMonitoringParser.PATIENTMONITOR_IBP_SYSTOLICPRESSURE);

			ibpDiastolicPressure = (Parameter) patientMonitoring
			        .getNodeByIdentifier(PatientMonitoringParser.PATIENTMONITOR_IBP_DIASTOLICPRESSURE);

			spo2Param = (Parameter) patientMonitoring
			        .getNodeByIdentifier(PatientMonitoringParser.PATIENTMONITOR_SPO2_OXYGENLEVEL);

			heartRate = (Parameter) patientMonitoring
			        .getNodeByIdentifier(PatientMonitoringParser.PATIENTMONITOR_ECG_HEARTRATE);

			heartRate.addObserver(dataListener);

		}

	}

	/**
	 * update() method of this class is invoked when a parameter of device is modified
	 * @author Rohit_Sharma41
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
//			if (ControllerMediator.getInstance().getConfigureDevice() != null)
//			if (ControllerMediator.getInstance().getConfigureDevice().isPtMonitorSaveClicked())
//			{

				ecg = (int) ecgWave.getData();
				spo2 = (int) spo2Wave.getData();
				ibp = (int) ibpWave.getData();



				Platform.runLater(new Runnable()
				{
					@Override
					public void run()
					{
							//Main.getInstance().getPlotGraphs().setEcg(ecg);
							//Main.getInstance().getPlotGraphs().setSpo2(spo2);
							//Main.getInstance().getPlotGraphs().setIbp(ibp);
							Main.getInstance().getPlotGraphs().setPtMonitorCounter(Main.getInstance().getPlotGraphs().getPtMonitorCounter() + 1);

							//Main.getInstance().getPlotGraphs().initializeGraphTimelines();

							if (ControllerMediator.getInstance().getMainController().getDrawNewGraphs() != null)
							{

								ibpSys = String.valueOf(ibpSystolicPressure.getData());
								ibpDia = String.valueOf(ibpDiastolicPressure.getData());
								ecgWaveVal = String.valueOf(ecgWave.getData());
								spo2WaveVal = String.valueOf(spo2Wave.getData());
								ibpWaveVal = String.valueOf(ibpWave.getData());

								ControllerMediator.getInstance().getMainController().getDrawNewGraphs().getIbpVal()
								        .setText(ibpSys + "/" + ibpDia);
								ControllerMediator.getInstance().getMainController().getDrawNewGraphs().getSpo2Val()
								        .setText(String.valueOf(spo2Param.getData()));
								ControllerMediator.getInstance().getMainController().getDrawNewGraphs().getEcgVal()
								        .setText(String.valueOf(heartRate.getData()));

								/*if (Main.getInstance().getKafkaProducer().getProducer() == null)
								{
									Main.getInstance().getKafkaProducer().initializeProducer();
								}*/
							}

					}

				});



				/*if (!ecgWaveVal.equals("") && !spo2WaveVal.equals("") && !ibpWaveVal.equals(""))
				{
						Main.getInstance().getKafkaProducer().setData1(String.valueOf(heartRate.getData()));
						Main.getInstance().getKafkaProducer().setData2(String.valueOf(spo2Param.getData()));
						Main.getInstance().getKafkaProducer().setData3(ibpSys + "/" + ibpDia);
						Main.getInstance().getKafkaProducer().setIbpWaveVal(ibpWaveVal);
						Main.getInstance().getKafkaProducer().setEcgWaveVal(ecgWaveVal);
						Main.getInstance().getKafkaProducer().setSpo2WaveVal(spo2WaveVal);
						Main.getInstance().getKafkaProducer().getProducerThread().execute();
				}*/
			//}
		}
	}
}
