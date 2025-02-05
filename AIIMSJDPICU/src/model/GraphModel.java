/*******************************************************************************
 * © 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package model;

public class GraphModel {

	 private String serie;
	    private String category;
	    private double value;


		public GraphModel(String serie, String category, double value) {
			super();
			this.serie = serie;
			this.category = category;
			this.value = value;
		}
		public String getSerie() {
			return serie;
		}
		public void setSerie(String serie) {
			this.serie = serie;
		}
		public String getCategory() {
			return category;
		}
		public void setCategory(String category) {
			this.category = category;
		}
		public double getValue() {
			return value;
		}
		public void setValue(double value) {
			this.value = value;
		}


}
