/*******************************************************************************
 * © 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package model;

import java.util.Arrays;
import java.util.List;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class DemoTesting extends Application
{

	final ListView<String> selectedItems = new ListView<>();

	@Override
	public void start(Stage primaryStage)
	{
		// final MenuButton choices = new MenuButton("Obst");
		final ComboBox<CheckMenuItem> selectCombobox = new ComboBox<CheckMenuItem>();

		final List<CheckMenuItem> items = (List) Arrays.asList(new CheckMenuItem("Apfel"), new CheckMenuItem("Banane"),
		        new CheckMenuItem("Birne"), new CheckMenuItem("Kiwi"));
		// choices.getItems().addAll(items);
		selectCombobox.getItems().addAll(items);

		for (final CheckMenuItem item : items)
		{
			item.selectedProperty().addListener((observableValue, oldValue, newValue) ->
			{
				if (newValue)
				{
					selectedItems.getItems().add(item.getText());
				}
				else
				{
					selectedItems.getItems().remove(item.getText());
				}
			});
		}

		BorderPane borderPane = new BorderPane();
		borderPane.setTop(selectCombobox);
		borderPane.setCenter(selectedItems);
		primaryStage.setScene(new Scene(borderPane, 400, 300));
		primaryStage.show();
	}

	public static void main(String[] args)
	{
		launch(args);
	}
}
