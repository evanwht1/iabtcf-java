package com.iabtcf.v2.encoder;

import com.iabtcf.v2.PublisherTC;
import com.iabtcf.v2.Purpose;

import java.util.BitSet;

/**
 * @author ewhite 3/26/20
 */
public class PublisherTCBuilder {

	BitSet publisherPurposes = new BitSet();
	BitSet publisherPurposesLI = new BitSet();
	BitSet publisherCustomPurposes = new BitSet();
	BitSet publisherCustomPurposesLI = new BitSet();

	public PublisherTCBuilder() {}

	public PublisherTCBuilder(PublisherTC publisherTC) {
		publisherTC.getAllConsentedPurposes().forEach(publisherPurposes::set);
		publisherTC.getAllLegitimateInterestPurposes().forEach(publisherPurposesLI::set);
		publisherTC.getAllConsentedCustomPurposes().forEach(publisherCustomPurposes::set);
		publisherTC.getAllLegitimateInterestCustomPurposes().forEach(publisherCustomPurposesLI::set);
	}

	public PublisherTCBuilder addPublisherPurposeConsent(final int purpose) {
		publisherPurposes.set(purpose);
		return this;
	}

	public PublisherTCBuilder addPublisherPurposeConsent(final Purpose purpose) {
		return addPublisherPurposeConsent(purpose.getId());
	}

	public PublisherTCBuilder addPublisherPurposeLegitimateInterest(final int purpose) {
		publisherPurposesLI.set(purpose);
		return this;
	}

	public PublisherTCBuilder addPublisherCustomPurposeConsent(final int purpose) {
		publisherCustomPurposes.set(purpose);
		return this;
	}

	public PublisherTCBuilder addPublisherCustomPurposeLegitimateInterest(final int purpose) {
		publisherCustomPurposesLI.set(purpose);
		return this;
	}
}
