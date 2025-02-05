/*******************************************************************************
 * © 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package application;

//import java.io.File;
//import java.io.FileReader;
//import java.io.Reader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

//import com.fasterxml.jackson.databind.MappingIterator;
//import com.fasterxml.jackson.databind.ObjectReader;
//import com.fasterxml.jackson.dataformat.csv.CsvMapper;
//import com.fasterxml.jackson.dataformat.csv.CsvParser;

public class CSVReader {

//	private static final int NUMBER_OF_PARAMERTERS = 15;
//	private String[] csvHeader = new String[NUMBER_OF_PARAMERTERS + 1];
//	private Map<String, String[]> csvObjects;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
	private SimpleDateFormat sdfNew = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

//	public static void main(String[] args)
//	{
//		//File file = new File("C:\\Users\\rohit_sharma41\\Desktop\\DeviceData_204.csv");
//		File file = new File("");
//
//		try
//		{
//			// ---PARSING AS STRING
//			new CSVReader().parseCsvAsString(file);
//
//		}
//		catch (Exception e)
//		{
//			e.printStackTrace();
//		}
//
//	}


	int itrRow = 1;
//	/**
//	 * This method parses passed file as collection of Strings of rows
//	 *
//	 * @param file
//	 * @return
//	 * @throws Exception
//	 */
//	public Map<String, String[]> parseCsvAsString(File file) throws Exception
//	{
//		csvObjects = new LinkedHashMap<String, String[]>();
//
//		CsvMapper csvMapper = new CsvMapper();
//		csvMapper.enable(CsvParser.Feature.WRAP_AS_ARRAY);
//		ObjectReader oReader = csvMapper.reader();// reader(String[].class);
//		oReader = oReader.forType(String[].class);
//
//		try (Reader reader = new FileReader(file))
//		{
//			MappingIterator<String[]> mi = oReader.readValues(reader);
//			while (mi.hasNext())
//			{
//				String result[] = mi.next();
//				String values[] = new String[NUMBER_OF_PARAMERTERS];
//
//				if (result[0].equalsIgnoreCase("date/time") || itrRow > 1)
//				{
//					for (int i = 0; i <= NUMBER_OF_PARAMERTERS; i++)
//					{
//						String j = "0.0";
//						if (itrRow > 1)
//						{
//							if (i > 0 && result[i] != null)
//							{
//								if (result[i].isEmpty() || result[i].equalsIgnoreCase("null")
//								        || result[i].contains("- - -") || result[i].contains("---")
//								        ||(!result[i].contains("- - -") && Double.parseDouble(result[i]) <-32000 || Double.parseDouble(result[i]) >32000)
//								        || (!result[i].contains("---") && Double.parseDouble(result[i]) <-32000 || Double.parseDouble(result[i]) >32000))
//								{
//									j = "---";
//								}
//								else
//								{
//									if (!result[i].contains(" "))
//										j = result[i];// .split("\\|")[0];
//									else
//										j = result[i].split(" ")[0];
//								}
//
//								values[i - 1] = j;
//							}
//						}
//						else
//						{
//							if (i < csvHeader.length)
//								csvHeader[i] = result[i];
//						}
//					}
//
//					if (itrRow > 1)
//					{
//					     csvObjects.put(result[0], values);
//						/*if(result[0].split(":").length==3)
//							csvObjects.put(result[0].substring(0, result[0].lastIndexOf(":")), values);
//						else if(result[0].split(":").length==2)
//							csvObjects.put(result[0], values);*/
//					}
//					itrRow++;
//				}
//			}
//
//
//
//			/*for (Map.Entry<String, String[]> entry : csvObjects.entrySet())
//			{
//				System.out.print("Key :" + entry.getKey() + "------------");
//				for (String d : entry.getValue())
//				{
//					if (d == null)
//						System.out.print("null" + "   ");
//					else
//						System.out.print(d + "   ");
//				}
//				System.out.println("\n \n ");
//			}*/
//		}
//		return csvObjects;
//	}

	/**
	 * This method extracts data(in mins) from parsed CSV obj(csvData) from startTime to endTime
	 * @param csvData
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	/*public Map<String, String[]> extractCSVData(Map<String, String[]> csvData, String startTime, String endTime)
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
				for(String csvStr:csvData.keySet())
				{
					Calendar csvTime=Calendar.getInstance();
					csvStr = csvStr.replaceAll("/", "-");
					csvTime.setTime(sdf.parse(csvStr));

					if(TimeUnit.MILLISECONDS.toMinutes(csvTime.getTimeInMillis() - startCal.getTimeInMillis())==0)
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
			e.printStackTrace();
		}
		return paramValuesList;
	}*/

//	/**
//	 * This method extracts data(in secs) from parsed CSV obj(csvData) from startTime to endTime
//	 * @param csvData
//	 * @param startTime
//	 * @param endTime
//	 * @return
//	 */
//	public Map<String, String[]> extractCSVsecsData(Map<String, String[]> csvData, String startTime, String endTime)
//	{
//		Map<String, String[]> paramValuesList = new LinkedHashMap<String, String[]>();
//
//		startTime = startTime + ":00";// + firstEntry.getKey().split(" ")[1].split(":")[2];
//		endTime = endTime + ":00";
//		Calendar startCal = Calendar.getInstance();
//		try
//		{
//			startCal.setTime(sdfNew.parse(startTime));
//			boolean endTimeMatched = false;
//			endTime = endTime.replaceAll("-", "/");
//			while (!endTimeMatched)
//			{
//				String tempStartTime = sdfNew.format(startCal.getTime());
//				tempStartTime = tempStartTime.replaceAll("-", "/");
//
//				paramValuesList.put(tempStartTime, null);
//				if(csvData.keySet().contains(tempStartTime))
//				{
//					paramValuesList.put(tempStartTime, csvData.get(tempStartTime.replaceAll("-", "/")));
//				}
//
//				/*for (String csvStr : csvData.keySet())
//				{
//					csvStr = csvStr.replaceAll("/", "-");
//					Calendar csvTime = Calendar.getInstance();
//					csvTime.setTime(sdfNew.parse(csvStr));
//
//					if (TimeUnit.MILLISECONDS.toSeconds(csvTime.getTimeInMillis() - startCal.getTimeInMillis()) == 0)
//					{
//						paramValuesList.put(tempStartTime, csvData.get(csvStr.replaceAll("-", "/")));
//						break;
//					}
//
//				}*/
//				startCal.add(Calendar.SECOND, 1);
//
//				if (tempStartTime.equalsIgnoreCase(endTime))
//					endTimeMatched = true;
//			}
//
//
//		}
//		catch (Exception e)
//		{
//			e.printStackTrace();
//		}
//		return paramValuesList;
//	}
}
