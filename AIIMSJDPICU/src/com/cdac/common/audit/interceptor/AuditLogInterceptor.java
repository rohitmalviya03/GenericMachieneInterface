/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package com.cdac.common.audit.interceptor;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.hibernate.CallbackException;
import org.hibernate.EmptyInterceptor;
import org.hibernate.Session;
import org.hibernate.type.Type;

import com.cdac.common.util.AuditLogUtil;

// TODO: Auto-generated Javadoc
/**
 * The Class AuditLogInterceptor.
 */
public class AuditLogInterceptor extends EmptyInterceptor {

	/** The user name. */
	String userName;

	/**
	 * Sets the user name.
	 *
	 * @param userName
	 *            the new user name
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/** The session. */
	Session session;

	/** The inserts. */
	private Set inserts = new HashSet();

	/** The updates. */
	private Set updates = new HashSet();

	/** The deletes. */
	private Set deletes = new HashSet();

	/**
	 * Sets the session.
	 *
	 * @param session
	 *            the new session
	 */
	public void setSession(Session session) {
		this.session = session;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hibernate.EmptyInterceptor#afterTransactionBegin(org.hibernate.
	 * Transaction)
	 */
	/**
	 * Called when a Hibernate transaction is begun via the Hibernate
	 * Transaction API.
	 */
	public void afterTransactionBegin(org.hibernate.Transaction tx) {
		//afterTransactionBegin method
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.hibernate.EmptyInterceptor#afterTransactionCompletion(org.hibernate.
	 * Transaction)
	 */
	/**
	 * Called after a transaction is committed or rolled back.
	 */
	public void afterTransactionCompletion(org.hibernate.Transaction tx) {
		//afterTransactionCompletion method
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hibernate.EmptyInterceptor#onSave(java.lang.Object,
	 * java.io.Serializable, java.lang.Object[], java.lang.String[],
	 * org.hibernate.type.Type[])
	 */
	/**
	 * Called before an object is saved.
	 */
	public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types)
			throws CallbackException {

		if (entity instanceof IAuditLog) {
			inserts.add(entity);
		}
		return false;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hibernate.EmptyInterceptor#onFlushDirty(java.lang.Object,
	 * java.io.Serializable, java.lang.Object[], java.lang.Object[],
	 * java.lang.String[], org.hibernate.type.Type[])
	 */
	/**
	 * Called when an object is detected to be dirty, during a flush.
	 */
	public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState,
			String[] propertyNames, Type[] types) throws CallbackException {

		if (entity instanceof IAuditLog) {
			updates.add(entity);
		}
		return false;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hibernate.EmptyInterceptor#onDelete(java.lang.Object,
	 * java.io.Serializable, java.lang.Object[], java.lang.String[],
	 * org.hibernate.type.Type[])
	 */
	/**
	 * Called before an object is deleted.
	 */
	public void onDelete(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {

		if (entity instanceof IAuditLog) {
			deletes.add(entity);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hibernate.EmptyInterceptor#preFlush(java.util.Iterator)
	 */
	/**
	 * Called before a flush
	 */
	public void preFlush(Iterator iterator) {
		//preFlush method
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hibernate.EmptyInterceptor#postFlush(java.util.Iterator)
	 */
	/**
	 * Called after a flush that actually ends in execution of the SQL
	 * statements required to synchronize in-memory state with the database.
	 */
	public void postFlush(Iterator iterator) {

		try {
			if (inserts != null)
			{

			for (Iterator it = inserts.iterator(); it.hasNext();) {
				IAuditLog entity = (IAuditLog) it.next();

				AuditLogUtil.logIt("Saved", userName, entity, session);
			}
			}

			if (updates != null)
			{

			for (Iterator it = updates.iterator(); it.hasNext();) {
				IAuditLog entity = (IAuditLog) it.next();
				AuditLogUtil.logIt("Updated", userName, entity, session);
			}
			}
			if (deletes != null)
			{

			for (Iterator it = deletes.iterator(); it.hasNext();) {
				IAuditLog entity = (IAuditLog) it.next();
				AuditLogUtil.logIt("Deleted",userName, entity,  session);
			}
			}

		}  finally {
			inserts.clear();
			updates.clear();
			deletes.clear();
		}
	}

}