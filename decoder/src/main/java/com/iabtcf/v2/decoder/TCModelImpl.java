package com.iabtcf.v2.decoder;

import com.iabtcf.v2.CoreString;
import com.iabtcf.v2.OutOfBandConsent;
import com.iabtcf.v2.PublisherTC;
import com.iabtcf.v2.TCModel;

import java.util.BitSet;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * @author SleimanJneidi
 * @author evanwht1
 */
class TCModelImpl implements TCModel {

    private final Supplier<CoreString> coreStringSupplier;
    private CoreString coreString;

    private OutOfBandConsent outOfBandVendors;

    private final Supplier<PublisherTC> publisherPurposesSupplier;
    private PublisherTC publisherPurposes;

    private TCModelImpl(final Builder b) {
        coreStringSupplier = b.coreString;
        if (b.disclosedVendors != Constants.EMPTY_SUPPLIER || b.allowedVendors != Constants.EMPTY_SUPPLIER) {
            outOfBandVendors = new OutOfBandVendors(b.disclosedVendors, b.allowedVendors);
        }
        publisherPurposesSupplier = b.publisherPurposes;
    }

    static Builder newBuilder() {
        return new Builder();
    }

    @Override
    public CoreString getCoreString() {
        if (coreString == null) {
            coreString = coreStringSupplier.get();
        }
        return coreString;
    }

    @Override
    public Optional<OutOfBandConsent> getOutOfBandConsent() {
        return Optional.ofNullable(outOfBandVendors);
    }

    @Override
    public Optional<PublisherTC> getPublisherTC() {
        if (publisherPurposes == null && publisherPurposesSupplier != null) {
            publisherPurposes = publisherPurposesSupplier.get();
        }
        return Optional.ofNullable(publisherPurposes);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final TCModelImpl that = (TCModelImpl) o;
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

        private Supplier<CoreString> coreString;
        private Supplier<BitSet> disclosedVendors = Constants.EMPTY_SUPPLIER;
        private Supplier<BitSet> allowedVendors = Constants.EMPTY_SUPPLIER;
        private Supplier<PublisherTC> publisherPurposes;

        private Builder() {}

        Builder coreString(final Supplier<CoreString> coreString) {
            this.coreString = coreString;
            return this;
        }

        Builder disclosedVendors(final Supplier<BitSet> disclosedVendors) {
            this.disclosedVendors = disclosedVendors;
            return this;
        }

        Builder allowedVendors(final Supplier<BitSet> allowedVendors) {
            this.allowedVendors = allowedVendors;
            return this;
        }

        Builder publisherPurposes(final Supplier<PublisherTC> publisherPurposes) {
            this.publisherPurposes = publisherPurposes;
            return this;
        }

        TCModel build() {
            return new TCModelImpl(this);
        }
    }
}
