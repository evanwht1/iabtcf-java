package com.iabtcf.v2;

import org.junit.Test;

import java.util.BitSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class PublisherRestrictionTest {

    @Test
    public void testEquals() {
        PublisherRestriction pub1 = new PublisherRestriction(RestrictionType.NOT_ALLOWED, bitSetOf(1, 2));
        PublisherRestriction pub2 = new PublisherRestriction(RestrictionType.NOT_ALLOWED, bitSetOf(2, 1));

        assertNotNull(pub1);
        assertEquals(pub1, pub2);
        assertEquals(pub1, pub1);
    }

    @Test
    public void testHash() {
        PublisherRestriction pub1 = new PublisherRestriction(RestrictionType.NOT_ALLOWED, bitSetOf(1, 2));
        PublisherRestriction pub2 = new PublisherRestriction(RestrictionType.NOT_ALLOWED, bitSetOf(1, 2));

        assertEquals(pub1.hashCode(), pub2.hashCode());
    }

    private static BitSet bitSetOf(Integer... ints) {
        final BitSet bitSet = new BitSet(ints.length);
        for (int i : ints) {
            bitSet.set(i);
        }
        return bitSet;
    }
}
