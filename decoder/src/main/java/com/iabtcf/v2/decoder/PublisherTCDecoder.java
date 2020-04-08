package com.iabtcf.v2.decoder;

import com.iabtcf.v2.Field;
import com.iabtcf.v2.PublisherTC;

import java.util.BitSet;

import static com.iabtcf.v2.Field.PublisherTC.NUM_CUSTOM_PURPOSES;
import static com.iabtcf.v2.Field.PublisherTC.PUB_PURPOSES_LI_TRANSPARENCY;
import static com.iabtcf.v2.Field.PublisherTC.PUB_PURPOSE_CONSENT;

/**
 * Decoding Util to read the Publisher Transparency and Consent fields from a TC String
 *
 * @author evanwht1
 */
class PublisherTCDecoder {

	/**
	 * Reads the Publisher Transparency and Consent fields from a BitVector.
	 *
	 * WARNING: This assumes you are at the correct position in the bit vector to read it's field. {@link TCModelDecoder}
	 *          will have already read the {@link Field.PublisherTC#SEGMENT_TYPE} and therefor the bit vector should be at the
	 *          4th bit before calling this. Unit tests should mimic this behavior accordingly.
	 *
	 * @param bitInputStream bit input stream to read from
	 * @return PublisherTC fields contained in the bit vector
	 */
	static PublisherTC decode(BitInputStream bitInputStream) {
		final BitSet consents = bitInputStream.readBitSet(PUB_PURPOSE_CONSENT.getLength());
		final BitSet liTransparency = bitInputStream.readBitSet(PUB_PURPOSES_LI_TRANSPARENCY.getLength());

		final int numberOfCustomPurposes = bitInputStream.readInt(NUM_CUSTOM_PURPOSES);
		final BitSet customPurposes = bitInputStream.readBitSet(numberOfCustomPurposes);
		final BitSet customLiTransparency = bitInputStream.readBitSet(numberOfCustomPurposes);

		return PublisherTC.newBuilder()
		                  .purposeConsents(consents)
		                  .purposeLegitimateInterest(liTransparency)
		                  .customPurposeConsents(customPurposes)
		                  .customPurposeLegitimateInterest(customLiTransparency)
		                  .build();
	}
}
