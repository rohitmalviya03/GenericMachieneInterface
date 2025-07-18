/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package controllers;

import java.net.URL;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import mediatorPattern.ControllerMediator;
import model.EventLogModel;
import model.EventLogSession;
import model.ExecutedEventSession;
import model.PatientSession;
import model.UserSession;
import services.AddEventService;
import services.DeleteEventLogService;
import services.GetEventLogListService;

/**
 * This controller is used to view Events log , add a custom Event , Update a
 * Event log and to delete a Event Log
 *
 * @author Sudeep_Sahoo
 *
 */
public class EventsController implements Initializable {

	@FXML
	private TableView<IntraopEventlog> eventLogTable;

	@FXML
	private TableColumn<IntraopEventlog, String> columnEvent;

	@FXML
	private TableColumn<IntraopEventlog, Date> columnTime;

	@FXML
	private TableColumn<IntraopEventlog, Boolean> clmnCritical;

	@FXML
	private Button btnClose;

	@FXML
	private Button btnDelete;

	@FXML
	private Button btnEdit;
	@FXML
	private TextField txtEventName;

	@FXML
	private DatePicker datePickerDate;

	@FXML
	private TextArea txtComments;

	@FXML
	private CheckBox checkBoxCritical;

	public CheckBox getCheckBoxCritical() {
		return checkBoxCritical;
	}

	public void setCheckBoxCritical(CheckBox checkBoxCritical) {
		this.checkBoxCritical = checkBoxCritical;
	}

	@FXML
	private Button btnSave;

	@FXML
	private Button btnCancel;

	@FXML
	private Label lblErroMsg;

	@FXML
	private VBox vbTimeControl;

	@FXML
	private Button btnTime;

	@FXML
	private AnchorPane disablePaneHistory;

	@FXML
	private AnchorPane anchorLoader;

	private static final Logger LOGGER = Logger.getLogger(EventsController.class.getName());

	private int patientId = -1;
	private long caseId = 0;
	private String surgeryType = "";
	private int selectedEventId = 0;
	// private ObservableList<IntraopEventlog> eventLogList =
	// FXCollections.observableArrayList();
	private List<IntraopEventlog> eventLogList;

	private int selectedEventLogId = 0;
	private Boolean isCustomEvent = true;
	Spinner<String> hourSpinner = new Spinner<>();
	Spinner<String> minuteSpinner = new Spinner<>();
	Spinner<String> meridiemSpinner = new Spinner<>();
	private HBox hBoxTimeControl;
	private Boolean isDataModified = false;
	private Boolean isCriticalEvent = false;

	// Event Handlers

	public Boolean getIsCriticalEvent() {
		return isCriticalEvent;
	}

	public void setIsCriticalEvent(Boolean isCriticalEvent) {
		this.isCriticalEvent = isCriticalEvent;
	}
	private EventHandler<MouseEvent> btnEditHandler;
	private EventHandler<MouseEvent> btnCloseHandler;
	private EventHandler<MouseEvent> btnSaveHandler;
	private EventHandler<MouseEvent> btnTimeHandler;
	private EventHandler<MouseEvent> btnDeleteHandler;
	private EventHandler<MouseEvent> btnResetHandler;
	private ChangeListener<IntraopEventlog> tableChangeListener;
	private EventHandler<WorkerStateEvent> saveEventServiceSuccessHandler;
	private EventHandler<WorkerStateEvent> saveEventServiceFailedHandler;
	private EventHandler<WorkerStateEvent> getEventLogServiceSuccessHandler;
	private EventHandler<WorkerStateEvent> getEventLogServiceFailedHandler;
	private EventHandler<WorkerStateEvent> deleteEventLogServiceSuccessHandler;
	private EventHandler<WorkerStateEvent> deleteEventLogServiceFailedHandler;

	private Boolean isTimeInSelected = false;

	/**
	 * This method initializes the all Event handlers and Default values on page
	 * load
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {

			if (ControllerMediator.getInstance().getEventsController().isCriticalEvent) {
				checkBoxCritical.setSelected(true);
			}
			// ---- Time control

			hBoxTimeControl = Main.getInstance().getUtility().getTimeControl();
			vbTimeControl.getChildren().add(hBoxTimeControl);
			hourSpinner = (Spinner) hBoxTimeControl.getChildren().get(0);
			minuteSpinner = (Spinner) hBoxTimeControl.getChildren().get(2);
			meridiemSpinner = (Spinner) hBoxTimeControl.getChildren().get(3);

			// checkBoxCritical.getStylesheets().add(this.getClass().getResource("/styles/newStyles.css").toString());
			// checkBoxCritical.getStyleClass().add("criticalCheckBox");

			btnCancel.setVisible(true);
			if (PatientSession.getInstance().getPatient() != null) {
				patientId = PatientSession.getInstance().getPatient().getPatientId();
			}

			if (PatientSession.getInstance().getPatientCase() != null) {
				caseId = PatientSession.getInstance().getPatientCase().getCaseId();
				surgeryType = PatientSession.getInstance().getPatientCase().getSpeciality();
			}
			if (patientId != -1 && caseId != 0 && surgeryType != "") {
				getEventLogList();
			}

			columnEvent.setCellValueFactory(new PropertyValueFactory<IntraopEventlog, String>("customEventName"));
			columnTime.setCellValueFactory(new PropertyValueFactory("eventTime"));
			clmnCritical.setCellValueFactory(new PropertyValueFactory("criticalEvent"));

			clmnCritical.setCellFactory(column -> {
				return new TableCell<IntraopEventlog, Boolean>() {
					@Override
					protected void updateItem(Boolean item, boolean empty) {
						super.updateItem(item, empty);

						if (item != null && item.toString().equalsIgnoreCase("true")) {
							setText("Critical");
							IntraopEventlog logObj = getTableView().getItems().get(getIndex());
							if (logObj.isCriticalEvent()) {
								setTextFill(Color.WHITE);
								setStyle("-fx-background-color: #ef3e47");
							} else {

								if (getTableView().getSelectionModel().getSelectedItems().contains(logObj))
									setTextFill(Color.WHITE);
								else
									setTextFill(Color.BLACK);
							}

						} else {
							setText(null);
							setStyle("");
						}
					}
				};
			});


//			columnEvent.setCellValueFactory(new Callback<CellDataFeatures<IntraopEventlog, String>, ObservableValue<String>>() {
//			     public ObservableValue<String> call(CellDataFeatures<IntraopEventlog, String> p) {
//
//			         return p.getValue().get;
//			     }
//			  });




			/// changes from sahil.sharma08 end here
			// columnTime.setCellValueFactory(new
			// PropertyValueFactory<IntraopEventlog, Date>("eventTime"));
			//
			// columnTime.setCellFactory((TableColumn<IntraopEventlog, Date>
			// column) -> { return new TableCell<IntraopEventlog, Date>() {
			//
			// @Override protected void updateItem(Date item, boolean empty) {
			// super.updateItem(item, empty); if (item == null || empty) {
			// setText(null); } else { setText(formatter.format(item)); } } };
			// });
			//
			/// changes from sahil.sharma08 end here

			btnCloseHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent event) {
					checkBoxCritical.setSelected(false);
					closePopup();
				}
			};
			btnClose.addEventHandler(MouseEvent.MOUSE_CLICKED, btnCloseHandler);

			// ---On click of Delete button

			btnDeleteHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent event) {
					try {
						int selectedId = -1;
						IntraopEventlog selectedEvent = eventLogTable.getSelectionModel().getSelectedItem();
						selectedId = selectedEvent.getEventLogId();
						if (selectedEvent.getCustomEventName().equalsIgnoreCase("Time In")) {
							isTimeInSelected = true;
						}
						if (selectedId != -1) {
							confirmDelete(selectedId);
						}
					} catch (Exception e) {
					}
				}
			};
			btnDelete.addEventHandler(MouseEvent.MOUSE_CLICKED, btnDeleteHandler);

			eventLogTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
			// ---Enable buttons only when a table row is selected.

			tableChangeListener = new ChangeListener<IntraopEventlog>() {

				@Override
				public void changed(ObservableValue<? extends IntraopEventlog> observable, IntraopEventlog oldValue,
						IntraopEventlog newValue) {

					if (newValue != null) {
						if (newValue != null) {

							btnDelete.setDisable(false);
							btnEdit.setDisable(false);
							disablePaneHistory.setVisible(false);

						}
					}
				}
			};

			eventLogTable.getSelectionModel().selectedItemProperty().addListener(tableChangeListener);

			btnSaveHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent event) {
					saveEvent();
				}
			};
			btnSave.addEventHandler(MouseEvent.MOUSE_CLICKED, btnSaveHandler);

			btnEditHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent event) {
					IntraopEventlog selectedEvent = eventLogTable.getSelectionModel().getSelectedItem();

					if (selectedEvent != null) {

						txtEventName.setText(selectedEvent.getCustomEventName());
						if (selectedEvent.getIntraopEventsmaster() != null
								&& selectedEvent.getIntraopEventsmaster().getEventId() != null) {
							isCustomEvent = false;
						} else {
							isCustomEvent = true;
						}
						if (selectedEvent.getEventTime() != null) {
							String dateTimeArr[] = selectedEvent.getEventTime().toString().split(" ");
							String timearray[] = dateTimeArr[1].split("\\.");
							LocalDate localDate = LocalDate.parse(dateTimeArr[0]);
							datePickerDate.setValue(localDate);
							setTime(timearray[0]);
						}
					}
					txtComments.setText(selectedEvent.getComments());
					selectedEventLogId = selectedEvent.getEventLogId();
					if (selectedEvent.getIntraopEventsmaster() != null) {
						if (selectedEvent.getIntraopEventsmaster().getEventId() != null) {
							selectedEventId = selectedEvent.getIntraopEventsmaster().getEventId();
							txtEventName.setDisable(true);
						}

					} else {
						txtEventName.setDisable(false);
					}
					checkBoxCritical.setSelected(selectedEvent.isCriticalEvent());
					btnCancel.setVisible(true);
					btnSave.setText("Update");
					disablePaneHistory.setVisible(true);

				}
			};
			btnEdit.addEventHandler(MouseEvent.MOUSE_CLICKED, btnEditHandler);

			btnResetHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent event) {
					reset();

				}
			};
			btnCancel.addEventHandler(MouseEvent.MOUSE_CLICKED, btnResetHandler);

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
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					txtEventName.requestFocus();
				}
			});

		} catch (Exception e) {
			LOGGER.error("## Exception occured:" + e);
		}
	}

	/**
	 * This method is used to close the Events Log Window and calls the remove
	 * listeners method
	 */
	private void closePopup() {
		try {
			if (isDataModified) {
				ControllerMediator.getInstance().getMainController().getDrawSidePane().setInit(false);
				ControllerMediator.getInstance().getMainController().loadSideBarEvents();

			}
			removeListener();
			Main.getInstance().getStageManager().closeSecondaryStage();
			ControllerMediator.getInstance().getEventsController().setIsCriticalEvent(false);

		} catch (Exception e) {
			LOGGER.error("## Exception occured:" + e);
		}

	}

	/**
	 * This method open delete confirmation dialogue box asking for,Are you
	 * sure, you want to delete the record?.On confirmation it calls Delete
	 * Event log service
	 *
	 * @param eventid
	 *            Integer
	 */
	private void confirmDelete(int eventid) {
		try {
			boolean status = Main.getInstance().getUtility().confirmDelete();
			if (status) {
				deleteEvent(eventid);
			}
		} catch (Exception e) {
			LOGGER.error("## Exception occured:" + e);
		}

	}

	/**
	 * This method delete selected event with eventLogId when confirmed
	 *
	 * @param eventLogId
	 *            Integer
	 */

	private void deleteEvent(int eventLogId) {
		try {

			DeleteEventLogService deleteEventLogService = DeleteEventLogService.getInstance(eventLogId);
			deleteEventLogService.restart();

			deleteEventLogServiceSuccessHandler = new EventHandler<WorkerStateEvent>() {

				@Override
				public void handle(WorkerStateEvent event) {
					// String deleteStatus =
					// deleteEventLogService.getDeleteStatus();
					Main.getInstance().getUtility().showNotification("Information", "Information",
							"Event deleted successfully.");
					List<IntraopEventlog> eventSessionList = ExecutedEventSession.getInstance().getExecutedEventList();
					if (eventSessionList != null) {
						for (IntraopEventlog obj : eventSessionList) {
							if (obj.getEventLogId() == eventLogId) {
								eventSessionList.remove(obj);
								break;
							}
						}
						ExecutedEventSession.getInstance().getExecutedEventList().clear();
						ExecutedEventSession.getInstance().setExecutedEventList(eventSessionList);
					}

					getEventLogList();
					isDataModified = true;
					if (isTimeInSelected) {
						EventLogSession.getInstance().setEventTime(null);
						isTimeInSelected = false;
					}

					deleteEventLogService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
							deleteEventLogServiceSuccessHandler);
					deleteEventLogService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
							deleteEventLogServiceFailedHandler);
				}
			};

			deleteEventLogService.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
					deleteEventLogServiceSuccessHandler);

			deleteEventLogServiceFailedHandler = new EventHandler<WorkerStateEvent>() {

				@Override
				public void handle(WorkerStateEvent event) {

					Main.getInstance().getUtility().showNotification("Error", "Error",
							deleteEventLogService.getException().getMessage());

					deleteEventLogService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
							deleteEventLogServiceSuccessHandler);
					deleteEventLogService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
							deleteEventLogServiceFailedHandler);
				}
			};

			deleteEventLogService.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
					deleteEventLogServiceFailedHandler);

		} catch (Exception e) {
			LOGGER.error("## Exception occured:" + e);
		}
	}

	/**
	 * This method is used to Add and Update a Events Details in Database
	 */
	private void saveEvent() {
		try {

			lblErroMsg.setVisible(false);
			if (txtEventName.getText() != null && !txtEventName.getText().trim().isEmpty()
					&& datePickerDate.getValue() != null) {
				if (validate()) {

					anchorLoader.setVisible(true);
					try {
						SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

						Date dateTime = simpleDateFormat.parse(datePickerDate.getValue() + " "
								+ Main.getInstance().getUtility().getTime(hBoxTimeControl));

						// ---creating IntraopEventlog obj
						IntraopEventlog intraopEventLog = new IntraopEventlog();

						intraopEventLog.setEventTime(dateTime);
						intraopEventLog.setCreatedBy(
								UserSession.getInstance().getUserWithrRolesForUserAuthentication().getUserID());
						intraopEventLog.setCreatedTime(new Date());

						if (PatientSession.getInstance().getPatient() != null)
							intraopEventLog.setPatient(PatientSession.getInstance().getPatient());
						if (PatientSession.getInstance().getPatientCase() != null)
							intraopEventLog.setPatientcase(PatientSession.getInstance().getPatientCase());

						IntraopEventsmaster intraopEventsmasterObj = null;
						if (selectedEventId != 0) {
							intraopEventsmasterObj = new IntraopEventsmaster();
							intraopEventsmasterObj.setEventId(selectedEventId);
							intraopEventsmasterObj.setEventName(txtEventName.getText());
						}

						intraopEventLog.setCustomEventName(txtEventName.getText().trim());

						intraopEventLog.setIntraopEventsmaster(intraopEventsmasterObj);
						intraopEventLog.setSurgeryType(surgeryType);

						if (txtComments.getText() != null && !txtComments.getText().isEmpty()) {
							intraopEventLog.setComments(txtComments.getText());
						}

						if (selectedEventLogId != 0) {
							intraopEventLog.setEventLogId(selectedEventLogId);
						}
						intraopEventLog.setCriticalEvent(checkBoxCritical.isSelected());

						// ---calling service
						AddEventService addEventService = AddEventService.getInstance(intraopEventLog);
						addEventService.restart();

						// ---on service success

						saveEventServiceSuccessHandler = new EventHandler<WorkerStateEvent>() {

							@Override
							public void handle(WorkerStateEvent event) {
								IntraopEventlog result = addEventService.getReturnedObj();

								if (result.getEventLogId() != null) {
									if (selectedEventLogId != 0) {
										Main.getInstance().getUtility().showNotification("Information", "Success",
												"Event log updated successfully!");
										List<IntraopEventlog> eventSessionList = ExecutedEventSession.getInstance()
												.getExecutedEventList();
										if (eventSessionList != null) {
											for (IntraopEventlog obj : eventSessionList) {
												if (obj.getEventLogId() == selectedEventLogId) {
													obj = intraopEventLog;
													break;
												}
											}
											if (ExecutedEventSession.getInstance().getExecutedEventList() != null) {
												ExecutedEventSession.getInstance().getExecutedEventList().clear();
											}
											ExecutedEventSession.getInstance().setExecutedEventList(eventSessionList);
										}

									} else {
										Main.getInstance().getUtility().showNotification("Information", "Success",
												"Event logged successfully!");
										if (ExecutedEventSession.getInstance().getExecutedEventList() != null) {
											ExecutedEventSession.getInstance().getExecutedEventList()
													.add(intraopEventLog);
										}
									}

									if (txtEventName.getText().toString().trim().equalsIgnoreCase("Shift Out")) {
										closePopup();
										ControllerMediator.getInstance().getMainController()
												.bindPopupFxml(FxmlView.SHIFT_OUT);

									}

									getEventLogList();
									reset();
								}
								anchorLoader.setVisible(false);
								isDataModified = true;

								addEventService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
										saveEventServiceSuccessHandler);
								addEventService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
										saveEventServiceFailedHandler);
							}
						};

						addEventService.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
								saveEventServiceSuccessHandler);

						saveEventServiceFailedHandler = new EventHandler<WorkerStateEvent>() {

							@Override
							public void handle(WorkerStateEvent event) {

								Main.getInstance().getUtility().showNotification("Error", "Error",
										addEventService.getException().getMessage());
								anchorLoader.setVisible(false);

								addEventService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
										saveEventServiceSuccessHandler);
								addEventService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
										saveEventServiceFailedHandler);
							}
						};

						addEventService.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
								saveEventServiceFailedHandler);

					} catch (Exception ex) {
						LOGGER.error("## Exception occured:" + ex);
					}
				} else {
					lblErroMsg.setVisible(true);
				}
			} else {
				lblErroMsg.setText("* Please enter mandatory fields.");
				lblErroMsg.setVisible(true);
			}

		} catch (Exception e) {
			LOGGER.error("## Exception occured:" + e);
		}
	}

	public void setCriticalEvent() {
		// checkBoxCritical.selectedProperty().set(false);
		// System.out.println("Before#####"+checkBoxCritical.isSelected());
		// checkBoxCritical.selectedProperty().set(true);
		// System.out.println("After#####"+checkBoxCritical.isSelected());
		//
		System.out.println("Before#####" + checkBoxCritical.isSelected());
		// checkBoxCritical= new CheckBox();
		// checkBoxCritical.setSelected(true);
		System.out.println("After#####" + checkBoxCritical.isSelected());
		// checkBoxCritical.setSelected(true);
	}

	/**
	 * This method is used to reset the Add Event form
	 */
	private void reset() {
		try {
			txtComments.setText("");
			txtEventName.setText("");
			datePickerDate.setValue(null);
			selectedEventId = 0;
			selectedEventLogId = 0;
			btnDelete.setDisable(true);
			btnEdit.setDisable(true);
			disablePaneHistory.setVisible(false);
			btnSave.setText("Save");
			txtEventName.setDisable(false);
			lblErroMsg.setVisible(false);
			Date input = new Date();
			LocalDate date = input.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			datePickerDate.setValue(date);
			eventLogTable.getSelectionModel().clearSelection();
			isCustomEvent = true;
			resetTime();
			checkBoxCritical.setSelected(false);

		} catch (Exception e) {
			LOGGER.error("## Exception occured:" + e);
		}

	}

	/**
	 * This method is used to fetch all Events log from database
	 */
	private void getEventLogList() {
		try {

			System.out.println("get events called");
			GetEventLogListService getEventLogListService = GetEventLogListService.getInstance(patientId, caseId,
					surgeryType);
			getEventLogListService.restart();

			getEventLogServiceSuccessHandler = new EventHandler<WorkerStateEvent>() {

				@Override
				public void handle(WorkerStateEvent event) {

					eventLogList = getEventLogListService.getEventLogRecordList();
					eventLogTable.getItems().clear();
					eventLogTable.getItems().addAll(eventLogList);
					if (ExecutedEventSession.getInstance().getExecutedEventList() != null) {
						ExecutedEventSession.getInstance().getExecutedEventList().clear();
						ExecutedEventSession.getInstance().setExecutedEventList(eventLogList);
					}

					// for (Node r: eventLogTable.lookupAll(".table-row-cell")){
					// System.out.println(r);
					// eventLogTable.getItems().
					// }

					getEventLogListService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
							getEventLogServiceSuccessHandler);
					getEventLogListService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
							getEventLogServiceFailedHandler);
				}
			};

			getEventLogListService.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
					getEventLogServiceSuccessHandler);

			getEventLogServiceFailedHandler = new EventHandler<WorkerStateEvent>() {

				@Override
				public void handle(WorkerStateEvent event) {

					getEventLogListService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
							getEventLogServiceSuccessHandler);
					getEventLogListService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
							getEventLogServiceFailedHandler);
					Main.getInstance().getUtility().showNotification("Error", "Error",
							getEventLogListService.getException().getMessage());
				}
			};

			getEventLogListService.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
					getEventLogServiceFailedHandler);

		} catch (Exception e) {
			LOGGER.error("## Exception occured:" + e);
		}
	};

	/**
	 * This method is used to validate Add Event form.Returns true when the form
	 * is valid and returns false when form is not valid
	 *
	 * @return Boolean
	 */
	private boolean validate() {
		try {

			/// for checking whether or not the date entered is in future
			try {
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				Date dateEntered = simpleDateFormat.parse(datePickerDate.getValue() + " "
						+ Main.getInstance().getUtility().getTime(hBoxTimeControl).toString());
				if (isCustomEvent) {
					List<EventLogModel> eventList = new ArrayList<EventLogModel>();
					eventList = EventLogSession.getInstance().getEventList();
					if (eventList != null) {
						for (EventLogModel eventobj : eventList) {
							if (eventobj.getEventname().equalsIgnoreCase(txtEventName.getText())) {
								lblErroMsg.setText("Please enter a Custom event name.");
								return false;
							}
						}
					}
				}

				if (isCustomEvent) {
					for (IntraopEventlog obj : eventLogList) {

						if (obj.getCustomEventName().equalsIgnoreCase(txtEventName.getText())
								&& obj.getEventLogId() != selectedEventLogId) {
							lblErroMsg.setText(" Event already has been logged. ");
							return false;
						}
					}
				}
				if (!Validations.isAlphaNumericWithMultipleSpaces(txtEventName.getText())) {
					lblErroMsg.setText("*Please enter a valid Event name.");
					return false;
				}
				if (!Validations.maxLength(txtEventName.getText(), 250)) {
					lblErroMsg.setText("*Please enter a event name less than 250");
					return false;
				}

				if (Validations.futureDateCheck(dateEntered)) {
					lblErroMsg.setText("*Future date is not allowed.");
					return false;
				}

				if (txtComments.getText() != null && !txtComments.getText().trim().isEmpty()
						&& !Validations.maxLength(txtComments.getText(), 2000)) {
					lblErroMsg.setText("*Please enter Comments less than 2000 characters");
					return false;
				}

			} catch (Exception e) {
				LOGGER.error("## Exception occured:" + e);
			}
			return true;

		} catch (Exception e) {
			LOGGER.error("## Exception occured:" + e);
			return false;
		}
	}

	/**
	 * This method is used to set time in time control
	 *
	 *
	 * @param time:
	 *            local time in string
	 */
	public void setTime(String time) {
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
			LOGGER.error("## Exception occured:" + e);
		}

	}

	/**
	 * This method is used to reset the time control
	 */
	public void resetTime() {
		try {
			LocalTime timeNow = Main.getInstance().getUtility().getLocalSystemTime();
			setTime(timeNow.toString());
		} catch (Exception e) {
			LOGGER.error("## Exception occured:" + e);
		}

	}

	/**
	 * This method is used to remove all listeners and nullify class level
	 * variables
	 */
	public void removeListener() {
		try {

			btnEdit.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnEditHandler);
			btnClose.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnCloseHandler);
			btnCancel.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnResetHandler);
			btnDelete.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnDeleteHandler);
			btnTime.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnTimeHandler);
			btnSave.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnSaveHandler);
			eventLogTable.getSelectionModel().selectedItemProperty().removeListener(tableChangeListener);
			surgeryType = null;
			eventLogList = null;
			isCustomEvent = null;
			hourSpinner = null;
			minuteSpinner = null;
			meridiemSpinner = null;
			hBoxTimeControl = null;
			isDataModified = null;

			// Event Handlers

			btnEditHandler = null;
			btnCloseHandler = null;
			btnSaveHandler = null;
			btnTimeHandler = null;
			btnDeleteHandler = null;
			btnResetHandler = null;
			tableChangeListener = null;
			saveEventServiceSuccessHandler = null;
			saveEventServiceFailedHandler = null;
			getEventLogServiceSuccessHandler = null;
			getEventLogServiceFailedHandler = null;
			deleteEventLogServiceSuccessHandler = null;
			deleteEventLogServiceFailedHandler = null;

		} catch (Exception e) {
			LOGGER.error("## Exception occured:" + e);
		}
	}

}
