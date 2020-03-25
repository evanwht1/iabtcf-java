package com.iabtcf.v2.encoder;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author evanwht1@gmail.com
 */
public class BitsTest {

	@Test
	void testWriteByte() {
		Bits bits = new Bits();
		bits.write(Byte.SIZE, Byte.MAX_VALUE);
		byte[] bytes = bits.toByteArray();
		assertEquals(1, bytes.length);
		System.out.println(Integer.toBinaryString(Byte.MAX_VALUE));
		System.out.println(Integer.toBinaryString(bytes[0]));
		assertEquals(Byte.MAX_VALUE, bytes[0]);
	}

	@Test
	void testWriteInt() {
		Bits bits = new Bits();
		bits.write(Integer.MAX_VALUE, Integer.SIZE);
		byte[] bytes = bits.toByteArray();
		assertEquals((Integer.SIZE / Byte.SIZE), bytes.length);
		assertEquals(Byte.MAX_VALUE, bytes[0]);
		assertEquals(Byte.MAX_VALUE, bytes[1]);
		assertEquals(Byte.MAX_VALUE, bytes[2]);
		assertEquals(Byte.MAX_VALUE, bytes[3]);
	}
}
