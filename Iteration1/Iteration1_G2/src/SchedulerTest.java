import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SchedulerTest {

    /**
     * Initialize and create a scheduler for unit test.
     */
    private Queue queue1 ;
    private Queue queue2;
    private Queue queue3;
    private Queue queue4;

    private Scheduler scheduler;

    @BeforeEach
    public void setUp() {
        queue1 = new Queue();
        queue2 = new Queue();
        queue3 = new Queue();
        queue4 = new Queue();

        scheduler = new Scheduler(queue1, queue2, queue3, queue4);
    }

    /**
     * Test the scheduler to ensure it is not null.
     */
    @Test
    public void schedulerTest() {
        assertNotNull(scheduler);
    }


}
