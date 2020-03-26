package com.iabtcf.v2.decoder;

import com.iabtcf.v2.Field;
import com.iabtcf.v2.SegmentType;
import com.iabtcf.v2.TCModel;

import java.util.Base64;

import static com.iabtcf.v2.Field.Vendors.SEGMENT_TYPE;

/**
 * Decoder to read all information from a IAB TC v2 String.
 *
 * @author SleimanJneidi
 * @author evanwht1
 */
public class TCModelDecoder {

    private static final Base64.Decoder DECODER = Base64.getUrlDecoder();

    public static TCModel decode(String consentString) {
        final String[] split = consentString.split("\\.");
        final BitInputStream coreStringVector = vectorFromString(split[0]);

        int version = coreStringVector.readInt(Field.CoreString.VERSION);
        switch (version) {
            case 1:
                // TODO : add version1
                throw new UnsupportedOperationException("Version 1 not supported yet");
            case 2:
                final TCModelImpl.Builder builder = TCModelImpl
                        .newBuilder()
                        .coreString(() -> CoreStringDecoder.decode(version, coreStringVector));
                for (int i = 1; i < split.length; i++){
                    readOptionalVector(builder, vectorFromString(split[i]));
                }
                return builder.build();
            default:
                throw new UnsupportedOperationException("Version " + version + " unsupported");
        }
    }


    private static void readOptionalVector(TCModelImpl.Builder builder, BitInputStream bitInputStream) {
        SegmentType segmentType = SegmentType.valueOf(bitInputStream.readInt(SEGMENT_TYPE));
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

    private static BitInputStream vectorFromString(String base64UrlEncodedString) {
        byte[] bytes = DECODER.decode(base64UrlEncodedString);
        return BitInputStream.from(bytes);
    }
}
