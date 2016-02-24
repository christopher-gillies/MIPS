package org.kidneyomics.util;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.kidneyomics.util.interval.Interval;
import org.kidneyomics.util.interval.IntervalBucket;

public class BEDUtilTest {

	@Test
	public void test() throws IOException {
		ClassLoader classLoader = getClass().getClassLoader();
		
		File file = new File(classLoader.getResource("locations.bed").getFile());

		List<Interval<BEDEntry>> entries = BEDUtil.readBedFileToIntervals(file);
		
		assertEquals(7,entries.size());
		
		for(Interval<BEDEntry> entry : entries) {
			System.err.println(entry);
		}
	}
	
	
	@Test
	public void testOrganize() throws IOException {
		ClassLoader classLoader = getClass().getClassLoader();
		
		File file = new File(classLoader.getResource("locations.bed").getFile());

		List<Interval<BEDEntry>> entries = BEDUtil.readBedFileToIntervals(file);
		
		assertEquals(7,entries.size());
		
		Map<String,List<Interval<BEDEntry>>> map = BEDUtil.organizeByChr(entries);
		
		assertEquals(3,map.keySet().size());
		
		assertEquals(2,map.get("1").size());
		
		assertEquals(2,map.get("X").size());
		
		assertEquals(3,map.get("3").size());
	}

	
	@Test
	public void testOrganizeBuckets() throws IOException {
		ClassLoader classLoader = getClass().getClassLoader();
		
		File file = new File(classLoader.getResource("locations.bed").getFile());

		List<Interval<BEDEntry>> entries = BEDUtil.readBedFileToIntervals(file);
		
		assertEquals(7,entries.size());
		
		Map<String,List<Interval<BEDEntry>>> map = BEDUtil.organizeByChr(entries);
		
		Map<String,IntervalBucket<BEDEntry>[]> mapBucket = BEDUtil.organizeByChrIntoBuckets(map);
		
		assertEquals(3,mapBucket.keySet().size());
		
		assertEquals(1,mapBucket.get("1").length);
		
		assertEquals(1,mapBucket.get("X").length);
		
		assertEquals(2,mapBucket.get("3").length);
	}
}
