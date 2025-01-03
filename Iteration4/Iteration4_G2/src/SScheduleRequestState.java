import java.util.ArrayList;

import static java.lang.Math.abs;

public class SScheduleRequestState extends SchedulerState {

    public SchedulerState entry(SchedulerContext context, InfoPacket packet){
        //Schedule Idle Elevators
        //Dispatch idle elevator if there are pending requests
        //if no pending requests send elevator to ground
        //Find idle elevator i
        for (int i = 0; i < context.getElevatorDirection().length; i++) {
            if(context.getElevatorDirection()[i] == 2){
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
                    } else {
                        direction = 2;
                    }

                    int[] elevatorDirection = context.getElevatorDirection();
                    elevatorDirection[elevatorNum] = direction;
                    context.setElevatorDirection(elevatorDirection);
                    return new SFloorArrivalState();
                } else {
                    if (destFloor != -1) {
                        //Send to a pending request
                        moveElevator(context, elevatorNum, context.getElevatorFloor()[elevatorNum], destFloor);
                    }
                }
                break;
            }
        }

        for (int i = 0; i < context.getElevatorDirection().length; i++) {
            if (context.getElevatorDirection()[i] == 2 && context.getElevatorFloor()[i] != 0) {
                //Send all idle elevators to ground if they are not already
                moveElevator(context, i, context.getElevatorFloor()[i], 0);
            }
        }

        return this;
    }

    private void moveElevator(SchedulerContext context, int elevatorNum, int elevatorFloor, int destFloor){
        //Move Elevator
        InfoPacket outPacket = new InfoPacket();
        outPacket.setType(7);
        boolean up = context.getElevatorFloor()[elevatorNum] < destFloor;
        outPacket.setDirection(up);
        int outDirection = up ? 1 : 0;
        //Update elevator direction in context
        int[] direction = context.getElevatorDirection();
        direction[elevatorNum] = outDirection;
        context.setElevatorDirection(direction);
        //Update destination
        int[] destination = context.getElevatorDestination();
        destination[elevatorNum] = destFloor;
        context.setElevatorDestination(destination);

        //TODO set Direction Lights
        context.sendPacket(outPacket, Config.elevatorBasePort+elevatorNum);
        //Setup timeout for elevator getting stuck between floors
        InfoPacket timeoutPacket = new InfoPacket();
        timeoutPacket.setType(10);
        timeoutPacket.setElevatorNum(elevatorNum);
        int timeoutTime = abs(destFloor - elevatorFloor) * Config.elevatorMovementTimeFault;
        context.setTimer(timeoutTime, timeoutPacket, Config.schedulerPort);
    }

    @Override
    public String toString() {
        return "SScheduleRequestState";
    }

}
