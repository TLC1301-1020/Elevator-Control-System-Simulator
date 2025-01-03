public class EDoorsOpen extends ElevatorState{

    public ElevatorState doorsClose(ElevatorContext context){
        if(context.isInjectDoorFault()){
            System.out.println("Door Fault Injected");
            context.setInjectDoorFault(false);
        } else {
            InfoPacket closeNotifPacket = new InfoPacket();
            closeNotifPacket.setType(13);
            closeNotifPacket.setElevatorNum(context.getElevatorNum());
            context.sendPacket(closeNotifPacket, Config.schedulerPort);
            System.out.println("Closing doors...");
        }
        return new EDoorsClosed();
    }

    @Override
    public String toString() {
        return "EDoorsOpen";
    }
}
