package Server;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class NmapScanner {

    public static void runNmap(String targetIP) {
        try {
            // Command to run Nmap (scan ports 1-1000)
            String command = "nmap -p 1-1000 " + targetIP;

            // Run the command
            Process process = Runtime.getRuntime().exec(command);

            // Get the output of the Nmap command
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            // Wait for the process to finish
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String targetIP = "10.226.28.174"; // Replace with a reachable IP address
        runNmap(targetIP);  // Run Nmap scan on the target IP
    }
}
