/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.cdac.framework.SystemConfiguration;

import application.FxmlView;
import application.Main;
import javafx.application.Platform;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import services.InitilizationService;

/**
 * SplashScreenController.java<br>
 * Purpose:This class contains logic to handle Splash screen inputs
 *
 * @author Rohit_Sharma41
 *
 */
public class SplashScreenController implements Initializable {

	@FXML
	private Label lblAppVersion;

	private EventHandler<WorkerStateEvent> initializeSuccessHandler;
	private EventHandler<WorkerStateEvent> initializeFailedHandler;
	private static final Logger LOG = Logger.getLogger(SplashScreenController.class.getName());

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			String appDetails = SystemConfiguration.getInstance().getAppVersion();
			lblAppVersion.setText(appDetails);
			callInitilizeServerService();
		} catch (Exception e) {
			LOG.error("## Exception occured:", e);

		}
	}

	/**
	 * This method calls a service which initializes server
	 * 
	 * @throws Exception
	 */
	private void callInitilizeServerService() throws Exception
	{
		try {

			InitilizationService initilizeServerService = new InitilizationService();
			initilizeServerService.restart();

			initializeSuccessHandler = new EventHandler<WorkerStateEvent>() {

				@Override
				public void handle(WorkerStateEvent event) {

					try
					{
						Main.getInstance().getStageManager().switchScene(FxmlView.LOGIN);
						initilizeServerService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
						        initializeFailedHandler);
						initilizeServerService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
						        initializeSuccessHandler);
						Main.getInstance().getUtility().showNotification("Information", "Information",
						        "Connected to Server successfully.");
					}
					catch (Exception e)
					{
						LOG.error("## Exception occured:", e);

					}

				}

			};

			initilizeServerService.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, initializeSuccessHandler);

			initializeFailedHandler = new EventHandler<WorkerStateEvent>() {

				@Override
				public void handle(WorkerStateEvent event) {

					try
					{
						initilizeServerService.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
						        initializeFailedHandler);
						initilizeServerService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
						        initializeSuccessHandler);

						Main.getInstance().getUtility().warningDialogue("Information",
						        "Unable to connect server. Please contact Administrator.");
						Platform.exit();
						System.exit(0);
					}
					catch (Exception e)
					{
						LOG.error("## Exception occured:", e);
					}
				}

			};

			initilizeServerService.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED, initializeFailedHandler);

		}
		catch (Exception e)
		{
			LOG.error("## Exception occured:", e);
			Main.getInstance().getUtility().showNotification("Error", "Error", "Something went wrong");
		}
	}
}
