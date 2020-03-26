package com.iabtcf.v2.decoder;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BitInputStreamTest {

    @Test
    void testReadSmall() {
        String bitString = "0000 1000 0000 0001";
        BitInputStream bitInputStream = vectorFromBitString(bitString);
        assertEquals(0, bitInputStream.readInt(() -> 4));
        assertEquals(128, bitInputStream.readInt(() -> 8));
        assertEquals(1, bitInputStream.readInt(() -> 4));
    }

    @Test
    void testReadInteger() {
        String bitString = "0111 1111 1111 1111 1111 1111 1111 1111";
        BitInputStream bitInputStream = vectorFromBitString(bitString);
        assertEquals(Integer.MAX_VALUE, bitInputStream.readInt(() -> 32));
    }

    @Test
    void testReadLong() {
        String bitString = "0111 1111 1111 1111 1111 1111 1111 1111" + "1111 1111 1111 1111 1111 1111 1111 1111";
        BitInputStream bitInputStream = vectorFromBitString(bitString);
        assertEquals(Long.MAX_VALUE, bitInputStream.readLong(64));
    }

    @Test
    void tesReadInstant() {
        String bitString = "001110101101110010100111000111000100";
        BitInputStream bitInputStream = vectorFromBitString(bitString);
        assertEquals(Instant.parse("2020-01-26T18:19:25.200Z"), bitInputStream.readInstant(() -> 36));
    }

    @Test
    void tesReadEpochInstant() {
        String bitString = Stream.generate(() -> "0").limit(36).collect(Collectors.joining());
        BitInputStream bitInputStream = vectorFromBitString(bitString);
        assertEquals(Instant.EPOCH, bitInputStream.readInstant(() -> 36));
    }

    @Test
    void testReadBit() {
        String bitString = "10101010";
        BitInputStream bitInputStream = vectorFromBitString(bitString);
        for (int i = 0; i < bitString.length(); i++) {
            if (bitString.charAt(i) == '1') {
                assertTrue(bitInputStream.readBit());
            } else {
                assertFalse(bitInputStream.readBit());
            }
        }
    }

    @Test
    void testReadSixBitString() {
        String bitString = "000000 000001";
        BitInputStream bitInputStream = vectorFromBitString(bitString);
        assertEquals("AB", bitInputStream.readString(() -> 12));
    }

    private BitInputStream vectorFromBitString(String bits) {
        return BitInputStream.from(bytesFromBitString(bits));
    }

    private byte[] bytesFromBitString(String bitString) {
        String spaceTrimmed = bitString.replaceAll(" ", "");
        byte[] bytes = new byte[(int) Math.ceil(spaceTrimmed.length() / 8.0)];
        int j = 0;
        for (int i = 0; i < spaceTrimmed.length(); i += 8) {
            int endIndex;
            if (i + 8 < spaceTrimmed.length()) {
                endIndex = i + 8;
            } else {
                endIndex = spaceTrimmed.length();
            }
            String sub = spaceTrimmed.substring(i, endIndex);
            sub = sub.length() == 8
                    ? sub
                    : sub + Stream.generate(() -> "0")
                                  .limit(8 - sub.length())
                                  .collect(Collectors.joining());
            bytes[j++] = (byte) (Integer.parseInt(sub, 2));
        }
        return bytes;
    }
}
