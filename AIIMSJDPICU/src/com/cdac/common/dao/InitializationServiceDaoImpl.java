/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package com.cdac.common.dao;

import org.hibernate.SessionFactory;
import org.apache.log4j.Logger;

import com.cdac.common.util.HibernateUtilities;
import com.cdac.framework.exceptions.ServiceExceptionWrapper;

public class InitializationServiceDaoImpl implements InitializationServiceDao
{
	// ~ Static attributes/initializers
	/** The Constant LOGGER. */
	// -------------------------------------------------------------
	private static final Logger LOGGER = Logger.getLogger(InitializationServiceDaoImpl.class.getName());


	// ~ Methods
	// ------------------------------------------------------------------------------------

	private static InitializationServiceDaoImpl instance = null;
	private InitializationServiceDaoImpl()
	{

	}

	public static InitializationServiceDaoImpl getInstance()
	{
		if(instance == null)
		{
			instance = new InitializationServiceDaoImpl();
		}
		return instance;
	}
	/**
	 * This method returns string containing success/error for succesfull server startup
	 * 
	 * @return string containing success/error for succesfull server startup
	 * 
	 */
	@SuppressWarnings("unused")
	@Override
	public String startingServer() throws Exception 
	{
		LOGGER.debug("Logger Name: " + LOGGER.getName());
		String result="";
		try 
		{

			// creates a new session factory object
			SessionFactory sessionFactory = HibernateUtilities.createSessionFactory();
			result="Session Created for the first time";

		}
		catch (Exception e)
		{
			LOGGER.error("## Exception occured:" + e.getMessage());
			throw new ServiceExceptionWrapper(e);
		}


		return result;
	}

}
