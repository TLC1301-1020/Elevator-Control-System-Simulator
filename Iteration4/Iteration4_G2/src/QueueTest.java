//DONE


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class QueueTest {

    @Test
    public void testQueue() {
        System.out.println("QueueTest - testQueue");

        Queue queue = new Queue();
        InfoPacket infoPacket = new InfoPacket();
        infoPacket.setType(1);

        queue.put(infoPacket);

        InfoPacket receivedPacket = queue.get();
        System.out.println("Received InfoPacket: " + receivedPacket.toString());

        assertEquals(infoPacket, receivedPacket);

        System.out.println("QueueTest - testQueue Passed");
    }
}
