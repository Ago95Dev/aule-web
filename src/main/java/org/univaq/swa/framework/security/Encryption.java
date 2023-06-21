package org.univaq.swa.framework.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Encryption {
    private Encryption() {
    }

    //metodo pretty printing del digest
    private static String bytesToHexString(byte[] byteArray) {
        StringBuilder hexStringBuffer = new StringBuilder();
        for (byte b : byteArray) {
            char[] hexDigits = new char[2];
            hexDigits[0] = Character.forDigit((b >> 4) & 0xF, 16);
            hexDigits[1] = Character.forDigit((b & 0xF), 16);
            hexStringBuffer.append(new String(hexDigits));
        }
        return hexStringBuffer.toString();
    }

    //metodo encrypt della password
    public static String encryptPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes());
            byte[] digest = md.digest();
            return bytesToHexString(digest);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Encryption.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}

