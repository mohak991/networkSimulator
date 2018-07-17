package edu.uic.networkSim;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SimFramework extends JFrame implements ActionListener {
  private static final long serialVersionUID = 1L;
  private static final String[] NETWORK_COMPONENTS = {"Switch", "Server", "Client"};
  private static final String MAC_ADDRESS = "128.128.0.";
  private static final String IP_ADDRESS = "192.0.0.";
  private static final int[] APPLICATION_PACKET_TIMES = {50, 100, 200, 500};
  private static final int[] MIN_PACKETS = {1, 2, 2, 5};
  private static final int[] MAX_PACKETS = {10, 25, 20, 5};
  private static final String BROADCAST_IP_ADDRESS = "0.0.0.0";
  private static SimFramework w;
  private final ImageIcon iconServer;
  private final ScheduledExecutorService service;
  private JComboBox<String> deviceDropDown;
  private JComboBox<String> fromDevice;
  private JComboBox<String> toDevice;
  private JComboBox<String> fromDevice1;// This is for switch client Change name latter
  private JComboBox<String> toDevice1; // This is for switch client Change name latter
  private JComboBox<String> appDropDown;
  private JComboBox<String> connClientDropDown;
  private JComboBox<String> allDeviceDropDown;
  private JComboBox<String> removeFromDevice;
  private JComboBox<String> removeToDevice;
  private JButton remove;
  private JButton removeConnections;
  private JButton runAppButton;
  private JTextArea logTextArea;
  private JButton selectDeviceButton;
  private JButton getAllRunningApps;
  private JButton connectClientSwitchButton;
  private JButton connectSwitchServerButton;
  private JPanel serverPanel;
  private JPanel clientPanel;
  private JPanel switchPanel;
  private Container parentContainer;
  private int windowWidth;
  private int windowHeight;
  private JPanel clientSwitchPanel;
  private JPanel serverSwitchPanel;
  private Map<String, SimDevice> deviceMap;
  private boolean drawLine;
  private JPanel appPanel;
  private Map<String, JPanel> listOfLines;
  private long timeCounter;
  private List<SimApp> listOfSimApps;
  private List<SimDevice> listOfServers;
  private List<SimDevice> listOfClients;
  private List<SimDevice> listOfSwitches;
  private List<String> listOfConnectedDevices;
  private List<String> connections;
  private Map<String, String> applicationsMap;
  private List<Object> allDevices;
  private List<String> allDeviceNames;
  // Assignment 2
  private int countOfDevices;
  private int countOfIPs;
  private ImageIcon iconSwitch;
  private ImageIcon iconClient;
  private Date startDate;
  private JLabel timerLabel;
  private JPanel timerPanel;
  private JButton export;
  private Map<String, String> clientMap;
  private Map<String, String> switchMap;

  //shasaboo
  private List<String> pathList;
  private JComboBox<String> sourceClient;
  private JComboBox<String> destinatedServer;
  private JButton sendPackets;
  private JPanel sendPacketPanel;

  public SimFramework() {
    listOfSimApps = new ArrayList<>();
    listOfServers = new ArrayList<>();
    listOfClients = new ArrayList<>();
    listOfSwitches = new ArrayList<>();
    listOfConnectedDevices = new ArrayList<>();
    allDevices = new ArrayList<>();
    allDeviceNames = new ArrayList<>();
    listOfLines = new HashMap<>();
    connections = new ArrayList<>();
    deviceMap = new HashMap<>();
    applicationsMap = new HashMap<>();
    destinatedServer = new JComboBox<>();
    clientMap = new HashMap<>();
    switchMap = new HashMap<>();
    iconServer = new ImageIcon("server.jpg");
    iconSwitch = new ImageIcon("switch.jpg");
    iconClient = new ImageIcon("client.jpg");
    pathList = new ArrayList<>();
    createLayout();
    service = Executors.newSingleThreadScheduledExecutor();
    setIntialTimer();
    service.scheduleAtFixedRate(getRunnable(), 0, 1, TimeUnit.SECONDS);
  }

  private void createLayout() {
    createNetworkPanel();
    parentContainer = getContentPane();
    parentContainer.setLocation(0, 0);
    parentContainer.setLayout(null);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    windowWidth = 1680/*Toolkit.getDefaultToolkit().getScreenSize().width*/;
    windowHeight = 1050/*Toolkit.getDefaultToolkit().getScreenSize().height*/;
    setSize(windowWidth, windowHeight);
    System.out.println("Window Height " + windowHeight);
    System.out.println("Window Width " + windowWidth);
    final JPanel devicePanel = new JPanel();
    devicePanel.setBounds((windowWidth / 2 - 150), 0, 340, 50);
    JLabel label = new JLabel("Device List: ");
    label.setForeground(Color.BLACK);
    label.setFont(new Font("Courier", Font.BOLD, 16));
    devicePanel.add(label);
    deviceDropDown = new JComboBox<>(NETWORK_COMPONENTS);
    fromDevice = new JComboBox<>();
    toDevice = new JComboBox<>();
    fromDevice1 = new JComboBox<>();
    toDevice1 = new JComboBox<>();
    selectDeviceButton = new JButton("Add");
    selectDeviceButton.addActionListener(this);
    devicePanel.add(deviceDropDown);
    devicePanel.add(selectDeviceButton);
    parentContainer.add(devicePanel);
    createLogLayout();
    createDeleteLayout();
    createTimerLayout();
    logTime("Initialized Layout..." + "\n");
    setVisible(true);
  }

  private void setIntialTimer() {
    final SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
    String s = format.format(new Date());
    try {
      startDate = format.parse(s);
    } catch (ParseException e1) {
      e1.printStackTrace();
    }
  }

  private Runnable getRunnable() {
    return new Runnable() {
      public void run() {
        try {
          SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
          String s1 = format.format(new Date());
          Date newDate = format.parse(s1);
          long diff = newDate.getTime() - startDate.getTime();
          long diffSeconds = diff / 1000 % 60;
          long diffMinutes = diff / (60 * 1000) % 60;
          long diffHours = diff / (60 * 60 * 1000);
          timerLabel.setText(diffHours + ":" + diffMinutes + ":" + diffSeconds);
        } catch (ParseException e1) {
          e1.printStackTrace();
        }
      }
    };
  }

  private void createNetworkPanel() {
    serverPanel = new JPanel();
    clientPanel = new JPanel();
    switchPanel = new JPanel();
    clientPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 5));
    serverPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 5));
    switchPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 5));
    clientPanel.setBounds(0, 270, 105, 770);
    switchPanel.setBounds(222, 270, 105, 770);
    serverPanel.setBounds(422, 270, 105, 770);
  }

  private void createLogLayout() {
    JPanel logPanel = new JPanel();
    logPanel.setBounds(800, 250, windowWidth - 900, windowHeight - 500);
    JLabel label = new JLabel("Network Logs");
    label.setForeground(Color.BLACK);
    label.setFont(new Font("Courier", Font.BOLD, 20));
    getAllRunningApps = new JButton("Get All Running Apps");
    export = new JButton("Export Data");
    export.addActionListener(this);
    getAllRunningApps.addActionListener(this);
    getAllRunningApps.setEnabled(false);
    logPanel.setLayout(new BoxLayout(logPanel, BoxLayout.Y_AXIS));
    logTextArea = new JTextArea(25, 60);
    logTextArea.setForeground(Color.BLUE);
    JScrollPane scroll = new JScrollPane(logTextArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    logPanel.add(label);
    logPanel.add(scroll);
    logPanel.add(getAllRunningApps);
    logPanel.add(export);
    parentContainer.add(logPanel);
    parentContainer.repaint();
    setVisible(true);
  }

  private void createDeleteLayout() {
    JPanel deletePanel = new JPanel();
    deletePanel.setBounds(100, 0, 200, 100);
    allDeviceDropDown = new JComboBox<>();
    remove = new JButton("Remove");
    remove.addActionListener(this);
		/*removeFromDevice = new JComboBox<>();
		removeToDevice = new JComboBox<>();
		removeConnections = new JButton("Remove Connections");
		removeConnections.addActionListener(this);*/
    deletePanel.add(allDeviceDropDown);
    deletePanel.add(remove);
		/*deletePanel.add(removeFromDevice);
		deletePanel.add(removeToDevice);
		deletePanel.add(removeConnections);*/
    parentContainer.add(deletePanel);
    parentContainer.repaint();
    setVisible(true);
  }

  private void createTimerLayout() {
    timerPanel = new JPanel();
    timerPanel.setBounds(1420, 0, 200, 100);
    timerLabel = new JLabel();
    Font font = new Font("Courier", Font.BOLD, 28);
    timerLabel.setFont(font);
    timerPanel.add(timerLabel);
    parentContainer.add(timerPanel);
    parentContainer.repaint();
    setVisible(true);
  }

  private void logTime(String message) {
    String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
    logTextArea.append(time + ": " + message);
  }

  public static void main(String[] args) {
    w = new SimFramework();
  }

  private void writeToTextFile() {
    String text = logTextArea.getText();
    JFileChooser chooser = new JFileChooser();
    chooser.setCurrentDirectory(new File("./"));
    int actionDialog = chooser.showSaveDialog(this);
    if (actionDialog == JFileChooser.APPROVE_OPTION) {
      File fileName = new File(chooser.getSelectedFile() + "");
      if (fileName != null) {
        if (fileName.exists()) {
          actionDialog = JOptionPane.showConfirmDialog(this, "Replace existing file?");
          if (actionDialog == JOptionPane.NO_OPTION) {
            return;
          }
        }
        try {
          BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
          out.write(text);
          out.close();
        } catch (Exception e) {
          System.err.println("Error: " + e.getMessage());
        }
      }
    }
  }

  private void packetSent(String sourceAddress, String destinationAddress) {
    String switchKey = clientMap.get(sourceAddress);
    String path = sourceAddress + "-" + switchKey;
    int hopCount = 1;
    pathList = new ArrayList<>();
    shortestPath(switchMap, switchKey, path, hopCount);
    System.out.println("Path List::" + pathList);
    if (destinationAddress.contains("Broadcast")) {
      sendBroadCastMessage(sourceAddress, BROADCAST_IP_ADDRESS);
    } else {
      sendUnicastPackets(sourceAddress, destinationAddress);
    }
  }

  private void sendBroadCastMessage(String sourceAddress, String destinationAddress) {
    logTime("Broadcast Received from : " + sourceAddress + " " + destinationAddress + "\n");
    String clienMac = deviceMap.get(sourceAddress).getDeviceMacAddress();
    for (SimDevice s : listOfSwitches) {
      logTime("Broadcast Going to Switch from : " + sourceAddress + " (" + clienMac + ") " + s.getDeviceName() + " (" + s.getDeviceMacAddress() + ") " + "\n");
    }
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    logTime("Package received to destination on Server with mac address " + deviceMap.get("Server2").getDeviceMacAddress());
  }

  private void sendUnicastPackets(String sourceAddress, String destinationAddress) {
    String[] paths = new String[2];
    List<String> myList = new ArrayList<>();
    for (String s : pathList) {
      if (s.contains(destinationAddress)) {
        myList.add(s);
      }
    }
    int lengthOfString = 0;
    if (myList.size() > 1) {
      for (int i = 0; i < myList.size(); i++) {
        if (i != 0) {
          if (myList.get(i).length() < lengthOfString) {
            paths = myList.get(i).split(":");
            lengthOfString = myList.get(i).length();
          }
        } else {
          lengthOfString = myList.get(i).length();
          paths = myList.get(i).split(";");
        }
      }
    } else {
      paths = myList.get(0).split(":");
    }
    logTime("Shortest Path: " + paths[0] + "\n");

    String[] devices = paths[0].split("-");
    logTime("Mac Address Path - ");
    String message = "";
    for (String device : devices) {
      message = message + deviceMap.get(device).getDeviceMacAddress() + "-";
    }
    logTime(message + "\n");
    String appName = applicationsMap.get(sourceAddress);
    for (SimApp app : listOfSimApps) {
      if (app.getAppName().equals(appName)) {
        updatePacketIps(app, sourceAddress, destinationAddress);
        int i = 0;
        for (SimPacket packet : app.getSimMessage().getSimPacket()) {
          logTime(sourceAddress + "(" + app.getSimMessage().getSourceAddress() + ")"
            + " sending packet " + ++i + " to " + destinationAddress + "(" + app.getSimMessage().getDestinationAddress() + ")" + "\n");
          try {
            logTime("Waiting..." + packet.getPacketTime() + "ms" + "\n");
            Thread.sleep(packet.getPacketTime());
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
        logTime("Ending Application " + "\n");
      }
    }
  }

  private void updatePacketIps(SimApp app, String sourceAddress, String destinationAddress) {
    SimDevice sourceDevice = deviceMap.get(sourceAddress);
    app.getSimMessage().setSourceAddress(sourceDevice.getDeviceMacAddress());

    SimDevice destDevice = deviceMap.get(destinationAddress);
    app.getSimMessage().setDestinationAddress(destDevice.getDeviceMacAddress());
  }

  private void createApplications() {
    // Assignment2 : Running only 4 Applications.
    for (int i = 0; i < 4; i++) {
      String appName = "Application " + (i + 1);
      SimApp simAppObj = new SimApp();
      simAppObj.setAppName(appName);
      int numberOfPackets = new Random().nextInt(MAX_PACKETS[i]) + MIN_PACKETS[i];
      //1 packet 50 ms then total packet * 50 ms - total time
      SimMessage msg = new SimMessage(numberOfPackets, (APPLICATION_PACKET_TIMES[i] * numberOfPackets));
      simAppObj.setSimMessage(msg);
      listOfSimApps.add(simAppObj);
      appDropDown.addItem(appName);
    }
  }

  private void createAppLayout() {
    appPanel = new JPanel();
    appPanel.setBounds((windowWidth / 2 - 280), 150, 540, 50);
    appDropDown = new JComboBox<>();
    JLabel label = new JLabel("Start Applications: ");
    label.setForeground(Color.BLACK);
    label.setFont(new Font("Courier", Font.BOLD, 16));
    connClientDropDown = new JComboBox<>();
    runAppButton = new JButton("Run");
    runAppButton.addActionListener(this);
    appPanel.add(label);
    appPanel.add(connClientDropDown);
    appPanel.add(appDropDown);
    appPanel.add(runAppButton);
    createApplications();
    appPanel.setVisible(true);
    parentContainer.add(appPanel);
    parentContainer.repaint();
    setVisible(true);
  }

  private void addItemToJComboBox(JComboBox<String> myComboBox, String item) {
    boolean exists = false;
    for (int index = 0; index < myComboBox.getItemCount() && !exists; index++) {
      if (item.equals(myComboBox.getItemAt(index))) {
        exists = true;
      }
    }
    if (!exists) {
      myComboBox.addItem(item);
    }
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getSource().equals(selectDeviceButton)) {
      addDevices(e);
    } else if (e.getSource().equals(connectClientSwitchButton)) {
      connectDevices(e, fromDevice, toDevice);
    } else if (e.getSource().equals(connectSwitchServerButton)) {
      connectDevices(e, fromDevice1, toDevice1);
    } else if (e.getSource().equals(runAppButton)) {
      runApplications(e);
    } else if (e.getSource().equals(getAllRunningApps)) {
      showRunningAppsInLog();
    } else if (e.getSource().equals(remove)) {
      removeDevice();
    } else if (e.getSource().equals(sendPackets)) {
      packetSent((String) sourceClient.getSelectedItem(), (String) destinatedServer.getSelectedItem());
    } else if (e.getSource().equals(removeConnections)) {
      //removeConnections();
    } else if (e.getSource().equals(export)) {
      writeToTextFile();
    }
  }

  private void showRunningAppsInLog() {
    int i = 1;
    for (Map.Entry<String, String> entry : applicationsMap.entrySet()) {
      logTime(entry.getValue() + " running on " + entry.getKey() + "..." + "\n");
    }
  }

  private void runApplications(ActionEvent e) {
    String appName = (String) appDropDown.getSelectedItem();
    String clientName = (String) connClientDropDown.getSelectedItem();
    applicationsMap.put(clientName, appName);
    //appDropDown.removeItem(appName);
    //shasaboo
    if (sendPacketPanel == null) {
      createSendPacketsPanel();
    }
    sourceClient.addItem(clientName);
    connClientDropDown.removeItem(clientName);
    logTime("Running " + appName + " on " + clientName + "\n");
    getAllRunningApps.setEnabled(true);
    if (connClientDropDown.getItemCount() <= 0) {
      appPanel.setVisible(false);
    }
    if (applicationsMap.isEmpty()) {
      getAllRunningApps.setEnabled(true);
    }
  }

  private void createSendPacketsPanel() {
    //shasaboo
    JLabel routingLabel = new JLabel("Send Packets: ");
    Font font = new Font("Courier", Font.BOLD, 16);
    routingLabel.setFont(font);
    sendPackets = new JButton("Send Packets");
    sendPackets.addActionListener(this);
    sourceClient = new JComboBox<>();
    //shasaboo - End
    //shasaboo
    sendPacketPanel = new JPanel();
    sendPacketPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
    sendPacketPanel.setBounds((windowWidth / 2 - 280), 200, 540, 50);
    sendPacketPanel.add(routingLabel);
    sendPacketPanel.add(sourceClient);
    sendPacketPanel.add(destinatedServer);
    sendPacketPanel.add(sendPackets);
    destinatedServer.addItem("Broadcast");
    parentContainer.add(sendPacketPanel);
  }

  //Remove fromd evice location. Remov from lists. Remove connections. First check if nay application is running.
  //deviceMao
  //connactedDevices
  //fromDevice
  //toDevice
  private void connectDevices(ActionEvent e, JComboBox<String> fDevice, JComboBox<String> tDevice) {
    Point start = deviceMap.get(((String) fDevice.getSelectedItem())).getDeviceLocation();
    Point end = deviceMap.get(((String) tDevice.getSelectedItem())).getDeviceLocation();
    String fromName = deviceMap.get(((String) fDevice.getSelectedItem())).getDeviceName();
    String toName = deviceMap.get(((String) tDevice.getSelectedItem())).getDeviceName();
    if (!connections.contains(fromName + "" + "" + "" + toName) && !fromName.equals(toName)) {
      if (fromName.contains("Client")) {
        clientMap.put(fromName, toName);
      } else {
        if (switchMap.get(fromName) != null) {
          switchMap.put(fromName, switchMap.get(fromName) + ";" + toName);
        } else {
          switchMap.put(fromName, toName);
        }
        if (fromName.contains("Switch") && toName.contains("Switch")) {
          if (switchMap.get(toName) != null) {
            switchMap.put(toName, switchMap.get(toName) + ";" + fromName);
          } else {
            switchMap.put(toName, fromName);
          }
        }
      }
      System.out.println("ClientMap:: " + clientMap.toString());
      System.out.println("ServerMap:: " + switchMap.toString());
      logTime("Creating connection between " + fromName + " and " + toName + "\n");
      drawLine = true;
      listOfConnectedDevices.add(fromName);
      listOfConnectedDevices.add(toName);
      updateIPAddress(fromName, toName);
      if (fromName.contains("Client") || toName.contains("Client")) {
        if (appPanel == null) {
          createAppLayout();
        }
        if (fromName.contains("Client")) {
          addItemToJComboBox(connClientDropDown, fromName);
          appPanel.setVisible(true);
        } else {
          addItemToJComboBox(connClientDropDown, toName);
          appPanel.setVisible(true);
        }
      }
      if (fromName.contains("Switch") && toName.contains("Switch")) {
        //Assignment 2 addition . Done to show connections to swtich as well
        showClientSwitchPane(fromName);
        showClientSwitchPane(toName);
      } else {
        if (fromName.contains("Switch")) {
          showClientSwitchPane(fromName);
        } else if (toName.contains("Switch")) {
          showClientSwitchPane(toName);
        }
      }
      JPanel draw = new JPanel() {
        private static final long serialVersionUID = 1L;

        @Override
        public void paint(Graphics arg0) {
          if (fromName.contains("Switch") && toName.contains("Switch")) {
            arg0.drawLine(start.x - 12, start.y + 22, end.x - 12, end.y - 44);
          } else {
            arg0.drawLine(start.x + 12, start.y, end.x - 76, end.y);
          }
          drawLine = false;
          if (drawLine) {
            super.paint(arg0);
          }
          arg0.dispose();
        }
      };
      draw.setVisible(true);
      connections.add(fromName + "" + "" + "" + toName);
      connections.add(toName + "" + "" + "" + fromName);
      listOfLines.put(fromName + "" + "" + "" + toName, draw);
      listOfLines.put(toName + "" + "" + "" + fromName, draw);
      parentContainer.add(draw, 0);
      draw.setBounds(0, 0, windowWidth, windowHeight);
      parentContainer.repaint();
      setVisible(true);
    } else if (fromName.equals(toName)) {
      logTime("Cannot have same from and to devices..." + "\n");
    } else {
      logTime("Connection Exist..." + "\n");
    }
  }

  private void updateIPAddress(String fromName, String toName) {
    if (fromName.contains("Switch") && toName.contains("Switch")) {
      return;
    }
    if (!fromName.contains("Switch")) {
      if (fromName.contains("Server")) {
        SimDevice sw = listOfServers.get(listOfServers.size() - 1);
        sw.setDeviceIpAddress(IP_ADDRESS + countOfIPs++);
        logTime(fromName + ": IP Address " + sw.getDeviceIpAddress() + "\n");
      } else {
        SimDevice sw = listOfClients.get(listOfClients.size() - 1);
        sw.setDeviceIpAddress(IP_ADDRESS + countOfIPs++);
        logTime(fromName + ": IP Address " + sw.getDeviceIpAddress() + "\n");
      }
    }

    if (!toName.contains("Switch")) {
      if (toName.contains("Server")) {
        SimDevice sw = listOfServers.get(listOfServers.size() - 1);
        sw.setDeviceIpAddress(IP_ADDRESS + countOfIPs++);
        logTime(toName + ": IP Address " + sw.getDeviceIpAddress() + "\n");
      } else {
        SimDevice sw = listOfClients.get(listOfClients.size() - 1);
        sw.setDeviceIpAddress(IP_ADDRESS + countOfIPs++);
        logTime(toName + ": IP Address " + sw.getDeviceIpAddress() + "\n");
      }
    }
  }

  private void addDevices(ActionEvent e) {
    String selectedDevice = ((String) deviceDropDown.getSelectedItem()); //Check what kind of device it is
    JLabel macAddressLabel = new JLabel(MAC_ADDRESS + countOfDevices++);
    macAddressLabel.setForeground(Color.RED);
    if (selectedDevice.equalsIgnoreCase("client")) {
      if (listOfClients.size() < 6) {
        JLabel lb = new JLabel(iconClient);
        String name = "Client" + (listOfClients.size() + 1);
        SimClient c = new SimClient(name, lb.getLocation(), macAddressLabel.getText());
        listOfClients.add(c);
        JLabel uiName = new JLabel(name);
        Font font = new Font("Courier", Font.BOLD, 16);
        uiName.setFont(font);
        clientPanel.add(uiName);
        clientPanel.add(lb);
        clientPanel.add(macAddressLabel);
        parentContainer.add(clientPanel, 0);
        deviceMap.put(name, c);
        showClientSwitchPane(name);
        c.setDeviceLocation(lb.getLocationOnScreen());
        addItemToJComboBox(allDeviceDropDown, name);
        //addItemToJComboBox(removeFromDevice, name);
        logTime("Added :" + name + "\n");
      } else {
        logTime("Reached max number of client.." + "\n");
      }
    } else if (selectedDevice.equalsIgnoreCase("server")) {
      if (listOfServers.size() < 6) {
        JLabel lb = new JLabel(iconServer);
        String name = "Server" + (listOfServers.size() + 1);
        SimServer s = new SimServer(name, lb.getLocation(), macAddressLabel.getText());
        listOfServers.add(s);
        JLabel uiName = new JLabel(name);
        Font font = new Font("Courier", Font.BOLD, 16);
        uiName.setFont(font);
        serverPanel.add(uiName);
        serverPanel.add(lb);
        serverPanel.add(macAddressLabel);
        parentContainer.add(serverPanel);
        deviceMap.put(name, s);
        logTime("Added :" + name + "\n");
        showSwitchServerPane(name);
        showClientSwitchPane(name);
        s.setDeviceLocation(lb.getLocationOnScreen());
        addItemToJComboBox(allDeviceDropDown, name);
        if (destinatedServer != null) {
          addItemToJComboBox(destinatedServer, name);
        }
        //addItemToJComboBox(removeToDevice, name);
      } else {
        logTime("Reached max number of servers.." + "\n");
      }
    } else {
      if (listOfSwitches.size() < 6) {
        JLabel lb = new JLabel(iconSwitch);
        final String name = "Switch" + (listOfSwitches.size() + 1);
        SimSwitch sw = new SimSwitch(name, lb.getLocation(), macAddressLabel.getText());
        listOfSwitches.add(sw);
        JLabel uiName = new JLabel(name);
        Font font = new Font("Courier", Font.BOLD, 16);
        uiName.setFont(font);
        switchPanel.add(uiName);
        switchPanel.add(lb);
        switchPanel.add(macAddressLabel);
        parentContainer.add(switchPanel);
        deviceMap.put(name, sw);
        logTime("Added :" + name + "\n");
        showSwitchServerPane(name);
        showClientSwitchPane(name);
        sw.setDeviceLocation(lb.getLocationOnScreen());
        addItemToJComboBox(allDeviceDropDown, name);
        //addItemToJComboBox(removeToDevice, name);
        //addItemToJComboBox(removeFromDevice, name);
      } else {
        logTime("Reached max number of switches.." + "\n");
      }
    }
    parentContainer.repaint();
    setVisible(true);

  }

  private void showSwitchServerPane(String name) {
    if (serverSwitchPanel == null) {
      serverSwitchPanel = new JPanel();
      serverSwitchPanel.setBounds((windowWidth / 2 - 320), 50, 640, 50);
      JLabel label = new JLabel("Sw-Ser/Sw-Sw:");
      label.setForeground(Color.BLACK);
      label.setFont(new Font("Courier", Font.BOLD, 16));
      serverSwitchPanel.add(label);
      serverSwitchPanel.add(fromDevice1);
      serverSwitchPanel.add(toDevice1);
      connectSwitchServerButton = new JButton("Connect");
      connectSwitchServerButton.addActionListener(this);
      serverSwitchPanel.add(connectSwitchServerButton);
    }
    if (!name.contains("Client")) {
      addItemToJComboBox(toDevice1, name);
      setVisible(true);
    }
    if (!name.contains("Server") && !name.contains("Client")) {
      addItemToJComboBox(fromDevice1, name);
      setVisible(true);
    }
    if (!listOfServers.isEmpty() && !listOfSwitches.isEmpty()) {
      serverSwitchPanel.repaint();
      fromDevice1.repaint();
      toDevice1.repaint();
      parentContainer.add(serverSwitchPanel);
      parentContainer.repaint();
    }
  }

  private void showClientSwitchPane(String name) {
    if (clientSwitchPanel == null) {
      clientSwitchPanel = new JPanel();
      clientSwitchPanel.setBounds((windowWidth / 2 - 320), 100, 640, 50);
      JLabel label = new JLabel("Cl-Sw/Sw-Sw:");
      label.setForeground(Color.BLACK);
      label.setFont(new Font("Courier", Font.BOLD, 16));
      clientSwitchPanel.add(label);
      clientSwitchPanel.add(fromDevice);
      clientSwitchPanel.add(toDevice);
      connectClientSwitchButton = new JButton("Connect");
      connectClientSwitchButton.addActionListener(this);
      clientSwitchPanel.add(connectClientSwitchButton);
    }
    if (!name.contains("Server")) {
      if (name.contains("Switch") && listOfConnectedDevices.contains(name)) {
        addItemToJComboBox(fromDevice, name);
        setVisible(true);
      } else if (name.contains("Client")) {
        addItemToJComboBox(fromDevice, name);
        setVisible(true);
      }
    }
    if (!name.contains("Server") && !name.contains("Client")) {//switch
      if (listOfConnectedDevices.contains(name)) {
        addItemToJComboBox(toDevice, name);
        setVisible(true);
      }
    }
    if (!listOfClients.isEmpty() && !listOfServers.isEmpty() && !listOfSwitches.isEmpty() && toDevice.getItemCount() > 0) {
      clientSwitchPanel.repaint();
      fromDevice.repaint();
      toDevice.repaint();
      parentContainer.add(clientSwitchPanel);
      parentContainer.repaint();
    }
  }

  private void removeItemsFromList(List<SimDevice> list, String name) {
    list.removeIf(a -> a.getDeviceName().equals(name));
  }

  private void removeDevice() {
    String name = (String) allDeviceDropDown.getSelectedItem();
    if (name != null) {
      if (name.contains("Client")) {
        removeItemsFromList(listOfClients, name);
        clientPanel.remove(0);
        clientPanel.remove(1);
        clientPanel.remove(2);
        clientPanel.repaint();
        fromDevice.removeItem(allDeviceDropDown.getSelectedItem());
        //removeFromDevice.removeItem(allDeviceDropDown.getSelectedItem());
      }
      if (name.contains("Switch")) {
        removeItemsFromList(listOfSwitches, name);
        switchPanel.remove(0);
        switchPanel.remove(1);
        switchPanel.remove(2);
        switchPanel.repaint();
        fromDevice.removeItem(allDeviceDropDown.getSelectedItem());
        toDevice.removeItem(allDeviceDropDown.getSelectedItem());
        fromDevice1.removeItem(allDeviceDropDown.getSelectedItem());
        toDevice1.removeItem(allDeviceDropDown.getSelectedItem());
        //removeFromDevice.removeItem(allDeviceDropDown.getSelectedItem());
        //removeToDevice.removeItem(allDeviceDropDown.getSelectedItem());
      }
      if (name.contains("Server")) {
        removeItemsFromList(listOfServers, name);
        serverPanel.remove(0);
        serverPanel.remove(1);
        serverPanel.remove(2);
        serverPanel.repaint();
        toDevice1.removeItem(allDeviceDropDown.getSelectedItem());
        //removeToDevice.removeItem(allDeviceDropDown.getSelectedItem());
      }
      allDeviceDropDown.removeItem(name);
      logTime("Removed " + name + "\n");
      parentContainer.repaint();
      setVisible(true);
    }
  }

  private void removeConnections() {
    //visible false
    String fromto = (String) removeFromDevice.getSelectedItem() + (String) removeToDevice.getSelectedItem();
    String tofrom = (String) removeToDevice.getSelectedItem() + (String) removeFromDevice.getSelectedItem();
    listOfLines.remove(fromto);
    listOfLines.remove(tofrom);
    parentContainer.repaint();
    setVisible(true);
  }

  public void shortestPath(Map<String, String> switchMap, String switchKey, String path, int hopCount) {
    String switchValue = "";
    switchValue = switchMap.get(switchKey);
    if (switchValue == null) {
      path = path + ":" + 0;
      pathList.add(path);
    }
    if (switchValue.contains(";")) {
      String[] temp = switchValue.split(";");
      for (String aTemp : temp) {
        if (aTemp.contains("Server")) {
          hopCount++;
          pathList.add(path + "-" + aTemp + ":" + hopCount);
        } else {
          if (!path.contains(aTemp)) {
            path += "-" + aTemp;
            hopCount++;
            switchKey = aTemp;
            shortestPath(switchMap, switchKey, path, hopCount);
          }
        }
      }
    } else if (switchValue.contains("server")) {
      hopCount++;
      pathList.add(path + ":" + hopCount);
    } else {
      if (!path.contains(switchValue)) {
        path += "-" + switchValue;
        hopCount++;
        switchKey = switchValue;
        shortestPath(switchMap, switchKey, path, hopCount);
      }
    }
  }
}
