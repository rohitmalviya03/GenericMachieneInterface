/*******************************************************************************
 * © 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package utility;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.log4j.Logger;

import application.Main;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class DrawNewGraphs
{
	private static final Logger LOG = Logger.getLogger(DrawNewGraphs.class.getName());

	private double paneWidth;
	private double paneHeight;

	private static final int MINS_OR_SEC_GAP = 1; //---secs gap between graph lines
	private static final int PIXEL_GAP=90; //---no of pixels for 1 min(or sec)
	private static final String UP_TRIANGLE_UNICODE = "\u25B2";
	private static final String DOWN_TRIANGLE_UNICODE = "\u25BC";
	private static final double SCALE_UP_FACTOR=2;
	private static final double SCALE_NORMAL_FACTOR = 1;
	private static final double SCALE_DOWN_FACTOR=0.5;
	private double scaleArr[] = { 0.5, 1, 2 };

	private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

	// ---graph panes
	Pane graphPane;
	int graphPaneWidth;
	double graphPaneHeight;
	GraphicsContext graphBrush;
	Pane graphYaxis;
	Pane TimePane;
	double timePaneHeight;
	public double getTimePaneHeight()
	{
		return timePaneHeight;
	}
	public Pane getGraphPane()
	{
		return graphPane;
	}
	public int getGraphPaneWidth()
	{
		return graphPaneWidth;
	}
	public double getGraphPaneHeight()
	{
		return graphPaneHeight;
	}
	public GraphicsContext getGraphBrush()
	{
		return graphBrush;
	}


	//---ECG
	Pane ECGPane;
	Pane ECGGraph;
	Label ecgVal;
	int ECGGraphWidth;
	double ECGGraphHeight;
	Canvas ecgCanvas;
	GraphicsContext ecgBrush;

	double noOfGraphLines;
	double pixelsBtwXLines;
	String lastTimeLabel;
	String startTimeLabel = "";
	public int getECGGraphWidth()
	{
		return ECGGraphWidth;
	}
	public double getECGGraphHeight()
	{
		return ECGGraphHeight;
	}
	public GraphicsContext getEcgBrush()
	{
		return ecgBrush;
	}
	public String getLastTimeLabel()
	{
		return lastTimeLabel;
	}
	public String getstartTimeLabel()
	{
		return startTimeLabel;
	}
	public Label getEcgVal()
	{
		return ecgVal;
	}



	//---IBP
	Pane IBPPane;
	Label ibpVal;
	Label ibpMeanVal;
	int ibpGraphWidth;
	double ibpGraphHeight;
	GraphicsContext ibpBrush;
	public Label getIbpVal()
	{
		return ibpVal;
	}

	public Label getIbpMeanVal()
	{
		return ibpMeanVal;
	}
	public int getIbpGraphWidth()
	{
		return ibpGraphWidth;
	}
	public double getIbpGraphHeight()
	{
		return ibpGraphHeight;
	}
	public GraphicsContext getIbpBrush()
	{
		return ibpBrush;
	}



	//---SPO2
	Pane SPO2Pane;
	Label spo2Val;
	int SPO2GraphWidth;
	double SPO2GraphHeight;
	GraphicsContext spo2Brush;
	public Label getSpo2Val()
	{
		return spo2Val;
	}
	public int getSPO2GraphWidth()
	{
		return SPO2GraphWidth;
	}
	public double getSPO2GraphHeight()
	{
		return SPO2GraphHeight;
	}
	public GraphicsContext getSpo2Brush()
	{
		return spo2Brush;
	}



	//---ETCO2
	Pane ETCO2Pane;
	Label etcVal;
	int etcGraphWidth;
	double etcGraphHeight;
	GraphicsContext etcBrush;
	public Label getEtcVal()
	{
		return etcVal;
	}
	public int getEtcGraphWidth()
	{
		return etcGraphWidth;
	}
	public double getEtcGraphHeight()
	{
		return etcGraphHeight;
	}
	public GraphicsContext getEtcBrush()
	{
		return etcBrush;
	}


	//---PRESSURE
	Pane PressurePane;
	Label rrTotalVal;
	int pressureGraphWidth;
	double pressureGraphHeight;
	GraphicsContext pressureBrush;
	public Label getRRTotalVal()
	{
		return rrTotalVal;
	}
	public int getPressureGraphWidth()
	{
		return pressureGraphWidth;
	}
	public double getPressureGraphHeight()
	{
		return pressureGraphHeight;
	}
	public GraphicsContext getPressureBrush()
	{
		return pressureBrush;
	}


	//---Left panel values and their getters
	Label o2Val;
	Label totalFlowVal;
	Label tvinspVal;
	Label rrsetVal;
	Label ieVal;
	Label peepiVal;
	Label pmaxVal;
	Label pmeanVal;
	Label peepVal;
	Label circuitO2Val;
	Label mvexpVal;
	Label tvexpVal;

	public Label getTotalFlowVal()
	{
		return totalFlowVal;
	}

	public Label getRrsetVal()
	{
		return rrsetVal;
	}

	public Label getPeepiVal()
	{
		return peepiVal;
	}
	public Label getO2Val()
	{
		return o2Val;
	}

	public Label getTvinspVal()
	{
		return tvinspVal;
	}

	public Label getIeVal()
	{
		return ieVal;
	}
	public Label getPmaxVal()
	{
		return pmaxVal;
	}
	public Label getPmeanVal()
	{
		return pmeanVal;
	}

	public Label getTvexpVal()
	{
		return tvexpVal;
	}

	public Label getCircuitO2Val()
	{
		return circuitO2Val;
	}

	public Label getMvexpVal()
	{
		return mvexpVal;
	}

	public Label getPeepVal()
	{
		return peepVal;
	}


	private static final int IBP_MULTIPLICATION_FACTOR = 100;
	private static final int ETCO2_MULTIPLICATION_FACTOR = 100;
	private static final int MAX_PRESSURE_VAL = 3584;
	private int ibpYAxisValues[] = { 200, 250, 300, 400 };
	private int pressureYAxisValues[] = { 40, 80, 120 };
	private int etco2YAxisValues[] = { 40, 60, 80, 100 };

	private int xValue;
	private int secondXValue;
	private double calcGraphWidth;
	private double etcGraphYLayout;
	private double secondXLineHeight;

	private Label ecgScaleLbl;
	private Label ibpScaleLbl;
	private Label spScaleLbl;
	private Label etcScaleLbl;
	private Label pressScaleLbl;

	private String ibpYAxisLbl;
	private String etcYAxisLbl;
	private String pressYAxisLbl;




	public double getEtcGraphYLayout()
	{
		return etcGraphYLayout;
	}
	public double getCalcGraphWidth()
	{
		return calcGraphWidth;
	}
	public int getxValue()
	{
		return xValue;
	}
	public void setxValue(int xValue)
	{
		this.xValue = xValue;
	}
	public int getSecondXValue()
	{
		return secondXValue;
	}
	public void setSecondXValue(int secondXValue)
	{
		this.secondXValue = secondXValue;
	}
	public double getSecondXLineHeight()
	{
		return secondXLineHeight;
	}
	public String getIbpYAxisLbl()
	{
		return ibpYAxisLbl;
	}
	public String getEtcYAxisLbl()
	{
		return etcYAxisLbl;
	}
	public String getPressYAxisLbl()
	{
		return pressYAxisLbl;
	}

	public void createCenterPane(Pane centerPane)
	{

		try
		{
			paneWidth = centerPane.getPrefWidth();
			paneHeight = centerPane.getPrefHeight();
			centerPane.setStyle("-fx-background-color:#000000");

			// ---Left Panel Area
			Pane leftPanel = new Pane();
			leftPanel.setPrefSize(paneWidth * 0.13, paneHeight * 0.93);
			leftPanel.setLayoutX(leftPanel.getPrefWidth() * 0.05);
			leftPanel.setLayoutY(paneHeight * 0.05);
			divideleftPanel(leftPanel);

			//---Graph Area
			graphYaxis = new Pane();

			graphYaxis.setPrefSize(paneWidth * 0.015, paneHeight * 0.957);
			graphYaxis.setLayoutX(leftPanel.getPrefWidth() + paneWidth * 0.015);
			graphYaxis.setLayoutY(paneHeight * 0.05);

			graphPane = new Pane();
			graphPane.setPrefSize(paneWidth * 0.835, paneHeight * 0.957);
			graphPane.setLayoutX(leftPanel.getPrefWidth() + paneWidth * 0.03);
			graphPane.setLayoutY(paneHeight * 0.02);
			graphPaneWidth = (int) graphPane.getPrefWidth();
			graphPaneHeight = graphPane.getPrefHeight();
			Canvas graphCanvas = new Canvas(graphPaneWidth, graphPaneHeight);
			graphBrush = graphCanvas.getGraphicsContext2D();
			graphBrush.setStroke(Color.RED);
			graphBrush.setLineWidth(2);
			graphPane.getChildren().add(graphCanvas);
			divideGraphPane();


			//---Bottom Area
			/*Pane bottomPane=new Pane();
			bottomPane.setPrefSize(paneWidth, paneHeight * 0.18);
			bottomPane.setLayoutY(graphPane.getPrefHeight());
			divideBottomPane(bottomPane);*/

			//---adding elements to centerPane
			centerPane.getChildren().addAll(leftPanel, graphYaxis, graphPane);
		}
		catch (Exception e)
		{
			LOG.error("## Exception occured:", e);
		}
	}

	private void divideGraphPane()
	{
		//---TimeLine Area

		addTimePane();

		//---ECG Area
		ECGPane = new Pane();
		ECGPane.setId("ECGPane");
		ECGPane.setPrefSize(graphPane.getPrefWidth(), graphPane.getPrefHeight() * 0.16);
		ECGPane.setLayoutY(TimePane.getPrefHeight());
		calcGraphWidth = calcualteGraphAreaWidth(ECGPane.getPrefWidth() * 0.8);
		double calcParamWidth = ECGPane.getPrefWidth() - calcGraphWidth - ECGPane.getPrefWidth() * 0.06;

		Calendar currentTime = Calendar.getInstance();
		// addLabelsToTimePane(currentTime);

		ECGGraph = new Pane();
		ECGGraph.setStyle(" -fx-border-color:grey;");
		ECGGraph.setPrefSize(calcGraphWidth, ECGPane.getPrefHeight());
		ECGGraphWidth = new Double(ECGGraph.getPrefWidth()).intValue();
		ECGGraphHeight = ECGGraph.getPrefHeight();
		ecgCanvas = new Canvas(ECGGraphWidth, ECGGraphHeight);
		ecgBrush = ecgCanvas.getGraphicsContext2D();
		ecgBrush.setStroke(Color.web("#00ff00"));
		ecgBrush.setLineWidth(1);
		ECGGraph.getChildren().add(ecgCanvas);

		VBox ECGscaleBtns = new VBox();
		ECGscaleBtns.setAlignment(Pos.CENTER);
		ECGscaleBtns.setStyle(" -fx-border-color:grey;");
		ECGscaleBtns.setPrefSize(ECGPane.getPrefWidth() * 0.05, ECGPane.getPrefHeight());
		ECGscaleBtns.setLayoutX(ECGGraph.getPrefWidth() + ECGPane.getPrefWidth() * 0.005);
		Button ecgUpBtn = new Button(UP_TRIANGLE_UNICODE);
		//ecgScaleLbl = new Label(String.valueOf(Main.getInstance().getPlotGraphs().getEcgScalingVar()));
		ecgScaleLbl = new Label("");
		ecgScaleLbl.setPrefHeight(10);
		ecgScaleLbl.setTextFill(Color.WHITE);
		Button ecgDownBtn = new Button(DOWN_TRIANGLE_UNICODE);
		ECGscaleBtns.getChildren().addAll(ecgUpBtn, ecgScaleLbl, ecgDownBtn);
		setScaleBtnActions(ecgUpBtn, ecgDownBtn, "ecg");
		addScaleVarLbl(ECGPane, String.valueOf(scaleArr[1]));

		Pane ECGParam = new Pane();
		ECGParam.setStyle(" -fx-border-color:grey;");
		ECGParam.setPrefSize(calcParamWidth, ECGPane.getPrefHeight());
		ECGParam.setLayoutX(ECGGraph.getPrefWidth() + ECGscaleBtns.getPrefWidth() + ECGPane.getPrefWidth() * 0.01);

		Label ecgLbl = new Label("ECG");
		ecgLbl.setTextFill(Color.web("#00ff00"));// ---green
		ecgLbl.setStyle("-fx-font-size:" + ECGPane.getPrefWidth() * 0.02 + "px;-fx-font-family:Haettenschweiler");
		ecgLbl.setLayoutX(ECGParam.getPrefWidth() * 0.03);
		ecgLbl.setLayoutY(ECGParam.getPrefHeight() * 0.03);
		Label ecgUnit = new Label("bpm");
		ecgUnit.setTextFill(Color.web("#00ff00"));
		ecgUnit.setStyle("-fx-font-size:" + ECGPane.getPrefWidth() * 0.01 + "px;-fx-font-weight:bold");
		ecgUnit.setLayoutY(ECGParam.getPrefHeight() * 0.3);
		ecgUnit.setLayoutX(ECGParam.getPrefWidth() * 0.03);
		Image hrtImg = new Image(this.getClass().getResourceAsStream("/res/Heart.png"));
		ImageView imgVw=new ImageView(hrtImg);
		imgVw.setFitHeight(40);
		imgVw.setFitWidth(40);
		Label heart = new Label("", imgVw);
		// Label heart = new Label("\u2764");
		//heart.setStyle("-fx-font-size:" + ECGPane.getPrefWidth() * 0.025 + "px;-fx-font-weight:bold");
		heart.setTextFill(Color.RED);
		heart.setLayoutX(ECGParam.getPrefWidth() * 0.83);
		heart.setLayoutY(0 - ECGParam.getPrefHeight() * 0.03);

		ecgVal = new Label("---");
		ecgVal.setTextFill(Color.web("#00ff00"));
		ecgVal.setStyle("-fx-font-size:" + ECGPane.getPrefHeight() * 0.85 + "px;-fx-font-weight:bold");
		ecgVal.setLayoutX(ECGParam.getPrefWidth() * 0.25);
		ecgVal.setLayoutY(0 - ECGParam.getPrefHeight() * 0.15);
		ECGParam.getChildren().addAll(ecgLbl, ecgVal, ecgUnit, heart);

		ECGPane.getChildren().addAll(ECGGraph, ECGscaleBtns, ECGParam);

		//addYLabels(ECGPane, new int[] { (int) ECGPane.getPrefHeight() / 2, 0, 0 - (int) ECGPane.getPrefHeight() / 2 });


		//---IBP Area
		IBPPane = new Pane();
		IBPPane.setId("IBPPane");
		IBPPane.setPrefSize(graphPane.getPrefWidth(), graphPane.getPrefHeight() * 0.16);
		IBPPane.setLayoutY(TimePane.getPrefHeight() + ECGPane.getPrefHeight() + graphPane.getPrefHeight() * 0.02);


		Pane IBPGraph = new Pane();
		IBPGraph.setStyle(" -fx-border-color:grey; -fx-border-width: 1px;");
		IBPGraph.setPrefSize(calcGraphWidth, IBPPane.getPrefHeight());
		ibpGraphWidth = (int) IBPGraph.getPrefWidth();
		ibpGraphHeight = IBPGraph.getPrefHeight();
		Canvas ibpCanvas = new Canvas(ibpGraphWidth, ibpGraphHeight);
		ibpBrush = ibpCanvas.getGraphicsContext2D();
		ibpBrush.setStroke(Color.web("#ff5050"));
		ibpBrush.setLineWidth(1);
		IBPGraph.getChildren().add(ibpCanvas);

		Canvas ibpDashedLinesCanvas = new Canvas(ibpGraphWidth, ibpGraphHeight);
		GraphicsContext ibpDashedLinesBrush = ibpDashedLinesCanvas.getGraphicsContext2D();
		ibpDashedLinesBrush.setStroke(Color.GRAY);
		ibpDashedLinesBrush.setLineWidth(1);
		ibpDashedLinesBrush.setLineDashes(4);
		IBPGraph.getChildren().add(ibpDashedLinesCanvas);
		ibpDashedLinesBrush.strokeLine(0, ibpGraphHeight / 4, ibpGraphWidth, ibpGraphHeight / 4);
		ibpDashedLinesBrush.strokeLine(0, 3 * (ibpGraphHeight / 4), ibpGraphWidth, 3 * (ibpGraphHeight / 4));

		VBox IBPScaleBtns = new VBox();
		IBPScaleBtns.setAlignment(Pos.CENTER);
		IBPScaleBtns.setStyle(" -fx-border-color:grey;");
		IBPScaleBtns.setPrefSize(IBPPane.getPrefWidth() * 0.05, IBPPane.getPrefHeight());
		IBPScaleBtns.setLayoutX(IBPGraph.getPrefWidth() + IBPPane.getPrefWidth() * 0.005);
		Button ibpUpBtn = new Button(UP_TRIANGLE_UNICODE);
		//ibpScaleLbl = new Label(String.valueOf(Main.getInstance().getPlotGraphs().getIbpScalingVar()));
		ibpScaleLbl = new Label("");
		ibpScaleLbl.setPrefHeight(10);
		ibpScaleLbl.setTextFill(Color.WHITE);
		Button ibpDownBtn = new Button(DOWN_TRIANGLE_UNICODE);
		IBPScaleBtns.getChildren().addAll(ibpUpBtn,ibpScaleLbl, ibpDownBtn);
		setScaleBtnActions(ibpUpBtn, ibpDownBtn, "ibp");

		Pane IBPParam = new Pane();
		IBPParam.setStyle(" -fx-border-color:grey;");
		IBPParam.setPrefSize(calcParamWidth, IBPPane.getPrefHeight());
		IBPParam.setLayoutX(IBPGraph.getPrefWidth() + IBPScaleBtns.getPrefWidth() + IBPPane.getPrefWidth() * 0.01);

		Label ibpLbl = new Label("IBP");
		ibpLbl.setTextFill(Color.web("#ff5050"));// ---red
		ibpLbl.setStyle("-fx-font-size:" + IBPPane.getPrefWidth() * 0.02 + "px;-fx-font-family:Haettenschweiler");
		ibpLbl.setLayoutX(IBPParam.getPrefWidth() * 0.03);
		ibpLbl.setLayoutY(IBPParam.getPrefHeight() * 0.03);

		Label ibpUnit = new Label("Sys/ \n Dia");
		ibpUnit.setTextFill(Color.web("#ff5050"));
		ibpUnit.setStyle("-fx-font-size:" + IBPPane.getPrefWidth() * 0.01 + "px;-fx-font-weight:bold");
		ibpUnit.setLayoutX(IBPParam.getPrefWidth() * 0.03);
		ibpUnit.setLayoutY(IBPParam.getPrefHeight() * 0.3);

		ibpVal = new Label("---/---");
		ibpVal.setTextFill(Color.web("#ff5050"));
		ibpVal.setStyle("-fx-font-size:" + IBPPane.getPrefHeight() * 0.45 + "px;-fx-font-weight:bold");
		ibpVal.setLayoutX(IBPParam.getPrefWidth() * 0.27);
		ibpVal.setLayoutY(IBPParam.getPrefHeight() * 0.13);
		// ibpVal.setLayoutY(IBPParam.getPrefHeight() * 0.08);

		Label ibpMean = new Label("Mean");
		ibpMean.setTextFill(Color.web("#ff5050"));
		ibpMean.setStyle("-fx-font-size:" + IBPPane.getPrefWidth() * 0.012 + "px;-fx-font-weight:bold");
		ibpMean.setLayoutX(IBPParam.getPrefWidth() * 0.03);
		ibpMean.setLayoutY(IBPParam.getPrefHeight() * 0.7);

		ibpMeanVal = new Label("(---)");
		ibpMeanVal.setTextFill(Color.web("#ff5050"));
		ibpMeanVal.setStyle("-fx-font-size:" + IBPParam.getPrefWidth() * 0.1 + "px;-fx-font-weight:bold");
		ibpMeanVal.setLayoutY(IBPParam.getPrefHeight() * 0.6);
		ibpMeanVal.setLayoutX(IBPParam.getPrefWidth() * 0.45);

		IBPParam.getChildren().addAll(ibpLbl, ibpVal, ibpMean, ibpMeanVal, ibpUnit);

		IBPPane.getChildren().addAll(IBPGraph, IBPScaleBtns, IBPParam);
		addYLabels(IBPPane, new int[] { 200, 100, 0 });
		ibpYAxisLbl="200";

		//---SPO2 Area
		SPO2Pane = new Pane();
		SPO2Pane.setId("SPO2Pane");
		SPO2Pane.setPrefSize(graphPane.getPrefWidth(), graphPane.getPrefHeight() * 0.16);
		SPO2Pane.setLayoutY(TimePane.getPrefHeight() + ECGPane.getPrefHeight() + IBPPane.getPrefHeight()
		        + graphPane.getPrefHeight() * 0.04);

		Pane SPO2Graph = new Pane();
		SPO2Graph.setStyle(" -fx-border-color:grey; -fx-border-width: 1px;");
		SPO2Graph.setPrefSize(calcGraphWidth, SPO2Pane.getPrefHeight());
		SPO2GraphWidth = (int) SPO2Graph.getPrefWidth();
		SPO2GraphHeight = SPO2Graph.getPrefHeight();
		Canvas spo2Canvas = new Canvas(SPO2GraphWidth, SPO2GraphHeight);
		spo2Brush = spo2Canvas.getGraphicsContext2D();
		spo2Brush.setStroke(Color.web("#ffd966"));
		spo2Brush.setLineWidth(1);
		SPO2Graph.getChildren().add(spo2Canvas);

		VBox SPO2ScaleBtns = new VBox();
		SPO2ScaleBtns.setAlignment(Pos.CENTER);
		SPO2ScaleBtns.setStyle(" -fx-border-color:grey;");
		SPO2ScaleBtns.setPrefSize(SPO2Pane.getPrefWidth() * 0.05, SPO2Pane.getPrefHeight());
		SPO2ScaleBtns.setLayoutX(SPO2Graph.getPrefWidth() + SPO2Pane.getPrefWidth() * 0.005);
		Button spUpBtn = new Button(UP_TRIANGLE_UNICODE);
		//spScaleLbl = new Label(String.valueOf(Main.getInstance().getPlotGraphs().getSpScalingVar()));
		spScaleLbl = new Label("");
		spScaleLbl.setPrefHeight(10);
		spScaleLbl.setTextFill(Color.WHITE);
		Button spDownBtn = new Button(DOWN_TRIANGLE_UNICODE);
		SPO2ScaleBtns.getChildren().addAll(spUpBtn, spScaleLbl, spDownBtn);
		setScaleBtnActions(spUpBtn, spDownBtn, "spo2");
		addScaleVarLbl(SPO2Pane, String.valueOf(scaleArr[1]));

		Pane SPO2Param = new Pane();
		SPO2Param.setStyle(" -fx-border-color:grey;");
		SPO2Param.setPrefSize(calcParamWidth, SPO2Pane.getPrefHeight());
		SPO2Param.setLayoutX(SPO2Graph.getPrefWidth() + SPO2ScaleBtns.getPrefWidth() + SPO2Pane.getPrefWidth() * 0.01);

		Label spo2Lbl = new Label("SpO2");
		spo2Lbl.setTextFill(Color.web("#ffd966"));// ---yellow
		spo2Lbl.setStyle("-fx-font-size:" + SPO2Pane.getPrefWidth() * 0.02 + "px;-fx-font-family:Haettenschweiler");
		spo2Lbl.setLayoutX(IBPParam.getPrefWidth() * 0.03);
		spo2Lbl.setLayoutY(IBPParam.getPrefHeight() * 0.03);

		Label spo2Unit = new Label("%");
		spo2Unit.setTextFill(Color.web("#ffd966"));
		spo2Unit.setStyle("-fx-font-size:" + SPO2Pane.getPrefWidth() * 0.01 + "px;-fx-font-weight:bold");
		spo2Unit.setLayoutX(IBPParam.getPrefWidth() * 0.03);
		spo2Unit.setLayoutY(SPO2Param.getPrefHeight() * 0.3);

		spo2Val = new Label("---");
		spo2Val.setTextFill(Color.web("#ffd966"));
		spo2Val.setStyle("-fx-font-size:" + SPO2Pane.getPrefHeight() * 0.85 + "px;-fx-font-weight:bold");
		spo2Val.setLayoutX(SPO2Param.getPrefWidth() * 0.28);
		spo2Val.setLayoutY(0 - SPO2Param.getPrefHeight() * 0.15);

		SPO2Param.getChildren().addAll(spo2Lbl, spo2Val, spo2Unit);
		SPO2Pane.getChildren().addAll(SPO2Graph, SPO2ScaleBtns, SPO2Param);
//		addYLabels(SPO2Pane,new int[] { (int) SPO2Pane.getPrefHeight() / 2, 0, 0 - (int) SPO2Pane.getPrefHeight() / 2 });


		//---ETCO2 Area
		ETCO2Pane = new Pane();
		ETCO2Pane.setId("ETCO2Pane");
		ETCO2Pane.setPrefSize(graphPane.getPrefWidth(), graphPane.getPrefHeight() * 0.16);
		ETCO2Pane.setLayoutY(TimePane.getPrefHeight() + ECGPane.getPrefHeight() + IBPPane.getPrefHeight()
		        + SPO2Pane.getPrefHeight() + graphPane.getPrefHeight() * 0.065);
		etcGraphYLayout = ETCO2Pane.getLayoutY();

		Pane ETCO2Graph = new Pane();
		ETCO2Graph.setStyle(" -fx-border-color:grey; -fx-border-width: 1px;");
		ETCO2Graph.setPrefSize(calcGraphWidth, ETCO2Pane.getPrefHeight());
		etcGraphWidth = (int) ETCO2Graph.getPrefWidth();
		etcGraphHeight = ETCO2Graph.getPrefHeight();
		Canvas etcCanvas = new Canvas(etcGraphWidth, etcGraphHeight);
		etcBrush = etcCanvas.getGraphicsContext2D();
		etcBrush.setStroke(Color.web("#b3ecff"));
		etcBrush.setLineWidth(1);
		ETCO2Graph.getChildren().add(etcCanvas);


		VBox ETCO2ScaleBtns = new VBox();
		ETCO2ScaleBtns.setAlignment(Pos.CENTER);
		ETCO2ScaleBtns.setStyle(" -fx-border-color:grey;");
		ETCO2ScaleBtns.setPrefSize(ETCO2Pane.getPrefWidth() * 0.05, ETCO2Pane.getPrefHeight());
		ETCO2ScaleBtns.setLayoutX(ETCO2Graph.getPrefWidth() + ETCO2Pane.getPrefWidth() * 0.005);
		Button etcUpBtn = new Button(UP_TRIANGLE_UNICODE);
		//etcScaleLbl = new Label(String.valueOf(Main.getInstance().getPlotGraphs().getEtcScalingVar()));
		etcScaleLbl = new Label("");
		etcScaleLbl.setPrefHeight(10);
		etcScaleLbl.setTextFill(Color.WHITE);
		Button etcDownBtn = new Button(DOWN_TRIANGLE_UNICODE);
		ETCO2ScaleBtns.getChildren().addAll(etcUpBtn, etcScaleLbl, etcDownBtn);
		setScaleBtnActions(etcUpBtn, etcDownBtn, "etco2");

		Pane ETCO2Param = new Pane();
		ETCO2Param.setStyle(" -fx-border-color:grey;");
		ETCO2Param.setPrefSize(calcParamWidth, ETCO2Pane.getPrefHeight());
		ETCO2Param.setLayoutX(ETCO2Graph.getPrefWidth() +ETCO2ScaleBtns.getPrefWidth()+ ETCO2Pane.getPrefWidth() * 0.01);

		Label etcLbl = new Label("EtCO2");
		etcLbl.setTextFill(Color.web("#b3ecff"));// ---sky blue
		etcLbl.setStyle("-fx-font-size:" + ETCO2Pane.getPrefWidth() * 0.02 + "px;-fx-font-family:Haettenschweiler");
		etcLbl.setLayoutX(IBPParam.getPrefWidth() * 0.03);
		etcLbl.setLayoutY(IBPParam.getPrefHeight() * 0.03);

		Label etcUnit = new Label("mmHg");
		etcUnit.setTextFill(Color.web("#b3ecff"));
		etcUnit.setStyle("-fx-font-size:" + ETCO2Pane.getPrefWidth() * 0.01 + "px;-fx-font-weight:bold");
		etcUnit.setLayoutX(IBPParam.getPrefWidth() * 0.03);
		etcUnit.setLayoutY(ETCO2Param.getPrefHeight() * 0.3);

		etcVal = new Label("---");
		etcVal.setTextFill(Color.web("#b3ecff"));
		etcVal.setStyle("-fx-font-size:" + ETCO2Pane.getPrefHeight() * 0.85 + "px;-fx-font-weight:bold");
		etcVal.setLayoutX(ETCO2Param.getPrefWidth() * 0.3);
		etcVal.setLayoutY(0 - ETCO2Param.getPrefHeight() * 0.15);

		ETCO2Param.getChildren().addAll(etcLbl, etcVal, etcUnit);
		ETCO2Pane.getChildren().addAll(ETCO2Graph, ETCO2ScaleBtns, ETCO2Param);
		addYLabels(ETCO2Pane, new int[] { 40, 20, 00 });
		etcYAxisLbl = "40";

		//---Pressure Area
		PressurePane = new Pane();
		PressurePane.setId("PressurePane");
		PressurePane.setPrefSize(graphPane.getPrefWidth(), graphPane.getPrefHeight() * 0.16);
		PressurePane.setLayoutY(TimePane.getPrefHeight() + ECGPane.getPrefHeight() + IBPPane.getPrefHeight()
		        + SPO2Pane.getPrefHeight() + ETCO2Pane.getPrefHeight() + graphPane.getPrefHeight() * 0.08);
		Pane PressureGraph = new Pane();
		PressureGraph.setStyle(" -fx-border-color:grey; -fx-border-width: 1px;");
		PressureGraph.setPrefSize(calcGraphWidth, PressurePane.getPrefHeight());

		pressureGraphWidth = (int) PressureGraph.getPrefWidth();
		pressureGraphHeight = PressureGraph.getPrefHeight();
		Canvas pressureCanvas = new Canvas(pressureGraphWidth, pressureGraphHeight);
		pressureBrush = pressureCanvas.getGraphicsContext2D();
		pressureBrush.setStroke(Color.web("#47d1d1"));
		pressureBrush.setLineWidth(1);
		PressureGraph.getChildren().add(pressureCanvas);

		Canvas pressDashedLinesCanvas = new Canvas(pressureGraphWidth, pressureGraphHeight);
		GraphicsContext pressDashedLinesBrush = pressDashedLinesCanvas.getGraphicsContext2D();
		pressDashedLinesBrush.setStroke(Color.GRAY);
		pressDashedLinesBrush.setLineWidth(1);
		pressDashedLinesBrush.setLineDashes(4);
		PressureGraph.getChildren().add(pressDashedLinesCanvas);
		pressDashedLinesBrush.strokeLine(0, pressureGraphHeight / 2, pressureGraphWidth, pressureGraphHeight / 2);
		// pressDashedLinesBrush.strokeLine(0, 3 * (pressureGraphHeight / 4),
		// pressureGraphWidth, 3 * (pressureGraphHeight / 4));

		VBox PressureScaleBtns = new VBox();
		PressureScaleBtns.setAlignment(Pos.CENTER);
		PressureScaleBtns.setStyle(" -fx-border-color:grey;");
		PressureScaleBtns.setPrefSize(PressurePane.getPrefWidth() * 0.05, PressurePane.getPrefHeight());
		PressureScaleBtns.setLayoutX(PressureGraph.getPrefWidth() + PressurePane.getPrefWidth() * 0.005);
		Button pressUpBtn = new Button(UP_TRIANGLE_UNICODE);
		//pressScaleLbl = new Label(String.valueOf(Main.getInstance().getPlotAnesthesiaGraph().getPeakScalingVar()));
		pressScaleLbl = new Label("");
		pressScaleLbl.setPrefHeight(10);
		pressScaleLbl.setTextFill(Color.WHITE);
		Button pressDownBtn = new Button(DOWN_TRIANGLE_UNICODE);
		PressureScaleBtns.getChildren().addAll(pressUpBtn, pressScaleLbl, pressDownBtn);
		setScaleBtnActions(pressUpBtn, pressDownBtn, "pressure");

		Pane PressureParam = new Pane();
		PressureParam.setStyle(" -fx-border-color:grey;");
		PressureParam.setPrefSize(calcParamWidth, PressurePane.getPrefHeight());
		PressureParam.setLayoutX(PressureGraph.getPrefWidth() +PressureScaleBtns.getPrefWidth()+ PressurePane.getPrefWidth() * 0.01);

		Label rrLbl = new Label("RR");
		rrLbl.setTextFill(Color.web("#47d1d1"));// ---ocean blue
		rrLbl.setStyle("-fx-font-size:" + PressurePane.getPrefWidth() * 0.02 + "px;-fx-font-family:Haettenschweiler");
		rrLbl.setLayoutX(IBPParam.getPrefWidth() * 0.03);
		rrLbl.setLayoutY(IBPParam.getPrefHeight() * 0.03);

		Label rrUnit = new Label("rpm");
		rrUnit.setTextFill(Color.web("#47d1d1"));
		rrUnit.setStyle("-fx-font-size:" + ETCO2Pane.getPrefWidth() * 0.01 + "px;-fx-font-weight:bold");
		rrUnit.setLayoutX(PressureParam.getPrefWidth() * 0.03);
		rrUnit.setLayoutY(PressureParam.getPrefHeight() * 0.3);

		rrTotalVal = new Label("---");
		rrTotalVal.setTextFill(Color.web("#47d1d1"));
		rrTotalVal.setStyle("-fx-font-size:" + PressurePane.getPrefHeight() * 0.85 + "px;-fx-font-weight:bold");
		rrTotalVal.setLayoutX(PressureParam.getPrefWidth() * 0.25);
		rrTotalVal.setLayoutY(0 - PressureParam.getPrefHeight() * 0.15);

		PressureParam.getChildren().addAll(rrLbl, rrUnit, rrTotalVal);
		PressurePane.getChildren().addAll(PressureGraph, PressureScaleBtns, PressureParam);
		addYLabels(PressurePane, new int[] { 40, 20, 0 });
		pressYAxisLbl = "40";

		// ---Now all panes are set so draw YAxis labels since the method contains ref to all panes
		// addLblToYaxis(graphYaxis);

		// ---calculating height for Second X line which shows current index for
		// ETCO2 and PRESSURE Pane
		secondXLineHeight = TimePane.getPrefHeight() + ECGPane.getPrefHeight() + IBPPane.getPrefHeight()
		        + SPO2Pane.getPrefHeight() + ETCO2Pane.getPrefHeight() + graphPane.getPrefHeight() * 0.08
		        + PressurePane.getPrefHeight();

		//---Events Area
		Pane eventsPane = new Pane();
		eventsPane.setPrefSize(calcGraphWidth, graphPane.getPrefHeight() * 0.07);
		eventsPane.setStyle(" -fx-border-color:grey; ");
		eventsPane.setLayoutY(
		        TimePane.getPrefHeight() + ECGPane.getPrefHeight() + IBPPane.getPrefHeight() + SPO2Pane.getPrefHeight()
		                + ETCO2Pane.getPrefHeight() + PressurePane.getPrefHeight() + graphPane.getPrefHeight() * 0.1);


		//---adding elements to graphPane
		graphPane.getChildren().remove(TimePane);
		graphPane.getChildren().addAll(ECGPane, IBPPane, SPO2Pane, ETCO2Pane, PressurePane, eventsPane, TimePane);
	}

	int scaleArrIndexEcg = 1, scaleArrIndexIbp = 1, scaleArrIndexSp = 1, scaleArrIndexEtc = 1, scaleArrIndexPress = 1;
	int ibpYvalsArrIndex, etco2YvalsArrIndex, pressYvalsArrIndex;

	private void setScaleBtnActions(Button up, Button down, String param)
	{
		if (param.equalsIgnoreCase("ibp") || param.equalsIgnoreCase("pressure") || param.equalsIgnoreCase("etco2"))
		{
			down.setDisable(true);
		}

		up.setOnMouseClicked(e ->
		{
			try
			{
				down.setDisable(false);
				if (param.equalsIgnoreCase("ecg"))
				{
					scaleArrIndexEcg++;
					Main.getInstance().getPlotGraphs().setEcgScalingVar(scaleArr[scaleArrIndexEcg]);
					// ecgScaleLbl.setText(String.valueOf(scaleArr[scaleArrIndexEcg]));
					addScaleVarLbl(ECGPane, String.valueOf(scaleArr[scaleArrIndexEcg]));

					if (scaleArrIndexEcg == scaleArr.length - 1)
					{
						up.setDisable(true);
						down.setDisable(false);
					}
					System.out.println("+ ECG " + scaleArrIndexEcg);
				}
				else if (param.equalsIgnoreCase("ibp"))
				{
					System.out.println("+ IBP");
					/*scaleArrIndexIbp++;
					Main.getInstance().getPlotGraphs().setIbpScalingVar(scaleArr[scaleArrIndexIbp]);
					ibpScaleLbl.setText(String.valueOf(scaleArr[scaleArrIndexIbp]));
					if (scaleArrIndexIbp == scaleArr.length - 1)
					{
						up.setDisable(true);
						down.setDisable(false);
					}*/
					ibpYvalsArrIndex++;
					ibpYAxisLbl = String.valueOf(ibpYAxisValues[ibpYvalsArrIndex]);
					addYLabels(IBPPane, new int[] { ibpYAxisValues[ibpYvalsArrIndex], ibpYAxisValues[ibpYvalsArrIndex]/2, 0 });
					Main.getInstance().getPlotGraphs().setIbpScalingVar(ibpYAxisValues[ibpYvalsArrIndex] * IBP_MULTIPLICATION_FACTOR);
					if (ibpYvalsArrIndex == ibpYAxisValues.length - 1)
					{
						up.setDisable(true);
						down.setDisable(false);
					}
				}
				else if (param.equalsIgnoreCase("spo2"))
				{
					scaleArrIndexSp++;
					Main.getInstance().getPlotGraphs().setSpScalingVar(scaleArr[scaleArrIndexSp]);
					// spScaleLbl.setText(String.valueOf(scaleArr[scaleArrIndexSp]));
					addScaleVarLbl(SPO2Pane, String.valueOf(scaleArr[scaleArrIndexSp]));
					if (scaleArrIndexSp == scaleArr.length - 1)
					{
						up.setDisable(true);
						down.setDisable(false);
					}
					System.out.println("+ SPO2");
				}
				else if (param.equalsIgnoreCase("etco2"))
				{
					System.out.println("+ ETCO2");

					/*scaleArrIndexEtc++;
					Main.getInstance().getPlotGraphs().setEtcScalingVar(scaleArr[scaleArrIndexEtc]);
					etcScaleLbl.setText(String.valueOf(scaleArr[scaleArrIndexEtc]));
					if (scaleArrIndexEtc == scaleArr.length - 1)
					{
						up.setDisable(true);
						down.setDisable(false);
					}*/
					etco2YvalsArrIndex++;
					etcYAxisLbl = String.valueOf(etco2YAxisValues[etco2YvalsArrIndex]);
					addYLabels(ETCO2Pane, new int[] { etco2YAxisValues[etco2YvalsArrIndex],
					        etco2YAxisValues[etco2YvalsArrIndex] / 2, 0 });
					Main.getInstance().getPlotGraphs().setEtcScalingVar(etco2YAxisValues[etco2YvalsArrIndex] * ETCO2_MULTIPLICATION_FACTOR);
					if (etco2YvalsArrIndex == etco2YAxisValues.length - 1)
					{
						up.setDisable(true);
						down.setDisable(false);
					}
				}
				else if (param.equalsIgnoreCase("pressure"))
				{
					System.out.println("+ PRESSURE");
					/*scaleArrIndexPress++;
					Main.getInstance().getPlotAnesthesiaGraph().setPeakScalingVar(scaleArr[scaleArrIndexPress]);
					pressScaleLbl.setText(String.valueOf(scaleArr[scaleArrIndexPress]));
					if (scaleArrIndexPress == scaleArr.length - 1)
					{
						up.setDisable(true);
						down.setDisable(false);
					}*/

					pressYvalsArrIndex++;
					pressYAxisLbl = String.valueOf(pressureYAxisValues[pressYvalsArrIndex]);
					addYLabels(PressurePane, new int[] { pressureYAxisValues[pressYvalsArrIndex],
					        pressureYAxisValues[pressYvalsArrIndex] / 2, 0 });
					/*double scaledVar = (pressureYAxisValues[pressYvalsArrIndex] * MAX_PRESSURE_VAL)
					        / pressureYAxisValues[pressureYAxisValues.length - 1];*/
					double scaledVar = pressureYAxisValues[pressYvalsArrIndex] ;
					Main.getInstance().getPlotAnesthesiaGraph().setPeakScalingVar(scaledVar);
					if (pressYvalsArrIndex == pressureYAxisValues.length - 1)
					{
						up.setDisable(true);
						down.setDisable(false);
					}
				}
			}
			catch (Exception e1)
			{
				LOG.error("## Exception occured:", e1);

			}

		});

		down.setOnMouseClicked(e ->
		{
			try
			{
				up.setDisable(false);
				if (param.equalsIgnoreCase("ecg"))
				{
					scaleArrIndexEcg--;
					Main.getInstance().getPlotGraphs().setEcgScalingVar(scaleArr[scaleArrIndexEcg]);
					// ecgScaleLbl.setText(String.valueOf(scaleArr[scaleArrIndexEcg]));
					addScaleVarLbl(ECGPane, String.valueOf(scaleArr[scaleArrIndexEcg]));
					if (scaleArrIndexEcg == 0)
					{
						up.setDisable(false);
						down.setDisable(true);
					}
					System.out.println("- ECG ");
				}
				else if (param.equalsIgnoreCase("ibp"))
				{
					System.out.println("- IBP");
					/*scaleArrIndexIbp--;
					Main.getInstance().getPlotGraphs().setIbpScalingVar(scaleArr[scaleArrIndexIbp]);
					ibpScaleLbl.setText(String.valueOf(scaleArr[scaleArrIndexIbp]));
					if (scaleArrIndexIbp == 0)
					{
						up.setDisable(false);
						down.setDisable(true);
					}*/

					ibpYvalsArrIndex--;
					ibpYAxisLbl = String.valueOf(ibpYAxisValues[ibpYvalsArrIndex]);
					addYLabels(IBPPane, new int[] { ibpYAxisValues[ibpYvalsArrIndex], ibpYAxisValues[ibpYvalsArrIndex]/2, 0 });
					Main.getInstance().getPlotGraphs().setIbpScalingVar(ibpYAxisValues[ibpYvalsArrIndex] * IBP_MULTIPLICATION_FACTOR);
					if (ibpYvalsArrIndex == 0)
					{
						up.setDisable(false);
						down.setDisable(true);
					}
				}
				else if (param.equalsIgnoreCase("spo2"))
				{
					scaleArrIndexSp--;
					Main.getInstance().getPlotGraphs().setSpScalingVar(scaleArr[scaleArrIndexSp]);
					// spScaleLbl.setText(String.valueOf(scaleArr[scaleArrIndexSp]));
					addScaleVarLbl(SPO2Pane, String.valueOf(scaleArr[scaleArrIndexSp]));
					if (scaleArrIndexSp == 0)
					{
						up.setDisable(false);
						down.setDisable(true);
					}
					System.out.println("- SPO2");
				}
				else if (param.equalsIgnoreCase("etco2"))
				{
					System.out.println("- ETCO2");
					/*scaleArrIndexEtc--;
					Main.getInstance().getPlotGraphs().setEtcScalingVar(scaleArr[scaleArrIndexEtc]);
					etcScaleLbl.setText(String.valueOf(scaleArr[scaleArrIndexEtc]));
					if (scaleArrIndexEtc == 0)
					{
						up.setDisable(false);
						down.setDisable(true);
					}*/
					etco2YvalsArrIndex--;
					etcYAxisLbl = String.valueOf(etco2YAxisValues[etco2YvalsArrIndex]);
					addYLabels(ETCO2Pane, new int[] { etco2YAxisValues[etco2YvalsArrIndex],
					        etco2YAxisValues[etco2YvalsArrIndex] / 2, 0 });
					Main.getInstance().getPlotGraphs().setEtcScalingVar(etco2YAxisValues[etco2YvalsArrIndex] * ETCO2_MULTIPLICATION_FACTOR);
					if (etco2YvalsArrIndex == 0)
					{
						up.setDisable(false);
						down.setDisable(true);
					}

				}
				else if (param.equalsIgnoreCase("pressure"))
				{
					System.out.println("- PRESSURE");
					/*scaleArrIndexPress--;
					Main.getInstance().getPlotAnesthesiaGraph().setPeakScalingVar(scaleArr[scaleArrIndexPress]);
					pressScaleLbl.setText(String.valueOf(scaleArr[scaleArrIndexPress]));
					if (scaleArrIndexPress == 0)
					{
						up.setDisable(false);
						down.setDisable(true);
					}*/

					pressYvalsArrIndex--;
					pressYAxisLbl = String.valueOf(pressureYAxisValues[pressYvalsArrIndex]);
					addYLabels(PressurePane, new int[] { pressureYAxisValues[pressYvalsArrIndex],
					        pressureYAxisValues[pressYvalsArrIndex] / 2, 0 });
					/*double scaledVar = (pressureYAxisValues[pressYvalsArrIndex] * MAX_PRESSURE_VAL)
					        / pressureYAxisValues[pressureYAxisValues.length - 1];*/
					double scaledVar = pressureYAxisValues[pressYvalsArrIndex];
					Main.getInstance().getPlotAnesthesiaGraph().setPeakScalingVar(scaledVar);
					if (pressYvalsArrIndex == 0)
					{
						up.setDisable(false);
						down.setDisable(true);
					}
				}
			}
			catch (Exception e1)
			{
				LOG.error("## Exception occured:", e1);

			}
		});
	}

	public double getInitialPeakScalingVar()
	{
		return pressureYAxisValues[0];
		// return (pressureYAxisValues[0] * MAX_PRESSURE_VAL) /
		// pressureYAxisValues[pressureYAxisValues.length - 1];

	}

	private double calcualteGraphAreaWidth(double percentage)
	{
		double calcWidth = 0;
		pixelsBtwXLines = MINS_OR_SEC_GAP * PIXEL_GAP;
		noOfGraphLines = new Double(percentage / pixelsBtwXLines).intValue();
		// calcWidth = (noOfGraphLines + 1) * pixelsBtwXLines;
		calcWidth = (noOfGraphLines - 1) * pixelsBtwXLines;

		if (calcWidth > percentage + 5)
		{
			calcWidth = (noOfGraphLines) * pixelsBtwXLines;
		}
		else
		{
			noOfGraphLines += 1;
		}
		return calcWidth;
	}

	private void addTimePane()
	{
		TimePane = new Pane();
		TimePane.setPrefSize(graphPane.getPrefWidth() * 0.8, graphPane.getPrefHeight() * 0.03);
		graphPane.getChildren().add(TimePane);
		timePaneHeight = TimePane.getPrefHeight();

	}

	private void addLabelsToTimePane(Calendar currentTime)
	{
		// ---Assumption: 1px covered in 1 sec => 1min-60px
		double xIncrement = -17;

		for (int i = 0; i <= noOfGraphLines; i++)
		{

			Label timeLbl = new Label();
			timeLbl.setText(sdf.format(currentTime.getTime()));
			/*if (i == 0)
				timeLbl.setLayoutX(0);
			else*/
				timeLbl.setLayoutX(xIncrement);

			timeLbl.setStyle("-fx-font-size:15px");
			timeLbl.setTextFill(Color.SILVER);
			TimePane.getChildren().add(timeLbl);
			// lastTimeLabel = sdf.format(currentTime.getTime());
			if (startTimeLabel.isEmpty())
				startTimeLabel = timeLbl.getText();
			lastTimeLabel = timeLbl.getText();
			currentTime.add(Calendar.SECOND, MINS_OR_SEC_GAP);// ---minute
			xIncrement = xIncrement + pixelsBtwXLines;
		}
	}

	/**
	 * This method returns number of milliseconds in one pixel
	 * @return
	 *//*
	private double xValueTimelineSpeed()
	{
		return (noOfGraphLines * MINS_OR_SEC_GAP * 1000) / calcGraphWidth;
	}*/

	/*private int calcIncrementForSecs()
	{
		System.out.println("--");
		int val = new Double(ECGGraphWidth / (noOfGraphLines * MINS_OR_SEC_GAP)).intValue();
		return val;
	}*/

	private void addScaleVarLbl(Pane currentPane, String value)
	{
		// ---Remove old Y labels
		Pane erasePane = new Pane();
		erasePane.setPrefSize(paneWidth * 0.018, currentPane.getPrefHeight());
		erasePane.setLayoutX(0 - currentPane.getPrefWidth() * 0.022);
		erasePane.setStyle("-fx-background-color:black");

		currentPane.getChildren().addAll(erasePane);

		Label yLbl = new Label(value);
		yLbl.setTextFill(Color.WHITE);
		yLbl.setLayoutY(
		        currentPane.getLayoutX() + (currentPane.getPrefHeight() * 0.5) - currentPane.getPrefHeight() * 0.12);
		yLbl.setLayoutX(0 - currentPane.getPrefWidth() * 0.022);

		if (currentPane.getId().equalsIgnoreCase("ECGPane"))
		{
			yLbl.setTextFill(Color.web("#00ff00"));
		}
		else if (currentPane.getId().equalsIgnoreCase("SPO2Pane"))
		{
			yLbl.setTextFill(Color.web("#ffd966"));
		}
		currentPane.getChildren().addAll(yLbl);
	}

	private void addYLabels(Pane currentPane, int vals[])
	{
		// ---Remove old Y labels
		Pane erasePane = new Pane();
		erasePane.setPrefSize(paneWidth * 0.018, currentPane.getPrefHeight());
		erasePane.setLayoutX(0 - currentPane.getPrefWidth() * 0.022);
		erasePane.setStyle("-fx-background-color:black");

		currentPane.getChildren().addAll(erasePane);

		Label yLbl1 = new Label(String.valueOf(vals[0]));
		yLbl1.setTextFill(Color.WHITE);
		yLbl1.setLayoutY(currentPane.getLayoutX() - currentPane.getPrefHeight() * 0.05);
		yLbl1.setLayoutX(0 - currentPane.getPrefWidth() * 0.022);
		if (yLbl1.getText().length() == 1)
		{
			yLbl1.setLayoutX(0 - currentPane.getPrefWidth() * 0.008);
		}
		else if (yLbl1.getText().length() == 2)
		{
			yLbl1.setLayoutX(0 - currentPane.getPrefWidth() * 0.015);
		}
		else if (yLbl1.getText().length() == 4)
		{
			yLbl1.setLayoutX(0 - currentPane.getPrefWidth() * 0.028);
		}


		Label yLbl3 = new Label(String.valueOf(vals[1]));
		yLbl3.setTextFill(Color.WHITE);
		yLbl3.setLayoutY(currentPane.getLayoutX() + (currentPane.getPrefHeight() * 0.5) - currentPane.getPrefHeight() * 0.12);
		yLbl3.setLayoutX(0 - currentPane.getPrefWidth() * 0.022);
		if (yLbl3.getText().length() == 1)
		{
			yLbl3.setLayoutX(0 - currentPane.getPrefWidth() * 0.008);
		}
		else if (yLbl3.getText().length() == 2)
		{
			yLbl3.setLayoutX(0 - currentPane.getPrefWidth() * 0.015);
		}
		else if (yLbl3.getText().length() == 4)
		{
			yLbl3.setLayoutX(0 - currentPane.getPrefWidth() * 0.028);
		}

		Label yLbl2 = new Label(String.valueOf(vals[2]));
		yLbl2.setTextFill(Color.WHITE);
		yLbl2.setLayoutY(currentPane.getLayoutX() + currentPane.getPrefHeight() - currentPane.getPrefHeight() * 0.18);
		yLbl2.setLayoutX(0 - currentPane.getPrefWidth() * 0.022);
		if (yLbl2.getText().length() == 1)
		{
			yLbl2.setLayoutX(0 - currentPane.getPrefWidth() * 0.008);
		}
		else if (yLbl2.getText().length() == 2)
		{
			yLbl2.setLayoutX(0 - currentPane.getPrefWidth() * 0.015);
		}
		else if (yLbl2.getText().length() == 4)
		{
			yLbl2.setLayoutX(0 - currentPane.getPrefWidth() * 0.028);
		}

		if(currentPane.getId().equalsIgnoreCase("ECGPane"))
		{
			yLbl1.setTextFill(Color.web("#00ff00"));
			yLbl3.setTextFill(Color.web("#00ff00"));
			yLbl2.setTextFill(Color.web("#00ff00"));
		}
		else if (currentPane.getId().equalsIgnoreCase("IBPPane"))
		{
			yLbl1.setTextFill(Color.web("#ff5050"));
			yLbl3.setTextFill(Color.web("#ff5050"));
			yLbl2.setTextFill(Color.web("#ff5050"));
		}
		else if (currentPane.getId().equalsIgnoreCase("SPO2Pane"))
		{
			yLbl1.setTextFill(Color.web("#ffd966"));
			yLbl3.setTextFill(Color.web("#ffd966"));
			yLbl2.setTextFill(Color.web("#ffd966"));
		}
		else if (currentPane.getId().equalsIgnoreCase("ETCO2Pane"))
		{
			yLbl1.setTextFill(Color.web("#b3ecff"));
			yLbl3.setTextFill(Color.web("#b3ecff"));
			yLbl2.setTextFill(Color.web("#b3ecff"));
		}
		else if (currentPane.getId().equalsIgnoreCase("PressurePane"))
		{
			yLbl1.setTextFill(Color.web("#47d1d1"));
			yLbl3.setTextFill(Color.web("#47d1d1"));
			yLbl2.setTextFill(Color.web("#47d1d1"));
		}

		currentPane.getChildren().addAll(yLbl1, yLbl3, yLbl2);

	}


	private void divideleftPanel(Pane leftPanel)
	{

		// ---LOWER PANEL AREA
		Pane lowerHalf = new Pane();
		lowerHalf.setPrefSize(leftPanel.getPrefWidth(), leftPanel.getPrefHeight() * 0.5);

		//---UPPER PANEL AREA
		Pane upperHalf = new Pane();
		upperHalf.setPrefSize(leftPanel.getPrefWidth(), leftPanel.getPrefHeight() * 0.5);
		upperHalf.setLayoutY(lowerHalf.getPrefHeight() * 1.03);

		// ---O2
		Pane o2Pane = new Pane();
		o2Pane.setStyle(" -fx-border-color:grey;");
		o2Pane.setPrefSize(upperHalf.getPrefWidth() * 0.48, upperHalf.getPrefHeight() * 0.3);

		FlowPane o2lblPane = new FlowPane();
		o2lblPane.setPrefSize(o2Pane.getPrefWidth(), o2Pane.getPrefHeight() * 0.2);
		o2lblPane.setAlignment(Pos.CENTER);
		Label o2Lbl = new Label();
		o2Lbl.setText("O2");
		o2Lbl.setTextFill(Color.SILVER);
		// o2Lbl.setStyle("-fx-font-weight:bold");
		// o2Lbl.setStyle("-fx-font-size:" + o2Pane.getPrefWidth() * 0.25 +
		// "px;-fx-font-family:Haettenschweiler");
		// o2Lbl.setLayoutX(o2Pane.getPrefWidth() * 0.05);
		// o2Lbl.setLayoutY(o2Pane.getPrefHeight() * 0.05);
		o2lblPane.getChildren().add(o2Lbl);

		FlowPane o2unitPane = new FlowPane();
		o2unitPane.setAlignment(Pos.CENTER);
		o2unitPane.setPrefSize(o2Pane.getPrefWidth(), o2Pane.getPrefHeight() * 0.2);
		Label o2Unit = new Label();
		o2Unit.setText("%");
		o2Unit.setTextFill(Color.SILVER);
		o2unitPane.setLayoutY(o2lblPane.getPrefHeight());
		o2unitPane.getChildren().add(o2Unit);

		FlowPane o2valPane = new FlowPane();
		o2valPane.setAlignment(Pos.CENTER);
		o2valPane.setPrefSize(o2Pane.getPrefWidth(), o2Pane.getPrefHeight() * 0.6);
		o2Val = new Label("---");
		o2Val.setTextFill(Color.WHITE);
		o2Val.setStyle("-fx-font-size:" + o2Pane.getPrefWidth() * 0.4 + "px;-fx-font-weight:bold");
		// o2Val.setLayoutX(o2Pane.getPrefWidth() * 0.08);
		// o2Val.setLayoutY(o2Pane.getPrefHeight() * 0.25);
		o2valPane.setLayoutY(o2lblPane.getPrefHeight() + o2unitPane.getPrefHeight());
		o2valPane.getChildren().add(o2Val);

		o2Pane.getChildren().addAll(o2lblPane, o2unitPane, o2valPane);

		// ---totalflow
		Pane totalFlowPane = new Pane();
		totalFlowPane.setStyle(" -fx-border-color:grey;");
		totalFlowPane.setPrefSize(upperHalf.getPrefWidth() * 0.48, upperHalf.getPrefHeight() * 0.3);
		totalFlowPane.setLayoutX(totalFlowPane.getPrefWidth() + upperHalf.getPrefWidth() * 0.04);

		FlowPane tflblPane = new FlowPane();
		tflblPane.setPrefSize(totalFlowPane.getPrefWidth(), totalFlowPane.getPrefHeight() * 0.2);
		tflblPane.setAlignment(Pos.CENTER);
		Label totalFlowLbl = new Label("Total Flow");
		totalFlowLbl.setTextFill(Color.SILVER);
		// totalFlowLbl.setStyle("-fx-font-weight:bold");
		//totalFlowLbl.setStyle("-fx-font-size:" + totalFlowPane.getPrefWidth() * 0.25 + "px;-fx-font-family:Haettenschweiler");
		//totalFlowLbl.setLayoutX(totalFlowPane.getPrefWidth() * 0.05);
		//totalFlowLbl.setLayoutY(totalFlowPane.getPrefHeight() * 0.05);
		tflblPane.getChildren().add(totalFlowLbl);

		FlowPane tfunitPane = new FlowPane();
		tfunitPane.setPrefSize(o2Pane.getPrefWidth(), o2Pane.getPrefHeight() * 0.2);
		tfunitPane.setAlignment(Pos.CENTER);
		Label tfUnit = new Label();
		tfUnit.setText("l/min");
		tfUnit.setTextFill(Color.SILVER);
		tfunitPane.setLayoutY(tflblPane.getPrefHeight());
		tfunitPane.getChildren().add(tfUnit);

		FlowPane tfvalPane = new FlowPane();
		tfvalPane.setPrefSize(o2Pane.getPrefWidth(), o2Pane.getPrefHeight() * 0.6);
		tfvalPane.setAlignment(Pos.CENTER);
		totalFlowVal = new Label("---");
		totalFlowVal.setTextFill(Color.WHITE);
		totalFlowVal.setStyle("-fx-font-size:" + totalFlowPane.getPrefWidth() * 0.4 + "px;-fx-font-weight:bold");
		//totalFlowVal.setLayoutX(totalFlowPane.getPrefWidth() * 0.08);
		//totalFlowVal.setLayoutY(totalFlowPane.getPrefHeight() * 0.25);
		tfvalPane.setLayoutY(tflblPane.getPrefHeight() + tfunitPane.getPrefHeight());
		tfvalPane.getChildren().add(totalFlowVal);

		totalFlowPane.getChildren().addAll(tflblPane, tfunitPane, tfvalPane);



		// ---TVInsp
		Pane tvinspPane = new Pane();
		tvinspPane.setStyle(" -fx-border-color:grey;");
		tvinspPane.setPrefSize(upperHalf.getPrefWidth() * 0.48, upperHalf.getPrefHeight() * 0.3);
		tvinspPane.setLayoutY(o2Pane.getPrefHeight() + upperHalf.getPrefHeight() * 0.03);

		FlowPane tvlblPane = new FlowPane();
		tvlblPane.setPrefSize(tvinspPane.getPrefWidth(), tvinspPane.getPrefHeight() * 0.2);
		tvlblPane.setAlignment(Pos.CENTER);
		Label tvinspLbl = new Label("TVInsp");
		tvinspLbl.setTextFill(Color.SILVER);
		// totalFlowLbl.setStyle("-fx-font-weight:bold");
		//tvinspLbl.setLayoutX(tvinspPane.getPrefWidth() * 0.05);
		//tvinspLbl.setLayoutY(tvinspPane.getPrefHeight() * 0.05);
		tvlblPane.getChildren().add(tvinspLbl);

		FlowPane tvunitPane = new FlowPane();
		tvunitPane.setPrefSize(tvinspPane.getPrefWidth(), tvinspPane.getPrefHeight() * 0.2);
		tvunitPane.setAlignment(Pos.CENTER);
		Label tvUnit = new Label();
		tvUnit.setText("ml");
		tvUnit.setTextFill(Color.SILVER);
		tvunitPane.setLayoutY(tvlblPane.getPrefHeight());
		tvunitPane.getChildren().add(tvUnit);

		FlowPane tvvalPane = new FlowPane();
		tvvalPane.setPrefSize(tvinspPane.getPrefWidth(), tvinspPane.getPrefHeight() * 0.6);
		tvvalPane.setAlignment(Pos.CENTER);
		tvinspVal = new Label("---");
		tvinspVal.setTextFill(Color.WHITE);
		tvinspVal.setStyle("-fx-font-size:" + tvinspPane.getPrefWidth() * 0.4 + "px;-fx-font-weight:bold");
		//tvinspVal.setLayoutX(tvinspPane.getPrefWidth() * 0.13);
		//tvinspVal.setLayoutY(tvinspPane.getPrefHeight() * 0.25);
		tvvalPane.setLayoutY(tvlblPane.getPrefHeight() + tvunitPane.getPrefHeight());
		tvvalPane.getChildren().add(tvinspVal);

		tvinspPane.getChildren().addAll(tvlblPane, tvunitPane, tvvalPane);


		// ---rrset
		Pane rrsetPane = new Pane();
		rrsetPane.setStyle(" -fx-border-color:grey;");
		rrsetPane.setPrefSize(lowerHalf.getPrefWidth() * 0.48, lowerHalf.getPrefHeight() * 0.31);
		rrsetPane.setLayoutX(tvinspPane.getPrefWidth() + lowerHalf.getPrefWidth() * 0.04);
		rrsetPane.setLayoutY(tvinspPane.getPrefHeight() + lowerHalf.getPrefHeight() * 0.03);

		FlowPane rrlblPane = new FlowPane();
		rrlblPane.setPrefSize(rrsetPane.getPrefWidth(), rrsetPane.getPrefHeight() * 0.2);
		rrlblPane.setAlignment(Pos.CENTER);
		Label rrsetLbl = new Label("RRSet");
		rrsetLbl.setTextFill(Color.SILVER);
		// rrsetLbl.setStyle("-fx-font-weight:bold");
		//rrsetLbl.setStyle("-fx-font-size:" + rrsetPane.getPrefWidth() * 0.25 + "px;-fx-font-family:Haettenschweiler");
		//rrsetLbl.setLayoutX(rrsetPane.getPrefWidth() * 0.05);
		//rrsetLbl.setLayoutY(rrsetPane.getPrefHeight() * 0.05);
		rrlblPane.getChildren().add(rrsetLbl);

		FlowPane rrunitPane = new FlowPane();
		rrunitPane.setPrefSize(rrsetPane.getPrefWidth(), rrsetPane.getPrefHeight() * 0.2);
		rrunitPane.setAlignment(Pos.CENTER);
		Label rrUnit = new Label();
		rrUnit.setText("/min");
		rrUnit.setTextFill(Color.SILVER);
		rrunitPane.setLayoutY(rrlblPane.getPrefHeight());
		rrunitPane.getChildren().add(rrUnit);

		FlowPane rrvalPane = new FlowPane();
		rrvalPane.setPrefSize(rrsetPane.getPrefWidth(), rrsetPane.getPrefHeight() * 0.6);
		rrvalPane.setAlignment(Pos.CENTER);
		rrsetVal = new Label("---");
		rrsetVal.setTextFill(Color.WHITE);
		rrsetVal.setStyle("-fx-font-size:" + rrsetPane.getPrefWidth() * 0.4 + "px;-fx-font-weight:bold");
		//rrsetVal.setLayoutX(rrsetPane.getPrefWidth() * 0.1);
		//rrsetVal.setLayoutY(rrsetPane.getPrefHeight() * 0.25);
		rrvalPane.setLayoutY(rrlblPane.getPrefHeight() + rrunitPane.getPrefHeight());
		rrvalPane.getChildren().add(rrsetVal);

		rrsetPane.getChildren().addAll(rrlblPane, rrunitPane, rrvalPane);

		// ---I:Eratio
		Pane iePane = new Pane();
		iePane.setStyle(" -fx-border-color:grey;");
		iePane.setPrefSize(upperHalf.getPrefWidth() * 0.48, upperHalf.getPrefHeight() * 0.3);
		iePane.setLayoutY(o2Pane.getPrefHeight() + tvinspPane.getPrefHeight() + upperHalf.getPrefHeight() * 0.06);

		FlowPane ielbllPane = new FlowPane();
		ielbllPane.setPrefSize(iePane.getPrefWidth(), iePane.getPrefHeight() * 0.2);
		ielbllPane.setAlignment(Pos.CENTER);
		Label ieLbl = new Label("I:E");
		ieLbl.setTextFill(Color.SILVER);
		// ieLbl.setStyle("-fx-font-weight:bold");
		//ieLbl.setStyle("-fx-font-size:" + iePane.getPrefWidth() * 0.25 + "px;-fx-font-family:Haettenschweiler");
		//ieLbl.setLayoutX(iePane.getPrefWidth() * 0.05);
		//ieLbl.setLayoutY(iePane.getPrefHeight() * 0.05);
		ielbllPane.getChildren().add(ieLbl);

		FlowPane ieunitPane = new FlowPane();
		ieunitPane.setPrefSize(iePane.getPrefWidth(), iePane.getPrefHeight() * 0.2);
		ieunitPane.setAlignment(Pos.CENTER);
		Label ieUnit = new Label();
		ieUnit.setText("");
		ieUnit.setTextFill(Color.SILVER);
		ieunitPane.setLayoutY(ielbllPane.getPrefHeight());
		ieunitPane.getChildren().add(ieUnit);

		FlowPane ievalPane = new FlowPane();
		ievalPane.setPrefSize(iePane.getPrefWidth(), iePane.getPrefHeight() * 0.6);
		ievalPane.setAlignment(Pos.CENTER);
		ieVal = new Label("---");
		ieVal.setTextFill(Color.WHITE);
		ieVal.setStyle("-fx-font-size:" + iePane.getPrefWidth() * 0.4 + "px;-fx-font-weight:bold");
		//ieVal.setLayoutX(iePane.getPrefWidth() * 0.13);
		//ieVal.setLayoutY(iePane.getPrefHeight() * 0.25);
		ievalPane.setLayoutY(ielbllPane.getPrefHeight()+ieunitPane.getPrefHeight());
		ievalPane.getChildren().add(ieVal);

		iePane.getChildren().addAll(ielbllPane,ieunitPane,ievalPane);

		// ---peepi
		Pane peepiPane = new Pane();
		peepiPane.setStyle(" -fx-border-color:grey;");
		peepiPane.setPrefSize(lowerHalf.getPrefWidth() * 0.48, lowerHalf.getPrefHeight() * 0.31);
		peepiPane.setLayoutX(peepiPane.getPrefWidth() + lowerHalf.getPrefWidth() * 0.04);
		peepiPane.setLayoutY(o2Pane.getPrefHeight() + tvinspPane.getPrefHeight() + lowerHalf.getPrefHeight() * 0.06);

		FlowPane peeplblPane = new FlowPane();
		peeplblPane.setPrefSize(peepiPane.getPrefWidth(), peepiPane.getPrefHeight() * 0.2);
		peeplblPane.setAlignment(Pos.CENTER);
		Label peepiLbl = new Label("PEEP");
		peepiLbl.setTextFill(Color.SILVER);
		// peepiLbl.setStyle("-fx-font-weight:bold");
		//peepiLbl.setStyle("-fx-font-size:" + peepiPane.getPrefWidth() * 0.25 + "px;-fx-font-family:Haettenschweiler");
		// peepiLbl.setLayoutX(peepiPane.getPrefWidth() * 0.05);
		// peepiLbl.setLayoutY(peepiPane.getPrefHeight() * 0.05);
		peeplblPane.getChildren().add(peepiLbl);

		FlowPane peepunitPane = new FlowPane();
		peepunitPane.setPrefSize(peepiPane.getPrefWidth(), peepiPane.getPrefHeight() * 0.2);
		peepunitPane.setAlignment(Pos.CENTER);
		Label peepUnit = new Label();
		peepUnit.setText("cmH2O");
		peepUnit.setTextFill(Color.SILVER);
		peepunitPane.setLayoutY(peeplblPane.getPrefHeight());
		peepunitPane.getChildren().add(peepUnit);

		FlowPane peepvalPane = new FlowPane();
		peepvalPane.setPrefSize(peepiPane.getPrefWidth(), peepiPane.getPrefHeight() * 0.6);
		peepvalPane.setAlignment(Pos.CENTER);
		peepiVal = new Label("---");
		peepiVal.setTextFill(Color.WHITE);
		peepiVal.setStyle("-fx-font-size:" + peepiPane.getPrefWidth() * 0.4 + "px;-fx-font-weight:bold");
		// peepiVal.setLayoutX(peepiPane.getPrefWidth() * 0.2);
		// peepiVal.setLayoutY(peepiPane.getPrefHeight() * 0.25);
		peepvalPane.setLayoutY(peeplblPane.getPrefHeight() + peepunitPane.getPrefHeight());
		peepvalPane.getChildren().add(peepiVal);

		peepiPane.getChildren().addAll(peeplblPane, peepunitPane, peepvalPane);

		upperHalf.getChildren().addAll(o2Pane, totalFlowPane, tvinspPane, rrsetPane, iePane, peepiPane);




		// ---LOWER PANEL AREA
		// ---pmax
		Pane pmaxPane = new Pane();
		pmaxPane.setStyle(" -fx-border-color:grey;");
		pmaxPane.setPrefSize(lowerHalf.getPrefWidth() * 0.48, lowerHalf.getPrefHeight() * 0.31);

		FlowPane pmaxlblPane = new FlowPane();
		pmaxlblPane.setPrefSize(pmaxPane.getPrefWidth(), pmaxPane.getPrefHeight() * 0.2);
		pmaxlblPane.setAlignment(Pos.CENTER);
		Label pmaxLbl = new Label("Ppeak");
		pmaxLbl.setTextFill(Color.web("#ff5050"));
		// pmaxLbl.setStyle("-fx-font-weight:bold");
		//pmaxLbl.setStyle("-fx-font-size:" + pmaxPane.getPrefWidth() * 0.25 + "px;-fx-font-family:Haettenschweiler");
		//pmaxLbl.setLayoutX(pmaxPane.getPrefWidth() * 0.05);
		//pmaxLbl.setLayoutY(pmaxPane.getPrefHeight() * 0.05);
		pmaxlblPane.getChildren().add(pmaxLbl);

		FlowPane pmaxunitPane = new FlowPane();
		pmaxunitPane.setPrefSize(pmaxPane.getPrefWidth(), pmaxPane.getPrefHeight() * 0.2);
		pmaxunitPane.setAlignment(Pos.CENTER);
		Label pmaxUnit = new Label();
		pmaxUnit.setText("cmH20");
		pmaxUnit.setTextFill(Color.web("#ff5050"));
		pmaxunitPane.setLayoutY(pmaxlblPane.getPrefHeight());
		pmaxunitPane.getChildren().add(pmaxUnit);

		FlowPane pmaxvalPane = new FlowPane();
		pmaxvalPane.setPrefSize(pmaxPane.getPrefWidth(), pmaxPane.getPrefHeight() * 0.6);
		pmaxvalPane.setAlignment(Pos.CENTER);
		pmaxVal = new Label("---");
		pmaxVal.setTextFill(Color.web("#ff5050"));
		pmaxVal.setStyle("-fx-font-size:" + pmaxPane.getPrefWidth() * 0.4 + "px;-fx-font-weight:bold");
		//pmaxVal.setLayoutX(pmaxPane.getPrefWidth() * 0.1);
		//pmaxVal.setLayoutY(pmaxPane.getPrefHeight() * 0.25);
		pmaxvalPane.setLayoutY(pmaxlblPane.getPrefHeight()+pmaxunitPane.getPrefHeight());
		pmaxvalPane.getChildren().add(pmaxVal);

		pmaxPane.getChildren().addAll(pmaxlblPane,pmaxunitPane,pmaxvalPane);

		// ---Pmean
		Pane pmeanPane = new Pane();
		pmeanPane.setStyle(" -fx-border-color:grey;");
		pmeanPane.setPrefSize(lowerHalf.getPrefWidth() * 0.48, lowerHalf.getPrefHeight() * 0.31);
		pmeanPane.setLayoutX(pmaxPane.getPrefWidth() + lowerHalf.getPrefWidth() * 0.04);

		FlowPane pmeanlblPane = new FlowPane();
		pmeanlblPane.setPrefSize(pmeanPane.getPrefWidth(), pmeanPane.getPrefHeight() * 0.2);
		pmeanlblPane.setAlignment(Pos.CENTER);
		Label pmeanLbl = new Label("Pmean");
		pmeanLbl.setTextFill(Color.web("#ff5050"));
		// pmeanLbl.setStyle("-fx-font-weight:bold");
		//pmeanLbl.setStyle("-fx-font-size:" + pmeanPane.getPrefWidth() * 0.25 + "px;-fx-font-family:Haettenschweiler");
		//pmeanLbl.setLayoutX(pmeanPane.getPrefWidth() * 0.05);
		//pmeanLbl.setLayoutY(pmeanPane.getPrefHeight() * 0.05);
		pmeanlblPane.getChildren().add(pmeanLbl);

		FlowPane pmeanunitPane = new FlowPane();
		pmeanunitPane.setPrefSize(pmeanPane.getPrefWidth(), pmeanPane.getPrefHeight() * 0.2);
		pmeanunitPane.setAlignment(Pos.CENTER);
		Label pmeanUnit = new Label();
		pmeanUnit.setText("cmH20");
		pmeanUnit.setTextFill(Color.web("#ff5050"));
		pmeanunitPane.setLayoutY(pmeanlblPane.getPrefHeight());
		pmeanunitPane.getChildren().add(pmeanUnit);

		FlowPane pmeanvalPane = new FlowPane();
		pmeanvalPane.setPrefSize(pmeanPane.getPrefWidth(), pmeanPane.getPrefHeight() * 0.6);
		pmeanvalPane.setAlignment(Pos.CENTER);
		pmeanVal = new Label("---");
		pmeanVal.setTextFill(Color.web("#ff5050"));
		pmeanVal.setStyle("-fx-font-size:" + pmeanPane.getPrefWidth() * 0.4 + "px;-fx-font-weight:bold");
		//pmeanVal.setLayoutX(pmeanPane.getPrefWidth() * 0.1);
		//pmeanVal.setLayoutY(pmeanPane.getPrefHeight() * 0.25);
		pmeanvalPane.setLayoutY(pmeanlblPane.getPrefHeight()+pmeanunitPane.getPrefHeight());
		pmeanvalPane.getChildren().add(pmeanVal);

		pmeanPane.getChildren().addAll(pmeanlblPane,pmeanunitPane,pmeanvalPane);

		// ---Peep
		Pane peepPane = new Pane();
		peepPane.setStyle(" -fx-border-color:grey;");
		peepPane.setPrefSize(lowerHalf.getPrefWidth() * 0.48, lowerHalf.getPrefHeight() * 0.31);
		peepPane.setLayoutY(pmaxPane.getPrefHeight() + lowerHalf.getPrefHeight() * 0.03);

		FlowPane plblPane = new FlowPane();
		plblPane.setPrefSize(peepPane.getPrefWidth(), peepPane.getPrefHeight() * 0.2);
		plblPane.setAlignment(Pos.CENTER);
		Label peepLbl = new Label("PEEP");
		peepLbl.setTextFill(Color.web("#ff5050"));
		// peepLbl.setStyle("-fx-font-weight:bold");
		//peepLbl.setStyle("-fx-font-size:" + peepPane.getPrefWidth() * 0.25 + "px;-fx-font-family:Haettenschweiler");
		//peepLbl.setLayoutX(peepPane.getPrefWidth() * 0.05);
		//peepLbl.setLayoutY(peepPane.getPrefHeight() * 0.05);
		plblPane.getChildren().add(peepLbl);

		FlowPane punitPane = new FlowPane();
		punitPane.setPrefSize(peepPane.getPrefWidth(), peepPane.getPrefHeight() * 0.2);
		punitPane.setAlignment(Pos.CENTER);
		Label pUnit = new Label();
		pUnit.setText("cmH20");
		pUnit.setTextFill(Color.web("#ff5050"));
		punitPane.setLayoutY(plblPane.getPrefHeight());
		punitPane.getChildren().add(pUnit);

		FlowPane pvalPane = new FlowPane();
		pvalPane.setPrefSize(peepPane.getPrefWidth(), peepPane.getPrefHeight() * 0.6);
		pvalPane.setAlignment(Pos.CENTER);
		peepVal = new Label("---");
		peepVal.setTextFill(Color.web("#ff5050"));
		peepVal.setStyle("-fx-font-size:" + peepPane.getPrefWidth() * 0.4 + "px;-fx-font-weight:bold");
		//peepVal.setLayoutX(peepPane.getPrefWidth() * 0.2);
		//peepVal.setLayoutY(peepPane.getPrefHeight() * 0.25);
		pvalPane.setLayoutY(plblPane.getPrefHeight()+punitPane.getPrefHeight());
		pvalPane.getChildren().add(peepVal);

		peepPane.getChildren().addAll(plblPane, punitPane, pvalPane);

		// ---Circuit O2
		Pane circuitO2Pane = new Pane();
		circuitO2Pane.setStyle(" -fx-border-color:grey;");
		circuitO2Pane.setPrefSize(lowerHalf.getPrefWidth() * 0.48, lowerHalf.getPrefHeight() * 0.31);
		circuitO2Pane.setLayoutX(peepPane.getPrefWidth() + lowerHalf.getPrefWidth() * 0.04);
		circuitO2Pane.setLayoutY(pmaxPane.getPrefHeight() + lowerHalf.getPrefHeight() * 0.03);

		FlowPane colblPane = new FlowPane();
		colblPane.setPrefSize(circuitO2Pane.getPrefWidth(), circuitO2Pane.getPrefHeight() * 0.2);
		colblPane.setAlignment(Pos.CENTER);
		Label circuitO2Lbl = new Label("CircuitO2");
		circuitO2Lbl.setTextFill(Color.WHITE);
		// circuitO2Lbl.setStyle("-fx-font-weight:bold");
		//circuitO2Lbl.setStyle("-fx-font-size:" + circuitO2Pane.getPrefWidth() * 0.25 + "px;-fx-font-family:Haettenschweiler");
		//circuitO2Lbl.setLayoutX(circuitO2Pane.getPrefWidth() * 0.05);
		//circuitO2Lbl.setLayoutY(circuitO2Pane.getPrefHeight() * 0.05);
		colblPane.getChildren().add(circuitO2Lbl);

		FlowPane counitPane = new FlowPane();
		counitPane.setPrefSize(circuitO2Pane.getPrefWidth(), circuitO2Pane.getPrefHeight() * 0.2);
		counitPane.setAlignment(Pos.CENTER);
		Label coUnit = new Label();
		coUnit.setText("%");
		coUnit.setTextFill(Color.SILVER);
		counitPane.setLayoutY(colblPane.getPrefHeight());
		counitPane.getChildren().add(coUnit);

		FlowPane covalPane = new FlowPane();
		covalPane.setPrefSize(circuitO2Pane.getPrefWidth(), circuitO2Pane.getPrefHeight() * 0.6);
		covalPane.setAlignment(Pos.CENTER);
		circuitO2Val = new Label("---");
		circuitO2Val.setTextFill(Color.WHITE);
		circuitO2Val.setStyle("-fx-font-size:" + circuitO2Pane.getPrefWidth() * 0.4 + "px;-fx-font-weight:bold");
		//circuitO2Val.setLayoutX(circuitO2Pane.getPrefWidth() * 0.2);
		//circuitO2Val.setLayoutY(circuitO2Pane.getPrefHeight() * 0.25);
		covalPane.setLayoutY(colblPane.getPrefHeight()+counitPane.getPrefHeight());
		covalPane.getChildren().add(circuitO2Val);

		circuitO2Pane.getChildren().addAll(colblPane,counitPane,covalPane);

		// ---MVexp
		Pane mvexpPane = new Pane();
		mvexpPane.setStyle(" -fx-border-color:grey;");
		mvexpPane.setPrefSize(lowerHalf.getPrefWidth() * 0.48, lowerHalf.getPrefHeight() * 0.31);
		mvexpPane.setLayoutY(pmaxPane.getPrefHeight() + peepPane.getPrefHeight() + lowerHalf.getPrefHeight() * 0.06);

		FlowPane mvlblPane = new FlowPane();
		mvlblPane.setPrefSize(mvexpPane.getPrefWidth(), mvexpPane.getPrefHeight() * 0.2);
		mvlblPane.setAlignment(Pos.CENTER);
		Label mvexpLbl = new Label("MVexp");
		mvexpLbl.setTextFill(Color.web("#00ff00"));
		// mvexpLbl.setStyle("-fx-font-weight:bold");
		//mvexpLbl.setStyle("-fx-font-size:" + mvexpPane.getPrefWidth() * 0.21 + "px;-fx-font-family:Haettenschweiler");
		//mvexpLbl.setLayoutX(mvexpPane.getPrefWidth() * 0.05);
		//mvexpLbl.setLayoutY(mvexpPane.getPrefHeight() * 0.05);
		mvlblPane.getChildren().add(mvexpLbl);

		FlowPane mvunitPane = new FlowPane();
		mvunitPane.setPrefSize(mvexpPane.getPrefWidth(), mvexpPane.getPrefHeight() * 0.2);
		mvunitPane.setAlignment(Pos.CENTER);
		Label mvUnit = new Label();
		mvUnit.setText("l/min");
		mvUnit.setTextFill(Color.web("#00ff00"));
		mvunitPane.setLayoutY(mvlblPane.getPrefHeight());
		mvunitPane.getChildren().add(mvUnit);

		FlowPane mvvalPane = new FlowPane();
		mvvalPane.setPrefSize(mvexpPane.getPrefWidth(), mvexpPane.getPrefHeight() * 0.6);
		mvvalPane.setAlignment(Pos.CENTER);
		mvexpVal = new Label("---");
		mvexpVal.setTextFill(Color.web("#00ff00"));
		mvexpVal.setStyle("-fx-font-size:" + mvexpPane.getPrefWidth() * 0.4 + "px;-fx-font-weight:bold");
		//mvexpVal.setLayoutX(mvexpPane.getPrefWidth() * 0.1);
		//mvexpVal.setLayoutY(mvexpPane.getPrefHeight() * 0.25);
		mvvalPane.setLayoutY(mvlblPane.getPrefHeight()+mvunitPane.getPrefHeight());
		mvvalPane.getChildren().add(mvexpVal);

		mvexpPane.getChildren().addAll(mvlblPane,mvunitPane,mvvalPane);

		// ---TVexp
		Pane tvexpPane = new Pane();
		tvexpPane.setStyle(" -fx-border-color:grey;");
		tvexpPane.setPrefSize(lowerHalf.getPrefWidth() * 0.48, lowerHalf.getPrefHeight() * 0.31);
		tvexpPane.setLayoutX(mvexpPane.getPrefWidth() + lowerHalf.getPrefWidth() * 0.04);
		tvexpPane.setLayoutY(pmaxPane.getPrefHeight() + peepPane.getPrefHeight() + lowerHalf.getPrefHeight() * 0.06);

		FlowPane tvxlblPane = new FlowPane();
		tvxlblPane.setPrefSize(tvexpPane.getPrefWidth(), tvexpPane.getPrefHeight() * 0.2);
		tvxlblPane.setAlignment(Pos.CENTER);
		Label tvexpLbl = new Label("TVexp");
		tvexpLbl.setTextFill(Color.web("#00ff00"));
		// tvexpLbl.setStyle("-fx-font-weight:bold");
		//tvexpLbl.setStyle("-fx-font-size:" + tvexpPane.getPrefWidth() * 0.25 + "px;-fx-font-family:Haettenschweiler");
		//tvexpLbl.setLayoutX(tvexpPane.getPrefWidth() * 0.05);
		//tvexpLbl.setLayoutY(tvexpPane.getPrefHeight() * 0.05);
		tvxlblPane.getChildren().add(tvexpLbl);

		FlowPane tvxunitPane = new FlowPane();
		tvxunitPane.setPrefSize(tvexpPane.getPrefWidth(), tvexpPane.getPrefHeight() * 0.2);
		tvxunitPane.setAlignment(Pos.CENTER);
		Label tvxUnit = new Label();
		tvxUnit.setText("ml");
		tvxUnit.setTextFill(Color.web("#00ff00"));
		tvxunitPane.setLayoutY(tvxlblPane.getPrefHeight());
		tvxunitPane.getChildren().add(tvxUnit);

		FlowPane tvxvalPane = new FlowPane();
		tvxvalPane.setPrefSize(tvexpPane.getPrefWidth(), tvexpPane.getPrefHeight() * 0.6);
		tvxvalPane.setAlignment(Pos.CENTER);
		tvexpVal = new Label("---");
		tvexpVal.setTextFill(Color.web("#00ff00"));
		tvexpVal.setStyle("-fx-font-size:" + tvexpPane.getPrefWidth() * 0.4 + "px;-fx-font-weight:bold");
		//tvexpVal.setLayoutX(tvexpPane.getPrefWidth() * 0.1);
		//tvexpVal.setLayoutY(tvexpPane.getPrefHeight() * 0.25);
		tvxvalPane.setLayoutY(tvxlblPane.getPrefHeight() + tvxunitPane.getPrefHeight());
		tvxvalPane.getChildren().add(tvexpVal);

		tvexpPane.getChildren().addAll(tvxlblPane, tvxunitPane, tvxvalPane);

		lowerHalf.getChildren().addAll(pmaxPane, pmeanPane, peepPane, circuitO2Pane, mvexpPane, tvexpPane);

		leftPanel.getChildren().addAll(upperHalf, lowerHalf);
	}


}
