import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class FloorTest {

    private Queue queue;
    private FloorData floorData;
    private Floor floor;
    private ByteArrayOutputStream outputStream;
    @BeforeEach
    void setUp() {
        queue = new Queue();
        floorData = new FloorData();
        floor = new Floor(queue, floorData);
    }

    //checks the handling of floor data
    @Test
    void testFloorDataHandling() {
        // Add floor requests
        InfoPacket packet1 = new InfoPacket();
        packet1.setType(1);
        packet1.setOriginFloor(2);
        packet1.setDirection(true);
        floorData.addFloorRequests(1, 2, packet1);

        InfoPacket packet2 = new InfoPacket();
        packet2.setType(1);
        packet2.setOriginFloor(3);
        packet2.setDirection(false);
        floorData.addFloorRequests(0, 3, packet2);

        ArrayList<InfoPacket> poppedRequests = floorData.popNumFloorRequests(1, 2, 1);
        System.out.println("Popped Requests: " + poppedRequests);
        assertEquals(1, poppedRequests.size());
        assertEquals(packet1, poppedRequests.get(0));

        floorData.setElevatorDirectionLights(1, 2, true);
        System.out.println("Elevator Direction Lights (1, 2): " + floorData.getElevatorDirectionLights(1, 2));
        assertTrue(floorData.getElevatorDirectionLights(1, 2));

        floorData.setfloorButtonLights(0, 3, true);
        System.out.println("Floor Button Lights (0, 3): " + floorData.getfloorButtonLights(0, 3));
        assertTrue(floorData.getfloorButtonLights(0, 3));
    }

    //checks the sendPacket() method
    @Test
    void testSendPacket() {
        // Create an InfoPacket for testing
        InfoPacket packet = new InfoPacket();
        packet.setType(1);
        packet.setOriginFloor(3);
        packet.setDirection(true);

        // Test sending a packet
        assertDoesNotThrow(() -> floor.sendPacket(packet, 1123));
        System.out.println("Packet sent successfully.");
    }

    //checks the construction of the Floor
    @Test
    void testFloorConstruction() {
        assertNotNull(floor);
    }

    @Test
    void testSendPacketWithNullPacket() {
        System.out.println("Test sending a null packet");
        assertThrows(NullPointerException.class, () -> floor.sendPacket(null, 3000));
    }


    // Test sending a packet with an invalid port number (negative)
    @Test
    void testSendPacketWithInvalidPort() {
        // Create an InfoPacket for testing
        InfoPacket packet = new InfoPacket();
        packet.setType(1);
        packet.setOriginFloor(3);
        packet.setDirection(true);

        assertThrows(RuntimeException.class, () -> floor.sendPacket(packet, -1));
    }

    // Test sending a packet with valid data
    @Test
    void testSendPacketWithValidData() {
        InfoPacket packet = new InfoPacket();
        packet.setType(1);
        packet.setOriginFloor(3);
        packet.setDirection(true);
        System.out.println("Sending packet: " + packet.toString());
        assertDoesNotThrow(() -> floor.sendPacket(packet, 3000));
    }


}
