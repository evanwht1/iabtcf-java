package com.iabtcf.v2.encoder;

import com.iabtcf.v2.RestrictionType;

import java.time.Instant;
import java.util.BitSet;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

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
	Map<Integer, EnumMap<RestrictionType, RangeData>> publisherRestrictions = new HashMap<>();

	// OOB
	RangeData disclosedVendors = new RangeData();
	RangeData allowedVendors = new RangeData();

	// PublisherTC
	BitSet publisherPurposes = new BitSet();
	BitSet publisherPurposesLI = new BitSet();
	BitSet publisherCustomPurposes = new BitSet();
	BitSet publisherCustomPurposesLI = new BitSet();

	private TCModelBuilder() {}

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

	public TCModelBuilder addPurposeConsent(final int val) {
		purposesConsent.set(val);
		return this;
	}

	public TCModelBuilder addPurposesLegitimateInterest(final int val) {
		purposesLITransparency.set(val);
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
