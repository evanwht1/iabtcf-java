package com.iabtcf.v2;

/**
 * @author evanwht1
 */
public enum SpecialFeature {

	USE_PRECISE_GEOLOCATION_DATA(1),
	ACTIVELY_SCAN_DEVICE_CHARACTERISTICS_FOR_IDENTIFICATION(2),
	;

	private final int id;

	SpecialFeature(final int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
}
