import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import static org.junit.jupiter.api.Assertions.*;

class UDPThreadTest {

    private DatagramSocket socket; // Socket to be used by UDPThread for listening to incoming packets
    private Queue queue; // Queue to hold processed packets by UDPThread

    @BeforeEach
    void setUp() throws Exception {
        // Set up a DatagramSocket and Queue before each test
        // The DatagramSocket is bound to any available port on the local host
        socket = new DatagramSocket();
        queue = new Queue();
    }

    @Test
    void testUDPThreadReceivingAndSending() throws Exception {
        // Create an instance of UDPThread with the test socket and queue
        UDPThread udpThread = new UDPThread(socket, queue);
        // Start the UDPThread in a new thread
        Thread thread = new Thread(udpThread);
        thread.start();

        // Prepare a test message to send to the UDPThread
        String testMessage = "Test message";
        byte[] sendData = testMessage.getBytes();
        InetAddress address = InetAddress.getLocalHost(); // Get the local host address
        // Create a DatagramPacket with the test message, targeting the local host and the port the socket is listening on
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, socket.getLocalPort());

        // Send the packet using a new DatagramSocket
        DatagramSocket senderSocket = new DatagramSocket();
        senderSocket.send(sendPacket);

        // Wait briefly to give the UDPThread time to receive and process the packet
        Thread.sleep(1000); // Adjust this timing based on expected processing time

        // After waiting, check if the packet was processed by verifying the queue is not empty
        assertFalse(queue.isEmpty(), "Queue should not be empty after receiving a packet");

        // Stop the UDPThread to clean up after the test
        // This is important to prevent the thread from running indefinitely
        thread.interrupt();
        // Close the sockets to release the network resources used by the test
        socket.close();
        senderSocket.close();
    }
}
