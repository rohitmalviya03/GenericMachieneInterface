package server;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TooManyListenersException;
import java.util.stream.Collectors;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.fazecast.jSerialComm.SerialPortPacketListener;



public class ServerCom implements Runnable, SerialPortPacketListener {
//	static CommPortIdentifier portId;
	static Enumeration portList;
	static List<String> portList11;
	InputStream inputStream;
	static SerialPort serialPort;
	Thread readThread;
	public static String data = "";

	
	static char eot_char = '';
	static Map res = ReadPropertyFile.getPropertyValues();
	final static String formatid = (String) res.get("formatid");

	final static String comportno = (String) res.get("comport");
	static int forId = Integer.parseInt(formatid);


	static String currentDirectory;
	static String path_HIMS_LOG = "";
	static String path_MachineData = "";
	static String[] argument;
	
	StringBuffer bf_p = new StringBuffer();
	static String message = "";


	static char stx;
	static int counterPID_Packet = 235966128;
	static List testCode = new ArrayList();
	static String name_pat = "";
	static int StxCounter = 1;

	static final DecimalFormat df = new DecimalFormat("0.00");

	public static String getData() {

		return data;
	}

	public static void setData(String data) {
		ServerCom.data = data;
	}

	public static void sendData() {
		SerialPort[] ports = SerialPort.getCommPorts();

        // Specify the COM port you want to target (COM4 in this case)
        String targetPortName = "COM4";

        // Find the specified COM port in the list
        SerialPort targetPort = findPortByName(ports, targetPortName);

        if (targetPort != null) {
            // Open the target serial port
            if (targetPort.openPort()) {
                try {
                    // Set the baud rate, data bits, parity, and stop bits
                    targetPort.setBaudRate(9600);
                    targetPort.setNumDataBits(8);
                    targetPort.setParity(SerialPort.NO_PARITY);
                    targetPort.setNumStopBits(1);

                    // Get the output stream from the serial port
                    OutputStream outputStream = targetPort.getOutputStream();

                    // Data to be sent
                    String dataToSend = "30hI 73";

                    // Send the data
                    outputStream.write(dataToSend.getBytes());
                    outputStream.flush();

                    System.out.println("Data sent to " + targetPortName + ": " + dataToSend);

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    // Close the serial port when done
                   // targetPort.closePort();
                }
            } else {
                System.err.println("Failed to open " + targetPortName);
            }
        } else {
            System.err.println(targetPortName + " not found among available serial ports");
        }
    }

	
	public static void main(String[] args) {
		// -------------------file log

		argument = args;


		sendData();
		// --------------------------

		SerialPort[] ports = SerialPort.getCommPorts();
		if (ports != null && ports.length > 0) {
			for (SerialPort port : ports) {
				System.out.println("List of all ports open ......" + port.getSystemPortName());
				
				if (port.getSystemPortName().equals(comportno)) {
					serialPort = port;
					// System.out.println("selected port ....." + serialPort);
					System.out.println("selected port ....." + port.getSystemPortName());
				
					try {
						ServerCom reader = new ServerCom();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}

	}

	
	public static List<String> getPortNames() {
		return Arrays.stream(SerialPort.getCommPorts()).map(SerialPort::getSystemPortName).collect(Collectors.toList());
	}

	// ============================================
	public ServerCom() throws IOException, TooManyListenersException {
		try {

			serialPort.openPort();
			serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);

			serialPort.addDataListener(this);
			int dataAvailable = serialPort.LISTENING_EVENT_DATA_AVAILABLE;

			serialPort.setBaudRate(9600);
			serialPort.setNumDataBits(8);
			serialPort.setParity(0);
			serialPort.setNumStopBits(1);

			// inputStream = serialPort.getInputStream();

			serialReadData();

		}

		catch (Exception e) {
			System.out.println(e);
		}
		readThread = new Thread(this);
		readThread.start();
	}

	private void serialReadData() throws IOException {
		System.out.println("format Id : ::::--::"+forId);

System.out.println("Serial Poert at reciveing  :::"+serialPort);
		while(true) {
		switch (forId) {
		

		// FOr Laura_Smart Machine
		case 20051: {
			if (serialPort != null) {
			
			try (InputStream in = serialPort.getInputStream();
					BufferedReader bReader = new BufferedReader(new InputStreamReader(in));) {
				String line;
			

				String message;

				String smpl = "Seq.No:";
				String samplecode = null;
				String stx = "";
				String etxstx = "";

				// System.out.println(" buffer reade value " + bReader.toString());

				while (true) {

					message = bReader.readLine();
				

					System.out.println(" message " + message);
				

					}
				

			} catch (IOException e) {
				e.printStackTrace();
			}

		} // clsoing for case 1

			break;

		
			// ---------------------------------------------------------------

		// ---------------------------------------------------------------------

		// -----------------------------------------------------
		// -------------------------------------------------------------------
		// ============================================================================================================================
		}}}// switch

	}

	public void run() {
		try {
			Thread.sleep(20000);
		} catch (InterruptedException e) {
			System.out.println(e);
		}
	}

	public void parse(String message) {

		System.out.println(" message inside parse  --------:" + message);

	}
//code started to seend data to hit API

	  private static SerialPort findPortByName(SerialPort[] ports, String portName) {
	        for (SerialPort port : ports) {
	            if (port.getSystemPortName().equals(portName)) {
	                return port;
	            }
	        }
	        return null;
	    }
	@Override
	public int getListeningEvents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void serialEvent(SerialPortEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getPacketSize() {
		// TODO Auto-generated method stub
		return 0;
	}
	// ================================
	


	// ----------------------------------------------------------------------------

	// -------------------------------------------------------------------------
	// ----------------------------------------------------------------------------

	// ===============================================
	// =====================================================================================

}