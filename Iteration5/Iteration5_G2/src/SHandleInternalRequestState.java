import java.util.ArrayList;
import static java.lang.Math.abs;

/**
 * The type S handle internal request state.
 * This state schedules elevators to handle a internal elevator button press
 */
public class SHandleInternalRequestState extends SchedulerState {

    public SchedulerState entry(SchedulerContext context, InfoPacket packet) {
        //Elevators internal requests
        ArrayList<InfoPacket>[] requestQueueElevators = context.getRequestQueueElevators()[packet.getElevatorNum()];

        int upFloor = -1;
        int downFloor = -1;
        int elevatorNum = packet.getElevatorNum();
        int elevatorDirection = context.getElevatorDirection()[elevatorNum];
        int elevatorFloor = context.getElevatorFloor()[elevatorNum];
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
            if (elevatorFloor - j >= 0) {
                if (!requestQueueElevators[elevatorFloor - j].isEmpty()) {
                    downFloor = elevatorFloor - j;
                    break;
                }
            }
        }

        if (elevatorDirection == 0) {
            //Currently going down
            if (downFloor != -1){
                //if internal request below go there
                //if new internal floor is closer change destination
                if(downFloor == elevatorFloor){
                    return new SFloorArrivalState();
                }
                moveElevator(context, elevatorNum, elevatorFloor, downFloor);
            }
        } else if (elevatorDirection == 1) {
            //Currently going up
            if (upFloor != -1){
                //if internal request up go there
                //if new internal floor is closer change destination
                if(upFloor == elevatorFloor){
                    return new SFloorArrivalState();
                }
                moveElevator(context, elevatorNum, elevatorFloor, upFloor);
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

        context.sendPacket(outPacket, Config.elevatorBasePort + elevatorNum);
        //Setup timeout for elevator getting stuck between floors
        InfoPacket timeoutPacket = new InfoPacket();
        timeoutPacket.setType(10);
        timeoutPacket.setElevatorNum(elevatorNum);
        int timeoutTime = abs(destFloor - elevatorFloor) * Config.elevatorMovementTimeFault;
        context.setTimer(timeoutTime, timeoutPacket, Config.schedulerPort);
    }

    @Override
    public String toString() {
        return "SHandleInternalRequestState";
    }
}
