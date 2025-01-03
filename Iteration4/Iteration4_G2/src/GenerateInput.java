import java.util.Random;

public class GenerateInput {
    public static void main(String args[]){
        for (int i =0; i<100; i++){
            Random r = new Random();
            int originFloor =r.nextInt(Config.numFloors);
            int destFloor =r.nextInt(Config.numFloors);
            String direction = destFloor>originFloor ? "Up" : "Down";
            System.out.println("14:05:15.0 "+originFloor+" "+direction+" "+destFloor);
        }
    }
}
