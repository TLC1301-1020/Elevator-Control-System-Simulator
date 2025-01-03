import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPThread implements Runnable {
    private DatagramSocket socket;
    private Queue queue;

    public UDPThread(DatagramSocket socket, Queue queue) {
        this.socket = socket;
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            while (true) {
                // Receive UDP packet
                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                socket.receive(receivePacket);

                // Process the received data and save to the queue
                InfoPacket receivedInfoPacket = new InfoPacket();
                receivedInfoPacket.unpack(receivePacket.getData());
                queue.put(receivedInfoPacket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
