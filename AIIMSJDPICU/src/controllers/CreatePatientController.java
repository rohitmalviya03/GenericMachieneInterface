/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package controllers;

import java.math.BigDecimal;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.cdac.common.GeneratedEntities.Address;
import com.cdac.common.GeneratedEntities.Patient;
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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import mediatorPattern.ControllerMediator;
import model.EditCaseFlagSession;
import model.EditPatientSession;
import model.PatientSession;
import services.CreatePatientService;
import services.GetPatientDetailsService;
import utility.ReadXmlFile;

/**
 * This controller is used to Create a new patient or Update an existing Patient
 *
 * @author Sudeep_Sahoo
 *
 */
public class CreatePatientController implements Initializable {

	@FXML
	private TextField txtCRNum;

	@FXML
	private TextField txtFirstName;

	@FXML
	private TextField txtLastName;

	@FXML
	private ChoiceBox<String> selectGender;

	@FXML
	private DatePicker dobPicker;

	@FXML
	private TextField txtAge;

	@FXML
	private ChoiceBox<String> selectUnit;

	@FXML
	private TextField txtWeight;

	@FXML
	private TextField txtHeight;

	@FXML
	private Label lblErrorMsg;

	@FXML
	private TextField txtBMI;

	@FXML
	private TextField txtBSA;

	@FXML
	private TextField txtMobile;

	@FXML
	private TextField txtGuardian;

	@FXML
	private TextField txtPhone;

	@FXML
	private TextField txtEmergencyContact;

	@FXML
	private TextField txtEmail;

	@FXML
	private ChoiceBox<String> selectCountry;

	@FXML
	private ChoiceBox<String> selectState;

	@FXML
	private TextField txtHouseNo;

	@FXML
	private TextField txtStreet;

	@FXML
	private TextField txtLocation;

	@FXML
	private TextField txtDistrict;

	@FXML
	private TextField txtCity;

	@FXML
	private ChoiceBox<String> selectArea;

	@FXML
	private TextField txtPincode;

	@FXML
	private Button btnProceed;

	@FXML
	private TabPane tabPane;

	@FXML
	private Tab tabPatientDemography;

	@FXML
	private Tab tabContactDetails;

	@FXML
	private Tab tabAddressDetails;

	@FXML
	private Button btnBack;

	@FXML
	private Button btnClose;

	@FXML
	private Button btnReset;

	@FXML
	private TextField txtState;

	@FXML
	private ImageView lodingImgView;

	@FXML
	private Label lblHeader;

	@FXML
	private RadioButton radioDob;

	@FXML
	private ToggleGroup ageToogleGroup;

	@FXML
	private RadioButton radioAge;

	@FXML
	private VBox vbDatePicker;

	@FXML
	private HBox hbPatientAge;

	@FXML
	private ChoiceBox<String> choiceBloodGroup;

	@FXML
	private AnchorPane disablePaneMainContent;

	private static final Logger LOGGER = Logger.getLogger(CreatePatientController.class.getName());

	private Patient editPatientDetails;// = new Patient();
	private int selectedAddressId;
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	boolean selectedIndia = true;

	// Event Handlers
	private EventHandler<WorkerStateEvent> createPatientServiceFailedHandler;
	private EventHandler<WorkerStateEvent> createPatientServiceSuccessHandler;
	private EventHandler<WorkerStateEvent> getPatientDetailsServiceFailedHandler;
	private EventHandler<WorkerStateEvent> getPatientDetailsServiceSuccessHandler;
	private EventHandler<MouseEvent> btnResetHandler;
	private EventHandler<MouseEvent> btnCloseHandler;
	private EventHandler<MouseEvent> btnBackHandler;
	private EventHandler<MouseEvent> btnProceedHandler;
	private ChangeListener<Toggle> ageToogleGroupListener;
	private ChangeListener<String> txtCRNumChangeListener;
	private ChangeListener<String> txtMobileChangeListener;
	private ChangeListener<String> txtFirstnameChangeListener;
	private ChangeListener<String> txtAgeChangeListener;
	private ChangeListener<String> txtLastNameChangeListener;
	private ChangeListener<String> txtWeightListener;
	private ChangeListener<String> txtHeightListener;
	private ChangeListener<String> txtGuardianChangeListener;
	private ChangeListener<String> txtEmergencyContactListener;
	private ChangeListener<String> txtEmailChangeListener;
	private ChangeListener<Boolean> txtWeightChangeListener;
	private ChangeListener<Boolean> txtHeightChangeListener;
	private ChangeListener<String> selectCountryChangeListener;

	/**
	 * This method initializes the all Event handlers and Default values on page
	 * load
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		try {
			radioAge.setUserData("Age");
			radioDob.setUserData("DOB");
			radioAge.setToggleGroup(ageToogleGroup);
			radioDob.setToggleGroup(ageToogleGroup);

			// ---populating choiceBoxes
			List<String> genderList = new ArrayList<String>();
			genderList.add("Select");
			genderList.add("Male");
			genderList.add("Female");
			genderList.add("Ambiguous");
			genderList.add("Transgender");
			selectGender.getItems().clear();
			selectGender.getItems().addAll(genderList);
			selectGender.getSelectionModel().select(0);

			List<String> unitList = new ArrayList<String>();
			unitList.add("Select");
			unitList.add("Days");
			unitList.add("Weeks");
			unitList.add("Months");
			unitList.add("Years");

			selectUnit.getItems().clear();
			selectUnit.getItems().addAll(unitList);
			selectUnit.getSelectionModel().select(0);

			List<String> bgList = new ArrayList<String>();
			bgList.add("Select");
			bgList.add("O+");
			bgList.add("O-");
			bgList.add("A+");
			bgList.add("A-");
			bgList.add("B+");
			bgList.add("B-");
			bgList.add("AB+");
			bgList.add("AB-");
			bgList.add("H/H");

			choiceBloodGroup.getItems().clear();
			choiceBloodGroup.getItems().addAll(bgList);
			choiceBloodGroup.getSelectionModel().select(0);

			selectCountry.getItems().clear();
			selectCountry.getItems().addAll(ReadXmlFile.readCountriesFile());
			selectCountry.getSelectionModel().select("India");

			selectState.getItems().clear();
			selectState.getItems().addAll(ReadXmlFile.readStatesFile());
			selectState.getSelectionModel().select("Punjab");

			List<String> areaList = new ArrayList<String>();
			areaList.add("Select");
			areaList.add("Urban");
			areaList.add("Semi-Urban");
			areaList.add("Rural");

			selectArea.getItems().clear();
			selectArea.getItems().addAll(areaList);
			selectArea.getSelectionModel().select(0);

			txtCity.setText("Chandigarh");
			txtMobile.setText("NR");
			txtGuardian.setText("NR");

			// ---disable all tabs except first
			tabContactDetails.setDisable(true);
			tabAddressDetails.setDisable(true);

			ageToogleGroupListener = new ChangeListener<Toggle>() {

				@Override
				public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {

					if (newValue != null) {

						if (newValue.getUserData().toString().equalsIgnoreCase("DOB")) {
							vbDatePicker.setVisible(true);
							hbPatientAge.setVisible(false);
						} else {
							vbDatePicker.setVisible(false);
							hbPatientAge.setVisible(true);
							dobPicker.setValue(null);
						}
					}

				}

			};
			ageToogleGroup.selectedToggleProperty().addListener(ageToogleGroupListener);

			txtMobileChangeListener = new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					if (txtCRNum.getText().length() == 12) {
						if (txtMobile.getText().length() > 10) {
							String s = txtMobile.getText().substring(0, 10);
							txtMobile.setText(s);
						}
					}
				}
			};
			txtMobile.textProperty().addListener(txtMobileChangeListener);

			txtFirstnameChangeListener = new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

					if (txtFirstName.getText().length() > 50) {
						String s = txtFirstName.getText().substring(0, 50);
						txtFirstName.setText(s);
					}

				}
			};
			txtFirstName.textProperty().addListener(txtFirstnameChangeListener);
			txtLastNameChangeListener = new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

					if (txtLastName.getText().length() > 50) {
						String s = txtLastName.getText().substring(0, 50);
						txtLastName.setText(s);
					}

				}
			};
			txtLastName.textProperty().addListener(txtLastNameChangeListener);

			txtWeightListener = new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

					if (txtWeight.getText().length() > 6) {
						String s = txtWeight.getText().substring(0, 6);
						txtWeight.setText(s);
					}

				}
			};
			txtWeight.textProperty().addListener(txtWeightListener);

			txtHeightListener = new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

					if (txtHeight.getText().length() > 6) {
						String s = txtHeight.getText().substring(0, 6);
						txtHeight.setText(s);
					}

				}
			};
			txtHeight.textProperty().addListener(txtHeightListener);

			txtAgeChangeListener = new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

					if (txtAge.getText().length() > 3) {
						String s = txtAge.getText().substring(0, 3);
						txtAge.setText(s);
					}

				}
			};
			txtAge.textProperty().addListener(txtAgeChangeListener);

			txtCRNumChangeListener = new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					if (txtCRNum.getText().length() > 12) {
						String s = txtCRNum.getText().substring(0, 12);
						txtCRNum.setText(s);
					}
				}
			};
			txtCRNum.textProperty().addListener(txtCRNumChangeListener);

			txtEmailChangeListener = new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

					if (txtEmail.getText().length() > 50) {
						String s = txtEmail.getText().substring(0, 50);
						txtEmail.setText(s);
					}

				}
			};
			txtEmail.textProperty().addListener(txtEmailChangeListener);

			txtGuardianChangeListener = new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

					if (txtGuardian.getText().length() > 50) {
						String s = txtGuardian.getText().substring(0, 50);
						txtGuardian.setText(s);
					}

				}
			};
			txtGuardian.textProperty().addListener(txtGuardianChangeListener);

			txtEmergencyContactListener = new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

					if (txtEmergencyContact.getText().length() > 50) {
						String s = txtEmergencyContact.getText().substring(0, 50);
						txtEmergencyContact.setText(s);
					}

				}
			};
			txtEmergencyContact.textProperty().addListener(txtEmergencyContactListener);

			// ---On selecting India as a country, show state dropdown

			selectCountryChangeListener = new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					if (newValue != null) {
						if (newValue.equalsIgnoreCase("India")) {
							selectState.setVisible(true);
							selectedIndia = true;
							txtState.setVisible(false);
						} else {
							selectedIndia = false;
							selectState.setVisible(false);
							txtState.setVisible(true);
						}
					}
				}
			};

			selectCountry.getSelectionModel().selectedItemProperty().addListener(selectCountryChangeListener);

			// ---BMI and BSA auto-population

			txtWeightChangeListener = new ChangeListener<Boolean>() {
				@Override
				public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue,
						Boolean newPropertyValue) {
					if (!newPropertyValue) {
						assignBMInBSA();

					}
				}
			};

			txtWeight.focusedProperty().addListener(txtWeightChangeListener);

			txtHeightChangeListener = new ChangeListener<Boolean>() {
				@Override
				public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue,
						Boolean newPropertyValue) {
					if (!newPropertyValue) {

						assignBMInBSA();

					}
				}
			};

			txtHeight.focusedProperty().addListener(txtHeightChangeListener);

			btnCloseHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent event) {

					Main.getInstance().getStageManager().closeSecondaryStage();
					EditPatientSession.getInstance().setIsEdit(false);
					removeListeners();

				}
			};
			btnClose.addEventHandler(MouseEvent.MOUSE_CLICKED, btnCloseHandler);

			btnResetHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent event) {
					String selectedItem = tabPane.getSelectionModel().getSelectedItem().getText();

					if (selectedItem.equals("Patient Demography")) {
						txtCRNum.setText("");
						txtFirstName.setText("");
						txtLastName.setText("");
						selectGender.getSelectionModel().select(0);
						dobPicker.setValue(null);
						txtAge.setText("");
						selectUnit.getSelectionModel().select(0);
						txtWeight.setText("");
						txtHeight.setText("");
						txtBMI.setText("");
						txtBSA.setText("");
						choiceBloodGroup.getSelectionModel().select(0);
						lblErrorMsg.setVisible(false);
					} else if (selectedItem.equals("Contact Details")) {
						txtMobile.setText("");
						txtGuardian.setText("");
						txtPhone.setText("");
						txtEmergencyContact.setText("");
						txtEmail.setText("");
						lblErrorMsg.setVisible(false);
					} else if (selectedItem.equals("Address Details")) {
						selectCountry.getSelectionModel().select(0);
						selectState.getSelectionModel().select(0);
						txtHouseNo.setText("");
						txtStreet.setText("");
						txtLocation.setText("");
						txtDistrict.setText("");
						txtCity.setText("");
						selectArea.getSelectionModel().select(0);
						txtPincode.setText("");
						lblErrorMsg.setVisible(false);
					}
				}
			};
			btnReset.addEventHandler(MouseEvent.MOUSE_CLICKED, btnResetHandler);

			btnBackHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent event) {

					btnProceed.setText("Proceed");
					String selectedItem = tabPane.getSelectionModel().getSelectedItem().getText();

					if (selectedItem.equals("Contact Details")) {
						tabPane.getSelectionModel().select(tabPatientDemography);
						btnBack.setDisable(true);

						tabPatientDemography.setDisable(false);
						tabContactDetails.setDisable(true);
						tabAddressDetails.setDisable(true);

					} else if (selectedItem.equals("Address Details")) {
						tabPane.getSelectionModel().select(tabContactDetails);

						tabPatientDemography.setDisable(true);
						tabContactDetails.setDisable(false);
						tabAddressDetails.setDisable(true);

					}
				}
			};
			btnBack.addEventHandler(MouseEvent.MOUSE_CLICKED, btnBackHandler);

			btnProceedHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent event) {

					boolean isLastTab = false;
					try {
						if (tabPane.getSelectionModel().getSelectedItem() == null) {
							tabPane.getSelectionModel().select(tabPatientDemography);
						}

						String selectedItem = tabPane.getSelectionModel().getSelectedItem().getText();

						if (btnProceed.getText().equalsIgnoreCase("save")
								|| btnProceed.getText().equalsIgnoreCase("Update")) {
							if (validateAdrressDetails()) {
								lblErrorMsg.setVisible(false);
								btnBack.setDisable(false);
								isLastTab = true;
							} else {
								lblErrorMsg.setVisible(true);
							}

						} else if (selectedItem.equals("Patient Demography")) {
							// calculateDOB();
							if (validatePatientDemography()) {
								tabPane.getSelectionModel().select(tabContactDetails);
								lblErrorMsg.setVisible(false);
								tabPatientDemography.setDisable(true);
								tabContactDetails.setDisable(false);
								tabAddressDetails.setDisable(true);
								btnBack.setDisable(false);
							} else {
								lblErrorMsg.setVisible(true);
							}

						} else if (selectedItem.equals("Contact Details")) {
							if (validateContactDetails()) {
								lblErrorMsg.setVisible(false);
								btnBack.setDisable(false);
								tabPane.getSelectionModel().select(tabAddressDetails);
								if (EditPatientSession.getInstance().getIsEdit()) {
									btnProceed.setText("Update");
								} else {
									btnProceed.setText("Save");
								}

								tabPatientDemography.setDisable(true);
								tabContactDetails.setDisable(true);
								tabAddressDetails.setDisable(false);
							} else {
								lblErrorMsg.setVisible(true);
							}
						}

						// ---create Patient entity
						if (isLastTab) {

							disablePaneMainContent.setVisible(true);
							Patient patient = new Patient();

							Address addressEntity = new Address();
							addressEntity.setCountry(selectCountry.getSelectionModel().getSelectedItem().toString());

							if (txtState.isVisible()) {
								addressEntity.setState(txtState.getText());
							} else if (selectState.isVisible()) {
								addressEntity.setState(selectState.getSelectionModel().getSelectedItem().toString());
							}
							addressEntity.setHouseNo(txtHouseNo.getText());
							addressEntity.setStreet(txtStreet.getText());
							addressEntity.setLocation(txtLocation.getText());
							addressEntity.setDistrict(txtDistrict.getText());
							addressEntity.setCityVillage(txtCity.getText());
							if (selectArea.getSelectionModel().getSelectedIndex() != 0) {
								addressEntity.setArea(selectArea.getSelectionModel().getSelectedItem().toString());
							}
							addressEntity.setPinCode(txtPincode.getText());

							if (EditPatientSession.getInstance().getIsEdit()) {
								patient.setPatientId(EditPatientSession.getInstance().getPatientId());
								addressEntity.setAddressId(selectedAddressId);

							}
							patient.setAddress(addressEntity);

							patient.setCrnumber(Long.parseLong(txtCRNum.getText()));
							patient.setFirstName(txtFirstName.getText());
							patient.setLastName(txtLastName.getText());
							patient.setGender(selectGender.getSelectionModel().getSelectedItem().toString());
							if (dobPicker.getValue() != null && !dobPicker.getValue().toString().matches("")) {
								patient.setDob(simpleDateFormat.parse(dobPicker.getValue().toString()));
							}
							patient.setAge(Integer.parseInt(txtAge.getText()));
							patient.setAgeUnit(selectUnit.getSelectionModel().getSelectedItem().toString());
							patient.setWeight(Float.parseFloat(txtWeight.getText()));
							patient.setHeight(Float.parseFloat(txtHeight.getText()));
							if (choiceBloodGroup.getSelectionModel().getSelectedIndex() != 0) {
								patient.setBloodGroup(choiceBloodGroup.getSelectionModel().getSelectedItem());
							}
							if (!txtBMI.getText().isEmpty()) {
								patient.setBmi(Float.parseFloat(txtBMI.getText()));
							}
							if (!txtBSA.getText().isEmpty()) {
								patient.setBsa(Float.parseFloat(txtBSA.getText()));
							}

							patient.setMobilephone(txtMobile.getText());
							patient.setGuardianName(txtGuardian.getText());
							patient.setHomePhone(txtPhone.getText());
							patient.setEmergencyName(txtEmergencyContact.getText());
							patient.setEmail(txtEmail.getText());
							patient.setIsDeleted(false);

							/* processPane.setVisible(true); */
							btnProceed.setDisable(true);
							btnBack.setDisable(true);
							btnReset.setDisable(true);
							// tabAddressDetails.setDisable(true);

							// ---check if CR num already exists

							callCreatePatientService(patient);

						}

					} catch (Exception e) {
						LOGGER.error("## Exception occured:" + e);

					}

				}
			};
			btnProceed.addEventHandler(MouseEvent.MOUSE_CLICKED, btnProceedHandler);

			if (EditPatientSession.getInstance() != null) {
				if (EditPatientSession.getInstance().getIsEdit()) {
					lblHeader.setText("Edit Patient");

					btnReset.setDisable(true);
					disablePaneMainContent.setVisible(true);
					GetPatientDetailsService getPatientDetailsService = GetPatientDetailsService
							.getInstance(EditPatientSession.getInstance().getPatientId());
					txtCRNum.setDisable(true);

					getPatientDetailsService.restart();

					getPatientDetailsServiceSuccessHandler = new EventHandler<WorkerStateEvent>() {

						@Override
						public void handle(WorkerStateEvent event) {

							editPatientDetails = getPatientDetailsService.getPatientDetails();

							if (editPatientDetails != null) {
								selectedAddressId = editPatientDetails.getAddress().getAddressId();
								txtCRNum.setText(editPatientDetails.getCrnumber() + "");
								txtFirstName.setText(editPatientDetails.getFirstName());
								txtLastName.setText(editPatientDetails.getLastName());
								selectGender.getSelectionModel().select(editPatientDetails.getGender());
								if (editPatientDetails.getDob() != null) {
									dobPicker.setValue(LocalDate.parse(editPatientDetails.getDob() + ""));
									ageToogleGroup.selectToggle(radioDob);
								} else {
									ageToogleGroup.selectToggle(radioAge);
								}
								txtAge.setText(editPatientDetails.getAge() + "");
								selectUnit.getSelectionModel().select(editPatientDetails.getAgeUnit());
								txtWeight.setText(editPatientDetails.getWeight() + "");
								txtHeight.setText(editPatientDetails.getHeight() + "");
								selectUnit.getSelectionModel().select(editPatientDetails.getAgeUnit());
								txtBMI.setText(editPatientDetails.getBmi() + "");
								txtBSA.setText(editPatientDetails.getBsa() + "");
								txtMobile.setText(editPatientDetails.getMobilephone());
								txtGuardian.setText(editPatientDetails.getGuardianName());
								txtPhone.setText(editPatientDetails.getHomePhone());
								txtEmergencyContact.setText(editPatientDetails.getEmergencyName());
								txtEmail.setText(editPatientDetails.getEmail());
								selectCountry.getSelectionModel().select(editPatientDetails.getAddress().getCountry());
								if (editPatientDetails.getAddress().getCountry() != null
										&& editPatientDetails.getAddress().getCountry().equalsIgnoreCase("India")) {
									selectState.getSelectionModel().select(editPatientDetails.getAddress().getState());
								} else {
									txtState.setText(editPatientDetails.getAddress().getState());
								}

								txtHouseNo.setText(editPatientDetails.getAddress().getHouseNo());
								txtStreet.setText(editPatientDetails.getAddress().getStreet());
								txtLocation.setText(editPatientDetails.getAddress().getLocation());
								txtDistrict.setText(editPatientDetails.getAddress().getDistrict());
								txtCity.setText(editPatientDetails.getAddress().getCityVillage());
								if (editPatientDetails.getAddress().getArea() != null) {
									selectArea.getSelectionModel().select(editPatientDetails.getAddress().getArea());
								}
								txtPincode.setText(editPatientDetails.getAddress().getPinCode());
								if (editPatientDetails.getBloodGroup() != null) {
									choiceBloodGroup.getSelectionModel().select(editPatientDetails.getBloodGroup());
								}

							}
							disablePaneMainContent.setVisible(false);

							getPatientDetailsService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
									getPatientDetailsServiceSuccessHandler);
							getPatientDetailsService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
									getPatientDetailsServiceFailedHandler);

							getPatientDetailsServiceFailedHandler = null;

							getPatientDetailsServiceSuccessHandler = null;
						}
					};

					getPatientDetailsService.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
							getPatientDetailsServiceSuccessHandler);

					getPatientDetailsServiceFailedHandler = new EventHandler<WorkerStateEvent>() {

						@Override
						public void handle(WorkerStateEvent event) {
							Main.getInstance().getUtility().showNotification("Error", "Error",
									getPatientDetailsService.getException().getMessage());
							disablePaneMainContent.setVisible(false);

							getPatientDetailsService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
									getPatientDetailsServiceSuccessHandler);
							getPatientDetailsService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
									getPatientDetailsServiceFailedHandler);
							getPatientDetailsServiceFailedHandler = null;

							getPatientDetailsServiceSuccessHandler = null;
						}
					};

					getPatientDetailsService.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
							getPatientDetailsServiceFailedHandler);

				}
			}

			dobPicker.focusedProperty().addListener(new ChangeListener<Boolean>() {
				@Override
				public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue,
						Boolean newPropertyValue) {
					if (!newPropertyValue) {

						if (dobPicker.getValue() != null) {
							try {

								Date dob = simpleDateFormat.parse(dobPicker.getValue().toString());

								if (!Validations.futureDateCheck(dob)) {

									calculateDOB();
									// selectUnit.setDisable(true);
									// txtAge.setDisable(true);
								} else {
									lblErrorMsg.setVisible(true);

									lblErrorMsg.setText("*Date of Birth should not be a future date..");
								}
							} catch (Exception e) {
								LOGGER.error("## Exception occured:" + e);
							}
						} else {
							// selectUnit.setDisable(false);
							// txtAge.setDisable(false);
						}
					}

				}
			});
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					txtCRNum.requestFocus();
				}
			});
		} catch (Exception e) {
			LOGGER.error("## Exception occured:" + e);
			Main.getInstance().getUtility().showNotification("Error", "Error", "Something went wrong");
		}

	}

	/**
	 * This method is used to close Create Patient window.
	 */
	private void closePopup() throws Exception {

		try {
			removeListeners();
			Main.getInstance().getStageManager().closeSecondaryStage();
			if (EditPatientSession.getInstance().getIsFromDashboard()) {
				EditPatientSession.getInstance().setIsFromDashboard(false);
				ControllerMediator.getInstance().getMainController().fillDashboardPatientBanner();
			}
		} catch (Exception e) {
			LOGGER.error("## Exception occured:" + e);

		}
	}

	/**
	 * This method is used to call CreatePatientService which saves patient
	 * details in database
	 *
	 * @param patient
	 * @throws Exception
	 */
	private void callCreatePatientService(Patient patient) throws Exception {

		try {

			CreatePatientService createPatientService = CreatePatientService.getInstance(patient);
			createPatientService.restart();

			// ---on service success

			createPatientServiceSuccessHandler = new EventHandler<WorkerStateEvent>() {

				@Override
				public void handle(WorkerStateEvent event) {

					Patient patientObj = createPatientService.getReturnedPatient();

					Integer patientId = 0;
					try {
						if (patientObj.getPatientId() != null) {
							patientId = patientObj.getPatientId();
						}
					} catch (Exception ex) {
						patientId = 0;
					}

					if (patientId > 0) {

						PatientSession.getInstance().setPatient(patientObj);
						if (EditPatientSession.getInstance() != null) {
							if (EditPatientSession.getInstance().getIsEdit()) {
								try {
									closePopup();
								} catch (Exception e1) {
									LOGGER.error("## Exception occured:" + e1);
								}
							} else {
								try {
									PatientSession.getInstance().setPatientCase(null);
									EditCaseFlagSession.getInstance().setIsEdit(false);
									navigateToCreateCaseController();
								} catch (Exception e1) {
									LOGGER.error("## Exception occured:" + e1);
								}
							}
						}

						if (EditPatientSession.getInstance() != null && EditPatientSession.getInstance().getIsEdit()) {

							Main.getInstance().getUtility().showNotification("Information", "Success",
									"Patient updated successfully!");

						} else {
							Main.getInstance().getUtility().showNotification("Information", "Success",
									"Patient created successfully!");

						}

						EditPatientSession.getInstance().setIsEdit(false);

					}

					else {

						btnProceed.setDisable(false);
						btnBack.setDisable(false);
						btnReset.setDisable(false);
						tabAddressDetails.setDisable(false);
					}
					disablePaneMainContent.setVisible(false);

					createPatientService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
							createPatientServiceSuccessHandler);
					createPatientService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
							createPatientServiceFailedHandler);
					createPatientServiceFailedHandler = null;
					createPatientServiceSuccessHandler = null;
				}
			};

			createPatientService.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
					createPatientServiceSuccessHandler);

			createPatientServiceFailedHandler = new EventHandler<WorkerStateEvent>() {

				@Override
				public void handle(WorkerStateEvent event) {
					Main.getInstance().getUtility().showNotification("Error", "Error",
							createPatientService.getException().getMessage());
					btnProceed.setDisable(false);
					disablePaneMainContent.setVisible(false);
					btnBack.setDisable(false);
					btnReset.setDisable(false);
					createPatientService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
							createPatientServiceSuccessHandler);
					createPatientService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
							createPatientServiceFailedHandler);
					createPatientServiceFailedHandler = null;
					createPatientServiceSuccessHandler = null;
				}
			};

			createPatientService.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
					createPatientServiceFailedHandler);

		} catch (Exception e) {
			LOGGER.error("## Exception occured:" + e);

		}

	}

	/**
	 *
	 * This method is used to close Create patient window and redirect to Create
	 * Case screen
	 *
	 * @throws Exception
	 */
	private void navigateToCreateCaseController() throws Exception {

		try {

			closePopup();

			ControllerMediator.getInstance().getMainController().bindPopupFxml(FxmlView.CREATE_CASE);

		} catch (Exception e) {
			LOGGER.error("## Exception occured:" + e);

		}
	}

	/**
	 *
	 * This method is used to calculate BMI and BSA values for given height and
	 * weight
	 *
	 * @param height
	 * @param weight
	 * @return
	 */
	private String calcBMInBSAVal(double height, double weight) {
		try {
			double heightInMeter = height / 100;
			double heightInMeterSquare = Math.pow(heightInMeter, 2);
			double bmiVal = weight / heightInMeterSquare;
			double bsaVal = Math.sqrt((height * weight) / 3600);
			BigDecimal bmiDecimal = new BigDecimal(bmiVal);
			BigDecimal bmiRounded = bmiDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);
			BigDecimal bsaDecimal = new BigDecimal(bsaVal);
			BigDecimal bsaRounded = bsaDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);
			return bmiRounded + "-" + bsaRounded;
		} catch (Exception e) {
			LOGGER.error("## Exception occured:" + e);
			return "";
		}

	}

	/**
	 * This method is used to assign values to BMI and BSA test fields
	 */
	private void assignBMInBSA() {
		try {

			if (txtHeight.getText() != null && !txtHeight.getText().isEmpty() && txtWeight.getText() != null
					&& !txtWeight.getText().isEmpty()) {
				if (validateWeightAndHeight()) {

					String bmiBsaVal = calcBMInBSAVal(Double.parseDouble(txtHeight.getText()),
							Double.parseDouble(txtWeight.getText()));
					txtBMI.setText(bmiBsaVal.split("-")[0]);
					txtBSA.setText(bmiBsaVal.split("-")[1]);
				}
			} else {
				if (txtWeight.getText().isEmpty() || txtWeight.getText().equals("")) {
					txtBMI.setText("");
					txtBSA.setText("");
				}
			}

		} catch (Exception e) {
			LOGGER.error("## Exception occured:" + e);
		}
	}

	private boolean validateWeightAndHeight() {

		try {

			if (!Validations.isDigit(txtWeight.getText())) {
				lblErrorMsg.setText("*Please enter a valid value for weight");
				return false;
			}

			if (!Validations.decimalsUpto2placesAndLength3(txtWeight.getText())) {
				lblErrorMsg.setText("*Please enter weight upto 3 digits and decimal upto 2 digits only");
				return false;
			}
			if (!Validations.nonZeroBigDecimal(txtWeight.getText())) {
				lblErrorMsg.setText("*Please enter weight value more than 0");
				return false;
			}

			// for height

			if (!Validations.isDigit(txtHeight.getText())) {
				lblErrorMsg.setText("*Please enter a valid value for height");
				return false;
			}

			if (!Validations.decimalsUpto2placesAndLength3(txtHeight.getText())) {
				lblErrorMsg.setText("*Please enter height upto 3 digits and decimal upto 2 digits only");
				return false;
			}
			if (!Validations.nonZeroBigDecimal(txtHeight.getText())) {
				lblErrorMsg.setText("*Please enter height value more than 0");
				return false;
			}

			return true;
		} catch (Exception e) {
			LOGGER.error("## Exception occured:" + e);
			return false;
		}

	}

	/**
	 * This method is used to validate Patient Demography Tab
	 *
	 * @return true when form is valid and false if the validation fails
	 */
	private boolean validatePatientDemography() {
		try {
			// for mandatory check

			if (txtCRNum.getText() == null || txtCRNum.getText().equals("") || txtFirstName.getText() == null
					|| txtFirstName.getText().equals("") || selectGender.getSelectionModel().getSelectedIndex() == 0
					|| txtWeight.getText() == null || txtWeight.getText().equals("") || txtHeight.getText() == null
					|| txtHeight.getText().equals("")) {

				lblErrorMsg.setText("*Please fill all the mandatory fields");
				return false;

			}
			if (ageToogleGroup.getSelectedToggle().getUserData().toString().equalsIgnoreCase("Age")) {

				if (txtAge.getText() == null || txtAge.getText().equals("")
						|| selectUnit.getSelectionModel().getSelectedIndex() == 0) {

					lblErrorMsg.setText("*Please fill all the mandatory fields");
					return false;
				}
			} else {
				if (dobPicker.getValue() == null) {
					lblErrorMsg.setText("*Please fill all the mandatory fields");
					return false;
				}
			}
			assignBMInBSA();
			// for CR NUmber

			if (!Validations.isDigitWithoutDot(txtCRNum.getText()) || txtCRNum.getText().length() != 12) {
				lblErrorMsg.setText("*Please enter a valid CR Number having 12 digits");
				return false;
			}

			if (!Validations.nonZeroBigDecimal(txtCRNum.getText())) {
				lblErrorMsg.setText("*Please enter CR Number more than 0");
				return false;
			}

			// for first name
			if (!Validations.isAlphaWithSpaceAndDot(txtFirstName.getText())
					|| !Validations.maxLength(txtFirstName.getText(), 50)) {
				lblErrorMsg.setText("*Please enter a valid first name upto 50 characters");
				return false;
			}

			// for last name

			if ((txtLastName.getText() != null && !txtLastName.getText().equals("")
					&& !Validations.isAlphaWithSpaceAndDot(txtLastName.getText()))
					|| (txtLastName.getText() != null && !txtLastName.getText().equals("")
							&& !Validations.maxLength(txtLastName.getText(), 50))) {
				lblErrorMsg.setText("*Please enter a valid last name upto 50 characters");
				return false;
			}
			// for date of birth
			if (dobPicker.getValue() != null) {
				try {

					Date dob = simpleDateFormat.parse(dobPicker.getValue().toString());

					if (Validations.futureDateCheck(dob)) {
						lblErrorMsg.setText("*Date of Birth should not be a future date..");
						return false;
					}
				} catch (Exception e) {
					LOGGER.error("## Exception occured:" + e);
				}
			}

			// for age
			if (dobPicker.getValue() == null) {
				if (!Validations.isDigitWithoutDot(txtAge.getText()) || !Validations.maxLength(txtAge.getText(), 3)) {
					lblErrorMsg.setText("*Please enter a valid age upto 3 digits");
					return false;
				}

				if (!Validations.nonZeroBigDecimal(txtAge.getText())) {
					lblErrorMsg.setText("*Please enter age value more than 0");
					return false;
				}
			}
			// for weight

			if (!Validations.isDigit(txtWeight.getText())) {
				lblErrorMsg.setText("*Please enter a valid value for weight");
				return false;
			}

			if (!Validations.decimalsUpto2placesAndLength3(txtWeight.getText())) {
				lblErrorMsg.setText("*Please enter weight upto 3 digits and decimal upto 2 digits only");
				return false;
			}
			if (!Validations.nonZeroBigDecimal(txtWeight.getText())) {
				lblErrorMsg.setText("*Please enter weight value more than 0");
				return false;
			}

			// for height

			if (!Validations.isDigit(txtHeight.getText())) {
				lblErrorMsg.setText("*Please enter a valid value for height");
				return false;
			}

			if (!Validations.decimalsUpto2placesAndLength3(txtHeight.getText())) {
				lblErrorMsg.setText("*Please enter height upto 3 digits and decimal upto 2 digits only");
				return false;
			}
			if (!Validations.nonZeroBigDecimal(txtHeight.getText())) {
				lblErrorMsg.setText("*Please enter height value more than 0");
				return false;
			}

			// assignBMInBSA();

			// for BMI and BSA

			if (!Validations.decimalsUpto2placesAndLength3(txtBMI.getText())) {
				lblErrorMsg.setText("*BMI value can be upto 3 digits and decimal upto 2 digits only");
				return false;
			}
			if (!Validations.nonZeroBigDecimal(txtBMI.getText())) {
				lblErrorMsg.setText("*Please enter BMI value more than 0");
				return false;
			}
			if (!Validations.decimalsUpto2placesAndLength3(txtBSA.getText())) {
				lblErrorMsg.setText("*BSA value can be upto 3 digits and decimal upto 2 digits only");
				return false;
			}
			if (!Validations.nonZeroBigDecimal(txtBSA.getText())) {
				lblErrorMsg.setText("*Please enter BSA value more than 0");
				return false;
			}

			return true;

		} catch (Exception e) {
			LOGGER.error("## Exception occured:" + e);
			return false;
		}

	}

	/**
	 * This method is used to validate Patient Contact Details Tab
	 *
	 * @return true when form is valid and false if the validation fails
	 */
	private boolean validateContactDetails() {
		try {

			// for mandatory check

			if (txtMobile.getText() == null || txtMobile.getText().equals("") || txtGuardian.getText() == null
					|| txtGuardian.getText().equals("")) {
				lblErrorMsg.setText("*Please fill all the mandatory fields");
				return false;
			}

			// for mobile
			if (txtMobile.getText() != null && !txtMobile.getText().trim().equalsIgnoreCase("NR")) {
				if (!Validations.isDigitWithoutDot(txtMobile.getText()) || txtMobile.getText().length() != 10) {
					lblErrorMsg.setText("*Please enter a valid mobile no. having 10 digits");
					return false;
				}

				if (!Validations.nonZeroBigDecimal(txtMobile.getText())) {
					lblErrorMsg.setText("*Please enter mobile no. more than 0");
					return false;
				}
			}

			// for guardian
			if (!Validations.isAlphaWithSpaceAndDot(txtGuardian.getText())
					|| !Validations.maxLength(txtGuardian.getText(), 50)) {
				lblErrorMsg.setText("*Please enter a valid guardian name upto 50 characters");
				return false;
			}

			// for home phone
			if (txtPhone.getText() != null && !txtPhone.getText().equals("")) {
				if (!Validations.isDigitWithoutDot(txtPhone.getText())
						|| !Validations.maxLength(txtPhone.getText(), 12)) {
					lblErrorMsg.setText("*Please enter a valid home no. upto 12 digits");
					return false;
				}

				if (!Validations.nonZeroBigDecimal(txtPhone.getText())) {
					lblErrorMsg.setText("*Please enter mobile no. more than 0");
					return false;
				}
			}

			// for emergency name

			if ((txtEmergencyContact.getText() != null && !txtEmergencyContact.getText().equals("")
					&& !Validations.isAlphaWithSpaceAndDot(txtEmergencyContact.getText()))
					|| (txtEmergencyContact.getText() != null && !txtEmergencyContact.getText().equals("")
							&& !Validations.maxLength(txtEmergencyContact.getText(), 50))) {
				lblErrorMsg.setText("*Please enter a valid emergency name upto 50 characters");
				return false;
			}

			// for email check
			if ((txtEmail.getText() != null && !txtEmail.getText().equals("")
					&& !Validations.validEmail(txtEmail.getText()))
					|| (txtEmail.getText() != null && !txtEmail.getText().equals("")
							&& !Validations.maxLength(txtEmail.getText(), 50))) {
				lblErrorMsg.setText("*Please enter a valid email upto 50 characters");
				return false;
			}
			return true;

		} catch (Exception e) {
			LOGGER.error("## Exception occured:" + e);
			return false;
		}
	}

	/**
	 * This method is used to validate Patient Address Details tab
	 *
	 * @return true when form is valid and false if the validation fails
	 */
	private boolean validateAdrressDetails() {
		try {
			// for mandatory check

			if (selectedIndia) {
				if (selectState.getSelectionModel().getSelectedItem() == null
						|| selectState.getValue().trim().equalsIgnoreCase("Select") || txtCity.getText() == null
						|| txtCity.getText().equals("") || selectCountry.getSelectionModel().getSelectedItem() == null
						|| selectCountry.getValue().trim().equalsIgnoreCase("Select")) {
					lblErrorMsg.setText("*Please fill all the mandatory fields");
					return false;
				}
			} else {
				if (txtState.getText() == null || txtState.getText().equals("") || txtCity.getText() == null
						|| txtCity.getText().equals("")
						|| selectCountry.getSelectionModel().getSelectedItem() == null) {
					lblErrorMsg.setText("*Please fill all the mandatory fields");
					return false;
				}
				// for state
				if ((!Validations.isAlphaWithSpaceAndDot(txtState.getText()))
						|| !Validations.maxLength(txtState.getText(), 50)) {
					lblErrorMsg.setText("*Please enter a state name upto 50 characters");
					return false;
				}
			}

			// for house no
			// if (txtHouseNo.getText() != null &&
			// !txtHouseNo.getText().trim().isEmpty()
			// && !Validations.isDigitWithoutDot(txtHouseNo.getText())) {
			// lblErrorMsg.setText("Please enter a valid number for House No");
			// return false;
			// }
			if (txtHouseNo.getText() != null && !txtHouseNo.getText().trim().isEmpty()
					&& !Validations.isAlphaNumericWithSpaceAndSpecialCharacterWithSlash(txtHouseNo.getText())) {
				lblErrorMsg.setText("Please enter a valid house no");
				return false;
			}
			if (txtHouseNo.getText() != null && !txtHouseNo.getText().trim().isEmpty()
					&& !Validations.maxLength(txtHouseNo.getText(), 50)) {
				lblErrorMsg.setText("Please enter a house no less than 50 character");
				return false;
			}
			// if (txtHouseNo.getText() != null &&
			// !txtHouseNo.getText().trim().isEmpty()
			// && !Validations.nonZeroBigDecimal(txtHouseNo.getText())) {
			// lblErrorMsg.setText("Please enter a house no other than 0");
			// return false;
			// }

			// for street

			if (txtStreet.getText() != null && !txtStreet.getText().trim().isEmpty()
					&& !Validations.isAlphaNumericWithSpaceAndSpecialCharacter(txtStreet.getText())) {
				lblErrorMsg.setText("Please enter a valid street name");
				return false;
			}
			if (txtStreet.getText() != null && !txtStreet.getText().trim().isEmpty()
					&& !Validations.maxLength(txtStreet.getText(), 50)) {
				lblErrorMsg.setText("Please enter a street name less than 50 character");
				return false;
			}

			// for location

			if (txtLocation.getText() != null && !txtLocation.getText().trim().isEmpty()
					&& !Validations.isAlphaNumericWithSpaceAndSpecialCharacter(txtLocation.getText())) {
				lblErrorMsg.setText("Please enter a valid location name");
				return false;
			}
			if (txtLocation.getText() != null && !txtLocation.getText().trim().isEmpty()
					&& !Validations.maxLength(txtLocation.getText(), 50)) {
				lblErrorMsg.setText("Please enter a location name less than 50 character");
				return false;
			}

			// for district
			if (txtDistrict.getText() != null && !txtDistrict.getText().trim().isEmpty()
					&& !Validations.isAlpha(txtDistrict.getText())) {
				lblErrorMsg.setText("Please enter a valid district name");
				return false;
			}
			if (txtDistrict.getText() != null && !txtDistrict.getText().trim().isEmpty()
					&& !Validations.maxLength(txtDistrict.getText(), 50)) {
				lblErrorMsg.setText("Please enter a district name less than 50 character");
				return false;
			}

			// for city
			if (!Validations.isAlpha(txtCity.getText()) || !Validations.maxLength(txtCity.getText(), 50)) {
				lblErrorMsg.setText("*Please enter a valid city name upto 50 characters");
				return false;
			}

			// for pin code

			if (txtPincode.getText() != null && !txtPincode.getText().trim().isEmpty()
					&& !Validations.isDigitWithoutDot(txtPincode.getText())) {
				lblErrorMsg.setText("Please enter a valid number for  Pin code");
				return false;
			}

			if (txtPincode.getText() != null && !txtPincode.getText().trim().isEmpty()
					&& !Validations.nonZeroBigDecimal(txtPincode.getText())) {
				lblErrorMsg.setText("*Please enter value more than 0 for Pin code");
				return false;
			}
			if (txtPincode.getText() != null && !txtPincode.getText().trim().isEmpty()
					&& !Validations.maxLength(txtPincode.getText(), 6)) {
				lblErrorMsg.setText("Please enter a  value less than 6 digits for Pin code");
				return false;
			}

			return true;
		} catch (Exception e) {
			LOGGER.error("## Exception occured:" + e);
			return false;
		}

	}

	/**
	 * This method is used to calculate Age and age unit from DOB calendar input
	 *
	 * @throws Exception
	 */
	private void calculateDOB() throws Exception {

		Calendar cal = Calendar.getInstance();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date nowDate = new Date();
		Date dateTime;

		try {
			dateTime = simpleDateFormat.parse(dobPicker.getValue().toString());
			long diff = nowDate.getTime() - dateTime.getTime();

			if (cal != null) {
				Date ageDate = new Date(diff);
				cal.setTime(ageDate);
				int years = cal.get(Calendar.YEAR);
				int month = cal.get(Calendar.MONTH);
				int days = cal.get(Calendar.DAY_OF_MONTH);

				if ((years - 1970) <= 0) {
					if (month <= 0) {
						if ((days - 1) <= 0) {
							txtAge.setText("1");
							selectUnit.getSelectionModel().select("Days");
						} else {
							txtAge.setText((days - 1) + "");
							selectUnit.getSelectionModel().select("Days");
						}
					} else {
						txtAge.setText(month + "");
						selectUnit.getSelectionModel().select("Months");
					}
				} else {
					txtAge.setText((years - 1970) + "");
					selectUnit.getSelectionModel().select("Years");
				}

			}

		} catch (Exception e) {

			LOGGER.error("## Exception occured:" + e);

		}

	}

	/**
	 * This method is used to remove all listeners and nullify class level
	 * Variables
	 */
	private void removeListeners() {
		try {

			txtEmail.textProperty().removeListener(txtEmailChangeListener);
			txtEmergencyContact.textProperty().removeListener(txtEmergencyContactListener);
			txtGuardian.textProperty().removeListener(txtGuardianChangeListener);
			btnProceed.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnProceedHandler);
			btnClose.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnCloseHandler);
			btnReset.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnResetHandler);
			btnBack.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnBackHandler);
			txtCRNum.textProperty().removeListener(txtCRNumChangeListener);
			txtMobile.textProperty().removeListener(txtMobileChangeListener);
			txtWeight.focusedProperty().removeListener(txtWeightChangeListener);
			txtHeight.focusedProperty().removeListener(txtHeightChangeListener);
			txtLastName.textProperty().removeListener(txtLastNameChangeListener);
			txtFirstName.textProperty().removeListener(txtFirstnameChangeListener);
			txtAge.textProperty().removeListener(txtAgeChangeListener);
			txtHeight.textProperty().removeListener(txtHeightListener);
			txtWeight.textProperty().removeListener(txtWeightListener);
			ageToogleGroup.selectedToggleProperty().removeListener(ageToogleGroupListener);
			selectCountry.getSelectionModel().selectedItemProperty().removeListener(selectCountryChangeListener);
			editPatientDetails = null;
			simpleDateFormat = null;
			btnResetHandler = null;
			btnCloseHandler = null;
			btnBackHandler = null;
			btnProceedHandler = null;
			ageToogleGroupListener = null;
			txtCRNumChangeListener = null;
			txtMobileChangeListener = null;
			txtFirstnameChangeListener = null;
			txtAgeChangeListener = null;
			txtLastNameChangeListener = null;
			txtWeightListener = null;
			txtHeightListener = null;
			txtGuardianChangeListener = null;
			txtEmergencyContactListener = null;
			txtEmailChangeListener = null;
			txtWeightChangeListener = null;
			txtHeightChangeListener = null;
			selectCountryChangeListener = null;

		} catch (Exception e) {
			LOGGER.error("## Exception occured:" + e);
		}
	}

}
