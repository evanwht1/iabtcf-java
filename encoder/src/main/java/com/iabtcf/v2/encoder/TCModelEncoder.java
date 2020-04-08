package com.iabtcf.v2.encoder;

import com.iabtcf.v2.Field.Vendors;
import com.iabtcf.v2.SegmentType;
import com.iabtcf.v2.TCModel;

import java.util.StringJoiner;
import java.util.stream.IntStream;

/**
 * @author evanwht1@gmail.com
 */
class TCModelEncoder {

	static String encode(TCModel builder) {
		final StringJoiner sj = new StringJoiner(".")
				.add(CoreStringEncoder.encode(builder.getCoreString()));

		builder.getOutOfBandConsent().ifPresent(oob -> {
			if (oob.getAllDisclosedVendors().count() > 0) {
				sj.add(VendorEncoder.encode(SegmentType.DISCLOSED_VENDOR, RangeData.from(oob.getAllDisclosedVendors())));
			}
			if (oob.getAllAllowedVendors().count() > 0) {
				sj.add(VendorEncoder.encode(SegmentType.ALLOWED_VENDOR, RangeData.from(oob.getAllAllowedVendors())));
			}
		});
		builder.getPublisherTC().ifPresent(ptc -> sj.add(PublisherTCEncoder.encode(ptc)));

		return sj.toString();
	}

	static void writeRange(BitOutputStream bs, IntStream stream) {
		writeRange(bs, RangeData.from(stream));
	}

	static void writeRange(BitOutputStream bs, RangeData data) {
		int maxVendor = data.getMaxId();
		bs.write(Vendors.MAX_VENDOR_ID, maxVendor);
		if (!shouldRangeEncode(maxVendor, data)) {
			bs.write(Vendors.IS_RANGE_ENCODING, false);
			for (int i = 1; i <= maxVendor; i++) {
				bs.write(data.get(i));
			}
		} else {
			bs.write(Vendors.IS_RANGE_ENCODING, true);
			bs.write(Vendors.NUM_ENTRIES, data.size());
			data.getRanges().forEach(r -> {
				bs.write(Vendors.IS_A_RANGE, r.isARange());
				bs.write(Vendors.START_OR_ONLY_VENDOR_ID, r.lower);
				if (r.isARange()) {
					bs.write(Vendors.END_VENDOR_ID, r.upper);
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
