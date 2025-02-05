/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package com.cdac.framework.exceptions;

public class KeyExistsException extends Exception
{
	String key;
	String parent;

	public KeyExistsException(String conflictingKey, String parentName)
	{
		this.key = conflictingKey;
		parent = parentName;
	}

	@Override
	public String getMessage()
	{
		return "An object with key: " + this.key + " already points to the parent:" + parent;
	}
}
