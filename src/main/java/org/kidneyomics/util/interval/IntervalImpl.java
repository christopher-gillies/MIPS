package org.kidneyomics.util.interval;

public class IntervalImpl<T> implements Interval<T> {

	int start;
	int end;
	T payload;
	
	public IntervalImpl(int start, int end, T payload) {
		this.payload = payload;
		this.start = start;
		this.end = end;
		if(this.start < 0 || this.end < 0) {
			throw new IllegalArgumentException("start and end both must be positive");
		}
		if(start > end) {
			System.err.println("start: " + start + " end: " + end);
			throw new IllegalArgumentException("start must be less than or equal to the end");
		}
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof IntervalImpl) {
			IntervalImpl<?> other = (IntervalImpl<?>) o;
			if(this.start == other.start && this.end == other.end) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		int result = 17;
		result = 31 * result + start;
		result = 31 * result + end;
		return result;
	}

	@Override
	public int compareTo(Interval<?> b) {
		int cmp = this.start() - b.start();
		if(cmp == 0) {
			return this.length() - b.length();
		} else if(cmp < 0) {
			return -1;
		} else {
			return 1;
		}
	}
	
	public String toString() {
		return "[ " + this.start + "-" + this.end + " ]";
	}
	
	public String toString(String chr) {
		return chr + ":" + this.start + "-" + this.end;
	}
	
	public int length() {
		return this.end - this.start + 1;
	}

	@Override
	public T payload() {
		return payload;
	}

	@Override
	public int start() {
		return start;
	}

	@Override
	public int end() {
		return end;
	}

	@Override
	public boolean overlapsWith(Interval<?> interval) {

		if(interval.start() >= this.start && interval.start() <= this.end) {
			/*
			 * INTERNAL OR PARTIAL INTERSECTION GREATER END POINT
			 * |-----------|
			 *    |--|
			 * 
			 * |-------------|
			 *               |--------|
			 */
			return true;
		} else if(interval.start() <= this.start && interval.end() >= this.start) {
			/*
			 * 
			 * 
			 * A        |----------------|
			 * B |-------|
			 * 
			 * A  |-------|
			 * B |---------------|
			 */
			return true;
		} else {
			/*
			 * 
			 * A |--------|
			 * B           |---------|
			 * 
			 * A                |---------------------------|
			 * B |-------------|
			 */
			return false;
		}
	}


	@Override
	public int overlap(Interval<?> interval) {
		if(this.overlapsWith(interval)) {
			int maxStart = Math.max(this.start, interval.start());
			int minEnd = Math.min(this.end, interval.end());
			return minEnd - maxStart + 1;
		} else {
			return 0;
		}
	}

	@Override
	public Interval<T> merge(Interval<T> interval, T payload) {
		int start = Math.min(this.start(), interval.start());
		int end = Math.max(this.end(), interval.end());
		return new IntervalImpl<T>(start, end, payload);
	}
	
}
