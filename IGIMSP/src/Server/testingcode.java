package Server;

import com.fazecast.jSerialComm.*;

public class testingcode {
    public static void main(String[] args) {
        // Get a list of available serial ports
    	
    	 String portName = "COM4"; // Example port name, replace with your actual port
         
       // SerialPort[] ports = SerialPort.getCommPorts();
    	 StringBuffer packet;
    	 SerialPort port = SerialPort.getCommPort(portName);
         
        // Check if any ports are available
        if (port == null) {
            System.out.println("No serial ports found.");
            return;
        }
        
        // Select the first available port (you may adjust this based on your setup)
       // SerialPort port = ports[0];
        
        // Open the selected port
        if (!port.openPort()) {
            System.out.println("Failed to open port: " + port.getSystemPortName());
            return;
        }
        
        System.out.println("Listening to port: " + port.getSystemPortName());
        
        // Configure the serial port settings (baud rate, data bits, stop bits, parity)
        port.setComPortParameters(9600, 8, 1, 0);
        
        // Create a listener for incoming data
        port.addDataListener(new SerialPortDataListener() {
            @Override
            public int getListeningEvents() {
                return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
            }
            
            @Override
            public void serialEvent(SerialPortEvent event) {
                if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
                    return;
                
                // Read data from the serial port
                byte[] newData = new byte[port.bytesAvailable()];
                port.readBytes(newData, newData.length);
                
                // Convert byte array to string and print
                String dataStr = new String(newData);
                System.out.print(dataStr);
                port.writeBytes(dataStr.getBytes(), dataStr.length()); 
            }
        });
        
        // Wait indefinitely
        while (true) {
            try {
                Thread.sleep(1000); // Sleep for a while to allow data processing
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
