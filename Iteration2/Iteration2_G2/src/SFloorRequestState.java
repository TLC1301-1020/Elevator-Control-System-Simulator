import java.util.ArrayList;

public class SFloorRequestState extends SchedulerState{
    public SFloorRequestState(SchedulerContext context, InfoPacket packet) {
        super(context);

        //add to request queue
        if (packet.getType() == 1){
            ArrayList<InfoPacket>[][] requestQueueFloors = context.getRequestQueueFloors();
            int direction = packet.isDirection() ? 1 : 0;
            requestQueueFloors[direction][packet.getOriginFloor()].add(packet);
            context.setRequestQueueFloors(requestQueueFloors);
        } else if (packet.getType() == 2) {
            ArrayList<InfoPacket>[][] requestQueueElevators = context.getRequestQueueElevators();
            requestQueueElevators[packet.getElevatorNum()][packet.getDestFloor()].add(packet);
            context.setRequestQueueElevators(requestQueueElevators);
        }

        //Got to Command if there is and idle elevator
        int[] elevatorDirection = context.getElevatorDirection();
        boolean idle=false;
        for (int i = 0; i < elevatorDirection.length; i++) {
            if(elevatorDirection[i] == 2){
                idle = true;
                break;
            }
        }
        if(idle) {
            context.setCurrentState(new SSendCommandState(context));
        }
    }

    @Override
    public String toString() {
        return "SFloorRequestState";
    }
}
