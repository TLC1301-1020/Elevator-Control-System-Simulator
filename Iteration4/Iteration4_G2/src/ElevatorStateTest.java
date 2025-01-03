import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ElevatorStateTest {

    private ElevatorState state;
    private ElevatorContext context;
    private Queue queueSend;

    @BeforeEach
    public void setUp() {
        queueSend = new Queue();
        context = new ElevatorContext(1);
        state = new ElevatorState();
    }

    @Test
    public void testEntry() {
        // Entry should return the same state in this base class
        assertSame(state, state.entry(context));
    }

    @Test
    public void testExit() {
        // Exit method has no return value and no specified behavior, so we ensure it does not throw an exception
        assertDoesNotThrow(() -> state.exit(context));
    }

    @Test
    public void testLightsOff() {
        // LightsOff method should turn on the light for the specified destination floor, which seems incorrect, but we'll test the current behavior
        InfoPacket infoPacket = new InfoPacket();
        infoPacket.setDestFloor(2);
        state.lightsOff(context, infoPacket);
        assertTrue(context.getFloorLights()[2]);
    }

    @Test
    public void testDoorsOpen() {
        // DoorsOpen method should return the same state in this base class
        assertSame(state, state.doorsOpen(context));
    }

    @Test
    public void testDoorsClose() {
        // DoorsClose method should return the same state in this base class
        assertSame(state, state.doorsClose(context));
    }

    @Test
    public void testStopMotor() {
        // StopMotor method should return the same state in this base class
        assertSame(state, state.stopMotor(context));
    }

    @Test
    public void testStartMotor() {
        // StartMotor method should return the same state in this base class
        InfoPacket infoPacket = new InfoPacket();
        assertSame(state, state.startMotor(context, infoPacket));
    }

    @Test
    public void testFloorArrival() {
        // FloorArrival should update the current floor and queue a packet
        int floor = 3;
        state.floorArrival(context, floor);
        assertEquals(floor, context.getCurFloor());
        assertFalse(queueSend.isEmpty());
        InfoPacket queuedPacket = queueSend.get();
        assertEquals(4, queuedPacket.getType());
        assertEquals(floor, queuedPacket.getOriginFloor());
    }

    @Test
    public void testFloorRequest() {
        // FloorRequest should turn on the light for the requested floor and queue a packet
        int requestedFloor = 4;
        InfoPacket infoPacket = new InfoPacket();
        infoPacket.setDestFloor(requestedFloor);
        state.floorRequest(context, infoPacket);
        assertTrue(context.getFloorLights()[requestedFloor]);
        assertFalse(queueSend.isEmpty());
        InfoPacket queuedPacket = queueSend.get();
        assertEquals(2, queuedPacket.getType());
        assertEquals(requestedFloor, queuedPacket.getDestFloor());
    }
}
