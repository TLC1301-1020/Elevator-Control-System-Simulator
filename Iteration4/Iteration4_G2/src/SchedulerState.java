import java.util.ArrayList;

public class SchedulerState {

    public SchedulerState entry(SchedulerContext context, InfoPacket packet){
        return this;
    }

    public void exit(SchedulerContext context){

    }

    public SchedulerState elevatorArrival(SchedulerContext context, InfoPacket packet){
        System.out.println("Elevator: "+packet.getElevatorNum()+" has: reached floor: "+packet.getOriginFloor()+" and: "+context.getElevatorOccupants()[packet.getElevatorNum()]+" people were on board");
        //Update elevator floor
        context.getElevatorFloor()[packet.getElevatorNum()] = packet.getOriginFloor();
        if(packet.getOriginFloor() == context.getElevatorDestination()[packet.getElevatorNum()]) {
            return new SFloorArrivalState();
        }else {
            //check for request at the given floor in the right direction whenever a not idle elevator arrives
            int direction = context.getElevatorDirection()[packet.getElevatorNum()];
            boolean full = context.getElevatorOccupants()[packet.getElevatorNum()] == Config.elevatorCapacity;
            ArrayList<InfoPacket> requestQueueFloors = context.getRequestQueueFloors()[direction][packet.getOriginFloor()];
            ArrayList<InfoPacket> requestQueueElevators = context.getRequestQueueElevators()[packet.getElevatorNum()][packet.getOriginFloor()];
            //Only service request for people getting on if the elevator isn't full
            if(!requestQueueElevators.isEmpty() || (!requestQueueFloors.isEmpty() && !full)){
                return new SFloorArrivalState();
            } else {
                return this;
            }
        }
    }
    public SchedulerState requestFloor(SchedulerContext context, InfoPacket packet){
        return new SFloorRequestState();
    }

    public SchedulerState timeout(SchedulerContext context, InfoPacket packet) {
        if(packet.getType() == 10){
            int[] direction = context.getElevatorDirection();
            direction[packet.getElevatorNum()] = -1;
            context.setElevatorDirection(direction);
            InfoPacket stopPacket = new InfoPacket();
            stopPacket.setType(6);
            context.sendPacket(stopPacket, Config.elevatorBasePort+packet.getElevatorNum());
            System.out.println("Floor movement Fault in elevator: "+packet.getElevatorNum());
        } else if(packet.getType() == 11){
            //Door close failure resend close Packet
            InfoPacket openPacket = new InfoPacket();
            openPacket.setType(5);
            openPacket.setOpenClose(true);
            context.sendPacket(openPacket, Config.elevatorBasePort+packet.getElevatorNum());
            InfoPacket faultPacket = new InfoPacket();
            faultPacket.setType(11);
            faultPacket.setElevatorNum(packet.getElevatorNum());
            context.setTimer(Config.doorMovementFault, faultPacket, Config.schedulerPort);
            System.out.println("Door close Fault in elevator: "+packet.getElevatorNum());

        } else if(packet.getType() == 12){
            //door open failure resend open packet
            InfoPacket openPacket = new InfoPacket();
            openPacket.setType(5);
            openPacket.setOpenClose(false);
            context.sendPacket(openPacket, Config.elevatorBasePort+packet.getElevatorNum());
            InfoPacket faultPacket = new InfoPacket();
            faultPacket.setType(12);
            faultPacket.setElevatorNum(packet.getElevatorNum());
            context.setTimer(Config.doorMovementFault, faultPacket, Config.schedulerPort);
            System.out.println("Door open Fault in elevator: "+packet.getElevatorNum());
        }
        return this;
    }

    public SchedulerState doorNotification(SchedulerContext context, InfoPacket packet){
        int elevatorNum = packet.getElevatorNum();
        if(packet.getType() == 13){

            //Doors closed
            context.killTimer(elevatorNum);
            //Schedule elevator once doors are closed
            //Check if there are any occupants
            if(context.getElevatorOccupants()[elevatorNum] > 0){
                return new SHandleInternalRequestState();
            } else {
                //If no occupants elevator is now idle
                context.getElevatorDirection()[elevatorNum] = 2;
                return new SScheduleRequestState();
            }
        } else if (packet.getType() == 14) {
            //Doors opened
            context.killTimer(elevatorNum);

            //Close Doors
            InfoPacket closePacket = new InfoPacket();
            closePacket.setType(5);
            closePacket.setOpenClose(true);
            context.sendPacket(closePacket, Config.elevatorBasePort+elevatorNum);
            InfoPacket faultPacket = new InfoPacket();
            faultPacket.setType(11);
            faultPacket.setElevatorNum(packet.getElevatorNum());
            context.setTimer(Config.doorMovementFault, faultPacket, Config.schedulerPort);
        }
        return this;
    }

    @Override
    public String toString() {
        return "SchedulerState";
    }

}
