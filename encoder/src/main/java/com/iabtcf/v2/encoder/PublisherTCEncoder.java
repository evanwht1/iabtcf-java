package com.iabtcf.v2.encoder;

import com.iabtcf.v2.Field;
import com.iabtcf.v2.PublisherTC;
import com.iabtcf.v2.SegmentType;

import java.util.Base64;

/**
 * @author ewhite 3/26/20
 */
public class PublisherTCEncoder {

	static String encode(final PublisherTC publisherTC) {
		final BitOutputStream bs = new BitOutputStream();
		bs.write(Field.PublisherTC.SEGMENT_TYPE, SegmentType.PUBLISHER_TC.getValue());
		bs.write(Field.PublisherTC.PUB_PURPOSE_CONSENT, publisherTC::isPurposeConsented);
		bs.write(Field.PublisherTC.PUB_PURPOSES_LI_TRANSPARENCY, publisherTC::isPurposeLegitimateInterest);
		final int numCustomPurposes = Math.max(publisherTC.getAllConsentedCustomPurposes().max().orElse(0), publisherTC.getAllLegitimateInterestCustomPurposes().max().orElse(0));
		bs.write(Field.PublisherTC.NUM_CUSTOM_PURPOSES, numCustomPurposes);
		for (int i = 1; i <= numCustomPurposes; i++) {
			bs.write(publisherTC.isCustomPurposeConsented(i));
		}
		for (int i = 1; i <= numCustomPurposes; i++) {
			bs.write(publisherTC.isCustomPurposeLegitimateInterest(i));
		}
		return Base64.getEncoder().encodeToString(bs.toByteArray());
	}
}
