package com.iabtcf.v2.encoder;

import com.iabtcf.v2.PublisherTC;
import com.iabtcf.v2.TCModel;
import com.iabtcf.v2.decoder.TCModelDecoder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author ewhite 4/8/20
 */
public class PublisherTCEncoderTest {

	@Test
	void testEncode() {
		final PublisherTC publisherTC = PublisherTC.newBuilder()
		                                     .addPurposeConsent(1)
		                                     .addPurposeLegitimateInterest(2)
		                                     .addCustomPurposeConsent(3)
		                                     .addCustomPurposeLegitimateInterest(4)
		                                     .build();
		final String encode = PublisherTCEncoder.encode(publisherTC);
		TCModel tcModel = TCModelDecoder.decode("COtybn4PA_zT4KjACBENAPCIAEBAAECAAIAAAAAAAAAA." + encode);
		assertTrue(tcModel.getPublisherTC().isPresent());
		final PublisherTC publisherTC1 = tcModel.getPublisherTC().get();
		assertTrue(publisherTC1.isPurposeConsented(1));
		assertEquals(1, publisherTC1.getAllConsentedPurposes().count());
		assertTrue(publisherTC1.isPurposeLegitimateInterest(2));
		assertEquals(1, publisherTC1.getAllLegitimateInterestPurposes().count());
		assertTrue(publisherTC1.isCustomPurposeConsented(3));
		assertEquals(1, publisherTC1.getAllConsentedCustomPurposes().count());
		assertTrue(publisherTC1.isCustomPurposeLegitimateInterest(4));
		assertEquals(1, publisherTC1.getAllLegitimateInterestCustomPurposes().count());
	}
}
