/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package com.cdac.common.GeneratedEntities;

import java.util.Date;

public class EventLogEventStatus {

	private Boolean eventPerformed;
	private IntraopSurgeryevents surgeryEvent;
	private IntraopEventlog eventLog;
	public IntraopEventlog getEventLog() {
		return eventLog;
	}

	public void setEventLog(IntraopEventlog eventLog) {
		this.eventLog = eventLog;
	}

	private String comments;
	private Integer eventLogId;
	private Date createdTime;

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Integer getEventLogId() {
		return eventLogId;
	}

	public void setEventLogId(Integer eventLogId) {
		this.eventLogId = eventLogId;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Date getEventTime() {
		return eventTime;
	}

	public void setEventTime(Date eventTime) {
		this.eventTime = eventTime;
	}

	private Date eventTime;

	public Boolean getEventPerformed() {
		return eventPerformed;
	}

	public void setEventPerformed(Boolean eventPerformed) {
		this.eventPerformed = eventPerformed;
	}

	public IntraopSurgeryevents getIntraopEventLog() {
		return surgeryEvent;
	}

	public void setsurgeryEvent(IntraopSurgeryevents intraopEventLog) {
		this.surgeryEvent = intraopEventLog;
	}
}
