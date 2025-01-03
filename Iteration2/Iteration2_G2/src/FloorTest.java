import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FloorTest {

    Queue queue1;
    Queue queue2;
    Floor floor;
    @BeforeEach
    void setUp() {
        queue1 = new Queue();
        queue2 = new Queue();
        floor = new Floor(1, queue1, queue2);
    }

    @Test
    public void testFloorPut() throws InterruptedException{

        Thread thread = new Thread(floor);
        thread.start();

        // give time for floor set up
        Thread.sleep(1000);

        // assert floor has put into queue2
        assertNotNull(queue2.get());

        thread.interrupt();
    }
}
