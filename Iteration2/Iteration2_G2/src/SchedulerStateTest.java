import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SchedulerStateTest {

    // Initialize instances
    private SchedulerContext context;
    private SchedulerState schedulerState;

    @BeforeEach
    public void setUp() {
        //Schedulercontext with new Queue
        context = new SchedulerContext(new Queue());
        schedulerState = new SchedulerState(context);
    }

    @Test
    public void schedulerStateTest() {
        assertNotNull(schedulerState);
    }
}
