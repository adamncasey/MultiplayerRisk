package networking;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.util.Arrays;
import java.util.Base64;

public class SignatureTest {
    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        // Generate new key
        KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");
        gen.initialize(2048);
        KeyPair keyPair = gen.generateKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        String plaintext = "{ \\\"command\\\": \\\"reject_join_game\\\", \\\"payload\\\": \\\"Game in progress\\\"}";

        // Compute signature
        Signature instance = Signature.getInstance("SHA256withRSA");
        instance.initSign(privateKey);
        instance.update((plaintext).getBytes());
        byte[] signature = instance.sign();

        // Compute digest
        MessageDigest sha1 = MessageDigest.getInstance("SHA-256");
        byte[] digest = sha1.digest((plaintext).getBytes());

        // Encrypt digest
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        byte[] cipherText = cipher.doFinal(digest);

        Base64.Encoder encoder = Base64.getMimeEncoder();

        System.out.println(plaintext);
        System.out.print("Digest: ");
        System.out.println(Arrays.toString(digest));
        System.out.println(encoder.encodeToString(signature));
        System.out.println(Arrays.toString(cipherText));

        System.out.println(keyPair.getPublic().getFormat());

        System.out.println("-----BEGIN PUBLIC KEY-----");
        System.out.println(encoder.encodeToString(keyPair.getPublic().getEncoded()));
        System.out.println("-----END PUBLIC KEY-----");
    }
}
