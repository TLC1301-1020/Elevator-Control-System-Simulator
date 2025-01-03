import java.util.ArrayList;

public class SchedulerState {

    private SchedulerContext context;

    public SchedulerState(SchedulerContext context){
        this.context = context;
    }

    public SchedulerState elevatorArrival(InfoPacket packet){
        return new SFloorArrivalState(context, packet);
    }
    public SchedulerState requestFloor(InfoPacket packet){
        return new SFloorRequestState(context, packet);
    }

    public SchedulerContext getContext() {
        return context;
    }

    public void setContext(SchedulerContext context) {
        this.context = context;
    }

    @Override
    public String toString() {
        return "SchedulerState";
    }

}
