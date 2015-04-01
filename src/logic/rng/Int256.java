package logic.rng;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Random;

public class Int256 {

    private static Random random = new Random();

    public final int[] value;
    public final String string;

    public static Int256 fromRandom(){
        int[] value = new int[8];
        for(int i = 0; i != 8; ++i){
            value[i] = random.nextInt();
        }
        return new Int256(value);
    }

    public static Int256 fromString(String s){
        int[] value = new int[8];
        for(int i = 0; i != 8; ++i){
            String part = s.substring(i*8, (i+1)*8);
            value[i] = (int)Long.parseLong(part, 16);
        }
        return new Int256(value);
    }

    public static Int256 fromHash(Int256 i){
        MessageDigest digest = null;
        try{
            digest = MessageDigest.getInstance("SHA-256");
        }catch(NoSuchAlgorithmException e){
        } // Never thrown as algorithm is hard coded

        byte[] hashBytes = digest.digest(i.toBytes());
        ByteBuffer bbuffer = ByteBuffer.wrap(hashBytes);
        IntBuffer ibuffer = bbuffer.asIntBuffer();
        int[] value = new int[8];
        ibuffer.get(value);
        return new Int256(value);
    }

    public boolean compare(Int256 n){
        for(int i = 0; i != 8; ++i){
            if(value[i] != n.value[i]){
                return false;
            }
        }
        return true;
    }

    private Int256(int[] value) {
        if(value.length != 8) {
            throw new IllegalArgumentException("value must be int array of length 8: 256bit");
        }
        this.value = value;
        String string = "";
        for(int i : value){
            string += Integer.toHexString(i);
        }
        this.string = string;
    }

    private byte[] toBytes(){
        ByteBuffer bbuffer = ByteBuffer.allocate(256);
        IntBuffer ibuffer = bbuffer.asIntBuffer();
        ibuffer.put(value);
        return bbuffer.array(); 
    }
}
