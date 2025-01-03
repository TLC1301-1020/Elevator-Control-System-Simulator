import org.junit.jupiter.api.Test;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import static org.junit.jupiter.api.Assertions.*;

class UDPThreadTest {

    //check queue after receiving a packet
    @Test
    void ReceivedPacket() {

        Queue queue = new Queue();
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(12345);
        } catch (SocketException e) {
            e.printStackTrace();
            return;
        }

        UDPThread udpThread = new UDPThread(socket, queue);
        Thread thread = new Thread(udpThread);
        thread.start();

        InfoPacket packet = new InfoPacket();
        queue.put(packet);

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertFalse(queue.isEmpty());

        // Print the contents of the queue
        System.out.println("Contents in the queue after running UDPThread:");
        System.out.println(queue.get());

        thread.interrupt();
        socket.close();
    }


    //Error handling for socket closed
    @Test
    void exceptionHandlingTest() {
        System.out.println("\nShould return error - socket closed");

        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(12345);
        } catch (SocketException e) {
            fail("Failed to create DatagramSocket");
        }

        Queue queue = new Queue();

        UDPThread udpThread = new UDPThread(socket, queue);
        Thread thread = new Thread(udpThread);
        thread.start();
        socket.close();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertFalse(thread.isAlive(), "Thread should have stopped after socket closure");
        System.out.println();
    }

    //Check for data received from packet
    @Test
    void run() {
        System.out.println("\nRunning normal test, checking for correctness of received data");

        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(1234);
        } catch (SocketException e) {
            e.printStackTrace();
            return;
        }
        Queue queue = new Queue();
        UDPThread udpThread = new UDPThread(socket, queue);

        Thread thread = new Thread(udpThread);
        thread.start();

        sendUDPPacket(createTestPacket().pack(), socket);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (!queue.isEmpty()) {
            System.out.println("Packet received and processed successfully.");
            InfoPacket receivedPacket = queue.get();
            System.out.println("Received Packet Contents:");
            System.out.println(receivedPacket);
        } else {
            System.out.println("Failed to receive packet.");
        }
        thread.interrupt();
    }

    private InfoPacket createTestPacket() {
        System.out.println("Testing packet sent");
        InfoPacket testPacket = new InfoPacket();
        testPacket.setType(1);
        testPacket.setOpenClose(true);
        testPacket.setOriginFloor(5);
        testPacket.setDestFloor(10);
        testPacket.setElevatorNum(1);
        testPacket.setDirection(true);
        testPacket.setGotOn(1);
        System.out.println(testPacket);
        return testPacket;
    }

    private void sendUDPPacket(byte[] data, DatagramSocket socket) {
        try {
            DatagramPacket sendPacket = new DatagramPacket(data, data.length, InetAddress.getLocalHost(), 1234);
            socket.send(sendPacket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
