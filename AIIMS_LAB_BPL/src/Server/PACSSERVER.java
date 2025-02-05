package Server;

import javax.swing.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.formdev.flatlaf.FlatLightLaf;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PACSSERVER {

	private static JFrame frame;
	private JTextField crTextField;
	private JButton sendDataButton;
	private JButton startSchedulerButton;
	private JButton clearLogButton; // Button to clear log
	private JLabel sampleNumberLabel, titleLabel, footerLabel;
	private static JTextArea messageArea; // TextArea for messages
	private ScheduledExecutorService scheduler;
	private static JLabel statusLabel;
	private JButton startListenerButton; // Button to start listener
	// private JFrame frame;
	// private JTextField crTextField;
	// private JButton sendDataButton, startSchedulerButton, startListenerButton,
	// clearLogButton;
	// private JLabel titleLabel, sampleNumberLabel, statusLabel;
	// private JTextArea messageArea;

	// read property data

	static final String FILE_NAME = "./machineLog.txt"; // File to save the data

	private static final char START_BLOCK = 0x0B; // <VT> (0B in HEX)
	private static final char END_BLOCK_1 = 0x1C; // <FS> (1C in HEX)
	private static final char END_BLOCK_2 = 0x0D; // <CR> (0D in HEX)
	static Map res = ReadPropertyFile.getPropertyValues();
	// static int result_port = Integer.parseInt((String)
	// res.get("PACS_SERVER_IP"));

	// end

	// PostgreSQL and PACS server information
	private final String POSTGRES_URL = "jdbc:postgresql://10.226.80.35:5444/hmis_aiims_patna";
	private final String POSTGRES_USER = "hmisaiimsp";
	private final String POSTGRES_PASSWORD = "hmisaiimsp";
	private static String PACS_SERVER_IP = (String) res.get("PACS_SERVER_IP");
	private static int PACS_SERVER_PORT = Integer.parseInt((String) res.get("PACS_SERVER_PORT"));
	private static int PACS_LISTNER_PORT = Integer.parseInt((String) res.get("PACS_LISTNER_PORT"));;
	private static boolean isListening = false;

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new PACSSERVER().createAndShowGUI());
	}

	private void createAndShowGUI() {
		// Set Look and Feel (FlatLaf for modern UI)
		try {
			UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf());
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		// Frame setup
		frame = new JFrame("PACS");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.setBackground(new Color(240, 240, 240));
		frame.setSize(1200, 800);
		frame.setLocationRelativeTo(null); // Center the window

		// Title Label with Modern Color Scheme
		titleLabel = new JLabel("", SwingConstants.CENTER);
		titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
		titleLabel.setForeground(new Color(255, 255, 255));
		titleLabel.setOpaque(true);
		titleLabel.setBackground(new Color(33, 150, 243));
		titleLabel.setPreferredSize(new Dimension(0, 30));
		frame.add(titleLabel, BorderLayout.NORTH);

		footerLabel = new JLabel("", SwingConstants.CENTER);
		footerLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
		footerLabel.setForeground(new Color(255, 255, 255));
		footerLabel.setOpaque(true);
		footerLabel.setBackground(new Color(33, 150, 243));
		footerLabel.setPreferredSize(new Dimension(0, 10));
		frame.add(footerLabel, BorderLayout.SOUTH);

		// Main content panel with GridBagLayout for alignment
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridBagLayout());
		mainPanel.setBackground(Color.WHITE);
		mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(15, 15, 15, 15);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.CENTER;

		// Left Panel - CR Number and Buttons
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new GridBagLayout());
		leftPanel.setBackground(Color.WHITE);

		JLabel crNumberLabel = new JLabel("Enter CR Number:");
		crNumberLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		crNumberLabel.setForeground(new Color(60, 60, 60));
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		leftPanel.add(crNumberLabel, gbc);

		crTextField = new JTextField();
		crTextField.setPreferredSize(new Dimension(250, 35));
		crTextField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		crTextField.setBorder(BorderFactory.createLineBorder(new Color(33, 150, 243), 2));
		gbc.gridy = 1;
		leftPanel.add(crTextField, gbc);

		// Send Data Button
		sendDataButton = createModernButton("Send Order Manually", new Color(76, 175, 80),
				e ->   
		
					//fetchSampleDataManually()
					
		getPendingOrderByCR(crTextField.getText())
				
				);
		gbc.gridx = 0;
		gbc.gridy = 2;
		leftPanel.add(sendDataButton, gbc);

		// Start Scheduler Button
		startSchedulerButton = createModernButton("Get Pending Order List", new Color(33, 150, 243),
				e -> startScheduler());
		gbc.gridx = 1;
		gbc.gridy = 3;
		leftPanel.add(startSchedulerButton, gbc);

		// Start Listener Button
		startListenerButton = createModernButton("Transfer Result to HIS", new Color(33, 150, 243),
				e -> startListener(PACS_LISTNER_PORT)); // Orange color
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.gridwidth = 2;
		leftPanel.add(startListenerButton, gbc);

		// Clear Log Button
		clearLogButton = createModernButton("Clear Log", new Color(244, 67, 54), e -> clearLog()); // Red color
		gbc.gridy = 5;
		leftPanel.add(clearLogButton, gbc);

		// Sample number label
		sampleNumberLabel = new JLabel("Sample Number: N/A");
		sampleNumberLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		sampleNumberLabel.setForeground(new Color(60, 60, 60));
		gbc.gridy = 5;
		leftPanel.add(sampleNumberLabel, gbc);

		// Right Panel - Log Area and System Status
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new GridBagLayout());
		rightPanel.setBackground(Color.WHITE);

		// Message Area (Scrollable JTextArea)
		messageArea = new JTextArea(10, 230);
		messageArea.setEditable(false);
		messageArea.setLineWrap(true);
		messageArea.setWrapStyleWord(true);
		messageArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		JScrollPane scrollPane = new JScrollPane(messageArea);
		scrollPane.setPreferredSize(new Dimension(400, 200));
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		rightPanel.add(scrollPane, gbc);

		// Status Panel (Modern and clean)
		JPanel statusPanel = new JPanel();
		statusPanel.setLayout(new BorderLayout());
		statusPanel.setBorder(BorderFactory.createTitledBorder("System Status"));
		statusLabel = new JLabel("System Status: Ready");
		statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		statusLabel.setForeground(new Color(60, 60, 60));
		statusPanel.add(statusLabel, BorderLayout.CENTER);
		gbc.gridy = 1;
		rightPanel.add(statusPanel, gbc);

		// Footer
		// Add a footer label for credits or additional info
		JLabel footerLabel = new JLabel("PACS - HIS Integration © CDAC  2024");
		footerLabel.setFont(new Font("Arial", Font.ITALIC, 10));
		footerLabel.setForeground(Color.GRAY);
		// gbc.gridy = 10;
		// gbc.gridx = 8;
		gbc.gridwidth = 3;
		mainPanel.add(footerLabel, gbc);
		// Add the panels to the main panel
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		mainPanel.add(leftPanel, gbc);

		gbc.gridx = 1;
		mainPanel.add(rightPanel, gbc);

		frame.add(mainPanel, BorderLayout.CENTER);
		frame.setVisible(true);
		// frame end

	}

	private JButton createModernButton(String text, Color backgroundColor, ActionListener action) {
		JButton button = new JButton(text);
		button.setPreferredSize(new Dimension(180, 40));
		button.setFont(new Font("Arial", Font.BOLD, 14));
		button.setBackground(backgroundColor);
		button.setForeground(Color.WHITE);
		button.setFocusPainted(false);
		button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		button.addActionListener(action);
		button.setUI(new javax.swing.plaf.basic.BasicButtonUI());
		return button;
	}

	/// function to fetch patinet data from HIS sever /// pending worklist...

	private static JSONArray getPendingOrderList() {
		// Declare the JSONArray to store the response data
		final JSONArray[] jsonResponse = new JSONArray[1]; // Using array to allow assignment within the inner class
		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() throws Exception {
				try {
					System.out.println("Calling API");
					// Open the connection to the API
					URL url = new URL("http://10.226.28.174:8085/api/v1/pacs/patientdetails");
					HttpURLConnection connection = (HttpURLConnection) url.openConnection();
					connection.setRequestMethod("GET"); // Assuming it's a GET request to fetch sample data
					connection.setRequestProperty("Content-Type", "application/json");
					connection.setDoInput(true);

					// Get the response code
					int responseCode = connection.getResponseCode();
					System.out.println("Response Code: " + responseCode);
					if (responseCode == HttpURLConnection.HTTP_OK) { // Success
						// Read the response
						BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
						String inputLine;
						StringBuilder response = new StringBuilder();

						while ((inputLine = in.readLine()) != null) {
							response.append(inputLine);
						}
						in.close();
						System.out.println("Response: " + response.toString());
						saveToFile("Response: " + response.toString(), FILE_NAME);

						// Parse the JSON response as a JSONArray
						jsonResponse[0] = new JSONArray(response.toString());
					} else {
						saveToFile("Failed to fetch data. Response Code: " + responseCode, FILE_NAME);
						appendMessage("Failed to fetch data. Response Code: " + responseCode);
						
						System.out.println("Failed to fetch data. Response Code: " + responseCode);
					}
				} catch (IOException e) {
					System.out.println("Error fetching sample data: " + e.getMessage());
					appendMessage("Error fetching sample data: " + e.getMessage());
					
					saveToFile("Stack Trace: " + getStackTraceAsString(e), FILE_NAME);
					// addLogEntry("Error fetching sample data: ");
					e.printStackTrace();
				}
				return null; // Return null from background thread
			}

			@Override
			protected void done() {
				// The task has completed, you can handle the result in the main thread
				try {
					// If the background task was successful, the jsonResponse is already populated
					if (jsonResponse[0] != null) {
						System.out.println("Successfully fetched data: " + jsonResponse[0]);
						for (int i = 0; i < jsonResponse[0].length(); i++) {
							JSONObject patient = jsonResponse[0].getJSONObject(i);

							// Extract fields from each patient object
							String his_order_id = patient.getString("hisOrderId");
							String patient_id = patient.getString("patientId");
							String patient_fname = patient.getString("patientFname");
							String patient_mname = patient.getString("patientMname");
							String patient_lname = patient.getString("patientLname");
							String patient_sex = patient.getString("patientGender");

							//
							String phone_number = patient.getString("phoneNumber");
							String email_id = patient.getString("emailId");
							String patient_weight = patient.getString("patientWeight");
							String patient_type = patient.getString("patientType");
							String patient_history = patient.getString("patientHistory");
							String center_id = patient.getString("centerId");
							String patient_birth_date = patient.getString("patientBirthDate");
							String modality = patient.getString("modality");
							String test_id = patient.getString("testId");
							String test_name = patient.getString("testName");
							String referring_physician_id = patient.getString("referringPhysicianId");
							String referring_physician_name = patient.getString("referringPhysicianName");
							String radiologist_id = patient.getString("radiologistId");
							String technician_id = patient.getString("technicianId");

							String hl7Message;
							try {
								hl7Message = generateHL7Message(his_order_id, patient_id, patient_fname,
										patient_mname, patient_lname, patient_sex, patient_birth_date, phone_number,
										email_id, patient_weight, patient_type, patient_history, center_id,
										modality, test_id, test_name, referring_physician_id, referring_physician_name,
										radiologist_id, technician_id);
								ServerConnector.sendToServer(hl7Message);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							// System.out.println(hl7Message);
						

						}
					} else {
						System.out.println("No data received.");
					}
				} catch (Exception e) {
					saveToFile("Stack Trace: " + getStackTraceAsString(e), FILE_NAME);
					// addLogEntry("Some Issue in fetching data");
					e.printStackTrace();
				}
			}
		};
		worker.execute(); // Run in background thread to keep UI responsive

		// Return the JSONArray after the background task is complete
		// Since SwingWorker executes asynchronously, it won't immediately return the
		// data,
		// so you may need to handle it via a callback or future mechanism.
		return jsonResponse[0]; // This will be null initially until the task completes
	}

	
	
	
	
	
	
	
	//fetch details based on CR no from HIS Server
	

	private static JSONArray getPendingOrderByCR(String cerNos) {
	    JSONArray jsonResponse = null; // Declare JSONArray to store the response data

	    
	  //.  String crNumber = crTextField.getText(); // Get the entered CR number

		if (cerNos == null || cerNos.trim().isEmpty()) {
			// Show a message dialog box with the warning
			JOptionPane.showMessageDialog(frame, "Please enter the cr number first", "Input Error",
					JOptionPane.WARNING_MESSAGE);
			return null;
		}
	    
	    
	    try {
	        // Create a JSON object containing the cerNos
	        JSONObject requestBody = new JSONObject();
	        try {
				requestBody.put("patientId", cerNos);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	        System.out.println("Calling API with cerNos: " + requestBody.toString());

	        // Open the connection to the API
	        URL url = new URL("http://10.226.28.174:8085/api/v1/pacs/patientbycr/"+cerNos);
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	        connection.setRequestMethod("GET"); // Change to POST
	        connection.setRequestProperty("Content-Type", "application/json");
	        connection.setDoInput(true);
	        connection.setDoOutput(true); // Enable output stream for POST request

	        // Write the request body to the output stream
	      /*  try (OutputStream os = connection.getOutputStream()) {
	            byte[] input = requestBody.toString().getBytes("utf-8");
	            os.write(input, 0, input.length);
	        }
*/
	        // Get the response code
	        int responseCode = connection.getResponseCode();
	        System.out.println("Response Code: " + responseCode);
	        
	        
	        if (responseCode == HttpURLConnection.HTTP_OK) { // Success
	            // Read the response
	            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	            String inputLine;
	            StringBuilder response = new StringBuilder();

	            while ((inputLine = in.readLine()) != null) {
	                response.append(inputLine);
	            }
	            in.close();
	            System.out.println("Response: " + response.toString());

	            // Parse the JSON response as a JSONArray
	            try {
					jsonResponse = new JSONArray(response.toString());
					
					
					JSONObject patient = jsonResponse.getJSONObject(0);

					// Extract fields from each patient object
					String his_order_id = patient.getString("hisOrderId");
					String patient_id = patient.getString("patientId");
					String patient_fname = patient.getString("patientFname");
					String patient_mname = patient.getString("patientMname");
					String patient_lname = patient.getString("patientLname");
					String patient_sex = patient.getString("patientGender");

					//
					String phone_number = patient.getString("phoneNumber");
					String email_id = patient.getString("emailId");
					String patient_weight = patient.getString("patientWeight");
					String patient_type = patient.getString("patientType");
					String patient_history = patient.getString("patientHistory");
					String center_id = patient.getString("centerId");
					String patient_birth_date = patient.getString("patientBirthDate");
					String modality = patient.getString("modality");
					String test_id = patient.getString("testId");
					String test_name = patient.getString("testName");
					String referring_physician_id = patient.getString("referringPhysicianId");
					String referring_physician_name = patient.getString("referringPhysicianName");
					String radiologist_id = patient.getString("radiologistId");
					String technician_id = patient.getString("technicianId");

					String hl7Message;
					
						hl7Message = generateHL7Message(his_order_id, patient_id, patient_fname,
								patient_mname, patient_lname, patient_sex, patient_birth_date, phone_number,
								email_id, patient_weight, patient_type, patient_history, center_id,
								modality, test_id, test_name, referring_physician_id, referring_physician_name,
								radiologist_id, technician_id);
						try {
							ServerConnector.sendToServer(hl7Message);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							saveToFile("Stack Trace: " + getStackTraceAsString(e), FILE_NAME);

						}
					
					
					
					
					
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					saveToFile("Stack Trace: " + getStackTraceAsString(e), FILE_NAME);
					appendMessage("Error fetching sample data ");
				}
	        }
	        
	        else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) { // Handle NOT_FOUND
	            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
	            StringBuilder errorResponse = new StringBuilder();
	            String inputLine;

	            while ((inputLine = in.readLine()) != null) {
	                errorResponse.append(inputLine);
	            }
	            in.close();
	            System.out.println("Error Response: " + errorResponse.toString());
	            System.out.println("No orders found for the given patientId.");
	            
	        	JOptionPane.showMessageDialog(frame, "No orders found for the given patientId.", "Input Error",
						JOptionPane.WARNING_MESSAGE);
	        } 
	        else {
	            System.out.println("Failed to fetch data. Response Code: " + responseCode);
	        }
	    } catch (IOException e) {
	        System.out.println("Error fetching sample data: " + e.getMessage());
			saveToFile("Error fetching sample data: " + e.getMessage(), FILE_NAME);
			appendMessage("Error fetching sample data ");
	        e.printStackTrace();
	    }

	    return jsonResponse; // Return the JSONArray after the API call
	}
	
	//
	
	
	
	
	
	
	// end

	// Method to fetch sample ID from PostgreSQL, generate HL7, and send to PACS
	private void fetchSampleAndSendHL7() { // Fetch data from the server in house at aiims RB
		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() throws Exception {
				String sampleId = null; // Initialize sampleId to store fetched sample number
				try {
					Class.forName("org.postgresql.Driver");
					try (Connection conn = DriverManager.getConnection(POSTGRES_URL, POSTGRES_USER,
							POSTGRES_PASSWORD)) {
						String query = "SELECT his_order_id, patient_id, patient_fname, patient_mname, patient_lname, patient_gender, center_id, TO_CHAR(patient_birth_date, 'YYYY-MM-DD') AS patient_birth_date, modality, test_id, test_name, phone_number, email_id, patient_weight, patient_type, patient_history, referring_physician_id, referring_physician_name, radiologist_id, technician_id, TO_CHAR(created_at, 'YYYY-MM-DD HH24:MI:SS') AS created_at FROM pacs_patient_sample_data where pacs_order_status=1;\r\n"
								+ "";
						try (PreparedStatement stmt = conn.prepareStatement(query)) {
							ResultSet rs = stmt.executeQuery();
							while (rs.next()) {

								String his_order_id = rs.getString("his_order_id");
								String patient_id = rs.getString("patient_id");
								String patient_fname = rs.getString("patient_fname");
								String patient_mname = rs.getString("patient_mname");
								String patient_lname = rs.getString("patient_lname");
								String patient_sex = rs.getString("patient_gender");

								//
								String phone_number = rs.getString("phone_number");
								String email_id = rs.getString("email_id");
								String patient_weight = rs.getString("patient_weight");
								String patient_type = rs.getString("patient_type");
								String patient_history = rs.getString("patient_history");
								String center_id = rs.getString("center_id");
								String patient_birth_date = rs.getString("patient_birth_date");
								String modality = rs.getString("modality");
								String test_id = rs.getString("test_id");
								String test_name = rs.getString("test_name");
								String referring_physician_id = rs.getString("referring_physician_id");
								String referring_physician_name = rs.getString("referring_physician_name");
								String radiologist_id = rs.getString("radiologist_id");
								String technician_id = rs.getString("technician_id");

								String hl7Message = generateHL7Message(his_order_id, patient_id, patient_fname,
										patient_mname, patient_lname, patient_sex, patient_birth_date, phone_number,
										email_id, patient_weight, patient_type, patient_history, center_id,
										modality, test_id, test_name, referring_physician_id, referring_physician_name,
										radiologist_id, technician_id);
								System.out.println(hl7Message);
								Boolean orderStatus = sendHL7ToPACSServer(hl7Message);
								if (orderStatus) {
									updatePacsOrderStatus(patient_id, 2);
								}
							}
						}
					} catch (Exception e) {
						appendMessage("Error: " + e.getMessage());
						saveToFile("Error: " + e.getMessage(), FILE_NAME);
						saveToFile("Stack Trace: " + getStackTraceAsString(e), FILE_NAME);

					}
				} catch (ClassNotFoundException e) {
					appendMessage("PostgreSQL JDBC Driver not found.");
					saveToFile("Stack Trace: " + getStackTraceAsString(e), FILE_NAME);

				}
				// Update the sample number label in the done method
				if (sampleId != null) {
					sampleNumberLabel.setText("Sample Number: " + sampleId);
				} else {
					sampleNumberLabel.setText("Sample Number: Not Found");
				}
				return null;
			}
		};
		worker.execute(); // Run in background thread to keep UI responsive
	}

	// Method to fetch sample data based on entered CR number
	private void fetchSampleDataManually() {
		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() throws Exception {
				String sampleId = null; // Initialize sampleId to store fetched sample number
				String crNumber = crTextField.getText(); // Get the entered CR number

				if (crNumber == null || crNumber.trim().isEmpty()) {
					// Show a message dialog box with the warning
					JOptionPane.showMessageDialog(frame, "CR Number is required.", "Input Error",
							JOptionPane.WARNING_MESSAGE);
					return null;
				}

				try {
					// Load the PostgreSQL JDBC Driver
					Class.forName("org.postgresql.Driver");

					// Establish the connection using try-with-resources
					try (Connection conn = DriverManager.getConnection(POSTGRES_URL, POSTGRES_USER,
							POSTGRES_PASSWORD)) {

						// Define the query
						String query = "SELECT his_order_id, patient_id, patient_fname, patient_mname, patient_lname, patient_gender, "
								+ "center_id, TO_CHAR(patient_birth_date, 'YYYY-MM-DD') AS patient_birth_date, modality, test_id, "
								+ "test_name, phone_number, email_id, patient_weight, patient_type, patient_history, referring_physician_id, "
								+ "referring_physician_name, radiologist_id, technician_id, TO_CHAR(created_at, 'YYYY-MM-DD HH24:MI:SS') AS created_at "
								+ "FROM pacs_patient_sample_data WHERE patient_id = ?";

						// Use try-with-resources for PreparedStatement and ResultSet to manage
						// resources effectively
						try (PreparedStatement stmt = conn.prepareStatement(query)) {
							stmt.setString(1, crNumber); // Set the patient ID (crNumber)

							try (ResultSet rs = stmt.executeQuery()) {
								// Check if the ResultSet is empty (no rows)
								if (!rs.next()) {
									// No data found for the given patient_id
									appendMessage("No data found for the given patient ID: " + crNumber);
									saveToFile("No data found for the patient ID: " + crNumber, FILE_NAME);
									System.out.println("No data found for the given patient ID: " + crNumber);
								} else {
									// Process each row in the result set
									do {
										// Retrieve values from the result set
										String his_order_id = rs.getString("his_order_id");
										String patient_id = rs.getString("patient_id");
										String patient_fname = rs.getString("patient_fname");
										String patient_mname = rs.getString("patient_mname");
										String patient_lname = rs.getString("patient_lname");
										String patient_sex = rs.getString("patient_gender");
										String phone_number = rs.getString("phone_number");
										String email_id = rs.getString("email_id");
										String patient_weight = rs.getString("patient_weight");
										String patient_type = rs.getString("patient_type");
										String patient_history = rs.getString("patient_history");
										String center_id = rs.getString("center_id");
										String patient_birth_date = rs.getString("patient_birth_date");
										String modality = rs.getString("modality");
										String test_id = rs.getString("test_id");
										String test_name = rs.getString("test_name");
										String referring_physician_id = rs.getString("referring_physician_id");
										String referring_physician_name = rs.getString("referring_physician_name");
										String radiologist_id = rs.getString("radiologist_id");
										String technician_id = rs.getString("technician_id");

										// Generate the HL7 message
										String hl7Message = generateHL7Message(his_order_id, patient_id, patient_fname,
												patient_mname, patient_lname, patient_sex, patient_birth_date,
												phone_number, email_id, patient_weight, patient_type, patient_history,
												center_id, modality, test_id, test_name, referring_physician_id,
												referring_physician_name, radiologist_id, technician_id);

										// Print and send the HL7 message to the PACS server
										// System.out.println(hl7Message);
										Boolean orderStatus = sendHL7ToPACSServer(hl7Message);
										if (orderStatus) {
											updatePacsOrderStatus(patient_id, 2);
										}
									} while (rs.next()); // Continue if there are more rows
								}
							} catch (SQLException e) {
								appendMessage("Error processing the result set");
								saveToFile("Stack Trace: " + getStackTraceAsString(e), FILE_NAME);
							}
						} catch (SQLException e) {
							appendMessage("Error executing query");
							saveToFile("Stack Trace: " + getStackTraceAsString(e), FILE_NAME);
						}

					} catch (SQLException e) {
						appendMessage("Failed to connect to PostgreSQL database");
						saveToFile("Stack Trace: " + getStackTraceAsString(e), FILE_NAME);
					}

				} catch (ClassNotFoundException e) {
					appendMessage("PostgreSQL JDBC Driver not found.");
					saveToFile("Stack Trace: " + getStackTraceAsString(e), FILE_NAME);
				}

				return null;
			}
		};

		worker.execute(); // Run in background thread to keep UI responsive
	}

	// Generate HL7 Message based on sample ID
	private static String generateHL7Message(String his_order_id, String pat_id, String pat_fname, String pat_mname,
			String pat_lname, String pat_gender, String patient_birth_date, String phone_number, String email_id,
			String patient_weight, String patient_type, String patient_history, String center_id, String modality,
			String test_id, String test_name, String referring_physician_id, String referring_physician_name,
			String radiologist_id, String technician_id) {
		// Customize HL7 message generation based on your requirements

		/*
		 * 
		 * .[
		 * MSH|^~\&|HIS_APP|HIS_FACILITY|PACS_APP|PACS_FACILITY|DateTime||ORM^O01|
		 * MsgCtrlId_ORM|P|2.3
		 * PID|1||P0001||Smith^John^Doe^^||john.smith@example.com|M|||||C001^1990-05-14^
		 * CT Head Scan PV1|1|||||||R001^Dr. Alice Walker||||||||||OP||VisitNumber
		 * ORC|NW|||||||||||RAD001 OBR|1|1|555-0100|Outpatient^No significant medical
		 * history.||||||||||||||||||||75.50||||||||||
		 * 
		 * 
		 * 
		 */

		SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");

		// Define the output format (yyyyMMdd)
		SimpleDateFormat outputFormat = new SimpleDateFormat("yyyyMMdd");

		Date currentDate = new Date();

		// Define the desired format
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
		String formattedDate = sdf.format(currentDate);

		try {
			// Parse the input string into a Date object
			Date dobDate = inputFormat.parse(patient_birth_date);

			// Convert the Date object into the desired format
			patient_birth_date = outputFormat.format(dobDate);

			// Output the formatted date
			System.out.println("Formatted DOB (yyyyMMdd): " + patient_birth_date);

		} catch (Exception e) {
			e.printStackTrace();
		}

		String OrderMSg = START_BLOCK
				+ "MSH|^~\\&|HIS_APP|HIS_FACILITY|PACS_APP|PACS_FACILITY|DateTime||ORM^O01|MsgCtrlId_ORM|P|2.3\r"
				+ "PID|1||PatientID||PatientLast^PatientFirst^PatientMiddle^^Title||DateOfBirth|Gender|||||Phone^Email^History\r"
				+ "PV1|1|||||||RefPhyID^RefPhyName||||||||||OP||VisitNumber\r" + "ORC|NW|||||||||||RadiologistID\r"
				+ "OBR|1|OID|CenterID|TestID^TestName||||||||||||||||||||MODALITY||||||||||TechnicianID\r" + END_BLOCK_1
				+ END_BLOCK_2;

		// OrderMSg=OrderMSg.replaceAll("sampleId", sampleId);
		OrderMSg = OrderMSg.replaceAll("DateTime", formattedDate);
		OrderMSg = OrderMSg.replaceAll("OID", his_order_id);
		OrderMSg = OrderMSg.replaceAll("CenterID", center_id);
		OrderMSg = OrderMSg.replaceAll("PatientID", pat_id);
		OrderMSg = OrderMSg.replaceAll("PatientLast", pat_lname);
		OrderMSg = OrderMSg.replaceAll("PatientFirst", pat_fname);
		OrderMSg = OrderMSg.replaceAll("PatientMiddle", pat_mname);
		OrderMSg = OrderMSg.replaceAll("Title", "");
		OrderMSg = OrderMSg.replaceAll("Gender", pat_gender);
		OrderMSg = OrderMSg.replaceAll("Phone", phone_number);
		OrderMSg = OrderMSg.replaceAll("History", patient_history);
		OrderMSg = OrderMSg.replaceAll("DateOfBirth", patient_birth_date);
		OrderMSg = OrderMSg.replaceAll("Email", email_id);
		OrderMSg = OrderMSg.replaceAll("PatientType", patient_type);
		OrderMSg = OrderMSg.replaceAll("MODALITY", modality);
		OrderMSg = OrderMSg.replaceAll("TestID", test_id);
		OrderMSg = OrderMSg.replaceAll("TestName", test_name);
		OrderMSg = OrderMSg.replaceAll("RefPhyID", referring_physician_id);
		OrderMSg = OrderMSg.replaceAll("RefPhyName", referring_physician_name);
		OrderMSg = OrderMSg.replaceAll("RadiologistID", radiologist_id);
		OrderMSg = OrderMSg.replaceAll("TechnicianID", technician_id);

		String oldOrderMSgTemplate = START_BLOCK   //This is not in use;
				+ "MSH|^~\\&|HIS_APP|HIS_HOSPITAL|PACS_APP|PACS_HOSPITAL|202410151230||ORM^O01|MSG001|P|2.3\r\n"
				+ "PID|1||123456||Doe^John^^Mr.||19800101|M|||||555-1234^john.doe@example.com^No relevant history\r\n"
				+ "PV1|1|||||||123^Dr. Smith||||||||||OP||50\r\n" + "ORC|NW|||||||||||789\r\n"
				+ "OBR|1|HOID123|Center456|TST001^CT Scan Head||||||||||||||||||||CR||||||||||TECH456\r\n" + "";

		return OrderMSg;

	}

	// Method to send HL7 message to PACS server over socket
	private Boolean sendHL7ToPACSServer(String hl7Message) {
		saveToFile("Connected on : " + PACS_SERVER_IP + ": " + PACS_SERVER_PORT, FILE_NAME);
		boolean isSend = true;
		try (Socket socket = new Socket(PACS_SERVER_IP, PACS_SERVER_PORT)) {

			saveToFile("Connected on : " + PACS_SERVER_IP + ": " + PACS_SERVER_PORT, FILE_NAME);
			OutputStream outputStream = socket.getOutputStream();
			outputStream.write(hl7Message.getBytes());
			outputStream.flush();
			System.out.println("HL7 Message Sent:\n" + hl7Message);
			saveToFile("HL7 Message Sent:\n" + hl7Message, FILE_NAME);
			appendMessage("HL7 Data Sent Successfully!");
			InputStream input = socket.getInputStream();

			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			// PrintWriter writer = new PrintWriter(output, true);

			String line;
			StringBuilder receivedData = new StringBuilder();

			int order_packet_buffer_counter = 0;
			int red = -1;
			byte[] buffer = new byte[800 * 1024]; // a read buffer of 5KiB
			byte[] redData;
			StringBuilder clientData = new StringBuilder();
			String redDataText = "";

			while ((red = socket.getInputStream().read(buffer)) > -1) // **Code Starts For Receiving Client
			// Messages
			{

				redData = new byte[red];

				System.arraycopy(buffer, 0, redData, 0, red);

				redDataText = new String(redData, "UTF-8"); // assumption that client sends data UTF-8 encoded
				System.out.println("ACK RECIVED : " + redDataText);
				saveToFile("ACK RECIVED : " + redDataText, FILE_NAME);

			}

		} catch (Exception e) {
			isSend = false;
			appendMessage("Error sending HL7 message: " + e.getMessage());

			saveToFile("Stack Trace: " + getStackTraceAsString(e), FILE_NAME);

		}
		return isSend;
	}

	// Method to start scheduler
	private void startScheduler() {

		try {
			if (scheduler == null || scheduler.isShutdown()) {
				scheduler = Executors.newScheduledThreadPool(1);
				scheduler.scheduleAtFixedRate(() -> {
					try {
						// fetchSampleAndSendHL7(); //to fetch data from local server
						getPendingOrderList();// to fetch data drom HIS Server ////
					} catch (Exception e) {
						saveToFile("Error in scheduler: " + e.getMessage(), FILE_NAME);

						appendMessage("Error in scheduler: " + e.getMessage());
					}
				}, 0, 1, TimeUnit.MINUTES); // Runs every 1 minute
				appendMessage("Scheduler started. Fetching data every 1 minute.");
				statusLabel.setText("Scheduler started. Fetching data every 1 minute.");
			}
		}

		catch (Exception e) {
			saveToFile("Stack Trace: " + getStackTraceAsString(e), FILE_NAME);
			

			// TODO: handle exception
		}
	}

	// result handle
	public static void startListener(int port) {
		new Thread(() -> {
			try (ServerSocket serverSocket = new ServerSocket(port)) {
				appendMessage("Listener started on port " + port + ".");
				isListening = true;

				// Continuously accept new clients
				while (isListening) {
					Socket clientSocket = serverSocket.accept(); // Accept a client connection
					appendMessage("Client connected: " + clientSocket.getInetAddress());

					// Handle the client connection in a separate thread
					handleResultData(clientSocket);
				}
			} catch (IOException e) {
				statusLabel.setText("");
				appendMessage("Error on listener: " + e.getMessage());
				saveToFile("Stack Trace: " + getStackTraceAsString(e), FILE_NAME);

			}
		}).start();
	}

	private static void handleResultData(Socket clientSocket) {
		new Thread(() -> {
			try {
				InputStream input = clientSocket.getInputStream();
				OutputStream output = clientSocket.getOutputStream();

				BufferedReader reader = new BufferedReader(new InputStreamReader(input));
				PrintWriter writer = new PrintWriter(output, true);

				String line;
				StringBuilder receivedData = new StringBuilder();

				int order_packet_buffer_counter = 0;
				int red = -1;
				byte[] buffer = new byte[800 * 1024]; // a read buffer of 5KiB
				byte[] redData;
				StringBuilder clientData = new StringBuilder();
				String redDataText = "";

				while ((red = clientSocket.getInputStream().read(buffer)) > -1) // **Code Starts For Receiving Client
				// Messages
				{

					redData = new byte[red];

					System.arraycopy(buffer, 0, redData, 0, red);

					redDataText = new String(redData, "UTF-8"); // assumption that client sends data UTF-8 encoded
					System.out.println("RESULT : " + redDataText);

					String ackMSG = START_BLOCK
							+ "MSH|^~\\&|HIS_APP|HIS_FACILITY|PACS_APP|HIS_FACILITY|TIMESTAMP||ACK^O01|MSGCTRLID|P|2.3\r"
							+ "MSA|AA|MSGCTRLID|Result Accept\r" + END_BLOCK_1 + END_BLOCK_2;

//					String ackTemplate = START_BLOCK+"MSH|^~\\&|HL72LIS||LIS|BMX|myla|YYYYMMDDHHMMSS+0530||ACK^R22|CONTROLID|P|2.5.1\r"
//							+ "MSA|AA|CONTROLID\r"+END_BLOCK_1+END_BLOCK_2;
					String[] msgPart = redDataText.split("\r");
					String[] mshPart = msgPart[0].split("\\|");

					Date currentDate = new Date();
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
					String formattedDate = dateFormat.format(currentDate);

					// if(mshPart[0].equals("MSH")) {
					String controlId = mshPart[9]; // Extract CONTROLID from received message
					ackMSG = ackMSG.replaceAll("TIMESTAMP", formattedDate);
					ackMSG = ackMSG.replaceAll("MSGCTRLID", controlId);
					output.write(ackMSG.getBytes());
					saveToFile("ACK SENT : " + ackMSG, FILE_NAME);
					System.out.println("ACK SENT " + "--- - ---" + ackMSG);

				}
			} catch (Exception e) {
				// TODO: handle exception
				saveToFile("Stack Trace: " + getStackTraceAsString(e), FILE_NAME);

			}
		}).start();
	}
	///

	// Method to clear the log messages
	private void clearLog() {
		messageArea.setText("");
		appendMessage("Log cleared.");
	}

	// Method to append messages to the JTextArea
	private static void appendMessage(String message) {
		SwingUtilities.invokeLater(() -> {
			messageArea.append(message + "\n");
			messageArea.setCaretPosition(messageArea.getDocument().getLength()); // Auto-scroll to the bottom
		});
	}

	PrintStream cout;
	// static Map res = ReadPropertyFile.getPropertyValues();
	static String server_ip = (String) res.get("server_ip");
	static String server_port = (String) res.get("server_port");
	private static ServerSocket serverSocket;
	private static boolean listening = true; // Flag to control listening state

	public static void sendToServer(String hl7Message) throws Exception {
		String serverAddress = server_ip;// "10.226.28.174"; // Replace with actual server IP
		int port = Integer.parseInt(server_port);// 8002; // Replace with actual server port

		appendMessage("LIS is trying to connect on : " + server_ip + " : " + server_port);

		try (Socket socket = new Socket(serverAddress, port); OutputStream out = socket.getOutputStream()) {
			InputStream in = socket.getInputStream();

			appendMessage("LIS is Connected on : " + server_ip + " : " + server_port);
			System.out.println("LIS is Connected on : " + server_ip + " : " + server_port);
			// System.out.println(hl7Message);
			saveToFile("LIS is Connected on : " + server_ip + " : " + server_port, AIIMSLAB.FILE_NAME);

			out.write(hl7Message.getBytes());
			out.flush();
			saveToFile("Sent :  " + hl7Message, AIIMSLAB.FILE_NAME);

			// new Thread(new ResponseReceiver(socket)).start();

		} catch (Exception e) {
			appendMessage("Error connecting to server : " + e.getMessage());
			saveToFile("Error connecting to server : " + e.getMessage(), AIIMSLAB.FILE_NAME);

			System.out.println("Error connecting to server : " + e.getMessage());
			throw new Exception("Error connecting to server: " + e.getMessage());

		}

	}

	private void updatePacsOrderStatus(String pat_id, int status) {
		// Query to update PACS order status in the database
		String updateQuery = "UPDATE pacs_patient_sample_data SET pacs_order_status = ? WHERE patient_id	 = ?";

		try (Connection conn = DriverManager.getConnection(POSTGRES_URL, POSTGRES_USER, POSTGRES_PASSWORD);
				PreparedStatement stmt = conn.prepareStatement(updateQuery)) {

			// Set the parameters for the update statement
			stmt.setInt(1, status); // Set the new order status (e.g., "Sent to PACS")
			stmt.setString(2, pat_id); // Set the order identifier (his_order_id)

			// Execute the update
			int rowsAffected = stmt.executeUpdate();

			// Check if any rows were updated
			if (rowsAffected > 0) {
				System.out.println("Order status updated successfully for his_order_id: " + pat_id);
				saveToFile("Order status updated successfully for his_order_id: " + pat_id, FILE_NAME);

			} else {
				System.out.println("No matching order found for his_order_id: " + pat_id);
				saveToFile("No matching order found for his_order_id: " + pat_id, FILE_NAME);

			}
		} catch (SQLException e) {
			appendMessage("Error updating PACS order status for his_order_id: " + pat_id);
			saveToFile("Stack Trace: " + getStackTraceAsString(e), FILE_NAME);
			System.out.println("Error updating PACS order status: " + e.getMessage());
		}
	}

	public static void saveToFile(String data, String jsonFileName) {
		try (FileWriter fw = new FileWriter(jsonFileName, true);
				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter out = new PrintWriter(bw)) {
			String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			out.println(timestamp + " - " + data);
		} catch (IOException e) {
			System.err.println("Error writing to file: " + e.getMessage());
			saveToFile("Stack Trace: " + getStackTraceAsString(e), FILE_NAME);

		}
	}

	private static String getStackTraceAsString(Exception e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		return sw.toString();
	}
}
