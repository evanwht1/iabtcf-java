package com.iabtcf.v2.encoder;

import com.iabtcf.v2.CoreString;
import com.iabtcf.v2.OutOfBandConsent;
import com.iabtcf.v2.PublisherTC;
import com.iabtcf.v2.Purpose;
import com.iabtcf.v2.RestrictionType;
import com.iabtcf.v2.SpecialFeature;
import com.iabtcf.v2.TCModel;

import java.time.Instant;
import java.util.BitSet;
import java.util.EnumMap;

/**
 * @author evanwht1@gmail.com
 */
public class TCModelBuilder {

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
	RangeData vendorConsents = new RangeData();
	RangeData vendorLegitimateInterests = new RangeData();
	EnumMap<Purpose, EnumMap<RestrictionType, RangeData>> publisherRestrictions = new EnumMap<>(Purpose.class);

	// OOB
	RangeData disclosedVendors = new RangeData();
	RangeData allowedVendors = new RangeData();

	// PublisherTC
	BitSet publisherPurposes = new BitSet();
	BitSet publisherPurposesLI = new BitSet();
	BitSet publisherCustomPurposes = new BitSet();
	BitSet publisherCustomPurposesLI = new BitSet();

	public TCModelBuilder() {}

	public TCModelBuilder(TCModel model) {
		CoreString coreString = model.getCoreString();
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
		coreString.getAllConsentedVendors().forEach(vendorConsents::add);
		coreString.getAllLegitimateInterestVendors().forEach(vendorLegitimateInterests::add);
		coreString.getAllPublisherRestrictions().forEach(pr -> {
			RangeData rangeData = new RangeData();
			pr.getAllVendors().forEach(rangeData::add);
			publisherRestrictions.computeIfAbsent(pr.getPurpose(), p -> new EnumMap<>(RestrictionType.class))
								 .put(pr.getRestrictionType(), rangeData);

		});

		OutOfBandConsent outOfBandConsent = model.getOutOfBandConsent();
		outOfBandConsent.getAllDisclosedVendors().forEach(disclosedVendors::add);
		outOfBandConsent.getAllAllowedVendors().forEach(allowedVendors::add);

		PublisherTC purposesTC = model.getPublisherTC();
		purposesTC.getAllConsentedPurposes().forEach(publisherPurposes::set);
		purposesTC.getAllLegitimateInterestPurposes().forEach(publisherPurposesLI::set);
		purposesTC.getAllConsentedCustomPurposes().forEach(publisherCustomPurposes::set);
		purposesTC.getAllLegitimateInterestCustomPurposes().forEach(publisherCustomPurposesLI::set);
	}

	public TCModelBuilder version(final int val) {
		version = val;
		return this;
	}

	public TCModelBuilder consentRecordCreated(final Instant val) {
		created = val;
		return this;
	}

	public TCModelBuilder consentRecordLastUpdated(final Instant val) {
		lastUpdated = val;
		return this;
	}

	public TCModelBuilder consentManagerProviderId(final int val) {
		cmpId = val;
		return this;
	}

	public TCModelBuilder consentManagerProviderVersion(final int val) {
		cmpVersion = val;
		return this;
	}

	public TCModelBuilder consentScreen(final int val) {
		consentScreen = val;
		return this;
	}

	public TCModelBuilder consentLanguage(final String val) {
		consentLanguage = val;
		return this;
	}

	public TCModelBuilder vendorListVersion(final int val) {
		vendorListVersion = val;
		return this;
	}

	public TCModelBuilder policyVersion(final int val) {
		policyVersion = val;
		return this;
	}

	public TCModelBuilder isServiceSpecific(final boolean val) {
		isServiceSpecific = val;
		return this;
	}

	public TCModelBuilder useNonStandardStacks(final boolean val) {
		useNonStandardStacks = val;
		return this;
	}

	public TCModelBuilder isPurposeOneTreatment(final boolean val) {
		isPurposeOneTreatment = val;
		return this;
	}

	public TCModelBuilder publisherCountryCode(final String val) {
		publisherCountryCode = val;
		return this;
	}

	public TCModelBuilder addSpecialFeatureOptedIn(final int val) {
		specialFeaturesOptInts.set(val);
		return this;
	}

	public TCModelBuilder addSpecialFeatureOptedIn(final SpecialFeature specialFeature) {
		specialFeaturesOptInts.set(specialFeature.getId());
		return this;
	}

	public TCModelBuilder addPurposeConsent(final int val) {
		purposesConsent.set(val);
		return this;
	}

	public TCModelBuilder addPurposeConsent(final Purpose purpose) {
		purposesConsent.set(purpose.getId());
		return this;
	}

	public TCModelBuilder addPurposesLegitimateInterest(final int val) {
		purposesLITransparency.set(val);
		return this;
	}

	public TCModelBuilder addPurposesLegitimateInterest(final Purpose purpose) {
		purposesLITransparency.set(purpose.getId());
		return this;
	}

	public TCModelBuilder addVendorConsent(final int val) {
		vendorConsents.add(val);
		return this;
	}

	public TCModelBuilder addVendorLegitimateInterest(final int val) {
		vendorLegitimateInterests.add(val);
		return this;
	}

	public TCModelBuilder addPublisherRestriction(final int vendor,
												  final int purpose,
												  final RestrictionType restrictionType) {
		return addPublisherRestriction(vendor, Purpose.valueOf(purpose), restrictionType);
	}

	public TCModelBuilder addPublisherRestriction(final int vendor,
	                                              final Purpose purpose,
	                                              final RestrictionType restrictionType) {
		publisherRestrictions.computeIfAbsent(purpose, p -> new EnumMap<>(RestrictionType.class))
		                     .computeIfAbsent(restrictionType, r -> new RangeData())
		                     .add(vendor);
		return this;
	}

	// OOB

	public TCModelBuilder addDisclosedVendor(final int vendor) {
		disclosedVendors.add(vendor);
		return this;
	}

	public TCModelBuilder addAllowedVendor(final int vendor) {
		allowedVendors.add(vendor);
		return this;
	}

	// PublisherTC

	public TCModelBuilder addPublisherPurposeConsent(final int purpose) {
		publisherPurposes.set(purpose);
		return this;
	}

	public TCModelBuilder addPublisherPurposeConsent(final Purpose purpose) {
		return addPublisherPurposeConsent(purpose.getId());
	}

	public TCModelBuilder addPublisherPurposeLegitimateInterest(final int purpose) {
		publisherPurposesLI.set(purpose);
		return this;
	}

	public TCModelBuilder addPublisherCustomPurposeConsent(final int purpose) {
		publisherCustomPurposes.set(purpose);
		return this;
	}

	public TCModelBuilder addPublisherCustomPurposeLegitimateInterest(final int purpose) {
		publisherCustomPurposesLI.set(purpose);
		return this;
	}

	public String build() {
		return TCModelEncoder.encode(this);
	}
}
