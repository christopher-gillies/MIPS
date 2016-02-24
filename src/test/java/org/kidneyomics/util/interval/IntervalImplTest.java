package org.kidneyomics.util.interval;

import static org.junit.Assert.*;

import org.junit.Test;

public class IntervalImplTest {

	@Test
	public void testOverlaps1() {
		
		IntervalImpl<Integer> int1 = new IntervalImpl<Integer>(0, 100, null);
		IntervalImpl<Integer> int2 = new IntervalImpl<Integer>(0, 100, null);
		
		assertEquals(101,int1.length());
		
		assertTrue(int1.overlapsWith(int2));
		
		assertEquals(101,int1.overlap(int2));
		
	}
	
	
	@Test
	public void testOverlaps2() {
		
		IntervalImpl<Integer> int1 = new IntervalImpl<Integer>(0, 100, null);
		IntervalImpl<Integer> int2 = new IntervalImpl<Integer>(0, 0, null);
		
		assertEquals(101,int1.length());
		
		assertTrue(int1.overlapsWith(int1));
		
		assertEquals(1,int1.overlap(int2));
		
	}

	
	@Test
	public void testOverlaps3() {
		
		IntervalImpl<Integer> int1 = new IntervalImpl<Integer>(0, 100, null);
		IntervalImpl<Integer> int2 = new IntervalImpl<Integer>(100, 100, null);
		
		assertEquals(101,int1.length());
		
		assertTrue(int1.overlapsWith(int1));
		
		assertEquals(1,int1.overlap(int2));
		
	}
	
	
	@Test
	public void testOverlaps4() {
		
		IntervalImpl<Integer> int1 = new IntervalImpl<Integer>(0, 100, null);
		IntervalImpl<Integer> int2 = new IntervalImpl<Integer>(101, 102, null);
		
		assertEquals(101,int1.length());
		
		assertFalse(int1.overlapsWith(int2));
		
		assertEquals(0,int1.overlap(int2));
		
	}
	
	@Test
	public void testOverlaps5() {
		
		IntervalImpl<Integer> int1 = new IntervalImpl<Integer>(0, 100, null);
		IntervalImpl<Integer> int2 = new IntervalImpl<Integer>(50, 74, null);
		
		assertEquals(101,int1.length());
		
		assertTrue(int1.overlapsWith(int2));
		
		assertEquals(25,int1.overlap(int2));
		
	}

	
	@Test
	public void testCompare1() {
		
		IntervalImpl<Integer> int1 = new IntervalImpl<Integer>(0, 100, null);
		IntervalImpl<Integer> int2 = new IntervalImpl<Integer>(101, 102, null);
		
		assertTrue(int1.compareTo(int2) < 0);
		
	}
	
	@Test
	public void testCompare2() {
		
		IntervalImpl<Integer> int1 = new IntervalImpl<Integer>(0, 100, null);
		IntervalImpl<Integer> int2 = new IntervalImpl<Integer>(0, 50, null);
		
		assertTrue(int1.compareTo(int2) > 0);
		
	}
	
	@Test
	public void testCompare3() {
		
		IntervalImpl<Integer> int1 = new IntervalImpl<Integer>(0, 100, null);
		IntervalImpl<Integer> int2 = new IntervalImpl<Integer>(0, 100, null);
		
		assertTrue(int1.compareTo(int2) == 0);
		
	}
	
	@Test
	public void testCompare4() {
		
		IntervalImpl<Integer> int1 = new IntervalImpl<Integer>(1, 100, null);
		IntervalImpl<Integer> int2 = new IntervalImpl<Integer>(0, 200, null);
		
		assertTrue(int1.compareTo(int2) > 0);
		
	}
	
	
	@Test
	public void testEqauls1() {
		
		IntervalImpl<Integer> int1 = new IntervalImpl<Integer>(1, 100, null);
		IntervalImpl<Integer> int2 = new IntervalImpl<Integer>(0, 200, null);
		
		assertFalse(int1.equals(int2));
		
	}
	
	@Test
	public void testEqauls2() {
		
		IntervalImpl<Integer> int1 = new IntervalImpl<Integer>(1, 200, null);
		IntervalImpl<Integer> int2 = new IntervalImpl<Integer>(1, 200, null);
		
		assertTrue(int1.equals(int2));
		assertEquals(int1.hashCode(), int2.hashCode());
	}
}
