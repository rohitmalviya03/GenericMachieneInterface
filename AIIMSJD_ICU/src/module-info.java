module AIIMSJD_ICU {
	requires javafx.controls;
	requires javafx.fxml;
	requires RXTXcomm;
	
	opens application to javafx.graphics, javafx.fxml;
}
