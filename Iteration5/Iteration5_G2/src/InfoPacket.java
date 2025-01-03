import java.nio.ByteBuffer;

/**
 * The type Info packet.
 * Data class that encapsulates the packets sent in between the scheduler, elevators and floor
 */
public class InfoPacket {

    /* Info Packet Types and the data fields that are used by each type
       None indicates no fields are used
    Type:
    1. Floor requests to scheduler from floor
        originFloor
        direction
    2. Internal floor requests to scheduler from elevator
        destFloor
        elevatorNum
    3. Message to elevator for internal button press from floor(input file)
        destFloor
    4. Floor notification from elevator to scheduler
        elevator Num
        originFloor
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
        originfloor
        elevator num
        direction
        gotOn
    10. Floor movement timeout
        elevator num
    11. Door close timeout
        elevator num
    12. Door open timeout
        elevator num
    13. Doors closed notification
        elevator num
    14. Doors open notification
        elevator num
    15. Inject Floor fault from floor to elevator
        destFloor
    */

    private int type;
    private boolean openClose; //True Close doors, False Open doors
    private int originFloor;
    private int destFloor;
    private int elevatorNum;
    private boolean direction; //True Up, False Down
    private int gotOn;

    /**
     * Instantiates a new Info packet.
     */
    public InfoPacket(){

    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public int getType() {
        return type;
    }

    /**
     * Sets type.
     *
     * @param type the type
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * Is open close boolean.
     *
     * @return the boolean
     */
    public boolean isOpenClose() {
        return openClose;
    }

    /**
     * Sets open close.
     *
     * @param openClose the open close
     */
    public void setOpenClose(boolean openClose) {
        this.openClose = openClose;
    }

    /**
     * Gets origin floor.
     *
     * @return the origin floor
     */
    public int getOriginFloor() {
        return originFloor;
    }

    /**
     * Sets origin floor.
     *
     * @param originFloor the origin floor
     */
    public void setOriginFloor(int originFloor) {
        this.originFloor = originFloor;
    }

    /**
     * Gets dest floor.
     *
     * @return the dest floor
     */
    public int getDestFloor() {
        return destFloor;
    }

    /**
     * Sets dest floor.
     *
     * @param destFloor the dest floor
     */
    public void setDestFloor(int destFloor) {
        this.destFloor = destFloor;
    }

    /**
     * Gets elevator num.
     *
     * @return the elevator num
     */
    public int getElevatorNum() {
        return elevatorNum;
    }

    /**
     * Sets elevator num.
     *
     * @param elevatorNum the elevator num
     */
    public void setElevatorNum(int elevatorNum) {
        this.elevatorNum = elevatorNum;
    }

    /**
     * Is direction boolean.
     *
     * @return the boolean
     */
    public boolean isDirection() {
        return direction;
    }

    /**
     * Sets direction.
     *
     * @param direction the direction
     */
    public void setDirection(boolean direction) {
        this.direction = direction;
    }

    /**
     * Gets got on.
     *
     * @return the got on
     */
    public int getGotOn() {
        return gotOn;
    }

    /**
     * Sets got on.
     *
     * @param gotOn the got on
     */
    public void setGotOn(int gotOn) {
        this.gotOn = gotOn;
    }

    /**
     * Pack Info Packet fields into a byte [ ].
     *
     * @return the byte [ ]
     */
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
        packedDataBuffer.putInt(gotOn);

        return packedDataBuffer.array();
    }

    /**
     * Unpack the byte [ ] into InfoPacket fields.
     *
     * @param frame the byte [ ] to unpack
     */
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
        gotOn = unpackedDataBuffer.getInt();
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
