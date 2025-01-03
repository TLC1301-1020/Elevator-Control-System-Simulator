import java.util.ArrayList;

/**
 * The type S floor request state.
 * This state handles when a request for a floor occurs either from the floor or the elevator
 */
public class SFloorRequestState extends SchedulerState{

    public SchedulerState entry(SchedulerContext context, InfoPacket packet){
        //add to request queue
        if (packet.getType() == 1){
            ArrayList<InfoPacket>[][] requestQueueFloors = context.getRequestQueueFloors();
            int direction = packet.isDirection() ? 1 : 0;
            requestQueueFloors[direction][packet.getOriginFloor()].add(packet);
            context.setRequestQueueFloors(requestQueueFloors);
            return new SScheduleRequestState();
        } else if (packet.getType() == 2) {
            ArrayList<InfoPacket>[][] requestQueueElevators = context.getRequestQueueElevators();
            requestQueueElevators[packet.getElevatorNum()][packet.getDestFloor()].add(packet);
            context.setRequestQueueElevators(requestQueueElevators);
            //If the timer for the elevator is null it has not been given a command so schedule it
            if (context.getTimer(packet.getElevatorNum()) == null) {
                return new SHandleInternalRequestState();
            } else {
                return this;
            }
        }
        //Should never reach this
        throw new IllegalArgumentException();
    }

    @Override
    public String toString() {
        return "SFloorRequestState";
    }
}
