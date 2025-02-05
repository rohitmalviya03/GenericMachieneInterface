/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.cdac.common.GeneratedEntities.Fluidvalue;
import com.cdac.common.GeneratedEntities.Patient;
import com.cdac.common.GeneratedEntities.Patientcase;
import com.cdac.common.util.Validations;

import application.Main;
import javafx.application.Platform;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import mediatorPattern.ControllerMediator;
import model.PatientSession;
import services.SaveAndGetTotalFluidService;

/**
 * This controller is used to add Fluids from Main Dashboard
 *
 * @author Sudeep_Sahoo
 *
 */
public class DashboardFluidController implements Initializable {

	@FXML
	private Label lblTime;

	@FXML
	private Label lblSelectetMethod;

	@FXML
	private Button btnClose;

	@FXML
	private Label lblFluidName;

	@FXML
	private VBox vbVolume;

	@FXML
	private Label lblVolume;

	@FXML
	private TextField txtVolume;

	@FXML
	private Label lblError;

	@FXML
	private Button btnSave;

	@FXML
	private AnchorPane disablePaneMainContent;

	@FXML
	private VBox vbVolumeLeft;

	@FXML
	private TextField txtVolumeLeft;

	private Integer selectedFluidId;
	private Boolean isStop;

	private Patient patient;
	private Patientcase patientCase;
	private Fluidvalue selectedFluid;

	private Calendar selectedTime;

	public void setSelectedTime(Calendar selectedTime) {
		this.selectedTime = selectedTime;
	}

	private boolean isTertiaryWindow;

	public void setTertiaryWindow(boolean isTertiaryWindow) {
		this.isTertiaryWindow = isTertiaryWindow;
	}

	private boolean fromHistoryScreen;

	public void setFromHistoryScreen(boolean fromHistoryScreen) {
		this.fromHistoryScreen = fromHistoryScreen;
	}

	public boolean isTertiaryWindow() {
		return isTertiaryWindow;
	}

	private EventHandler<MouseEvent> btnCloseHandler;

	private EventHandler<MouseEvent> btnSaveHandler;

	private EventHandler<WorkerStateEvent> addFluidServiceSuccessHandler;

	private EventHandler<WorkerStateEvent> addFluidServiceFailedHandler;

	private EventHandler<javafx.scene.input.KeyEvent> enterKeyPressEventHandler;

	public Label getLblTime() {
		return lblTime;
	}

	public Label getLblFluidName() {
		return lblFluidName;
	}

	public void setSelectedFluid(Fluidvalue selectedFluid) {
		this.selectedFluid = selectedFluid;
	}

	public void setSelectedFluidId(Integer selectedFluidId) {
		this.selectedFluidId = selectedFluidId;
	}

	public void setIsStop(Boolean isStop) {
		this.isStop = isStop;
	}

	private static final Logger LOG = Logger.getLogger(DashboardFluidController.class.getName());

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		try {
			ControllerMediator.getInstance().registerDashboardFluidController(this);
			if (PatientSession.getInstance().getPatient() != null)
				patient = PatientSession.getInstance().getPatient();
			if (PatientSession.getInstance().getPatientCase() != null)
				patientCase = PatientSession.getInstance().getPatientCase();
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
			btnSaveHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent event) {
					try {
						saveFluid();
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
							saveFluid();
						} catch (Exception e) {
							LOG.error("## Exception occured:", e);
						}
					}
				}
			};
			txtVolume.addEventHandler(javafx.scene.input.KeyEvent.KEY_RELEASED, enterKeyPressEventHandler);
			txtVolumeLeft.addEventHandler(javafx.scene.input.KeyEvent.KEY_RELEASED, enterKeyPressEventHandler);

		} catch (Exception e) {
			LOG.error("## Exception occured:", e);
		}

	}

	/**
	 * This method is used to Check whether user wants to start a Fluid or Stop
	 * a Fluid
	 */
	public void checkStartOrStop() {
		try {
			if (isStop) {
				btnSave.setText("Stop");
				vbVolume.setDisable(true);
				vbVolumeLeft.setVisible(true);
				txtVolume.setText(selectedFluid.getInitialVolume() + "");

				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						txtVolumeLeft.requestFocus();
					}
				});
			} else {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {

						txtVolume.requestFocus();
					}
				});
			}
		} catch (Exception e) {
			LOG.error("## Exception occured:", e);
		}
	}

	/**
	 * This method is used to remove all event handlers and nullify the class
	 * level variables
	 */
	private void removeEventHandlers() {

		try {
			btnClose.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnCloseHandler);
			btnSave.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnSaveHandler);
			txtVolume.removeEventHandler(javafx.scene.input.KeyEvent.KEY_RELEASED, enterKeyPressEventHandler);
			txtVolumeLeft.removeEventHandler(javafx.scene.input.KeyEvent.KEY_RELEASED, enterKeyPressEventHandler);
			selectedFluidId = null;
			isStop = null;
			patient = null;
			patientCase = null;
			selectedFluid = null;
			btnCloseHandler = null;
			btnSaveHandler = null;
			addFluidServiceSuccessHandler = null;
			addFluidServiceFailedHandler = null;

		} catch (Exception e) {
			LOG.error("## Exception occured:", e);
		}
	}

	/**
	 * This method is used to close Fluid Window
	 */
	private void closePopup() {
		try {
			removeEventHandlers();
			if (isTertiaryWindow) {
				Main.getInstance().getStageManager().closeTertiaryStage();
			} else {
				Main.getInstance().getStageManager().closeSecondaryStage();
			}
		} catch (Exception e) {
			LOG.error("## Exception occured:", e);
		}

	}

	/**
	 * This method is used to validate the Fluid form
	 *
	 * @return
	 */
	private boolean validateStopForm() {

		try {

			if (!Validations.isDigit(txtVolume.getText())) {
				lblError.setText("*Please enter a valid volume value");
				return false;
			}
			if (!Validations.decimalsUpto2places(txtVolume.getText())) {
				lblError.setText("*Please enter volume upto 4 digits and" + "\n" + " upto 2 decimal only");
				return false;
			}

			if (!Validations.nonZeroBigDecimal(txtVolume.getText())) {
				lblError.setText("*Please enter volume value more than 0");
				return false;
			}

			if (isStop) {
				if (txtVolumeLeft == null || txtVolumeLeft.getText().trim().isEmpty()) {
					lblError.setVisible(true);
					lblError.setText("*Please enter Volume left.");
					return false;
				}
				if (!Validations.isDigit(txtVolumeLeft.getText())) {
					lblError.setText("*Please enter a valid Volume left value");
					return false;
				}
				if (!Validations.decimalsUpto2places(txtVolumeLeft.getText())) {
					lblError.setText("*Please enter Volume left upto 4 digits and" + "\n" + " upto 2 decimal only");
					return false;
				}

				if (txtVolumeLeft != null && !txtVolumeLeft.getText().trim().isEmpty()) {
					Float volumeInfused = selectedFluid.getInitialVolume() - Float.parseFloat(txtVolumeLeft.getText());
					if (volumeInfused < 0) {
						lblError.setVisible(true);
						lblError.setText("*Initial Volume should be greater " + "\n" + " than Volume left.");
						return false;
					}
				}
			}

			return true;

		} catch (Exception e) {
			LOG.error("## Exception occured:", e);
			return false;
		}
	}

	/**
	 * This method is used to call Save fluid service and saves the Fluid
	 * details in Data base
	 */
	private void saveFluid() {
		try {

			if (txtVolume.getText() != null && !txtVolume.getText().trim().isEmpty()) {

				if (validateStopForm()) {
					lblError.setVisible(false);
					disablePaneMainContent.setVisible(true);

					Fluidvalue fluid = new Fluidvalue();
					fluid.setVolumeUnit("ml");
					fluid.setPatientId(patient.getPatientId());
					fluid.setCaseId(patientCase.getCaseId());
					if (!isStop) {
						fluid.setFluidId(selectedFluidId);
						fluid.setFluidName(lblFluidName.getText());
						fluid.setInitialVolume(Float.valueOf(txtVolume.getText()));
						fluid.setStartTime(selectedTime.getTime());

					} else {

						fluid = selectedFluid;
						Float volumeInfused = selectedFluid.getInitialVolume()
								- Float.parseFloat(txtVolumeLeft.getText());
						if (volumeInfused >= 0) {
							fluid.setVolume(volumeInfused);
						}
						fluid.setFinishTime(selectedTime.getTime());
						fluid.setIsCompleted(true);
					}
					List<String> fluidList = new ArrayList<String>();
					fluidList.add(lblFluidName.getText());
					SaveAndGetTotalFluidService addFluidService = SaveAndGetTotalFluidService.getInstance(fluid,
							PatientSession.getInstance().getPatientCase().getCaseId(), fluidList, isStop);
					addFluidService.restart();

					addFluidServiceSuccessHandler = new EventHandler<WorkerStateEvent>() {

						@Override
						public void handle(WorkerStateEvent event) {

							try {
								Map<String, Fluidvalue> returnMap = addFluidService.getReturnMap();
								Fluidvalue returnedObj = returnMap.get("Success");

								if (returnedObj != null) {
									if (returnedObj.getFinishTime() == null) {
										Calendar cal = Calendar.getInstance();
										cal.setTime(returnedObj.getStartTime());

										ControllerMediator.getInstance().getMainController().getDrawTabbedCenter()
												.getFluidstartedMap().put(returnedObj.getFluidName(), cal);
										// .put(returnedObj.getFluidName(),
										// sdf.format(returnedObj.getStartTime()));

										if (fromHistoryScreen)
											ControllerMediator.getInstance().getGridHistoricalViewController()
													.fillFluidGridCell("start", returnedObj.getFluidName(), cal,
															String.valueOf(returnedObj.getInitialVolume()), "");

										ControllerMediator.getInstance().getMainController().getDrawTabbedCenter()
												.fillFluidGridCell("start", returnedObj.getFluidName(), cal,
														String.valueOf(returnedObj.getInitialVolume()), "");

									} else {
										Calendar cal = Calendar.getInstance();
										cal.setTime(returnedObj.getFinishTime());

										ControllerMediator.getInstance().getMainController().getDrawTabbedCenter()
												.getFluidstartedMap().remove(returnedObj.getFluidName());
										if (isStop) {
											Map<String, String> returnTotalsMap = addFluidService.getFluidVolume();

											if (fromHistoryScreen)
												ControllerMediator.getInstance().getGridHistoricalViewController()
														.fillFluidGridCell("stop", returnedObj.getFluidName(), cal,
																String.valueOf(returnedObj.getVolume()),
																returnTotalsMap.get(returnedObj.getFluidName()));

											ControllerMediator.getInstance().getMainController().getDrawTabbedCenter()
													.fillFluidGridCell("stop", returnedObj.getFluidName(), cal,
															String.valueOf(returnedObj.getVolume()),
															returnTotalsMap.get(returnedObj.getFluidName()));
										}

										// List<String> fluidList = new
										// ArrayList<String>();
										// fluidList.add(returnedObj.getFluidName());
										// getFluidTotalsListService =
										// GetTotalFluidVolumeService
										// .getInstance(PatientSession.getInstance().getPatientCase().getCaseId(),
										// fluidList);
										// getFluidTotalsListService.restart();

										// getFluidTotalsListServiceSuccessHandler
										// = new
										// EventHandler<WorkerStateEvent>() {
										//
										// @Override
										// public void handle(WorkerStateEvent
										// event) {
										// Map<String, String> returnTotalsMap =
										// getFluidTotalsListService.getFluidVolume();
										//
										// ControllerMediator.getInstance().getMainController().getDrawTabbedCenter()
										// .fillFluidGridCell("stop",
										// returnedObj.getFluidName(),
										// sdf.format(returnedObj.getFinishTime()),
										// String.valueOf(returnedObj.getVolume()),
										// returnTotalsMap.get(returnedObj.getFluidName()));
										// getFluidTotalsListService.removeEventHandler(
										// WorkerStateEvent.WORKER_STATE_SUCCEEDED,
										// getFluidTotalsListServiceSuccessHandler);
										// getFluidTotalsListService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
										// getFluidTotalsListServiceFailedHandler);
										// }
										//
										// };
										//
										// getFluidTotalsListService.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
										// getFluidTotalsListServiceSuccessHandler);
										//
										// getFluidTotalsListServiceFailedHandler
										// = new
										// EventHandler<WorkerStateEvent>() {
										//
										// @Override
										// public void handle(WorkerStateEvent
										// event) {
										// Main.getInstance().getUtility().showNotification("Error",
										// "Error",
										// getFluidTotalsListService.getException().getMessage());
										// getFluidTotalsListService.removeEventHandler(
										// WorkerStateEvent.WORKER_STATE_SUCCEEDED,
										// getFluidTotalsListServiceSuccessHandler);
										// getFluidTotalsListService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
										// getFluidTotalsListServiceFailedHandler);
										// }
										//
										// };
										//
										// getFluidTotalsListService.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
										// getFluidTotalsListServiceFailedHandler);

									}
								}
								disablePaneMainContent.setVisible(false);
								addFluidService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
										addFluidServiceSuccessHandler);
								addFluidService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
										addFluidServiceFailedHandler);
								closePopup();
								Main.getInstance().getUtility().showNotification("Information", "Success",
										"Fluid saved successfully!");
							} catch (Exception e) {
								LOG.error("## Exception occured:", e);
								Main.getInstance().getUtility().showNotification("Error", "Error",
										"Something went wrong");
							}
						}

					};

					addFluidService.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
							addFluidServiceSuccessHandler);

					addFluidServiceFailedHandler = new EventHandler<WorkerStateEvent>() {

						@Override
						public void handle(WorkerStateEvent event) {
							try {
								Main.getInstance().getUtility().showNotification("Error", "Error",
										addFluidService.getException().getMessage());
								disablePaneMainContent.setVisible(false);
								addFluidService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
										addFluidServiceSuccessHandler);
								addFluidService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
										addFluidServiceFailedHandler);
							} catch (Exception e) {
								LOG.error("## Exception occured:", e);
							}
						}
					};

					addFluidService.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED, addFluidServiceFailedHandler);
				}

			} else {
				lblError.setVisible(true);
				lblError.setText("*Please enter Volume.");

			}

		} catch (Exception e) {
			LOG.error("## Exception occured:", e);
		}
	}

}
