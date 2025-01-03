import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static java.lang.Math.abs;

/**
 * The type Floor io.
 * Read the input file and puts internal elevator button presses in a queue for the floor
 * Also sends floor button presses to scheduler
 */
public class FloorIO implements Runnable{

    /**
     * The Floor.
     */
    Floor floor;
    /**
     * The Floor data.
     */
    FloorData floorData;

    /**
     * Instantiates a new Floor io.
     *
     * @param floor     the floor
     * @param floorData the floor data
     */
    public FloorIO(Floor floor, FloorData floorData){
        this.floorData = floorData;
        this.floor = floor;
    }

    @Override
    public void run() {
        long unixStartTime = System.currentTimeMillis() / 1000L;
        String filename = "input.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                //split input from file on whitespace
                String[] split = line.split(" ");

                //Split time read form input file
                String[] time = split[0].split(":");
                int dotIndex = time[2].indexOf(".");
                if(dotIndex != -1){
                    time[2] = time[2].substring(0, dotIndex);
                }

                //Determine time to wait until time from input file
                long currentUnixTime = System.currentTimeMillis() / 1000L;
                long lineUnixTime = unixStartTime + Integer.parseInt(time[0])*3600 + Integer.parseInt(time[1])*60 + Integer.parseInt(time[2]);
                long waitTime = lineUnixTime-currentUnixTime;

                if(waitTime>0){
                    try {
                        //sleep slightly less than a second to help with drift
                        Thread.sleep(waitTime*950);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }



                int orginFloor = Integer.parseInt(split[1]);
                int destFloor = Integer.parseInt(split[3]);
                //Remove negative from floors as there are no actual negative floors. Negative is just error injection
                int direction = (abs(destFloor) - abs(orginFloor)) > 0 ? 1 : 0;

                // Send Packet to Scheduler for External Floor request
                InfoPacket packet = new InfoPacket();
                packet.setType(1);
                //abs origin floor since it may be negative indicating error injection
                packet.setOriginFloor(abs(orginFloor));
                split[2] = split[2].toLowerCase();
                if (split[2].equals("up")){
                    packet.setDirection(true);
                } else {
                    packet.setDirection(false);
                }
                floor.sendPacket(packet, Config.schedulerPort);

                // Send Packet to Elevator for internal button press
                packet = new InfoPacket();
                //inject floor fault if negative
                if (orginFloor < 0){
                    packet.setType(15);
                } else {
                    packet.setType(3);
                }
                packet.setDestFloor(destFloor);
                floorData.addFloorRequests(direction, abs(orginFloor), packet);
                floorData.setfloorButtonLights(direction, abs(orginFloor), true);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
