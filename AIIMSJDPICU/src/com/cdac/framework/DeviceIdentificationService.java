/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package com.cdac.framework;

import java.util.ArrayList;
import java.util.List;

public class DeviceIdentificationService
{
    static DeviceIdentificationService deviceService;
    List<IDeviceHandshake> handshakeList;
    IDeviceHandshake lastHandshakeInstance;

    private DeviceIdentificationService()
	{
	    handshakeList = new ArrayList<IDeviceHandshake>();
	}

	/**
	 * Returns a list of available handshakes.
	 *
	 * @return handshakeList is a list of all device handshakes available in the
	 *         system.
	 */
	public List<IDeviceHandshake> getHandshakeList()
	{
		return handshakeList;
	}

	public static DeviceIdentificationService getInstance()
    {
		if (deviceService == null)
		{
			deviceService = new DeviceIdentificationService();
			return deviceService;
		}
		else
		{
			return deviceService;
		}
    }

	// TODO: Should be called from AppStartup which will read from a common
	// source, construct the given handshakes and register them here.
    public void registerHandshakes(IDeviceHandshake deviceHandshake)
    {
		// Create chain of responsibility.
		/*if (lastHandshakeInstance != null)
		{
			lastHandshakeInstance.registerNextDeviceHandshakeClass(deviceHandshake);
		}
		// Update lastHandshakeInstance to current.
		lastHandshakeInstance = deviceHandshake;*/
    	handshakeList.clear();
		handshakeList.add(deviceHandshake);
    }

	IDeviceHandshake getFirstHandshake()
	{
		return handshakeList.get(0);
	}

}
