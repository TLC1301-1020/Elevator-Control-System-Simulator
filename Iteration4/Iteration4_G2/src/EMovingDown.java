public class EMovingDown extends ElevatorState{

    public ElevatorState entry(ElevatorContext context){
        System.out.println("Elevator : "+context.getElevatorNum()+" Moving DOWN");
        context.setElevatorTimer(false);
        return this;
    }

    public void exit(ElevatorContext context){
        context.killTimer();
    }
    public ElevatorState stopMotor(ElevatorContext context){
        return new EDoorsClosed();
    }

    @Override
    public String toString() {
        return "EMovingDown";
    }
}
