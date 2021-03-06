package org.kidneyomics.util;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import htsjdk.samtools.SAMFileHeader.SortOrder;
import htsjdk.samtools.SAMRecord;
import htsjdk.samtools.SAMRecordIterator;
import htsjdk.samtools.SamPairUtil;
import htsjdk.samtools.SamReader;
import htsjdk.samtools.SamReaderFactory;

public class BAMProcessorImplQueue implements BAMProcessor {

	private SamReader reader;
	private SAMRecordIterator iterator;
	private Queue<SAMRecord> queue;
	private boolean onlyUseProperlyPairedReads = true;
	private long readCount = 0;
	private Logger logger = LoggerFactory.getLogger(BAMProcessorImplQueue.class);
	private int logSkipSize = 100000;
	
	public BAMProcessorImplQueue withLogSkipSize(int logSkipSize) {
		this.logSkipSize = logSkipSize;
		return this;
	}
	
	private BAMProcessorImplQueue(File in) {
		reader = SamReaderFactory.makeDefault().open(in);
		iterator = reader.iterator();
		queue = new LinkedList<SAMRecord>();
	}
	
	public static BAMProcessorImplQueue getBAMProcessor(File in) {
		return new BAMProcessorImplQueue(in);
	}
	
	/**
	 * This is needed when bam is sorted in coordinate order instead of query order
	 * @return the next pair of mapped reads, if only one read is mapped then return that one read
	 * return null if no mapped reads are left.
	 */
	public SAMRecordPair getNextReadPair() {
		SAMRecord mate1;
		//Get first mapped read
		if(onlyUseProperlyPairedReads) {
			while( (mate1 = getFirstMate()) != null && mate1.getReadUnmappedFlag() == true && mate1.getProperPairFlag() == false); 
		} else {
			while( (mate1 = getFirstMate()) != null && mate1.getReadUnmappedFlag() == true); 
		}
		if(mate1 == null) {
			return null;
		}
		
		//At this point mate1 should not be null;
		if(mate1.getMateUnmappedFlag()) {
			SAMRecordPair srp = new SAMRecordPair();
			srp.setMate1(mate1);
			return srp;
		} 
		
		//find mate
		SAMRecord mate2 = getSecondMate(mate1);
		if(mate2 == null) {
			throw new IllegalStateException("Second mate not found for:\n" + mate1.toString());
		} else {
			SAMRecordPair srp = new SAMRecordPair();
			srp.setMate1(mate1);
			srp.setMate2(mate2);
			return srp;
		}
		
	}
	
	SAMRecord getFirstMate() {
		//check queue, and then check the record iterator
		SAMRecord mate1 = null;
		if(queue.peek() != null) {
			mate1 = queue.poll();
		} else {
			if(iterator.hasNext()) {
				mate1 = iterator.next();
				countRead(mate1);
			}
		}
		return mate1;
		
	}
	
	SAMRecord getSecondMate(SAMRecord mate1) {
		
		SAMRecord mate2 = checkForMateInQueue(mate1);
		
		if(mate2 != null) {
			return mate2;
		}  else {
			return readMateFromFile(mate1);
		}
		
		
	}
	
	private SAMRecord readMateFromFile(SAMRecord mate1) {
		SAMRecord mate2 = null;
		while(mate2 == null && iterator.hasNext()) {
			SAMRecord tmp = iterator.next();
			countRead(tmp);
			if(areMates(mate1,tmp)) {
				mate2 = tmp;
			} else {
				//if the record is unmapped
				if(!tmp.getReadUnmappedFlag()) {
					queue.add(tmp);
				}
			}
		}
		return mate2;
	}
	
	private SAMRecord checkForMateInQueue(SAMRecord mate1) {
		SAMRecord mate2 = null;
		Iterator<SAMRecord> queueIter = queue.iterator();
		while(mate2 == null && queueIter.hasNext()) {
			SAMRecord tmp = queueIter.next();
			if(areMates(mate1,tmp)) {
				mate2 = tmp;
				queueIter.remove();
			}
		}
		return mate2;
	}
	
	private void countRead(final SAMRecord record) {
		if(record != null) {
			readCount++;
			if(readCount % logSkipSize == 0) {
				logger.info("Single reads processed: " + readCount);
				logger.info("Last read: " + record.getReferenceName() + ":" + record.getAlignmentStart() + "-" + record.getAlignmentEnd());
			}
		}
	}
	
	/*
	 * Maybe relax this to only ensure that the names match?
	 */
	public boolean areMates(final SAMRecord mate1, final SAMRecord mate2) {
		if(mate1.getMateAlignmentStart() == mate2.getAlignmentStart()
				&& mate2.getMateAlignmentStart() == mate1.getAlignmentStart() 
				&& mate1.getReadName().equals(mate2.getReadName())) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void close() throws Exception {
		iterator.close();	
	}

	
}
