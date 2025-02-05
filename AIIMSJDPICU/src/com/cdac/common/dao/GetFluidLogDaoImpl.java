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

import com.cdac.common.GeneratedEntities.Fluid;
import com.cdac.common.GeneratedEntities.Fluidvalue;
import com.cdac.common.audit.interceptor.AuditLogInterceptor;
import com.cdac.common.pojoClasses.Volume;
import com.cdac.common.util.HibernateUtilities;
import com.cdac.framework.exceptions.ServiceExceptionWrapper;

public class GetFluidLogDaoImpl implements GetFluidLogDAo
{
	// ~ Static attributes/initializers
	private static GetFluidLogDaoImpl getFluidLogDaoImpl = null;
	/** The Constant LOGGER. */
	// -------------------------------------------------------------
	private static final Logger LOGGER = Logger.getLogger(GetFluidLogDaoImpl.class.getName());


	// ~ Methods
	// ------------------------------------------------------------------------------------

	private GetFluidLogDaoImpl()
	{

	}

	public static GetFluidLogDaoImpl getInstance()
	{
		if(getFluidLogDaoImpl == null)
		{
			getFluidLogDaoImpl = new GetFluidLogDaoImpl();
		}
		return getFluidLogDaoImpl;
	}


	/**
	 * This method returns  list of all the fluids available in the Database
	 * from Fluid entity
	 *
	 * @param patientId
	 * @param caseId
	 *
	 * @param userName.
	 * @return list of all the fluids available in the Database
	 *
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Fluid> getAllFluid(String userName) throws Exception
	{
		LOGGER.debug("Logger Name: " + LOGGER.getName());
		List<Fluid> list = null;
		Session session = null;
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

			// retrieve all the fluids from database
			Query query=session.createQuery("from Fluid f WHERE f.isFavourite=1");
			list=query.list();

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

		return list;
	}

	/**
	 * This method returns custom list according to the search entry by the user
	 * from Fluid entity
	 *  @param userEntry
	 *  - the search entry by the user
	 * @param patientId
	 * @param caseId
	 *
	 * @param userName.
	 * @return custom list according to the search entry by the user
	 *
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Fluid> getCustomList(String userEntry, int patientID, Long caseID, String userName) throws Exception
	{
		LOGGER.debug("Logger Name: " + LOGGER.getName());
		List<Fluid> CustomList=new ArrayList<Fluid>();
		Session session = null;
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

			// retrieve custom fluids from database
			Query query=session.createQuery("from Fluid where fluidName like '%"+userEntry+"%'");

			CustomList=query.list();

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

		return CustomList;
	}

	/**
	 * This method returns map containing success/error message along with fluid added
	 * from Fluid entity
	 *
	 *  @param fluidvalue
	 *  - the fluid to be added/updated
	 *
	 * @param userName.
	 *
	 * @return map containing success/error message along with fluid added
	 *
	 */
	@Override
	public Map<String, Fluidvalue> addFluid(Fluidvalue fluidvalue, String userName) throws Exception

	{
		LOGGER.debug("Logger Name: " + LOGGER.getName());
		Session session = null;
		Map<String, Fluidvalue> map=new HashMap<String, Fluidvalue>();
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

			/*if (fluidvalue.getFluidValueId() != null && !fluidvalue.getIsCompleted())
			{
				map.put("Cannot add fliud while previous one is not completed", fluidvalue);

			}
			else
			{
				session.saveOrUpdate(fluidvalue);
				map.put("Success", fluidvalue);
			}*/

			// for setting created time and created by in case of new record and updated time and updated by in case of exsisting record
			if(fluidvalue.getFluidValueId()!=null)
			{
				Date updatedTime= new Date();
				fluidvalue.setUpdatedBy(userName);
				fluidvalue.setUpdatedTime(updatedTime);
			}
			else
			{
				Date createdTime= new Date();
				fluidvalue.setCreatedBy(userName);
				fluidvalue.setCreatedTime(createdTime);
			}


			if (fluidvalue.getFluidValueId() == null || fluidvalue.getIsCompleted())
			{
				session.saveOrUpdate(fluidvalue);
				map.put("Success", fluidvalue);

			}
			else
			{
				map.put("Cannot add fluid while previous one is not completed", fluidvalue);
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
		return map;
	}

	/**
	 * This method returns map containing success/error message along with list of fluid object corresponding to patientID and caseID
	 * from Fluid entity
	 *
	 * @param patientId
	 * @param caseId
	 *
	 * @param userName.
	 *
	 * @return  map containing success/error message along with list of fluid object corresponding to patientID and caseID
	 *
	 */

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, List<Fluidvalue>> viewFluid(Long caseID, String userName) throws Exception
	{
		LOGGER.debug("Logger Name: " + LOGGER.getName());
		Session session = null;
		Map<String, List<Fluidvalue>> map=new HashMap<String, List<Fluidvalue>>();
		List<Fluidvalue> fluidvalueslist=new ArrayList<Fluidvalue>();
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

			// retrieves fluids corresponding to patientID and caseID
			Query query=session.createQuery("from Fluidvalue where caseId=?");
			query.setParameter(0, caseID);
			fluidvalueslist=query.list();
			map.put("List of fluids", fluidvalueslist);

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
	 * This method returns map containing success/error message along with list of fluid object corresponding to patientID and caseID
	 * from Fluid entity
	 *
	 * @param patientId
	 * @param caseId
	 *
	 * @param userName.
	 *
	 * @return  map containing success/error message along with list of fluid object corresponding to patientID and caseID
	 *
	 */

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, List<Fluidvalue>> viewTimedFluid(Long caseID, Date startTime, Date endTime, String userName) throws Exception
	{
		LOGGER.debug("Logger Name: " + LOGGER.getName());
		Session session = null;
		Map<String, List<Fluidvalue>> map=new HashMap<String, List<Fluidvalue>>();
		List<Fluidvalue> fluidvalueslist=new ArrayList<Fluidvalue>();
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

			// retrieves fluids corresponding to patientID and caseID
			Query query=session.createQuery("from Fluidvalue where caseId=? AND (startTime between ? and ? or finishTime between ? and ?)");
			query.setParameter(0, caseID);
			query.setParameter(1, startTime);
			query.setParameter(2, endTime);
			query.setParameter(3, startTime);
			query.setParameter(4, endTime);
			fluidvalueslist=query.list();
			map.put("List of fluids", fluidvalueslist);

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
	 * This method returns string containing success/error message for deleting a fluid from database
	 * from Fluid entity
	 *
	 *
	 * @param fluidValueID
	 * - id of the fluid to be deleted
	 *
	 * @param userName.
	 *
	 *
	 * @return string containing success/error message for deleting a fluid from database
	 *
	 */

	@Override
	public String deleteFluid(int fluidValueID, String userName) throws Exception
	{
		LOGGER.debug("Logger Name: " + LOGGER.getName());
		Session session = null;
		String result = "";
		Fluidvalue fluidvalue=new Fluidvalue();
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


			// gets the fluid corresponding to the ID provided
			fluidvalue=(Fluidvalue)session.get(Fluidvalue.class, new Integer(fluidValueID));


			session.delete(fluidvalue);
			result = "fluid Deleted";

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

	/**
	 * This method returns fluid object corresponding to the ID provided
	 * from Fluid entity
	 *
	 *
	 * @param fluidValueID
	 * - id of the fluid to be retrieved
	 *
	 * @param userName.
	 *
	 *
	 * @return fluid object corresponding to the ID provided
	 *
	 */

	@Override
	public Fluidvalue getFluidForUpdate(Integer fluidValueId, String userName) throws Exception
	{
		LOGGER.debug("Logger Name: " + LOGGER.getName());
		Session session = null;
		Fluidvalue fluidForUpdation=new Fluidvalue();
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

			//gets the fluid corresponding to the ID provided
			fluidForUpdation=(Fluidvalue)session.get(Fluidvalue.class,fluidValueId);

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

		return fluidForUpdation;
	}

	/**
	 * This method returns total volume  object corresponding to a perticular fluid
	 * from Fluid entity
	 *
	 *
	 * @param fluidID
	 * - id of the fluid whose total volume is to calculate
	 *
	 * @param patientId
	 * @param caseId
	 * @param userName.
	 *
	 *
	 * @return fluid object corresponding to the ID provided
	 *
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Volume getTotalVolume(String fluidName, int patientID, Long caseID, String userName) throws Exception
	{
		LOGGER.debug("Logger Name: " + LOGGER.getName());
		Volume totalVolume = new Volume();
		Session session = null;
		float totalVolumeCalculator = 0f;
		String Volumeunit = "";
		List<Fluidvalue> FluidValueListForcalculatingVolume=new ArrayList<Fluidvalue>();
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

			// retrieves all the fluid entries correspondin to the fluid id , case id, patient id
			Query query=session.createQuery("from Fluidvalue where patientId=? and caseId=? and fluidName=?");

			query.setParameter(0, patientID);
			query.setParameter(1, caseID);
			query.setParameter(2, fluidName);
			FluidValueListForcalculatingVolume=query.list();
			if(!FluidValueListForcalculatingVolume.isEmpty())
			{
				for(Fluidvalue object : FluidValueListForcalculatingVolume)
				{
					if(object.getVolume()!=null)
					{
						totalVolumeCalculator=totalVolumeCalculator+object.getVolume();
						Volumeunit=object.getVolumeUnit();
					}
				}
			}
			totalVolume.setValue(totalVolumeCalculator);
			totalVolume.setUnit(Volumeunit);
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
		return totalVolume;
	}
	/**
	 * This method returns  list of all the fluids available in the Database
	 * from Fluid entity
	 *
	 * @param patientId
	 * @param caseId
	 *
	 * @param userName.
	 * @return list of all the fluids available in the Database
	 *
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Fluidvalue searchFluidValue(Long caseID,Date timeStamp,String fluidName,String userName) throws Exception
	{
		LOGGER.debug("Logger Name: " + LOGGER.getName());
		Fluidvalue fluidvalue=new Fluidvalue();

		Session session = null;
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

			// retrieve all the fluids from database
			Query query=session.createQuery("from Fluidvalue fv where fv.caseId=? and fv.fluidName=? and fv.startTime=?");
			query.setParameter(0, caseID);
			query.setParameter(1, fluidName);
			query.setParameter(2, timeStamp);

			fluidvalue = (Fluidvalue) query.uniqueResult();

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

		return fluidvalue;
	}

	/**
	 * This method returns  list of all the fluids available in the Database
	 * from Fluid entity
	 *
	 * @param patientId
	 * @param caseId
	 *
	 * @param userName.
	 * @return list of all the fluids available in the Database
	 *
	 */
	@SuppressWarnings("unchecked")
	@Override
	public HashMap<String,String> getTotalFluidNameVolume(Long caseID,List<String> fluidNameList,String userName) throws Exception
	{
		LOGGER.debug("Logger Name: " + LOGGER.getName());
		List<Fluidvalue> fluidvalueslist=new ArrayList<Fluidvalue>();
		HashMap<String,String> totalFluidVolume = new HashMap<>();
		float volume=0f;
		String volumeUnit=" ml";
		String totalVolume="";
		Session session = null;
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

			// retrieve all the fluids from database
			Query query=session.createQuery("from Fluidvalue fv where fv.caseId=? and fv.fluidName in (:ids)");
			query.setParameter(0, caseID);
			query.setParameterList("ids", fluidNameList);


			fluidvalueslist = query.list();
		for(int i=0;i<fluidNameList.size();i++) {

				volume=0f;
			for(Fluidvalue object: fluidvalueslist)
			{

					if(object.getFluidName().equals(fluidNameList.get(i))) {
						if(object.getVolume()!=null)
						{
							volume=volume+object.getVolume();
						}
					}


				}
			totalVolume = volume+volumeUnit;
			totalFluidVolume.put(fluidNameList.get(i),totalVolume);

			}

			totalVolume = volume+volumeUnit;
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

		return totalFluidVolume;
	}

}
