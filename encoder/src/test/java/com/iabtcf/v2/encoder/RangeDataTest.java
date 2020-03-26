package com.iabtcf.v2.encoder;

import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author evanwht1@gmail.com
 */
public class RangeDataTest {

	@Test
	void testBuildSingleRange() {
		final RangeData rangeData = new RangeData();
		rangeData.add(1, 2, 3);
		assertEquals(1, rangeData.size());
		assertEquals(3, rangeData.getMaxId());
		final RangeData.Range first = rangeData.getRanges().iterator().next();
		assertEquals(1, first.lower);
		assertEquals(3, first.upper);
	}

	@Test
	void testBuildMultiRange() {
		final RangeData rangeData = new RangeData();
		rangeData.add(1, 2, 3, 5, 7, 8, 9);
		assertEquals(3, rangeData.size());
		assertEquals(9, rangeData.getMaxId());

		Set<RangeData.Range> expectedRanges = Set.of(
				new RangeData.Range(1, 3),
				new RangeData.Range(5),
				new RangeData.Range(7, 9)
				);
		assertEquals(expectedRanges, rangeData.getRanges());
	}

	@Test
	void testAddToExistingRange() {
		final RangeData rangeData = new RangeData();
		rangeData.add(1, 2, 3, 4);
		assertEquals(1, rangeData.size());
		rangeData.add(2);
		assertEquals(1, rangeData.size());
	}

	@Test
	void testRangesInOrder() {
		final RangeData rangeData = new RangeData();
		rangeData.add(1, 2, 3, 5, 7, 8, 9);
		assertEquals(3, rangeData.size());
		assertEquals(9, rangeData.getMaxId());

		final Iterator<RangeData.Range> iterator = rangeData.getRanges().iterator();
		final RangeData.Range prev = iterator.next();
		while (iterator.hasNext()) {
			final RangeData.Range cur = iterator.next();
			assertTrue(prev.upper < cur.lower);
		}
	}
}
