package com.iabtcf.v2.encoder;

/**
 * @author ewhite 4/8/20
 */
@FunctionalInterface
public interface ValueChecker {

	boolean check(int value);
}
