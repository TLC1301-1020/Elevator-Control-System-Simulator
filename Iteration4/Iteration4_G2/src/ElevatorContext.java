import java.io.IOException;
import java.net.*;

public class ElevatorContext {
    private final int elevatorNum;

    private ElevatorState currentState;
    private int curFloor; //TODO synchronized?

    //TODO private Thread doorTimer;
    private boolean injectDoorFault = false;

    private boolean injectFloorFault = false;

    private Thread timer;

    private boolean[] floorLights;

    public ElevatorContext(int elevatorNum){
        floorLights = new boolean[Config.numFloors];
        this.elevatorNum = elevatorNum;
        curFloor = 0;

        currentState = new ElevatorState();
        //Set the current state to Entry/Exit conditions are called
        setCurrentState(new EDoorsClosed());
    }

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
        } else if (infoPacket.getType() == 3) {
            //Internal Button Press
            setCurrentState(currentState.floorRequest(this, infoPacket));
        }
    }

    public void setCurrentState(ElevatorState newState) {
        ElevatorState state = newState;
        while (state != currentState){
            currentState.exit(this);
            currentState = state;
            state = state.entry(this);
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

    public void floorArrival(int floor){
        setCurrentState(currentState.floorArrival(this, floor));
    }

    public ElevatorState getCurrentState() {
        return currentState;
    }

    public void killTimer(){
        timer.interrupt();
    }

    public void setElevatorTimer(boolean direction){
        timer = new Thread(new ElevatorTimer(this, direction));
        timer.start();
    }

    public int getElevatorNum() {
        return elevatorNum;
    }

    public int getCurFloor() {
        return curFloor;
    }

    public void setCurFloor(int curFloor) {
        this.curFloor = curFloor;
    }

    public boolean[] getFloorLights() {
        return floorLights;
    }

    public boolean isInjectDoorFault() {
        return injectDoorFault;
    }

    public void setInjectDoorFault(boolean injectDoorFault) {
        this.injectDoorFault = injectDoorFault;
    }

    public boolean isInjectFloorFault() {
        return injectFloorFault;
    }

    public void setInjectFloorFault(boolean injectFloorFault) {
        this.injectFloorFault = injectFloorFault;
    }

}
