package com.iabtcf.v2.decoder;

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
	 * Builds a {@link CoreStringImpl} from the given bit vector.
	 *
	 * @param bitVector BitVector created from the first segment of the web safe 64 encoded TC string
	 * @return a CoreString with all fields parsed
	 */
	static CoreStringImpl decode(final int version, BitVector bitVector) {
		// Read fields in order!
		return CoreStringImpl.newBuilder()
		                     .version(version)
		                     .consentRecordCreated(bitVector.readNextInstantFromDeciSecond(CREATED))
		                     .consentRecordLastUpdated(bitVector.readNextInstantFromDeciSecond(LAST_UPDATED))
		                     .consentManagerProviderId(bitVector.readNextInt(CMP_ID))
		                     .consentManagerProviderVersion(bitVector.readNextInt(CMP_VERSION))
		                     .consentScreen(bitVector.readNextInt(CONSENT_SCREEN))
		                     .consentLanguage(bitVector.readNextString(CONSENT_LANGUAGE))
		                     .vendorListVersion(bitVector.readNextInt(VENDOR_LIST_VERSION))
		                     .policyVersion(bitVector.readNextInt(TCF_POLICY_VERSION))
		                     .isServiceSpecific(bitVector.readNextBit(IS_SERVICE_SPECIFIC))
		                     .useNonStandardStacks(bitVector.readNextBit(USE_NON_STANDARD_STACKS))
		                     .specialFeaturesOptInts(bitVector.readNextBitSet(SPECIAL_FEATURE_OPT_INS.getLength()))
		                     .purposesConsent(bitVector.readNextBitSet(PURPOSES_CONSENT.getLength()))
		                     .purposesLITransparency(bitVector.readNextBitSet(PURPOSE_LI_TRANSPARENCY.getLength()))
		                     .isPurposeOneTreatment(bitVector.readNextBit(PURPOSE_ONE_TREATMENT))
		                     .publisherCountryCode(bitVector.readNextString(PUBLISHER_CC))
		                     .vendorConsents(VendorsDecoder.decode(bitVector))
		                     .vendorLegitimateInterests(VendorsDecoder.decode(bitVector))
		                     .publisherRestrictions(decodePublisherRestrictions(bitVector))
		                     .build();
	}

	/**
	 * Builds a map of purpose id to the restrction type and a list of vendors it applies to.
	 *
	 * @param bitVector bit vector to read from
	 * @return map of purpose to publisher restriction and vendors it applies to
	 */
	static Map<Integer, EnumMap<RestrictionType, BitSet>> decodePublisherRestrictions(BitVector bitVector) {
		final Map<Integer, EnumMap<RestrictionType, BitSet>> restrictions = new HashMap<>();
		int numberOfPublisherRestrictions = bitVector.readNextInt(NUM_PUB_RESTRICTIONS);

		for (int i = 0; i < numberOfPublisherRestrictions; i++) {
			int purposeId = bitVector.readNextInt(PURPOSE_ID);
			int restrictionTypeId = bitVector.readNextInt(RESTRICTION_TYPE);
			RestrictionType restrictionType = RestrictionType.valueOf(restrictionTypeId);

			BitSet vendorIds = VendorsDecoder.vendorIdsFromRange(bitVector, numberOfPublisherRestrictions);

			restrictions.computeIfAbsent(purposeId, p -> new EnumMap<>(RestrictionType.class))
						.put(restrictionType, vendorIds);
		}
		return restrictions;
	}
}
