package com.iabtcf.v2;

import java.util.BitSet;
import java.util.Objects;
import java.util.stream.IntStream;

/**
 * The Publisher TC segment in the TC string represents publisher purposes transparency & consent signals which is
 * different than the other TC String segments; they are used to collect consumer purposes transparency & consent for
 * vendors. This segment supports the standard list of purposes defined by the TCF as well as Custom Purposes defined
 * by the publisher if they so choose.
 *
 * @author evanwht1
 */
public interface PublisherTC {

	/**
	 * The user's consent value for each Purpose established on the legal basis of consent, for the publisher. The
	 * Purposes are numerically identified and published in the Global Vendor List.
	 *
	 * @param purpose id of the purpose
	 * @return if the user consented to this purpose
	 */
	boolean isPurposeConsented(final int purpose);

	/**
	 * The user's consent value for each Purpose established on the legal basis of consent, for the publisher. The
	 * Purposes are numerically identified and published in the Global Vendor List.
	 *
	 * @return all purposes the user consented to
	 */
	IntStream getAllConsentedPurposes();

	/**
	 * The Purpose’s transparency requirements are met for each Purpose established on the legal basis of legitimate
	 * interest and the user has not exercised their “Right to Object” to that Purpose.
	 *
	 * By default or if the user has exercised their “Right to Object to a Purpose, the corresponding bit for that
	 * purpose is set to 0.
	 *
	 * @param purpose id of the purpose
	 * @return if the purpose's transparency requirement has been met
	 */
	boolean isPurposeLegitimateInterest(final int purpose);

	/**
	 * The Purpose’s transparency requirements are met for each Purpose established on the legal basis of legitimate
	 * interest and the user has not exercised their “Right to Object” to that Purpose.
	 *
	 * By default or if the user has exercised their “Right to Object to a Purpose, the corresponding bit for that
	 * purpose is set to 0.
	 *
	 * @return all purpose's that have met their transparency requirement
	 */
	IntStream getAllLegitimateInterestPurposes();

	/**
	 * @param customPurpose id of the custom purpose
	 * @return if the user consented to this custom purpose
	 */
	boolean isCustomPurposeConsented(final int customPurpose);

	/**
	 * @return all custom purposes the user consented to
	 */
	IntStream getAllConsentedCustomPurposes();

	/**
	 * @param customPurpose id of the custom purpose
	 * @return if the custom purpose's transparency requirement has been met
	 */
	boolean isCustomPurposeLegitimateInterest(final int customPurpose);

	/**
	 * @return all custom purpose's that have met their transparency requirement
	 */
	IntStream getAllLegitimateInterestCustomPurposes();

	static Builder newBuilder() {
		return new Builder();
	}

	static Builder newBuilder(final PublisherTC publisherTC) {
		return new Builder(publisherTC);
	}

	class Builder {

		private BitSet purposesConsent;
		private BitSet purposesLegitimateInterest;
		private BitSet customPurposesConsent;
		private BitSet customPurposesLegitimateInterest;

		private Builder() {
			purposesConsent = EmptyConstants.BIT_SET;
			purposesLegitimateInterest = EmptyConstants.BIT_SET;
			customPurposesConsent = EmptyConstants.BIT_SET;
			customPurposesLegitimateInterest = EmptyConstants.BIT_SET;
		}

		private Builder(final PublisherTC publisherTC) {
			purposesConsent = new BitSet();
			purposesLegitimateInterest = new BitSet();
			customPurposesConsent = new BitSet();
			customPurposesLegitimateInterest = new BitSet();
			publisherTC.getAllConsentedPurposes().forEach(purposesConsent::set);
			publisherTC.getAllLegitimateInterestPurposes().forEach(purposesLegitimateInterest::set);
			publisherTC.getAllConsentedCustomPurposes().forEach(customPurposesConsent::set);
			publisherTC.getAllLegitimateInterestCustomPurposes().forEach(customPurposesLegitimateInterest::set);
		}

		public Builder addPurposeConsent(final int purpose) {
			if (purposesConsent == EmptyConstants.BIT_SET) {
				purposesConsent = new BitSet();
			}
			purposesConsent.set(purpose);
			return this;
		}

		public Builder purposeConsents(final BitSet set) {
			purposesConsent = set == null ? EmptyConstants.BIT_SET : set;
			return this;
		}

		public Builder addPurposeLegitimateInterest(final int purpose) {
			if (purposesLegitimateInterest == EmptyConstants.BIT_SET) {
				purposesLegitimateInterest = new BitSet();
			}
			purposesLegitimateInterest.set(purpose);
			return this;
		}

		public Builder purposeLegitimateInterest(final BitSet set) {
			purposesLegitimateInterest = set == null ? EmptyConstants.BIT_SET : set;
			return this;
		}

		public Builder addCustomPurposeConsent(final int purpose) {
			if (customPurposesConsent == EmptyConstants.BIT_SET) {
				customPurposesConsent = new BitSet();
			}
			customPurposesConsent.set(purpose);
			return this;
		}

		public Builder customPurposeConsents(final BitSet set) {
			customPurposesConsent = set == null ? EmptyConstants.BIT_SET : set;
			return this;
		}

		public Builder addCustomPurposeLegitimateInterest(final int purpose) {
			if (customPurposesLegitimateInterest == EmptyConstants.BIT_SET) {
				customPurposesLegitimateInterest = new BitSet();
			}
			customPurposesLegitimateInterest.set(purpose);
			return this;
		}

		public Builder customPurposeLegitimateInterest(final BitSet set) {
			customPurposesLegitimateInterest = set == null ? EmptyConstants.BIT_SET : set;
			return this;
		}

		public PublisherTC build() {
			return new PublisherTCImpl(this);
		}

		private static final class PublisherTCImpl implements PublisherTC {

			private final BitSet purposesConsent;
			private final BitSet purposesLegitimateInterest;
			private final BitSet customPurposesConsent;
			private final BitSet customPurposesLegitimateInterest;

			PublisherTCImpl(final Builder b) {
				this.purposesConsent = b.purposesConsent;
				this.purposesLegitimateInterest = b.purposesLegitimateInterest;
				this.customPurposesConsent = b.customPurposesConsent;
				this.customPurposesLegitimateInterest = b.customPurposesLegitimateInterest;
			}

			@Override
			public boolean isPurposeConsented(final int purpose) {
				return purposesConsent.get(purpose);
			}

			@Override
			public IntStream getAllConsentedPurposes() {
				return purposesConsent.stream();
			}

			@Override
			public boolean isPurposeLegitimateInterest(final int purpose) {
				return purposesLegitimateInterest.get(purpose);
			}

			@Override
			public IntStream getAllLegitimateInterestPurposes() {
				return purposesLegitimateInterest.stream();
			}

			@Override
			public boolean isCustomPurposeConsented(final int customPurpose) {
				return customPurposesConsent.get(customPurpose);
			}

			@Override
			public IntStream getAllConsentedCustomPurposes() {
				return customPurposesConsent.stream();
			}

			@Override
			public boolean isCustomPurposeLegitimateInterest(final int customPurpose) {
				return customPurposesLegitimateInterest.get(customPurpose);
			}

			@Override
			public IntStream getAllLegitimateInterestCustomPurposes() {
				return customPurposesLegitimateInterest.stream();
			}

			@Override
			public boolean equals(final Object o) {
				if (this == o) {
					return true;
				}
				if (o == null || getClass() != o.getClass()) {
					return false;
				}
				final PublisherTCImpl that = (PublisherTCImpl) o;
				return Objects.equals(purposesConsent, that.purposesConsent) &&
				       Objects.equals(purposesLegitimateInterest, that.purposesLegitimateInterest) &&
				       Objects.equals(customPurposesConsent, that.customPurposesConsent) &&
				       Objects.equals(customPurposesLegitimateInterest, that.customPurposesLegitimateInterest);
			}

			@Override
			public int hashCode() {
				return Objects.hash(purposesConsent, purposesLegitimateInterest, customPurposesConsent, customPurposesLegitimateInterest);
			}

			@Override
			public String toString() {
				return "PublisherTCImpl{" +
				       "publisherPurposesConsent: " + purposesConsent +
				       ", publisherPurposesLITransparency: " + purposesLegitimateInterest +
				       ", customPurposesConsent: " + customPurposesConsent +
				       ", customPurposesLITransparency: " + customPurposesLegitimateInterest +
				       '}';
			}
		}
	}
}
