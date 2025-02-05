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
import java.util.List;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.cdac.common.GeneratedEntities.IcuPlanEntity;
import com.cdac.common.util.Validations;

import application.Main;
import javafx.fxml.Initializable;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import model.PatientSession;
import services.GetIcuPlanReadingsService;
import services.IcuPlanSaveService;

/**
 * @author rohi.bhardwaj
 *
 */
public class IcuPlanController implements Initializable {

	private static final Logger LOGGER = Logger.getLogger(IcuPlanController.class.getName());

	@FXML
	private Button btnClose;

	@FXML
	private ChoiceBox<String> choiceExtubation;

	@FXML
	private ChoiceBox<String> choicePostExtubation;

	@FXML
	private ChoiceBox<String> choiceSedation;

	@FXML
	private ChoiceBox<String> choiceAnalgesia;

	@FXML
	private ChoiceBox<String> choiceParalysis;

	@FXML
	private TextField txtMao;

	@FXML
	private TextField txtCvp;

	@FXML
	private TextField txtHr;

	@FXML
	private TextField txtSpO2;

	@FXML
	private TextField txtRlRate;

	@FXML
	private TextField txtDns;

	@FXML
	private ChoiceBox<String> choiceDrl;

	@FXML
	private ChoiceBox<String> choiceInvestigations;

	@FXML
	private TextField txtNurseName;

	@FXML
	private Label lblError;

	@FXML
	private Button btnSave;

	@FXML
	private AnchorPane disablePaneMainContent;

	private long caseId = 0;
	private Integer icuPlanId = 0;
	private IcuPlanEntity icuPlanReadings = new IcuPlanEntity();

	private List<String> extubationList = new ArrayList<String>();
	private List<String> postExtubationList = new ArrayList<String>();
	private List<String> sedationList = new ArrayList<String>();
	private List<String> analgesiaList = new ArrayList<String>();
	private List<String> paralysisList = new ArrayList<String>();
	private List<String> drlList = new ArrayList<String>();
	private List<String> investigationsList = new ArrayList<String>();

	private EventHandler<MouseEvent> btnSaveHandler;
	private EventHandler<MouseEvent> btnCloseHandler;
	private EventHandler<WorkerStateEvent> icuPlansaveServiceSuccessHandler;
	private EventHandler<WorkerStateEvent> icuPlansaveServiceFailedHandler;
	private EventHandler<WorkerStateEvent> getIcuPlanDetailsServiceSuccessHandler;
	private EventHandler<WorkerStateEvent> getIcuPlanDetailsServiceFailedHandler;

	@Override
	public void initialize(URL location, ResourceBundle resource) {

		if (PatientSession.getInstance().getPatientCase() != null) {
			caseId = PatientSession.getInstance().getPatientCase().getCaseId();

		}

		extubationList.add("Select");
		extubationList.add("Within 2 hr");
		extubationList.add("Keep Sedated and extubate < 6 hrs");
		extubationList.add("> 6hrs");
		extubationList.add("Ventilate overnight");

		postExtubationList.add("Select");
		postExtubationList.add("Yes");
		postExtubationList.add("No");

		sedationList.add("Select");
		sedationList.add("Propofol");
		sedationList.add("Dexmed");
		sedationList.add("Others");

		analgesiaList.add("Select");
		analgesiaList.add("Morphine");
		analgesiaList.add("Fentanyl");
		analgesiaList.add("Paracetamol");
		analgesiaList.add("Others");

		paralysisList.add("Select");
		paralysisList.add("Yes");
		paralysisList.add("No");

		drlList.add("Select");
		drlList.add("5%D");
		drlList.add("Plasmalyte");

		investigationsList.add("Select");
		investigationsList.add("Xray");
		investigationsList.add("EKG");
		investigationsList.add("Hb");
		investigationsList.add("Coag");
		investigationsList.add("LFT");
		investigationsList.add("RFT");

		choiceExtubation.getItems().clear();
		choiceExtubation.getItems().addAll(extubationList);
		choiceExtubation.getSelectionModel().select(0);

		choicePostExtubation.getItems().clear();
		choicePostExtubation.getItems().addAll(postExtubationList);
		choicePostExtubation.getSelectionModel().select(0);

		choiceSedation.getItems().clear();
		choiceSedation.getItems().addAll(sedationList);
		choiceSedation.getSelectionModel().select(0);

		choiceAnalgesia.getItems().clear();
		choiceAnalgesia.getItems().addAll(analgesiaList);
		choiceAnalgesia.getSelectionModel().select(0);

		choiceParalysis.getItems().clear();
		choiceParalysis.getItems().addAll(paralysisList);
		choiceParalysis.getSelectionModel().select(0);

		choiceDrl.getItems().clear();
		choiceDrl.getItems().addAll(drlList);
		choiceDrl.getSelectionModel().select(0);

		choiceInvestigations.getItems().clear();
		choiceInvestigations.getItems().addAll(investigationsList);
		choiceInvestigations.getSelectionModel().select(0);

		btnSaveHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
			public void handle(javafx.scene.input.MouseEvent event) {
				try {
					save();
				} catch (Exception e1) {
					LOGGER.error("## Exception occured:", e1);
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

		getIcuPlanDetails();

	}

	private boolean validateReadings() {
		if ((txtMao.getText().trim().isEmpty() && txtMao.getText().trim().isEmpty())
				&& (txtMao.getText().trim().isEmpty() && txtMao.getText().trim().isEmpty())
				&& (txtCvp.getText().trim().isEmpty() && txtCvp.getText().trim().isEmpty())
				&& (txtHr.getText().trim().isEmpty() && txtMao.getText().trim().isEmpty())
				&& (txtSpO2.getText().trim().isEmpty() && txtSpO2.getText().trim().isEmpty())
				&& (txtRlRate.getText().trim().isEmpty() && txtRlRate.getText().trim().isEmpty())
				&& (txtDns.getText().trim().isEmpty() && txtDns.getText().trim().isEmpty())
				&& (txtNurseName.getText().trim().isEmpty() && txtNurseName.getText().trim().isEmpty())
				&& choiceExtubation.getSelectionModel().getSelectedIndex() == 0
				&& choicePostExtubation.getSelectionModel().getSelectedIndex() == 0
				&& choiceSedation.getSelectionModel().getSelectedIndex() == 0
				&& choiceAnalgesia.getSelectionModel().getSelectedIndex() == 0
				&& choiceParalysis.getSelectionModel().getSelectedIndex() == 0
				&& choiceDrl.getSelectionModel().getSelectedIndex() == 0
				&& choiceInvestigations.getSelectionModel().getSelectedIndex() == 0) {
			lblError.setText("*Please enter atleast one field to save.");
			return false;
		}
		if (txtMao.getText() != null && !txtMao.getText().trim().isEmpty()
				&& !Validations.decimalsUpto2places3digit(txtMao.getText())) {
			lblError.setText("*Please enter a valid Mao value upto 3 digits and upto 2 decimal only.");
			return false;
		}
		if (txtCvp.getText() != null && !txtCvp.getText().trim().isEmpty()
				&& !Validations.decimalsUpto2places3digit(txtCvp.getText())) {
			lblError.setText("*Please enter a valid CVP value upto 3 digits and upto 2 decimal only.");
			return false;
		}
		if (txtHr.getText() != null && !txtHr.getText().trim().isEmpty()
				&& !Validations.decimalsUpto2places3digit(txtHr.getText())) {
			lblError.setText("*Please enter a valid HR value upto 3 digits and upto 2 decimal only.");
			return false;
		}
		if (txtSpO2.getText() != null && !txtSpO2.getText().trim().isEmpty()
				&& !Validations.decimalsUpto2places3digit(txtSpO2.getText())) {
			lblError.setText("*Please enter a valid SpO2 value upto 3 digits and upto 2 decimal only.");
			return false;
		}
		if (txtRlRate.getText() != null && !txtRlRate.getText().trim().isEmpty()
				&& !Validations.decimalsUpto2places3digit(txtRlRate.getText())) {
			lblError.setText("*Please enter a valid RL Rate value upto 3 digits and upto 2 decimal only.");
			return false;
		}
		if (txtDns.getText() != null && !txtDns.getText().trim().isEmpty()
				&& !Validations.decimalsUpto2places3digit(txtDns.getText())) {
			lblError.setText("*Please enter a valid DNS Rate value upto 3 digits and upto 2 decimal only.");
			return false;
		}

		return true;
	}

	private void save() throws Exception {
		try {
			if (validateReadings()) {
				lblError.setVisible(false);
				disablePaneMainContent.setVisible(true);
				if (icuPlanId != 0) {
					icuPlanReadings.setIcuPlanId(icuPlanId);
				}

				if (caseId != 0) {
					icuPlanReadings.setCaseId(caseId);
				}

				if (choiceExtubation.getSelectionModel().getSelectedIndex() != 0) {
					icuPlanReadings.setExtubation(choiceExtubation.getSelectionModel().getSelectedItem());
				}

				if (choicePostExtubation.getSelectionModel().getSelectedIndex() != 0) {
					icuPlanReadings.setPostExtubation(choicePostExtubation.getSelectionModel().getSelectedItem());
				}

				if (choiceSedation.getSelectionModel().getSelectedIndex() != 0) {
					icuPlanReadings.setSedation(choiceSedation.getSelectionModel().getSelectedItem());
				}

				if (choiceAnalgesia.getSelectionModel().getSelectedIndex() != 0) {
					icuPlanReadings.setAnalgesia(choiceAnalgesia.getSelectionModel().getSelectedItem());
				}

				if (choiceParalysis.getSelectionModel().getSelectedIndex() != 0) {
					icuPlanReadings.setParalysis(choiceParalysis.getSelectionModel().getSelectedItem());
				}

				if (choiceDrl.getSelectionModel().getSelectedIndex() != 0) {
					icuPlanReadings.setDrl(choiceDrl.getSelectionModel().getSelectedItem());
				}

				if (choiceInvestigations.getSelectionModel().getSelectedIndex() != 0) {
					icuPlanReadings.setInvestigations(choiceInvestigations.getSelectionModel().getSelectedItem());
				}

				if (!txtMao.getText().isEmpty()) {
					icuPlanReadings.setMao(new BigDecimal(txtMao.getText()));
				}

				if (!txtCvp.getText().isEmpty()) {
					icuPlanReadings.setCvp(new BigDecimal(txtCvp.getText()));
				}

				if (!txtHr.getText().isEmpty()) {
					icuPlanReadings.setHr(new BigDecimal(txtHr.getText()));
				}

				if (!txtSpO2.getText().isEmpty()) {
					icuPlanReadings.setSpO2(new BigDecimal(txtSpO2.getText()));
				}

				if (!txtRlRate.getText().isEmpty()) {
					icuPlanReadings.setRlRate(new BigDecimal(txtRlRate.getText()));
				}

				if (!txtDns.getText().isEmpty()) {
					icuPlanReadings.setDnsRate(new BigDecimal(txtDns.getText()));
				}

				if (!txtNurseName.getText().isEmpty()) {
					icuPlanReadings.setNurseName(txtNurseName.getText());
				}

				IcuPlanSaveService icuPlansaveService = IcuPlanSaveService.getInstance(icuPlanReadings);
				icuPlansaveService.restart();

				icuPlansaveServiceSuccessHandler = new EventHandler<WorkerStateEvent>() {

					@Override
					public void handle(WorkerStateEvent event) {

						disablePaneMainContent.setVisible(false);
						closePopup();
						icuPlansaveService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
								icuPlansaveServiceSuccessHandler);
						icuPlansaveService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
								icuPlansaveServiceFailedHandler);
						if (icuPlanId != 0) {
							Main.getInstance().getUtility().showNotification("Information", "Success",
									"ICU Plan Details updated successfully!");
						} else {

							Main.getInstance().getUtility().showNotification("Information", "Success",
									"ICU Plan saved successfully!");
						}
					}
				};

				icuPlansaveService.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
						icuPlansaveServiceSuccessHandler);

				icuPlansaveServiceFailedHandler = new EventHandler<WorkerStateEvent>() {

					@Override
					public void handle(WorkerStateEvent event) {

						disablePaneMainContent.setVisible(false);
						icuPlansaveService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
								icuPlansaveServiceSuccessHandler);
						icuPlansaveService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
								icuPlansaveServiceFailedHandler);
						Main.getInstance().getUtility().showNotification("Error", "Error",
								icuPlansaveService.getException().getMessage());
					}
				};

				icuPlansaveService.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
						icuPlansaveServiceFailedHandler);
			} else {
				lblError.setVisible(true);
				disablePaneMainContent.setVisible(false);
			}
		} catch (Exception e) {
			LOGGER.error("## Exception occured:" + e.getMessage());

		}

	}

	private void closePopup() {
		try {

			removeListeners();
			Main.getInstance().getStageManager().closeSecondaryStage();

		} catch (Exception e) {
			LOGGER.error("## Exception occured:" + e);
		}

	}

	private void removeListeners() {
		btnSave.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnSaveHandler);
		btnClose.removeEventHandler(MouseEvent.MOUSE_CLICKED, btnCloseHandler);
	}

	private void getIcuPlanDetails() {
		disablePaneMainContent.setVisible(true);

		GetIcuPlanReadingsService getIcuPlanReadingsService = GetIcuPlanReadingsService.getInstance(caseId);
		getIcuPlanReadingsService.restart();

		// ---on service success

		getIcuPlanDetailsServiceSuccessHandler = new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent event) {

				IcuPlanEntity icuplanRecord = getIcuPlanReadingsService.getIcuPlanRecord();

				if (icuplanRecord.getIcuPlanId() != null) {
					icuPlanId = icuplanRecord.getIcuPlanId();
					if (icuplanRecord.getMao() != null) {
						txtMao.setText(icuplanRecord.getMao() + "");
					}
					if (icuplanRecord.getCvp() != null) {
						txtCvp.setText(icuplanRecord.getCvp() + "");
					}
					if (icuplanRecord.getHr() != null) {
						txtHr.setText(icuplanRecord.getHr() + "");
					}
					if (icuplanRecord.getSpO2() != null) {
						txtSpO2.setText(icuplanRecord.getSpO2() + "");
					}
					if (icuplanRecord.getRlRate() != null) {
						txtRlRate.setText(icuplanRecord.getRlRate() + "");
					}
					if (icuplanRecord.getDnsRate() != null) {
						txtDns.setText(icuplanRecord.getDnsRate() + "");
					}
					if (icuplanRecord.getNurseName() != null) {
						txtNurseName.setText(icuplanRecord.getNurseName() + "");
					}

					if (icuplanRecord.getExtubation() != null) {
						choiceExtubation.getSelectionModel().select(icuplanRecord.getExtubation());
					}
					if (icuplanRecord.getPostExtubation() != null) {
						choicePostExtubation.getSelectionModel().select(icuplanRecord.getPostExtubation());
					}
					if (icuplanRecord.getSedation() != null) {
						choiceSedation.getSelectionModel().select(icuplanRecord.getSedation());
					}
					if (icuplanRecord.getAnalgesia() != null) {
						choiceAnalgesia.getSelectionModel().select(icuplanRecord.getAnalgesia());
					}
					if (icuplanRecord.getParalysis() != null) {
						choiceParalysis.getSelectionModel().select(icuplanRecord.getParalysis());
					}
					if (icuplanRecord.getInvestigations() != null) {
						choiceInvestigations.getSelectionModel().select(icuplanRecord.getInvestigations());
					}
					if (icuplanRecord.getDrl() != null) {
						choiceDrl.getSelectionModel().select(icuplanRecord.getDrl());
					}

				}

				disablePaneMainContent.setVisible(false);

				getIcuPlanReadingsService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
						getIcuPlanDetailsServiceSuccessHandler);
				getIcuPlanReadingsService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
						getIcuPlanDetailsServiceFailedHandler);

			}
		};

		getIcuPlanReadingsService.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
				getIcuPlanDetailsServiceSuccessHandler);

		getIcuPlanDetailsServiceFailedHandler = new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent event) {

				disablePaneMainContent.setVisible(false);
				getIcuPlanReadingsService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
						getIcuPlanDetailsServiceSuccessHandler);
				getIcuPlanReadingsService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
						getIcuPlanDetailsServiceFailedHandler);
				Main.getInstance().getUtility().showNotification("Error", "Error",
						getIcuPlanReadingsService.getException().getMessage());
			}
		};

		getIcuPlanReadingsService.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
				getIcuPlanDetailsServiceFailedHandler);

	}
}
