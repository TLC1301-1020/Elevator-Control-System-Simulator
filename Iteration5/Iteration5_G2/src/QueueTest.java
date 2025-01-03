import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import static org.junit.jupiter.api.Assertions.*;

public class QueueTest {

    private Queue queue;

    @BeforeEach
    void setUp() {
        queue = new Queue();
    }


    //verify the behavior of the put and get methods of the Queue
    @Test
    void testPutAndGet() {
        System.out.println("\nTesting put and get");
        InfoPacket packet1 = new InfoPacket();
        queue.put(packet1);
        assertFalse(queue.isEmpty());
        InfoPacket retrievedPacket = queue.get();
        assertTrue(queue.isEmpty());

        System.out.println("Retrieved Packet: " + retrievedPacket);
        assertEquals(packet1, retrievedPacket);
    }

    //testing isEmpty function
    @Test
    void testIsEmpty() {
        System.out.println("Testing empty queue");
        assertTrue(queue.isEmpty());
        InfoPacket packet = new InfoPacket();
        queue.put(packet);
        assertFalse(queue.isEmpty());

        System.out.println("Is Empty after putting packet: " + queue.isEmpty());
    }

    //test the concurrency behavior of Queue
    @Test
    void testConcurrency() throws InterruptedException {
        System.out.println("\nTesting concurrency");
        int numTasks = 1000;
        ExecutorService executor = Executors.newFixedThreadPool(10);
        for (int i = 0; i < numTasks; i++) {
            executor.submit(() -> {
                queue.put(new InfoPacket());
            });
        }
        for (int i = 0; i < numTasks; i++) {
            executor.submit(() -> {
                queue.get();
            });
        }
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);
        assertTrue(queue.isEmpty());
        System.out.println("Queue is empty after concurrency test: " + queue.isEmpty());
    }

    //put and get test for multiple packets
    @Test
    void testPutAndGetWithMultiplePackets() {
        System.out.println("Testing put and get with multiple packets");
        InfoPacket packet1 = new InfoPacket();
        InfoPacket packet2 = new InfoPacket();
        InfoPacket packet3 = new InfoPacket();

        queue.put(packet1);
        queue.put(packet2);
        queue.put(packet3);

        assertFalse(queue.isEmpty());

        InfoPacket retrievedPacket1 = queue.get();
        InfoPacket retrievedPacket2 = queue.get();
        InfoPacket retrievedPacket3 = queue.get();

        assertTrue(queue.isEmpty());

        assertEquals(packet1, retrievedPacket1);
        assertEquals(packet2, retrievedPacket2);
        assertEquals(packet3, retrievedPacket3);

        System.out.println("Retrieved Packets: \n" + retrievedPacket1 + "\n" + retrievedPacket2 + "\n" + retrievedPacket3);
    }


}
