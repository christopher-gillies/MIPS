package org.kidneyomics.util.interval;

import java.util.Comparator;

public class IntersectionComparator implements Comparator<Interval<?>> {

	@Override
	public int compare(Interval<?> o1, Interval<?> o2) {
		
		if(o1.overlapsWith(o2)) {
			return 0;
		} else {
			return o1.compareTo(o2);
		}
	}

}
