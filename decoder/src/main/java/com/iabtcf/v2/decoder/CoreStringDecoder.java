package com.iabtcf.v2.decoder;

import com.iabtcf.v2.CoreString;
import com.iabtcf.v2.RestrictionType;

import java.util.BitSet;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import static com.iabtcf.v2.Field.CoreString.CMP_ID;
import static com.iabtcf.v2.Field.CoreString.CMP_VERSION;
import static com.iabtcf.v2.Field.CoreString.CONSENT_LANGUAGE;
import static com.iabtcf.v2.Field.CoreString.CONSENT_SCREEN;
import static com.iabtcf.v2.Field.CoreString.CREATED;
import static com.iabtcf.v2.Field.CoreString.IS_SERVICE_SPECIFIC;
import static com.iabtcf.v2.Field.CoreString.LAST_UPDATED;
import static com.iabtcf.v2.Field.CoreString.PUBLISHER_CC;
import static com.iabtcf.v2.Field.CoreString.PURPOSES_CONSENT;
import static com.iabtcf.v2.Field.CoreString.PURPOSE_LI_TRANSPARENCY;
import static com.iabtcf.v2.Field.CoreString.PURPOSE_ONE_TREATMENT;
import static com.iabtcf.v2.Field.CoreString.SPECIAL_FEATURE_OPT_INS;
import static com.iabtcf.v2.Field.CoreString.TCF_POLICY_VERSION;
import static com.iabtcf.v2.Field.CoreString.USE_NON_STANDARD_STACKS;
import static com.iabtcf.v2.Field.CoreString.VENDOR_LIST_VERSION;
import static com.iabtcf.v2.Field.PublisherRestrictions.NUM_PUB_RESTRICTIONS;
import static com.iabtcf.v2.Field.PublisherRestrictions.PURPOSE_ID;
import static com.iabtcf.v2.Field.PublisherRestrictions.RESTRICTION_TYPE;

/**
 * @author evanwht1
 */
class CoreStringDecoder {

	/**
	 * Builds a {@link CoreString} from the given bit input stream.
	 *
	 * @param bitInputStream BitInputStream created from the first segment of the web safe 64 encoded TC string
	 * @return a CoreString with all fields parsed
	 */
	static CoreString decode(final int version, BitInputStream bitInputStream) {
		// Read fields in order!
		return CoreString.newBuilder()
		                     .version(version)
		                     .consentRecordCreated(bitInputStream.readInstant(CREATED))
		                     .consentRecordLastUpdated(bitInputStream.readInstant(LAST_UPDATED))
		                     .consentManagerProviderId(bitInputStream.readInt(CMP_ID))
		                     .consentManagerProviderVersion(bitInputStream.readInt(CMP_VERSION))
		                     .consentScreen(bitInputStream.readInt(CONSENT_SCREEN))
		                     .consentLanguage(bitInputStream.readString(CONSENT_LANGUAGE))
		                     .vendorListVersion(bitInputStream.readInt(VENDOR_LIST_VERSION))
		                     .policyVersion(bitInputStream.readInt(TCF_POLICY_VERSION))
		                     .isServiceSpecific(bitInputStream.readBit(IS_SERVICE_SPECIFIC))
		                     .useNonStandardStacks(bitInputStream.readBit(USE_NON_STANDARD_STACKS))
		                     .specialFeatureOptIns(bitInputStream.readBitSet(SPECIAL_FEATURE_OPT_INS.getLength()))
		                     .purposeConsents(bitInputStream.readBitSet(PURPOSES_CONSENT.getLength()))
		                     .purposeLegitimateInterests(bitInputStream.readBitSet(PURPOSE_LI_TRANSPARENCY.getLength()))
		                     .isPurposeOneTreatment(bitInputStream.readBit(PURPOSE_ONE_TREATMENT))
		                     .publisherCountryCode(bitInputStream.readString(PUBLISHER_CC))
		                     .vendorConsents(VendorsDecoder.decode(bitInputStream))
		                     .vendorLegitimateInterests(VendorsDecoder.decode(bitInputStream))
		                     .publisherRestrictions(decodePublisherRestrictions(bitInputStream))
		                     .build();
	}

	/**
	 * Builds a map of purpose id to the restriction type and a list of vendors it applies to.
	 *
	 * @param bitInputStream bit input stream to read from
	 * @return map of purpose to publisher restriction and vendors it applies to
	 */
	private static Map<Integer, EnumMap<RestrictionType, BitSet>> decodePublisherRestrictions(BitInputStream bitInputStream) {
		final Map<Integer, EnumMap<RestrictionType, BitSet>> restrictions = new HashMap<>();
		int numberOfPublisherRestrictions = bitInputStream.readInt(NUM_PUB_RESTRICTIONS);

		for (int i = 0; i < numberOfPublisherRestrictions; i++) {
			int purpose = bitInputStream.readInt(PURPOSE_ID);
			int restrictionTypeId = bitInputStream.readInt(RESTRICTION_TYPE);
			RestrictionType restrictionType = RestrictionType.valueOf(restrictionTypeId);

			BitSet vendorIds = VendorsDecoder.vendorIdsFromRange(bitInputStream, numberOfPublisherRestrictions);

			restrictions.computeIfAbsent(purpose, p -> new EnumMap<>(RestrictionType.class))
						.put(restrictionType, vendorIds);
		}
		return restrictions;
	}
}
