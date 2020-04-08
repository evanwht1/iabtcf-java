package com.iabtcf.v2;

import java.util.stream.IntStream;

public interface PublisherRestriction {

    int getPurposeId();

    RestrictionType getRestrictionType();

    boolean isVendorIncluded(final int vendor);

    IntStream getAllVendors();

}
