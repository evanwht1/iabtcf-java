package com.iabtcf.v2.encoder;

import com.iabtcf.v2.Field;
import com.iabtcf.v2.SegmentType;

import java.util.Base64;

import static com.iabtcf.v2.encoder.TCModelEncoder.writeRange;

/**
 * @author ewhite 3/26/20
 */
public class VendorEncoder {

	static String encode(SegmentType type, RangeData data) {
		final BitOutputStream bitOutputStream = new BitOutputStream();
		bitOutputStream.write(Field.Vendors.SEGMENT_TYPE, type.getValue());
		writeRange(bitOutputStream, data);
		return Base64.getEncoder().encodeToString(bitOutputStream.toByteArray());
	}
}
