import java.util.ArrayList;

public class SFloorArrivalState extends SchedulerState {

    public SchedulerState entry(SchedulerContext context, InfoPacket packet) {
        //State happens when the elevator stops at a floor
        //remove request from queue
        ArrayList<InfoPacket>[][] requestQueueFloors = context.getRequestQueueFloors();
        ArrayList<InfoPacket>[][] requestQueueElevators = context.getRequestQueueElevators();
        int floor = packet.getOriginFloor();
        int elevatorNum = packet.getElevatorNum();
        int direction = context.getElevatorDirection()[elevatorNum];
        boolean requestServiced;

        requestServiced = !requestQueueFloors[direction][floor].isEmpty();
        requestQueueFloors[direction][floor].clear();
        context.setRequestQueueFloors(requestQueueFloors);

        requestQueueElevators[elevatorNum][floor].clear();
        context.setRequestQueueElevators(requestQueueElevators);

        //Stop Elevator
        InfoPacket outPacket = new InfoPacket();
        outPacket.setType(6);
        context.sendPacket(outPacket, Config.elevatorBasePort+ elevatorNum);
        //Open Doors
        outPacket = new InfoPacket();
        outPacket.setType(5);
        outPacket.setOpenClose(false);
        context.sendPacket(outPacket, Config.elevatorBasePort+ elevatorNum);
        //Turn off Elevator Button Lights
        outPacket = new InfoPacket();
        outPacket.setType(8);
        context.sendPacket(outPacket, Config.elevatorBasePort+ elevatorNum);

        outPacket = new InfoPacket();
        int elevatorDirection = context.getElevatorDirection()[elevatorNum];
        if(elevatorDirection == 2){
            //Lights off
            outPacket.setType(9);
            outPacket.setOriginFloor(floor);
            context.sendPacket(outPacket, Config.floorPort);
        }else {
            //Lights off with direction
            outPacket.setType(9);
            outPacket.setDirection(elevatorDirection == 1);
            outPacket.setOriginFloor(floor);
            context.sendPacket(outPacket, Config.floorPort);
        }

        //If a external request was serviced and someone just got on handle internal requests
        if (requestServiced){
            return this;
        }

        //Check if there are any internal buttons still pressed
        ArrayList<InfoPacket>[] requestQueueElevator = context.getRequestQueueElevators()[elevatorNum];
        for (int i = 0; i < requestQueueElevators.length; i++) {
            if (!requestQueueElevator[i].isEmpty()) {
                return new SHandleInternalRequestState();
            }
        }
        //If no internal button presses handle an external button press as the elevator is now idle
        context.getElevatorDirection()[elevatorNum] = 2;
        return new SScheduleRequestState();
    }

    @Override
    public String toString() {
        return "SFloorArrivalState";
    }
}
