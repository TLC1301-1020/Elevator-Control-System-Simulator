public class Main {

    public static void main(String args[]){
        //Using single directional queue to ensure data that was put in is not instantly taken out by the same class
        //floor -> scheduler
        Queue queue1 = new Queue();
        //scheduler -> floor
        Queue queue2 = new Queue();
        //scheduler -> elevator
        Queue queue3 = new Queue();
        //elevator -> scheduler
        Queue queue4 = new Queue();

        Thread scheduler = new Thread(new Scheduler(queue1, queue2, queue3, queue4));
        Thread elevator = new Thread(new Elevator(1,queue3, queue4));
        Thread floor = new Thread(new Floor(1, queue1, queue2));

        scheduler.start();
        elevator.start();
        floor.start();
    }
}
