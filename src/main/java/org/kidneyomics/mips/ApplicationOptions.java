package org.kidneyomics.mips;

import org.springframework.stereotype.Component;

@Component
public class ApplicationOptions {
	private String BAMList;
	private String regionList;
	private String outfile;
	
	private Command command;
	
	public enum Command {
		HELP,
		SUMMARIZE_PROBES_COVERAGE
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
	
	
	
}
