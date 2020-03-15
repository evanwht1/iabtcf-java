package com.iabtcf.v2.decoder;

import java.util.BitSet;
import java.util.function.Supplier;

/**
 * @author evanwht1@gmail.com
 */
class Constants {

	private Constants() { }

	static final BitSet EMPTY_BIT_SET = new BitSet();

	static final Supplier<BitSet> EMPTY_SUPPLIER = () -> EMPTY_BIT_SET;
}
