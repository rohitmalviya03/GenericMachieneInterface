/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package controllers;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.cdac.framework.FwkServices;
import com.cdac.framework.PortManagerAndListener;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.input.MouseEvent;

public class FwkServicesController implements Initializable
{

	@FXML
	private Button btnClose;

	@FXML
	private ChoiceBox<String> choiceBoxDevice;

	@FXML
	private ChoiceBox<String> choiceBoxPort;

	private final Logger LOG = Logger.getLogger(FwkServicesController.class);

	private EventHandler<MouseEvent> btnCloseHandler;

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		LOG.info("Reached -> " + LOG.getName());

	//	FwkServices.getInstance().startIdentifyingDevicesAutomatically();

		ObservableList<String> availableDevicesList=FXCollections.observableArrayList();
		availableDevicesList=FXCollections.observableArrayList(FwkServices.getInstance().getAllSupportedDeviceNames());
		availableDevicesList.add("Select");
		choiceBoxDevice.setItems(availableDevicesList);
		choiceBoxDevice.getSelectionModel().select("Select");

		ObservableList<String> availablePortList = FXCollections.observableArrayList("Select");
		List<PortManagerAndListener> allPorts = FwkServices.getInstance().getPortDeviceMapping();
		for (PortManagerAndListener obj : allPorts)
		{
			availablePortList.add(obj.getPortName());
		}
		choiceBoxPort.setItems(availablePortList);
		choiceBoxPort.getSelectionModel().select(0);




		for (PortManagerAndListener obj : allPorts)
		{	
			if (obj.getDeviceListeningToThisPort() != null)
			{
				System.out.println("PORT NAME ::: " + obj.getPortName() + " | ATTACHED DEVICE ::: "
				        + obj.getDeviceListeningToThisPort().getNameOfDevice());
			}
		}


		btnCloseHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
			public void handle(javafx.scene.input.MouseEvent event) {
				btnClose.removeEventHandler(MouseEvent.MOUSE_PRESSED, btnCloseHandler);
//				Stage stage = Main.getInstance().getChildStage();
//				stage.close();
				Main.getInstance().getStageManager().closeSecondaryStage();
			}
		};
		btnClose.addEventHandler(MouseEvent.MOUSE_PRESSED, btnCloseHandler);

	}
}
