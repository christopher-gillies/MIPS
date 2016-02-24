package org.kidneyomics.util;

import org.kidneyomics.util.interval.Interval;
import org.kidneyomics.util.interval.IntervalImpl;

public class BEDEntry {
	
	private final String chr;
	private final int start;
	private final int end;
	
	public BEDEntry(String chr, int start, int end) {
		this.chr = chr;
		this.start = start;
		this.end = end;
	}
	
	public String chr() {
		return chr;
	}
	
	public int start1Based() {
		return start + 1;
	}
	
	public int end1Based() {
		return end + 1;
	}
	
	public Interval<BEDEntry> toInterval() {
		return new IntervalImpl<BEDEntry>(this.start1Based(), this.end1Based(), this);
	}
}
