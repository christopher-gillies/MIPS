package org.kidneyomics.mips;

import java.io.BufferedWriter;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.kidneyomics.util.BEDEntry;
import org.kidneyomics.util.BEDUtil;
import org.kidneyomics.util.ChromosomeStringComparator;
import org.kidneyomics.util.RunCommand;
import org.kidneyomics.util.interval.Interval;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WriteOutNonoverlappingRegionsCommand implements RunCommand {

	
	ApplicationOptions applicationOptions;
	
	
	Logger logger;
	
	@Autowired
	public WriteOutNonoverlappingRegionsCommand(ApplicationOptions applicationOptions, LoggerService loggerService) {
		logger = loggerService.getLogger(this);
		this.applicationOptions = applicationOptions;
	}
	
	@Override
	public void runCommand() {
		String bedFile = applicationOptions.getRegionList();
		String outfile = applicationOptions.getOutfile();
		boolean intervalOut = applicationOptions.getIntervalOutput();
		List<Interval<BEDEntry>> entries = null;
		Map<String,List<Interval<BEDEntry>>> entriesPerChr = null;
		try {
			//remove duplicate entries i.e. have the same start and end positions
			entries = BEDUtil.readBedFileToIntervals(new File(bedFile),true);
			entriesPerChr = BEDUtil.organizeByChr(entries,true);
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		//List<BEDEntry> bedEntries = new LinkedList<>();
		try(BufferedWriter writer = Files.newBufferedWriter(Paths.get(outfile), Charset.defaultCharset(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
			List<String> keys = new LinkedList<String>();
			keys.addAll(entriesPerChr.keySet());
			Collections.sort(keys,new ChromosomeStringComparator());
			for(String key : keys) {
				List<Interval<BEDEntry>> values = entriesPerChr.get(key);
				for(Interval<BEDEntry> value : values) {
					//bedEntries.add(value.payload());
					BEDEntry entry = value.payload();
					if(intervalOut) {
						writer.write(entry.toString());
					} else {
						writer.write(entry.toBEDString());
					}
					writer.write("\n");
				}
			}
			
			
			
		} catch(Exception e) {
			
		}
		
		
	}

}
