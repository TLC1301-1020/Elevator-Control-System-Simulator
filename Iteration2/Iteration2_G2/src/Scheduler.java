/*
* Scheduler
* Takes InfoPackets from the Floor and passes them to the scheduler and back
 */
public class Scheduler implements Runnable {

    private Queue queueReceive;
    private Queue queueSend;

    public Scheduler(Queue receive, Queue send){
        queueReceive = receive;
        queueSend = send;
    }
    @Override
    public void run() {
        InfoPacket currentData;
        SchedulerContext schedulerContext = new SchedulerContext(queueSend);
        //Check for incoming events
        while(true){
            currentData = queueReceive.get();
            System.out.println("Scheduler state: "+ schedulerContext.getCurrentState() + " received: " + currentData.toString());
            if (currentData.getType() == 1 || currentData.getType() == 2){
                //Floor Request
                schedulerContext.setCurrentState(schedulerContext.getCurrentState().requestFloor(currentData));
            } else if (currentData.getType() == 4) {
                //Floor Arrival
                schedulerContext.setCurrentState(schedulerContext.getCurrentState().elevatorArrival(currentData));
            }
        }
    }
}
