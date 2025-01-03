public class Elevator implements Runnable {
    private int elevatorCarId;
    private Queue queueReceive;
    private Queue queueSend;

     /**
     * To intialize an Elevator instance
     * @param elevatorCarId a unique id for the elevator
     * @param queueReceive the queue that the elevator will receive message 
     * @param queueSend the queue that the elevator will send the message
     */
    public Elevator(int elevatorCarId, Queue queueReceive, Queue queueSend){

        this.elevatorCarId = elevatorCarId;
        this.queueReceive = queueReceive;
        this.queueSend = queueSend;

    }

    
    @Override
    public void run() {
        // Run the elevator thread, it continuously receive a message from the scheduler, and send back the response message to the scheduler.
        while (true) {
            // Get the message from scheduler.
            InfoPacket messageReceived = queueReceive.get();
            System.out.println("Elevator " + elevatorCarId + " received message: " + messageReceived.getMsg() + " from scheduler. ");

            // Send a response message back to the Scheduler.
            InfoPacket messageSend = new InfoPacket();
            messageSend.setMsg("Message sent from Elevator " + elevatorCarId);
            queueSend.put(messageSend);
        }
    }
}
