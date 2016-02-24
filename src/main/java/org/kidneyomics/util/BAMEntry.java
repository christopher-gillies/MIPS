package org.kidneyomics.util;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class BAMEntry {
	private String id;
	private String location;
	private File file;
	public BAMEntry(String id, String location) {
		this.id = id;
		this.location = location;
		this.file = new File(location);
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getLocation() {
		return location;
	}
	
	public void setLocation(String location) {
		this.location = location;
	}
	
	public File getFile() {
		return file;
	}
	
	
	public static List<BAMEntry> readBamList(File file) throws IOException {
	
		List<String> lines = FileUtils.readLines(file);
		LinkedList<BAMEntry> result = new LinkedList<>();
		for(String line : lines) {
			if(line.isEmpty()) {
				continue;
			}
			String cols[] = line.split("\t");
			
			if(cols.length < 2) {
				throw new IllegalStateException(line + " has less than 2 columns");
			}
			
			BAMEntry entry = new BAMEntry(cols[0],cols[1]);
			result.add(entry);
		}
		
		return result;
		
	}
}
