import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

import static java.lang.Math.abs;

/**
 * The type Floor.
 * Receives notifications that an elevator has arrived at a floor and sends the internal button presses from the input
 * file to the elevator
 */
public class Floor {

    private Queue queueReceive;
    private FloorData floorData;
    DatagramSocket socket;

    /**
     * Instantiates a new Floor.
     *
     * @param queueReceive the queue receive
     * @param floorData    the floor data
     */
    public Floor(Queue queueReceive, FloorData floorData) {
        this.queueReceive = queueReceive;
        this.floorData = floorData;
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }

    private void run() {
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
                    ArrayList<InfoPacket> requestsToHandle = floorData.popNumFloorRequests(direction, floor, gotOn);
                    for (int i =0; i < gotOn; i++){
                        InfoPacket request = requestsToHandle.removeFirst(); //Pop first request
                        request.setElevatorNum(elevatorNum);
                        sendPacket(request, Config.elevatorBasePort + elevatorNum);
                    }
                    //Handle Lights
                    floorData.setElevatorDirectionLights(direction, floor, false);
                }
            }
        }
    }

    /**
     * Send packet.
     *
     * @param packet   the packet
     * @param sendPort the send port
     */
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

    /**
     * Main.
     *
     * @param args the args
     */
    public static void main(String args[]){
        //Sleep for 6 sec to allow other instances to start before sending packets
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Queue queueReceive = new Queue();
        try {
            DatagramSocket socket = new DatagramSocket(2999);
            Thread udpThread = new Thread(new UDPThread(socket, queueReceive));
            udpThread.start();
            FloorData floorData = new FloorData();
            Floor floor = new Floor(queueReceive, floorData);
            Thread floorIO = new Thread(new FloorIO(floor, floorData));
            floorIO.start();
            floor.run();
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }

    }

}
