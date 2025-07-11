/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package utility;

import java.text.SimpleDateFormat;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.cdac.framework.SystemConfiguration;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import mediatorPattern.ControllerMediator;
import model.OTSession;

/**
 * KafkaProducer.java<br>
 * Purpose: This class contains logic to act as producer for Kafka messaging
 * system
 *
 * @author Rohit_Sharma41
 *
 */
public class KafkaProducer
{
	private static final Logger LOG = Logger.getLogger(KafkaProducer.class.getName());

	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
	//private ProducerWorker producerThread;
	private Producer<Integer, String> producer;
	private String selectedOT = "", topic = "", producedData = "";

	public void setSelectedOT(String selectedOT)
	{
		this.selectedOT = selectedOT;
	}

	private static final String HYPHEN = "-";
	// private static final String KAFKA_IP = "10.74.18.64:9092";

	public Producer<Integer, String> getProducer()
	{
		return producer;
	}

	/*public ProducerWorker getProducerThread()
	{
		return producerThread;
	}*/

	public void setTopic(String topic)
	{
		this.topic = topic;
	}

	public void setProducedData(String producedData)
	{
		this.producedData = producedData;
	}

	/**
	 * Constructor
	 */
	public KafkaProducer()
	{
		//producerThread = new ProducerWorker();
	}

	/**
	 * This method sets producer properties for its initialization
	 */
	public void initializeProducer()
	{
		try
		{
			if (producer == null)
			{
				Properties producerProps = new Properties();
				producerProps.put("metadata.broker.list", SystemConfiguration.getInstance().getKafkaIp());
				producerProps.put("serializer.class", "kafka.serializer.StringEncoder");
				producerProps.put("request.required.acks", "1");
				ProducerConfig producerConfig = new ProducerConfig(producerProps);

				producer = new Producer<Integer, String>(producerConfig);

			}
		}
		catch (Exception e)
		{
			LOG.error("## Exception occured:", e);

		}

	}

	/**
	 * This method creates a string of required data and passes it over KAFKA
	 */
	public void callProducer()
	{
		try
		{
			if (ControllerMediator.getInstance().getMainController().isPublishMode())
			{
				if (selectedOT.isEmpty())
				{
					if (OTSession.getInstance().getSelectedOT() != null)
						selectedOT = OTSession.getInstance().getSelectedOT();
					selectedOT = selectedOT.replaceAll(" ", "_");
					selectedOT = selectedOT.replaceAll(HYPHEN, "_");
				}
				String finalTopic = selectedOT + HYPHEN + topic;
				KeyedMessage<Integer, String> keyedMsg = new KeyedMessage<Integer, String>(finalTopic, producedData);
				producer.send(keyedMsg);

				//LOG.debug("PRODUCER LOGIC >>> KEY :=====|" + finalTopic + "| , DATA :=========|" + producedData + "|");
			}
		}
		catch (Exception e)
		{
			// Main.getInstance().getUtility().errorDialogue("Error",
			// "Connection failed! Please contact administrator.");
			ControllerMediator.getInstance().getMainController().getToggleButtonPublish().setSelected(false);
			LOG.error("## Exception occured:", e);

		}

	}

	/**
	 * This method runs producer logic in background thread
	 *//*
	public class ProducerWorker extends UIWork
	{

		public ProducerWorker()
		{
			super("Kafka-Producer");
		}

		@Override
		public Object workLogic()
		{
			callProducer();
			return null;
		}

	}*/
}
