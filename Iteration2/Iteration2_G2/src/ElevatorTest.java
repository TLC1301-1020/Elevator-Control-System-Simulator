import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ElevatorTest {
    //initialization
    Elevator elevator;
    Queue queue3;
    Queue queue4;
    Thread thread;

    @BeforeEach
    void setUp() {
        //Set queues and instances
        queue3 = new Queue();
        queue4 = new Queue();
        elevator = new Elevator(1, queue3, queue4);
        thread = new Thread(elevator);
        thread.start();

        
        // Allow time for elevator start up
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    void tearDown() {
        //interrupt elevator thread 
        thread.interrupt();
        
        // Clear queues 
        //queue3.clear();
        //queue4.clear();
    }

    @Test
    public void testElevatorPut() throws InterruptedException {
        //putting message in queue3
        InfoPacket sendingData = new InfoPacket();
        queue3.put(sendingData);
        
        // Assert elevator has put into queue4
        assertNotNull(queue4.get());
    }

    @Test
    public void testMultipleMessages() throws InterruptedException {
        //messages into queue3 then check
        InfoPacket sendingData1 = new InfoPacket();
        InfoPacket sendingData2 = new InfoPacket();
        queue3.put(sendingData1);
        queue3.put(sendingData2);
        
        // process messages
        Thread.sleep(2000);
        
        // Assert elevator has put into queue4 then check
        assertNotNull(queue4.get()); 
        assertNotNull(queue4.get());
    }
}
