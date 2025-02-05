package ca.uhn.hl7v2.examples;

import java.io.InputStream;
import java.io.IOException;
import java.util.Properties;
import java.io.FileInputStream;
import java.util.Map;
import java.util.HashMap;

public class ReadPropertyFile
{
    private static final String OS_NAME;
    private static final String pathFileName = "/read.properties";
    private static String windowsPath;
    private static String linuxPath;
    public static HashMap propertyMap;
    
    static {
        OS_NAME = System.getProperties().getProperty("os.name");
        ReadPropertyFile.windowsPath = "c:/TcpFilessys/property";
        //ReadPropertyFile.linuxPath = "/opt/TcpFiles/property";
        //ReadPropertyFile.windowsPath="c:/TcpFiles/property";  //location of Prop file in System 
       // ReadPropertyFile.windowsPath="C:/Users/hackb/OneDrive/Desktop/";  //location of Prop file in System 
          
        
    }
    
    public static Map getPropertyValues() {
        ReadPropertyFile.propertyMap = new HashMap();
        String path = "";
        final String ip = "ip";
        final String port = "port";
        final String eqp = "eqp";
        final String hos = "hos";
        final String uid = "uid";
        final String formatid = "formatid";
        final String pr="pr";
        final String orderip="orderip";
        //final String local_port="local_port";
        if (ReadPropertyFile.OS_NAME.startsWith("Win")) {
            path = ReadPropertyFile.windowsPath;
        }
        else {
            path = ReadPropertyFile.linuxPath;
        }
        path = String.valueOf(path) + "/read.properties";
        try {
            Throwable t = null;
            try {
                final InputStream input = new FileInputStream(path);
                try {
                    final Properties prop = new Properties();
                    prop.load(input);
                    ReadPropertyFile.propertyMap.put(ip, prop.getProperty(ip));
                    ReadPropertyFile.propertyMap.put(port, prop.getProperty(port));
                    ReadPropertyFile.propertyMap.put(eqp, prop.getProperty(eqp));
                    ReadPropertyFile.propertyMap.put(hos, prop.getProperty(hos));
                    ReadPropertyFile.propertyMap.put(uid, prop.getProperty(uid));
                    ReadPropertyFile.propertyMap.put(formatid, prop.getProperty(formatid));
                    ReadPropertyFile.propertyMap.put(pr, prop.getProperty(pr));
                    ReadPropertyFile.propertyMap.put(orderip, prop.getProperty(orderip));
                      
                }
                finally {
                    if (input != null) {
                        input.close();
                    }
                }
            }
            finally {
                if (t == null) {
                    final Throwable exception = null;
                    t = exception;
                }
                else {
                    final Throwable exception = null;
                    if (t != exception) {
                        t.addSuppressed(exception);
                    }
                }
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        return ReadPropertyFile.propertyMap;
    }
}