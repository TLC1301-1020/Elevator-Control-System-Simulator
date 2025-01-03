public class EDoorsOpen extends ElevatorState{

    public ElevatorState entry(ElevatorContext context){
        System.out.println("Opening doors...");
        return this;
    }
    public ElevatorState doorsClose(ElevatorContext context){
        return new EDoorsClosed();
    }

    @Override
    public String toString() {
        return "EDoorsOpen";
    }
}
