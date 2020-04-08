package com.iabtcf.v2.encoder;

import com.iabtcf.v2.Field;
import com.iabtcf.v2.SegmentType;

import java.util.Base64;

/**
 * @author ewhite 3/26/20
 */
public class PublisherTCEncoder {

	static String writePublisherTCString(final PublisherTCBuilder builder) {
		final BitOutputStream bs = new BitOutputStream();
		bs.write(Field.PublisherTC.SEGMENT_TYPE, SegmentType.PUBLISHER_TC.getValue());
		BitOutputStreamUtil.write(bs, Field.PublisherTC.PUB_PURPOSE_CONSENT, builder.publisherPurposes);
		BitOutputStreamUtil.write(bs, Field.PublisherTC.PUB_PURPOSES_LI_TRANSPARENCY, builder.publisherPurposesLI);
		final int numCustomPurposes = builder.publisherCustomPurposes.cardinality();
		bs.write(Field.PublisherTC.NUM_CUSTOM_PURPOSES, numCustomPurposes);
		for (int i = 0; i < numCustomPurposes; i++) {
			bs.write(builder.publisherCustomPurposes.get(i));
		}
		for (int i = 0; i < numCustomPurposes; i++) {
			bs.write(builder.publisherCustomPurposesLI.get(i));
		}
		return Base64.getEncoder().encodeToString(bs.toByteArray());
	}
}
