package Server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

// Define the OBXSegment class
public class OBXSegment {
    private String patient_id;
    private String param_id;
    private String param_name;
    private String param_value;
    private String param_unit;
    private String param_referenceRange;
    private String timestamp;
    private String packettimestamp;

	public String getPackettimestamp() {
		return packettimestamp;
	}
	public void setPackettimestamp(String packettimestamp) {
		this.packettimestamp = packettimestamp;
	}
	public String getPatient_id() {
		return patient_id;
	}
	public void setPatient_id(String patient_id) {
		this.patient_id = patient_id;
	}
	public String getParam_id() {
		return param_id;
	}
	public void setParam_id(String param_id) {
		this.param_id = param_id;
	}
	public String getParam_name() {
		return param_name;
	}
	public void setParam_name(String param_name) {
		this.param_name = param_name;
	}
	public String getParam_value() {
		return param_value;
	}
	public void setParam_value(String param_value) {
		this.param_value = param_value;
	}
	public String getParam_unit() {
		return param_unit;
	}
	public void setParam_unit(String param_unit) {
		this.param_unit = param_unit;
	}
	public String getParam_referenceRange() {
		return param_referenceRange;
	}
	public void setParam_referenceRange(String param_referenceRange) {
		this.param_referenceRange = param_referenceRange;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

    // Getters and setters for the fields

}