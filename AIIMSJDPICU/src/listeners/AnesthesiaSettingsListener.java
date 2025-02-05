/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package listeners;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.cdac.device.anesthesia.AnesthesiaParser;
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
 * AnesthesiaSettingsListener.java <br>
 * Purpose: This class captures frequent changes in setting values from
 * Anesthesia machine
 *
 * @author Rohit_Sharma41
 *
 */
public class AnesthesiaSettingsListener {

	private static final Logger LOG = Logger.getLogger(AnesthesiaSettingsListener.class.getName());

	private DataListener dataListener;
	private static final String ANESTHESIA_SETTINGS_TOPIC = "AnesthesiaSettings";
	private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

	private Parameter PeakPressure;
	private Parameter TotalFlow;
	private Parameter MeanPressure;
	private Parameter PeepValue;
	private Parameter CircuitOxygen;
	private Parameter TVExpiratory;
	private Parameter MVExpiratory;
	private Parameter O2Percentage;
	private Parameter RRTotalParam;

	private String peak;
	private String flow;
	private String pmean;
	private String peepExp;
	private String circuitO2;
	private String tvExp;
	private String mvExp;
	private String o2Percent;
	private String rrTotal;
	private String currentTimeStamp = "";

	public AnesthesiaSettingsListener() {
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
				TotalFlow = (Parameter) anesthesiaParser
				        .getNodeByIdentifier(AnesthesiaParser.ANESTHESIA_MEASUREDDATA_TOTALFLOW);
				PeakPressure = (Parameter) anesthesiaParser
				        .getNodeByIdentifier(AnesthesiaParser.ANESTHESIA_MEASUREDDATA_PPEAK);
				MeanPressure = (Parameter) anesthesiaParser
				        .getNodeByIdentifier(AnesthesiaParser.ANESTHESIA_MEASUREDDATA_PMEAN);
				PeepValue = (Parameter) anesthesiaParser
				        .getNodeByIdentifier(AnesthesiaParser.ANESTHESIA_MEASUREDDATA_PEEPExt);
				CircuitOxygen = (Parameter) anesthesiaParser
				        .getNodeByIdentifier(AnesthesiaParser.ANESTHESIA_MEASUREDDATA_CIRCUITO2);
				TVExpiratory = (Parameter) anesthesiaParser
				        .getNodeByIdentifier(AnesthesiaParser.ANESTHESIA_MEASUREDDATA_TVEXP);
				MVExpiratory = (Parameter) anesthesiaParser
				        .getNodeByIdentifier(AnesthesiaParser.ANESTHESIA_MEASUREDDATA_MVEXP);
				O2Percentage = (Parameter) anesthesiaParser
				        .getNodeByIdentifier(AnesthesiaParser.ANESTHESIA_MEASUREDDATA_O2PERCENT);
				RRTotalParam = (Parameter) anesthesiaParser
				        .getNodeByIdentifier(AnesthesiaParser.ANESTHESIA_MEASUREDDATA_RRTOTAL);

				TotalFlow.addObserver(dataListener);
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
	 *
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
				peak = String.valueOf(PeakPressure.getData());
				pmean = String.valueOf(MeanPressure.getData());
				peepExp = String.valueOf(PeepValue.getData());
				circuitO2 = String.valueOf(CircuitOxygen.getData());
				tvExp = String.valueOf(TVExpiratory.getData());
				mvExp = String.valueOf(MVExpiratory.getData());
				o2Percent = String.valueOf(O2Percentage.getData());
				flow = String.valueOf(TotalFlow.getData());
				rrTotal = String.valueOf(RRTotalParam.getData());

				if (DevicesListenerStatusSession.getInstance().isAnesthesiaMachineAssigned())
				{
					ConfigureDeviceSession.getInstance().getLastDeviceReadings().put("TIME", sdf.format(new Date()));
					ConfigureDeviceSession.getInstance().getLastDeviceReadings().put("RR", rrTotal);

					// ---creating parameters map for TREND graph plot
					if (!peak.contains("---") && Double.parseDouble(peak) <= 0)
						peak = "---";
					if (!pmean.contains("---") && Double.parseDouble(pmean) <= 0)
						pmean = "---";
					if (!peepExp.contains("---") && Double.parseDouble(peepExp) <= 0)
						peepExp = "---";
					if (!circuitO2.contains("---") && Double.parseDouble(circuitO2) <= 0)
						circuitO2 = "---";
					if (!mvExp.contains("---") && Double.parseDouble(mvExp) <= 0)
						mvExp = "---";
					if (!tvExp.contains("---") && Double.parseDouble(tvExp) <= 0)
						tvExp = "---";
					if (!rrTotal.contains("---") && Double.parseDouble(rrTotal) <= 0)
						rrTotal = "---";

					Main.getInstance().getPlotTrendGraph().getAnesParamMap().put(ParamMinMaxRange.PPEAK, peak);
					Main.getInstance().getPlotTrendGraph().getAnesParamMap().put(ParamMinMaxRange.PMEAN, pmean);
					Main.getInstance().getPlotTrendGraph().getAnesParamMap().put(ParamMinMaxRange.PEEPEXT, peepExp);
					Main.getInstance().getPlotTrendGraph().getAnesParamMap().put(ParamMinMaxRange.CIRCUITO2, circuitO2);
					Main.getInstance().getPlotTrendGraph().getAnesParamMap().put(ParamMinMaxRange.MVEXP, mvExp);
					Main.getInstance().getPlotTrendGraph().getAnesParamMap().put(ParamMinMaxRange.TVEXP, tvExp);
					Main.getInstance().getPlotTrendGraph().getAnesParamMap().put(ParamMinMaxRange.RR, rrTotal);


					Map<Calendar, String[]> paramValMap = new HashMap<Calendar, String[]>();
					paramValMap.put(Calendar.getInstance(),
					        new String[] { ParamMinMaxRange.PPEAK+"|" +peak,
					        			   ParamMinMaxRange.PMEAN+"|" +pmean,
					        			   ParamMinMaxRange.PEEP+"|" +peepExp,
					        			   ParamMinMaxRange.CIRCUITO2+"|" +circuitO2,
					        			   ParamMinMaxRange.TVEXP+"|" +tvExp,
					        			   ParamMinMaxRange.MVEXP+"|" +mvExp,
					        			   ParamMinMaxRange.RR+"|" +rrTotal,});
					ControllerMediator.getInstance().getMainController().getDrawTabbedCenter().getDeviceParamValMap().put("anes", paramValMap);

					/*if (!currentTimeStamp.equalsIgnoreCase(sdf.format(Calendar.getInstance().getTime())))
					{
						ControllerMediator.getInstance().getMainController().getDrawTabbedCenter().fillParamInGrid(
					        sdf.format(Calendar.getInstance().getTime()),
					        new String[] { ParamMinMaxRange.PPEAK+"|" +peak,
					        			   ParamMinMaxRange.PMEAN+"|" +pmean,
					        			   ParamMinMaxRange.PEEP+"|" +peepExp,
					        			   ParamMinMaxRange.CIRCUITO2+"|" +circuitO2,
					        			   ParamMinMaxRange.TVEXP+"|" +tvExp,
					        			   ParamMinMaxRange.MVEXP+"|" +mvExp,
					        			   ParamMinMaxRange.RR+"|" +rrTotal,},
						        "anes");
						currentTimeStamp = sdf.format(Calendar.getInstance().getTime());
					}*/

					/*	if (ControllerMediator.getInstance().getMainController().getxValueTrendTimeline()
					        .getStatus() != Status.RUNNING)
						ControllerMediator.getInstance().getMainController().initializeXValTrendTimeline();

					Main.getInstance().getPlotTrendGraph().initializeAnesGraphTimeline();*/

					// ---Send data over KAFKA
					if (ControllerMediator.getInstance().getMainController().isPublishMode())
					{
						String data = peak + "|" + pmean + "|" + peepExp + "|" + circuitO2 + "|" + tvExp + "|" + mvExp + "|"
						        + o2Percent + "|" + flow + "|" + rrTotal;
						Main.getInstance().getKafkaProducer().setTopic(ANESTHESIA_SETTINGS_TOPIC);
						Main.getInstance().getKafkaProducer().setProducedData(data);
						// Main.getInstance().getKafkaProducer().getProducerThread().execute();
						Main.getInstance().getKafkaProducer().callProducer();
					}
				}

				if (DevicesListenerStatusSession.getInstance().isRemoveAnesthesiaMachineListener()) {
					TotalFlow.removeObserver(dataListener);

				}

				/*Platform.runLater(new Runnable() {

					@Override
					public void run() {
						// if(ControllerMediator.getInstance().getManageAvailableDevicesController().isAnesthesiaMachineAssigned)
						// {
						// TODO Auto-generated method stub
						// ControllerMediator.getInstance().getMainController().getDrawNewGraphs().getPmaxVal().setText(peak);
						// ControllerMediator.getInstance().getMainController().getDrawNewGraphs().getPmeanVal().setText(pmean);
						// ControllerMediator.getInstance().getMainController().getDrawNewGraphs().getPeepVal().setText(peepExp);
						// ControllerMediator.getInstance().getMainController().getDrawNewGraphs().getCircuitO2Val().setText(circuitO2);
						// ControllerMediator.getInstance().getMainController().getDrawNewGraphs().getMvexpVal().setText(mvExp);
						// ControllerMediator.getInstance().getMainController().getDrawNewGraphs().getTvexpVal().setText(tvExp);
						// ControllerMediator.getInstance().getMainController().getDrawNewGraphs().getO2Val().setText(o2Percent);
						// ControllerMediator.getInstance().getMainController().getDrawNewGraphs().getTotalFlowVal().setText(flow);
						// ControllerMediator.getInstance().getMainController().getDrawNewGraphs().getRRTotalVal().setText(rrTotal);
						if (DevicesListenerStatusSession.getInstance().isAnesthesiaMachineAssigned())
						{
						if (ControllerMediator.getInstance().getMainController().getxValueTrendTimeline()
								.getStatus() != Status.RUNNING)
							ControllerMediator.getInstance().getMainController().initializeXValTrendTimeline();

						Main.getInstance().getPlotTrendGraph().initializeAnesGraphTimeline();
						}
						// }
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
