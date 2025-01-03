import java.util.Random;

/**
 * The type Generate input.
 * Used to generate random input that can be put into input.txt
 */
public class GenerateInput {
    /**
     * Main.
     *
     * @param args the args
     */
    public static void main(String args[]){
        //Start time
        int[] time = {0,0,0};
        //Randomly create some input lines
        for (int i =0; i<50; i++){
            Random r = new Random();
            int originFloor =r.nextInt(Config.numFloors);
            int destFloor =r.nextInt(Config.numFloors);
            while(destFloor == originFloor){
                destFloor =r.nextInt(Config.numFloors);
            }
            int timeGap = r.nextInt(5);
            //Turn the time in seconds into minutes and hours
            time[2] += timeGap;
            if(time[2]>=60){
                time[2] =0;
                time[1]++;
            }
            if(time[1]>=60){
                time[1] = 0;
                time[0]++;
            }
            String direction = destFloor>originFloor ? "Up" : "Down";
            System.out.println(time[0]+":"+time[1]+":"+time[2]+" "+originFloor+" "+direction+" "+destFloor);
        }
    }
}
