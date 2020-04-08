package com.iabtcf.v2.encoder;

import com.iabtcf.v2.Field;

import java.util.BitSet;

/**
 * @author ewhite 4/8/20
 */
public class BitOutputStreamUtil {

	static void write(BitOutputStream bs, Field field, BitSet set) {
		write(bs, field.getLength(), set);
	}

	static void write(BitOutputStream bs, int length, BitSet set) {
		for (int i = 1; i <= length; i++) {
			bs.write(set.get(i));
		}
	}

	static void write(BitOutputStream bs, Field field, ValueChecker checker) {
		write(bs, field.getLength(), checker);
	}

	static void write(BitOutputStream bs, int length, ValueChecker checker) {
		for (int i = 1; i <= length; i++) {
			bs.write(checker.isSet(i));
		}
	}
}
