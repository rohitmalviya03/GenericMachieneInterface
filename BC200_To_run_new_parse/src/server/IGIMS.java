package server;

import gnu.io.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class IGIMS {
    // COM port settings
    private static final String PORT_NAME = "COM4"; // Change this to your COM port name
    private static final int BAUD_RATE = 9600;
    private static final int DATA_BITS = SerialPort.DATABITS_8;
    private static final int STOP_BITS = SerialPort.STOPBITS_1;
    private static final int PARITY = SerialPort.PARITY_NONE;

    public static void main(String[] args) {
        try {
            // Obtain the COM port identifier
            CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(PORT_NAME);

            // Open the COM port
            SerialPort serialPort = (SerialPort) portIdentifier.open("SerialCommunication", 2000);
            serialPort.setSerialPortParams(BAUD_RATE, DATA_BITS, STOP_BITS, PARITY);

            // Obtain input and output streams
            InputStream inputStream = serialPort.getInputStream();
            OutputStream outputStream = serialPort.getOutputStream();

            // Create a thread to listen for incoming data
            Thread readerThread = new Thread(() -> {
                try {
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = inputStream.read(buffer)) > -1) {
                        String receivedMessage = new String(buffer, 0, len);
                        System.out.println("Received: " + receivedMessage);

                        // Process received message and send response
                        String response = processMessage(receivedMessage);
                        if (response != null) {
                            outputStream.write(response.getBytes());
                            outputStream.flush();
                            System.out.println("Sent: " + response);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            readerThread.start();

            // Keep the program running until user interrupts
            while (true) {
                Thread.sleep(1000);
            }

        } catch (NoSuchPortException | PortInUseException | UnsupportedCommOperationException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Method to process the received message and generate response
    private static String processMessage(String receivedMessage) {
        // Example processing logic
        if (receivedMessage.equals("Hello")) {
            return "Hi there!";
        } else if (receivedMessage.equals("How are you?")) {
            return "I'm fine, thank you!";
        } else {
            return null; // No response for unknown messages
        }
    }
}
