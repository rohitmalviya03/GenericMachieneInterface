package Server;

import javax.swing.*;
import javax.swing.border.LineBorder;

import com.formdev.flatlaf.FlatLightLaf;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AIIMSLAB {

    private static JLabel statusLabel;
    private static JLabel connectionStatusLabel;
    static final String FILE_NAME = "./monitor_data_log.txt"; // File to save the data

    public static void main(String[] args) {
        // Create the main frame
    	FlatLightLaf.install();

        JFrame frame = new JFrame("Microbiology Lab Order System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 600);
        frame.setLayout(new GridBagLayout());
        frame.getContentPane().setBackground(Color.decode("#F0F8FF")); // Light blue background

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Title
        JLabel titleLabel = new JLabel("Microbiology Lab Order System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.decode("#2E8B57")); // Sea green color
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        frame.add(titleLabel, gbc);

        // Sample number label and text field
        JLabel sampleLabel = new JLabel("Enter Sample Number:");
        sampleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        frame.add(sampleLabel, gbc);

        JTextField sampleNumberField = new JTextField(20);
        sampleNumberField.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Color.decode("#2E8B57"), 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5) // Padding
        ));
        sampleNumberField.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        frame.add(sampleNumberField, gbc);

        // Specimen type label and combo box
        JLabel specimenLabel = new JLabel("Select Specimen Type:");
        specimenLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 2;
        frame.add(specimenLabel, gbc);

        String[] specimenTypes = {"URIN", "BLOOD", "OTHER"};
        JComboBox<String> specimenTypeComboBox = new JComboBox<>(specimenTypes);
        specimenTypeComboBox.setSelectedItem("URIN"); // Default selection
        specimenTypeComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        frame.add(specimenTypeComboBox, gbc);

        // Status labels
        statusLabel = new JLabel("");
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        frame.add(statusLabel, gbc);

        connectionStatusLabel = new JLabel("");
        connectionStatusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        gbc.gridy = 5;
        frame.add(connectionStatusLabel, gbc);

        // Send button with enhanced styling
        JButton sendButton = new JButton("Send Order Request");
        sendButton.setFont(new Font("Arial", Font.BOLD, 16));
        sendButton.setBackground(Color.decode("#2E8B57")); // Sea green button
        sendButton.setForeground(Color.WHITE);
        sendButton.setFocusPainted(false);
        sendButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        sendButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        frame.add(sendButton, gbc);

        // Handle button click
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String sampleNumber = sampleNumberField.getText();
                String specimenType = (String) specimenTypeComboBox.getSelectedItem();
                if (!sampleNumber.isEmpty()) {
                    try {
                        // Generate HL7 message and send to server
                        String hl7Message = HL7MessageGenerator.generateOrderMessage(sampleNumber, specimenType);
                        
                        ServerConnector.sendToServer(hl7Message);
                        updateStatusLabel("Order sent successfully for sample: " + sampleNumber);
                        saveToFile(hl7Message, FILE_NAME);
                    } catch (Exception ex) {
                        updateStatusLabel("Error sending order: " + ex.getMessage());
                    }
                } else {
                    updateStatusLabel("Please enter a sample number.");
                }
            }
        });

        // Add a footer label for credits or additional info
        JLabel footerLabel = new JLabel("Microbiology Lab System © 2024");
        footerLabel.setFont(new Font("Arial", Font.ITALIC, 10));
        footerLabel.setForeground(Color.GRAY);
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        frame.add(footerLabel, gbc);

        // Display the frame
        frame.setLocationRelativeTo(null); // Center the frame on the screen
        frame.setVisible(true);
    }

    public static void updateStatusLabel(String message) {
        SwingUtilities.invokeLater(() -> {
            statusLabel.setText(message);
        });
    }
    
    public static void updateConnectionStatusLabel(String message) {
        SwingUtilities.invokeLater(() -> {
            connectionStatusLabel.setText(message);
        });
    }

    public static void saveToFile(String data, String jsonFileName) {
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
