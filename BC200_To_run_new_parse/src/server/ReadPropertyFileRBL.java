package server;

import java.io.InputStream;
import java.io.IOException;
import java.util.Properties;


import java.io.FileInputStream;
import java.util.Map;
import java.util.HashMap;

public class ReadPropertyFileRBL
{
    private static final String OS_NAME;
    private static final String pathFileName = "/read.properties";
    private static String windowsPath;
    private static String linuxPath;
    public static HashMap propertyMap;
    
    static {
        OS_NAME = System.getProperties().getProperty("os.name");
        ReadPropertyFileRBL.windowsPath = "c:/TcpFiles/property";
        ReadPropertyFileRBL.linuxPath = "/opt/TcpFiles/property";
    }
    
    public static Map getPropertyValues() {
        ReadPropertyFileRBL.propertyMap = new HashMap();
        String path = "";
        final String ip = "ip";
        final String port = "port";
        final String eqp = "eqp";
        final String hos = "hos";
        final String uid = "uid";
        final String formatid = "formatid";
        final String order_port = "order_port";
        System.out.println("check");
        final String httpcheck="httpcheck";  
        if (ReadPropertyFileRBL.OS_NAME.startsWith("Win")) {
            path = ReadPropertyFileRBL.windowsPath;
        }
        else {
            path = ReadPropertyFileRBL.linuxPath;
        }
        path = String.valueOf(path) + "/read.properties";
        try {
            Throwable t = null;
            try {
                final InputStream input = new FileInputStream(path);
                try {
                    final Properties prop = new Properties();
                    prop.load(input);
                    ReadPropertyFileRBL.propertyMap.put(ip, prop.getProperty(ip));
                    ReadPropertyFileRBL.propertyMap.put(port, prop.getProperty(port));
                    ReadPropertyFileRBL.propertyMap.put(eqp, prop.getProperty(eqp));
                    ReadPropertyFileRBL.propertyMap.put(hos, prop.getProperty(hos));
                    ReadPropertyFileRBL.propertyMap.put(uid, prop.getProperty(uid));
                    ReadPropertyFileRBL.propertyMap.put(formatid, prop.getProperty(formatid));
                    ReadPropertyFileRBL.propertyMap.put(order_port, prop.getProperty(order_port));
                    ReadPropertyFileRBL.propertyMap.put(order_port, prop.getProperty(order_port));
                    ReadPropertyFileRBL.propertyMap.put(httpcheck, prop.getProperty(httpcheck));
                    
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
        return ReadPropertyFileRBL.propertyMap;
    }
}