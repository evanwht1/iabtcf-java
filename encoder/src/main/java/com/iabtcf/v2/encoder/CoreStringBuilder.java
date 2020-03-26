package com.iabtcf.v2.encoder;

import com.iabtcf.v2.CoreString;
import com.iabtcf.v2.Purpose;
import com.iabtcf.v2.RestrictionType;
import com.iabtcf.v2.SpecialFeature;

import java.time.Instant;
import java.util.BitSet;
import java.util.EnumMap;

/**
 * @author ewhite 3/26/20
 */
public class CoreStringBuilder {

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

	public CoreStringBuilder() {}

	public CoreStringBuilder(CoreString coreString) {
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
	}

	public CoreStringBuilder version(final int val) {
		version = val;
		return this;
	}

	public CoreStringBuilder consentRecordCreated(final Instant val) {
		created = val;
		return this;
	}

	public CoreStringBuilder consentRecordLastUpdated(final Instant val) {
		lastUpdated = val;
		return this;
	}

	public CoreStringBuilder consentManagerProviderId(final int val) {
		cmpId = val;
		return this;
	}

	public CoreStringBuilder consentManagerProviderVersion(final int val) {
		cmpVersion = val;
		return this;
	}

	public CoreStringBuilder consentScreen(final int val) {
		consentScreen = val;
		return this;
	}

	public CoreStringBuilder consentLanguage(final String val) {
		consentLanguage = val;
		return this;
	}

	public CoreStringBuilder vendorListVersion(final int val) {
		vendorListVersion = val;
		return this;
	}

	public CoreStringBuilder policyVersion(final int val) {
		policyVersion = val;
		return this;
	}

	public CoreStringBuilder isServiceSpecific(final boolean val) {
		isServiceSpecific = val;
		return this;
	}

	public CoreStringBuilder useNonStandardStacks(final boolean val) {
		useNonStandardStacks = val;
		return this;
	}

	public CoreStringBuilder isPurposeOneTreatment(final boolean val) {
		isPurposeOneTreatment = val;
		return this;
	}

	public CoreStringBuilder publisherCountryCode(final String val) {
		publisherCountryCode = val;
		return this;
	}

	public CoreStringBuilder addSpecialFeatureOptedIn(final int val) {
		specialFeaturesOptInts.set(val);
		return this;
	}

	public CoreStringBuilder addSpecialFeatureOptedIn(final SpecialFeature specialFeature) {
		specialFeaturesOptInts.set(specialFeature.getId());
		return this;
	}

	public CoreStringBuilder addPurposeConsent(final int val) {
		purposesConsent.set(val);
		return this;
	}

	public CoreStringBuilder addPurposeConsent(final Purpose purpose) {
		purposesConsent.set(purpose.getId());
		return this;
	}

	public CoreStringBuilder addPurposesLegitimateInterest(final int val) {
		purposesLITransparency.set(val);
		return this;
	}

	public CoreStringBuilder addPurposesLegitimateInterest(final Purpose purpose) {
		purposesLITransparency.set(purpose.getId());
		return this;
	}

	public CoreStringBuilder addVendorConsent(final int val) {
		vendorConsents.add(val);
		return this;
	}

	public CoreStringBuilder addVendorLegitimateInterest(final int val) {
		vendorLegitimateInterests.add(val);
		return this;
	}

	public CoreStringBuilder addPublisherRestriction(final int vendor,
	                                              final int purpose,
	                                              final RestrictionType restrictionType) {
		return addPublisherRestriction(vendor, Purpose.valueOf(purpose), restrictionType);
	}

	public CoreStringBuilder addPublisherRestriction(final int vendor,
	                                              final Purpose purpose,
	                                              final RestrictionType restrictionType) {
		publisherRestrictions.computeIfAbsent(purpose, p -> new EnumMap<>(RestrictionType.class))
		                     .computeIfAbsent(restrictionType, r -> new RangeData())
		                     .add(vendor);
		return this;
	}
	
}
