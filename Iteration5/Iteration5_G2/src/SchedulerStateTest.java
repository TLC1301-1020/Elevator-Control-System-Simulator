import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SchedulerStateTest {

    private SchedulerContext context;
    private SchedulerState schedulerState;


    @BeforeEach
    void setUp() {
        schedulerState = new SchedulerState();
        context = new SchedulerContext();

        int numElevators = 3;
        context.setElevatorDirection(new int[numElevators]);
        context.setElevatorOccupants(new int[numElevators]);
        context.setElevatorFloor(new int[numElevators]);
        context.setElevatorDestination(new int[numElevators]);

    }

    //test to verify the actual result of entry() matches the expected result
    @Test
    public void testEntry() {
        System.out.println("\nTesting entry");
        InfoPacket packet = new InfoPacket();
        SchedulerState result = schedulerState.entry(context, packet);
        System.out.println("Expected: " + schedulerState);
        System.out.println("Actual: " + result);
        assertEquals(schedulerState, result);
    }

    //test exit function, without unexpected exceptions
    @Test
    void testExit() {
        System.out.println("\nTesting on exit");
        assertDoesNotThrow(() -> schedulerState.exit(context));
    }

    //Ensures the elevatorArrival() function returns the expected state after processing the elevator arrival event
    @Test
    public void testElevatorArrival() {
        System.out.println("\nTesting Elevator Arrivals");
        InfoPacket packet = new InfoPacket();
        SchedulerState result = schedulerState.elevatorArrival(context, packet);
        System.out.println("Expected: instance of SFloorArrivalState");
        System.out.println("Actual: " + result.getClass().getSimpleName());
        assertTrue(result instanceof SFloorArrivalState);

    }
    //Test requestFloor() function correctly transitions the scheduler to the expected state after processing a request floor event
    @Test
    public void testRequestFloor() {
        System.out.println("\nTesting Request Floor");
        InfoPacket packet = new InfoPacket();
        SchedulerState result = schedulerState.requestFloor(context, packet);
        System.out.println("Expected: SFloorRequestState");
        System.out.println("Actual: " + result.getClass().getSimpleName());
        assertEquals(SFloorRequestState.class.getName(), result.getClass().getName());
    }

    //This tests timeout() function does not change the state of the scheduler
    @Test
    public void testTimeout() {
        System.out.println("\nTesting timeout");
        InfoPacket packet = new InfoPacket();
        SchedulerState result = schedulerState.timeout(context, packet);
        System.out.println("Expected: " + schedulerState);
        System.out.println("Actual: " + result);
        assertEquals(schedulerState, result);
    }


    //verifies that the doorNotification() function in the SchedulerState class behaves correctly
    @Test
    public void testDoorNotification() {
        System.out.println("Testing Door Notification");
        InfoPacket packet = new InfoPacket();
        SchedulerState result = schedulerState.doorNotification(context, packet);
        System.out.println("Expected: " + schedulerState);
        System.out.println("Actual: " + result);
        assertEquals(schedulerState, result);
    }


    //Test when a non-idle elevator arrives at the origin floor and there's a request at that floor
    @Test
    void NonIdle_Arriving() {
        int elevatorNum = 2;
        context.getElevatorDirection()[elevatorNum] = 1;
        context.getElevatorOccupants()[elevatorNum] = 3;
        int originFloor = 3;
        context.getElevatorFloor()[elevatorNum] = originFloor;

        InfoPacket packet = new InfoPacket();
        packet.setElevatorNum(elevatorNum);
        packet.setOriginFloor(originFloor);

        context.getRequestQueueFloors()[1][originFloor].add(packet);
        SchedulerState nextState = schedulerState.elevatorArrival(context, packet);
        System.out.println("Expected: SFloorArrivalState");
        System.out.println("Actual: " + nextState.getClass().getSimpleName());
        assertEquals(SFloorArrivalState.class, nextState.getClass());
    }

    //verifies the behavior of the timeout() method, when door open failure occurs
    @Test
    void testTimeout_DoorOpenFailure() {
        int elevatorNum = 3;
        int doorOpenTimeoutType = 12;
        InfoPacket packet = new InfoPacket();
        packet.setElevatorNum(elevatorNum);
        packet.setType(doorOpenTimeoutType);

        SchedulerState nextState = schedulerState.timeout(context, packet);
        System.out.println("Expected: " + schedulerState);
        System.out.println("Actual: " + nextState);
        assertEquals(schedulerState, nextState);
    }

    // Test when an elevator arrives at the origin floor and it's idle
    @Test
    void testElevatorArrival_IdleElevator() {

        int elevatorNum = 0;
        context.getElevatorDirection()[elevatorNum] = 0;
        context.getElevatorOccupants()[elevatorNum] = 0;
        int originFloor = 5;
        context.getElevatorFloor()[elevatorNum] = originFloor;
        context.getElevatorDestination()[elevatorNum] = originFloor;

        InfoPacket packet = new InfoPacket();
        packet.setElevatorNum(elevatorNum);
        packet.setOriginFloor(originFloor);

        SchedulerState nextState = schedulerState.elevatorArrival(context, packet);
        System.out.println("Expected: SFloorArrivalState");
        System.out.println("Actual: " + nextState.getClass().getSimpleName());
        assertEquals(SFloorArrivalState.class, nextState.getClass());
    }

}
