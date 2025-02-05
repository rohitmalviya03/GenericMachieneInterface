/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package com.cdac.framework;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.cdac.framework.datastructures.MedicalDeviceData;
import com.cdac.framework.datastructures.NodeBase;

public class DeviceDataSaveService extends Work
{
	private static final Logger LOG = Logger.getLogger(DeviceDataSaveService.class);
//	private static final String DEFAULT_TREND_VALUE = "32000|-32000";
	private static DeviceDataSaveService dataSaveService;
	private PrintWriter printWriter;
	private char delimiter;
	private String previousDeviceHeader = "";
	private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	private Map<String, String> parametersMap;
	private List<String> listOfParameters;
	private boolean isCaseLoaded = false;
	private File deviceDataFile = null;
	private boolean alreadyExists = false;

	static
	{
		dataSaveService = new DeviceDataSaveService();
	}

	private DeviceDataSaveService()
	{
		super("DeviceDataSaveService");
		//Create file and add the delimiter information.
		//createFileAndWriteHeaders();//TODO Sahil Gupta
	}

	/**
	 * This code was earlier in
	 */
	public void createFileAndWriteHeaders() {
		try
		{
			printWriter = new PrintWriter(new FileOutputStream(deviceDataFile.getAbsolutePath(), true));
		}
		catch (FileNotFoundException e)
		{
			LOG.error("File not found for writing device data:" + e.getMessage());
			e.printStackTrace();
			return;
		}

		parametersMap = new LinkedHashMap<String, String>();
		delimiter = SystemConfiguration.getInstance().getDelimiterForDataDump();
		if(!alreadyExists)
		printWriter.append("sep=" + delimiter + "\n");
		listOfParameters = createParameterListAndWriteHeader();
	}

	public static DeviceDataSaveService getInstance()
	{
		return dataSaveService;
	}

	private List<String> createParameterListAndWriteHeader()
	{
		StringBuilder header = new StringBuilder("Date/Time").append(delimiter);
		List<String> listOfParameters = SystemConfiguration.getInstance().getListOfParametersToSave();
		for (String parameterName : listOfParameters)
		{
			header.append(parameterName).append(delimiter);
			// Setup the tree map so that we have a map ready to hold the values
			// later.
			// This is done because we don't know the order in which the values
			// will be retrieved later. Therefore, putting the keys in an order
			// first will not disturb the order when values are inserted.
			parametersMap.put(parameterName, null);
		}

		// Add a header.
		if(!alreadyExists)
		printWriter.println(header);

		return listOfParameters;
	}

	public void setIsCaseLoaded(boolean isCaseLoaded)
	{
		this.isCaseLoaded = isCaseLoaded;
	}

	/**
	 * This method is for creating a file based on the case number
	 * If file is already existing then new file will not be created
	 * @param caseNumber
	 * @return
	 */
	public File createFile(long caseNumber)
	{
		File directory = new File(SystemConfiguration.getInstance().getLocationForDeviceDataDump());
	    if (! directory.exists()){
	        directory.mkdir();
	        // If you require it to make the entire directory path including parents,
	        // use directory.mkdirs(); here instead.
	    }
		deviceDataFile = new File(directory, "DeviceData_" + caseNumber + ".csv");
		if (!deviceDataFile.exists()) {
			try {
				deviceDataFile.createNewFile();
				alreadyExists = false;
			} catch (IOException e) {
				e.printStackTrace();
				LOG.error("## Exception occured:", e);
			}
		}
		else
		{
			alreadyExists = true;
		}
		return deviceDataFile;
	}

	public void closeFile()
	{
		printWriter.close();
	}

	@Override
	public Object workLogic()
	{
		if(isCaseLoaded)
		{
			LOG.info("Generating data dump for all the devices and saving them to the file");
			if (printWriter == null)
			{
				LOG.warn("Cannot save device data.");
				return null;
			}

			List<PortManagerAndListener> portsAndDevices = DeviceScanner.getInstance().getPortDeviceMapping();

			StringBuilder dataLine = new StringBuilder(dateFormat.format(new Date())).append(delimiter);

			for (PortManagerAndListener device : portsAndDevices)
			{
				if (device.getDeviceListeningToThisPort() instanceof CoreDevice)
				{
					CoreDevice deviceParser = (CoreDevice) device.getDeviceListeningToThisPort();
					for (String branchParameter : listOfParameters)
					{
						MedicalDeviceData deviceData = deviceParser.getDeviceData();
						NodeBase branch[] = null;
						try
						{
							branch = deviceData.getBranch(branchParameter);
						}
						catch(Exception e)
						{
							System.out.println("+++>>>>>>Branch is " + branchParameter);
						}

						if (branch != null)
						{
							parametersMap.put(branchParameter, branch[2].getData() + "");
							// Reset the parameters so that they can now track the next cycle
							//branch[2].resetSavedData(DEFAULT_TREND_VALUE);//TODO Remove resetting
						}
					}
				}
			}

			// Data captured, let's create the line to be added into the file.
			for (String value : parametersMap.values())
			{
				dataLine.append(value).append(delimiter);
			}

			// Let's write the line to the file.
			printWriter.println(dataLine.toString());
			printWriter.flush();

			// Clear all values from parametersMap so that it is ready for next
			// time.
			parametersMap.replaceAll((k, v) -> null);
		}

		return null;
	}

}
