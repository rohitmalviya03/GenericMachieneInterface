package ca.uhn.hl7v2.examples;

import javax.imageio.ImageIO;
import javax.swing.*;

import ca.uhn.hl7v2.llp.LLPException;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
public class HL7GUI {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("CDAC NOIDA");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1500, 400);
        frame.setLocationRelativeTo(null); // Center the JFrame on the screen
        
        
        //logo
        JPanel imgpanel=new JPanel();  
		imgpanel.setLayout(new FlowLayout());      
		BufferedImage myPicture;
		try {
			myPicture = ImageIO.read(new File("cdaclogo.png"));
			JLabel picLabel = new JLabel(new ImageIcon(myPicture));
			imgpanel.add(picLabel);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
        
        
        
        
        
        ImageIcon frameIcon = new ImageIcon("sdi.png"); // Replace with your icon image
        frame.setIconImage(frameIcon.getImage());

        JPanel logoPanel = new JPanel();
        JLabel logoLabel = new JLabel(new ImageIcon("cdac.png")); // Replace with your logo image
        logoPanel.add(logoLabel);
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add margin

        
        
        JLabel label = new JLabel("HORIBA LOG");
        label.setFont(new Font("Roboto", Font.BOLD, 24)); // Modern font and size

        // Create a custom JTextArea for console output
        ConsoleTextArea consoleTextArea = new ConsoleTextArea(200, 100);
        JScrollPane scrollPane = new JScrollPane(consoleTextArea);

        // Create buttons with modern styles
        JButton startButton = createModernButton("Connect");
        JButton sampleEntry = createModernButton("Send to Machine");
      
      
        
        JTextField textField = new JTextField("Enter Sample No here...");
        textField.setFont(new Font("Roboto", Font.PLAIN, 16)); // Modern font and size
        addPlaceholder(textField, "Enter Sample No here...");

        // Add action listeners to the buttons
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                HL7Client1.main(null);
            }
        });

        sampleEntry.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userInput = textField.getText();
                try {
					HL7Client1.manualsampleEntry(userInput);
				} catch (LLPException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }
        });

        // Create a nested panel for buttons and text field
        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.add(startButton, BorderLayout.WEST);
        buttonPanel.add(sampleEntry, BorderLayout.EAST);
        buttonPanel.add(textField, BorderLayout.CENTER);
        JPanel buttonPane2 = new JPanel(new BorderLayout());
        buttonPanel.add(startButton, BorderLayout.WEST);
        buttonPanel.add(sampleEntry, BorderLayout.EAST);
        buttonPanel.add(textField, BorderLayout.CENTER);

        // Apply modern styles to text area
        consoleTextArea.setFont(new Font("Roboto", Font.PLAIN, 14)); // Modern font and size
        consoleTextArea.setForeground(Color.DARK_GRAY);
        consoleTextArea.setBackground(Color.WHITE);
        consoleTextArea.setCaretColor(Color.DARK_GRAY);

        // Add components to the main panel

        
        JPanel northPanel = new JPanel(); // Create a new panel to hold buttonPanel and imgPanel
        northPanel.setLayout(new FlowLayout()); // Use a FlowLayout within northPanel

        northPanel.add(buttonPanel); // Add buttonPanel to the northPanel
        northPanel.add(imgpanel);    // Add imgPanel to the northPanel

        //panel.add(northPanel, BorderLayout.NORTH); 
       // panel.add(label, BorderLayout.NORTH);
        //panel.add(imgpanel,BorderLayout.NORTH);
       panel.add(buttonPanel, BorderLayout.NORTH);
      
       // panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Add the main panel to the frame
        frame.getContentPane().add(panel);
        frame.setVisible(true);

        // Redirect console output to the custom JTextArea
        System.setOut(new PrintStream(new TextAreaOutputStream(consoleTextArea)));
        System.setErr(new PrintStream(new TextAreaOutputStream(consoleTextArea)));
    }

    // Helper method to create modern-styled buttons
    private static JButton createModernButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Roboto", Font.BOLD, 16)); // Modern font and size
        button.setBackground(Color.BLUE);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    
    
    private static void addPlaceholder(JTextField textField, String placeholder) {
        textField.setText(placeholder);
        textField.setForeground(Color.GRAY);
        textField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals(placeholder)) {
                    textField.setText("");
                    textField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setText(placeholder);
                    textField.setForeground(Color.GRAY);
                }
            }
        });
    }
    // Rest of the code for ConsoleTextArea and TextAreaOutputStream remains the same
}


class ConsoleTextArea extends JTextArea {
    public ConsoleTextArea(int rows, int columns) {
        super(rows, columns);
        setEditable(false);
    }

    public void appendText(String text) {
        append(text);
        setCaretPosition(getDocument().getLength());
    }
}

class TextAreaOutputStream extends OutputStream {
    private final JTextArea textArea;

    public TextAreaOutputStream(JTextArea textArea) {
        this.textArea = textArea;
    }

    public void write(int b) throws IOException {
        textArea.append(String.valueOf((char) b));
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }
}