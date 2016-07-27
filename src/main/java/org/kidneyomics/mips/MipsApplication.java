package org.kidneyomics.mips;

import org.kidneyomics.util.RunCommand;
import org.slf4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class MipsApplication {

	public static void main(String[] args) {
		
		SpringApplication springApplication = new SpringApplication(new Object[] { MipsApplication.class });
    	springApplication.setLogStartupInfo(false);
    	ApplicationContext context = springApplication.run(args);
    	
    	ApplicationOptions options = context.getBean(ApplicationOptions.class);
    	
    	
    	LoggerService loggerService = context.getBean(LoggerService.class);
    	Logger logger = loggerService.getLogger(MipsApplication.class);
    	
    	RunCommand command = null;
    	switch(options.getCommand()) {
		case HELP:
			break;
		case SUMMARIZE_PROBES_COVERAGE:
			logger.info("Summarizing coverage");
			command = context.getBean(CoverageCalculatorCommand.class);
			break;
		case WRITE_OUT_NONOVERLAPPING_REGIONS:
			logger.info("Writing out non-overlapping regions");
			command = context.getBean(WriteOutNonoverlappingRegionsCommand.class);
			break;
		case CHECK_REGION_OVERLAP_PROBES:
			logger.info("Checking if the regions sepecified overlaps some probe");
			command = context.getBean(CheckIfRegionOverlapsSomeProbeCommand.class);
			break;
		case GENE_PROBE_COVERAGE:
			logger.info("Checking that genes are covered by probes");
			command = context.getBean(GeneProbeCoverageCommand.class);
			break;
		default:
			break;
    	
    	}
    	
    	if(command != null) {
    		command.runCommand();
    	}
	}
}
