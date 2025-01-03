import java.util.ArrayList;

public class Queue {
    private ArrayList<InfoPacket> data;

    public Queue(){
        data = new ArrayList<>();
    }

    //Get The info packet from the shared buffer. This method is Blocking
    public synchronized InfoPacket get(){
        InfoPacket contents;

        while (data.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        contents = data.get(0);
        data.remove(0);
        notifyAll();
        return contents;
    }

    //Put the info packet into the shared buffer. This method is blocking
    public synchronized void put(InfoPacket contents){
        /*while (data.isFull) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }*/

        data.addLast(contents);
        notifyAll();
    }

    public boolean isEmpty(){
        return data.isEmpty();
    }
}
