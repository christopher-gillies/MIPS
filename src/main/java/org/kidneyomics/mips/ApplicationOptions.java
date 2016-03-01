package org.kidneyomics.mips;

import org.springframework.stereotype.Component;

@Component
public class ApplicationOptions {
	private String BAMList;
	private String regionList;
	private String outfile;
	private boolean mergeOverlappingRegions = false;
	private boolean intervalOutput = false;
	
	private Command command;
	
	public enum Command {
		HELP,
		SUMMARIZE_PROBES_COVERAGE,
		WRITE_OUT_NONOVERLAPPING_REGIONS
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
	
	
	
	
	
}
