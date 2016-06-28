package org.kidneyomics.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.kidneyomics.util.interval.Interval;
import org.kidneyomics.util.interval.IntervalBucket;

import org.kidneyomics.util.interval.IntervalUtil;

public class BEDUtil {
	
	/**
	 * 
	 * @param file
	 * @return list of intervals
	 * @throws IOException
	 */
	public static List<Interval<BEDEntry>> readBedFileToIntervals(File file) throws IOException {
		return readBedFileToIntervals(file,false);
		
	}
	
	public static List<Interval<BEDEntry>> readBedFileToIntervals(File file, boolean removeDuplicates) throws IOException {
		List<BEDEntry> entries = readBedFile(file);
		List<Interval<BEDEntry>>  result = new LinkedList<>();
		HashSet<BEDEntry> set = new HashSet<>();
		for(BEDEntry entry : entries) {
			Interval<BEDEntry> interval = entry.toInterval();
			
			/*
				set.contains(entry)	removeDuplicates	!set.contains(entry) 	!removeDuplicates	OR
					0			0			1			1		1
					0			1			1			0		1
					1			0			0			1		1
					1			1			0			0		0
			
			enter this block unless we are removing duplicates and the set contains the entry
			
			*/
			
			//only store if we are not removing duplicates of if we are removing duplicates, then only if set does not contain the interval
			if(!set.contains(entry) || !removeDuplicates) {
				set.add(entry);
				result.add(interval);
			}
		}
		return result;
		
	}
	
	
	/**
	 * 
	 * @param file -- bed file of regions
	 * @return a bucket of non overlapping intervals per chromosome
	 * @throws IOException
	 */
	public static Map<String,IntervalBucket<BEDEntry>[]> readBedFileToChrMapOfIntervals(File file, boolean mergeOverlappingRegions) throws IOException {
		 List<Interval<BEDEntry>> entries = readBedFileToIntervals(file);
		 Map<String,List<Interval<BEDEntry>>> intervalMap = organizeByChr(entries, mergeOverlappingRegions);
		 Map<String,IntervalBucket<BEDEntry>[]> bucketMap = organizeByChrIntoBuckets(intervalMap);
		 
		 return bucketMap;
	}
	
	/**
	 * 
	 * @param file --- bed file
	 * @return list of bed entries
	 * @throws IOException
	 */
	public static List<BEDEntry> readBedFile(File file) throws IOException {
		List<String> lines = FileUtils.readLines(file);
		LinkedList<BEDEntry> result = new LinkedList<>();
		for(String line : lines) {
			if(line.isEmpty()) {
				continue;
			}
			String cols[] = line.split("\t");
			
			if(cols.length < 3) {
				throw new IllegalStateException(line + " has less than 3 columns");
			}
			
			BEDEntry entry = new BEDEntry(cols[0], Integer.parseInt(cols[1]), Integer.parseInt(cols[2]));
			result.add(entry);
		}
		
		return result;
	}
	
	/**
	 * 
	 * @param list -- intervals
	 * @return intervals organized by chr in a map
	 */
	public static Map<String,List<Interval<BEDEntry>>> organizeByChr(List<Interval<BEDEntry>> list, boolean mergeOverlappingIntervals) {
		Map<String,List<Interval<BEDEntry>>> map = new HashMap<>();
		
		for(Interval<BEDEntry> entry : list) {
			String chr = entry.payload().chr();
			
			if(map.containsKey(chr)) {
				map.get(chr).add(entry);
			} else {
				List<Interval<BEDEntry>> listForChr = new LinkedList<>();
				listForChr.add(entry);
				map.put(chr, listForChr);
			}
		}
		
		if(mergeOverlappingIntervals) {
			for(Map.Entry<String, List<Interval<BEDEntry>>> entry : map.entrySet()) {
				//String chr = entry.getKey();
				List<Interval<BEDEntry>> value = entry.getValue();
				entry.setValue(createNonOverlapping(value));
			}
		}
		return map;
	}
	
	/**
	 * 
	 * @param inMap -- map of each chromosome of intervals
	 * @return a map for each chromosome of non overlapping intervals
	 */
	public static Map<String,IntervalBucket<BEDEntry>[]> organizeByChrIntoBuckets(Map<String,List<Interval<BEDEntry>>> inMap) {
		Map<String,IntervalBucket<BEDEntry>[]> map = new HashMap<>();
		
		for(Map.Entry<String, List<Interval<BEDEntry>>> entry : inMap.entrySet()) {
			IntervalBucket<BEDEntry> buckets[] = IntervalUtil.mergeOverlappingIntervals(entry.getValue());
			map.put(entry.getKey(), buckets);
		}
		
		return map;
	}
	
	public static List<Interval<BEDEntry>> createNonOverlapping(List<Interval<BEDEntry>> intervals) {
		
		/*
		 * Sort by start position
		 */
		Collections.sort(intervals);
		
		List<Interval<BEDEntry>> nonOverlappingIntervals = new ArrayList<Interval<BEDEntry>>(intervals.size());
		Iterator<Interval<BEDEntry>> iterA = intervals.iterator();
		
		Interval<BEDEntry> current = iterA.next();
		while(iterA.hasNext()) {
			Interval<BEDEntry> next = iterA.next();
			if(current.overlapsWith(next)) {
				/*
				 * if they overlap then set the current interval to be the merging of the two intervals under investigation
				 */
				current = BEDEntry.merge(current, next);
				if(current == null) {
					throw new IllegalStateException("");
				}
			} else {
				/*
				 * else they do not overlap so save the current interval and then set the current interval to be the next interval
				 * the next loop iteration will update next
				 */
				nonOverlappingIntervals.add(current);
				current = next;
			}
		}
		/*
		 * add last interval
		 */
		nonOverlappingIntervals.add(current);
		
		return nonOverlappingIntervals;
	}
	
}
