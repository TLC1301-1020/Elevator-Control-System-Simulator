import java.util.ArrayList;

/**
 * The type Floor data.
 * Data object to add synchronicity to the data used by the two floor threads
 */
public class FloorData {

    private ArrayList<InfoPacket>[][] floorRequests; //Direction: Floor
    private boolean[][] floorButtonLights; //Direction: Floor
    private boolean[][] elevatorDirectionLights; //Direction: Floor

    /**
     * Instantiates a new Floor data.
     */
    public FloorData(){
        floorRequests = new ArrayList[2][Config.numFloors];
        for (int j=0; j<floorRequests.length; j++){
            for (int i = 0; i < floorRequests[j].length; i++) {
                floorRequests[j][i] = new ArrayList<>();
            }
        }
        floorButtonLights = new boolean[2][Config.numFloors];
        elevatorDirectionLights = new boolean[2][Config.numFloors];
    }

    /**
     * Gets elevator direction lights.
     *
     * @param direction the direction
     * @param floor     the floor
     * @return the elevator direction lights
     */
    public boolean getElevatorDirectionLights(int direction, int floor) {
        synchronized (elevatorDirectionLights){
            return elevatorDirectionLights[direction][floor];
        }
    }

    /**
     * Sets elevator direction lights.
     *
     * @param direction the direction
     * @param floor     the floor
     * @param value     the value
     */
    public void setElevatorDirectionLights(int direction, int floor, boolean value) {
        synchronized (elevatorDirectionLights){
            elevatorDirectionLights[direction][floor] = value;
        }
    }

    /**
     * Gets button lights.
     *
     * @param direction the direction
     * @param floor     the floor
     * @return the button lights
     */
    public boolean getfloorButtonLights(int direction, int floor) {
        synchronized (floorButtonLights){
            return floorButtonLights[direction][floor];
        }
    }

    /**
     * Sets button lights.
     *
     * @param direction the direction
     * @param floor     the floor
     * @param value     the value
     */
    public void setfloorButtonLights(int direction, int floor, boolean value) {
        synchronized (floorButtonLights){
            floorButtonLights[direction][floor] = value;
        }
    }

    /**
     * Add floor requests.
     *
     * @param direction the direction
     * @param floor     the floor
     * @param packet    the packet
     */
    public void addFloorRequests(int direction, int floor, InfoPacket packet) {
        synchronized (floorRequests){
            floorRequests[direction][floor].add(packet);
            floorRequests.notifyAll();
        }
    }

    /**
     * Pop @param num number of requests from the floor requests queue
     *
     * @param direction the direction
     * @param floor     the floor
     * @param num       the num
     * @return the array list
     */
    public ArrayList<InfoPacket> popNumFloorRequests(int direction, int floor, int num) {
        ArrayList<InfoPacket> contents = new ArrayList<>();
        synchronized (floorRequests){
            while(floorRequests[direction][floor].size() < num){
                try {
                    floorRequests.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            for (int i =0; i < num; i++){
                contents.add(floorRequests[direction][floor].removeFirst()); //Pop first request
            }
            return contents;
        }
    }
}
