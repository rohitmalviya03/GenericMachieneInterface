package application;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

public class Main extends Application {

    private static Label statusLabel;
    private static Label connectionStatusLabel;
    static final String FILE_NAME = "./monitor_data_log.txt"; // File to save the data

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Microbiology Lab");

        // Title
        Label titleLabel = new Label("Microbiology Lab Order System");
        titleLabel.getStyleClass().add("title");

        // Create UI components
        Label sampleLabel = new Label("Enter Sample Number:");
        sampleLabel.getStyleClass().add("label");

        TextField sampleNumberField = new TextField();
        sampleNumberField.setPromptText("Sample Number");
        sampleNumberField.getStyleClass().add("text-field");

        // Create a ComboBox for specimen types
        Label specimenLabel = new Label("Select Specimen Type:");
        specimenLabel.getStyleClass().add("label");
        
        ComboBox<String> specimenTypeComboBox = new ComboBox<>();
        specimenTypeComboBox.getItems().addAll("URIN", "BLOOD", "OTHER");
        specimenTypeComboBox.setValue("URIN"); // Default selection
        specimenTypeComboBox.getStyleClass().add("combo-box");

        statusLabel = new Label();
        statusLabel.getStyleClass().add("status");

        connectionStatusLabel = new Label();
        connectionStatusLabel.getStyleClass().add("status");

        Button sendButton = new Button("Send Order Request");
        sendButton.getStyleClass().add("button");

        // Handle button click
        sendButton.setOnAction(e -> {
            String sampleNumber = sampleNumberField.getText();
            String specimenType = specimenTypeComboBox.getValue();
            if (!sampleNumber.isEmpty()) {
                try {
                    // Generate HL7 message and send to server
                    String hl7Message = HL7MessageGenerator.generateOrderMessage(sampleNumber, specimenType);
                    
                    ServerConnector.sendToServer(hl7Message);
                    updateStatusLabel("Order sent successfully for sample: " + sampleNumber);
                    saveToFile(hl7Message,FILE_NAME);
                } catch (Exception ex) {
                    updateStatusLabel("Error sending order: " + ex.getMessage());
                }
            } else {
                updateStatusLabel("Please enter a sample number.");
            }
        });

        // Create a GridPane layout for better alignment
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.getStyleClass().add("grid-pane");

        // Adding the title at the top
        gridPane.add(titleLabel, 0, 0, 2, 1); // Span title across two columns
        gridPane.add(sampleLabel, 0, 1);
        gridPane.add(sampleNumberField, 1, 1);
        gridPane.add(specimenLabel, 0, 2);
        gridPane.add(specimenTypeComboBox, 1, 2);
        
        // Center the button in the grid
        gridPane.add(sendButton, 0, 3, 2, 1); // Span button across two columns
        GridPane.setHalignment(sendButton, javafx.geometry.HPos.CENTER); // Center align button
        
        gridPane.add(statusLabel, 0, 6, 2, 1); // Span status label across two columns
        gridPane.add(connectionStatusLabel, 0, 8, 2, 1);

        // Create and set the Scene
        Scene scene = new Scene(gridPane, 1600, 600);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();

        // Start the server listener
       //  startServerListener();
    }

    private void startServerListener() {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                while (true) {
                    String receivedData = ServerConnector.receiveFromServer(); // Implement this method

                    if (receivedData != null) {
                        updateStatusLabel("Received: " + receivedData);
                        saveToFile("Received: " + receivedData,FILE_NAME);
                    }

                    Thread.sleep(2000);
                }
            }
        };

        new Thread(task).start();
    }

    public static void updateStatusLabel(String message) {
        javafx.application.Platform.runLater(() -> {
            statusLabel.setText(message);
        });
    }
    
    public static void updateConnectionStatusLabel(String message) {
        javafx.application.Platform.runLater(() -> {
            connectionStatusLabel.setText(message);
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
    
    
	static void saveToFile(String data, String jsonFileName) {
		try (FileWriter fw = new FileWriter(jsonFileName, true);
				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter out = new PrintWriter(bw)) {
			String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			out.println(timestamp + " - " + data);
		} catch (IOException e) {
			System.err.println("Error writing to file: " + e.getMessage());
		}
	} 

}
