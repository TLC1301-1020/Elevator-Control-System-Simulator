import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ElevatorTest {
    Elevator elevator;
    Queue queue3;
    Queue queue4;

    @BeforeEach
    void setUp() {
        queue3 = new Queue();
        queue4 = new Queue();
        elevator = new Elevator(1,queue3, queue4);
    }

    @Test
    public void testElevatorPut() throws InterruptedException{
        Thread thread = new Thread(elevator);
        thread.start();

        // allow time for elevator start up
        Thread.sleep(1000);

        InfoPacket sendingData = new InfoPacket();

        queue3.put(sendingData);

        // assert elevator has put into queue4
        assertNotNull(queue4.get());

        thread.interrupt();
    }
}
