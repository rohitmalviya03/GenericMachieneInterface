/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package com.cdac.framework.datastructures;

import java.util.Collection;

import com.cdac.framework.exceptions.KeyExistsException;

public class VirtualMedicalDevice<T> extends NodeBase<T>
{
	public VirtualMedicalDevice(String uniqueNameForVMD, NodeBase<T> parent)
	{
		super(uniqueNameForVMD, parent);
	}

	/**
	 * This method should not be used within or below this hierarchy. Use
	 * {@link #addChannel(String, Channel)} instead.
	 */
	@Override
	@Deprecated
	protected NodeBase<T> addNewChild(String key, NodeBase<T> child) throws KeyExistsException
	{
		return super.addNewChild(key, child);
	}

	/**
	 * Add a customized channel
	 * 
	 * @param referralName
	 * @param channel
	 * @return
	 * @throws KeyExistsException
	 */
	private Channel<T> addChannel(String referralName, Channel<T> channel) throws KeyExistsException
	{
		channel.setParent(this); // Preventive step
		super.addNewChild(referralName, channel);
		return channel;
	}

	/**
	 * Add a channel of default type.
	 * 
	 * @param referralConstant
	 * @param channelName
	 * @return
	 * @throws KeyExistsException
	 */
	private Channel<T> addChannel(String referralConstant, String channelName) throws KeyExistsException
	{
		Channel<T> channel = new Channel<>(channelName, this);
		super.addNewChild(referralConstant, channel);
		return channel;
	}

	/**
	 * The channel name should be unique within this VMD
	 * 
	 * @param channelName
	 * @return
	 * @throws KeyExistsException
	 */
	private Channel<T> addChannel(String channelName) throws KeyExistsException
	{
		return addChannel(channelName, channelName);
	}

	Channel<T> createOrGetChannel(String name)
	{
		Channel<T> channel = (Channel<T>) children.get(name);
		if (channel == null)
		{
			try
			{
				channel = addChannel(name);
			}
			catch (KeyExistsException e)
			{
				// This will not occur.
			}
		}

		return channel;
	}

	public Collection getAllChannels()
	{
		return getChildren();
	}

	public Channel getChannel(String channelIdentifier)
	{
		return (Channel) getChild(channelIdentifier);
	}
}
