/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package controllers;

import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.cdac.common.GeneratedEntities.Entityvalues;
import com.cdac.common.GeneratedEntities.Fdamedications;
import com.cdac.common.GeneratedEntities.Fluid;
import com.cdac.common.GeneratedEntities.Investigationparameterfields;
import com.cdac.common.GeneratedEntities.Users;
import com.cdac.common.pojoClasses.UserWithrRolesForUserAuthentication;

import application.FxmlView;
import application.Main;
import javafx.application.Platform;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import mediatorPattern.ControllerMediator;
import model.MasterDataSession;
import model.OTSession;
import model.UserSession;
import services.AuthenticateUserService;
import services.GetFavoriteDataService;
import services.GetMasterDataService;

/**
 * This controller is used to log in to the application
 *
 * @author Sudeep_Sahoo
 *
 */
public class LoginController implements Initializable {

	@FXML
	private Button btnClose;

	@FXML
	private ImageView imageBg;

	@FXML
	private TextField txtUsername;

	@FXML
	private PasswordField txtPassword;

	@FXML
	private Button btnReset;

	@FXML
	private Button btnLogin;

	@FXML
	private Label lblError;

	@FXML
	private ImageView lodingImg;

	@FXML
	private Label lblMessage;

	@FXML
	private Pane paneLoader;

	@FXML
	private BorderPane borderPaneLogin;

	@FXML
	private Label lblErrorCapsLock;

	@FXML
	private VBox vbOTSelection;

	@FXML
	private ChoiceBox<String> choiceOT;

	@FXML
	private Button btnProceed;

	@FXML
	private VBox vbLogin;

	@FXML
	private Label lblErrorOT;

	@FXML
	private HBox hbPageHeaderContainer;

	@FXML
	private Label lblHeader;

	@FXML
	private VBox vbMainContainer;

	@FXML
	private StackPane stackpaneBGHolder;

	private static final Logger LOGGER = Logger.getLogger(LoginController.class.getName());
	private boolean isInitialized = false;
	private boolean isCapsLock = false;
	private EventHandler<WorkerStateEvent> authenticationSuccessHandler;
	private EventHandler<WorkerStateEvent> authenticationFailedHandler;
	private EventHandler<WorkerStateEvent> getMasterDataSuccessHandler;
	private EventHandler<WorkerStateEvent> getMasterDataFailedHandler;
	private EventHandler<WorkerStateEvent> getFavMasterDataServiceSuccessHandler;
	private EventHandler<WorkerStateEvent> getFavMasterDataServiceFailedHandler;
	private EventHandler<javafx.scene.input.MouseEvent> resetHandler;
	private EventHandler<javafx.scene.input.KeyEvent> keyPasswordEventHandler;
	private EventHandler<javafx.scene.input.KeyEvent> keyUsernameEventHandler;
	private EventHandler<javafx.scene.input.MouseEvent> loginHandler;
	private EventHandler<javafx.scene.input.MouseEvent> closeHandler;
	private EventHandler<javafx.scene.input.KeyEvent> keyEventHandler;
	private EventHandler<javafx.scene.input.MouseEvent> proceedBtnHandler;

	/**
	 * This method initializes the all Event handlers and Default values on page
	 * load
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		try {
			ControllerMediator.getInstance().registerLoginController(this);
			double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
			double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();

			borderPaneLogin.setPrefSize(screenWidth, screenHeight);
			vbMainContainer.setPrefSize(screenWidth * 0.95, screenHeight * 0.95);
			stackpaneBGHolder.setPrefSize(screenWidth * 0.95, screenHeight * 0.95);
			hbPageHeaderContainer.setPrefWidth(screenWidth);
			lblHeader.setPrefWidth(screenWidth * 0.97);
			imageBg.setFitHeight(screenHeight * 0.98);
			imageBg.setFitWidth(screenWidth * 1.2);
			vbLogin.setVisible(true);
			vbOTSelection.setVisible(false);
			lblErrorOT.setVisible(false);
			lblMessage.setVisible(false);
			lblErrorCapsLock.setVisible(false);
			paneLoader.setVisible(false);

			// ---Set Background image
			Image bgImg = new Image(this.getClass().getResourceAsStream("/res/bg_Fullscreen.jpg"));
			imageBg.setImage(bgImg);

			/*
			 * imageBg.prefHeight(screenHeight); imageBg.prefWidth(screenWidth);
			 */
			Image spinnerImg = new Image(this.getClass().getResourceAsStream("/res/Spinner.gif"));
			lodingImg.setImage(spinnerImg);

			keyUsernameEventHandler = new EventHandler<javafx.scene.input.KeyEvent>() {

				@Override
				public void handle(javafx.scene.input.KeyEvent event) {

					if (event.getCode().equals(KeyCode.ENTER)) {
						try {
							logIn();
						} catch (Exception e) {
							LOGGER.error("## Exception occured:", e);
						}
					}
				}
			};
			txtUsername.addEventHandler(javafx.scene.input.KeyEvent.KEY_RELEASED, keyUsernameEventHandler);

			keyPasswordEventHandler = new EventHandler<javafx.scene.input.KeyEvent>() {

				@Override
				public void handle(javafx.scene.input.KeyEvent event) {

					if (event.getCode().equals(KeyCode.ENTER)) {
						try {
							logIn();
						} catch (Exception e) {
							LOGGER.error("## Exception occured:", e);
						}
					}
				}
			};
			txtPassword.addEventHandler(javafx.scene.input.KeyEvent.KEY_RELEASED, keyPasswordEventHandler);

			resetHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent event) {

					try
					{
						txtPassword.setText("");
						txtUsername.setText("");
						lblError.setVisible(false);
					}
					catch (Exception e)
					{
						LOGGER.error("## Exception occured:", e);
					}
				}
			};
			btnReset.addEventHandler(MouseEvent.MOUSE_CLICKED, resetHandler);

			loginHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent event) {
					try {
						logIn();
					} catch (Exception e1) {
						LOGGER.error("## Exception occured:", e1);
					}
				}
			};
			btnLogin.addEventHandler(MouseEvent.MOUSE_CLICKED, loginHandler);

			closeHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent event) {
					Platform.exit();
					System.exit(0);
				}
			};
			btnClose.addEventHandler(MouseEvent.MOUSE_CLICKED, closeHandler);

			keyEventHandler = new EventHandler<javafx.scene.input.KeyEvent>() {

				@Override
				public void handle(javafx.scene.input.KeyEvent event) {

					try
					{
						if (event.getCode() == KeyCode.CAPS) {

							if (isCapsLock) {
								lblErrorCapsLock.setVisible(true);
							} else {
								lblErrorCapsLock.setVisible(false);
							}
							isCapsLock = !isCapsLock;
						}
					}
					catch (Exception e)
					{
						LOGGER.error("## Exception occured:", e);
					}
				}
			};
			borderPaneLogin.addEventHandler(javafx.scene.input.KeyEvent.KEY_RELEASED, keyEventHandler);

			proceedBtnHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent event) {

					try
					{
						if (choiceOT.getSelectionModel().getSelectedIndex() != 0)
						{
							if (OTSession.getInstance() != null)
							{
								OTSession.getInstance().setSelectedOT(choiceOT.getSelectionModel().getSelectedItem());
							}

							Main.getInstance().getParentStage().hide();
							navigateToMainDashboard();

						}
						else
						{
							lblErrorOT.setVisible(true);
						}
					}
					catch (Exception e)
					{
						LOGGER.error("## Exception occured:", e);
					}
				}
			};
			btnProceed.addEventHandler(MouseEvent.MOUSE_CLICKED, proceedBtnHandler);

			// ---If CAPS-LOCK is ON
			if (Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK)) {
				isCapsLock = false;
				lblErrorCapsLock.setVisible(true);
			} else {
				isCapsLock = true;
			}

			/*// ---If DB initialize service called once, skip next time
			if (!isInitialized) {
				try {
					// callInitilizeServerService();
				} catch (Exception e1) {
					LOGGER.error("## Exception occured:", e1);
				}
			}*/
		} catch (Exception e) {
			LOGGER.error("## Exception occured:", e);
			Main.getInstance().getUtility().showNotification("Error", "Error", "Something went wrong");
		}
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				txtUsername.requestFocus();
			}
		});

	}

	/*
	 * public void callInitilizeServerService() throws Exception { //
	 * LOGGER.debug("Logger Name: " + LOGGER.getName()); try {
	 * paneLoader.setVisible(true); lblMessage.setVisible(true);
	 * lodingImg.setVisible(true); InitilizationService initilizeServerService =
	 * new InitilizationService(); initilizeServerService.restart();
	 *
	 * initializeSuccessHandler = new EventHandler<WorkerStateEvent>() {
	 *
	 * @Override public void handle(WorkerStateEvent event) {
	 *
	 * System.out.println("Connected to Server..........");
	 * lodingImg.setVisible(false); lblMessage.setVisible(false);
	 * paneLoader.setVisible(false); txtUsername.requestFocus();
	 *
	 *
	 * isInitialized = true;
	 * initilizeServerService.removeEventHandler(WorkerStateEvent.
	 * WORKER_STATE_FAILED, initializeFailedHandler);
	 * initilizeServerService.removeEventHandler(WorkerStateEvent.
	 * WORKER_STATE_SUCCEEDED, initializeSuccessHandler);
	 * Main.getInstance().getUtility().showNotification("Information",
	 * "Information", "Connected to Server successfully."); }
	 *
	 * };
	 *
	 * initilizeServerService.addEventHandler(WorkerStateEvent.
	 * WORKER_STATE_SUCCEEDED, initializeSuccessHandler);
	 *
	 * initializeFailedHandler = new EventHandler<WorkerStateEvent>() {
	 *
	 * @Override public void handle(WorkerStateEvent event) {
	 * System.out.println("Error in connecting to server............");
	 * Main.getInstance().getUtility().showNotification("Error", "Error",
	 * initilizeServerService.getException().getMessage()); isInitialized =
	 * false; initilizeServerService.removeEventHandler(WorkerStateEvent.
	 * WORKER_STATE_FAILED, initializeFailedHandler);
	 * initilizeServerService.removeEventHandler(WorkerStateEvent.
	 * WORKER_STATE_SUCCEEDED, initializeSuccessHandler); }
	 *
	 * };
	 *
	 * initilizeServerService.addEventHandler(WorkerStateEvent.
	 * WORKER_STATE_FAILED, initializeFailedHandler);
	 *
	 * } catch (
	 *
	 * Exception e) { // LOGGER.error("## Exception occured:" + e.getMessage());
	 * Main.getInstance().getUtility().showNotification("Error", "Error",
	 * "Something went wrong"); } }
	 */

	/**
	 *
	 * This method is used to call AuthenticateUserService for user
	 * authentication
	 *
	 * @throws Exception
	 */
	private void logIn() throws Exception
	{
		// LOGGER.debug("Logger Name: " + LOGGER.getName());
		try {

			if (!txtUsername.getText().isEmpty() && !txtPassword.getText().isEmpty()) {
				lblError.setVisible(false);
				Users user = new Users();
				user.setUserName(txtUsername.getText());
				user.setPassword(txtPassword.getText());
				callAuthenticateUserService(user);

			} else {
				lblError.setVisible(true);
				lblError.setText("* Please enter User Name and Password.");
			}
		} catch (Exception e) {
			LOGGER.error("## Exception occured:", e);
		}
	}

	/**
	 * This method is used to handle ENTER-KEY events on username and password
	 * text fields
	 */
	private void handelEnterKeyAction()
	{

		EventHandler<javafx.scene.input.KeyEvent> keyUsernameEventHandler = new EventHandler<javafx.scene.input.KeyEvent>() {

			@Override
			public void handle(javafx.scene.input.KeyEvent event) {

				if (event.getCode().equals(KeyCode.ENTER)) {
					try {
						logIn();
					} catch (Exception e) {
						LOGGER.error("## Exception occured:", e);
					}
				}
			}
		};
		txtUsername.addEventHandler(javafx.scene.input.KeyEvent.KEY_RELEASED, keyUsernameEventHandler);

		EventHandler<javafx.scene.input.KeyEvent> keyPasswordEventHandler = new EventHandler<javafx.scene.input.KeyEvent>() {

			@Override
			public void handle(javafx.scene.input.KeyEvent event) {

				if (event.getCode().equals(KeyCode.ENTER)) {
					try {
						logIn();
					} catch (Exception e) {
						LOGGER.error("## Exception occured:", e);
					}
				}
			}
		};
		txtPassword.addEventHandler(javafx.scene.input.KeyEvent.KEY_RELEASED, keyPasswordEventHandler);

	}

	/**
	 * This method is used to call AuthenticateUserService which authenticate
	 * the user trying to log in
	 *
	 * @param user
	 * @throws Exception
	 */
	private void callAuthenticateUserService(Users user) throws Exception
	{
		// LOGGER.debug("Logger Name: " + LOGGER.getName());
		try {
			lodingImg.setVisible(true);
			txtUsername.setDisable(true);
			txtPassword.setDisable(true);
			btnReset.setDisable(true);
			btnLogin.setDisable(true);

			AuthenticateUserService authenticateUserService = AuthenticateUserService.getInstance(user);
			authenticateUserService.restart();

			authenticationSuccessHandler = new EventHandler<WorkerStateEvent>() {

				@Override
				public void handle(WorkerStateEvent event) {

					try
					{
						txtUsername.setDisable(false);
						txtPassword.setDisable(false);
						btnReset.setDisable(false);
						btnLogin.setDisable(false);

						Map<String, Object> userDetailsMapDB = authenticateUserService.getUserDetails();
						lblError.setVisible(false);

						if (userDetailsMapDB != null) {
							for (Map.Entry<String, Object> entry : userDetailsMapDB.entrySet()) {
								String msg = entry.getKey();

								if (msg.equalsIgnoreCase("success")) {
									UserWithrRolesForUserAuthentication userObj = (UserWithrRolesForUserAuthentication) entry
											.getValue();
									UserSession.getInstance().setUserWithrRolesForUserAuthentication(userObj);

									vbLogin.setVisible(false);
									vbOTSelection.setVisible(true);
									try {

										fetchMasterData();
									} catch (Exception e1) {

										LOGGER.error("## Exception occured:", e1);
									}

								} else if (msg.equalsIgnoreCase("error")) {
									lodingImg.setVisible(false);
									lblError.setText(entry.getValue().toString());
									txtPassword.setText("");
									lblError.setVisible(true);

								}
								entry.getValue();
							}
						} else {

							lblError.setText("Some error occurred! Try again later.");
							txtPassword.setText("");
							lblError.setVisible(true);
						}
						// getFavDatas();
						authenticateUserService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
								authenticationFailedHandler);
						authenticateUserService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
								authenticationSuccessHandler);
					}
					catch (Exception e)
					{
						LOGGER.error("## Exception occured:", e);
					}

				}

			};

			authenticateUserService.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
					authenticationSuccessHandler);

			authenticationFailedHandler = new EventHandler<WorkerStateEvent>() {

				@Override
				public void handle(WorkerStateEvent event) {
					try
					{
						Main.getInstance().getUtility().showNotification("Error", "Error",
								authenticateUserService.getException().getMessage());
						lodingImg.setVisible(false);
						authenticateUserService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
								authenticationFailedHandler);
						authenticateUserService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
								authenticationSuccessHandler);
					}
					catch (Exception e)
					{
						LOGGER.error("## Exception occured:", e);
					}
				}

			};

			authenticateUserService.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED, authenticationFailedHandler);
		} catch (Exception e) {
			LOGGER.error("## Exception occured:", e);

		}

	}

	/**
	 * This method is used to navigate main dashborad screen after User
	 * Authentication
	 *
	 * @throws Exception
	 */
	public void navigateToMainDashboard() throws Exception
	{
		// LOGGER.debug("Logger Name: " + LOGGER.getName());
		try {
			removeListeners();

			Main.getInstance().getStageManager().switchScene(FxmlView.MAIN);

		} catch (Exception e) {
			LOGGER.error("## Exception occured:", e);

		}
	}

	/**
	 * This method is used to fetch master data for dropdowns used in the
	 * application from data base and put these in application session
	 */
	public void fetchMasterData()
	{

		GetMasterDataService masterDataService = GetMasterDataService.getInstance();
		masterDataService.restart();

		getMasterDataSuccessHandler = new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent event) {

				try
				{
					Map<String, List<Entityvalues>> entityDetailsList = new HashMap<String, List<Entityvalues>>();
					entityDetailsList = masterDataService.getEntityDetailsList();

					if (entityDetailsList != null) {
						if (MasterDataSession.getInstance().getMasterDataMap() != null) {

							List<String> masterDataList;

							for (Map.Entry<String, List<Entityvalues>> entry : entityDetailsList.entrySet()) {

								masterDataList = new ArrayList<String>();

								if (entry.getValue() != null) {
									for (Entityvalues obj : entry.getValue()) {
										if (obj != null) {
											if (obj.getEntityValue() != null && !obj.getEntityValue().isEmpty())
												masterDataList.add(obj.getEntityValue());

										}
									}

									if (MasterDataSession.getInstance().getMasterDataMap().get(entry.getKey()) == null) {
										MasterDataSession.getInstance().getMasterDataMap().put(entry.getKey(),
												masterDataList);
									}

								}
								if (entry.getValue() != null
										&& entry.getKey().toString().equalsIgnoreCase("Specialities")) {
									masterDataList = new ArrayList<String>();
									for (Entityvalues obj : entry.getValue()) {
										if (obj != null) {
											if (obj.getEntityValue() != null && !obj.getEntityValue().isEmpty())
												masterDataList.add(obj.getEntityValueId() + "");
										}
									}
									if (MasterDataSession.getInstance().getMasterDataMap()
											.get("SpecialitiesKeys") == null) {
										MasterDataSession.getInstance().getMasterDataMap().put("SpecialitiesKeys",
												masterDataList);
									}
								}
							}

							// try {
							// // if
							// //
							// (ControllerMediator.getInstance().getCreateCaseController()
							// // != null) {
							// //
							// ControllerMediator.getInstance().getCreateCaseController().fillDropdownValues();
							// // }
							// } catch (Exception e1) {
							// LOGGER.error("## Exception occured:", e1);
							// }
						}
					}
					List<String> otListString = new ArrayList<>();
					if (MasterDataSession.getInstance().getMasterDataMap().get("OT") != null) {
						otListString.add("Select");
						otListString.addAll(MasterDataSession.getInstance().getMasterDataMap().get("OT"));
					}
					choiceOT.getItems().clear();
					choiceOT.getItems().addAll(otListString);
					choiceOT.getSelectionModel().select(0);
					lodingImg.setVisible(false);
					masterDataService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED, getMasterDataFailedHandler);
					masterDataService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
							getMasterDataSuccessHandler);
				}
				catch (Exception e)
				{
					LOGGER.error("## Exception occured:", e);
				}
			}

		};

		masterDataService.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, getMasterDataSuccessHandler);

		getMasterDataFailedHandler = new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent event) {

				try
				{
					Main.getInstance().getUtility().showNotification("Error", "Error",
							masterDataService.getException().getMessage());
					masterDataService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED, getMasterDataFailedHandler);
					masterDataService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
							getMasterDataSuccessHandler);
				}
				catch (Exception e)
				{
					LOGGER.error("## Exception occured:", e);
				}
			}

		};

		masterDataService.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED, getMasterDataFailedHandler);

		try {

			getFavMasterData();
		} catch (Exception e1) {
			LOGGER.error("## Exception occured:", e1);
		}

	}

	/**
	 * This method is used to fetch all master data of Test , Favourite Fluids
	 * and Favourite Medications .It saves all these data in application session
	 */
	private void getFavMasterData()
	{

		GetFavoriteDataService getFavoriteDataService = GetFavoriteDataService.getInstance();
		getFavoriteDataService.restart();

		getFavMasterDataServiceSuccessHandler = new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent event) {

				try
				{
					Map<String, List<Investigationparameterfields>> testparamList = getFavoriteDataService
							.getTestParameters();
					if (MasterDataSession.getInstance().getTestParamDataMap().get("TestParam") == null) {
						MasterDataSession.getInstance().getTestParamDataMap().put("TestParam", testparamList);
					}

					List<Fluid> allFluidslist = getFavoriteDataService.getAllFluidslist();
					if (MasterDataSession.getInstance().getFavFluidDataMap().get("FavFluids") == null) {
						MasterDataSession.getInstance().getFavFluidDataMap().put("FavFluids", allFluidslist);
					}

					List<Fdamedications> fdamedicationsList = getFavoriteDataService.getFdamedicationsList();
					if (MasterDataSession.getInstance().getFavMedDataMap().get("FavMedication") == null) {
						MasterDataSession.getInstance().getFavMedDataMap().put("FavMedication", fdamedicationsList);
					}

					getFavoriteDataService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
							getFavMasterDataServiceFailedHandler);
					getFavoriteDataService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
							getFavMasterDataServiceSuccessHandler);
				}
				catch (Exception e)
				{
					LOGGER.error("## Exception occured:", e);
				}
			}

		};

		getFavoriteDataService.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
				getFavMasterDataServiceSuccessHandler);

		getFavMasterDataServiceFailedHandler = new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent event) {
				try
				{
					Main.getInstance().getUtility().showNotification("Error", "Error",
							getFavoriteDataService.getException().getMessage());
					getFavoriteDataService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
							getFavMasterDataServiceFailedHandler);
					getFavoriteDataService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
							getFavMasterDataServiceSuccessHandler);
				}
				catch (Exception e)
				{
					LOGGER.error("## Exception occured:", e);
				}
			}

		};

		getFavoriteDataService.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
				getFavMasterDataServiceFailedHandler);

	}

	/**
	 * This method is used to remove all listeners
	 */
	private void removeListeners()
	{

		btnReset.removeEventHandler(MouseEvent.MOUSE_CLICKED, resetHandler);
		btnClose.removeEventHandler(MouseEvent.MOUSE_CLICKED, closeHandler);
		txtUsername.removeEventHandler(javafx.scene.input.KeyEvent.KEY_RELEASED, keyUsernameEventHandler);
		txtPassword.removeEventHandler(javafx.scene.input.KeyEvent.KEY_RELEASED, keyPasswordEventHandler);
		borderPaneLogin.removeEventHandler(javafx.scene.input.KeyEvent.KEY_RELEASED, keyEventHandler);
		btnLogin.removeEventHandler(MouseEvent.MOUSE_CLICKED, loginHandler);
		btnProceed.removeEventHandler(MouseEvent.MOUSE_CLICKED, proceedBtnHandler);

	}
}
