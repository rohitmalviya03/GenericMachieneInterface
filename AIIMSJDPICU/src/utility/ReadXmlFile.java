/*******************************************************************************
 * © 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package utility;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * ReadXmlFile.java<br>
 * Purpose: This class contains logic to read XML file
 *
 * @author Rohit_Sharma41
 *
 */
public class ReadXmlFile {
	private static final Logger LOG = Logger.getLogger(ReadXmlFile.class.getName());
	private static ObservableList<String> countryList;
	private static ObservableList<String> stateList;

	/**
	 * This method read countries from CountriesList.xml
	 *
	 * @return list of country names present in the xml file
	 */
	public static ObservableList<String> readCountriesFile() {
		countryList = FXCollections.observableArrayList();
		countryList.add("Select");
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			DefaultHandler handler = new DefaultHandler() {

				public void characters(char ch[], int start, int length) throws SAXException {
					String list = new String(ch, start, length);

					for (int i = 0; i < list.split(",").length; i++) {
						String val = list.split(",")[i].trim();
						if (!val.isEmpty())
							countryList.add(val);
					}

				}

			};

			saxParser.parse(ReadXmlFile.class.getResourceAsStream("/res/CountriesList.xml"), handler);

		} catch (Exception e) {
			LOG.error("## Exception occured:", e);
		}

		return countryList;
	}

	/**
	 * This method read States from IndianStates.xml
	 *
	 * @return list of state names present in the xml file
	 */
	public static ObservableList<String> readStatesFile() {
		stateList = FXCollections.observableArrayList();
		stateList.add("Select");
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			DefaultHandler handler = new DefaultHandler() {

				public void characters(char ch[], int start, int length) throws SAXException {
					String list = new String(ch, start, length);

					for (int i = 0; i < list.split(",").length; i++) {
						String val = list.split(",")[i].trim();
						if (!val.isEmpty())
							stateList.add(val);

					}

				}

			};

			saxParser.parse(ReadXmlFile.class.getResourceAsStream("/res/IndianStates.xml"), handler);

		} catch (Exception e) {
			LOG.error("## Exception occured:", e);
		}
		return stateList;
	}
}
