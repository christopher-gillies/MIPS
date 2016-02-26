package org.kidneyomics.util;

import org.kidneyomics.util.interval.Interval;
import org.kidneyomics.util.interval.IntervalImpl;

public class BEDEntry implements Comparable<BEDEntry> {
	
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
	
	public static Interval<BEDEntry> merge(Interval<BEDEntry> a, Interval<BEDEntry> b) {
		Interval<BEDEntry> result = null;
		BEDEntry entryA = a.payload();
		BEDEntry entryB = b.payload();
		
		if(entryA.chr().equals(entryB.chr())) {
			BEDEntry merged = new BEDEntry(entryA.chr, Math.min(entryA.start, entryB.start), Math.max(entryA.end, entryB.end)   );
			result = a.merge(b, merged);
		}
		
		return result;
		
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof BEDEntry) {
			BEDEntry other = (BEDEntry) o;
			return other.chr.equals(this.chr) && other.end == this.end && other.start == this.start;
		} else {
			return false;
		}
	}
	
	@Override
	public String toString() {
		return chr + ":" + start + "-" + end;
	}
	
	public String toBEDString() {
		return chr + "\t" + start + "\t" + end;
	}
	
	
	@Override
	public int hashCode() {
		int result = 17;
		result = 31 * result + start;
		result = 31 * result + end;
		result = 31 * result + chr.hashCode();
		return result;
	}

	@Override
	public int compareTo(BEDEntry o) {
		if(this.chr.equals(o.chr)) {
			return this.start - o.start;
		} else {
			return this.chr.compareTo(o.chr);
		}
	}
	
}
