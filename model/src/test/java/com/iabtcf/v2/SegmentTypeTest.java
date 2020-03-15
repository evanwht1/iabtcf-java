package com.iabtcf.v2;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author evanwht1@gmail.com
 */
public class SegmentTypeTest {

	@Test
	public void testDecode() {
		assertEquals(SegmentType.DEFAULT, SegmentType.valueOf(SegmentType.DEFAULT.getValue()));
		assertEquals(SegmentType.DISCLOSED_VENDOR, SegmentType.valueOf(SegmentType.DISCLOSED_VENDOR.getValue()));
		assertEquals(SegmentType.ALLOWED_VENDOR, SegmentType.valueOf(SegmentType.ALLOWED_VENDOR.getValue()));
		assertEquals(SegmentType.PUBLISHER_TC, SegmentType.valueOf(SegmentType.PUBLISHER_TC.getValue()));
	}
}
