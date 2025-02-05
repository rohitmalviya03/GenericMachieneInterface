/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package com.cdac.framework.datastructures;

import java.util.Collection;

import com.cdac.framework.exceptions.KeyExistsException;

public class Channel<T> extends NodeBase<T>
{

	public Channel(String uniqueNameofChannel, NodeBase<T> parent)
	{
		super(uniqueNameofChannel, parent);
	}

	/**
	 * Constructs a parameter of default type and adds to the hierarchy.
	 * 
	 * @param referralConstant
	 * @param uniqueNameOfTheParameter
	 * @return
	 * @throws KeyExistsException
	 */
	private Parameter<T> addParameter(String referralConstant, String uniqueNameOfTheParameter)
	        throws KeyExistsException
	{
		Parameter<T> param = new Parameter<T>(uniqueNameOfTheParameter, this);
		addNewChild(referralConstant, param);
		return param;
	}

	/**
	 * Parameter Name should be unique within this channel.
	 * 
	 * @param paramName
	 * @return
	 * @throws KeyExistsException
	 */
	private Parameter<T> addParameter(String paramName) throws KeyExistsException
	{
		return addParameter(paramName, paramName);
	}

	Parameter<T> createOrGetParameter(String name)
	{
		Parameter<T> parameter = (Parameter<T>) children.get(name);
		if (parameter == null)
		{
			try
			{
				parameter = addParameter(name);
			}
			catch (KeyExistsException e)
			{
				// This will not occur.
			}
		}

		return parameter;
	}

	public Collection getAllParameters()
	{
		return getChildren();
	}

	public Parameter getParameter(String parameterIdentifier)
	{
		return (Parameter) getChild(parameterIdentifier);
	}
}
