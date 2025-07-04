package Server.Mic;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

import Server.AIIMSLAB;

public class ApiClient {

    /**
     * Sends JSON data to the specified API URL via POST request.
     * 
     * @param apiUrl   The endpoint URL
     * @param jsonData The JSON string to send
     * @return The HTTP response code from the server
     * @throws Exception on connection or IO error
     */
	public static int sendJsonToApi(JSONObject jsonData) throws Exception {
	    URL url = new URL(AIIMSLAB.aiimsUrl);
	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

	    conn.setRequestMethod("POST");
	    conn.setRequestProperty("Content-Type", "application/json; utf-8");
	    conn.setRequestProperty("Accept", "application/json");
	    conn.setDoOutput(true);

	    String jsonString = jsonData.toString();  // Convert JSONObject to string

	    try (OutputStream os = conn.getOutputStream()) {
	        byte[] input = jsonString.getBytes("utf-8"); // Get bytes from string
	        os.write(input, 0, input.length);
	    }

	    int responseCode = conn.getResponseCode();
    	AIIMSLAB.saveToFile("Result API Response : " + responseCode, AIIMSLAB.FILE_NAME);
		
	    
	    conn.disconnect();

	    return responseCode;
	}

    
}
