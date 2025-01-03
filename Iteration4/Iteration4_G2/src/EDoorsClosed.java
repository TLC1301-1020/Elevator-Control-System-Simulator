public class EDoorsClosed extends ElevatorState{

    public ElevatorState doorsOpen(ElevatorContext context){
        if(context.isInjectDoorFault()){
            System.out.println("Door Fault Injected");
            context.setInjectDoorFault(false);
        } else {
            InfoPacket openNotifPacket = new InfoPacket();
            openNotifPacket.setType(14);
            openNotifPacket.setElevatorNum(context.getElevatorNum());
            context.sendPacket(openNotifPacket, Config.schedulerPort);
            System.out.println("Opening doors...");
        }
        return new EDoorsOpen();
    }

    public ElevatorState startMotor(ElevatorContext context, InfoPacket infoPacket){
        if (context.isInjectFloorFault()){
            //inject floor fault by not moving
            return this;
        } else {
            if (infoPacket.isDirection()) {
                return new EMovingUp();
            } else {
                return new EMovingDown();
            }
        }
    }

    @Override
    public String toString() {
        return "EDoorsClosed";
    }
}
