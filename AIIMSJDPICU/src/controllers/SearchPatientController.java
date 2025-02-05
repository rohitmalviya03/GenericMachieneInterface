/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.cdac.common.GeneratedEntities.Patient;
import com.cdac.common.GeneratedEntities.SearchPatientEntity;

import application.FxmlView;
import application.Main;
import javafx.application.Platform;
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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import model.EditPatientSession;
import model.PatientSession;
import services.SearchPatientService;

/**
 * This controller is used for Search Patients on the basis of Search criteria
 * entered
 *
 * @author Sudeep_Sahoo
 *
 */
public class SearchPatientController implements Initializable {

	@FXML
	private TextField txtCRNum;

	@FXML
	private TextField txtFirstName;

	@FXML
	private TextField txtLastName;

	@FXML
	private TextField txtContactNum;

	@FXML
	private TextField txtCaseNum;

	@FXML
	private Button btnSearch;

	@FXML
	private TableView<Patient> tablePatient;

	@FXML
	private TableColumn<Patient, String> columnCRNumber;

	@FXML
	private TableColumn<Patient, String> columnFirstName;

	@FXML
	private TableColumn<Patient, String> columnLastName;

	@FXML
	private TableColumn<Patient, String> columnAge;

	@FXML
	private TableColumn<Patient, String> columnUnit;

	@FXML
	private TableColumn<Patient, String> columnContact;

	@FXML
	private TableColumn<Patient, String> columnFatherGuardian;

	@FXML
	private Pagination paginator;

	@FXML
	private Button btnViewCase;

	@FXML
	private Button btnCreate;

	@FXML
	private Button btnClose;

	@FXML
	private StackPane stackPaneLoader;

	@FXML
	private Button btnEdit;

	@FXML
	private Label lblError;

	private SearchPatientEntity searchPatientEntity;
	private int selectedPatientId;

	private EventHandler<WorkerStateEvent> searchSuccessHandler;
	private EventHandler<WorkerStateEvent> searchFailedHandler;
	private EventHandler<javafx.scene.input.MouseEvent> btnCloseHandler;
	private EventHandler<javafx.scene.input.MouseEvent> btnCreateHandler;
	private EventHandler<javafx.scene.input.MouseEvent> btnSearchHandler;
	private ChangeListener<Number> paginatorListener;
	private ChangeListener<Patient> tableHandler;

	private EventHandler<javafx.scene.input.MouseEvent> btnViewCaseHandler;
	private EventHandler<javafx.scene.input.MouseEvent> btnEditHandler;
	private EventHandler<javafx.scene.input.KeyEvent> keyCRNumEventHandler;
	private EventHandler<javafx.scene.input.KeyEvent> keyFirstNameEventHandler;
	private EventHandler<javafx.scene.input.KeyEvent> keyLastNameEventHandler;
	private EventHandler<javafx.scene.input.KeyEvent> keyContactNumEventHandler;
	private EventHandler<javafx.scene.input.KeyEvent> keyCaseNumEventHandler;

	private static final Logger LOG = Logger.getLogger(SearchPatientController.class.getName());

	/**
	 * This method initializes the all Event handlers and Default values on page
	 * load
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		try {
			// ---populating tableView
			columnCRNumber.setCellValueFactory(new PropertyValueFactory<Patient, String>("crnumber"));
			columnFirstName.setCellValueFactory(new PropertyValueFactory<Patient, String>("firstName"));
			columnLastName.setCellValueFactory(new PropertyValueFactory<Patient, String>("lastName"));
			columnAge.setCellValueFactory(new PropertyValueFactory<Patient, String>("age"));
			columnUnit.setCellValueFactory(new PropertyValueFactory<Patient, String>("ageUnit"));
			columnContact.setCellValueFactory(new PropertyValueFactory<Patient, String>("mobilephone"));
			columnFatherGuardian.setCellValueFactory(new PropertyValueFactory<Patient, String>("guardianName"));

			// ---On pressing ENTER key after filling textboxes
			try {
				captureEnterKeyEvent();
			} catch (Exception e2) {

				LOG.error("## Exception occured:", e2);
			}

			btnCloseHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent event) {

					try {
						closePopup();
					} catch (Exception e) {

						LOG.error("## Exception occured:", e);
					}

				}
			};
			btnClose.addEventHandler(MouseEvent.MOUSE_CLICKED, btnCloseHandler);

			// ---On Create button click

			btnCreateHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent event) {
					try {

						closePopup();
						Main.getInstance().setLastFxmlScreen("SearchPatient");
						Main.getInstance().getStageManager().switchSceneAfterMain(FxmlView.CREATE_PATIENT);

					} catch (Exception e) {

						LOG.error("## Exception occured:", e);
					}
				}
			};
			btnCreate.addEventHandler(MouseEvent.MOUSE_CLICKED, btnCreateHandler);

			// ---On Search button click

			btnSearchHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent event) {
					try {
						searchButtonClicked();
					} catch (Exception e) {

						LOG.error("## Exception occured:", e);
					}
				}
			};
			btnSearch.addEventHandler(MouseEvent.MOUSE_CLICKED, btnSearchHandler);

			// ---Paginator button clicks

			paginatorListener = new ChangeListener<Number>() {

				@Override
				public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newIndex) {
					try {
						createPage(searchPatientEntity, newIndex.intValue() + 1);
					} catch (Exception e1) {

						LOG.error("## Exception occured:", e1);
					}

				}

			};

			// ---Paginator button clicks
			paginator.currentPageIndexProperty().addListener(paginatorListener);

			// ---Enable ViewCase button only when a table row is selected.

			tableHandler = new ChangeListener<Patient>() {

				@Override
				public void changed(ObservableValue<? extends Patient> observable, Patient oldValue, Patient newValue) {

					try
					{
						if (newValue != null) {
							btnViewCase.setDisable(false);
							btnEdit.setDisable(false);
							if (tablePatient.getSelectionModel().getSelectedItem() != null) {
								selectedPatientId = tablePatient.getSelectionModel().getSelectedItem().getPatientId();
							}
						}
					}
					catch (Exception e)
					{
						LOG.error("## Exception occured:", e);

					}
				}
			};

			tablePatient.getSelectionModel().selectedItemProperty().addListener(tableHandler);

			// ---On click of ViewCase button

			btnViewCaseHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent event) {
					try {
						Main.getInstance().setLastFxmlScreen("SearchPatient");
						Patient selectedPateintRow = tablePatient.getSelectionModel().getSelectedItem();
						PatientSession.getInstance().setPatient(selectedPateintRow);

						try {

							closePopup();
							Main.getInstance().getStageManager().switchSceneAfterMain(FxmlView.VIEW_ALL_CASES);

						} catch (Exception e) {

							LOG.error("## Exception occured:", e);
						}

					} catch (Exception e1) {

						LOG.error("## Exception occured:", e1);

					}
				}
			};
			btnViewCase.addEventHandler(MouseEvent.MOUSE_CLICKED, btnViewCaseHandler);

			btnEditHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent event) {
					try {
						closePopup();

						if (EditPatientSession.getInstance() != null)
						{

							EditPatientSession.getInstance().setIsEdit(true);
							EditPatientSession.getInstance().setPatientId(selectedPatientId);
						}

						Main.getInstance().getStageManager().switchSceneAfterMain(FxmlView.CREATE_PATIENT);
					} catch (Exception e1) {

						LOG.error("## Exception occured:", e1);
					}
				}
			};
			btnEdit.addEventHandler(MouseEvent.MOUSE_CLICKED, btnEditHandler);
		} catch (Exception e) {
			LOG.error("## Exception occured:", e);
			Main.getInstance().getUtility().showNotification("Error", "Error", "Something went wrong");
		}

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				try
				{
					txtCRNum.requestFocus();
				}
				catch (Exception e)
				{
					LOG.error("## Exception occured:", e);
				}
			}
		});

	}

	/**
	 * This method is used to Close Search Patient Window
	 *
	 * @throws Exception
	 */

	private void closePopup() throws Exception
	{
		try {
			removeListeners();
			Main.getInstance().getStageManager().closeSecondaryStage();
		} catch (Exception e) {
			LOG.error("## Exception occured:", e);
		}
	}

	/**
	 * This method handles ENTER-KEY events on text fields
	 *
	 * @throws Exception
	 */
	private void captureEnterKeyEvent() throws Exception
	{
		try {

			keyCRNumEventHandler = new EventHandler<javafx.scene.input.KeyEvent>() {

				@Override
				public void handle(javafx.scene.input.KeyEvent event) {
					if (event.getCode().equals(KeyCode.ENTER)) {
						try {
							searchButtonClicked();
						} catch (Exception e) {

							LOG.error("## Exception occured:", e);
						}
					}
				}
			};
			txtCRNum.addEventHandler(javafx.scene.input.KeyEvent.KEY_RELEASED, keyCRNumEventHandler);

			keyFirstNameEventHandler = new EventHandler<javafx.scene.input.KeyEvent>() {

				@Override
				public void handle(javafx.scene.input.KeyEvent event) {
					if (event.getCode().equals(KeyCode.ENTER)) {
						try {
							searchButtonClicked();
						} catch (Exception e) {

							LOG.error("## Exception occured:", e);
						}
					}
				}
			};
			txtFirstName.addEventHandler(javafx.scene.input.KeyEvent.KEY_RELEASED, keyFirstNameEventHandler);

			keyLastNameEventHandler = new EventHandler<javafx.scene.input.KeyEvent>() {

				@Override
				public void handle(javafx.scene.input.KeyEvent event) {
					if (event.getCode().equals(KeyCode.ENTER)) {
						try {
							searchButtonClicked();
						} catch (Exception e) {

							LOG.error("## Exception occured:", e);
						}
					}
				}
			};
			txtLastName.addEventHandler(javafx.scene.input.KeyEvent.KEY_RELEASED, keyLastNameEventHandler);

			keyContactNumEventHandler = new EventHandler<javafx.scene.input.KeyEvent>() {

				@Override
				public void handle(javafx.scene.input.KeyEvent event) {
					if (event.getCode().equals(KeyCode.ENTER)) {
						try {
							searchButtonClicked();
						} catch (Exception e) {

							LOG.error("## Exception occured:", e);
						}
					}
				}
			};
			txtContactNum.addEventHandler(javafx.scene.input.KeyEvent.KEY_RELEASED, keyContactNumEventHandler);

			keyCaseNumEventHandler = new EventHandler<javafx.scene.input.KeyEvent>() {

				@Override
				public void handle(javafx.scene.input.KeyEvent event) {
					if (event.getCode().equals(KeyCode.ENTER)) {
						try {
							searchButtonClicked();
						} catch (Exception e) {

							LOG.error("## Exception occured:", e);
						}
					}
				}
			};
			txtCaseNum.addEventHandler(javafx.scene.input.KeyEvent.KEY_RELEASED, keyCaseNumEventHandler);

		} catch (Exception e) {
			LOG.error("## Exception occured:", e);
		}

	}

	/**
	 * This method is used to create a searchPatientEntity with entered details
	 * and pass it to SearchPatientService
	 *
	 * @throws Exception
	 */
	private void searchButtonClicked() throws Exception
	{
		try {
			if (!txtCaseNum.getText().isEmpty() || !txtContactNum.getText().isEmpty() || !txtCRNum.getText().isEmpty()
					|| !txtFirstName.getText().isEmpty() || !txtLastName.getText().isEmpty()) {
				lblError.setVisible(false);
				btnViewCase.setDisable(true);
				paginator.setVisible(false);
				btnCreate.setDisable(true);
				tablePatient.getItems().clear();
				tablePatient.getItems().addAll(new ArrayList<>());
				// tablePatient.setItems(FXCollections.observableArrayList());

				// ---populate searchPatientEntity
				searchPatientEntity = new SearchPatientEntity();

				if (!txtCRNum.getText().isEmpty() && txtCRNum.getText() != null) {
					searchPatientEntity.setcRNumber(Long.parseLong(txtCRNum.getText()));
				}

				if (!txtFirstName.getText().isEmpty() && txtFirstName.getText() != null) {
					searchPatientEntity.setFirstName(txtFirstName.getText());
				}

				if (!txtLastName.getText().isEmpty() && txtLastName.getText() != null) {
					searchPatientEntity.setLastName(txtLastName.getText());
				}

				if (!txtCaseNum.getText().isEmpty() && txtCaseNum.getText() != null) {
					searchPatientEntity.setCaseID(Long.parseLong(txtCaseNum.getText()));
				}

				if (!txtContactNum.getText().isEmpty() && txtContactNum.getText() != null) {
					searchPatientEntity.setMobilePhone(Long.parseLong(txtContactNum.getText()));
				}

				// ---call to Javafx Service : searchPatientService
				stackPaneLoader.setVisible(true);
				btnSearch.setDisable(true);
				int pageNum = 1;

				// ---initial service call
				callSearchPatientService(searchPatientEntity, pageNum);
			} else {
				lblError.setVisible(true);
				lblError.setText("* Please enter atleast one search criteria to search.");
				tablePatient.setItems(null);
			}

		} catch (Exception e) {
			LOG.error("## Exception occured:", e);
			Main.getInstance().getUtility().showNotification("Error", "Error", "Something went wrong");
		}

	}

	/**
	 * This method is used to search patients on the basis of search criteria
	 * entered
	 *
	 * @param searchPatientEntity
	 * @param pageNum
	 * @throws Exception
	 */
	private void callSearchPatientService(SearchPatientEntity searchPatientEntity, int pageNum) throws Exception
	{
		try {
			SearchPatientService searchPatientService = SearchPatientService.getInstance(searchPatientEntity, pageNum);
			searchPatientService.restart();

			searchSuccessHandler = new EventHandler<WorkerStateEvent>() {

				@SuppressWarnings("unchecked")
				@Override
				public void handle(WorkerStateEvent event) {

					try
					{
						stackPaneLoader.setVisible(false);
						btnSearch.setDisable(false);

						Map<String, Object> patientListDetails = new HashMap<String, Object>();
						patientListDetails = searchPatientService.getPatientListDetails();

						List<Patient> patientsList = new ArrayList<Patient>();

						if (patientListDetails != null)
						{
							for (Map.Entry<String, Object> entry : patientListDetails.entrySet())
							{
								String checkStringClass = "";

								Object obj = entry.getValue();

								if (obj.getClass().isInstance(patientsList))
								{
									patientsList = (ArrayList<Patient>) obj;

									// ---populating Grid

									paginator.setDisable(false);

								}
								else if (obj.getClass().isInstance(checkStringClass))
								{
								}

								else
								{

									// ---set number of pages in paginator
									paginator.setVisible(true);
									Long key = (Long) entry.getValue();
									int remainder = Integer.valueOf(key + "") % 10;
									Long size = key / 10;
									if (remainder != 0)
									{
										size = size + 1;
									}

									paginator.setPageCount(size.intValue());
								}

							}

						}
						tablePatient.getItems().clear();
						tablePatient.getItems().addAll(patientsList);
						// tablePatient.setItems(FXCollections.observableArrayList(patientsList));
						btnCreate.setDisable(false);
						searchPatientService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
						        searchSuccessHandler);
						searchPatientService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
						        searchFailedHandler);
					}
					catch (Exception e)
					{
						LOG.error("## Exception occured:", e);
					}
				}

			};

			searchPatientService.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, searchSuccessHandler);

			searchFailedHandler = new EventHandler<WorkerStateEvent>() {

				@Override
				public void handle(WorkerStateEvent event) {
					try
					{
						btnSearch.setDisable(false);
						stackPaneLoader.setVisible(false);

						Main.getInstance().getUtility().showNotification("Error", "Error",
								searchPatientService.getException().getMessage());
						searchPatientService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
								searchSuccessHandler);
						searchPatientService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED, searchFailedHandler);
					}
					catch (Exception e)
					{
						LOG.error("## Exception occured:", e);
					}
				}

			};
			searchPatientService.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED, searchFailedHandler);

		} catch (Exception e) {
			LOG.error("## Exception occured:", e);
		}
	}

	/**
	 * This method used for pagination of search patient table
	 *
	 * @param searchPatientEntity
	 * @param pageIndex
	 * @throws Exception
	 */
	private void createPage(SearchPatientEntity searchPatientEntity, int pageIndex) throws Exception
	{
		try {
			stackPaneLoader.setVisible(true);
			paginator.setDisable(true);
			btnViewCase.setDisable(true);
			callSearchPatientService(searchPatientEntity, pageIndex);
		} catch (Exception e) {
			LOG.error("## Exception occured:", e);
		}

	}

	/**
	 * This method is used to remove all listeners and nullify variables
	 */
	private void removeListeners()
	{

		btnSearch.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnSearchHandler);
		btnCreate.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnCreateHandler);
		btnEdit.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnEditHandler);
		btnClose.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnCloseHandler);
		btnViewCase.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnViewCaseHandler);
		paginator.currentPageIndexProperty().removeListener(paginatorListener);
		tablePatient.getSelectionModel().selectedItemProperty().removeListener(tableHandler);
		txtCRNum.removeEventHandler(javafx.scene.input.KeyEvent.KEY_RELEASED, keyCRNumEventHandler);
		txtCaseNum.removeEventHandler(javafx.scene.input.KeyEvent.KEY_RELEASED, keyCaseNumEventHandler);
		txtFirstName.removeEventHandler(javafx.scene.input.KeyEvent.KEY_RELEASED, keyFirstNameEventHandler);
		txtLastName.removeEventHandler(javafx.scene.input.KeyEvent.KEY_RELEASED, keyLastNameEventHandler);
		txtContactNum.removeEventHandler(javafx.scene.input.KeyEvent.KEY_RELEASED, keyContactNumEventHandler);
		searchPatientEntity = null;
		searchSuccessHandler = null;
		searchFailedHandler = null;
		btnCloseHandler = null;
		btnCreateHandler = null;
		btnSearchHandler = null;
		paginatorListener = null;
		tableHandler = null;
		btnViewCaseHandler = null;
		btnEditHandler = null;
		keyCRNumEventHandler = null;
		keyFirstNameEventHandler = null;
		keyLastNameEventHandler = null;
		keyContactNumEventHandler = null;
		keyCaseNumEventHandler = null;

	}

}
