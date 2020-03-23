package com.iabtcf.v2.decoder;

import com.iabtcf.v2.CoreString;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static com.iabtcf.v2.Field.CoreString.VERSION;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author ewhite 2/22/20
 */
class CoreStringDecoderTest {

	 /**
	  * the string was created here https://www.iabtcf.com/#/encode
	  */
	@Test
	void testParse() {
		String base64CoreString = "COtybn4PA_zT4KjACBENAPCIAEBAAECAAIAAAAAAAAAA";
		final BitVector bitVector = Util.vectorFromBase64String(base64CoreString);
		CoreString coreString = CoreStringDecoder.decode(bitVector.readNextInt(VERSION), bitVector);

		assertEquals(2, coreString.getVersion());
		assertEquals(Instant.parse("2020-01-26T17:01:00Z"), coreString.getCreated());
		assertEquals(Instant.parse("2021-02-02T17:01:00Z"), coreString.getLastUpdated());
		assertEquals(675, coreString.getCmpId());
		assertEquals(2, coreString.getCmpVersion());
		assertEquals(1, coreString.getConsentScreen());
		assertEquals(15, coreString.getVendorListVersion());
		assertEquals(2, coreString.getPolicyVersion());
		assertEquals("EN", coreString.getConsentLanguage());
		assertEquals("AA", coreString.getPublisherCountryCode());
		assertFalse(coreString.isServiceSpecific());
		assertTrue(coreString.isPurposeOneTreatment());
		assertFalse(coreString.isUseNonStandardStacks());

		assertTrue(coreString.isSpecialFeatureOptedIn(1));
		assertTrue(coreString.isPurposeConsented(2));
		assertTrue(coreString.isPurposeConsented(10));
		assertTrue(coreString.isPurposeLegitimateInterest(2));
		assertTrue(coreString.isPurposeLegitimateInterest(9));
	}
}
