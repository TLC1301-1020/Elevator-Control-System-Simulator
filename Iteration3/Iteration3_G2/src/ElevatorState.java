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
        InfoPacket packet = new InfoPacket();
        packet.setType(2);
        packet.setDestFloor(infoPacket.getDestFloor());
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
