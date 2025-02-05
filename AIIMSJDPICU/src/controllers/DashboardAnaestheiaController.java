/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package controllers;

import java.net.URL;
import java.util.Calendar;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.cdac.common.GeneratedEntities.AnethesiaMachineData;
import com.cdac.common.util.Validations;

import application.Main;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import mediatorPattern.ControllerMediator;
import model.PatientSession;
import utility.SaveDevcieParamToDB;
/**
 * This controller is used to Add anaesthesia Details in data base
 * @author Sudeep_Sahoo
 *
 */
public class DashboardAnaestheiaController implements Initializable {

	@FXML
	private Button btnClose;

	@FXML
	private TextField txtPpeak;

	@FXML
	private TextField txtPmean;

	@FXML
	private TextField txtPEEP;

	@FXML
	private TextField txtCircuitO2;

	@FXML
	private TextField txtTVexp;

	@FXML
	private TextField txtMVexp;

	@FXML
	private TextField txtRR;

	@FXML
	private Label lblError;

	@FXML
	private Button btnSave;

	@FXML
	private Label lblTime;

	private Calendar selectedTime;

	public void setSelectedTime(Calendar selectedTime) {
		this.selectedTime = selectedTime;
	}

	@FXML
	private AnchorPane disablePaneMainContent;

	public Label getLblTime() {
		return lblTime;
	}

	private AnethesiaMachineData anethesiaDataObj;// = new
													// AnethesiaMachineData();
	private SaveDevcieParamToDB saveParam = new SaveDevcieParamToDB();

	private EventHandler<MouseEvent> btnCloseHandler;

	private EventHandler<MouseEvent> btnSaveHandler;

	private EventHandler<javafx.scene.input.KeyEvent> enterKeyPressEventHandler;

	private boolean isTertiaryWindow;

	private boolean fromHistoryScreen;

	private static final Logger LOG = Logger.getLogger(DashboardAnaestheiaController.class.getName());

	public void setFromHistoryScreen(boolean fromHistoryScreen) {
		this.fromHistoryScreen = fromHistoryScreen;
	}

	public boolean isTertiaryWindow() {
		return isTertiaryWindow;
	}

	public void setTertiaryWindow(boolean isTertiaryWindow) {
		this.isTertiaryWindow = isTertiaryWindow;
	}

	public void setAnethesiaDataObj(AnethesiaMachineData anethesiaDataObj) {
		this.anethesiaDataObj = anethesiaDataObj;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			ControllerMediator.getInstance().registerDashboardAnaestheiaController(this);
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
						save();
					} catch (Exception e) {
						LOG.error("## Exception occured:", e);
						Main.getInstance().getUtility().showNotification("Error", "Error", "Something went wrong");
					}
				}
			};
			btnSave.addEventHandler(MouseEvent.MOUSE_CLICKED, btnSaveHandler);


			enterKeyPressEventHandler = new EventHandler<javafx.scene.input.KeyEvent>() {

				@Override
				public void handle(javafx.scene.input.KeyEvent event) {

					if (event.getCode().equals(KeyCode.ENTER)) {
						try {
							save();
						} catch (Exception e) {
							LOG.error("## Exception occured:", e);
						}
					}
				}
			};
			txtCircuitO2.addEventHandler(javafx.scene.input.KeyEvent.KEY_RELEASED, enterKeyPressEventHandler);
			txtMVexp.addEventHandler(javafx.scene.input.KeyEvent.KEY_RELEASED, enterKeyPressEventHandler);
			txtPEEP.addEventHandler(javafx.scene.input.KeyEvent.KEY_RELEASED, enterKeyPressEventHandler);
			txtPmean.addEventHandler(javafx.scene.input.KeyEvent.KEY_RELEASED, enterKeyPressEventHandler);
			txtPpeak.addEventHandler(javafx.scene.input.KeyEvent.KEY_RELEASED, enterKeyPressEventHandler);
			txtRR.addEventHandler(javafx.scene.input.KeyEvent.KEY_RELEASED, enterKeyPressEventHandler);
			txtTVexp.addEventHandler(javafx.scene.input.KeyEvent.KEY_RELEASED, enterKeyPressEventHandler);


			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					try {
						txtPpeak.requestFocus();
					} catch (Exception e) {
						LOG.error("## Exception occured:", e);
						Main.getInstance().getUtility().showNotification("Error", "Error", "Something went wrong");
					}
				}
			});
		} catch (Exception e) {
			LOG.error("## Exception occured:", e);
			Main.getInstance().getUtility().showNotification("Error", "Error", "Something went wrong");
		}

	}
/**
 * This method is used to remove Event handlers anf nullify the class level variables
 */
	private void removeEventHandlers() {
		try {
			btnClose.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnCloseHandler);
			btnSave.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnSaveHandler);

			txtCircuitO2.removeEventHandler(javafx.scene.input.KeyEvent.KEY_RELEASED, enterKeyPressEventHandler);
			txtMVexp.removeEventHandler(javafx.scene.input.KeyEvent.KEY_RELEASED, enterKeyPressEventHandler);
			txtPEEP.removeEventHandler(javafx.scene.input.KeyEvent.KEY_RELEASED, enterKeyPressEventHandler);
			txtPmean.removeEventHandler(javafx.scene.input.KeyEvent.KEY_RELEASED, enterKeyPressEventHandler);
			txtPpeak.removeEventHandler(javafx.scene.input.KeyEvent.KEY_RELEASED, enterKeyPressEventHandler);
			txtRR.removeEventHandler(javafx.scene.input.KeyEvent.KEY_RELEASED, enterKeyPressEventHandler);
			txtTVexp.removeEventHandler(javafx.scene.input.KeyEvent.KEY_RELEASED, enterKeyPressEventHandler);

			anethesiaDataObj = null;
			saveParam = null;
			btnCloseHandler = null;
			btnSaveHandler = null;
		} catch (Exception e) {
			LOG.error("## Exception occured:", e);
		}

	}
/**
 * This method is used to close the Anaesthesia Details window
 */
	private void closePopup() {
		try {
			removeEventHandlers();
			if (isTertiaryWindow)
				Main.getInstance().getStageManager().closeTertiaryStage();
			else
				Main.getInstance().getStageManager().closeSecondaryStage();

		} catch (Exception e) {
			LOG.error("## Exception occured:", e);
		}

	}
/**
 * This method is used to fill the Anaesthesia Params
 * @param obj
 */
	public void autopopulateTextboxes(AnethesiaMachineData obj) {
		try {
			txtPpeak.setText(obj.getpPeak());
			txtPmean.setText(obj.getpMean());
			txtPEEP.setText(obj.getPeep());
			txtCircuitO2.setText(obj.getCircuitO2());
			txtTVexp.setText(obj.getTvExp());
			txtMVexp.setText(obj.getMvExp());
			txtRR.setText(obj.getRr());
		} catch (Exception e) {
			LOG.error("## Exception occured:", e);

		}

	}
/**
 * This method is used to Hide the Page loaded
 */
	public void hideDisablePane() {
		try {
			disablePaneMainContent.setVisible(false);
		} catch (Exception e) {
			LOG.error("## Exception occured:", e);
		}

	}
/**
 * This Method is used to Save Anaesthesia details in data base
 */
	private void save() {
		try {

			if (txtPpeak.getText() != null && !txtPpeak.getText().trim().isEmpty()
					|| txtPmean.getText() != null && !txtPmean.getText().trim().isEmpty()
					|| txtPEEP.getText() != null && !txtPEEP.getText().trim().isEmpty()
					|| txtCircuitO2.getText() != null && !txtCircuitO2.getText().trim().isEmpty()
					|| txtTVexp.getText() != null && !txtTVexp.getText().trim().isEmpty()
					|| txtMVexp.getText() != null && !txtMVexp.getText().trim().isEmpty()
					|| txtRR.getText() != null && !txtRR.getText().trim().isEmpty()) {
				if (isValidForm()) {
					disablePaneMainContent.setVisible(true);

					anethesiaDataObj.setpPeak(txtPpeak.getText());
					anethesiaDataObj.setpMean(txtPmean.getText());
					anethesiaDataObj.setPeep(txtPEEP.getText());
					anethesiaDataObj.setCircuitO2(txtCircuitO2.getText());
					anethesiaDataObj.setTvExp(txtTVexp.getText());
					anethesiaDataObj.setMvExp(txtMVexp.getText());
					anethesiaDataObj.setRr(txtRR.getText());
					anethesiaDataObj.setTimeStamp(selectedTime.getTime());
					anethesiaDataObj.setCaseId(PatientSession.getInstance().getPatientCase().getCaseId());
					anethesiaDataObj.setDeviceId(null);

					saveParam.saveAnesData(anethesiaDataObj, true);

					if (fromHistoryScreen)
						ControllerMediator.getInstance().getGridHistoricalViewController().fillDeviceGridCell("anes",
								anethesiaDataObj, selectedTime);

					ControllerMediator.getInstance().getMainController().getDrawTabbedCenter()
							.fillDeviceGridCell("anes", anethesiaDataObj, selectedTime);
					closePopup();
				} else {
					lblError.setVisible(true);
				}
			} else {
				lblError.setText("* Please enter atleast one parameter to save.");
				lblError.setVisible(true);
			}

		} catch (Exception e) {
			LOG.error("## Exception occured:", e);
		}
	}
/**
 * This method is used to validate the Anaesthesia Form
 * @return
 */
	private boolean isValidForm() {
		try {

			if (txtPpeak.getText() != null && !txtPpeak.getText().trim().isEmpty()) {
				if (!Validations.isDigit(txtPpeak.getText())) {
					lblError.setText("Please enter a valid Ppeak value");
					return false;
				}
				if (!Validations.maxLength(txtPpeak.getText(), 7)) {
					lblError.setText("Please enter Ppeak value upto 7 digit.");
					return false;
				}
			}

			if (txtPmean.getText() != null && !txtPmean.getText().trim().isEmpty()) {

				if (!Validations.isDigit(txtPmean.getText())) {
					lblError.setText("Please enter a valid Pmean value.");
					return false;
				}
				if (!Validations.maxLength(txtPmean.getText(), 7)) {
					lblError.setText("Please enter Pmean value upto 7 digit.");
					return false;
				}
			}

			if (txtPEEP.getText() != null && !txtPEEP.getText().trim().isEmpty()) {
				if (!Validations.isDigit(txtPEEP.getText())) {
					lblError.setText("Please enter a valid PEEP value .");
					return false;
				}
				if (!Validations.maxLength(txtPEEP.getText(), 7)) {
					lblError.setText("Please enter PEEP value upto 7 digit.");
					return false;
				}
			}

			if (txtCircuitO2.getText() != null && !txtCircuitO2.getText().trim().isEmpty()) {
				if (!Validations.isDigit(txtCircuitO2.getText())) {
					lblError.setText("Please enter a valid CircuitO2 value .");
					return false;
				}

				if (!Validations.maxLength(txtCircuitO2.getText(), 7)) {
					lblError.setText("Please enter CircuitO2 value upto 7 digit.");
					return false;
				}
			}

			if (txtTVexp.getText() != null && !txtTVexp.getText().trim().isEmpty()) {
				if (!Validations.isDigit(txtTVexp.getText())) {
					lblError.setText("Please enter a valid TVexp value.");
					return false;
				}
				if (!Validations.maxLength(txtTVexp.getText(), 7)) {
					lblError.setText("Please enter TVexp value upto 7 digit.");
					return false;
				}
			}
			if (txtMVexp.getText() != null && !txtMVexp.getText().trim().isEmpty()) {
				if (!Validations.isDigit(txtMVexp.getText())) {
					lblError.setText("Please enter a valid txtMVexp value.");
					return false;
				}
				if (!Validations.maxLength(txtMVexp.getText(), 7)) {
					lblError.setText("Please enter txtMVexp value upto 7 digit.");
					return false;
				}
			}

			if (txtRR.getText() != null && !txtRR.getText().trim().isEmpty()) {
				if (!Validations.isDigit(txtRR.getText())) {
					lblError.setText("Please enter a valid RR value .");
					return false;
				}
				if (!Validations.maxLength(txtRR.getText(), 7)) {
					lblError.setText("Please enter RR value upto 7 digit.");
					return false;
				}
			}

			return true;

		} catch (Exception e) {
			LOG.error("## Exception occured:", e);
			return false;
		}
	}

	// public boolean isValidForm() {
	// if (txtPpeak.getText() != null && !txtPpeak.getText().trim().isEmpty()
	// && !Validations.isDigit(txtPpeak.getText())) {
	// lblError.setText("Please enter a valid Ppeak value");
	// return false;
	// }
	// if (txtPmean.getText() != null && !txtPmean.getText().trim().isEmpty()
	// && !Validations.isDigit(txtPmean.getText())) {
	// lblError.setText("Please enter a valid Pmean value");
	// return false;
	// }
	//
	// if (txtPEEP.getText() != null && !txtPEEP.getText().trim().isEmpty()
	// && !Validations.isDigit(txtPEEP.getText())) {
	// lblError.setText("Please enter a valid PEEP value");
	// return false;
	// }
	//
	// if (txtCircuitO2.getText() != null &&
	// !txtCircuitO2.getText().trim().isEmpty()
	// && !Validations.isDigit(txtCircuitO2.getText())) {
	// lblError.setText("Please enter a CircuitO2 value");
	// return false;
	// }
	//
	// if (txtTVexp.getText() != null && !txtTVexp.getText().trim().isEmpty()
	// && !Validations.isDigit(txtTVexp.getText())) {
	// lblError.setText("Please enter a valid TVexp value");
	// return false;
	// }
	// if (txtMVexp.getText() != null && !txtMVexp.getText().trim().isEmpty()
	// && !Validations.isDigit(txtMVexp.getText())) {
	// lblError.setText("Please enter a valid MVexp value");
	// return false;
	// }
	// if (txtRR.getText() != null && !txtRR.getText().trim().isEmpty() &&
	// !Validations.isDigit(txtRR.getText())) {
	// lblError.setText("Please enter a valid RR value");
	// return false;
	// }
	//
	// return true;
	// }
}
