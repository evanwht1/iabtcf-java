package com.iabtcf.v2.decoder;

import com.iabtcf.v2.Field;
import com.iabtcf.v2.SegmentType;
import com.iabtcf.v2.TCModel;
import com.iabtcf.v2.decoder.exceptions.UnsupportedVersionException;

import static com.iabtcf.v2.Field.Vendors.SEGMENT_TYPE;

/**
 * Decoder to read all information from a IAB TC v2 String.
 *
 * @author SleimanJneidi
 * @author evanwht1
 */
public class TCModelDecoder {

    public static TCModel decode(String consentString) throws UnsupportedVersionException {
        final String[] split = consentString.split("\\.");
        final BitInputStream coreStringVector = BitInputStream.fromBase64String(split[0]);

        int version = coreStringVector.readInt(Field.CoreString.VERSION);
        // TODO : add version 1
        if (version == 2) {
            final TCModelImpl.Builder builder = TCModelImpl
                    .newBuilder()
                    .coreString(() -> CoreStringDecoder.decode(version, coreStringVector));
            for (int i = 1; i < split.length; i++) {
                readOptionalSegment(builder, BitInputStream.fromBase64String(split[i]));
            }
            return builder.build();
        }
        throw new UnsupportedVersionException(version);
    }

    private static void readOptionalSegment(TCModelImpl.Builder builder, BitInputStream bitInputStream) {
        final SegmentType segmentType = SegmentType.valueOf(bitInputStream.readInt(SEGMENT_TYPE));
        switch (segmentType) {
            case DISCLOSED_VENDOR:
                builder.disclosedVendors(() -> VendorsDecoder.decode(bitInputStream));
                break;
            case ALLOWED_VENDOR:
                builder.allowedVendors(() -> VendorsDecoder.decode(bitInputStream));
                break;
            case PUBLISHER_TC:
                builder.publisherPurposes(() -> PublisherTCDecoder.decode(bitInputStream));
                break;
        }
    }
}
