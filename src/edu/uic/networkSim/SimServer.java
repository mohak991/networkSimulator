package edu.uic.networkSim;

import java.awt.Point;
import java.util.List;

import javax.swing.JLabel;

/*
 * Same index for running app and client being serve in below list
 * Array should be editable via GUI
 */
public class SimServer extends SimDevice {
    private List<SimApp> listOfRunningApps;
    private List<SimClient> listOfConnectedServers;
    
    
    public SimServer(String serverName, Point serverPoint, String macAddress) {
    	    super(serverName, serverPoint, macAddress);
	}
}
