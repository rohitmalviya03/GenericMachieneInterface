/*******************************************************************************
 * © 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package utility;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.log4j.Logger;

import controllers.MainController;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import mediatorPattern.ControllerMediator;

public class PlotGraphs
{

	private static final Logger LOG = Logger.getLogger(PlotGraphs.class.getName());

	private Integer timelinecounter = 0;

	private MainController mainController;
	private static final double PTMONITOR_WAVEFORM_SPEED = 30;
	private static final double ECG_WAVEFORM_SPEED = 3.32;
	private static final double SPO2_WAVEFORM_SPEED = 10;
	private static final double IBP_WAVEFORM_SPEED = 10;
	private static final double ETCO2_WAVEFORM_SPEED = 40;

	private double ecgScalingVar = 1;
	private double ibpScalingVar = 20000;// 1;
	private double spScalingVar = 1;
	private double etcScalingVar = 400;


	private Timeline graphTimeline = new Timeline();
	private boolean isGraphRunning = false;
	private boolean firstRun = true;


	// ---ECG graph
	private static Queue<Integer> ecgWaveQueue = new ConcurrentLinkedQueue<Integer>();
	private double x1 = 0, x2 = 0, y1 = 0, y2 = 0;
	private double ecgScalingFactor = 0.05;
	private double ecgHalfHeight = 0;
	private boolean ecgPlotStarted = false;
	private Timeline ecgTimeline = new Timeline();
	private boolean isECGRunning = false;


	// ---SPO2 graph
	private static Queue<Integer> spo2WaveQueue = new ConcurrentLinkedQueue<Integer>();
	private double spX1 = 0, spX2 = 0, spY1 = 0, spY2 = 0;
	private double spo2ScalingFactor = 0.2;
	private double spo2HalfHeight = 0;
	private boolean spo2PlotStarted = false;
	private Timeline spo2Timeline = new Timeline();
	private boolean isSPO2Running = false;



	// ---IBP graph
	private static Queue<Integer> ibpWaveQueue = new ConcurrentLinkedQueue<Integer>();
	private double ibpX1 = 0, ibpX2 = 0, ibpY1 = 0, ibpY2 = 0;
	private double ibpScalingFactor = 1;// 0.005;
	private double ibpHalfHeight = 0;
	private boolean ibpPlotStarted = false;
	private Timeline ibpTimeline = new Timeline();
	private boolean isIBPRunning = false;


	// ---ETCO2 graph
	private static Queue<Integer> etcWaveQueue = new ConcurrentLinkedQueue<Integer>();
	private double etcX1 = 0, etcX2 = 0, etcY1 = 0, etcY2 = 0;
	private double etcScalingFactor = 1;// 0.05;
	private double etcHalfHeight = 0;
	private boolean etcPlotStarted = false;
	private Timeline etcTimeline = new Timeline();
	private boolean isETCRunning = false;


	private int ptMonitorCounter = 2;


	public Timeline getGraphTimeline()
	{
		return graphTimeline;
	}
	public void setGraphTimeline(Timeline graphTimeline)
	{
		this.graphTimeline = graphTimeline;
	}
	public boolean isGraphRunning()
	{
		return isGraphRunning;
	}
	public void setGraphRunning(boolean isGraphRunning)
	{
		this.isGraphRunning = isGraphRunning;
	}
	public boolean isFirstRun()
	{
		return firstRun;
	}
	public void setFirstRun(boolean firstRun)
	{
		this.firstRun = firstRun;
	}
	public int getPtMonitorCounter()
	{
		return ptMonitorCounter;
	}
	public void setPtMonitorCounter(int ptMonitorCounter)
	{
		this.ptMonitorCounter = ptMonitorCounter;
	}
	public static Queue<Integer> getEcgWaveQueue() {
		return ecgWaveQueue;
	}
	public static Queue<Integer> getSpo2WaveQueue() {
		return spo2WaveQueue;
	}
	public static Queue<Integer> getIbpWaveQueue() {
		return ibpWaveQueue;
	}
	public static Queue<Integer> getEtcWaveQueue() {
		return etcWaveQueue;
	}

	public double getEcgScalingVar()
	{
		return ecgScalingVar;
	}

	public double getIbpScalingVar()
	{
		return ibpScalingVar;
	}

	public double getSpScalingVar()
	{
		return spScalingVar;
	}

	public double getEtcScalingVar()
	{
		return etcScalingVar;
	}
	public void setEcgScalingVar(double ecgScalingVar)
	{
		this.ecgScalingVar = ecgScalingVar;
	}
	public void setIbpScalingVar(double ibpScalingVar)
	{
		this.ibpScalingVar = ibpScalingVar;
	}
	public void setSpScalingVar(double spScalingVar)
	{
		this.spScalingVar = spScalingVar;
	}
	public void setEtcScalingVar(double etcScalingVar)
	{
		this.etcScalingVar = etcScalingVar;
	}
	public void setEcgHalfHeight(double ecgHalfHeight)
	{
		this.ecgHalfHeight = ecgHalfHeight;
	}
	public double getEcgHalfHeight()
	{
		return ecgHalfHeight;
	}
	public double getSpo2HalfHeight()
	{
		return spo2HalfHeight;
	}
	public void setSpo2HalfHeight(double spo2HalfHeight)
	{
		this.spo2HalfHeight = spo2HalfHeight;
	}
	public double getIbpHalfHeight()
	{
		return ibpHalfHeight;
	}
	public void setIbpHalfHeight(double ibpHalfHeight)
	{
		this.ibpHalfHeight = ibpHalfHeight;
	}
	public double getEtcHalfHeight()
	{
		return etcHalfHeight;
	}
	public void setEtcHalfHeight(double etcHalfHeight)
	{
		this.etcHalfHeight = etcHalfHeight;
	}


	private void initializeEcgTimeline()
	{
		try
		{
			timelinecounter = 0;
			if (firstRun)
			{
				mainController = ControllerMediator.getInstance().getMainController();
				firstRun = false;
			}

			if (!isECGRunning)
			{
				//resetCoordinates();
				x1 = mainController.getDrawNewGraphs().getxValue();
				x2 = mainController.getDrawNewGraphs().getxValue();

				ecgTimeline = new Timeline(new KeyFrame(Duration.millis(ECG_WAVEFORM_SPEED), ae -> drawECGgraph()));

				ecgTimeline.setCycleCount(Animation.INDEFINITE);
				ecgTimeline.play();

				isECGRunning = true;
			}
		}
		catch (Exception e)
		{
			LOG.error("## Exception occured:", e);

		}
	}

	private void initializeSPO2Timeline()
	{
		try
		{
			if (firstRun)
			{
				mainController = ControllerMediator.getInstance().getMainController();
				firstRun = false;
			}

			if (!isSPO2Running)
			{
				//resetCoordinates();
				spX1 = mainController.getDrawNewGraphs().getxValue();
				spX2 = mainController.getDrawNewGraphs().getxValue();

				spo2Timeline = new Timeline(new KeyFrame(Duration.millis(SPO2_WAVEFORM_SPEED), ae -> drawSPO2graph()));

				spo2Timeline.setCycleCount(Animation.INDEFINITE);
				spo2Timeline.play();

				isSPO2Running = true;
			}
		}
		catch (Exception e)
		{
			LOG.error("## Exception occured:", e);

		}
	}

	private void initializeIBPTimeline()
	{
		try
		{
			if (firstRun)
			{
				mainController = ControllerMediator.getInstance().getMainController();
				firstRun = false;
			}

			if (!isIBPRunning)
			{
				//resetCoordinates();
				ibpScalingFactor = mainController.getDrawNewGraphs().getIbpGraphHeight();

				ibpX1 = mainController.getDrawNewGraphs().getxValue();
				ibpX2 = mainController.getDrawNewGraphs().getxValue();

				ibpTimeline = new Timeline(new KeyFrame(Duration.millis(IBP_WAVEFORM_SPEED), ae -> drawIBPgraph()));

				ibpTimeline.setCycleCount(Animation.INDEFINITE);
				ibpTimeline.play();

				isIBPRunning = true;
			}
		}
		catch (Exception e)
		{
			LOG.error("## Exception occured:", e);

		}
	}

	private void initializeETCTimeline()
	{
		try
		{
			if (firstRun)
			{
				mainController = ControllerMediator.getInstance().getMainController();
				firstRun = false;
			}

			if (!isETCRunning)
			{
				//resetCoordinates();
				etcScalingFactor = mainController.getDrawNewGraphs().getEtcGraphHeight();
				etcX1 = mainController.getDrawNewGraphs().getSecondXValue();
				etcX2 = mainController.getDrawNewGraphs().getSecondXValue();

				etcTimeline = new Timeline(new KeyFrame(Duration.millis(ETCO2_WAVEFORM_SPEED), ae -> drawEtcGraph()));

				etcTimeline.setCycleCount(Animation.INDEFINITE);
				etcTimeline.play();

				isETCRunning = true;
			}
		}
		catch (Exception e)
		{
			LOG.error("## Exception occured:", e);

		}
	}



	/**
	 * This method contains logic for ECG GRPAH PLOTTING
	 */
	Integer value = 0;

	private void drawECGgraph()
	{
		if (!ecgWaveQueue.isEmpty())
		{

			value = ecgWaveQueue.poll();

			if (!ecgPlotStarted)
			{
				ecgHalfHeight = mainController.getDrawNewGraphs().getECGGraphHeight() * 0.5;
				x1 = mainController.getDrawNewGraphs().getxValue();
				y1 = calcEndpt(value, ecgHalfHeight, ecgScalingFactor * ecgScalingVar);

				ecgPlotStarted = true;
			}

			x2 = mainController.getDrawNewGraphs().getxValue();
			y2 = calcEndpt(value, ecgHalfHeight, ecgScalingFactor * ecgScalingVar);

			if (x1 > x2)
			{
				x1 = 0;
			}

			mainController.getDrawNewGraphs().getEcgBrush().strokeLine(x1, y1, x2, y2);
			mainController.getDrawNewGraphs().getEcgBrush().clearRect(x2 + 1, 0, 5,
			        mainController.getDrawNewGraphs().getECGGraphHeight());

			x1 = x2;
			y1 = y2;
		}
		else
		{
			timelinecounter = 0;
		}


	}

	/**
	 * This method contains logic for SPO2 GRPAH PLOTTING
	 */

	Integer spo2value=0;

	private void drawSPO2graph()
	{
		if (!spo2WaveQueue.isEmpty())
		{
			spo2value = spo2WaveQueue.poll();

			if (!spo2PlotStarted)
			{
				spo2HalfHeight = mainController.getDrawNewGraphs().getSPO2GraphHeight() * 0.5;
				spX1 = mainController.getDrawNewGraphs().getxValue();
				spY1 = calcEndpt(spo2value, spo2HalfHeight, spo2ScalingFactor * spScalingVar);
				spo2PlotStarted = true;
			}
			spX2 = mainController.getDrawNewGraphs().getxValue();
			spY2 = calcEndpt(spo2value, spo2HalfHeight, spo2ScalingFactor * spScalingVar);

			if (spX1 > spX2)
			{
				spX1 = 0;
			}

			mainController.getDrawNewGraphs().getSpo2Brush().strokeLine(spX1, spY1, spX2, spY2);
			mainController.getDrawNewGraphs().getSpo2Brush().clearRect(spX2 + 1, 0, 5,
			        mainController.getDrawNewGraphs().getSPO2GraphHeight());

			spX1 = spX2;
			spY1 = spY2;
		}


	}

	/**
	 * This method contains logic for IBP GRPAH PLOTTING
	 */
	Integer ibpvalue=0;

	private void drawIBPgraph()
	{
		if (!ibpWaveQueue.isEmpty())
		{
			ibpvalue = ibpWaveQueue.poll();
			if (!ibpPlotStarted)
			{
				ibpHalfHeight = mainController.getDrawNewGraphs().getIbpGraphHeight();
				ibpX1 = mainController.getDrawNewGraphs().getxValue();
				ibpY1 = calcEndpt(ibpvalue, ibpHalfHeight, ibpScalingFactor / ibpScalingVar);
				ibpPlotStarted = true;
			}
			ibpX2 = mainController.getDrawNewGraphs().getxValue();
			ibpY2 = calcEndpt(ibpvalue, ibpHalfHeight, ibpScalingFactor / ibpScalingVar);

			if (ibpX1 > ibpX2)
			{
				ibpX1 = 0;
			}
			mainController.getDrawNewGraphs().getIbpBrush().strokeLine(ibpX1, ibpY1, ibpX2, ibpY2);
			mainController.getDrawNewGraphs().getIbpBrush().clearRect(ibpX2 + 1, 0, 5,
			        mainController.getDrawNewGraphs().getIbpGraphHeight());

			ibpX1 = ibpX2;
			ibpY1 = ibpY2;
		}


	}

	Integer etcvalue=0;

	private void drawEtcGraph()
	{
		if (!etcWaveQueue.isEmpty())
		{
			etcvalue = etcWaveQueue.poll();
			etcvalue-=120;
			if(etcvalue<0)
				etcvalue=0;
			if (!etcPlotStarted)
			{
				etcHalfHeight = mainController.getDrawNewGraphs().getEtcGraphHeight();
				etcX1 = mainController.getDrawNewGraphs().getSecondXValue();
				etcY1 = calcEndpt(etcvalue, etcHalfHeight, etcScalingFactor / etcScalingVar);
				etcPlotStarted = true;
			}
			etcX2 = mainController.getDrawNewGraphs().getSecondXValue();
			etcY2 = calcEndpt(etcvalue, etcHalfHeight, etcScalingFactor / etcScalingVar);

			if (etcX1 > etcX2)
			{
				etcX1 = 0;
			}
			mainController.getDrawNewGraphs().getEtcBrush().strokeLine(etcX1, etcY1, etcX2, etcY2);
			mainController.getDrawNewGraphs().getEtcBrush().clearRect(etcX2 + 1, 0, 5,
			        mainController.getDrawNewGraphs().getIbpGraphHeight());

			etcX1 = etcX2;
			etcY1 = etcY2;
		}


	}

	/**
	 * This method calculate end point Y2 for each coordinate by using
	 *
	 * @param yval
	 * @param halfHeight
	 * @param scalingFactor
	 * @return
	 */
	private double calcEndpt(int yval, double halfHeight, double scalingFactor)
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


	/**
	 * This method reset x and y coordinates to 0, after stopping graph plotting
	 * timeline on logout
	 */
	private void resetCoordinates()
	{
		x1 = mainController.getDrawNewGraphs().getxValue();
		// y1 = mainController.getDrawNewGraphs().getECGGraphHeight() * 0.5;
		x2 = mainController.getDrawNewGraphs().getxValue();
		// y2 = mainController.getDrawNewGraphs().getECGGraphHeight() * 0.5;

		spX1 = mainController.getDrawNewGraphs().getxValue();
		// spY1 = mainController.getDrawNewGraphs().getSPO2GraphHeight() * 0.5;
		spX2 = mainController.getDrawNewGraphs().getxValue();
		// spY2 = mainController.getDrawNewGraphs().getSPO2GraphHeight() * 0.5;

		ibpX1 = mainController.getDrawNewGraphs().getxValue();
		// ibpY1 = mainController.getDrawNewGraphs().getIbpGraphHeight() * 0.5;
		ibpX2 = mainController.getDrawNewGraphs().getxValue();
		// ibpY2 = mainController.getDrawNewGraphs().getIbpGraphHeight() * 0.5;

	}

}
