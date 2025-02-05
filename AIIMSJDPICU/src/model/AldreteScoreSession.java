/*******************************************************************************
 * © 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package model;

import javafx.stage.Stage;

public class AldreteScoreSession {


	private final static AldreteScoreSession instance = new AldreteScoreSession();

	public static AldreteScoreSession getInstance()
	{
		return instance;
	}

	private String PACUparams;
	private int PACUTotalScore;
	private String shiftingOutParams;
	private int shiftingOutTotal;
	private String selectedType;
	private Stage postOpStage;

	public Stage getPostOpStage() {
		return postOpStage;
	}
	public void setPostOpStage(Stage postOpStage) {
		this.postOpStage = postOpStage;
	}
	public String getPACUparams() {
		return PACUparams;
	}
	public void setPACUparams(String pACUparams) {
		PACUparams = pACUparams;
	}
	public int getPACUTotalScore() {
		return PACUTotalScore;
	}
	public void setPACUTotalScore(int pACUTotalScore) {
		PACUTotalScore = pACUTotalScore;
	}
	public String getShiftingOutParams() {
		return shiftingOutParams;
	}
	public void setShiftingOutParams(String shiftingOutParams) {
		this.shiftingOutParams = shiftingOutParams;
	}
	public int getShiftingOutTotal() {
		return shiftingOutTotal;
	}
	public void setShiftingOutTotal(int shiftingOutTotal) {
		this.shiftingOutTotal = shiftingOutTotal;
	}
	public String getSelectedType() {
		return selectedType;
	}
	public void setSelectedType(String selectedType) {
		this.selectedType = selectedType;
	}



}
