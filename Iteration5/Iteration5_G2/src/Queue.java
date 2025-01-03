import java.util.ArrayList;

/**
 * The type Queue.
 * Synchronized FIFO queue of infoPackets backed by an arrayList
 */
public class Queue {
    private ArrayList<InfoPacket> data;

    /**
     * Instantiates a new Queue.
     */
    public Queue(){
        data = new ArrayList<>();
    }

    /**
     * Get The info packet from the shared buffer. This method is Blocking
     *
     * @return the info packet
     */
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

    /**
     * Put the info packet into the shared buffer. This method is blocking
     *
     * @param contents the contents
     */
    public synchronized void put(InfoPacket contents){
        data.addLast(contents);
        notifyAll();
    }

    /**
     * Is empty boolean.
     *
     * @return the boolean
     */
    public boolean isEmpty(){
        return data.isEmpty();
    }
}
