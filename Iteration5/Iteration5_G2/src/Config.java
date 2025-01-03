/**
 * The type Config.
 */
public class Config {
    /**
     * The constant numFloors. Number of floor in the building
     */
    public final static int numFloors = 21;
    /**
     * The constant numElevators. Number of elevators in the system
     */
    public final static int numElevators = 4;

    /**
     * The constant elevatorMovementTime. Time it takes the elevator to move between floors
     */
    public final static int elevatorMovementTime = 3;

    /**
     * The constant elevatorMovementTimeFault. Timeout time for an elevator to move between floors
     */
    public final static int elevatorMovementTimeFault = 4;

    /**
     * The constant doorMovementFault. Timeout time for a elevator to acknowledge a door movement command
     */
    public final static int doorMovementFault = 5;

    /**
     * The constant elevatorCapacity. The capacity of the elevator car
     */
    public final static int elevatorCapacity = 5;
    /**
     * The constant schedulerPort. Scheduler UDP port
     */
    public final static int schedulerPort = 3000;
    /**
     * The constant floorPort. Floor UDP port
     */
    public final static int floorPort = 2999;
    /**
     * The constant elevatorBasePort. Base elevator UDP port
     */
    public final static int elevatorBasePort = 3001;
}
