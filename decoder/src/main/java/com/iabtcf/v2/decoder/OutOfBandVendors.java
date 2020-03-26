package com.iabtcf.v2.decoder;

import com.iabtcf.v2.OutOfBandConsent;

import java.util.BitSet;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.IntStream;

/**
 * @author evanwht1
 */
class OutOfBandVendors implements OutOfBandConsent {

	private final Supplier<BitSet> disclosedVendorSupplier;
	private BitSet disclosedVendor;
	private final Supplier<BitSet> allowedVendorsSupplier;
	private BitSet allowedVendors;

	OutOfBandVendors(final Supplier<BitSet> disclosedVendor, Supplier<BitSet> allowedVendors) {
		this.disclosedVendorSupplier = disclosedVendor;
		this.allowedVendorsSupplier = allowedVendors;
	}

	@Override
	public boolean isVendorDisclosed(final int vendor) {
		if (disclosedVendor == null) {
			disclosedVendor = disclosedVendorSupplier.get();
		}
		return disclosedVendor.get(vendor);
	}

	@Override
	public IntStream getAllDisclosedVendors() {
		if (disclosedVendor == null) {
			disclosedVendor = disclosedVendorSupplier.get();
		}
		return disclosedVendor.stream();
	}

	@Override
	public boolean isVendorAllowed(final int vendor) {
		if (allowedVendors == null) {
			allowedVendors = allowedVendorsSupplier.get();
		}
		return allowedVendors.get(vendor);
	}

	@Override
	public IntStream getAllAllowedVendors() {
		if (allowedVendors == null) {
			allowedVendors = allowedVendorsSupplier.get();
		}
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
		final OutOfBandVendors that = (OutOfBandVendors) o;
		return disclosedVendor.equals(that.disclosedVendor) &&
		       allowedVendors.equals(that.allowedVendors);
	}

	@Override
	public int hashCode() {
		return Objects.hash(disclosedVendor, allowedVendors);
	}

	@Override
	public String toString() {
		return "OutOfBandVendors{" +
		       "disclosedVendor: " + disclosedVendor +
		       ", allowedVendors: " + allowedVendors +
		       '}';
	}
}
