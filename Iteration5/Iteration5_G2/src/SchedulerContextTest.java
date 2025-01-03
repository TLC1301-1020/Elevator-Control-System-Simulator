//DONE
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SchedulerContextTest {

    private SchedulerContext schedulerContext;

    @BeforeEach
    public void setUp() {
        schedulerContext = new SchedulerContext();
    }

    @Test
    public void testReceivePacket_FloorRequest() {
        InfoPacket infoPacket = new InfoPacket();
        infoPacket.setType(1);
        infoPacket.setOriginFloor(3);
        infoPacket.setDirection(true);

        SchedulerState currentState = schedulerContext.getCurrentState();
        int initialQueueSize = schedulerContext.getRequestQueueFloors()[1][3].size();

        schedulerContext.receivePacket(infoPacket);
        // Ensure state has changed
        assertNotEquals(currentState, schedulerContext.getCurrentState());
        // Ensure packet is added to queue
        assertEquals(initialQueueSize + 1, schedulerContext.getRequestQueueFloors()[1][3].size());
    }

    @Test
    void testElevatorDirection() {
        SchedulerContext context = new SchedulerContext();
        int[] elevatorDirection = {1, 0, 1};
        context.setElevatorDirection(elevatorDirection);
        assertEquals(elevatorDirection, context.getElevatorDirection());
    }
}
