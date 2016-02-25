package org.kidneyomics.util.interval;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class IntervalBucket<T> extends IntervalImpl<T> {

	private final Set<Interval<T>> subIntervals;
	
	public IntervalBucket(int start, int end, T payload) {
		super(start, end, null);
		this.subIntervals = new HashSet<>();
		
		IntervalImpl<T> firstInterval = new IntervalImpl<T>(start, end, payload);
		addInterval(firstInterval);
	}
	
	public IntervalBucket(Interval<T> interval) {
		super(interval.start(), interval.end(), null);
		this.subIntervals = new HashSet<>();
		
		addInterval(interval);
	}
	
	public IntervalBucket<T> addInterval(Interval<T> interval) {
		
		if(this.overlapsWith(interval)) {
			
			if(this.start > interval.start()) {
				this.start = interval.start();
			}
			
			if(this.end < interval.end()) {
				this.end = interval.end();
			}
			
			subIntervals.add(interval);
			
			
		} else {
			throw new IllegalArgumentException(interval + " does not overlap with " + this);
		}
		
		return this;
		
	}
	
	public List<Interval<T>> findBestMatchesByOverlap(Interval<T> interval) {
		List<Interval<T>> results = new LinkedList<>();
		if(this.overlapsWith(interval)) {
			int bestOverlap = 0;
			for(Interval<T> item : subIntervals) {
				int tmpOverlap = item.overlap(interval);
				if(tmpOverlap > bestOverlap) {
					bestOverlap = tmpOverlap;
					//clear out old results
					results.clear();
					
					results.add(item);
				} else if(tmpOverlap == bestOverlap) {
					results.add(item);
				}
			}
			return results;
		} else {
			return results;
		}
	}
	
	public List<Interval<T>> findBestMatchesByStartAndEndPosition(Interval<T> interval) {
		List<Interval<T>> results = new LinkedList<>();
		if(this.overlapsWith(interval)) {
			int closestStart = Integer.MAX_VALUE;
			int closestEnd = Integer.MAX_VALUE;
			for(Interval<T> item : subIntervals) {
				int distanceStart = Math.abs(item.start() - interval.start());
				int distanceEnd = Math.abs(item.end() - interval.end());
				//if the start is less than the closest start or the distance start = the current distance start and the end is closer, then clear out current results and update best values
				if(distanceStart < closestStart || (distanceStart == closestStart && distanceEnd < closestEnd) ) {
					closestStart = distanceStart;
					closestEnd = distanceEnd;
					//clear out old results
					results.clear();
					
					results.add(item);
					//perfect match based on start and end then add
				} else if(distanceStart == closestStart && distanceEnd == closestEnd) {
					//this should not actually be possible with current implementation, because intervals are defined as equal if their start and end positions match
					//the sub intervals are stored in a set so these would be identical
					results.add(item);
				}
			}
			return results;
		} else {
			return results;
		}
	}
	
	public int size() {
		return this.subIntervals.size();
	}
	
	

}
