import javax.swing.*;
import java.net.DatagramSocket;
import java.net.SocketException;

/*
* Scheduler
* Takes InfoPackets from the Floor and passes them to the scheduler and back
 */
public class Scheduler {

    private Queue queueReceive;

    private ElevatorSystemGUI gui;

    public Scheduler(Queue receive){
        //gui = new ElevatorSystemGUI();
        queueReceive = receive;
        run();
    }

    private void run() {
        InfoPacket currentData;
        SchedulerContext schedulerContext = new SchedulerContext();
        //SwingUtilities.invokeLater(ElevatorSystemGUI::createGUI);
        //Check for incoming events
        while(true){
            currentData = queueReceive.get();
            System.out.println("Scheduler state: "+ schedulerContext.getCurrentState() + " received: " + currentData.toString());
            //updateGUI(currentData);
            schedulerContext.receivePacket(currentData);
        }
    }

    public static void main(String args[]){

        Queue queueReceive = new Queue();

        try {
            DatagramSocket socket = new DatagramSocket(3000);
            Thread udpThread = new Thread(new UDPThread(socket, queueReceive));
            udpThread.start();
            Scheduler scheduler = new Scheduler(queueReceive);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }

    }

    private void updateGUI(InfoPacket msg) {

        // update direction
        Boolean currentDirection = null; // Use the Boolean wrapper class to allow null

        try {
            currentDirection = msg.isDirection();
        } catch (NullPointerException e) {
            // currentDirection remains null if an exception is caught
        }

        String currentDirectionString;
        if (currentDirection == null) {
            currentDirectionString = "Idle"; // Handle the null case
        } else if (currentDirection) {
            currentDirectionString = "Up"; // True case
        } else {
            currentDirectionString = "Down"; // False case, no need for (!currentDirection)
        }
        gui.updateDirection(currentDirectionString);

        // update destination floor
        int destinationFloor = msg.getDestFloor();
        gui.updateDestFloorStatus(destinationFloor);

        // update origin floor
        int originFloor = msg.getOriginFloor();
        gui.updateOriginFloorStatus(originFloor);

        // update doors
        Boolean doors = null;
        String doorsStatus;

        try {
            doors = msg.isOpenClose();
        } catch (NullPointerException e) {
            // currentDirection remains null if an exception is caught
        }

        if (doors ==  null) {
            doorsStatus = "Unknown";
        } else if (doors) {
            doorsStatus = "Open";
        }
        else {
            doorsStatus = "Closed";
        }
        gui.updateDoors(doorsStatus);

    }
}
