/*
* Scheduler
* Takes InfoPackets from the Floor and passes them to the scheduler and back
 */
public class Scheduler implements Runnable {

    private Queue queueFloorOut;
    private Queue queueFloorIn;
    private Queue queueElevatorIn;
    private Queue queueElevatorOut;

    public Scheduler(Queue queue1, Queue queue2, Queue queue3, Queue queue4){
        queueFloorIn = queue1;
        queueFloorOut = queue2;
        queueElevatorIn = queue4;
        queueElevatorOut = queue3;

    }
    @Override
    public void run() {
        InfoPacket currentData;
        //Take data from floor and pass it to the elevator and back
        while(true){
            currentData = queueFloorIn.get();
            queueElevatorOut.put(currentData);
            currentData = queueElevatorIn.get();
            queueFloorOut.put(currentData);
        }
    }
}
