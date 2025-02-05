package Server;

import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Erba680 {

    private static JTextArea textArea;
    private static JPanel animationPanel;
    private static Timer animationTimer;
    private static int animationX = 0;

    public static void main(String[] args) {
        // Set the FlatLaf look and feel
        FlatLightLaf.install();

        // Create the frame
        JFrame frame = new JFrame("Generic Interface");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1400, 800);
        frame.setLocationRelativeTo(null); // Center the frame

        // Set the frame icon
        //ImageIcon frameIcon = new ImageIcon("resources/icon.png"); // Ensure you have this file in your resources folder
      //  frame.setIconImage(frameIcon.getImage());

        // Create the main panel with BorderLayout
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Custom panel with gradient background
        class GradientPanel extends JPanel {
            private Color color1;
            private Color color2;

            public GradientPanel(Color color1, Color color2) {
                this.color1 = color1;
                this.color2 = color2;
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                int width = getWidth();
                int height = getHeight();
                GradientPaint gp = new GradientPaint(0, 0, color1, width, height, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, width, height);
            }
        }

        // Create the top panel with GridBagLayout
        JPanel topPanel = new GradientPanel(new Color(173, 216, 230), new Color(224, 255, 255)); // Light pink to light pink gradient
        topPanel.setLayout(new GridBagLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        JPanel bottomPanel = new GradientPanel(new Color(173, 216, 230), new Color(224, 255, 255)); // Light pink to light pink gradient
        bottomPanel.setLayout(new GridBagLayout());
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbcd = new GridBagConstraints();
        gbcd.insets = new Insets(5, 5, 5, 5);

        // Create and decorate the "Select Machine" panel
        JPanel machinePanel = new GradientPanel(new Color(173, 216, 230), new Color(224, 255, 255)); // Light blue to light cyan gradient
        machinePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        machinePanel.setBorder(BorderFactory.createTitledBorder("Machine Selection"));
        String[] machines = {"Erba680", "Horriba", "Other"};
        JComboBox<String> comboBox = new JComboBox<>(machines);
        machinePanel.add(new JLabel("Select Machine:"));
        machinePanel.add(comboBox);

        // Create and decorate the "Port Number" panel
        JPanel portPanel = new GradientPanel(new Color(173, 216, 230), new Color(224, 255, 255)); // Light blue to light cyan gradient
        portPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        portPanel.setBorder(BorderFactory.createTitledBorder("Port Configuration"));
        JTextField portTextField = new JTextField(10);
        portPanel.add(new JLabel("Port Number:"));
        portPanel.add(portTextField);

        // Create the buttons
        JButton button1 = new JButton("Connect to Machine");
        JButton button2 = new JButton("Start Communication");
      //  JButton button3 = new JButton("Button 3");
        JButton exitButton = new JButton("Exit");

        // Style the buttons (optional)
        button1.setBackground(new Color(70, 130, 180));
        button1.setForeground(Color.WHITE);
        button2.setBackground(new Color(70, 130, 180));
        button2.setForeground(Color.WHITE);
     //   button3.setBackground(new Color(70, 130, 180));
    //    button3.setForeground(Color.WHITE);
        exitButton.setBackground(new Color(220, 20, 60));
        exitButton.setForeground(Color.WHITE);

        // Set button icons (optional, ensure you have these files in your resources folder)
        button1.setIcon(new ImageIcon("resources/button1.png"));
        button2.setIcon(new ImageIcon("resources/button2.png"));
    //    button3.setIcon(new ImageIcon("resources/button3.png"));
        exitButton.setIcon(new ImageIcon("resources/exit.png"));

        // Create the text area with a scroll pane
        textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(textArea);

    

        // Runnable to handle socket connection and data receiving
        class SocketHandler implements Runnable {
            private final int port;

            public SocketHandler(int port) {
                this.port = port;
            }

            @Override
            public void run() {
                try (ServerSocket serverSocket = new ServerSocket(port)) {
                    logMessage("Server started on port: " + port);
                    while (true) {
                        try (Socket clientSocket = serverSocket.accept();
                             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
                            logMessage("Client connected: " + clientSocket.getRemoteSocketAddress());
                            String inputLine;
                            while ((inputLine = in.readLine()) != null) {
                                logMessage("Received: " + inputLine);
                            }
                        } catch (IOException e) {
                            logMessage("Error in client communication: " + e.getMessage());
                        }
                    }
                } catch (IOException e) {
                    logMessage("Server error: " + e.getMessage());
                }
            }
        }

  

        // Action listener for buttons
        ActionListener buttonListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedMachine = (String) comboBox.getSelectedItem();
                String portNumber = portTextField.getText();
                String logMessage = "Selected Machine: " + selectedMachine + ", Port Number: " + portNumber;
                logMessage(logMessage);

                if (e.getSource() == button1) {
                //    executeCode(selectedMachine, portNumber);
                } else if (e.getSource() == exitButton) {
                    System.exit(0);
                }
            }
        };

        button1.addActionListener(buttonListener);
        button2.addActionListener(buttonListener);
     //   button3.addActionListener(buttonListener);
        exitButton.addActionListener(buttonListener);

        // Create the button panel and add buttons to it
        JPanel buttonPanel = new GradientPanel(new Color(240, 128, 128), new Color(250, 128, 114)); // Light coral to salmon gradient
        buttonPanel.setLayout(new GridLayout(1, 4, 5, 5)); // Modified to add Exit button
        buttonPanel.setBorder(BorderFactory.createTitledBorder("Actions"));
        buttonPanel.add(button1);
        buttonPanel.add(button2);
    //    buttonPanel.add(button3);
        buttonPanel.add(exitButton); // Added Exit button

        // Add components to the top panel with proper alignment
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        topPanel.add(machinePanel, gbc);

        gbc.gridy = 1;
        topPanel.add(portPanel, gbc);

        gbc.gridy = 2;
        topPanel.add(buttonPanel, gbc);

        // Create an animation panel
        animationPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.RED);
                g.fillOval(animationX, 10, 20, 20); // Draw a red circle
            }
        };
        animationPanel.setPreferredSize(new Dimension(200, 50));

        // Animation logic using Timer
        animationTimer = new Timer(30, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                animationX += 5;
                if (animationX > animationPanel.getWidth()) {
                    animationX = 0;
                }
                animationPanel.repaint();
            }
        });
       // animationTimer.start();

        // Add components to the main panel
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(animationPanel, BorderLayout.SOUTH);
        panel.add(bottomPanel,BorderLayout.SOUTH);
        // Add panel to the frame
        frame.add(panel);
        frame.setVisible(true);
    }

    private static void logMessage(String message) {
        textArea.append(message + "\n");
        try (FileWriter fw = new FileWriter("log.txt", true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
