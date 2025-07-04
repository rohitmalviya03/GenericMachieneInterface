package Server.Mic;

import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Server.ABC;
import Server.AIIMSLAB;
import Server.HL7MessageGenerator;
import Server.insertOrganismData;
import Server.result_parameter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class MestriaReciver {

	static final String FILE_NAME = "./machineLog.txt"; // File to save the data
	   private static final byte START_BLOCK = 0x0B; // VT
	    private static final byte END_BLOCK = 0x1C;   // FS
	    private static final byte CARRIAGE_RETURN = 0x0D; // CR


		public static void handleMbClient(Socket clientSocket) throws ClassNotFoundException {
			new Thread(() -> {
				try {
					InputStream input = clientSocket.getInputStream();
					OutputStream output = clientSocket.getOutputStream();

					BufferedReader reader = new BufferedReader(new InputStreamReader(input));
					PrintWriter writer = new PrintWriter(output, true);

					String line;
					StringBuilder receivedData = new StringBuilder();


					int order_packet_buffer_counter = 0;
					int red = -1;
					byte[] buffer = new byte[800 * 1024]; // a read buffer of 5KiB
					byte[] redData;
					StringBuilder clientData = new StringBuilder();
					String redDataText = "";

					while ((red = clientSocket.getInputStream().read(buffer)) > -1) // **Code Starts For Receiving Client
						// Messages
					{					

						redData = new byte[red];

						System.arraycopy(buffer, 0, redData, 0, red);

						redDataText = new String(redData, "UTF-8"); // assumption that client sends data UTF-8 encoded
						System.out.println(redDataText);



						//Stream<T> stream;


						// When data is received, log it and send back an ACK
						String receivedMessage = redDataText.toString();
						System.out.println("Received message: " + receivedMessage);
						// AIIMSLAB.updateStatusLabel("Data received: " + receivedMessage);
						AIIMSLAB.saveToFile("Received : "+receivedMessage, FILE_NAME);
						// Send ACK response
						String ackResponse = AIIMSLAB.generateAckResponse(receivedMessage);
						//if(!ackResponse.equals("NO_ACK")) {
						writer.println(ackResponse);  // Send ACK response
						writer.flush();
						System.out.println("Sent ACK response: " + ackResponse);
						AIIMSLAB.saveToFile("Sent ACK response: " + ackResponse, FILE_NAME);
						//}

						//Process Result



						String[] msgPart=receivedMessage.split("\r");

						String[] mshpart;


						String[] msgtypeField;
						String msgType="";

						//  AIIMSLAB.saveToFile("Recieved MSG Type : " + msgType, FILE_NAME);


						String[] SPMMSGFILED;

						String[] msgSection = null;
						String sampleNo="";
						String resultTimeStamp="";
						String sampleType="";
						String organismName="";
						String organismCode="";

						String micValue="";
						String[] mainresult;
						String[] antiBiotic;
						Boolean isResPos=true;

						//					for(int i=0;i<msgPart.length;i++) {
						//						System.out.println("MSG SECTION:: "+msgPart[i]);
						//						
						//						
						//						msgSection=msgPart[i].split("\\|");
						//				}

						JSONObject antibioticResult = new JSONObject();
						JSONObject mainJson = new JSONObject();
						JSONArray antibiotics = new JSONArray();
						JSONArray antibioticsArray = new JSONArray();
						List<result_parameter> resultData = new ArrayList<>();

						
						
						
						
						

						for(int j=0;j<msgPart.length;j++) {

							
							

							msgSection=msgPart[j].split("\\|");
							for(int i =0;i<msgSection.length;i++) {

								result_parameter resultUnit = new result_parameter();
								msgSection[i]=msgSection[i].replaceAll("\\n", "");
								if(msgSection[i].equals("MSH")) {
									msgType=msgSection[8];
									resultTimeStamp=msgSection[6];
									System.out.println("MSG TYPE: "+msgType);
									AIIMSLAB.saveToFile("MSG TYPE: "+msgType, FILE_NAME);
									continue;

								}

								else if(msgSection[i].equals("SPM")) {
									sampleNo=msgSection[2];
									String[] data=msgSection[4].split("\\^");
									sampleType=data[0];
									System.out.println("SAMPLE NO :"+sampleNo);
									System.out.println("SAMPLLE Type"+sampleType);
									AIIMSLAB.saveToFile("SAMPLE NO :"+sampleNo, FILE_NAME);
									AIIMSLAB.saveToFile("SAMPLLE Type"+sampleType, FILE_NAME);

									//resultData.add(resultUnit);

								}

								else if(msgSection[i].equals("OBX")) {
									if(msgSection[1].equals("1")) {
										String splitOrg=msgSection[5]; 

										String[] data=splitOrg.split("\\^");
										organismName=data[1];
										organismCode=data[0];
										System.out.println("Organism type :"+organismName);
										AIIMSLAB.saveToFile("Organism type :"+organismName , FILE_NAME);

										if(msgSection[5].contains("Negative")) {
//
											isResPos=false;
//											//continue;
										}
										//resultUnit.setOrganismName(organismName);
										//resultUnit.setOrganismCode(organismCode);
										//resultData.add(resultUnit);
									}
									else {
										if(Integer.parseInt(msgSection[1])>2 && msgSection[2].equals("SN")) {
											micValue=msgSection[5];
											resultUnit.setPatient_id(sampleNo);
											System.out.println("MIC Value :"+micValue);
											AIIMSLAB.saveToFile("MIC Value :"+micValue, FILE_NAME);
											micValue.replaceAll("\\^", "");
											resultUnit.setMiCValue(micValue);
											//resultData.add(resultUnit);
											mainresult=msgSection[8].split("\\^");
											antiBiotic=msgSection[3].split("\\^");
											System.out.println("antiBiotic LIS CODE :"+antiBiotic[0]);
											AIIMSLAB.saveToFile("antiBiotic :"+antiBiotic[0], FILE_NAME);
											System.out.println("antiBiotic Name :"+antiBiotic[1]);
											AIIMSLAB.saveToFile("antiBiotic :"+antiBiotic[1], FILE_NAME);
											//System.out.println("Result  :"+mainresult[0] +" :: "+mainresult[1] );





											resultUnit.setSample_type(sampleType);
											resultUnit.setOrganismName(organismName);
											resultUnit.setOrganismCode(organismCode);
											resultUnit.setAntiBioticCode(antiBiotic[0]);
											resultUnit.setAntiBiotic_Name(antiBiotic[1]);
											resultUnit.setResult(mainresult[0]);
											resultData.add(resultUnit);



										}
										else if  (isResPos==false) {
											micValue=msgSection[5];
											resultUnit.setPatient_id(sampleNo);
											System.out.println("MIC Value :"+micValue);
											AIIMSLAB.saveToFile("MIC Value :"+micValue, FILE_NAME);
											resultUnit.setMiCValue(micValue);
											//resultData.add(resultUnit);
											mainresult=msgSection[8].split("\\^");
											antiBiotic=msgSection[3].split("\\^");
											System.out.println("antiBiotic LIS CODE :"+antiBiotic[0]);
											AIIMSLAB.saveToFile("antiBiotic :"+antiBiotic[0], FILE_NAME);
											System.out.println("antiBiotic Name :"+antiBiotic[1]);
											AIIMSLAB.saveToFile("antiBiotic :"+antiBiotic[1], FILE_NAME);
											//System.out.println("Result  :"+mainresult[0] +" :: "+mainresult[1] );





											resultUnit.setSample_type(sampleType);
											resultUnit.setOrganismName(organismName);
											resultUnit.setOrganismCode("");
											resultUnit.setAntiBioticCode("");
											resultUnit.setAntiBiotic_Name("");
											resultUnit.setResult("");
											resultData.add(resultUnit);
										}
									}



								}
							}

							//System.out.println(resultData);





						}
						
						if(isResPos)
						{
							resultData= new ArrayList<>();
						for(int j=0;j<msgPart.length;j++) {

							
									

							msgSection=msgPart[j].split("\\|");
							for(int i =0;i<msgSection.length;i++) {

								result_parameter resultUnit = new result_parameter();
								msgSection[i]=msgSection[i].replaceAll("\\n", "");
								if(msgSection[i].equals("MSH")) {
									msgType=msgSection[8];
									resultTimeStamp=msgSection[6];
									System.out.println("MSG TYPE: "+msgType);
									AIIMSLAB.saveToFile("MSG TYPE: "+msgType, FILE_NAME);
									continue;

								}

								else if(msgSection[i].equals("SPM")) {
									sampleNo=msgSection[3];
									String[] data=msgSection[4].split("\\^");
									sampleType=data[0];
									System.out.println("SAMPLE NO :"+sampleNo);
									System.out.println("SAMPLLE Type"+sampleType);
									AIIMSLAB.saveToFile("SAMPLE NO :"+sampleNo, FILE_NAME);
									AIIMSLAB.saveToFile("SAMPLLE Type"+sampleType, FILE_NAME);

									//resultData.add(resultUnit);

								}

								else if(msgSection[i].equals("OBX")) {
									if(msgSection[1].equals("1")) {
										String splitOrg=msgSection[5]; 

										String[] data=splitOrg.split("\\^");
										organismName=data[1];
										organismCode=data[0];
										System.out.println("Organism type :"+organismName);
										AIIMSLAB.saveToFile("Organism type :"+organismName , FILE_NAME);

//										if(msgSection[5].contains("Negative")) {
//
//											isResPos=false;
//											//continue;
//										}
										//resultUnit.setOrganismName(organismName);
										//resultUnit.setOrganismCode(organismCode);
										//resultData.add(resultUnit);
									}
									else {
										if(Integer.parseInt(msgSection[1])>2 && msgSection[2].equals("SN")) {
											micValue=msgSection[5];
											resultUnit.setPatient_id(sampleNo);
											System.out.println("MIC Value :"+micValue);
											AIIMSLAB.saveToFile("MIC Value :"+micValue, FILE_NAME);
											resultUnit.setMiCValue(micValue);
											//resultData.add(resultUnit);
											mainresult=msgSection[8].split("\\^");
											antiBiotic=msgSection[3].split("\\^");
											System.out.println("antiBiotic LIS CODE :"+antiBiotic[0]);
											AIIMSLAB.saveToFile("antiBiotic :"+antiBiotic[0], FILE_NAME);
											System.out.println("antiBiotic Name :"+antiBiotic[1]);
											AIIMSLAB.saveToFile("antiBiotic :"+antiBiotic[1], FILE_NAME);
											//System.out.println("Result  :"+mainresult[0] +" :: "+mainresult[1] );





											resultUnit.setSample_type(sampleType);
											resultUnit.setOrganismName(organismName);
											resultUnit.setOrganismCode(organismCode);
											resultUnit.setAntiBioticCode(antiBiotic[0]);
											resultUnit.setAntiBiotic_Name(antiBiotic[1]);
											resultUnit.setResult(mainresult[0]);
										
											resultData.add(resultUnit);



										}
									
									}



								}
							}

							//System.out.println(resultData);





						}

					}

						//end Resullt Process




						JSONArray jsonArray = new JSONArray();
						JSONObject sampleObject = new JSONObject();
						Set<String> uniqueEntries = new HashSet<>();

						for (result_parameter result : resultData) {
							try {
								// Only set sampleNo and sampleType once
								if (sampleObject.length() == 0) {
									// sampleObject.put("results", new JSONArray()); 
									result.setPatient_id(result.getPatient_id().replaceAll(" ",""));
									Map mp=getEssentialDetailsFromDb(result.getPatient_id());
									sampleObject.put("resultTimeStamp", resultTimeStamp);
									//sampleObject.put("sampleType", result.getSample_type());
									sampleObject.put("sampleNo", result.getPatient_id());
									
									//pat detilas add//
									
									String patientId = (String) mp.get("hrgnum_puk");
									String requisitionNo = (String) mp.get("hivt_req_do");
									String orderId = (String) mp.get("his_order_id");
									String firstNameFromMap = (String) mp.get("hrgstr_fname");
									String middleNameFromMap = (String) mp.get("hrgstr_mname");
									String lastNameFromMap = (String) mp.get("hrgstr_lname");
									String genderFromMap = (String) mp.get("patient_gender");
									String sampleTypeFromMap = (String) mp.get("hivnum_sample_type");
									
									String sampleCode = (String) mp.get("sampleCode");
									
									
									String patientTypeFromMap = (String) mp.get("hivt_pat_type");
									String collectionDateFromMap = (String) mp.get("pat_sample_collection_date");
									String birthDateFromMap = (String) mp.get("patient_birth_date");
									String testStatusFromMap = (String) mp.get("org_test_status");
									String orderCreatedAtFromMap = (String) mp.get("order_created_at");
									String resultCreatedAtFromMap = (String) mp.get("result_created_at");
									String modifiedAtFromMap = (String) mp.get("modify_at");
									

									
									sampleObject.put("hivtnumReqDno", mp.getOrDefault("hivt_req_do", "-"));
									sampleObject.put("labCode", mp.getOrDefault("gnum_lab_code", "-"));
									sampleObject.put("testCode", mp.getOrDefault("gnum_test_code", "-"));
									sampleObject.put("machineCode", mp.getOrDefault("gnum_machine_code", "-"));
									sampleObject.put("genderCode",genderFromMap);
									sampleObject.put("age", mp.getOrDefault("age", "-"));
									sampleObject.put("syncFlag", mp.getOrDefault("sync_flag", "-"));
									sampleObject.put("entryDate", mp.getOrDefault("gdt_entry_date", "-"));
									sampleObject.put("modifyDate", mp.getOrDefault("gdt_modify_date", "-"));
									sampleObject.put("patName", mp.getOrDefault("hrgstr_fname", "-"));
									sampleObject.put("patCrNo", patientId);
									sampleObject.put("sampleCode", sampleCode);
									
									
									sampleObject.put("sampleType",  sampleTypeFromMap);
									
									
									sampleObject.put("labCode", mp.getOrDefault("labCode", "-"));

									sampleObject.put("testCode", mp.getOrDefault("testCode" , "-"));

									sampleObject.put("deptCode", mp.getOrDefault("deptCode","-"));

									
									System.out.println("Sample Object: " + sampleObject.toString());
									
									//
									
									AIIMSLAB.saveToFile("Sample Object: " + sampleObject.toString(),"");




								}

								// Create the antibiotic result object
								JSONObject jsonObject = new JSONObject();
								jsonObject.put("antiBioticName", result.getAntiBiotic_Name());
								jsonObject.put("organismCode", result.getOrganismCode());
								jsonObject.put("organismName", result.getOrganismName());
								jsonObject.put("micValue", result.getMiCValue());
								jsonObject.put("antiBioticCode", result.getAntiBioticCode());
								jsonObject.put("result", result.getResult());

								String uniqueKey = result.getAntiBioticCode() + "|" + result.getOrganismName();

								// Check if this antibiotic result combination already exists
								if (!uniqueEntries.contains(uniqueKey)) {
									uniqueEntries.add(uniqueKey);

									// Check if "results" key exists and is a JSONArray
									if (!sampleObject.has("results")) {
										sampleObject.put("results", new JSONArray());
									}

									// Add jsonObject to the "results" JSONArray
									sampleObject.getJSONArray("results").put(jsonObject);
								}

							} catch (JSONException e) {
								e.printStackTrace();
							}
						}

						// Add the sample object to the array if it has results
						try {
							if (sampleObject.length() > 0 && sampleObject.getJSONArray("results").length() > 0) {
								jsonArray.put(sampleObject);
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						// Print or save the JSON array
						try {
							System.out.println(jsonArray.toString(4)); 
							
							AIIMSLAB.saveToFile(jsonArray.toString(4),"");

						} catch (JSONException e) {
							e.printStackTrace();
						}



						//Calling method to insert organism data
						/*
						insertOrganismData.insertParaValue(sampleNo,organismCode,resultData);  //To store data in Local DB   //Working1


						  try {

								insertOrganismData.insertParaValueJson(sampleNo,jsonArray.toString(4));
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}  //insertjson
						try {
							insertOrganismData.sendPostRequest(jsonArray.toString(4),sampleNo);  //To Store data in UAT or Prod Database  //Working
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}


						 */
						
						
						
						
						//update result status and entry date of result after send result the HIS
			            
						
						
						
						
						String updtResStatus ="update mb_orders_dtl SET result_created_at = CURRENT_TIMESTAMP ,org_test_status=? where  hivt_req_do =? and hivnum_sample_no =?";
						

						  String sqliteDbUrl = "jdbc:sqlite:vitrossample.sqlite";

						    try (Connection conn = DriverManager.getConnection(sqliteDbUrl);
						         PreparedStatement stmt = conn.prepareStatement(updtResStatus)) {
						    	stmt.setString(1, "2"); //result updated
						    	
						    	stmt.setString(2, String.valueOf(sampleObject.get("hivtnumReqDno")));
					            
					            stmt.setString(3, String.valueOf(sampleObject.get("sampleNo")));
					            //stmt.setString(1, String.valueOf(sampleObject.get("hivtnumReqDno")));
					            stmt.executeUpdate();
						    }
						    
					catch (Exception e) {
						
						e.printStackTrace();
						AIIMSLAB.saveToFile("Issue in updating the reesult statussss","");
						// TODO: handle exception
					}
					
						
						//end saving results
						if (jsonArray.length() > 0 && isResPos==true) {
						    try {
						        sendPostRequest(jsonArray.toString(4), sampleNo,"1");  // Pretty prints with indent
						    } catch (JSONException e) {
						        e.printStackTrace();
						    }
						} else if(isResPos==false) {
						    System.out.println("Only BC Result Negative");
						    try {
						        sendPostRequest(jsonArray.toString(4), sampleNo,"0");  // Pretty prints with indent
						    } catch (JSONException e) {
						        e.printStackTrace();
						    }
							AIIMSLAB.saveToFile("Only BC Result Negative","");
						}

					}

				} catch (IOException e) {
					AIIMSLAB.updateStatusLabel("Error handling client: " + e.getMessage());
					AIIMSLAB.saveToFile("Stack Trace: " + AIIMSLAB.getStackTraceAsString(e), FILE_NAME);

					e.printStackTrace();
				} 


				/* finally {
					try {
						clientSocket.close();  // Close the client socket after communication
						updateConnectionStatusLabel("Client disconnected.");
					} catch (IOException ex) {
						AIIMSLAB.updateStatusLabel("Error closing client socket: " + ex.getMessage());
					}
				}*/
			}).start();
		}


		private static Map<String, String> getEssentialDetailsFromDb(String SampleNO) {
			
			Map<String ,String> patOrderDetils = new HashMap<String, String>();
			  String FETCH_SQL = "SELECT * FROM mb_orders_dtl WHERE hivnum_sample_no = ?";

			  String sqliteDbUrl = "jdbc:sqlite:vitrossample.sqlite";

			    try (Connection conn = DriverManager.getConnection(sqliteDbUrl);
			         PreparedStatement stmt = conn.prepareStatement(FETCH_SQL)) {
		            stmt.setString(1, SampleNO);

		            

					/*
					 * String insertSQL = "INSERT INTO mb_orders_dtl (" +
					 * "his_order_id, patage, labcode, testcode, deptcode, sampleNo, sampleCode, samcollDate, "
					 * +
					 * "pataddress, patward, reqdno, patient_id, patient_fname, patient_mname, patient_lname, "
					 * +
					 * "patient_sex, patdob, phone_number, email_id, patient_type, center_id, test_id, test_name, sampletypename, machineId, testparams) "
					 * +
					 * "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
					 * ;
					 */
		            
		            
		            
		            try (ResultSet rs = stmt.executeQuery()) {
		            	 if (rs.next()) {
		            		 System.out.println("----------------PAT Details -----------------");
		                     System.out.println("Patient ID: " + rs.getString("hrgnum_puk"));
		                     System.out.println("Requisition No: " + rs.getString("hivt_req_do"));
		                     System.out.println("Order ID: " + rs.getString("his_order_id"));
		                     System.out.println("First Name: " + rs.getString("hrgstr_fname"));
		                     System.out.println("Middle Name: " + rs.getString("hrgstr_mname"));
		                     System.out.println("Last Name: " + rs.getString("hrgstr_lname"));
		                     System.out.println("Gender: " + rs.getString("patient_gender"));
		                     System.out.println("Sample Type: " + rs.getString("hivnum_sample_type"));
		                  
		                     System.out.println("Sample Type: " + rs.getString("sampleCode"));
			                  
		                     
		                     System.out.println("Patient Type: " + rs.getString("hivt_pat_type"));
		                     System.out.println("Collection Date: " + rs.getString("pat_sample_collection_date"));
		                     System.out.println("Birth Date: " + rs.getString("patient_birth_date"));
		                     System.out.println("Test Status: " + rs.getInt("org_test_status"));
		                     System.out.println("Order Created At: " + rs.getString("order_created_at"));
		                     System.out.println("Result Created At: " + rs.getString("result_created_at"));
		                     System.out.println("Modified At: " + rs.getTimestamp("modify_at"));
		                     

		                     // Store in HashMap with correct keys
		                     patOrderDetils.put("hrgnum_puk", rs.getString("hrgnum_puk") != null ? rs.getString("hrgnum_puk") : "-");
		                     patOrderDetils.put("hivt_req_do", rs.getString("hivt_req_do") != null ? rs.getString("hivt_req_do") : "-");
		                     patOrderDetils.put("his_order_id", rs.getString("his_order_id") != null ? rs.getString("his_order_id") : "-");
		                     patOrderDetils.put("hrgstr_fname", rs.getString("hrgstr_fname") != null ? rs.getString("hrgstr_fname") : "-");
		                     patOrderDetils.put("hrgstr_mname", rs.getString("hrgstr_mname") != null ? rs.getString("hrgstr_mname") : "-");
		                     patOrderDetils.put("hrgstr_lname", rs.getString("hrgstr_lname") != null ? rs.getString("hrgstr_lname") : "-");
		                     patOrderDetils.put("patient_gender", rs.getString("patient_gender") != null ? rs.getString("patient_gender") : "-");
		                     patOrderDetils.put("hivnum_sample_type", rs.getString("hivnum_sample_type") != null ? rs.getString("hivnum_sample_type") : "-");
		                     patOrderDetils.put("hivt_pat_type", rs.getString("hivt_pat_type") != null ? rs.getString("hivt_pat_type") : "-");
		                     patOrderDetils.put("pat_sample_collection_date", rs.getString("pat_sample_collection_date") != null ? rs.getString("pat_sample_collection_date") : "-");
		                     patOrderDetils.put("patient_birth_date", rs.getString("patient_birth_date") != null ? rs.getString("patient_birth_date").toString() : "-");
		                     patOrderDetils.put("org_test_status", rs.getString("org_test_status") != null ? String.valueOf(rs.getInt("org_test_status")) : "-");
		                     patOrderDetils.put("order_created_at", rs.getString("order_created_at") != null ? rs.getString("order_created_at").toString() : "-");
		                     patOrderDetils.put("result_created_at", rs.getString("result_created_at") != null ? rs.getString("result_created_at").toString() : "-");
		                     patOrderDetils.put("modify_at", rs.getString("modify_at") != null ? rs.getString("modify_at").toString() : "-");
		                     patOrderDetils.put("age", rs.getString("hivstr_age") != null ? rs.getString("hivstr_age").toString() : "-");

		                     patOrderDetils.put("labCode", rs.getString("gnum_lab_code") != null ? rs.getString("gnum_lab_code").toString() : "-");

		                     patOrderDetils.put("testCode", rs.getString("gnum_test_code") != null ? rs.getString("gnum_test_code").toString() : "-");

		                     patOrderDetils.put("deptCode", rs.getString("hgnum_dept_code_reqd") != null ? rs.getString("hgnum_dept_code_reqd").toString() : "-");
		                     patOrderDetils.put("sampleCode", rs.getString("samplecode") != null ? rs.getString("samplecode").toString() : "-");

		                     System.out.println("----------------End Details -----------------");
		                 } else {
		                     System.out.println("No record found for Sample No: " + SampleNO);
		                 }
		            }


		        } catch (SQLException e) {
		            e.printStackTrace();
		        }
			// TODO Auto-generated method stub
				return patOrderDetils;
			
		}

		public static String generateAckResponse(String receivedMessage) {
			String ackTemplate = START_BLOCK+"MSH|^~\\&|HL72LIS||LIS|BMX|myla|YYYYMMDDHHMMSS||ACK^R22|CONTROLID|P|2.5.1\r"
					+ "MSA|AA|CONTROLID\r"+END_BLOCK+CARRIAGE_RETURN;

			String[] msgPart = receivedMessage.split("\r");
			String[] mshPart = msgPart[0].split("\\|");

			Date currentDate = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssZ");
			String formattedDate = dateFormat.format(currentDate);

			//if(mshPart[0].equals("MSH")) {
			String controlId = mshPart[9];  // Extract CONTROLID from received message
			ackTemplate = ackTemplate.replace("CONTROLID", controlId);
			ackTemplate = ackTemplate.replace("YYYYMMDDHHMMSS", formattedDate);
			return ackTemplate;
			//}
			//return "NO_ACK";

		}
		
		 public static void sendPostRequest(String jsonData,String sampleno,String res) {
		       // String urlString = "http://10.226.28.174:8380/HISInvestigationG5/new_investigation/masters/MachineInterface.jsp?type=6&sampleno="+sampleno; // Replace with your endpoint
		     
		        try {
		            // Create URL object
		            //URL url = new URL(urlString);
		        	   URL url = new URL(AIIMSLAB.aiimsUrl + "/api/v1/machine/mb/saveorganismresult?ispos="+res);
		      	     
		               HttpURLConnection connection = null;

		            connection = (HttpURLConnection) url.openConnection();

		            // Set up the connection
		            connection.setRequestMethod("POST");
		            connection.setRequestProperty("Content-Type", "application/json");
		            connection.setDoOutput(true);

		            // Write JSON data to the output stream
		            try (OutputStream os = connection.getOutputStream()) {
		                byte[] input = jsonData.getBytes("utf-8");
		                os.write(input, 0, input.length);
		            }

		            // Get the response code
		            int responseCode = connection.getResponseCode();
		            System.out.println("Response Code: " + responseCode);
		            AIIMSLAB.saveToFile("Response Code: " + responseCode,"");
		            // Handle the response if needed (e.g., read input stream)

		            
		            //update result status and entry date of result after send result the HIS
		            
		            
		            
		            
		            
		            
		        } catch (Exception e) {
		            e.printStackTrace();
		        } finally {
		            
		        }
		    }

}
