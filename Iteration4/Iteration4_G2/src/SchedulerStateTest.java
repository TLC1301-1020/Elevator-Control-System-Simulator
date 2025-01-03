//DONE
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SchedulerStateTest {

    private SchedulerContext context;
    private InfoPacket packet;

    @BeforeEach
    void setUp() {

        context = new SchedulerContext();
        packet = new InfoPacket();
    }

    @Test
    void testSchedulerStateEntry() {
        SchedulerState state = new SchedulerState();
        SchedulerState newState = state.entry(context, packet);
        assertEquals(state, newState);
    }

    @Test
    void testSchedulerStateExit() {
        SchedulerState state = new SchedulerState();
        state.exit(context);
    }

    @Test
    void testElevatorArrival() {
        SchedulerState state = new SchedulerState();
        SchedulerState newState = state.elevatorArrival(context, packet);
        //assertTrue(newState instanceof SFloorNotificationState);
    }

    @Test
    void testRequestFloor() {
        SchedulerState state = new SchedulerState();
        SchedulerState newState = state.requestFloor(context, packet);
        assertTrue(newState instanceof SFloorRequestState);
    }



}
