/*******************************************************************************
 * © 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package model;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class TestParamModel {

	private Label lblTestParam;
	private TextField txtTestParam;
	private TextField txtExpTestParam;
	public TextField getTxtExpTestParam() {
		return txtExpTestParam;
	}
	public void setTxtExpTestParam(TextField txtExpTestParam) {
		this.txtExpTestParam = txtExpTestParam;
	}
	private Label lblunit;
	private int paramFieldId;
	public Label getLblTestParam() {
		return lblTestParam;
	}
	public void setLblTestParam(Label lblTestParam) {
		this.lblTestParam = lblTestParam;
	}
	public TextField getTxtTestParam() {
		return txtTestParam;
	}
	public void setTxtTestParam(TextField txtTestParam) {
		this.txtTestParam = txtTestParam;
	}
	public Label getLblunit() {
		return lblunit;
	}
	public void setLblunit(Label lblunit) {
		this.lblunit = lblunit;
	}
	public int getParamFieldId() {
		return paramFieldId;
	}
	public void setParamFieldId(int paramFieldId) {
		this.paramFieldId = paramFieldId;
	}




}
