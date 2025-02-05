/*******************************************************************************
 * © 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
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

import application.Main;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import mediatorPattern.ControllerMediator;
import model.DashboardTestSession;
import model.MasterDataSession;

/**
 * This controller is used to add New Tests to the Dashboard Test Panel
 *
 * @author Sudeep_Sahoo
 *
 */
public class DashboardAddNewTestController implements Initializable {

	@FXML
	private Button btnClose;

	@FXML
	private Button btnAddTest;

	@FXML
	private ChoiceBox<String> choiceTestName;

	@FXML
	private Label lblErr;

	private List<String> testList;

	private EventHandler<MouseEvent> btnCloseHandler;

	private EventHandler<MouseEvent> btnAddTestHandler;

	private EventHandler<javafx.scene.input.KeyEvent> enterKeyPressEventHandler;

	private static final Logger LOG = Logger.getLogger(DashboardAddNewTestController.class.getName());

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {

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
			btnAddTestHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent event) {
					try {
						addNewTest();
					} catch (Exception e) {
						LOG.error("## Exception occured:", e);
						Main.getInstance().getUtility().showNotification("Error", "Error", "Something went wrong");

					}
				}
			};
			btnAddTest.addEventHandler(MouseEvent.MOUSE_CLICKED, btnAddTestHandler);

			enterKeyPressEventHandler = new EventHandler<javafx.scene.input.KeyEvent>() {

				@Override
				public void handle(javafx.scene.input.KeyEvent event) {

					if (event.getCode().equals(KeyCode.ENTER)) {
						try {
							addNewTest();
						} catch (Exception e) {
							LOG.error("## Exception occured:", e);
						}
					}
				}
			};
			choiceTestName.addEventHandler(javafx.scene.input.KeyEvent.KEY_RELEASED, enterKeyPressEventHandler);

			getListOfTest();
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					try {
						choiceTestName.requestFocus();
					} catch (Exception e) {
						LOG.error("## Exception occured:", e);
					}
				}
			});
		} catch (Exception e) {
			Main.getInstance().getUtility().showNotification("Error", "Error", "Something went wrong");
			LOG.error("## Exception occured:", e);
		}

	}

	/**
	 * This method is used to remove all listeners
	 */
	private void removeEventHandlers() {
		try {
			btnClose.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnCloseHandler);
			btnAddTest.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnAddTestHandler);
			choiceTestName.removeEventHandler(javafx.scene.input.KeyEvent.KEY_RELEASED, enterKeyPressEventHandler);
		} catch (Exception e) {
			LOG.error("## Exception occured:", e);
		}

	}

	/**
	 * This method closes Add Test screen
	 */
	private void closePopup() {
		try {
			removeEventHandlers();
			Main.getInstance().getStageManager().closeSecondaryStage();
		} catch (Exception e) {
			btnClose.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnCloseHandler);
			btnAddTest.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnAddTestHandler);
		}

	}

	/**
	 * This method is used to add New test to Dashboard
	 */

	private void addNewTest() {
		try {

			List<String> testListAdded = new ArrayList<String>();
			boolean isExist = false;
			if ((choiceTestName.getSelectionModel().getSelectedIndex() != 0)) {
				lblErr.setVisible(false);
				if (DashboardTestSession.getInstance() != null) {

					testListAdded = DashboardTestSession.getInstance().getTestList();
					if (testListAdded != null) {
						for (String testName : testListAdded) {
							if (testName.equalsIgnoreCase(choiceTestName.getSelectionModel().getSelectedItem())) {
								isExist = true;
								break;
							}
						}
					}

				}
				if (!isExist) {
					ControllerMediator.getInstance().getMainController().getDrawTabbedCenter()
							.addNewTestGrid(choiceTestName.getSelectionModel().getSelectedItem());
					testListAdded.add(choiceTestName.getSelectionModel().getSelectedItem());
					DashboardTestSession.getInstance().setTestList(testListAdded);
					closePopup();
				} else {
					lblErr.setVisible(true);
					lblErr.setText("* Selected Test has already been added.");
				}

			} else {
				lblErr.setVisible(true);
				lblErr.setText("* Please select Test name to add.");
			}

		} catch (Exception e) {
			LOG.error("## Exception occured:", e);
		}
	}

	/**
	 * This method call getTestListService and fetches available tests
	 */
	private void getListOfTest() {
		try {

			testList = new ArrayList<>();
			testList.add("Select");
			// if(MasterDataSession.getInstance().getMasterDataMap().get("TestFav")!=null)
			if (MasterDataSession.getInstance().getTestParamDataMap().get("TestParam") != null) {
				testList.addAll(MasterDataSession.getInstance().getTestParamDataMap().get("TestParam").keySet());

			}
			List<String> testListFiltered = new ArrayList<String>();
			for (String testName : testList) {
				if (!testName.equalsIgnoreCase("TTE") && !testName.equalsIgnoreCase("TEE")) {
					testListFiltered.add(testName);
				}

			}

			choiceTestName.getItems().clear();
			choiceTestName.getItems().addAll(testListFiltered);
			choiceTestName.getSelectionModel().select(0);
			// getTestListService = GetTestListService.getInstance();
			// getTestListService.restart();
			//
			// getTestListServiceSuccessHandler = new
			// EventHandler<WorkerStateEvent>() {
			//
			// @Override
			// public void handle(WorkerStateEvent event) {
			// listOfTest = getTestListService.getListOfTest();
			// if (listOfTest != null) {
			// for (Investigationstests testObj : listOfTest) {
			// testList.add(testObj.getTestName());
			// }
			// choiceTestName.setItems(testList);
			// choiceTestName.getSelectionModel().select(0);
			//
			// }
			// getTestListService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
			// getTestListServiceSuccessHandler);
			// getTestListService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
			// getTestListServiceFailedHandler);
			// }
			//
			// };
			//
			// getTestListService.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
			// getTestListServiceSuccessHandler);
			//
			// getTestListServiceFailedHandler = new
			// EventHandler<WorkerStateEvent>() {
			//
			// @Override
			// public void handle(WorkerStateEvent event) {
			// Main.getInstance().getUtility().showNotification("Error",
			// "Error",
			// getTestListService.getException().getMessage());
			// getTestListService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
			// getTestListServiceSuccessHandler);
			// getTestListService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
			// getTestListServiceFailedHandler);
			// }
			//
			// };
			//
			// getTestListService.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
			// getTestListServiceFailedHandler);

		} catch (Exception e) {
			LOG.error("## Exception occured:", e);
		}
	}
}
