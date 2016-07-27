package org.kidneyomics.mips.gtf;

import java.awt.image.BufferedImageFilter;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.zip.GZIPInputStream;

import org.apache.commons.lang3.StringUtils;

public class GTFReader implements Iterable<GTFEntry>, Iterator<GTFEntry>, Closeable {

	private final GTFEntry.FORMAT format;
	private BufferedReader fileReader = null;
	private String nextLine = null;
	
	
	private GTFReader(GTFEntry.FORMAT format) {
		this.format = format;
	}
	
	public static GTFReader createGENCODEGTFReader(File file) throws IOException {
		
		GTFReader reader = new GTFReader(GTFEntry.FORMAT.GENCODE);
		if(file.getAbsolutePath().endsWith("gz")) {
			InputStream fileStream = new FileInputStream(file);
			InputStream gzipStream = new GZIPInputStream(fileStream);
			Reader decoder = new InputStreamReader(gzipStream, "UTF-8");
			BufferedReader buffered = new BufferedReader(decoder);
			reader.fileReader = buffered;
		} else {
			reader.fileReader = Files.newBufferedReader(file.toPath(), Charset.defaultCharset());
		}
		
		/*
		 * Skip the header lines
		 */
		String line = null;
		do {
			line = reader.fileReader.readLine();
		} while(!StringUtils.isEmpty(line) && line.startsWith("#"));
		
		//set next line
		reader.nextLine = line;
		
		return reader;
	}
	
	
	@Override
	public boolean hasNext() {
		return nextLine != null;
	}

	@Override
	public GTFEntry next() {
		if(!this.hasNext()) {
			return null;
		}
		
		String currentLine = nextLine;
		try {
			nextLine = fileReader.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//TODO: add logic for new formats
		
		return GTFEntry.createGENCODEEntry(currentLine);
	}

	@Override
	public void remove() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Iterator<GTFEntry> iterator() {
		return this;
	}

	@Override
	public void close() throws IOException {
		this.fileReader.close();
	}
	
}
