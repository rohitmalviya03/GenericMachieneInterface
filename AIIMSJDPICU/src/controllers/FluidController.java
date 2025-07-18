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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.cdac.common.GeneratedEntities.Fluid;
import com.cdac.common.GeneratedEntities.Fluidvalue;
import com.cdac.common.GeneratedEntities.Patient;
import com.cdac.common.GeneratedEntities.Patientcase;
import com.cdac.common.pojoClasses.Volume;
import com.cdac.common.util.Validations;
import com.jfoenix.controls.JFXToggleButton;

import application.Main;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.PatientSession;
import model.UserSession;
import services.AddFluidService;
import services.DeleteFluidService;
import services.GetTotalFluidVolume;
import services.SearchFluidService;
import services.ViewFluidService;

/**
 * This controller is used to view Fluids log , total fluids given, update a
 * fluid log and delete a fluid log from database
 *
 * @author Sudeep_Sahoo
 *
 */
public class FluidController implements Initializable {

	private static final Logger LOGGER = Logger.getLogger(FluidController.class.getName());
	@FXML
	private Button btnClose;

	@FXML
	private TextField txtVolume;

	@FXML
	private TextArea txtAreaComments;

	@FXML
	private Button btnSave;

	@FXML
	private TextField textSearchFliud;

	// @FXML
	// private ListView<String> listViewFavFluid;

	@FXML
	private ListView<String> listViewSearchFluids;

	@FXML
	private Label lblErr;

	@FXML
	private Label lblTotalVolume;

	@FXML
	private DatePicker datePickerDate;

	@FXML
	private TableView<Fluidvalue> tableFluids;

	@FXML
	private TableColumn<Fluidvalue, String> clmnFluid;

	@FXML
	private TableColumn<Fluidvalue, String> clmnVolume;

	@FXML
	private TableColumn<Fluidvalue, Date> clmnStartTime;
	@FXML
	private Button btnDelete;

	@FXML
	private Button btnEdit;

	@FXML
	private VBox vbTotalfluids;

	@FXML
	private TitledPane titledPaneRecords;

	@FXML
	private Button btnCancel;

	@FXML
	private VBox vbTimeControl;

	@FXML
	private Button btnTime;

	@FXML
	private AnchorPane disablePaneHistory;

	// @FXML
	// private AnchorPane disablePaneFluidList;

	@FXML
	private VBox vbVolumeLeft;

	@FXML
	private TextField txtVolumeLeft;

	@FXML
	private JFXToggleButton toggleBtnStop;

	@FXML
	private VBox vbFinishDate;

	@FXML
	private DatePicker datePickerFinishDate;

	@FXML
	private VBox vbFinishTimeControl;

	@FXML
	private Button btnFinishTime;
	@FXML
	private AnchorPane anchorLoader;

	private Patient patient;
	private Patientcase patientCase;

	// private GetAllFluidsService getAllFluidsService;

	// private List<Fluid> allFluidslistDB;
	private List<Fluid> searchFluidListDB;

	// private ObservableList<String> allFluidslist;

	private String selectedSearchVal = "";
	private boolean isValid;
	private Integer selectedFluidId;

	private float totalVolume;
	private Volume fluidVolume = new Volume();
	private Boolean isFromFavList = false;

	private List<Fluidvalue> fluidList = new ArrayList<Fluidvalue>();
	private Fluidvalue selectedTableRow;
	private Label labelFluidName = new Label();
	private Label labelTotalFluid = new Label();
	Spinner<String> hourSpinner = new Spinner<>();
	Spinner<String> minuteSpinner = new Spinner<>();
	Spinner<String> meridiemSpinner = new Spinner<>();

	Spinner<String> hourFinishSpinner = new Spinner<>();
	Spinner<String> minuteFinishSpinner = new Spinner<>();
	Spinner<String> meridiemFinishSpinner = new Spinner<>();
	private HBox hBoxTimeControl;
	private HBox hBoxFinishTimeControl;
	private ChangeListener<String> textSearchFliudChangeListener;
	private ChangeListener<Boolean> textSearchFliudChangeListenerBoolean;
	private ChangeListener<String> listViewSearchFluidsChangeListenerBoolean;
	private EventHandler<MouseEvent> btnSaveHandler;
	private EventHandler<MouseEvent> btnCloseHandler;
	private EventHandler<MouseEvent> btnDeleteHandler;
	private EventHandler<MouseEvent> btnEditHandler;
	private EventHandler<MouseEvent> btnCancelHandler;
	private EventHandler<MouseEvent> btnTimeHandler;
	private EventHandler<MouseEvent> btnFinishTimeHandler;
	private ChangeListener<Boolean> toggleBtnStopChangeListener;
	private EventHandler<WorkerStateEvent> getTotalVolumeServiceSuccessHandler;
	private EventHandler<WorkerStateEvent> getTotalVolumeServiceFailedHandler;
	private EventHandler<WorkerStateEvent> searchFluidServiceSuccessHandler;
	private EventHandler<WorkerStateEvent> searchFluidServiceFailedHandler;
	private EventHandler<WorkerStateEvent> addFluidServiceSuccessHandler;
	private EventHandler<WorkerStateEvent> addFluidServiceFailedHandler;
	private EventHandler<WorkerStateEvent> viewFluidServiceSuccessHandler;
	private EventHandler<WorkerStateEvent> viewFluidServiceFailedHandler;
	private EventHandler<WorkerStateEvent> deleteFluidServiceFailedHandler;
	private EventHandler<WorkerStateEvent> deleteFluidServiceSuccessHandler;
	private ChangeListener<Fluidvalue> tableFluidsChangeListener;

	/**
	 * This method initializes the all Event handlers and Default values on page
	 * load
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		try {
			// disablePaneFluidList.setVisible(false);
			anchorLoader.setVisible(true);
			btnSave.setText("Save");
			disablePaneHistory.setVisible(false);
			// ---- Time control

			hBoxTimeControl = Main.getInstance().getUtility().getTimeControl();
			vbTimeControl.getChildren().add(hBoxTimeControl);
			hourSpinner = (Spinner) hBoxTimeControl.getChildren().get(0);
			minuteSpinner = (Spinner) hBoxTimeControl.getChildren().get(2);
			meridiemSpinner = (Spinner) hBoxTimeControl.getChildren().get(3);

			hBoxFinishTimeControl = Main.getInstance().getUtility().getTimeControl();
			hourFinishSpinner = (Spinner) hBoxFinishTimeControl.getChildren().get(0);
			minuteFinishSpinner = (Spinner) hBoxFinishTimeControl.getChildren().get(2);
			meridiemFinishSpinner = (Spinner) hBoxFinishTimeControl.getChildren().get(3);
			vbFinishTimeControl.getChildren().add(hBoxFinishTimeControl);
			// ---populating tableView
			clmnFluid.setCellValueFactory(new PropertyValueFactory<Fluidvalue, String>("fluidName"));
			clmnVolume.setCellValueFactory(new PropertyValueFactory<Fluidvalue, String>("volume"));

			/// changes from sahil.sharma08 start here
			// clmnStartTime.setCellValueFactory(new
			/// PropertyValueFactory<Fluidvalue, Date>("startTime"));

			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
			clmnStartTime.setCellValueFactory(new PropertyValueFactory("startTime"));
			clmnStartTime.setCellFactory((TableColumn<Fluidvalue, Date> column) -> {
				return new TableCell<Fluidvalue, Date>() {
					@Override
					protected void updateItem(Date item, boolean empty) {
						super.updateItem(item, empty);
						if (item == null || empty) {
							setText(null);
						} else {
							setText(formatter.format(item));
						}
					}
				};
			});
			/// changes from sahil.sharma08 end here

			// vbPageContent.setDisable(true);

			if (PatientSession.getInstance().getPatient() != null) {
				patient = PatientSession.getInstance().getPatient();
			}
			if (PatientSession.getInstance().getPatientCase() != null) {
				patientCase = PatientSession.getInstance().getPatientCase();
			}

			// ---populate favorite fluid list on startup
			// try {
			// //callGetAllFluidsService();
			//
			// if(MasterDataSession.getInstance().getMasterDataMap().get("FavFluids")!=null){
			// listViewFavFluid.setItems(MasterDataSession.getInstance().getMasterDataMap().get("FavFluids"));
			// }else{
			// callGetAllFluidsService();
			// }
			//
			// } catch (Exception e3) {
			// // TODO Auto-generated catch block
			// e3.printStackTrace();
			// }

			textSearchFliudChangeListener = new ChangeListener<String>() {

				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					lblErr.setVisible(false);
					if (!isFromFavList) {
						if (Validations.isAlphaNumericWithSpaceAndSpecialCharacter(textSearchFliud.getText())) {
							if (newValue.length() >= 5 && !newValue.isEmpty() && !newValue.equals("")) {
								if (!newValue.equalsIgnoreCase(selectedSearchVal)) {

									listViewSearchFluids.getItems().clear();
									listViewSearchFluids.getItems().addAll(new ArrayList<>());
									try {
										callSearchFluidService(newValue);
									} catch (Exception e1) {
										LOGGER.error("## Exception occured:" + e1);
									}
								}

							} else if (newValue.length() < 5) {
								listViewSearchFluids.getSelectionModel().clearSelection();
								listViewSearchFluids.setVisible(false);
							}
						} else {
							lblErr.setVisible(true);
							lblErr.setText("Please enter a valid search entry");
							listViewSearchFluids.setVisible(false);
						}
					} else {
						isFromFavList = false;
					}
				}
			};

			textSearchFliud.textProperty().addListener(textSearchFliudChangeListener);

			textSearchFliudChangeListenerBoolean = new ChangeListener<Boolean>() {

				@Override
				public void changed(ObservableValue<? extends Boolean> observable, Boolean oldPropertyValue,
						Boolean newPropertyValue) {
					if (!newPropertyValue) {
						lblErr.setVisible(false);
						listViewSearchFluids.setVisible(false);
					}
				}
			};

			textSearchFliud.focusedProperty().addListener(textSearchFliudChangeListenerBoolean);

			// listViewFavFluid.getSelectionModel().selectedItemProperty()
			// .addListener((ObservableValue<? extends String> observable,
			// String
			// oldValue, String newValue) -> {
			// if (newValue != null) {
			// // vbPageContent.setDisable(false);
			//
			// lblErr.setVisible(false);
			//
			// // titledPaneMainContent.setText(newValue);
			// isFromFavList = true;
			// textSearchFliud.setText(newValue);
			// btnSave.setText("Save");
			// try {
			// disableStopPanel();
			// } catch (Exception e1) {
			// // TODO Auto-generated catch block
			// e1.printStackTrace();
			// }
			// // textSearchFliud.setText("");
			// for (Fluid fluidObj : allFluidslistDB) {
			// if (fluidObj.getFluidName().equalsIgnoreCase(newValue)) {
			// try {
			// getTotalVolume(fluidObj.getFluidId());
			// } catch (Exception e1) {
			// // TODO Auto-generated catch block
			// e1.printStackTrace();
			// }
			// }
			// }
			// }
			// });

			listViewSearchFluidsChangeListenerBoolean = new ChangeListener<String>() {

				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					if (newValue != null) {

						selectedSearchVal = newValue;
						listViewSearchFluids.setVisible(false);
						// listViewFavFluid.getSelectionModel().clearSelection();
						textSearchFliud.setText(newValue);
						btnSave.setText("Save");
						try {
							disableStopPanel();
						} catch (Exception e1) {
							LOGGER.error("## Exception occured:" + e1);
						}
						for (Fluid fluidObj : searchFluidListDB) {
							if (fluidObj.getFluidName().equalsIgnoreCase(newValue)) {
								try {
									getTotalVolume(fluidObj.getFluidName());
								} catch (Exception e1) {
									LOGGER.error("## Exception occured:" + e1);
								}
							}
						}
					}
				}
			};

			listViewSearchFluids.getSelectionModel().selectedItemProperty()
					.addListener(listViewSearchFluidsChangeListenerBoolean);

			btnCloseHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent event) {
					try {
						closePopup();
					} catch (Exception e) {
						LOGGER.error("## Exception occured:" + e);
					}
				}
			};
			btnClose.addEventHandler(MouseEvent.MOUSE_CLICKED, btnCloseHandler);

			btnSaveHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent event) {
					isValid = validate();
					if (isValid) {
						anchorLoader.setVisible(true);

						try {
							Fluidvalue fluid = new Fluidvalue();
							if (selectedFluidId != null) {
								fluid.setFluidValueId(selectedFluidId);
								fluid.setIsCompleted(true);
								fluid.setUpdatedBy(
										UserSession.getInstance().getUserWithrRolesForUserAuthentication().getUserID());
								fluid.setUpdatedTime(new Date());
							}
							fluid.setVolumeUnit("ml");
							fluid.setPatientId(patient.getPatientId());
							fluid.setCaseId(patientCase.getCaseId());

							// fluid.setFluidId(extractFluidId(textSearchFliud.getText(),
							// allFluidslistDB));
							fluid.setFluidName(textSearchFliud.getText());

							if (txtAreaComments.getText() != null && !txtAreaComments.getText().trim().matches(""))
								fluid.setComments(txtAreaComments.getText().trim());

							if (txtVolume.getText() != null) {
								fluid.setInitialVolume(Float.parseFloat(txtVolume.getText()));
							}
							if (toggleBtnStop.isSelected()) {
								Float volumeInfused = 0f;
								volumeInfused = Float.parseFloat(txtVolume.getText())
										- Float.parseFloat(txtVolumeLeft.getText());
								if (volumeInfused >= 0) {
									fluid.setVolume(volumeInfused);
								}

								if (datePickerFinishDate.getValue() != null) {
									SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

									LocalTime time = Main.getInstance().getUtility().getTime(hBoxFinishTimeControl);
									Date dateTime = simpleDateFormat
											.parse(datePickerFinishDate.getValue() + " " + time.toString());

									fluid.setFinishTime(dateTime);
								}

							}

							if (datePickerDate.getValue() != null) {

								SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

								LocalTime time = Main.getInstance().getUtility().getTime(hBoxTimeControl);
								Date dateTime = simpleDateFormat
										.parse(datePickerDate.getValue() + " " + time.toString());

								fluid.setStartTime(dateTime);

							}

							if (fluid.getFluidValueId() == null) {
								fluid.setCreatedBy(
										UserSession.getInstance().getUserWithrRolesForUserAuthentication().getUserID());
								fluid.setCreatedTime(new Date());
							}

							callAddFluidService(fluid);
						} catch (Exception e1) {
							LOGGER.error("## Exception occured:" + e1);
						}
					} else {
						lblErr.setVisible(true);
					}
				}
			};
			btnSave.addEventHandler(MouseEvent.MOUSE_CLICKED, btnSaveHandler);

			tableFluidsChangeListener = new ChangeListener<Fluidvalue>() {

				@Override
				public void changed(ObservableValue<? extends Fluidvalue> observable, Fluidvalue oldValue,
						Fluidvalue newValue) {
					if (newValue != null) {
						selectedTableRow = tableFluids.getSelectionModel().getSelectedItem();
						btnDelete.setDisable(false);
						btnEdit.setDisable(false);
					}
				}
			};

			tableFluids.getSelectionModel().selectedItemProperty().addListener(tableFluidsChangeListener);

			btnDeleteHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent event) {
					boolean result = Main.getInstance().getUtility().confirmDelete();
					if (result) {
						try {
							callDeleteFluidService(selectedTableRow);
						} catch (Exception e1) {
							LOGGER.error("## Exception occured:" + e1);
						}
					} else

					{
						try {
							reset();
						} catch (Exception e1) {
							LOGGER.error("## Exception occured:" + e1);
						}
					}
				}
			};
			btnDelete.addEventHandler(MouseEvent.MOUSE_CLICKED, btnDeleteHandler);

			btnEditHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent event) {
					try {
						callFluidUpdateService(selectedTableRow);
					} catch (Exception e1) {
						LOGGER.error("## Exception occured:" + e1);
					}
					disablePaneHistory.setVisible(true);
					btnDelete.setDisable(true);
					btnEdit.setDisable(true);
					anchorLoader.setVisible(false);
					// listViewFavFluid.getSelectionModel().clearSelection();
				}
			};
			btnEdit.addEventHandler(MouseEvent.MOUSE_CLICKED, btnEditHandler);

			btnCancelHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent event) {
					try {
						reset();
					} catch (Exception e) {
						LOGGER.error("## Exception occured:" + e);
					}
				}
			};
			btnCancel.addEventHandler(MouseEvent.MOUSE_CLICKED, btnCancelHandler);

			btnTimeHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent event) {
					LocalTime timeNow = Main.getInstance().getUtility().getLocalSystemTime();
					try {
						setTime(timeNow.toString());
					} catch (Exception e1) {
						LOGGER.error("## Exception occured:" + e1);
					}
					Date input = new Date();
					LocalDate date = input.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
					datePickerDate.setValue(date);
				}
			};
			btnTime.addEventHandler(MouseEvent.MOUSE_CLICKED, btnTimeHandler);

			btnFinishTimeHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent event) {
					LocalTime timeNow = Main.getInstance().getUtility().getLocalSystemTime();
					try {
						setStopTime(timeNow.toString());
					} catch (Exception e1) {
						LOGGER.error("## Exception occured:" + e1);
					}
					Date input = new Date();
					LocalDate date = input.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
					datePickerFinishDate.setValue(date);
				}
			};
			btnFinishTime.addEventHandler(MouseEvent.MOUSE_CLICKED, btnFinishTimeHandler);

			toggleBtnStopChangeListener = new ChangeListener<Boolean>() {
				@Override
				public void changed(ObservableValue<? extends Boolean> observable, Boolean oldPropertyValue,
						Boolean newPropertyValue) {
					if (newPropertyValue) {
						// enableStopPanel();
						vbVolumeLeft.setDisable(false);
						txtVolumeLeft.setDisable(false);
						vbFinishDate.setDisable(false);
						vbFinishTimeControl.setDisable(false);
						datePickerFinishDate.setDisable(false);
						try {
							resetStopTime();
						} catch (Exception e1) {
							LOGGER.error("## Exception occured:" + e1);
						}
						Date input = new Date();
						LocalDate date = input.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
						datePickerFinishDate.setValue(date);
						btnFinishTime.setDisable(false);

					} else {

						toggleBtnStop.setSelected(false);
						txtVolumeLeft.setDisable(true);
						vbVolumeLeft.setDisable(true);
						vbFinishDate.setDisable(true);
						vbFinishTimeControl.setDisable(true);
						datePickerFinishDate.setDisable(true);
						btnFinishTime.setDisable(true);

						// disableStopPanel();
					}
				}
			};
			toggleBtnStop.selectedProperty().addListener(toggleBtnStopChangeListener);

			Date input = new Date();
			LocalDate date = input.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			datePickerDate.setValue(date);

			try {
				callViewFluidService();
			} catch (Exception e2) {
				LOGGER.error("## Exception occured:" + e2);
			}
			try {
				resetTime();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				LOGGER.error("## Exception occured:" + e1);
			}
		} catch (Exception e) {
			LOGGER.error("## Exception occured:" + e);
			Main.getInstance().getUtility().showNotification("Error", "Error", "Something went wrong 18");
		}

	}

	/**
	 * This method is used to extract Fluid Id for selected Fluid Value from the
	 * Fluid list
	 *
	 * @param selectedFluidVal
	 *            String
	 * @param fluidObjList
	 *            Fluid
	 * @return
	 * @throws Exception
	 */
//	private int extractFluidId(String selectedFluidVal, List<Fluid> fluidObjList) throws Exception {
//		int fluidId = 0;
//
//		try {
//			for (Fluid obj : fluidObjList) {
//				if (obj != null) {
//					if (obj.getFluidName().equalsIgnoreCase(selectedFluidVal)) {
//						fluidId = obj.getFluidId();
//						break;
//					}
//				}
//			}
//		} catch (Exception e) {
//			LOGGER.error("## Exception occured:" + e);
//		}
//		return fluidId;
//	}

	// /**
	// * This method call GetAllFluidsService which fetches all fluid values
	// from
	// * database
	// *
	// */
	// public void callGetAllFluidsService() throws Exception{
	// LOGGER.debug("Logger Name: " + LOGGER.getName());
	// try {
	// getAllFluidsService = GetAllFluidsService.getInstance();
	// getAllFluidsService.restart();
	//
	// getAllFluidsService.setOnSucceeded(e -> {
	// allFluidslist = FXCollections.observableArrayList();
	// System.out.println("getAllFluidsService service was success----->>>");
	// allFluidslistDB = getAllFluidsService.getAllFluidslist();
	//
	// if (allFluidslistDB != null) {
	// for (Fluid obj : allFluidslistDB) {
	// if (obj != null) {
	// allFluidslist.add(obj.getFluidName());
	// }
	// }
	// listViewFavFluid.setItems(allFluidslist);
	//
	// }
	// });
	//
	// getAllFluidsService.setOnFailed(e -> {
	// System.out.println("getAllFluidsService service was failed----->>>");
	// Main.getInstance().getUtility().showNotification("Error", "Error",
	// getAllFluidsService.getException().getMessage());
	//
	// });
	// }
	// catch (Exception e)
	// {
	// LOGGER.error("## Exception occured:" + e.getMessage());
	// Main.getInstance().getUtility().showNotification("Error",
	// "Error","Something went wrong 2");
	// }
	//
	// }

	/**
	 * This method is used to fetch total Fluid volume given for the selected
	 * fluid
	 *
	 * @param fluidName
	 *            String
	 * @throws Exception
	 */
	private void getTotalVolume(String fluidName) throws Exception {

		try {
			GetTotalFluidVolume getTotalVolumeService = GetTotalFluidVolume.getInstance(patient.getPatientId(),
					patientCase.getCaseId(), fluidName);
			getTotalVolumeService.restart();

			fluidVolume = new Volume();

			getTotalVolumeServiceSuccessHandler = new EventHandler<WorkerStateEvent>() {

				@Override
				public void handle(WorkerStateEvent event) {
					fluidVolume = getTotalVolumeService.getTotalVolume();
					if (fluidVolume != null) {
						totalVolume = fluidVolume.getValue();
						lblTotalVolume.setText(totalVolume + " ml ");
					}
					getTotalVolumeService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
							getTotalVolumeServiceSuccessHandler);
					getTotalVolumeService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
							getTotalVolumeServiceFailedHandler);
				}

			};

			getTotalVolumeService.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
					getTotalVolumeServiceSuccessHandler);

			getTotalVolumeServiceFailedHandler = new EventHandler<WorkerStateEvent>() {

				@Override
				public void handle(WorkerStateEvent event) {
					Main.getInstance().getUtility().showNotification("Error", "Error",
							getTotalVolumeService.getException().getMessage());
					getTotalVolumeService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
							getTotalVolumeServiceSuccessHandler);
					getTotalVolumeService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
							getTotalVolumeServiceFailedHandler);
				}

			};
		} catch (Exception e) {
			LOGGER.error("## Exception occured:" + e);
		}

	}

	/**
	 * This method is used call SearchFluidService which searches for fluid with
	 * matching searchCriteria
	 *
	 * @param searchCriteria
	 * @throws Exception
	 */
	private void callSearchFluidService(String searchCriteria) throws Exception {

		try {
			SearchFluidService searchFluidService = SearchFluidService.getInstance(searchCriteria,
					patient.getPatientId(), patientCase.getCaseId());
			searchFluidService.restart();

			searchFluidServiceSuccessHandler = new EventHandler<WorkerStateEvent>() {

				@Override
				public void handle(WorkerStateEvent event) {
					List<String> searchFluidList = new ArrayList<>();
					searchFluidListDB = searchFluidService.getSearchFluidList();

					if (searchFluidListDB != null) {
						for (Fluid obj : searchFluidListDB) {
							if (obj != null) {
								searchFluidList.add(obj.getFluidName());
							}
						}
						listViewSearchFluids.getItems().clear();
						listViewSearchFluids.getItems().addAll(searchFluidList);
						if (searchFluidListDB.size() != 0) {
							listViewSearchFluids.setVisible(true);
						} else {
							listViewSearchFluids.setVisible(false);
						}

					}
					searchFluidService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
							searchFluidServiceSuccessHandler);
					searchFluidService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
							searchFluidServiceFailedHandler);
				}

			};

			searchFluidService.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
					searchFluidServiceSuccessHandler);

			searchFluidServiceFailedHandler = new EventHandler<WorkerStateEvent>() {

				@Override
				public void handle(WorkerStateEvent event) {
					Main.getInstance().getUtility().showNotification("Error", "Error",
							searchFluidService.getException().getMessage());
					searchFluidService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
							searchFluidServiceSuccessHandler);
					searchFluidService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
							searchFluidServiceFailedHandler);
				}

			};

			searchFluidService.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED, searchFluidServiceFailedHandler);
		} catch (Exception e) {
			LOGGER.error("## Exception occured:" + e);
		}
	}

	/**
	 * This method is used to call AddFluidService which saves the provided
	 * fluid details in database
	 *
	 * @param fluidValue
	 * @throws Exception
	 */
	private void callAddFluidService(Fluidvalue fluidValue) throws Exception {
		try {

			AddFluidService addFluidService = AddFluidService.getInstance(fluidValue);
			addFluidService.restart();

			addFluidServiceSuccessHandler = new EventHandler<WorkerStateEvent>() {

				@Override
				public void handle(WorkerStateEvent event) {
					Map<String, Fluidvalue> fluidAddMapDB = new HashMap<String, Fluidvalue>();
					fluidAddMapDB = addFluidService.getReturnMap();
					if (fluidAddMapDB != null) {

						for (Map.Entry<String, Fluidvalue> entry : fluidAddMapDB.entrySet()) {

							if (entry.getKey().equalsIgnoreCase("Success")) {
								if (selectedFluidId != null) {
									Main.getInstance().getUtility().showNotification("Information", "Success",
											"Fluid updated successfully!");

								} else {
									Main.getInstance().getUtility().showNotification("Information", "Success",
											"Fluid saved successfully!");

								}

								// closePopup();

							}

						}
						try {
							reset();
						} catch (Exception e1) {
							LOGGER.error("## Exception occured:" + e1);
						}
						try {
							callViewFluidService();
						} catch (Exception e1) {
							LOGGER.error("## Exception occured:" + e1);
						}
					}
					addFluidService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
							addFluidServiceSuccessHandler);
					addFluidService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
							addFluidServiceFailedHandler);

				}

			};

			addFluidService.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, addFluidServiceSuccessHandler);

			addFluidServiceFailedHandler = new EventHandler<WorkerStateEvent>() {

				@Override
				public void handle(WorkerStateEvent event) {
					Main.getInstance().getUtility().showNotification("Error", "Error",
							addFluidService.getException().getMessage());
					anchorLoader.setVisible(false);
					addFluidService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
							addFluidServiceSuccessHandler);
					addFluidService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
							addFluidServiceFailedHandler);
				}

			};

			addFluidService.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED, addFluidServiceFailedHandler);

		} catch (Exception e) {
			LOGGER.error("## Exception occured:" + e);
		}
	}

	/**
	 * This method is used to Remove all Listeners
	 */
	private void removeEventHandlers() {
		try {
			textSearchFliud.textProperty().removeListener(textSearchFliudChangeListener);
			textSearchFliud.focusedProperty().removeListener(textSearchFliudChangeListenerBoolean);
			listViewSearchFluids.getSelectionModel().selectedItemProperty()
					.removeListener(listViewSearchFluidsChangeListenerBoolean);
			btnClose.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnCloseHandler);
			btnSave.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnSaveHandler);
			btnDelete.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnDeleteHandler);
			btnEdit.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnEditHandler);
			btnCancel.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnCancelHandler);
			btnTime.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnTimeHandler);
			btnFinishTime.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnFinishTimeHandler);
			toggleBtnStop.selectedProperty().removeListener(toggleBtnStopChangeListener);
		} catch (Exception e) {
			LOGGER.error("## Exception occured:" + e);
		}

	}

	/**
	 * This method is used to close Fluids log window
	 *
	 * @throws Exception
	 */
	private void closePopup() throws Exception {
		try {
			removeEventHandlers();
			Main.getInstance().getStageManager().closeSecondaryStage();
		} catch (Exception e) {
			LOGGER.error("## Exception occured:" + e);
		}

	}

	/**
	 * This method is used to validate Add/Edit Fluid form.Returns true when
	 * form is valid and returns false when form is not valid
	 *
	 * @return
	 */
	private boolean validate() {
		try {

			if (textSearchFliud.getText() == null || textSearchFliud.getText().equals("")) {
				lblErr.setText("*Please fill all the mandatory fields");
				return false;
			}
			if (Validations.isDigit(textSearchFliud.getText())) {
				lblErr.setText("*Please enter a valid Fluid Name");
				return false;
			}
			if (txtVolume.getText().equals("") || datePickerDate.getValue() == null) {

				lblErr.setText("*Please fill all the mandatory fields");
				return false;
			}

			if (!Validations.maxLength(textSearchFliud.getText(), 999)) {
				lblErr.setText("*Please enter a fluid name less than 1000");
				return false;
			}

			/*
			 * if(!Validations.isDigit(txtVolume.getText())) { lblErr.setText(
			 * "*Volume can only have digits"); return false; }
			 */
			if (!Validations.isDigit(txtVolume.getText())) {
				lblErr.setText("*Please enter a valid volume value");
				return false;
			}
			if (!Validations.decimalsUpto2places(txtVolume.getText())) {
				lblErr.setText("*Please enter volume upto 4 digits and upto 2 decimal only");
				return false;
			}

			if (!Validations.nonZeroBigDecimal(txtVolume.getText())) {
				lblErr.setText("*Please enter volume value more than 0");
				return false;
			}
			if (txtAreaComments.getText() != null && !txtAreaComments.getText().trim().isEmpty()
					&& !Validations.maxLength(txtAreaComments.getText(), 2000)) {
				lblErr.setText("*Please enter Comments less than 2000 characters");
				return false;
			}

			/// for checking whether or not the date entered is in future
			try {
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				Date dateEntered = simpleDateFormat.parse(
						datePickerDate.getValue() + " " + Main.getInstance().getUtility().getTime(hBoxTimeControl));
				if (Validations.futureDateCheck(dateEntered)) {
					lblErr.setText("*Future Start Date Time is not allowed.");
					return false;
				}
			} catch (Exception e) {
				LOGGER.error("## Exception occured:" + e);
			}
			if (toggleBtnStop.isSelected()) {
				if ((txtVolumeLeft.getText() == null || txtVolumeLeft.getText().trim().isEmpty()
						|| datePickerFinishDate.getValue() == null)) {
					lblErr.setText("*Please fill all the mandatory fields");
					return false;
				}
				try {
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
					Date dateEntered = simpleDateFormat.parse(datePickerFinishDate.getValue() + " "
							+ Main.getInstance().getUtility().getTime(hBoxFinishTimeControl));
					if (Validations.futureDateCheck(dateEntered)) {
						lblErr.setText("*Future Finish Date Time is not allowed.");
						return false;
					}
				} catch (Exception e) {
					LOGGER.error("## Exception occured:" + e);
				}
				if (datePickerDate.getValue() != null && datePickerFinishDate.getValue() != null) {
					try {
						SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

						Date finishTime = simpleDateFormat.parse(datePickerFinishDate.getValue() + " "
								+ Main.getInstance().getUtility().getTime(hBoxFinishTimeControl));
						Date startTime = simpleDateFormat.parse(datePickerDate.getValue() + " "
								+ Main.getInstance().getUtility().getTime(hBoxTimeControl));

						Boolean result = Validations.startEndTimeCheck(startTime, finishTime);
						if (!result) {
							lblErr.setText("*Finish Date Time should be after Start Date Time");
							return false;
						}
					} catch (Exception e) {
						LOGGER.error("## Exception occured:" + e);
					}
				}

				Float volumeInfused = 0f;
				volumeInfused = Float.parseFloat(txtVolume.getText()) - Float.parseFloat(txtVolumeLeft.getText());
				if (volumeInfused < 0) {

					lblErr.setText("* Volume left should not be greater than Intial Volume ");
					return false;
				}

			}

			return true;

		} catch (Exception e) {
			LOGGER.error("## Exception occured:" + e);
			return false;
		}
	}

	/**
	 * This method is used to Disable the Stop fluid panel at the time of start
	 * fluid
	 *
	 * @throws Exception
	 */
	private void disableStopPanel() throws Exception {

		try {
			toggleBtnStop.setSelected(false);
			txtVolumeLeft.setDisable(true);
			vbVolumeLeft.setDisable(true);
			vbFinishDate.setDisable(true);
			vbFinishTimeControl.setDisable(true);
			datePickerFinishDate.setDisable(true);
			btnFinishTime.setDisable(true);
		} catch (Exception e) {
			LOGGER.error("## Exception occured:" + e);
		}
	}

	/**
	 * This method is used to enable the Stop fluid panel at the time of stop
	 * fluid
	 *
	 * @throws Exception
	 */
//	private void enableStopPanel() throws Exception {
//
//		try {
//			vbVolumeLeft.setDisable(false);
//			txtVolumeLeft.setDisable(false);
//			vbFinishDate.setDisable(false);
//			vbFinishTimeControl.setDisable(false);
//			datePickerFinishDate.setDisable(false);
//			resetStopTime();
//			Date input = new Date();
//			LocalDate date = input.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//			datePickerFinishDate.setValue(date);
//			btnFinishTime.setDisable(false);
//		} catch (Exception e) {
//			LOGGER.error("## Exception occured:" + e);
//		}
//
//	}

	/**
	 * This method is used to reset Add/Edit fluid form
	 *
	 * @throws Exception
	 */
	private void reset() throws Exception {

		try {
			textSearchFliud.setText("");
			txtVolume.setText("");
			lblTotalVolume.setText("0 ml");
			resetTime();
			resetStopTime();
			txtAreaComments.setText("");
			lblErr.setVisible(false);
			Date input = new Date();
			LocalDate date = input.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			datePickerDate.setValue(date);
			titledPaneRecords.setDisable(false);
			btnCancel.setVisible(false);
			textSearchFliud.setDisable(false);
			tableFluids.getSelectionModel().clearSelection();
			selectedFluidId = null;
			btnSave.setText("Save");
			disablePaneHistory.setVisible(false);
			btnDelete.setDisable(true);
			btnEdit.setDisable(true);
			toggleBtnStop.setDisable(false);
			toggleBtnStop.setSelected(false);
			datePickerFinishDate.setDisable(true);
			txtVolumeLeft.setDisable(true);
			txtVolumeLeft.setText("");
			datePickerFinishDate.setValue(null);
			btnFinishTime.setDisable(true);
			anchorLoader.setVisible(true);
		} catch (Exception e) {
			LOGGER.error("## Exception occured:" + e);
		}

	}

	/**
	 *
	 * This method is used to call ViewFluidService and get the list of fluids
	 * to be shown in the grid
	 *
	 * @throws Exception
	 */
	private void callViewFluidService() throws Exception {

		try {
			ViewFluidService viewFluidService = ViewFluidService.getInstance(patient.getPatientId(),
					patientCase.getCaseId());
			viewFluidService.restart();

			viewFluidServiceSuccessHandler = new EventHandler<WorkerStateEvent>() {

				@Override
				public void handle(WorkerStateEvent event) {
					Map<String, List<Fluidvalue>> fluidViewMapDB = viewFluidService.getFluidMapDB();
					if (fluidViewMapDB != null) {
						for (Map.Entry<String, List<Fluidvalue>> entry : fluidViewMapDB.entrySet()) {
							if (entry.getKey().equalsIgnoreCase("List of fluids")) {
								tableFluids.getItems().clear();
								tableFluids.getItems().addAll(entry.getValue());
								fluidList = entry.getValue();
								btnDelete.setDisable(true);
								btnEdit.setDisable(true);
							}
						}
						vbTotalfluids.getChildren().clear();

						List<String> fluidNameList = new ArrayList<String>();
						for (Fluidvalue fluidObj : fluidList) {
							Boolean isFound = false;
							if (fluidNameList.size() > 0) {
								for (int i = 0; i < fluidNameList.size(); i++) {
									if (fluidObj.getFluidName().equalsIgnoreCase(fluidNameList.get(i))) {
										isFound = true;
									}

								}
								if (!isFound) {
									fluidNameList.add(fluidObj.getFluidName());
								}

							} else {

								fluidNameList.add(fluidObj.getFluidName());
							}

						}

						if (fluidNameList.size() > 0) {
							for (int i = 0; i < fluidNameList.size(); i++) {

								float totalvolume = 0;
								for (Fluidvalue fluidObj : fluidList) {
									if (fluidObj.getFluidName().equalsIgnoreCase(fluidNameList.get(i))) {
										if (fluidObj.getVolume() != null) {
											totalvolume = totalvolume + fluidObj.getVolume();
										}
									}

								}

								labelFluidName = new Label();
								labelTotalFluid = new Label();
								labelFluidName.setPrefWidth(180);
								labelTotalFluid.setPrefWidth(110);
								labelFluidName.setText(fluidNameList.get(i));
								labelTotalFluid.setText(totalvolume + "");
								HBox fluidHbox = new HBox();

								fluidHbox.setSpacing(50);
								fluidHbox.getChildren().add(labelFluidName);
								fluidHbox.getChildren().add(labelTotalFluid);
								fluidHbox.setStyle("-fx-border-width: 0.25;" + " -fx-padding: 0 0 0 10;");
								fluidHbox.setStyle("-fx-border-style: solid inside;" + "-fx-border-color: grey;");
								fluidHbox.setStyle("-fx-min-height: 30.0px;");

								vbTotalfluids.getChildren().add(fluidHbox);

							}
						}

					}
					viewFluidService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
							viewFluidServiceSuccessHandler);
					viewFluidService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
							viewFluidServiceFailedHandler);
				}

			};

			viewFluidService.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, viewFluidServiceSuccessHandler);

			viewFluidServiceFailedHandler = new EventHandler<WorkerStateEvent>() {

				@Override
				public void handle(WorkerStateEvent event) {
					Main.getInstance().getUtility().showNotification("Error", "Error",
							viewFluidService.getException().getMessage());
					viewFluidService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
							viewFluidServiceSuccessHandler);
					viewFluidService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
							viewFluidServiceFailedHandler);
				}

			};

			viewFluidService.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED, viewFluidServiceFailedHandler);
		} catch (Exception e) {
			LOGGER.error("## Exception occured:" + e);
		}

	}

	/**
	 * This method is used to call DeleteFluidService and delete selected fluid
	 * from database
	 *
	 * @param fluidSelected
	 * @throws Exception
	 */
	private void callDeleteFluidService(Fluidvalue fluidSelected) throws Exception {

		try {
			DeleteFluidService deleteFluidService = DeleteFluidService.getInstance(fluidSelected.getFluidValueId());
			deleteFluidService.restart();

			deleteFluidServiceSuccessHandler = new EventHandler<WorkerStateEvent>() {

				@Override
				public void handle(WorkerStateEvent event) {
					String result = deleteFluidService.getResult();
					if (result.equalsIgnoreCase("fluid Deleted")) {
						Main.getInstance().getUtility().showNotification("Information", "Success",
								"Fluid deleted successfully!");
						try {
							callViewFluidService();
						} catch (Exception e1) {
							LOGGER.error("## Exception occured:" + e1);
						}
					}
					try {
						getTotalVolume(fluidSelected.getFluidName());
					} catch (Exception e1) {
						LOGGER.error("## Exception occured:" + e1);
					}

					List<String> fluidNameList = new ArrayList<String>();
					for (Fluidvalue fluidObj : fluidList) {
						Boolean isFound = false;

						if (fluidNameList.size() > 0) {
							for (int i = 0; i < fluidNameList.size(); i++) {
								if (fluidObj.getFluidName().equalsIgnoreCase(fluidNameList.get(i))) {
									isFound = true;
								}

							}
							if (!isFound) {
								fluidNameList.add(fluidObj.getFluidName());
							}

						} else {

							fluidNameList.add(fluidObj.getFluidName());
						}

					}

					if (fluidNameList.size() > 0) {
						for (int i = 0; i < fluidNameList.size(); i++) {

							float totalvolume = 0;
							for (Fluidvalue fluidObj : fluidList) {
								if (fluidObj.getFluidName().equalsIgnoreCase(fluidNameList.get(i))) {
									totalvolume = totalvolume + fluidObj.getVolume();
								}

							}
						}
					}
					deleteFluidService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
							deleteFluidServiceSuccessHandler);
					deleteFluidService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
							deleteFluidServiceFailedHandler);
				}

			};

			deleteFluidService.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
					deleteFluidServiceSuccessHandler);

			deleteFluidServiceFailedHandler = new EventHandler<WorkerStateEvent>() {

				@Override
				public void handle(WorkerStateEvent event) {
					Main.getInstance().getUtility().showNotification("Error", "Error",
							deleteFluidService.getException().getMessage());
					deleteFluidService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
							deleteFluidServiceSuccessHandler);
					deleteFluidService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
							deleteFluidServiceFailedHandler);
				}

			};

			deleteFluidService.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED, deleteFluidServiceFailedHandler);

		} catch (Exception e) {
			LOGGER.error("## Exception occured:" + e);
		}
	}

	/**
	 * This method is used to call FluidUpdateService and get the fluid details
	 * from database and fill the form
	 *
	 * @param fluidValueId
	 * @throws Exception
	 */
	private void callFluidUpdateService(Fluidvalue fluidValue) throws Exception {
		try {
			Fluidvalue selectedFluid = new Fluidvalue();
			selectedFluid = fluidValue;
			if (selectedFluid != null) {
				isFromFavList = true;
				textSearchFliud.setDisable(true);
				// listViewFavFluid.setDisable(true);
				// disablePaneFluidList.setVisible(true);
				btnCancel.setVisible(true);
				btnSave.setText("Update");
				selectedFluidId = selectedFluid.getFluidValueId();
				textSearchFliud.setText(selectedFluid.getFluidName());
				txtVolume.setText(selectedFluid.getInitialVolume().toString());
				float volumeLeft = 0f;
				if (selectedFluid.getInitialVolume() != null && selectedFluid.getVolume() != null) {
					volumeLeft = Float.valueOf(selectedFluid.getInitialVolume().toString())
							- Float.valueOf(selectedFluid.getVolume().toString());
				}
				if (volumeLeft != 0) {
					txtVolumeLeft.setText(volumeLeft + "");
				}

				txtAreaComments.setText(selectedFluid.getComments());

				if (selectedFluid.getStartTime() != null) {

					String dateTimeArr[] = selectedFluid.getStartTime().toString().split(" ");
					String timearray[] = dateTimeArr[1].split("\\.");
					LocalDate localDate = LocalDate.parse(dateTimeArr[0]);
					datePickerDate.setValue(localDate);
					try {
						setTime(timearray[0]);
					} catch (Exception e1) {
						LOGGER.error("## Exception occured:" + e1);
					}

				}
				if (selectedFluid.getFinishTime() != null) {
					toggleBtnStop.setSelected(true);
					toggleBtnStop.setDisable(true);
					btnFinishTime.setDisable(false);
					datePickerFinishDate.setDisable(false);
					txtVolumeLeft.setDisable(false);
					String dateTimeArr[] = selectedFluid.getFinishTime().toString().split(" ");
					String timearray[] = dateTimeArr[1].split("\\.");
					LocalDate localDate = LocalDate.parse(dateTimeArr[0]);
					datePickerFinishDate.setValue(localDate);
					try {
						setStopTime(timearray[0]);
					} catch (Exception e1) {
						LOGGER.error("## Exception occured:" + e1);
					}

				}

				try {
					getTotalVolume(selectedFluid.getFluidName());
				} catch (Exception e1) {
					LOGGER.error("## Exception occured:" + e1);
				}

			} else {
				Date input = new Date();
				LocalDate date = input.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				datePickerDate.setValue(date);

			}

		} catch (Exception e) {
			LOGGER.error("## Exception occured:" + e);
		}
	}

	/**
	 *
	 * This method is used to set time in Start time control
	 *
	 * @param time:
	 *            local time in string
	 * @param time
	 * @throws Exception
	 */
	private void setTime(String time) throws Exception {

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
	 *
	 * This method is used to set time in Stop time control
	 *
	 * @param time:
	 *            local time in string
	 * @param time
	 * @throws Exception
	 */
	private void setStopTime(String time) throws Exception {

		try {
			String[] hourMinutearray = time.split(":");
			int hour = 0;
			if (Integer.valueOf(hourMinutearray[0].toString()) < 12) {
				meridiemFinishSpinner.getValueFactory().setValue("AM");
				hour = Integer.valueOf(hourMinutearray[0].toString());
				if (hour == 0) {
					hour = 12;
				}
			} else {
				meridiemFinishSpinner.getValueFactory().setValue("PM");
				hour = Integer.valueOf(hourMinutearray[0].toString());
				if (hour != 12) {
					hour = hour - 12;
				}

			}
			if (hour < 10) {
				hourFinishSpinner.getValueFactory().setValue("0" + hour);
			} else {
				hourFinishSpinner.getValueFactory().setValue("" + hour);
			}

			minuteFinishSpinner.getValueFactory().setValue(hourMinutearray[1]);
		} catch (Exception e) {
			LOGGER.error("## Exception occured:" + e);
		}

	}

	/**
	 * This method is used to reset Stop time control
	 *
	 * @throws Exception
	 */
	private void resetStopTime() throws Exception {

		try {
			LocalTime timeNow = Main.getInstance().getUtility().getLocalSystemTime();

			setStopTime(timeNow.toString());
		} catch (Exception e) {
			LOGGER.error("## Exception occured:" + e);
		}
	}

	/**
	 * This method is used to reset Start time control
	 *
	 * @throws Exception
	 */
	private void resetTime() throws Exception {

		try {
			LocalTime timeNow = Main.getInstance().getUtility().getLocalSystemTime();
			setTime(timeNow.toString());

		} catch (Exception e) {
			LOGGER.error("## Exception occured:" + e);
		}
	}
}
