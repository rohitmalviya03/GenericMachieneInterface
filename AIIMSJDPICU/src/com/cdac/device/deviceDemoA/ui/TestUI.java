/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package com.cdac.device.deviceDemoA.ui;

import java.util.ArrayList;
import java.util.List;

import com.cdac.device.deviceDemoA.DeviceDemoAParser;
import com.cdac.framework.CoreDevice;
import com.cdac.framework.datastructures.Channel;
import com.cdac.framework.datastructures.IDataObserver;
import com.cdac.framework.datastructures.NodeBase;
import com.cdac.framework.datastructures.Parameter;

public class TestUI
{

	CoreDevice model;

	public TestUI(CoreDevice device)
	{
		model = device;
		Channel channel1 = (Channel) model.getNodeByIdentifier(DeviceDemoAParser.VENTILATOR_CHANNEL1);
		channel1.addObserver(new Channel1Listener());
	}

	class Channel1Listener implements IDataObserver
	{

		Parameter param1;
		Parameter param2;
		List<NodeBase> nodesToListen;
		
		public Channel1Listener()
		{
			Parameter param1 = (Parameter) model.getNodeByIdentifier(DeviceDemoAParser.VENT_CHANNEL1_PARAM1);
			Parameter param2 = (Parameter) model.getNodeByIdentifier(DeviceDemoAParser.VENT_CHANNEL1_PARAM2);

			nodesToListen = new ArrayList<>();
			nodesToListen.add(param1);
			nodesToListen.add(param2);
		}

		@Override
		public NotificationRule getNotificationRule()
		{
			return NotificationRule.SELECTED_NODES;
		}

		@Override
		public List<NodeBase> getNodesUnderObservation()
		{
			return nodesToListen;
		}

		@Override
		public void update(NodeBase notificationTriggeringNode, NodeBase nodeThatLeadToEventNotification)
		{
			System.out.println("Channel1_Parameter1:" + param1.getData());
			System.out.println("Channel1_Parameter2:" + param2.getData());

		}

	}

	class Channel2Listener implements IDataObserver
	{

		@Override
		public NotificationRule getNotificationRule()
		{
			return NotificationRule.EVERY_NODE;
		}

		@Override
		public List<NodeBase> getNodesUnderObservation()
		{
			return null;
		}

		@Override
		public void update(NodeBase notificationTriggeringNode, NodeBase nodeThatLeadToEventNotification)
		{
			System.out.println("Channel2 Parameter:" + nodeThatLeadToEventNotification.getData());
		}

	}

}
