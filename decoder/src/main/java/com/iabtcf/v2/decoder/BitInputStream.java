package com.iabtcf.v2.decoder;

import com.iabtcf.v2.Field;

import java.time.Instant;
import java.util.Base64;
import java.util.BitSet;

/**
 * @author SleimanJneidi
 * @author evanwht1
 */
class BitInputStream {

    private final byte[] data;
    private int position = 0;

    private BitInputStream(byte[] bytes) {
        this.data = bytes;
    }

    static BitInputStream fromBase64String(final String str) {
        return BitInputStream.from(Base64.getUrlDecoder().decode(str));
    }

    static BitInputStream from(byte[] bytes) {
        return new BitInputStream(bytes);
    }

    int readInt(Field field) {
        return (int) readLong(field.getLength());
    }

    BitSet readBitSet(int length) {
        final BitSet set = new BitSet(length);
        for (int i = 1; i <= length; i++) {
            set.set(i, readBit());
        }
        return set;
    }

    Instant readInstant(Field field) {
        long epochDeci = readLong(field.getLength()) * 100;
        return Instant.ofEpochMilli(epochDeci);
    }

    String readString(Field field) {
        final StringBuilder sb = new StringBuilder(field.getLength() / 6);
        for (int i = 0; i < field.getLength(); i += 6) {
            char c = (char) (readLong(6) + 'A');
            sb.append(c);
        }
        return sb.toString();
    }

    boolean readBit(Field field) {
        return readBit();
    }

    /**
     * Reads a bit at position out of the byte it is stored in. The byte it is stored in is the position divided by Byte.SIZE and the position
     * in in the byte is the position mod Byte.SIZE. That is then right shifted by 7 - position in byte and anded with 1.
     *
     * @return whether or not the bit at the current position is a 1
     */
    boolean readBit() {
        final boolean b = (data[position / Byte.SIZE] >> (7 - (position % Byte.SIZE)) & 1) == 1;
        position++;
        return b;
    }

    /**
     * Reads a number of bits equal to length into a long. Each bit read shifts the previously read bits left making the first bit read the most significant
     * (Big endian)
     *
     * @param length number of bits to read
     * @return number representing the bits in big endian format but viewed as if only the right most length bits were used
     */
    long readLong(int length) {
        long num = 0L;
        int bitIndex = length - 1;
        for (int i = 1; i <= length; i++) {
            if (readBit()) {
                num |= 1L << bitIndex;
            }
            bitIndex--;
        }
        return num;
    }
}
