package org.kidneyomics.util.interval;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

public class IntervalUtilTest {

	@Test
	public void testMergeOverlapping() {
		List<Interval<Integer>> list = new LinkedList<>();
		
		for(int i = 200; i < 300; i++) {
			IntervalImpl<Integer> interval = new IntervalImpl<Integer>(200 , i, null);
			list.add(interval);
		}
		
		for(int i = 100; i < 200; i++) {
			IntervalImpl<Integer> interval = new IntervalImpl<Integer>(100 , i, null);
			list.add(interval);
		}
		
		for(int i = 0; i < 100; i++) {
			IntervalImpl<Integer> interval = new IntervalImpl<Integer>(0 , i, null);
			list.add(interval);
		}
		
		
		
		IntervalBucket<Integer>[] buckets = IntervalUtil.mergeOverlappingIntervals(list);
		
		assertEquals(3,buckets.length);
		
		int sum = 0;
		for(IntervalBucket<Integer> bucket : buckets) {
			System.err.println(bucket);
			sum += bucket.size();
		}
		
		assertEquals(0,buckets[0].start());
		assertEquals(99,buckets[0].end());
		
		
		assertEquals(100,buckets[1].start());
		assertEquals(199,buckets[1].end());
		
		assertEquals(200,buckets[2].start());
		assertEquals(299,buckets[2].end());
		
		assertEquals(300,sum);
		
		
	}
	
	@Test
	public void findBestMatches() {
		List<Interval<Integer>> list = new LinkedList<>();
		
		for(int i = 200; i < 300 - 1; i++) {
			IntervalImpl<Integer> interval = new IntervalImpl<Integer>(i ,  i + 1, null);
			list.add(interval);
		}
		
		for(int i = 100; i < 200 - 1; i++) {
			IntervalImpl<Integer> interval = new IntervalImpl<Integer>(i , i + 1, null);
			list.add(interval);
		}
		
		for(int i = 0; i < 100 - 1; i++) {
			IntervalImpl<Integer> interval = new IntervalImpl<Integer>(i , i + 1, null);
			list.add(interval);
		}
		
		
		
		IntervalBucket<Integer>[] buckets = IntervalUtil.mergeOverlappingIntervals(list);
		
		for(int i = 0; i < buckets.length; i++) {
			System.err.println(buckets[i]);
		}
		
		List<Interval<Integer>> results = IntervalUtil.findBestMatchesByOverlap(buckets, new IntervalImpl<Integer>(100,101,null), new IntersectionComparator<Integer>() );
		
		assertEquals(1,results.size());
	}
}
