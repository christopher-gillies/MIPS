package org.kidneyomics.util.interval;

import java.util.Comparator;

public class IntersectionComparator<T> implements Comparator<Interval<T>> {

	@Override
	public int compare(Interval<T> o1, Interval<T> o2) {
		
		if(o1.overlapsWith(o2)) {
			return 0;
		} else {
			return o1.compareTo(o2);
		}
	}

}
