public class SchedulerState {

    public SchedulerState entry(SchedulerContext context, InfoPacket packet){
        return this;
    }

    public void exit(SchedulerContext context){

    }

    public SchedulerState elevatorArrival(SchedulerContext context, InfoPacket packet){
        return new SFloorNotificationState();
    }
    public SchedulerState requestFloor(SchedulerContext context, InfoPacket packet){
        return new SFloorRequestState();
    }

    @Override
    public String toString() {
        return "SchedulerState";
    }

}
