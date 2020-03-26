package com.iabtcf.v2.encoder;

import com.iabtcf.v2.Field.CoreString;
import com.iabtcf.v2.Field.PublisherRestrictions;
import com.iabtcf.v2.Purpose;
import com.iabtcf.v2.RestrictionType;

import java.util.Base64;
import java.util.EnumMap;

import static com.iabtcf.v2.encoder.TCModelEncoder.writeRange;

/**
 * @author ewhite 3/26/20
 */
class CoreStringEncoder {

	static String writeCoreString(final CoreStringBuilder builder) {
		final Bits bits = new Bits();
		bits.write(CoreString.VERSION, builder.version);
		bits.write(CoreString.CREATED, builder.created);
		bits.write(CoreString.LAST_UPDATED, builder.lastUpdated);
		bits.write(CoreString.CMP_ID, builder.cmpId);
		bits.write(CoreString.CMP_VERSION, builder.cmpVersion);
		bits.write(CoreString.CONSENT_SCREEN, builder.consentScreen);
		bits.write(CoreString.CONSENT_LANGUAGE, builder.consentLanguage);
		bits.write(CoreString.VENDOR_LIST_VERSION, builder.vendorListVersion);
		bits.write(CoreString.TCF_POLICY_VERSION, builder.policyVersion);
		bits.write(builder.isServiceSpecific);
		bits.write(builder.useNonStandardStacks);
		bits.write(CoreString.SPECIAL_FEATURE_OPT_INS, builder.specialFeaturesOptInts);
		bits.write(CoreString.PURPOSES_CONSENT, builder.purposesConsent);
		bits.write(CoreString.PURPOSE_LI_TRANSPARENCY, builder.purposesLITransparency);
		bits.write(builder.isPurposeOneTreatment);
		bits.write(CoreString.PUBLISHER_CC, builder.publisherCountryCode);
		writeRange(bits, builder.vendorConsents);
		writeRange(bits, builder.vendorLegitimateInterests);
		writePubRestriction(bits, builder.publisherRestrictions);
		return Base64.getEncoder().encodeToString(bits.toByteArray());
	}

	private static void writePubRestriction(Bits bits, EnumMap<Purpose, EnumMap<RestrictionType, RangeData>> restrictions) {
		int numRestrictions = restrictions.values().stream().mapToInt(EnumMap::size).sum();
		bits.write(PublisherRestrictions.NUM_PUB_RESTRICTIONS, numRestrictions);
		restrictions.forEach((p, value) -> value.forEach((r, vendors) -> {
			bits.write(PublisherRestrictions.PURPOSE_ID, p.getId());
			bits.write(PublisherRestrictions.RESTRICTION_TYPE, r.getValue());
			bits.write(PublisherRestrictions.NUM_ENTRIES, vendors.size());
			vendors.getRanges().forEach(range -> {
				bits.write(PublisherRestrictions.IS_A_RANGE, range.lower != range.upper);
				bits.write(PublisherRestrictions.START_OR_ONLY_VENDOR_ID, range.lower);
				if (range.upper != range.lower){
					bits.write(PublisherRestrictions.END_VENDOR_ID, range.upper);
				}
			});
		}));
	}
}
