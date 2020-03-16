package com.iabtcf.v2;

import java.util.stream.IntStream;

public interface PublisherRestriction {

    Purpose getPurpose();

    RestrictionType getRestrictionType();

    boolean isVendorIncluded(final int vendor);

    IntStream getAllVendors();

}
