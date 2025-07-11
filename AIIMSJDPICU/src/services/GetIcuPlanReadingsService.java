/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package services;

import com.cdac.common.GeneratedEntities.IcuPlanEntity;
import com.cdac.common.util.DAOFactory;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import model.UserSession;

/**
 * @author rohi.bhardwaj
 *
 */
public class GetIcuPlanReadingsService extends Service<Void> {

	private IcuPlanEntity icuPlanRecord;
	private static long caseId;

	public IcuPlanEntity getIcuPlanRecord() {
		return icuPlanRecord;
	}

	private static GetIcuPlanReadingsService instance = null;

	public static GetIcuPlanReadingsService getInstance(long caseIdVal) {
		if (instance == null) {
			instance = new GetIcuPlanReadingsService();
		}
		caseId = caseIdVal;
		return instance;
	}

	private GetIcuPlanReadingsService() {
		super();
	}

	@Override
	protected Task<Void> createTask() {

		return new Task<Void>() {

			@Override
			protected Void call() throws Exception {

				try {
					icuPlanRecord = DAOFactory.icuPlanReadings().getIcuPlanDetails(caseId,
							UserSession.getInstance().getUserWithrRolesForUserAuthentication().getUserID());
				} catch (Exception e) {
					throw e;
				}

				return null;
			}

		};

	}

}
