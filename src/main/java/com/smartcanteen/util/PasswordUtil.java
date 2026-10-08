package com.smartcanteen.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Password hashing with PBKDF2 (built into Java, no extra library needed).
 * A random "salt" is added, so two users with the same password get different hashes.
 * Stored format:  iterations:salt:hash   (salt and hash are Base64 text)
 */
public class PasswordUtil {

    private static final int ITERATIONS = 65536;
    private static final int KEY_LENGTH_BITS = 256;
    private static final int SALT_BYTES = 16;

    private PasswordUtil() {
    }

    public static String hashPassword(String plainPassword) {
        byte[] salt = new byte[SALT_BYTES];
        new SecureRandom().nextBytes(salt);
        byte[] hash = pbkdf2(plainPassword, salt, ITERATIONS);

        return ITERATIONS + ":" + Base64.getEncoder().encodeToString(salt)
                + ":" + Base64.getEncoder().encodeToString(hash);
    }

    public static boolean verifyPassword(String plainPassword, String storedHash) {
        if (plainPassword == null || storedHash == null) {
            return false;
        }
        String[] parts = storedHash.split(":");
        if (parts.length != 3) {
            return false;
        }
        try {
            int iterations = Integer.parseInt(parts[0]);
            byte[] salt = Base64.getDecoder().decode(parts[1]);
            byte[] expectedHash = Base64.getDecoder().decode(parts[2]);
            byte[] actualHash = pbkdf2(plainPassword, salt, iterations);
            return MessageDigest.isEqual(expectedHash, actualHash);   // constant-time comparison
        } catch (IllegalArgumentException e) {
            return false;   // stored value is not in the expected format
        }
    }

    private static byte[] pbkdf2(String password, byte[] salt, int iterations) {
        try {
            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, KEY_LENGTH_BITS);
            return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new IllegalStateException("Password hashing is not available", e);
        }
    }

    /** Handy for creating hashes for seed.sql:  java -cp target/classes com.smartcanteen.util.PasswordUtil mypassword */
    public static void main(String[] args) {
        for (String password : args) {
            System.out.println(hashPassword(password));
        }
    }
}
