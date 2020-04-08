package com.iabtcf.v2.encoder;

import com.iabtcf.v2.Field;

import java.time.Instant;

/**
 * @author evanwht1@gmail.com
 */
class BitOutputStream {

	private static final int DEFAULT_SIZE = 2048;

	private byte[] buffer;
	private int pos;

	BitOutputStream() {
		this(DEFAULT_SIZE);
	}

	BitOutputStream(int size) {
		buffer = new byte[bytesForBits(size)];
		pos = 0;
	}

	private void ensureWriteable(int length) {
		if (length >= ((buffer.length * Byte.SIZE) - pos)) {
			final byte[] newBuffer = new byte[buffer.length * 2];
			System.arraycopy(buffer, 0, newBuffer, 0, pos);
			buffer = newBuffer;
		}
	}

	void write(Field field, int value) {
		write(field.getLength(), value);
	}

	void write(Field field, Instant value) {
		write(field.getLength(), value.toEpochMilli() / 100);
	}

	void write(Field field, long value) {
		write(field.getLength(), value);
	}

	void write(int length, long value) {
		ensureWriteable(length);
		for (int shiftPos = length - 1; shiftPos >= 0; shiftPos--) {
			writeBit((value >>> shiftPos & 1) == 1);
		}
	}

	void write(Field field, String value) {
		assert (field.getLength() % Field.CHAR_LENGTH) == 0 && (field.getLength() / Field.CHAR_LENGTH) == value.length();
		write(value);
	}

	void write(String value) {
		for (byte b : value.getBytes()) {
			write(Field.CHAR_LENGTH, (b - 'A'));
		}
	}

	void write(Field field, ValueChecker checker) {
		write(field.getLength(), checker);
	}

	void write(int length, ValueChecker checker) {
		for (int i = 1; i <= length; i++) {
			write(checker.isSet(i));
		}
	}

	void write(Field field, boolean set) {
		assert field.getLength() == 1;
		writeBit(set);
	}

	void write(boolean set) {
		writeBit(set);
	}

	private void writeBit(boolean set) {
		if (set) {
			buffer[pos / Byte.SIZE] |= (1 << (Byte.SIZE - 1 - (pos % Byte.SIZE)));
		}
		pos++;
	}

	byte[] toByteArray() {
		int byteLength = bytesForBits(pos);
		byte[] arr = new byte[byteLength];
		System.arraycopy(buffer, 0, arr, 0, byteLength);
		return arr;
	}

	private int bytesForBits(int bits) {
		return (bits / Byte.SIZE) + ((bits % Byte.SIZE) >= 1 ? 1 : 0);
	}
}
