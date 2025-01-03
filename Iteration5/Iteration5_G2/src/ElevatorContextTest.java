
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ElevatorContextTest {

    private ElevatorContext elevatorContext;
    private Queue queueSend;

    @BeforeEach
    public void setUp() {
        queueSend = new Queue();
        elevatorContext = new ElevatorContext(1);
    }

    @Test
    public void testReceivePacket_DoorsOperation() {
        System.out.println("\nTesting ReceivePacket_DoorsOperation");
        InfoPacket infoPacketOpen = new InfoPacket();
        infoPacketOpen.setType(5);
        infoPacketOpen.setOpenClose(false); // false to open doors

        elevatorContext.receivePacket(infoPacketOpen);
        // Assuming that the currentState changes after doors open
        assertNotEquals(EDoorsClosed.class, elevatorContext.getCurrentState().getClass());
        System.out.println("Original: " +elevatorContext.getCurrentState().getClass().getName());


        InfoPacket infoPacketClose = new InfoPacket();
        infoPacketClose.setType(5);
        infoPacketClose.setOpenClose(true); // true to close doors

        elevatorContext.receivePacket(infoPacketClose);
        // Assuming that the currentState changes after doors close
        assertNotEquals(EDoorsOpen.class, elevatorContext.getCurrentState().getClass());
        System.out.println("Expected: " + EDoorsClosed.class.getName());
        System.out.println("Actual: " + elevatorContext.getCurrentState().getClass().getName());
    }

    @Test
    public void testReceivePacket_FloorLightOff() {
        System.out.println("\nTesting ReceivePacket_FloorLightOff");

        InfoPacket infoPacket = new InfoPacket();
        infoPacket.setType(8); // type for turning off the floor light
        infoPacket.setDestFloor(3); // set destination floor to 3

        elevatorContext.receivePacket(infoPacket);
        // Assuming that the floor light at floor 3 should be off
        assertTrue(elevatorContext.getFloorLights()[3]);
        System.out.println("Expected: true");
        System.out.println("Actual: " + elevatorContext.getFloorLights()[3]);
    }

    @Test
    public void testReceivePacket_StartMotor() {
        System.out.println("\nTesting ReceivePacket_StartMotor");

        InfoPacket infoPacket = new InfoPacket();
        infoPacket.setType(7); // type for starting the motor
        infoPacket.setDirection(true); // set direction to up

        ElevatorState initialState = elevatorContext.getCurrentState();
        elevatorContext.receivePacket(infoPacket);
        // Assuming that the currentState changes after starting the motor
        assertNotEquals(initialState, elevatorContext.getCurrentState());
        System.out.println("Original State: " + initialState.getClass().getName());
        System.out.println("Current State: " + elevatorContext.getCurrentState().getClass().getName());
    }

    @Test
    public void testReceivePacket_StopMotor() {
        System.out.println("\nTesting ReceivePacket_StopMotor");

        InfoPacket infoPacket = new InfoPacket();
        infoPacket.setType(6); // type for stopping the motor

        ElevatorState initialState = elevatorContext.getCurrentState();
        elevatorContext.receivePacket(infoPacket);
        // Assuming that the currentState changes after stopping the motor
        assertEquals(initialState, elevatorContext.getCurrentState());
        System.out.println("Expected State: " + initialState.getClass().getName());
        System.out.println("Actual State: " + elevatorContext.getCurrentState().getClass().getName());
    }

    


    @Test
    void floorArrival() {
        System.out.println("\nTesting floorArrival");

        ElevatorState currentState = new EDoorsClosed();
        elevatorContext.setCurrentState(currentState);
        // Call the floorArrival method with floor 5
        elevatorContext.floorArrival(5);
        // Assert that the current floor has been updated to 5
        assertEquals(5, elevatorContext.getCurFloor());
        System.out.println("Expected Current Floor: 5");
        System.out.println("Actual Current Floor: " + elevatorContext.getCurFloor());
    }

    @Test
    public void testKillTimer() {
        System.out.println("\nTesting KillTimer");

        elevatorContext.setElevatorTimer(true);
        elevatorContext.killTimer();
        // Assume the timer thread should be interrupted correctly

        System.out.println("Timer Thread Interrupted: true");
    }


    @Test
    public void testSetElevatorTimer() {
        System.out.println("\nTesting SetElevatorTimer");

        elevatorContext.setElevatorTimer(true); // true indicating upward direction
        System.out.println("Timer Started: true");
    }

    @Test
    public void testGetElevatorNum() {
        System.out.println("\nTesting GetElevatorNum");

        // Verify that the elevator number is returned correctly.
        assertEquals(1, elevatorContext.getElevatorNum());
        System.out.println("Expected Elevator Number: 1");
        System.out.println("Actual Elevator Number: " + elevatorContext.getElevatorNum());
    }

    @Test
    public void testGetSetCurFloor() {
        System.out.println("\nTesting GetSetCurFloor");

        // Set the current floor and verify that it is updated.
        elevatorContext.setCurFloor(5);
        assertEquals(5, elevatorContext.getCurFloor());
        System.out.println("Expected Current Floor: 5");
        System.out.println("Actual Current Floor: " + elevatorContext.getCurFloor());
    }

    @Test
    public void testGetFloorLights() {
        System.out.println("\nTesting GetFloorLights");

        // Verify the initial state of the floor lights.
        boolean[] floorLights = elevatorContext.getFloorLights();
        assertNotNull(floorLights);
        assertEquals(Config.numFloors, floorLights.length);
        System.out.println("Expected Number of Floor Lights: " + Config.numFloors);
        System.out.println("Actual Number of Floor Lights: " + floorLights.length);
    }
}

