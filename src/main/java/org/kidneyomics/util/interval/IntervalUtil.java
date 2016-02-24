package org.kidneyomics.util.interval;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class IntervalUtil {

	/**
	 * 
	 * @param intervals to merge
	 * @return a list of buckets containing the intervals
	 */
	public static <T> IntervalBucket<T>[] mergeOverlappingIntervals(List<Interval<T>> intervals) {
		ArrayList<IntervalBucket<T>> buckets = new ArrayList<>();
		
		Collections.sort(intervals);
		for(Interval<T> interval : intervals) {
			boolean added = false;
			for(IntervalBucket<T> bucket : buckets) {
				if(interval.overlapsWith(bucket)) {
					bucket.addInterval(interval);
					added = true;
					break;
				}
			}
			if(!added) {
				IntervalBucket<T> bucket = new IntervalBucket<T>(interval.start(), interval.end(), interval.payload());
				buckets.add(bucket);
			}
		}

		Collections.sort(buckets);
		
		IntervalBucket<T>[] array = new IntervalBucket[buckets.size()];
		buckets.toArray(array);
		return array;
	}
	
	/**
	 * 
	 * @param array --- sorted array of non overlapping buckets
	 * @param target --- target interval
	 * @param comparator --- comparator for binary search
	 * @return a list of best matching intervals
	 */
	public static <T> List<Interval<T>> findBestMatches(IntervalBucket<T>[] buckets, Interval<T> target, IntersectionComparator<T> comparator) {
		
		LinkedList<Interval<T>> results = new LinkedList<>();
		int index = Arrays.binarySearch(buckets, target, comparator);
		if(index >= 0) {
			IntervalBucket<T> bucket = buckets[index];
			results.addAll(bucket.findBestMatches(target));
		}
		
		return results;
	}
	
	
}
