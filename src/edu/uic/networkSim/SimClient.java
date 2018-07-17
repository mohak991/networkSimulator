package edu.uic.networkSim;

import java.awt.Point;
import java.util.List;

/*
 * A client object doesn't need to be immediately assigned to a server when created but 
 * launching any application will require a server connection.
 */
public class SimClient extends SimDevice {
	private List<SimApp> listOfRunningApps;
	private List<SimClient> listOfClientsBeingServed;

	public SimClient(String clientName, Point clientPoint, String macAddress) {
		super(clientName, clientPoint, macAddress);
	}
}
