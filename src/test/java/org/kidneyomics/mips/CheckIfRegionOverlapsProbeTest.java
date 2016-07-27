package org.kidneyomics.mips;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.kidneyomics.mips.ApplicationOptions;
import org.kidneyomics.mips.CheckIfRegionOverlapsSomeProbeCommand;
import org.kidneyomics.mips.LoggerService;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CheckIfRegionOverlapsProbeTest {

	@Test
	public void test1() throws IOException {
		ClassLoader classLoader = getClass().getClassLoader();
		
		File file = new File(classLoader.getResource("locations.bed").getFile());
		
		ApplicationOptions ao = mock(ApplicationOptions.class);
		when(ao.getRegion()).thenReturn("1:150");
		
		when(ao.getRegionList()).thenReturn(file.getAbsolutePath());
		
		LoggerService loggerService = new LoggerService();
		
		CheckIfRegionOverlapsSomeProbeCommand command = new CheckIfRegionOverlapsSomeProbeCommand(ao,loggerService);
		
		command.runCommand();

		assertEquals(2,command.getMatchingIntervals().size());
	}
	
	@Test
	public void test2() throws IOException {
		ClassLoader classLoader = getClass().getClassLoader();
		
		File file = new File(classLoader.getResource("locations.bed").getFile());
		
		ApplicationOptions ao = mock(ApplicationOptions.class);
		when(ao.getRegion()).thenReturn("1:202-203");
		
		when(ao.getRegionList()).thenReturn(file.getAbsolutePath());
		
		LoggerService loggerService = new LoggerService();
		
		CheckIfRegionOverlapsSomeProbeCommand command = new CheckIfRegionOverlapsSomeProbeCommand(ao,loggerService);
		
		command.runCommand();

		assertEquals(1,command.getMatchingIntervals().size());
	}
	
	@Test
	public void test3() throws IOException {
		ClassLoader classLoader = getClass().getClassLoader();
		
		File file = new File(classLoader.getResource("locations.bed").getFile());
		
		ApplicationOptions ao = mock(ApplicationOptions.class);
		when(ao.getRegion()).thenReturn("1:250");
		
		when(ao.getRegionList()).thenReturn(file.getAbsolutePath());
		
		LoggerService loggerService = new LoggerService();
		
		CheckIfRegionOverlapsSomeProbeCommand command = new CheckIfRegionOverlapsSomeProbeCommand(ao,loggerService);
		
		command.runCommand();

		assertEquals(1,command.getMatchingIntervals().size());
	}
}
