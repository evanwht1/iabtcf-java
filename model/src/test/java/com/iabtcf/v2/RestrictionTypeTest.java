package com.iabtcf.v2;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RestrictionTypeTest {

	@ParameterizedTest
	@EnumSource(RestrictionType.class)
	void testDecode(RestrictionType type) {
		assertEquals(type, RestrictionType.valueOf(type.getValue()));
	}

}