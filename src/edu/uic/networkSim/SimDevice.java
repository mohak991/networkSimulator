package edu.uic.networkSim;

import java.awt.Point;

public abstract class SimDevice {
	private String deviceName;
	private Point deviceLocation;
	private String deviceMacAddress;
	private String deviceIpAddress;

	public SimDevice(String deviceName, Point deviceLocation, String deviceMacAddress) {
		this.deviceName = deviceName;
		this.deviceLocation = deviceLocation;
		this.deviceMacAddress = deviceMacAddress;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public Point getDeviceLocation() {
		return deviceLocation;
	}

	public void setDeviceLocation(Point deviceLocation) {
		this.deviceLocation = deviceLocation;
	}

	public String getDeviceMacAddress() {
		return deviceMacAddress;
	}

	public void setDeviceMacAddress(String deviceMacAddress) {
		this.deviceMacAddress = deviceMacAddress;
	}

	public String getDeviceIpAddress() {
		return deviceIpAddress;
	}

	public void setDeviceIpAddress(String deviceIpAddress) {
		this.deviceIpAddress = deviceIpAddress;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
}