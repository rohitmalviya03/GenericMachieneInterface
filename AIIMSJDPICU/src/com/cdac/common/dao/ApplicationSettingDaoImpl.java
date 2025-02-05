/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package com.cdac.common.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.cdac.common.GeneratedEntities.Applicationsetting;
import com.cdac.common.audit.interceptor.AuditLogInterceptor;
import com.cdac.common.util.HibernateUtilities;


/**
 * @author sahil.sharma08
 *
 */

//------------------------------- This service is not in use------------------------------------
public class ApplicationSettingDaoImpl implements ApplicationSettingDao {

	// ~ Static attributes/initializers
	
	private static final Logger LOGGER = Logger.getLogger(ApplicationSettingDaoImpl.class.getName());
	
	
	
	// ~ Methods
	// ------------------------------------------------------------------------------------
	
	private static ApplicationSettingDaoImpl instance = null;
	private ApplicationSettingDaoImpl()
	{

	}

	public static ApplicationSettingDaoImpl getInstance()
	{
		if(instance == null)
		{
			instance = new ApplicationSettingDaoImpl();
		}
		return instance;
	}
	
	/**
	 * This method returns String  having success/failure message 
	 * from application setting entity
	 * 
	 * 
	 * @param applicationsettingObject
	 * - the setting which needs to be saved in the database.
	 * 
	 * @param userName.
	 * 
	 * 
	 * @return String  having success/failure message.
	 * 
	 */
	@Override
	public String addSetting(Applicationsetting applicationsettingObject, String userName) throws Exception 
	{
		LOGGER.info("Logger Name: " + LOGGER.getName());
		Session session = null;
		String result="";
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
			
			// saves the application setting into the database
			session.saveOrUpdate(applicationsettingObject);
			
			//commits the session
			session.getTransaction().commit();
			
			result="Succesfully added the  application setting with the parameterID: "+applicationsettingObject.getParameterId();
		}
		catch (Exception e)
		{
			if (session != null && session.getTransaction() != null)
			{
				// If transaction is open,roll back
				session.getTransaction().rollback();
			}
			LOGGER.log(Level.INFO, "Exception occur", e);
			throw e;
		}
		finally
		{

			session.close(); // Session close

		}
		return result;
	}
	
	/**
	 * This method returns list of application setting 
	 * from application setting entity
	 * 
	 * 
	 * @param userName.
	 * 
	 * 
	 * @return list of application setting.
	 * 
	 */
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Applicationsetting> getListOfApplicationSettings(String userName) throws Exception 
	{
		LOGGER.info("Logger Name: " + LOGGER.getName());
		Session session = null;
		List<Applicationsetting> listOfApplicationSetting=new ArrayList<Applicationsetting>();

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
			
			// retrieves all the setting from the database
			Query query= session.createQuery("from Applicationsetting");
			
			listOfApplicationSetting=query.list();
			
			// Session commit
			session.getTransaction().commit(); 
		}
		catch (Exception e)
		{
			if (session != null && session.getTransaction() != null)
			{
				// If transaction is open,rollback
				session.getTransaction().rollback();
			}
			LOGGER.log(Level.INFO, "Exception occur", e);
			throw e;
		}
		finally
		{
			session.close(); // Session close
		}
		return listOfApplicationSetting;
	}
	/**
	 * This method string containing the success message along with the delteted application setting 
	 * from application setting entity
	 * 
	 *  @param parameterID.
	 * - Setting ID which is to be deleted.
	 * 
	 * @param userName.
	 * 
	 * 
	 * @return string containing the success message along with the delteted application setting 
	 * 
	 */
	@Override
	public String deleteSetting(Integer parameterID, String userName) throws Exception 
	{
		LOGGER.info("Logger Name: " + LOGGER.getName());
		Session session = null;
		String result="";
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

			/// deletes the application setting from the database 
			Query query =session.createQuery("delete from Applicationsetting where parameterId=:parameterID");
			query.setParameter("parameterID", parameterID);
			query.executeUpdate();

			result="Succesfullt deleted application setting with the ID: "+parameterID;
			
			// Session commit
			session.getTransaction().commit(); 
		}
		catch (Exception e)
		{
			if (session != null && session.getTransaction() != null)
			{
				// If transaction is open,rollback
				session.getTransaction().rollback();
			}
			LOGGER.log(Level.INFO, "Exception occur", e);
			throw e;
		}
		finally
		{
			session.close(); // Session close
		}
		return result;
	}
	
}
