/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package com.cdac.framework.exceptions;

import java.io.IOException;
import java.sql.SQLException;

import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;

import org.hibernate.AnnotationException;
import org.hibernate.HibernateException;
import org.hibernate.LazyInitializationException;
import org.hibernate.NonUniqueObjectException;
import org.hibernate.PersistentObjectException;
import org.hibernate.PropertyValueException;
import org.hibernate.StaleStateException;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.DataException;
import org.hibernate.exception.GenericJDBCException;
import org.hibernate.exception.SQLGrammarException;
import org.hibernate.hql.internal.ast.QuerySyntaxException;
import org.hibernate.service.spi.ServiceException;

import com.mysql.jdbc.exceptions.MySQLSyntaxErrorException;

@SuppressWarnings("serial")
public class ServiceExceptionWrapper extends Exception
{
	Exception exceptionClass = null;
	public ServiceExceptionWrapper(Exception e)
	{
		exceptionClass = e;		
	}

	@Override
	public String getMessage()
	{
		if(exceptionClass instanceof PropertyValueException || exceptionClass instanceof NullPointerException)
		{
			return "Please check that you have entered all the mandatory fields";
		}
		else if(exceptionClass instanceof ArithmeticException)
		{
			return "Please check that you have entered all field values correctly";
		}
		else if(exceptionClass instanceof NumberFormatException)
		{
			return "Values you are trying to add is of wrong format";
		}
		else if(exceptionClass instanceof IOException)
		{
			return "Please check your configuration settings";
		}

		/// Hibernate and SQL exceptions
		
		else if(exceptionClass instanceof ServiceException)
		{
			return "Please check your Database connection settings";
		}
		else if(exceptionClass instanceof StaleStateException)
		{
			return "Record you are trying of access has been deleted ";
		}
		else if(exceptionClass instanceof DataException)
		{
			return "Values you are trying to add has crossed the maximum limit ";
		}
		else if(exceptionClass instanceof ConstraintViolationException || exceptionClass instanceof NonUniqueObjectException || exceptionClass instanceof QuerySyntaxException || exceptionClass instanceof HibernateException || exceptionClass instanceof LazyInitializationException)
		{
			return "Something went wrong with the application. Please contact the support team.";
		}
		
		/// finnaly when an unexpected exception occurs
		else
		{
			return "Something went wrong with the application. Please contact the support team.";
		}
		
		
		//Persistence
		/*else if(exceptionClass instanceof EntityNotFoundException)
				{
							return "Data you are trying to add is of wrong format";
						}
						else if(exceptionClass instanceof PersistenceException)
						{
							return "Something went wrong with the application. Please contact the support team.";
						}*/
		/*else if(exceptionClass instanceof PersistentObjectException)//not able to test
		{
			return "Something went wrong with the application. Please contact the support team.";
		}*/
		/*else if(exceptionClass instanceof SQLGrammarException)
				{
					return "Something went wrong with the application. Please contact the support team.";
				}*/
		/*else if(exceptionClass instanceof SQLException)
				{
					return "Something went wrong with the application. Please contact the support team.";
				}
				else if(exceptionClass instanceof MySQLSyntaxErrorException)
				{
					return "Something went wrong with the application. Please contact the support team.";
				}*/
		/*
				
				else if(exceptionClass instanceof AnnotationException)//not able to test
		{
			return "Something went wrong with the application. Please contact the support team.";
		}
		else if(exceptionClass instanceof GenericJDBCException)//not able to test
		{
			return "Something went wrong with the application. Please contact the support team.";
		}
		else if(exceptionClass instanceof IllegalArgumentException)//not able to test
		{
			return "Data you are trying to add is of wrong format";
		}
				*
				*/

		
		
		
	}
}
