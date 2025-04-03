package com.smarthealth.io.smarthealth.shared.lib;

import java.util.Random;
import java.security.SecureRandom;


public class UID {
  private static final Random RANDOM = new SecureRandom();

    public static String generateUUIDv4() {
        byte[] randomBytes = new byte[16];
        RANDOM.nextBytes(randomBytes);

        // Set version (4) and variant bits
        randomBytes[6] &= 0x0F;
        randomBytes[6] |= 0x40;
        randomBytes[8] &= 0x3F;
        randomBytes[8] |= 0x80;

        return bytesToHex(randomBytes, true);
    }

    /* public static String generateUUIDv7() {
        long timestamp = Instant.now().toEpochMilli();
        byte[] uuid = new byte[16];
        ByteBuffer.wrap(uuid).putLong(timestamp);
        RANDOM.nextBytes(uuid, 6, 10);

        // Set version (7) and variant bits
        uuid[6] &= 0x0F;
        uuid[6] |= 0x70;
        uuid[8] &= 0x3F;
        uuid[8] |= 0x80;

        return bytesToHex(uuid, true);
    } */

    public static String generateShortId() {
        byte[] randomBytes = new byte[12];
        RANDOM.nextBytes(randomBytes);
        return bytesToHex(randomBytes, false);
    }

    private static String bytesToHex(byte[] bytes, boolean addDashes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            sb.append(String.format("%02x", bytes[i]));
            if (addDashes && (i == 3 || i == 5 || i == 7 || i == 9)) {
                sb.append("-");
            }
        }
        return sb.toString();
    }
}
