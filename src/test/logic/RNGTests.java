package test.logic;

import logic.rng.Int256;
import logic.rng.RNG;
import logic.rng.SeededGenerator;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Created by Adam on 18/04/2015.
 */
public class RNGTests {
    @Test
    public void groupEOutputCompatibilityTest() {
        String[] numbers = new String[] {"20892c50f49165afc31b6d96bc1339edae7629b5bf60c55bc3e46738453b55c6",
                "fddaa4dea50cb5aa740234b0b1c808568b7c5309b4fe3b2cc1f1397c260108b1",
                "7add42d07f4261c642d59e7df2abd7fc9ca95fcbe087feeb7929a36f348c8d2f"};

        List<Int256> nums256 = new LinkedList<>();
        for(String num : numbers) {
            nums256.add(Int256.fromString(num));
        }

        List<Integer> list = RNG.getDiceRolls(nums256, 1, 3);

        assertEquals(1, list.size());

        assertEquals(2, (int)list.get(0));
    }

    @Test
    public void testHexCodeCompatibility() throws DecoderException {
        String hex = "7add42d07f4261c642d59e7df2abd7fc9ca95fcbe087feeb7929a36f348c8d2f";

        byte[] apacheBytes = Hex.decodeHex(hex.toCharArray());
        byte[] carsonBytes = SeededGenerator.hexToByte(hex);
        byte[] groupjBytes = (Int256.fromString(hex)).toBytes();

        assertArrayEquals(apacheBytes, carsonBytes);
        assertArrayEquals(groupjBytes, apacheBytes);
    }

    private byte[] intArrayToByteArray(int[] ints) {
        ByteBuffer buf = ByteBuffer.allocate(32);

        for(int i : ints) {
            buf.putInt(i);
        }

        return buf.array();
    }
}
