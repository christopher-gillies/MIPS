package org.kidneyomics.mips;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.kidneyomics.mips.ApplicationOptions.Command;
import org.kidneyomics.util.OptionProcessor;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

@Component
public class ApplicationOptionsProcessor implements OptionProcessor {


	
	ApplicationOptions applicationOptions;
	
	
	Logger logger;
	
	
	@Autowired
	ApplicationOptionsProcessor(ApplicationArguments args, LoggerService loggerService, ApplicationOptions applicationOptions) throws ParseException {
		this.logger = loggerService.getLogger(this);
		this.applicationOptions = applicationOptions;
		try {
			processInputs(args.getSourceArgs());
		} catch(Exception e) {
			logger.info(e.getMessage());
			System.exit(0);
		}
		
	}
	@Override
	public void processInputs(String[] args) throws ParseException {
		
		Options options = new Options();

		{
			Option option = Option.builder()
			.argName("command")
			.longOpt("command")
			.desc("the command to perform: summarizeCoverage, mergeOverlappingRegions")
			.numberOfArgs(1)
			.hasArg(true)
			.valueSeparator(' ')
			.required(false)
			.build();
			options.addOption(option);
		}
		
		{
			Option option = Option.builder()
			.argName("checkRegion")
			.longOpt("checkRegion")
			.desc("the region that you want to check")
			.numberOfArgs(1)
			.hasArg(true)
			.valueSeparator(' ')
			.required(false)
			.build();
			options.addOption(option);
		}
		
		{
			Option option = Option.builder()
			.argName("bamList")
			.longOpt("bamList")
			.desc("a bam list")
			.numberOfArgs(1)
			.hasArg(true)
			.valueSeparator(' ')
			.required(false)
			.build();
			options.addOption(option);
		}
		
		{
			Option outfileOp = Option.builder()
			.argName("outfile")
			.longOpt("outfile")
			.desc("The file to write out to")
			.numberOfArgs(1)
			.hasArg(true)
			.required(false)
			.build();
			options.addOption(outfileOp);
		}
			
		{
			Option infileOp = Option.builder()
			.argName("regionList")
			.longOpt("regionList")
			.desc("A bed file containing the regions of the probes.")
			.numberOfArgs(1)
			.hasArg(true)
			.required(false)
			.build();
			options.addOption(infileOp);
		}
		
		{
			Option option = Option.builder()
			.argName("mergeOverlappingRegions")
			.longOpt("mergeOverlappingRegions")
			.desc("this flag will tell the program to merge bed region entries that overlap with each other. this is used with the summarizeCoverage command")
			.numberOfArgs(0)
			.hasArg(false)
			.required(false)
			.build();
			options.addOption(option);
		}
		
		{
			Option option = Option.builder()
			.argName("intervalOutput")
			.longOpt("intervalOutput")
			.desc("this flag will tell the program to output an interval list chr:start-end 1based instead of 0based BED")
			.numberOfArgs(0)
			.hasArg(false)
			.required(false)
			.build();
			options.addOption(option);
		}
		
		CommandLineParser parser = new DefaultParser();
		
		CommandLine cmd = parser.parse( options, args);
		
		if(cmd.hasOption("help")) {
			printHelp(options);
			applicationOptions.setCommand(Command.HELP);
			return;
		}
		
		if(cmd.hasOption("intervalOutput")) {
			applicationOptions.setIntervalOutput(true);
		} else {
			applicationOptions.setIntervalOutput(false);
		}
		
		if(cmd.hasOption("mergeOverlappingRegions")) {
			applicationOptions.setMergeOverlappingRegions(true);
		} else {
			applicationOptions.setMergeOverlappingRegions(false);
		}
		
		if(cmd.hasOption("regionList")) {
			applicationOptions.setRegionList(cmd.getOptionValue("regionList"));
		}
		
		if(cmd.hasOption("bamList")) {
			applicationOptions.setBAMList(cmd.getOptionValue("bamList"));
		}
		
		if(cmd.hasOption("outfile")) {
			applicationOptions.setOutfile(cmd.getOptionValue("outfile"));
		}
		
		if(cmd.hasOption("checkRegion")) {
			applicationOptions.setRegion(cmd.getOptionValue("checkRegion"));
		}
		
		
		if(cmd.hasOption("command")) {
			String command = cmd.getOptionValue("command");
			
			if(command.equals("summarizeCoverage")) {
				applicationOptions.setCommand(Command.SUMMARIZE_PROBES_COVERAGE);
			} else if(command.equals("mergeOverlappingRegions")) {
				applicationOptions.setCommand(Command.WRITE_OUT_NONOVERLAPPING_REGIONS);
			} else if(command.equals("checkIfRegionOverlapsProbes")) {
				applicationOptions.setCommand(Command.CHECK_REGION_OVERLAP_PROBES);
			}
		
		} else {
			applicationOptions.setCommand(Command.HELP);
		}
		
		if(applicationOptions.getCommand() == Command.HELP) {
			printHelp(options);
		}
	}
	
	public void printHelp(Options options) {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp( "VCFCommandLineTools", options );
		//System.exit(0);
	}
	
	
	
}
