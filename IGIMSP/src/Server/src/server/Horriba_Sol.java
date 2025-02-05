package server;







		import org.apache.commons.io.monitor.FileAlterationListener;
		import org.apache.commons.io.monitor.FileAlterationObserver;
		import org.apache.commons.io.monitor.FileAlterationMonitor;
		import org.apache.commons.io.FileUtils;

		import java.io.File;

			import java.io.BufferedReader;
			import java.io.FileReader;
			import java.io.IOException;
			import java.util.Timer;
			import java.util.TimerTask;
	import java.util.regex.Matcher;
	import java.util.regex.Pattern;

			public class Horriba_Sol {
			    public static void main(String[] args) {
			        Timer timer = new Timer();

			        // Schedule the task to run every 2 minutes
			        timer.scheduleAtFixedRate(new ReadFileTask("D://Horiba_log/InterLink_P8000_ResultLog_20230718.txt"), 0, 4 * 60 * 1000);
			    }
			}

			class ReadFileTask extends TimerTask {
			    private String filePath;

			    public ReadFileTask(String filePath) {
			        this.filePath = filePath;
			    }

			    @Override
			    public void run() {
			        try {
			            // Read the file's content
			            StringBuilder content = new StringBuilder();
			            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			                String line;
			                while ((line = reader.readLine()) != null) {
			                    // Check if the line starts with "PID"
			                  
			                	if (line.startsWith("MSH")) {
			                        // Split the line by the pipe character "|"
			                        String[] parts = line.split("\\|");

			                        // Check if there are at least 7 elements in the array
			                        if (parts.length > 6) {
			                            // Extract and print the value after the 6th pipe
			                            String value = parts[6];
			                            
			                            System.out.println("Date Time: " + value);
			                        }
			                	
			                	}
			                	
			                	if (line.startsWith("PID")) {
			                        // Split the line by the pipe character "|"
			                        String[] parts = line.split("\\|");

			                        // Check if there are at least 7 elements in the array
			                            // Extract and print the value after the 6th pipe
			                            String value = parts[3];
			                            String[] pat_id = value.split("\\^");

			                            System.out.println("Patient Id " + pat_id[0]);
			                        
			                	
			                	}
			                	
			                	
			                	if (line.startsWith("PID")) {
			                        // Split the line by the pipe character "|"
			                		 String[] parts = line.split("\\|");

			                         // Check if there are at least 5 elements in the array
			                         if (parts.length > 4) {
			                             // Extract and print the value after the 4th pipe
			                             String value = parts[5];
			                             

				                            String[] name = value.split("\\^");
			                             System.out.println("Pat Name: " + name[1]);
			                         } 
			                	
			                	}
			                  	if (line.startsWith("OBX")) {
			                        // Split the line by the pipe character "|"
			                		 String[] parts = line.split("\\|");

			                         // Check if there are at least 5 elements in the array
			                         if (parts.length > 3) {
			                             // Extract and print the value after the 4th pipe
			                             String value = parts[3];
			                             
			                            // if(value.equals("WBC")|| value.equals("NEU#") || value.equals("LYM#") || value.equals("MON#") || value.equals("EOS#") || value.equals("BAS#") || value.equals("IMG#") || value.equals("NEU%") || value.equals("LYM%") || value.equals("MON%") || value.equals("EOS%") || value.equals("BAS%") || value.equals("IMG%") || value.equals("RBC") || value.equals("HGB") || value.equals("HCT") || value.equals("MCV") || value.equals("MCH") || value.equals("MCHC") || value.equals("RDW-CV") || value.equals("RDW-SD") || value.equals("PLT") || value.equals("MPV") || value.equals("PDW") || value.equals("PCT") || value.equals("PLCC") || value.equals("NRBC#") || value.equals("NRBC%") || value.equals("PLCR") ) {
			 		                     			
			                             String[] tc = value.split("\\^");
			                             System.out.println("Test Code " + tc[0]);}
			                         
			                         if (parts.length > 5) {
			                             // Extract and print the value after the 4th pipe
			                             String value = parts[5];
			                             
			                            // if(value.equals("WBC")|| value.equals("NEU#") || value.equals("LYM#") || value.equals("MON#") || value.equals("EOS#") || value.equals("BAS#") || value.equals("IMG#") || value.equals("NEU%") || value.equals("LYM%") || value.equals("MON%") || value.equals("EOS%") || value.equals("BAS%") || value.equals("IMG%") || value.equals("RBC") || value.equals("HGB") || value.equals("HCT") || value.equals("MCV") || value.equals("MCH") || value.equals("MCHC") || value.equals("RDW-CV") || value.equals("RDW-SD") || value.equals("PLT") || value.equals("MPV") || value.equals("PDW") || value.equals("PCT") || value.equals("PLCC") || value.equals("NRBC#") || value.equals("NRBC%") || value.equals("PLCR") ) {
			 		                     			
			                             String[] tc = value.split("\\^");
			                             System.out.println("Test Code " + tc[0]);}
			                         
			                	
			                	}
			                  	
//			  /                	if (line.startsWith("OBX")) {
//			                        // Split the line by the pipe character "|"
//			                		 String[] parts = line.split("\\|");
	//
//			                         // Check if there are at least 5 elements in the array
//			                         if (parts.length > 5) {
//			                             // Extract and print the value after the 4th pipe
//			                             String value = parts[5];
//			                             
//			                            // if(value.equals("WBC")|| value.equals("NEU#") || value.equals("LYM#") || value.equals("MON#") || value.equals("EOS#") || value.equals("BAS#") || value.equals("IMG#") || value.equals("NEU%") || value.equals("LYM%") || value.equals("MON%") || value.equals("EOS%") || value.equals("BAS%") || value.equals("IMG%") || value.equals("RBC") || value.equals("HGB") || value.equals("HCT") || value.equals("MCV") || value.equals("MCH") || value.equals("MCHC") || value.equals("RDW-CV") || value.equals("RDW-SD") || value.equals("PLT") || value.equals("MPV") || value.equals("PDW") || value.equals("PCT") || value.equals("PLCC") || value.equals("NRBC#") || value.equals("NRBC%") || value.equals("PLCR") ) {
//			 		                     			
//			                             String[] tc = value.split("\\^");
//			                             System.out.println("Test Code " + tc[0]);}
//			                         
//			                	
//			                	}
			                	
			                	
			                	
			                    
			                }
			                
			            }
			               
			           
			        } catch (IOException e) {
			            e.printStackTrace();
			        }
			    }
			}
	
		
		
	

