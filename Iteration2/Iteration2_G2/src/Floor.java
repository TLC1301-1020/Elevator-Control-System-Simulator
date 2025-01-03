import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Floor implements Runnable {

    private Queue queueScheduler;
    private Queue queueElevator;

    public Floor(int floorNumber, Queue queueScheduler, Queue queueElevator) {
        this.queueScheduler = queueScheduler;
        this.queueElevator = queueElevator;
    }

    @Override
    public void run() {
        String filename = "input.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                //split input from file on whitespace
                String[] split = line.split(" ");

                // Send Packet to Scheduler for External Floor request
                InfoPacket dataFromFloor = new InfoPacket();
                dataFromFloor.setType(1);
                dataFromFloor.setOriginFloor(Integer.parseInt(split[1]));
                split[3] = split[3].toLowerCase();
                if (split[3].equals("up")){
                    dataFromFloor.setDirection(true);
                } else {
                    dataFromFloor.setDirection(false);
                }

                // Send data to the Scheduler for processing
                System.out.println("Sent: "+dataFromFloor.toString()+" to Scheduler");
                queueScheduler.put(dataFromFloor);

                // Send Packet to Elevator for internal button press
                dataFromFloor = new InfoPacket();
                dataFromFloor.setType(3);
                dataFromFloor.setDestFloor(Integer.parseInt(split[3]));

                // Send data to the Scheduler for processing
                System.out.println("Sent: "+dataFromFloor.toString()+" to Scheduler");
                queueElevator.put(dataFromFloor);

                // Simulate some delay between events
                try {
                    Thread.sleep(1000); // Adjust as needed
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
