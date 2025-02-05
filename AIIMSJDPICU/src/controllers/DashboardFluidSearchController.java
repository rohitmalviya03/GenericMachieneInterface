/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.cdac.common.GeneratedEntities.Fluid;
import com.cdac.common.GeneratedEntities.Patient;
import com.cdac.common.GeneratedEntities.Patientcase;
import com.cdac.common.util.Validations;

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
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import mediatorPattern.ControllerMediator;
import model.PatientSession;
import scala.reflect.api.Trees.TryExtractor;
import services.SearchFluidService;
import utility.DrawTabbedCenter;

/**
 * This Controller is used to Search fluids from data base based on fluid name
 * entered by user and add the selected fluid to the Fluid panel in main
 * Dashboard
 *
 * @author Sudeep_Sahoo
 *
 */
public class DashboardFluidSearchController implements Initializable {

	@FXML
	private Button btnClose;

	@FXML
	private TextField txtFluidName;

	@FXML
	private Button btnSave;

	@FXML
	private Label lblError;

	@FXML
	private ListView<String> fluidSearchList;
	private List<String> searchFluidList;
	private String selectedSearchVal = "";
	private SearchFluidService searchFluidService;
	private List<Fluid> searchFluidListDB;
	private Patient patient;
	private Patientcase patientCase;

	private EventHandler<MouseEvent> btnSaveHandler;

	private EventHandler<MouseEvent> btnCloseHandler;

	private EventHandler<WorkerStateEvent> searchFluidServiceFailedHandler;

	private EventHandler<WorkerStateEvent> searchFluidServiceSuccessHandler;

	private ChangeListener<Boolean> fluidNameChangeListener;

	private ChangeListener<String> txtFluidNameChangeListener;

	private ChangeListener<String> fluidSearchListChangeListener;

	private EventHandler<javafx.scene.input.KeyEvent> enterKeyPressEventHandler;

	private static final Logger LOG = Logger.getLogger(DashboardFluidSearchController.class.getName());

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			if (PatientSession.getInstance().getPatient() != null)
				patient = PatientSession.getInstance().getPatient();
			if (PatientSession.getInstance().getPatientCase() != null)
				patientCase = PatientSession.getInstance().getPatientCase();
			txtFluidNameChangeListener = new ChangeListener<String>() {

				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					try {
						if (Validations.isAlphaNumericWithSpaceAndSpecialCharacter(txtFluidName.getText())) {
							if (newValue.length() >= 5 && !newValue.isEmpty() && !newValue.equals("")) {
								if (!newValue.equalsIgnoreCase(selectedSearchVal)) {
									searchFluidList = new ArrayList<String>();
									fluidSearchList.getItems().clear();
									fluidSearchList.getItems().addAll(searchFluidList);
									callSearchFluidService(newValue.trim());
								}

							} else if (newValue.length() < 5) {
								fluidSearchList.getSelectionModel().clearSelection();
								fluidSearchList.setVisible(false);
							}
						} else {

							fluidSearchList.setVisible(false);
						}
					} catch (Exception e) {
						LOG.error("## Exception occured:", e);
					}

				}
			};
			txtFluidName.textProperty().addListener(txtFluidNameChangeListener);
			fluidNameChangeListener = new ChangeListener<Boolean>() {
				@Override
				public void changed(ObservableValue<? extends Boolean> observable, Boolean oldPropertyValue,
						Boolean newPropertyValue) {
					try {
						if (!newPropertyValue) {

							fluidSearchList.setVisible(false);
						}
					} catch (Exception e) {
						LOG.error("## Exception occured:", e);
					}
				}
			};
			txtFluidName.focusedProperty().addListener(fluidNameChangeListener);
			btnSaveHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent event) {
					try {
						addNewFluid();
					} catch (Exception e) {
						LOG.error("## Exception occured:", e);

					}
				}
			};
			btnSave.addEventHandler(MouseEvent.MOUSE_CLICKED, btnSaveHandler);

			enterKeyPressEventHandler = new EventHandler<javafx.scene.input.KeyEvent>() {

				@Override
				public void handle(javafx.scene.input.KeyEvent event) {

					if (event.getCode().equals(KeyCode.ENTER)) {
						try {
							addNewFluid();
						} catch (Exception e) {
							LOG.error("## Exception occured:", e);
						}
					}
				}
			};
			txtFluidName.addEventHandler(javafx.scene.input.KeyEvent.KEY_RELEASED, enterKeyPressEventHandler);

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
			fluidSearchListChangeListener = new ChangeListener<String>() {

				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					try {
						if (newValue != null) {

							selectedSearchVal = newValue;
							txtFluidName.setText(selectedSearchVal);
							fluidSearchList.setVisible(false);

						}
					} catch (Exception e) {
						LOG.error("## Exception occured:", e);
					}
				}

			};
			fluidSearchList.getSelectionModel().selectedItemProperty().addListener(fluidSearchListChangeListener);
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					txtFluidName.requestFocus();
				}
			});
		} catch (Exception e) {
			LOG.error("## Exception occured:", e);
			Main.getInstance().getUtility().showNotification("Error", "Error", "Something went wrong");
		}

	}

	/**
	 * This method is used to remove all event handlers
	 */
	private void removeEventHandlers() {
		try {
			btnSave.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnSaveHandler);
			btnClose.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnCloseHandler);
			txtFluidName.textProperty().removeListener(txtFluidNameChangeListener);
			txtFluidName.focusedProperty().removeListener(fluidNameChangeListener);
			fluidSearchList.getSelectionModel().selectedItemProperty().removeListener(fluidSearchListChangeListener);
			txtFluidName.removeEventHandler(javafx.scene.input.KeyEvent.KEY_RELEASED, enterKeyPressEventHandler);
		} catch (Exception e) {
			LOG.error("## Exception occured:", e);
		}

	}
	/**
	 * This method is used to add New Fluid to Dashboard
	 */
	private void addNewFluid() {
		try {

			if (!txtFluidName.getText().trim().isEmpty() && Validations.maxLength(txtFluidName.getText(), 999)) {
				lblError.setVisible(false);
				closePopup();
				ControllerMediator.getInstance().getMainController().getDrawTabbedCenter()
						.addNewGridRow(txtFluidName.getText() + "|0.00ml", DrawTabbedCenter.FLUID_GRID_NAME);
			} else {
				lblError.setText("Please enter Fluid Name less than 1000 characters.");
				lblError.setVisible(true);
			}

		} catch (Exception e) {
			LOG.error("## Exception occured:", e);
		}
	}

	/**
	 * This method is used to Close the Search fluid window
	 */
	private void closePopup() {
		try {
			removeEventHandlers();
			Main.getInstance().getStageManager().closeSecondaryStage();
		} catch (Exception e) {
			LOG.error("## Exception occured:", e);
		}

	}

	/**
	 * This method is used to call SearchFluidService which searches for fluid
	 * with matching searchCriteria
	 *
	 * @param searchCriteria
	 */
	private void callSearchFluidService(String searchCriteria) {
		try {

			searchFluidService = SearchFluidService.getInstance(searchCriteria, patient.getPatientId(),
					patientCase.getCaseId());
			searchFluidService.restart();

			searchFluidServiceSuccessHandler = new EventHandler<WorkerStateEvent>() {

				@Override
				public void handle(WorkerStateEvent event) {
					try {
						searchFluidList = new ArrayList<String>();
						searchFluidListDB = searchFluidService.getSearchFluidList();
						if (searchFluidListDB != null) {
							for (Fluid obj : searchFluidListDB) {
								if (obj != null) {
									searchFluidList.add(obj.getFluidName());
								}
							}
							fluidSearchList.getItems().clear();
							fluidSearchList.getItems().addAll(searchFluidList);
							if (searchFluidListDB.size() != 0) {
								fluidSearchList.setVisible(true);
							} else {
								fluidSearchList.setVisible(false);
							}

						}
						searchFluidService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
								searchFluidServiceSuccessHandler);
						searchFluidService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
								searchFluidServiceFailedHandler);
					} catch (Exception e) {
						LOG.error("## Exception occured:", e);
					}
				}
			};

			searchFluidService.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
					searchFluidServiceSuccessHandler);

			searchFluidServiceFailedHandler = new EventHandler<WorkerStateEvent>() {

				@Override
				public void handle(WorkerStateEvent event) {
					try {
						Main.getInstance().getUtility().showNotification("Error", "Error",
								searchFluidService.getException().getMessage());
						searchFluidService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
								searchFluidServiceSuccessHandler);
						searchFluidService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
								searchFluidServiceFailedHandler);
					} catch (Exception e) {
						LOG.error("## Exception occured:", e);
					}
				}
			};

			searchFluidService.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED, searchFluidServiceFailedHandler);

		} catch (Exception e) {
			LOG.error("## Exception occured:", e);
		}
	}

}
