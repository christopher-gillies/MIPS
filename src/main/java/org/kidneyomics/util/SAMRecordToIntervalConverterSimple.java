package org.kidneyomics.util;

import java.util.List;

import org.kidneyomics.util.interval.Interval;
import org.springframework.core.convert.converter.Converter;

import htsjdk.samtools.SAMRecord;

public class SAMRecordToIntervalConverterSimple implements Converter<SAMRecord, Interval<BEDEntry>> {

	@Override
	public Interval<BEDEntry> convert(SAMRecord arg0) {
		
		BEDEntry entry = new BEDEntry(arg0.getReferenceName(), arg0.getAlignmentStart(), arg0.getAlignmentEnd());
		
		return entry.toInterval();
		
	}

}
