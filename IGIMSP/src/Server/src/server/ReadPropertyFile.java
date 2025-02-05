package server;

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
        //ReadPropertyFile.windowsPath = "C:/TcpFiles/property";
       // ReadPropertyFile.linuxPath = "/opt/TcpFiles/property";
       // ReadPropertyFile.windowsPath = "C://TcpFilessys/property";
        
        //for aiims RB sysmax ca-2500
        ReadPropertyFile.windowsPath = "C:/TcpFilessys/property";
        ReadPropertyFile.linuxPath = "/opt/TcpFilessys/property";
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