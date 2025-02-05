package Server;

public class result_parameter {

	    private String patient_id;
	    private String organismName;
	    private String organismCode;
	    public String getOrganismName() {
			return organismName;
		}
		public void setOrganismName(String organismName) {
			this.organismName = organismName;
		}
		public String getOrganismCode() {
			return organismCode;
		}
		public void setOrganismCode(String organismCode) {
			this.organismCode = organismCode;
		}
		private String antiBiotic;
	    private String antiBioticCode;
	    private String antiBiotic_Name;
	    private String miCValue;
	    private String result;

	    private String sample_type;
	    
	    public String getSample_type() {
			return sample_type;
		}
		public void setSample_type(String sample_type) {
			this.sample_type = sample_type;
		}
		private String timestamp;
	    //private String packettimestamp;
		public String getPatient_id() {
			return patient_id;
		}
		public void setPatient_id(String patient_id) {
			this.patient_id = patient_id;
		}
		public String getAntiBiotic() {
			return antiBiotic;
		}
		public void setAntiBiotic(String antiBiotic) {
			this.antiBiotic = antiBiotic;
		}
		public String getAntiBioticCode() {
			return antiBioticCode;
		}
		public void setAntiBioticCode(String antiBioticCode) {
			this.antiBioticCode = antiBioticCode;
		}
		public String getAntiBiotic_Name() {
			return antiBiotic_Name;
		}
		public void setAntiBiotic_Name(String antiBiotic_Name) {
			this.antiBiotic_Name = antiBiotic_Name;
		}
		public String getMiCValue() {
			return miCValue;
		}
		public void setMiCValue(String miCValue) {
			this.miCValue = miCValue;
		}
		public String getResult() {
			return result;
		}
		public void setResult(String result) {
			this.result = result;
		}
		public String getTimestamp() {
			return timestamp;
		}
		public void setTimestamp(String timestamp) {
			this.timestamp = timestamp;
		}
}