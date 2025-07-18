/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package com.cdac.common.util;

import com.cdac.common.dao.AnesthesiaDetailsDaoImpl;
import com.cdac.common.dao.AnethesiaMachineDataDaoImpl;
import com.cdac.common.dao.ApplicationSettingDaoImpl;
import com.cdac.common.dao.AuditReportDaoImpl;
import com.cdac.common.dao.CPBDetailsDaoImpl;
import com.cdac.common.dao.CannulationsDaoImpl;
import com.cdac.common.dao.CreateCaseDaoImpl;
import com.cdac.common.dao.CreateLocationDaoImpl;
import com.cdac.common.dao.CreatePatientDaoImpl;
import com.cdac.common.dao.DeleteInvestigationValueDaoImpl;
import com.cdac.common.dao.DeviceConfigurationDaoImpl;
import com.cdac.common.dao.DeviceConfigurationFetchListDaoImpl;
import com.cdac.common.dao.EchoDetailsDaoImpl;
import com.cdac.common.dao.EventLogDaoImpl;
import com.cdac.common.dao.GetDefaultUnitDaoImpl;
import com.cdac.common.dao.GetDeviceInfoDaoImpl;
import com.cdac.common.dao.GetFluidLogDaoImpl;
import com.cdac.common.dao.GetInvestigationValueViewDaoImpl;
import com.cdac.common.dao.GetMasterDataDaoImpl;
import com.cdac.common.dao.GetOutputDaoImpl;
import com.cdac.common.dao.GetPatientDaoImpl;
import com.cdac.common.dao.IcuPlanDaoImpl;
import com.cdac.common.dao.InitializationServiceDaoImpl;
import com.cdac.common.dao.InvestigationTestDaoImpl;
import com.cdac.common.dao.InvestigationTestParameterFieldsDaoImpl;
import com.cdac.common.dao.MedicationLogDaoImpl;
import com.cdac.common.dao.PatientMonitorDataDaoImpl;
import com.cdac.common.dao.PatientReportDaoImpl;
import com.cdac.common.dao.RecoveryRoomReadingDaoImpl;
import com.cdac.common.dao.SearchPatientDAOImpl;
import com.cdac.common.dao.SetInvestigationValuesDaoImpl;
import com.cdac.common.dao.SpecialityOTDaoImpl;
import com.cdac.common.dao.UpdateInvestigationValueDaoImpl;
import com.cdac.common.dao.UserAuthenticationDaoImpl;
import com.cdac.common.dao.ViewAllCasesDaoImpl;


/**
 * A factory for creating DAO objects.
 */
public class DAOFactory {

	/**
	 * Gets the creates the location implementation class instance.
	 *
	 * @return the creates the location Dao Impl
	 */
	public static CreateLocationDaoImpl getCreateLocation() {
		return CreateLocationDaoImpl.getInstance();
	}

	/**
	 * Gets the event log implementation class instance.
	 *
	 * @return the event log Dao Impl
	 */
	public static EventLogDaoImpl getEventLog() {
		return EventLogDaoImpl.getInstance();
	}

	/*
	 * public static OutputDetailsDaoImpl getOutputDetails() { return new
	 * OutputDetailsDaoImpl.getInstance(); }
	 */

	/**
	 * Gets the Search patient implementation class instance.
	 *
	 * @return the search patient DAO impl
	 */
	public static SearchPatientDAOImpl searchPatient() {
		return SearchPatientDAOImpl.getInstance();
	}

	/**
	 * Gets the Create new patient implementation class instance.
	 *
	 * @return the creates the patient dao impl
	 */
	public static CreatePatientDaoImpl createPatient() {
		return CreatePatientDaoImpl.getInstance();
	}

	/**
	 * Gets the View all cases implementation class instance.
	 *
	 * @return the view all cases dao impl
	 */
	public static ViewAllCasesDaoImpl viewAllCases()
	{
		return ViewAllCasesDaoImpl.getInstance();
	}

	/**
	 * Gets the entity details implementation class instance.
	 *
	 * @return the entity details dao impl
	 */
	public static GetMasterDataDaoImpl getMasterDataDetails()
	{
		return GetMasterDataDaoImpl.getInstance();
	}

	/**
	 * Gets the create case implementation class instance.
	 *
	 * @return the create case dao impl
	 */

	public static CreateCaseDaoImpl createCase()
	{
		return CreateCaseDaoImpl.getInstance();
	}


	/**
	 * Gets the medication log implementation class instance.
	 *
	 * @return the medication log dao impl
	 */
	public static MedicationLogDaoImpl medicationLog()
	{
		return MedicationLogDaoImpl.getInstance();
	}

	/**
	 * Gets the anesthesia details implementation class instance.
	 *
	 * @return the anesthesia details dao impl
	 */

	public static AnesthesiaDetailsDaoImpl anesthesiaDetails()
	{
		return AnesthesiaDetailsDaoImpl.getInstance();
	}

	/**
	 * Gets the user authentication implementation class instance.
	 *
	 * @return the user authentication dao impl
	 */
	public static UserAuthenticationDaoImpl authenticateUser()
	{
		return UserAuthenticationDaoImpl.getInstance();
	}
	/**
	 * Gets the device configuration implementation class instance.
	 *
	 * @return the device configuration dao impl
	 */

	public static DeviceConfigurationDaoImpl deviceConfiguration()
	{
		return DeviceConfigurationDaoImpl.getInstance();
	}

	/**
	 * Gets the device configuration fetch list implementation class instance.
	 *
	 * @return the device configuration fetch list dao impl
	 */
	public static DeviceConfigurationFetchListDaoImpl deviceConfigurationFetchlist()
	{
		return DeviceConfigurationFetchListDaoImpl.getInstance();
	}

	/**
	 * Gets the get device info implementation class instance.
	 *
	 * @return the get device info dao impl
	 */
	public static GetDeviceInfoDaoImpl getDeviceList()
	{
		return GetDeviceInfoDaoImpl.getInstance();
	}

	/**
	 * Gets the get fluid implementation class instance.
	 *
	 * @return the get fluid dao impl
	 */
	public static GetFluidLogDaoImpl getFluidList()
	{
		return GetFluidLogDaoImpl.getInstance();
	}

	/**
	 * Gets the default unit corresponding to a medication
	 *
	 * @return the default unit
	 */
	public static GetDefaultUnitDaoImpl getDefaultUnit()
	{
		return GetDefaultUnitDaoImpl.getInstance();
	}

	/**
	 * Gets the get output implementation class instance.
	 *
	 * @return the get output dao impl
	 */
	public static GetOutputDaoImpl getOutputdetails()
	{
		return GetOutputDaoImpl.getInstance();
	}

	/**
	 * Gets the initialisation implementation class instance.
	 *
	 * @return the initialisation dao impl
	 */
	public static InitializationServiceDaoImpl getSessionStarted()
	{
		return InitializationServiceDaoImpl.getInstance();
	}

	/**
	 * Gets the investigation test implementation class instance.
	 *
	 * @return the investigation test dao impl
	 */
	public static InvestigationTestDaoImpl getInvestigationTest()
	{
		return InvestigationTestDaoImpl.getInstance();
	}

	/**
	 * Gets the investigation test parameter field implementation class instance.
	 *
	 * @return the investigation test parameter field dao impl
	 */
	public static InvestigationTestParameterFieldsDaoImpl getInvestigationParameterFields()
	{
		return InvestigationTestParameterFieldsDaoImpl.getInstance();
	}
	/**
	 * Gets the investiagtion value view implementation class instance.
	 *
	 * @return the investiagtion value view dao impl
	 */
	public static GetInvestigationValueViewDaoImpl getInvestigationValueView()
	{
		return GetInvestigationValueViewDaoImpl.getInstance();
	}

	/**
	 * Gets the set investigation value implementation class instance.
	 *
	 * @return the set investigation value dao impl
	 */
	public static SetInvestigationValuesDaoImpl investigationValues()
	{
		return SetInvestigationValuesDaoImpl.getInstance();
	}

	/**
	 * Gets the delete investigation value implementation class instance.
	 *
	 * @return the delete investigation value dao impl
	 */
	public static DeleteInvestigationValueDaoImpl deleteInvestigationValue()
	{
		return DeleteInvestigationValueDaoImpl.getInstance();
	}

	/**
	 * Gets the update investigation value implementation class instance.
	 *
	 * @return the update investigation value dao impl
	 */
	public static UpdateInvestigationValueDaoImpl updateInvestigationValue()
	{
		return UpdateInvestigationValueDaoImpl.getInstance();
	}

	/**
	 * Gets the get patient implementation class instance.
	 *
	 * @return the get patient dao impl
	 */
	public static GetPatientDaoImpl getPatient()
	{
		return GetPatientDaoImpl.getInstance();
	}
	/**
	 * Gets the get application setting implementation class instance.
	 *
	 * @return the application setting dao impl
	 */

	public static ApplicationSettingDaoImpl getApplicationSetting()
	{
		return ApplicationSettingDaoImpl.getInstance();
	}

	/**
	 * Gets the SpecialityOT  implementation class instance.
	 *
	 * @return theSpecialityOT dao impl
	 */
	public static SpecialityOTDaoImpl getOT()
	{
		return SpecialityOTDaoImpl.getInstance();
	}
	/**
	 * Gets the RecoveryRoomReading  implementation class instance.
	 *
	 * @return RecoveryRoomReading dao impl
	 */
	public static RecoveryRoomReadingDaoImpl recoveryRoomReading()
	{
		return RecoveryRoomReadingDaoImpl.getInstance();
	}
	/**
	 * Gets the PatientDetails  implementation class instance.
	 *
	 * @return RecoveryRoomReading dao impl
	 */
	public static PatientReportDaoImpl getPatientDetailsReport()
	{
		return PatientReportDaoImpl.getInstance();
	}
	/**
	 * Gets the PatientDetails  implementation class instance.
	 *
	 * @return RecoveryRoomReading dao impl
	 */
	public static CannulationsDaoImpl cannulations()
	{
		return CannulationsDaoImpl.getInstance();
	}
	/**
	 * Gets the PatientMonitorData  implementation class instance.
	 *
	 * @return PatientMonitorData dao impl
	 */
	public static PatientMonitorDataDaoImpl patientMonitorData()
	{
		return PatientMonitorDataDaoImpl.getInstance();
	}
	/**
	 * Gets the DeviceParameterMaster  implementation class instance.
	 *
	 * @return DeviceParameterMaster dao impl
	 */
	/*public static DeviceParameterMasterDaoImpl deviceParameterMaster()
	{
		return DeviceParameterMasterDaoImpl.getInstance();
	}*/
	/**
	 * Gets the AnethesiaMachineData  implementation class instance.
	 *
	 * @return AnethesiaMachineData dao impl
	 */
	public static AnethesiaMachineDataDaoImpl anethesiaMachineData()
	{
		return AnethesiaMachineDataDaoImpl.getInstance();
	}
	public static CPBDetailsDaoImpl CPBDetails()
	{
		return CPBDetailsDaoImpl.getInstance();
	}
	public static EchoDetailsDaoImpl EchoDetails()
	{
		return EchoDetailsDaoImpl.getInstance();
	}
	public static AuditReportDaoImpl AuditReport()
	{
		return AuditReportDaoImpl.getInstance();
	}
	public static AuditReportDaoImpl1 AuditReport1()
	{
		return AuditReportDaoImpl1.getInstance();
	}
	public static AuditReportDaoImpl2 AuditReport2()
	{
		return AuditReportDaoImpl2.getInstance();
	}


	public static IcuPlanDaoImpl icuPlanReadings()
	{
		return IcuPlanDaoImpl.getInstance();
	}
}
