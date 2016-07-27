package org.kidneyomics.util.interval;

public interface Interval<T> extends Comparable<Interval<?>> {
	T payload();
	int length();
	int start();
	int end();
	int overlap(Interval<?> interval);
	boolean overlapsWith(Interval<?> interval);
	/**
	 * 
	 * @param interval
	 * @return a new interval spanning both the input interval and the current interval.
	 */
	Interval<T> merge(Interval<T> interval, T payload);
}
