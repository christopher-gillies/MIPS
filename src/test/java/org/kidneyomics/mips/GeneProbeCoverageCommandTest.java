package org.kidneyomics.mips;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

public class GeneProbeCoverageCommandTest {

	@Test
	public void test() throws IOException {
		
		ApplicationOptions options = new ApplicationOptions();
		LoggerService service = new LoggerService();
		
		
		/*
		String gtf = applicationOptions.getGtf();
		String genesFile = applicationOptions.getGeneFile();
		String bedFile = applicationOptions.getRegionList();
		String outfile = applicationOptions.getOutfile();
		
		 */
		ClassLoader classLoader = getClass().getClassLoader();
		
		//set GTF options
		File gtf = new File(classLoader.getResource("gencode.v19.annotation.10000.gtf.gz").getFile());
		options.setGtf(gtf.getAbsolutePath());
		
		String tempDir = FileUtils.getTempDirectoryPath();
		
		//set genesFile
		File genesFile = new File(tempDir + "/genes.txt");
		
		FileUtils.writeStringToFile(genesFile, "ARHGEF16\n");
		options.setGeneFile(genesFile.getAbsolutePath());
		
		//set bedfile
		File bedFile = new File(tempDir + "/bed.txt");
		
		FileUtils.writeStringToFile(bedFile, "1	3385999	3386157\n");
		options.setRegionList(bedFile.getAbsolutePath());

		File outFile = new File(tempDir + "/out.txt");
		options.setOutfile(outFile.getAbsolutePath());
		
		GeneProbeCoverageCommand command = new GeneProbeCoverageCommand(options, service);
		
		command.runCommand();
		
		
		assertTrue(outFile.exists());
		
		List<String> entries = FileUtils.readLines(outFile);
		assertTrue(entries.size() > 1);
		
		//System.err.println("Missing entries: " + (entries.size() - 1));
		int matchCheck = 0;
		int count = 0;
		for(String entry : entries) {
			assertFalse(entry.contains("3386000")); 
			 count++;
			 
			if(entry.contains("3389642")) {
				//System.err.println(entry.toString());
				matchCheck++;
			}
		}
		
		assertEquals(2,matchCheck);
		assertEquals(21, count - 1 + command.getLastResult().size()); //subtract one for header line
		
		outFile.delete();
		bedFile.delete();
		genesFile.delete();
		
		
		
		
		
	}

}
