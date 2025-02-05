/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package utility;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

import com.cdac.common.GeneratedEntities.EventLogEventStatus;
import com.cdac.common.GeneratedEntities.IntraopEventlog;
import com.cdac.common.GeneratedEntities.IntraopEventsmaster;

import application.FxmlView;
import application.Main;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import mediatorPattern.ControllerMediator;
import model.EventLogModel;
import model.EventLogSession;
import model.ExecutedEventSession;
import model.PatientSession;
import services.EventLogService;

/**
 * This Class is used to Draw the Events Side bar on Main Dashboard
 *
 * @author Sudeep_Sahoo
 *
 */
public class DrawDashboardSideBar {

	private Pane sidePane;
	private List<EventLogEventStatus> allEventLogDetails;
	private List<EventLogModel> eventLogDetails = new ArrayList<EventLogModel>();
	private List<EventLogModel> executedEventLogDetails = new ArrayList<EventLogModel>();
	private List<String> eventList;
	String selectedEventName = "";
	private VBox vbEventsList = new VBox();
	private int patientId = -1;
	private long caseId = 0;
	private String surgeryType = "";
	private EventHandler<MouseEvent> btnEventHandler;
	private EventHandler<MouseEvent> btnCriticalEventHandler;
	private EventHandler<WorkerStateEvent> EventLogServiceSuccessHandler;
	private EventHandler<WorkerStateEvent> EventLogServiceFailedHandler;
	private boolean isInit = true;
	private Button critcalEventBtn;

	public boolean isInit() {
		return isInit;
	}

	public void setInit(boolean isInit) {
		this.isInit = isInit;
	}

	private static final Logger LOG = Logger.getLogger(DrawDashboardSideBar.class.getName());

	/**
	 * This method is used to create A pane for Events Side bar in Main
	 * Dashboard
	 *
	 * @param pane
	 * @param disablePaneMainContent
	 */
	public void createSidePane(Pane pane, AnchorPane disablePaneMainContent) {

		try {

			List<IntraopEventlog> eventSessionList = ExecutedEventSession.getInstance().getExecutedEventList();
			if (eventSessionList != null)
				for (IntraopEventlog obj : eventSessionList) {
					System.out.println("Name--" + obj.getCustomEventName() + "######  Time--" + obj.getEventTime());
				}
			pane.getChildren().clear();
			disablePaneMainContent.setVisible(true);
			if (PatientSession.getInstance().getPatient() != null) {
				patientId = PatientSession.getInstance().getPatient().getPatientId();
			}

			if (PatientSession.getInstance().getPatientCase() != null) {
				caseId = PatientSession.getInstance().getPatientCase().getCaseId();
				surgeryType = PatientSession.getInstance().getPatientCase().getSpeciality();
			}

			vbEventsList.setSpacing(2);

			if (patientId != -1 && caseId != 0 && surgeryType != "") {
				getAllEventLogs(surgeryType, patientId, caseId, disablePaneMainContent);

			}
			if (sidePane != null) {
				sidePane.getChildren().clear();
			}

			sidePane = pane;
			ScrollPane eventScrollPane = new ScrollPane();
			eventScrollPane.setPrefSize(sidePane.getPrefWidth(), sidePane.getPrefHeight());
			eventScrollPane.setStyle("-fx-padding: 5.0px;");
			eventScrollPane.setContent(vbEventsList);
			sidePane.getChildren().add(eventScrollPane);

		} catch (Exception e) {
			LOG.error("## Exception occured:", e);
		}
	}

	/**
	 * This method is used to fetch all events for given
	 *
	 * @param surgeryID
	 * @param patientID
	 * @param patientCaseID
	 */
	public void getAllEventLogs(String surgeryID, int patientID, Long patientCaseID,
			AnchorPane disablePaneMainContent) {
		try {

			eventLogDetails = new ArrayList<EventLogModel>();
			executedEventLogDetails = new ArrayList<EventLogModel>();
			EventLogService eventLogService = EventLogService.getInstance(surgeryID, patientID, patientCaseID);
			eventLogService.restart();

			EventLogServiceSuccessHandler = new EventHandler<WorkerStateEvent>() {

				@Override
				public void handle(WorkerStateEvent event) {

					allEventLogDetails = eventLogService.getEventLogDetails();
					if (allEventLogDetails != null) {

						eventList = new ArrayList<>();
						for (EventLogEventStatus obj : allEventLogDetails) {
							if (obj != null) {

								IntraopEventsmaster intraopEventsmasterObj = obj.getIntraopEventLog()
										.getIntraopEventsmaster();
								eventList.add(intraopEventsmasterObj.getEventName());
								EventLogModel eventLogModelObj = new EventLogModel();
								eventLogModelObj.setEventLogId(obj.getEventLog().getEventLogId());
								eventLogModelObj.setEventId(intraopEventsmasterObj.getEventId());
								eventLogModelObj.setEventname(intraopEventsmasterObj.getEventName());
								eventLogModelObj.setType(intraopEventsmasterObj.getEventType());
								eventLogModelObj.setEventTime(obj.getEventLog().getEventTime());
								eventLogModelObj.setDetails(obj.getEventLog().getComments());
								eventLogModelObj.setStatus(obj.getEventPerformed());

								eventLogDetails.add(eventLogModelObj);

								if (obj.getEventPerformed()) {
									executedEventLogDetails.add(eventLogModelObj);
								}

							}
						}
						EventLogSession.getInstance().setEventList(eventLogDetails);
						getEventList(disablePaneMainContent);

					}
					eventLogService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
							EventLogServiceSuccessHandler);
					eventLogService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
							EventLogServiceFailedHandler);
				}
			};

			eventLogService.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, EventLogServiceSuccessHandler);

			EventLogServiceFailedHandler = new EventHandler<WorkerStateEvent>() {

				@Override
				public void handle(WorkerStateEvent event) {
					Main.getInstance().getUtility().showNotification("Error", "Error",
							eventLogService.getException().getMessage());

					eventLogService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
							EventLogServiceSuccessHandler);
					eventLogService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
							EventLogServiceFailedHandler);

				}
			};

			eventLogService.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED, EventLogServiceFailedHandler);

		} catch (Exception e) {
			LOG.error("## Exception occured:", e);
		}
	}

	/**
	 * This method is used to create the side pane and show it on main Dashboard
	 *
	 * @param disablePaneMainContent
	 * @return
	 */
	public VBox getEventList(AnchorPane disablePaneMainContent) {
		try {
			vbEventsList.getChildren().clear();
			critcalEventBtn = new Button("Critical Event");
			Tooltip criticalEventTooltip = new Tooltip("Critical Event");
			critcalEventBtn.setTooltip(criticalEventTooltip);
			critcalEventBtn.getStylesheets().add(this.getClass().getResource("/styles/newStyles.css").toString());
			critcalEventBtn.setPrefHeight(sidePane.getHeight() * 0.07);
			critcalEventBtn.setPrefWidth(sidePane.getWidth() * 0.83);
			critcalEventBtn.getStyleClass().add("sidebarCriticalEventBtn");

			btnCriticalEventHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent event) {
					ControllerMediator.getInstance().getEventsController().setIsCriticalEvent(true);
					Main.getInstance().getStageManager().switchSceneAfterMain(FxmlView.EVENTS_LOG);

				}
			};
			critcalEventBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, btnCriticalEventHandler);

			vbEventsList.getChildren().add(critcalEventBtn);
			if (eventLogDetails != null && eventList.size() != 0) {
				for (EventLogModel obj : eventLogDetails) {

					if (obj != null) {

						Button eventBtn = new Button(obj.getEventname());
						Tooltip eventTooltip = new Tooltip(obj.getEventname());
						eventBtn.setTooltip(eventTooltip);
						eventBtn.getStylesheets().add(this.getClass().getResource("/styles/newStyles.css").toString());
						if (obj.getStatus()) {
							String timeArr[] = obj.getEventTime().toString().split(" ");
							timeArr = timeArr[1].split(":");
							String eventTimeStr = timeArr[0] + ":" + timeArr[1];
							eventBtn.getStyleClass().add("sidebarBtnExecuted");
							eventBtn.setText(obj.getEventname() + "\n" + eventTimeStr);
							eventBtn.setDisable(true);

							if (obj.getEventname().equalsIgnoreCase("time in")) {
								EventLogSession.getInstance().setEventTime(obj.getEventTime());
							}

							if (isInit) {
								IntraopEventlog intraopEventLog = new IntraopEventlog();
								intraopEventLog.setCustomEventName(obj.getEventname());
								intraopEventLog.setEventLogId(obj.getEventLogId());
								intraopEventLog.setEventTime(obj.getEventTime());
								if (ExecutedEventSession.getInstance().getExecutedEventList() != null) {

									ExecutedEventSession.getInstance().getExecutedEventList().add(intraopEventLog);
								} else {
									List<IntraopEventlog> eventSessionList = new ArrayList<IntraopEventlog>();
									eventSessionList.add(intraopEventLog);
									ExecutedEventSession.getInstance().setExecutedEventList(eventSessionList);
								}

							}
						} else {
							eventBtn.getStyleClass().add("sidebarBtn");
						}

						eventBtn.setPrefHeight(sidePane.getHeight() * 0.072);
						eventBtn.setPrefWidth(sidePane.getWidth() * 0.83);

						eventBtn.wrapTextProperty().setValue(true);
						if (eventBtn.getText().length() > 17) {
							eventBtn.setStyle(
									String.format("-fx-font-size: %dpx;", (int) (0.07 * sidePane.getWidth())));
						} else {
							eventBtn.setStyle(
									String.format("-fx-font-size: %dpx;", (int) (0.075 * sidePane.getWidth())));
						}

						vbEventsList.getChildren().add(eventBtn);

						btnEventHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
							public void handle(javafx.scene.input.MouseEvent event) {

								if (!obj.getStatus()) {

									if (obj.getEventname().toString().equalsIgnoreCase("Shift Out")) {

										ControllerMediator.getInstance().getMainController()
												.bindPopupFxml(FxmlView.SHIFT_OUT);
										ControllerMediator.getInstance().getPostOperativeController()
												.setBtnEvent(eventBtn);
										ControllerMediator.getInstance().getPostOperativeController()
												.setIsFromDasboard(true);
									} else {

										ControllerMediator.getInstance().getMainController()
												.bindPopupFxml(FxmlView.DASHBOARD_EVENT);
										ControllerMediator.getInstance().getDashboardEventController()
												.setSelectedEventName(obj.getEventname().toString());
										ControllerMediator.getInstance().getDashboardEventController().setEventName();
										ControllerMediator.getInstance().getDashboardEventController()
												.setBtnEvent(eventBtn);
										ControllerMediator.getInstance().getDashboardEventController()
												.setSelectedEventId(Integer.valueOf(obj.getEventId()));

									}

								}

							}
						};
						eventBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, btnEventHandler);
					}
				}

				vbEventsList.setStyle("-fx-background-color:#ffffff");
				vbEventsList.setOpacity(1);

			} else {

				vbEventsList.setStyle("-fx-background-color:#505050");
				vbEventsList.setOpacity(0.5);
				Label lblNoEventFound = new Label(" No events found for" + "\n" + " the selected speciality");
				lblNoEventFound.setStyle("-fx-font-size:34 px");
				lblNoEventFound.setStyle("-fx-font-weight:bold");
				lblNoEventFound.setStyle("-fx-text-fill: #ffffff");
				vbEventsList.getChildren().add(lblNoEventFound);
				vbEventsList.setAlignment(Pos.CENTER);
				vbEventsList.setPrefHeight(Screen.getPrimary().getVisualBounds().getHeight() * 0.83);
				vbEventsList.setPrefWidth(Screen.getPrimary().getVisualBounds().getWidth() * 0.12);
			}
			disablePaneMainContent.setVisible(false);
			return vbEventsList;

		} catch (Exception e) {
			LOG.error("## Exception occured:", e);
			return vbEventsList;
		}
	}

	/**
	 * This method is used to remove all listeners
	 */
	public void removeListeners() {
		try {
			for (EventLogModel obj : eventLogDetails) {

				if (obj != null) {

					Button eventBtn = new Button(obj.getEventname());
					eventBtn.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnEventHandler);
				}
			}
			critcalEventBtn.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnCriticalEventHandler);
		} catch (Exception e) {
			LOG.error("## Exception occured:", e);
		}

	}

}
