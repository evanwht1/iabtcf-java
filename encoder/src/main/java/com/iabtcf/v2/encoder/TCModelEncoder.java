package com.iabtcf.v2.encoder;

import com.iabtcf.v2.Field.Vendors;
import com.iabtcf.v2.SegmentType;

import java.util.StringJoiner;

/**
 * @author evanwht1@gmail.com
 */
class TCModelEncoder {

	static String encode(TCModelBuilder builder) {
		final StringJoiner sj = new StringJoiner(".")
				.add(CoreStringEncoder.writeCoreString(builder.coreStringBuilder));

		final OutOfBandBuilder outOfBandBuilder = builder.getOutOfBandBuilder();
		if (!outOfBandBuilder.disclosedVendors.isEmpty()) {
			sj.add(VendorEncoder.writeVendorString(SegmentType.DISCLOSED_VENDOR, outOfBandBuilder.disclosedVendors));
		}
		if (!outOfBandBuilder.allowedVendors.isEmpty()) {
			sj.add(VendorEncoder.writeVendorString(SegmentType.ALLOWED_VENDOR, outOfBandBuilder.disclosedVendors));
		}
		final PublisherTCBuilder publisherTCBuilder = builder.getPublisherTCBuilder();
		if (!publisherTCBuilder.publisherPurposes.isEmpty() || !publisherTCBuilder.publisherPurposesLI.isEmpty()
				|| ! publisherTCBuilder.publisherCustomPurposes.isEmpty() || !publisherTCBuilder.publisherCustomPurposesLI.isEmpty()) {
			sj.add(PublisherTCEncoder.writePublisherTCString(builder.getPublisherTCBuilder()));
		}

		return sj.toString();
	}

	static void writeRange(Bits bits, RangeData data) {
		int maxVendor = data.getMaxId();
		bits.write(Vendors.MAX_VENDOR_ID, maxVendor);
		if (!shouldRangeEncode(maxVendor, data)) {
			bits.write(Vendors.IS_RANGE_ENCODING, false);
			for (int i = 0; i < maxVendor; i++) {
				bits.write(data.get(i));
			}
		} else {
			bits.write(Vendors.IS_RANGE_ENCODING, true);
			bits.write(Vendors.NUM_ENTRIES, data.size());
			data.getRanges().forEach(r -> {
				bits.write(Vendors.IS_A_RANGE, r.isARange());
				bits.write(Vendors.START_OR_ONLY_VENDOR_ID, r.lower);
				if (r.isARange()) {
					bits.write(Vendors.END_VENDOR_ID, r.upper);
				}
			});
		}
	}

	private static final int MIN_RANGE_ENCODING_LENGTH =
			Vendors.NUM_ENTRIES.getLength() +
			Vendors.IS_A_RANGE.getLength() +
			Vendors.START_OR_ONLY_VENDOR_ID.getLength();

	/**
	 * Determines whether it would be better to encode a list as a bit field or a range encoding. Logic follows that if
	 * the max vendor id is small enough and the cardinality of the set is small enough, encoding as a bit field will
	 * take up less space.
	 *
	 * @param maxId maxId in the set that is trying to be encoded
	 * @param rangeData number of ids that need to be set
	 * @return if the set would be better encoded as a bit field.
	 */
	static boolean shouldRangeEncode(int maxId, RangeData rangeData) {
		if (maxId <= MIN_RANGE_ENCODING_LENGTH) {
			return false;
		}
		final int rangeSize = rangeData.getRanges()
		                               .stream()
		                               .mapToInt(r -> Vendors.IS_A_RANGE.getLength() +
		                                              Vendors.START_OR_ONLY_VENDOR_ID.getLength() +
		                                              (r.isARange() ? Vendors.END_VENDOR_ID.getLength() : 0))
		                               .sum();
		return  maxId > (Vendors.NUM_ENTRIES.getLength() + rangeSize);
	}
}
