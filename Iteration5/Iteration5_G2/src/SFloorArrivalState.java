import java.util.ArrayList;

/**
 * The type S floor arrival state.
 * This state handles when a elevator arrives at a floor
 */
public class SFloorArrivalState extends SchedulerState {

    public SchedulerState entry(SchedulerContext context, InfoPacket packet) {
        //State happens when the elevator stops at a floor
        int elevatorNum = packet.getElevatorNum();
        //Kill floor movement timeout
        context.killTimer(elevatorNum);
        //remove request from queue
        ArrayList<InfoPacket>[][] requestQueueFloors = context.getRequestQueueFloors();
        ArrayList<InfoPacket>[][] requestQueueElevators = context.getRequestQueueElevators();
        int floor = context.getElevatorFloor()[elevatorNum];
        int direction = context.getElevatorDirection()[elevatorNum];
        int occupants = context.getElevatorOccupants()[elevatorNum];

        //Let people off
        int gettingOff = requestQueueElevators[elevatorNum][floor].size();
        requestQueueElevators[elevatorNum][floor].clear();
        context.setRequestQueueElevators(requestQueueElevators);
        occupants -= gettingOff;

        //Let people on as long as there is room
        int gotOn = 0;
        while(!requestQueueFloors[direction][floor].isEmpty() && occupants<Config.elevatorCapacity){
            requestQueueFloors[direction][floor].removeFirst();
            occupants++;
            gotOn++;
        }
        //Store new occupant amount in context
        int[] occupantArray = context.getElevatorOccupants();
        occupantArray[elevatorNum] = occupants;
        context.setElevatorOccupants(occupantArray);
        context.setRequestQueueFloors(requestQueueFloors);

        System.out.println("Elevator: "+elevatorNum+" has: stopped at floor: "+floor+" direction: "+ direction +" and: "+gettingOff+" got off and: "+gotOn+" got on");
        //Stop Elevator
        InfoPacket outPacket = new InfoPacket();
        outPacket.setType(6);
        context.sendPacket(outPacket, Config.elevatorBasePort + elevatorNum);
        //elevator maybe sent to floor but nobody will get on or off so don't open doors
        if (gotOn != 0 || gettingOff != 0) {
            //Open Doors
            outPacket = new InfoPacket();
            outPacket.setType(5);
            outPacket.setOpenClose(false);
            context.sendPacket(outPacket, Config.elevatorBasePort + elevatorNum);
            InfoPacket faultPacket = new InfoPacket();
            faultPacket.setType(12);
            faultPacket.setElevatorNum(elevatorNum);
            context.setTimer(Config.doorMovementFault, faultPacket, Config.schedulerPort);
        } else {
            //Reschedule elevator if nobody got on or off
            //Check if there are any occupants
            if(context.getElevatorOccupants()[elevatorNum] > 0){
                return new SHandleInternalRequestState();
            } else {
                //If no occupants elevator is now idle
                context.getElevatorDirection()[elevatorNum] = 2;
                return new SScheduleRequestState();
            }
        }
        //Turn off Elevator Button Lights
        outPacket = new InfoPacket();
        outPacket.setType(8);
        context.sendPacket(outPacket, Config.elevatorBasePort + elevatorNum);

        outPacket = new InfoPacket();
        //Lights off
        outPacket.setType(9);
        outPacket.setOriginFloor(floor);
        outPacket.setElevatorNum(elevatorNum);
        outPacket.setGotOn(gotOn); //tell floor how many people got on
        outPacket.setDirection(direction == 1); //direction should never be idle (dir == 2) here
        context.sendPacket(outPacket, Config.floorPort);

        return this;
    }

    @Override
    public String toString() {
        return "SFloorArrivalState";
    }
}
