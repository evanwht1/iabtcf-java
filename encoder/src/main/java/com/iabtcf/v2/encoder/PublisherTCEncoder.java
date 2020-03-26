package com.iabtcf.v2.encoder;

import com.iabtcf.v2.Field;
import com.iabtcf.v2.SegmentType;

import java.util.Base64;

/**
 * @author ewhite 3/26/20
 */
public class PublisherTCEncoder {

	static String writePublisherTCString(final PublisherTCBuilder builder) {
		final Bits bits = new Bits();
		bits.write(Field.PublisherTC.SEGMENT_TYPE, SegmentType.PUBLISHER_TC.getValue());
		bits.write(Field.PublisherTC.PUB_PURPOSE_CONSENT, builder.publisherPurposes);
		bits.write(Field.PublisherTC.PUB_PURPOSES_LI_TRANSPARENCY, builder.publisherPurposesLI);
		final int numCustomPurposes = builder.publisherCustomPurposes.cardinality();
		bits.write(Field.PublisherTC.NUM_CUSTOM_PURPOSES, numCustomPurposes);
		for (int i = 0; i < numCustomPurposes; i++) {
			bits.write(builder.publisherCustomPurposes.get(i));
		}
		for (int i = 0; i < numCustomPurposes; i++) {
			bits.write(builder.publisherCustomPurposesLI.get(i));
		}
		return Base64.getEncoder().encodeToString(bits.toByteArray());
	}
}
