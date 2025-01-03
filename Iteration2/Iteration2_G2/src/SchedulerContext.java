import java.util.ArrayList;
import java.util.Arrays;

public class SchedulerContext {

    private SchedulerState currentState;
    private int elevatorFloor[];
    private int elevatorDirection[]; // 0:down 1:up 2:idle
    private ArrayList<InfoPacket>[][] requestQueueElevators; //Elevators: Floors
    private ArrayList<InfoPacket>[][] requestQueueFloors; //Floor: Direction
    private Queue sendQueue;

    public SchedulerContext(Queue sendQueue){
        int numFloors = 7;
        int numElevators = 1;
        this.sendQueue =sendQueue;
        elevatorFloor = new int[numElevators];
        elevatorDirection = new int[numElevators];
        requestQueueElevators = new ArrayList[numElevators][numFloors];
        for(int i =0; i< requestQueueElevators.length;i++){
            for(int j =0; j < requestQueueElevators[i].length;j++){
                requestQueueElevators[i][j] = new ArrayList<>();
            }
        }
        requestQueueFloors = new ArrayList[2][numFloors];
        for (int j=0; j<requestQueueFloors.length; j++){
            for (int i = 0; i < requestQueueFloors[j].length; i++) {
                requestQueueFloors[j][i] = new ArrayList<>();
            }
        }
        Arrays.fill(elevatorDirection, 2);
        currentState = new SIdleState(this);
    }

    public SchedulerState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(SchedulerState currentState) {
        this.currentState = currentState;
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

    public Queue getSendQueue() {
        return sendQueue;
    }

    public void setSendQueue(Queue sendQueue) {
        this.sendQueue = sendQueue;
    }

    public int[] getElevatorDirection() {
        return elevatorDirection;
    }

    public void setElevatorDirection(int[] elevatorDirection) {
        this.elevatorDirection = elevatorDirection;
    }
}
