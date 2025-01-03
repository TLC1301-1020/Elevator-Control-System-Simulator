/**
 * The type Elevator state.
 */
public class ElevatorState {

    /**
     * Entry elevator state.
     *
     * @param context the context
     * @return the elevator state
     */
    public ElevatorState entry(ElevatorContext context){
        return this;
    }

    /**
     * Exit.
     *
     * @param context the context
     */
    public void exit(ElevatorContext context){

    }

    /**
     * Lights off elevator event.
     *
     * @param context    the context
     * @param infoPacket the info packet
     * @return the elevator state
     */
    public ElevatorState lightsOff(ElevatorContext context, InfoPacket infoPacket){
        context.getFloorLights()[infoPacket.getDestFloor()] = true;
        return this;
    }

    /**
     * Doors open elevator event.
     *
     * @param context the context
     * @return the elevator state
     */
    public ElevatorState doorsOpen(ElevatorContext context){
        return this;
    }

    /**
     * Doors close elevator event.
     *
     * @param context the context
     * @return the elevator state
     */
    public ElevatorState doorsClose(ElevatorContext context){
        return this;
    }

    /**
     * Stop motor elevator event.
     *
     * @param context the context
     * @return the elevator state
     */
    public ElevatorState stopMotor(ElevatorContext context){
        return this;
    }

    /**
     * Start motor elevator event.
     *
     * @param context    the context
     * @param infoPacket the info packet
     * @return the elevator state
     */
    public ElevatorState startMotor(ElevatorContext context, InfoPacket infoPacket){
        return this;
    }

    /**
     * Floor arrival elevator event.
     *
     * @param context the context
     * @param floor   the floor
     * @return the elevator state
     */
    public ElevatorState floorArrival(ElevatorContext context, int floor){
        //Send packet to scheduler notifying that the elevator has arrived at a floor
        InfoPacket packet = new InfoPacket();
        packet.setType(4);
        packet.setElevatorNum(context.getElevatorNum());
        packet.setOriginFloor(floor);
        int prevFloor = context.getCurFloor();
        //update the elevators current floor
        context.setCurFloor(floor);
        context.sendPacket(packet, Config.schedulerPort);

        //Measure the time taken to move in between this floor and the previous one
        long currentTime = System.currentTimeMillis();
        float timeTaken = currentTime - context.getLastFloorTime();
        //update the last floor time to the current time to setup next measurement
        context.setLastFloorTime(currentTime);
        System.out.println("Elevator: "+ context.getElevatorNum() +" took "+ timeTaken/1000 +" seconds to move between floor "+ prevFloor +" and floor "+floor);
        return this;
    }

    /**
     * Floor request elevator event.
     *
     * @param context    the context
     * @param infoPacket the info packet
     * @return the elevator state
     */
    public ElevatorState floorRequest(ElevatorContext context, InfoPacket infoPacket){
        if (infoPacket.getType() == 15) {
            //inject a floor fault
            context.setInjectFloorFault(true);
        }

        InfoPacket packet = new InfoPacket();
        packet.setType(2);
        int destFloor = infoPacket.getDestFloor();
        //negative destination floor indicate inject a door fault
        //Even dest floor -> open door fault
        //odd dest floor -> close door fault
        if (destFloor < 0) {
            //inject a door fault
            destFloor = destFloor * -1;
            if (destFloor % 2 == 0){
                context.setInjectDoorOpenFault(true);
            } else {
                context.setInjectDoorCloseFault(true);
            }
        }
        packet.setDestFloor(destFloor);
        //Floor Light On
        context.getFloorLights()[destFloor] = true;
        packet.setElevatorNum(context.getElevatorNum());
        context.sendPacket(packet, Config.schedulerPort);
        return this;
    }

    @Override
    public String toString() {
        return "ElevatorState";
    }
}
