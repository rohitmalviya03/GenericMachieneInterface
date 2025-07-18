/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package com.cdac.common.dao;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.cdac.common.GeneratedEntities.Entityvalues;
import com.cdac.common.audit.interceptor.AuditLogInterceptor;
import com.cdac.common.util.HibernateUtilities;
import com.cdac.framework.exceptions.ServiceExceptionWrapper;

public class SpecialityOTDaoImpl implements SpecialityOTDao{

	// ~ Static attributes/initializers
	/** The Constant LOGGER. */
	// -------------------------------------------------------------
	private static final Logger LOGGER = Logger.getLogger(SpecialityOTDaoImpl.class.getName());


	// ~ Methods
	// ------------------------------------------------------------------------------------

	private static SpecialityOTDaoImpl instance = null;
	private SpecialityOTDaoImpl()
	{

	}

	public static SpecialityOTDaoImpl getInstance()
	{
		if(instance == null)
		{
			instance = new SpecialityOTDaoImpl();
		}
		return instance;
	}


	/**
	 * This method returns list of all OT corresponding to a specialities
	 * from Entityvalues entity
	 *
	 *
	 * @param specialityID
	 * - speciality ID corresponding to which OT is to retrieved
	 *
	 *
	 *
	 * @param userName
	 *
	 * @return list of all OT corresponding to a specialities
	 *
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Entityvalues> getAllOT(List<Integer> specialityIDList, String userName) throws Exception
	{
		LOGGER.debug("Logger Name: " + LOGGER.getName());
		Session session = null;
		List<Entityvalues> listOfOT= new ArrayList<Entityvalues>();
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

			if(specialityIDList!=null)
			{
				/// retrieving the list of all OT corresponding to the specialities
				Query query= session.createQuery("select evot from Specialityot so inner join Entityvalues evot on so.entityValueOT.entityValueId=evot.entityValueId where so.entityValueSpeciality.entityValueId in (:ids)");
				query.setParameterList("ids",specialityIDList);
				listOfOT=query.list();
			}
			else
			{
				Query query = session.createQuery("select evot from Specialityot so inner join Entityvalues evot on so.entityValueOT.entityValueId=evot.entityValueId");
				listOfOT=query.list();
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
		return listOfOT;
	}

}
