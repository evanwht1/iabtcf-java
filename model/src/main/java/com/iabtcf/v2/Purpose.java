package com.iabtcf.v2;

/**
 * @author evanwht1
 */
public enum Purpose {

	STORE_AND_ACCESS_INFO_ON_DEVICE(1),
	SELECT_BASIC_ADS(2),
	CREATE_PERSONALISED_ADS_PROFILE(3),
	SELECT_PERSONAL_ADS(4),
	CREATE_PERSONAL_CONTENT_PROFILE(5),
	SELECT_PERSONALISED_CONTENT(6),
	MEASURE_AD_PERFORMANCE(7),
	MEASURE_CONTENT_PERFORMANCE(8),
	APPLY_MARKET_RESEARCH_TO_GENERATE_AUDIENCE_INSIGHTS(9),
	DEVELOP_AND_IMPROVE_PRODUCTS(10),
	;

	private final int id;

	Purpose(final int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public static Purpose valueOf(final int id) {
		switch (id) {
			case 1: return STORE_AND_ACCESS_INFO_ON_DEVICE;
			case 2: return SELECT_BASIC_ADS;
			case 3: return CREATE_PERSONALISED_ADS_PROFILE;
			case 4: return SELECT_PERSONAL_ADS;
			case 5: return CREATE_PERSONAL_CONTENT_PROFILE;
			case 6: return SELECT_PERSONALISED_CONTENT;
			case 7: return MEASURE_AD_PERFORMANCE;
			case 8: return MEASURE_CONTENT_PERFORMANCE;
			case 9: return APPLY_MARKET_RESEARCH_TO_GENERATE_AUDIENCE_INSIGHTS;
			case 10: return DEVELOP_AND_IMPROVE_PRODUCTS;
			default:
				throw new IllegalArgumentException("No purpose for id " + id);
		}
	}
}
