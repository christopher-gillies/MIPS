package org.kidneyomics.mips;

import org.kidneyomics.util.BAMEntry;
import org.kidneyomics.util.BAMProcessor;
import org.kidneyomics.util.BAMProcessorImplDict;
import org.kidneyomics.util.BEDEntry;
import org.kidneyomics.util.BEDUtil;
import org.kidneyomics.util.RunCommand;
import org.kidneyomics.util.SAMRecordPair;
import org.kidneyomics.util.SAMRecordToIntervalConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.management.openmbean.OpenMBeanOperationInfoSupport;

import org.kidneyomics.util.interval.IntersectionComparator;
import org.kidneyomics.util.interval.Interval;
import org.kidneyomics.util.interval.IntervalBucket;
import org.kidneyomics.util.interval.IntervalUtil;
import org.slf4j.Logger;


@Component
public class CoverageCalculator implements RunCommand {


	ApplicationOptions applicationOptions;
	
	
	Logger logger;
	
	@Autowired
	public CoverageCalculator(ApplicationOptions applicationOptions, LoggerService loggerService) {
		logger = loggerService.getLogger(this);
		this.applicationOptions = applicationOptions;
	}
	
	@Override
	public void runCommand() {
		logger.info("Initializing data structures");
		String bamList = applicationOptions.getBAMList();
		String bedFile = applicationOptions.getRegionList();
		String outfile = applicationOptions.getOutfile();
		/*
		 * Construct bam list
		 */
		
		List<BAMEntry> bamEntries = null;
		try {
			bamEntries = BAMEntry.readBamList(new File(bamList));
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		logger.info("Analyzing bed file");
		/*
		 * Construct probe map
		 */
		Map<String, IntervalBucket<BEDEntry>[]> probeMap = null;
		List<Interval<BEDEntry>> entries = null;
		try {
			entries = BEDUtil.readBedFileToIntervals(new File(bedFile));
			probeMap = BEDUtil.organizeByChrIntoBuckets(BEDUtil.organizeByChr(entries));
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		
		/*
		 * Construct Results map 
		 * probe --> sample --> count
		 */
		Map<BEDEntry,Map<String,Double>> coveragePerProbe = new HashMap<>();
		for(Interval<BEDEntry> entry : entries) {
			Map<String,Double> coverageForSample = new HashMap<>();
			//add entry for each sample
			for(BAMEntry bamEntry : bamEntries) {
				coverageForSample.put(bamEntry.getId(), 0.0);
			}
			//add probe map
			
			coveragePerProbe.put(entry.payload(), coverageForSample);
		}
		
		Map<String,Integer> unmappedReadCountPerSample = new HashMap<>();
		Map<String,Integer> readsPerSample = new HashMap<>();
		
		try {
			//for each bam
			for(BAMEntry bam : bamEntries) {
				//Process BAM
				logger.info("Processing bam for " + bam.getId() + ": " + bam.getLocation());
				File fin = bam.getFile();
				int pairsCounted = 0;
				int unmapped = 0;
				
				//read each read pair from bam
				try(BAMProcessor processor = BAMProcessorImplDict.getBAMProcessor(fin)) {
					
					SAMRecordPair pair = null;
					while( ( pair = processor.getNextReadPair()) != null) {
						
						//add fractional count per best matched probe
						Set<BEDEntry> matchedProbes = matchProbes(pair,probeMap);
						double num = matchedProbes.size();
						if(num == 0.0) {
							unmapped++;
						}
						for(BEDEntry probe : matchedProbes) {
							Map<String,Double> sampleCoverage = coveragePerProbe.get(probe);
							double currentCount = sampleCoverage.get(bam.getId());
							sampleCoverage.put(bam.getId(), currentCount + 1.0 / num);
							
						}
						
						pairsCounted++;
						if(pairsCounted % 100000 == 0) {
							logger.info(pairsCounted + " reads counted");
						}
						
					}
				}
				readsPerSample.put(bam.getId(), pairsCounted);
				unmappedReadCountPerSample.put(bam.getId(), unmapped);
			}
			
			logger.info("Writing out results to " + outfile);
			//write results
			writeResults(outfile, coveragePerProbe, unmappedReadCountPerSample, readsPerSample);
		} catch(Exception e) {
			e.printStackTrace();
		}
		

	}
	
	private void writeResults(String outfile,Map<BEDEntry,Map<String,Double>> coveragePerProbe, Map<String,Integer> unmappedReadCountPerSample, Map<String,Integer> readsPerSample) throws IOException {
		
		try(BufferedWriter writer = Files.newBufferedWriter(Paths.get(outfile), Charset.defaultCharset(), StandardOpenOption.CREATE)) {
		
			//write header
			List<String> sampleOrder = new LinkedList<String>();
			sampleOrder.addAll(unmappedReadCountPerSample.keySet());
			
			writer.append("probe\t");
			Iterator<String> iter = sampleOrder.iterator();
			while(iter.hasNext()) {
				writer.append(iter.next());
				if(iter.hasNext()) {
					writer.append("\t");
				}
			}
			writer.append("\n");
			
			List<BEDEntry> probes = new LinkedList<>();
			probes.addAll(coveragePerProbe.keySet());
			Collections.sort(probes);
			
			for(BEDEntry probe : probes) {
				Map<String,Double> coverageForProbe = coveragePerProbe.get(probe);
				writer.append(probe.toString());
				writer.append("\t");
				iter = sampleOrder.iterator();
				while(iter.hasNext()) {
					String id = iter.next();
					writer.append(coverageForProbe.get(id).toString());
					if(iter.hasNext()) {
						writer.append("\t");
					}
				}
				writer.append("\n");
			}
			
			
			//add row for total
			writer.append("total");
			writer.append("\t");
			
			iter = sampleOrder.iterator();
			while(iter.hasNext()) {
				String id = iter.next();
				writer.append(readsPerSample.get(id).toString());
				if(iter.hasNext()) {
					writer.append("\t");
				}
			}
			writer.append("\n");
			
			//add row for unmapped
			writer.append("unmapped");
			writer.append("\t");
			
			iter = sampleOrder.iterator();
			while(iter.hasNext()) {
				String id = iter.next();
				writer.append(unmappedReadCountPerSample.get(id).toString());
				if(iter.hasNext()) {
					writer.append("\t");
				}
			}
			writer.append("\n");
		}
	}
	
	private SAMRecordToIntervalConverter converter = new SAMRecordToIntervalConverter();
	private IntersectionComparator<BEDEntry> comparator = new IntersectionComparator<>();
	
	/**
	 * 
	 * @param pair -- pair of reads
	 * @param probeMap
	 * @return the set of probes that best match the read pair, hopefully just one probe
	 */
	private Set<BEDEntry> matchProbes(SAMRecordPair pair, Map<String, IntervalBucket<BEDEntry>[]> probeMap) {
		
		
		IntervalBucket<BEDEntry>[] bucketsForChr = probeMap.get(pair.getMate1().getReferenceName());
		
		if(bucketsForChr == null) {
			throw new IllegalStateException(pair.getMate1().getReferenceName() + " not found in probeMap " + probeMap.keySet());
		}
		
		HashSet<BEDEntry> entriesForRead = new HashSet<>();
		
		List<Interval<BEDEntry>> mate1 = converter.convert(pair.getMate1());
		List<Interval<BEDEntry>> mate2 = converter.convert(pair.getMate2());
		
		//mate 1
		for(Interval<BEDEntry> entry : mate1) {
			List<Interval<BEDEntry>> bestMatches = IntervalUtil.findBestMatches(bucketsForChr, entry, comparator);
			for(Interval<BEDEntry> ie : bestMatches) {
				entriesForRead.add(ie.payload());
			}
		}
		
		//mate 2
		for(Interval<BEDEntry> entry : mate2) {
			List<Interval<BEDEntry>> bestMatches = IntervalUtil.findBestMatches(bucketsForChr, entry, comparator);
			for(Interval<BEDEntry> ie : bestMatches) {
				entriesForRead.add(ie.payload());
			}
		}
		
		return entriesForRead;
		
	}
	
	
}
