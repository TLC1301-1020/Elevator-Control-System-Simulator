/**
 * The type E doors open.
 * State when the elevator is not moving and the doors are open
 */
public class EDoorsOpen extends ElevatorState{

    /**
     * Doors close elevator event.
     *
     * @param context the context
     * @return the elevator state
     */
    public ElevatorState doorsClose(ElevatorContext context){
        if(context.isInjectDoorCloseFault()){
            System.out.println("Door Close Fault Injected");
            context.setInjectDoorCloseFault(false);
            return this;
        } else {
            InfoPacket closeNotifPacket = new InfoPacket();
            closeNotifPacket.setType(13);
            closeNotifPacket.setElevatorNum(context.getElevatorNum());
            context.sendPacket(closeNotifPacket, Config.schedulerPort);
            System.out.println("Closing doors...");
            return new EDoorsClosed();
        }
    }

    @Override
    public String toString() {
        return "EDoorsOpen";
    }
}
