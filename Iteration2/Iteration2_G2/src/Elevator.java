public class Elevator implements Runnable
{
    enum ElevatorStates{
        MOVING_UP,
        MOVING_DOWN,
        CLOSE_DOOR,
        OPEN_DOOR,
        FLOOR_REACHED
    }
    private int elevatorCarId;
    private Queue queueReceive;
    private Queue queueSend;
    private ElevatorStates curState;
    private int curFloor;
    private int destFloor;

     /**
      * To intialize an Elevator instance
      * @param elevatorCarId a unique id for the elevator
      * @param queueReceive the queue that the elevator will receive message
      * @param queueSend the queue that the elevator will send the message
      * @param curFloor the current floor of the elevator, assuming it is at floor 0.
      * @param curState the current state of the elevator, assuming the first state is at close door state
     */
    public Elevator(int elevatorCarId, Queue queueReceive, Queue queueSend){

        
        this.elevatorCarId = elevatorCarId;
        this.queueReceive = queueReceive;
        this.queueSend = queueSend;
        this.curFloor = 0;
        this.curState = ElevatorStates.CLOSE_DOOR;

    }

    @Override
    public void run() {
        // Run the elevator thread, it continuously receives a message from the scheduler, and sends back the response message to the scheduler.
        while (true) {
            // Get the message from the scheduler.
            InfoPacket messageReceived = queueReceive.get();
            System.out.println("Elevator " + elevatorCarId +" state: "+ curState + " received message: " + messageReceived.toString());

            if (messageReceived != null) {
                switch (curState) {
                    case CLOSE_DOOR:
                        // Process door event
                        if (messageReceived.getType() == 5 && !messageReceived.isOpenClose()) {
                            // Simulate door closing
                            System.out.println("Opening doors...");
                            try {
                                Thread.sleep(3000); // Assuming it takes 3 seconds to close the doors
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            curState = ElevatorStates.OPEN_DOOR;
                        } else if (messageReceived.getType() == 3) {
                            // Check for floor requests
                            // Compare the requested floor with the current floor
                            InfoPacket packet = new InfoPacket();
                            packet.setType(2);
                            packet.setDestFloor(messageReceived.getDestFloor());
                            packet.setElevatorNum(elevatorCarId);
                            queueSend.put(packet);
                        } else if (messageReceived.getType() == 7) {
                            destFloor = messageReceived.getDestFloor();
                            if(messageReceived.isDirection()){
                                curState = ElevatorStates.MOVING_UP;
                            } else {
                                curState = ElevatorStates.MOVING_DOWN;
                            }
                        }
                        break;

                    case OPEN_DOOR:
                        // Process open door event
                        // Internal floor requests to scheduler from elevator
                        if (messageReceived.getType() == 5 && messageReceived.isOpenClose()) {
                            // Internal floor request for this elevator
                            System.out.println("Closing doors...");
                            try {
                                Thread.sleep(3000); // Door opening for 3 seconds
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            curState = ElevatorStates.CLOSE_DOOR;
                        } else if (messageReceived.getType() == 3) {
                            // Check for floor requests
                            // Compare the requested floor with the current floor
                            InfoPacket packet = new InfoPacket();
                            packet.setType(2);
                            packet.setDestFloor(messageReceived.getDestFloor());
                            packet.setElevatorNum(elevatorCarId);
                            queueSend.put(packet);
                        }
                        break;

                    case MOVING_UP:
                        // Simulate elevator moving up
                        curFloor = destFloor;
                        // Turn to Floor Reached state to check if it is reached the required floor
                        curState = ElevatorStates.FLOOR_REACHED;
                        if (messageReceived.getType() == 3) {
                            // Check for floor requests
                            // Compare the requested floor with the current floor
                            InfoPacket packet = new InfoPacket();
                            packet.setType(2);
                            packet.setDestFloor(messageReceived.getDestFloor());
                            packet.setElevatorNum(elevatorCarId);
                            queueSend.put(packet);
                        }
                        break;

                    case MOVING_DOWN:
                        // Simulate the elevator to moving down
                        curFloor = destFloor;
                        // Move to Floor Reached state to check if it is reached the required floor
                        curState = ElevatorStates.FLOOR_REACHED;
                        if (messageReceived.getType() == 3) {
                            // Check for floor requests
                            // Compare the requested floor with the current floor
                            InfoPacket packet = new InfoPacket();
                            packet.setType(2);
                            packet.setDestFloor(messageReceived.getDestFloor());
                            packet.setElevatorNum(elevatorCarId);
                            queueSend.put(packet);
                        }
                        break;

                    case FLOOR_REACHED:
                        // Check if the elevator has reached the required floor
                        System.out.println("Elevator arrived at floor: "+curFloor);
                        InfoPacket packet = new InfoPacket();
                        packet.setType(4);
                        packet.setElevatorNum(elevatorCarId);
                        packet.setOriginFloor(curFloor);
                        queueSend.put(packet);
                        curState = ElevatorStates.OPEN_DOOR;
                        System.out.println("Opening doors...");
                        break;
                }
            }
        }
    }
}
    
