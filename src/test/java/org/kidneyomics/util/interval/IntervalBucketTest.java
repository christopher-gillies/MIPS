package org.kidneyomics.util.interval;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

public class IntervalBucketTest {

	@Test
	public void testAdd() {
		IntervalBucket<Integer> bucket = new IntervalBucket<Integer>(50, 100, null);
		
		assertEquals(50,bucket.start);
		assertEquals(100,bucket.end);
		
		
		bucket.addInterval(new IntervalImpl<Integer>(25, 50, null));
		
		assertEquals(2,bucket.size());
		assertEquals(25,bucket.start);
		assertEquals(100,bucket.end);
		
		
		bucket.addInterval(new IntervalImpl<Integer>(10, 200, null));
		
		assertEquals(3,bucket.size());
		assertEquals(10,bucket.start);
		assertEquals(200,bucket.end);
		
		
		bucket.addInterval(new IntervalImpl<Integer>(200, 250, null));
		
		assertEquals(4,bucket.size());
		assertEquals(10,bucket.start);
		assertEquals(250,bucket.end);
		
		
	}
	
	
	@Test
	public void findBestMatch() {
		IntervalBucket<Integer> bucket = new IntervalBucket<Integer>(200, 200, null);
		for(int i = 0; i < 100; i++) {
			IntervalImpl<Integer> interval = new IntervalImpl<Integer>(200 + i, 200 + i + 1, null);
			bucket.addInterval(interval);
		}
		
		List<Interval<Integer>> bestIntervals = bucket.findBestMatches( new IntervalImpl<Integer>(200, 201, null));
		
		assertEquals(1,bestIntervals.size());
		
		System.err.println(bestIntervals.get(0));
		
		assertEquals(200,bestIntervals.get(0).start());
		assertEquals(201,bestIntervals.get(0).end());
	}

}
