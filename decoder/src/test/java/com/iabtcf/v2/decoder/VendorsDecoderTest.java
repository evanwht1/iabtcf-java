package com.iabtcf.v2.decoder;

import org.junit.jupiter.api.Test;

import java.util.BitSet;

import static com.iabtcf.v2.Field.Vendors.SEGMENT_TYPE;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author ewhite 2/22/20
 */
class VendorsDecoderTest {

	@Test
	void testParseDisclosedVendors() {
		String base64CoreString = "IBAgAAAgAIAwgAgAAAAEAAAACA";
		final BitInputStream bitInputStream = BitInputStream.fromBase64String(base64CoreString);
		bitInputStream.readInt(SEGMENT_TYPE);
		BitSet vendors = VendorsDecoder.decode(bitInputStream);
		assertTrue(vendors.get(23));
		assertTrue(vendors.get(37));
		assertTrue(vendors.get(47));
		assertTrue(vendors.get(48));
		assertTrue(vendors.get(53));
		assertTrue(vendors.get(65));
		assertTrue(vendors.get(98));
		assertTrue(vendors.get(129));
	}

	@Test
	void testParseAllowedVendors() {
		String base64CoreString = "QAagAQAgAIAwgA";
		final BitInputStream bitInputStream = BitInputStream.fromBase64String(base64CoreString);
		bitInputStream.readInt(SEGMENT_TYPE);
		BitSet vendors = VendorsDecoder.decode(bitInputStream);
		assertTrue(vendors.get(12));
		assertTrue(vendors.get(23));
		assertTrue(vendors.get(37));
		assertTrue(vendors.get(47));
		assertTrue(vendors.get(48));
		assertTrue(vendors.get(53));
	}
}
