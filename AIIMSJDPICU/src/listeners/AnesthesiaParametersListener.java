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
 * AnesthesiaParametersListener.java <br>
 * Purpose: This class captures frequent changes in parameter values from
 * Anesthesia machine
 *
 * @author Rohit_Sharma41
 *
 */
public class AnesthesiaParametersListener
{
	private static final Logger LOG = Logger.getLogger(AnesthesiaParametersListener.class.getName());

	private DataListener dataListener;
	private static final String ANESTHESIA_PARAMETER_TOPIC = "AnesthesiaParameter";

	// private Parameter TotalFlow;
	private Parameter IERatio;
	private Parameter TVInspiratory;
	private Parameter RRSetParam;
	private Parameter PeepIntrinsic;

	// private String flow;
	private String ieRatio;
	private String tvInsp;
	private String rrset;
	private String peepIntr;

	public AnesthesiaParametersListener()
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
				// TotalFlow = (Parameter) anesthesiaParser
				// .getNodeByIdentifier(AnesthesiaParser.ANESTHESIA_STATUSDATA_TOTALFLOW);
				IERatio = (Parameter) anesthesiaParser.getNodeByIdentifier(AnesthesiaParser.ANESTHESIA_STATUSDATA_IE);
				TVInspiratory = (Parameter) anesthesiaParser
				        .getNodeByIdentifier(AnesthesiaParser.ANESTHESIA_STATUSDATA_TVINSP);
				RRSetParam = (Parameter) anesthesiaParser.getNodeByIdentifier(AnesthesiaParser.ANESTHESIA_STATUSDATA_RR);
				PeepIntrinsic = (Parameter) anesthesiaParser
				        .getNodeByIdentifier(AnesthesiaParser.ANESTHESIA_STATUSDATA_PEEP);

				PeepIntrinsic.addObserver(dataListener);

				Main.getInstance().getManageDeviceController().getScannedDevices(device.getNameOfDevice());

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
				// flow = String.valueOf(TotalFlow.getData());
				ieRatio = String.valueOf(IERatio.getData());
				tvInsp = String.valueOf(TVInspiratory.getData());
				rrset = String.valueOf(RRSetParam.getData());
				peepIntr = String.valueOf(PeepIntrinsic.getData());

				// ---Send data over KAFKA
				if (ControllerMediator.getInstance().getMainController().isPublishMode())
				{
					String data = ieRatio + "|" + tvInsp + "|" + rrset + "|" + peepIntr;
					Main.getInstance().getKafkaProducer().setTopic(ANESTHESIA_PARAMETER_TOPIC);
					Main.getInstance().getKafkaProducer().setProducedData(data);
					// Main.getInstance().getKafkaProducer().getProducerThread().execute();
					Main.getInstance().getKafkaProducer().callProducer();
				}
				if(DevicesListenerStatusSession.getInstance().isRemoveAnesthesiaMachineListener()){
					PeepIntrinsic.removeObserver(dataListener);

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
					if(ControllerMediator.getInstance().getManageAvailableDevicesController().isAnesthesiaMachineAssigned)
					{
						// TODO Auto-generated method stub
						ControllerMediator.getInstance().getMainController().getDrawNewGraphs().getRrsetVal().setText(rrset);
						ControllerMediator.getInstance().getMainController().getDrawNewGraphs().getIeVal().setText(ieRatio);
						ControllerMediator.getInstance().getMainController().getDrawNewGraphs().getTvinspVal().setText(tvInsp);
						ControllerMediator.getInstance().getMainController().getDrawNewGraphs().getPeepiVal().setText(peepIntr);
					}
				}
			});*/
		}
	}
}

