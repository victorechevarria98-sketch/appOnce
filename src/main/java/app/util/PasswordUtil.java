// src/main/java/com/ecosimulator/utils/PasswordUtil.java
package app.util;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import de.mkammerer.argon2.Argon2Factory.Argon2Types;

public class PasswordUtil {
    private static final Argon2 argon2 = Argon2Factory.create(Argon2Types.ARGON2id);
    private static final int ITERATIONS = 4;
    private static final int MEMORY = 65536; // 64MB
    private static final int PARALLELISM = 4;
    
    /**
     * Hashea una contraseña usando Argon2id
     */
    public static String hashPassword(String password) {
        return argon2.hash(ITERATIONS, MEMORY, PARALLELISM, password.toCharArray());
    }
    
    /**
     * Verifica una contraseña contra su hash
     */
    public static boolean verifyPassword(String password, String hash) {
        return argon2.verify(hash, password.toCharArray());
    }
    
    /**
     * Genera un token aleatorio seguro
     */
    public static String generateSecureToken() {
        byte[] bytes = new byte[32];
        new java.security.SecureRandom().nextBytes(bytes);
        return java.util.Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}