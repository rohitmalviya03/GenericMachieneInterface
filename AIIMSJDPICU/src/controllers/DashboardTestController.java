/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package controllers;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.cdac.common.GeneratedEntities.Investigationparameterfields;
import com.cdac.common.GeneratedEntities.Investigationstests;
import com.cdac.common.pojoClasses.InvestigationMapperAndValue;
import com.cdac.common.pojoClasses.InvestigationSetValue;
import com.cdac.common.util.Validations;

import application.Main;
import javafx.application.Platform;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import mediatorPattern.ControllerMediator;
import model.PatientSession;
import model.TestParamModel;
import services.GetAllTestParamService;
import services.GetInvestigationSetValueListService;
import services.SaveTestService;
import services.UpdateTestService;

/**
 * This controller is used to add Test values in Data base
 *
 * @author Sudeep_Sahoo
 *
 */
public class DashboardTestController implements Initializable {

	@FXML
	private Label lblTime;

	@FXML
	private Button btnClose;

	@FXML
	private TitledPane titledPaneTest;

	@FXML
	private TextField txtTestName;

//	@FXML
//	private VBox vbCommonDetails;

	@FXML
	private TextArea txtAreaComments;

	@FXML
	private FlowPane flowPaneTest;

	@FXML
	private Label lblErrorMsg;

	@FXML
	private Button btnReset;

	@FXML
	private Button btnsave;

	@FXML
	private AnchorPane disablePaneMainContent;

	private List<InvestigationSetValue> investigationValueList = new ArrayList<InvestigationSetValue>();
	private int selectedTestId;
	private List<TestParamModel> paramList;
	private List<String> columnHeadrsUpdated = new ArrayList<String>();
	private List<String> columnName = new ArrayList<String>();
	private Boolean isTestSession = false;
	TextArea txtArea = new TextArea();
	private Boolean isValidForm = true;
	private Boolean isEmptyForm = true;
	private boolean invalidEntry = false;
	private Investigationstests selectedTestObj = new Investigationstests();

	Spinner<String> hourSpinner = new Spinner<>();
	Spinner<String> minuteSpinner = new Spinner<>();
	Spinner<String> meridiemSpinner = new Spinner<>();

	private Date selectedTime;
	private Map<String, TextField> textboxMap = new HashMap<String, TextField>();
	private InvestigationMapperAndValue investigationMapperAndValue;

	public Map<String, TextField> getTextboxMap() {
		return textboxMap;
	}

	public void setInvestigationMapperAndValue(InvestigationMapperAndValue investigationMapperAndValue) {
		this.investigationMapperAndValue = investigationMapperAndValue;
	}

	public Investigationstests getSelectedTestObj() {
		return selectedTestObj;
	}

	public void setSelectedTestObj(Investigationstests selectedTestObj) {
		this.selectedTestObj = selectedTestObj;
	}

	public Label getLblTime() {
		return lblTime;
	}

	public TextField getTxtTestName() {
		return txtTestName;
	}

	public void setSelectedTime(Date selectedTime) {
		this.selectedTime = selectedTime;
	}

	private List<InvestigationSetValue> testValuesList = new ArrayList<InvestigationSetValue>();

	private EventHandler<WorkerStateEvent> getInvestigationSetValueListServiceSuccessHandler;

	private EventHandler<WorkerStateEvent> getInvestigationSetValueListServiceFailedHandler;

	private EventHandler<WorkerStateEvent> updateTestServiceSuccessHandler;

	private EventHandler<WorkerStateEvent> updateTestServiceFailedHandler;

	private EventHandler<WorkerStateEvent> saveTestServiceSuccessHandler;

	private EventHandler<WorkerStateEvent> saveTestServiceFailedHandler;

	private EventHandler<WorkerStateEvent> getAllTestParamServiceSuccessHandler;

	private EventHandler<WorkerStateEvent> getAllTestParamServiceFailedHandler;

	private GetInvestigationSetValueListService getInvestigationSetValueListService;

	private EventHandler<javafx.scene.input.KeyEvent> enterKeyPressEventHandler;

	private UpdateTestService updateTestService;

	private EventHandler<MouseEvent> saveHandler;

	private EventHandler<MouseEvent> closeHandler;

	private boolean isTertiaryWindow;

	private boolean fromHistoryScreen;

	public void setFromHistoryScreen(boolean fromHistoryScreen) {
		this.fromHistoryScreen = fromHistoryScreen;
	}

	public boolean isTertiaryWindow() {
		return isTertiaryWindow;
	}

	public void setTertiaryWindow(boolean isTertiaryWindow) {
		this.isTertiaryWindow = isTertiaryWindow;
	}

	private static final Logger LOG = Logger.getLogger(DashboardTestController.class.getName());

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			ControllerMediator.getInstance().registerDashboardTestController(this);
			txtTestName.setDisable(true);
			closeHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent event) {
					try {
						closePopup();
					} catch (Exception e) {
						LOG.error("## Exception occured:", e);
					}
				}
			};
			btnClose.addEventHandler(MouseEvent.MOUSE_CLICKED, closeHandler);
			saveHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent event) {

					try {
						if (investigationMapperAndValue != null) {

							if (investigationMapperAndValue.getInvestigationvaluescasemapper() == null) {
								saveTestParams();
							} else {
								getAllTestParamandIds();
							}
						}
					} catch (Exception e) {
						LOG.error("## Exception occured:", e);

					}
				}
			};
			btnsave.addEventHandler(MouseEvent.MOUSE_CLICKED, saveHandler);
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
			btnClose.removeEventHandler(MouseEvent.MOUSE_CLICKED, closeHandler);
			btnsave.removeEventHandler(MouseEvent.MOUSE_CLICKED, saveHandler);

			for (TestParamModel obj : paramList) {

				TextField txtField= new  TextField();
				txtField=obj.getTxtTestParam();
				txtField.removeEventHandler(javafx.scene.input.KeyEvent.KEY_RELEASED, enterKeyPressEventHandler);

			}


		} catch (Exception e) {
			LOG.error("## Exception occured:", e);
		}

	}

	/**
	 * This Method is used get test value list from data base
	 */
	private void getAllTestParamandIds() {
		try {

			isEmptyForm = true;
			getInvestigationSetValueListService = GetInvestigationSetValueListService.getInstance();
			getInvestigationSetValueListService.restart();

			getInvestigationSetValueListServiceSuccessHandler = new EventHandler<WorkerStateEvent>() {

				@Override
				public void handle(WorkerStateEvent event) {

					try {
						List<InvestigationSetValue> testParamandIDList = getInvestigationSetValueListService
								.getInvestigationSetValueList();
						List<InvestigationSetValue> testsList = new ArrayList<InvestigationSetValue>();
						for (Map.Entry<String, TextField> entry : textboxMap.entrySet()) {
							InvestigationSetValue obj = new InvestigationSetValue();
							for (InvestigationSetValue paramAndIDobj : testParamandIDList) {
								if (paramAndIDobj.getName().trim().equalsIgnoreCase(entry.getKey())) {
									obj = paramAndIDobj;
									break;
								}
							}

							if (!entry.getValue().getText().isEmpty()){
								if (Validations.decimalsWithNegative(entry.getValue().getText())) {

									invalidEntry = false;
									lblErrorMsg.setVisible(false);
									isEmptyForm = false;
									obj.setValue(BigDecimal.valueOf(Double.parseDouble(entry.getValue().getText())));

								} else {
									isEmptyForm = false;
									lblErrorMsg.setText(
											"Please enter test value upto 10 digits and decimal upto 2 digits only ");
									invalidEntry = true;
									lblErrorMsg.setVisible(true);
									break;
								}

							}
							testsList.add(obj);
							// isEmptyForm=false;
						}
//						for (TestParamModel obj : paramList) {
//
//							if (!obj.getTxtTestParam().getText().isEmpty()) {
//
//								if (Validations.decimalsWithNegative(obj.getTxtTestParam().getText())) {
//
//									invalidEntry = false;
//									lblErrorMsg.setVisible(false);
//									isEmptyForm = false;
//
//								} else {
//									isEmptyForm = false;
//									lblErrorMsg.setText(
//											"Please enter test value upto 10 digits and decimal upto 2 digits only ");
//									invalidEntry = true;
//									lblErrorMsg.setVisible(true);
//									break;
//								}
//							}
//						}




						if (!isEmptyForm) {
							if (!invalidEntry) {
								updateTest(investigationMapperAndValue, testsList);
							}
						} else {
							lblErrorMsg.setVisible(true);
							isValidForm = false;
							lblErrorMsg.setText("Please enter atleast one test value.");
							disablePaneMainContent.setVisible(false);
						}
						getInvestigationSetValueListService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
								getInvestigationSetValueListServiceSuccessHandler);
						getInvestigationSetValueListService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
								getInvestigationSetValueListServiceFailedHandler);
					} catch (Exception e) {
						LOG.error("## Exception occured:", e);
					}
				}
			};

			getInvestigationSetValueListService.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
					getInvestigationSetValueListServiceSuccessHandler);

			getInvestigationSetValueListServiceFailedHandler = new EventHandler<WorkerStateEvent>() {

				@Override
				public void handle(WorkerStateEvent event) {
					try {
						Main.getInstance().getUtility().showNotification("Error", "Error",
								getInvestigationSetValueListService.getException().getMessage());
						getInvestigationSetValueListService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
								getInvestigationSetValueListServiceSuccessHandler);
						getInvestigationSetValueListService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
								getInvestigationSetValueListServiceFailedHandler);
					} catch (Exception e) {
						LOG.error("## Exception occured:", e);
					}
				}

			};

			getInvestigationSetValueListService.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
					getInvestigationSetValueListServiceFailedHandler);

		} catch (Exception e) {
			LOG.error("## Exception occured:", e);
		}
	}

	/**
	 * This method is used to update the test values in Data base
	 *
	 * @param investigationMapperAndValue
	 * @param testsList
	 */
	private void updateTest(InvestigationMapperAndValue investigationMapperAndValue,
			List<InvestigationSetValue> testsList) {

		try {
			updateTestService = UpdateTestService.getInstance(
					investigationMapperAndValue.getInvestigationvaluescasemapper().getInvestigationValuesCaseMapperId(),
					txtAreaComments.getText(), investigationMapperAndValue.getInvestigationvaluescasemapper().getDate(),
					testsList);

			updateTestService.restart();

			updateTestServiceSuccessHandler = new EventHandler<WorkerStateEvent>() {

				@Override
				public void handle(WorkerStateEvent event) {
					try {
						closePopup();
						if (updateTestService.getResult().equalsIgnoreCase("Succesfully updated the values"))
							Main.getInstance().getUtility().showNotification("Information", "Success",
									"Test updated successfully!");
						Calendar cal = Calendar.getInstance();
						cal.setTime(investigationMapperAndValue.getInvestigationvaluescasemapper().getDate());
						if (fromHistoryScreen) {
							ControllerMediator.getInstance().getGridHistoricalViewController()
									.fillTestsGridCell(testsList, cal, txtTestName.getText());
						}
						ControllerMediator.getInstance().getMainController().getDrawTabbedCenter()
								.fillTestsGridCell(testsList, cal, txtTestName.getText());
						updateTestService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
								updateTestServiceSuccessHandler);
						updateTestService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
								updateTestServiceFailedHandler);
					} catch (Exception e) {
						LOG.error("## Exception occured:", e);
					}
				}

			};

			updateTestService.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, updateTestServiceSuccessHandler);

			updateTestServiceFailedHandler = new EventHandler<WorkerStateEvent>() {

				@Override
				public void handle(WorkerStateEvent event) {
					try {
						Main.getInstance().getUtility().showNotification("Error", "Error",
								updateTestService.getException().getMessage());
						updateTestService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
								updateTestServiceSuccessHandler);
						updateTestService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
								updateTestServiceFailedHandler);
					} catch (Exception e) {
						LOG.error("## Exception occured:", e);
					}
				}

			};

			updateTestService.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED, updateTestServiceFailedHandler);

		} catch (Exception e) {
			LOG.error("## Exception occured:", e);
		}

	}

	/**
	 * This method closes Test screen
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
	 * This method is used to create test value list for save and call the save
	 * test value method
	 */
	public void saveTestParams() {
		try {

			validateTestForm();

			isEmptyForm = true;
			if (isValidForm) {
				disablePaneMainContent.setVisible(true);
				if (!selectedTestObj.getTestName().trim().equalsIgnoreCase("TTE")
						&& !selectedTestObj.getTestName().trim().equalsIgnoreCase("TEE")) {

					investigationValueList = new ArrayList<>();
					for (TestParamModel obj : paramList) {

						InvestigationSetValue investigationValue = new InvestigationSetValue();

						investigationValue.setInvestigationParameterFieldID(obj.getParamFieldId());
						investigationValue.setName(obj.getLblTestParam().getText());

						if (!obj.getTxtTestParam().getText().isEmpty()) {

							if (Validations.decimalsWithNegative(obj.getTxtTestParam().getText())) {

								invalidEntry = false;
								BigDecimal paramValue = new BigDecimal(obj.getTxtTestParam().getText());
								isEmptyForm = false;
								investigationValue.setValue(paramValue);
							} else {
								isEmptyForm = false;
								lblErrorMsg.setText(
										"Please enter test value upto 10 digits and decimal upto 2 digits only ");
								invalidEntry = true;
								disablePaneMainContent.setVisible(false);
								break;
							}
							investigationValueList.add(investigationValue);
						}

					}
					if (!invalidEntry) {
						if (isEmptyForm) {
							lblErrorMsg.setVisible(true);
							isValidForm = false;
							lblErrorMsg.setText("Please enter atleast one test value.");
							disablePaneMainContent.setVisible(false);
							disablePaneMainContent.setVisible(false);
						} else {
							if (!isTestSession) {

								saveTest();

							} else {

								updateTest(investigationMapperAndValue, investigationValueList);

							}

						}

					} else {
						lblErrorMsg.setVisible(true);
					}
				} else {

					if (txtArea.getText() != null && !txtArea.getText().trim().equals("")) {
						txtAreaComments.setText(txtArea.getText());
						investigationValueList = new ArrayList<>();
						InvestigationSetValue investigationValue = new InvestigationSetValue();
						investigationValue.setInvestigationParameterFieldID(81);
						investigationValue.setName("Dummy");
						investigationValue.setValue(new BigDecimal("1"));
						investigationValueList.add(investigationValue);

						if (!isTestSession) {

							saveTest();

						} else {

						}

					} else {
						isEmptyForm = true;
						disablePaneMainContent.setVisible(false);
					}
				}

			} else {
				lblErrorMsg.setVisible(true);
				disablePaneMainContent.setVisible(false);

			}

		} catch (Exception e) {
			LOG.error("## Exception occured:", e);
		}
	}

	/**
	 * This method is used to call service and save test information in data
	 * base
	 */
	public void saveTest() {
		try {

			// getTestDateTime();

			SaveTestService saveTestService = SaveTestService.getInstance(
					PatientSession.getInstance().getPatientCase().getCaseId(), selectedTestId,
					txtAreaComments.getText(), selectedTime, investigationValueList);
			saveTestService.restart();

			saveTestServiceSuccessHandler = new EventHandler<WorkerStateEvent>() {

				@Override
				public void handle(WorkerStateEvent event) {
					try {
						String result = saveTestService.getResult();
						if (result.equalsIgnoreCase("Succesfully saved the test values")) {
							closePopup();

							Calendar cal = Calendar.getInstance();
							cal.setTime(selectedTime);
							if (fromHistoryScreen) {
								ControllerMediator.getInstance().getGridHistoricalViewController()
										.fillTestsGridCell(investigationValueList, cal, txtTestName.getText());
							}

							ControllerMediator.getInstance().getMainController().getDrawTabbedCenter()
									.fillTestsGridCell(investigationValueList, cal, txtTestName.getText());

							Main.getInstance().getUtility().showNotification("Information", "Success",
									"Test saved successfully!");
						}
						disablePaneMainContent.setVisible(false);
						saveTestService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
								saveTestServiceSuccessHandler);
						saveTestService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
								saveTestServiceFailedHandler);
					} catch (Exception e) {
						LOG.error("## Exception occured:", e);
						Main.getInstance().getUtility().showNotification("Error", "Error", "Something went wrong");

					}
				}

			};

			saveTestService.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, saveTestServiceSuccessHandler);

			saveTestServiceFailedHandler = new EventHandler<WorkerStateEvent>() {

				@Override
				public void handle(WorkerStateEvent event) {
					try {
						btnsave.setDisable(false);
						btnClose.setDisable(false);
						Main.getInstance().getUtility().showNotification("Error", "Error",
								saveTestService.getException().getMessage());
						disablePaneMainContent.setVisible(false);
						saveTestService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
								saveTestServiceSuccessHandler);
						saveTestService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
								saveTestServiceFailedHandler);
					} catch (Exception e) {
						LOG.error("## Exception occured:", e);
					}
				}

			};

			saveTestService.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED, saveTestServiceFailedHandler);

		} catch (Exception e) {
			LOG.error("## Exception occured:", e);
		}
	}

	/**
	 * This method gets input datetime and converts it to localTime
	 */
	/*
	 * public void getTestDateTime() { SimpleDateFormat simpleDateFormat = new
	 * SimpleDateFormat("yyyy-MM-dd HH:mm"); LocalDate localDate =
	 * datePickerDate.getValue(); LocalTime time =
	 * Main.getInstance().getUtility().getTime(hBoxTimeControl); String dateTime
	 * = localDate + " " + time;
	 *
	 * try { // testDateTime = simpleDateFormat.parse(dateTime); testDateTime =
	 * selectedTime; } catch (Exception e1) { // TODO Auto-generated catch block
	 * e1.printStackTrace(); } }
	 */
	/**
	 * This method is used to validate test form
	 */
	public void validateTestForm() {
		try {

			lblErrorMsg.setVisible(false);
			isValidForm = true;

			/*
			 * if (txtTestName.getText().isEmpty() || datePickerDate.getValue()
			 * == null) { lblErrorMsg.setText("Please enter mandatory fields.");
			 * isValidForm = false; return; }
			 *
			 * /// for checking whether or not the date entered is in future try
			 * { SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
			 * "yyyy-MM-dd HH:mm"); Date dateEntered = simpleDateFormat
			 * .parse(datePickerDate.getValue() + " " +
			 * Main.getInstance().getUtility().getTime(hBoxTimeControl)); if
			 * (Validations.futureDateCheck(dateEntered)) { lblErrorMsg.setText(
			 * "Future date is not allowed."); isValidForm = false; return; } }
			 * catch (Exception e) { e.printStackTrace(); }
			 */

			if (txtAreaComments.getText() != null && !txtAreaComments.getText().trim().isEmpty()
					&& !Validations.maxLength(txtAreaComments.getText(), 2000)) {
				lblErrorMsg.setText("*Please enter Comments less than 2000 characters");
				isValidForm = false;
				return;
			}

		} catch (Exception e) {
			LOG.error("## Exception occured:", e);
		}
	}

	/**
	 * This method is used to call getAllTestParamService and fetches test
	 * parameters for the selected test
	 *
	 * @param selectedTest
	 */
	public void getAllParam(Investigationstests selectedTest) {

		try {
			paramList = new ArrayList<>();
			GetAllTestParamService getAllTestParamService;

			flowPaneTest.getChildren().clear();

			if (!selectedTest.getTestName().trim().equalsIgnoreCase("TTE")
					&& !selectedTest.getTestName().trim().equalsIgnoreCase("TEE")) {
			//	vbCommonDetails.setVisible(true);
				getAllTestParamService = GetAllTestParamService.getInstance(selectedTest);
				getAllTestParamService.restart();

				selectedTestId = selectedTest.getInvestigationsTestsId();

				getAllTestParamServiceSuccessHandler = new EventHandler<WorkerStateEvent>() {

					@Override
					public void handle(WorkerStateEvent event) {
						try {
							TestParamModel testParam;
							List<Investigationparameterfields> listOfInvestigationParameterFields = new ArrayList<Investigationparameterfields>();
							columnHeadrsUpdated = new ArrayList<String>();
							columnName = new ArrayList<String>();

							columnHeadrsUpdated.add("Date");
							columnHeadrsUpdated.add("Details");
							listOfInvestigationParameterFields = getAllTestParamService
									.getListOfInvestigationParameterFields();
							if (listOfInvestigationParameterFields != null) {
								enterKeyPressEventHandler = new EventHandler<javafx.scene.input.KeyEvent>() {

									@Override
									public void handle(javafx.scene.input.KeyEvent event) {

										if (event.getCode().equals(KeyCode.ENTER)) {
											try {

												if (investigationMapperAndValue != null) {

													if (investigationMapperAndValue.getInvestigationvaluescasemapper() == null) {
														saveTestParams();
													} else {
														getAllTestParamandIds();
													}
												}

											} catch (Exception e) {
												LOG.error("## Exception occured:", e);
											}
										}
									}
								};

								for (Investigationparameterfields obj : listOfInvestigationParameterFields) {
									testParam = new TestParamModel();
									HBox hboxTest = new HBox();
									hboxTest.setSpacing(5);
									Label testParamLabel = new Label();
									testParamLabel.setPrefWidth(100);
									testParamLabel.setText(obj.getName());
									Tooltip toolTip = new Tooltip();
									toolTip.setText(obj.getName());
									testParamLabel.setTooltip(toolTip);
									TextField txtTestParam = new TextField();
									txtTestParam.setPrefWidth(85);
									txtTestParam.setId(obj.getName());

									txtTestParam.addEventHandler(javafx.scene.input.KeyEvent.KEY_RELEASED, enterKeyPressEventHandler);

									textboxMap.put(obj.getName().trim(), txtTestParam);
									Label testUnit = new Label();
									testUnit.setPrefWidth(90);
									String unitSymbol = "";
									if (obj.getMeasuringUnit() != null) {
										unitSymbol = obj.getMeasuringUnit().getUnitSymbol();

									} else {

										if (obj.getMeasuringUnitRatio() != null) {
											unitSymbol = obj.getMeasuringUnitRatio().getNumeratorUnit().getUnitSymbol()
													+ "/"
													+ obj.getMeasuringUnitRatio().getDenominatorUnit().getUnitSymbol();
										}
									}
									testUnit.setText(unitSymbol);
									testParam.setLblTestParam(testParamLabel);
									testParam.setTxtTestParam(txtTestParam);
									testParam.setLblunit(testUnit);
									testParam.setParamFieldId(obj.getInvestigationParameterFieldsId());
									paramList.add(testParam);
									String header = "";
									header = obj.getName();
									columnName.add(header);
									if (unitSymbol != null && !unitSymbol.trim().equals("")) {

										header = header + " (" + unitSymbol + ")";

									}
									columnHeadrsUpdated.add(header);
									hboxTest.getChildren().add(testParamLabel);
									hboxTest.getChildren().add(txtTestParam);
									hboxTest.getChildren().add(testUnit);
									flowPaneTest.getChildren().add(hboxTest);
									if (flowPaneTest.getChildren().size() == 1) {
										Platform.runLater(new Runnable() {
											@Override
											public void run() {
												txtTestParam.requestFocus();
											}
										});
									}
								}
								if (isTestSession) {
									for (InvestigationSetValue testValueObj : testValuesList) {
										for (TestParamModel obj : paramList) {

											if (obj.getLblTestParam().getText()
													.equalsIgnoreCase(testValueObj.getName())) {
												if (testValueObj.getValue() != null) {
													obj.getTxtTestParam().setText(testValueObj.getValue().toString());
												} else {
													obj.getTxtTestParam().setText("");
												}

											}

										}

									}

								} else {

								}

							}

							if (investigationMapperAndValue != null)
								if (investigationMapperAndValue.getInvestigationSetValueList() != null) {
									for (InvestigationSetValue investigationSetValue : investigationMapperAndValue
											.getInvestigationSetValueList()) {
										TextField txt = textboxMap.get(investigationSetValue.getName().trim());
										if (investigationSetValue.getValue() != null)
											txt.setText(String.valueOf(investigationSetValue.getValue()));
									}
								}
							getAllTestParamService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
									getAllTestParamServiceSuccessHandler);
							getAllTestParamService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
									getAllTestParamServiceFailedHandler);
						} catch (Exception e) {
							LOG.error("## Exception occured:", e);

						}
					}

				};

				getAllTestParamService.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
						getAllTestParamServiceSuccessHandler);

				getAllTestParamServiceFailedHandler = new EventHandler<WorkerStateEvent>() {

					@Override
					public void handle(WorkerStateEvent event) {
						try {
							Main.getInstance().getUtility().showNotification("Error", "Error",
									getAllTestParamService.getException().getMessage());
							getAllTestParamService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
									getAllTestParamServiceSuccessHandler);
							getAllTestParamService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
									getAllTestParamServiceFailedHandler);
						} catch (Exception e) {
							LOG.error("## Exception occured:", e);
						}
					}

				};

				getAllTestParamService.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
						getAllTestParamServiceFailedHandler);

			} else {
				columnHeadrsUpdated = new ArrayList<String>();
				columnName = new ArrayList<String>();
				columnHeadrsUpdated.add("Date");
				columnHeadrsUpdated.add("Details");
				VBox vbox = new VBox();
				Label lbl = new Label();
				lbl.setText("Comments");

				txtArea.setPromptText("Comments");
				txtArea.setText("");
				txtArea.setMaxHeight(100);
				vbox.getChildren().add(lbl);
				vbox.getChildren().add(txtArea);
				flowPaneTest.getChildren().add(vbox);
			//	vbCommonDetails.setVisible(false);

			}
		} catch (Exception e) {
			LOG.error("## Exception occured:", e);
		}

	}

	/*
	 * public void autoPopulateTextbox() { if (investigationMapperAndValue !=
	 * null) if (investigationMapperAndValue.getInvestigationSetValueList() !=
	 * null) { for (InvestigationSetValue investigationSetValue :
	 * investigationMapperAndValue .getInvestigationSetValueList()) { TextField
	 * txt = textboxMap.get(investigationSetValue.getName().trim());
	 * txt.setText(String.valueOf(investigationSetValue.getValue())); } } }
	 */

}
