public class Queue {
    private InfoPacket data = null;

    //Put The info packet into the shared buffer. This method is Blocking
    public synchronized InfoPacket get(){
        InfoPacket contents;

        while (data==null) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        contents = data;
        data = null;
        notifyAll();
        return contents;
    }

    //Get the info packet from the shared buffer. This method is blocking
    public synchronized void put(InfoPacket contents){
        while (data!=null) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        data = contents;
        notifyAll();
    }
}
