package org.kidneyomics.mips;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.kidneyomics.mips.gtf.GTFEntry;
import org.kidneyomics.mips.gtf.GTFReader;
import org.kidneyomics.util.BEDEntry;
import org.kidneyomics.util.BEDUtil;
import org.kidneyomics.util.RunCommand;
import org.kidneyomics.util.interval.IntersectionComparator;
import org.kidneyomics.util.interval.Interval;
import org.kidneyomics.util.interval.IntervalBucket;
import org.kidneyomics.util.interval.IntervalUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GeneProbeCoverageCommand implements RunCommand {

	
	ApplicationOptions applicationOptions;
	
	
	Logger logger;
	
	@Autowired
	public GeneProbeCoverageCommand(ApplicationOptions applicationOptions, LoggerService loggerService) {
		logger = loggerService.getLogger(this);
		this.applicationOptions = applicationOptions;
	}
	
	private List<GTFEntry> lastResult = null;
	
	List<GTFEntry> getLastResult() {
		return lastResult;
	}
	
	@Override
	public void runCommand() {
		// TODO Auto-generated method stub
		IntersectionComparator comparator = new IntersectionComparator();
		
		logger.info("Initializing data structures for finding which genes are covered by probes");
		String gtf = applicationOptions.getGtf();
		String genesFile = applicationOptions.getGeneFile();
		String bedFile = applicationOptions.getRegionList();
		String outfile = applicationOptions.getOutfile();
		
		if(StringUtils.isEmpty(gtf)) {
			throw new RuntimeException("Please specify a gtf file");
		}
		
		if(StringUtils.isEmpty(genesFile)) {
			throw new RuntimeException("Please specify a gene file");
		}
		
		if(StringUtils.isEmpty(bedFile)) {
			throw new RuntimeException("Please specify a bed file");
		}
		
		if(StringUtils.isEmpty(outfile)) {
			throw new RuntimeException("Please specify a outfile");
		}
		
		File gtfFile = new File(gtf);
		
		
		Map<String, IntervalBucket<BEDEntry>[]> probeMap = null;
		List<Interval<BEDEntry>> entries = null;
		List<GTFEntry> coveredEntries = new LinkedList<>();
		lastResult = coveredEntries;
		
		try(GTFReader reader = GTFReader.createGENCODEGTFReader(gtfFile)) {
			
			BufferedWriter outWriter = Files.newBufferedWriter(Paths.get(outfile), Charset.defaultCharset(), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
			
			outWriter.write(GTFEntry.toStringHeader());
			outWriter.write("\n");
			List<String> genes = FileUtils.readLines(new File(genesFile));
			
			//this set is used for creating intervals to keep
			//and keep track of how many region in the gtf match this gene
			Map<String,Integer> geneMap = new HashMap<>();
			//geneSet.addAll(genes);
			for(String gene : genes) {
				geneMap.put(gene, 0);
			}
			entries = BEDUtil.readBedFileToIntervals(new File(bedFile),true);
			Map<String,List<Interval<BEDEntry>>> entriesPerChr = BEDUtil.organizeByChr(entries,true);
			probeMap = BEDUtil.organizeByChrIntoBuckets(entriesPerChr);
			
			//this step is used to merge the overlapping intervals
			//entries.clear();
			//for(Map.Entry<String, List<Interval<BEDEntry>>> entry : entriesPerChr.entrySet()) {
			//	entries.addAll(entry.getValue());
			//}
			
			//loop though gtf decide to keep or not based on gene name
			int entriesRead = 0;
			for(GTFEntry entry : reader) {
				entriesRead += 1;
				
				if(entriesRead % 1000 == 0) {
					logger.info("Read: " + entriesRead + " GTF entries");
					logger.info("Last read: ");
					logger.info(entry.toString());
				}
				
				String gene = entry.getGene_name();
				
				if(entry.getType() != GTFEntry.TYPE.CDS) {
					continue;
				}
				
				if(geneMap.containsKey(gene)) {
					//mark this gene as being found
					geneMap.put(gene, 1);
					
					String chr = entry.getChr();
					
					List<Interval<BEDEntry>> matches = IntervalUtil.findBestMatchesByOverlap(probeMap.get(chr), entry.toInterval(), comparator);
					
					if(matches.size() == 0) {
						outWriter.write(entry.toString());
						outWriter.write("\n");
						//System.err.println("Non-match:\n" + entry.toString());
					} else {
						coveredEntries.add(entry);
						//System.err.println("Match:\n" + entry.toString());
					}
					
				}
				
								
				
				
			}
			
			
					
			int genesCovered = 0;
			for(Map.Entry<String,Integer> geneEntry : geneMap.entrySet()) {
				if(geneEntry.getValue() != 0) {
					genesCovered++;
				} else {
					logger.info(geneEntry.getKey() + " not found in GTF");
				}
			}
			
			logger.info("Covered " + genesCovered + " genes of " + geneMap.keySet().size());
			
			outWriter.close();
			
		} catch(Exception e) {
			e.printStackTrace();
		}

	}
	
	

}
