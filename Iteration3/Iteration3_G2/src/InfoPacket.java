import java.nio.ByteBuffer;

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
    9. Lights off to floor
        floor
        direction (optional)
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

    public byte[] pack() {

        // Allocate the size for a packedDataBuffer
        ByteBuffer packedDataBuffer = ByteBuffer.allocate(128);

        // Pack the data into the packedDataBuffer
        packedDataBuffer.putInt(type);
        packedDataBuffer.put((byte) (openClose ? 1 : 0));
        packedDataBuffer.putInt(originFloor);
        packedDataBuffer.putInt(destFloor);
        packedDataBuffer.putInt(elevatorNum);
        packedDataBuffer.put((byte) (direction ? 1 : 0));

        return packedDataBuffer.array();
    }

    public void unpack(byte[] frame) {

        // To wrap a byte array(received message from udp thread) into unpackedDataBuffer
        ByteBuffer unpackedDataBuffer = ByteBuffer.wrap(frame);

        // Unpack the data from the unpackedDataBuffer
        type = unpackedDataBuffer.getInt();
        openClose = unpackedDataBuffer.get() == 1;
        originFloor = unpackedDataBuffer.getInt();
        destFloor = unpackedDataBuffer.getInt();
        elevatorNum = unpackedDataBuffer.getInt();
        direction = unpackedDataBuffer.get() == 1;
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
