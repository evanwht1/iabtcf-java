package com.iabtcf.v2.decoder.exceptions;

import java.io.IOException;

/**
 * @author ewhite 4/8/20
 */
public class UnsupportedVersionException extends IOException {

	/**
	 * Constructs an UnsupportedEncodingException without a detail message.
	 */
	public UnsupportedVersionException() {
		super();
	}

	/**
	 * Constructs an UnsupportedEncodingException with a detail message.
	 * @param version version encountered
	 */
	public UnsupportedVersionException(int version) {
		super(String.format("Unsupported version: %d", version));
	}
}
