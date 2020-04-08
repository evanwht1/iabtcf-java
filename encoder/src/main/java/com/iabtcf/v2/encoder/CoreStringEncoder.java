package com.iabtcf.v2.encoder;

import com.iabtcf.v2.Field.CoreString;
import com.iabtcf.v2.Field.PublisherRestrictions;
import com.iabtcf.v2.PublisherRestriction;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.iabtcf.v2.encoder.TCModelEncoder.writeRange;

/**
 * @author ewhite 3/26/20
 */
class CoreStringEncoder {

	static String encode(final com.iabtcf.v2.CoreString coreString) {
		final BitOutputStream bs = new BitOutputStream();
		bs.write(CoreString.VERSION, coreString.getVersion());
		bs.write(CoreString.CREATED, coreString.getCreated());
		bs.write(CoreString.LAST_UPDATED, coreString.getLastUpdated());
		bs.write(CoreString.CMP_ID, coreString.getCmpId());
		bs.write(CoreString.CMP_VERSION, coreString.getCmpVersion());
		bs.write(CoreString.CONSENT_SCREEN, coreString.getConsentScreen());
		bs.write(CoreString.CONSENT_LANGUAGE, coreString.getConsentLanguage());
		bs.write(CoreString.VENDOR_LIST_VERSION, coreString.getVendorListVersion());
		bs.write(CoreString.TCF_POLICY_VERSION, coreString.getPolicyVersion());
		bs.write(coreString.isServiceSpecific());
		bs.write(coreString.isUseNonStandardStacks());
		BitOutputStreamUtil.write(bs, CoreString.SPECIAL_FEATURE_OPT_INS, coreString::isSpecialFeatureOptedIn);
		BitOutputStreamUtil.write(bs, CoreString.PURPOSES_CONSENT, coreString::isPurposeConsented);
		BitOutputStreamUtil.write(bs, CoreString.PURPOSE_LI_TRANSPARENCY, coreString::isPurposeLegitimateInterest);
		bs.write(coreString.isPurposeOneTreatment());
		bs.write(CoreString.PUBLISHER_CC, coreString.getPublisherCountryCode());
		writeRange(bs, coreString.getAllConsentedVendors());
		writeRange(bs, coreString.getAllLegitimateInterestVendors());
		writePubRestriction(bs, coreString.getAllPublisherRestrictions());
		return Base64.getEncoder().encodeToString(bs.toByteArray());
	}

	private static void writePubRestriction(BitOutputStream bs, Stream<PublisherRestriction> restrictions) {
		final List<PublisherRestriction> restrictionList = restrictions.collect(Collectors.toList());
		bs.write(PublisherRestrictions.NUM_PUB_RESTRICTIONS, restrictionList.size());
		restrictionList.forEach(r -> {
			bs.write(PublisherRestrictions.PURPOSE_ID, r.getPurposeId());
			bs.write(PublisherRestrictions.RESTRICTION_TYPE, r.getRestrictionType().getValue());
			bs.write(PublisherRestrictions.NUM_ENTRIES, r.getAllVendors().count());
			final RangeData data = RangeData.from(r.getAllVendors());
			data.getRanges().forEach(range -> {
				bs.write(PublisherRestrictions.IS_A_RANGE, range.lower != range.upper);
				bs.write(PublisherRestrictions.START_OR_ONLY_VENDOR_ID, range.lower);
				if (range.upper != range.lower){
					bs.write(PublisherRestrictions.END_VENDOR_ID, range.upper);
				}
			});
		});
	}
}
