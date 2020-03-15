package com.iabtcf.v2;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RestrictionTypeTest {

	@Test
	public void testDecode() {
		assertEquals(RestrictionType.NOT_ALLOWED, RestrictionType.valueOf(RestrictionType.NOT_ALLOWED.getValue()));
		assertEquals(RestrictionType.REQUIRE_CONSENT, RestrictionType.valueOf(RestrictionType.REQUIRE_CONSENT.getValue()));
		assertEquals(RestrictionType.REQUIRE_LEGITIMATE_INTEREST, RestrictionType.valueOf(RestrictionType.REQUIRE_LEGITIMATE_INTEREST.getValue()));
		assertEquals(RestrictionType.UNDEFINED, RestrictionType.valueOf(RestrictionType.UNDEFINED.getValue()));
	}

}