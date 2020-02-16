package com.iabtcf.v2;

public enum RestrictionType {
    NOT_ALLOWED,
    REQUIRE_CONSENT,
    REQUIRE_LEGITIMATE_INTEREST;

    public static RestrictionType fromId(int id) {
        switch (id) {
            case 1:
                return REQUIRE_CONSENT;
            case 2:
                return REQUIRE_LEGITIMATE_INTEREST;
            default:
                return NOT_ALLOWED;
        }
    }
}