package com.iabtcf.v2.encoder;

import com.iabtcf.v2.CoreString;
import com.iabtcf.v2.Purpose;
import com.iabtcf.v2.RestrictionType;
import com.iabtcf.v2.SpecialFeature;
import com.iabtcf.v2.TCModel;
import com.iabtcf.v2.decoder.TCModelDecoder;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author ewhite 4/8/20
 */
public class CoreStringEncoderTest {

	@Test
	void testEncodeCoreString() {
		CoreString.Builder builder = CoreString.newBuilder()
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

		final CoreString build = builder.build();
		final String encode = CoreStringEncoder.encode(build);
		final TCModel decode = TCModelDecoder.decode(encode);
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
	}
}
