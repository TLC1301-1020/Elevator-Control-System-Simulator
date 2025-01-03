public class InfoPacket {

    /* Info Packet Types
    1. Floor requests to scheduler from floor
        originFloor
        Direction
    2. Internal floor requests to scheduler from elevator
        Dest Floor
        ElevatorNum
    3. Message to elevator for internal button press from floor(input file)
        Dest Floor
    4. Floor notification from elevator to scheduler
        elevator Num
        OriginFloor
    5. Open/close doors
        openClose
    6. Stop motor
        none
    7. Motor up/down
        direction
        destFloor
    8. Lights off
        none
    */

    private byte[] packed;
    private int type;
    private boolean openClose; //True Close doors, False Open doors
    private int originFloor;
    private int destFloor;
    private int elevatorNum;
    private boolean direction; //True Up, False Down

    public InfoPacket(){

    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isOpenClose() {
        return openClose;
    }

    public void setOpenClose(boolean openClose) {
        this.openClose = openClose;
    }

    public int getOriginFloor() {
        return originFloor;
    }

    public void setOriginFloor(int originFloor) {
        this.originFloor = originFloor;
    }

    public int getDestFloor() {
        return destFloor;
    }

    public void setDestFloor(int destFloor) {
        this.destFloor = destFloor;
    }

    public int getElevatorNum() {
        return elevatorNum;
    }

    public void setElevatorNum(int elevatorNum) {
        this.elevatorNum = elevatorNum;
    }

    public boolean isDirection() {
        return direction;
    }

    public void setDirection(boolean direction) {
        this.direction = direction;
    }

    public byte[] pack(){
        return null;
    }

    public void unpack(byte[] frame){

    }

    public byte[] getPacked() {
        return packed;
    }

    @Override
    public String toString() {
        String string = "Type: "+Integer.toString(type)
                +" openClose: "+Boolean.toString(openClose)
                +" originFloor: "+Integer.toString(originFloor)
                +" destFloor: "+Integer.toString(destFloor)
                +" elevatorNum: "+Integer.toString(elevatorNum)
                +" direction: "+Boolean.toString(direction);
        return string;
    }

}
