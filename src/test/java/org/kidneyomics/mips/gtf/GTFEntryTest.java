package org.kidneyomics.mips.gtf;

import static org.junit.Assert.*;

import org.junit.Test;

public class GTFEntryTest {

	@Test
	public void test() {
		String line = "chr1	HAVANA	CDS	906066	906138	.	+	0	gene_id \"ENSG00000187583.6\"; transcript_id \"ENST00000379407.3\"; gene_type \"protein_coding\"; gene_status \"KNOWN\"; gene_name \"PLEKHN1\"; transcript_type \"protein_coding\"; transcript_status \"KNOWN\"; transcript_name \"PLEKHN1-004\"; exon_number 5;  exon_id \"ENSE00001385460.1\";  level 2; tag \"basic\"; tag \"appris_candidate\"; tag \"CCDS\"; ccdsid \"CCDS53256.1\"; havana_gene \"OTTHUMG00000040756.4\"; havana_transcript \"OTTHUMT00000473255.1\"";
		
		GTFEntry entry = GTFEntry.createGENCODEEntry(line);
		
		assertNotNull(entry);
		
		assertEquals("1",entry.getChr());
		assertEquals(GTFEntry.SOURCE.HAVANA,entry.getSource());
		assertEquals(GTFEntry.TYPE.CDS,entry.getType());
		assertEquals(906066,entry.getStart());
		assertEquals(906138,entry.getEnd());
		assertEquals(GTFEntry.STRAND.PLUS,entry.getStrand());
		assertEquals(GTFEntry.PHASE.ZERO,entry.getPhase());
		
		
		assertEquals("ENSG00000187583.6",entry.getGene_id());
		assertEquals("ENST00000379407.3",entry.getTranscript_id());
		assertEquals("protein_coding",entry.getGene_type());
		assertEquals("KNOWN",entry.getGene_status());
		assertEquals("PLEKHN1",entry.getGene_name());
		assertEquals("protein_coding",entry.getTranscript_type());
		assertEquals("KNOWN",entry.getTranscript_status());
		assertEquals("PLEKHN1-004",entry.getTranscript_name());
		
		assertEquals("5",entry.getExon_number());
		assertEquals("ENSE00001385460.1",entry.getExon_id());
		assertEquals("2",entry.getLevel());
	}

}
