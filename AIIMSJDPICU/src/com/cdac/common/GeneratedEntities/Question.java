/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package com.cdac.common.GeneratedEntities;
// Generated Sep 12, 2017 10:07:52 AM by Hibernate Tools 4.3.1.Final

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

// TODO: Auto-generated Javadoc
/**
 * Question generated by hbm2java.
 */
@Entity
@Table(name = "question", catalog = "intraop_dev")
public class Question implements java.io.Serializable
{

	/** The question id. */
	private Integer questionId;
	
	/** The question text. */
	private String questionText;
	
	/** The caption. */
	private String caption;
	
	/** The is reportable. */
	private Boolean isReportable;
	
	/** The is concordable. */
	private Boolean isConcordable;
	
	/** The order type. */
	private Integer orderType;
	
	/** The created on. */
	private Date createdOn;
	
	/** The created by. */
	private String createdBy;
	
	/** The updated on. */
	private Date updatedOn;
	
	/** The updated by. */
	private String updatedBy;
	
	/** The parent question id. */
	private Integer parentQuestionId;
	
	/** The category. */
	private String category;
	
	/** The department. */
	private String department;
	
	/** The answers. */
	private Set<Answer> answers = new HashSet<Answer>(0);

	/**
	 * Instantiates a new question.
	 */
	public Question()
	{
	}

	/**
	 * Instantiates a new question.
	 *
	 * @param questionText the question text
	 */
	public Question(String questionText)
	{
		this.questionText = questionText;
	}

	/**
	 * Instantiates a new question.
	 *
	 * @param questionText the question text
	 * @param caption the caption
	 * @param isReportable the is reportable
	 * @param isConcordable the is concordable
	 * @param orderType the order type
	 * @param createdOn the created on
	 * @param createdBy the created by
	 * @param updatedOn the updated on
	 * @param updatedBy the updated by
	 * @param parentQuestionId the parent question id
	 * @param category the category
	 * @param department the department
	 * @param answers the answers
	 */
	public Question(String questionText, String caption, Boolean isReportable, Boolean isConcordable, Integer orderType,
	        Date createdOn, String createdBy, Date updatedOn, String updatedBy, Integer parentQuestionId,
	        String category, String department, Set<Answer> answers)
	{
		this.questionText = questionText;
		this.caption = caption;
		this.isReportable = isReportable;
		this.isConcordable = isConcordable;
		this.orderType = orderType;
		this.createdOn = createdOn;
		this.createdBy = createdBy;
		this.updatedOn = updatedOn;
		this.updatedBy = updatedBy;
		this.parentQuestionId = parentQuestionId;
		this.category = category;
		this.department = department;
		this.answers = answers;
	}

	/**
	 * Gets the question id.
	 *
	 * @return the question id
	 */
	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "QuestionID", unique = true, nullable = false)
	public Integer getQuestionId()
	{
		return this.questionId;
	}

	/**
	 * Sets the question id.
	 *
	 * @param questionId the new question id
	 */
	public void setQuestionId(Integer questionId)
	{
		this.questionId = questionId;
	}

	/**
	 * Gets the question text.
	 *
	 * @return the question text
	 */
	@Column(name = "QuestionText", nullable = false, length = 500)
	public String getQuestionText()
	{
		return this.questionText;
	}

	/**
	 * Sets the question text.
	 *
	 * @param questionText the new question text
	 */
	public void setQuestionText(String questionText)
	{
		this.questionText = questionText;
	}

	/**
	 * Gets the caption.
	 *
	 * @return the caption
	 */
	@Column(name = "Caption", length = 45)
	public String getCaption()
	{
		return this.caption;
	}

	/**
	 * Sets the caption.
	 *
	 * @param caption the new caption
	 */
	public void setCaption(String caption)
	{
		this.caption = caption;
	}

	/**
	 * Gets the checks if is reportable.
	 *
	 * @return the checks if is reportable
	 */
	@Column(name = "IsReportable")
	public Boolean getIsReportable()
	{
		return this.isReportable;
	}

	/**
	 * Sets the checks if is reportable.
	 *
	 * @param isReportable the new checks if is reportable
	 */
	public void setIsReportable(Boolean isReportable)
	{
		this.isReportable = isReportable;
	}

	/**
	 * Gets the checks if is concordable.
	 *
	 * @return the checks if is concordable
	 */
	@Column(name = "IsConcordable")
	public Boolean getIsConcordable()
	{
		return this.isConcordable;
	}

	/**
	 * Sets the checks if is concordable.
	 *
	 * @param isConcordable the new checks if is concordable
	 */
	public void setIsConcordable(Boolean isConcordable)
	{
		this.isConcordable = isConcordable;
	}

	/**
	 * Gets the order type.
	 *
	 * @return the order type
	 */
	@Column(name = "OrderType")
	public Integer getOrderType()
	{
		return this.orderType;
	}

	/**
	 * Sets the order type.
	 *
	 * @param orderType the new order type
	 */
	public void setOrderType(Integer orderType)
	{
		this.orderType = orderType;
	}

	/**
	 * Gets the created on.
	 *
	 * @return the created on
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CreatedOn", length = 19)
	public Date getCreatedOn()
	{
		return this.createdOn;
	}

	/**
	 * Sets the created on.
	 *
	 * @param createdOn the new created on
	 */
	public void setCreatedOn(Date createdOn)
	{
		this.createdOn = createdOn;
	}

	/**
	 * Gets the created by.
	 *
	 * @return the created by
	 */
	@Column(name = "CreatedBy", length = 45)
	public String getCreatedBy()
	{
		return this.createdBy;
	}

	/**
	 * Sets the created by.
	 *
	 * @param createdBy the new created by
	 */
	public void setCreatedBy(String createdBy)
	{
		this.createdBy = createdBy;
	}

	/**
	 * Gets the updated on.
	 *
	 * @return the updated on
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UpdatedOn", length = 19)
	public Date getUpdatedOn()
	{
		return this.updatedOn;
	}

	/**
	 * Sets the updated on.
	 *
	 * @param updatedOn the new updated on
	 */
	public void setUpdatedOn(Date updatedOn)
	{
		this.updatedOn = updatedOn;
	}

	/**
	 * Gets the updated by.
	 *
	 * @return the updated by
	 */
	@Column(name = "UpdatedBy", length = 45)
	public String getUpdatedBy()
	{
		return this.updatedBy;
	}

	/**
	 * Sets the updated by.
	 *
	 * @param updatedBy the new updated by
	 */
	public void setUpdatedBy(String updatedBy)
	{
		this.updatedBy = updatedBy;
	}

	/**
	 * Gets the parent question id.
	 *
	 * @return the parent question id
	 */
	@Column(name = "ParentQuestionID")
	public Integer getParentQuestionId()
	{
		return this.parentQuestionId;
	}

	/**
	 * Sets the parent question id.
	 *
	 * @param parentQuestionId the new parent question id
	 */
	public void setParentQuestionId(Integer parentQuestionId)
	{
		this.parentQuestionId = parentQuestionId;
	}

	/**
	 * Gets the category.
	 *
	 * @return the category
	 */
	@Column(name = "Category", length = 45)
	public String getCategory()
	{
		return this.category;
	}

	/**
	 * Sets the category.
	 *
	 * @param category the new category
	 */
	public void setCategory(String category)
	{
		this.category = category;
	}

	/**
	 * Gets the department.
	 *
	 * @return the department
	 */
	@Column(name = "Department", length = 45)
	public String getDepartment()
	{
		return this.department;
	}

	/**
	 * Sets the department.
	 *
	 * @param department the new department
	 */
	public void setDepartment(String department)
	{
		this.department = department;
	}

	/**
	 * Gets the answers.
	 *
	 * @return the answers
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "question")
	public Set<Answer> getAnswers()
	{
		return this.answers;
	}

	/**
	 * Sets the answers.
	 *
	 * @param answers the new answers
	 */
	public void setAnswers(Set<Answer> answers)
	{
		this.answers = answers;
	}

}
