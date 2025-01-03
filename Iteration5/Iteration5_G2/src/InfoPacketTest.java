import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.nio.ByteBuffer;

class InfoPacketTest {
    //infoPacket created, set with specific values using setters
    @Test
    void packAndUnpack() {
        System.out.println("\npack and unpack");
        // Create an instance of InfoPacket and set its fields
        InfoPacket infoPacket1 = new InfoPacket();
        infoPacket1.setType(3);
        infoPacket1.setOpenClose(true);
        infoPacket1.setOriginFloor(5);
        infoPacket1.setDestFloor(7);
        infoPacket1.setElevatorNum(2);
        infoPacket1.setDirection(false);
        infoPacket1.setGotOn(1);

        // Print original InfoPacket
        System.out.println("Original InfoPacket: " + infoPacket1);

        // Pack InfoPacket into a byte array
        byte[] packedData = infoPacket1.pack();

        // Print packed data
        System.out.println("Packed data: " + ByteBuffer.wrap(packedData));

        // Unpack the byte array into another instance of InfoPacket
        InfoPacket infoPacket2 = new InfoPacket();
        infoPacket2.unpack(packedData);

        // Print unpacked InfoPacket
        System.out.println("Unpacked InfoPacket: " + infoPacket2);

        // Assert that unpacked fields match the original InfoPacket
        assertEquals(infoPacket1.getType(), infoPacket2.getType());
        assertEquals(infoPacket1.isOpenClose(), infoPacket2.isOpenClose());
        assertEquals(infoPacket1.getOriginFloor(), infoPacket2.getOriginFloor());
        assertEquals(infoPacket1.getDestFloor(), infoPacket2.getDestFloor());
        assertEquals(infoPacket1.getElevatorNum(), infoPacket2.getElevatorNum());
        assertEquals(infoPacket1.isDirection(), infoPacket2.isDirection());
        assertEquals(infoPacket1.getGotOn(), infoPacket2.getGotOn());
    }


}
