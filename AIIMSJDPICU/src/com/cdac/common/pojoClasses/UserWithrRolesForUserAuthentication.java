/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package com.cdac.common.pojoClasses;

public class UserWithrRolesForUserAuthentication 
{

	private String userID;
	private String userName;
	private String designation;
	
	
	
	
	public UserWithrRolesForUserAuthentication(String userName, String designation)
	{
		super();
		this.userName = userName;
		this.designation = designation;
	}
	public UserWithrRolesForUserAuthentication(String userID, String userName, String designation) {
		super();
		this.userID = userID;
		this.userName = userName;
		this.designation = designation;
	}
	public UserWithrRolesForUserAuthentication()
	{
		
	}
	
	
	public String getUserName() {
		return userName;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getDesignation() {
		return designation;
	}
	public void setDesignation(String designation) {
		this.designation = designation;
	}
	
	
	

}
