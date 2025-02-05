/*******************************************************************************
 * © 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package utility;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import mediatorPattern.ControllerMediator;

/**
 *
 * PlotTrendGraph.java<br>
 * Purpose: This class contains logic to draw trend charts(S5PAtientMonitor and Anesthesia)
 *
 * @author Rohit_Sharma41
 *
 */
public class PlotTrendGraph
{
	private static final Logger LOG = Logger.getLogger(PlotTrendGraph.class.getName());

	private DrawTabbedCenter drawTabbedCenter;
	private Timeline graphTimeline;
	private boolean isTimelineRunning = false;
	private boolean isLiveGraphStarted = false;


	// ---Patient Monitor
	private Map<String, String> ptMntrParamMap = new HashMap<String, String>();
	private double X, Y;
	private String symbol;
	private double scalingFactor, ptMntrGraphHeight;
	private Double scaleArr[] = {};
	private Map<String, Double[]> lastCoordinatesMap = new HashMap<String, Double[]>();
	private int iterations = 1;

	// ---Anesthesia
	private Timeline anesGraphTimeline;
	private boolean isAnesTimelineRunning = false;
	private boolean isAnesGraphStarted = false;
	private Map<String, String> anesParamMap = new HashMap<String, String>();
	private double anesX, anesY;
	private String anesSymbol;
	private double anesScalingFactor, anesGraphHeight;
	private Double anesScaleArr[] = {};
	private Map<String, Double[]> anesLastCoordinatesMap = new HashMap<String, Double[]>();
	private int anesIterations = 1;


	public void setAnesIterations(int anesIterations)
	{
		this.anesIterations = anesIterations;
	}
	public void setIterations(int iterations)
	{
		this.iterations = iterations;
	}
	public Map<String, String> getPtMntrParamMap()
	{
		return ptMntrParamMap;
	}

	public Map<String, String> getAnesParamMap()
	{
		return anesParamMap;
	}



	/*private void initializeLiveGraphTimeline()
	{
		if (!isTimelineRunning)
		{
			drawTabbedCenter = ControllerMediator.getInstance().getMainController().getDrawTabbedCenter();
			ptMntrGraphHeight = drawTabbedCenter.getPtMntrGraphPanel().getPrefHeight();

			graphTimeline = new Timeline(new KeyFrame(
			        Duration.seconds(SystemConfiguration.getInstance().getTrendGraphSpeed()), ae -> drawLiveGraph()));

			graphTimeline.setCycleCount(Animation.INDEFINITE);
			graphTimeline.play();

			drawLiveGraph();
			isTimelineRunning = true;
		}
	}*/


	/**
	 * This method contains code to draw S5PatientMonitor trend chart
	 */
	public void drawLiveGraph()
	{
		if (ptMntrParamMap != null)
		{
			drawTabbedCenter = ControllerMediator.getInstance().getMainController().getDrawTabbedCenter();
			ptMntrGraphHeight = drawTabbedCenter.getPtMntrGraphPanel().getPrefHeight();
			for (Map.Entry<String, String> entry : ptMntrParamMap.entrySet())
			{

				//---If parameter value is negative or ---, then dont stroke any symbol or line
				if (entry.getValue() != null)
				if(!entry.getValue().isEmpty())
				if (!entry.getValue().contains("---"))
				{
					if (entry.getKey().toString().equalsIgnoreCase(ParamMinMaxRange.HR))
					{
						symbol = ParamMinMaxRange.HR_LEGEND;
						drawTabbedCenter.getPtMntrGraphBrush().setStroke(Color.web(ParamMinMaxRange.HR_COLOR));
						scaleArr = drawTabbedCenter.getParamScaleMap().get(ParamMinMaxRange.HR);
					}
					else if (entry.getKey().toString().equalsIgnoreCase(ParamMinMaxRange.IBPSYS))
					{
						symbol = ParamMinMaxRange.IBPSYS_LEGEND;
						drawTabbedCenter.getPtMntrGraphBrush().setStroke(Color.web(ParamMinMaxRange.IBP_COLOR));
						scaleArr = drawTabbedCenter.getParamScaleMap().get(ParamMinMaxRange.IBP);
					}
					else if (entry.getKey().toString().equalsIgnoreCase(ParamMinMaxRange.IBPDIA))
					{
						symbol = ParamMinMaxRange.IBPDIA_LEGEND;
						drawTabbedCenter.getPtMntrGraphBrush().setStroke(Color.web(ParamMinMaxRange.IBP_COLOR));
						scaleArr = drawTabbedCenter.getParamScaleMap().get(ParamMinMaxRange.IBP);
					}
					else if (entry.getKey().toString().equalsIgnoreCase(ParamMinMaxRange.SPO2))
					{
						symbol = ParamMinMaxRange.SPO2_LEGEND;
						drawTabbedCenter.getPtMntrGraphBrush().setStroke(Color.web(ParamMinMaxRange.SPO2_COLOR));
						scaleArr = drawTabbedCenter.getParamScaleMap().get(ParamMinMaxRange.SPO2);
					}
					else if (entry.getKey().toString().equalsIgnoreCase(ParamMinMaxRange.ETCO2))
					{
						symbol = ParamMinMaxRange.HR_LEGEND;
						drawTabbedCenter.getPtMntrGraphBrush().setStroke(Color.web(ParamMinMaxRange.ETCO2_COLOR));
						scaleArr = drawTabbedCenter.getParamScaleMap().get(ParamMinMaxRange.ETCO2);
					}

					scalingFactor = ptMntrGraphHeight / scaleArr[0];

					X = drawTabbedCenter.getxValue();
					Y = ptMntrGraphHeight - (Double.parseDouble(entry.getValue()) * scalingFactor);

					if (iterations > 1)
					{
						Double[] lastPts = lastCoordinatesMap.get(entry.getKey());
						if (lastPts != null)
						{
							if(lastPts[0]<X)
							{
								drawTabbedCenter.getPtMntrGraphBrush().strokeLine(lastPts[0] + 5, lastPts[1] - 3, X - 3, Y - 3);
							}
						}
					}

					/*if (!isLiveGraphStarted)
					{
						// ---To draw first set of point on X=0
								X = drawTabbedCenter.getxValue() + 8;
					}*/

					drawTabbedCenter.getPtMntrGraphBrush().strokeText(symbol, X - 8, Y);
					lastCoordinatesMap.put(entry.getKey(), new Double[] { X - 8, Y });
				}
			}
			iterations++;
			isLiveGraphStarted = true;

			ptMntrParamMap.put(ParamMinMaxRange.HR, "---");
			ptMntrParamMap.put(ParamMinMaxRange.IBPSYS, "---");
			ptMntrParamMap.put(ParamMinMaxRange.IBPDIA, "---");
			ptMntrParamMap.put(ParamMinMaxRange.SPO2, "---");
			ptMntrParamMap.put(ParamMinMaxRange.ETCO2, "---");
		}
	}

	/*private void initializeAnesGraphTimeline()
	{
		if (!isAnesTimelineRunning)
		{
			if (drawTabbedCenter == null)
				drawTabbedCenter = ControllerMediator.getInstance().getMainController().getDrawTabbedCenter();
			anesGraphHeight = drawTabbedCenter.getAnesGraphPanel().getPrefHeight();

			anesGraphTimeline = new Timeline(new KeyFrame(
			        Duration.seconds(SystemConfiguration.getInstance().getTrendGraphSpeed()), ae -> drawAnesGraph()));

			anesGraphTimeline.setCycleCount(Animation.INDEFINITE);
			anesGraphTimeline.play();

			drawAnesGraph();
			isAnesTimelineRunning = true;
		}

	}*/

	/**
	 * This method contains code to draw Anesthesia trend chart
	 */
	public void drawAnesGraph()
	{
		if (anesParamMap != null)
		{
			drawTabbedCenter = ControllerMediator.getInstance().getMainController().getDrawTabbedCenter();
			anesGraphHeight = drawTabbedCenter.getAnesGraphPanel().getPrefHeight();
			for (Map.Entry<String, String> entry : anesParamMap.entrySet())
			{
				//---If parameter value is negative or ---, then dont stroke any symbol or line
				if (entry.getValue() != null)
				if(!entry.getValue().isEmpty())
				if (!entry.getValue().contains("---"))
				{
					if (entry.getKey().toString().equalsIgnoreCase(ParamMinMaxRange.PPEAK))
					{
						anesSymbol = ParamMinMaxRange.PPEAK_LEGEND;
						drawTabbedCenter.getAnesGraphBrush().setStroke(Color.web(ParamMinMaxRange.PPEAK_COLOR));
						anesScaleArr = drawTabbedCenter.getParamScaleMap().get(ParamMinMaxRange.PPEAK);
					}
					else if (entry.getKey().toString().equalsIgnoreCase(ParamMinMaxRange.PMEAN))
					{
						anesSymbol = ParamMinMaxRange.PMEAN_LEGEND;
						drawTabbedCenter.getAnesGraphBrush().setStroke(Color.web(ParamMinMaxRange.PMEAN_COLOR));
						anesScaleArr = drawTabbedCenter.getParamScaleMap().get(ParamMinMaxRange.PMEAN);
					}
					else if (entry.getKey().toString().equalsIgnoreCase(ParamMinMaxRange.PEEPEXT))
					{
						anesSymbol = ParamMinMaxRange.PEEPEXT_LEGEND;
						drawTabbedCenter.getAnesGraphBrush().setStroke(Color.web(ParamMinMaxRange.PEEPEXT_COLOR));
						anesScaleArr = drawTabbedCenter.getParamScaleMap().get(ParamMinMaxRange.PEEPEXT);
					}
					else if (entry.getKey().toString().equalsIgnoreCase(ParamMinMaxRange.CIRCUITO2))
					{
						anesSymbol = ParamMinMaxRange.CIRCUITO2_LEGEND;
						drawTabbedCenter.getAnesGraphBrush().setStroke(Color.web(ParamMinMaxRange.CIRCUITO2_COLOR));
						anesScaleArr = drawTabbedCenter.getParamScaleMap().get(ParamMinMaxRange.CIRCUITO2);
					}
					else if (entry.getKey().toString().equalsIgnoreCase(ParamMinMaxRange.TVEXP))
					{
						anesSymbol = ParamMinMaxRange.TVEXP_LEGEND;
						drawTabbedCenter.getAnesGraphBrush().setStroke(Color.web(ParamMinMaxRange.TVEXP_COLOR));
						anesScaleArr = drawTabbedCenter.getParamScaleMap().get(ParamMinMaxRange.TVEXP);
					}
					else if (entry.getKey().toString().equalsIgnoreCase(ParamMinMaxRange.MVEXP))
					{
						anesSymbol = ParamMinMaxRange.MVEXP_LEGEND;
						drawTabbedCenter.getAnesGraphBrush().setStroke(Color.web(ParamMinMaxRange.MVEXP_COLOR));
						anesScaleArr = drawTabbedCenter.getParamScaleMap().get(ParamMinMaxRange.MVEXP);
					}
					else if (entry.getKey().toString().equalsIgnoreCase(ParamMinMaxRange.RR))
					{
						anesSymbol = ParamMinMaxRange.RR_LEGEND;
						drawTabbedCenter.getAnesGraphBrush().setStroke(Color.web(ParamMinMaxRange.RR_COLOR));
						anesScaleArr = drawTabbedCenter.getParamScaleMap().get(ParamMinMaxRange.RR);
					}

					anesScalingFactor = anesGraphHeight / anesScaleArr[0];

					anesX = drawTabbedCenter.getxValue();
					anesY = anesGraphHeight - (Double.parseDouble(entry.getValue()) * anesScalingFactor);

					if (anesIterations > 1)
					{
						Double[] lastPts = anesLastCoordinatesMap.get(entry.getKey());
						if (lastPts != null)
						{
							if(lastPts[0]<anesX)
							{
								drawTabbedCenter.getAnesGraphBrush().strokeLine(lastPts[0] + 5, lastPts[1] - 3, anesX - 3, anesY - 3);
							}
						}
					}

					/*if (!isAnesGraphStarted)
					{
								anesX = drawTabbedCenter.getxValue() + 8;

					}*/

					drawTabbedCenter.getAnesGraphBrush().strokeText(anesSymbol, anesX - 8, anesY);
					anesLastCoordinatesMap.put(entry.getKey(), new Double[] { anesX - 8, anesY });
				}

			}
			anesIterations++;
			isAnesGraphStarted = true;

			anesParamMap.put(ParamMinMaxRange.PPEAK, "---");
			anesParamMap.put(ParamMinMaxRange.PMEAN, "---");
			anesParamMap.put(ParamMinMaxRange.PEEPEXT, "---");
			anesParamMap.put(ParamMinMaxRange.CIRCUITO2, "---");
			anesParamMap.put(ParamMinMaxRange.MVEXP, "---");
			anesParamMap.put(ParamMinMaxRange.TVEXP, "---");
			anesParamMap.put(ParamMinMaxRange.RR, "---");
		}
	}
}
