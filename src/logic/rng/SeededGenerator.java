package logic.rng;

import java.util.Map;
import java.util.HashMap;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Die Class Wraps around cryptographic dice rolling To use this class: Call
 * method addHash(...) to submit hashes of random numbers (seed sources), call
 * addNumber(...) to submit the random numbers (seed sources) contributing to
 * the seed of the generator, call finalise before requesting random numbers
 * through nextInt().
 */
public class SeededGenerator {
    private byte[] mixednumber = new byte[32];
    private int ptr = 0;

    private SecureRandom sr;

    private boolean finalised = false;

    public SeededGenerator() {
        try {
            sr = SecureRandom.getInstance("SHA1PRNG", "SUN");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Converts a hexadecimal string to a byte array
     * 
     * @param str
     *            The string to convert
     */
    private static byte[] hexToByte(String str) {
        int len = str.length();
        byte[] data = new byte[len >> 1];

        for (int i = 0; i < len; i += 2)
            data[i >> 1] = (byte) ((Character.digit(str.charAt(i), 16) << 4) + Character
                    .digit(str.charAt(i + 1), 16));

        return data;
    }

    /**
     * Hashes a byte array
     * 
     * @param arr
     *            Byte array to hash
     * @return Byte array containing hash
     * @throws NoSuchAlgorithmException
     */
    public byte[] hashByteArr(byte[] arr) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(arr);
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e.toString());
        }
    }

    /**
     * Adds and verifies a number sent by a player after its corresponding hash
     * was sent
     * 
     * @param playerid
     *            The player id sending the number
     * @param number
     *            The number itself
     */
    public synchronized void addNumber(byte[] number) {
        if (number.length != mixednumber.length)
            throw new IllegalArgumentException("Invalid number length provided");

        for (int i = 0; i < mixednumber.length; i++)
            mixednumber[i] ^= number[i];
    }

    /**
     * Performs final checks before results from the hash can be returned
     * 
     * @throws HashMismatchException
     */
    public void finalise() {
        if (finalised)
            return;

        finalised = true;

        mixednumber = hashByteArr(mixednumber); // So all seeds create a
                                                // pseudo-random sequence of
                                                // numbers
    }

    /**
     * Returns a number between 0 and 255
     * 
     * @return The resulting byte
     */
    private int getByte() {
        int res;

        if (!finalised) {
            throw new IllegalStateException(
                    "method finalise() must be called before numbers can be generated.");
        }

        if (ptr >= mixednumber.length) {
            mixednumber = hashByteArr(mixednumber);
            ptr = 0;

        }
        res = mixednumber[ptr++] & 0xFF; // Bit hack to force remove sign
                                         // extension of MSB
        return res;
    }

    public long nextInt() {
        ByteBuffer buff = ByteBuffer.allocate(8);
        buff.putInt(0);
        for (int i = 0; i < 4; i++) {
            buff.put((byte) getByte());
        }
        buff.rewind();
        return buff.getLong();
    }
}
