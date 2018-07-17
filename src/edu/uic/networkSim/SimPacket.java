package edu.uic.networkSim;

public class SimPacket {
	private String sourceAddress;
	private String destinationAddress;
	private int appIndex;
	private int sequenceNumber;
	private int packetTime;
	
	public SimPacket() {
	}
	
	public int getPacketTime() {
		return packetTime;
	}

	public void setPacketTime(int packetTime) {
		this.packetTime = packetTime;
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

	public int getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(int sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}
}
