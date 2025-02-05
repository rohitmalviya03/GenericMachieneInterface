/*******************************************************************************
 * © 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package model;

import java.util.Date;
import java.util.List;

public class EventLogSession {

	private final static EventLogSession instance = new EventLogSession();

	public static EventLogSession getInstance() {
		return instance;
	}

	public Date getEventTime()
	{
		return eventTime;
	}

	public void setEventTime(Date eventTime)
	{
		this.eventTime = eventTime;
	}

	private EventLogModel eventLog;
	private Date eventTime;
	private List<EventLogModel> eventList;

	public List<EventLogModel> getEventList() {
		return eventList;
	}

	public void setEventList(List<EventLogModel> eventList) {
		this.eventList = eventList;
	}

	public EventLogModel getEventLog() {
		return eventLog;
	}

	public void setEventLog(EventLogModel eventLog) {
		this.eventLog = eventLog;
	}

}
