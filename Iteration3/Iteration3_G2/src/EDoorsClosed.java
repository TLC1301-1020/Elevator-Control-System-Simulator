public class EDoorsClosed extends ElevatorState{

    public ElevatorState entry(ElevatorContext context){
        System.out.println("Closing doors...");
        return this;
    }

    public ElevatorState doorsOpen(ElevatorContext context){
        return new EDoorsOpen();
    }

    public ElevatorState startMotor(ElevatorContext context, InfoPacket infoPacket){
        if(infoPacket.isDirection()){
            return new EMovingUp();
        }else {
            return new EMovingDown();
        }
    }

    @Override
    public String toString() {
        return "EDoorsClosed";
    }
}
