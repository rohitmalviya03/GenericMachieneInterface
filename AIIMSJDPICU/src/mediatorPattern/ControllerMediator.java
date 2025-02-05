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

public class ControllerMediator implements MediateControllers {
	private LoginController loginController;
	private MainController mainController;
	private BackendObserver backendObserver;
	private ManageAvailableDevicesController manageDeviceController;
	private AssignInfusionPumpController assignInfusionPumpController;
	private HistoricalDataController historicalDataController;
	private MedicationInfusionController medicationInfusionController;
	private DashboardAnaestheiaController dashboardAnaestheiaController;
	private PatientMonitorController patientMonitorController;
	private StopInfusionController stopInfusionController;
	private DashboardFluidController dashboardFluidController;
	private DashboardEventController dashboardEventController;
	private DashboardOutputController dashboardOutputController;
	private DashboardTestController dashboardTestController;
	private PostOperativeController postOperativeController;
	private  CreateCaseController createCaseController;
	private  GetSnapshot getSnapshot;
	private GetSnapshotController getSnapshotController;
	private GridHistoricalViewController gridHistoricalViewController;
	private EventsController eventsController;
	@Override
	public void registerLoginController(LoginController controller) {
		loginController = controller;

	}

	@Override
	public LoginController getLoginController() {
		return loginController;

	}

	@Override
	public void registerMainController(MainController controller) {
		mainController = controller;

	}

	@Override
	public MainController getMainController() {
		return mainController;
	}



	@Override
	public void registerBackendObserver(BackendObserver observer) {
		backendObserver = observer;
	}

	@Override
	public BackendObserver getBackendObserver() {
		return backendObserver;
	}

	@Override
	public void registerManageAvailableDevicesController(ManageAvailableDevicesController controller) {
		manageDeviceController = controller;

	}

	@Override
	public ManageAvailableDevicesController getManageAvailableDevicesController() {
		// TODO Auto-generated method stub
		return manageDeviceController;
	}
	@Override
	public void registerAssignInfusionPumpController(AssignInfusionPumpController controller) {
		assignInfusionPumpController=controller;

	}

	@Override
	public AssignInfusionPumpController getAssignInfusionPumpController() {

		return assignInfusionPumpController;
	}

	@Override
	public void registerHistoricalDataController(HistoricalDataController controller)
	{
		historicalDataController = controller;

	}

	@Override
	public HistoricalDataController getHistoricalDataController()
	{
		// TODO Auto-generated method stub
		return historicalDataController;
	}

	private ControllerMediator() {
	}

	public static ControllerMediator getInstance() {
		return ControllerMediatorHolder.INSTANCE;
	}

	private static class ControllerMediatorHolder {
		private static final ControllerMediator INSTANCE = new ControllerMediator();
	}

	@Override
	public void registerMedicationInfusionController(MedicationInfusionController controller)
	{
		medicationInfusionController = controller;

	}

	@Override
	public MedicationInfusionController getMedicationInfusionController()
	{
		return medicationInfusionController;
	}

	@Override
	public void registerDashboardAnaestheiaController(DashboardAnaestheiaController controller)
	{
		dashboardAnaestheiaController = controller;
	}

	@Override
	public DashboardAnaestheiaController getDashboardAnaestheiaController()
	{
		return dashboardAnaestheiaController;
	}

	@Override
	public void registerPatientMonitorController(PatientMonitorController controller)
	{
		patientMonitorController = controller;
	}

	@Override
	public PatientMonitorController getPatientMonitorController()
	{
		return patientMonitorController;
	}

	@Override
	public void registerStopInfusionController(StopInfusionController controller)
	{
		stopInfusionController = controller;
	}

	@Override
	public StopInfusionController getStopInfusionController()
	{
		return stopInfusionController;
	}

	@Override
	public void registerDashboardFluidController(DashboardFluidController controller)
	{
		dashboardFluidController = controller;
	}

	@Override
	public DashboardFluidController getDashboardFluidController()
	{
		return dashboardFluidController;
	}

	@Override
	public void registerDashboardEventController(DashboardEventController controller) {
		dashboardEventController=controller;
	}
	@Override
	public DashboardEventController getDashboardEventController()
	{
		return dashboardEventController;
	}
	@Override
	public void registerDashboardOutputController(DashboardOutputController controller)
	{
		dashboardOutputController = controller;

	}

	@Override
	public DashboardOutputController getDashboardOutputController()
	{
		return dashboardOutputController;
	}

	@Override
	public void registerDashboardTestController(DashboardTestController controller)
	{
		dashboardTestController = controller;
	}

	@Override
	public DashboardTestController getDashboardTestController()
	{
		return dashboardTestController;
	}
	@Override
	public PostOperativeController getPostOperativeController()
	{
		return postOperativeController;
	}

	@Override
	public void registerPostOperativeController(PostOperativeController controller) {
		// TODO Auto-generated method stub
		postOperativeController = controller;
	}
	@Override
	public CreateCaseController getCreateCaseController()
	{
		return createCaseController;
	}

	@Override
	public void registerCreateCaseController(CreateCaseController controller) {
		// TODO Auto-generated method stub
		createCaseController = controller;
	}


	@Override
	public GetSnapshot getGetSnapshot()
	{
		return getSnapshot;
	}

	@Override
	public void registerGetSnapshot(GetSnapshot controller) {
		// TODO Auto-generated method stub
		getSnapshot = controller;
	}

	@Override
	public void registerGetSnapshotController(GetSnapshotController controller) {
		// TODO Auto-generated method stub
		getSnapshotController = controller;
	}

	@Override
	public GetSnapshotController GetSnapshotController() {
		// TODO Auto-generated method stub
		return getSnapshotController;
	}

	@Override
	public void registerGridHistoricalViewController(GridHistoricalViewController controller)
	{
		gridHistoricalViewController = controller;

	}

	@Override
	public GridHistoricalViewController getGridHistoricalViewController()
	{
		// TODO Auto-generated method stub
		return gridHistoricalViewController;
	}


	@Override
	public void registerEventsController(EventsController controller)
	{
		eventsController = controller;

	}

	@Override
	public EventsController getEventsController()
	{
		// TODO Auto-generated method stub
		return eventsController;
	}

}
