package org.kidneyomics.util;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.kidneyomics.util.interval.Interval;

public class BEDUtil {
	
	/**
	 * 
	 * @param file
	 * @return list of intervals
	 * @throws IOException
	 */
	public static List<Interval<BEDEntry>> readBedFileToIntervals(File file) throws IOException {
		List<BEDEntry> entries = readBedFile(file);
		List<Interval<BEDEntry>>  result = new LinkedList<>();
		for(BEDEntry entry : entries) {
			result.add(entry.toInterval());
		}
		return result;
		
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
	public static Map<String,List<Interval<BEDEntry>>> organizeByChr(List<Interval<BEDEntry>> list) {
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
		
		return map;
	}
}
