import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ElevatorStateTest {

    private ElevatorContext context;
    private ElevatorState elevatorState;
    private Queue queueSend;


    @BeforeEach
    void setUp() {
        queueSend = new Queue();
        context = new ElevatorContext(1);
        elevatorState = new ElevatorState();
    }

    @Test
    void testEntry() {
        System.out.println("\nTesting Entry state");
        assertSame(elevatorState, elevatorState.entry(context));
        System.out.println("Expected State: " + elevatorState);
        System.out.println("Actual State: " + elevatorState.entry(context));
    }

    @Test
    void testExit() {
        System.out.println("\nTesting Exit state");
        assertDoesNotThrow(() -> elevatorState.exit(context));
        System.out.println("Expected: No exceptions thrown");
        System.out.println("Actual: No exceptions thrown");
    }

    @Test
    void testLightsOff() {
        System.out.println("\nTesting Lights off");
        InfoPacket infoPacket = new InfoPacket();
        infoPacket.setDestFloor(2);
        elevatorState.lightsOff(context, infoPacket);

        assertTrue(context.getFloorLights()[2]);
        System.out.println("Expected: true");
        System.out.println("Actual: " + context.getFloorLights()[2]);
    }

    @Test
    void testDoorsOpen() {
        System.out.println("\nTesting doors open");
        ElevatorState newState = elevatorState.doorsOpen(context);
        assertEquals(elevatorState, newState);
        System.out.println("Expected State: " + elevatorState);
        System.out.println("Actual State: " + newState);
    }

    @Test
    void testDoorsClose() {
        System.out.println("\nTesting doors close");
        ElevatorState newState = elevatorState.doorsClose(context);
        assertEquals(elevatorState, newState);
        System.out.println("Expected State: " + elevatorState);
        System.out.println("Actual State: " + newState);
    }

    @Test
    void testStopMotor() {
        System.out.println("\nTesting stop motor");
        System.out.println("Initial State: " + elevatorState); // Print initial state
        InfoPacket infoPacket = new InfoPacket();
        ElevatorState newState = elevatorState.startMotor(context, infoPacket);
        assertEquals(elevatorState, newState);
        System.out.println("Expected State: " + elevatorState); // Print expected state (same as initial state)
        System.out.println("Actual State: " + newState);        // Print actual state (same as initial state)
    }


    @Test
    void testStartMotor() {
        System.out.println("\nTesting start motor");
        System.out.println("Initial State: " + elevatorState); // Print initial state
        InfoPacket infoPacket = new InfoPacket();
        ElevatorState newState = elevatorState.startMotor(context, infoPacket);
        assertEquals(elevatorState, newState);
        System.out.println("Expected State: " + elevatorState); // Print expected state (same as initial state)
        System.out.println("Actual State: " + newState);        // Print actual state (same as initial state)
    }

    @Test
    public void testFloorArrival() {
        System.out.println("\nTesting  3 floor arrival ");
        int floor = 3;
        long currentTime = System.currentTimeMillis();
        context.setLastFloorTime(currentTime);
        ElevatorState newState = elevatorState.floorArrival(context, floor);
        assertEquals(elevatorState, newState);
        assertEquals(floor, context.getCurFloor());
        System.out.println("Expected: 3 "+ elevatorState);
        System.out.println("Actual: " + context.getCurFloor()+" " + newState);
    }

    @Test
    public void testFloorRequest() {
        System.out.println("\nTesting floor request");
        InfoPacket infoPacket = new InfoPacket();
        infoPacket.setDestFloor(7);
        ElevatorState newState = elevatorState.floorRequest(context, infoPacket);
        assertEquals(elevatorState, newState);
        assertTrue(context.getFloorLights()[7]);
        System.out.println("Expected: true");
        System.out.println("Actual: " + context.getFloorLights()[7]);
    }

    

}
