import java.util.ArrayList;

public class SFloorNotificationState extends SchedulerState{

    public SchedulerState entry(SchedulerContext context, InfoPacket packet) {
        //Update elevator floor
        context.getElevatorFloor()[packet.getElevatorNum()] = packet.getOriginFloor();
        if(packet.getOriginFloor() == context.getElevatorDestination()[packet.getElevatorNum()]) {
            return new SFloorArrivalState();
        }else {
            //check for request at the given floor in the right direction whenever a not idle elevator arrives
            int direction = context.getElevatorDirection()[packet.getElevatorNum()];
            ArrayList<InfoPacket> requestQueueFloors = context.getRequestQueueFloors()[direction][packet.getOriginFloor()];
            ArrayList<InfoPacket> requestQueueElevators = context.getRequestQueueElevators()[packet.getElevatorNum()][packet.getOriginFloor()];
            if(!requestQueueElevators.isEmpty() || !requestQueueFloors.isEmpty()){
                return new SFloorArrivalState();
            } else {
                return this;
            }
        }
    }
}
