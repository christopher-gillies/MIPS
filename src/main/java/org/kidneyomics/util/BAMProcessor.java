package org.kidneyomics.util;

public interface BAMProcessor extends AutoCloseable {
	SAMRecordPair getNextReadPair();
}