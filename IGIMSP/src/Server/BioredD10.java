package Server;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import com.fazecast.jSerialComm.SerialPort;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PdfStamper;


public class BioredD10 {
	 static String sample_no="";
    static String testval="";
    static String testcode="";
    

	static Map res = ReadPropertyFile.getPropertyValues();
	static String pdfDir = (String) res.get("pdfdir");
	static String portname = (String) res.get("portname");
	private static boolean isValid=false;
	

	
    public static void main(String[] args) {
        // Get a list of available serial ports
        SerialPort[] ports = SerialPort.getCommPorts();
        
        
        // Select the port you want to use
       
        String portName =portname;// "COM7"; // Example: "COM1" on Windows, "/dev/ttyUSB0" on Linux
 
        // Find the port by name or number
        SerialPort port = SerialPort.getCommPort(portName);
char etx='';
		
		char stx='';
		char NAK='';
        //
        
       
        
        //
        
       
        
        System.out.println(port.getPortDescription());
        // Open the selected port
        if (port != null&&port.openPort()) {
            System.out.println("Port opened successfully.");
            
            // Set parameters (baud rate, data bits, stop bits, parity)
            port.setBaudRate(9600);
            port.setNumDataBits(8);
            port.setNumStopBits(1);
            port.setParity(SerialPort.NO_PARITY);
            StringBuilder receivedData = new StringBuilder();
            // Create a thread to continuously read data from the port
            StringBuffer databuffer= new StringBuffer();
            String SampleNo=null;
            List<String> list = new ArrayList<String>();

            
            Thread readerThread = new Thread(() -> {
            	
                while (true) {
                    byte[] buffer = new byte[1024];
                    int numRead = port.readBytes(buffer, buffer.length);
                    
                    if (numRead > 0) {
                        for (int i = 0; i < numRead; i++) {
                            char c = (char) buffer[i];
                            byte[] buffer1 = new byte[1024];
                          
                            long bytesToWrite=10;
                            byte[] ackBytes = { 0x06 }; // ASCII code for ACK character
                        	
                         // Assuming port is your port object
                         port.writeBytes(ackBytes, ackBytes.length); 
							//port.writeBytes(buffer1, bytesToWrite);
                            
                            if (c == '\n') {
                                System.out.println("Received data: " + receivedData.toString());
                                databuffer.append(receivedData.toString());
                              // System.out.println("Data buffer"+databuffer);
                             //  splitFunction(receivedData.toString());
                               list.add(receivedData.toString());
                             //  System.out.println(list);
                             //  System.out.println(list);
                               receivedData.setLength(0); // Clear the buffer
                                
                                
                               String data= receivedData.toString();
                        	
                            
                            } else {
                                receivedData.append(c);
                            }
                        }
                        
                       // System.out.println(list);
                       
                        for(String str:list) {
                        	
                        	//System.out.println("str");
                        	
                        	if(str.contains("O|1")) { //A1c^AREA
                        		
                        	String [] data1=	str.split("\\|");
                        //	System.out.println(data1);
                        	 sample_no=data1[2];
                        	}
                        	
                        	if(str.contains("A1c^AREA")) { 
                        		 isValid=true;
                        		String [] data2=	str.split("\\|");
                                //	System.out.println(data1);
                                	 testval=data2[3];
                                	 testcode=data2[2];
                        		
                        	}
                        	else {
                        		 isValid=false;
                        		
                        	}
                        }
                     
                        
                        

                        //if(receivedData.toString().equals("")) {
                        if( isValid && !testval.equals(null) && !testval.equals("")  ) {
                        	

                            System.out.println("SAMPLE NO :: "+sample_no);
                            System.out.println("test coe:: "+testcode);
                            System.out.println("test val:: "+testval); 
                        try {
                        	//pdfDir
                        	System.out.println("PDF directory ::  "+pdfDir);
                			String base64val=fileReadAndConvertBase64(pdfDir,sample_no);
                			 System.out.println("base 64 for sample no. -->>> "+sample_no+"<<----->>"+base64val); 
                			
                			if(base64val.equals(null)|| base64val.equals("")) {
                				
                				//System.out.println("Base 64 is null");
                			}
                			else {
                				
                			ABC abc= new ABC();
                			abc.insertSampleDtlpdfbase64(sample_no, "A1c",testval,base64val);
                			//continue;
                			
                			
                			}
                		} catch (Exception e) {
                			// TODO Auto-generated catch block
                			e.printStackTrace();
                		}
                        
                    }
                    
                        
                    }
//                    
//                    else {
//                    	
//                    	System.out.println("ROHIT");
//                    }
                 
                    
                   
                }
                
               
            }
            
            		);
         
            readerThread.start();
            
            
           
            
           
        
            
           
            // You can write to the port in a similar manner if needed
            
        } else {
            System.err.println("Failed to open the port.");
        }
        
     
        
        
     //   System.out.println("RRRRRRRRRRR");
    }
    
    
    
    public static String fileReadAndConvertBase64(String sourceFolderPath,String sam) {
    	String base64String="";
		try {
			File sourceFolder = new File(sourceFolderPath);
			// System.out.println("sourceFolder: " + sourceFolder.getPath());

			// List PDF files in the source folder
			File[] pdfFiles = sourceFolder.listFiles((dir, name) -> name.toLowerCase().endsWith(".pdf"));
			StringBuilder str = new StringBuilder();
		//	System.out.println(111);
			if (pdfFiles != null && pdfFiles.length > 0) {
				// System.out.println("pdfFiles.length"+pdfFiles.length);

				
				
				for (File pdfFile : pdfFiles) {
					// Convert the PDF file to Base64

					// System.out.println("pdfFiles"+pdfFile);
					
					String fileName = pdfFile.getName();
					int hyphenIndex = fileName.indexOf("-");
					if (hyphenIndex != -1) {
						
						
						
						String filePrefix = fileName.substring(0, hyphenIndex);
						//str.append("Base64 representation for " + pdfFile.getName() + base64String);
						String dd="";
//						 try {
//							 pdfFile= compressPDF(pdfFile);
//					            System.out.println("PDF compressed successfully.");
//					        } catch (IOException e) {
//					            e.printStackTrace();
//					        }
						if(sam.equals(filePrefix)) {
							base64String = convertToBase64(pdfFile);
							//base64String = convertToBase642(dd,dd);
							
						}
						else{
							System.out.println("File Not found for Sample No ---->"+sam);
							//break;
							//base64String=null;
						}
						// str.append("Base64 representation for " + pdfFile.getName() + ": " +
						// filePrefix);
						// logger.info("Base64 representation for " + pdfFile.getName() + ": " +
						// filePrefix);
					//	logger.info("Base64 representation for " + filePrefix + ":------------" + base64String);
						
					//	System.out.println("base64:::   "+filePrefix+" -- "+base64String);
					}
					// str.append("Base64 representation for " + pdfFile.getName() + base64String);
					// logger.info("Base64 representation for " + pdfFile.getName()+":------------"
					// + base64String);
					// System.out.println("Base64 representation for " + pdfFile.getme() + ":\n" +
					// base64String);
				}
			} else {
				// System.out.println("No PDF files found in the source folder.");
			}
//logger.info(""+str.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return base64String;
	}

    
    
    
    
    
	private static String convertToBase64(File file) throws Exception {
		FileInputStream fileInputStream = new FileInputStream(file);
		byte[] bytes = new byte[(int) file.length()];
		fileInputStream.read(bytes);
		fileInputStream.close();

	
		return Base64.getEncoder().encodeToString(bytes);
	}
    
	
	 private static String convertToBase642(String inputFilePath, String outputFilePath) throws IOException {
	        FileInputStream inputStream = new FileInputStream(inputFilePath);
	        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	        byte[] buffer = new byte[1024];
	        int bytesRead;
	        while ((bytesRead = inputStream.read(buffer)) != -1) {
	            outputStream.write(buffer, 0, bytesRead);
	        }
	        inputStream.close();
	        byte[] compressedBytes = outputStream.toByteArray();
	        String base64String = Base64.getEncoder().encodeToString(compressedBytes);
	        FileOutputStream fileOutputStream = new FileOutputStream(outputFilePath);
	        fileOutputStream.write(base64String.getBytes());
	        fileOutputStream.close();
			return base64String;
	    }
    private static String  compressPDF(String inputFilePath, String outputFilePath) throws IOException, DocumentException {
        PdfReader reader = new PdfReader(new FileInputStream(inputFilePath));
        Document document = new Document();
        PdfCopy copy = new PdfCopy(document, new FileOutputStream(outputFilePath));
        document.open();
        for (int i = 1; i <= reader.getNumberOfPages(); i++) {
            copy.addPage(copy.getImportedPage(reader, i));
        }
        document.close();
        reader.close();
		return outputFilePath;
    }

}
