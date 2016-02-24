package org.kidneyomics.util.interval;

public interface Interval<T> extends Comparable<Interval<T>> {
	T payload();
	int length();
	int start();
	int end();
	int overlap(Interval<T> interval);
	boolean overlapsWith(Interval<T> interval);
}
