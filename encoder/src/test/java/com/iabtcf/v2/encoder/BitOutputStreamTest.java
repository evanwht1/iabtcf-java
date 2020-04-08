package com.iabtcf.v2.encoder;

import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.util.BitSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author evanwht1@gmail.com
 */
public class BitOutputStreamTest {

	@Test
	void testWriteByteMax() {
		final BitOutputStream bitOutputStream = new BitOutputStream();
		bitOutputStream.write(Byte.SIZE, Byte.MAX_VALUE);
		byte[] bytes = bitOutputStream.toByteArray();
		assertEquals(1, bytes.length);
		assertEquals(Byte.MAX_VALUE, (bytes[0]));
	}

	@Test
	void testWritFullByte() {
		final BitOutputStream bitOutputStream = new BitOutputStream();
		bitOutputStream.write(Byte.SIZE, 0xff);
		byte[] bytes = bitOutputStream.toByteArray();
		assertEquals(1, bytes.length);
		assertEquals(0xff, (bytes[0] & 0xff));
	}

	@Test
	void testWriteInt() {
		final BitOutputStream bitOutputStream = new BitOutputStream();
		bitOutputStream.write(Integer.SIZE, Integer.MAX_VALUE);
		byte[] bytes = bitOutputStream.toByteArray();
		assertEquals((Integer.SIZE / Byte.SIZE), bytes.length);
		assertEquals(Integer.MAX_VALUE, ByteBuffer.wrap(bytes).getInt());
	}

	@Test
	void testWriteLong() {
		final BitOutputStream bitOutputStream = new BitOutputStream();
		bitOutputStream.write(Long.SIZE, Long.MAX_VALUE);
		byte[] bytes = bitOutputStream.toByteArray();
		assertEquals((Long.SIZE / Byte.SIZE), bytes.length);
		assertEquals(Long.MAX_VALUE, ByteBuffer.wrap(bytes).getLong());
	}

	@Test
	void testWriteString() {
		final BitOutputStream bitOutputStream = new BitOutputStream();
		bitOutputStream.write("AB");
		byte[] bytes = bitOutputStream.toByteArray();
		assertEquals(2, bytes.length);
		assertEquals(0, bytes[0]);
		assertEquals(16, bytes[1]);
	}

	@Test
	void testWriteBitSet() {
		final BitOutputStream bs = new BitOutputStream();
		final BitSet bitSet = new BitSet();
		bitSet.set(1);
		bitSet.set(6);
		bs.write(Byte.SIZE, bitSet::get);
		byte[] bytes = bs.toByteArray();
		assertEquals(1, bytes.length);
		// bit sets are 1 indexed but we write in 0 index so 0th and 5th bit should be set resulting in -124
		assertEquals(-124, bytes[0]);
	}
}
