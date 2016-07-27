package org.kidneyomics.mips.gtf;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class GTFReaderTest {

	@Test
	public void test() throws IOException {
		ClassLoader classLoader = getClass().getClassLoader();
		
		File file = new File(classLoader.getResource("gencode.v19.annotation.10000.gtf.gz").getFile());
		
		int count = 0;
		try(GTFReader reader = GTFReader.createGENCODEGTFReader(file)) {
			for(GTFEntry entry : reader) {
				count+=1;
				if(count == 1) {
					System.err.println(entry);
				}
			}
		}
		System.err.println("Lines read: " + count);
		
		assertEquals(9995,count);
		
		
	}

}
