package Server;
import com.fazecast.jSerialComm.SerialPort;
import com.formdev.flatlaf.FlatLightLaf;

import Server.ABC;
import java.util.Arrays;
import java.util.List;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


//Machine Interface GUI   ......###########........... TCP/IP , Serial Communication byRohit Malviya
public class MonitorGUI {

	
	
	//
	
    private static JTextPane textPane;
    protected static String portNumber;
    protected static String selectedMachine;
    protected static String selectedProtocol;
    static ServerSocket server = null;
    static Socket client = null;
    static int port;
    static ExecutorService pool = null;
    static int clientcount = 0;
    static  JRadioButton tcpIpRadioButton = new JRadioButton("TCP/IP");
    static  JRadioButton comPortRadioButton = new JRadioButton("Serial Port");
    static JTextField baudRateTextField = new JTextField(10);
	protected static String portName;
    static JComboBox<String> comPortComboBox = new JComboBox<>();
    protected static String BaudRate;
    static JCheckBox dataFormMachine = new JCheckBox("Start Data Reciving");
    public static void main(String[] args) {
    

                                      
        FlatLightLaf.install();

        // Create the frame
        JFrame frame = new JFrame("Generic Interface by CDAC Noida");
       // ImageIcon frameIcon = new ImageIcon("resource/cdac-logo.png"); // Ensure you have this file in your resources folder
       // frame.setIconImage(frameIcon.getImage());
        Image image = Toolkit.getDefaultToolkit().getImage("resource/bgimgm.jpg");
        
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1400, 800);
        frame.setLocationRelativeTo(null); // Center the frame

        frame.setBackground(Color.blue);
     
//        Image icon = Toolkit.getDefaultToolkit().getImage("D:\\icon.png");    
//        f.setIconImage(icon);    
//        f.setLayout(null);     
//        f.setSize(400,400);     
//        f.setVisible(true);     
//        
        
        // Create the main panel with BorderLayout
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create the top panel with GridBagLayout
       // JPanel topPanel = new JPanel(new GridBagLayout());
      //  topPanel.setBackground(new Color(245, 245, 245)); // Light grey background for the top panel
        
        
        // Create the top panel with GridBagLayout and background image
        Image backgroundImage = Toolkit.getDefaultToolkit().getImage("resource/bgimgm2.jpg");

        JPanel topPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
       // GridBagConstraints gbc = new GridBagConstraints();
       // gbc.insets = new Insets(5, 5, 5, 5);

        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        
        
        

        // Create and decorate the "Select Machine" panel
        JPanel machinePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        machinePanel.setBackground(new Color(245, 245, 245));
        machinePanel.setBorder(BorderFactory.createTitledBorder("Machine  & Protocol Selection"));
        String[] machines = {"Advia2120","Erba680", "Horriba", "Sysmex","Other"};
        JComboBox<String> comboBox = new JComboBox<>(machines);
      //  machinePanel.add(new JLabel("Select Machine:"));
      //  machinePanel.add(comboBox);
        
  
        String[] protocol = {"HL7", "ASTM", "Other"};
        JComboBox<String> comboBox2 = new JComboBox<>(protocol);
        
      //  machinePanel.add(new JLabel("Protocol:"));
       // machinePanel.add(comboBox2);

       
        JPanel portPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        portPanel.setBackground(new Color(245, 245, 245));
        portPanel.setBorder(BorderFactory.createTitledBorder("Port Configuration"));
        JTextField portTextField = new JTextField(10);
      //  portPanel.add(new JLabel("Port Number:"));
        JLabel portNumberLabel = new JLabel("Port Number:");
        

       
      
        
        
      //  JTextField portTextField = new JTextField(10);
        portNumberLabel.setVisible(false); // Initially hidden
        portTextField.setVisible(false); // Initially hidden
        portPanel.add(portNumberLabel);
        portPanel.add(portTextField);
      
        portTextField.setVisible(false); 

       
        ButtonGroup portButtonGroup = new ButtonGroup();
      //  portButtonGroup.add(tcpIpRadioButton);
        portButtonGroup.add(comPortRadioButton);
     
        
        
     //   portPanel.add(tcpIpRadioButton);
        portPanel.add(comPortRadioButton);
    
       
        
        String[] tIntverl = {"5","10","15","20","25","30"};
        JComboBox<String> tIntervalcomboBox = new JComboBox<>(tIntverl);
        JLabel comPortLabel = new JLabel("COM Port :  ");

        comPortLabel.setVisible(false); // Initially hidden
        comPortComboBox.setVisible(false); // Initially hidden
      //  portPanel.add(comPortLabel);
        portPanel.add(comPortComboBox);
        portPanel.add(new JLabel("Baud Rate:"));
      
        portPanel.add(baudRateTextField);
        portPanel.add(new JLabel("Time Interval:"));
        portPanel.add(tIntervalcomboBox);
        // Create the buttons
        JButton button1 = new JButton("Connect to Machine");
        JButton button2 = new JButton("Download Log");
        
        // Style the buttons 
        button1.setBackground(new Color(70, 130, 180));
        
      //  button1.setPreferredSize(new Dimension(150, 50));
        button1.setForeground(Color.WHITE);
        button2.setBackground(new Color(70, 130, 180));
        button2.setForeground(Color.WHITE);
        JButton exitButton = new JButton("Exit");
        exitButton.setBackground(new Color(220, 20, 60));
        exitButton.setForeground(Color.WHITE);
        // Create the text pane with a scroll pane
        textPane = new JTextPane();
        textPane.setEditable(false); 
        JScrollPane scrollPane = new JScrollPane(textPane);

        populatePortComboBox();
        
        tcpIpRadioButton.addActionListener(new ActionListener() {   //event on button click to show/hide Port Number  Field
            @Override
            public void actionPerformed(ActionEvent e) {
                portNumberLabel.setVisible(true);
                portTextField.setVisible(true);
                comPortLabel.setVisible(false);
                comPortComboBox.setVisible(false);
                frame.revalidate();
                frame.repaint();
            }
        });
 
        comPortRadioButton.addActionListener(new ActionListener() {  //event on button click to List Port Name Combo box..
            @Override
            public void actionPerformed(ActionEvent e) {
                portNumberLabel.setVisible(false);
                portTextField.setVisible(false);
                comPortLabel.setVisible(true);
                comPortComboBox.setVisible(true);
                frame.revalidate();
                frame.repaint();
            }
        });
        
        //download log file
//        
//        button2.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                String logContent = textPane.getText();
//                try (BufferedWriter writer = new BufferedWriter(new FileWriter("log.txt"))) {
//                    writer.write(logContent);
//                    JOptionPane.showMessageDialog(frame, "Log has been downloaded to log.txt");
//                } catch (IOException ex) {
//                    ex.printStackTrace();
//                    JOptionPane.showMessageDialog(frame, "Error occurred while downloading the log.");
//                }
//            }
//        });
//        
        
        //
        
        
        
        
        
        
        
        
        // Action listener for buttons
        ActionListener buttonListener = new ActionListener() {   //execution on button click event Start Comunication..
            private String interVal;

			@Override
            public void actionPerformed(ActionEvent e) {
            
            	
            
            	
            	if (e.getSource() == exitButton) {   // to close the application
            	int res=	JOptionPane.showConfirmDialog(frame, "Are you sure to close this application.");

            	if(res==JOptionPane.YES_OPTION){  
            		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
            		System.exit(0);
            	}  
                    
                    
            	}
            	
            	else if(e.getSource() == button2) {  ///to download log file on button click..
            		
            		
           		 String logContent = textPane.getText();
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter("C:/TcpFiles/property/Machinelog.txt"))) {
                        writer.write(logContent);
                        JOptionPane.showMessageDialog(frame, "Log has been downloaded to Machinelog.txt");
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(frame, "Error occurred while downloading the log.");
                    }
           		
           	}
           	
            	else {   //  to start the Communication when click on start communication 
            		 
            		
            		
            		
            		
              	  try {
              		
              		  	selectedMachine = (String) comboBox.getSelectedItem();
              		 
		                selectedProtocol = (String) comboBox2.getSelectedItem();
		                portNumber = portTextField.getText();
		                portName=  (String) comPortComboBox.getSelectedItem();
		                BaudRate=(String) baudRateTextField.getText();
		                interVal=(String) tIntervalcomboBox.getSelectedItem();
		                
		                interVal=(String) tIntervalcomboBox.getSelectedItem();
  					if (validateInputs( portNumber,portName)) {
  				
  		            
  		                new Thread(() -> {
  		                	
  		                	
  		                	
  		                	String logMessage = "Selected  Port Name: " + portName + "Baud Rate: "+BaudRate;
	    		                logMessage(logMessage, Color.MAGENTA);
  		                	
	    		                
	    		                RequestTransfer.start(portName,BaudRate,interVal);
	    		              
  		                	
  		                	
  		                }).start();
  						  
  					  }
  					
  				} catch (Exception e1) {
  					// TODO Auto-generated catch block
  					e1.printStackTrace();
  				}
              	
            		
            		
            
            }
            	
            }
        };

        button1.addActionListener(buttonListener);   //Start Communication event
        button2.addActionListener(buttonListener);   //Download log event
        exitButton.addActionListener(buttonListener);  //Exit Button event

        // Create the button panel and add buttons to it
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 5, 5));  //create button panel and them add into the top panel which is exists in the Main Frame
        buttonPanel.setBorder(BorderFactory.createTitledBorder("Actions"));  //Border and header line
        
        buttonPanel.setBackground(new Color(245, 245, 245)); // Light grey background
        buttonPanel.setOpaque(true); // Ensure background color is displayed
        buttonPanel.add(button1);
        buttonPanel.add(button2);
        buttonPanel.add(exitButton);
       
        
        
        
        //changed the  button font 
        
        button1.setFont(new Font("Monospaced",Font.BOLD,12)); 
        button2.setFont(new Font("Monospaced",Font.BOLD,12));
        exitButton.setFont(new Font("Monospaced",Font.BOLD,12));
        
        
    

        JPanel hisConfig = new JPanel(new GridLayout(6, 2, 5, 5));
        hisConfig.setBorder(BorderFactory.createTitledBorder("HIS Configuration"));
      //  JCheckBox c1 = new JCheckBox("HIS Configuration"); 
    ////    hisConfig.add(c1);
        // Create labels and text fields
        JLabel lblUrl = new JLabel("HIS URL:");
        JTextField txtUrl = new JTextField();
        JLabel lblEquipId = new JLabel("Equipment ID:");
        JTextField txtEquipId = new JTextField();
        JLabel lblHospitalCode = new JLabel("Hospital Code:");
        JTextField txtHospitalCode = new JTextField();
        
        // Add labels and text fields to the panel
        hisConfig.add(lblUrl);hisConfig.add(txtUrl);
        hisConfig.add(lblEquipId); hisConfig.add(txtEquipId);
        hisConfig.add(lblHospitalCode); hisConfig.add(txtHospitalCode);
        
       
       
      
       
        
        // Initially hide the additional fields panel
        hisConfig.setVisible(true);
        
        // Add components to the top panel with proper alignment
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
     
     //   topPanel.add(machinePanel, gbc);

        gbc.gridy = 1;
        topPanel.add(portPanel, gbc);

        gbc.gridy = 2;
      //  topPanel.add(hisConfig,gbc);

       

     
        gbc.gridy=4;
        topPanel.add(buttonPanel, gbc);
        // Add components to the main panel
        panel.add(topPanel, BorderLayout.NORTH);
        
        
        panel.add(scrollPane, BorderLayout.CENTER);

        // Add panel to the frame
        frame.add(panel);
        frame.setVisible(true);
    }
    
    // Method to populate combo box with available ports using jSerialComm
    private static void populatePortComboBox() {
        SerialPort[] ports = SerialPort.getCommPorts();
        for (SerialPort port : ports) {
        	comPortComboBox.addItem(port.getSystemPortName());
        }
    }
	      

    static void logMessage(String message, Color color) {
        StyledDocument doc = textPane.getStyledDocument();
        Style style = textPane.addStyle("Style", null);
        StyleConstants.setForeground(style, color);
      //  StyleConstants.setBackground(style, color);

        try {
            doc.insertString(doc.getLength(), message + "\n", style);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    MonitorGUI(int port) {
        this.port = port;
        pool = Executors.newFixedThreadPool(5);  //max size was 5 change until connections not fully closed
    }

  
	        

  

  private static boolean validateInputs(  String portNumber,String portName) {
	  
	  
//      if (selectedMachine == null || selectedMachine.isEmpty()) {
//          JOptionPane.showMessageDialog(null, "Please select a machine name.", "Validation Error", JOptionPane.ERROR_MESSAGE);
//          return false;
//      }
   if(comPortRadioButton.isSelected()) {
    	  System.out.println("COM PORT");
    	  if (portName.isEmpty()) {
              JOptionPane.showMessageDialog(null, "Please Select a port Name.", "Validation Error", JOptionPane.ERROR_MESSAGE);
              return false;
          }
    	  
      }
   else {
	   
	   JOptionPane.showMessageDialog(null, "Please Select Serial port.", "Validation Error", JOptionPane.ERROR_MESSAGE);
       return false;
   }
     
      return true;
  }




//public void writeBuffer(byte[] txbuf) {
//    byte[] framebyte = {DataConstants.CTRLCHAR, (byte) (DataConstants.FRAMECHAR & DataConstants.BIT5COMPL), 0};
//    byte[] ctrlbyte = {DataConstants.CTRLCHAR, (byte) (DataConstants.CTRLCHAR & DataConstants.BIT5COMPL), 0};
//
//    byte check_sum = 0x00;
//    byte b1 = 0x00;
//    byte b2 = 0x00;
//
//    int txbuflen = txbuf.length + 1;
//    byte[] temptxbuff = new byte[txbuflen];
//
//    for (int j = 0; j < txbuflen; j++) {
//        temptxbuff[j] = 0;
//    }
//
//    temptxbuff[0] = DataConstants.FRAMECHAR;
//
//    int i = 1;
//
//    for (byte b : txbuf) {
//        switch (b) {
//            case DataConstants.FRAMECHAR:
//                temptxbuff[i] = framebyte[0];
//                temptxbuff[i + 1] = framebyte[1];
//                i += 2;
//                b1 += framebyte[0];
//                b1 += framebyte[1];
//                check_sum += b1;
//                break;
//            case DataConstants.CTRLCHAR:
//                temptxbuff[i] = ctrlbyte[0];
//                temptxbuff[i + 1] = ctrlbyte[1];
//                i += 2;
//                b2 += ctrlbyte[0];
//                b2 += ctrlbyte[1];
//                check_sum += b2;
//                break;
//            default:
//                temptxbuff[i] = b;
//                i++;
//                check_sum += b;
//                break;
//        }
//    }
//
//    int buflen = i;
//    byte[] finaltxbuff = new byte[buflen + 2];
//
//    for (int j = 0; j < buflen; j++) {
//        finaltxbuff[j] = temptxbuff[j];
//    }
//
//    finaltxbuff[buflen] = check_sum;
//    finaltxbuff[buflen + 1] = DataConstants.FRAMECHAR;
//
//    try {
//        serialPort.writeBytes(finaltxbuff, finaltxbuff.length);
//    } catch (Exception ex) {
//        System.err.println("Error opening/writing to serial port: " + ex.getMessage());
//    }
//}





}






