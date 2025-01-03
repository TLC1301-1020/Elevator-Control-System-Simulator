import java.util.ArrayList;

public class SScheduleRequestState extends SchedulerState {

    public SchedulerState entry(SchedulerContext context, InfoPacket packet){
        //Schedule Idle Elevators
        //Dispatch idle elevator if there are pending requests
        //if no pending requests send elevator to ground
        //Find idle elevator i
        int[] elevatorDirection = context.getElevatorDirection();
        for (int i = 0; i < elevatorDirection.length; i++) {
            if(elevatorDirection[i] == 2){
                int destFloor = -1;
                //Schedule
                ArrayList<InfoPacket>[][] requestQueueFloors = context.getRequestQueueFloors();
                boolean flag = false;
                //find closest request
                int num = context.getElevatorFloor()[i];
                for (int j = 0; j < requestQueueFloors[0].length; j++) {
                    for (int k = 0; k < requestQueueFloors.length; k++) {
                        //up Floor
                        if (num + j < requestQueueFloors[k].length) {
                            if (!requestQueueFloors[k][num + j].isEmpty()) {
                                flag = true;
                                destFloor = num + j;
                                break;
                            }
                        }
                        //down floor
                        if (num - j > 0) {
                            if (!requestQueueFloors[k][num - j].isEmpty()) {
                                destFloor = num - j;
                                flag = true;
                                break;
                            }
                        }
                    }
                    if (flag) {
                        break;
                    }
                }

                //if Current floor
                int elevatorNum = i;
                if (context.getElevatorFloor()[elevatorNum] == destFloor) {
                    //Handle a request on the current floor. Ie turn around or ground floor for idle elevator
                    ArrayList<InfoPacket> floorUp = requestQueueFloors[1][context.getElevatorFloor()[elevatorNum]];
                    ArrayList<InfoPacket> floorDown = requestQueueFloors[0][context.getElevatorFloor()[elevatorNum]];

                    int direction;
                    if (floorUp.isEmpty()){
                        //If no up request go down
                        direction = 0;
                    } else if (floorDown.isEmpty()) {
                        direction = 1;
                    }else {
                        direction = 2;
                    }
                    elevatorDirection = context.getElevatorDirection();
                    elevatorDirection[elevatorNum] = direction;
                    context.setElevatorDirection(elevatorDirection);

                    return new SFloorArrivalState();
                } else {
                    InfoPacket closePacket = new InfoPacket();
                    closePacket.setType(5);
                    closePacket.setOpenClose(true);
                    context.sendPacket(closePacket, Config.elevatorBasePort+elevatorNum);

                    if (destFloor == -1) {
                        //Send to ground if no pending requests
                        destFloor = 0;
                        if (context.getElevatorFloor()[elevatorNum] == 0){
                            //if elevator is already at ground do nothing
                            break;
                        }
                    }

                    //Move elevator
                    InfoPacket outPacket = new InfoPacket();
                    outPacket.setType(7);
                    boolean up = context.getElevatorFloor()[elevatorNum] < destFloor;
                    outPacket.setDirection(up);
                    //Update destination
                    context.getElevatorDestination()[elevatorNum] = destFloor;
                    int direction = up ? 1 : 0;
                    elevatorDirection = context.getElevatorDirection();
                    elevatorDirection[elevatorNum] = direction;
                    context.setElevatorDirection(elevatorDirection);
                    outPacket.setDestFloor(destFloor);

                    context.sendPacket(outPacket, Config.elevatorBasePort+elevatorNum);
                    //TODO set Direction Lights
                }
                break;
            }
        }
        return this;
    }

    @Override
    public String toString() {
        return "SScheduleRequestState";
    }

}
