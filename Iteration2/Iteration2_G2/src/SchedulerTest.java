import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SchedulerTest {

    /**
     * Initialize and create a scheduler for unit test.
     */
    private Queue queue1 ;
    private Queue queue2;

    private Scheduler scheduler;

    @BeforeEach
    public void setUp() {
        queue1 = new Queue();
        queue2 = new Queue();

        scheduler = new Scheduler(queue1, queue2);
    }

    /**
     * Test the scheduler to ensure it is not null.
     */
    @Test
    public void schedulerTest() {
        assertNotNull(scheduler);
    }

}
