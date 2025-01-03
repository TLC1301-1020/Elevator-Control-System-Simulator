import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

import static java.lang.Math.abs;

public class Floor {

    private Queue queueReceive;
    DatagramSocket socket;
    //TODO synchronzied floor request and split infinte loop into other thread for arrivals over time?
    private ArrayList<InfoPacket>[][] floorRequests; //Direction: Floor
    private boolean[][] floorButtonLights; //Direction: Floor

    private boolean[][] elevatorDirectionLights; //Direction: Floor

    public Floor(Queue queueReceive) {
        this.queueReceive = queueReceive;
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }

        floorRequests = new ArrayList[2][Config.numFloors];
        for (int j=0; j<floorRequests.length; j++){
            for (int i = 0; i < floorRequests[j].length; i++) {
                floorRequests[j][i] = new ArrayList<>();
            }
        }

        floorButtonLights = new boolean[2][Config.numFloors];
        elevatorDirectionLights = new boolean[2][Config.numFloors];

        run();
    }

    private void run() {
        String filename = "input.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                //split input from file on whitespace
                String[] split = line.split(" ");

                int orginFloor = Integer.parseInt(split[1]);
                int destFloor = Integer.parseInt(split[3]);
                int direction = (destFloor - orginFloor) > 0 ? 1 : 0;

                // Send Packet to Scheduler for External Floor request
                InfoPacket packet = new InfoPacket();
                packet.setType(1);
                //abs origin floor since it may be negative indicating error injection
                packet.setOriginFloor(abs(orginFloor));
                split[2] = split[2].toLowerCase();
                if (split[2].equals("up")){
                    packet.setDirection(true);
                } else {
                    packet.setDirection(false);
                }
                sendPacket(packet, Config.schedulerPort);

                // Send Packet to Elevator for internal button press
                packet = new InfoPacket();
                //inject floor fault if negative
                if (orginFloor < 0){
                    packet.setType(15);
                } else {
                    packet.setType(3);
                }
                packet.setDestFloor(destFloor);
                floorRequests[direction][orginFloor].add(packet);
                floorButtonLights[direction][orginFloor] = true;

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true){
            InfoPacket messageReceived = queueReceive.get();
            if (messageReceived != null) {
                if (messageReceived.getType() == 9) {
                    // Get the message from the scheduler.
                    System.out.println("Floor received message: " + messageReceived);
                    //Handle floor arrival
                    //Send elevator button presses
                    int direction = messageReceived.isDirection() ? 1 : 0;
                    int elevatorNum = messageReceived.getElevatorNum();
                    int floor = messageReceived.getOriginFloor();
                    int gotOn = messageReceived.getGotOn();
                    ArrayList<InfoPacket> requestsToHandle = floorRequests[direction][floor];
                    for (int i =0; i < gotOn; i++){
                        InfoPacket request = requestsToHandle.removeFirst(); //Pop first request
                        request.setElevatorNum(elevatorNum);
                        sendPacket(request, Config.elevatorBasePort + elevatorNum);
                    }
                    //Handle Lights
                    floorButtonLights[direction][floor] = false;
                }
            }
        }
    }

    public void sendPacket(InfoPacket packet, int sendPort){
        System.out.println("Sent: "+packet.toString()+" port: "+ sendPort);
        // Send packet
        byte[] sendData = packet.pack();
        try {
            InetAddress sendAddress = InetAddress.getLocalHost();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, sendAddress, sendPort);
            try {
                socket.send(sendPacket);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String args[]){
        //Sleep for 5 sec to allow other instances to start before sending packets
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Queue queueReceive = new Queue();
        try {
            DatagramSocket socket = new DatagramSocket(2999);
            Thread udpThread = new Thread(new UDPThread(socket, queueReceive));
            udpThread.start();
            Floor floor = new Floor(queueReceive);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }

    }

}
