/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package com.cdac.framework;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

import com.cdac.framework.exceptions.ServiceExceptionWrapper;


public class ApplicationSetting extends PropertiesConfiguration
{

	// ~ Static attributes/initializers
	// -------------------------------------------------------------
	private static final Logger LOGGER = Logger.getLogger(ApplicationSetting.class.getName());
	// ~ Methods
	// ------------------------------------------------------------------------------------


	/**
	 * This method saves the application setting into the properties files 

	 * @param parameterValue
	 *            value of parameter setting
	 * @param parameterName
	 *            name of the parameter setting
	 * @return void
	 * 
	 */
	//// for saving application setting
	public void savingApplicationSetting(String parameterName,String parameterValue) throws Exception
	{

		LOGGER.debug("Logger Name: " + LOGGER.getName());
		Properties properties =null;
		properties=	new Properties();
		InputStream inputStream = null;
		try
		{
			/// load the properties file into input stream
			inputStream = new FileInputStream("Resources/applicationSetting.properties");
			properties.load(inputStream);

			// set the properties value
			properties.setProperty(parameterName.trim(), parameterValue.trim());
			// save the changes into the properties files
			properties.store(new FileOutputStream("Resources/applicationSetting.properties"), null);
		}
		catch (Exception e) 
		{
			LOGGER.error("## Exception occured:" + e.getMessage());
			throw new ServiceExceptionWrapper(e);
		}
		finally 
		{
			if (inputStream != null) 
			{
				try 
				{
					inputStream.close();
				} 
				catch (Exception e) 
				{
					LOGGER.error("## Exception occured:" + e.getMessage());
					throw new ServiceExceptionWrapper(e);
				}
			}
		}
	}



	/**
	 * This method retrives the application setting  value corresponding to the key 

	 * @param searchApplicationName
	 *            name of the setting 
	 *
	 * @return String
	 * 			  value of the application setting
	 * @throws Exception 
	 * 
	 */
	public String getApplicationSetting(String searchApplicationName) throws Exception
	{
		LOGGER.debug("Logger Name: " + LOGGER.getName());
		Properties properties =null;
		properties=	new Properties();
		InputStream inputStream = null;
		String result ="";
		try 
		{
			/// load the properties file into input stream
			inputStream = new FileInputStream("Resources/applicationSetting.properties");
			properties.load(inputStream);

			// search the value of property corresponding to the name
			result=properties.getProperty(searchApplicationName);
		}
		catch (Exception e) 
		{
			LOGGER.error("## Exception occured:" + e.getMessage());
			throw new ServiceExceptionWrapper(e);
		}
		finally 
		{
			if (inputStream != null) 
			{
				try 
				{
					inputStream.close();
				} 
				catch (Exception e) 
				{
					LOGGER.error("## Exception occured:" + e.getMessage());
					throw new ServiceExceptionWrapper(e);
				}
			}
		}
		
		return result;
	}



	/**
	 * This method retrives the list of the application setting  names available in the system 

	 *
	 * @return List<String>
	 * 			  list of the application setting  names available in the system
	 * @throws Exception 
	 * 
	 */
	public List<String> getAllKeys() throws Exception
	{
		LOGGER.debug("Logger Name: " + LOGGER.getName());
		Properties properties =null;
		properties=	new Properties();
		InputStream inputStream = null;
		List<String> listOfkeys= new ArrayList<String>();
		try 
		{
			/// load the properties file into input stream
			inputStream = new FileInputStream("Resources/applicationSetting.properties");
			properties.load(inputStream);

			for(String property : properties.stringPropertyNames())
			{
				// add the setting name into the list
				listOfkeys.add(property);
			}
		} 
		catch (Exception e) 
		{
			LOGGER.error("## Exception occured:" + e.getMessage());
			throw new ServiceExceptionWrapper(e);
		}
		finally 
		{
			if (inputStream != null) 
			{
				try 
				{
					inputStream.close();
				} 
				catch (Exception e) 
				{
					LOGGER.error("## Exception occured:" + e.getMessage());
					throw new ServiceExceptionWrapper(e);
				}
			}
		}
		return listOfkeys;
	}


	/**
	 * This method deletes the application setting key value pair from the properties files
	 * 
	 * @param key
	 *            name of the setting to be deleted
	 *
	 *
	 * @return void
	 * @throws Exception 
	 * 
	 */
	public void deleteSetting(String key) throws Exception
	{
		LOGGER.debug("Logger Name: " + LOGGER.getName());
		Properties properties =null;
		properties=	new Properties();
		InputStream inputStream = null;
		try 
		{
			/// load the properties file into input stream
			inputStream = new FileInputStream("Resources/applicationSetting.properties");
			properties.load(inputStream);

			// deleting the property value
			properties.remove(key);

			// save the changes into the properties files
			properties.store(new FileOutputStream("Resources/applicationSetting.properties"), null);


		}
		catch (Exception e) 
		{
			LOGGER.error("## Exception occured:" + e.getMessage());
			throw new ServiceExceptionWrapper(e);
		}
		finally 
		{
			if (inputStream != null) 
			{
				try 
				{
					inputStream.close();
				} 
				catch (Exception e) 
				{
					LOGGER.error("## Exception occured:" + e.getMessage());
					throw new ServiceExceptionWrapper(e);
				}
			}
		}
	}


}


