/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package com.cdac.common.GeneratedEntities;
// Generated Sep 27, 2017 5:46:58 PM by Hibernate Tools 4.3.1.Final

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * IntraopEventlist generated by hbm2java
 */
@Entity
@Table(name = "intraop_eventlist", catalog = "intraop_dev")
public class IntraopEventlist implements java.io.Serializable
{

	private int eventListId;
	private String surgeryTypeName;
	private String eventName;
	private Integer order;
	private String icons;
	private String action;
	private String createdBy;
	private Date createdTime;
	private String updatedBy;
	private Date updatedTime;

	public IntraopEventlist()
	{
	}

	public IntraopEventlist(int eventListId)
	{
		this.eventListId = eventListId;
	}

	public IntraopEventlist(int eventListId, String surgeryTypeName, String eventName, Integer order, String icons,
	        String action, String createdBy, Date createdTime, String updatedBy, Date updatedTime)
	{
		this.eventListId = eventListId;
		this.surgeryTypeName = surgeryTypeName;
		this.eventName = eventName;
		this.order = order;
		this.icons = icons;
		this.action = action;
		this.createdBy = createdBy;
		this.createdTime = createdTime;
		this.updatedBy = updatedBy;
		this.updatedTime = updatedTime;
	}

	@Id

	@Column(name = "EventListID", unique = true, nullable = false)
	public int getEventListId()
	{
		return this.eventListId;
	}

	public void setEventListId(int eventListId)
	{
		this.eventListId = eventListId;
	}

	@Column(name = "SurgeryTypeName", length = 45)
	public String getSurgeryTypeName()
	{
		return this.surgeryTypeName;
	}

	public void setSurgeryTypeName(String surgeryTypeName)
	{
		this.surgeryTypeName = surgeryTypeName;
	}

	@Column(name = "EventName", length = 45)
	public String getEventName()
	{
		return this.eventName;
	}

	public void setEventName(String eventName)
	{
		this.eventName = eventName;
	}

	@Column(name = "Order")
	public Integer getOrder()
	{
		return this.order;
	}

	public void setOrder(Integer order)
	{
		this.order = order;
	}

	@Column(name = "Icons", length = 45)
	public String getIcons()
	{
		return this.icons;
	}

	public void setIcons(String icons)
	{
		this.icons = icons;
	}

	@Column(name = "Action", length = 45)
	public String getAction()
	{
		return this.action;
	}

	public void setAction(String action)
	{
		this.action = action;
	}

	@Column(name = "CreatedBy", length = 45)
	public String getCreatedBy()
	{
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy)
	{
		this.createdBy = createdBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CreatedTime", length = 19)
	public Date getCreatedTime()
	{
		return this.createdTime;
	}

	public void setCreatedTime(Date createdTime)
	{
		this.createdTime = createdTime;
	}

	@Column(name = "UpdatedBy", length = 45)
	public String getUpdatedBy()
	{
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy)
	{
		this.updatedBy = updatedBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UpdatedTime", length = 19)
	public Date getUpdatedTime()
	{
		return this.updatedTime;
	}

	public void setUpdatedTime(Date updatedTime)
	{
		this.updatedTime = updatedTime;
	}

}
