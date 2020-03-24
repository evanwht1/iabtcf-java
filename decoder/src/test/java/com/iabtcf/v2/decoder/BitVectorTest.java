package com.iabtcf.v2.decoder;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BitVectorTest {

    @Test
    void testReadSmall() {
        String bitString = "0000 1000 0000 0001";
        BitVector bitVector = vectorFromBitString(bitString);
        assertEquals(0, bitVector.readNextInt(() -> 4));
        assertEquals(128, bitVector.readNextInt(() -> 8));
        assertEquals(1, bitVector.readNextInt(() -> 4));
    }

    @Test
    void testReadInteger() {
        String bitString = "0111 1111 1111 1111 1111 1111 1111 1111";
        BitVector bitVector = vectorFromBitString(bitString);
        assertEquals(Integer.MAX_VALUE, bitVector.readNextInt(() -> 32));
    }

    @Test
    void testReadLong() {
        String bitString = "0111 1111 1111 1111 1111 1111 1111 1111" + "1111 1111 1111 1111 1111 1111 1111 1111";
        BitVector bitVector = vectorFromBitString(bitString);
        assertEquals(Long.MAX_VALUE, bitVector.readNextLong(64));
    }

    @Test
    void tesReadInstant() {
        String bitString = "001110101101110010100111000111000100";
        BitVector bitVector = vectorFromBitString(bitString);
        assertEquals(Instant.parse("2020-01-26T18:19:25.200Z"), bitVector.readNextInstantFromDeciSecond(() -> 36));
    }

    @Test
    void tesReadEpochInstant() {
        String bitString = Stream.generate(() -> "0").limit(36).collect(Collectors.joining());
        BitVector bitVector = vectorFromBitString(bitString);
        assertEquals(Instant.EPOCH, bitVector.readNextInstantFromDeciSecond(() -> 36));
    }

    @Test
    void testReadBit() {
        String bitString = "10101010";
        BitVector bitVector = vectorFromBitString(bitString);
        for (int i = 0; i < bitString.length(); i++) {
            if (bitString.charAt(i) == '1') {
                assertTrue(bitVector.readNextBit());
            } else {
                assertFalse(bitVector.readNextBit());
            }
        }
    }

    @Test
    void testReadSixBitString() {
        String bitString = "000000 000001";
        BitVector bitVector = vectorFromBitString(bitString);
        assertEquals("AB", bitVector.readNextString(() -> 12));
    }

    private BitVector vectorFromBitString(String bits) {
        return BitVector.from(bytesFromBitString(bits));
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
