import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class QueueTest {

    @Test
    public void testQueue() throws InterruptedException {

        Queue queue = new Queue();

        // Create test data
        InfoPacket testData = new InfoPacket();
        testData.setType(1);

        queue.put(testData);
        InfoPacket retrievedData = queue.get();

        // Check if the retrieved data is the same as the original test data
        assertEquals(1, retrievedData.getType());

        // empty the queue
        assertNull(queue.get());
    }
}
