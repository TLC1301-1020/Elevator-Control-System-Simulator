/**
 * The type E moving down.
 * The state when the elevator is moving down
 */
public class EMovingDown extends ElevatorState{

    /**
     * Entry EMovingDown state.
     *
     * @param context the context
     * @return the elevator state
     */
    public ElevatorState entry(ElevatorContext context){
        System.out.println("Elevator : "+context.getElevatorNum()+" Moving DOWN");
        context.setElevatorTimer(false);
        return this;
    }

    /**
     * Exit.
     *
     * @param context the context
     */
    public void exit(ElevatorContext context){
        context.killTimer();
    }

    /**
     * Stop motor elevator event.
     *
     * @param context the context
     * @return the elevator state
     */
    public ElevatorState stopMotor(ElevatorContext context){
        return new EDoorsClosed();
    }

    @Override
    public String toString() {
        return "EMovingDown";
    }
}
