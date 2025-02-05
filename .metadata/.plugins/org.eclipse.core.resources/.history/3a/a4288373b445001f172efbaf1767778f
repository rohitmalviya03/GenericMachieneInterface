/*******************************************************************************
 * © 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package utility;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.log4j.Logger;

import controllers.MainController;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import mediatorPattern.ControllerMediator;

public class PlotAnesthesiaGraph
{
	private static final Logger LOG = Logger.getLogger(PlotAnesthesiaGraph.class.getName());

	private Timeline graphTimeline;
	private static final double ANESTHESIA_PRESSUREWAVE_SPEED = 39;// 50;
	private static final int ANESTHESIA_DEFAULT_VALUE = 512;
	private boolean isGraphRunning = false;

	private double peakScalingFactor = 1;// 0.04;
	private double peakScalingVar = 1;
	private double X1 = 0, X2 = 0, Y1 = 0, Y2 = 0;
	private List<Integer> pressWaveArray = new ArrayList<Integer>();
	private static Queue<Integer> pressWaveQueue = new ConcurrentLinkedQueue<Integer>();

	private double peakGraphHeight = 0;
	private boolean peakPlotStarted = false;
	private MainController mainController;
	int arrCounter = 0;
	public void setPressWaveArray(List<Integer> pressWaveArray)
	{
		arrCounter = 0;
		this.pressWaveArray = pressWaveArray;
	}
	public Queue<Integer> getPressWaveQueue()
	{
		return pressWaveQueue;
	}

	public double getPeakScalingVar()
	{
		return peakScalingVar;
	}

	public void setPeakScalingVar(double peakScalingVar)
	{
		this.peakScalingVar = peakScalingVar;
	}
	public double getPeakGraphHeight()
	{
		return peakGraphHeight;
	}
	public void setPeakGraphHeight(double peakGraphHeight)
	{
		this.peakGraphHeight = peakGraphHeight;
	}


	private void initializeGraphTimeline()
	{
		try
		{
			if (!isGraphRunning)
			{
				mainController = ControllerMediator.getInstance().getMainController();
				peakScalingVar = mainController.getDrawNewGraphs().getInitialPeakScalingVar();
				peakScalingFactor = mainController.getDrawNewGraphs().getPressureGraphHeight();

				X1 = mainController.getDrawNewGraphs().getSecondXValue();
				X2 = mainController.getDrawNewGraphs().getSecondXValue();

				graphTimeline = new Timeline(new KeyFrame(Duration.millis(ANESTHESIA_PRESSUREWAVE_SPEED),
				        ae -> drawPressureGraph()));

				graphTimeline.setCycleCount(Animation.INDEFINITE);
				graphTimeline.play();
				isGraphRunning = true;
			}
		}
		catch (Exception e)
		{
			LOG.error("## Exception occured:", e);
		}
	}

	double value = 0;

	private void drawPressureGraph()
	{
		try
		{
			if (!pressWaveQueue.isEmpty())
			{
				value = pressWaveQueue.poll();

				value = ((value - ANESTHESIA_DEFAULT_VALUE) * 20) / ANESTHESIA_DEFAULT_VALUE;

				if (!peakPlotStarted)
				{

					peakGraphHeight = mainController.getDrawNewGraphs().getPressureGraphHeight();
					X1 = mainController.getDrawNewGraphs().getSecondXValue();
					Y1 = endPointY2(value, peakGraphHeight, peakScalingFactor / peakScalingVar);
					peakPlotStarted = true;
				}

				X2 = mainController.getDrawNewGraphs().getSecondXValue();
				Y2 = endPointY2(value, peakGraphHeight, peakScalingFactor / peakScalingVar);

				//if (X1 > new Double(mainController.getDrawNewGraphs().getCalcGraphWidth()).intValue() - 1 || X2 == 0)
				if(X1>X2)
				{
					X1 = 0;
				}
				mainController.getDrawNewGraphs().getPressureBrush().strokeLine(X1, Y1, X2, Y2);
				mainController.getDrawNewGraphs().getPressureBrush().clearRect(X2 + 1, 0, 5, peakGraphHeight);
				X1 = X2;
				Y1 = Y2;
			}
		}
		catch (Exception e)
		{
			LOG.error("## Exception occured:", e);
		}


	}


	private double endPointY2(double yval, double halfHeight, double scalingFactor)
	{
		double returnY = 0;
		double processedVal = yval * scalingFactor;

		if (yval > 0)
		{
			returnY = halfHeight - processedVal;
		}
		else if (yval < 0)
		{

			returnY = halfHeight + Math.abs(processedVal);
		}
		else if (yval == 0)
		{
			returnY = halfHeight;
		}

		return returnY;
	}


}
