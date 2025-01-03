public class ElevatorState {

    public ElevatorState entry(ElevatorContext context){
        return this;
    }

    public void exit(ElevatorContext context){

    }

    public ElevatorState lightsOff(ElevatorContext context, InfoPacket infoPacket){
        context.getFloorLights()[infoPacket.getDestFloor()] = true;
        return this;
    }

    public ElevatorState doorsOpen(ElevatorContext context){
        return this;
    }

    public ElevatorState doorsClose(ElevatorContext context){
        return this;
    }

    public ElevatorState stopMotor(ElevatorContext context){
        return this;
    }

    public ElevatorState startMotor(ElevatorContext context, InfoPacket infoPacket){
        return this;
    }

    public ElevatorState floorArrival(ElevatorContext context, int floor){
        InfoPacket packet = new InfoPacket();
        packet.setType(4);
        packet.setElevatorNum(context.getElevatorNum());
        packet.setOriginFloor(floor);
        context.setCurFloor(floor);
        context.sendPacket(packet, Config.schedulerPort);
        return this;
    }

    public ElevatorState floorRequest(ElevatorContext context, InfoPacket infoPacket){
        if (infoPacket.getType() == 15) {
            //inject a floor fault
            context.setInjectFloorFault(true);
        }

        InfoPacket packet = new InfoPacket();
        packet.setType(2);
        int destFloor = infoPacket.getDestFloor();
        if (destFloor < 0) {
            //inject a door fault
            context.setInjectDoorFault(true);
            destFloor = destFloor * -1;
        }
        packet.setDestFloor(destFloor);
        //Floor Light On
        context.getFloorLights()[infoPacket.getDestFloor()] = true;
        packet.setElevatorNum(context.getElevatorNum());
        context.sendPacket(packet, Config.schedulerPort);
        return this;
    }

    @Override
    public String toString() {
        return "ElevatorState";
    }
}
