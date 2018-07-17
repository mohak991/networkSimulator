package edu.uic.networkSim;

public class SimApp {
  private SimMessage simMessage;
  private String appName;

  public String getAppName() {
    return appName;
  }

  public void setAppName(String appName) {
    this.appName = appName;
  }

  public SimMessage getSimMessage() {
    return simMessage;
  }

  public void setSimMessage(SimMessage simMessage) {
    this.simMessage = simMessage;
  }
}
