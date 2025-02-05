package Server;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.fazecast.jSerialComm.SerialPort;

public class RequestTransfer {

	   private static SerialPort serialPort;
	
	public  static void start(String portNamedesc,String baudRate,String interVal) {
	
	 
      
      // Create a new SerialPort object with default settings.
      serialPort = SerialPort.getCommPorts()[0]; // Default to the first available port

     // System.out.println("Select the Port to which Datex AS3 Monitor is to be connected, Available Ports:");
      SerialPort[] ports = SerialPort.getCommPorts();
      for (SerialPort port : ports) {
          System.out.println(" " + port.getSystemPortName());
      }

     // System.out.print("COM port(" + serialPort.getSystemPortName() + "): ");
      Scanner scanner = new Scanner(System.in);
      String portName = portNamedesc;
      serialPort.setBaudRate(Integer.parseInt(baudRate));
      if (!portName.isEmpty()) {
          serialPort = SerialPort.getCommPort(portName);
      }

      try {
          if (serialPort.openPort()) {
        	  
        	  
        	  
        	  
        	  
              System.out.println("You may now connect the serial cable to the Datex AS3 Monitor");
              MonitorGUI.logMessage("You may now connect the serial cable to the Datex AS3 Monitor",Color.BLUE);
              System.out.println("Press any key to continue..");
             // MonitorGUI.logMessage("Press any key to continue..",Color.BLUE);
              //System.in.read();

              
              // to recive data from Machine first
              
            //  if() {}
              Thread dataReaderThread2 = new Thread(() -> {
            	  
            	  
                  while (true) {
                      if (serialPort.bytesAvailable() > 0) {
                          byte[] readBuffer = new byte[serialPort.bytesAvailable()];
                          int numRead = serialPort.readBytes(readBuffer, readBuffer.length);
                          System.out.print(new String(readBuffer, 0, numRead));
                          MonitorGUI.logMessage("Data Recieved : "+new String(readBuffer, 0, numRead),Color.BLUE);
                          
                          
                      }

                      try {
                          Thread.sleep(100);
                      } catch (InterruptedException e) {
                          e.printStackTrace();
                      }
                  }
              });
             // dataReaderThread2.start();
              
              
              //
              System.out.println();
              System.out.print("Enter Transmission interval (seconds): ");
        
              String sInterval =interVal ;//scanner.nextLine();
              MonitorGUI.logMessage("Transmission interval (seconds):"+sInterval,Color.BLUE);

              short nInterval = 5;
              if (!sInterval.isEmpty()) nInterval = Short.parseShort(sInterval);

              System.out.println("Requesting " + nInterval + " second Transmission from monitor");
              MonitorGUI.logMessage("Requesting " + nInterval + " second Transmission from monitor",Color.BLUE);
              System.out.println();
             // System.out.println("Data will be written to CSV file AS3ExportData.csv in the same folder");
         //     MonitorGUI.logMessage("Data will be written to CSV file AS3ExportData.csv in the same folder",Color.BLUE);
              // Request transfer based on the DRI level of the monitor
              RequestTransfer(DataConstants.DRI_PH_DISPL, nInterval, DataConstants.DRI_LEVEL_2005);
              RequestTransfer(DataConstants.DRI_PH_DISPL, nInterval, DataConstants.DRI_LEVEL_2003);
              RequestTransfer(DataConstants.DRI_PH_DISPL, nInterval, DataConstants.DRI_LEVEL_2001);

              System.out.println("Press Escape button to Stop");

              Thread dataReaderThread = new Thread(() -> {
            	  
            	  //ReadBuffer();
                  while (true) {
                      if (serialPort.bytesAvailable() > 0) {
                          byte[] readBuffer = new byte[serialPort.bytesAvailable()];
                          int numRead = serialPort.readBytes(readBuffer, readBuffer.length);
                          System.out.print(new String(readBuffer, 0, numRead));
                          MonitorGUI.logMessage("Data Recieved : "+new String(readBuffer, 0, numRead),Color.BLUE);
                          byte[] copyarray = new byte[numRead];
                          for (int i = 0; i < readBuffer.length; i++)
                          {
                              copyarray[i] = readBuffer[i];
                              CreateFrameListFromByte(copyarray[i]);
                          }
                              System.out.println("inside data receving");
                              MonitorGUI.logMessage("inside data receving",Color.BLUE);
                              
                              if (FrameList.size() > 0)
                              {
                            	  
                            	  System.out.println("after data receving");
                            	  MonitorGUI.logMessage("after data receving",Color.BLUE);
                                  CreateRecordList();
                                  ReadSubRecords();
                                 
                                  FrameList.clear();
                                  RecordList.clear();
                                  //m_bList.RemoveRange(0, m_bList.Count);
                              }
                          //}
                      }

                      try {
                          Thread.sleep(100);
                      } catch (InterruptedException e) {
                          e.printStackTrace();
                      }
                  }
              });
              dataReaderThread.start();

              while (true) {
                  if (System.in.available() > 0 && System.in.read() == 27) { // ESC key is ASCII code 27
                      break;
                  }
              }
          } else {
              System.out.println("Error: Unable to open port.");
              MonitorGUI.logMessage("Error: Unable to open port.",Color.BLUE);
          }
      } catch (Exception ex) {
          System.out.println("Error opening/writing to serial port: " + ex.getMessage());
          MonitorGUI.logMessage("Error opening/writing to serial port: " + ex.getMessage(),Color.BLUE);
      } finally {
          if (serialPort != null && serialPort.isOpen()) {
              serialPort.closePort();
          }
      }
      
	}
	
	
	 private static Request request_ptr = new Request();
	    private static boolean m_transmissionstart;
	
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	public static void RequestTransfer(byte Trtype, short Interval, byte DRIlevel) {
        // Set Record Header
        request_ptr.hdr.r_len = 49;
        request_ptr.hdr.r_dri_level = DRIlevel;
        request_ptr.hdr.r_time = 0;
        request_ptr.hdr.r_maintype = DataConstants.DRI_MT_PHDB;

        request_ptr.hdr.sr_offset1 = 0;
        request_ptr.hdr.sr_type1 = 0;
        request_ptr.hdr.sr_offset2 = 0;
        request_ptr.hdr.sr_type2 = (byte) 0xFF;

        // Request transmission subrecord
        request_ptr.phdbr.phdb_rcrd_type = Trtype;
        request_ptr.phdbr.tx_interval = Interval;
        if (Interval != 0) {
            request_ptr.phdbr.phdb_class_bf =
                    DataConstants.DRI_PHDBCL_REQ_BASIC_MASK | DataConstants.DRI_PHDBCL_REQ_EXT1_MASK |
                    DataConstants.DRI_PHDBCL_REQ_EXT2_MASK | DataConstants.DRI_PHDBCL_REQ_EXT3_MASK;
        } else {
            request_ptr.phdbr.phdb_class_bf = 0x0000;
        }

        // Serialize request_ptr to a byte array
        ByteBuffer buffer = ByteBuffer.allocate(49);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putShort(request_ptr.hdr.r_len);
        buffer.put(request_ptr.hdr.r_dri_level);
        buffer.putInt(request_ptr.hdr.r_time);
        buffer.put(request_ptr.hdr.r_maintype);
        buffer.putShort(request_ptr.hdr.sr_offset1);
        buffer.put(request_ptr.hdr.sr_type1);
        buffer.putShort(request_ptr.hdr.sr_offset2);
        buffer.put(request_ptr.hdr.sr_type2);
        buffer.put(request_ptr.phdbr.phdb_rcrd_type);
        buffer.putShort(request_ptr.phdbr.tx_interval);
        buffer.putInt(request_ptr.phdbr.phdb_class_bf);

        byte[] data = buffer.array();
        WriteBuffer(data);

        m_transmissionstart = true;
    }
	
	
	  public static void WriteBuffer(byte[] txbuf) {
	        byte[] framebyte = {DataConstants.CTRLCHAR, (byte) (DataConstants.FRAMECHAR & DataConstants.BIT5COMPL), 0};
	        byte[] ctrlbyte = {DataConstants.CTRLCHAR, (byte) (DataConstants.CTRLCHAR & DataConstants.BIT5COMPL), 0};

	        byte check_sum = 0x00;
	        byte b1 = 0x00;
	        byte b2 = 0x00;

	        int txbuflen = txbuf.length + 1;
	        byte[] temptxbuff = new byte[txbuflen];

	        for (int j = 0; j < txbuflen; j++) {
	            temptxbuff[j] = 0;
	        }

	        temptxbuff[0] = DataConstants.FRAMECHAR;

	        int i = 1;

	        for (byte b : txbuf) {
	            switch (b) {
	                case DataConstants.FRAMECHAR:
	                    temptxbuff[i] = framebyte[0];
	                    temptxbuff[i + 1] = framebyte[1];
	                    i += 2;
	                    b1 += framebyte[0];
	                    b1 += framebyte[1];
	                    check_sum += b1;
	                    break;
	                case DataConstants.CTRLCHAR:
	                    temptxbuff[i] = ctrlbyte[0];
	                    temptxbuff[i + 1] = ctrlbyte[1];
	                    i += 2;
	                    b2 += ctrlbyte[0];
	                    b2 += ctrlbyte[1];
	                    check_sum += b2;
	                    break;
	                default:
	                    temptxbuff[i] = b;
	                    i++;
	                    check_sum += b;
	                    break;
	            }
	        }

	        int buflen = i;
	        byte[] finaltxbuff = new byte[buflen + 2];

	        for (int j = 0; j < buflen; j++) {
	            finaltxbuff[j] = temptxbuff[j];
	        }

	        finaltxbuff[buflen] = check_sum;
	        finaltxbuff[buflen + 1] = DataConstants.FRAMECHAR;
	        String asciiString = new String(finaltxbuff, StandardCharsets.US_ASCII);

	        System.out.println("ASCII representation: " + asciiString);
	        StringBuilder hexString = new StringBuilder();
	        for (byte b : finaltxbuff) {
	            hexString.append(String.format("%02X ", b)); // Two characters for each byte in hexadecimal
	        }
	        String hex = hexString.toString().trim(); // Trim to remove trailing space

	       // System.out.println("Hexadecimal representation: " + hex);
	        try {
	            serialPort.writeBytes(finaltxbuff, finaltxbuff.length);
	            MonitorGUI.logMessage("DATA LENGTH :"+finaltxbuff.length,Color.MAGENTA);
	            MonitorGUI.logMessage("DATA Sent "+hex,Color.BLUE);
	        } catch (Exception ex) {
	            System.err.println("Error opening/writing to serial port: " + ex.getMessage());
	        }
	    }
	
	
	  
	  
	  public static int ReadBuffer() {
		  int bytesreadtotal = 0;

          

          return bytesreadtotal;
	    }
	  
	  
	  
//	  private static void ClearReadBuffer() {
//	        // Implement the buffer clearing logic
//	    }
//
//	 
//	    private static void CreateRecordList() {
//	        // Implement the record list creation logic
//	    }
//
//	    private static void ReadSubRecords() {
//	        // Implement the sub-records reading logic
//	    }

	    
	    private static boolean m_fstart = true;
	    private static boolean m_storestart = false;
	    private static boolean m_storeend = false;
	    private  static boolean m_bitshiftnext = false;
	    private static List<Byte> m_bList = new ArrayList();
	    
	    public static List<Byte[]> FrameList = new ArrayList<Byte[]>();
	    public static List<DatexRecordType> RecordList = new ArrayList();
	    
	    private static StringBuilder m_strBuilder = new StringBuilder();

public static void CreateFrameListFromByte(byte b)
	    {
	System.out.println("RRR"+b);
	
	  if (b == DataConstants.FRAMECHAR && m_fstart)
	  {
		  System.out.println(1);
	  	m_fstart = false;
	  	m_storestart = true;
	  }
	  else if (b == DataConstants.FRAMECHAR && (m_fstart == false))
	  {
		  System.out.println(2);
	  	m_fstart = true;
	  	m_storeend = true;
	  	m_storestart = false;
	  	if (b != DataConstants.FRAMECHAR) m_bList.add(b);
	  }

	  if (m_storestart == true)
	  { System.out.println(3);
	  	if (b == DataConstants.CTRLCHAR)
	  		
	  		m_bitshiftnext = true;
	  	else
	  	{ System.out.println(4);
	  		if (m_bitshiftnext == true)
	  		{
	  			b |= DataConstants.BIT5;
	  			m_bitshiftnext = false;
	  			m_bList.add(b);
	  		}
	  		else if (b != DataConstants.FRAMECHAR) m_bList.add(b);
	  	}
	  	
	  }
	  else if (m_storeend == true)
	  { System.out.println(5);
	  	int framelen = m_bList.size();
	  	if (framelen != 0)
	  	{
	  		Byte[] bArray = new Byte[framelen];
	  		
	  		Byte[] byteArray = new Byte[m_bList.size()];

	  		byteArray = m_bList.toArray(new Byte[0]);
	  		bArray = (Byte[]) m_bList.toArray();
	  		System.out.println(bArray);
	  		//Calculate checksum
	  		byte checksum = 0x00;
	  		for (int j = 0; j < (framelen - 1); j++)
	  		{
	  			checksum += bArray[j];
	  		}
	  		if (checksum == bArray[framelen - 1])
	  		{
	  			FrameList.add(bArray);
	  		}
	  		m_bList.clear();
	  		m_storeend = false;
	  	}
	  	else
	  	{ System.out.println(6);
	  		m_storestart = true;
	  		m_storeend = false;
	  		m_fstart = false;
	  	}
	  	
	  }
	  
	  System.out.println(FrameList.size());
	  MonitorGUI.logMessage("FrameList : "+FrameList,Color.BLUE);
	    }








public static void CreateRecordList() {  
    int recordDataSize = 0;
    byte[] fullRecord = new byte[1490];
    
    for (Byte[] fArray : FrameList) {
        DatexRecordType recordDtx = new DatexRecordType();
        
        // Initialize fullRecord with 0x00
        for (int i = 0; i < fullRecord.length; i++) {
            fullRecord[i] = 0x00;
        }
        
        recordDataSize = fArray.length;
        
        // Copy fArray into fullRecord
        for (int n = 0; n < fArray.length && recordDataSize < 1490; n++) {
            fullRecord[n] = fArray[n];
        }
        
        // Copy fullRecord into recordDtx
        ByteBuffer buffer = ByteBuffer.wrap(fullRecord).order(ByteOrder.LITTLE_ENDIAN);
        // Assume that you have a method to parse the buffer into the DatexRecordType object
        
        recordDtx = parseBufferToRecord(buffer);
        
        RecordList.add(recordDtx);
    }
}


private static DatexRecordType parseBufferToRecord(ByteBuffer buffer) {
	// TODO Auto-generated method stub
	return null;
}


public static void ReadSubRecords() {
	
	System.out.println("Inside read subrecords");
    for (DatexRecordType dxRecord : RecordList) {
        short dxRecordMainType = dxRecord.hdr.getR_maintype();

        if (dxRecordMainType == DataConstants.DRI_MT_PHDB) {
            short[] srOffsetArray = {
                dxRecord.hdr.getSr_offset1(), dxRecord.hdr.getSr_offset2(), dxRecord.hdr.getSr_offset3(), dxRecord.hdr.getSr_offset4(),
                dxRecord.hdr.getSr_offset5(), dxRecord.hdr.getSr_offset6(), dxRecord.hdr.getSr_offset7(), dxRecord.hdr.getSr_offset8()
            };
            byte[] srTypeArray = {
                dxRecord.hdr.getSr_type1(), dxRecord.hdr.getSr_type2(), dxRecord.hdr.getSr_type3(), dxRecord.hdr.getSr_type4(),
                dxRecord.hdr.getSr_type5(), dxRecord.hdr.getSr_type6(), dxRecord.hdr.getSr_type7(), dxRecord.hdr.getSr_type8()
            };

            long unixTime = dxRecord.hdr.getR_time();
            DriPhdb phdataPtr = new DriPhdb();

            writeNumericHeaders();

            for (int i = 0; i < 8 && (srTypeArray[i] != (byte) 0xFF); i++) {
                int offset = srOffsetArray[i];

                byte[] buffer = new byte[270];
                System.arraycopy(dxRecord.getData(), 4 + offset, buffer, 0, 270);

                ByteBuffer byteBuffer = ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN);
            	short heartRate = (short) (((buffer[7] & 0xFF) << 8) | buffer[6] & 0xFF);  //to get incoming values from monitor
            	
            	System.out.println("heart Rate : "+heartRate);
                switch (i) {
                    case 0:
                        parseBufferToBasic(byteBuffer, phdataPtr.getBasic());
                        break;
                    case 1:
                        parseBufferToExt1(byteBuffer, phdataPtr.ext1);
                        break;
                    case 2:
                       // parseBufferToExt2(byteBuffer, phdataPtr.ext2);
                        break;
                    case 3:
                      //  parseBufferToExt3(byteBuffer, phdataPtr.ext3);
                        break;
                }
            }

            LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(unixTime), ZoneId.systemDefault());

            System.out.println();
            System.out.println("Time: " + dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            m_strBuilder.append(dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))).append(',');
            m_strBuilder.append(dateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"))).append(',');
        
            showBasicSubRecord(phdataPtr);
            showExt1and2SubRecord(phdataPtr);
        }
    }
}


public static void parseBufferToBasic(ByteBuffer buffer, BasicPhdbType basic) {
    // Example: Assuming the buffer contains an int followed by a float
 //   basic.co2 = buffer.getInt();
 //   basic.field2 = buffer.getFloat();
	
	System.out.println(buffer.getInt());
    // Parse more fields as needed
}

public static void parseBufferToExt1(ByteBuffer buffer, Ext1Phdb ext) {
    // Example: Assuming the buffer contains an int followed by a double
   // ext.ecg = buffer.getInt();
  //  ext.ecg12 = buffer.getDouble();
    // Parse more fields as needed
}



public static void writeNumericHeaders() {
	
	
    if (m_transmissionstart) {
        m_strBuilder.append("VitalSignsCapture v1.005\n");
        m_strBuilder.append("Datex AS3 Monitor\n");

        m_strBuilder.append("Date,Time,Heart Rate(/min),Systolic BP(mmHg),Diastolic BP(mmHg),Mean BP(mmHg),SpO2(%),ETCO2(mmHg),");
        m_strBuilder.append("AA ET, AA FI, AA MAC SUM, AA, O2 FI, N2O FI, N2O ET, RR, T1, T2, P1 HR, P1 Sys, P1 Dia, P1 Mean, P2 HR, P2 Sys, P2 Dia, P2Mean,");
        m_strBuilder.append("PPeak, PPlat, TV Exp, TV Insp, Peep, MV Exp, Compliance, RR,");
        m_strBuilder.append("ST II(mm),ST V5(mm),ST aVL(mm),");
        m_strBuilder.append("SE, RE, ENTROPY BSR, BIS, BIS BSR, BIS EMG, BIS SQI\n");
    }
    
    
    
    
}


public static void showBasicSubRecord(DriPhdb driSR) {
	
	System.out.println("Inside basic data read");
    short so1 = driSR.getBasic().ecg.hr;
    short so2 = driSR.getBasic().nibp.sys;
    short so3 = driSR.getBasic().nibp.dia;
    short so4 = driSR.getBasic().nibp.mean;
    short so5 = driSR.getBasic().SpO2.SpO2;
    short so6 = driSR.getBasic().co2.et;

    String s1 = validateDataFormatString(so1, 1, true);
    validateAddData(so1, 1, true);

    String s2 = validateDataFormatString(so2, 0.01, true);
    validateAddData(so2, 0.01, true);

    String s3 = validateDataFormatString(so3, 0.01, true);
    validateAddData(so3, 0.01, true);

    String s4 = validateDataFormatString(so4, 0.01, true);
    validateAddData(so4, 0.01, true);

    String s5 = validateDataFormatString(so5, 0.01, true);
    validateAddData(so5, 0.01, true);

    double et = (so6 * driSR.getBasic().co2.amb_press);
    validateAddData(et, 0.00001, true);
    String s6 = validateDataFormatString(et, 0.00001, true);

    short so7 = driSR.getBasic().aa.et;
    short so8 = driSR.getBasic().aa.fi;
    short so9 = driSR.getBasic().aa.macSum;
    int so10 = driSR.getBasic().aa.hdr.labelInfo;

    validateAddData(so7, 0.01, false);
    validateAddData(so8, 0.01, false);
    validateAddData(so9, 0.01, false);

    String s10 = "";

    switch (so10) {
        case 0:
            s10 = "Unknown";
            break;
        case 1:
            s10 = "None";
            break;
        case 2:
            s10 = "HAL";
            break;
        case 3:
            s10 = "ENF";
            break;
        case 4:
            s10 = "ISO";
            break;
        case 5:
            s10 = "DES";
            break;
        case 6:
            s10 = "SEV";
            break;
    }

    m_strBuilder.append(s10);
    m_strBuilder.append(',');

    double so11 = driSR.getBasic().o2.fi;
    double so12 = driSR.getBasic().n2o.fi;
    double so13 = driSR.getBasic().n2o.et;
    double so14 = driSR.getBasic().co2.rr;
    double so15 = driSR.getBasic().t1.temp;
    double so16 = driSR.getBasic().t2.temp;

    double so17 = driSR.getBasic().p1.hr;
    double so18 = driSR.getBasic().p1.sys;
    double so19 = driSR.getBasic().p1.dia;
    double so20 = driSR.getBasic().p1.mean;
    double so21 = driSR.getBasic().p2.hr;
    double so22 = driSR.getBasic().p2.sys;
    double so23 = driSR.getBasic().p2.dia;
    double so24 = driSR.getBasic().p2.mean;

    double so25 = driSR.getBasic().flow_vol.ppeak;
    double so26 = driSR.getBasic().flow_vol.pplat;
    double so27 = driSR.getBasic().flow_vol.tvExp;
    double so28 = driSR.getBasic().flow_vol.tvInsp;
    double so29 = driSR.getBasic().flow_vol.peep;
    double so30 = driSR.getBasic().flow_vol.mvExp;
    double so31 = driSR.getBasic().flow_vol.compliance;
    double so32 = driSR.getBasic().flow_vol.rr;

    validateAddData(so11, 0.01, false);
    validateAddData(so12, 0.01, false);
    validateAddData(so13, 0.01, false);
    validateAddData(so14, 1, false);
    validateAddData(so15, 0.01, false);
    validateAddData(so16, 0.01, false);

    validateAddData(so17, 1, true);
    validateAddData(so18, 0.01, true);
    validateAddData(so19, 0.01, true);
    validateAddData(so20, 0.01, true);
    validateAddData(so21, 1, true);
    validateAddData(so22, 0.01, true);
    validateAddData(so23, 0.01, true);
    validateAddData(so24, 0.01, true);

    validateAddData(so25, 0.01, true);
    validateAddData(so26, 0.01, true);
    validateAddData(so27, 0.1, true);
    validateAddData(so28, 0.1, true);
    validateAddData(so29, 0.01, true);
    validateAddData(so30, 0.01, false);
    validateAddData(so31, 0.01, true);
    validateAddData(so32, 1, true);

    String s9 = validateDataFormatString(so9, 0.01, false);
    String s15 = validateDataFormatString(so15, 0.01, false);
    String s16 = validateDataFormatString(so16, 0.01, false);

    String s18 = validateDataFormatString(so18, 0.01, true);
    String s19 = validateDataFormatString(so19, 0.01, true);
    String s20 = validateDataFormatString(so20, 0.01, true);

    String s22 = validateDataFormatString(so22, 0.01, true);
    String s23 = validateDataFormatString(so23, 0.01, true);
    String s24 = validateDataFormatString(so24, 0.01, true);

    System.out.printf("ECG HR %s/min NIBP %s/%s(%s)mmHg SpO2 %s%% ETCO2 %smmHg\n", s1, s2, s3, s4, s5, s6);
    System.out.printf("IBP1 %s/%s(%s)mmHg IBP2 %s/%s(%s)mmHg MAC %s T1 %s°C T2 %s°C\n", s18, s19, s20, s22, s23, s24, s9, s15, s16);
    	MonitorGUI.logMessage("ECG HR %s/min NIBP %s/%s(%s)mmHg SpO2 %s%% ETCO2 %smmHg\n"+s1+s2+s3+s4+s5+s6,Color.MAGENTA);
    	MonitorGUI.logMessage("IBP1 %s/%s(%s)mmHg IBP2 %s/%s(%s)mmHg MAC %s T1 %s°C T2 %s°C\n"+s18+ s19+ s20+ s22+ s23+ s24+ s9+ s15+ s16,Color.MAGENTA);
    m_transmissionstart = false;
}


private StringBuilder stringBuilder = new StringBuilder();
private static boolean transmissionStart = false;

public static void showExt1and2SubRecord(DriPhdb driSR) {
    short so1 = driSR.ext1.ecg12.stII;
    short so2 = driSR.ext1.ecg12.stV5;
    short so3 = driSR.ext1.ecg12.stAVL;

    String pathCsv = Paths.get("").toAbsolutePath().toString() + "/AS3ExportData.csv";

    validateAddData(so1, 0.01, false);
    String s1 = validateDataFormatString(so1, 0.01, false);

    validateAddData(so2, 0.01, false);
    String s2 = validateDataFormatString(so2, 0.01, false);

    validateAddData(so3, 0.01, false);
    String s3 = validateDataFormatString(so3, 0.01, false);

    short so4 = driSR.ext2.ent.eeg_ent;
    short so5 = driSR.ext2.ent.emg_ent;
    short so6 = driSR.ext2.ent.bsr_ent;
    short so7 = driSR.ext2.eeg_bis.bis;
    short so8 = driSR.ext2.eeg_bis.sr_val;
    short so9 = driSR.ext2.eeg_bis.emg_val;
    short so10 = driSR.ext2.eeg_bis.sqi_val;

    validateAddData(so4, 1, true);
    validateAddData(so5, 1, true);
    validateAddData(so6, 1, true);
    validateAddData(so7, 1, true);
    validateAddData(so8, 1, true);
    validateAddData(so9, 1, true);
    validateAddData(so10, 1, true);

  //  exportToCSVFile(pathCsv);

    System.out.printf("ST II %.1fmm ST V5 %.1fmm ST aVL %.1fmm\n", Double.parseDouble(s1), Double.parseDouble(s2), Double.parseDouble(s3));

    // Clear StringBuilder and reset transmission start flag
    m_strBuilder.setLength(0);
    transmissionStart = false;
}


public static String validateDataFormatString(Object value, double decimalShift, boolean roundData) {
    // Convert the value to an integer
    int val = Integer.parseInt(value.toString());
    // Convert the value to a double and apply the decimal shift
    double dval = Double.parseDouble(value.toString()) * decimalShift;
    
    // Round the value if required
    if (roundData) {
        dval = Math.round(dval);
    }

    // Convert the double value to a string
    String str = Double.toString(dval);

    // Check if the value is below the invalid limit
    if (val < DataConstants.DATA_INVALID_LIMIT) {
        str = "-";
    }

    return str;
}

public static boolean validateAddData(Object value, double decimalShift, boolean roundData) {
    // Convert the value to an integer
    int val = Integer.parseInt(value.toString());
    // Convert the value to a double and apply the decimal shift
    double dval = Double.parseDouble(value.toString()) * decimalShift;

    // Round the value if required
    if (roundData) {
        dval = Math.round(dval);
    }

    // Convert the double value to a string
    String str = Double.toString(dval);

    // Check if the value is below the invalid limit
    if (val < DataConstants.DATA_INVALID_LIMIT) {
        str = "-";
        m_strBuilder.append(str);
        m_strBuilder.append(',');
        return false;
    }

    m_strBuilder.append(str);
    m_strBuilder.append(',');
    return true;
}
}




