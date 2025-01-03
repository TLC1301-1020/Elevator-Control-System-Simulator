import java.net.DatagramSocket;
import java.net.SocketException;

public class Elevator {
    private int elevatorNum;
    private Queue queueReceive;

    /**
    * To intialize an Elevator instance
    * @param elevatorNum a unique id for the elevator
    * @param queueReceive the queue that the elevator will receive message
    *
    */
    public Elevator(int elevatorNum, Queue queueReceive){
        this.elevatorNum = elevatorNum;
        this.queueReceive = queueReceive;
        run();
    }

    private void run() {
        ElevatorContext context = new ElevatorContext(elevatorNum);
        // Run the elevator thread, it continuously receives a message from the scheduler, and sends back the response message to the scheduler.
        while (true) {
            InfoPacket messageReceived = queueReceive.get();
            if (messageReceived != null) {
                // Get the message from the scheduler.
                System.out.println("Elevator " + elevatorNum + " current floor: "+ context.getCurFloor() +" state: " + context.getCurrentState() + " received message: " + messageReceived.toString());
                context.receivePacket(messageReceived);
            }
        }

    }

    public static void main(String args[]){

        Queue queueReceive = new Queue();

        //Get port num and elevator num from command line
        //Port = elevatorBasePort+elevatorNum
        int elevatorNum = Integer.parseInt(args[0]);
        int port = elevatorNum+3001;

        try {
            DatagramSocket socket = new DatagramSocket(port);
            Thread udpThread = new Thread(new UDPThread(socket, queueReceive));
            udpThread.start();
            Elevator elevator = new Elevator(elevatorNum, queueReceive);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }

    }
}