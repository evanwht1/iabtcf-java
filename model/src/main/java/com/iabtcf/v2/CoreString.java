package com.iabtcf.v2;

import java.time.Instant;
import java.util.BitSet;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * All details required to communicate basic vendor transparency and consent.
 *
 * @author evanwht1
 */
public interface CoreString {

	/**
	 * @return version of the encoding format
	 */
	int getVersion();

	/**
	 * The instant the record was first created as read from an Epoch Decisecond (1/10th of a second)
	 *
	 * @return when the record was first created
	 */
	Instant getCreated();

	/**
	 * The instant the record was last updated as read from an Epoch Decisecond (1/10th of a second)
	 *
	 * @return When the record was last updated
	 */
	Instant getLastUpdated();

	/**
	 * A unique ID assigned to the Consent Management Platform
	 *
	 * @return ID of the last Consent Management Platform that updated this record
	 */
	int getCmpId();

	/**
	 * Each Consent Management Platform should update their version number as a record of which version the user gave
	 * consent and transparency was established
	 *
	 * @return version of the last Consent Management Platform that updated this record
	 */
	int getCmpVersion();

	/**
	 * The number is a CMP internal designation and is CmpVersion specific. The number is used for identifying on which
	 * screen a user gave consent as a record.
	 *
	 * @return screen number at which consent was given
	 */
	int getConsentScreen();

	/**
	 * two-letter ISO 639-1 language code in which the Consent Management Platform UI was presented
	 *
	 * @return language in which the CMP UI was presented
	 */
	String getConsentLanguage();

	/**
	 * @return version of the GVL used to create this TC String
	 */
	int getVendorListVersion();

	/**
	 * From the corresponding field in the GVL that was used for obtaining consent. A new policy version invalidates
	 * existing strings and requires CMPs to re-establish transparency and consent from users.
	 *
	 * @return version of policy used within GVL
	 */
	int getPolicyVersion();

	/**
	 * Whether the signals encoded in this TC String were from service-specific storage versus global consesu.org
	 * shared storage
	 *
	 * @return if signals are service-specific or global
	 */
	boolean isServiceSpecific();

	/**
	 * Setting this to 1 means that a publisher-run CMP – that is still IAB Europe registered – is using customized
	 * Stack descriptions and not the standard stack descriptions defined in the Policies (Appendix A section E). A CMP
	 * that services multiple publishers sets this value to 0.
	 *
	 * @return if teh CMP used non-IAB standard stacks during consent gathering
	 */
	boolean isUseNonStandardStacks();

	/**
	 * The TCF Policies designates certain Features as “special” which means a CMP must afford the user a means to opt
	 * in to their use. These “Special Features” are published and numerically identified in the Global Vendor List
	 * separately from normal Features.
	 *
	 * @see Feature
	 * @param specialFeature the special feature id
	 * @return if the user opted in to that special feature
	 */
	boolean isSpecialFeatureOptedIn(final int specialFeature);

	/**
	 * The TCF Policies designates certain Features as “special” which means a CMP must afford the user a means to opt
	 * in to their use. These “Special Features” are published and numerically identified in the Global Vendor List
	 * separately from normal Features.
	 *
	 * @see Feature
	 * @return all special features the user opted in to
	 */
	IntStream getAllOptedInSpecialFeatures();

	/**
	 * The user’s consent value for each Purpose established on the legal basis of consent.
	 * <br>
	 * The Purposes are numerically identified and published in the Global Vendor List. From left to right, Purpose 1
	 * maps to the 0th bit, purpose 24 maps to the bit at index 23. Special Purposes are a different ID space and not
	 * included in this field.
	 *
	 * @see Purpose
	 * @param purpose id of the purpose
	 * @return if the user consented to that purpose
	 */
	boolean isPurposeConsented(final int purpose);

	/**
	 * The user’s consent value for each Purpose established on the legal basis of consent.
	 * <br>
	 * The Purposes are numerically identified and published in the Global Vendor List. From left to right, Purpose 1
	 * maps to the 0th bit, purpose 24 maps to the bit at index 23. Special Purposes are a different ID space and not
	 * included in this field.
	 *
	 * @see Purpose
	 * @return all purposes the user consented to
	 */
	IntStream getAllConsentedPurposes();

	/**
	 * The Purpose’s transparency requirements are met for each Purpose on the legal basis of legitimate interest and
	 * the user has not exercised their “Right to Object” to that Purpose.
	 * <br>
	 * By default or if the user has exercised their “Right to Object” to a Purpose, the corresponding bit for that
	 * Purpose is set to 0. From left to right, Purpose 1 maps to the 0th bit, purpose 24 maps to the bit at index 23.
	 * Special Purposes are a different ID space and not included in this field.
	 *
	 * @see Purpose
	 * @param purpose id of the purpose
	 * @return if the purpose's transparency requirement has been met
	 */
	boolean isPurposeLegitimateInterest(final int purpose);

	/**
	 * The Purpose’s transparency requirements are met for each Purpose on the legal basis of legitimate interest and
	 * the user has not exercised their “Right to Object” to that Purpose.
	 * <br>
	 * By default or if the user has exercised their “Right to Object” to a Purpose, the corresponding bit for that
	 * Purpose is set to 0. From left to right, Purpose 1 maps to the 0th bit, purpose 24 maps to the bit at index 23.
	 * Special Purposes are a different ID space and not included in this field.
	 *
	 * @see Purpose
	 * @return all purpose's that have met their transparency requirement
	 */
	IntStream getAllLegitimateInterestPurposes();

	/**
	 * CMPs can use the PublisherCC field to indicate the legal jurisdiction the publisher is under to help vendors
	 * determine whether the vendor needs consent for Purpose 1.
	 * <br>
	 * In a globally-scoped TC string, this field must always have a value of 0. When a CMP encounters a
	 * globally-scoped TC String with PurposeOneTreatment=1 then it is considered invalid and the CMP must discard it
	 * and re-establish transparency and consent.
	 *
	 * @see Purpose
	 * @return if purpose 1 was NOT disclosed at all
	 */
	boolean isPurposeOneTreatment();

	/**
	 * The country code of the country that determines legislation of reference. Commonly, this corresponds to the
	 * country in which the publisher’s business entity is established.
	 *
	 * @return ISO 3166-1 alpha-2 country code
	 */
	String getPublisherCountryCode();

	/**
	 * @param vendor id of the vendor
	 * @return if the user has consented to the vendor processing their personal data
	 */
	boolean isVendorConsented(final int vendor);

	/**
	 * @return all vendors that have consent to process this users personal data
	 */
	IntStream getAllConsentedVendors();

	/**
	 * If a user exercises their “Right To Object” to a vendor’s processing based on a legitimate interest.
	 *
	 * @param vendor id of the vendor
	 * @return if the vendor can process this user based on legitimate interest
	 */
	boolean isVendorLegitimateInterest(final int vendor);

	/**
	 * If a user exercises their “Right To Object” to a vendor’s processing based on a legitimate interest.
	 *
	 * @return All vendors that can process this user based on legitimate interest
	 */
	IntStream getAllLegitimateInterestVendors();

	/**
	 * Vendors must always respect a 0 (Not Allowed) regardless of whether or not they have not declared that Purpose
	 * to be “flexible”. Values 1 and 2 are in accordance with a vendors declared flexibility. Eg. if a vendor has
	 * Purpose 2 declared as Legitimate Interest but also declares that Purpose as flexible and this field is set to
	 * 1, they must then check for the “consent” signal in the VendorConsents section to make a determination on
	 * whether they have the legal basis for processing user personal data under that Purpose.
	 *
	 * If a vendor has not declared a Purpose flexible and this value is 1 or 2 they may ignore the signal.
	 *
	 * Note: Purpose 1 is always required to be registered as a consent purpose and can not be flexible per Policies.
	 *
	 * @param purpose The vendors declared purpose id
	 * @param vendor id of the vendor
	 * @return what overriding restriction type applies (default to {@link RestrictionType#UNDEFINED})
	 */
	RestrictionType getPublisherRestriction(final Purpose purpose, final int vendor);

	RestrictionType getPublisherRestriction(final int purpose, final int vendor);

	Stream<PublisherRestriction> getAllPublisherRestrictions();

	static Builder newBuilder() {
		return new Builder();
	}

	static Builder newBuilder(CoreString coreString) {
		return new Builder(coreString);
	}

	class Builder {

		int version;
		Instant created;
		Instant lastUpdated;
		int cmpId;
		int cmpVersion;
		int consentScreen;
		String consentLanguage;
		int vendorListVersion;
		int policyVersion;
		boolean isServiceSpecific;
		boolean useNonStandardStacks;
		boolean isPurposeOneTreatment;
		String publisherCountryCode;
		BitSet specialFeaturesOptInts = new BitSet();
		BitSet purposesConsent = new BitSet();
		BitSet purposesLITransparency = new BitSet();
		BitSet vendorConsents = new BitSet();
		BitSet vendorLegitimateInterests = new BitSet();
		Map<Integer, EnumMap<RestrictionType, BitSet>> publisherRestrictions = new HashMap<>();

		Builder() {}

		Builder(CoreString coreString) {
			version = coreString.getVersion();
			created = coreString.getCreated();
			lastUpdated = coreString.getLastUpdated();
			cmpId = coreString.getCmpId();
			cmpVersion = coreString.getCmpVersion();
			consentScreen = coreString.getConsentScreen();
			consentLanguage = coreString.getConsentLanguage();
			vendorListVersion = coreString.getVendorListVersion();
			policyVersion = coreString.getPolicyVersion();
			isServiceSpecific = coreString.isServiceSpecific();
			useNonStandardStacks = coreString.isUseNonStandardStacks();
			isPurposeOneTreatment = coreString.isPurposeOneTreatment();
			publisherCountryCode = coreString.getPublisherCountryCode();
			coreString.getAllOptedInSpecialFeatures().forEach(specialFeaturesOptInts::set);
			coreString.getAllConsentedPurposes().forEach(purposesConsent::set);
			coreString.getAllLegitimateInterestPurposes().forEach(purposesLITransparency::set);
			coreString.getAllConsentedVendors().forEach(vendorConsents::set);
			coreString.getAllLegitimateInterestVendors().forEach(vendorLegitimateInterests::set);
			coreString.getAllPublisherRestrictions().forEach(pr -> {
				BitSet rangeData = new BitSet();
				pr.getAllVendors().forEach(rangeData::set);
				publisherRestrictions.computeIfAbsent(pr.getPurposeId(), p -> new EnumMap<>(RestrictionType.class))
				                     .put(pr.getRestrictionType(), rangeData);

			});
		}

		public Builder version(final int val) {
			version = val;
			return this;
		}

		public Builder consentRecordCreated(final Instant val) {
			created = val;
			return this;
		}

		public Builder consentRecordLastUpdated(final Instant val) {
			lastUpdated = val;
			return this;
		}

		public Builder consentManagerProviderId(final int val) {
			cmpId = val;
			return this;
		}

		public Builder consentManagerProviderVersion(final int val) {
			cmpVersion = val;
			return this;
		}

		public Builder consentScreen(final int val) {
			consentScreen = val;
			return this;
		}

		public Builder consentLanguage(final String val) {
			consentLanguage = val;
			return this;
		}

		public Builder vendorListVersion(final int val) {
			vendorListVersion = val;
			return this;
		}

		public Builder policyVersion(final int val) {
			policyVersion = val;
			return this;
		}

		public Builder isServiceSpecific(final boolean val) {
			isServiceSpecific = val;
			return this;
		}

		public Builder useNonStandardStacks(final boolean val) {
			useNonStandardStacks = val;
			return this;
		}

		public Builder isPurposeOneTreatment(final boolean val) {
			isPurposeOneTreatment = val;
			return this;
		}

		public Builder publisherCountryCode(final String val) {
			publisherCountryCode = val;
			return this;
		}

		public Builder addSpecialFeatureOptedIn(final int val) {
			specialFeaturesOptInts.set(val);
			return this;
		}

		public Builder addSpecialFeatureOptedIn(final SpecialFeature specialFeature) {
			specialFeaturesOptInts.set(specialFeature.getId());
			return this;
		}

		public Builder specialFeatureOptIns(final BitSet bitSet) {
			specialFeaturesOptInts = bitSet;
			return this;
		}

		public Builder addPurposeConsent(final int val) {
			purposesConsent.set(val);
			return this;
		}

		public Builder addPurposeConsent(final Purpose purpose) {
			purposesConsent.set(purpose.getId());
			return this;
		}

		public Builder purposeConsents(final BitSet bitSet) {
			purposesConsent = bitSet;
			return this;
		}

		public Builder addPurposesLegitimateInterest(final int val) {
			purposesLITransparency.set(val);
			return this;
		}

		public Builder addPurposesLegitimateInterest(final Purpose purpose) {
			purposesLITransparency.set(purpose.getId());
			return this;
		}

		public Builder purposeLegitimateInterests(final BitSet bitSet) {
			purposesLITransparency = bitSet;
			return this;
		}

		public Builder addVendorConsent(final int val) {
			vendorConsents.set(val);
			return this;
		}

		public Builder vendorConsents(final BitSet bitSet) {
			vendorConsents = bitSet;
			return this;
		}

		public Builder addVendorLegitimateInterest(final int val) {
			vendorLegitimateInterests.set(val);
			return this;
		}

		public Builder vendorLegitimateInterests(final BitSet bitSet) {
			vendorLegitimateInterests = bitSet;
			return this;
		}

		public Builder addPublisherRestriction(final int vendor,
		                                       final Purpose purpose,
		                                       final RestrictionType restrictionType) {
			return addPublisherRestriction(vendor, purpose.getId(), restrictionType);
		}

		public Builder addPublisherRestriction(final int vendor,
		                                       final int purpose,
		                                       final RestrictionType restrictionType) {
			publisherRestrictions.computeIfAbsent(purpose, p -> new EnumMap<>(RestrictionType.class))
			                     .computeIfAbsent(restrictionType, r -> new BitSet())
			                     .set(vendor);
			return this;
		}

		public Builder publisherRestrictions(final Map<Integer, EnumMap<RestrictionType, BitSet>> map) {
			publisherRestrictions = map;
			return this;
		}

		public CoreString build() {
			return new CoreStringImpl(this);
		}

		private static final class CoreStringImpl implements CoreString {

			private final int version;
			private final Instant consentRecordCreated;
			private final Instant consentRecordLastUpdated;
			private final int consentManagerProviderId;
			private final int consentManagerProviderVersion;
			private final int consentScreen;
			private final String consentLanguage;
			private final int vendorListVersion;
			private final int policyVersion;
			private final boolean isServiceSpecific;
			private final boolean useNonStandardStacks;
			private final boolean isPurposeOneTreatment;
			private final String publisherCountryCode;
			private final BitSet specialFeaturesOptInts;
			private final BitSet purposesConsent;
			private final BitSet purposesLITransparency;
			private final BitSet vendorConsents;
			private final BitSet vendorLegitimateInterests;
			private final Map<Integer, EnumMap<RestrictionType, BitSet>> publisherRestrictions;

			private CoreStringImpl(final Builder builder) {
				version = builder.version;
				consentRecordCreated = builder.created;
				consentRecordLastUpdated = builder.lastUpdated;
				consentManagerProviderId = builder.cmpId;
				consentManagerProviderVersion = builder.cmpVersion;
				consentScreen = builder.consentScreen;
				consentLanguage = builder.consentLanguage;
				vendorListVersion = builder.vendorListVersion;
				policyVersion = builder.policyVersion;
				isServiceSpecific = builder.isServiceSpecific;
				useNonStandardStacks = builder.useNonStandardStacks;
				isPurposeOneTreatment = builder.isPurposeOneTreatment;
				publisherCountryCode = builder.publisherCountryCode;
				specialFeaturesOptInts = builder.specialFeaturesOptInts;
				purposesConsent = builder.purposesConsent;
				purposesLITransparency = builder.purposesLITransparency;
				vendorConsents = builder.vendorConsents;
				vendorLegitimateInterests = builder.vendorLegitimateInterests;
				publisherRestrictions = builder.publisherRestrictions;
			}

			public static Builder newBuilder() {
				return new Builder();
			}

			@Override
			public int getVersion() {
				return version;
			}

			@Override
			public Instant getCreated() {
				return consentRecordCreated;
			}

			@Override
			public Instant getLastUpdated() {
				return consentRecordLastUpdated;
			}

			@Override
			public int getCmpId() {
				return consentManagerProviderId;
			}

			@Override
			public int getCmpVersion() {
				return consentManagerProviderVersion;
			}

			@Override
			public int getConsentScreen() {
				return consentScreen;
			}

			@Override
			public String getConsentLanguage() {
				return consentLanguage;
			}

			@Override
			public int getVendorListVersion() {
				return vendorListVersion;
			}

			@Override
			public int getPolicyVersion() {
				return policyVersion;
			}

			@Override
			public boolean isServiceSpecific() {
				return isServiceSpecific;
			}

			@Override
			public boolean isUseNonStandardStacks() {
				return useNonStandardStacks;
			}

			@Override
			public boolean isPurposeOneTreatment() {
				return isPurposeOneTreatment;
			}

			@Override
			public String getPublisherCountryCode() {
				return publisherCountryCode;
			}

			@Override
			public boolean isSpecialFeatureOptedIn(final int specialFeature) {
				return specialFeaturesOptInts.get(specialFeature);
			}

			@Override
			public IntStream getAllOptedInSpecialFeatures() {
				return specialFeaturesOptInts.stream();
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
				return purposesLITransparency.get(purpose);
			}

			@Override
			public IntStream getAllLegitimateInterestPurposes() {
				return purposesLITransparency.stream();
			}

			@Override
			public boolean isVendorConsented(final int vendor) {
				return vendorConsents.get(vendor);
			}

			@Override
			public IntStream getAllConsentedVendors() {
				return vendorConsents.stream();
			}

			@Override
			public boolean isVendorLegitimateInterest(final int vendor) {
				return vendorLegitimateInterests.get(vendor);
			}

			@Override
			public IntStream getAllLegitimateInterestVendors() {
				return vendorLegitimateInterests.stream();
			}

			@Override
			public RestrictionType getPublisherRestriction(final Purpose purpose, final int vendor) {
				return getPublisherRestriction(purpose.getId(), vendor);
			}

			@Override
			public RestrictionType getPublisherRestriction(final int purpose, final int vendor) {
				if (publisherRestrictions.containsKey(purpose)) {
					return publisherRestrictions.get(purpose).entrySet().stream()
					                            .filter(e -> e.getValue().get(vendor))
					                            .findFirst()
					                            .map(Map.Entry::getKey)
					                            .orElse(RestrictionType.UNDEFINED);
				}
				return RestrictionType.UNDEFINED;
			}

			@Override
			public Stream<PublisherRestriction> getAllPublisherRestrictions() {
				return publisherRestrictions.entrySet()
				                            .stream()
				                            .flatMap(e -> e.getValue().entrySet()
				                                           .stream()
				                                           .map(r -> new PublisherRestriction() {
					                                           @Override
					                                           public int getPurposeId() {
						                                           return e.getKey();
					                                           }

					                                           @Override
					                                           public RestrictionType getRestrictionType() {
						                                           return r.getKey();
					                                           }

					                                           @Override
					                                           public boolean isVendorIncluded(final int vendor) {
						                                           return r.getValue().get(vendor);
					                                           }

					                                           @Override
					                                           public IntStream getAllVendors() {
						                                           return r.getValue().stream();
					                                           }
				                                           }));
			}

			@Override
			public boolean equals(final Object o) {
				if (this == o) {
					return true;
				}
				if (o == null || getClass() != o.getClass()) {
					return false;
				}
				final CoreStringImpl that = (CoreStringImpl) o;
				return version == that.version &&
				       consentManagerProviderId == that.consentManagerProviderId &&
				       consentManagerProviderVersion == that.consentManagerProviderVersion &&
				       consentScreen == that.consentScreen &&
				       vendorListVersion == that.vendorListVersion &&
				       policyVersion == that.policyVersion &&
				       isServiceSpecific == that.isServiceSpecific &&
				       useNonStandardStacks == that.useNonStandardStacks &&
				       isPurposeOneTreatment == that.isPurposeOneTreatment &&
				       Objects.equals(consentRecordCreated, that.consentRecordCreated) &&
				       Objects.equals(consentRecordLastUpdated, that.consentRecordLastUpdated) &&
				       Objects.equals(consentLanguage, that.consentLanguage) &&
				       Objects.equals(publisherCountryCode, that.publisherCountryCode) &&
				       Objects.equals(specialFeaturesOptInts, that.specialFeaturesOptInts) &&
				       Objects.equals(purposesConsent, that.purposesConsent) &&
				       Objects.equals(purposesLITransparency, that.purposesLITransparency) &&
				       Objects.equals(vendorConsents, that.vendorConsents) &&
				       Objects.equals(vendorLegitimateInterests, that.vendorLegitimateInterests) &&
				       Objects.equals(publisherRestrictions, that.publisherRestrictions);
			}

			@Override
			public int hashCode() {
				return Objects.hash(version,
						consentRecordCreated,
						consentRecordLastUpdated,
						consentManagerProviderId,
						consentManagerProviderVersion,
						consentScreen,
						consentLanguage,
						vendorListVersion,
						policyVersion,
						isServiceSpecific,
						useNonStandardStacks,
						isPurposeOneTreatment,
						publisherCountryCode,
						specialFeaturesOptInts,
						purposesConsent,
						purposesLITransparency,
						vendorConsents,
						vendorLegitimateInterests,
						publisherRestrictions);
			}

			@Override
			public String toString() {
				return "CoreString{" +
				       "version: " + version +
				       ", consentRecordCreated: " + consentRecordCreated +
				       ", consentRecordLastUpdated: " + consentRecordLastUpdated +
				       ", consentManagerProviderId: " + consentManagerProviderId +
				       ", consentManagerProviderVersion: " + consentManagerProviderVersion +
				       ", consentScreen: " + consentScreen +
				       ", consentLanguage: " + consentLanguage +
				       ", vendorListVersion: " + vendorListVersion +
				       ", policyVersion: " + policyVersion +
				       ", isServiceSpecific: " + isServiceSpecific +
				       ", useNonStandardStacks: " + useNonStandardStacks +
				       ", isPurposeOneTreatment: " + isPurposeOneTreatment +
				       ", publisherCountryCode: " + publisherCountryCode +
				       ", specialFeaturesOptInts: " + specialFeaturesOptInts +
				       ", purposesConsent: " + purposesConsent +
				       ", purposesLITransparency: " + purposesLITransparency +
				       ", vendorConsents: " + vendorConsents +
				       ", vendorLegitimateInterests: " + vendorLegitimateInterests +
				       ", publisherRestrictions: " + publisherRestrictions +
				       '}';
			}
		}
	}
}
