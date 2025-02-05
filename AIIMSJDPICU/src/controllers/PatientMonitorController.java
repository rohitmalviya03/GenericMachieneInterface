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

import com.cdac.common.GeneratedEntities.PatientMonitorData;
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
 * PatientMonitorController.java<br>
 * Purpose: This class contains logic to handle inputs from Patient Monitor popup screen
 *
 * @author Rohit_Sharma41
 *
 */
public class PatientMonitorController implements Initializable {

	@FXML
	private Button btnClose;

	@FXML
	private TextField txtHR;

	@FXML
	private TextField txtIBPSys;

	@FXML
	private TextField txtIBPDia;

	@FXML
	private TextField txtSpO2;

	@FXML
	private TextField txtEtCO2;

	@FXML
	private TextField txtTemp1;

	@FXML
	private TextField txtTemp2;

	@FXML
	private Label lblError;
	@FXML
	private Label lblTime;

	@FXML
	private Label temp1lbl;

	@FXML
	private Label temp2lbl;

	@FXML
	private Button btnSave;

	private Calendar selectedTime;

	public void setSelectedTime(Calendar selectedTime) {
		this.selectedTime = selectedTime;
	}

	@FXML
	private AnchorPane disablePaneMainContent;

	private PatientMonitorData patientMonitorData;// = new PatientMonitorData();

	private EventHandler<MouseEvent> btnCloseHandler;

	private EventHandler<MouseEvent> btnSaveHandler;

	private EventHandler<javafx.scene.input.KeyEvent> enterKeyPressEventHandler;

	private boolean isTertiaryWindow;

	private boolean fromHistoryScreen;

	@FXML
	private TextField txtBis;

	@FXML
	private Label bisLbl;

	@FXML
	private TextField txtETAgent;

	@FXML
	private TextField txtMac;

	@FXML
	private TextField txtIBPmean;
	
	@FXML
	private TextField txtNIBPSys;
	
	@FXML
	private TextField txtNIBPDia;
	
	@FXML
	private TextField txtNIBPMean;
	

	public void setFromHistoryScreen(boolean fromHistoryScreen) {
		this.fromHistoryScreen = fromHistoryScreen;
	}

	public boolean isTertiaryWindow() {
		return isTertiaryWindow;
	}

	public void setTertiaryWindow(boolean isTertiaryWindow) {
		this.isTertiaryWindow = isTertiaryWindow;
	}

	public Label getLblTime() {
		return lblTime;
	}

	public void setPatientMonitorData(PatientMonitorData patientMonitorData) {
		this.patientMonitorData = patientMonitorData;
	}

	private static final Logger LOG = Logger.getLogger(PatientMonitorController.class.getName());

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try
		{
			ControllerMediator.getInstance().registerPatientMonitorController(this);

			btnCloseHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent event) {
					try
					{
						closePopup();
					}
					catch (Exception e)
					{
						LOG.error("## Exception occured:", e);
					}
				}
			};
			btnClose.addEventHandler(MouseEvent.MOUSE_CLICKED, btnCloseHandler);

			btnSaveHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent event) {
					try
					{
						save();
					}
					catch (Exception e)
					{
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
			txtBis.addEventHandler(javafx.scene.input.KeyEvent.KEY_RELEASED, enterKeyPressEventHandler);
			txtETAgent.addEventHandler(javafx.scene.input.KeyEvent.KEY_RELEASED, enterKeyPressEventHandler);
			txtEtCO2.addEventHandler(javafx.scene.input.KeyEvent.KEY_RELEASED, enterKeyPressEventHandler);
			txtHR.addEventHandler(javafx.scene.input.KeyEvent.KEY_RELEASED, enterKeyPressEventHandler);
			txtIBPDia.addEventHandler(javafx.scene.input.KeyEvent.KEY_RELEASED, enterKeyPressEventHandler);
			txtIBPSys.addEventHandler(javafx.scene.input.KeyEvent.KEY_RELEASED, enterKeyPressEventHandler);
			txtMac.addEventHandler(javafx.scene.input.KeyEvent.KEY_RELEASED, enterKeyPressEventHandler);
			txtIBPmean.addEventHandler(javafx.scene.input.KeyEvent.KEY_RELEASED, enterKeyPressEventHandler);
			txtNIBPDia.addEventHandler(javafx.scene.input.KeyEvent.KEY_RELEASED, enterKeyPressEventHandler);
			txtNIBPMean.addEventHandler(javafx.scene.input.KeyEvent.KEY_RELEASED, enterKeyPressEventHandler);			
			txtNIBPSys.addEventHandler(javafx.scene.input.KeyEvent.KEY_RELEASED, enterKeyPressEventHandler);
			txtSpO2.addEventHandler(javafx.scene.input.KeyEvent.KEY_RELEASED, enterKeyPressEventHandler);
			txtTemp1.addEventHandler(javafx.scene.input.KeyEvent.KEY_RELEASED, enterKeyPressEventHandler);
			txtTemp2.addEventHandler(javafx.scene.input.KeyEvent.KEY_RELEASED, enterKeyPressEventHandler);

			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					try
					{
						txtHR.requestFocus();
					}
					catch (Exception e)
					{
						LOG.error("## Exception occured:", e);
					}
				}
			});
		}
		catch (Exception e)
		{
			LOG.error("## Exception occured:", e);
		}

	}

	/**
	 * This method closes popup screen
	 */
	private void closePopup()
	{
		removeEventHandlers();

		// Stage stage = Main.getInstance().getChildStage();
		// //Main.getInstance().setChildStage((Stage) stage.getOwner());
		// stage.close();
		if (isTertiaryWindow)
			Main.getInstance().getStageManager().closeTertiaryStage();
		else
			Main.getInstance().getStageManager().closeSecondaryStage();
	}

	/**
	 * This method remove active event handlers from all the components
	 */
	private void removeEventHandlers() {
		btnClose.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnCloseHandler);
		btnSave.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnSaveHandler);

		txtBis.removeEventHandler(javafx.scene.input.KeyEvent.KEY_RELEASED, enterKeyPressEventHandler);
		txtETAgent.removeEventHandler(javafx.scene.input.KeyEvent.KEY_RELEASED, enterKeyPressEventHandler);
		txtEtCO2.removeEventHandler(javafx.scene.input.KeyEvent.KEY_RELEASED, enterKeyPressEventHandler);
		txtHR.removeEventHandler(javafx.scene.input.KeyEvent.KEY_RELEASED, enterKeyPressEventHandler);
		txtIBPDia.removeEventHandler(javafx.scene.input.KeyEvent.KEY_RELEASED, enterKeyPressEventHandler);
		txtIBPSys.removeEventHandler(javafx.scene.input.KeyEvent.KEY_RELEASED, enterKeyPressEventHandler);
		txtMac.removeEventHandler(javafx.scene.input.KeyEvent.KEY_RELEASED, enterKeyPressEventHandler);
		txtIBPmean.removeEventHandler(javafx.scene.input.KeyEvent.KEY_RELEASED, enterKeyPressEventHandler);
		txtNIBPDia.removeEventHandler(javafx.scene.input.KeyEvent.KEY_RELEASED, enterKeyPressEventHandler);
		txtNIBPSys.removeEventHandler(javafx.scene.input.KeyEvent.KEY_RELEASED, enterKeyPressEventHandler);
		txtNIBPMean.removeEventHandler(javafx.scene.input.KeyEvent.KEY_RELEASED, enterKeyPressEventHandler);
		txtSpO2.removeEventHandler(javafx.scene.input.KeyEvent.KEY_RELEASED, enterKeyPressEventHandler);
		txtTemp1.removeEventHandler(javafx.scene.input.KeyEvent.KEY_RELEASED, enterKeyPressEventHandler);
		txtTemp2.removeEventHandler(javafx.scene.input.KeyEvent.KEY_RELEASED, enterKeyPressEventHandler);

		patientMonitorData = null;// = new PatientMonitorData();
		btnCloseHandler = null;
		btnSaveHandler = null;
	}

	/**
	 * This method autopopulates textboxes on the popup screen
	 *
	 * @param obj object containing values of parameters for S5PatientMonitor device
	 */
	public void autopopulateTextboxes(PatientMonitorData obj)
	{
		try
		{
			txtHR.setText(obj.getHr());
			txtIBPSys.setText(obj.getiBPSys());
			txtIBPDia.setText(obj.getiBPDia());
			txtSpO2.setText(obj.getSpO2());
			txtEtCO2.setText(obj.getEtCO2());
			txtTemp1.setText(obj.getTemp1());
			txtTemp2.setText(obj.getTemp2());
			txtBis.setText(obj.getBIS());
			txtETAgent.setText(obj.getETAgent());
			txtMac.setText(obj.getMAC());
			txtIBPmean.setText(obj.getiBPMean());
			txtNIBPDia.setText(obj.getNiBPDia());
			txtNIBPSys.setText(obj.getNiBPSys());
			txtNIBPMean.setText(obj.getNiBPMean());
		}
		catch (Exception e)
		{
			LOG.error("## Exception occured:", e);
		}
	}

	/**
	 * this method hides Disable pane
	 */
	public void hideDisablePane()
	{
		try
		{
			disablePaneMainContent.setVisible(false);
		}
		catch (Exception e)
		{
			LOG.error("## Exception occured:", e);
		}
	}

	/**
	 * This method save the values entered in textfields for S5Patient Monitor
	 * parameters into the database
	 */
	public void save() {
		try
		{
			if (txtHR.getText() != null && !txtHR.getText().trim().isEmpty()
					|| txtIBPSys.getText() != null && !txtIBPSys.getText().trim().isEmpty()
					|| txtIBPDia.getText() != null && !txtIBPDia.getText().trim().isEmpty()
					|| txtSpO2.getText() != null && !txtSpO2.getText().trim().isEmpty()
					|| txtEtCO2.getText() != null && !txtEtCO2.getText().trim().isEmpty()
					|| txtTemp1.getText() != null && !txtTemp1.getText().trim().isEmpty()
			        || txtTemp2.getText() != null && !txtTemp2.getText().trim().isEmpty()
			        || txtBis.getText() != null && !txtBis.getText().trim().isEmpty()
			        || txtETAgent.getText() != null && !txtETAgent.getText().trim().isEmpty()
			        || txtMac.getText() != null && !txtMac.getText().trim().isEmpty()
			        || txtIBPmean.getText() != null && !txtIBPmean.getText().trim().isEmpty()
			        || txtNIBPSys.getText() != null && !txtNIBPSys.getText().trim().isEmpty()
			        		
			        || txtNIBPMean.getText() != null && !txtNIBPMean.getText().trim().isEmpty()
			        || txtNIBPDia.getText() != null && !txtNIBPDia.getText().trim().isEmpty())
			{
				if (isValidForm()) {
					SaveDevcieParamToDB saveParam = new SaveDevcieParamToDB();
					disablePaneMainContent.setVisible(true);
					lblError.setVisible(false);

					/*
					 * Calendar cal = Calendar.getInstance(); if (selectedTime !=
					 * null) { cal.setTime(selectedTime.getTime()); } else {
					 * cal.set(Calendar.HOUR_OF_DAY,
					 * Integer.valueOf(lblTime.getText().split(":")[0]));
					 * cal.set(Calendar.MINUTE,
					 * Integer.valueOf(lblTime.getText().split(":")[1]));
					 * cal.set(Calendar.SECOND, 0); cal.set(Calendar.MILLISECOND,
					 * 0); }
					 */

					patientMonitorData.setHr(txtHR.getText());
					patientMonitorData.setiBPSys(txtIBPSys.getText());
					patientMonitorData.setiBPDia(txtIBPDia.getText());
					patientMonitorData.setSpO2(txtSpO2.getText());
					patientMonitorData.setEtCO2(txtEtCO2.getText());
					patientMonitorData.setTemp1(txtTemp1.getText());
					patientMonitorData.setTemp2(txtTemp2.getText());
					patientMonitorData.setBIS(txtBis.getText());
					patientMonitorData.setETAgent(txtETAgent.getText());
					patientMonitorData.setMAC(txtMac.getText());
					patientMonitorData.setiBPMean(txtIBPmean.getText());
					patientMonitorData.setNiBPMean(txtNIBPMean.getText());
					patientMonitorData.setNiBPDia(txtNIBPDia.getText());
					patientMonitorData.setNiBPSys(txtNIBPSys.getText());

					patientMonitorData.setTimeStamp(selectedTime.getTime());
					patientMonitorData.setCaseId(PatientSession.getInstance().getPatientCase().getCaseId());
					patientMonitorData.setDeviceId(null);
					saveParam.savePtMntrData(patientMonitorData, true);

					if (fromHistoryScreen)
						ControllerMediator.getInstance().getGridHistoricalViewController().fillDeviceGridCell("ptmonitor",
								patientMonitorData, selectedTime);

					ControllerMediator.getInstance().getMainController().getDrawTabbedCenter()
							.fillDeviceGridCell("ptmonitor", patientMonitorData, selectedTime);
					closePopup();
				} else {
					lblError.setVisible(true);
				}
			} else {
				lblError.setText("* Please enter atleast one parameter to save.");
				lblError.setVisible(true);
			}
		}
		catch (Exception e)
		{
			LOG.error("## Exception occured:", e);
		}
	}

	/**
	 * This method validates textfields on the popup window for valid input
	 *
	 * @return true/false depending upon whether input is valid
	 */
	public boolean isValidForm() {

		if (txtHR.getText() != null && !txtHR.getText().trim().isEmpty()) {
			if (!Validations.isDigit(txtHR.getText())) {
				lblError.setText("Please enter a valid HR value");
				return false;
			}
			if (!Validations.maxLength(txtHR.getText(), 7)) {
				lblError.setText("Please enter HR value upto 7 digit.");
				return false;
			}
		}

		if (txtIBPSys.getText() != null && !txtIBPSys.getText().trim().isEmpty()) {

			if (!Validations.isDigit(txtIBPSys.getText())) {
				lblError.setText("Please enter a valid IBPSys value.");
				return false;
			}
			if (!Validations.maxLength(txtIBPSys.getText(), 7)) {
				lblError.setText("Please enter IBPSys value upto 7 digit.");
				return false;
			}
		}

		if (txtIBPDia.getText() != null && !txtIBPDia.getText().trim().isEmpty()) {
			if (!Validations.isDigit(txtIBPDia.getText())) {
				lblError.setText("Please enter a valid IBPDia value .");
				return false;
			}
			if (!Validations.maxLength(txtIBPDia.getText(), 7)) {
				lblError.setText("Please enter IBPDia value upto 7 digit.");
				return false;
			}
		}

		if (txtSpO2.getText() != null && !txtSpO2.getText().trim().isEmpty()) {
			if (!Validations.isDigit(txtSpO2.getText())) {
				lblError.setText("Please enter a valid SpO2 value .");
				return false;
			}

			if (!Validations.maxLength(txtSpO2.getText(), 7)) {
				lblError.setText("Please enter SpO2 value upto 7 digit.");
				return false;
			}
		}

		if (txtEtCO2.getText() != null && !txtEtCO2.getText().trim().isEmpty()) {
			if (!Validations.isDigit(txtEtCO2.getText())) {
				lblError.setText("Please enter a valid EtCO2 value.");
				return false;
			}
			if (!Validations.maxLength(txtEtCO2.getText(), 7)) {
				lblError.setText("Please enter EtCO2 value upto 7 digit.");
				return false;
			}
		}
		if (txtTemp1.getText() != null && !txtTemp1.getText().trim().isEmpty()) {
			if (!Validations.isDigit(txtTemp1.getText())) {
				lblError.setText("Please enter a valid Temp1 value .");
				return false;
			}
			if (!Validations.maxLength(txtTemp1.getText(), 7)) {
				lblError.setText("Please enter Temp1 value upto 7 digit.");
				return false;
			}
		}

		if (txtTemp2.getText() != null && !txtTemp2.getText().trim().isEmpty()) {
			if (!Validations.isDigit(txtTemp2.getText())) {
				lblError.setText("Please enter a valid Temp2 value.");
				return false;
			}
			if (!Validations.maxLength(txtTemp2.getText(), 7)) {
				lblError.setText("Please enter Temp2 value upto 7 digit.");
				return false;
			}
		}

		if (txtBis.getText() != null && !txtBis.getText().trim().isEmpty())
		{
			if (!Validations.isDigit(txtBis.getText()))
			{
				lblError.setText("Please enter a valid BIS value.");
				return false;
			}
			if (!Validations.maxLength(txtBis.getText(), 7))
			{
				lblError.setText("Please enter BIS value upto 7 digit.");
				return false;
			}
		}

		if (txtETAgent.getText() != null && !txtETAgent.getText().trim().isEmpty())
		{
			if (!Validations.isDigit(txtETAgent.getText()))
			{
				lblError.setText("Please enter a valid ETAgent value.");
				return false;
			}
			if (!Validations.maxLength(txtETAgent.getText(), 7))
			{
				lblError.setText("Please enter ETAgent value upto 7 digit.");
				return false;
			}
		}

		if (txtMac.getText() != null && !txtMac.getText().trim().isEmpty())
		{
			if (!Validations.isDigit(txtMac.getText()))
			{
				lblError.setText("Please enter a valid Mac value.");
				return false;
			}
			if (!Validations.maxLength(txtMac.getText(), 7))
			{
				lblError.setText("Please enter Mac value upto 7 digit.");
				return false;
			}
		}

		if (txtIBPmean.getText() != null && !txtIBPmean.getText().trim().isEmpty())
		{
			if (!Validations.isDigit(txtIBPmean.getText()))
			{
				lblError.setText("Please enter a valid IBPmean value .");
				return false;
			}
			if (!Validations.maxLength(txtIBPmean.getText(), 7))
			{
				lblError.setText("Please enter IBPmean value upto 7 digit.");
				return false;
			}
		}

		if (txtNIBPSys.getText() != null && !txtNIBPSys.getText().trim().isEmpty())
		{
			if (!Validations.isDigit(txtNIBPSys.getText()))
			{
				lblError.setText("Please enter a valid NIBPSys value .");
				return false;
			}
			if (!Validations.maxLength(txtNIBPSys.getText(), 7))
			{
				lblError.setText("Please enter NIBPSys value upto 7 digit.");
				return false;
			}
		}

		if (txtNIBPDia.getText() != null && !txtNIBPDia.getText().trim().isEmpty())
		{
			if (!Validations.isDigit(txtNIBPDia.getText()))
			{
				lblError.setText("Please enter a valid NIBPDia value .");
				return false;
			}
			if (!Validations.maxLength(txtNIBPDia.getText(), 7))
			{
				lblError.setText("Please enter NIBPDia value upto 7 digit.");
				return false;
			}
		}
		
		if (txtNIBPMean.getText() != null && !txtNIBPMean.getText().trim().isEmpty())
		{
			if (!Validations.isDigit(txtNIBPMean.getText()))
			{
				lblError.setText("Please enter a valid NIBPMean value .");
				return false;
			}
			if (!Validations.maxLength(txtNIBPMean.getText(), 7))
			{
				lblError.setText("Please enter NIBPMean value upto 7 digit.");
				return false;
			}
		}
		return true;
	}

}
