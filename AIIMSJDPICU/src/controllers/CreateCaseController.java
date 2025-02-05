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
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.cdac.common.GeneratedEntities.Patient;
import com.cdac.common.GeneratedEntities.Patientcase;
import com.cdac.common.util.Validations;
import com.cdac.framework.SystemConfiguration;

import application.FxmlView;
import application.Main;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Platform;
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
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import mediatorPattern.ControllerMediator;
import model.EditCaseFlagSession;
import model.MasterDataSession;
import model.OTSession;
import model.PatientSession;
import services.CreateCaseService;
import services.GetCaseDetailsService;

/**
 * This Controller is used to Create a new case and Edit an existing case
 *
 * @author Sudeep_Sahoo
 *
 */
public class CreateCaseController implements Initializable {

	private static final Logger LOGGER = Logger.getLogger(CreateCaseController.class.getName());

	@FXML
	private Tab tabCaseDetails;

	@FXML
	private ChoiceBox<String> choiceboxSpecialities;

	@FXML
	private TextField textDateTime;

	@FXML
	private DatePicker datepickerAdmitDate;

	@FXML
	private DatePicker datepickerPlanDate;

	@FXML
	private RadioButton radioUnitDayCare;

	@FXML
	private RadioButton radioUnitInPatient;

	@FXML
	private RadioButton radioSurgeryEmergency;

	@FXML
	private RadioButton radioSurgerySemiEmergency;

	@FXML
	private RadioButton radioSurgeryElective;

	@FXML
	private TextArea textAreaDetails;
	
	@FXML
	private ChoiceBox<String> choiceBoxMainCat;
	
	@FXML
	private ChoiceBox<String> choiceBoxDiagnosis1;
	
	@FXML
	private ChoiceBox<String> choiceBoxDiagnosis2;
	
	@FXML
	private ChoiceBox<String> choiceBoxDiagnosis3;
	
	private List<String> diagonsisList;
	/*
	 * @FXML private Tab tabDiagnosis;
	 *
	 * @FXML private RadioButton radioDiagFav;
	 *
	 * @FXML private ToggleGroup diagnosisRadio;
	 *
	 * @FXML private RadioButton radioDiagSearch;
	 *
	 * @FXML private VBox vbDiagFav;
	 *
	 * @FXML private TextField textFieldSelectedDiag;
	 *
	 * @FXML private ListView<String> listViewFavDiag;
	 *
	 * @FXML private Button btnAddFavDiag;
	 *
	 * @FXML private Button btnDeleteFavDiag;
	 *
	 * @FXML private VBox vbDiagSearch;
	 *
	 * @FXML private ChoiceBox<String> choiceBoxCategory;
	 *
	 * @FXML private ChoiceBox<String> choiceBoxSubcategory;
	 *
	 * @FXML private TextField textAutoSearchDiag;
	 *
	 * @FXML private Button btnAddDiagSearch;
	 *
	 * @FXML private Button btnDeleteDiagSearch;
	 *
	 * @FXML private Tab tabPlannedProcedure;
	 *
	 * @FXML private RadioButton radioPlanFav;
	 *
	 * @FXML private ToggleGroup planRadio;
	 *
	 * @FXML private RadioButton radioPlanSearch;
	 *
	 * @FXML private VBox vbPlanFav;
	 *
	 * @FXML private TextField textSelectedPlan;
	 *
	 * @FXML private ListView<String> listViewFavPlannedProcedure;
	 *
	 * @FXML private Button btnAddFavPlan;
	 *
	 * @FXML private Button btnDeleteFavPlan;
	 *
	 * @FXML private VBox vbPlanSearch;
	 *
	 * @FXML private ChoiceBox<String> choiceBoxSection;
	 *
	 * @FXML private ChoiceBox<String> choiceBoxBodySystem;
	 *
	 * @FXML private ChoiceBox<String> choiceBoxOperation;
	 *
	 * @FXML private TextField textAutoSearchPlan;
	 *
	 * @FXML private Button btnAddSearchPlan;
	 *
	 * @FXML private Button btnDeleteSearchPlan;
	 *
	 * @FXML private TableView<CreateCaseTableBean> tableDiagnosis;
	 *
	 * @FXML private TableColumn<CreateCaseTableBean, String> columnDiagnosisId;
	 *
	 * @FXML private TableColumn<CreateCaseTableBean, String>
	 * columnDiagnosisDesc;
	 *
	 * @FXML private TableView<CreateCaseTableBean> tablePlannedProcedure;
	 *
	 * @FXML private TableColumn<CreateCaseTableBean, String> columnPlanId;
	 *
	 * @FXML private TableColumn<CreateCaseTableBean, String> columnPlanDesc;
	 */
	@FXML
	private Button btnReset;

	@FXML
	private Button btnProceed;

	/*
	 * @FXML private Button btnBack;
	 */
	@FXML
	private Button btnClose;

	// @FXML
	// private Button btnPrevPage;

	@FXML
	private TabPane tabPaneCase;

	@FXML
	private TextField textWardNo;

	@FXML
	private ToggleGroup surgeryGroup;

	@FXML
	private ToggleGroup unitGroup;

	/*
	 * @FXML private StackPane stackPaneDiagSearch;
	 *
	 * @FXML private ListView<String> listDiagSearch;
	 *
	 * @FXML private Label lblSelectedSearchDiag;
	 *
	 * @FXML private StackPane stackPanePlanSearch;
	 *
	 * @FXML private ListView<String> listPlanSearch;
	 *
	 * @FXML private Label lblSelectedSearchPlan;
	 */
	@FXML
	private VBox vbWardNo;

	@FXML
	private FontAwesomeIconView prevPageIcon;

	@FXML
	private ListView<String> listViewAnaesthesiologist;

	@FXML
	private ListView<String> listViewOT;

	@FXML
	private TextField txtAnaesthesiologist;

	@FXML
	private TextField txtOtheranaesthesiologist;

	@FXML
	private TextField txtOT;

	@FXML
	private TextArea textAreaPlanDetails;

	@FXML
	private TextArea textAreaDiagnosisDetails;

	@FXML
	private TextField txtSurgeons;

	@FXML
	private Label lblErrorMsg;

	@FXML
	private VBox vbPlanTimeControl;

	@FXML
	private Button btnPlanTime;

	@FXML
	private VBox vbAdmitTimeControl;

	@FXML
	private Button btnAdmitTime;

	@FXML
	private AnchorPane disablePaneMainContent;

	@FXML
	private Label lblPageHeader;

	@FXML
	private Label lblName;

	@FXML
	private Label lblGender;

	@FXML
	private Label lblAge;

	@FXML
	private Label lblCRNum;

	@FXML
	private Label lblHeight;

	@FXML
	private Label lblWeight;

	@FXML
	private Label lblBMI;

	@FXML
	private Label lblBSA;

	private CreateCaseService createCaseService;
	private Patient patientInfo;

	// ---Diagnosis services
	/*
	 * private GetDiagnosisFavoritesService getDiagnosisFavoritesService;
	 * private GetDiagnosisCategoryService getDiagnosisCategoryService; private
	 * GetDiagnosisSubCategoryService getDiagnosisSubCategoryService; private
	 * SearchDiagnosisServcie searchDiagnosisService;
	 */

	// ---PlannedProcedure Service
	/*
	 * private GetPlannedProcedureFavoritesService
	 * getPlannedProcedureFavoritesService; private GetProcedureCategoryService
	 * getProcedureCategoryService; private GetProcedureSubCategoryService
	 * getProcedureSubCategoryService; private GetProcedureSubSubCategoryService
	 * getProcedureSubSubCategoryService; private SearchProcedureService
	 * searchProcedureService;
	 */

	// ---list to be bind with UI controls (dropdowns,listview,choicebox)
	private Patientcase patientCaseObj;

	private List<String> AneastheologistsList;
	private List<String> aneastheologistsSearchList;
	private List<String> operationTheaterList;
	private List<String> OTSearchList;
	/*
	 * private ObservableList<String> diagnosisFavouritesList; private
	 * ObservableList<String> plannedProcedureFavouritesList;
	 */

	// ---list returned from DB services (object lists)
	/*
	 * private List<Diagnosis> diagnosisFavouritesListDB; private
	 * List<Plannedprocedure> plannedProcedureFavouritesListDB; private
	 * List<Diagnosiscategory> diagnosisCategoriesDb; private
	 * List<Procedurecategory> procedureCategoriesDB; private
	 * List<Diagnosissubcategory> diagnosisSubCategoriesDB; private
	 * List<Proceduresubcategory> procedureSubCategoriesDB; private
	 * List<Proceduresubsubcategory> procedureSubSubCategoriesDB; private
	 * List<Diagnosis> diagnosisSearchListDB; private List<Plannedprocedure>
	 * procedureSearchListDB;
	 *
	 * private ObservableList<CreateCaseTableBean> diagnosisList; private
	 * ObservableList<CreateCaseTableBean> plannedProcedureList;
	 */

	private String unitValue = "Day Care";
	private String surgeryTypeValue = "Emergency";

	/*
	 * private boolean planCat = false, planSubCat = false, planSubSubCat =
	 * false; private Boolean isPlanListSearchSelected = false; private Boolean
	 * isDiagListSearchSelected = false;
	 */

	private Boolean isAnasthesioListSearchSelected = false;
	private Boolean isOTListSearchSelected = false;

	private Patientcase editCaseDetails;

	Spinner<String> hourSpinnerCaseAdmit = new Spinner<>();
	Spinner<String> minuteSpinnerCaseAdmit = new Spinner<>();
	Spinner<String> meridiemSpinnerCaseAdmit = new Spinner<>();
	private HBox hBoxCaseAdmitTimeControl;

	Spinner<String> hourSpinnerCasePlan = new Spinner<>();
	Spinner<String> minuteSpinnerCasePlan = new Spinner<>();
	Spinner<String> meridiemSpinnerCasePlan = new Spinner<>();
	private HBox hBoxCasePlanTimeControl;

	private String selectedSpeciality = "";
	private String mainCategory = "";
	
	private String diagnosis1 = "";
	private String diagnosis2 = "";
	private String diagnosis3 = "";
	
	

	// Event handlers for Buttons
	private EventHandler<javafx.scene.input.MouseEvent> btnResetHandler;
	private EventHandler<javafx.scene.input.MouseEvent> btnProceedHandler;
	private EventHandler<javafx.scene.input.MouseEvent> btnCloseHandler;
	// private EventHandler<javafx.scene.input.MouseEvent> btnPrevPageHandler;
	private EventHandler<javafx.scene.input.MouseEvent> btnAdmitTimeHandler;
	private EventHandler<javafx.scene.input.MouseEvent> btnPlanTimeHandler;
	
	//changes by vivek
	private EventHandler<javafx.scene.input.MouseEvent> btnMainCategoryHandler;
	//changes end

	private ChangeListener<String> listViewAnaesthesiologistListener;
	private ChangeListener<String> listViewOTChangeListener;
	private ChangeListener<String> textOTChangeListener;
	private ChangeListener<String> textAnaesthesiologistListener;
	private ChangeListener<Boolean> OtFocusChangeListener;
	private ChangeListener<Boolean> AneathesiaFocusChangeListener;

	private EventHandler<WorkerStateEvent> createCaseSuccessHandler;
	private EventHandler<WorkerStateEvent> createCaseFailedHandler;
	private EventHandler<WorkerStateEvent> GetCaseSuccessHandler;
	private EventHandler<WorkerStateEvent> GetCasesFailedHandler;

	private ChangeListener<Toggle> unitGroupChangeListener;

	private ChangeListener<Toggle> surgeryGroupChangeListener;
	private ChangeListener<String> choiceBoxMainCatListener;

	/**
	 * This method initializes the all Event handlers and Default values on page
	 * load
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		// ---fetch MasterData for major dropdowns
		try {
			if (ControllerMediator.getInstance().getCreateCaseController() == null) {
				ControllerMediator.getInstance().registerCreateCaseController(this);
				/*
				 * try { fetchFavouritesAndCategories(); } catch (Exception e) {
				 *
				 * e.printStackTrace(); }
				 */
			} else {
				// fillFavList();
			}

			txtOT.setText(OTSession.getInstance().getSelectedOT());

			if (MasterDataSession.getInstance().getMasterDataMap() != null
					&& MasterDataSession.getInstance().getMasterDataMap().size() == 0) {
				ControllerMediator.getInstance().getLoginController().fetchMasterData();
				
				
				// fillDropdownValues();
			} else {
				fillDropdownValues();
			}
			
			fillMainCategoryDropdownValues();
			populateMainCategory();
			
			
			// ---- Case Admit Time control

			hBoxCaseAdmitTimeControl = Main.getInstance().getUtility().getTimeControl();
			vbAdmitTimeControl.getChildren().add(hBoxCaseAdmitTimeControl);
			hourSpinnerCaseAdmit = (Spinner) hBoxCaseAdmitTimeControl.getChildren().get(0);
			minuteSpinnerCaseAdmit = (Spinner) hBoxCaseAdmitTimeControl.getChildren().get(2);
			meridiemSpinnerCaseAdmit = (Spinner) hBoxCaseAdmitTimeControl.getChildren().get(3);

			// ----Case Plan Time control

			hBoxCasePlanTimeControl = Main.getInstance().getUtility().getTimeControl();
			vbPlanTimeControl.getChildren().add(hBoxCasePlanTimeControl);
			hourSpinnerCasePlan = (Spinner) hBoxCasePlanTimeControl.getChildren().get(0);
			minuteSpinnerCasePlan = (Spinner) hBoxCasePlanTimeControl.getChildren().get(2);
			meridiemSpinnerCasePlan = (Spinner) hBoxCasePlanTimeControl.getChildren().get(3);
			/*
			 * try { fetchFavouritesAndCategories(); } catch (Exception e6) {
			 *
			 * e6.printStackTrace(); }
			 *
			 * // --- Populate Multiselect dropdowns Anesthesiologists and
			 * Surgeons // populateMultiSelectDropdowns();
			 *
			 * radioDiagFav.setOnAction(e -> { try { DiagRadioSelect(); } catch
			 * (Exception e1) {
			 *
			 * e1.printStackTrace(); }
			 *
			 * });
			 *
			 *
			 * radioDiagSearch.setOnAction(e -> { try { DiagRadioSelect(); }
			 * catch (Exception e1) {
			 *
			 * e1.printStackTrace(); }
			 *
			 * }); radioPlanFav.setOnAction(e -> { try { PlanRadioSelect(); }
			 * catch (Exception e1) {
			 *
			 * e1.printStackTrace(); }
			 *
			 * }); radioPlanSearch.setOnAction(e -> { try { PlanRadioSelect(); }
			 * catch (Exception e1) {
			 *
			 * e1.printStackTrace(); }
			 *
			 * });
			 *
			 *
			 * btnBack.setDisable(true);
			 *
			 * // ---disable add/delete buttons
			 *
			 * / btnAddFavDiag.setDisable(true);
			 * btnDeleteFavDiag.setDisable(true);
			 * btnAddDiagSearch.setDisable(true);
			 * btnDeleteDiagSearch.setDisable(true);
			 * btnAddFavPlan.setDisable(true);
			 * btnDeleteFavPlan.setDisable(true);
			 * btnAddSearchPlan.setDisable(true);
			 * btnDeleteSearchPlan.setDisable(true);
			 *
			 * // ---initialize String lists diagnosisFavouritesList =
			 * FXCollections.observableArrayList();
			 * plannedProcedureFavouritesList =
			 * FXCollections.observableArrayList();
			 *
			 * // procedureSubCategories = FXCollections.observableArrayList();
			 * // procedureSubSubCategories =
			 * FXCollections.observableArrayList(); // diagnosisSearchList =
			 * FXCollections.observableArrayList();
			 *
			 * // ---set table columns columnDiagnosisId.setCellValueFactory(new
			 * PropertyValueFactory<CreateCaseTableBean, String>("Id"));
			 * columnDiagnosisDesc .setCellValueFactory(new
			 * PropertyValueFactory<CreateCaseTableBean,
			 * String>("description")); columnPlanId.setCellValueFactory(new
			 * PropertyValueFactory<CreateCaseTableBean, String>("Id"));
			 * columnPlanDesc.setCellValueFactory(new
			 * PropertyValueFactory<CreateCaseTableBean,
			 * String>("description"));
			 *
			 * // ---setting default observable list to Diagnosis & Procedure //
			 * Favourite // Items tables diagnosisList =
			 * FXCollections.observableArrayList(); plannedProcedureList =
			 * FXCollections.observableArrayList();
			 * tableDiagnosis.setItems(diagnosisList);
			 * tablePlannedProcedure.setItems(plannedProcedureList);
			 */

			// ---set default toggle group and rado buttons
			try {
				setRadioButtons();
			} catch (Exception e5) {

				LOGGER.error("## Exception occured:", e5);
			}

			// ---auto-populate date & time
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yy HH:mm", Locale.ENGLISH);
			textDateTime.setText(simpleDateFormat.format(new java.util.Date()));

			// ---disable all tabs except first one
			/*
			 * tabDiagnosis.setDisable(true);
			 * tabPlannedProcedure.setDisable(true);
			 */

			// ---on Click of Unit Radio buttons
			unitGroupChangeListener = new ChangeListener<Toggle>() {

				@Override
				public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
					if (unitGroup.getSelectedToggle() != null) {
						unitValue = unitGroup.getSelectedToggle().getUserData().toString();
						if (unitValue.equalsIgnoreCase("Day Care")) {
							vbWardNo.setVisible(false);
							textWardNo.setText("");
						} else if (unitValue.equalsIgnoreCase("In patient")) {
							vbWardNo.setVisible(true);
						}
					}
				}
			};
			unitGroup.selectedToggleProperty().addListener(unitGroupChangeListener);
			// ---on Click of Surgery Type Radio buttons
			surgeryGroupChangeListener = new ChangeListener<Toggle>() {

				@Override
				public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
					if (surgeryGroup.getSelectedToggle() != null) {
						surgeryTypeValue = surgeryGroup.getSelectedToggle().getUserData().toString();

					}
				}
			};
			surgeryGroup.selectedToggleProperty().addListener(surgeryGroupChangeListener);
			
			

			/*
			 * // ---on Click of Diagnosis Category dropdown
			 * choiceBoxCategory.getSelectionModel().selectedItemProperty()
			 * .addListener((ObservableValue<? extends String> observable,
			 * String oldValue, String newValue) -> { if (newValue != null) {
			 * System.out.println("Diagnosis category----" + newValue);
			 * choiceBoxSubcategory.setItems(FXCollections.observableArrayList()
			 * ); int categoryId = extractDiagnosisCategoryId(newValue,
			 * diagnosisCategoriesDb); try {
			 * getDiagnosisSubCategory(categoryId); } catch (Exception e1) {
			 *
			 * e1.printStackTrace(); }
			 *
			 * } });
			 *
			 * // ---on Click of Procedure Category dropdown
			 * choiceBoxSection.getSelectionModel().selectedItemProperty()
			 * .addListener((ObservableValue<? extends String> observable,
			 * String oldValue, String newValue) -> { if (newValue != null) {
			 * System.out.println("Procedure Category----" + newValue); if
			 * (!newValue.equalsIgnoreCase("Select")) { planCat = true;
			 * planSubCat = false; planSubSubCat = false; } else { planCat =
			 * false; planSubCat = false; planSubSubCat = false; }
			 * choiceBoxBodySystem.setItems(FXCollections.observableArrayList())
			 * ;
			 * choiceBoxOperation.setItems(FXCollections.observableArrayList());
			 * String categoryCode = extractProcedureCategoryId(newValue,
			 * procedureCategoriesDB); try {
			 * getProcedureSubCategory(categoryCode); } catch (Exception e1) {
			 *
			 * e1.printStackTrace(); } } });
			 *
			 * // ---on Click of Procedure Sub Category dropdown
			 * choiceBoxBodySystem.getSelectionModel().selectedItemProperty()
			 * .addListener((ObservableValue<? extends String> observable,
			 * String oldValue, String newValue) -> {
			 *
			 * if (newValue != null) { System.out.println(
			 * "Procedure Sub Category----" + newValue); if
			 * (!newValue.equalsIgnoreCase("Select")) { planCat = false;
			 * planSubCat = true; planSubSubCat = false; } else {
			 *
			 * planSubCat = false; planSubSubCat = false; }
			 * choiceBoxOperation.setItems(FXCollections.observableArrayList());
			 * String subCategoryCode = extractProcedureSubCategoryId(newValue,
			 * procedureSubCategoriesDB); try {
			 * getProcedureSubSubCategory(subCategoryCode); } catch (Exception
			 * e1) {
			 *
			 * e1.printStackTrace(); } }
			 *
			 * });
			 *
			 * // ---on Click of Procedure Sub Sub Category dropdown
			 * choiceBoxOperation.getSelectionModel().selectedItemProperty()
			 * .addListener((ObservableValue<? extends String> observable,
			 * String oldValue, String newValue) -> { if (newValue != null) { if
			 * (!newValue.equalsIgnoreCase("Select")) { planCat = false;
			 * planSubCat = false; planSubSubCat = true; } } });
			 *
			 * // ---DIAGNOSIS search textbox SearchDiagnosis searchDiagnosisObj
			 * = new SearchDiagnosis();
			 * textAutoSearchDiag.textProperty().addListener((observable,
			 * oldValue, newValue) -> {
			 *
			 * if (!isDiagListSearchSelected) {
			 *
			 * lblSelectedSearchDiag.setText("");
			 * listDiagSearch.getSelectionModel().clearSelection(); if
			 * (Validations.isAlphaNumericWithSpace(newValue)) {
			 * lblErrorMsg.setVisible(false); if (5 <= newValue.length() &&
			 * !newValue.isEmpty() && !("").equals(newValue)) {
			 * textAutoSearchDiag.setDisable(true); System.out.println(
			 * "DIAGNOSIS search textbox changed from -----" + oldValue + " to "
			 * + newValue);
			 *
			 * // ---create searchDiagnosis Object
			 * searchDiagnosisObj.setDiagnosisValue(newValue);
			 *
			 * // int categoryId = extractDiagnosisCategoryId( //
			 * choiceBoxCategory.getSelectionModel().getSelectedItem().toString(
			 * ), // diagnosisCategoriesDb); int categoryId = -1; if
			 * (choiceBoxCategory.getSelectionModel().getSelectedIndex() != 0) {
			 * String categoryIdStr =
			 * MasterDataSession.getInstance().getMasterDataMap()
			 * .get("DiagCategoriesId")
			 * .get(choiceBoxCategory.getSelectionModel().getSelectedIndex());
			 * categoryId = Integer.valueOf(categoryIdStr); } if (categoryId !=
			 * -1) { searchDiagnosisObj.setDiagnosisCategoryID(categoryId); }
			 * else { searchDiagnosisObj.setDiagnosisCategoryID(null); }
			 *
			 * int subCategoryId = extractDiagnosisSubCategoryId(
			 * choiceBoxSubcategory.getSelectionModel().getSelectedItem().
			 * toString(), diagnosisSubCategoriesDB); if (subCategoryId != -1) {
			 * searchDiagnosisObj.setDiagnosisSubCategoryID(subCategoryId); }
			 * else { searchDiagnosisObj.setDiagnosisSubCategoryID(null); }
			 *
			 * searchDiagnosisObj.setVersion("ICD-10");
			 *
			 * // ---pass object to SearchDaoServie try {
			 * callSearchDiagnosisServie(searchDiagnosisObj); } catch (Exception
			 * e1) {
			 *
			 * e1.printStackTrace(); }
			 *
			 * } } else { lblErrorMsg.setText(
			 * "* Please enter valid input to search Diagnosis.");
			 * lblErrorMsg.setVisible(true);
			 *
			 * } } else { isDiagListSearchSelected = false;
			 * lblErrorMsg.setVisible(false); } });
			 *
			 * // ---Diagnosis Search list
			 * listDiagSearch.focusedProperty().addListener(new
			 * ChangeListener<Boolean>() {
			 *
			 * @Override public void changed(ObservableValue<? extends Boolean>
			 * arg0, Boolean oldPropertyValue, Boolean newPropertyValue) { if
			 * (!newPropertyValue) { listDiagSearch.setVisible(false); } } });
			 * listDiagSearch.getSelectionModel().selectedItemProperty().
			 * addListener(new ChangeListener<String>() {
			 *
			 * @Override public void changed(ObservableValue<? extends String>
			 * observable, String oldValue, String newValue) {
			 *
			 * Platform.runLater(new Runnable() {
			 *
			 * @Override public void run() {
			 *
			 * textAutoSearchDiag.setText(newValue); } });
			 * isDiagListSearchSelected = true;
			 * listDiagSearch.setVisible(false);
			 * btnAddDiagSearch.setDisable(false); } });
			 *
			 * // ---PROCEDURE search textbox
			 * textAutoSearchPlan.textProperty().addListener((observable,
			 * oldValue, newValue) -> { System.out.println("Selected####" +
			 * isPlanListSearchSelected); if (!isPlanListSearchSelected) {
			 *
			 * lblSelectedSearchPlan.setText("");
			 * listPlanSearch.getSelectionModel().clearSelection(); if
			 * (Validations.isAlphaNumericWithSpace(newValue)) {
			 * lblErrorMsg.setVisible(false); if (5 <= newValue.length() &&
			 * !newValue.isEmpty() && !("").equals(newValue)) {
			 * textAutoSearchPlan.setDisable(true); System.out.println(
			 * "PROCEDURE search textbox changed from -----" + oldValue + " to "
			 * + newValue);
			 *
			 * String Id = ""; if (planCat) { Id = extractProcedureCategoryId(
			 * choiceBoxSection.getSelectionModel().getSelectedItem().toString()
			 * , procedureCategoriesDB); } if (planSubCat) { Id =
			 * extractProcedureSubCategoryId(
			 * choiceBoxBodySystem.getSelectionModel().getSelectedItem().
			 * toString(), procedureSubCategoriesDB);
			 *
			 * } if (planSubSubCat) { Id = extractProcedureSubSubCategoryId(
			 * choiceBoxOperation.getSelectionModel().getSelectedItem().toString
			 * (), procedureSubSubCategoriesDB);
			 *
			 * } if (!planCat && !planSubCat && !planSubSubCat) { Id = ""; }
			 *
			 * try { callSearchProcedureServie(Id,
			 * textAutoSearchPlan.getText()); } catch (Exception e1) {
			 *
			 * e1.printStackTrace(); }
			 *
			 * } }
			 *
			 * else { lblErrorMsg.setText(
			 * "* Please enter valid input to search Planned Procedure.");
			 * lblErrorMsg.setVisible(true);
			 *
			 * }
			 *
			 * } else { isPlanListSearchSelected = false;
			 * lblErrorMsg.setVisible(false); }
			 *
			 * });
			 *
			 * // ---Procedure Search list
			 * listPlanSearch.focusedProperty().addListener(new
			 * ChangeListener<Boolean>() {
			 *
			 * @Override public void changed(ObservableValue<? extends Boolean>
			 * arg0, Boolean oldPropertyValue, Boolean newPropertyValue) { if
			 * (!newPropertyValue) { listPlanSearch.setVisible(false); } } });
			 * listPlanSearch.getSelectionModel().selectedItemProperty().
			 * addListener(new ChangeListener<String>() {
			 *
			 * @Override public void changed(ObservableValue<? extends String>
			 * observable, String oldValue, String newValue) {
			 *
			 * Platform.runLater(new Runnable() {
			 *
			 * @Override public void run() {
			 *
			 * textAutoSearchPlan.setText(newValue); } });
			 *
			 * isPlanListSearchSelected = true;
			 * listPlanSearch.setVisible(false);
			 * btnAddSearchPlan.setDisable(false); } });
			 *
			 * try { diagnosisFavouriteAddDelFunc(); } catch (Exception e4) {
			 *
			 * e4.printStackTrace(); }
			 *
			 * try { diagnosisSearchAddDelFunc(); } catch (Exception e4) {
			 *
			 * e4.printStackTrace(); }
			 *
			 * try { planFavouriteAddDelFunc(); } catch (Exception e3) {
			 *
			 * e3.printStackTrace(); }
			 *
			 * try { planSearchAddDelFunc(); } catch (Exception e2) {
			 *
			 * e2.printStackTrace(); }
			 */
			// if
			// (Main.getInstance().getLastFxmlScreen().equalsIgnoreCase("CreatePatient"))
			// {
			// btnPrevPage.setDisable(true);
			// btnPrevPage.setStyle("-fx-opacity: 1;");
			// prevPageIcon.setVisible(false);
			//
			// }

			// btnPrevPageHandler = new
			// EventHandler<javafx.scene.input.MouseEvent>() {
			// public void handle(javafx.scene.input.MouseEvent event) {
			//
			// Scene scene = Main.getInstance().getParentScene();
			// StackPane root = (StackPane) scene.getRoot();
			// if (root.getChildren().size() == 5) {
			// root.getChildren().remove(4);
			// root.getChildren().get(3).setVisible(true);
			// } else if (root.getChildren().size() == 4) {
			// root.getChildren().remove(3);
			// root.getChildren().get(2).setVisible(true);
			// }
			//
			// Stage stage = Main.getInstance().getParentStage();
			// stage.setScene(scene);
			// stage.show();
			// }
			// };
			// btnPrevPage.addEventHandler(MouseEvent.MOUSE_CLICKED,
			// btnPrevPageHandler);

			btnCloseHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent event) {
					if (EditCaseFlagSession.getInstance() != null) {
						if (EditCaseFlagSession.getInstance().getIsEditFromDashboard()) {
							try {
								closeEditCasePopup();
							} catch (Exception e1) {

								LOGGER.error("## Exception occured:", e1);
							}

						} else {
							try {
								closeCreateCasePopup();
							} catch (Exception e1) {

								LOGGER.error("## Exception occured:", e1);
							}
						}

					}

				}
			};
			
			btnClose.addEventHandler(MouseEvent.MOUSE_CLICKED, btnCloseHandler);
			
			btnResetHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent event) {
					String selectedItem = tabPaneCase.getSelectionModel().getSelectedItem().getText();

					if (("Case Details").equals(selectedItem)) {
						choiceboxSpecialities.getSelectionModel().select(0);
						datepickerAdmitDate.setValue(null);
						// timePickeradmitTime.setValue(null);
						datepickerPlanDate.setValue(null);
						// timePickerPlanTime.setValue(null);
						textWardNo.setText("");
						unitGroup.selectToggle(radioUnitDayCare);
						surgeryGroup.selectToggle(radioSurgeryEmergency);
						textAreaDetails.setText("");
						txtAnaesthesiologist.setText("");
						txtOT.setText("");
						txtOtheranaesthesiologist.setText("");
						txtSurgeons.setText("");
						choiceBoxDiagnosis1.getItems().clear();
						choiceBoxDiagnosis2.getItems().clear();
						choiceBoxDiagnosis3.getItems().clear();
						choiceBoxMainCat.getSelectionModel().select(0);
						choiceBoxDiagnosis1.setDisable(true);
						choiceBoxDiagnosis2.setDisable(true);
						choiceBoxDiagnosis3.setDisable(true);
						try {
							resetTime();
						} catch (Exception e1) {

							LOGGER.error("## Exception occured:", e1);
						}
						// populateMultiSelectDropdowns();

					}
					/*
					 * else if (("Diagnosis").equals(selectedItem)) {
					 * listViewFavDiag.getSelectionModel().clearSelection();
					 * btnAddFavDiag.setDisable(true);
					 * btnDeleteFavDiag.setDisable(true);
					 *
					 * textAutoSearchDiag.setText("");
					 * listDiagSearch.getSelectionModel().clearSelection();
					 * btnAddDiagSearch.setDisable(true);
					 * btnDeleteDiagSearch.setDisable(true);
					 * textAreaDiagnosisDetails.setText("");
					 * diagnosisList.clear();
					 * tableDiagnosis.setItems(diagnosisList); } else if ((
					 * "Planned Procedure").equals(selectedItem)) {
					 * listViewFavPlannedProcedure.getSelectionModel().
					 * clearSelection(); btnAddFavPlan.setDisable(true);
					 * btnDeleteFavPlan.setDisable(true);
					 * textAreaPlanDetails.setText("");
					 * textAutoSearchPlan.setText("");
					 * listPlanSearch.getSelectionModel().clearSelection();
					 * btnAddSearchPlan.setDisable(true);
					 * btnDeleteSearchPlan.setDisable(true);
					 *
					 * plannedProcedureList.clear();
					 * tablePlannedProcedure.setItems(plannedProcedureList); }
					 */

				}
			};
			btnReset.addEventHandler(MouseEvent.MOUSE_CLICKED, btnResetHandler);
			
	 choiceBoxMainCatListener = new ChangeListener<String>() {

				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					if (newValue != null) {
						try {
							//choiceVeinDetails.setDisable(false);
							if (!oldValue.equals(newValue)) {
								// choiceVeinDetails.setItems(subclavianVeinDetailList);
								choiceBoxDiagnosis1.getItems().clear();
								choiceBoxDiagnosis2.getItems().clear();
								choiceBoxDiagnosis3.getItems().clear();
								if(!newValue.equals("Select")){
									diagonsisList = MasterDataSession.getInstance().getMasterDataMap().get(newValue);
									choiceBoxDiagnosis1.getItems().add("Select");
									choiceBoxDiagnosis2.getItems().add("Select");
									choiceBoxDiagnosis3.getItems().add("Select");
									choiceBoxDiagnosis1.getItems().addAll(diagonsisList);
									choiceBoxDiagnosis2.getItems().addAll(diagonsisList);
									choiceBoxDiagnosis3.getItems().addAll(diagonsisList);
									choiceBoxDiagnosis1.getSelectionModel().select(0);
									choiceBoxDiagnosis2.getSelectionModel().select(0);
									choiceBoxDiagnosis3.getSelectionModel().select(0);
									choiceBoxDiagnosis1.setDisable(false);
									choiceBoxDiagnosis2.setDisable(false);
									choiceBoxDiagnosis3.setDisable(false);
								}
								else
								{
									
									choiceBoxDiagnosis1.setDisable(true);
									choiceBoxDiagnosis2.setDisable(true);
									choiceBoxDiagnosis3.setDisable(true);
								}
							}
							// choiceVeinDetails.getSelectionModel().select(0);
						} catch (Exception e) {
							LOGGER.error("## Exception occured:", e);

						}

					}
				}
			};

			choiceBoxMainCat.getSelectionModel().selectedItemProperty().addListener(choiceBoxMainCatListener);
		
			// ---on click of back button, navigate to previous tab
			/*
			 * btnBack.setOnAction(new EventHandler<ActionEvent>() {
			 *
			 * @Override public void handle(ActionEvent event) {
			 * btnAddFavDiag.setDisable(true);
			 * btnDeleteFavDiag.setDisable(true);
			 * btnAddDiagSearch.setDisable(true);
			 * btnDeleteDiagSearch.setDisable(true);
			 * tableDiagnosis.getSelectionModel().clearSelection();
			 * listViewFavDiag.getSelectionModel().clearSelection(); //
			 * listDiagSearch.getSelectionModel().clearSelection();
			 * listDiagSearch.setVisible(false);
			 * lblSelectedSearchDiag.setVisible(false);
			 * textAutoSearchDiag.setText("");
			 *
			 * btnProceed.setText("Proceed"); String selectedItem =
			 * tabPaneCase.getSelectionModel().getSelectedItem().getText();
			 *
			 * if (("Diagnosis").equals(selectedItem)) {
			 * lblErrorMsg.setVisible(false);
			 * tabPaneCase.getSelectionModel().select(tabCaseDetails);
			 * btnBack.setDisable(true);
			 *
			 * tabCaseDetails.setDisable(false); tabDiagnosis.setDisable(true);
			 * tabPlannedProcedure.setDisable(true);
			 *
			 * } else if (("Planned Procedure").equals(selectedItem)) {
			 * tabPaneCase.getSelectionModel().select(tabDiagnosis);
			 *
			 * tabCaseDetails.setDisable(true); tabDiagnosis.setDisable(false);
			 * tabPlannedProcedure.setDisable(true);
			 *
			 * } } });
			 */

			btnProceedHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent event) {
					// TODO Auto-generated method stub
					/*
					 * btnBack.setDisable(false);
					 *
					 * btnAddFavDiag.setDisable(true);
					 * btnDeleteFavDiag.setDisable(true);
					 * btnAddDiagSearch.setDisable(true);
					 * btnDeleteDiagSearch.setDisable(true);
					 * btnAddFavPlan.setDisable(true);
					 * btnDeleteFavPlan.setDisable(true);
					 * btnAddSearchPlan.setDisable(true);
					 * btnDeleteSearchPlan.setDisable(true);
					 *
					 * tableDiagnosis.getSelectionModel().clearSelection();
					 * listViewFavDiag.getSelectionModel().clearSelection(); //
					 * listDiagSearch.getSelectionModel().clearSelection();
					 * listDiagSearch.setVisible(false);
					 * lblSelectedSearchDiag.setVisible(false);
					 * textAutoSearchDiag.setText("");
					 *
					 * listPlanSearch.setVisible(false);
					 * lblSelectedSearchPlan.setVisible(false);
					 * textAutoSearchPlan.setText("");
					 *
					 * tablePlannedProcedure.getSelectionModel().clearSelection(
					 * ); listViewFavPlannedProcedure.getSelectionModel().
					 * clearSelection();
					 */

					/*---Done By Sudeep .
					 * As diagnosis and Planned procedure tabs are removed
					 * boolean isLastTab = false;
					 *  */
					boolean isLastTab = true;
					try {
						if (tabPaneCase.getSelectionModel().getSelectedItem() == null) {
							tabPaneCase.getSelectionModel().select(tabCaseDetails);
						}
						// String selectedItem =
						// tabPaneCase.getSelectionModel().getSelectedItem().getText();
						String selectedItem = "Case Details";
						if (btnProceed.getText().equalsIgnoreCase("save")
								|| btnProceed.getText().equalsIgnoreCase("Update")) {

							/*
							 * ---------------Added By Sudeep as planned
							 * procedure and diagnosis removed validation called
							 * from here------------------
							 */

							if (validateCaseDetails()) {
								// tabPaneCase.getSelectionModel().select(tabDiagnosis);
								lblErrorMsg.setVisible(false);
								tabCaseDetails.setDisable(true);
								// tabDiagnosis.setDisable(false);
								// tabPlannedProcedure.setDisable(true);
							} else {
								lblErrorMsg.setVisible(true);
								isLastTab = false;
							}

							// if (validatePlannedProcedure()) {
							// lblErrorMsg.setVisible(false);
							// isLastTab = true;
							// } else {
							// lblErrorMsg.setVisible(true);
							// }
						} else if (("Case Details").equals(selectedItem)) {
							if (validateCaseDetails()) {
								// tabPaneCase.getSelectionModel().select(tabDiagnosis);
								lblErrorMsg.setVisible(false);
								tabCaseDetails.setDisable(true);
								// tabDiagnosis.setDisable(false);
								// tabPlannedProcedure.setDisable(true);
							} else {
								lblErrorMsg.setVisible(true);
							}

						} else if (("Diagnosis").equals(selectedItem)) {
							if (validateDiagnosis()) {
								lblErrorMsg.setVisible(false);
								// tabPaneCase.getSelectionModel().select(tabPlannedProcedure);

								if (EditCaseFlagSession.getInstance().getIsEdit()) {
									btnProceed.setText("Update");
								} else {
									btnProceed.setText("Save");
								}

								tabCaseDetails.setDisable(true);
								// tabDiagnosis.setDisable(true);
								// tabPlannedProcedure.setDisable(false);

							} else {
								lblErrorMsg.setVisible(true);
							}
						}

						if (isLastTab) {
							disablePaneMainContent.setVisible(true);

							if (choiceboxSpecialities.getSelectionModel().getSelectedIndex() != 0) {

								lblErrorMsg.setVisible(false);
								SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm",
										Locale.ENGLISH);
								patientCaseObj = new Patientcase();

								if (datepickerAdmitDate.getValue() != null
										&& Main.getInstance().getUtility().getTime(hBoxCaseAdmitTimeControl) != null) {

									String admitDateTime = datepickerAdmitDate.getValue() + " "
											+ Main.getInstance().getUtility().getTime(hBoxCaseAdmitTimeControl);
									Date admitDate = simpleDateFormat.parse(admitDateTime);
									patientCaseObj.setAdmitDateTime(admitDate);
								}
								if (datepickerPlanDate.getValue() != null
										&& Main.getInstance().getUtility().getTime(hBoxCasePlanTimeControl) != null) {
									String planDateTime = datepickerPlanDate.getValue() + " "
											+ Main.getInstance().getUtility().getTime(hBoxCasePlanTimeControl);
									Date planDate = simpleDateFormat.parse(planDateTime);
									patientCaseObj.setPlannedProcedureDateTime(planDate);
								}
								String mainCategory = choiceBoxMainCat.getSelectionModel().getSelectedItem();
								

									if(!mainCategory.equals("Select")){
										
										patientCaseObj.setMainCategory(mainCategory);
										String diagnosis1=choiceBoxDiagnosis1.getSelectionModel().getSelectedItem();
										String diagnosis2=choiceBoxDiagnosis2.getSelectionModel().getSelectedItem();
										String diagnosis3=choiceBoxDiagnosis3.getSelectionModel().getSelectedItem();
										if(!diagnosis1.equals("Select"))
										patientCaseObj.setDiagnosis1(diagnosis1);
										if(!diagnosis2.equals("Select"))
										patientCaseObj.setDiagnosis2(diagnosis2);
										if(!diagnosis3.equals("Select"))
										patientCaseObj.setDiagnosis3(diagnosis3);
									}
								patientCaseObj.setCaseDateTime(new Date());
								patientCaseObj.setoT(txtOT.getText());
								patientCaseObj.setAnesthesiologists(txtAnaesthesiologist.getText());
								patientCaseObj.setSurgeons(txtSurgeons.getText());
								patientCaseObj.setSurgeryType(surgeryTypeValue);
								patientCaseObj.setUnit(unitValue);
								patientCaseObj.setStatus("Open");
								patientCaseObj.setDetails(textAreaDetails.getText());
								patientCaseObj.setWardNumber(textWardNo.getText());
								patientCaseObj.setDiagnosisText(textAreaDiagnosisDetails.getText());
								patientCaseObj.setPlannedProcedureText(textAreaPlanDetails.getText());
								patientCaseObj.setOtherAnesthesiologists(txtOtheranaesthesiologist.getText());							
								

								// ---setting diagnosis values and ids
								String diagnosisId = "", diagnosisVal = "";
								/*
								 * for (CreateCaseTableBean bean :
								 * diagnosisList) { diagnosisId = diagnosisId +
								 * bean.getId() + ","; diagnosisVal =
								 * diagnosisVal + bean.getDescription() + "|"; }
								 * if (!diagnosisId.isEmpty()) { diagnosisId =
								 * diagnosisId.substring(0, diagnosisId.length()
								 * - 1); } if (!diagnosisVal.isEmpty()) {
								 * diagnosisVal = diagnosisVal.substring(0,
								 * diagnosisVal.length() - 1); }
								 */
								patientCaseObj.setDiagnosis(diagnosisId);
								patientCaseObj.setDiagnosisValues(diagnosisVal);

								// ---setting planned procedure values and ids
								String planID = "", planVal = "";
								/*
								 * for (CreateCaseTableBean bean :
								 * plannedProcedureList) { planID = planID +
								 * bean.getId() + ","; planVal = planVal +
								 * bean.getDescription() + "|"; } if
								 * (!planID.isEmpty()) { planID =
								 * planID.substring(0, planID.length() - 1); }
								 * if (!planVal.isEmpty()) { planVal =
								 * planVal.substring(0, planVal.length() - 1); }
								 */
								patientCaseObj.setPlannedProcedure(planID);
								patientCaseObj.setPlannedProcedureValues(planVal);

								List<String> specialitiesKeysList = MasterDataSession.getInstance().getMasterDataMap()
										.get("SpecialitiesKeys");
								int selectedIndex = choiceboxSpecialities.getSelectionModel().getSelectedIndex();
								patientCaseObj.setSpeciality(specialitiesKeysList.get(selectedIndex - 1));

								if (EditCaseFlagSession.getInstance().getIsEdit()) {
									patientCaseObj.setCaseId(EditCaseFlagSession.getInstance().getCaseId());
								}

								createCaseService = CreateCaseService.getInstance(patientCaseObj,
										PatientSession.getInstance().getPatient().getPatientId());

								/*
								 * tabPlannedProcedure.setDisable(true);
								 * btnBack.setDisable(true);
								 */
								btnReset.setDisable(true);
								callCreateCaseService();

							} else {

								lblErrorMsg.setText("* Please Enter mandatory fields.");
								lblErrorMsg.setVisible(true);
							}
						}

						Main.getInstance().getUtility().warningDialogue("Information",
								"Please do not forget to scan the devices in the beginning.");

					} catch (Exception e) {
						LOGGER.error("## Exception occured:", e);

					}

				}
			};
			btnProceed.addEventHandler(MouseEvent.MOUSE_CLICKED, btnProceedHandler);

			listViewAnaesthesiologistListener = new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

					Platform.runLater(new Runnable() {
						@Override
						public void run() {

							isAnasthesioListSearchSelected = true;
							txtAnaesthesiologist.setText(newValue);

							listViewAnaesthesiologist.setVisible(false);
							lblErrorMsg.setVisible(false);

						}
					});

				}
			};

			listViewAnaesthesiologist.getSelectionModel().selectedItemProperty()
					.addListener(listViewAnaesthesiologistListener);

			listViewOTChangeListener = new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					Platform.runLater(new Runnable() {
						@Override
						public void run() {

							isOTListSearchSelected = true;

							txtOT.setText(newValue);
							listViewOT.setVisible(false);
							lblErrorMsg.setVisible(false);

						}
					});

				}
			};

			listViewOT.getSelectionModel().selectedItemProperty().addListener(listViewOTChangeListener);

			textAnaesthesiologistListener = new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					if (newValue != null && newValue != "") {
						if (!isAnasthesioListSearchSelected) {

							if (Validations.isAlphaNumericWithSpaceAndSpecialCharacter(newValue)) {
								lblErrorMsg.setVisible(false);

								if (3 <= newValue.length() && !newValue.isEmpty() && !("").equals(newValue)) {

									try {
										searchAneasthesiologists();
									} catch (Exception e1) {

										LOGGER.error("## Exception occured:", e1);
									}
								} else {
									listViewAnaesthesiologist.setVisible(false);
								}
							} else {
								if (!newValue.isEmpty()) {
									lblErrorMsg.setText("* Please enter valid input to search Aneasthesiologists.");
									lblErrorMsg.setVisible(true);
								}
								listViewAnaesthesiologist.getItems().addAll(new ArrayList());
							}
						} else {
							isAnasthesioListSearchSelected = false;
							listViewAnaesthesiologist.setVisible(false);
						}
					}
				}
			};
			txtAnaesthesiologist.textProperty().addListener(textAnaesthesiologistListener);

			textOTChangeListener = new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

					if (newValue != null && newValue != "") {
						if (!isOTListSearchSelected) {

							if (Validations.isAlphaNumericWithSpace(newValue)) {
								lblErrorMsg.setVisible(false);
								if (3 <= newValue.length() && !newValue.isEmpty() && !("").equals(newValue)) {

									try {
										searchOT();
									} catch (Exception e1) {

										LOGGER.error("## Exception occured:", e1);
									}
								} else {
									listViewOT.setVisible(false);
								}
							} else {
								if (!newValue.isEmpty()) {
									lblErrorMsg.setText("* Please enter valid input to search OT.");
									lblErrorMsg.setVisible(true);
								}
								listViewOT.getItems().addAll(new ArrayList<>());
							}
						} else {
							isOTListSearchSelected = false;
							listViewOT.setVisible(false);
						}
					}
				}
			};
			txtOT.textProperty().addListener(textOTChangeListener);

			OtFocusChangeListener = new ChangeListener<Boolean>() {
				@Override
				public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue,
						Boolean newPropertyValue) {
					if (!newPropertyValue) {
						listViewOT.setVisible(false);
						// txtOT.setText("");
					}

				}
			};

			txtOT.focusedProperty().addListener(OtFocusChangeListener);

			AneathesiaFocusChangeListener = new ChangeListener<Boolean>() {
				@Override
				public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue,
						Boolean newPropertyValue) {
					if (!newPropertyValue) {
						listViewAnaesthesiologist.setVisible(false);
						// txtAnaesthesiologist.setText("");
					}

				}
			};

			txtAnaesthesiologist.focusedProperty().addListener(AneathesiaFocusChangeListener);

			btnAdmitTimeHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent event) {
					LocalTime timeNow = Main.getInstance().getUtility().getLocalSystemTime();
					try {
						setCaseAdmitTime(timeNow.toString());
					} catch (Exception e1) {

						LOGGER.error("## Exception occured:", e1);
					}
					Date input = new Date();
					LocalDate date = input.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
					datepickerAdmitDate.setValue(date);
				}
			};
			btnAdmitTime.addEventHandler(MouseEvent.MOUSE_CLICKED, btnAdmitTimeHandler);

			btnPlanTimeHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent event) {
					LocalTime timeNow = Main.getInstance().getUtility().getLocalSystemTime();
					try {
						setCasePlanTime(timeNow.toString());
					} catch (Exception e1) {

						LOGGER.error("## Exception occured:", e1);
					}
					Date input = new Date();
					LocalDate date = input.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
					datepickerPlanDate.setValue(date);
				}
			};
			btnPlanTime.addEventHandler(MouseEvent.MOUSE_CLICKED, btnPlanTimeHandler);

			if (EditCaseFlagSession.getInstance() != null) {

				if (EditCaseFlagSession.getInstance().getIsEdit()) {
					disablePaneMainContent.setVisible(true);
					lblPageHeader.setText("Edit Case");
					choiceboxSpecialities.setDisable(true);
					try {
						if (EditCaseFlagSession.getInstance().getIsEditFromDashboard()) {
							disablePaneMainContent.setVisible(true);
							if (PatientSession.getInstance().getPatientCase() != null) {
								editCaseDetails = PatientSession.getInstance().getPatientCase();
								fillEditCaseForm();
							}

						} else {
							getCaseDetails();
						}

					} catch (Exception e1) {

						LOGGER.error("## Exception occured:", e1);
					}

				} else {
					Date input = new Date();
					LocalDate date = input.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
					datepickerPlanDate.setValue(date);
					LocalTime timeNow = Main.getInstance().getUtility().getLocalSystemTime();
					setCasePlanTime(timeNow.toString());
				}
			}

			// ---Set Patient Details Header
			patientInfo = PatientSession.getInstance().getPatient();
			if (patientInfo != null) {
				lblName.setText(patientInfo.getFirstName() + " " + patientInfo.getLastName());
				lblGender.setText(patientInfo.getGender());
				lblAge.setText(String.valueOf(patientInfo.getAge()) + " " + patientInfo.getAgeUnit());
				lblCRNum.setText(String.valueOf(patientInfo.getCrnumber()));
				lblHeight.setText(String.valueOf(patientInfo.getHeight()));
				lblWeight.setText(String.valueOf(patientInfo.getWeight()));
				lblBMI.setText(String.valueOf(patientInfo.getBmi()));
				lblBSA.setText(String.valueOf(patientInfo.getBsa()));
			}
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					choiceboxSpecialities.requestFocus();
				}
			});
		} catch (Exception e) {
			LOGGER.error("## Exception occured:" + e.getMessage());
			Main.getInstance().getUtility().showNotification("Error", "Error", "Something went wrong");
		}

	}

	/**
	 * This method is used to close Edit Case Window
	 *
	 * @throws Exception
	 */
	private void closeEditCasePopup() throws Exception {

		try {
			removeListeners();
			Main.getInstance().getStageManager().closeSecondaryStage();
			if(EditCaseFlagSession.getInstance().getIsEditFromDashboard()){
				ControllerMediator.getInstance().getMainController().getDrawSidePane().setInit(false);
				ControllerMediator.getInstance().getMainController().loadSideBarEvents();
			}
		} catch (Exception e) {
			LOGGER.error("## Exception occured:" + e);

		}
	}

	/**
	 * This method is used to close Create Case Window
	 *
	 * @throws Exception
	 */
	private void closeCreateCasePopup() throws Exception {

		try {
			removeListeners();
			Main.getInstance().getStageManager().closeSecondaryStage();

		} catch (Exception e) {
			LOGGER.error("## Exception occured:" + e);
		}
	}

	/**
	 * This method call getCaseDetailsService and fetches case details for Edit
	 * case on the base of caseId
	 *
	 * @throws Exception
	 */
	private void getCaseDetails() throws Exception {

		try {

			GetCaseDetailsService getCaseDetailsService = GetCaseDetailsService
					.getInstance(EditCaseFlagSession.getInstance().getCaseId());
			getCaseDetailsService.restart();

			GetCaseSuccessHandler = new EventHandler<WorkerStateEvent>() {

				@Override
				public void handle(WorkerStateEvent event) {

					/*
					 * diagnosisList = FXCollections.observableArrayList();
					 * plannedProcedureList =
					 * FXCollections.observableArrayList();
					 */

					editCaseDetails = getCaseDetailsService.getCaseDetails();
					if (editCaseDetails != null) {
						fillEditCaseForm();
					}

					disablePaneMainContent.setVisible(false);

					getCaseDetailsService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
							GetCaseSuccessHandler);
					getCaseDetailsService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
							GetCasesFailedHandler);
					GetCaseSuccessHandler = null;
					GetCasesFailedHandler = null;
				}
			};

			getCaseDetailsService.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, GetCaseSuccessHandler);

			GetCasesFailedHandler = new EventHandler<WorkerStateEvent>() {

				@Override
				public void handle(WorkerStateEvent event) {

					Main.getInstance().getUtility().showNotification("Error", "Error",
							getCaseDetailsService.getException().getMessage());
					disablePaneMainContent.setVisible(false);

					getCaseDetailsService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
							GetCaseSuccessHandler);
					getCaseDetailsService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
							GetCasesFailedHandler);
					GetCaseSuccessHandler = null;
					GetCasesFailedHandler = null;
				}
			};

			getCaseDetailsService.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED, GetCasesFailedHandler);

		} catch (Exception e) {
			LOGGER.error("## Exception occured:" + e);

		}

	}

	/**
	 * This method is used to fill the Edit Case form with the Case details
	 * fetched from database
	 */
	@SuppressWarnings({ "deprecation" })
	private void fillEditCaseForm() {

		try {

			if (editCaseDetails != null) {
				isAnasthesioListSearchSelected = true;
				isOTListSearchSelected = true;

				if (editCaseDetails.getCreatedTime() != null) {

					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yy HH:mm", Locale.ENGLISH);

					textDateTime.setText(simpleDateFormat.format(editCaseDetails.getCreatedTime()));
				}

				if (editCaseDetails.getAnesthesiologists() != null) {
					txtAnaesthesiologist.setText(editCaseDetails.getAnesthesiologists());
				}

				txtOtheranaesthesiologist.setText(editCaseDetails.getOtherAnesthesiologists());

				if (editCaseDetails.getoT() != null) {
					txtOT.setText(editCaseDetails.getoT());
				}

				txtSurgeons.setText(editCaseDetails.getSurgeons());
				textAreaDetails.setText(editCaseDetails.getDetails());
				textAreaDiagnosisDetails.setText(editCaseDetails.getDiagnosisText());
				textAreaPlanDetails.setText(editCaseDetails.getPlannedProcedureText());
				textWardNo.setText(editCaseDetails.getWardNumber());

				if (editCaseDetails.getSurgeryType().equalsIgnoreCase("Elective")) {
					radioSurgeryElective.setSelected(true);
				} else if (editCaseDetails.getSurgeryType().equalsIgnoreCase("Emergency")) {
					radioSurgeryEmergency.setSelected(true);
				} else {
					radioSurgerySemiEmergency.setSelected(true);
				}

				if (editCaseDetails.getUnit().equalsIgnoreCase("Day Care")) {
					radioUnitDayCare.setSelected(true);
				} else {
					radioUnitInPatient.setSelected(true);
				}
				if (editCaseDetails.getAdmitDateTime() != null) {

					java.sql.Date date = new java.sql.Date(editCaseDetails.getAdmitDateTime().getTime());
					// String AdmitDateTimearay[] =
					// editCaseDetails.getAdmitDateTime().toString().split(" ");
					//
					// String timearray[] = AdmitDateTimearay[1].split("\\.");

					String timeStr = "";
					timeStr = editCaseDetails.getAdmitDateTime().getHours() + ":"
							+ editCaseDetails.getAdmitDateTime().getMinutes() + ":"
							+ editCaseDetails.getAdmitDateTime().getSeconds();

					LocalDate localDate = LocalDate.parse(date.toString());
					datepickerAdmitDate.setValue(localDate);
					try {
						setCaseAdmitTime(timeStr);
					} catch (Exception e1) {

						LOGGER.error("## Exception occured:", e1);
					}
				}

				if (editCaseDetails.getPlannedProcedureDateTime() != null) {

					java.sql.Date date = new java.sql.Date(editCaseDetails.getPlannedProcedureDateTime().getTime());
					String timeStr = "";
					timeStr = editCaseDetails.getPlannedProcedureDateTime().getHours() + ":"
							+ editCaseDetails.getPlannedProcedureDateTime().getMinutes() + ":"
							+ editCaseDetails.getPlannedProcedureDateTime().getSeconds();
					LocalDate planLocalDate = LocalDate.parse(date.toString());
					datepickerPlanDate.setValue(planLocalDate);
					try {
						setCasePlanTime(timeStr);
					} catch (Exception e1) {

						LOGGER.error("## Exception occured:", e1);
					}
				}

				// String diagValues = "";
				// diagValues = editCaseDetails.getDiagnosisValues();
				//
				// String diagIds = "";
				// diagIds = editCaseDetails.getDiagnosis();

				/*
				 * if (!diagValues.trim().equalsIgnoreCase("")) {
				 *
				 * String diagValArr[] = diagValues.split("\\|"); String
				 * diagIDArr[] = diagIds.split("\\,");
				 *
				 * for (int i = 0; i < diagValArr.length; i++) {
				 * diagnosisList.add(new CreateCaseTableBean(diagIDArr[i],
				 * diagValArr[i])); }
				 *
				 * } tableDiagnosis.setItems(diagnosisList);
				 */

				// String planValues = "";
				// String planIds = "";
				// planValues = editCaseDetails.getPlannedProcedureValues();
				// planIds = editCaseDetails.getPlannedProcedure();

				/*
				 * if (!planValues.trim().equalsIgnoreCase("")) {
				 *
				 * String planValArr[] = planValues.split("\\|"); String
				 * planIdArr[] = planIds.split("\\,");
				 *
				 * for (int j = 0; j < planValArr.length; j++) {
				 * plannedProcedureList.add(new
				 * CreateCaseTableBean(planIdArr[j], planValArr[j])); }
				 *
				 * } tablePlannedProcedure.setItems(plannedProcedureList);
				 */
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						choiceboxSpecialities.getSelectionModel().select(editCaseDetails.getSpeciality());
						selectedSpeciality = editCaseDetails.getSpeciality();
						if(editCaseDetails.getMainCategory() != null){
							mainCategory =editCaseDetails.getMainCategory();
							if(editCaseDetails.getDiagnosis1() != null){
								diagnosis1 =editCaseDetails.getDiagnosis1();
							}
							if(editCaseDetails.getDiagnosis2() != null){
								diagnosis2 =editCaseDetails.getDiagnosis2();
							}
							if(editCaseDetails.getDiagnosis3() != null){
								diagnosis3 =editCaseDetails.getDiagnosis3();
							}
						}
						
						populateMainCategory();
						
						 
					}

					
				});
			}
			disablePaneMainContent.setVisible(false);

		} catch (Exception e) {
			LOGGER.error("## Exception occured:", e);
		}
	}
	private void populateMainCategory() {
		if (!mainCategory.equalsIgnoreCase("")) {
			choiceBoxMainCat.getSelectionModel().select(mainCategory);
			choiceBoxDiagnosis1.getItems().clear();
			choiceBoxDiagnosis2.getItems().clear();
			choiceBoxDiagnosis3.getItems().clear();
			diagonsisList = MasterDataSession.getInstance().getMasterDataMap().get(mainCategory);
			choiceBoxDiagnosis1.getItems().add("Select");
			choiceBoxDiagnosis2.getItems().add("Select");
			choiceBoxDiagnosis3.getItems().add("Select");
			choiceBoxDiagnosis1.getItems().addAll(diagonsisList);
			choiceBoxDiagnosis2.getItems().addAll(diagonsisList);
			choiceBoxDiagnosis3.getItems().addAll(diagonsisList);
			if (!diagnosis1.equalsIgnoreCase("")) 
				choiceBoxDiagnosis1.getSelectionModel().select(diagnosis1);
			else
				choiceBoxDiagnosis1.getSelectionModel().select(0);
			
			if (!diagnosis2.equalsIgnoreCase("")) 
				choiceBoxDiagnosis2.getSelectionModel().select(diagnosis2);
			else
				choiceBoxDiagnosis2.getSelectionModel().select(0);
			
			if (!diagnosis3.equalsIgnoreCase("")) 
				choiceBoxDiagnosis3.getSelectionModel().select(diagnosis3);
			else
				choiceBoxDiagnosis3.getSelectionModel().select(0);
			
		} else {
			choiceBoxMainCat.getSelectionModel().select(0);
			choiceBoxDiagnosis1.setDisable(true);
			choiceBoxDiagnosis2.setDisable(true);
			choiceBoxDiagnosis3.setDisable(true);
		}
	}
	/**
	 * This method fetches Favourites & Categories for both Diagnosis & Planned
	 * Procedure
	 */
	/*
	 * public void fetchFavouritesAndCategories() throws Exception {
	 * LOGGER.debug("Logger Name: " + LOGGER.getName()); try { // ---fetch
	 * favourites list for Diagnosis getDiagnosisFavourites();
	 *
	 * // ---fetch Diagnosis category list getDiagnosisCategory();
	 *
	 * // ---fetch favourites list for Planned Procedure
	 * getPlannedProcedureFavourites();
	 *
	 * // ---fetch Plan category list getProcedureCategory(); } catch (Exception
	 * e) { LOGGER.error("## Exception occured:" + e.getMessage());
	 * Main.getInstance().getUtility().showNotification("Error", "Error",
	 * "Something went wrong"); } }
	 *
	 *//**
		 * This method handles Add-Delete functionality for diagnosis favourites
		 * tab
		 */
	/*
	 * public void diagnosisFavouriteAddDelFunc() throws Exception {
	 * LOGGER.debug("Logger Name: " + LOGGER.getName()); try { // ---DIAGNOSIS
	 * ADD BUTTON // ---On selection of any Favourite Diagnosis item, enable ADD
	 * // button listViewFavDiag.getSelectionModel().selectedItemProperty()
	 * .addListener((ObservableValue<? extends String> observable, String
	 * oldValue, String newValue) -> { if (newValue != null) {
	 * System.out.println("Selected Favourite Diagnosis----" + newValue);
	 * btnAddFavDiag.setDisable(false); } });
	 *
	 * // ---Onclick of ADD button btnAddFavDiag.setOnAction(e -> { String
	 * selectedFavDiag =
	 * listViewFavDiag.getSelectionModel().getSelectedItem().toString(); String
	 * Id = extractDiagnosisFavId(selectedFavDiag, diagnosisFavouritesListDB);
	 * boolean flag = true; for (CreateCaseTableBean obj : diagnosisList) { if
	 * (obj.getId().equalsIgnoreCase(Id)) { flag = false; break; } }
	 *
	 * if (flag) { diagnosisList.add(new CreateCaseTableBean(Id,
	 * selectedFavDiag)); }
	 *
	 * });
	 *
	 * // ---DIAGNOSIS DELETE BUTTON // ---On selection of any row from table,
	 * enable DELETE button
	 * tableDiagnosis.getSelectionModel().selectedItemProperty()
	 * .addListener((ObservableValue<? extends CreateCaseTableBean> observable,
	 * CreateCaseTableBean oldValue, CreateCaseTableBean newValue) -> { if
	 * (newValue != null) { System.out.println(
	 * "Selected Diagnosis Table row----" + newValue.getDescription());
	 * btnDeleteFavDiag.setDisable(false);
	 * btnDeleteDiagSearch.setDisable(false); } });
	 *
	 * // ---Onclick of DELETE button btnDeleteFavDiag.setOnAction(e -> {
	 *
	 * String selectedTableVal =
	 * tableDiagnosis.getSelectionModel().getSelectedItem().getDescription();
	 *
	 * Iterator<CreateCaseTableBean> iterator = diagnosisList.iterator(); while
	 * (iterator.hasNext()) { CreateCaseTableBean obj = iterator.next(); if (obj
	 * != null) { if (obj.getDescription().equalsIgnoreCase(selectedTableVal)) {
	 * iterator.remove(); } } }
	 *
	 * tableDiagnosis.setItems(diagnosisList);
	 *
	 * if (diagnosisList.size() == 0) { btnDeleteFavDiag.setDisable(true); }
	 *
	 * }); } catch (Exception e) { LOGGER.error("## Exception occured:" +
	 * e.getMessage());
	 * Main.getInstance().getUtility().showNotification("Error", "Error",
	 * "Something went wrong"); }
	 *
	 * }
	 *
	 *//**
		 * This method handles Add-Delete functionality for diagnosis search tab
		 */
	/*
	 * public void diagnosisSearchAddDelFunc() throws Exception {
	 *
	 * // ---DIAGNOSIS SEARCH ADD BUTTON // ---On selection of any Searched
	 * Diagnosis item, enable ADD button
	 *
	 * listDiagSearch.getSelectionModel().selectedItemProperty()
	 * .addListener((ObservableValue<? extends String> observable, String
	 * oldValue, String newValue) -> { if (newValue != null) {
	 * System.out.println("Selected Diagnosis Search item ----" + newValue);
	 * btnAddDiagSearch.setDisable(false); } });
	 *
	 * LOGGER.debug("Logger Name: " + LOGGER.getName()); try { // ---Onclick of
	 * ADD button btnAddDiagSearch.setOnAction(e -> { String selectedDiag =
	 * textAutoSearchDiag.getText(); if (!("").equals(selectedDiag) || "" !=
	 * selectedDiag || !selectedDiag.isEmpty()) { String Id =
	 * extractDiagnosisFavId(selectedDiag, diagnosisSearchListDB); boolean flag
	 * = true; for (CreateCaseTableBean obj : diagnosisList) { if
	 * (obj.getId().equalsIgnoreCase(Id)) { flag = false; break; } }
	 *
	 * if (flag) { diagnosisList.add(new CreateCaseTableBean(Id, selectedDiag));
	 * textAutoSearchDiag.setText(""); lblErrorMsg.setVisible(false); //
	 * btnAddDiagSearch.setDisable(true); } else { lblErrorMsg.setText(
	 * "* Selected Diagnosis has been already added.");
	 * lblErrorMsg.setVisible(true); } }
	 *
	 * });
	 *
	 * // ---DIAGNOSIS SEARCH DELETE BUTTON // ---Onclick of DELETE button
	 * btnDeleteDiagSearch.setOnAction(e -> {
	 *
	 * String selectedTableVal =
	 * tableDiagnosis.getSelectionModel().getSelectedItem().getDescription();
	 *
	 * Iterator<CreateCaseTableBean> iterator = diagnosisList.iterator(); while
	 * (iterator.hasNext()) { CreateCaseTableBean obj = iterator.next(); if (obj
	 * != null) { if (obj.getDescription().equalsIgnoreCase(selectedTableVal)) {
	 * iterator.remove(); } } }
	 *
	 * tableDiagnosis.setItems(diagnosisList);
	 *
	 * if (diagnosisList.size() == 0) { btnDeleteDiagSearch.setDisable(true); }
	 *
	 * }); } catch (Exception e) { LOGGER.error("## Exception occured:" +
	 * e.getMessage());
	 * Main.getInstance().getUtility().showNotification("Error", "Error",
	 * "Something went wrong"); }
	 *
	 * }
	 *
	 *//**
		 * This method handles Add-Delete functionality for plan favourites tab
		 */
	/*
	 * public void planFavouriteAddDelFunc() throws Exception { LOGGER.debug(
	 * "Logger Name: " + LOGGER.getName()); try {
	 *
	 * // ---PROCEDURE ADD BUTTON // ---On selection of any Favourite Procedure
	 * item , enable ADD // button
	 * listViewFavPlannedProcedure.getSelectionModel().selectedItemProperty()
	 * .addListener((ObservableValue<? extends String> observable, String
	 * oldValue, String newValue) -> { if (newValue != null) {
	 * System.out.println("Selected Favourite Procedure----" + newValue);
	 * btnAddFavPlan.setDisable(false); } });
	 *
	 * // ---Onclick of ADD button btnAddFavPlan.setOnAction(e -> { String
	 * selectedFavPlan =
	 * listViewFavPlannedProcedure.getSelectionModel().getSelectedItem().
	 * toString(); String Id = extractProcedureFavId(selectedFavPlan,
	 * plannedProcedureFavouritesListDB); boolean flag = true; for
	 * (CreateCaseTableBean obj : plannedProcedureList) { if
	 * (obj.getId().equalsIgnoreCase(Id)) { flag = false; break; } }
	 *
	 * if (flag) { plannedProcedureList.add(new CreateCaseTableBean(Id,
	 * selectedFavPlan)); }
	 *
	 * });
	 *
	 * // ---PROCEDURE DELETE BUTTON // ---On selection of any row from table,
	 * enable DELETE button
	 * tablePlannedProcedure.getSelectionModel().selectedItemProperty()
	 * .addListener((ObservableValue<? extends CreateCaseTableBean> observable,
	 * CreateCaseTableBean oldValue, CreateCaseTableBean newValue) -> { if
	 * (newValue != null) { System.out.println(
	 * "Selected Procedure Table row----" + newValue.getDescription());
	 * btnDeleteFavPlan.setDisable(false);
	 * btnDeleteSearchPlan.setDisable(false); } });
	 *
	 * // ---Onclick of DELETE button btnDeleteFavPlan.setOnAction(e -> {
	 *
	 * String selectedTableVal =
	 * tablePlannedProcedure.getSelectionModel().getSelectedItem().
	 * getDescription();
	 *
	 * Iterator<CreateCaseTableBean> iterator = plannedProcedureList.iterator();
	 * while (iterator.hasNext()) { CreateCaseTableBean obj = iterator.next();
	 * if (obj != null) { if
	 * (obj.getDescription().equalsIgnoreCase(selectedTableVal)) {
	 * iterator.remove(); } } }
	 *
	 * tablePlannedProcedure.setItems(plannedProcedureList);
	 *
	 * if (plannedProcedureList.size() == 0) {
	 * btnDeleteFavPlan.setDisable(true); }
	 *
	 * }); } catch (Exception e) { LOGGER.error("## Exception occured:" +
	 * e.getMessage());
	 * Main.getInstance().getUtility().showNotification("Error", "Error",
	 * "Something went wrong"); } }
	 *
	 *//**
		 * This method handles Add-Delete functionality for plan Search tab
		 */
	/*
	 * public void planSearchAddDelFunc() throws Exception { // ---PROCEDURE
	 * SEARCH ADD BUTTON // ---On selection of any Procedure item from searched
	 * list, enable ADD // button
	 *
	 * listPlanSearch.getSelectionModel().selectedItemProperty()
	 * .addListener((ObservableValue<? extends String> observable, String
	 * oldValue, String newValue) -> { if (newValue != null) {
	 * System.out.println("Selected Favourite Procedure----" + newValue);
	 * btnAddSearchPlan.setDisable(false); } });
	 *
	 *
	 * // ---Onclick of ADD button LOGGER.debug("Logger Name: " +
	 * LOGGER.getName()); try { btnAddSearchPlan.setOnAction(e -> { if
	 * (!textAutoSearchPlan.getText().equals("") ||
	 * !textAutoSearchPlan.getText().isEmpty()) { String selectedPlan =
	 * textAutoSearchPlan.getText();
	 *
	 * if (!("").equals(selectedPlan) || "" != selectedPlan ||
	 * !selectedPlan.isEmpty()) { String Id =
	 * extractProcedureFavId(selectedPlan, procedureSearchListDB); boolean flag
	 * = true; for (CreateCaseTableBean obj : plannedProcedureList) { if
	 * (obj.getId().equalsIgnoreCase(Id)) { flag = false; break; } }
	 *
	 * if (flag) { plannedProcedureList.add(new CreateCaseTableBean(Id,
	 * selectedPlan)); textAutoSearchPlan.setText("");
	 * lblErrorMsg.setVisible(false);
	 *
	 * } else { lblErrorMsg.setText(
	 * "* Selected Planned Procedure has been already added.");
	 * lblErrorMsg.setVisible(true); } } }
	 *
	 * });
	 *
	 * // ---Onclick of DELETE button btnDeleteSearchPlan.setOnAction(e -> {
	 * String selectedTableVal =
	 * tablePlannedProcedure.getSelectionModel().getSelectedItem().
	 * getDescription();
	 *
	 * Iterator<CreateCaseTableBean> iterator = plannedProcedureList.iterator();
	 * while (iterator.hasNext()) { CreateCaseTableBean obj = iterator.next();
	 * if (obj != null) { if
	 * (obj.getDescription().equalsIgnoreCase(selectedTableVal)) {
	 * iterator.remove(); } } }
	 *
	 * tablePlannedProcedure.setItems(plannedProcedureList);
	 *
	 * if (plannedProcedureList.size() == 0) {
	 * btnDeleteSearchPlan.setDisable(true); }
	 *
	 * }); } catch (Exception e) { LOGGER.error("## Exception occured:" +
	 * e.getMessage());
	 * Main.getInstance().getUtility().showNotification("Error", "Error",
	 * "Something went wrong"); } }
	 *
	 *//**
		 * This method extracts specialityID(operationTheaterID also)for given
		 * textBoxValue from obsList
		 *
		 * @param textBoxValue
		 * @param obsList
		 * @return
		 *//*
		 * public int extractIdFromList(String textBoxValue, List<Entityvalues>
		 * obsList) { int idToReturn = -1; for (Entityvalues obj : obsList) { if
		 * (obj != null) { String entityName = obj.getEntityValue(); int
		 * entityID = obj.getEntityValueId();
		 *
		 * if (entityName.equalsIgnoreCase(textBoxValue.trim())) { idToReturn =
		 * entityID; break; } } }
		 *
		 * return idToReturn; }
		 */

	/**
	 * This method extracts surgeonsIDs and anesthesiologistIDs List for given
	 * textBoxValue from obsList
	 *
	 * @param textBoxValue
	 * @param obsList
	 * @return
	 */
	/*
	 * public List<Long> extractLongIdsList(String textBoxValue,
	 * List<Entityvalues> obsList) { List<Long> listToReturn = new
	 * ArrayList<Long>();
	 *
	 * if (textBoxValue.contains(",")) { String mulitpleValues[] =
	 * textBoxValue.split(",");
	 *
	 * for (int i = 0; i < mulitpleValues.length; i++) { String name =
	 * mulitpleValues[i].trim();
	 *
	 * for (Entityvalues obj : obsList) { if (obj != null) { String entityName =
	 * obj.getEntityValue(); Long entityID = (long) obj.getEntityValueId();
	 *
	 * if (entityName.equalsIgnoreCase(name)) { listToReturn.add(entityID);
	 * break; } } } }
	 *
	 * }
	 *
	 * return listToReturn; }
	 */

	/**
	 * This method extract Diagnosis Category Id for selected Diagnosis value
	 * from List
	 *
	 * @param textBoxValue
	 * @param obsList
	 * @return
	 */
	/*
	 * public int extractDiagnosisCategoryId(String textBoxValue,
	 * List<Diagnosiscategory> obsList) { int idToReturn = -1;
	 *
	 * for (Diagnosiscategory obj : obsList) { if (obj != null) { String
	 * entityName = obj.getDiagnosisCategoryValue(); int entityID =
	 * obj.getDiagnosisCategoryId();
	 *
	 * if (entityName.equalsIgnoreCase(textBoxValue.trim())) { idToReturn =
	 * entityID; break; } }
	 *
	 * }
	 *
	 * return idToReturn; }
	 */

	/**
	 * This method extract Diagnosis Sub Category Id for selected Sub category
	 * value from dropdown
	 *
	 * @param textBoxValue
	 * @param obsList
	 * @return
	 */
	/*
	 * public int extractDiagnosisSubCategoryId(String textBoxValue,
	 * List<Diagnosissubcategory> obsList) { int idToReturn = -1;
	 *
	 * for (Diagnosissubcategory obj : obsList) { if (obj != null) { String
	 * entityName = obj.getDiagnosisSubcategoryValue(); int entityID =
	 * obj.getDiagnosisSubCategory();
	 *
	 * if (entityName.equalsIgnoreCase(textBoxValue.trim())) { idToReturn =
	 * entityID; break; } }
	 *
	 * }
	 *
	 * return idToReturn;
	 *
	 * }
	 */

	/**
	 * This method extracts Procedure Category Id for given textBoxValue from
	 * obsList
	 *
	 * @param textBoxValue
	 * @param obsList
	 * @return
	 */
	// public String extractProcedureCategoryId(String textBoxValue,
	// List<Procedurecategory> obsList) {
	// String returnCategoryCode = "No Code!";
	//
	// for (Procedurecategory obj : obsList) {
	// if (obj != null) {
	// String entityName = obj.getProcedureCategoryValue();
	// String categoryCode = obj.getProcedureCategoryCode();
	//
	// if (entityName.equalsIgnoreCase(textBoxValue.trim())) {
	// returnCategoryCode = categoryCode;
	// break;
	// }
	// }
	//
	// }
	//
	// return returnCategoryCode;
	// }

	/**
	 * This method extract Procedure Sub Category Id for given textBoxValue from
	 * obsList
	 *
	 * @param textBoxValue
	 * @param obsList
	 * @return
	 */
	/*
	 * public String extractProcedureSubCategoryId(String textBoxValue,
	 * List<Proceduresubcategory> obsList) { String returnSubCategoryCode =
	 * "No Code!";
	 *
	 * for (Proceduresubcategory obj : obsList) { if (obj != null) { String
	 * entityName = obj.getProcedureSubCategoryValue(); String categoryCode =
	 * obj.getProcedureSubCategoryCode();
	 *
	 * if (entityName.equalsIgnoreCase(textBoxValue.trim())) {
	 * returnSubCategoryCode = categoryCode; break; } }
	 *
	 * }
	 *
	 * return returnSubCategoryCode; }
	 */

	/**
	 * This method extract Procedure Sub Sub Category Id for given textBoxValue
	 * from obsList
	 *
	 * @param textBoxValue
	 * @param obsList
	 * @return
	 */
	/*
	 * public String extractProcedureSubSubCategoryId(String textBoxValue,
	 * List<Proceduresubsubcategory> obsList) { String returnSubSubCategoryCode
	 * = "No Code!";
	 *
	 * for (Proceduresubsubcategory obj : obsList) { if (obj != null) { String
	 * entityName = obj.getProcedureSubSubCategoryValue(); String categoryCode =
	 * obj.getProcedureSubSubCategoryCode();
	 *
	 * if (entityName.equalsIgnoreCase(textBoxValue.trim())) {
	 * returnSubSubCategoryCode = categoryCode; break; } }
	 *
	 * }
	 *
	 * return returnSubSubCategoryCode; }
	 */

	/**
	 * This method extract Diagnosis Favourite Id for given textBoxValue from
	 * obsList
	 *
	 * @param textBoxValue
	 * @param obsList
	 * @return
	 */
	/*
	 * public String extractDiagnosisFavId(String textBoxValue, List<Diagnosis>
	 * obsList) { String returFavId = "No Code!"; for (Diagnosis obj : obsList)
	 * { if (obj != null) { String entityName = obj.getDiagnosisName(); String
	 * categoryCode = obj.getDiagnosisId();
	 *
	 * if (entityName.equalsIgnoreCase(textBoxValue.trim())) { returFavId =
	 * categoryCode; break; } }
	 *
	 * }
	 *
	 * return returFavId; }
	 */

	/**
	 * This method extract Procedure Favourite Id for given textBoxValue from
	 * obsList
	 *
	 * @param textBoxValue
	 * @param obsList
	 * @return
	 */
	/*
	 * public String extractProcedureFavId(String textBoxValue,
	 * List<Plannedprocedure> obsList) { String returFavId = "No Code!"; for
	 * (Plannedprocedure obj : obsList) { if (obj != null) { String entityName =
	 * obj.getProcedureName(); String categoryCode = obj.getProcedureId();
	 *
	 * if (entityName.equalsIgnoreCase(textBoxValue.trim())) { returFavId =
	 * categoryCode; break; } }
	 *
	 * }
	 *
	 * return returFavId; }
	 */
	/**
	 * This method populate drop-downs with the database returned lists
	 *
	 * @throws Exception
	 */
	private void fillDropdownValues() throws Exception {
		// ---Populating Specialities dropdown

		try {
			List<String> specialitiesList = new ArrayList<>();
			specialitiesList.add("Select");
			specialitiesList.addAll(MasterDataSession.getInstance().getMasterDataMap().get("Specialities"));
			choiceboxSpecialities.getItems().clear();
			choiceboxSpecialities.getItems().addAll(specialitiesList);
			if (!selectedSpeciality.equalsIgnoreCase("")) {
				choiceboxSpecialities.getSelectionModel().select(selectedSpeciality);
			} else {
				choiceboxSpecialities.getSelectionModel().select(0);
			}
			List<String> mainCategoryList = new ArrayList<>();
			mainCategoryList.add("Select");
			mainCategoryList.addAll(Arrays.asList(SystemConfiguration.getInstance().getMainCat().split(",")));
			choiceBoxMainCat.getItems().clear();
			choiceBoxMainCat.getItems().addAll(mainCategoryList);
		
			
			// ---Populating operationTheater dropdown
			operationTheaterList = MasterDataSession.getInstance().getMasterDataMap().get("OT");

			AneastheologistsList = MasterDataSession.getInstance().getMasterDataMap().get("Anesthesiologists");

		} catch (Exception e) {
			LOGGER.error("## Exception occured:" + e);

		}

	}
	
	private void fillMainCategoryDropdownValues() throws Exception {
		// ---Populating MainCategory dropdown

		try {
			List<String> mainCategoryList = new ArrayList<>();
			mainCategoryList.add("Select");
			mainCategoryList.addAll(Arrays.asList(SystemConfiguration.getInstance().getMainCat().split(",")));
			choiceBoxMainCat.getItems().clear();
			choiceBoxMainCat.getItems().addAll(mainCategoryList);
		
			choiceBoxMainCat.getSelectionModel().select(0);
			
			
				

		} catch (Exception e) {
			LOGGER.error("## Exception occured:" + e);

		}

	}
	
	

	/**
	 * This method is used to search Aneasthesiologist from the list of
	 * Aneasthesiologist fetched from database, on the basis of text input
	 *
	 * @throws Exception
	 */
	private void searchAneasthesiologists() throws Exception {

		try {
			listViewAnaesthesiologist.setVisible(false);
			aneastheologistsSearchList = new ArrayList<>();

			for (String obj : AneastheologistsList) {

				if (obj.toLowerCase().contains(txtAnaesthesiologist.getText().toLowerCase())) {
					aneastheologistsSearchList.add(obj);
				}

			}
			if (aneastheologistsSearchList != null && aneastheologistsSearchList.size()!=0) {
				listViewAnaesthesiologist.getItems().clear();
				listViewAnaesthesiologist.getItems().addAll(aneastheologistsSearchList);
				listViewAnaesthesiologist.setVisible(true);
			}
		} catch (Exception e) {
			LOGGER.error("## Exception occured:" + e);

		}

	}

	/**
	 * This method is used to search OT Name from the list of OT Name fetched
	 * from database, on the basis of text input
	 *
	 * @throws Exception
	 */
	private void searchOT() throws Exception {

		try {
			OTSearchList = new ArrayList<>();

			for (String obj : operationTheaterList) {
				if (obj.toLowerCase().contains(txtOT.getText().toLowerCase())) {
					OTSearchList.add(obj);
				}

			}
			if (OTSearchList != null) {
				listViewOT.getItems().clear();
				listViewOT.getItems().addAll(OTSearchList);
				listViewOT.setVisible(true);
			}
		} catch (Exception e) {
			LOGGER.error("## Exception occured:" + e);
		}

	}

	/**
	 * This method fetches DIAGNOSIS Favourites
	 */
	/*
	 *
	 *
	 *
	 *
	 *
	 * public void getDiagnosisFavourites() throws Exception { LOGGER.debug(
	 * "Logger Name: " + LOGGER.getName()); try {
	 *
	 * getDiagnosisFavoritesService =
	 * GetDiagnosisFavoritesService.getInstance();
	 *
	 * if (!getDiagnosisFavoritesService.isRunning()) { System.out.println(
	 * "In get diagnosis fav service----->>");
	 * getDiagnosisFavoritesService.restart();
	 *
	 * // ---on service success getDiagnosisFavoritesService.setOnSucceeded(e ->
	 * { System.out.println(
	 * "getDiagnosisFavoritesService javafx service was success----->>>");
	 * diagnosisFavouritesListDB =
	 * getDiagnosisFavoritesService.getDiagnosisFavourites();
	 *
	 * if (diagnosisFavouritesListDB != null) { for (Diagnosis obj :
	 * diagnosisFavouritesListDB) { if (obj != null) { if
	 * (obj.getDiagnosisName() != null)
	 * diagnosisFavouritesList.add(obj.getDiagnosisName()); } } if
	 * (MasterDataSession.getInstance().getMasterDataMap().get("FavDiag") ==
	 * null) { MasterDataSession.getInstance().getMasterDataMap().put("FavDiag",
	 * diagnosisFavouritesList); }
	 *
	 * flllDiagFavList(); }
	 *
	 * });
	 *
	 * // ---on service failure getDiagnosisFavoritesService.setOnFailed(e -> {
	 * System.out.println(
	 * "getDiagnosisFavoritesService javafx service was failed----->>>");
	 * Main.getInstance().getUtility().showNotification("Error", "Error",
	 * getDiagnosisFavoritesService.getException().getMessage()); }); } } catch
	 * (Exception e) { LOGGER.error("## Exception occured:" + e.getMessage());
	 * Main.getInstance().getUtility().showNotification("Error", "Error",
	 * "Something went wrong"); }
	 *
	 * }
	 *
	 *//**
		 * This method fetches PLANNED PROCEDURE Favourites
		 */
	/*
	 * public void getPlannedProcedureFavourites() throws Exception {
	 * LOGGER.debug("Logger Name: " + LOGGER.getName()); try {
	 *
	 * getPlannedProcedureFavoritesService =
	 * GetPlannedProcedureFavoritesService.getInstance();
	 *
	 * if (!getPlannedProcedureFavoritesService.isRunning()) {
	 * getPlannedProcedureFavoritesService.restart();
	 *
	 * // ---on service success
	 * getPlannedProcedureFavoritesService.setOnSucceeded(e -> {
	 * System.out.println(
	 * "getPlannedProcedureFavoritesService javafx service was success----->>>"
	 * ); plannedProcedureFavouritesListDB = getPlannedProcedureFavoritesService
	 * .getPlannedProcedureFavourites();
	 *
	 * if (plannedProcedureFavouritesListDB != null) { for (Plannedprocedure obj
	 * : plannedProcedureFavouritesListDB) { if (obj != null) {
	 * plannedProcedureFavouritesList.add(obj.getProcedureName()); } } if
	 * (MasterDataSession.getInstance().getMasterDataMap().get("FavProcedure")
	 * == null) {
	 * MasterDataSession.getInstance().getMasterDataMap().put("FavProcedure",
	 * plannedProcedureFavouritesList); }
	 *
	 * flllProcedureFavList();
	 *
	 * }
	 *
	 * });
	 *
	 * // ---on service failure
	 * getPlannedProcedureFavoritesService.setOnFailed(e -> {
	 * System.out.println(
	 * "getPlannedProcedureFavoritesService javafx service was failed----->>>");
	 * Main.getInstance().getUtility().showNotification("Error", "Error",
	 * getPlannedProcedureFavoritesService.getException().getMessage()); }); } }
	 * catch (Exception e) { LOGGER.error("## Exception occured:" +
	 * e.getMessage());
	 * Main.getInstance().getUtility().showNotification("Error", "Error",
	 * "Something went wrong"); }
	 *
	 * }
	 *
	 *//**
		 * This method fetches DIAGNOSIS Category
		 */
	/*
	 * public void getDiagnosisCategory() throws Exception { LOGGER.debug(
	 * "Logger Name: " + LOGGER.getName()); try { ObservableList<String>
	 * diagnosisCategories = FXCollections.observableArrayList();
	 * ObservableList<String> diagnosisCategoriesID =
	 * FXCollections.observableArrayList();
	 *
	 * getDiagnosisCategoryService = GetDiagnosisCategoryService.getInstance();
	 *
	 * if (!getDiagnosisCategoryService.isRunning()) {
	 * getDiagnosisCategoryService.restart();
	 *
	 * // ---on service failure getDiagnosisCategoryService.setOnSucceeded(e ->
	 * { System.out.println(
	 * "getDiagnosisCategoryService javafx service was sucess----->>>");
	 * diagnosisCategories.add("Select"); diagnosisCategoriesID.add("Select");
	 * diagnosisCategoriesDb =
	 * getDiagnosisCategoryService.getDiagnosisCategory();
	 *
	 * if (diagnosisCategoriesDb != null) { for (Diagnosiscategory obj :
	 * diagnosisCategoriesDb) { if (obj != null) {
	 * diagnosisCategories.add(obj.getDiagnosisCategoryValue());
	 * diagnosisCategoriesID.add(obj.getDiagnosisCategoryId() + ""); }
	 *
	 * }
	 *
	 * if
	 * (MasterDataSession.getInstance().getMasterDataMap().get("DiagCategories")
	 * == null) {
	 * MasterDataSession.getInstance().getMasterDataMap().put("DiagCategories",
	 * diagnosisCategories); } if
	 * (MasterDataSession.getInstance().getMasterDataMap().get(
	 * "DiagCategoriesId") == null) {
	 * MasterDataSession.getInstance().getMasterDataMap().put(
	 * "DiagCategoriesId", diagnosisCategoriesID); }
	 *
	 * flllDiagCategoryDropDown(); }
	 *
	 * }); // ---on service failure getDiagnosisCategoryService.setOnFailed(e ->
	 * { System.out.println(
	 * "getDiagnosisCategoryService javafx service was failed----->>>");
	 * Main.getInstance().getUtility().showNotification("Error", "Error",
	 * getDiagnosisCategoryService.getException().getMessage()); }); } } catch
	 * (Exception e) { LOGGER.error("## Exception occured:" + e.getMessage());
	 * Main.getInstance().getUtility().showNotification("Error", "Error",
	 * "Something went wrong"); }
	 *
	 * }
	 *
	 *//**
		 * This method fetches PROCEDURE Category
		 */
	/*
	 * public void getProcedureCategory() throws Exception {
	 *
	 * LOGGER.debug("Logger Name: " + LOGGER.getName()); try {
	 * ObservableList<String> procedureCategoriesID =
	 * FXCollections.observableArrayList(); getProcedureCategoryService =
	 * GetProcedureCategoryService.getInstance(); ObservableList<String>
	 * procedureCategories = FXCollections.observableArrayList(); if
	 * (!getProcedureCategoryService.isRunning()) {
	 * getProcedureCategoryService.restart();
	 *
	 * // ---on service failure getProcedureCategoryService.setOnSucceeded(e ->
	 * {
	 *
	 * procedureCategories.add("Select"); procedureCategoriesID.add("Select");
	 * procedureCategoriesDB =
	 * getProcedureCategoryService.getProcedureCategory();
	 *
	 * if (procedureCategoriesDB != null) { for (Procedurecategory obj :
	 * procedureCategoriesDB) { if (obj != null) {
	 * procedureCategories.add(obj.getProcedureCategoryValue());
	 * procedureCategoriesID.add(obj.getProcedureCategoryId() + ""); } }
	 *
	 * if (MasterDataSession.getInstance().getMasterDataMap().get(
	 * "ProcedureCategories") == null) {
	 * MasterDataSession.getInstance().getMasterDataMap().put(
	 * "ProcedureCategories", procedureCategories); } if
	 * (MasterDataSession.getInstance().getMasterDataMap().get(
	 * "ProcedureCategoriesID") == null) {
	 * MasterDataSession.getInstance().getMasterDataMap().put(
	 * "ProcedureCategoriesID", procedureCategoriesID); }
	 *
	 * flllProcedureSectionDropDown(); }
	 *
	 * });
	 *
	 * // ---on service failure getProcedureCategoryService.setOnFailed(e -> {
	 * System.out.println(
	 * "getProcedureCategoryService javafx service was failed----->>>");
	 * Main.getInstance().getUtility().showNotification("Error", "Error",
	 * getProcedureCategoryService.getException().getMessage()); });
	 *
	 * } } catch (Exception e) { LOGGER.error("## Exception occured:" +
	 * e.getMessage());
	 * Main.getInstance().getUtility().showNotification("Error", "Error",
	 * "Something went wrong"); }
	 *
	 * }
	 *
	 * public void fillFavList() {
	 *
	 * if (MasterDataSession.getInstance().getMasterDataMap().get("FavDiag") !=
	 * null) {
	 * listViewFavDiag.setItems(MasterDataSession.getInstance().getMasterDataMap
	 * ().get("FavDiag")); } if
	 * (MasterDataSession.getInstance().getMasterDataMap().get("FavProcedure")
	 * != null) { listViewFavPlannedProcedure
	 * .setItems(MasterDataSession.getInstance().getMasterDataMap().get(
	 * "FavProcedure")); } if
	 * (MasterDataSession.getInstance().getMasterDataMap().get("DiagCategories")
	 * != null) { choiceBoxCategory.setItems(MasterDataSession.getInstance().
	 * getMasterDataMap().get("DiagCategories"));
	 * choiceBoxCategory.getSelectionModel().select(0); } if
	 * (MasterDataSession.getInstance().getMasterDataMap().get(
	 * "ProcedureCategories") != null) {
	 * choiceBoxSection.setItems(MasterDataSession.getInstance().
	 * getMasterDataMap().get("ProcedureCategories"));
	 * choiceBoxSection.getSelectionModel().select(0); } }
	 *
	 * public void flllDiagFavList() { if
	 * (MasterDataSession.getInstance().getMasterDataMap().get("FavDiag") !=
	 * null) {
	 * listViewFavDiag.setItems(MasterDataSession.getInstance().getMasterDataMap
	 * ().get("FavDiag")); } }
	 *
	 * public void flllProcedureFavList() { if
	 * (MasterDataSession.getInstance().getMasterDataMap().get("FavProcedure")
	 * != null) { listViewFavPlannedProcedure
	 * .setItems(MasterDataSession.getInstance().getMasterDataMap().get(
	 * "FavProcedure")); } }
	 *
	 * public void flllDiagCategoryDropDown() { if
	 * (MasterDataSession.getInstance().getMasterDataMap().get("DiagCategories")
	 * != null) { choiceBoxCategory.setItems(MasterDataSession.getInstance().
	 * getMasterDataMap().get("DiagCategories"));
	 * choiceBoxCategory.getSelectionModel().select(0); } }
	 *
	 * public void flllProcedureSectionDropDown() { if
	 * (MasterDataSession.getInstance().getMasterDataMap().get(
	 * "ProcedureCategories") != null) {
	 * choiceBoxSection.setItems(MasterDataSession.getInstance().
	 * getMasterDataMap().get("ProcedureCategories"));
	 * choiceBoxSection.getSelectionModel().select(0); } }
	 *
	 *//**
		 * This method fetches DIAGNOSIS Sub Category
		 *
		 * @param categoryId
		 */
	/*
	 * public void getDiagnosisSubCategory(int categoryId) throws Exception {
	 * LOGGER.debug("Logger Name: " + LOGGER.getName()); try {
	 *
	 * ObservableList<String> diagnosisSubCategories =
	 * FXCollections.observableArrayList(); getDiagnosisSubCategoryService =
	 * GetDiagnosisSubCategoryService.getInstance(categoryId);
	 *
	 * if (!getDiagnosisSubCategoryService.isRunning()) {
	 * getDiagnosisSubCategoryService.restart();
	 *
	 * // ---on service success getDiagnosisSubCategoryService.setOnSucceeded(e
	 * -> { System.out.println(
	 * "getDiagnosisSubCategoryService javafx service was success----->>>");
	 *
	 * diagnosisSubCategories.add("Select"); diagnosisSubCategoriesDB =
	 * getDiagnosisSubCategoryService.getdiagnosisSubCategory();
	 *
	 * if (diagnosisSubCategoriesDB != null) { for (Diagnosissubcategory obj :
	 * diagnosisSubCategoriesDB) { if (obj != null) {
	 * diagnosisSubCategories.add(obj.getDiagnosisSubcategoryValue()); } }
	 *
	 * choiceBoxSubcategory.setItems(diagnosisSubCategories);
	 * choiceBoxSubcategory.getSelectionModel().select(0); }
	 *
	 * });
	 *
	 * // ---on service failure getDiagnosisSubCategoryService.setOnFailed(e ->
	 * { System.out.println(
	 * "getDiagnosisSubCategoryService javafx service was failed----->>>");
	 * Main.getInstance().getUtility().showNotification("Error", "Error",
	 * getDiagnosisSubCategoryService.getException().getMessage()); }); } }
	 * catch (Exception e) { LOGGER.error("## Exception occured:" +
	 * e.getMessage());
	 * Main.getInstance().getUtility().showNotification("Error", "Error",
	 * "Something went wrong"); }
	 *
	 * }
	 *
	 *//**
		 * This method fetches PROCEDURE Sub Category
		 *
		 * @param categoryCode
		 */
	/*
	 * public void getProcedureSubCategory(String categoryCode) throws Exception
	 * { LOGGER.debug("Logger Name: " + LOGGER.getName()); try {
	 * ObservableList<String> procedureSubCategories =
	 * FXCollections.observableArrayList(); getProcedureSubCategoryService =
	 * GetProcedureSubCategoryService.getInstance(categoryCode);
	 *
	 * if (!getProcedureSubCategoryService.isRunning()) {
	 * getProcedureSubCategoryService.restart();
	 *
	 * // ---on service success getProcedureSubCategoryService.setOnSucceeded(e
	 * -> { System.out.println(
	 * "getProcedureSubCategoryService javafx service was success----->>>");
	 *
	 * procedureSubCategories.add("Select"); procedureSubCategoriesDB =
	 * getProcedureSubCategoryService.getProcedureSubCategory();
	 *
	 * if (procedureSubCategoriesDB != null) { for (Proceduresubcategory obj :
	 * procedureSubCategoriesDB) { if (obj != null) {
	 * procedureSubCategories.add(obj.getProcedureSubCategoryValue()); } }
	 *
	 * choiceBoxBodySystem.setItems(procedureSubCategories);
	 * choiceBoxBodySystem.getSelectionModel().select(0); }
	 *
	 * });
	 *
	 * // ---on service failure getProcedureSubCategoryService.setOnFailed(e ->
	 * { System.out.println(
	 * "getProcedureSubCategoryService javafx service was failed----->>>");
	 * Main.getInstance().getUtility().showNotification("Error", "Error",
	 * getProcedureSubCategoryService.getException().getMessage()); });
	 *
	 * } } catch (Exception e) { LOGGER.error("## Exception occured:" +
	 * e.getMessage());
	 * Main.getInstance().getUtility().showNotification("Error", "Error",
	 * "Something went wrong"); }
	 *
	 * }
	 *
	 *//**
		 * This method fetches PROCEDURE Sub Sub Category
		 *
		 * @param subCategoryCode
		 *//*
		 * public void getProcedureSubSubCategory(String subCategoryCode) throws
		 * Exception {
		 *
		 * LOGGER.debug("Logger Name: " + LOGGER.getName()); try {
		 *
		 * ObservableList<String> procedureSubSubCategories =
		 * FXCollections.observableArrayList();
		 * getProcedureSubSubCategoryService =
		 * GetProcedureSubSubCategoryService.getInstance(subCategoryCode);
		 *
		 * if (!getProcedureSubSubCategoryService.isRunning()) {
		 * getProcedureSubSubCategoryService.restart();
		 *
		 * // ---on service success
		 * getProcedureSubSubCategoryService.setOnSucceeded(e -> {
		 * System.out.println(
		 * "getProcedureSubSubCategoryService javafx service was success----->>>"
		 * );
		 *
		 * procedureSubSubCategories.add("Select"); procedureSubSubCategoriesDB
		 * = getProcedureSubSubCategoryService.getProcedureSubSubCategory();
		 *
		 * if (procedureSubSubCategoriesDB != null) { for
		 * (Proceduresubsubcategory obj : procedureSubSubCategoriesDB) { if (obj
		 * != null) {
		 * procedureSubSubCategories.add(obj.getProcedureSubSubCategoryValue());
		 * } }
		 *
		 * choiceBoxOperation.setItems(procedureSubSubCategories);
		 * choiceBoxOperation.getSelectionModel().select(0); }
		 *
		 * });
		 *
		 * // ---on service failure
		 * getProcedureSubSubCategoryService.setOnFailed(e -> {
		 * System.out.println(
		 * "getProcedureSubSubCategoryService javafx service was failed----->>>"
		 * ); Main.getInstance().getUtility().showNotification("Error", "Error",
		 * getProcedureSubSubCategoryService.getException().getMessage()); });
		 *
		 * } } catch (Exception e) { LOGGER.error("## Exception occured:" +
		 * e.getMessage());
		 * Main.getInstance().getUtility().showNotification("Error", "Error",
		 * "Something went wrong"); }
		 *
		 * }
		 *
		 *
		 *
		 *
		 *
		 *
		 */

	/**
	 * This method is used to call CreateCase DB service for create and update a
	 * case
	 *
	 * @throws Exception
	 */
	private void callCreateCaseService() throws Exception {

		try {
			createCaseService.restart();

			createCaseSuccessHandler = new EventHandler<WorkerStateEvent>() {

				@Override
				public void handle(WorkerStateEvent event) {

					Patientcase result = createCaseService.getResult();

					if (result != null) {
						/*
						 * SimpleDateFormat simpleDateFormat = new
						 * SimpleDateFormat("yyyy-MM-dd HH:mm");
						 * if(result.getAdmitDateTime()!=null){ Date admitDate=
						 * result.getAdmitDateTime(); try { admitDate=
						 * simpleDateFormat.parse(simpleDateFormat.format(
						 * admitDate)); } catch (ParseException e) { // TODO
						 * Auto-generated catch block e.printStackTrace(); }
						 * result.setAdmitDateTime(admitDate); }
						 */
						// if(result.getPlannedProcedureDateTime()!=null){
						// Date planDate= result.getPlannedProcedureDateTime();
						// result.setPlannedProcedureDateTime(new
						// Date(simpleDateFormat.format(planDate)));
						// }

						btnProceed.setDisable(true);

						try {
							PatientSession.getInstance().setPatientCase(result);
							if (!EditCaseFlagSession.getInstance().getIsEdit()) {
								closeCreateCasePopup();

								Main.getInstance().getStageManager().switchScene(FxmlView.MAIN);

								Main.getInstance().getUtility().showNotification("Information", "Success",
										"Case created successfully!");
								//
								//
								// FXMLLoader fxmlLoader = new FXMLLoader(
								// getClass().getResource("/View/NewDashboard.fxml"));
								// Pane pane = fxmlLoader.load();
								//
								// StackPane stackPane = new StackPane();
								// stackPane.getChildren().add(pane);
								//
								// Scene scene = new Scene(stackPane);
								// scene.getStylesheets()
								// .add(this.getClass().getResource("/styles/newStyles.css").toString());
								//
								// Main.getInstance().getParentStage().setScene(scene);
								// Main.getInstance().getParentStage().show();
								//
								// Main.getInstance().setParentScene(scene);

							} else {
								closeEditCasePopup();
								Main.getInstance().getUtility().showNotification("Information", "Success",
										"Case updated successfully!");

							}

						} catch (Exception e1) {

							LOGGER.error("## Exception occured:", e1);
						}

					}
					disablePaneMainContent.setVisible(false);

					createCaseService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
							createCaseSuccessHandler);
					createCaseService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED, createCaseFailedHandler);
					createCaseSuccessHandler = null;
					createCaseFailedHandler = null;
				}
			};

			createCaseService.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, createCaseSuccessHandler);

			createCaseFailedHandler = new EventHandler<WorkerStateEvent>() {

				@Override
				public void handle(WorkerStateEvent event) {
					disablePaneMainContent.setVisible(false);

					createCaseService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
							createCaseSuccessHandler);
					createCaseService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED, createCaseFailedHandler);
					createCaseSuccessHandler = null;
					createCaseFailedHandler = null;
					Main.getInstance().getUtility().showNotification("Error", "Error",
							createCaseService.getException().getMessage());
				}
			};

			createCaseService.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED, createCaseFailedHandler);

		} catch (Exception e) {

			LOGGER.error("## Exception occured:" + e);

		}
	}

	/**
	 * This method call GetCase DB service
	 */
	/*
	 * public void callGetCaseService(Long caseId) throws Exception {
	 * LOGGER.debug("Logger Name: " + LOGGER.getName()); try { getCaseService =
	 * GetCaseService.getInstance(caseId); getCaseService.restart();
	 *
	 * getCaseService.setOnSucceeded(e -> { System.out.println(
	 * "getCaseService javafx service was successful----->>>"); Patientcase
	 * createdCaseObj = getCaseService.getPatientCase();
	 * PatientSession.getInstance().setPatientCase(createdCaseObj); try {
	 * ControllerMediator.getInstance().getLoginController().
	 * navigateToMainDashboard(); } catch (Exception e1) {
	 *
	 * e1.printStackTrace(); }
	 *
	 * });
	 *
	 * getCaseService.setOnFailed(e -> { System.out.println(
	 * "getCaseService javafx service was failed----->>>");
	 * Main.getInstance().getUtility().showNotification("Error", "Error",
	 * getCaseService.getException().getMessage()); }); } catch (Exception e) {
	 * LOGGER.error("## Exception occured:" + e.getMessage());
	 * Main.getInstance().getUtility().showNotification("Error", "Error",
	 * "Something went wrong"); } }
	 */

	/**
	 * This method call search service for DIAGNOSIS
	 *
	 * @param searchDiagnosisEntity
	 */
	/*
	 *
	 * public void callSearchDiagnosisServie(SearchDiagnosis
	 * searchDiagnosisEntity) throws Exception {
	 *
	 * LOGGER.debug("Logger Name: " + LOGGER.getName()); try {
	 * ObservableList<String> diagnosisSearchList =
	 * FXCollections.observableArrayList(); searchDiagnosisService =
	 * SearchDiagnosisServcie.getInstance(searchDiagnosisEntity);
	 *
	 * if (!searchDiagnosisService.isRunning()) {
	 * searchDiagnosisService.restart();
	 *
	 * // ---on service success searchDiagnosisService.setOnSucceeded(e -> {
	 *
	 * System.out.println(
	 * "searchDiagnosisService javafx service was success----->>>");
	 * listDiagSearch.setVisible(true); diagnosisSearchListDB =
	 * searchDiagnosisService.getDiagnosisList();
	 *
	 * if (diagnosisSearchListDB != null) { for (Diagnosis obj :
	 * diagnosisSearchListDB) { diagnosisSearchList.add(obj.getDiagnosisName());
	 * }
	 *
	 * listDiagSearch.setItems(diagnosisSearchList);
	 *
	 * textAutoSearchDiag.setDisable(false); textAutoSearchDiag.requestFocus();
	 * textAutoSearchDiag.end();
	 *
	 * if (diagnosisSearchListDB.isEmpty()) { listDiagSearch.setVisible(false);
	 * btnAddDiagSearch.setDisable(true); } else {
	 * listDiagSearch.setVisible(true); } }
	 *
	 * });
	 *
	 * // ---on service failure searchDiagnosisService.setOnFailed(e -> {
	 * System.out.println(
	 * "searchDiagnosisService javafx service was failed----->>>");
	 * Main.getInstance().getUtility().showNotification("Error", "Error",
	 * searchDiagnosisService.getException().getMessage()); }); } } catch
	 * (Exception e) { LOGGER.error("## Exception occured:" + e.getMessage());
	 * Main.getInstance().getUtility().showNotification("Error", "Error",
	 * "Something went wrong"); } }
	 *
	 *//**
		 * This method call search service for PROCEDURE
		 *
		 * @param Id
		 * @param searchValue
		 *//*
		 * public void callSearchProcedureServie(String Id, String searchValue)
		 * throws Exception { LOGGER.debug("Logger Name: " + LOGGER.getName());
		 * try { searchProcedureService = SearchProcedureService.getInstance(Id,
		 * searchValue); ObservableList<String> procedureSearchList =
		 * FXCollections.observableArrayList();
		 *
		 * if (!searchProcedureService.isRunning()) {
		 * searchProcedureService.restart();
		 *
		 * // ---on service success searchProcedureService.setOnSucceeded(e -> {
		 * System.out.println(
		 * "searchProcedureService javafx service was success----->>>");
		 * listPlanSearch.setVisible(true); procedureSearchListDB =
		 * searchProcedureService.getProcedureList();
		 *
		 * if (procedureSearchListDB != null) { for (Plannedprocedure obj :
		 * procedureSearchListDB) { if (obj != null) {
		 * procedureSearchList.add(obj.getProcedureName()); } }
		 *
		 * listPlanSearch.setItems(procedureSearchList);
		 *
		 * textAutoSearchPlan.setDisable(false);
		 * textAutoSearchPlan.requestFocus(); textAutoSearchPlan.end();
		 *
		 * if (procedureSearchListDB.isEmpty()) {
		 * listPlanSearch.setVisible(false); btnAddSearchPlan.setDisable(true);
		 * } else { listPlanSearch.setVisible(true); } }
		 *
		 * }); // ---on service failure searchProcedureService.setOnFailed(e ->
		 * { System.out.println(
		 * "searchProcedureService javafx service was failed----->>>");
		 * Main.getInstance().getUtility().showNotification("Error", "Error",
		 * searchProcedureService.getException().getMessage()); }); } } catch
		 * (Exception e) { LOGGER.error("## Exception occured:" +
		 * e.getMessage());
		 * Main.getInstance().getUtility().showNotification("Error", "Error",
		 * "Something went wrong"); } }
		 *
		 *
		 *
		 */

	/**
	 * This method is used to set toggle groups for radio buttons
	 *
	 * @throws Exception
	 */
	private void setRadioButtons() throws Exception {

		try {
			surgeryGroup = new ToggleGroup();
			unitGroup = new ToggleGroup();

			radioUnitDayCare.setUserData("Day Care");
			radioUnitDayCare.setToggleGroup(unitGroup);

			radioUnitInPatient.setUserData("In patient");
			radioUnitInPatient.setToggleGroup(unitGroup);

			radioSurgeryEmergency.setUserData("Emergency");
			radioSurgeryEmergency.setToggleGroup(surgeryGroup);

			radioSurgerySemiEmergency.setUserData("Semi-Emergency");
			radioSurgerySemiEmergency.setToggleGroup(surgeryGroup);

			radioSurgeryElective.setUserData("Elective");
			radioSurgeryElective.setToggleGroup(surgeryGroup);
		} catch (Exception e) {
			LOGGER.error("## Exception occured:" + e);

		}

	}

	/**
	 * This method handles Diagnosis radiobutton clicks
	 */
	/*
	 * private void DiagRadioSelect() throws Exception {
	 *
	 * LOGGER.debug("Logger Name: " + LOGGER.getName()); try {
	 * lblSelectedSearchDiag.setVisible(false);
	 *
	 * if (radioDiagFav.isSelected()) { System.out.println(
	 * "Diagnosis fav selected");
	 *
	 * btnAddFavDiag.setDisable(true); btnDeleteFavDiag.setDisable(true);
	 * tableDiagnosis.getSelectionModel().clearSelection();
	 * listViewFavDiag.getSelectionModel().clearSelection();
	 *
	 * vbDiagFav.setVisible(true); // vbDiagSearch.setVisible(false);
	 * stackPaneDiagSearch.setVisible(false); } if
	 * (radioDiagSearch.isSelected()) { System.out.println(
	 * "Diagnosis search selected");
	 *
	 * btnAddDiagSearch.setDisable(true); btnDeleteDiagSearch.setDisable(true);
	 * tableDiagnosis.getSelectionModel().clearSelection();
	 * listDiagSearch.getSelectionModel().clearSelection();
	 * textAutoSearchDiag.setText(""); stackPaneDiagSearch.setVisible(true);
	 * listDiagSearch.setVisible(false); vbDiagFav.setVisible(false);
	 *
	 * } } catch (Exception e) { LOGGER.error("## Exception occured:" +
	 * e.getMessage());
	 * Main.getInstance().getUtility().showNotification("Error", "Error",
	 * "Something went wrong"); }
	 *
	 * }
	 *
	 *//**
		 * This method handles Plan radiobutton clicks
		 *//*
		 * private void PlanRadioSelect() throws Exception { LOGGER.debug(
		 * "Logger Name: " + LOGGER.getName()); try {
		 * lblSelectedSearchPlan.setVisible(false);
		 * lblSelectedSearchPlan.setText("");
		 *
		 * if (radioPlanFav.isSelected()) { btnAddFavPlan.setDisable(true);
		 * btnDeleteFavPlan.setDisable(true);
		 * tablePlannedProcedure.getSelectionModel().clearSelection();
		 * listViewFavPlannedProcedure.getSelectionModel().clearSelection();
		 * vbPlanFav.setVisible(true); stackPanePlanSearch.setVisible(false);
		 *
		 * } if (radioPlanSearch.isSelected()) {
		 *
		 * btnAddSearchPlan.setDisable(true);
		 * btnDeleteSearchPlan.setDisable(true);
		 * tablePlannedProcedure.getSelectionModel().clearSelection();
		 * textAutoSearchPlan.setText(""); stackPanePlanSearch.setVisible(true);
		 * listPlanSearch.setVisible(false); vbPlanFav.setVisible(false);
		 *
		 * } } catch (Exception e) { LOGGER.error("## Exception occured:" +
		 * e.getMessage());
		 * Main.getInstance().getUtility().showNotification("Error", "Error",
		 * "Something went wrong"); }
		 *
		 * }
		 */

	/**
	 * This method is used to validate create case form
	 *
	 * @return
	 */
	private boolean validateCaseDetails() {
		try {

			// mandatory check

			if (choiceboxSpecialities.getSelectionModel().getSelectedIndex() == 0) {
				lblErrorMsg.setText("*Please fill all the mandatory fields");
				return false;
			}

			// for admit date time
			if (datepickerAdmitDate.getValue() != null) {
				if (Main.getInstance().getUtility().getTime(hBoxCaseAdmitTimeControl) != null) {
					try {
						SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
						Date dateEntered = simpleDateFormat.parse(datepickerAdmitDate.getValue() + " "
								+ Main.getInstance().getUtility().getTime(hBoxCaseAdmitTimeControl));
						if (Validations.futureDateCheck(dateEntered)) {
							lblErrorMsg.setText("*Future date is not allowed for Admit.");
							return false;
						}
					} catch (Exception e) {
						LOGGER.error("## Exception occured:", e);
					}
				} else {
					try {
						SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
						Date dateEntered = simpleDateFormat.parse(datepickerAdmitDate.getValue().toString());

						if (Validations.futureDateCheck(dateEntered)) {
							lblErrorMsg.setText("*Future date is not allowed for Admit.");
							return false;
						}
					} catch (Exception e) {
						LOGGER.error("## Exception occured:", e);
					}
				}
			}

//			// for anesthesialogist
//			if (txtAnaesthesiologist.getText() != null && !txtAnaesthesiologist.getText().trim().isEmpty()) {
//				if (!Validations.isAlphaWithSpaceAndDot(txtAnaesthesiologist.getText())
//						|| !Validations.maxLength(txtAnaesthesiologist.getText(), 250)) {
//					lblErrorMsg.setText("*Please enter Anaesthesialogist name upto 250 characters");
//					return false;
//				}
//			}
//			// for other anesthesialogist
//			if (txtOtheranaesthesiologist.getText() != null && !txtOtheranaesthesiologist.getText().trim().isEmpty()) {
//				if (!Validations.isAlphaWithSpaceAndDot(txtOtheranaesthesiologist.getText())
//						|| !Validations.maxLength(txtOtheranaesthesiologist.getText(), 250)) {
//					lblErrorMsg.setText("*Please enter Other Anaesthesialogist name upto 250 characters");
//					return false;
//				}
//			}
			// for ot
			if (txtOT.getText() != null && !txtOT.getText().trim().isEmpty()) {
				if (!Validations.isAlphaNumericWithSpaceAndSpecialCharacter(txtOT.getText())
						|| !Validations.maxLength(txtOT.getText(), 100)) {
					lblErrorMsg.setText("*Please enter a valid OT name upto 100 characters");
					return false;
				}
			}
//			// for surgeons
//			if (txtSurgeons.getText() != null && !txtSurgeons.getText().trim().isEmpty()) {
//				if (!Validations.isAlphaWithSpaceAndDot(txtSurgeons.getText())
//						|| !Validations.maxLength(txtSurgeons.getText(), 250)) {
//					lblErrorMsg.setText("*Please enter a valid Surgeons name upto 250 characters");
//					return false;
//				}
//			}
			// for any other details

			if (textAreaDetails.getText() != null && !textAreaDetails.getText().trim().isEmpty()
					&& !Validations.maxLength(textAreaDetails.getText(), 2000)) {
				lblErrorMsg.setText("*Please enter Any Other Details less than 2000 characters");
				return false;
			}
			if (textAreaDiagnosisDetails.getText() != null && !textAreaDiagnosisDetails.getText().trim().isEmpty()
					&& !Validations.maxLength(textAreaDiagnosisDetails.getText(), 2000)) {
				lblErrorMsg.setText("*Please enter Diagnosis Details less than 2000 characters");
				return false;
			}
			if (textAreaPlanDetails.getText() != null && !textAreaPlanDetails.getText().trim().isEmpty()
					&& !Validations.maxLength(textAreaPlanDetails.getText(), 2000)) {
				lblErrorMsg.setText("*Please enter Planned Procedure Details less than 2000 characters");
				return false;
			}
			return true;
		} catch (Exception e) {
			LOGGER.error("## Exception occured:", e);
			return false;
		}

	}

	/**
	 * This method is used to validate Diagnosis Details Text input
	 *
	 * @return
	 */
	private boolean validateDiagnosis() {
		try {
			if (textAreaDiagnosisDetails.getText() != null && !textAreaDiagnosisDetails.getText().trim().isEmpty()
					&& !Validations.maxLength(textAreaDiagnosisDetails.getText(), 2000)) {
				lblErrorMsg.setText("*Please enter Diagnosis details less than 2000 characters");
				return false;
			}
			return true;

		} catch (Exception e) {
			LOGGER.error("## Exception occured:", e);
			return false;
		}
		// for diagnosis text


	}

	/**
	 * This method is used to validate Planned Procedure Details Text input
	 *
	 * @return
	 */
	private boolean validatePlannedProcedure() {
		// for diagnosis text
		try {

			if (textAreaPlanDetails.getText() != null && !textAreaPlanDetails.getText().trim().isEmpty()
					&& !Validations.maxLength(textAreaPlanDetails.getText(), 2000)) {
				lblErrorMsg.setText("*Please enter procedure details less than 2000 characters");
				return false;
			}
			return true;

		} catch (Exception e) {
			LOGGER.error("## Exception occured:", e);
			return false;
		}


	}

	/**
	 * This method is used to set Admit case time in time control
	 *
	 * @param time
	 *            local time in string
	 * @throws Exception
	 */
	public void setCaseAdmitTime(String time) throws Exception {
		try {
			String[] hourMinutearray = time.split(":");
			int hour = 0;
			if (Integer.valueOf(hourMinutearray[0].toString()) < 12) {
				meridiemSpinnerCaseAdmit.getValueFactory().setValue("AM");
				hour = Integer.valueOf(hourMinutearray[0].toString());
				if (hour == 0) {
					hour = 12;
				}
			} else {
				meridiemSpinnerCaseAdmit.getValueFactory().setValue("PM");
				hour = Integer.valueOf(hourMinutearray[0].toString());
				if (hour != 12) {
					hour = hour - 12;
				}

			}
			if (hour < 10) {
				hourSpinnerCaseAdmit.getValueFactory().setValue("0" + hour);
			} else {
				hourSpinnerCaseAdmit.getValueFactory().setValue("" + hour);
			}

			minuteSpinnerCaseAdmit.getValueFactory().setValue(hourMinutearray[1]);
		} catch (Exception e) {
			LOGGER.error("## Exception occured:" + e);

		}
	}

	/**
	 * This method is used to set Planned Procedure time in time control
	 *
	 * @param time
	 *            local time in string
	 * @throws Exception
	 */
	private void setCasePlanTime(String time) throws Exception {

		try {
			String[] hourMinutearray = time.split(":");
			int hour = 0;
			if (Integer.valueOf(hourMinutearray[0].toString()) < 12) {
				meridiemSpinnerCasePlan.getValueFactory().setValue("AM");
				hour = Integer.valueOf(hourMinutearray[0].toString());
				if (hour == 0) {
					hour = 12;
				}
			} else {
				meridiemSpinnerCasePlan.getValueFactory().setValue("PM");
				hour = Integer.valueOf(hourMinutearray[0].toString());
				if (hour != 12) {
					hour = hour - 12;
				}

			}
			if (hour < 10) {
				hourSpinnerCasePlan.getValueFactory().setValue("0" + hour);
			} else {
				hourSpinnerCasePlan.getValueFactory().setValue("" + hour);
			}

			minuteSpinnerCasePlan.getValueFactory().setValue(hourMinutearray[1]);
		} catch (Exception e) {
			LOGGER.error("## Exception occured:" + e);

		}
	}

	/**
	 * This method is used to reset Admit Case time control
	 *
	 * @throws Exception
	 */
	private void resetTime() throws Exception {

		try {
			LocalTime timeNow = Main.getInstance().getUtility().getLocalSystemTime();
			setCaseAdmitTime(timeNow.toString());
			setCasePlanTime(timeNow.toString());
		} catch (Exception e) {
			LOGGER.error("## Exception occured:" + e);

		}

	}

	/**
	 * This method is used to remove all listeners and nullify class level
	 * variables
	 */
	public void removeListeners() {
		try {
			btnProceed.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnProceedHandler);
			btnClose.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnCloseHandler);
			btnReset.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnResetHandler);
			// btnPrevPage.removeEventHandler(MouseEvent.MOUSE_CLICKED,
			// btnPrevPageHandler);
			btnAdmitTime.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnAdmitTimeHandler);
			btnPlanTime.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnPlanTimeHandler);
			unitGroup.selectedToggleProperty().removeListener(unitGroupChangeListener);
			surgeryGroup.selectedToggleProperty().removeListener(surgeryGroupChangeListener);

			txtAnaesthesiologist.textProperty().removeListener(textAnaesthesiologistListener);
			txtAnaesthesiologist.focusedProperty().removeListener(AneathesiaFocusChangeListener);
			txtOT.textProperty().removeListener(textOTChangeListener);
			txtOT.focusedProperty().removeListener(OtFocusChangeListener);
			listViewAnaesthesiologist.getSelectionModel().selectedItemProperty()
					.removeListener(listViewAnaesthesiologistListener);
			listViewOT.getSelectionModel().selectedItemProperty().removeListener(listViewOTChangeListener);
			
			choiceBoxMainCat.getSelectionModel().selectedItemProperty().removeListener(choiceBoxMainCatListener);

			patientCaseObj = null;
			AneastheologistsList = null;
			aneastheologistsSearchList = null;
			operationTheaterList = null;
			OTSearchList = null;
			unitValue = null;
			surgeryTypeValue = null;
			isAnasthesioListSearchSelected = null;
			isOTListSearchSelected = null;
			editCaseDetails = null;
			hourSpinnerCaseAdmit = null;
			minuteSpinnerCaseAdmit = null;
			meridiemSpinnerCaseAdmit = null;
			hBoxCaseAdmitTimeControl = null;
			hourSpinnerCasePlan = null;
			minuteSpinnerCasePlan = null;
			meridiemSpinnerCasePlan = null;
			hBoxCasePlanTimeControl = null;
			selectedSpeciality = null;

			btnResetHandler = null;
			btnProceedHandler = null;
			btnCloseHandler = null;
			// btnPrevPageHandler = null;
			btnAdmitTimeHandler = null;
			btnPlanTimeHandler = null;

			listViewAnaesthesiologistListener = null;
			listViewOTChangeListener = null;
			textOTChangeListener = null;
			textAnaesthesiologistListener = null;
			OtFocusChangeListener = null;
			AneathesiaFocusChangeListener = null;
			choiceBoxMainCatListener=null;

		} catch (Exception e) {
			LOGGER.error("## Exception occured:" + e);
		}

	}
}
