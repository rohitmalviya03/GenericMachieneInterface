/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package com.cdac.device.anesthesia;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import com.cdac.framework.CoreDevice;
import com.cdac.framework.IPacketMakerStrategy;
import com.cdac.framework.SystemConfiguration;
import com.cdac.framework.datastructures.MedicalDeviceData;
import com.cdac.framework.datastructures.NodeBase;
import com.cdac.framework.datastructures.Parameter;
import com.cdac.framework.exceptions.CustomPacketMakerNotFoundException;
import com.cdac.framework.exceptions.StartBytesNotAvailableException;
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortEvent;

import mediatorPattern.ControllerMediator;

public class AnesthesiaParser extends CoreDevice
{

	public static final Logger LOG = Logger.getLogger(AnesthesiaParser.class);
	MedicalDeviceData<List<Byte>> medicalDeviceData;

	public static final String ANESTHESIA_MEASUREDDATA_TVEXP = "Anesthesia.MeasuredData.TVExp";
	public static final String ANESTHESIA_MEASUREDDATA_PEEPExt = "Anesthesia.MeasuredData.PeepExt";
	public static final String ANESTHESIA_MEASUREDDATA_O2FLOW = "Anesthesia.MeasuredData.O2Flow";
	public static final String ANESTHESIA_MEASUREDDATA_MAC = "Anesthesia.MeasuredData.Mac";
	public static final String ANESTHESIA_MEASUREDDATA_N2OFLOW = "Anesthesia.MeasuredData.N2OFlow";
	public static final String ANESTHESIA_MEASUREDDATA_MVEXP = "Anesthesia.MeasuredData.MVExp";
	public static final String ANESTHESIA_MEASUREDDATA_PMEAN = "Anesthesia.MeasuredData.PMean";
	public static final String ANESTHESIA_MEASUREDDATA_RRTOTAL = "Anesthesia.MeasuredData.RRtol";
	public static final String ANESTHESIA_MEASUREDDATA_MVINSP = "Anesthesia.MeasuredData.MVinsp";
	public static final String ANESTHESIA_MEASUREDDATA_PPEAK = "Anesthesia.MeasuredData.PPeak";
	public static final String ANESTHESIA_MEASUREDDATA_CIRCUITO2 = "Anesthesia.MeasuredData.CircuitO2";
	public static final String ANESTHESIA_MEASUREDDATA_O2PERCENT = "Anesthesia.MeasuredData.O2Percent";
	public static final String ANESTHESIA_MEASUREDDATA_TOTALFLOW = "Anesthesia.MeasuredData.TotalFlow";
	public static final String ANESTHESIA_STATUSDATA_RR = "Anesthesia.StatusData.RR";
	public static final String ANESTHESIA_STATUSDATA_PMAX = "Anesthesia.StatusData.PMax";
	public static final String ANESTHESIA_STATUSDATA_IE = "Anesthesia.StatusData.Ie";
	public static final String ANESTHESIA_STATUSDATA_TVINSP = "Anesthesia.StatusData.TVInsp";
	// public static final String ANESTHESIA_STATUSDATA_TOTALFLOW =
	// "Anesthesia.StatusData.TotalFlow";
	public static final String ANESTHESIA_STATUSDATA_PEEP = "Anesthesia.StatusData.Peep";
	public static final String ANESTHESIA_WAVEFORM_PRESSURE = "Anesthesia.Waveform.Pressure";
	public static final String ANESTHESIA_WAVEFORM_CO2 = "Anesthesia.Waveform.CO2";

	/**
	 * This structure is for saving the trend data in csv file
	 */

	/*public static final String ANESTHESIA_MEASUREDDATA_TVEXP_SAVE = "Anesthesia.MeasuredData.TVExp_Save";
	public static final String ANESTHESIA_MEASUREDDATA_PEEPExt_SAVE = "Anesthesia.MeasuredData.PeepExt_Save";
	public static final String ANESTHESIA_MEASUREDDATA_O2FLOW_SAVE = "Anesthesia.MeasuredData.O2Flow_Save";
	public static final String ANESTHESIA_MEASUREDDATA_MAC_SAVE = "Anesthesia.MeasuredData.Mac_Save";
	public static final String ANESTHESIA_MEASUREDDATA_N2OFLOW_SAVE = "Anesthesia.MeasuredData.N2OFlow_Save";
	public static final String ANESTHESIA_MEASUREDDATA_MVEXP_SAVE = "Anesthesia.MeasuredData.MVExp_Save";
	public static final String ANESTHESIA_MEASUREDDATA_PMEAN_SAVE = "Anesthesia.MeasuredData.PMean_Save";
	public static final String ANESTHESIA_MEASUREDDATA_RRTOTAL_SAVE = "Anesthesia.MeasuredData.RRtol_Save";
	public static final String ANESTHESIA_MEASUREDDATA_MVINSP_SAVE = "Anesthesia.MeasuredData.MVinsp_Save";
	public static final String ANESTHESIA_MEASUREDDATA_PPEAK_SAVE = "Anesthesia.MeasuredData.PPeak_Save";
	public static final String ANESTHESIA_MEASUREDDATA_CIRCUITO2_SAVE = "Anesthesia.MeasuredData.CircuitO2_Save";
	public static final String ANESTHESIA_MEASUREDDATA_O2PERCENT_SAVE = "Anesthesia.MeasuredData.O2Percent_Save";
	public static final String ANESTHESIA_MEASUREDDATA_TOTALFLOW_SAVE = "Anesthesia.MeasuredData.TotalFlow_Save";
	public static final String ANESTHESIA_STATUSDATA_RR_SAVE = "Anesthesia.StatusData.RR_Save";
	public static final String ANESTHESIA_STATUSDATA_PMAX_SAVE = "Anesthesia.StatusData.PMax_Save";
	public static final String ANESTHESIA_STATUSDATA_IE_SAVE = "Anesthesia.StatusData.Ie_Save";
	public static final String ANESTHESIA_STATUSDATA_TVINSP_SAVE = "Anesthesia.StatusData.TVInsp_Save";
	// public static final String ANESTHESIA_STATUSDATA_TOTALFLOW_SAVE =
	// "Anesthesia.StatusData.TotalFlow_Save";
	public static final String ANESTHESIA_STATUSDATA_PEEP_SAVE = "Anesthesia.StatusData.Peep_Save";
	public static final String ANESTHESIA_WAVEFORM_PRESSURE_SAVE = "Anesthesia.Waveform.Pressure_Save";
	public static final String ANESTHESIA_WAVEFORM_CO2_SAVE = "Anesthesia.Waveform.CO2_Save";*/


	private Parameter TVExpiratory;
	private Parameter PeepValue;
	private Parameter O2MeasuredFlow;
	private Parameter MACValue;
	private Parameter N2OMeasuredFlow;
	private Parameter MVExpiratory;
	private Parameter TVInspiratory;
	private Parameter MeanPressure;
	private Parameter RespiratoryRateTotal;
	private Parameter MVInspiratory;
	private Parameter PeakPresssure;
	private Parameter CircuitOxygen;
	private Parameter O2Percentage;
	private Parameter RespiratoryRate;
	private Parameter MaximumPressure;
	private Parameter IERatio;
	private Parameter FlowTotal;
	private Parameter PeepSetValue;
	private Parameter PressureWaveform;
	private Parameter CO2Waveform;

	/**
	 * This structure is for saving the trend data in csv file
	 */

	/*private Parameter TVExpiratory_Save;
	private Parameter PeepValue_Save;
	private Parameter O2MeasuredFlow_Save;
	private Parameter MACValue_Save;
	private Parameter N2OMeasuredFlow_Save;
	private Parameter MVExpiratory_Save;
	private Parameter TVInspiratory_Save;
	private Parameter MeanPressure_Save;
	private Parameter RespiratoryRateTotal_Save;
	private Parameter MVInspiratory_Save;
	private Parameter PeakPresssure_Save;
	private Parameter CircuitOxygen_Save;
	private Parameter O2Percentage_Save;
	private Parameter RespiratoryRate_Save;
	private Parameter MaximumPressure_Save;
	private Parameter IERatio_Save;
	private Parameter FlowTotal_Save;
	private Parameter PeepSetValue_Save;
	private Parameter PressureWaveform_Save;
	private Parameter CO2Waveform_Save;*/

	public static final String DEVICE_NAME = "Anesthesia-0";
//	private static final String DEFAULT_TREND_VALUE = "32000|-32000";

	private PacketMakerOnStartByteAndEndByteBasis packetMakerOnStartByteAndEndByteBasis;

	public AnesthesiaParser(String workName, int messageLength) throws CustomPacketMakerNotFoundException
	{
		super(workName, messageLength, null, PacketingStrategy.CUSTOM);
		LOG.info("AnesthesiaParser constructor called");
		medicalDeviceData = createIHEDataObject();
		packetMakerOnStartByteAndEndByteBasis = new PacketMakerOnStartByteAndEndByteBasis(new byte[] { 58, 86, 84 },
		        new byte[] { 13 });
	}

	@Override
	public void deviceEventOccurred(SerialPortEvent dataFromPort)
	{
		// TODO Auto-generated method stub
		if (dataFromPort.getEventType() == SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
		{
			byte[] packet = dataFromPort.getReceivedData();
			// System.out.println("!!!!!!!!!Packets Reached Parser!!!!!!!!!!");
			for (byte b : packet)
			{
				// System.out.print(" " + b);
			}
			if (packet[0] == 100)
			{
				parsingLogicForVTd(packet);
			}
			//else if (packet[0] == 113 && SystemConfiguration.getInstance().getParseAnesthesiaWaveform())
			else if (packet[0] == 113 && ControllerMediator.getInstance().getMainController().isPublishMode())
			{
				parsingLogicForVTq(packet);
			}
//			else if (packet[0] == 119 && SystemConfiguration.getInstance().getParseAnesthesiaWaveform())
			else if (packet[0] == 119 && ControllerMediator.getInstance().getMainController().isPublishMode())
			{
				parsingLogicForVTw(packet);
			}
			setDataProcessingStatus(Parsing.SUCCESSFUL);
		}
	}

	@Override
	public PacketingStrategy getPacketingStrategy()
	{
		return PacketingStrategy.CUSTOM;
	}

	@Override
	public IPacketMakerStrategy getCustomPacketMaker() throws CustomPacketMakerNotFoundException
	{
		return packetMakerOnStartByteAndEndByteBasis;
	}

	@Override
	public byte[] getStartBytes() throws StartBytesNotAvailableException
	{
		// TODO Auto-generated method stub
		byte[] arrayOfStartBytes = { 58, 86, 84 };
		return arrayOfStartBytes;
	}

	@Override
	public int getListeningEvent()
	{
		return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
	}

	@Override
	protected MedicalDeviceData createIHEDataObject()
	{
		// TODO Auto-generated method stub
		medicalDeviceData = new MedicalDeviceData<List<Byte>>("Anesthesia");

		TVExpiratory = (Parameter) medicalDeviceData.createOrGetParameter(ANESTHESIA_MEASUREDDATA_TVEXP)[2];
		PeepValue = (Parameter) medicalDeviceData.createOrGetParameter(ANESTHESIA_MEASUREDDATA_PEEPExt)[2];
		O2MeasuredFlow = (Parameter) medicalDeviceData.createOrGetParameter(ANESTHESIA_MEASUREDDATA_O2FLOW)[2];
		MACValue = (Parameter) medicalDeviceData.createOrGetParameter(ANESTHESIA_MEASUREDDATA_MAC)[2];
		N2OMeasuredFlow = (Parameter) medicalDeviceData.createOrGetParameter(ANESTHESIA_MEASUREDDATA_N2OFLOW)[2];
		MVExpiratory = (Parameter) medicalDeviceData.createOrGetParameter(ANESTHESIA_MEASUREDDATA_MVEXP)[2];
		MeanPressure = (Parameter) medicalDeviceData.createOrGetParameter(ANESTHESIA_MEASUREDDATA_PMEAN)[2];
		RespiratoryRateTotal = (Parameter) medicalDeviceData.createOrGetParameter(ANESTHESIA_MEASUREDDATA_RRTOTAL)[2];
		MVInspiratory = (Parameter) medicalDeviceData.createOrGetParameter(ANESTHESIA_MEASUREDDATA_MVINSP)[2];
		PeakPresssure = (Parameter) medicalDeviceData.createOrGetParameter(ANESTHESIA_MEASUREDDATA_PPEAK)[2];
		CircuitOxygen = (Parameter) medicalDeviceData.createOrGetParameter(ANESTHESIA_MEASUREDDATA_CIRCUITO2)[2];
		O2Percentage = (Parameter) medicalDeviceData.createOrGetParameter(ANESTHESIA_MEASUREDDATA_O2PERCENT)[2];
		FlowTotal = (Parameter) medicalDeviceData.createOrGetParameter(ANESTHESIA_MEASUREDDATA_TOTALFLOW)[2];
		RespiratoryRate = (Parameter) medicalDeviceData.createOrGetParameter(ANESTHESIA_STATUSDATA_RR)[2];
		MaximumPressure = (Parameter) medicalDeviceData.createOrGetParameter(ANESTHESIA_STATUSDATA_PMAX)[2];
		IERatio = (Parameter) medicalDeviceData.createOrGetParameter(ANESTHESIA_STATUSDATA_IE)[2];
		TVInspiratory = (Parameter) medicalDeviceData.createOrGetParameter(ANESTHESIA_STATUSDATA_TVINSP)[2];
		// FlowTotal = (Parameter)
		// medicalDeviceData.createOrGetParameter(ANESTHESIA_STATUSDATA_TOTALFLOW)[2];
		PeepSetValue = (Parameter) medicalDeviceData.createOrGetParameter(ANESTHESIA_STATUSDATA_PEEP)[2];
		PressureWaveform = (Parameter) medicalDeviceData.createOrGetParameter(ANESTHESIA_WAVEFORM_PRESSURE)[2];
		CO2Waveform = (Parameter) medicalDeviceData.createOrGetParameter(ANESTHESIA_WAVEFORM_CO2)[2];

		/**
		 * This structure is for saving the trend data in csv file
		 */

		/*TVExpiratory_Save = (Parameter) medicalDeviceData.createOrGetParameter(ANESTHESIA_MEASUREDDATA_TVEXP_SAVE)[2];
		PeepValue_Save = (Parameter) medicalDeviceData.createOrGetParameter(ANESTHESIA_MEASUREDDATA_PEEPExt_SAVE)[2];
		O2MeasuredFlow_Save = (Parameter) medicalDeviceData.createOrGetParameter(ANESTHESIA_MEASUREDDATA_O2FLOW_SAVE)[2];
		MACValue_Save = (Parameter) medicalDeviceData.createOrGetParameter(ANESTHESIA_MEASUREDDATA_MAC_SAVE)[2];
		N2OMeasuredFlow_Save = (Parameter) medicalDeviceData.createOrGetParameter(ANESTHESIA_MEASUREDDATA_N2OFLOW_SAVE)[2];
		MVExpiratory_Save = (Parameter) medicalDeviceData.createOrGetParameter(ANESTHESIA_MEASUREDDATA_MVEXP_SAVE)[2];
		MeanPressure_Save = (Parameter) medicalDeviceData.createOrGetParameter(ANESTHESIA_MEASUREDDATA_PMEAN_SAVE)[2];
		RespiratoryRateTotal_Save = (Parameter) medicalDeviceData.createOrGetParameter(ANESTHESIA_MEASUREDDATA_RRTOTAL_SAVE)[2];
		MVInspiratory_Save = (Parameter) medicalDeviceData.createOrGetParameter(ANESTHESIA_MEASUREDDATA_MVINSP_SAVE)[2];
		PeakPresssure_Save = (Parameter) medicalDeviceData.createOrGetParameter(ANESTHESIA_MEASUREDDATA_PPEAK_SAVE)[2];
		CircuitOxygen_Save = (Parameter) medicalDeviceData.createOrGetParameter(ANESTHESIA_MEASUREDDATA_CIRCUITO2_SAVE)[2];
		O2Percentage_Save = (Parameter) medicalDeviceData.createOrGetParameter(ANESTHESIA_MEASUREDDATA_O2PERCENT_SAVE)[2];
		FlowTotal_Save = (Parameter) medicalDeviceData.createOrGetParameter(ANESTHESIA_MEASUREDDATA_TOTALFLOW_SAVE)[2];
		RespiratoryRate_Save = (Parameter) medicalDeviceData.createOrGetParameter(ANESTHESIA_STATUSDATA_RR_SAVE)[2];
		MaximumPressure_Save = (Parameter) medicalDeviceData.createOrGetParameter(ANESTHESIA_STATUSDATA_PMAX_SAVE)[2];
		IERatio_Save = (Parameter) medicalDeviceData.createOrGetParameter(ANESTHESIA_STATUSDATA_IE_SAVE)[2];
		TVInspiratory_Save = (Parameter) medicalDeviceData.createOrGetParameter(ANESTHESIA_STATUSDATA_TVINSP_SAVE)[2];
		// FlowTotal_Save = (Parameter)
		// medicalDeviceData.createOrGetParameter(ANESTHESIA_STATUSDATA_TOTALFLOW_SAVE)[2];
		PeepSetValue_Save = (Parameter) medicalDeviceData.createOrGetParameter(ANESTHESIA_STATUSDATA_PEEP_SAVE)[2];*/

		return medicalDeviceData;
	}

	@Override
	public NodeBase getNodeByIdentifier(String parameterIdentifier)
	{

		// TODO Auto-generated method stub
		NodeBase param = null;
		switch (parameterIdentifier)
		{

		case ANESTHESIA_MEASUREDDATA_TVEXP:
			param = TVExpiratory;
			break;

		case ANESTHESIA_MEASUREDDATA_PEEPExt:
			param = PeepValue;
			break;

		case ANESTHESIA_MEASUREDDATA_O2FLOW:
			param = O2MeasuredFlow;
			break;

		case ANESTHESIA_MEASUREDDATA_MAC:
			param = MACValue;
			break;

		case ANESTHESIA_MEASUREDDATA_N2OFLOW:
			param = N2OMeasuredFlow;
			break;

		case ANESTHESIA_MEASUREDDATA_MVEXP:
			param = MVExpiratory;
			break;

		case ANESTHESIA_STATUSDATA_TVINSP:
			param = TVInspiratory;
			break;

		case ANESTHESIA_MEASUREDDATA_PMEAN:
			param = MeanPressure;
			break;

		case ANESTHESIA_MEASUREDDATA_RRTOTAL:
			param = RespiratoryRateTotal;
			break;

		case ANESTHESIA_MEASUREDDATA_MVINSP:
			param = MVInspiratory;
			break;

		case ANESTHESIA_MEASUREDDATA_PPEAK:
			param = PeakPresssure;
			break;

		case ANESTHESIA_MEASUREDDATA_CIRCUITO2:
			param = CircuitOxygen;
			break;

		case ANESTHESIA_MEASUREDDATA_O2PERCENT:
			param = O2Percentage;
			break;

		case ANESTHESIA_MEASUREDDATA_TOTALFLOW:
			param = FlowTotal;
			break;

		case ANESTHESIA_STATUSDATA_RR:
			param = RespiratoryRate;
			break;

		case ANESTHESIA_STATUSDATA_PMAX:
			param = MaximumPressure;
			break;

		case ANESTHESIA_STATUSDATA_IE:
			param = IERatio;
			break;

		case ANESTHESIA_STATUSDATA_PEEP:
			param = PeepSetValue;
			break;

		/*case ANESTHESIA_MEASUREDDATA_TVEXP_SAVE:
			param = TVExpiratory;
			break;

		case ANESTHESIA_MEASUREDDATA_PEEPExt_SAVE:
			param = PeepValue;
			break;

		case ANESTHESIA_MEASUREDDATA_O2FLOW_SAVE:
			param = O2MeasuredFlow;
			break;

		case ANESTHESIA_MEASUREDDATA_MAC_SAVE:
			param = MACValue;
			break;

		case ANESTHESIA_MEASUREDDATA_N2OFLOW_SAVE:
			param = N2OMeasuredFlow;
			break;

		case ANESTHESIA_MEASUREDDATA_MVEXP_SAVE:
			param = MVExpiratory;
			break;

		case ANESTHESIA_STATUSDATA_TVINSP_SAVE:
			param = TVInspiratory;
			break;

		case ANESTHESIA_MEASUREDDATA_PMEAN_SAVE:
			param = MeanPressure;
			break;

		case ANESTHESIA_MEASUREDDATA_RRTOTAL_SAVE:
			param = RespiratoryRateTotal;
			break;

		case ANESTHESIA_MEASUREDDATA_MVINSP_SAVE:
			param = MVInspiratory;
			break;

		case ANESTHESIA_MEASUREDDATA_PPEAK_SAVE:
			param = PeakPresssure;
			break;

		case ANESTHESIA_MEASUREDDATA_CIRCUITO2_SAVE:
			param = CircuitOxygen;
			break;

		case ANESTHESIA_MEASUREDDATA_O2PERCENT_SAVE:
			param = O2Percentage;
			break;

		case ANESTHESIA_MEASUREDDATA_TOTALFLOW_SAVE:
			param = FlowTotal;
			break;

		case ANESTHESIA_STATUSDATA_RR_SAVE:
			param = RespiratoryRate;
			break;

		case ANESTHESIA_STATUSDATA_PMAX_SAVE:
			param = MaximumPressure;
			break;

		case ANESTHESIA_STATUSDATA_IE_SAVE:
			param = IERatio;
			break;

		case ANESTHESIA_STATUSDATA_PEEP_SAVE:
			param = PeepSetValue;
			break;*/

		case ANESTHESIA_WAVEFORM_PRESSURE:
			param = PressureWaveform;
			break;

		case ANESTHESIA_WAVEFORM_CO2:
			param = CO2Waveform;
			break;

		default:
			param = null;
		}
		return param;
	}

	public void parsingLogicForVTd(byte[] completePacket)
	{
		byte[] TVexp = new byte[4];
		byte[] Peep = new byte[3];
		byte[] o2MFlow = new byte[4];
		byte[] MAC = new byte[4];
		byte[] n2MFlow = new byte[4];
		byte[] MVexp = new byte[4];
		byte[] PMean = new byte[3];
		byte[] RRtol = new byte[3];
		byte[] MVinsp = new byte[3];
		byte[] ppeak = new byte[3];
		byte[] circuitO2 = new byte[3];

		circuitO2 = Arrays.copyOfRange(completePacket, 12, 15);
		CircuitOxygen.setValue(new String(circuitO2));
		//CircuitOxygen_Save.setValue(new String(circuitO2));
		//CircuitOxygen_Save.setValue(calculateTrendValue(CircuitOxygen_Save, new String(circuitO2)));

		// System.out.println("!!!!!!!circuitO2!!!!!! " + new
		// String(circuitO2));

		ppeak = Arrays.copyOfRange(completePacket, 15, 18);
		String peak = new String(ppeak);
		if (!peak.contains("-") && !peak.contains("<") && !peak.contains(">"))
		{
			PeakPresssure.setValue(Math.round(new Double(peak)));
			//PeakPresssure_Save.setValue(Math.round(new Double(peak)));
			//PeakPresssure_Save.setValue(calculateTrendValue(PeakPresssure_Save, Math.round(new Double(peak)) + ""));
		}
		else
		{
			PeakPresssure.setValue("---");
			//PeakPresssure_Save.setValue(null);
			//PeakPresssure_Save.setValue(calculateTrendValue(PeakPresssure_Save, DEFAULT_TREND_VALUE));
		}

		// System.out.println("!!!!!!!peak pressure!!!!!! " + new
		// String(ppeak));

		TVexp = Arrays.copyOfRange(completePacket, 2, 5);
		String tvExpi = new String(TVexp);
		if (!tvExpi.contains("-") && !tvExpi.contains("<") && !tvExpi.contains(">"))
		{
			TVExpiratory.setValue(Math.round(new Double(tvExpi)));
			//TVExpiratory_Save.setValue(Math.round(new Double(tvExpi)));
			//TVExpiratory_Save.setValue(calculateTrendValue(TVExpiratory_Save, Math.round(new Double(tvExpi)) + ""));
		}
		else
		{
			TVExpiratory.setValue("---");
			//TVExpiratory_Save.setValue(null);
			//TVExpiratory_Save.setValue(calculateTrendValue(TVExpiratory_Save, DEFAULT_TREND_VALUE));
		}
		// System.out.println("\n");
		// System.out.println("!!!!!TV exp!!!!! " + new String(TVexp));

		MAC = Arrays.copyOfRange(completePacket, 119, 121);
		MACValue.setValue(new String(MAC));
		//MACValue_Save.setValue(new String(MAC));
		//MACValue_Save.setValue(calculateTrendValue(MACValue_Save, new String(MAC)));
		// System.out.println("!!!!!!!!!!!!MAC!!!!!!!!!!" + new String(MAC));

		PMean = Arrays.copyOfRange(completePacket, 21, 24);
		String mean = new String(PMean);
		if (!mean.contains("-") && !mean.contains("<") && !mean.contains(">"))
		{
			MeanPressure.setValue(Math.round(new Double(mean)));
			//MeanPressure_Save.setValue(Math.round(new Double(mean)));
			//MeanPressure_Save.setValue(calculateTrendValue(MeanPressure_Save, Math.round(new Double(mean)) + ""));
		}
		else
		{
			MeanPressure.setValue("---");
			//MeanPressure_Save.setValue(null);
			//MeanPressure_Save.setValue(calculateTrendValue(MeanPressure_Save, DEFAULT_TREND_VALUE));
		}
		//MeanPressure.setValue(Math.round(new Double(new String(PMean))));
		// System.out.println("!!!!!!!!PMEAN!!!!!!!!!!!" + new String(PMean));

		RRtol = Arrays.copyOfRange(completePacket, 9, 12);
		String total = new String(RRtol);
		if (!total.contains("-") && !total.contains("<") && !total.contains(">"))
		{
			RespiratoryRateTotal.setValue(Math.round(new Double(total)));
			//RespiratoryRateTotal_Save.setValue(Math.round(new Double(total)));
			//RespiratoryRateTotal_Save.setValue(calculateTrendValue(RespiratoryRateTotal_Save, Math.round(new Double(total)) + ""));
		}
		else
		{
			RespiratoryRateTotal.setValue("---");
			//RespiratoryRateTotal_Save.setValue(null);
			//RespiratoryRateTotal_Save.setValue(calculateTrendValue(RespiratoryRateTotal_Save, DEFAULT_TREND_VALUE));
		}
		//RespiratoryRateTotal.setValue(Math.round(new Double(new String(RRtol))));
		// System.out.println("!!!!!!!!!!!!RRtol!!!!!!!!!!!!!!" + new
		// String(RRtol));

		Peep = Arrays.copyOfRange(completePacket, 65, 68);
		String peepe = new String(Peep);
		if (!peepe.contains("-") && !peepe.contains("<") && !peepe.contains(">"))
		{

			// PeepValue.setValue(Double.toString(Double.parseDouble(peepe) /
			// 10));
			PeepValue.setValue(Math.round(new Double(peepe) / 10));
			///PeepValue_Save.setValue(Math.round(new Double(peepe) / 10));
			//PeepValue_Save.setValue(calculateTrendValue(PeepValue_Save, Math.round(new Double(peepe) / 10) + ""));
			// System.out.println("!!!!!!Peepe!!!!!! " +
			// Double.toString(Double.parseDouble(peepe) / 10));
		}
		else
		{
			PeepValue.setValue("---");
			//PeepValue_Save.setValue(null);
			//PeepValue_Save.setValue(calculateTrendValue(PeepValue_Save, DEFAULT_TREND_VALUE));
			// System.out.println("!!!!!Peepe!!!!!! " + "---");
		}

		o2MFlow = Arrays.copyOfRange(completePacket, 181, 185);
		String o2MeasFlow = new String(o2MFlow);
		if (!o2MeasFlow.contains("-") && !o2MeasFlow.contains("<") && !o2MeasFlow.contains(">"))
		{
			O2MeasuredFlow.setValue(Double.toString(Double.parseDouble(o2MeasFlow) / 100));
			//O2MeasuredFlow_Save.setValue(Double.toString(Double.parseDouble(o2MeasFlow) / 100));
			//O2MeasuredFlow_Save.setValue(calculateTrendValue(O2MeasuredFlow_Save, Double.toString(Double.parseDouble(o2MeasFlow) / 100) + ""));
			// System.out.println("!!!!!o2 measurd flow!!!!!! " +
			// Double.toString(Double.parseDouble(o2MeasFlow) / 100));
		}
		else
		{
			O2MeasuredFlow.setValue("---");
			//O2MeasuredFlow_Save.setValue(null);
			//O2MeasuredFlow_Save.setValue(calculateTrendValue(O2MeasuredFlow_Save, DEFAULT_TREND_VALUE));
			// System.out.println("!!!!!o2 measurd flow!!!!! " + "---");
		}

		n2MFlow = Arrays.copyOfRange(completePacket, 185, 189);
		String n2MeasFlow = new String(n2MFlow);
		if (!n2MeasFlow.contains("-") && !n2MeasFlow.contains("<") && !n2MeasFlow.contains(">"))
		{

			N2OMeasuredFlow.setValue(Double.toString(Double.parseDouble(n2MeasFlow) / 100));
			//N2OMeasuredFlow_Save.setValue(Double.toString(Double.parseDouble(n2MeasFlow) / 100));
			//N2OMeasuredFlow_Save.setValue(calculateTrendValue(N2OMeasuredFlow_Save, Double.toString(Double.parseDouble(n2MeasFlow) / 100) + ""));
			// System.out.println("!!!!!n2o measurd flow!!!!!!! " +
			// Double.toString(Double.parseDouble(n2MeasFlow) / 100));
		}
		else
		{
			N2OMeasuredFlow.setValue("---");
			//N2OMeasuredFlow_Save.setValue(null);
			//N2OMeasuredFlow_Save.setValue(calculateTrendValue(N2OMeasuredFlow_Save, DEFAULT_TREND_VALUE));
			// System.out.println("!!!!!!!!!!!!!!n2o measurd flow!!!!!!!!!!!!! "
			// + "---");
		}

		MVexp = Arrays.copyOfRange(completePacket, 6, 9);
		String MVexpi = new String(MVexp);
		if (!MVexpi.contains("-") && !MVexpi.contains("<") && !MVexpi.contains(">"))
		{

			MVExpiratory.setValue(Math.round(new Double(Double.toString(Double.parseDouble(MVexpi) / 100))*10.0)/10.0);
			//MVExpiratory_Save.setValue(Math.round(new Double(Double.toString(Double.parseDouble(MVexpi) / 100))*10.0)/10.0);
			//MVExpiratory_Save.setValue(calculateTrendValue(MVExpiratory_Save, Math.round(new Double(Double.toString(Double.parseDouble(MVexpi) / 100))*10.0)/10.0 + ""));
			// System.out.println("!!!!!!!MV exp!!!!!!! " +
			// Double.toString(Double.parseDouble(MVexpi) / 100));
		}
		else
		{
			MVExpiratory.setValue("---");
			//MVExpiratory_Save.setValue(null);
			//MVExpiratory_Save.setValue(calculateTrendValue(MVExpiratory_Save, DEFAULT_TREND_VALUE));
			// System.out.println("!!!!!!!MV exp!!!!!!! " + "---");
		}

		MVinsp = Arrays.copyOfRange(completePacket, 53, 56);
		String MVinspiration = new String(MVinsp);
		if (!MVinspiration.contains("-") && !MVinspiration.contains("<") && !MVinspiration.contains(">"))
		{
			MVInspiratory.setValue(Double.toString(Double.parseDouble(MVinspiration) / 100));
			//MVInspiratory_Save.setValue(Double.toString(Double.parseDouble(MVinspiration) / 100));
			//MVInspiratory_Save.setValue(calculateTrendValue(MVInspiratory_Save, Double.toString(Double.parseDouble(MVinspiration) / 100) + ""));
		}
		else
		{
			MVInspiratory.setValue("---");
			//MVInspiratory_Save.setValue(null);
			//MVInspiratory_Save.setValue(calculateTrendValue(MVInspiratory_Save, DEFAULT_TREND_VALUE));
		}

		if (!o2MeasFlow.contains("-") && !o2MeasFlow.contains("<") && !o2MeasFlow.contains(">")
		        && !n2MeasFlow.contains("-") && !n2MeasFlow.contains("<") && !n2MeasFlow.contains(">"))
		{
			double o2Percent = (Double.parseDouble(o2MeasFlow)
			        / (Double.parseDouble(n2MeasFlow) + Double.parseDouble(o2MeasFlow))) * 100;
			O2Percentage.setValue(Math.round(new Double(String.valueOf(o2Percent))));
			//O2Percentage_Save.setValue(Math.round(new Double(String.valueOf(o2Percent))));
			//O2Percentage_Save.setValue(calculateTrendValue(O2Percentage_Save, Math.round(new Double(String.valueOf(o2Percent))) + ""));
			// System.out.println("!!!!!!!!!!o2 percentage!!!!!!! " +
			// o2Percent);
		}
		else if (!o2MeasFlow.contains("-") && !o2MeasFlow.contains("<") && !o2MeasFlow.contains(">"))
		{
			double o2Percent = (Double.parseDouble(o2MeasFlow) / Double.parseDouble(o2MeasFlow)) * 100;
			O2Percentage.setValue(Math.round(new Double(String.valueOf(o2Percent))));
			//O2Percentage_Save.setValue(Math.round(new Double(String.valueOf(o2Percent))));
			//O2Percentage_Save.setValue(calculateTrendValue(O2Percentage_Save, Math.round(new Double(String.valueOf(o2Percent))) + ""));
			// System.out.println("!!!!!!!!!!o2 percentage!!!!!!! " +
			// o2Percent);
		}
		else
		{
			O2Percentage.setValue("---");
			//O2Percentage_Save.setValue(null);
			//O2Percentage_Save.setValue(calculateTrendValue(O2Percentage_Save, DEFAULT_TREND_VALUE));
		}

		if(!o2MeasFlow.contains("-") && !o2MeasFlow.contains("<") && !o2MeasFlow.contains(">") && !n2MeasFlow.contains("-") && !n2MeasFlow.contains("<") && !n2MeasFlow.contains(">"))
		{
			double totalFlow = Double.parseDouble(n2MeasFlow) + Double.parseDouble(o2MeasFlow);
			FlowTotal.setValue(new Double(String.valueOf(totalFlow)) / 100);
			//FlowTotal_Save.setValue(new Double(String.valueOf(totalFlow)) / 100);
			//FlowTotal_Save.setValue(calculateTrendValue(FlowTotal_Save, new Double(String.valueOf(totalFlow)) / 100 + ""));
		}
		else if (!o2MeasFlow.contains("-") && !o2MeasFlow.contains("<") && !o2MeasFlow.contains(">"))
		{
			double totalFlow = Double.parseDouble(o2MeasFlow);
			FlowTotal.setValue(new Double(String.valueOf(totalFlow)) / 100);
			//FlowTotal_Save.setValue(new Double(String.valueOf(totalFlow)) / 100);
			//FlowTotal_Save.setValue(calculateTrendValue(FlowTotal_Save, new Double(String.valueOf(totalFlow)) / 100 + ""));
		}
		else if (!n2MeasFlow.contains("-") && !n2MeasFlow.contains("<") && !n2MeasFlow.contains(">"))
		{
			double totalFlow = Double.parseDouble(n2MeasFlow);
			FlowTotal.setValue(new Double(String.valueOf(totalFlow)) / 100);
			//FlowTotal_Save.setValue(new Double(String.valueOf(totalFlow)) / 100);
			//FlowTotal_Save.setValue(calculateTrendValue(FlowTotal_Save, new Double(String.valueOf(totalFlow)) / 100 + ""));
		}
		else
		{
			FlowTotal.setValue("---");
			//FlowTotal_Save.setValue(null);
			//FlowTotal_Save.setValue(calculateTrendValue(FlowTotal_Save, DEFAULT_TREND_VALUE));
		}
	}

	public void parsingLogicForVTq(byte[] completePacket)
	{
		byte[] RR = new byte[4];
		byte[] PMax = new byte[4];
		byte[] ie = new byte[4];
		// byte[] flow = new byte[4];
		byte[] TVInsp = new byte[4];
		byte[] peep=new byte[4];

		TVInsp = Arrays.copyOfRange(completePacket, 2, 5);
		String inspiratoryTV = new String(TVInsp);
		if (!inspiratoryTV.contains("-") && !inspiratoryTV.contains("<") && !inspiratoryTV.contains(">"))
		{
			TVInspiratory.setValue(Math.round(new Double(inspiratoryTV)));
			//TVInspiratory_Save.setValue(Math.round(new Double(inspiratoryTV)));
			//TVInspiratory_Save.setValue(calculateTrendValue(TVInspiratory_Save, Math.round(new Double(inspiratoryTV)) + ""));
		}
		else
		{
			TVInspiratory.setValue("---");
			//TVInspiratory_Save.setValue(null);
			//TVInspiratory_Save.setValue(calculateTrendValue(TVInspiratory_Save, DEFAULT_TREND_VALUE));
		}
		//TVInspiratory.setValue(Math.round(new Double(new String(TVInsp))));
		// System.out.println("!!!!!!!!!!TV INSP!!!!!!!" + new String(TVInsp));

		// flow = Arrays.copyOfRange(completePacket, 168, 172);
		// String totalFlow = new String(flow);
		// if (!totalFlow.contains("-") && !totalFlow.contains("<") &&
		// !totalFlow.contains(">"))
		// {
		//
		// FlowTotal.setValue(Double.toString(Double.parseDouble(totalFlow) /
		// 100));
		// //System.out.println("!!!!!!!total flow!!!!!!!" +
		// Double.toString(Double.parseDouble(totalFlow) / 100));
		// }
		// else
		// {
		// FlowTotal.setValue("---");
		// //System.out.println("!!!!!!!total flow!!!!!!" + "---");
		// }

		PMax = Arrays.copyOfRange(completePacket, 17, 19);
		String maximumP=new String(PMax);
		if (!maximumP.contains("-") && !maximumP.contains("<") && !maximumP.contains(">"))
		{
			MaximumPressure.setValue(Math.round(new Double(maximumP)));
			//MaximumPressure_Save.setValue(Math.round(new Double(maximumP)));
			//MaximumPressure_Save.setValue(calculateTrendValue(MaximumPressure_Save, Math.round(new Double(maximumP)) + ""));
		}
		else
		{
			MaximumPressure.setValue("---");
			//MaximumPressure_Save.setValue(null);
			//MaximumPressure_Save.setValue(calculateTrendValue(MaximumPressure_Save, DEFAULT_TREND_VALUE));
		}
		//MaximumPressure.setValue(Math.round(new Double(new String(PMax))));
		// System.out.println("\n");
		// System.out.println("!!!!!!PeakPressureLimit!!!!!!!" + new
		// String(PMax));

		RR = Arrays.copyOfRange(completePacket, 5, 8);
		String setRR=new String(RR);
		if (!setRR.contains("-") && !setRR.contains("<") && !setRR.contains(">"))
		{
			RespiratoryRate.setValue(Math.round(new Double(setRR)));
			//RespiratoryRate_Save.setValue(Math.round(new Double(setRR)));
			//RespiratoryRate_Save.setValue(calculateTrendValue(RespiratoryRate_Save, Math.round(new Double(setRR)) + ""));
		}
		else
		{
			RespiratoryRate.setValue("---");
			//RespiratoryRate_Save.setValue(null);
			//RespiratoryRate_Save.setValue(calculateTrendValue(RespiratoryRate_Save, DEFAULT_TREND_VALUE));
		}

		// System.out.println("!!!!!!RR!!!!!!" + new String(RR));

		ie = Arrays.copyOfRange(completePacket, 10, 12);
		String ieString = new String(ie);
		if (!ieString.contains("-") && !ieString.contains("<") && !ieString.contains(">"))
		{

			IERatio.setValue("1:" + String.valueOf(Double.parseDouble(ieString) / 10));
			//IERatio_Save.setValue("1:" + String.valueOf(Double.parseDouble(ieString) / 10));
			//IERatio_Save.setValue(calculateTrendValue(IERatio_Save, String.valueOf(Double.parseDouble(ieString) / 10) + ""));
			// System.out.println("!!!!!!!IE ratio!!!!!!!" + "1:" +
			// String.valueOf(Double.parseDouble(ieString) / 10));
		}
		else
		{
			IERatio.setValue("---");
			//IERatio_Save.setValue(null);
			//IERatio_Save.setValue(calculateTrendValue(IERatio_Save, DEFAULT_TREND_VALUE));
			// System.out.println("!!!!!!!ie ratio!!!!!!" + "---");
		}

		peep = Arrays.copyOfRange(completePacket, 14, 16);
		String peepSet=new String(peep);
		if(peepSet.equals("88"))
		{
			PeepSetValue.setValue("Off");
			//PeepSetValue_Save.setValue(null);
			//PeepSetValue_Save.setValue(calculateTrendValue(PeepSetValue_Save, DEFAULT_TREND_VALUE));

		}
		else
		{
			PeepSetValue.setValue(peepSet);
			//PeepSetValue_Save.setValue("peepSet");
			//PeepSetValue_Save.setValue(calculateTrendValue(PeepSetValue_Save, peepSet));
		}

	}

	public void parsingLogicForVTw(byte[] completeMessage)
	{
		String[] arrayForPressureWaveForm = new String[10];
		String[] arrayForCO2Waveform = new String[10];
		byte[] waveformSample = new byte[3];
		int cursor = 1;
		int i = 0;

		for (int cursorForSample = 0; cursorForSample < 10; cursorForSample++)
		{

			waveformSample = Arrays.copyOfRange(completeMessage, cursor, cursor + 3);
			String wfSample = String.valueOf(Integer.parseInt(new String(waveformSample), 16));
			// System.out.println("!!!!!!sample!!!!!!" + wfSample);
			arrayForPressureWaveForm[i++] = wfSample;
			cursor = cursor + 3;
		}

		PressureWaveform.setValue(arrayForPressureWaveForm);

		int j = 0;
		for (int cursorForSample = 0; cursorForSample < 10; cursorForSample++)
		{

			waveformSample = Arrays.copyOfRange(completeMessage, cursor, cursor + 3);
			String wfSample = String.valueOf(Integer.parseInt(new String(waveformSample), 16));
			// System.out.println("!!!!!!sample!!!!!!" + wfSample);
			arrayForCO2Waveform[j++] = wfSample;
			cursor = cursor + 3;
		}
		CO2Waveform.setValue(arrayForCO2Waveform);
	}

	/**
	 * This method keeps the trend value updated
	 * @param param previous value of parameter
	 * @param value current value of parameter
	 * @param minimum minimum possible value of the parameter
	 * @param maximum maximum possible value of the parameter
	 * @return final value of the parameter to be set
	 */
	/*private String calculateTrendValue(Parameter param, String value)
	{
		String finalValue = DEFAULT_TREND_VALUE;
		if(Integer.parseInt(value)<-32000)
		{
			return DEFAULT_TREND_VALUE;
		}
		if(param.getParameterData()==null)
		{
			param.setValue(finalValue);
		}

		String minValue = param.getParameterData().toString().split("\\|")[0];
		String maxValue = param.getParameterData().toString().split("\\|")[1];

		// If value is less than minimum value, then make it the minimum
		if(Integer.parseInt(value)<=Double.parseDouble(minValue))
		{
			minValue = value + "";
		}

		// If value is greater than maximum value, then make it the maximum
		if(Integer.parseInt(value)>Double.parseDouble(maxValue))
		{
			maxValue = value + "";
		}

		finalValue = minValue + "|" + maxValue;

		return finalValue;
	}*/

	@Override
	public MedicalDeviceData getDeviceData()
	{
		return medicalDeviceData;
	}

	@Override
	public void terminate()
	{
		// TODO Auto-generated method stub

	}

}
