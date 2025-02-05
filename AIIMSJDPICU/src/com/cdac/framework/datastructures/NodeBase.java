/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package com.cdac.framework.datastructures;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.cdac.framework.exceptions.KeyExistsException;

/**
 * Use this class to build a tree like structure.<br>
 * <b><u>Note:</u></b> This data structure currently does not support the remove
 * node functionality. The results of such an operation are undefined.
 *
 * @author Sahil_Jain08
 *
 * @param <T>
 */
public class NodeBase<T> implements Serializable
{
	NodeBase<T> parent;
	HashMap<String, NodeBase<T>> children;
	private T dataForThisNode;
	private String name;
	private String qualifiedName;
	private Set<IDataObserver> observers;
	private boolean dataUpdated;

	public NodeBase(String uniqueNameofThisNode, NodeBase<T> parent)
	{
		this.name = uniqueNameofThisNode;
		children = new HashMap<String, NodeBase<T>>();
		observers = new HashSet<IDataObserver>();
		setParent(parent);
	}

	/**
	 * Sends out the notification to the following:<br>
	 * 1. Parent<br>
	 * 2. External Listeners.<br>
	 *
	 * @param nodeWhoseValueIsChanged
	 *            is the node whose change in value finally lead to event
	 *            firing.
	 */
	protected void informListeners(NodeBase nodeWhoseValueIsChanged)
	{
		// Inform parent about the change
		if (parent != null)
		{
			parent.informListeners(nodeWhoseValueIsChanged);
		}

		// Inform external listeners.
		notifyObservers(nodeWhoseValueIsChanged);
	}

	/**
	 * Fire event and notify observers according to the notification rule
	 * preference of each listener.
	 *
	 * XXX: Logic may be required to switch the events to UI thread while event
	 * dispatch.
	 *
	 * @param nodeWhoseValueIsChanged
	 */
	private synchronized void notifyObservers(NodeBase nodeWhoseValueIsChanged)
	{
		for (IDataObserver observer : observers)
		{
			// System.out.println(observer + "----obs");
			switch(observer.getNotificationRule())
			{

			case SELECTED_NODES:
				boolean notifyThisObserver = true;
				for (NodeBase node : observer.getNodesUnderObservation())
				{
					if (!node.isModified())
					{
						notifyThisObserver = false;
						break;
					}
				}

				if (notifyThisObserver)
				{
					observer.update(this, nodeWhoseValueIsChanged);
					// Reset the modify status of all the parameters for this
					// rule.
					for (NodeBase node : observer.getNodesUnderObservation())
					{
						node.resetModifiedFlag();
					}
				}
				break;

			case EVERY_NODE:
				observer.update(this, nodeWhoseValueIsChanged);
				nodeWhoseValueIsChanged.resetModifiedFlag();
				break;

			default:
				// Do Nothing
			}
		}

	}

	public synchronized void addObserver(IDataObserver o)
	{
		observers.add(o);
	}

	/**
	 * Removes an observer.
	 *
	 * @param o
	 *            observer to be removed
	 * @return true if the observer was registered for this node.
	 */
	public synchronized boolean removeObserver(IDataObserver o)
	{
		return observers.remove(o);
	}

	protected NodeBase<T> addNewChild(String key, NodeBase<T> child) throws KeyExistsException
	{
		if (children.containsKey(key))
		{
			throw new KeyExistsException(key, getName());
		}
		return (NodeBase<T>) children.put(key, child);
	}

	protected void setData(T data)
	{
		this.dataForThisNode = data;
		this.dataUpdated = true;
		informListeners(this);
	}
	
	/**
	 * This method is for the sole purpose of resetting the saved parameter for use in 
	 * next duration
	 * @param data
	 */
	public void resetSavedData(T data)
	{
		this.dataForThisNode = data;
	}

	public boolean isModified()
	{
		return dataUpdated;
	}

	/**
	 * Resets the modified flag to indicate that the latest update in the value
	 * has been used.<br>
	 * XXX: There is a flaw in the present approach which will fail if there are
	 * multiple shared listeners.
	 */
	public void resetModifiedFlag()
	{
		dataUpdated = false;
	}

	public T getData()
	{
		return dataForThisNode;
	}

	public String getName()
	{
		return name;
	}

	/**
	 * Sets a parent of this node. Parenting should only be done once either
	 * during the construction of the node or later if not done during
	 * construction. Re-parenting is currently <b><u>NOT SUPPORTED</b></u> and
	 * runs a risk of losing a complete branch.
	 *
	 * @param parentNode
	 */
	void setParent(NodeBase<T> parentNode)
	{
		parent = parentNode;
		computeQualifiedName();
	}

	public Collection<NodeBase<T>> getChildren()
	{
		return children.values();
	}

	public NodeBase getChild(String identifier)
	{
		return children.get(identifier);
	}

	@Override
	public boolean equals(Object obj)
	{
		return getQualifiedName().equals(((NodeBase) obj).getQualifiedName());
	}

	@Override
	public int hashCode()
	{
		return getQualifiedName().hashCode();
	}

	/**
	 * Returns a qualified name of this node.
	 *
	 * @return qualified name.
	 */
	public String getQualifiedName()
	{
		return this.qualifiedName;
	}

	/**
	 * Computes a qualified name of this node in a tree hierarchy.<br>
	 * This method is called whenever the node is constructed or a parent is set
	 * to compute a qualified name after which need for re-computation should
	 * not occur as re-parenting is <b><u>not supported.</u></b> Example:
	 * Root.Parent.Child
	 *
	 * @return qualifiedName of this node.
	 */
	public void computeQualifiedName()
	{
		StringBuilder qualifiedName = new StringBuilder();
		if (parent != null)
		{
			qualifiedName.append(parent.getQualifiedName()).append(".");
		}

		qualifiedName.append(getName());

		this.qualifiedName = qualifiedName.toString();
	}
}
