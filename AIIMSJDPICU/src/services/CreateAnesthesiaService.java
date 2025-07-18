/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package services;

import java.util.List;

import com.cdac.common.GeneratedEntities.IntraopAnesthesiamedicationrecord;
import com.cdac.common.GeneratedEntities.IntraopAnesthesiarecord;
import com.cdac.common.util.DAOFactory;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import model.UserSession;

public class CreateAnesthesiaService extends Service<Void>
{
	private static IntraopAnesthesiarecord anesthesiaRecordStatic;
	private IntraopAnesthesiarecord createdAnesthesiaRecord;
	private static List<IntraopAnesthesiamedicationrecord> medicationListStatic;
	
	private static CreateAnesthesiaService addFluidService = null;
	private CreateAnesthesiaService() {
		super();
	}

	public static CreateAnesthesiaService getInstance(IntraopAnesthesiarecord anesthesiaRecord,
			List<IntraopAnesthesiamedicationrecord> medicationList)
	{
		if(addFluidService == null)
		{
			addFluidService = new CreateAnesthesiaService();
		}
		anesthesiaRecordStatic = anesthesiaRecord;
		medicationListStatic = medicationList;
		return addFluidService;
	}

	public IntraopAnesthesiarecord getCreatedAnesthesiaRecord()
	{
		return createdAnesthesiaRecord;
	}

	@Override
	protected Task<Void> createTask()
	{
		return new Task<Void>()
		{

			@Override
			protected Void call() throws Exception
			{
				try
				{
					createdAnesthesiaRecord = DAOFactory.anesthesiaDetails().createAnesthesiaRecord(anesthesiaRecordStatic,medicationListStatic,
					        UserSession.getInstance().getUserWithrRolesForUserAuthentication().getUserID());
				}
				catch (Exception e) 
				{
					throw e;
				}

				return null;
			}

		};
	}
}
