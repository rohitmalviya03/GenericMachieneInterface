package application;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.OutputStream;
import java.util.Enumeration;
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

public class MainController {
    @FXML
    private Label statusLabel;

    @FXML
    private void connectToComPort() {
        try {
            
        	
        	
        } catch (Exception e) {
            statusLabel.setText("Failed to connect: " + e.getMessage());
        }
    }
}
