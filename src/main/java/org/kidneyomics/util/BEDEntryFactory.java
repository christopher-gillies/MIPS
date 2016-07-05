package org.kidneyomics.util;

public class BEDEntryFactory {
	
	public static BEDEntry createByRegion(String region) {
		
		ChromosomePositionInterval interval = new ChromosomePositionInterval(region);
		if(interval.isInterval()) { 
			return new BEDEntry(interval.getChromosome(), interval.getStartPostion(), interval.getEndPostion());
		} else {
			return new BEDEntry(interval.getChromosome(), interval.getStartPostion(), interval.getStartPostion());
		}
	}
	
	
	public static BEDEntry create(String chr, int start, int end) {
		return new BEDEntry(chr, start, end);
	}
	
	
}
