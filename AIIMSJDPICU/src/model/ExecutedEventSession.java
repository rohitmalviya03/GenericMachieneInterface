/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package model;

import java.util.ArrayList;
import java.util.List;

import com.cdac.common.GeneratedEntities.IntraopEventlog;

public class ExecutedEventSession {
	private final static ExecutedEventSession instance = new ExecutedEventSession();

	public static ExecutedEventSession getInstance() {
		return instance;
	}

	public List<IntraopEventlog> getExecutedEventList() {
		return executedEventList;
	}

	public void setExecutedEventList(List<IntraopEventlog> executedEventList) {
		this.executedEventList = executedEventList;
	}

	private List<IntraopEventlog> executedEventList= new ArrayList<IntraopEventlog>() ;

}

