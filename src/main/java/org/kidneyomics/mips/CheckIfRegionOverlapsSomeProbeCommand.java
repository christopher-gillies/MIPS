package org.kidneyomics.mips;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.kidneyomics.util.BEDEntry;
import org.kidneyomics.util.BEDEntryFactory;
import org.kidneyomics.util.BEDUtil;
import org.kidneyomics.util.RunCommand;
import org.kidneyomics.util.interval.Interval;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CheckIfRegionOverlapsSomeProbeCommand implements RunCommand {

	private List<Interval<BEDEntry>> result = new LinkedList<Interval<BEDEntry>>();
	
	
	ApplicationOptions applicationOptions;
	
	
	Logger logger;
	
	@Autowired
	public CheckIfRegionOverlapsSomeProbeCommand(ApplicationOptions applicationOptions, LoggerService loggerService) {
		logger = loggerService.getLogger(this);
		this.applicationOptions = applicationOptions;
	}
	
	
	/**
	 * 
	 * @return the list of intervals matching the previous run
	 */
	List<Interval<BEDEntry>> getMatchingIntervals() {
		return result;
	}
	

	@Override
	public void runCommand() {
		logger.info("Printing out overlapping regions");
		String bedFile = applicationOptions.getRegionList();
		List<Interval<BEDEntry>> entries = null;
		//clear the list
		result.clear();
		
		//get input region
		Interval<BEDEntry> region = BEDEntryFactory.createByRegion(applicationOptions.getRegion()).toInterval();
		try {
			//remove duplicate entries i.e. have the same start and end positions
			entries = BEDUtil.readBedFileToIntervals(new File(bedFile),true);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		for(Interval<BEDEntry> entry : entries) {
			if(entry.overlapsWith(region) && entry.payload().chr().equals(region.payload().chr())) {
				result.add(entry);
				System.out.println(entry.payload().toString());
			}
		}

	}

}
