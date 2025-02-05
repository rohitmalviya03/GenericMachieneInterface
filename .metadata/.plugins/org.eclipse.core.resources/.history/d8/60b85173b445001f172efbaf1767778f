/*******************************************************************************
 * © 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import application.Main;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import mediatorPattern.ControllerMediator;
import model.AldreteScoreSession;

/**
 * This Controller is used to calculate Shift Out Aldrete score
 *
 * @author Sudeep_Sahoo
 *
 */
public class AldreteScoreController implements Initializable {

	private static final Logger LOGGER = Logger.getLogger(AldreteScoreController.class.getName());

	@FXML
	private Label lblTotalScore;

	@FXML
	private Button btnClose;

	@FXML
	private RadioButton radioActivity2;

	@FXML
	private ToggleGroup activity;

	@FXML
	private RadioButton radioActivity1;

	@FXML
	private RadioButton radioActivity0;

	@FXML
	private RadioButton radioCirculation2;

	@FXML
	private ToggleGroup circulation;

	@FXML
	private RadioButton radioCirculation1;

	@FXML
	private RadioButton radioCirculation0;

	@FXML
	private RadioButton radioOxygen2;

	@FXML
	private ToggleGroup oxygen;

	@FXML
	private RadioButton radioOxygen1;

	@FXML
	private RadioButton radioOxygen0;

	@FXML
	private RadioButton radioBreathing2;

	@FXML
	private ToggleGroup breathing;

	@FXML
	private RadioButton radioBreathing1;

	@FXML
	private RadioButton radioBreathing0;

	@FXML
	private RadioButton radioConsciousness2;

	@FXML
	private ToggleGroup consciousness;

	@FXML
	private RadioButton radioConsciousness1;

	@FXML
	private RadioButton radioConsciousness0;

	@FXML
	private Button btnSave;

	@FXML
	private Button btnReset;

	private int totalScore = 0;

	private String type;
	private String paramValues;

	private String activityParam;
	private String breathingParam;
	private String circulationParam;
	private String consciousnessParam;
	private String oxygenParam;

	private ChangeListener<Toggle> activityChangeListener;

	private ChangeListener<Toggle> breathingChangeListener;

	private ChangeListener<Toggle> circulationChangeListener;

	private ChangeListener<Toggle> consciousnessChangeListener;

	private ChangeListener<Toggle> oxygenChangeListener;

	private EventHandler<MouseEvent> btnCloseHandler;

	private EventHandler<MouseEvent> btnSaveHandler;

	private EventHandler<MouseEvent> btnResetHandler;

	/**
	 * This method initializes the all Event handlers and Default values on page
	 * load
	 */

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		// LOGGER.info("Reached -> " + LOGGER.getName());
		try {

			radioActivity0.setUserData("radioActivity0");
			radioActivity1.setUserData("radioActivity1");
			radioActivity2.setUserData("radioActivity2");

			radioActivity0.setToggleGroup(activity);
			radioActivity1.setToggleGroup(activity);
			radioActivity2.setToggleGroup(activity);

			radioBreathing0.setUserData("radioBreathing0");
			radioBreathing1.setUserData("radioBreathing1");
			radioBreathing2.setUserData("radioBreathing2");

			radioBreathing0.setToggleGroup(breathing);
			radioBreathing1.setToggleGroup(breathing);
			radioBreathing2.setToggleGroup(breathing);

			radioCirculation0.setUserData("radioCirculation0");
			radioCirculation1.setUserData("radioCirculation1");
			radioCirculation2.setUserData("radioCirculation2");

			radioCirculation0.setToggleGroup(circulation);
			radioCirculation1.setToggleGroup(circulation);
			radioCirculation2.setToggleGroup(circulation);

			radioConsciousness0.setUserData("radioConsciousness0");
			radioConsciousness1.setUserData("radioConsciousness1");
			radioConsciousness2.setUserData("radioConsciousness2");

			radioConsciousness0.setToggleGroup(consciousness);
			radioConsciousness1.setToggleGroup(consciousness);
			radioConsciousness2.setToggleGroup(consciousness);

			radioOxygen0.setUserData("radioOxygen0");
			radioOxygen1.setUserData("radioOxygen1");
			radioOxygen2.setUserData("radioOxygen2");

			radioOxygen0.setToggleGroup(oxygen);
			radioOxygen1.setToggleGroup(oxygen);
			radioOxygen2.setToggleGroup(oxygen);

			activityChangeListener = new ChangeListener<Toggle>() {

				@Override
				public void changed(ObservableValue<? extends Toggle> observable, Toggle old_toggle,
						Toggle new_toggle) {
					if (new_toggle != null) {

						System.out.println(new_toggle.getUserData().toString());
						if (new_toggle.getUserData().toString().equalsIgnoreCase("radioActivity0")) {
							totalScore += 0;
							if (old_toggle != null) {
								if (old_toggle.getUserData().toString().equalsIgnoreCase("radioActivity1")) {
									totalScore -= 1;
								} else if (old_toggle.getUserData().toString().equalsIgnoreCase("radioActivity2")) {
									totalScore -= 2;
								}
							}

							activityParam = 0 + "";

						} else if (new_toggle.getUserData().toString().equalsIgnoreCase("radioActivity1")) {
							totalScore += 1;
							if (old_toggle != null) {
								if (old_toggle.getUserData().toString().equalsIgnoreCase("radioActivity2")) {
									totalScore -= 2;
								}

							}
							activityParam = 1 + "";

						} else if (new_toggle.getUserData().toString().equalsIgnoreCase("radioActivity2")) {
							totalScore += 2;
							if (old_toggle != null) {

								if (old_toggle.getUserData().toString().equalsIgnoreCase("radioActivity1")) {
									totalScore -= 1;
								}
							}
							activityParam = 2 + "";

						}
						lblTotalScore.setText(totalScore + "");
					}
				}

			};
			activity.selectedToggleProperty().addListener(activityChangeListener);

			breathingChangeListener = new ChangeListener<Toggle>() {

				@Override
				public void changed(ObservableValue<? extends Toggle> observable, Toggle old_toggle,
						Toggle new_toggle) {
					if (new_toggle != null) {

						System.out.println(new_toggle.getUserData().toString());
						if (new_toggle.getUserData().toString().equalsIgnoreCase("radioBreathing0")) {
							totalScore += 0;
							if (old_toggle != null) {
								if (old_toggle.getUserData().toString().equalsIgnoreCase("radioBreathing1")) {
									totalScore -= 1;
								} else if (old_toggle.getUserData().toString().equalsIgnoreCase("radioBreathing2")) {
									totalScore -= 2;
								}
							}
							breathingParam = 0 + "";

						} else if (new_toggle.getUserData().toString().equalsIgnoreCase("radioBreathing1")) {
							totalScore += 1;
							if (old_toggle != null) {
								if (old_toggle.getUserData().toString().equalsIgnoreCase("radioBreathing2")) {
									totalScore -= 2;
								}

							}
							breathingParam = 1 + "";
						} else if (new_toggle.getUserData().toString().equalsIgnoreCase("radioBreathing2")) {
							totalScore += 2;
							if (old_toggle != null) {

								if (old_toggle.getUserData().toString().equalsIgnoreCase("radioBreathing1")) {
									totalScore -= 1;
								}
							}
							breathingParam = 2 + "";
						}
						lblTotalScore.setText(totalScore + "");
					}
				}

			};

			breathing.selectedToggleProperty().addListener(breathingChangeListener);

			circulationChangeListener = new ChangeListener<Toggle>() {

				@Override
				public void changed(ObservableValue<? extends Toggle> observable, Toggle old_toggle,
						Toggle new_toggle) {
					if (new_toggle != null) {

						System.out.println(new_toggle.getUserData().toString());
						if (new_toggle.getUserData().toString().equalsIgnoreCase("radioCirculation0")) {
							totalScore += 0;
							if (old_toggle != null) {
								if (old_toggle.getUserData().toString().equalsIgnoreCase("radioCirculation1")) {
									totalScore -= 1;
								} else if (old_toggle.getUserData().toString().equalsIgnoreCase("radioCirculation2")) {
									totalScore -= 2;
								}
							}
							circulationParam = 0 + "";

						} else if (new_toggle.getUserData().toString().equalsIgnoreCase("radioCirculation1")) {
							totalScore += 1;
							if (old_toggle != null) {
								if (old_toggle.getUserData().toString().equalsIgnoreCase("radioCirculation2")) {
									totalScore -= 2;
								}

							}
							circulationParam = 1 + "";
						} else if (new_toggle.getUserData().toString().equalsIgnoreCase("radioCirculation2")) {
							totalScore += 2;
							if (old_toggle != null) {

								if (old_toggle.getUserData().toString().equalsIgnoreCase("radioCirculation1")) {
									totalScore -= 1;
								}
							}
							circulationParam = 2 + "";

						}
						lblTotalScore.setText(totalScore + "");
					}
				}

			};

			circulation.selectedToggleProperty().addListener(circulationChangeListener);

			consciousnessChangeListener = new ChangeListener<Toggle>() {

				@Override
				public void changed(ObservableValue<? extends Toggle> observable, Toggle old_toggle,
						Toggle new_toggle) {
					if (new_toggle != null) {

						System.out.println(new_toggle.getUserData().toString());
						if (new_toggle.getUserData().toString().equalsIgnoreCase("radioConsciousness0")) {
							totalScore += 0;
							if (old_toggle != null) {
								if (old_toggle.getUserData().toString().equalsIgnoreCase("radioConsciousness1")) {
									totalScore -= 1;
								} else if (old_toggle.getUserData().toString()
										.equalsIgnoreCase("radioConsciousness2")) {
									totalScore -= 2;
								}
							}
							consciousnessParam = 0 + "";

						} else if (new_toggle.getUserData().toString().equalsIgnoreCase("radioConsciousness1")) {
							totalScore += 1;
							if (old_toggle != null) {
								if (old_toggle.getUserData().toString().equalsIgnoreCase("radioConsciousness2")) {
									totalScore -= 2;
								}

							}
							consciousnessParam = 1 + "";
						} else if (new_toggle.getUserData().toString().equalsIgnoreCase("radioConsciousness2")) {
							totalScore += 2;
							if (old_toggle != null) {

								if (old_toggle.getUserData().toString().equalsIgnoreCase("radioConsciousness1")) {
									totalScore -= 1;
								}
							}
							consciousnessParam = 2 + "";
						}
						lblTotalScore.setText(totalScore + "");
					}
				}

			};

			consciousness.selectedToggleProperty().addListener(consciousnessChangeListener);

			oxygenChangeListener = new ChangeListener<Toggle>() {

				@Override
				public void changed(ObservableValue<? extends Toggle> observable, Toggle old_toggle,
						Toggle new_toggle) {
					if (new_toggle != null) {

						System.out.println(new_toggle.getUserData().toString());
						if (new_toggle.getUserData().toString().equalsIgnoreCase("radioOxygen0")) {
							totalScore += 0;
							if (old_toggle != null) {
								if (old_toggle.getUserData().toString().equalsIgnoreCase("radioOxygen1")) {
									totalScore -= 1;
								} else if (old_toggle.getUserData().toString().equalsIgnoreCase("radioOxygen2")) {
									totalScore -= 2;
								}
							}
							oxygenParam = 0 + "";
						} else if (new_toggle.getUserData().toString().equalsIgnoreCase("radioOxygen1")) {
							totalScore += 1;
							if (old_toggle != null) {
								if (old_toggle.getUserData().toString().equalsIgnoreCase("radioOxygen2")) {
									totalScore -= 2;
								}

							}
							oxygenParam = 1 + "";
						} else if (new_toggle.getUserData().toString().equalsIgnoreCase("radioOxygen2")) {
							totalScore += 2;
							if (old_toggle != null) {

								if (old_toggle.getUserData().toString().equalsIgnoreCase("radioOxygen1")) {
									totalScore -= 1;
								}
							}
							oxygenParam = 2 + "";
						}

						lblTotalScore.setText(totalScore + "");
					}
				}

			};

			oxygen.selectedToggleProperty().addListener(oxygenChangeListener);

			btnCloseHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent event) {
					try {
						closePopup();
					} catch (Exception e1) {
						LOGGER.error("## Exception occured:", e1);
					}
				}
			};
			btnClose.addEventHandler(MouseEvent.MOUSE_CLICKED, btnCloseHandler);

			btnSaveHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent event) {
					paramValues = activityParam + "|" + breathingParam + "|" + circulationParam + "|"
							+ consciousnessParam + "|" + oxygenParam;
					// if (type.equalsIgnoreCase("PACU")) {
					// TextField PACUTotalScore = (TextField)
					// Main.getInstance().getParentScene().lookup("#txtPACUAlderateScore");
					TextField PACUTotalScore = ControllerMediator.getInstance().getPostOperativeController()
							.getTxtPACUAlderateScore();
					if (!lblTotalScore.getText().trim().equalsIgnoreCase("--")) {
						PACUTotalScore.setText(totalScore + "");
						AldreteScoreSession.getInstance().setPACUTotalScore(totalScore);
					} else {
						PACUTotalScore.setText("");
						AldreteScoreSession.getInstance().setPACUTotalScore(-1);
					}

					AldreteScoreSession.getInstance().setPACUparams(paramValues);

					// } else if (type.equalsIgnoreCase("ShiftingOut")) {
					// TextField PACUTotalScore = (TextField)
					// Main.getInstance().getParentScene()
					// .lookup("#txtOutAlderateScore");
					// PACUTotalScore.setText(totalScore + "");
					// AldreteScoreSession.getInstance().setShiftingOutParams(paramValues);
					// AldreteScoreSession.getInstance().setShiftingOutTotal(totalScore);
					// }

					AldreteScoreSession.getInstance().setSelectedType("");
					try {
						closePopup();
					} catch (Exception e1) {
						LOGGER.error("## Exception occured:", e1);
					}
				}
			};
			btnSave.addEventHandler(MouseEvent.MOUSE_CLICKED, btnSaveHandler);

			if (AldreteScoreSession.getInstance() != null) {

				type = AldreteScoreSession.getInstance().getSelectedType();

				if (type.equalsIgnoreCase("PACU")) {
					paramValues = AldreteScoreSession.getInstance().getPACUparams();
					if (AldreteScoreSession.getInstance().getPACUTotalScore() == -1) {
						lblTotalScore.setText("--");
					} else {
						lblTotalScore.setText(totalScore + "");
					}
					// totalScore =
					// AldreteScoreSession.getInstance().getPACUTotalScore();
				}
				// else if (type.equalsIgnoreCase("ShiftingOut")) {
				// paramValues =
				// AldreteScoreSession.getInstance().getShiftingOutParams();
				// // totalScore =
				// // AldreteScoreSession.getInstance().getShiftingOutTotal();
				// }

				if (paramValues != null) {
					String paramArr[] = paramValues.split("\\|");

					if (paramArr.length != 0) {
						if (paramArr[0].equalsIgnoreCase("2")) {
							activity.selectToggle(radioActivity2);

						} else if (paramArr[0].equalsIgnoreCase("1")) {
							activity.selectToggle(radioActivity1);

						} else if (paramArr[0].equalsIgnoreCase("0")) {
							activity.selectToggle(radioActivity0);

						}

						if (paramArr[1].equalsIgnoreCase("2")) {
							breathing.selectToggle(radioBreathing2);
						} else if (paramArr[1].equalsIgnoreCase("1")) {
							breathing.selectToggle(radioBreathing1);
						} else if (paramArr[1].equalsIgnoreCase("0")) {
							breathing.selectToggle(radioBreathing0);
						}

						if (paramArr[2].equalsIgnoreCase("2")) {
							circulation.selectToggle(radioCirculation2);
						} else if (paramArr[2].equalsIgnoreCase("1")) {
							circulation.selectToggle(radioCirculation1);
						} else if (paramArr[2].equalsIgnoreCase("0")) {
							circulation.selectToggle(radioCirculation0);
						}

						if (paramArr[3].equalsIgnoreCase("2")) {
							consciousness.selectToggle(radioConsciousness2);
						} else if (paramArr[3].equalsIgnoreCase("1")) {
							consciousness.selectToggle(radioConsciousness1);
						} else if (paramArr[3].equalsIgnoreCase("0")) {
							consciousness.selectToggle(radioConsciousness0);
						}

						if (paramArr[4].equalsIgnoreCase("2")) {
							oxygen.selectToggle(radioOxygen2);
						} else if (paramArr[4].equalsIgnoreCase("1")) {
							oxygen.selectToggle(radioOxygen1);
						} else if (paramArr[4].equalsIgnoreCase("0")) {
							oxygen.selectToggle(radioOxygen0);
						}
					}

				} else {
					activity.selectToggle(null);
					breathing.selectToggle(null);
					circulation.selectToggle(null);
					consciousness.selectToggle(null);
					oxygen.selectToggle(null);
					totalScore = 0;
					lblTotalScore.setText("--");
				}

			}

			btnResetHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent event) {
					activity.selectToggle(null);
					breathing.selectToggle(null);
					circulation.selectToggle(null);
					consciousness.selectToggle(null);
					oxygen.selectToggle(null);
					totalScore = 0;
					lblTotalScore.setText("--");
					// paramValues=null;
					activityParam = "--";
					breathingParam = "--";
					circulationParam = "--";
					consciousnessParam = "--";
					oxygenParam = "--";
					// paramValues = activityParam + "|" + breathingParam + "|"
					// + circulationParam + "|"
					// + consciousnessParam + "|" + oxygenParam;
					// AldreteScoreSession.getInstance().setPACUparams(paramValues);

				}
			};
			btnReset.addEventHandler(MouseEvent.MOUSE_CLICKED, btnResetHandler);

		} catch (Exception e) {
			LOGGER.error("## Exception occured:" + e);
			Main.getInstance().getUtility().showNotification("Error", "Error", "Something went wrong");
		}

	}

	/**
	 * This method is used to remove all listeners
	 *
	 */
	private void removeEventHandlers() {
		try {
			activity.selectedToggleProperty().removeListener(activityChangeListener);
			breathing.selectedToggleProperty().removeListener(breathingChangeListener);
			circulation.selectedToggleProperty().removeListener(circulationChangeListener);
			consciousness.selectedToggleProperty().removeListener(consciousnessChangeListener);
			oxygen.selectedToggleProperty().removeListener(oxygenChangeListener);
			btnClose.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnCloseHandler);
			btnSave.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnSaveHandler);
			btnReset.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnResetHandler);

		} catch (Exception e) {
			LOGGER.error("## Exception occured:" + e);
		}

	}

	/**
	 * This method is used to close the Window
	 *
	 * @throws Exception
	 */
	private void closePopup() throws Exception {
		LOGGER.debug("Logger Name: " + LOGGER.getName());
		try {
			removeEventHandlers();
			Main.getInstance().getStageManager().closeTertiaryStage();

		} catch (Exception e) {
			LOGGER.error("## Exception occured:" + e.getMessage());
			Main.getInstance().getUtility().showNotification("Error", "Error",
					"Something went wrong during closing the Window.");
		}

	}

}
