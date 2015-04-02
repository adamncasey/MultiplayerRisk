package logic.rng;

public class RC4 {

    private final int len = 256;
    private final int state[] = new int[len];
    private int x = 0;
    private int y = 0;

    // Initialise RC4
    public RC4(byte[] seed){
        if(seed.length != len){
            System.exit(-1);
        }
        for(int i = 0; i != len; ++i){
            state[i] = i;
        }

        int j = 0;
        for(int i = 0; i != len; ++i){
            j = (j + state[i] + (seed[i % len] & 0xFF)) % len;
            if(j < 0){
                j += len;
            }
            swap(i, j);
        }

        // Discard the first 1024 bytes
        for(int i = 0; i < 1024; ++i){
            getRandomByte();
        }
    }

    private void swap(int a, int b){
        int temp = state[a];
        state[a] = state[b];
        state[b] = temp;
    }

    private int getRandomByte(){
        x = (x + 1) % len;
        y = (y + state[x]) % len;
        swap(x, y);
        return state[(state[x] + state[y]) % len];
    }

    public int nextInt(){
        int ret = 0;
        for(int i = 0; i != 4; ++i){
            ret <<= 8;
            ret |= getRandomByte() & 0xFF;
        }
        return ret;
    }
}
