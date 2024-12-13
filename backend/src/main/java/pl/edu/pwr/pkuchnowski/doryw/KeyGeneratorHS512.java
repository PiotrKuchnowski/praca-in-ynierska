package pl.edu.pwr.pkuchnowski.doryw;

import java.security.SecureRandom;
import java.util.Base64;

public class KeyGeneratorHS512 {
    public static void main(String[] args) {
        byte[] secret = new byte[64]; // 512 bits / 8 = 64 bytes

        new SecureRandom().nextBytes(secret);

        String encodedSecret = Base64.getEncoder().encodeToString(secret);

        System.out.println(encodedSecret);
    }
}
