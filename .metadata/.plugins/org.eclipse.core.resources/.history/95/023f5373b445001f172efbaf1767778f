/*******************************************************************************
 * © 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package mediatorPattern;

import controllers.AssignInfusionPumpController;
import controllers.BackendObserver;
import controllers.CreateCaseController;
import controllers.DashboardAnaestheiaController;
import controllers.DashboardEventController;
import controllers.DashboardFluidController;
import controllers.DashboardOutputController;
import controllers.DashboardTestController;
import controllers.EventsController;
import controllers.GetSnapshotController;
import controllers.GridHistoricalViewController;
import controllers.HistoricalDataController;
import controllers.LoginController;
import controllers.MainController;
import controllers.ManageAvailableDevicesController;
import controllers.MedicationInfusionController;
import controllers.PatientMonitorController;
import controllers.PostOperativeController;
import controllers.StopInfusionController;
import utility.GetSnapshot;

public interface MediateControllers
{
	void registerLoginController(LoginController controller);
	LoginController getLoginController();

	void registerMainController(MainController controller);
	MainController getMainController();


	void registerBackendObserver(BackendObserver observer);
	BackendObserver getBackendObserver();

	void registerManageAvailableDevicesController(ManageAvailableDevicesController controller);
	ManageAvailableDevicesController getManageAvailableDevicesController();

	void registerAssignInfusionPumpController(AssignInfusionPumpController controller);
	AssignInfusionPumpController getAssignInfusionPumpController();

	void registerHistoricalDataController(HistoricalDataController historicalDataController);
	HistoricalDataController getHistoricalDataController();

	void registerMedicationInfusionController(MedicationInfusionController medicationInfusionController);
	MedicationInfusionController getMedicationInfusionController();

	void registerDashboardAnaestheiaController(DashboardAnaestheiaController dashboardAnaestheiaController);
	DashboardAnaestheiaController getDashboardAnaestheiaController();

	void registerPatientMonitorController(PatientMonitorController patientMonitorController);
	PatientMonitorController getPatientMonitorController();

	void registerStopInfusionController(StopInfusionController stopInfusionController);
	StopInfusionController getStopInfusionController();

	void registerDashboardFluidController(DashboardFluidController dashboardFluidController);
	DashboardFluidController getDashboardFluidController();
	void registerDashboardEventController(DashboardEventController dashboardEventController);
	DashboardEventController getDashboardEventController();

	void registerDashboardOutputController(DashboardOutputController dashboardOutputController);
	DashboardOutputController getDashboardOutputController();

	void registerDashboardTestController(DashboardTestController dashboardTestController);
	DashboardTestController getDashboardTestController();

	void registerPostOperativeController(PostOperativeController postOperativeController);
	PostOperativeController getPostOperativeController();

	void registerCreateCaseController(CreateCaseController createCaseController);
	CreateCaseController getCreateCaseController();

	void registerGetSnapshot(GetSnapshot getSnapshot);
	GetSnapshot getGetSnapshot();

	void registerGetSnapshotController(GetSnapshotController getSnapshot);
	GetSnapshotController GetSnapshotController();

	void registerGridHistoricalViewController(GridHistoricalViewController controller);
	GridHistoricalViewController getGridHistoricalViewController();

	void registerEventsController(EventsController controller);
	EventsController getEventsController();

}
