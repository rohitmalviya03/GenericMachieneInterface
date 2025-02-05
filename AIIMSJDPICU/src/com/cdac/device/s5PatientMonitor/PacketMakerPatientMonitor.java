/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package com.cdac.device.s5PatientMonitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;

import com.cdac.framework.IPacketMakerStrategy;

public class PacketMakerPatientMonitor implements IPacketMakerStrategy
{
	private static final Logger LOG = Logger.getLogger(PacketMakerPatientMonitor.class);
	byte[] arrayOfStartBytes;
	byte[] arrayOfEndBytes;
	ArrayList<Byte> partialMessage;
	Queue<byte[]> completeMessages;
	//ArrayList<Byte> pmNoStartByte;

	//public boolean flag;
	//public int StartByteFound = 0;
	//public int EndByteFound = 0;
	//public boolean isStartbytes=false;
	//private List<Byte> startBytes = new ArrayList<Byte>();
	//private boolean isStartBytesFound = false;
	//private boolean isEndBytesFound = false;

	//
	private List<Byte> packetUnderConstruction;
	private boolean firstByteMatched = false;
	private boolean startBytesMatched = false;


	public PacketMakerPatientMonitor(byte[] startBytes, byte[] endBytes)
	{
		// TODO XXX Resolve this
		arrayOfStartBytes = startBytes;
		arrayOfEndBytes = endBytes;
		partialMessage = new ArrayList<Byte>();
		completeMessages = new ConcurrentLinkedQueue<byte[]>();
		//pmNoStartByte = new ArrayList<Byte>();
		packetUnderConstruction = new ArrayList<Byte>();
		//flag = false;
	}
	private boolean m_fstart = true;
	private boolean m_storestart = false;
    private boolean m_storeend = false;
	private boolean m_bitshiftnext = false;

	public Queue<byte[]> CreateFrameListFromByte(byte b)
    {
		if (b == 0x7E && m_fstart)
		{
			m_fstart = false;
			m_storestart = true;
		}
		else if (b == 0x7E && (m_fstart == false))
		{
			m_fstart = true;
			m_storeend = true;
			m_storestart = false;
			if (b != 0x7E) partialMessage.add(b);
		}

		if (m_storestart == true)
		{
			if (b == 0x7D)
				m_bitshiftnext = true;
			else
			{
				if (m_bitshiftnext == true)
				{
					b |= 0x7C;
					m_bitshiftnext = false;
					partialMessage.add(b);
				}
				else if (b != 0x7E) partialMessage.add(b);
			}

		}
		else if (m_storeend == true)
		{
			int framelen = partialMessage.size();
			if (framelen != 0)
			{
				byte[] bArray = new byte[framelen];
				bArray = converTobyteArray(partialMessage);
				//Calculate checksum
				byte checksum = 0x00;
				for (int j = 0; j < (framelen - 1); j++)
				{
					checksum += bArray[j];
				}
				if (checksum == bArray[framelen - 1])
				{
					System.out.println("!!!!!!!!!!Checksum!!!!!!!!"+ checksum);
					completeMessages.add(bArray);
				}
				partialMessage.clear();
				m_storeend = false;
			}
			else
			{
				m_storestart = true;
				m_storeend = false;
				m_fstart = false;
			}

		}
		return completeMessages;
    }

	public Queue<byte[]> makePacket(byte[] data)
	{
		/*byte[] messageData = new byte[data.length];
		int addPMCursor = 0;
		int addPMNoStartByteCursor = 0;
		int addDataCursor = 0;
		int cursor = 0;*/

		/**
		 * Keep on adding the data we get inside an arrayList
		 */
		// System.out.println("Data Added is ");
		for (byte tempByte : data)
		{
			completeMessages = CreateFrameListFromByte(tempByte);
			// System.out.println(tempByte + " ");
//			packetUnderConstruction.add(new Byte(tempByte));
		}

		/*if (!startBytesMatched)
		{
			if (packetUnderConstruction.size() < arrayOfStartBytes.length)
			{
				// LOG.info("Returning empty queue as size of
				// packetUnderConstruction is insufficient.");
				// System.out.println("Returning empty queue as size of
				// packetUnderConstruction is insufficient.");
				return completeMessages;// We haven't yet got enough bytes to check the start bytes
			}
			else if(!firstByteMatched)
			{
				// Run the loop till the length
				int length = packetUnderConstruction.size();
				for (int i = 0; i < length; i++)
				{
					if (!(packetUnderConstruction.remove(0) == arrayOfStartBytes[0]))
					{
						continue;
					}
					else
					{
						firstByteMatched = true;
						startBytesMatched = true;
						break;
						if (packetUnderConstruction.get(0) == arrayOfStartBytes[1])
						{
							i++;
							packetUnderConstruction.remove(0);
							if (packetUnderConstruction.get(0) == arrayOfStartBytes[2])
							{
								i++;
								packetUnderConstruction.remove(0);
								startBytesMatched = true;
								break;
							}
							else
							{
								firstByteMatched = false;
								continue;
							}
						}
						else
						{
							firstByteMatched = false;
							continue;
						}
					}
				}
			}
		}
		else // Time to make packets
		{
			int length = packetUnderConstruction.size();
			for (int j = 0; j < length; j++)
			{
				byte toBeAdded = packetUnderConstruction.remove(0);
				if (toBeAdded == arrayOfEndBytes[0])
				{
					// Packet is complete and we are ready to make new
					// packet

					// System.out.println("##########Packet Created");
					completeMessages.add(converTobyteArray(partialMessage));
					partialMessage.clear();
					firstByteMatched = false;
					startBytesMatched = false;
					break;
				}
				else
				{
					partialMessage.add(toBeAdded);
				}
			}

		}*/

//		if (!isStartBytesFound)
//		{
//			for (int i = 0; i < data.length; i++)
//			{
//				if (startBytes.size() == arrayOfStartBytes.length)
//				{
//					break;
//				}
//
//				if (data[i] == arrayOfStartBytes[0])
//				{
//					startBytes.add(data[i]);
//					cursor++;
//				}
//				else if (arrayOfStartBytes.length > 1 && startBytes.contains(arrayOfStartBytes[0]))
//				{
//					startBytes.add(data[i]);
//					cursor++;
//				}
//			}
//			if (startBytes.size() == arrayOfStartBytes.length)
//			{
//				isStartBytesFound = true;
//				for (byte b : arrayOfStartBytes)
//				{
//					if (!startBytes.contains(b))
//					{
//						startBytes.clear();
//						isStartBytesFound = false;
//						break;
//					}
//				}
//			}
//		}
//
//		if (isStartBytesFound)
//		{
//			// Logic for capturing bytes
//			for (int j = cursor; j < data.length; j++)
//			{
//				if (!isEndBytesFound)
//				{
//					cursor++;
//					partialMessage.add(data[j]);
//				}
//				if (partialMessage.size() >= arrayOfEndBytes.length)
//				{
//					for (int k = 0; k < arrayOfEndBytes.length; k++)
//					{
//						if (!(arrayOfEndBytes[arrayOfEndBytes.length - 1 - k] == partialMessage
//						        .get(partialMessage.size() - 1 - k)))
//						{
//							isEndBytesFound = false;
//							break;
//						}
//						else
//						{
//							// System.out.println("End byte was found");
//							isEndBytesFound = true;
//						}
//					}
//				}
//				if (isEndBytesFound)
//				{
//					completeMessages.add(converTobyteArray(partialMessage));
//					partialMessage.clear();
//					startBytes.clear();
//					isStartBytesFound = false;
//					isEndBytesFound = false;
//					break;
//				}
//
//				 * if (!isEndBytesFound) { cursor++;
//				 * partialMessage.add(data[j]); System.out.println("Added " +
//				 * data[j] + " in PartialMessage"); } else {
//				 * completeMessages.add(converTobyteArray(partialMessage));
//				 * startBytes.clear(); isStartBytesFound = false; break; }
//
//			}
//
//		}

		// This is the condition when after the end byte is encountered, we still have
		// bytes left which could be the start of the next packet
		/*
		 * if (cursor < data.length) { for (int i = cursor; i < data.length;
		 * i++) { if (startBytes.size() == arrayOfStartBytes.length) { break; }
		 *
		 * if (data[i] == arrayOfStartBytes[0]) { startBytes.add(data[i]);
		 * cursor++; } else if (arrayOfStartBytes.length > 1 &&
		 * startBytes.contains(arrayOfStartBytes[0])) { startBytes.add(data[i]);
		 * cursor++; } } if (startBytes.size() == arrayOfStartBytes.length) {
		 * isStartBytesFound = true; for (byte b : arrayOfStartBytes) { if
		 * (!startBytes.contains(b)) { startBytes.clear(); isStartBytesFound =
		 * false; break; } } } }
		 */

		return completeMessages;
	}

	/*
	 * public Queue<byte[]> makePacket1(byte[] data) { // ******Add the
	 * partialMessage if any or the data with no start byte // and data that we
	 * are recieving from the device to the nextMessage // data array******
	 * byte[] messageData = new byte[data.length]; int addPMCursor = 0; int
	 * addPMNoStartByteCursor = 0; int addDataCursor = 0; int cursor = 0;
	 *
	 *
	 * if (!isStartBytesFound) { for (int i = 0; i < data.length; i++) { if
	 * (startBytes.size() == arrayOfStartBytes.length) { break; }
	 *
	 * if (data[i] == arrayOfStartBytes[0]) { startBytes.add(data[i]); cursor++;
	 * } else if (arrayOfStartBytes.length > 1 &&
	 * startBytes.contains(arrayOfStartBytes[0])) { startBytes.add(data[i]);
	 * cursor++; }
	 *
	 * else if (arrayOfStartBytes.length > 2 &&
	 * startBytes.contains(arrayOfStartBytes[1])) { startBytes.add(data[i]);
	 * cursor++; }
	 *
	 * } if (startBytes.size() == arrayOfStartBytes.length) { isStartBytesFound
	 * = true; for (byte b : arrayOfStartBytes) { if (!startBytes.contains(b)) {
	 * startBytes.clear(); isStartBytesFound = false; break; } }
	 *
	 * } }
	 *
	 * if (isStartBytesFound) { // Logic for capturing bytes for (int j =
	 * cursor; j < data.length; j++) { partialMessage.add(data[j]); } }
	 *
	 *
	 * if (partialMessage.size() != 0) { messageData = new byte[data.length +
	 * partialMessage.size()]; addDataCursor = partialMessage.size(); for (byte
	 * element : partialMessage) { messageData[addPMCursor] = element;
	 * addPMCursor++; } } else if (pmNoStartByte.size() != 0) { messageData =
	 * new byte[data.length + pmNoStartByte.size()]; addDataCursor =
	 * pmNoStartByte.size(); for (byte element : pmNoStartByte) {
	 * messageData[addPMNoStartByteCursor] = element; addPMNoStartByteCursor++;
	 * } } for (byte element : data) { messageData[addDataCursor] = element;
	 * addDataCursor++; }
	 *
	 * // ***** completed******
	 *
	 * // if (arrayOfEndBytes.length == 0) // { // arrayOfEndBytes =
	 * arrayOfStartBytes; // }
	 *
	 * // *****if partialMessage size is 0 or if it is less than the size of //
	 * identifier start byte then start the cursor from the 0th // element(cond:
	 * messageData length should be greater than or equal to //
	 * arrayOfStartBytes length)****** // part1 if case
	 *
	 * if ((partialMessage.size() == 0 && messageData.length >=
	 * arrayOfStartBytes.length) || (partialMessage.size() <
	 * arrayOfStartBytes.length && messageData.length >=
	 * arrayOfStartBytes.length)) { int pmadditionSize = 0; pmNoStartByte = new
	 * ArrayList<Byte>(); for (int chkEachElementCursor = 0;
	 * chkEachElementCursor < messageData.length; chkEachElementCursor++) { if
	 * (StartByteFound == EndByteFound) { // to CheckForStartByte if on any
	 * cursor element we get // start byte or not if
	 * (CheckForStartByte(chkEachElementCursor, messageData)) {
	 * chkEachElementCursor = AddToCompleteMessage(chkEachElementCursor,
	 * messageData); } else if (flag) { pmadditionSize++; if (pmadditionSize >
	 * partialMessage.size()) {
	 * partialMessage.add(messageData[chkEachElementCursor]);
	 * System.out.println("Partial Message:" + partialMessage); } } else { //
	 * ignore the bytes until we get the **Start Bytes** if // there is no start
	 * byte in the series of data add it // to the array List
	 * pmNoStartByte(temporary storage for // bytes which are not
	 * CheckForStartByteed) AddToPMNoStartByte(chkEachElementCursor,
	 * messageData); } } else { // to CheckForEndByte if on any cursor element
	 * we get // startbyte or not if (CheckForEndByte(chkEachElementCursor,
	 * messageData)) { chkEachElementCursor =
	 * AddToCompleteMessage(chkEachElementCursor, messageData); } else if (flag)
	 * { pmadditionSize++; if (pmadditionSize > partialMessage.size()) {
	 * partialMessage.add(messageData[chkEachElementCursor]);
	 * System.out.println("Partial Message:" + partialMessage); } } else { //
	 * ignore the bytes until we get the **Start Bytes** if // there is no start
	 * byte in the series of data add it // to the array List
	 * pmNoStartByte(temporary storage for // bytes which are not
	 * CheckForStartByteed) AddToPMNoStartByte(chkEachElementCursor,
	 * messageData); } } } } // part2 else if case else if
	 * (partialMessage.size() >= arrayOfStartBytes.length && messageData.length
	 * > arrayOfStartBytes.length) { int pmadditionSize = 0; for (int
	 * chkEachElementCursor = partialMessage.size() - (arrayOfStartBytes.length
	 * - 1); chkEachElementCursor < messageData.length; chkEachElementCursor++)
	 * { // to check if there are start bytes or not if (StartByteFound ==
	 * EndByteFound) { // to CheckForStartByte if on any cursor element we get
	 * // startbyte or not if (CheckForStartByte(chkEachElementCursor,
	 * messageData)) { chkEachElementCursor =
	 * AddToCompleteMessage(chkEachElementCursor, messageData); } else if (flag)
	 * { pmadditionSize = AddToPartialMessage(chkEachElementCursor, messageData,
	 * pmadditionSize); } else { // ignore } } else { if
	 * (CheckForEndByte(chkEachElementCursor, messageData)) {
	 * chkEachElementCursor = AddToCompleteMessage(chkEachElementCursor,
	 * messageData); } else if (flag) { pmadditionSize =
	 * AddToPartialMessage(chkEachElementCursor, messageData, pmadditionSize); }
	 * else { // ignore } } } } // part3 else case else { for (byte element :
	 * data) { pmNoStartByte.add(element); //
	 * System.out.println("PM no start byte" + pmNoStartByte); } }
	 *
	 *
	 * // TODO: XXX Resolve this return completeMessages;
	 *
	 * }
	 */

	/*
	 * public boolean CheckForStartByte(int chkEachElementCursor, byte[]
	 * messageData) { ArrayList<Byte> compareBytes = new ArrayList<Byte>();
	 * ArrayList<Byte> convertedList = new ArrayList<Byte>(); for (int j =
	 * chkEachElementCursor; j <= chkEachElementCursor +
	 * arrayOfStartBytes.length - 1; j++) { if (chkEachElementCursor <
	 * messageData.length - (arrayOfStartBytes.length - 1)) {
	 * compareBytes.add(messageData[j]); } } // System.out.println(" "); //
	 * System.out.println("compareBytes:" + compareBytes);
	 *
	 * for (int i = 0; i < arrayOfStartBytes.length; i++) {
	 * convertedList.add(arrayOfStartBytes[i]); } if
	 * (compareBytes.toString().equals(convertedList.toString())) { flag = true;
	 * StartByteFound++; return true; } else { return false; } }
	 *
	 * public boolean CheckForEndByte(int chkEachElementCursor, byte[]
	 * messageData) { ArrayList<Byte> compareBytes = new ArrayList<Byte>();
	 * ArrayList<Byte> convertedList = new ArrayList<Byte>(); for (int j =
	 * chkEachElementCursor; j <= chkEachElementCursor + arrayOfEndBytes.length
	 * - 1; j++) { if (chkEachElementCursor < messageData.length -
	 * (arrayOfEndBytes.length - 1)) { compareBytes.add(messageData[j]); } } //
	 * System.out.println("compareBytes:" + compareBytes); for (int i = 0; i <
	 * arrayOfEndBytes.length; i++) { convertedList.add(arrayOfEndBytes[i]); }
	 * if (compareBytes.toString().equals(convertedList.toString())) { flag =
	 * true; EndByteFound++; return true; } else { return false; } }
	 *
	 * public void AddToPMNoStartByte(int chkEachElementCursor, byte[]
	 * messageData) { if (chkEachElementCursor == messageData.length - 1) { int
	 * additionOfElement = 0; for (int i = messageData.length -
	 * (arrayOfEndBytes.length - 1); i < messageData.length; i++) {
	 * pmNoStartByte.add(additionOfElement++, messageData[i]); //
	 * System.out.println("pmNoStartByte:" + partialMessage); } } else { //
	 * System.out.println("ingore the bytes until first start byte // found"); }
	 *
	 * }
	 *
	 * public int AddToPartialMessage(int chkEachElementCursor, byte[]
	 * messageData, int pmadditionSize) { pmadditionSize++; if
	 * (partialMessage.size() < arrayOfStartBytes.length) { if (pmadditionSize >
	 * partialMessage.size()) {
	 * partialMessage.add(messageData[chkEachElementCursor]); //
	 * System.out.println("Partial Message:" + partialMessage); } } else { if
	 * (pmadditionSize > arrayOfStartBytes.length - 1) {
	 * partialMessage.add(messageData[chkEachElementCursor]); //
	 * System.out.println("Partial Message:" + partialMessage); } } return
	 * pmadditionSize; }
	 */

	private byte[] converTobyteArray(ArrayList<Byte> message)
	{
		Byte[] completeMessageTemp = new Byte[message.size()];
		byte[] completeMessageArray = new byte[message.size()];
		completeMessageArray = ArrayUtils.toPrimitive(message.toArray(completeMessageTemp));
		return completeMessageArray;
	}

	/*
	 * public int AddToCompleteMessage(int chkEachElementCursor, byte[]
	 * messageData) { if (partialMessage.size() != 0 && chkEachElementCursor >
	 * partialMessage.size() && StartByteFound == EndByteFound) {
	 * completeMessages.add(converTobyteArray(partialMessage)); //
	 * System.out.println("Complete Message:" + completeMessages); } // if we
	 * get the startbytes within the partialMessagedata, we remove the // data
	 * from that position till the size of partialMessage else { for (int
	 * remLastElements = partialMessage.size() - 1; remLastElements >=
	 * chkEachElementCursor; remLastElements--) {
	 * partialMessage.remove(remLastElements); } if (partialMessage.size() != 0
	 * && StartByteFound == EndByteFound) {
	 * completeMessages.add(converTobyteArray(partialMessage)); //
	 * System.out.println("Complete Message:" + completeMessages); } else { // }
	 * } partialMessage = new ArrayList<Byte>();
	 *
	 * // if start bytes found skip the start bytes and add the partialMessage
	 * // to completeMessage and skip the cursor for those bytes if
	 * (StartByteFound == EndByteFound) chkEachElementCursor =
	 * chkEachElementCursor + arrayOfEndBytes.length - 1; else
	 * chkEachElementCursor = chkEachElementCursor + arrayOfStartBytes.length -
	 * 1;
	 *
	 * return chkEachElementCursor; }
	 */
}
