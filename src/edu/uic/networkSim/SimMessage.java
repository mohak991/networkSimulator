package edu.uic.networkSim;

import java.util.ArrayList;
import java.util.List;

public class SimMessage {
	private String sourceAddress;
	private String destinationAddress;
	private int appIndex;
	private int sizeOfMessage;
	private int numberOfPackets;
	private List<SimPacket> simPacket;
    private int appTime;
    
    public SimMessage(int numberOfPackets, int appTime) {
		this.numberOfPackets = numberOfPackets;
		this.appTime = appTime;
		simPacket = new ArrayList<>();
		createPackets();
	}
	
	public int getAppTime() {
		return appTime;
	}
	
	public String getSourceAddress() {
		return sourceAddress;
	}

	public void setSourceAddress(String sourceAddress) {
		this.sourceAddress = sourceAddress;
	}

	public String getDestinationAddress() {
		return destinationAddress;
	}


	public void setDestinationAddress(String destinationAddress) {
		this.destinationAddress = destinationAddress;
	}

	public int getAppIndex() {
		return appIndex;
	}

	public void setAppIndex(int appIndex) {
		this.appIndex = appIndex;
	}

	public int getSizeOfMessage() {
		return sizeOfMessage;
	}

	public void setSizeOfMessage(int sizeOfMessage) {
		this.sizeOfMessage = sizeOfMessage;
	}

	public int getNumberOfPackets() {
		return numberOfPackets;
	}

	public void setNumberOfPackets(int numberOfPackets) {
		this.numberOfPackets = numberOfPackets;
	}

	public List<SimPacket> getSimPacket() {
		return simPacket;
	}

	public void setSimPacket(List<SimPacket> simPacket) {
		this.simPacket = simPacket;
	}

	public void setAppTime(int appTime) {
		this.appTime = appTime;
	}
	
	private void createPackets() {
		for(int i = 0; i< numberOfPackets; i++) {
			SimPacket packet = new SimPacket();
			packet.setPacketTime(appTime/numberOfPackets);
			simPacket.add(packet);
		}
	}
}
