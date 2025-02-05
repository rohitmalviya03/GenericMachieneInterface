/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package com.cdac.common.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.cdac.common.GeneratedEntities.Deviceconfiguration;
import com.cdac.common.audit.interceptor.AuditLogInterceptor;
import com.cdac.common.util.HibernateUtilities;
import com.cdac.framework.exceptions.ServiceExceptionWrapper;

public class DeviceConfigurationDaoImpl implements DeviceConfigurationDao
{
	// ~ Static attributes/initializers
	// -------------------------------------------------------------
	private static final Logger LOGGER = Logger.getLogger(DeviceConfigurationDaoImpl.class.getName());

	// ~ Instance attributes
	// ------------------------------------------------------------------------

	// ~ Constructors
	// -------------------------------------------------------------------------------

	// ~ Methods
	// ------------------------------------------------------------------------------------
	
	private static DeviceConfigurationDaoImpl instance = null;
	private DeviceConfigurationDaoImpl()
	{

	}

	public static DeviceConfigurationDaoImpl getInstance()
	{
		if(instance == null)
		{
			instance = new DeviceConfigurationDaoImpl();
		}
		return instance;
	}
	/**
	 * This method returns maps containing success/error meassage along with the device configuration ID.
	 * 
	 * @param dc
	 * contains the device configuration object which is to be saved
	 * 
	 * @param userName
	 * 
	 * @return maps containing success/error meassage along with the device configuration ID.
	 * @throws Exception 
	 * 
	 */
	//private Session session;
	@Override
	public Map<String, Object> createDeviceConfiguration(Deviceconfiguration dc,String userName) throws Exception 

	{
		// TODO Auto-generated method stub
		LOGGER.debug("Logger Name: " + LOGGER.getName());
		Session session=null;
		Map<String, Object> map= new HashMap<String, Object>();
		try 

		{
			// opens a new session from the session factory
			SessionFactory sessionFactory=HibernateUtilities.createSessionFactory();

			// For logging audit record
			AuditLogInterceptor interceptor=new AuditLogInterceptor();
			session=sessionFactory.withOptions().interceptor(interceptor).openSession();
			interceptor.setSession(session);
			interceptor.setUserName(userName);
			session.getTransaction().begin();

			// for setting created time and created by in case of new record and updated time and updated by in case of exsisting record
			
			if(dc.getDeviceCongigurationId()==0)
			{
				Date createdTime= new Date();
				dc.setCreatedBy(userName);
				dc.setCreatedTime(createdTime);
			}
			else
			{
				Date updatedTime= new Date();
				dc.setUpdatedBy(userName);
				dc.setUpdatedTime(updatedTime);
			}


			// persisiting the device configuration object into the database
			session.saveOrUpdate(dc);

			session.getTransaction().commit();
			map.put("Success", "Device configuration done with the device confuration id: "+dc.getDeviceCongigurationId());

		}
		catch (Exception e)
		{
			if (session != null && session.getTransaction() != null)
			{
				// If transaction is open,rollback
				session.getTransaction().rollback();
			}
			LOGGER.error("## Exception occured:" + e.getMessage());
			throw new ServiceExceptionWrapper(e);
		}
		finally
		{
			try 
			{
				if(session != null)
				{
					session.close(); // Session closes
				}
			}
			catch (Exception e2) 
			{
				LOGGER.error("## Exception occured:" + e2.getMessage());
				throw new ServiceExceptionWrapper(e2);
			}
		}
		return map;
	}

}
