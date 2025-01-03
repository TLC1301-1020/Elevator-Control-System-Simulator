import java.util.ArrayList;

public class SSendCommandState extends SchedulerState {
    public SSendCommandState(SchedulerContext context) {
        super(context);

        int floor = -1;
        InfoPacket outPacket = new InfoPacket();
        System.out.println("SSendCommandState");

        //Schedule
        if(context.getElevatorDirection()[0] == 0){
            //Down
            ArrayList<InfoPacket>[] requestQueueFloors = context.getRequestQueueFloors()[0];
            ArrayList<InfoPacket>[] requestQueueElevators = context.getRequestQueueElevators()[0];
            for (int i = 0; i < requestQueueElevators.length; i++) {
                if (!requestQueueFloors[i].isEmpty() || !requestQueueElevators[i].isEmpty()){
                    floor = i;
                    break;
                }
            }
        } else if (context.getElevatorDirection()[0] == 1) {
            //Up
            ArrayList<InfoPacket>[] requestQueueFloors = context.getRequestQueueFloors()[1];
            ArrayList<InfoPacket>[] requestQueueElevators = context.getRequestQueueElevators()[0];
            for (int i = 0; i < requestQueueElevators.length; i++) {
                if (!requestQueueFloors[i].isEmpty() || !requestQueueElevators[i].isEmpty()){
                    floor = i;
                    break;
                }
            }
        } else if (context.getElevatorDirection()[0] == 2) {
            //None
            ArrayList<InfoPacket>[][] requestQueueFloors = context.getRequestQueueFloors();
            boolean flag = false;
            for(int i=0; i<requestQueueFloors.length; i++){
                for(int j=0; j<requestQueueFloors[i].length; j++){
                    int num = context.getElevatorFloor()[0];
                    if (j+num < requestQueueFloors[i].length) {
                        if (!requestQueueFloors[i][j + num].isEmpty()) {
                            flag = true;
                            floor = j+num;
                            break;
                        }
                    }
                    //Down default is up and down are equal
                    if (j-num > 0) {
                        if (!requestQueueFloors[i][j - num].isEmpty()) {
                            floor = j-num;
                            flag = true;
                            break;
                        }
                    }
                }
                if (flag){
                    break;
                }
            }
        }

        //Send command
        if(floor != -1) {
            if (context.getElevatorFloor()[0] == floor) {
                outPacket.setType(5);
                outPacket.setOpenClose(false);
            } else {
                outPacket.setType(7);
                boolean up = context.getElevatorFloor()[0] > floor;
                outPacket.setDirection(up);
                int[] elevatorDirection = context.getElevatorDirection();
                int direction = up ? 1 : 0;
                elevatorDirection[0] = direction;
                context.setElevatorDirection(elevatorDirection);
                outPacket.setDestFloor(floor);

                InfoPacket closePacket = new InfoPacket();
                closePacket.setType(5);
                closePacket.setOpenClose(true);
                super.getContext().getSendQueue().put(closePacket);
            }


            super.getContext().getSendQueue().put(outPacket);
            //set Direction Lights
        }
    }

    @Override
    public String toString() {
        return "SSendCommandState";
    }

}
