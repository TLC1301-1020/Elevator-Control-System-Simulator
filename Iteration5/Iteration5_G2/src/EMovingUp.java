/**
 * The type E moving up.
 * The state when the elevator is moving up
 */
public class EMovingUp extends ElevatorState{

    /**
     * Entry EMovingUp state.
     *
     * @param context the context
     * @return the elevator state
     */
    public ElevatorState entry(ElevatorContext context){
        System.out.println("Elevator : "+context.getElevatorNum()+" Moving UP");
        context.setElevatorTimer(true);
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
        return "EMovingUp";
    }
}
