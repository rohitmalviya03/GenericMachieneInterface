package Server;


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
       // ReadPropertyFile.windowsPath = "c:/TcpFilessys/property";
        //ReadPropertyFile.linuxPath = "/opt/TcpFiles/property";
       // ReadPropertyFile.windowsPath="c:/TcpFiles/property";  //location of Prop file in System 
       // ReadPropertyFile.windowsPath="C:/Users/hab/OneDrive/Desktop/";  //location of Prop file in System 
          
        
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
        final String server_port="server_port";
        final String httpcheck="httpcheck";  
        
        final String portname="portname";
        final String server_ip="server_ip";
        
        
        
        final String db_url="db_url";
        
        final String db_user="db_user";
        final String db_pwd="db_pwd";
        
        
        path="config//read.properties";
        //final String local_port="local_port";
      /*  if (ReadPropertyFile.OS_NAME.startsWith("Win")) {
            path = ReadPropertyFile.windowsPath;
        }
        else {
            path = ReadPropertyFile.linuxPath;
        }
        path = String.valueOf(path) + "/read.properties";
       */
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
                   // ReadPropertyFile.propertyMap.put(pr, prop.getProperty(pr));
                    ReadPropertyFile.propertyMap.put(server_port, prop.getProperty(server_port));
                    ReadPropertyFile.propertyMap.put(portname, prop.getProperty(portname));
                    
                    ReadPropertyFile.propertyMap.put(httpcheck, prop.getProperty(httpcheck));
                    ReadPropertyFile.propertyMap.put(server_ip, prop.getProperty(server_ip));
                    
                    ReadPropertyFile.propertyMap.put(db_url, prop.getProperty(db_url));
                    ReadPropertyFile.propertyMap.put(db_user, prop.getProperty(db_user));
                    ReadPropertyFile.propertyMap.put(db_pwd, prop.getProperty(db_pwd));
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