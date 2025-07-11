/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package com.cdac.common.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.cdac.common.GeneratedEntities.Deviceconfiguration;
import com.cdac.common.GeneratedEntities.Entityvalues;
import com.cdac.common.GeneratedEntities.IntraopDevices;
import com.cdac.common.audit.interceptor.AuditLogInterceptor;
import com.cdac.common.pojoClasses.DeviceConfigurationForUI;
import com.cdac.common.util.HibernateUtilities;
import com.cdac.framework.exceptions.ServiceExceptionWrapper;

public class GetDeviceInfoDaoImpl implements GetDeviceInfoDao

{
	// ~ Static attributes/initializers
	/** The Constant LOGGER. */
	// -------------------------------------------------------------
	private static final Logger LOGGER = Logger.getLogger(ViewAllCasesDaoImpl.class.getName());
	
	private static GetDeviceInfoDaoImpl instance = null;
	private GetDeviceInfoDaoImpl()
	{

	}

	public static GetDeviceInfoDaoImpl getInstance()
	{
		if(instance == null)
		{
			instance = new GetDeviceInfoDaoImpl();
		}
		return instance;
	}

	//private Session session;


	// ~ Methods
	// ------------------------------------------------------------------------------------

	/**
	 * This method returns map list of devices configured w.r.t. patientID and caseID
	 * from Deviceconfiguration entity
	 * 
	 * 
	 * @param userName.
	 * 
	 * @param patientId
	 * - patient ID of the patient whose record is to retrieved
	 * 
	 * @param caseId
	 * -case ID of the patient whose record is to retrieved
	 * @return map list of devices configured w.r.t. patientID and caseID.
	 * @throws Exception 
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, List<DeviceConfigurationForUI>> getListOfdevices(String userName, Integer patientId, Long caseId) throws Exception

	{
		LOGGER.debug("Logger Name: " + LOGGER.getName());
		Map<String, List<DeviceConfigurationForUI>> map=new HashMap<String, List<DeviceConfigurationForUI>>();
		Session session = null;
		try
		{
			// opens a new session from the session factory
			SessionFactory sessionFactory =HibernateUtilities.createSessionFactory();

			// For logging audit record
			AuditLogInterceptor interceptor=new AuditLogInterceptor();
			session=sessionFactory.withOptions().interceptor(interceptor).openSession();
			interceptor.setSession(session);
			interceptor.setUserName(userName);
			session.getTransaction().begin();

			// for retrieving list of device configuration
			Query query=session.createQuery("from Deviceconfiguration con " + "where con.patientId=? and con.caseId=?");
			query.setParameter(0, patientId);
			query.setParameter(1, caseId);
			List<Deviceconfiguration> list=new ArrayList<Deviceconfiguration>();
			list=query.list();
			List<DeviceConfigurationForUI> listForDeviceConfiguration =new ArrayList<DeviceConfigurationForUI>();
			for(Deviceconfiguration deviceconfiguration : list)
			{
				DeviceConfigurationForUI configurationForUI= new DeviceConfigurationForUI();
				configurationForUI.setCaseId(deviceconfiguration.getCaseId());
				configurationForUI.setCreatedBy(deviceconfiguration.getCreatedBy());
				configurationForUI.setCreatedTime(deviceconfiguration.getCreatedTime());
				/*configurationForUI.setDepartment(deviceconfiguration.getDepartmentId());*/
				configurationForUI.setDeviceCongigurationId(deviceconfiguration.getDeviceCongigurationId());
				/*configurationForUI.setDeviceId(deviceconfiguration.getDeviceId());*/
				configurationForUI.setDeviceStatus(deviceconfiguration.getDeviceStatus());
				configurationForUI.setMedicine(deviceconfiguration.getMedicine());
				configurationForUI.setModel(deviceconfiguration.getModel());
				/*configurationForUI.setOt(deviceconfiguration.getotId());*/
				configurationForUI.setPortNumber(deviceconfiguration.getPortNumber());
				configurationForUI.setPatientId(deviceconfiguration.getPatientId());
				configurationForUI.setSerialNumber(deviceconfiguration.getSerialNumber());
				configurationForUI.setUpdatedBy(deviceconfiguration.getUpdatedBy());
				configurationForUI.setUpdatedTime(deviceconfiguration.getUpdatedTime());
				configurationForUI.setWardNumber(deviceconfiguration.getWardNumber());
				configurationForUI.setTransportData(deviceconfiguration.getTransportData());

				/*if(deviceconfiguration.getDepartmentId()!=null)
				{
					Entityvalues entityvalues=(Entityvalues)session.get(Entityvalues.class, new Integer(deviceconfiguration.getDepartmentId()));
					configurationForUI.setDepartmentName(entityvalues.getEntityValue());
				}*/
				configurationForUI.setDepartmentName(deviceconfiguration.getDepartment());
				
				/*if(deviceconfiguration.getotId()!=null)
				{
					Entityvalues entityvalues2=(Entityvalues)session.get(Entityvalues.class, new Integer(deviceconfiguration.getotId()));
					configurationForUI.setOtName(entityvalues2.getEntityValue());
				}*/
				configurationForUI.setOtName(deviceconfiguration.getOt());
				
				/*if(deviceconfiguration.getDeviceId()!=0)
				{
					IntraopDevices devices=(IntraopDevices)session.get(IntraopDevices.class, new Integer(deviceconfiguration.getDeviceId()));
					configurationForUI.setMake(devices.getMake());
				}*/
				listForDeviceConfiguration.add(configurationForUI);

			}

			map.put("list of devices",listForDeviceConfiguration);

			//commits the session
			session.getTransaction().commit();

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

	/**
	 * This method returns ID of the device configured whose status has been changed
	 * from Deviceconfiguration entity
	 * 
	 * @param deviceCongigurationId
	 * - device CongigurationId of the device whose status is to change
	 * @param deviceStatus
	 * - new status
	 * 
	 * @param userName.
	 * 
	 * 
	 * @return  ID of the device configured whose status has been changed
	 * 
	 */
	@Override
	public int changeStatus(int deviceCongigurationId,Boolean deviceStatus, String userName) throws Exception

	{
		LOGGER.debug("Logger Name: " + LOGGER.getName());
		Session session = null;
		int deviceConfigurationIdafterdeletion=0;
		Deviceconfiguration deviceconfiguration=new Deviceconfiguration();
		try
		{
			// opens a new session from the session factory
			SessionFactory sessionFactory = HibernateUtilities.createSessionFactory();

			// For logging audit record
			AuditLogInterceptor interceptor = new AuditLogInterceptor();
			session = sessionFactory.withOptions().interceptor(interceptor).openSession();
			interceptor.setSession(session);
			interceptor.setUserName(userName);
			session.getTransaction().begin();

			// retrieves the device config object corresponding to the ID provided
			deviceconfiguration=(Deviceconfiguration)session.get(Deviceconfiguration.class, new Integer(deviceCongigurationId));

			Date updatedTime= new Date();
			deviceconfiguration.setUpdatedBy(userName);
			deviceconfiguration.setUpdatedTime(updatedTime);
			deviceconfiguration.setDeviceStatus(deviceStatus);
			session.saveOrUpdate(deviceconfiguration);
			deviceConfigurationIdafterdeletion=deviceconfiguration.getDeviceCongigurationId();

			//commits the session
			session.getTransaction().commit();

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
		return deviceConfigurationIdafterdeletion;
	}

	/**
	 * This method returns string containg success/error for deleting the device config
	 * from Deviceconfiguration entity
	 * 
	 * @param deviceCongigurationId
	 * - device config ID to be deleted 
	 * @param userName.
	 * 
	 * 
	 * @return string containg success/error for deleting the device config
	 * 
	 */
	@Override
	public String deleteDeviceConfiguration(int deviceCongigurationId, String userName) throws Exception
	{
		LOGGER.debug("Logger Name: " + LOGGER.getName());
		Session session = null;
		String result = "";
		Deviceconfiguration deviceconfigurationForDeletion=new Deviceconfiguration();
		try
		{
			// opens a new session from the session factory
			SessionFactory sessionFactory = HibernateUtilities.createSessionFactory();

			// For logging audit record
			AuditLogInterceptor interceptor = new AuditLogInterceptor();
			session = sessionFactory.withOptions().interceptor(interceptor).openSession();
			interceptor.setSession(session);
			interceptor.setUserName(userName);
			session.getTransaction().begin();

			// retrieves the device config object corresponding to the ID provided
			deviceconfigurationForDeletion=(Deviceconfiguration)session.get(Deviceconfiguration.class, deviceCongigurationId);

			/*if(deviceconfigurationForDeletion!=null)
			{
				session.delete(deviceconfigurationForDeletion);
				result = "Configured Device with ID: "+deviceCongigurationId +" has been deleted succesfully";
			}
			else
			{
				result="Error, No Device is configured with the provided data";
			}*/


			if(deviceconfigurationForDeletion==null)
			{
				result="Error, No Device is configured with the provided data";

			}
			else
			{
				session.delete(deviceconfigurationForDeletion);
				result = "Configured Device with ID: "+deviceCongigurationId +" has been deleted succesfully";
			}
			//commits the session
			session.getTransaction().commit();
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
		return result;
	}

}
