/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package com.cdac.framework;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

public abstract class Work {

	private static final Logger LOG = Logger.getLogger(Work.class);
	private Callable workLogic;
	private String name;

	/**
	 * Constructor
	 * @param workName is a logical name of the task
	 */
	public Work(String workName) {
		name = workName;
	}

	/**
	 * Wraps the workLogic in a callable so that it can be executed.
	 */
	private void prepareExecutableWork() {
		workLogic = new Callable() {
			public Object call() throws Exception {
				Object object = null;
				try
				{
					object = workLogic();
				}
				catch (Throwable th)
				{
					// TODO: By default the executor service would have consumed
		            // this exception. Implement a way to re-throw it to the
		            // framework where it could process it properly.
					th.printStackTrace();
				}
				return object;
			}
		};
	}

	/**
	 * The main logic which needs to be executed in a seperate thread goes here.
	 *
	 * @return A provision to return any value.
	 */
	public abstract Object workLogic();

	/**
	 * Call this method to begin thread execution.
	 * @return {@link Future} object to determine the progress of execution.
	 */
	public final Future execute()
	{
		// LOG.info("Preparing workLogic");
		prepareExecutableWork();
		// LOG.info("Submitting workLogic for execution");
		return ThreadExecutorService.getExecutorService().submit(workLogic);
	}

	/**
	 * Schedule the work to be executed after some time interval.
	 *
	 * @param timeInMillis
	 * @return
	 */
	public final Future executeAfter(long timeInMillis)
	{
		prepareExecutableWork();
		return ThreadExecutorService.getExecutorService().schedule(workLogic, timeInMillis, TimeUnit.MILLISECONDS);
	}

	/**
	 * Schedule a logic that should execute repeatedly.
	 *
	 * @param timeInterval
	 *            is the interval between the completion of one execution and
	 *            starting of another.
	 * @return ScheduledFuture which may be used to cancel this otherwise it
	 *         will run forever.
	 */
	public final ScheduledFuture executeRepeatedly(long timeInterval)
	{
		return executeRepeatedly(0, timeInterval);
	}

	/**
	 * Schedule a logic that should execute repeatedly.
	 *
	 * @param startTime
	 *            is the time interval after which this will begin executing
	 *            repeatedly.
	 * @param timeInterval
	 *            is the interval between the completion of one execution and
	 *            starting of another.
	 * @return ScheduledFuture which may be used to cancel this otherwise it
	 *         will run forever.
	 */
	public final ScheduledFuture executeRepeatedly(long startTime, long timeInterval)
	{
		Runnable logicToExecuteRepeatedly = new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					workLogic();
				}
				catch (Throwable th)
				{
					// TODO: By default the executor service would have consumed
		            // this exception. Implement a way to re-throw it to the
		            // framework where it can process it properly.
					th.printStackTrace();
				}
			}
		};

		return ThreadExecutorService.getExecutorService().scheduleWithFixedDelay(logicToExecuteRepeatedly, startTime,
				timeInterval, TimeUnit.MILLISECONDS);
	}

	/**
	 * Returns the name of the Task.
	 * @return
	 */
	public String getName() {
		return name;
	}
}
