import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Floor implements Runnable {

    private Queue queueout;
    private Queue queuein;
    private int floorNumber;

    public Floor(int floorNumber, Queue queueout, Queue queuein) {
        this.floorNumber = floorNumber;
        this.queueout = queueout;
        this.queuein = queuein;
    }

    @Override
    public void run() {
        String filename = "input.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Simulate events or data generation for the floor
                InfoPacket dataFromFloor = new InfoPacket();
                dataFromFloor.setMsg(line);

                // Send data to the Scheduler for processing
                System.out.println("Sent: "+dataFromFloor.getMsg()+" to Scheduler");
                queueout.put(dataFromFloor);

                // Receive data back from the Scheduler
                InfoPacket dataFromScheduler = queuein.get();

                // Process the data received from the Scheduler
                processFromScheduler(dataFromScheduler);

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

    // Method to process data received from the Scheduler
    private void processFromScheduler(InfoPacket data) {
        System.out.println("Floor received: "+data.getMsg()+" from Scheduler");
    }
}
