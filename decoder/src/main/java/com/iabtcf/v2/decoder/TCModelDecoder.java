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
        return decode(consentString, true);
    }

    public static TCModel decode(String consentString, boolean lazy) throws UnsupportedVersionException {
        final String[] split = consentString.split("\\.");
        final BitInputStream coreStringVector = BitInputStream.fromBase64String(split[0]);

        int version = coreStringVector.readInt(Field.CoreString.VERSION);
        // TODO : add version 1
        if (version == 2) {
                final LazyTCModel.Builder builder = LazyTCModel
                        .newBuilder()
                        .coreString(() -> CoreStringDecoder.decode(version, coreStringVector));
                for (int i = 1; i < split.length; i++) {
                    readOptionalLazySegment(builder, BitInputStream.fromBase64String(split[i]));
                }
                return builder.build(lazy);
        }
        throw new UnsupportedVersionException(version);
    }

    private static void readOptionalLazySegment(LazyTCModel.Builder builder, BitInputStream bitInputStream) {
        switch (SegmentType.valueOf(bitInputStream.readInt(SEGMENT_TYPE))) {
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
