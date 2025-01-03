import java.util.ArrayList;

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
            return new SHandleInternalRequestState();
        }
        //Should never reach this
        return this;
    }

    @Override
    public String toString() {
        return "SFloorRequestState";
    }
}
