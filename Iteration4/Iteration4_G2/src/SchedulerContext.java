import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;

public class SchedulerContext {

    private SchedulerState currentState;
    private int[] elevatorFloor;

    private int[] elevatorOccupants;

    private Thread[] timer;
    private int[] elevatorDirection; // 0:down 1:up 2:idle
    private int[] elevatorDestination;
    private ArrayList<InfoPacket>[][] requestQueueElevators; //Elevators: Floors
    private ArrayList<InfoPacket>[][] requestQueueFloors; //Direction: Floor

    public SchedulerContext(){
        elevatorFloor = new int[Config.numElevators];
        elevatorDirection = new int[Config.numElevators];
        elevatorDestination = new int[Config.numElevators];
        elevatorOccupants = new int[Config.numElevators];
        timer = new Thread[Config.numElevators];

        requestQueueElevators = new ArrayList[Config.numElevators][Config.numFloors];
        for(int i =0; i< requestQueueElevators.length;i++){
            for(int j =0; j < requestQueueElevators[i].length;j++){
                requestQueueElevators[i][j] = new ArrayList<>();
            }
        }
        requestQueueFloors = new ArrayList[2][Config.numFloors];
        for (int j=0; j<requestQueueFloors.length; j++){
            for (int i = 0; i < requestQueueFloors[j].length; i++) {
                requestQueueFloors[j][i] = new ArrayList<>();
            }
        }
        Arrays.fill(elevatorDirection, 2);

        //Init current state
        currentState = new SchedulerState();
    }

    public void receivePacket(InfoPacket infoPacket){
        if (infoPacket.getType() == 1 || infoPacket.getType() == 2){
            //Floor Request
            setCurrentState(currentState.requestFloor(this, infoPacket), infoPacket);
        } else if (infoPacket.getType() == 4) {
            //Floor Arrival
            setCurrentState(currentState.elevatorArrival(this, infoPacket), infoPacket);
        } else if (infoPacket.getType() == 10 || infoPacket.getType() == 11 || infoPacket.getType() == 12) {
            setCurrentState(currentState.timeout(this, infoPacket), infoPacket);
        } else if (infoPacket.getType() == 13 || infoPacket.getType() == 14) {
            setCurrentState(currentState.doorNotification(this, infoPacket), infoPacket);
        }
    }

    public void setTimer(int time, InfoPacket packet, int port) {
        //If a timer is already running terminate it and start a new one
        if(timer[packet.getElevatorNum()] != null){
            timer[packet.getElevatorNum()].interrupt();
        }
        timer[packet.getElevatorNum()] = new Thread(new FaultTimer(time, packet, port));
        timer[packet.getElevatorNum()].start();
    }

    public void killTimer(int elevatorNum) {
        if (timer[elevatorNum] != null) {
            timer[elevatorNum].interrupt();
            timer[elevatorNum] = null;
        }
    }

    public void setCurrentState(SchedulerState newState, InfoPacket packet) {
        SchedulerState state = newState;
        while (state != currentState){
            System.out.println("Changing to state: "+ state);
            currentState.exit(this);
            currentState = state;
            state = state.entry(this, packet);
        }
    }

    public void sendPacket(InfoPacket packet, int sendPort){
        System.out.println("Sent: "+packet.toString()+" port: "+ sendPort);
        // Send packet
        byte[] sendData = packet.pack();
        DatagramSocket socket;
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
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

    public SchedulerState getCurrentState() {
        return currentState;
    }

    public int[] getElevatorFloor() {
        return elevatorFloor;
    }

    public void setElevatorFloor(int[] elevatorFloor) {
        this.elevatorFloor = elevatorFloor;
    }

    public ArrayList<InfoPacket>[][] getRequestQueueElevators() {
        return requestQueueElevators;
    }

    public void setRequestQueueElevators(ArrayList<InfoPacket>[][] requestQueueElevators) {
        this.requestQueueElevators = requestQueueElevators;
    }

    public ArrayList<InfoPacket>[][] getRequestQueueFloors() {
        return requestQueueFloors;
    }

    public void setRequestQueueFloors(ArrayList<InfoPacket>[][] requestQueueFloors) {
        this.requestQueueFloors = requestQueueFloors;
    }

    public int[] getElevatorDirection() {
        return elevatorDirection;
    }

    public void setElevatorDirection(int[] elevatorDirection) {
        this.elevatorDirection = elevatorDirection;
    }

    public int[] getElevatorDestination() {
        return elevatorDestination;
    }

    public void setElevatorDestination(int[] elevatorDestination) {
        this.elevatorDestination = elevatorDestination;
    }

    public int[] getElevatorOccupants() {
        return elevatorOccupants;
    }

    public void setElevatorOccupants(int[] elevatorOccupants) {
        this.elevatorOccupants = elevatorOccupants;
    }

}
