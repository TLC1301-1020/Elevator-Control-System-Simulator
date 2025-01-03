//DONE


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class InfoPacketTest {

    @Test
    public void testPackAndUnpack() {
        System.out.println("InfoPacketTest - testPackAndUnpack");

        InfoPacket infoPacket = new InfoPacket();
        infoPacket.setType(1);
        infoPacket.setOpenClose(true);
        infoPacket.setOriginFloor(2);
        infoPacket.setDestFloor(3);
        infoPacket.setElevatorNum(4);
        infoPacket.setDirection(true);

        byte[] packedData = infoPacket.pack();
        InfoPacket unpackedPacket = new InfoPacket();
        unpackedPacket.unpack(packedData);

        System.out.println("Unpacked InfoPacket: " + unpackedPacket.toString());

        assertEquals(infoPacket.getType(), unpackedPacket.getType());
        assertEquals(infoPacket.isOpenClose(), unpackedPacket.isOpenClose());
        assertEquals(infoPacket.getOriginFloor(), unpackedPacket.getOriginFloor());
        assertEquals(infoPacket.getDestFloor(), unpackedPacket.getDestFloor());
        assertEquals(infoPacket.getElevatorNum(), unpackedPacket.getElevatorNum());
        assertEquals(infoPacket.isDirection(), unpackedPacket.isDirection());

        System.out.println("InfoPacketTest - testPackAndUnpack Passed");
    }
}
