package com.iabtcf.v2.decoder;

import com.iabtcf.v2.Field;

import java.time.Instant;
import java.util.BitSet;

/**
 * @author SleimanJneidi
 * @author evanwht1
 */
class BitVector {

    private final byte[] data;
    private int position = 0;

    private BitVector(byte[] bytes) {
        this.data = bytes;
    }

    static BitVector from(byte[] bytes) {
        return new BitVector(bytes);
    }

    int readNextInt(Field field) {
        return (int) readNextLong(field.getLength());
    }

    BitSet readNextBitSet(int length) {
        final BitSet set = new BitSet(length);
        for (int i = 1; i <= length; i++) {
            set.set(i, readNextBit());
        }
        return set;
    }

    Instant readNextInstantFromDeciSecond(Field field) {
        long epochDeci = readNextLong(field.getLength()) * 100;
        return Instant.ofEpochMilli(epochDeci);
    }

    String readNextString(Field field) {
        final StringBuilder sb = new StringBuilder(field.getLength() / 6);
        for (int i = 0; i < field.getLength(); i += 6) {
            char c = (char) (readNextLong(6) + 'A');
            sb.append(c);
        }
        return sb.toString();
    }

    boolean readNextBit(Field field) {
        return readNextBit();
    }

    /**
     * Reads a bit at position out of the byte it is stored in. The byte it is stored in is the position divided by Byte.SIZE and the position
     * in in the byte is the position mod Byte.SIZE. That is then right shifted by 7 - position in byte and anded with 1.
     *
     * @return whether or not the bit at the current position is a 1
     */
    boolean readNextBit() {
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
    long readNextLong(int length) {
        long num = 0L;
        int bitIndex = length - 1;
        for (int i = 1; i <= length; i++) {
            if (readNextBit()) {
                num |= 1L << bitIndex;
            }
            bitIndex--;
        }
        return num;
    }
}
