/**
 * The type E doors closed.
 * State when the elevator is not moving and the doors are closed
 */
public class EDoorsClosed extends ElevatorState{

    /**
     * Doors open elevator event.
     *
     * @param context the context
     * @return the elevator state
     */
    public ElevatorState doorsOpen(ElevatorContext context){
        if(context.isInjectDoorOpenFault()){
            System.out.println("Door Open Fault Injected");
            context.setInjectDoorOpenFault(false);
            return this;
        } else {
            InfoPacket openNotifPacket = new InfoPacket();
            openNotifPacket.setType(14);
            openNotifPacket.setElevatorNum(context.getElevatorNum());
            context.sendPacket(openNotifPacket, Config.schedulerPort);
            System.out.println("Opening doors...");
            return new EDoorsOpen();
        }
    }

    /**
     * Start motor elevator event.
     *
     * @param context    the context
     * @param infoPacket the info packet
     * @return the elevator state
     */
    public ElevatorState startMotor(ElevatorContext context, InfoPacket infoPacket) {
        if (context.isInjectFloorFault()) {
            //inject floor fault by dropping the movement command
            System.out.println("Floor Movement Fault injected");
            return this;
        }
        //set current time as the last floor time for measurement when the elevator starts moving
        context.setLastFloorTime(System.currentTimeMillis());
        if (infoPacket.isDirection()) {
            return new EMovingUp();
        } else {
            return new EMovingDown();
        }
    }

    @Override
    public String toString() {
        return "EDoorsClosed";
    }
}
