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
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.cdac.common.GeneratedEntities.Patientcase;
import com.cdac.common.util.Validations;

import application.FxmlView;
import application.Main;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import model.MasterDataSession;
import model.OTSession;
import model.PatientSession;
import model.SearchCaseModel;
import services.SearchCaseService;

/**
 * This controller is used to Search Scheduled cases and load a Case from
 * searched list
 *
 * @author Sudeep_Sahoo
 *
 */
public class SearchCaseController implements Initializable {

	@FXML
	private Button btnClose;

	@FXML
	private DatePicker datePickerStartDate;

	@FXML
	private DatePicker datePickerEndDate;

	@FXML
	private ChoiceBox<String> choiceOT;

	@FXML
	private Button btnSearch;

	@FXML
	private TableView<SearchCaseModel> tableViewAllCase;

	@FXML
	private TableColumn<SearchCaseModel, Long> columnCRNumber;

	@FXML
	private TableColumn<SearchCaseModel, String> columnFirstName;

	@FXML
	private TableColumn<SearchCaseModel, String> columnLastName;

	@FXML
	private TableColumn<SearchCaseModel, Date> columnDate;

	@FXML
	private TableColumn<SearchCaseModel, String> columnStatus;

	@FXML
	private StackPane stackPaneLoader;

	@FXML
	private Pagination paginator;

	@FXML
	private Label lblError;

	@FXML
	private Button btnLoadcase;

	private EventHandler<WorkerStateEvent> SearchCaseSuccessHandler;
	private EventHandler<WorkerStateEvent> SearchCaseFailedHandler;
	private ChangeListener<Number> paginatorListener;
	private EventHandler<javafx.scene.input.MouseEvent> btnCloseHandler;
	private EventHandler<javafx.scene.input.MouseEvent> btnSearchHandler;
	private EventHandler<javafx.scene.input.MouseEvent> btnLoadCaseHandler;
	private ChangeListener<SearchCaseModel> tableHandler;
	private List<Patientcase> caseList = new ArrayList<Patientcase>();

	private static final Logger LOG = Logger.getLogger(SearchCaseController.class.getName());

	/**
	 * This method initializes the all Event handlers and Default values on page
	 * load
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		try
		{
			columnCRNumber.setCellValueFactory(new PropertyValueFactory<SearchCaseModel, Long>("crnumber"));
			columnDate.setCellValueFactory(new PropertyValueFactory<SearchCaseModel, Date>("plannedProcedureDateTime"));
			columnFirstName.setCellValueFactory(new PropertyValueFactory<SearchCaseModel, String>("firstName"));
			columnLastName.setCellValueFactory(new PropertyValueFactory<SearchCaseModel, String>("lastName"));
			columnStatus.setCellValueFactory(new PropertyValueFactory<SearchCaseModel, String>("status"));

			stackPaneLoader.setVisible(false);
			paginator.setDisable(true);

			List<String> otListString = new ArrayList<>();
			if (MasterDataSession.getInstance().getMasterDataMap().get("OT") != null) {
				otListString.add("Select");
				otListString.addAll(MasterDataSession.getInstance().getMasterDataMap().get("OT"));
			}
			choiceOT.getItems().clear();
			choiceOT.getItems().addAll(otListString);
			choiceOT.getSelectionModel().select(0);

			paginatorListener = new ChangeListener<Number>() {

				@Override
				public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newIndex) {
					try {
						createPage(newIndex.intValue() + 1);
					} catch (Exception e1) {

						LOG.error("## Exception occured:", e1);
					}

				}

			};

			// ---Paginator button clicks
			paginator.currentPageIndexProperty().addListener(paginatorListener);

			tableHandler = new ChangeListener<SearchCaseModel>() {

				@Override
				public void changed(ObservableValue<? extends SearchCaseModel> observable, SearchCaseModel oldValue,
						SearchCaseModel newValue) {

					try
					{
						if (newValue != null) {

							btnLoadcase.setDisable(false);
						}
					}
					catch (Exception e)
					{
						LOG.error("## Exception occured:", e);
					}

				}
			};

			tableViewAllCase.getSelectionModel().selectedItemProperty().addListener(tableHandler);

			btnLoadCaseHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent event) {
					try {
						// ---Save selected row into Patient session

						Patientcase selectedCaseRow = new Patientcase();
						for (Patientcase obj : caseList) {
							if (obj.getCaseId() == tableViewAllCase.getSelectionModel().getSelectedItem().getCaseId()) {
								selectedCaseRow = obj;
							}
						}
						PatientSession.getInstance().setPatientCase(selectedCaseRow);
						PatientSession.getInstance().setPatient(selectedCaseRow.getPatient());
						Main.getInstance().getStageManager().closeSecondaryStage();
						Main.getInstance().getStageManager().switchScene(FxmlView.MAIN);
						removehandler();

					} catch (Exception e1) {

						LOG.error("## Exception occured:", e1);

					}
				}
			};
			btnLoadcase.addEventHandler(MouseEvent.MOUSE_CLICKED, btnLoadCaseHandler);

			btnCloseHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent event) {
					try
					{
						Main.getInstance().getStageManager().closeSecondaryStage();
						removehandler();
					}
					catch (Exception e)
					{
						LOG.error("## Exception occured:", e);

					}
				}
			};
			btnClose.addEventHandler(MouseEvent.MOUSE_CLICKED, btnCloseHandler);

			btnSearchHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent event) {
					try
					{
						searchCase(1);
					}
					catch (Exception e)
					{
						LOG.error("## Exception occured:", e);

					}
				}
			};
			btnSearch.addEventHandler(MouseEvent.MOUSE_CLICKED, btnSearchHandler);

			if (OTSession.getInstance().getSelectedOT() != null) {
				choiceOT.getSelectionModel().select(OTSession.getInstance().getSelectedOT());
				;
			}
			Date input = new Date();
			LocalDate dateEnd = input.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			datePickerEndDate.setValue(dateEnd);
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, -7);
			Date inputStartDate = cal.getTime();
			LocalDate dateStart = inputStartDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			datePickerStartDate.setValue(dateStart);
			searchCase(1);
		}
		catch (Exception e)
		{
			LOG.error("## Exception occured:", e);

		}
	}

	/**
	 * This method is used to search cases on the basis of search criteria
	 * entered
	 *
	 * @param pageIndex
	 */
	private void searchCase(int pageIndex)
	{

		if (validate()) {
			lblError.setVisible(false);
			stackPaneLoader.setVisible(true);
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Date dateStart = null;
			Date dateEnd = null;
			String otName = "";
			if (datePickerStartDate.getValue() != null) {
				try {
					dateStart = simpleDateFormat.parse(datePickerStartDate.getValue() + " " + "00:00");
				} catch (ParseException e) {

					LOG.error("## Exception occured:", e);
				}
			}
			if (datePickerEndDate.getValue() != null) {
				try {
					dateEnd = simpleDateFormat.parse(datePickerEndDate.getValue() + " " + "23:59");
				} catch (ParseException e) {

					LOG.error("## Exception occured:", e);
				}
			}
			if (choiceOT.getSelectionModel().getSelectedIndex() != 0) {
				otName = choiceOT.getSelectionModel().getSelectedItem();
			}
			SearchCaseService searchCaseService = SearchCaseService.getInstance(otName, dateStart, dateEnd, pageIndex);
			searchCaseService.restart();
			// ---on service success

			SearchCaseSuccessHandler = new EventHandler<WorkerStateEvent>() {
				@SuppressWarnings("unchecked")
				@Override
				public void handle(WorkerStateEvent event) {

					try
					{
						Map<String, Object> caseDetails = new HashMap<String, Object>();
						caseDetails = searchCaseService.getCaseList();
						caseList = new ArrayList<Patientcase>();
						if (caseDetails != null) {
							for (Map.Entry<String, Object> entry : caseDetails.entrySet()) {
								String checkStringClass = "";
								Object obj = entry.getValue();
								if (obj.getClass().isInstance(caseList)) {
									caseList = (ArrayList<Patientcase>) obj;
									for (Patientcase caseObj : caseList) {
									}

									// ---populating Grid
									paginator.setDisable(false);

								} else if (obj.getClass().isInstance(checkStringClass)) {

								} else {
									// ---set number of pages in paginator
									paginator.setVisible(true);
									int key = Integer.valueOf(entry.getValue() + "");
									int size = key / 10;
									int remainder = key % 10;
									if (remainder != 0) {
										size = size + 1;
									}
									paginator.setPageCount(size);

								}

							}

						}
						List<SearchCaseModel> searchCaseList = new ArrayList<SearchCaseModel>();
						for (Patientcase obj : caseList) {

							SearchCaseModel searchCaseModelObj = new SearchCaseModel();
							searchCaseModelObj.setCaseId(obj.getCaseId());
							searchCaseModelObj.setCrnumber(obj.getPatient().getCrnumber());
							searchCaseModelObj.setFirstName(obj.getPatient().getFirstName());
							searchCaseModelObj.setLastName(obj.getPatient().getLastName());
							searchCaseModelObj.setPlannedProcedureDateTime(obj.getPlannedProcedureDateTime());
							searchCaseModelObj.setStatus(obj.getStatus());

							searchCaseList.add(searchCaseModelObj);
						}

						tableViewAllCase.getItems().clear();
						tableViewAllCase.getItems().addAll((searchCaseList));
						stackPaneLoader.setVisible(false);

						searchCaseService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
								SearchCaseSuccessHandler);
						searchCaseService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED, SearchCaseFailedHandler);
					}
					catch (Exception e)
					{
						LOG.error("## Exception occured:", e);

					}

				}
			};
			searchCaseService.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, SearchCaseSuccessHandler);
			SearchCaseFailedHandler = new EventHandler<WorkerStateEvent>() {

				@Override
				public void handle(WorkerStateEvent event) {
					try
					{
						stackPaneLoader.setVisible(false);

						Main.getInstance().getUtility().showNotification("Error", "Error",
								searchCaseService.getException().getMessage());
						stackPaneLoader.setVisible(false);
						searchCaseService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
								SearchCaseSuccessHandler);
						searchCaseService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED, SearchCaseFailedHandler);
					}
					catch (Exception e)
					{
						LOG.error("## Exception occured:", e);

					}

				}

			};

			searchCaseService.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED, SearchCaseFailedHandler);
		} else {
			lblError.setVisible(true);
		}

	}

	/**
	 * This method is used to validate the search case form
	 *
	 * @return
	 */
	private Boolean validate()
	{
		if (datePickerStartDate.getValue() != null && datePickerEndDate.getValue() == null) {
			lblError.setText("*Please enter To date.");
			return false;
		}
		if (datePickerStartDate.getValue() == null && datePickerEndDate.getValue() != null) {
			lblError.setText("*Please enter From date.");
			return false;
		}
		if (datePickerEndDate.getValue() != null && datePickerStartDate.getValue() != null) {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

			Date finishTime = null;
			Date startTime = null;
			try {
				startTime = simpleDateFormat.parse(datePickerStartDate.getValue() + "");
				finishTime = simpleDateFormat.parse(datePickerEndDate.getValue() + "");
			} catch (ParseException e) {

				LOG.error("## Exception occured:", e);
			}
			if (!Validations.startEndTimeCheck(startTime, finishTime)) {
				lblError.setText("*To date should be greater than From date.");
				return false;
			}
		}
		return true;
	}

	/**
	 * This method is used to remove listeners and nullify variables
	 */
	private void removehandler()
	{
		tableViewAllCase.getSelectionModel().selectedItemProperty().removeListener(tableHandler);
		btnLoadcase.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnLoadCaseHandler);
		btnClose.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnCloseHandler);
		btnSearch.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnSearchHandler);
		paginator.currentPageIndexProperty().removeListener(paginatorListener);
		caseList = null;
	}

	/**
	 * This method used for pagination of search case table
	 *
	 * @param pageIndex
	 * @throws Exception
	 */
	private void createPage(int pageIndex) throws Exception
	{

		try {
			stackPaneLoader.setVisible(true);
			paginator.setDisable(true);

			searchCase(pageIndex);
		} catch (Exception e) {
			LOG.error("## Exception occured:", e);
			Main.getInstance().getUtility().showNotification("Error", "Error", "Something went wrong");
		}

	}

}
