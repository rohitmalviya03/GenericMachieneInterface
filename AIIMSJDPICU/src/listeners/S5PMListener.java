/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package listeners;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.cdac.device.s5PatientMonitor.S5PatientMonitorParser;
import com.cdac.framework.IMedicalDevice;
import com.cdac.framework.datastructures.IDataObserver;
import com.cdac.framework.datastructures.NodeBase;
import com.cdac.framework.datastructures.Parameter;

import application.Main;
import mediatorPattern.ControllerMediator;
import model.ConfigureDeviceSession;
import model.DevicesListenerStatusSession;
import utility.ParamMinMaxRange;

/**
 * S5PMListener.java <br>
 * Purpose: This class captures frequent changes in parameter values from S5
 * Patient Monitor machine
 *
 * @author Rohit_Sharma41
 *
 */
public class S5PMListener {

	private static final Logger LOG = Logger.getLogger(S5PMListener.class.getName());

	private DataListener dataListener;
	private static final String S5PTMONITOR_PARAMETER_TOPIC = "S5PTMonitorParameter";
	private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");

	private Parameter HeartRate;
	private Parameter Bis;
	private Parameter Temperature1;
	private Parameter Temperature2;
	private Parameter O2Level;
	private Parameter IBPSystolicPressure;
	private Parameter IBPDiastolicPressure;
	private Parameter IBPMeanPressure;
	private Parameter NIBPSystolicPressure;
	private Parameter NIBPDiastolicPressure;
	private Parameter NIBPMeanPressure;
	private Parameter ETCO2;
	private Parameter ETAgent;
	private Parameter Mac;

	private String heartRateValue;
	private String bis;
	private String temp1;
	private String temp2;
	private String spo2;
	private String ibpSysPres;
	private String ibpDiaPres;
	private String ibpMeanPres;
	private String nibpSysPres;
	private String nibpDiaPres;
	private String nibpMeanPres;
	private String etCo2Value;
	private String etagentValue;
	private String macValue;
	private String currentTimeStamp = "";

	public S5PMListener() {
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
				S5PatientMonitorParser patientMonitor = (S5PatientMonitorParser) device;

				// Add listener to patientMonitoring parameters
				HeartRate = (Parameter) patientMonitor
						.getNodeByIdentifier(S5PatientMonitorParser.PATIENTMONITOR_ECG_HEARTRATE);
				Bis= (Parameter) patientMonitor
						.getNodeByIdentifier(S5PatientMonitorParser.PATIENTMONITOR_EEG_BIS);
				Temperature1 = (Parameter) patientMonitor
						.getNodeByIdentifier(S5PatientMonitorParser.PATIENTMONITOR_ECG_TEMPERATURE1);
				Temperature2 = (Parameter) patientMonitor
						.getNodeByIdentifier(S5PatientMonitorParser.PATIENTMONITOR_ECG_TEMPERATURE2);
				IBPSystolicPressure = (Parameter) patientMonitor
						.getNodeByIdentifier(S5PatientMonitorParser.PATIENTMONITOR_IBP_SYSTOLICPRESSURE);
				IBPDiastolicPressure = (Parameter) patientMonitor
						.getNodeByIdentifier(S5PatientMonitorParser.PATIENTMONITOR_IBP_DIASTOLICPRESSURE);
				IBPMeanPressure = (Parameter) patientMonitor
						.getNodeByIdentifier(S5PatientMonitorParser.PATIENTMONITOR_IBP_MEANPRESSURE);
				NIBPSystolicPressure = (Parameter) patientMonitor
						.getNodeByIdentifier(S5PatientMonitorParser.PATIENTMONITOR_NIBP_SYSTOLICPRESSURE);
				NIBPDiastolicPressure = (Parameter) patientMonitor
						.getNodeByIdentifier(S5PatientMonitorParser.PATIENTMONITOR_NIBP_DIASTOLICPRESSURE);
				NIBPMeanPressure = (Parameter) patientMonitor
						.getNodeByIdentifier(S5PatientMonitorParser.PATIENTMONITOR_NIBP_MEANPRESSURE);
				O2Level = (Parameter) patientMonitor
						.getNodeByIdentifier(S5PatientMonitorParser.PATIENTMONITOR_SPO2_OXYGENLEVEL);
				ETAgent = (Parameter) patientMonitor
				        .getNodeByIdentifier(S5PatientMonitorParser.PATIENTMONITOR_AA_ETAGENT);
				Mac = (Parameter) patientMonitor
				        .getNodeByIdentifier(S5PatientMonitorParser.PATIENTMONITOR_AA_MAC);
				ETCO2 = (Parameter) patientMonitor.
						getNodeByIdentifier(S5PatientMonitorParser.PATIENTMONITOR_CO2_ET);

				ETCO2.addObserver(dataListener);
				Main.getInstance().getManageDeviceController().getScannedDevices(device.getNameOfDevice());

				// ControllerMediator.getInstance().getDevices().getScannedDevices(device.getNameOfDevice());
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
				heartRateValue = String.valueOf(HeartRate.getData());
				bis = String.valueOf(Bis.getData());
				temp1 = String.valueOf(Temperature1.getData());
				temp2 = String.valueOf(Temperature2.getData());
				spo2 = String.valueOf(O2Level.getData());
				ibpSysPres = String.valueOf(IBPSystolicPressure.getData());
				ibpDiaPres = String.valueOf(IBPDiastolicPressure.getData());
				ibpMeanPres = String.valueOf(IBPMeanPressure.getData());
				nibpSysPres = String.valueOf(NIBPSystolicPressure.getData());
				nibpDiaPres = String.valueOf(NIBPDiastolicPressure.getData());
				nibpMeanPres = String.valueOf(NIBPMeanPressure.getData());
				etCo2Value = String.valueOf(ETCO2.getData());
				etagentValue = String.valueOf(ETAgent.getData());
				macValue = String.valueOf(Mac.getData());

				if (DevicesListenerStatusSession.getInstance().isS5MonitorAssigned()) {

					ConfigureDeviceSession.getInstance().getLastDeviceReadings().put("TIME", sdf.format(new Date()));
					ConfigureDeviceSession.getInstance().getLastDeviceReadings().put("IBP", ibpSysPres+"/"+ibpDiaPres);
					ConfigureDeviceSession.getInstance().getLastDeviceReadings().put("HR", heartRateValue);
					ConfigureDeviceSession.getInstance().getLastDeviceReadings().put("SP", spo2);
					ConfigureDeviceSession.getInstance().getLastDeviceReadings().put("TEMP", temp1);
					//ConfigureDeviceSession.getInstance().getLastDeviceReadings().put("NIBPMEAN", nibpMeanPres);
					

					try
					{
						if ((!heartRateValue.contains("- - -") && Double.parseDouble(heartRateValue) <= 0)|| heartRateValue.contains("- - -"))
							heartRateValue = "---";
					}
					catch (NumberFormatException e)
					{
						heartRateValue = "---";
					}

					try
					{
						if ((!ibpSysPres.contains("- - -") && Double.parseDouble(ibpSysPres) <= 0)|| ibpSysPres.contains("- - -"))
							ibpSysPres = "---";
					}
					catch (NumberFormatException e)
					{
						ibpSysPres = "---";
					}

					try
					{
						if ((!ibpDiaPres.contains("- - -") && Double.parseDouble(ibpDiaPres) <= 0)|| ibpDiaPres.contains("- - -"))
							ibpDiaPres = "---";
					}
					catch (NumberFormatException e)
					{
						ibpDiaPres = "---";
					}

					try
					{
						if ((!spo2.contains("- - -") && Double.parseDouble(spo2) <= 0)|| spo2.contains("- - -"))
							spo2 = "---";
					}
					catch (NumberFormatException e)
					{
						spo2 = "---";
					}

					try
					{
						if ((!etCo2Value.contains("- - -") && Double.parseDouble(etCo2Value) <= 0)|| etCo2Value.contains("- - -"))
							etCo2Value = "---";
					}
					catch (NumberFormatException e)
					{
						etCo2Value = "---";
					}

					try
					{
						if ((!temp1.contains("- - -") && Double.parseDouble(temp1) <= 0) || temp1.contains("- - -"))
							temp1 = "---";
					}
					catch (NumberFormatException e)
					{
						temp1 = "---";
					}

					try
					{
						if ((!temp2.contains("- - -") && Double.parseDouble(temp2) <= 0) || temp2.contains("- - -"))
							temp2 = "---";
					}
					catch (NumberFormatException e)
					{
						temp2 = "---";
					}

					try
					{
						if ((!bis.contains("- - -") && Double.parseDouble(bis) <= 0) || bis.contains("- - -"))
							bis = "---";
					}
					catch (NumberFormatException e)
					{
						bis = "---";
					}

					try
					{
						if ((!etagentValue.contains("- - -") && Double.parseDouble(etagentValue) <= 0) || etagentValue.contains("- - -"))
							etagentValue = "---";
					}
					catch (NumberFormatException e)
					{
						etagentValue = "---";
					}

					try
					{
						if ((!macValue.contains("- - -") && Double.parseDouble(macValue) <= 0) || macValue.contains("- - -"))
							macValue = "---";
					}
					catch (NumberFormatException e)
					{
						macValue = "---";
					}

					try
					{
						if ((!nibpMeanPres.contains("- - -") && Double.parseDouble(nibpMeanPres) <= 0) || nibpMeanPres.contains("- - -"))
							nibpMeanPres = "---";
					}
					catch (NumberFormatException e)
					{
						nibpMeanPres = "---";
					}

					try
					{
						if ((!nibpDiaPres.contains("- - -") && Double.parseDouble(nibpDiaPres) <= 0) || nibpDiaPres.contains("- - -"))
							nibpDiaPres = "---";
					}
					catch (NumberFormatException e)
					{
						nibpDiaPres = "---";
					}
					
					try
					{
						if ((!nibpSysPres.contains("- - -") && Double.parseDouble(nibpSysPres) <= 0) || nibpSysPres.contains("- - -"))
							nibpSysPres = "---";
					}
					catch (NumberFormatException e)
					{
						nibpSysPres = "---";
					}
					
					
					Main.getInstance().getPlotTrendGraph().getPtMntrParamMap().put(ParamMinMaxRange.HR, heartRateValue);
					Main.getInstance().getPlotTrendGraph().getPtMntrParamMap().put(ParamMinMaxRange.IBPSYS, ibpSysPres);
					Main.getInstance().getPlotTrendGraph().getPtMntrParamMap().put(ParamMinMaxRange.IBPDIA, ibpDiaPres);
					Main.getInstance().getPlotTrendGraph().getPtMntrParamMap().put(ParamMinMaxRange.SPO2, spo2);
					Main.getInstance().getPlotTrendGraph().getPtMntrParamMap().put(ParamMinMaxRange.ETCO2, etCo2Value);
					//Main.getInstance().getPlotTrendGraph().getPtMntrParamMap().put(ParamMinMaxRange.NI, etCo2Value);


					Map<Calendar, String[]> paramValMap = new HashMap<Calendar, String[]>();
					paramValMap.put(Calendar.getInstance(),
					        new String[] {  ParamMinMaxRange.HR+"|" + heartRateValue,
					        				ParamMinMaxRange.IBPSYS+"|" + ibpSysPres,
					        				ParamMinMaxRange.IBPDIA+"|" + ibpDiaPres,
					        				ParamMinMaxRange.SPO2+"|" + spo2,
					        				ParamMinMaxRange.ETCO2+"|" + etCo2Value,
					        				ParamMinMaxRange.TEMP1+"|" + temp1,
					        				ParamMinMaxRange.TEMP2+"|" + temp2,
					        				ParamMinMaxRange.BIS+"|" + bis,
					        				ParamMinMaxRange.ETAGENT+"|" + etagentValue,
					        				ParamMinMaxRange.MAC+"|" + macValue,
					        				ParamMinMaxRange.IBPMEAN+"|"+ibpMeanPres,
					        				ParamMinMaxRange.NIBPMEAN+"|"+nibpMeanPres,
					        				ParamMinMaxRange.NIBPDIA+"|"+nibpDiaPres,
					        				ParamMinMaxRange.NIBPSYS+"|"+nibpSysPres
						                  });
					ControllerMediator.getInstance().getMainController().getDrawTabbedCenter().getDeviceParamValMap().put("ptmonitor", paramValMap);

					/*if (!currentTimeStamp.equalsIgnoreCase(sdf.format(Calendar.getInstance().getTime())))
					{
						ControllerMediator.getInstance().getMainController().getDrawTabbedCenter().fillParamInGrid(
					        sdf.format(Calendar.getInstance().getTime()),
					        new String[] {  ParamMinMaxRange.HR+"|" + heartRateValue,
					        				ParamMinMaxRange.IBPSYS+"|" + ibpSysPres,
					        				ParamMinMaxRange.IBPDIA+"|" + ibpDiaPres,
					        				ParamMinMaxRange.SPO2+"|" + spo2,
					        				ParamMinMaxRange.ETCO2+"|" + etCo2Value,
					        				ParamMinMaxRange.TEMP1+"|" + temp1,
					        				ParamMinMaxRange.TEMP2+"|" + temp2

						                  },
					        "ptmonitor");
						currentTimeStamp = sdf.format(Calendar.getInstance().getTime());
					}*/


					/*if (ControllerMediator.getInstance().getMainController().getxValueTrendTimeline()
					        .getStatus() != Status.RUNNING)
						ControllerMediator.getInstance().getMainController().initializeXValTrendTimeline();

					Main.getInstance().getPlotTrendGraph().initializeLiveGraphTimeline();*/

					// ---Send data over KAFKA
					if (ControllerMediator.getInstance().getMainController().isPublishMode())
					{
						DecimalFormat df = new DecimalFormat("#.#");

						String data = heartRateValue + "|" + ibpSysPres + "/" + ibpDiaPres + "|" + ibpMeanPres + "|" + spo2
						        + "|" + etCo2Value + "|" + df.format(Double.parseDouble(temp1)) + "|" +  df.format(Double.parseDouble(temp2));

						Main.getInstance().getKafkaProducer().setTopic(S5PTMONITOR_PARAMETER_TOPIC);
						Main.getInstance().getKafkaProducer().setProducedData(data);
						// Main.getInstance().getKafkaProducer().getProducerThread().execute();
						Main.getInstance().getKafkaProducer().callProducer();
					}
				}

				if (DevicesListenerStatusSession.getInstance().isRemoveS5MonitorListener()) {
					ETCO2.removeObserver(dataListener);
				}

				/*Platform.runLater(new Runnable() {

					@Override
					public void run() {
						if (DevicesListenerStatusSession.getInstance().isS5MonitorAssigned()) {
							// TODO Auto-generated method stub

							 * ControllerMediator.getInstance().getMainController().
							 * getDrawNewGraphs().getEcgVal()
							 * .setText(heartRateValue);
							 * ControllerMediator.getInstance().getMainController().
							 * getDrawNewGraphs().getIbpVal() .setText(ibpSysPres +
							 * "/" + ibpDiaPres);
							 * ControllerMediator.getInstance().getMainController().
							 * getDrawNewGraphs().getIbpMeanVal() .setText("(" +
							 * ibpMeanPres + ")");
							 * ControllerMediator.getInstance().getMainController().
							 * getDrawNewGraphs().getSpo2Val() .setText(spo2 + "");
							 * ControllerMediator.getInstance().getMainController().
							 * getDrawNewGraphs().getEtcVal() .setText(etCo2Value +
							 * "");


							if (ControllerMediator.getInstance().getMainController().getxValueTrendTimeline()
									.getStatus() != Status.RUNNING)
								ControllerMediator.getInstance().getMainController().initializeXValTrendTimeline();

							Main.getInstance().getPlotTrendGraph().initializeLiveGraphTimeline();
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
