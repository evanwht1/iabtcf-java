package com.iabtcf.v2.decoder;

import com.iabtcf.v2.Field;
import com.iabtcf.v2.PublisherTC;
import com.iabtcf.v2.SegmentType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * @author ewhite 2/22/20
 */
class PublisherTCDecoderTest {

	@Test
	void testPublisherPurposes() {
		String publisherPurposes =
				"011"                           // segment type
				+ "100000000000000000000000"    // PubPurposesConsent
				+ "000000000000000000000001"    // PubPurposesLITransparency
				+ "000010"                      // number of custom purposes
				+ "01"                          // CustomPurposesConsent
				+ "11";                          // CustomPurposesLITransparency
		final BitVector publisherTCVector = Util.vectorFromBitString(publisherPurposes);

		assertEquals(SegmentType.PUBLISHER_TC.getValue(), publisherTCVector.readNextInt(Field.PublisherTC.SEGMENT_TYPE));
		final PublisherTC publisherTC = PublisherTCDecoder.decode(publisherTCVector);

		assertTrue(publisherTC.isPurposeConsented(1));
		assertTrue(publisherTC.isPurposeLegitimateInterest(24));
		assertTrue(publisherTC.isCustomPurposeConsented(2));
		assertTrue(publisherTC.isCustomPurposeLegitimateInterest(1));
		assertTrue(publisherTC.isCustomPurposeLegitimateInterest(2));
	}
}
