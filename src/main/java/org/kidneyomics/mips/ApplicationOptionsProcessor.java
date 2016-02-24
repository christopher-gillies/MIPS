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
import org.springframework.beans.factory.annotation.Autowired;

public class ApplicationOptionsProcessor implements OptionProcessor {

	@Autowired
	ApplicationOptions applicationOptions;

	@Override
	public void processInputs(String[] args) throws ParseException {
		
		Options options = new Options();

		{
			Option option = Option.builder()
			.argName("command")
			.longOpt("command")
			.desc("the command to perform: summarizeCoverage")
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
			.desc("The region of probes")
			.numberOfArgs(1)
			.hasArg(true)
			.required(false)
			.build();
			options.addOption(infileOp);
		}
		
		CommandLineParser parser = new DefaultParser();
		
		CommandLine cmd = parser.parse( options, args);
		
		if(cmd.hasOption("help")) {
			printHelp(options);
			applicationOptions.setCommand(Command.HELP);
			return;
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
		
		
		if(cmd.hasOption("command")) {
			String command = cmd.getOptionValue("command");
			
			if(command.equals("summarizeCoverage")) {
				applicationOptions.setCommand(Command.SUMMARIZE_PROBES_COVERAGE);
			}
		
		} else {
			applicationOptions.setCommand(Command.HELP);
		}
		
	}
	
	public void printHelp(Options options) {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp( "VCFCommandLineTools", options );
		//System.exit(0);
	}
	
	
	
}
