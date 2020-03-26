package com.iabtcf.v2.encoder;

import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.util.BitSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author evanwht1@gmail.com
 */
public class BitsTest {

	@Test
	void testWriteByteMax() {
		final Bits bits = new Bits();
		bits.write(Byte.SIZE, Byte.MAX_VALUE);
		byte[] bytes = bits.toByteArray();
		assertEquals(1, bytes.length);
		assertEquals(Byte.MAX_VALUE, (bytes[0]));
	}

	@Test
	void testWritFullByte() {
		final Bits bits = new Bits();
		bits.write(Byte.SIZE, 0xff);
		byte[] bytes = bits.toByteArray();
		assertEquals(1, bytes.length);
		assertEquals(0xff, (bytes[0] & 0xff));
	}

	@Test
	void testWriteInt() {
		final Bits bits = new Bits();
		bits.write(Integer.SIZE, Integer.MAX_VALUE);
		byte[] bytes = bits.toByteArray();
		assertEquals((Integer.SIZE / Byte.SIZE), bytes.length);
		assertEquals(Integer.MAX_VALUE, ByteBuffer.wrap(bytes).getInt());
	}

	@Test
	void testWriteLong() {
		final Bits bits = new Bits();
		bits.write(Long.SIZE, Long.MAX_VALUE);
		byte[] bytes = bits.toByteArray();
		assertEquals((Long.SIZE / Byte.SIZE), bytes.length);
		assertEquals(Long.MAX_VALUE, ByteBuffer.wrap(bytes).getLong());
	}

	@Test
	void testWriteString() {
		final Bits bits = new Bits();
		bits.write("AB");
		byte[] bytes = bits.toByteArray();
		assertEquals(2, bytes.length);
		assertEquals(0, bytes[0]);
		assertEquals(16, bytes[1]);
	}

	@Test
	void testWriteBitSet() {
		final Bits bits = new Bits();
		final BitSet bitSet = new BitSet();
		bitSet.set(1);
		bitSet.set(6);
		bits.write(Byte.SIZE, bitSet);
		byte[] bytes = bits.toByteArray();
		assertEquals(1, bytes.length);
		// bit sets are 1 indexed but we write in 0 index so 0th and 5th bit should be set resulting in -124
		assertEquals(-124, bytes[0]);
	}
}
