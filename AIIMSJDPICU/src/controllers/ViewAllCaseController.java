/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.cdac.common.GeneratedEntities.Patient;
import com.cdac.common.GeneratedEntities.Patientcase;

import application.FxmlView;
import application.Main;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import model.EditCaseFlagSession;
import model.PatientSession;
import services.ViewAllCaseService;

/**
 * ViewAllCaseController.java<br>
 * Purpose: This method contains code to handle inputs from ViewAllCase screen
 *
 * @author Rohit_Sharma41
 *
 */
public class ViewAllCaseController implements Initializable {

	@FXML
	private Label lblName;

	@FXML
	private Label lblGender;

	@FXML
	private Label lblAge;

	@FXML
	private Label lblCRNum;

	@FXML
	private Label lblHeight;

	@FXML
	private Label lblWeight;

	@FXML
	private Label lblBMI;

	@FXML
	private Label lblBSA;
	@FXML
	private StackPane stackPaneLoader;

	@FXML
	private TableView<Patientcase> tableViewAllCase;

	@FXML
	private TableColumn<Patientcase, String> columnCaseNumber;

	@FXML
	private TableColumn<Patientcase, Date> columnDate;

	@FXML
	private TableColumn<Patientcase, String> columnSurgeryType;

	@FXML
	private TableColumn<Patientcase, String> columnSpeciality;

	@FXML
	private TableColumn<Patientcase, String> columnStatus;

	@FXML
	private Pagination paginator;

	@FXML
	private Button btnLoadCase;

	@FXML
	private Button btnClose;

	@FXML
	private Button btnBack;

	@FXML
	private Button btnCreateCase;

	@FXML
	private Button btnEditCase;

	private static final Logger LOGGER = Logger.getLogger(CreatePatientController.class.getName());

	private Patient patientInfo;
	private EventHandler<WorkerStateEvent> GetAllCasesSuccessHandler;
	private EventHandler<WorkerStateEvent> GetAllCasesFailedHandler;
	private EventHandler<javafx.scene.input.MouseEvent> btnEditCaseHandler;
	private EventHandler<javafx.scene.input.MouseEvent> btnBackHandler;
	private EventHandler<javafx.scene.input.MouseEvent> btnCloseHandler;
	private EventHandler<javafx.scene.input.MouseEvent> btnLoadCaseHandler;
	private EventHandler<javafx.scene.input.MouseEvent> btnCreateCaseHandler;
	private ChangeListener<Patientcase> tableHandler;

	private ChangeListener<Number> paginatorListener;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			if (Main.getInstance().getLastFxmlScreen().equalsIgnoreCase("SearchPatient")) {
				btnBack.setVisible(true);
			}

			paginator.setVisible(false);

			btnCloseHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent event) {
					try
					{
						removeListerns();
						Main.getInstance().getStageManager().closeSecondaryStage();
					}
					catch (Exception e)
					{
						LOGGER.error("## Exception occured:", e);
					}

				}
			};
			btnClose.addEventHandler(MouseEvent.MOUSE_CLICKED, btnCloseHandler);

			btnBackHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent event) {
					try
					{
						removeListerns();
						Main.getInstance().getStageManager().switchSceneAfterMain(FxmlView.SEARCH_PATIENT);
					}
					catch (Exception e)
					{
						LOGGER.error("## Exception occured:", e);

					}

				}
			};
			btnBack.addEventHandler(MouseEvent.MOUSE_CLICKED, btnBackHandler);

			btnEditCaseHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent event) {
					try
					{
						if (EditCaseFlagSession.getInstance() != null) {
							EditCaseFlagSession.getInstance().setIsEdit(true);
							EditCaseFlagSession.getInstance()
									.setCaseId(tableViewAllCase.getSelectionModel().getSelectedItem().getCaseId());
						}
						removeListerns();
						Main.getInstance().setLastFxmlScreen("ViewAllCase");

						Main.getInstance().getStageManager().closeSecondaryStage();
						Main.getInstance().getStageManager().switchSceneAfterMain(FxmlView.CREATE_CASE);
					}
					catch (Exception e)
					{
						LOGGER.error("## Exception occured:", e);

					}
				}
			};
			btnEditCase.addEventHandler(MouseEvent.MOUSE_CLICKED, btnEditCaseHandler);

			btnCreateCaseHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent event) {

					try {
						if (EditCaseFlagSession.getInstance() != null) {
							EditCaseFlagSession.getInstance().setIsEdit(false);
							EditCaseFlagSession.getInstance().setIsEditFromDashboard(false);
						}
						removeListerns();
//						Stage stage = Main.getInstance().getChildStage();
//						if (stage != null) {
//							stage.close();
//						}
						Main.getInstance().setLastFxmlScreen("ViewAllCase");
						Main.getInstance().getStageManager().closeSecondaryStage();
						Main.getInstance().getStageManager().switchSceneAfterMain(FxmlView.CREATE_CASE);
//						ControllerMediator.getInstance().getMainController().bindPopupFxml("/View/createCase.fxml");


					} catch (Exception ex) {

						LOGGER.error("## Exception occured:", ex);

					}
				}
			};
			btnCreateCase.addEventHandler(MouseEvent.MOUSE_CLICKED, btnCreateCaseHandler);

			// ---create a case


				stackPaneLoader.setVisible(true);

				// ---Populating tableView
				columnCaseNumber.setCellValueFactory(new PropertyValueFactory<Patientcase, String>("caseId"));
				// changes from shail.sharma08 start here
				// columnDate.setCellValueFactory(new
				// PropertyValueFactory<Patientcase, String>("caseDateTime"));

//				SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
				columnDate.setCellValueFactory(new PropertyValueFactory("caseDateTime"));
//				columnDate.setCellFactory((TableColumn<Patientcase, Date> column) -> {
//					return new TableCell<Patientcase, Date>() {
//						@Override
//						protected void updateItem(Date item, boolean empty) {
//							super.updateItem(item, empty);
//							if (item == null || empty) {
//								setText(null);
//							} else {
//								setText(formatter.format(item));
//							}
//						}
//					};
//				});
				/// changes from sahil.sharma08 end here

				columnSurgeryType.setCellValueFactory(new PropertyValueFactory<Patientcase, String>("surgeryType"));
				columnSpeciality.setCellValueFactory(new PropertyValueFactory<Patientcase, String>("speciality"));
				columnStatus.setCellValueFactory(new PropertyValueFactory<Patientcase, String>("status"));

				// ---Set Patient Details Header
				patientInfo = PatientSession.getInstance().getPatient();
				lblName.setText(patientInfo.getFirstName() + " " + patientInfo.getLastName());
				lblGender.setText(patientInfo.getGender());
				lblAge.setText(String.valueOf(patientInfo.getAge())+" "+patientInfo.getAgeUnit());
				lblCRNum.setText(String.valueOf(patientInfo.getCrnumber()));
				lblHeight.setText(String.valueOf(patientInfo.getHeight()));
				lblWeight.setText(String.valueOf(patientInfo.getWeight()));
				lblBMI.setText(String.valueOf(patientInfo.getBmi()));
				lblBSA.setText(String.valueOf(patientInfo.getBsa()));

				// ---initial service call
				callViewAllCaseService(patientInfo.getPatientId(), 1);

				paginatorListener = new ChangeListener<Number>() {

					@Override
					public void changed(ObservableValue<? extends Number> observable, Number oldValue,
							Number newIndex) {
						try {
							createPage(patientInfo.getPatientId(), newIndex.intValue() + 1);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							LOGGER.error("## Exception occured:", e1);
						}

					}

				};

				// ---Paginator button clicks
				paginator.currentPageIndexProperty().addListener(paginatorListener);

				// ---Enable LoadCase button only when a table row is selected.
				tableHandler = new ChangeListener<Patientcase>() {

					@Override
					public void changed(ObservableValue<? extends Patientcase> observable, Patientcase oldValue,
							Patientcase newValue) {

						try
						{
							if (newValue != null) {

								btnLoadCase.setDisable(false);
								btnEditCase.setDisable(false);
							}
						}
						catch (Exception e)
						{
							LOGGER.error("## Exception occured:", e);

						}

					}
				};

				tableViewAllCase.getSelectionModel().selectedItemProperty().addListener(tableHandler);

				// ---On click of LoadCase button

				btnLoadCaseHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
					public void handle(javafx.scene.input.MouseEvent event) {
						try {
							// ---Save selected row into Patient session
							Patientcase selectedCaseRow = tableViewAllCase.getSelectionModel().getSelectedItem();
							PatientSession.getInstance().setPatientCase(selectedCaseRow);

							// ---Fetch list of all event to be displayed on
							// left
							// panel
							removeListerns();

							Main.getInstance().getStageManager().closeSecondaryStage();
							Main.getInstance().getStageManager().switchScene(FxmlView.MAIN);

							Main.getInstance().getUtility().warningDialogue("Information",
									"Please do not forget to scan the devices in the beginning.");

//							Stage stage = Main.getInstance().getChildStage();
//							if (stage != null) {
//								stage.close();
//							}
//
//							FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/NewDashboard.fxml"));
//							Pane pane = fxmlLoader.load();
//
//							StackPane stackPane = new StackPane();
//							stackPane.getChildren().add(pane);
//
//							Scene scene = new Scene(stackPane);
//							scene.getStylesheets().add(this.getClass().getResource("/styles/newStyles.css").toString());
//
//							Main.getInstance().getParentStage().setScene(scene);
//							Main.getInstance().getParentStage().show();
//
//							Main.getInstance().setParentScene(scene);

						} catch (Exception e1) {
							LOGGER.error("## Exception occured:", e1);
						}
					}
				};
				btnLoadCase.addEventHandler(MouseEvent.MOUSE_CLICKED, btnLoadCaseHandler);


		} catch (Exception e) {
			LOGGER.error("## Exception occured:", e);
		}

	}

	/**
	 * This method removes event handlers from all the components
	 */
	private void removeListerns()
	{

		btnBack.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnBackHandler);
		btnClose.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnCloseHandler);
		btnEditCase.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnEditCaseHandler);
		btnCreateCase.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnCreateCaseHandler);
		btnLoadCase.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnLoadCaseHandler);
		paginator.currentPageIndexProperty().removeListener(paginatorListener);
		tableViewAllCase.getSelectionModel().selectedItemProperty().removeListener(tableHandler);

		patientInfo = null;
		GetAllCasesSuccessHandler = null;
		GetAllCasesFailedHandler = null;
		btnEditCaseHandler = null;
		btnBackHandler = null;
		btnCloseHandler = null;
		btnLoadCaseHandler = null;
		btnCreateCaseHandler = null;
		tableHandler = null;
		paginatorListener = null;

	}

	/**
	 * This method calls ViewAllCaseService which fetches all cases from database
	 * @param patientID ID of the selected patient
	 * @param pageNum number of the selected page
	 */
	private void callViewAllCaseService(int patientID, int pageNum)
	{
		try {
			ViewAllCaseService viewAllCaseService = ViewAllCaseService.getInstance(patientID, pageNum);
			viewAllCaseService.restart();

			// ---on service success

			GetAllCasesSuccessHandler = new EventHandler<WorkerStateEvent>() {

				@SuppressWarnings("unchecked")
				@Override
				public void handle(WorkerStateEvent event) {


					try
					{
						Map<String, Object> caseDetails = new HashMap<String, Object>();
						caseDetails = viewAllCaseService.getCaseDetails();

						if (caseDetails != null) {
							for (Map.Entry<String, Object> entry : caseDetails.entrySet()) {
								String checkStringClass = "";

								Object obj = entry.getValue();
								List<Patientcase> caseList = new ArrayList<Patientcase>();
								if (obj.getClass().isInstance(caseList)) {
									caseList = (ArrayList<Patientcase>) obj;
									// for (Patientcase caseObj : caseList) {
									// }

									// ---populating Grid
									tableViewAllCase.getItems().clear();
									tableViewAllCase.getItems().addAll((caseList));
									paginator.setDisable(false);

								}
								else
								{

									// ---set number of pages in paginator
									if(!entry.getValue().toString().equalsIgnoreCase("No cases found")){
									paginator.setVisible(true);
									int key = (int) entry.getValue();
									int size = key / 10;
									int remainder=key%10;
									if(remainder!=0){
									size = size + 1;
									}


									paginator.setPageCount(size);

								}
								}

							}

						}
						stackPaneLoader.setVisible(false);
						viewAllCaseService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
								GetAllCasesSuccessHandler);
						viewAllCaseService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
								GetAllCasesFailedHandler);
					}
					catch (Exception e)
					{
						LOGGER.error("## Exception occured:", e);

					}
				}

			};

			GetAllCasesFailedHandler = new EventHandler<WorkerStateEvent>() {

				@Override
				public void handle(WorkerStateEvent event) {
					try
					{
						stackPaneLoader.setVisible(false);

						Main.getInstance().getUtility().showNotification("Error", "Error",
						        viewAllCaseService.getException().getMessage());
						stackPaneLoader.setVisible(false);
						viewAllCaseService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
						        GetAllCasesSuccessHandler);
						viewAllCaseService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
						        GetAllCasesFailedHandler);
					}
					catch (Exception e)
					{
						LOGGER.error("## Exception occured:", e);

					}
				}

			};
			viewAllCaseService.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, GetAllCasesSuccessHandler);
			viewAllCaseService.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED, GetAllCasesFailedHandler);

		} catch (Exception e) {
			LOGGER.error("## Exception occured:", e);
		}
	}

	/**
	 * This method creates a new page in the paginator components
	 * @param patientID ID of the selected patient
	 * @param pageIndex page index
	 */
	private void createPage(int patientID, int pageIndex)
	{
			stackPaneLoader.setVisible(true);
			paginator.setDisable(true);
			btnLoadCase.setDisable(true);

			callViewAllCaseService(patientID, pageIndex);

	}

}
