import java.util.ArrayList;

public class SHandleInternalRequestState extends SchedulerState {

    public SchedulerState entry(SchedulerContext context, InfoPacket packet) {
        //Elevators internal requests
        ArrayList<InfoPacket>[] requestQueueElevators = context.getRequestQueueElevators()[packet.getElevatorNum()];

        int upFloor = -1;
        int downFloor = -1;
        int elevatorNum = packet.getElevatorNum();
        int elevatorDirection = context.getElevatorDirection()[elevatorNum];
        int elevatorFloor = context.getElevatorFloor()[packet.getElevatorNum()];
        //Find Closest Up floor
        for (int j = 0; j < requestQueueElevators.length; j++) {
            if (j + elevatorFloor < requestQueueElevators.length) {
                if (!requestQueueElevators[j + elevatorFloor].isEmpty()) {
                    upFloor = j + elevatorFloor;
                    break;
                }
            }

        }
        //find Closest Down floor
        for (int j = 0; j < requestQueueElevators.length; j++) {
            if (elevatorFloor - j > 0) {
                if (!requestQueueElevators[elevatorFloor - j].isEmpty()) {
                    downFloor = elevatorFloor - j;
                    break;
                }
            }
        }

        //Current floor
        if (context.getElevatorFloor()[elevatorNum] == upFloor || context.getElevatorFloor()[elevatorNum] == downFloor) {
            return new SFloorArrivalState();
        } else {
            int destFloor = -1;
            if (downFloor == -1){
                destFloor = upFloor;
            } else if (upFloor == -1){
                destFloor = downFloor;
            } else {
                if (elevatorDirection == 0) {
                    //Down
                    destFloor = downFloor;
                } else if (elevatorDirection == 1) {
                    //Up
                    destFloor = upFloor;
                } else if (elevatorDirection == 2){
                    //idle
                    //Choose closest of up and down if idle
                    if ((upFloor - context.getElevatorFloor()[elevatorNum]) < (context.getElevatorFloor()[elevatorNum] - downFloor)){
                        destFloor = upFloor;
                    } else {
                        destFloor = downFloor;
                    }
                }
            }

            if (destFloor != -1) {
                //Close Doors
                InfoPacket closePacket = new InfoPacket();
                closePacket.setType(5);
                closePacket.setOpenClose(true);
                context.sendPacket(closePacket, Config.elevatorBasePort+elevatorNum);

                //Move Elevator
                InfoPacket outPacket = new InfoPacket();
                outPacket.setType(7);
                boolean up = context.getElevatorFloor()[elevatorNum] < destFloor;
                outPacket.setDirection(up);
                int outDirection = up ? 1 : 0;
                //Update elevator direction in context
                int[] direction = context.getElevatorDirection();
                direction[packet.getElevatorNum()] = outDirection;
                context.setElevatorDirection(direction);
                //Update destination
                context.getElevatorDestination()[elevatorNum] = destFloor;

                //TODO set Direction Lights
                context.sendPacket(outPacket, Config.elevatorBasePort+elevatorNum);
            }
        }
        return this;
    }

    @Override
    public String toString() {
        return "SHandleInternalRequestState";
    }
}
