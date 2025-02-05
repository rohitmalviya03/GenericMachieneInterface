/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.cdac.reports.GeneratePatientReport;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import mediatorPattern.ControllerMediator;
import model.ChartImageModel;
import utility.DrawTabbedCenterForSnapshot;
import utility.GetSnapshot;

/**
 * This controller is used to generate snapshots of recorded Device data for
 * Report
 *
 * @author Sudeep_Sahoo
 *
 */
public class GetSnapshotController implements Initializable {

	@FXML
	private VBox vbMainContent;

	@FXML
	private Button btnClose;

	@FXML
	private Button btnGetdata;

	@FXML
	private Button btnGetSnapshot;

	@FXML
	private Button btnGetS5Data;

	@FXML
	private BorderPane borderpaneMain;

	@FXML
	private AnchorPane disablePane;

	public AnchorPane getDisablePane() {
		return disablePane;
	}

	public void setDisablePane(AnchorPane disablePane) {
		this.disablePane = disablePane;
	}

	private boolean isReportGenerated = false;

	public boolean isReportGenerated() {
		return isReportGenerated;
	}

	public void setReportGenerated(boolean isReportGenerated) {
		this.isReportGenerated = isReportGenerated;
	}

	private DrawTabbedCenterForSnapshot drawtabbedCentreSnapshot;
	private GetSnapshot getSnapShot = new GetSnapshot();

	public GetSnapshot getGetSnapShot() {
		return getSnapShot;
	}

	public void setGetSnapShot(GetSnapshot getSnapShot) {
		this.getSnapShot = getSnapShot;
	}

	private static final Logger LOG = Logger.getLogger(GetSnapshotController.class.getName());

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {

			if (ControllerMediator.getInstance().GetSnapshotController() == null) {
				ControllerMediator.getInstance().registerGetSnapshotController(this);
			}

			drawtabbedCentreSnapshot = ControllerMediator.getInstance().getMainController()
					.getDrawTabbedCenterForSnapshot();

			ControllerMediator.getInstance().GetSnapshotController().setReportGenerated(false);
			ControllerMediator.getInstance().getMainController().getDrawTabbedCenterForSnapshot().setS5Called(false);

			drawtabbedCentreSnapshot.setCurrentTab("anesthesia");

			getSnapShot.initializeContainer(drawtabbedCentreSnapshot);
			vbMainContent.setPrefSize(Screen.getPrimary().getVisualBounds().getWidth() * 0.99,
					Screen.getPrimary().getVisualBounds().getHeight() * 0.9);
			ControllerMediator.getInstance().getMainController().getDrawTabbedCenterForSnapshot()
					.setImageListForReport(new ArrayList<ChartImageModel>());

		} catch (Exception e) {
			LOG.error("## Exception occured:", e);
		}
	}

	/**
	 * This method is used to Initialize snapshot container
	 */
	public void initializeConatiner() {

		try {
			VBox mainContent = drawtabbedCentreSnapshot.getMainContainer();
			vbMainContent.getChildren().clear();
			vbMainContent.getChildren().add(mainContent);
			drawtabbedCentreSnapshot.getAllSnapshots();
		} catch (Exception e) {
			LOG.error("## Exception occured:", e);
		}

	}

	/**
	 * This method is used to close the window
	 */
	public void closePopup() {
		try {
			Main.getInstance().getStageManager().closeSecondaryStage();
		} catch (Exception e) {
			LOG.error("## Exception occured:", e);
		}

	}

	/**
	 * This method is used to call generate report method
	 */
	public void startGeneratingReport() {
		try {
			GeneratePatientReport generateReport = new GeneratePatientReport();
			generateReport.getPatientDetails();
		} catch (Exception e) {
			LOG.error("## Exception occured:", e);
		}

	}

}
