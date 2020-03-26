package com.iabtcf.v2.encoder;

import com.iabtcf.v2.OutOfBandConsent;

/**
 * @author ewhite 3/26/20
 */
public class OutOfBandBuilder {

	RangeData disclosedVendors = new RangeData();
	RangeData allowedVendors = new RangeData();

	public OutOfBandBuilder() {}

	public OutOfBandBuilder(OutOfBandConsent outOfBandConsent) {
		outOfBandConsent.getAllDisclosedVendors().forEach(disclosedVendors::add);
		outOfBandConsent.getAllAllowedVendors().forEach(allowedVendors::add);
	}

	public OutOfBandBuilder addDisclosedVendor(final int vendor) {
		disclosedVendors.add(vendor);
		return this;
	}

	public OutOfBandBuilder addAllowedVendor(final int vendor) {
		allowedVendors.add(vendor);
		return this;
	}
}
