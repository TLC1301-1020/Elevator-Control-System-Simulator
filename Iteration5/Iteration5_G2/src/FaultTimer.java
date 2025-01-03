import java.io.IOException;
import java.net.*;

/**
 * The type Fault timer.
 * Time that sends a packet on timeout indicating a fault has occurred
 */
public class FaultTimer implements Runnable{

    private int time;

    private int port;
    private InfoPacket packet;

    /**
     * Instantiates a new Fault timer.
     *
     * @param time   the time
     * @param packet the packet to send on timeout
     * @param port   the port
     */
    public FaultTimer(int time, InfoPacket packet, int port){
        this.time = time;
        this.packet = packet;
        this.port = port;
    }

    @Override
    public void run() {
        //Wait the amount of seconds then generate a timeout event
        try {
            Thread.sleep(1000*time);
            sendPacket(packet, port);
        } catch (InterruptedException e) {
            //Return if interrupted
        }
    }

    /**
     * Send packet.
     *
     * @param packet   the packet
     * @param sendPort the send port
     */
    public void sendPacket(InfoPacket packet, int sendPort){
        System.out.println("Timeout packet Sent: "+packet.toString()+" port: "+ sendPort);
        // Send packet
        byte[] sendData = packet.pack();
        DatagramSocket socket;
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
        try {
            InetAddress sendAddress = InetAddress.getLocalHost();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, sendAddress, sendPort);
            try {
                socket.send(sendPacket);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }
}
