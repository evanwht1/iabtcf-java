package com.iabtcf.v2.encoder;

import com.iabtcf.v2.CoreString;
import com.iabtcf.v2.Field;
import com.iabtcf.v2.OutOfBandConsent;
import com.iabtcf.v2.PublisherTC;
import com.iabtcf.v2.Purpose;
import com.iabtcf.v2.RestrictionType;
import com.iabtcf.v2.SpecialFeature;
import com.iabtcf.v2.TCModel;
import com.iabtcf.v2.decoder.TCModelDecoder;
import com.iabtcf.v2.decoder.exceptions.UnsupportedVersionException;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author evanwht1@gmail.com
 */
public class TCModelEncoderTest {

	@Test
	void testEncodeFullString() throws UnsupportedVersionException {
		final TCModelBuilder builder = new TCModelBuilder();

		builder.getCoreStringBuilder()
		       .consentLanguage("en")
		       .addPurposeConsent(Purpose.STORE_AND_ACCESS_INFO_ON_DEVICE)
		       .addPurposesLegitimateInterest(Purpose.SELECT_BASIC_ADS)
		       .addVendorConsent(3)
		       .addVendorLegitimateInterest(4)
		       .addPublisherRestriction(5, Purpose.CREATE_PERSONALISED_ADS_PROFILE, RestrictionType.NOT_ALLOWED)
		       .version(2)
		       .policyVersion(5)
		       .vendorListVersion(6)
		       .isServiceSpecific(true)
		       .useNonStandardStacks(true)
		       .isPurposeOneTreatment(true)
		       .publisherCountryCode("US")
		       .consentScreen(7)
		       .consentRecordCreated(Instant.ofEpochMilli(10000L))
		       .consentRecordLastUpdated(Instant.ofEpochMilli(11000L))
		       .consentManagerProviderId(8)
		       .consentManagerProviderVersion(9)
		       .addSpecialFeatureOptedIn(SpecialFeature.USE_PRECISE_GEOLOCATION_DATA);

		builder.getOutOfBandBuilder()
		       .addAllowedVendor(10)
		       .addDisclosedVendor(11);

		builder.getPublisherTCBuilder()
		       .addPurposeConsent(12)
		       .addPurposeLegitimateInterest(13)
		       .addCustomPurposeConsent(14)
		       .addCustomPurposeLegitimateInterest(15);

		final String tcString = builder.buildTCString();
		final TCModel decode = TCModelDecoder.decode(tcString);

		final CoreString coreString = decode.getCoreString();
		assertEquals(2, coreString.getVersion());
		assertEquals(5, coreString.getPolicyVersion());
		assertEquals(6, coreString.getVendorListVersion());
		assertEquals(7, coreString.getConsentScreen());
		assertEquals(8, coreString.getCmpId());
		assertEquals(9, coreString.getCmpVersion());
		assertEquals(Instant.ofEpochMilli(10000L), coreString.getCreated());
		assertEquals(Instant.ofEpochMilli(11000L), coreString.getLastUpdated());
		assertTrue(coreString.isPurposeOneTreatment());
		assertTrue(coreString.isUseNonStandardStacks());
		assertTrue(coreString.isServiceSpecific());
		assertEquals("en", coreString.getConsentLanguage());
		assertEquals("US", coreString.getPublisherCountryCode());
		assertEquals(1, coreString.getAllConsentedPurposes().count());
		assertTrue(coreString.isPurposeConsented(Purpose.STORE_AND_ACCESS_INFO_ON_DEVICE.getId()));
		assertTrue(coreString.isPurposeLegitimateInterest(Purpose.SELECT_BASIC_ADS.getId()));
		assertEquals(1, coreString.getAllLegitimateInterestPurposes().count());
		assertTrue(coreString.isSpecialFeatureOptedIn(SpecialFeature.USE_PRECISE_GEOLOCATION_DATA.getId()));
		assertEquals(1, coreString.getAllOptedInSpecialFeatures().count());
		assertTrue(coreString.isVendorConsented(3));
		assertEquals(1, coreString.getAllConsentedVendors().count());
		assertTrue(coreString.isVendorLegitimateInterest(4));
		assertEquals(1, coreString.getAllLegitimateInterestVendors().count());

		assertTrue(decode.getOutOfBandConsent().isPresent());
		final OutOfBandConsent outOfBandConsent = decode.getOutOfBandConsent().get();
		assertTrue(outOfBandConsent.isVendorAllowed(10));
		assertEquals(1, outOfBandConsent.getAllAllowedVendors().count());
		assertTrue(outOfBandConsent.isVendorDisclosed(11));
		assertEquals(1, outOfBandConsent.getAllDisclosedVendors().count());

		assertTrue(decode.getPublisherTC().isPresent());
		final PublisherTC publisherTC1 = decode.getPublisherTC().get();
		assertTrue(publisherTC1.isPurposeConsented(12));
		assertEquals(1, publisherTC1.getAllConsentedPurposes().count());
		assertTrue(publisherTC1.isPurposeLegitimateInterest(13));
		assertEquals(1, publisherTC1.getAllLegitimateInterestPurposes().count());
		assertTrue(publisherTC1.isCustomPurposeConsented(14));
		assertEquals(1, publisherTC1.getAllConsentedCustomPurposes().count());
		assertTrue(publisherTC1.isCustomPurposeLegitimateInterest(15));
		assertEquals(1, publisherTC1.getAllLegitimateInterestCustomPurposes().count());
	}

	@Test
	void testShouldRangeEncode() {
		int maxId = Field.Vendors.NUM_ENTRIES.getLength() +
		            Field.Vendors.IS_A_RANGE.getLength() +
		            Field.Vendors.START_OR_ONLY_VENDOR_ID.getLength();
		assertFalse(TCModelEncoder.shouldRangeEncode(maxId, null));

		RangeData rangeData = new RangeData();
		rangeData.add(1, 3, 5, 7, 96);
		// 12 + (17 * 5) = 97
		assertFalse(TCModelEncoder.shouldRangeEncode(rangeData.getMaxId(), rangeData));

		rangeData.add(115);
		// 12 + (17 * 6) = 114
		assertTrue(TCModelEncoder.shouldRangeEncode(rangeData.getMaxId(), rangeData));
	}
}
