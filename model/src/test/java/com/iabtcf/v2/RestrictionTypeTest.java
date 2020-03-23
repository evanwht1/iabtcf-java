package com.iabtcf.v2;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RestrictionTypeTest {

	@Test
	void testDecode() {
		assertEquals(RestrictionType.NOT_ALLOWED, RestrictionType.valueOf(RestrictionType.NOT_ALLOWED.getValue()));
		assertEquals(RestrictionType.REQUIRE_CONSENT, RestrictionType.valueOf(RestrictionType.REQUIRE_CONSENT.getValue()));
		assertEquals(RestrictionType.REQUIRE_LEGITIMATE_INTEREST, RestrictionType.valueOf(RestrictionType.REQUIRE_LEGITIMATE_INTEREST.getValue()));
		assertEquals(RestrictionType.UNDEFINED, RestrictionType.valueOf(RestrictionType.UNDEFINED.getValue()));
	}

}