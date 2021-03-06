package org.kidneyomics.mips;

import org.springframework.stereotype.Component;

@Component
public class ApplicationOptions {
	private String BAMList;
	private String regionList;
	private String outfile;
	private String geneFile;
	
	/**
	 * The purpose of this option is to tell the coverage calculator command to merge
	 */
	private boolean mergeOverlappingRegions = false;
	private boolean intervalOutput = false;
	private String region = "";
	private String gtf = "";
	
	private Command command;
	
	public enum Command {
		HELP,
		SUMMARIZE_PROBES_COVERAGE,
		WRITE_OUT_NONOVERLAPPING_REGIONS,
		CHECK_REGION_OVERLAP_PROBES,
		GENE_PROBE_COVERAGE
	}
	
	

	
	public String getGeneFile() {
		return geneFile;
	}

	public void setGeneFile(String geneFile) {
		this.geneFile = geneFile;
	}

	public String getGtf() {
		return gtf;
	}

	public void setGtf(String gtf) {
		this.gtf = gtf;
	}

	String getBAMList() {
		return BAMList;
	}

	void setBAMList(String bAMList) {
		BAMList = bAMList;
	}

	Command getCommand() {
		return command;
	}

	void setCommand(Command command) {
		this.command = command;
	}

	String getRegionList() {
		return regionList;
	}

	void setRegionList(String regionList) {
		this.regionList = regionList;
	}

	String getOutfile() {
		return outfile;
	}

	void setOutfile(String outfile) {
		this.outfile = outfile;
	}

	boolean getMergeOverlappingRegions() {
		return mergeOverlappingRegions;
	}

	void setMergeOverlappingRegions(boolean mergeOverlappingRegions) {
		this.mergeOverlappingRegions = mergeOverlappingRegions;
	}

	boolean getIntervalOutput() {
		return intervalOutput;
	}

	void setIntervalOutput(boolean intervalOutput) {
		this.intervalOutput = intervalOutput;
	}

	String getRegion() {
		return region;
	}

	void setRegion(String region) {
		this.region = region;
	}
	
	
	
	
	
}
