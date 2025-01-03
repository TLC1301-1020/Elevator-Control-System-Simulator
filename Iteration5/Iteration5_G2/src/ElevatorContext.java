import java.io.IOException;
import java.net.*;

/**
 * The type Elevator context.
 * Stores context for the elevator state machine
 */
public class ElevatorContext {
    private final int elevatorNum;

    private long lastFloorTime = 0;

    private ElevatorState currentState;
    private int curFloor;

    private boolean injectDoorOpenFault = false;
    private boolean injectDoorCloseFault = false;

    private boolean injectFloorFault = false;

    private Thread timer;

    private boolean[] floorLights;

    /**
     * Instantiates a new Elevator context.
     *
     * @param elevatorNum the elevator num
     */
    public ElevatorContext(int elevatorNum){
        floorLights = new boolean[Config.numFloors];
        this.elevatorNum = elevatorNum;
        curFloor = 0;

        currentState = new ElevatorState();
        //Set the current state to Entry/Exit conditions are called
        setCurrentState(new EDoorsClosed());
    }

    /**
     * Receive packet.
     *
     * @param infoPacket the info packet
     */
    public void receivePacket(InfoPacket infoPacket){
        if (infoPacket.getType() == 5){
            //Open/Close Doors
            if (infoPacket.isOpenClose()) {
                setCurrentState(currentState.doorsClose(this));
            } else {
                setCurrentState(currentState.doorsOpen(this));
            }
        } else if (infoPacket.getType() == 8) {
            //Floor Light off
            setCurrentState(currentState.lightsOff(this, infoPacket));
        } else if (infoPacket.getType() == 7) {
            //Start Motor Up/Down
            setCurrentState(currentState.startMotor(this, infoPacket));
        } else if (infoPacket.getType() == 6) {
            //Stop Motor
            setCurrentState(currentState.stopMotor(this));
        } else if (infoPacket.getType() == 3 || infoPacket.getType() == 15) {
            //Internal Button Press
            setCurrentState(currentState.floorRequest(this, infoPacket));
        }
    }

    /**
     * Sets current state.
     *
     * @param newState the new state
     */
    public void setCurrentState(ElevatorState newState) {
        ElevatorState state = newState;
        while (state != currentState){
            currentState.exit(this);
            currentState = state;
            state = state.entry(this);
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
     * Floor arrival.
     *
     * @param floor the floor
     */
    public void floorArrival(int floor){
        setCurrentState(currentState.floorArrival(this, floor));
    }

    /**
     * Gets current state.
     *
     * @return the current state
     */
    public ElevatorState getCurrentState() {
        return currentState;
    }

    /**
     * Kill elevator movement timeout timer.
     */
    public void killTimer(){
        timer.interrupt();
    }

    /**
     * Set elevator timer.
     *
     * @param direction the direction
     */
    public void setElevatorTimer(boolean direction){
        timer = new Thread(new ElevatorTimer(this, direction));
        timer.start();
    }

    /**
     * Gets elevator num.
     *
     * @return the elevator num
     */
    public int getElevatorNum() {
        return elevatorNum;
    }

    /**
     * Get current floor.
     *
     * @return the cur floor
     */
    public int getCurFloor() {
        return curFloor;
    }

    /**
     * Sets current floor.
     *
     * @param curFloor the cur floor
     */
    public void setCurFloor(int curFloor) {
        this.curFloor = curFloor;
    }

    /**
     * Get floor lights boolean [ ].
     *
     * @return the boolean [ ]
     */
    public boolean[] getFloorLights() {
        return floorLights;
    }

    /**
     * Is inject door open fault boolean.
     *
     * @return the boolean
     */
    public boolean isInjectDoorOpenFault() {
        return injectDoorOpenFault;
    }

    /**
     * Sets inject door open fault.
     *
     * @param injectDoorOpenFault the inject door open fault
     */
    public void setInjectDoorOpenFault(boolean injectDoorOpenFault) {
        this.injectDoorOpenFault = injectDoorOpenFault;
    }

    /**
     * Is inject door close fault boolean.
     *
     * @return the boolean
     */
    public boolean isInjectDoorCloseFault() {
        return injectDoorCloseFault;
    }

    /**
     * Sets inject door close fault.
     *
     * @param injectDoorCloseFault the inject door close fault
     */
    public void setInjectDoorCloseFault(boolean injectDoorCloseFault) {
        this.injectDoorCloseFault = injectDoorCloseFault;
    }

    /**
     * Is inject floor fault boolean.
     *
     * @return the boolean
     */
    public boolean isInjectFloorFault() {
        return injectFloorFault;
    }

    /**
     * Sets inject floor fault.
     *
     * @param injectFloorFault the inject floor fault
     */
    public void setInjectFloorFault(boolean injectFloorFault) {
        this.injectFloorFault = injectFloorFault;
    }

    /**
     * Gets last floor time.
     *
     * @return the last floor time
     */
    public long getLastFloorTime() {
        return lastFloorTime;
    }

    /**
     * Sets last floor time.
     *
     * @param lastFloorTime the last floor time
     */
    public void setLastFloorTime(long lastFloorTime) {
        this.lastFloorTime = lastFloorTime;
    }

}
