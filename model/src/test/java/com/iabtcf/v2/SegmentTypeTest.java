package com.iabtcf.v2;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author evanwht1@gmail.com
 */
class SegmentTypeTest {

	@ParameterizedTest
	@EnumSource(SegmentType.class)
	void testDecode(SegmentType type) {
		assertEquals(type, SegmentType.valueOf(type.getValue()));
	}
}
