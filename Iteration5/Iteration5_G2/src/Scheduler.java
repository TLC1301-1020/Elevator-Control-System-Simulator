import java.net.DatagramSocket;
import java.net.SocketException;

/**
* Scheduler
* Receives incoming requests from the floor and elevators and schedules the elevators to handle the requests
 */
public class Scheduler {

    private Queue queueReceive;

    private ElevatorSystemGUI systemGUI;

    /**
     * Instantiates a new Scheduler.
     *
     * @param receive the receiveQueue
     */
    public Scheduler(Queue receive, ElevatorSystemGUI gui){
        queueReceive = receive;
        systemGUI = gui;
        run();
    }

    private void run() {
        InfoPacket currentData;
        SchedulerContext schedulerContext = new SchedulerContext();
        long unixStartTime = 0;
        boolean measurementTaken = false;
        //Check for incoming events
        while(true){
            currentData = queueReceive.get();
            //Record the time of the first packet
            if (unixStartTime == 0){
                unixStartTime = System.currentTimeMillis() / 1000L;
            }
            System.out.println("Scheduler state: "+ schedulerContext.getCurrentState() + " received: " + currentData.toString());
            schedulerContext.receivePacket(currentData);
            systemGUI.updateGUI(currentData, schedulerContext);
            //Measure time after each packet is handled
            if(!measurementTaken) {
                measurementTaken = measureTime(unixStartTime, schedulerContext);
            }
        }
    }

    /**
     * Print the difference between the current time and the start time after all request have been handled
     * Return True if a measurement was printed
     * Return false otherwise
     *
     * @param unixStartTime the unix start time of the system
     * @param schedulerContext the scheduler context
     */
    private boolean measureTime(long unixStartTime, SchedulerContext schedulerContext) {
        boolean noRequests = true;

        //check if the request queues are empty
        for (int i = 0; i < schedulerContext.getRequestQueueElevators().length; i++) {
            for (int j = 0; j < schedulerContext.getRequestQueueElevators()[i].length; j++) {
                noRequests &= schedulerContext.getRequestQueueElevators()[i][j].isEmpty();
            }
        }

        for (int j = 0; j < schedulerContext.getRequestQueueFloors().length; j++) {
            for (int i = 0; i < schedulerContext.getRequestQueueFloors()[j].length; i++) {
                noRequests &= schedulerContext.getRequestQueueFloors()[j][i].isEmpty();
            }
        }
        //If the request queue are empty the input file is finished
        if (noRequests) {
            long currentUnixTime = System.currentTimeMillis() / 1000L;
            long timeTaken = currentUnixTime - unixStartTime;
            long minutes = timeTaken / 60;
            long seconds = timeTaken % 60;

            String stringTimeTaken = minutes + " minutes " + seconds + " seconds";
            System.out.println("Measurement: It took: " + stringTimeTaken + " to handle all requests in the input file");
            return true;
        }
        return false;
    }

    /**
     * Main.
     *
     * @param args the args
     */
    public static void main(String args[]){

        Queue queueReceive = new Queue();
        ElevatorSystemGUI gui = new ElevatorSystemGUI(Config.numElevators);

        try {
            DatagramSocket socket = new DatagramSocket(3000);
            Thread udpThread = new Thread(new UDPThread(socket, queueReceive));
            udpThread.start();
            Thread guiThread = new Thread(new ElevatorSystemGUIThread(gui));
            guiThread.start();
            Scheduler scheduler = new Scheduler(queueReceive, gui);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }

    }

}
