/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package model;

import java.util.HashMap;
import java.util.Map;

public class ConfigureDeviceSession {

	private final static ConfigureDeviceSession instance = new ConfigureDeviceSession();

	public static ConfigureDeviceSession getInstance() {
		return instance;
	}

	private DeviceModel device = new DeviceModel();
	private Map<String, String> lastDeviceReadings = new HashMap<String, String>();

	public Map<String, String> getLastDeviceReadings()
	{
		return lastDeviceReadings;
	}

	public void setLastDeviceReadings(Map<String, String> lastDeviceReadings)
	{
		this.lastDeviceReadings = lastDeviceReadings;
	}

	public DeviceModel getDevice() {
		return device;
	}

	public void setDevice(DeviceModel device) {
		this.device = device;
	}

}
