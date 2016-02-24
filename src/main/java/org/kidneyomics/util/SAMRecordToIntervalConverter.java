package org.kidneyomics.util;

import org.kidneyomics.util.interval.Interval;
import org.kidneyomics.util.interval.IntervalImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;

import htsjdk.samtools.Cigar;
import htsjdk.samtools.CigarElement;
import htsjdk.samtools.CigarOperator;
import htsjdk.samtools.SAMRecord;

import java.util.LinkedList;
import java.util.List;

public class SAMRecordToIntervalConverter implements Converter<SAMRecord, List<Interval<BEDEntry>>> {

	Logger logger = LoggerFactory.getLogger(SAMRecordToIntervalConverter.class);
	
	@Override
	public List<Interval<BEDEntry>> convert(SAMRecord in) {
		
		
		/*
		 * example
		 * 
		 * 10M5N7M
		 * 
		 * start = 100
		 * 
		 * MMMMMMMMMMNNNNNMMMMMMM
		 * 
		 * F1 = start = 100, end = 109,
		 * F2 = start = 115, end = 121
		 * last element is inclusive
		 * 
		 * start = 100
		 * end = 100 + 10 - 1 = 109
		 * 
		 * newStart = 109 + 1 + 5 = 115
		 */
		
		List<Interval<BEDEntry>> list = new LinkedList<>();
		
		Cigar cigar = in.getCigar();
		
		List<CigarElement> elements = cigar.getCigarElements();
		
		boolean isNegativeStrand = in.getReadNegativeStrandFlag();
		
		
		int currentStart = in.getAlignmentStart();
		int currentLength = 0;
		CigarElement previous = null;
		for(final CigarElement element : elements) {
			//logger.info(element.getLength() + "");
            switch (element.getOperator()) {
            case M:
            case EQ:
            case X:
            	currentLength += element.getLength();
                break;
            case D:
            case N:
            	if(previous.getOperator().equals(CigarOperator.D) || previous.getOperator().equals(CigarOperator.N)) {
            		//Just keep skipping if the previous element was a D or N
            		currentStart += element.getLength();
            	} else {
	            	int currentEnd = currentStart + currentLength - 1;
	            	
	            	//if we are starting with a deletion then we should skip
	            	if(currentEnd >= currentStart) {
	            		list.add( buildFeature(in,currentStart,currentEnd,isNegativeStrand)  );
	            	}
	            	
	            	
	            	currentStart = currentEnd + element.getLength() + 1;
	            	currentLength = 0;
            	}
            	break;
            }
            previous = element;
		}
		
		//add last feature
		int currentEnd = currentStart + currentLength - 1;
		if(currentEnd >= currentStart) {
			list.add( buildFeature(in,currentStart,currentEnd,isNegativeStrand)  );
		}
		return list;
	}
	
	public int getNumberOfMappedBases(SAMRecord in) {
		Cigar cigar = in.getCigar();
		List<CigarElement> elements = cigar.getCigarElements();
		int mappedBases = 0;
		for(final CigarElement element : elements) {
			//logger.info(element.getLength() + "");
            switch (element.getOperator()) {
            case M:
            	mappedBases += element.getLength();
                break;
            default:
            }
		}
		return mappedBases;
	}
	
	private Interval<BEDEntry> buildFeature(SAMRecord in, int start, int end, boolean isNegativeStrand) {
		Interval<BEDEntry> f = new IntervalImpl<BEDEntry>(start, end, new BEDEntry(in.getReferenceName(), start, end));
		return f;
	}

	
	
}
