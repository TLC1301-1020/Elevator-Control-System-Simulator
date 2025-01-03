import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class QueueTest {

    @Test
    public void testQueue() throws InterruptedException {

        Queue queue = new Queue();

        // Create test data
        InfoPacket testData = new InfoPacket();
        testData.setMsg("Test Message");

        queue.put(testData);
        InfoPacket retrievedData = queue.get();

        // Check if the retrieved data is the same as the original test data
        assertEquals("Test Message", retrievedData.getMsg());

        // empty the queue
        assertNull(queue.get());
    }
}
