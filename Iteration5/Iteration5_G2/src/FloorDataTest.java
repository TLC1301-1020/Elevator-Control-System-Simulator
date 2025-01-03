import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class FloorDataTest {

    private FloorData floorData;

    @BeforeEach
    void setUp() {
        floorData = new FloorData();
    }


    // Test getting elevator direction lights
    @Test
    void testSetAndGetElevatorDirectionLights() {

        floorData.setElevatorDirectionLights(0, 1, true);


        boolean lights = floorData.getElevatorDirectionLights(0, 1);
        System.out.println("Elevator direction lights (0, 1): " + lights);
        assertTrue(lights, "Elevator direction light should be true");
    }
    // Test getting floor button lights
    @Test
    void testSetAndGetFloorButtonLights() {

        floorData.setfloorButtonLights(1, 2, true);

        boolean lights = floorData.getfloorButtonLights(1, 2);
        System.out.println("Floor button lights (1, 2): " + lights);
        assertTrue(lights, "Floor button light should be true");
    }


    // Test popping and content of popped requests
    @Test
    void testAddAndPopFloorRequests() {
        InfoPacket packet = new InfoPacket();
        packet.setType(1);
        packet.setOriginFloor(3);
        packet.setDirection(true);

        floorData.addFloorRequests(1, 3, packet);

        ArrayList<InfoPacket> poppedRequests = floorData.popNumFloorRequests(1, 3, 1);

        System.out.println("Popped requests: " + poppedRequests);
        assertEquals(1, poppedRequests.size(), "One request should be popped");
        assertEquals(packet, poppedRequests.get(0), "Popped request should match the added request");
    }
}
