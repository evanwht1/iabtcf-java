package com.iabtcf.v2;

import java.util.BitSet;
import java.util.Objects;
import java.util.stream.IntStream;

/**
 * Legal Basis for processing a user's personal data achieved outside of the TCF.
 *
 * @author evanwht1
 */
public interface OutOfBandConsent {

	/**
	 * @param vendor id of the vendor
	 * @return if the vendor was disclosed on the CMP UI
	 */
	boolean isVendorDisclosed(final int vendor);

	/**
	 * @return all vendors disclosed on the CMP UI
	 */
	IntStream getAllDisclosedVendors();

	/**
	 * @param vendor id of the vendor
	 * @return is the publisher permits the vendor to use OOB legal bases
	 */
	boolean isVendorAllowed(final int vendor);

	/**
	 * @return all vendors that the publisher permits to use OOB legal bases
	 */
	IntStream getAllAllowedVendors();

	static Builder newBuilder() {
		return new Builder();
	}

	static Builder newBuilder(OutOfBandConsent oob) {
		return new Builder(oob);
	}

	class Builder {

		private BitSet disclosedVendors = new BitSet();
		private BitSet allowedVendors = new BitSet();

		public Builder() {}

		public Builder(OutOfBandConsent outOfBandConsent) {
			outOfBandConsent.getAllDisclosedVendors().forEach(disclosedVendors::set);
			outOfBandConsent.getAllAllowedVendors().forEach(allowedVendors::set);
		}

		public Builder addDisclosedVendor(final int vendor) {
			disclosedVendors.set(vendor);
			return this;
		}

		public Builder disclosedVendors(final BitSet set) {
			disclosedVendors = set;
			return this;
		}

		public Builder addAllowedVendor(final int vendor) {
			allowedVendors.set(vendor);
			return this;
		}

		public Builder allowedVendors(final BitSet set) {
			allowedVendors = set;
			return this;
		}

		public OutOfBandConsent build() {
			return new OutOfBandConsentImpl(this);
		}

		private static final class OutOfBandConsentImpl implements OutOfBandConsent {

			private BitSet disclosedVendors;
			private BitSet allowedVendors;

			OutOfBandConsentImpl(final Builder b) {
				this.disclosedVendors = b.disclosedVendors;
				this.allowedVendors = b.allowedVendors;
			}

			@Override
			public boolean isVendorDisclosed(final int vendor) {
				return disclosedVendors.get(vendor);
			}

			@Override
			public IntStream getAllDisclosedVendors() {
				return disclosedVendors.stream();
			}

			@Override
			public boolean isVendorAllowed(final int vendor) {
				return allowedVendors.get(vendor);
			}

			@Override
			public IntStream getAllAllowedVendors() {
				return allowedVendors.stream();
			}

			@Override
			public boolean equals(final Object o) {
				if (this == o) {
					return true;
				}
				if (o == null || getClass() != o.getClass()) {
					return false;
				}
				final OutOfBandConsentImpl that = (OutOfBandConsentImpl) o;
				return disclosedVendors.equals(that.disclosedVendors) &&
				       allowedVendors.equals(that.allowedVendors);
			}

			@Override
			public int hashCode() {
				return Objects.hash(disclosedVendors, allowedVendors);
			}

			@Override
			public String toString() {
				return "OutOfBandVendors{" +
				       "disclosedVendor: " + disclosedVendors +
				       ", allowedVendors: " + allowedVendors +
				       '}';
			}
		}
	}
}
