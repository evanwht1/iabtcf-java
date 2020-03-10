package com.iabtcf.decoder;

import com.iabtcf.CoreString;
import com.iabtcf.TCModel;
import com.iabtcf.OutOfBandConsent;
import com.iabtcf.PublisherTC;

import java.util.BitSet;
import java.util.Objects;

/**
 * @author SleimanJneidi
 * @author evanwht1
 */
class BitVectorGDPRTCModel implements TCModel {

    private final CoreString coreString;
    private final OutOfBandConsent outOfBandVendors;
    private final PublisherTC publisherPurposes;

    private BitVectorGDPRTCModel(final Builder b) {
        coreString = b.coreString;
        if (b.disclosedVendors != Constants.EMPTY_BIT_SET || b.allowedVendors != Constants.EMPTY_BIT_SET) {
            outOfBandVendors = new OutOfBandVendors(b.disclosedVendors, b.allowedVendors);
        } else {
            outOfBandVendors = OutOfBandVendors.EMPTY;
        }
        publisherPurposes = b.publisherPurposes;
    }

    static Builder newBuilder() {
        return new Builder();
    }

    @Override
    public CoreString getCoreString() {
        return coreString;
    }

    @Override
    public OutOfBandConsent getOutOfBandSignals() {
        return outOfBandVendors;
    }

    @Override
    public PublisherTC getPublisherPurposesTC() {
        return publisherPurposes;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final BitVectorGDPRTCModel that = (BitVectorGDPRTCModel) o;
        return coreString.equals(that.coreString) &&
               outOfBandVendors.equals(that.outOfBandVendors) &&
               Objects.equals(publisherPurposes, that.publisherPurposes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coreString, outOfBandVendors, publisherPurposes);
    }

    @Override
    public String toString() {
        return "BitVectorGDPRTransparencyAndConsent{" +
               "coreString: " + coreString +
               ", outOfBandVendors: " + outOfBandVendors +
               ", publisherPurposes: " + publisherPurposes +
               '}';
    }

    static final class Builder {

        private CoreString coreString;
        private BitSet disclosedVendors = Constants.EMPTY_BIT_SET;
        private BitSet allowedVendors = Constants.EMPTY_BIT_SET;
        private PublisherTC publisherPurposes = PublisherTCImpl.EMPTY;

        private Builder() {}

        Builder coreString(final CoreString coreString) {
            this.coreString = coreString;
            return this;
        }

        Builder disclosedVendors(final BitSet disclosedVendors) {
            this.disclosedVendors = disclosedVendors;
            return this;
        }

        Builder allowedVendors(final BitSet allowedVendors) {
            this.allowedVendors = allowedVendors;
            return this;
        }

        Builder publisherPurposes(final PublisherTC publisherPurposes) {
            this.publisherPurposes = publisherPurposes;
            return this;
        }

        TCModel build() {
            return new BitVectorGDPRTCModel(this);
        }
    }
}
