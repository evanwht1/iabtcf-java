package com.iabtcf.v2.encoder;

import com.iabtcf.v2.Field;
import com.iabtcf.v2.Field.CoreString;
import com.iabtcf.v2.Field.PublisherRestrictions;
import com.iabtcf.v2.Field.Vendors;
import com.iabtcf.v2.Purpose;
import com.iabtcf.v2.RestrictionType;
import com.iabtcf.v2.SegmentType;

import java.util.Base64;
import java.util.EnumMap;

/**
 * @author evanwht1@gmail.com
 */
class TCModelEncoder {

	static String encode(TCModelBuilder builder) {
		String tcfString = writeCoreString(builder);

		if (!builder.disclosedVendors.isEmpty()) {
			tcfString += "." + writeVendorString(SegmentType.DISCLOSED_VENDOR, builder.disclosedVendors);
		}
		if (!builder.allowedVendors.isEmpty()) {
			tcfString += "." + writeVendorString(SegmentType.ALLOWED_VENDOR, builder.disclosedVendors);
		}
		if (!builder.publisherPurposes.isEmpty() || !builder.publisherPurposesLI.isEmpty()
				|| ! builder.publisherCustomPurposes.isEmpty() || !builder.publisherCustomPurposesLI.isEmpty()) {
			tcfString += "." + writePublisherTCString(builder);
		}

		return tcfString;
	}

	private static String writeCoreString(final TCModelBuilder builder) {
		final Bits bits = new Bits();
		bits.write(CoreString.VERSION, builder.version);
		bits.write(CoreString.CREATED, builder.created.toEpochMilli());
		bits.write(CoreString.LAST_UPDATED, builder.lastUpdated.toEpochMilli());
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

	private static String writeVendorString(SegmentType type, RangeData data) {
		final Bits bits = new Bits();
		bits.write(Vendors.SEGMENT_TYPE, type.getValue());
		writeRange(bits, data);
		return Base64.getEncoder().encodeToString(bits.toByteArray());
	}

	private static String writePublisherTCString(final TCModelBuilder builder) {
		final Bits bits = new Bits();
		bits.write(Field.PublisherTC.SEGMENT_TYPE, SegmentType.PUBLISHER_TC.getValue());
		bits.write(Field.PublisherTC.PUB_PURPOSE_CONSENT, builder.publisherPurposes);
		bits.write(Field.PublisherTC.PUB_PURPOSES_LI_TRANSPARENCY, builder.purposesLITransparency);
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

	private static void writeRange(Bits bits, RangeData data) {
		int maxVendor = data.getMaxId();
		bits.write(Vendors.MAX_VENDOR_ID, maxVendor);
		if (!shouldRangeEncode(maxVendor, data)) {
			bits.write(Vendors.IS_RANGE_ENCODING, false);
			for (int i = 0; i < maxVendor; i++) {
				bits.write(data.get(i));
			}
		} else {
			bits.write(Vendors.IS_RANGE_ENCODING, true);
			bits.write(Vendors.NUM_ENTRIES, data.size());
			data.getRanges().forEach(r -> {
				bits.write(Vendors.IS_A_RANGE, r.isARange());
				bits.write(Vendors.START_OR_ONLY_VENDOR_ID, r.lower);
				if (r.isARange()) {
					bits.write(Vendors.END_VENDOR_ID, r.upper);
				}
			});
		}
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

	private static final int MIN_RANGE_ENCODING_LENGTH =
			Vendors.IS_A_RANGE.getLength() +
			Vendors.NUM_ENTRIES.getLength() +
			Vendors.START_OR_ONLY_VENDOR_ID.getLength();

	/**
	 * Determines whether it would be better to encode a list as a bit field or a range encoding. Logic follows that if
	 * the max vendor id is small enough and the cardinality of the set is small enough, encoding as a bit field will
	 * take up less space.
	 *
	 * @param maxId maxId in the set that is trying to be encoded
	 * @param rangeData number of ids that need to be set
	 * @return if the set would be better encoded as a bit field.
	 */
	private static boolean shouldRangeEncode(int maxId, RangeData rangeData) {
		// TODO actually think of correct logic here (might need to keep a running flag as things are added to the set in the builder)
		return maxId < MIN_RANGE_ENCODING_LENGTH || (maxId / rangeData.size()) < 10;
	}
}
