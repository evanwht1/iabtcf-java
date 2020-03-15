package com.iabtcf.v2.encoder;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author evanwht1@gmail.com
 */
public class RangeData {

	private int maxId;
	private Set<Range> ranges;

	RangeData() {
		maxId = -1;
		ranges = new TreeSet<>();
	}

	boolean isEmpty() {
		return maxId == -1;
	}

	void add(final int i) {
		maxId = Math.max(i, maxId);
		// first see if this can be added to an existing range
		boolean needsNewRange = true;
		for (Range range : ranges) {
			if (range.add(i)) {
				compactRanges();
				needsNewRange = false;
				break;
			}
		}
		if (needsNewRange) {
			ranges.add(new Range(i));
		}
	}

	public boolean get(int i) {
		for (Range range: ranges) {
			if (range.contains(i)) {
				return true;
			}
		}
		return false;
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

	public int getMaxId() {
		return maxId;
	}

	public Set<Range> getRanges() {
		return ranges;
	}

	public int size() {
		return ranges.size();
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
	}
}
