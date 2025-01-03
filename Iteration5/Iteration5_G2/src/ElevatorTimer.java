/**
 * The type Elevator timer.
 * Timer to generate elevator floor arrival events
 */
public class ElevatorTimer implements Runnable{


    private ElevatorContext context;
    private boolean direction;

    /**
     * Instantiates a new Elevator timer.
     *
     * @param context   the context
     * @param direction the direction
     */
    public ElevatorTimer(ElevatorContext context, boolean direction){
        this.context = context;
        this.direction = direction;
    }

    @Override
    public void run() {
        int time = Config.elevatorMovementTime;
        int floorOffset = direction ? 1 : -1;
        //Wait the amount of seconds then generate a timeout event
        try {
            while(true) {
                Thread.sleep(1000 * time);
                context.floorArrival(context.getCurFloor() + floorOffset);
            }
        } catch (InterruptedException e) {
            //Return if interrupted
        }
    }
}
