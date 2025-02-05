package Server;

import com.fazecast.jSerialComm.*;
import com.fazecast.jSerialComm.*;

public class SerialCommunicator {

    private SerialPort comPort;

    public void initialize(String portName) {
        comPort = SerialPort.getCommPort(portName);
        System.out.println(comPort);
        comPort.setBaudRate(9600); // Set your baud rate here
        comPort.openPort();
    }

    public void sendCommand(String command) {
        byte[] commandBytes = command.getBytes();
        comPort.writeBytes(commandBytes, commandBytes.length);
    }

    public String readResponse() {
        byte[] readBuffer = new byte[1024];
        int numRead = comPort.readBytes(readBuffer, readBuffer.length);
        
        
        
        return new String(readBuffer, 0, numRead);
    }

    public void close() {
        comPort.closePort();
    }
}
