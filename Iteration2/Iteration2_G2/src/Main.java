public class Main {

    public static void main(String args[]){
        //Elevator Inbound
        Queue queueElevator = new Queue();
        //Scheduler Inbound
        Queue queueScheduler = new Queue();

        Thread scheduler = new Thread(new Scheduler(queueScheduler, queueElevator));
        Thread elevator = new Thread(new Elevator(0,queueElevator, queueScheduler));
        Thread floor = new Thread(new Floor(0, queueScheduler, queueElevator));

        scheduler.start();
        elevator.start();
        floor.start();
    }
}
