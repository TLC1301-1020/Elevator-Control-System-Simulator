//DONE
import org.junit.jupiter.api.Test;

import java.net.DatagramSocket;
import java.net.SocketException;

import static org.junit.jupiter.api.Assertions.*;


public class FloorTest {

    @Test
    void testFloorRun() {
        Queue schedulerQueue = new Queue();
        Queue elevatorQueue = new Queue();
        try {
            DatagramSocket socket1 = new DatagramSocket(Config.schedulerPort);
            DatagramSocket socket2 = new DatagramSocket(Config.elevatorBasePort);

            Thread thread1 = new Thread(new UDPThread(socket1, schedulerQueue));
            Thread thread2 = new Thread(new UDPThread(socket2, elevatorQueue));

            // Create a Floor instance
            Thread thread3 = new Thread() {
                public void run() {
                    Floor floor = new Floor(new Queue());
                }
            };
            thread3.start();


            // Wait for some time to allow the floor to process events
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Check if the queues contain expected packets
            assertTrue(queueContainsPacketOfType(schedulerQueue, 1));
            assertTrue(queueContainsPacketOfType(elevatorQueue, 3));

            thread3.interrupt();
            thread1.interrupt();
            thread2.interrupt();

        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }

    // Helper method, check if a queue contains a packet of a specific type
    private boolean queueContainsPacketOfType(Queue queue, int type) {
        while (!queue.isEmpty()) {
            InfoPacket packet = queue.get();
            if (packet.getType() == type) {
                return true;
            }
        }
        return false;
    }
}
