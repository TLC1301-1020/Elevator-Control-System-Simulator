import java.util.ArrayList;

public class SFloorArrivalState extends SchedulerState {
    public SFloorArrivalState(SchedulerContext context, InfoPacket packet) {
        super(context);

        //remove request from queue
        if (packet.getType() == 4){
            int direction = packet.isDirection() ? 1 : 0;
            ArrayList<InfoPacket>[][] requestQueueFloors = context.getRequestQueueFloors();
            requestQueueFloors[direction][packet.getOriginFloor()].clear();
            context.setRequestQueueFloors(requestQueueFloors);

            ArrayList<InfoPacket>[][] requestQueueElevators = context.getRequestQueueElevators();
            requestQueueElevators[packet.getElevatorNum()][packet.getDestFloor()].clear();
            context.setRequestQueueElevators(requestQueueElevators);
        }
        //Turn off Elevator Button Lights
        InfoPacket outPacket = new InfoPacket();
        outPacket.setType(8);
        super.getContext().getSendQueue().put(outPacket);

        //Turn off Floor Button Lights

        context.setCurrentState(new SSendCommandState(context));

    }

    @Override
    public String toString() {
        return "SFlorrArrivalState";
    }
}
