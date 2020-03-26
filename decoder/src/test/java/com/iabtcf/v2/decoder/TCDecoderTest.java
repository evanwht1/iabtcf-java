package com.iabtcf.v2.decoder;

import com.iabtcf.v2.CoreString;
import com.iabtcf.v2.OutOfBandConsent;
import com.iabtcf.v2.PublisherTC;
import com.iabtcf.v2.Purpose;
import com.iabtcf.v2.RestrictionType;
import com.iabtcf.v2.TCModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


class TCDecoderTest {

    @Test
    void testDecodeAllSegments() {
        String tcString = "COtybn4PA_zT4KjACBENAPCIAEBAAECAAIAAAAAAAAAA.IBAgAAAgAIAwgAgAAAAEAAAACA.QAagAQAgAIAwgA.cAAAAAAAITg=";
        TCModel tc = TCModelDecoder.decode(tcString);

        assertTrue(tc.getOutOfBandConsent().isPresent());
        final OutOfBandConsent outOfBandSignals = tc.getOutOfBandConsent().get();
        assertNotNull(outOfBandSignals);
        assertTrue(outOfBandSignals.isVendorAllowed(12));
        assertTrue(outOfBandSignals.isVendorAllowed(23));
        assertTrue(outOfBandSignals.isVendorAllowed(37));
        assertTrue(outOfBandSignals.isVendorAllowed(47));
        assertTrue(outOfBandSignals.isVendorAllowed(48));
        assertTrue(outOfBandSignals.isVendorAllowed(53));
        assertTrue(outOfBandSignals.isVendorDisclosed(23));
        assertTrue(outOfBandSignals.isVendorDisclosed(37));
        assertTrue(outOfBandSignals.isVendorDisclosed(47));
        assertTrue(outOfBandSignals.isVendorDisclosed(48));
        assertTrue(outOfBandSignals.isVendorDisclosed(53));
        assertTrue(outOfBandSignals.isVendorDisclosed(65));
        assertTrue(outOfBandSignals.isVendorDisclosed(98));
        assertTrue(outOfBandSignals.isVendorDisclosed(129));
        assertTrue(tc.getPublisherTC().isPresent());
        final PublisherTC publisherTC = tc.getPublisherTC().get();
        assertNotNull(publisherTC);
        assertTrue(publisherTC.isPurposeConsented(1));
        assertTrue(publisherTC.isPurposeLegitimateInterest(24));
        assertTrue(publisherTC.isCustomPurposeConsented(2));
        assertTrue(publisherTC.isCustomPurposeLegitimateInterest(1));
        assertTrue(publisherTC.isCustomPurposeLegitimateInterest(2));
    }

    @Test
    void testParseWithOOBSignals() {
        String tcString = "COtybn4PA_zT4KjACBENAPCIAEBAAECAAIAAAAAAAAAA.IBAgAAAgAIAwgAgAAAAEAAAACA.QAagAQAgAIAwgA";
        TCModel tc = TCModelDecoder.decode(tcString);

        assertTrue(tc.getOutOfBandConsent().isPresent());
        final OutOfBandConsent outOfBandSignals = tc.getOutOfBandConsent().get();
        assertNotNull(outOfBandSignals);
        assertTrue(outOfBandSignals.isVendorAllowed(12));
        assertTrue(outOfBandSignals.isVendorAllowed(23));
        assertTrue(outOfBandSignals.isVendorAllowed(37));
        assertTrue(outOfBandSignals.isVendorAllowed(47));
        assertTrue(outOfBandSignals.isVendorAllowed(48));
        assertTrue(outOfBandSignals.isVendorAllowed(53));
        assertTrue(outOfBandSignals.isVendorDisclosed(23));
        assertTrue(outOfBandSignals.isVendorDisclosed(37));
        assertTrue(outOfBandSignals.isVendorDisclosed(47));
        assertTrue(outOfBandSignals.isVendorDisclosed(48));
        assertTrue(outOfBandSignals.isVendorDisclosed(53));
        assertTrue(outOfBandSignals.isVendorDisclosed(65));
        assertTrue(outOfBandSignals.isVendorDisclosed(98));
        assertTrue(outOfBandSignals.isVendorDisclosed(129));
    }

    @Test
    void testPublisherRestrictions() {
        String bitString =
                "0000100011101011100"
                + "1000000000000001010"
                + "0000001110101110010"
                + "0000000000000101000"
                + "0000110011111000000"
                + "0000000000000000100"
                + "0011010000000011110"
                + "0001000000000000000"
                + "0000000000000000000"
                + "0000000000000000000"
                + "0000000000000000000"
                + "0000000000000000000"
                + "0000000000000000000"
                + "000000000011" // NumPubRestrictions (1)
                + "000001" // PurposeId
                + "01" // restriction type Require Consent
                + "000000000001"
                + "0"
                + "0000000000000001"
                + "000010" // PurposeId
                + "00" // restriction type Not Allowed
                + "000000000001"
                + "0"
                + "0000000000000010"
                + "000011" // PurposeId
                + "10" // restriction REQUIRE_LEGITIMATE_INTEREST
                + "000000000001"
                + "0"
                + "0000000000000011";

        String base64CoreString = Util.base64FromBitString(bitString);
        TCModel tc = TCModelDecoder.decode(base64CoreString);
        final CoreString coreString = tc.getCoreString();

        assertEquals(RestrictionType.REQUIRE_CONSENT, coreString.getPublisherRestriction(Purpose.STORE_AND_ACCESS_INFO_ON_DEVICE, 1));
        assertEquals(RestrictionType.UNDEFINED, coreString.getPublisherRestriction(Purpose.STORE_AND_ACCESS_INFO_ON_DEVICE, 2));
        assertEquals(RestrictionType.NOT_ALLOWED, coreString.getPublisherRestriction(Purpose.SELECT_BASIC_ADS, 2));
        assertEquals(RestrictionType.REQUIRE_LEGITIMATE_INTEREST, coreString.getPublisherRestriction(Purpose.CREATE_PERSONALISED_ADS_PROFILE, 3));
        assertEquals(RestrictionType.UNDEFINED, coreString.getPublisherRestriction(Purpose.SELECT_PERSONAL_ADS, 1));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "COtybn4PA_zT4KjACBENAPCIAEBAAECAAIAAAAAAAAAA.IFoEUQQgAIQwgIwQABAEAAAAOIAACAIAAAAQAIAgEAACEAAAAAgAQBAAAAAAAGBAAgAAAAAAAFAAECAAAgAAQARAEQAAAAAJAAIAAgAAAYQEAAAQmAgBC3ZAYzUw",
            "COtybn4PA_zT4KjACBENAPCIAEBAAECAAIAAAAAAAAAA"
    })
    void testVanillaDecode(String tcString) {
        assertNotNull(TCModelDecoder.decode(tcString));
    }

    @ParameterizedTest
    @ValueSource(strings = {"BObdrPUOevsguAfDqFENCNAAAAAmeAAA", "DGH"})
    void testBadString(String badString) {
        assertThrows(UnsupportedOperationException.class, () -> TCModelDecoder.decode(badString));
    }

    @Test
    void testDefaultSegmentType() {
        final String publisherPurposes = "00000000"; // segment type
        final String base64CoreString = "COtybn4PA_zT4KjACBENAPCIAEBAAECAAIAAAAAAAAAA." + Util.base64FromBitString(publisherPurposes);

        final TCModel tc = TCModelDecoder.decode(base64CoreString);
        assertTrue(tc.getPublisherTC().isEmpty());
    }

}
