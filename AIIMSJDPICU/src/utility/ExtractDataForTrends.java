/*******************************************************************************
 * © 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

/**
 * ExtractDataForTrends.java<br>
 * Purpose: This class contains logic which extracts specific time interval data
 * for drawing trend charts from the provided raw data
 *
 * @author Rohit_Sharma41
 *
 */
public class ExtractDataForTrends
{
	private static final Logger LOG = Logger.getLogger(ExtractDataForTrends.class.getName());
	private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");

	private static ExtractDataForTrends instance = null;

	public static ExtractDataForTrends getInstance()
	{
		if (instance == null)
		{
			instance = new ExtractDataForTrends();
		}
		return instance;
	}

	/**
	 * This method extracts data between start and end time from csvData map
	 *
	 * @param csvData data map containing all values
	 * @param startTime initial timestamp
	 * @param endTime final timestamp
	 * @return data map between start and end time
	 */
	public Map<String, String[]> extract(Map<String, String[]> csvData, String startTime, String endTime)
	{
		Map<String, String[]> paramValuesList = new LinkedHashMap<String, String[]>();
		Calendar startCal = Calendar.getInstance();
		try
		{
			startCal.setTime(sdf.parse(startTime));
			boolean endTimeMatched = false;
			endTime = endTime.replaceAll("-", "/");
			while (!endTimeMatched)
			{
				String tempStartTime = sdf.format(startCal.getTime());
				tempStartTime = tempStartTime.replaceAll("-", "/");

				paramValuesList.put(tempStartTime, null);
				
				for (String csvStr : csvData.keySet())
				{
					Calendar csvTime = Calendar.getInstance();
					csvStr = csvStr.replaceAll("/", "-");
					csvTime.setTime(sdf.parse(csvStr));

					if (TimeUnit.MILLISECONDS.toMinutes(csvTime.getTimeInMillis() - startCal.getTimeInMillis()) == 0)
					{
						// paramValuesList.put(tempStartTime,
						// csvData.get(csvStr.replaceAll("-", "/")));
						paramValuesList.put(tempStartTime, csvData.get(csvStr));
						break;
					}

				}
				startCal.add(Calendar.MINUTE, 1);

				if (tempStartTime.equalsIgnoreCase(endTime))
					endTimeMatched = true;
			}

		}
		catch (ParseException e)
		{
			LOG.error("## Exception occured:", e);
		}
		return paramValuesList;
	}
}
