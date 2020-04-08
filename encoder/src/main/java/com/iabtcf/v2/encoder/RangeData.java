package com.iabtcf.v2.encoder;

import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.IntStream;

/**
 * @author evanwht1@gmail.com
 */
public class RangeData {

	private int maxId = -1;
	private Set<Range> ranges = new TreeSet<>();

	public static RangeData from(IntStream stream) {
		final RangeData rangeData = new RangeData();
		stream.forEach(rangeData::add);
		return rangeData;
	}

	boolean isEmpty() {
		return maxId == -1;
	}

	void add(final int... ids) {
		for (int id : ids) {
			add(id);
		}
	}

	void add(final int id) {
		if (id < 0) {
			throw new IllegalArgumentException("input must be greater than 0");
		}
		maxId = Math.max(id, maxId);
		// first see if this can be added to an existing range
		boolean needsNewRange = true;
		for (Range range : ranges) {
			if (range.add(id)) {
				compactRanges();
				needsNewRange = false;
				break;
			}
		}
		if (needsNewRange) {
			ranges.add(new Range(id));
		}
	}

	boolean get(int id) {
		for (Range range: ranges) {
			if (range.contains(id)) {
				return true;
			}
		}
		return false;
	}

	int getMaxId() {
		return maxId;
	}

	private void compactRanges() {
		final Iterator<Range> iterator = ranges.iterator();
		Range prev = iterator.next();
		while (iterator.hasNext() && prev != null) {
			final Range cur = iterator.next();
			if (prev.merge(cur)) {
				iterator.remove();
			} else {
				prev = cur;
			}
		}
	}

	public Set<Range> getRanges() {
		return ranges;
	}

	public int size() {
		return ranges.size();
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		final RangeData rangeData = (RangeData) o;
		return maxId == rangeData.maxId &&
		       ranges.equals(rangeData.ranges);
	}

	@Override
	public int hashCode() {
		return Objects.hash(maxId, ranges);
	}

	@Override
	public String toString() {
		return "RangeData{" +
		       "maxId: " + maxId +
		       ", ranges: " + ranges +
		       '}';
	}

	static class Range implements Comparable<Range> {

		int lower;
		int upper;

		Range(final int value) {
			this(value, value);
		}

		Range(final int lower, final int upper) {
			this.lower = lower;
			this.upper = upper;
		}

		boolean isARange() {
			return lower != upper;
		}

		boolean add(int val) {
			if (lower <= val && val <= upper) {
				return true;
			} else if (val == (lower - 1)) {
				lower = val;
				return true;
			} else if (val == (upper + 1)) {
				upper = val;
				return true;
			}
			return false;
		}

		boolean contains(int i) {
			return lower <= i && i <= upper;
		}

		boolean merge(Range other) {
			if (lower == (other.upper + 1)) {
				lower = other.lower;
				return true;
			} else if (upper == (other.lower - 1)) {
				upper = other.upper;
				return true;
			}
			return false;
		}

		@Override
		public int compareTo(final Range o) {
			// -1 is this is less than o
			// 0 is same
			// 1 is this is more than o
			if (lower > o.upper) {
				return 1;
			} else if (upper < o.lower) {
				return -1;
			} else {
				// SHOULD NEVER HAVE RANGES THAT INTERSECT. those should be merged
				return 0;
			}
		}

		@Override
		public boolean equals(final Object o) {
			if (this == o) {
				return true;
			}
			if (o == null || getClass() != o.getClass()) {
				return false;
			}
			final Range range = (Range) o;
			return lower == range.lower &&
			       upper == range.upper;
		}

		@Override
		public int hashCode() {
			return Objects.hash(lower, upper);
		}

		@Override
		public String toString() {
			return "Range{" +
			       "lower: " + lower +
			       ", upper: " + upper +
			       '}';
		}
	}
}
