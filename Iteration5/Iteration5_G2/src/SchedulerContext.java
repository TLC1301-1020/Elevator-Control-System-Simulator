import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * The type Scheduler context.
 * Stores context for the Scheduler state machine
 */
public class SchedulerContext {

    private SchedulerState currentState;
    private int[] elevatorFloor;

    private int[] elevatorOccupants;

    private Thread[] timer;
    private int[] elevatorDirection; // 0:down 1:up 2:idle
    private int[] elevatorDestination;
    private ArrayList<InfoPacket>[][] requestQueueElevators; //Elevators: Floors
    private ArrayList<InfoPacket>[][] requestQueueFloors; //Direction: Floor
    private boolean[] doorFault;

    /**
     * Instantiates a new Scheduler context.
     */
    public SchedulerContext(){
        elevatorFloor = new int[Config.numElevators];
        elevatorDirection = new int[Config.numElevators];
        elevatorDestination = new int[Config.numElevators];
        elevatorOccupants = new int[Config.numElevators];
        doorFault = new boolean[Config.numElevators];
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

    /**
     * Receive packet.
     *
     * @param infoPacket the info packet
     */
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

    /**
     * Sets timer.
     *
     * @param time   the time
     * @param packet the packet
     * @param port   the port
     */
    public void setTimer(int time, InfoPacket packet, int port) {
        if (packet.getType() == 11 || packet.getType() == 12) {
            setDoorFaultStatus(packet.getElevatorNum());
        }

        //If a timer is already running terminate it and start a new one
        if(timer[packet.getElevatorNum()] != null){
            timer[packet.getElevatorNum()].interrupt();
        }
        timer[packet.getElevatorNum()] = new Thread(new FaultTimer(time, packet, port));
        timer[packet.getElevatorNum()].start();
    }

    /**
     * Get timer.
     *
     * @param elevatorNum the elevator num
     */
    public Thread getTimer(int elevatorNum) {
        return timer[elevatorNum];
    }

    /**
     * Kill timer.
     *
     * @param elevatorNum the elevator num
     */
    public void killTimer(int elevatorNum) {
        if (timer[elevatorNum] != null) {
            timer[elevatorNum].interrupt();
            timer[elevatorNum] = null;
        }
    }

    /**
     * Sets current state.
     *
     * @param newState the new state
     * @param packet   the packet
     */
    public void setCurrentState(SchedulerState newState, InfoPacket packet) {
        SchedulerState state = newState;
        while (state != currentState){
            System.out.println("Changing to state: "+ state);
            currentState.exit(this);
            currentState = state;
            state = state.entry(this, packet);
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

    /**
     * Gets current state.
     *
     * @return the current state
     */
    public SchedulerState getCurrentState() {
        return currentState;
    }

    /**
     * Get elevator floor int [ ].
     *
     * @return the int [ ]
     */
    public int[] getElevatorFloor() {
        return elevatorFloor;
    }

    /**
     * Sets elevator floor.
     *
     * @param elevatorFloor the elevator floor
     */
    public void setElevatorFloor(int[] elevatorFloor) {
        this.elevatorFloor = elevatorFloor;
    }

    /**
     * Get request queue elevators array list [ ] [ ].
     *
     * @return the array list [ ] [ ]
     */
    public ArrayList<InfoPacket>[][] getRequestQueueElevators() {
        return requestQueueElevators;
    }

    /**
     * Sets request queue elevators.
     *
     * @param requestQueueElevators the request queue elevators
     */
    public void setRequestQueueElevators(ArrayList<InfoPacket>[][] requestQueueElevators) {
        this.requestQueueElevators = requestQueueElevators;
    }

    /**
     * Get request queue floors array list [ ] [ ].
     *
     * @return the array list [ ] [ ]
     */
    public ArrayList<InfoPacket>[][] getRequestQueueFloors() {
        return requestQueueFloors;
    }

    /**
     * Sets request queue floors.
     *
     * @param requestQueueFloors the request queue floors
     */
    public void setRequestQueueFloors(ArrayList<InfoPacket>[][] requestQueueFloors) {
        this.requestQueueFloors = requestQueueFloors;
    }

    /**
     * Get elevator direction int [ ].
     *
     * @return the int [ ]
     */
    public int[] getElevatorDirection() {
        return elevatorDirection;
    }

    /**
     * Sets elevator direction.
     *
     * @param elevatorDirection the elevator direction
     */
    public void setElevatorDirection(int[] elevatorDirection) {
        this.elevatorDirection = elevatorDirection;
    }

    /**
     * Get elevator destination int [ ].
     *
     * @return the int [ ]
     */
    public int[] getElevatorDestination() {
        return elevatorDestination;
    }

    /**
     * Sets elevator destination.
     *
     * @param elevatorDestination the elevator destination
     */
    public void setElevatorDestination(int[] elevatorDestination) {
        this.elevatorDestination = elevatorDestination;
    }

    /**
     * Get elevator occupants int [ ].
     *
     * @return the int [ ]
     */
    public int[] getElevatorOccupants() {
        return elevatorOccupants;
    }

    /**
     * Sets elevator occupants.
     *
     * @param elevatorOccupants the elevator occupants
     */
    public void setElevatorOccupants(int[] elevatorOccupants) {
        this.elevatorOccupants = elevatorOccupants;
    }

    /**
     * Gets door fault status.
     */
    public boolean[] getDoorFaultStatus() {
        return doorFault;
    }

    /**
     * Sets door fault status to false.
     */
    public void resetDoorFaultStatus(int elevatorNum) { doorFault[elevatorNum] = false; }

    /**
     * Sets door fault status to true.
     */
    public void setDoorFaultStatus(int elevatorNum) { doorFault[elevatorNum] = true; }

}
