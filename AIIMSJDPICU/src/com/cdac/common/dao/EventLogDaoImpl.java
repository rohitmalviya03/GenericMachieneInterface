/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package com.cdac.common.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.cdac.common.GeneratedEntities.EventLogEventStatus;
import com.cdac.common.GeneratedEntities.IntraopEventlog;
import com.cdac.common.GeneratedEntities.IntraopEventsmaster;
import com.cdac.common.GeneratedEntities.IntraopSurgeryevents;
import com.cdac.common.GeneratedEntities.Patient;
import com.cdac.common.GeneratedEntities.Patientcase;
import com.cdac.common.GeneratedEntities.SurgeryEventStatus;
import com.cdac.common.audit.interceptor.AuditLogInterceptor;
import com.cdac.common.util.HibernateUtilities;
import com.cdac.framework.exceptions.ServiceExceptionWrapper;

// TODO: Auto-generated Javadoc
/**
 * The Class EventLogDaoImpl.
 *
 * @author gursimratpreetkaur_d
 */
public class EventLogDaoImpl implements EventLogDao{

	/** The Constant LOGGER. */
	private static final Logger LOGGER = Logger.getLogger(EventLogDaoImpl.class.getName());

	private static EventLogDaoImpl instance = null;
	private EventLogDaoImpl()
	{

	}

	public static EventLogDaoImpl getInstance()
	{
		if(instance == null)
		{
			instance = new EventLogDaoImpl();
		}
		return instance;
	}

	/*
	 * public List<IntraopEventsmaster> getAllSurgeryEvents_bkup(int surgeryID,
	 * String userName) throws Exception { LOGGER.debug("Logger Name: " +
	 * LOGGER.getName()); List<IntraopEventsmaster> eventsList = new
	 * ArrayList<>(); Session session = null; try {
	 *
	 * // opens a new session from the session factory SessionFactory
	 * sessionFactory = HibernateUtilities.createSessionFactory();
	 *
	 * // For logging audit record AuditLogInterceptor interceptor = new
	 * AuditLogInterceptor(); session =
	 * sessionFactory.withOptions().interceptor(interceptor).openSession();
	 * interceptor.setSession(session); interceptor.setUserName(userName);
	 * session.getTransaction().begin();
	 *
	 * Query query = session.createQuery(
	 * "FROM IntraopEventsmaster iem where iem.surgeryTypeId=?");
	 * query.setParameter(0, surgeryID); // query.setParameter(0, patientID); //
	 * query.setParameter(1, caseID); eventsList = query.list();
	 *
	 * session.getTransaction().commit();
	 *
	 * } catch (Exception e) { if (session != null && session.getTransaction()
	 * != null) { // If transaction is open,rollback
	 * session.getTransaction().rollback(); } LOGGER.log(Level.INFO,
	 * "Exception occur", e); throw e; } finally {
	 *
	 * session.close(); // Session close
	 *
	 * }
	 *
	 * // TODO Auto-generated method stub return eventsList; }
	 */

	/*
	 * (non-Javadoc)
	 *
	 * @see com.infosys.common.dao.EventLogDao#getAllSurgeryEvents(int,
	 * java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<SurgeryEventStatus> getAllSurgeryEvents(String surgeryName, int patientID, Long patientCaseID,
			String userName) throws Exception
	{
		LOGGER.debug("Logger Name: " + LOGGER.getName());
		List<SurgeryEventStatus> surgeryEventsListWithStatus = new ArrayList<SurgeryEventStatus>();
		Session session = null;
		System.out.println("--");
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

			// get event list
			Query queryEventList = session
					.createQuery("FROM IntraopSurgeryevents ise "
							+ "inner join IntraopEventsmaster iem on iem.eventId=ise.intraopEventsmaster.eventId"
							+ " where ise.surgeryName=?");
			queryEventList.setParameter(0, surgeryName);
			List<Object> surgeryEventsObjectList = queryEventList.list();

			// get event log
			Query query = session
					.createQuery(
							"FROM IntraopEventlog iel "
									+ "inner join IntraopEventsmaster iem on iem.eventId=iel.intraopEventsmaster.eventId "
									+ " where iel.patient.patientId=? and iel.patientcase.caseId=?");
			query.setParameter(0, patientID);
			query.setParameter(1, patientCaseID);
			List<Object> eventLogObjectList = query.list();

			session.getTransaction().commit();

			for (Iterator<Object> surgeryEventsIterator = surgeryEventsObjectList.iterator(); surgeryEventsIterator
					.hasNext();)
			{
				Object[] surgeryEventsObjectArray = (Object[]) surgeryEventsIterator.next();
				IntraopSurgeryevents surgeryEvent = (IntraopSurgeryevents) surgeryEventsObjectArray[0];

				boolean presentInEventLog=false;
				for (Iterator<Object> eventLogIterator = eventLogObjectList.iterator(); eventLogIterator.hasNext();)
				{
					Object[] eventLogObjectArray = (Object[]) eventLogIterator.next();
					IntraopEventlog eventLog = (IntraopEventlog) eventLogObjectArray[0];


					if(surgeryEvent.getIntraopEventsmaster().getEventId()==eventLog.getIntraopEventsmaster().getEventId())
					{
						presentInEventLog=true;
						break;
					}

					/*// cahnges by shail.sharma08 start here
					if(Validations.isDigitWithoutDot(eventLog.getEvent()))
					{
						if(surgeryEvent.getIntraopEventsmaster().getEventId()==new Integer(eventLog.getEvent()))
						{
							presentInEventLog=true;
							break;
						}
					}
					else if (eventLog.getEvent().equalsIgnoreCase(surgeryEvent.getIntraopEventsmaster().getEventName()))
					{
						presentInEventLog=true;
						break;
					}
					// changes by shail.sharma08 end here
					 */					/*SurgeryEventStatus surgeryEventStatus = new SurgeryEventStatus();
					surgeryEventStatus.setIntraopSurgeryEvents(surgeryEvent);

					if (eventLog.getIntraopEventsmaster().getEventId() == surgeryEvent.getIntraopEventsmaster()
					        .getEventId())
					{
						surgeryEventStatus.setEventPerformed(true);
					}
					else
					{
						surgeryEventStatus.setEventPerformed(false);

					}
					surgeryEventsListWithStatus.add(surgeryEventStatus);*/
				}


				SurgeryEventStatus surgeryEventStatus = new SurgeryEventStatus();
				surgeryEventStatus.setIntraopSurgeryEvents(surgeryEvent);
				surgeryEventStatus.setEventPerformed(presentInEventLog);
				surgeryEventsListWithStatus.add(surgeryEventStatus);
			}

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

		// TODO Auto-generated method stub

		if (surgeryEventsListWithStatus != null)
		{

			for (SurgeryEventStatus obj : surgeryEventsListWithStatus)
			{
				if (obj != null)
				{
					IntraopSurgeryevents intraopSurgeryeventsObj = new IntraopSurgeryevents();
					intraopSurgeryeventsObj = obj.getIntraopSurgeryEvents();

					if (intraopSurgeryeventsObj != null)
					{
						IntraopEventsmaster intraopEventsmasterObj = new IntraopEventsmaster();
						intraopEventsmasterObj = intraopSurgeryeventsObj.getIntraopEventsmaster();

						if (intraopEventsmasterObj != null)
						{
							System.out.println("Event ID ::::: " + intraopEventsmasterObj.getEventId() + " || "
									+ "Event name :::: " + intraopEventsmasterObj.getEventName());

						}
					}
				}
			}
		}

		return surgeryEventsListWithStatus;
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.infosys.common.dao.EventLogDao#createEventLog(com.infosys.common.
	 * GeneratedEntities.IntraopEventlog, java.lang.String)
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	@Override
	public IntraopEventlog createEventLog(IntraopEventlog intraopEventLog, String userName) throws Exception
	{
		LOGGER.debug("Logger Name: " + LOGGER.getName());
		IntraopEventlog foundEventLog = new IntraopEventlog();
		String customEventName =null;
		String status = "Closed";
		Session session = null;
		try {

			// opens a new session from the session factory
			SessionFactory sessionFactory = HibernateUtilities.createSessionFactory();

			// For logging audit record
			AuditLogInterceptor interceptor = new AuditLogInterceptor();
			session = sessionFactory.withOptions().interceptor(interceptor).openSession();
			interceptor.setSession(session);
			interceptor.setUserName(userName);
			session.getTransaction().begin();

			// for setting created time and created by in case of new record and updated time and updated by in case of exsisting record

			if(intraopEventLog.getEventLogId()!=null)
			{
				Date updatedTime= new Date();
				intraopEventLog.setUpdatedBy(userName);
				intraopEventLog.setUpdatedTime(updatedTime);
			}
			else
			{
				Date createdTime= new Date();
				intraopEventLog.setCreatedBy(userName);
				intraopEventLog.setCreatedTime(createdTime);
			}
			/// changes from sahil.sharma start here
			/*if(intraopEventLog.getIntraopEventsmaster()!=null)
				/// changes from sahil.sharma end here
			{
				// ---get all rows from intraop_eventlog
				caseId = patientcase.getCaseId();
				String query = "FROM IntraopEventlog WHERE patientcase.caseId=?";
				Query queryExecute = session.createQuery(query);
				queryExecute.setParameter(0, caseId);

				List<IntraopEventlog> objList = queryExecute.list();
				if (!objList.isEmpty())
				{
					for (IntraopEventlog obj : objList)
					{
						/// changes from sahil.sharma start here
						if(obj.getIntraopEventsmaster()!=null && intraopEventLog.getIntraopEventsmaster()!=null)
						{
							/// changes from sahil.sharma end here
							if (obj.getIntraopEventsmaster().getEventName()
									.equalsIgnoreCase(intraopEventLog.getIntraopEventsmaster().getEventName()))

							{
								intraopEventLog.setEventLogId(obj.getEventLogId());
								break;
							}
						}

						// changes from sahil.sharma start here
					if (obj.getEvent().equalsIgnoreCase(intraopEventLog.getEvent()))
					{
						intraopEventLog.setEventLogId(obj.getEventLogId());
					}
					// changes from sahil.sharma end here

					}
				}
			}*/


			customEventName = intraopEventLog.getCustomEventName();

			if(customEventName.equals("Shift Out")) {

				Patientcase patientcase2=intraopEventLog.getPatientcase();
				patientcase2.setStatus(status);
				session.saveOrUpdate(patientcase2);

			}


			session.saveOrUpdate(intraopEventLog);

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
		return intraopEventLog;

	}


	/*
	 * (non-Javadoc)
	 *
	 * @see com.infosys.common.dao.EventLogDao#getAllEvents(int, java.lang.Long,
	 * java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<EventLogEventStatus> getAllEvents(int patientID, Long caseID, String surgeryName, String userName)
			throws Exception
	{

		LOGGER.debug("Logger Name: " + LOGGER.getName());
		List<EventLogEventStatus> eventLogListWithStatus = new ArrayList<EventLogEventStatus>();
		Session session = null;

		try {

			// opens a new session from the session factory
			SessionFactory sessionFactory = HibernateUtilities.createSessionFactory();

			session = sessionFactory.openSession();
			session.getTransaction().begin();

			// Get event log
			Query query = session
					.createQuery(
							"FROM IntraopEventlog iel "
									+ "inner join IntraopEventsmaster iem on iem.eventId=iel.intraopEventsmaster.eventId "
									+ " where iel.patient.patientId=? and iel.patientcase.caseId=?");
			query.setParameter(0, patientID);
			query.setParameter(1, caseID);
			List<Object> eventLogObjectList = query.list();

			// Get Surgery Events
			Query querySurgeryEvents = session.createQuery("FROM IntraopSurgeryevents ise "
					+ "inner join IntraopEventsmaster iem on iem.eventId=ise.intraopEventsmaster.eventId"
					+ " where ise.surgeryName=? order by ise.eventOrder");
			querySurgeryEvents.setParameter(0, surgeryName);
			List<Object> surgeryEventsObjectList = querySurgeryEvents.list();

			for (Iterator<Object> surgeryEventsIterator = surgeryEventsObjectList.iterator(); surgeryEventsIterator
					.hasNext();)
			{
				Object[] surgeryEventsObjectArray = (Object[]) surgeryEventsIterator.next();
				IntraopSurgeryevents surgeryEvent = (IntraopSurgeryevents) surgeryEventsObjectArray[0];
				Boolean eventOccured=false;

				IntraopEventlog eventLogObj=new IntraopEventlog();
				for (Iterator<Object> eventLogIterator = eventLogObjectList.iterator(); eventLogIterator.hasNext();)
				{
					Object[] eventLogObjectArray = (Object[]) eventLogIterator.next();
					IntraopEventlog eventLog = (IntraopEventlog) eventLogObjectArray[0];
					eventLogObj=eventLog;

					if (eventLog.getIntraopEventsmaster().getEventId() == surgeryEvent.getIntraopEventsmaster().getEventId())
					{
						/*EventLogEventStatus eventLogStatus = new EventLogEventStatus();
						eventLogStatus.setEventPerformed(true);
						eventLogStatus.setIntraopEventLog(eventLog);
						//eventLogListWithStatus.add(eventLogStatus);
						 */
						eventOccured=true;
						break;
					}
					else
					{
						eventOccured=false;
						/*IntraopEventlog notPerformedEvent = new IntraopEventlog();
						notPerformedEvent.setIntraopEventsmaster(surgeryEvent.getIntraopEventsmaster());
						EventLogEventStatus eventLogStatus = new EventLogEventStatus();
						eventLogStatus.setEventPerformed(false);
						eventLogStatus.setIntraopEventLog(notPerformedEvent);*/

					}


					/*/// changes by sahil.sharma08 start here
					if(Validations.isDigitWithoutDot(eventLog.getEvent()))
					{
						if (new Integer(eventLog.getEvent()) == surgeryEvent.getIntraopEventsmaster().getEventId())
						{
							eventOccured=true;
							break;
						}
						else
						{
							eventOccured=false;
						}
					}
					else if (eventLog.getEvent().equalsIgnoreCase(surgeryEvent.getIntraopEventsmaster().getEventName()))
					{
						eventOccured=true;
						break;
					}
					else
					{
						eventOccured=false;
					}
					/// changes by sahil.sharma08 end here
					 */				}

				EventLogEventStatus eventLogStatus = new EventLogEventStatus();

				eventLogStatus.setEventPerformed(eventOccured);

				if(eventOccured)
				{
					eventLogStatus.setEventLog(eventLogObj);
				}
				else
				{
					IntraopEventlog eventlog=new IntraopEventlog();
					eventlog.setComments(null);
					eventlog.setCreatedBy(null);
					eventlog.setCreatedTime(null);
					eventlog.setEventLogId(null);
					eventlog.setEventTime(null);
					eventlog.setIntraopEventsmaster(null);
					// changes by sahil.sharma start here
					/*eventlog.setEvent(null);*/
					// changes by sahil.sharma end here
					eventLogStatus.setEventLog(eventlog);
				}

				eventLogStatus.setsurgeryEvent(surgeryEvent);
				eventLogListWithStatus.add(eventLogStatus);

			}

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

		return eventLogListWithStatus;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.infosys.common.dao.EventLogDao#deleteEventLog(int,
	 * java.lang.String)
	 */
	@Override
	public String deleteEventLog(int eventLogID, String userName) throws Exception
	{
		LOGGER.debug("Logger Name: " + LOGGER.getName());
		String result = "";
		Session session = null;
		;
		try {

			// opens a new session from the session factory
			SessionFactory sessionFactory = HibernateUtilities.createSessionFactory();

			// For logging audit record
			AuditLogInterceptor interceptor = new AuditLogInterceptor();
			session = sessionFactory.withOptions().interceptor(interceptor).openSession();
			interceptor.setSession(session);
			interceptor.setUserName(userName);
			session.getTransaction().begin();

			IntraopEventlog fromDB=(IntraopEventlog)session.get(IntraopEventlog.class,eventLogID);



			if (fromDB == null)
			{
				result = "Event Log ID does not exist";
			}
			else
			{


				session.delete(fromDB);
				result = "Event Log Deleted";
			}

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
	 * This method returns the IntraopEventLog record corresponding to the eventlogID provided
	 *
	 * @param eventLogId
	 *            - the ID of the corresponding IntraopEventLog record
	 * @param userName
	 *            - the logged in user
	 * @return IntraopEventLog record- IntraopEventLog record corresponding to the eventlogID provided
	 * @throws Exception
	 */
	@Override
	public IntraopEventlog getEventLogRecord(int eventLogId, String userName) throws Exception
	{
		LOGGER.debug("Logger Name: " + LOGGER.getName());
		IntraopEventlog eventLogRecord = new IntraopEventlog();
		Session session = null;

		try
		{

			// opens a new session from the session factory
			SessionFactory sessionFactory = HibernateUtilities.createSessionFactory();

			session = sessionFactory.openSession();
			session.getTransaction().begin();

			// Get event log
			eventLogRecord = (IntraopEventlog) session.get(IntraopEventlog.class, eventLogId);

			session.getTransaction().commit(); // commiting the session
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

		return eventLogRecord;
	}

	/**
	 * Class method to set snapshot file name corresponding to event log.
	 *
	 * @param eventLogID
	 *            the event log ID which needs to be altered
	 * @param snapshotFileName
	 *            the snapshotFileName which needs to be saved
	 * @param userName
	 *            the logged in user
	 * @return the string for success/fail
	 * @throws Exception
	 *             the exception
	 */
	@Override
	public String setSnapshotFileName(Integer eventLogID, String snapshotFileName, String userName) throws Exception
	{
		LOGGER.debug("Logger Name: " + LOGGER.getName());
		String result="";
		Session session = null;
		IntraopEventlog eventLogRecord = new IntraopEventlog();
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


			/// getting the event log record from DB
			eventLogRecord=(IntraopEventlog)session.get(IntraopEventlog.class, eventLogID);

			/// setting the snpshot file name
			eventLogRecord.setSnapshotFileName(snapshotFileName);

			/// saving/updating the snapshot file name
			session.saveOrUpdate(eventLogRecord);

			result="Succesfully added the Snapshot file name";
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
	 * Class method to get snapshot file name corresponding to event log.
	 *
	 * @param eventLogID
	 *            the event log ID which needs to be altered
	 * @param userName
	 *            the logged in user
	 * @return the string for Snapshot FIle Name
	 * @throws Exception
	 *             the exception
	 */

	@Override
	public String getSnapshotFileName(Integer eventLogID, String userName) throws Exception
	{
		LOGGER.debug("Logger Name: " + LOGGER.getName());
		String snapshotFileName="";
		Session session = null;
		IntraopEventlog eventLogRecord = new IntraopEventlog();

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

			/// getting the event log record from DB
			eventLogRecord=(IntraopEventlog)session.get(IntraopEventlog.class, eventLogID);

			snapshotFileName=eventLogRecord.getSnapshotFileName();
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
		return snapshotFileName;
	}

	/**
	 * Class method to get list of intraop event log correspondin to case id , patient id and surgery name.
	 *
	 * @param surgeryName
	 *            the surgery name for which the event list is required
	 * @param patientID
	 *            the patientID
	 * @param patientCaseID
	 *            patient case id
	 * @param userName
	 *            the logged in user
	 * @throws Exception
	 *             the exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<IntraopEventlog> getIntraopEventLogList(int patientID, Long caseID, String surgeryName, String userName)
			throws Exception
	{
		LOGGER.debug("Logger Name: " + LOGGER.getName());
		Session session = null;
		List<IntraopEventlog> eventLogRecordList = new ArrayList<IntraopEventlog>();

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

			// retrieving the list of records corresponding to patient id, case id, and surgery name
			Query query = session.createQuery("from IntraopEventlog where patient.patientId=? and patientcase.caseId=? and surgeryType=? order by eventTime asc");
			query.setParameter(0, patientID);
			query.setParameter(1, caseID);
			query.setParameter(2, surgeryName);

			eventLogRecordList=query.list();

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
		return eventLogRecordList;

	}


}
