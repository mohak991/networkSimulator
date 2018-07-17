package edu.uic.networkSim;

import java.awt.Point;
import java.util.List;

/*
 * The array sizes should be the same as the port count (for demo purposes, 60 ports will suffice). 
 */
public class SimSwitch extends SimDevice {
	private int portCount;
	private List<SimDevice> listOfCOnnectedDevices;
	
	public SimSwitch(String switchName, Point switchPoint, String macAddress) {
		super(switchName, switchPoint, macAddress);
	}

}
