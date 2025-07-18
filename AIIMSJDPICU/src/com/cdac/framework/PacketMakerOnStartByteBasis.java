/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package com.cdac.framework;

import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.lang.ArrayUtils;

public class PacketMakerOnStartByteBasis implements IPacketMakerStrategy
{
	byte[] arrayOfStartBytes;
	ArrayList<Byte> partialMessage;
	Queue<byte[]> completeMessages;
	ArrayList<Byte> pmNoStartByte;

	public boolean flag;
	public int flagForStartByte = 0;
	public int flagForEndByte = 0;

	public PacketMakerOnStartByteBasis(byte[] startBytes)
	{
		// TODO XXX Resolve this
		arrayOfStartBytes = startBytes;
		partialMessage = new ArrayList<Byte>();
		completeMessages = new ConcurrentLinkedQueue<byte[]>();
		pmNoStartByte = new ArrayList<Byte>();
		flag = false;
	}

	public Queue<byte[]> makePacket(byte[] data)
	{
	// ******Add the partialMessage if any or the data with no start byte and data that we are receiving from the device to the nextMessage data array******

		Byte[] messageData = new Byte[data.length];
		int addPMCursor = 0;
		int addPMNoStartByteCursor = 0;
		int addDataCursor = 0;

		if (partialMessage.size() != 0)
		{
			messageData = new Byte[data.length + partialMessage.size()];
			addDataCursor = partialMessage.size();
			for (byte element : partialMessage)
			{
				messageData[addPMCursor] = element;
				addPMCursor++;
			}
		}
		if (pmNoStartByte.size() != 0)
		{
			messageData = new Byte[data.length + pmNoStartByte.size()];
			addDataCursor = pmNoStartByte.size();
			for (byte element : pmNoStartByte)
			{
				messageData[addPMNoStartByteCursor] = element;
				addPMNoStartByteCursor++;
			}
		}
		for (byte element : data)
		{
			messageData[addDataCursor] = element;
			addDataCursor++;
		}
		// ***** completed******

		//*****if partialMessage size is 0 or if it is less than the size of identifier start byte then start the cursor from the 0th element
		// (cond: messageData length should be greater than or equal to
		// arrayOfStartBytes length)******

		if (partialMessage.size() == 0 && messageData.length >= arrayOfStartBytes.length
	        || partialMessage.size() < arrayOfStartBytes.length && messageData.length >= arrayOfStartBytes.length)
		{
			int pmadditionSize = 0;
			pmNoStartByte = new ArrayList<Byte>();
			for (int chkEachElementCursor = 0; chkEachElementCursor < messageData.length; chkEachElementCursor++)
			{
				// to check if on any cursor element we get startbyte or not
				if (checkForStartbyte(chkEachElementCursor, messageData))
				{
					chkEachElementCursor = addToCompleteMessage(chkEachElementCursor, messageData);
				}
				//once we get the start byte start adding the data to the partialMessage
				else if (flag)
				{
					pmadditionSize++;
					if (pmadditionSize > partialMessage.size())
					{
						partialMessage.add(messageData[chkEachElementCursor]);
					//	System.out.println("Partial Message:" + partialMessage);
					}
				}
				else
				{
					// ignore the bytes until we get the **Start Bytes**
					// if there is no start byte in the series of data add it to the array List pmNoStartByte(temporary storage for bytes which are not checked)
					if (chkEachElementCursor == messageData.length - 1)
					{
						int additionOfElement = 0;
						for (int i = messageData.length - (arrayOfStartBytes.length - 1); i < messageData.length; i++)
						{
							pmNoStartByte.add(additionOfElement++, messageData[i]);
//							System.out.println("pmNoStartByte:" + partialMessage);
						}
					}
					else
					{
//						System.out.println("ingore the bytes until first start byte found");
					}
				}
			}
		}

		else if (partialMessage.size() >= arrayOfStartBytes.length && messageData.length > arrayOfStartBytes.length)
		{
			int pmadditionSize = 0;
			for (int chkEachElementCursor = partialMessage.size() - (arrayOfStartBytes.length
		        - 1); chkEachElementCursor < messageData.length; chkEachElementCursor++)
			{
				// to check if there are start bytes or not
				if (checkForStartbyte(chkEachElementCursor, messageData))
				{
					chkEachElementCursor = addToCompleteMessage(chkEachElementCursor, messageData);
				}
				else if (flag)
				{
					pmadditionSize++;
					if (partialMessage.size() < arrayOfStartBytes.length)
					{
						if (pmadditionSize > partialMessage.size())
						{
							partialMessage.add(messageData[chkEachElementCursor]);
//							System.out.println("Partial Message:" + partialMessage);
						}
					}
					else
					{
						if (pmadditionSize > arrayOfStartBytes.length - 1)
						{
							partialMessage.add(messageData[chkEachElementCursor]);
//						System.out.println("Partial Message:" + partialMessage);
						}

					}
				}
				else
				{
					// ignore
				}
			}
		}

		// ****** if messageData length is less than the arrayOfStartBytes
		// length
		// then no need of checking directly add the data to pmNoStartByte
		// array******
		else
		{
			for (byte element : data)
			{
				pmNoStartByte.add(element);
//				System.out.println("PM no start byte message" + pmNoStartByte);
			}
		}

		return completeMessages;
	}

	public boolean checkForStartbyte(int chkEachElementCursor, Byte[] messageData)
	{
		ArrayList<Byte> compareBytes = new ArrayList<Byte>();
		ArrayList<Byte> convertedList = new ArrayList<Byte>();

		for (int j = chkEachElementCursor; j <= chkEachElementCursor + arrayOfStartBytes.length - 1; j++)
		{
			if (chkEachElementCursor < messageData.length - (arrayOfStartBytes.length - 1))
			compareBytes.add(messageData[j]);
		}


		for (int i = 0; i < arrayOfStartBytes.length; i++)
		convertedList.add(arrayOfStartBytes[i]);

//		System.out.println("compareBytes:" + compareBytes);

		if (compareBytes.toString().equals(convertedList.toString()))
		{
			flag = true;
			return true;
		}
		else
		{
		 return false;
		}
	}

	private byte[] converTobyteArray(ArrayList<Byte> message)
	{
		Byte [] completeMessageTemp = new Byte[message.size()];
		byte [] completeMessageArray = new byte[message.size()];
		completeMessageArray = ArrayUtils.toPrimitive(message.toArray(completeMessageTemp));
		return completeMessageArray;
	}

	public int addToCompleteMessage(int chkEachElementCursor, Byte[] messageData)
	{
		if (partialMessage.size() != 0 && chkEachElementCursor > partialMessage.size())
		{
			completeMessages.add(converTobyteArray(partialMessage));
			partialMessage = new ArrayList<Byte>();
		}
		else
		{
			for (int remLastElements = partialMessage.size()
		        - 1; remLastElements >= chkEachElementCursor; remLastElements--)
			{
				partialMessage.remove(remLastElements);
			}
			if (partialMessage.size() != 0)
			{
				completeMessages.add(converTobyteArray(partialMessage));
				// System.out.print("\n Complete Message:");
				partialMessage = new ArrayList<Byte>();
			}
		}
	// if start bytes found skip that start bytes and add the
	// partialMessage to completeMessage
		// chkEachElementCursor = chkEachElementCursor +
		// arrayOfStartBytes.length - 1;
	return chkEachElementCursor;
}
}

