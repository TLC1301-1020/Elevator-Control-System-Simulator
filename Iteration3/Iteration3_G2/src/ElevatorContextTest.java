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
        InfoPacket infoPacketOpen = new InfoPacket();
        infoPacketOpen.setType(5);
        infoPacketOpen.setOpenClose(false); // false to open doors

        elevatorContext.receivePacket(infoPacketOpen);
        // Assuming that the currentState changes after doors open
        assertNotEquals(EDoorsClosed.class, elevatorContext.getCurrentState().getClass());

        InfoPacket infoPacketClose = new InfoPacket();
        infoPacketClose.setType(5);
        infoPacketClose.setOpenClose(true); // true to close doors

        elevatorContext.receivePacket(infoPacketClose);
        // Assuming that the currentState changes after doors close
        assertNotEquals(EDoorsOpen.class, elevatorContext.getCurrentState().getClass());
    }

    @Test
    public void testReceivePacket_FloorLightOff() {
        InfoPacket infoPacket = new InfoPacket();
        infoPacket.setType(8); // type for turning off the floor light
        infoPacket.setDestFloor(3); // set destination floor to 3

        elevatorContext.receivePacket(infoPacket);
        // Assuming that the floor light at floor 3 should be off
        assertFalse(elevatorContext.getFloorLights()[3]);
    }

    @Test
    public void testReceivePacket_StartMotor() {
        InfoPacket infoPacket = new InfoPacket();
        infoPacket.setType(7); // type for starting the motor
        infoPacket.setDirection(true); // set direction to up

        ElevatorState initialState = elevatorContext.getCurrentState();
        elevatorContext.receivePacket(infoPacket);
        // Assuming that the currentState changes after starting the motor
        assertNotEquals(initialState, elevatorContext.getCurrentState());
    }

    @Test
    public void testReceivePacket_StopMotor() {
        InfoPacket infoPacket = new InfoPacket();
        infoPacket.setType(6); // type for stopping the motor

        ElevatorState initialState = elevatorContext.getCurrentState();
        elevatorContext.receivePacket(infoPacket);
        // Assuming that the currentState changes after stopping the motor
        assertNotEquals(initialState, elevatorContext.getCurrentState());
    }

    @Test
    public void testReceivePacket_FloorRequest() {
        InfoPacket infoPacket = new InfoPacket();
        infoPacket.setType(3); // type for internal button press
        infoPacket.setDestFloor(2); // set destination floor to 2

        elevatorContext.receivePacket(infoPacket);
        // Assuming the queue should have one packet after floor request
        assertFalse(queueSend.isEmpty());
        assertEquals(2, queueSend.get().getDestFloor());
    }

    @Test
    public void testFloorArrival() {
        int arrivalFloor = 3;
        elevatorContext.floorArrival(arrivalFloor);
        assertEquals(arrivalFloor, elevatorContext.getCurFloor());
        // Assuming the queue should have one packet after floor arrival
        assertFalse(queueSend.isEmpty());
        assertEquals(4, queueSend.get().getType());
    }

    @Test
    public void testKillTimer() {
        elevatorContext.setElevatorTimer(true);
        elevatorContext.killTimer();
        // We assume the timer thread should be interrupted correctly
        // Further inspection is needed to verify that the timer thread was actually interrupted
    }


    @Test
    public void testSetElevatorTimer() {
        elevatorContext.setElevatorTimer(true); // true indicating upward direction, for example
        // Assert that a timer thread has started.
        // This is difficult to test directly without a way to inspect the thread.
        // You would need to expose some internal state of ElevatorTimer or use a mocking framework.
    }

    @Test
    public void testGetElevatorNum() {
        // Verify that the elevator number is returned correctly.
        assertEquals(1, elevatorContext.getElevatorNum());
    }

    @Test
    public void testGetSetCurFloor() {
        // Set the current floor and verify that it is updated.
        elevatorContext.setCurFloor(5);
        assertEquals(5, elevatorContext.getCurFloor());
    }

    @Test
    public void testGetFloorLights() {
        // Verify the initial state of the floor lights.
        boolean[] floorLights = elevatorContext.getFloorLights();
        assertNotNull(floorLights);
        assertEquals(Config.numFloors, floorLights.length);
        // You could further test behavior like turning lights on/off if such methods are available.
    }



}
