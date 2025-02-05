package Server;

import java.io.IOException;
import java.net.InetAddress;

public class NetworkScanner {

    // Method to ping a specific IP address and resolve its hostname
    public static boolean isReachable(String ipAddress) {
        try {
            InetAddress inet = InetAddress.getByName(ipAddress);
            return inet.isReachable(1000); // Timeout in milliseconds
        } catch (IOException e) {
            return false;
        }
    }

    // Method to scan the network range and get the hostname (if available) and IP address
    public static void scanNetwork(String subnet) {
        // Loop through the entire subnet range based on the subnet mask 255.255.240.0
        for (int i = 1; i <= 254; i++) {
            for (int j = 16; j <= 31; j++) {
                String ipAddress = "10.226." + j + "." + i; // Construct IP address based on subnet and range
                if (isReachable(ipAddress)) {
                    try {
                        InetAddress inet = InetAddress.getByName(ipAddress);
                        String hostName = inet.getHostName(); // Get the hostname of the reachable IP
                        System.out.println("Device found at IP: " + ipAddress + " with Hostname: " + hostName);
                    } catch (IOException e) {
                        System.out.println("Device found at IP: " + ipAddress + " (Hostname not resolved)");
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        String subnet = "10.226"; // Base of your subnet
        scanNetwork(subnet); // Scan the subnet
    }
}
