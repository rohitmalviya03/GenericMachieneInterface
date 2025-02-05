/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package controllers;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.cdac.common.GeneratedEntities.IntraopEventlog;
import com.cdac.common.GeneratedEntities.IntraopEventsmaster;
import com.cdac.common.util.Validations;

import application.Main;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import mediatorPattern.ControllerMediator;
import model.EventLogSession;
import model.ExecutedEventSession;
import model.PatientSession;
import model.UserSession;
import services.AddEventService;

/**
 * This Controller is used to add Events from the Events side bar in main
 * Dashboard window
 *
 * @author Sudeep_Sahoo
 *
 */
public class DashboardEventController implements Initializable {

	@FXML
	private Button btnClose;

	@FXML
	private Label lblEventName;

	@FXML
	private DatePicker datePickerDate;

	@FXML
	private VBox vbTimeControl;

	@FXML
	private Button btnTime;

	@FXML
	private Label lblError;

	@FXML
	private Button btnSave;

	@FXML
	private AnchorPane disablePaneMainContent;

	@FXML
	private TextArea txtComments;

	private static String CPBOn = "CPB On";

	private String selectedEventName;

	public String getSelectedEventName() {
		return selectedEventName;
	}

	public void setSelectedEventName(String selectedEventName) {
		this.selectedEventName = selectedEventName;
	}

	Spinner<String> hourSpinner = new Spinner<>();
	Spinner<String> minuteSpinner = new Spinner<>();
	Spinner<String> meridiemSpinner = new Spinner<>();
	private HBox hBoxTimeControl;
	private String surgeryType = "";
	private transient AddEventService addEventService;
	private int selectedEventLogId = 0;
	private int selectedEventId = 0;
	private Button btnEvent;

	private EventHandler<MouseEvent> btnCloseHandler;

	private EventHandler<MouseEvent> btnSaveHandler;

	private EventHandler<MouseEvent> btnTimeHandler;

	private EventHandler<WorkerStateEvent> addEventServiceSuccessHandler;

	private EventHandler<WorkerStateEvent> addEventServiceFailedHandler;

	public Button getBtnEvent() {
		return btnEvent;
	}

	public void setBtnEvent(Button btnEvent) {
		this.btnEvent = btnEvent;
	}

	public int getSelectedEventId() {
		return selectedEventId;
	}

	public void setSelectedEventId(int selectedEventId) {
		this.selectedEventId = selectedEventId;
	}

	private static final Logger LOGGER = Logger.getLogger(DashboardEventController.class.getName());

	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		try {

			ControllerMediator.getInstance().registerDashboardEventController(this);

			if (PatientSession.getInstance().getPatientCase() != null) {

				surgeryType = PatientSession.getInstance().getPatientCase().getSpeciality();
			}
			// ---- Time control

			hBoxTimeControl = Main.getInstance().getUtility().getTimeControl();
			vbTimeControl.getChildren().add(hBoxTimeControl);
			hourSpinner = (Spinner<String>) hBoxTimeControl.getChildren().get(0);
			minuteSpinner = (Spinner<String>) hBoxTimeControl.getChildren().get(2);
			meridiemSpinner = (Spinner<String>) hBoxTimeControl.getChildren().get(3);

			btnSaveHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent event) {
					saveEvent();

					if(lblEventName.getText().equals(CPBOn)){
						Main.getInstance().getUtility().warningDialogue("Information",
							"Please mark Patient Fluid details.");
				}
}
			};
			btnSave.addEventHandler(MouseEvent.MOUSE_CLICKED, btnSaveHandler);

			btnCloseHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent event) {
					closePopup();
				}
			};
			btnClose.addEventHandler(MouseEvent.MOUSE_CLICKED, btnCloseHandler);

			btnTimeHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent event) {
					LocalTime timeNow = Main.getInstance().getUtility().getLocalSystemTime();
					setTime(timeNow.toString());
					Date input = new Date();
					LocalDate date = input.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
					datePickerDate.setValue(date);
				}
			};
			btnTime.addEventHandler(MouseEvent.MOUSE_CLICKED, btnTimeHandler);

			Date input = new Date();
			LocalDate date = input.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			datePickerDate.setValue(date);
			resetTime();

		} catch (Exception e) {
			LOGGER.error("## Exception occured:", e);
		}
	}

	/**
	 * This method is used to Remove all event handlers
	 */
	private void removeEventHandlers() {
		try {
			btnSave.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnSaveHandler);
			btnClose.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnCloseHandler);
			btnTime.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnTimeHandler);
		} catch (Exception e) {
			LOGGER.error("## Exception occured:", e);
		}

	}

	/**
	 * This method is used to Set the Selected Event from Dashboard
	 */
	public void setEventName() {

		try {
			lblEventName.setText(selectedEventName);
		} catch (Exception e) {
			LOGGER.error("## Exception occured:", e);
		}

	}

	/**
	 * This method is used to close the Add Event window
	 */
	private void closePopup() {
		try {
			removeEventHandlers();
			Main.getInstance().getStageManager().closeSecondaryStage();
		} catch (Exception e) {
			LOGGER.error("## Exception occured:", e);
		}

	}

	/**
	 * This method is used to set time in time control
	 *
	 *
	 * @param time:
	 *            local time in string
	 */
	private void setTime(String time) {

		try {

			String[] hourMinutearray = time.split(":");
			int hour = 0;
			if (Integer.valueOf(hourMinutearray[0].toString()) < 12) {
				meridiemSpinner.getValueFactory().setValue("AM");
				hour = Integer.valueOf(hourMinutearray[0].toString());
				if (hour == 0) {
					hour = 12;
				}
			} else {
				meridiemSpinner.getValueFactory().setValue("PM");
				hour = Integer.valueOf(hourMinutearray[0].toString());
				if (hour != 12) {
					hour = hour - 12;
				}

			}
			if (hour < 10) {
				hourSpinner.getValueFactory().setValue("0" + hour);
			} else {
				hourSpinner.getValueFactory().setValue("" + hour);
			}

			minuteSpinner.getValueFactory().setValue(hourMinutearray[1]);

		} catch (Exception e) {
			LOGGER.error("## Exception occured:", e);
		}

	}

	/**
	 * This method is used to reset the Time Control
	 */
	private void resetTime() {
		try {
			LocalTime timeNow = Main.getInstance().getUtility().getLocalSystemTime();
			setTime(timeNow.toString());
		} catch (Exception e) {
			LOGGER.error("## Exception occured:", e);
		}

	}

	/**
	 * This method is used to call add Event service and save the event details
	 * in Data base
	 */
	public void saveEvent() {

		try {

			boolean isValid = true;
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Date dateTime;
			IntraopEventlog intraopEventLog = new IntraopEventlog();
			try {
				dateTime = simpleDateFormat.parse(
						datePickerDate.getValue() + " " + Main.getInstance().getUtility().getTime(hBoxTimeControl));
				if (Validations.futureDateCheck(dateTime)) {
					lblError.setText("*Future date is not allowed.");
					lblError.setVisible(true);
					isValid = false;
				}
				intraopEventLog.setEventTime(dateTime);
			} catch (ParseException e) {
				LOGGER.error("## Exception occured:", e);
			}
			if (txtComments.getText() != null && !txtComments.getText().isEmpty()) {
				if (!Validations.maxLength(txtComments.getText(), 2000)) {
					lblError.setText("*Please enter Comments less than" + "\n" + "2000 characters");
					lblError.setVisible(true);
					isValid = false;
				}
			}
			if (isValid) {
				lblError.setVisible(false);
				disablePaneMainContent.setVisible(true);
				// ---creating IntraopEventlog obj

				intraopEventLog
						.setCreatedBy(UserSession.getInstance().getUserWithrRolesForUserAuthentication().getUserID());
				intraopEventLog.setCreatedTime(new Date());

				if (PatientSession.getInstance().getPatient() != null)
					intraopEventLog.setPatient(PatientSession.getInstance().getPatient());
				if (PatientSession.getInstance().getPatientCase() != null)
					intraopEventLog.setPatientcase(PatientSession.getInstance().getPatientCase());

				IntraopEventsmaster intraopEventsmasterObj = null;
				if (selectedEventId != 0) {
					intraopEventsmasterObj = new IntraopEventsmaster();
					intraopEventsmasterObj.setEventId(selectedEventId);
					intraopEventsmasterObj.setEventName(lblEventName.getText());
				}

				intraopEventLog.setCustomEventName(lblEventName.getText());
				if(!txtComments.getText().trim().isEmpty()){
				intraopEventLog.setComments(txtComments.getText());
				}

				intraopEventLog.setIntraopEventsmaster(intraopEventsmasterObj);
				intraopEventLog.setSurgeryType(surgeryType);

				if (selectedEventLogId != 0) {
					intraopEventLog.setEventLogId(selectedEventLogId);
				}

				// ---calling service
				addEventService = AddEventService.getInstance(intraopEventLog);
				addEventService.restart();

				addEventServiceSuccessHandler = new EventHandler<WorkerStateEvent>() {

					@Override
					public void handle(WorkerStateEvent event) {
						IntraopEventlog result = addEventService.getReturnedObj();

						if (result.getEventLogId() != null) {

							if (intraopEventLog.getCustomEventName().equalsIgnoreCase("time in")) {
								EventLogSession.getInstance().setEventTime(intraopEventLog.getEventTime());
							}
							if (ExecutedEventSession.getInstance().getExecutedEventList() != null) {
								ExecutedEventSession.getInstance().getExecutedEventList().add(intraopEventLog);
							} else {
								List<IntraopEventlog> eventSessionList = new ArrayList<IntraopEventlog>();
								eventSessionList.add(intraopEventLog);
								ExecutedEventSession.getInstance().setExecutedEventList(eventSessionList);
							}

							btnEvent.getStylesheets()
									.add(this.getClass().getResource("/styles/newStyles.css").toString());
							btnEvent.getStyleClass().add("sidebarBtnExecuted");
							btnEvent.setDisable(true);
							int hourValue = 0;
							if (meridiemSpinner.getValueFactory().getValue().equalsIgnoreCase("PM")) {
								hourValue = Integer.valueOf(hourSpinner.getValueFactory().getValue()) ;
								if(Integer.valueOf(hourSpinner.getValueFactory().getValue()) != 12){
									hourValue = hourValue + 12;
								}


							} else {
								hourValue = Integer.valueOf(hourSpinner.getValueFactory().getValue());
								if(Integer.valueOf(hourSpinner.getValueFactory().getValue()) == 12){
									hourValue = hourValue-12;
								}

							}
							btnEvent.setText(btnEvent.getText() + "\n" + hourValue + ":"
									+ minuteSpinner.getValueFactory().getValue());

						}
						disablePaneMainContent.setVisible(false);
						addEventService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
								addEventServiceSuccessHandler);
						addEventService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
								addEventServiceFailedHandler);

						closePopup();

						Main.getInstance().getUtility().showNotification("Information", "Success",
								"Event logged successfully!");


					}

				};

				addEventService.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, addEventServiceSuccessHandler);

				addEventServiceFailedHandler = new EventHandler<WorkerStateEvent>() {

					@Override
					public void handle(WorkerStateEvent event) {

						disablePaneMainContent.setVisible(false);
						addEventService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
								addEventServiceSuccessHandler);
						addEventService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
								addEventServiceFailedHandler);
						Main.getInstance().getUtility().showNotification("Error", "Error",
								addEventService.getException().getMessage());
					}

				};

				addEventService.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED, addEventServiceFailedHandler);
			}

		} catch (Exception e) {
			LOGGER.error("## Exception occured:", e);
		}
	}

}
