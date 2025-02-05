/*******************************************************************************
 * © 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package model;

public class DevicesListenerStatusSession {
	private final static DevicesListenerStatusSession instance = new DevicesListenerStatusSession();

	public static DevicesListenerStatusSession getInstance()
	{
		return instance;
	}
	private boolean isS5MonitorAssigned = false;
	private boolean removeS5MonitorListener = false;
	private boolean isAnesthesiaMachineAssigned = false;
	private boolean removeAnesthesiaMachineListener = false;
	private boolean isInjectomatassigned = false;
	private boolean removeInjectomatListener = false;
	private boolean isAnesthesiaMachineScanned=false;
	private boolean isS5MonitorScanned=false;

	public boolean isS5MonitorAssigned() {
		return isS5MonitorAssigned;
	}
	public void setS5MonitorAssigned(boolean isS5MonitorAssigned) {
		this.isS5MonitorAssigned = isS5MonitorAssigned;
	}
	public boolean isRemoveS5MonitorListener() {
		return removeS5MonitorListener;
	}
	public void setRemoveS5MonitorListener(boolean removeS5MonitorListener) {
		this.removeS5MonitorListener = removeS5MonitorListener;
	}
	public boolean isAnesthesiaMachineAssigned() {
		return isAnesthesiaMachineAssigned;
	}
	public void setAnesthesiaMachineAssigned(boolean isAnesthesiaMachineAssigned) {
		this.isAnesthesiaMachineAssigned = isAnesthesiaMachineAssigned;
	}
	public boolean isRemoveAnesthesiaMachineListener() {
		return removeAnesthesiaMachineListener;
	}
	public void setRemoveAnesthesiaMachineListener(boolean removeAnesthesiaMachineListener) {
		this.removeAnesthesiaMachineListener = removeAnesthesiaMachineListener;
	}
	public boolean isInjectomatassigned() {
		return isInjectomatassigned;
	}
	public void setInjectomatassigned(boolean isInjectomatassigned) {
		this.isInjectomatassigned = isInjectomatassigned;
	}
	public boolean isRemoveInjectomatListener() {
		return removeInjectomatListener;
	}
	public void setRemoveInjectomatListener(boolean removeInjectomatListener) {
		this.removeInjectomatListener = removeInjectomatListener;
	}
	public boolean isAnesthesiaMachineScanned() {
		return isAnesthesiaMachineScanned;
	}
	public void setAnesthesiaMachineScanned(boolean isAnesthesiaMachineScanned) {
		this.isAnesthesiaMachineScanned = isAnesthesiaMachineScanned;
	}
	public boolean isS5MonitorScanned() {
		return isS5MonitorScanned;
	}
	public void setS5MonitorScanned(boolean isS5MonitorScanned) {
		this.isS5MonitorScanned = isS5MonitorScanned;
	}




}
