package com.iabtcf.v2;

import java.util.Objects;
import java.util.Optional;

/**
 * @author evanwht1
 */
public interface TCModel {

    CoreString getCoreString();

    Optional<OutOfBandConsent> getOutOfBandConsent();

    Optional<PublisherTC> getPublisherTC();

    static Builder newBuilder() {
        return new Builder();
    }

    static Builder newBuilder(final TCModel tcModel) {
        return new Builder(tcModel);
    }

    class Builder {

        private CoreString coreString;
        private OutOfBandConsent outOfBandConsent;
        private PublisherTC publisherTC;

        private Builder() {}

        private Builder(final TCModel tcModel) {
            coreString = tcModel.getCoreString();
            outOfBandConsent = tcModel.getOutOfBandConsent().orElse(null);
            publisherTC = tcModel.getPublisherTC().orElse(null);
        }

        public Builder coreString(final CoreString coreString) {
            this.coreString = coreString;
            return this;
        }

        public Builder outOfBandConsent(final OutOfBandConsent outOfBandConsent) {
            this.outOfBandConsent = outOfBandConsent;
            return this;
        }

        public Builder publisherTC(final PublisherTC publisherTC) {
            this.publisherTC = publisherTC;
            return this;
        }

        public TCModel build() {
            return new TCModelImpl(this);
        }

        private static final class TCModelImpl implements TCModel {

            private final CoreString coreString;
            private final OutOfBandConsent outOfBandConsent;
            private final PublisherTC publisherTC;

            private TCModelImpl(final Builder b) {
                if (b.coreString == null) {
                    throw new IllegalStateException("Must have a core string");
                }
                coreString = b.coreString;
                outOfBandConsent = b.outOfBandConsent;
                publisherTC = b.publisherTC;
            }

            @Override
            public CoreString getCoreString() {
                return coreString;
            }

            @Override
            public Optional<OutOfBandConsent> getOutOfBandConsent() {
                return Optional.ofNullable(outOfBandConsent);
            }

            @Override
            public Optional<PublisherTC> getPublisherTC() {
                return Optional.ofNullable(publisherTC);
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
                       outOfBandConsent.equals(that.outOfBandConsent) &&
                       Objects.equals(publisherTC, that.publisherTC);
            }

            @Override
            public int hashCode() {
                return Objects.hash(coreString, outOfBandConsent, publisherTC);
            }

            @Override
            public String toString() {
                return "BitVectorGDPRTransparencyAndConsent{" +
                       "coreString: " + coreString +
                       ", outOfBandVendors: " + outOfBandConsent +
                       ", publisherPurposes: " + publisherTC +
                       '}';
            }
        }
    }
}
