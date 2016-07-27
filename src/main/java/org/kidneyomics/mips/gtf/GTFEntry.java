package org.kidneyomics.mips.gtf;

import java.util.HashMap;
import java.util.Map;

import org.kidneyomics.util.interval.Interval;
import org.kidneyomics.util.interval.IntervalImpl;

public class GTFEntry {
	
	private enum COLUMN {
		CHR(0),
		SOURCE(1),
		TYPE(2),
		START(3),
		END(4),
		SCORE(5),
		STRAND(6),
		PHASE(7),
		INFO(8);
		
		private final int num;
		
		private COLUMN(int num) {
			this.num = num;
		}
		
		public int index() {
			return this.num;
		}
		
	}
	
	public enum SOURCE {
		ENSEMBL,
		HAVANA,
		MISSING
	}
	
	public enum FORMAT {
		GENCODE,
		ENSEMBL
	}
	
	public enum STRAND {
		PLUS,
		MINUS
	}
	
	public enum PHASE {
		ZERO,
		ONE,
		TWO,
		NA
	}
	
	public enum TYPE {
		GENE,
		TRANSCRIPT,
		EXON,
		CDS,
		UTR,
		START_CODON,
		STOP_CODON,
		Selenocysteine,
	}
	
	//format
	/*
	 chr1	HAVANA	CDS	906066	906138	.	+	0	gene_id "ENSG00000187583.6"; transcript_id "ENST00000379407.3"; gene_type "protein_coding"; gene_status "KNOWN"; gene_name "PLEKHN1"; transcript_type "protein_coding"; transcript_status "KNOWN"; transcript_name "PLEKHN1-004"; exon_number 5;  exon_id "ENSE00001385460.1";  level 2; tag "basic"; tag "appris_candidate"; tag "CCDS"; ccdsid "CCDS53256.1"; havana_gene "OTTHUMG00000040756.4"; havana_transcript "OTTHUMT00000473255.1";
	 */
	
	private String chr;
	private SOURCE source;
	private TYPE type;
	private int start;
	private int end;
	private STRAND strand;
	private PHASE phase;
	private Map<String,String> info;
	
	private String gene_id;
	private String transcript_id;
	private String gene_type;
	private String gene_status;
	private String gene_name;
	private String transcript_name;
	private String transcript_type;
	private String transcript_status;
	private String exon_number;
	private String exon_id;
	private String level;
	
	private GTFEntry() {
		this.info = new HashMap<>();
	}
	
	
	public static GTFEntry createGENCODEEntry(String line) {
		
		GTFEntry result = new GTFEntry();
		
		String[] cols = line.split("\t");
		
		if(cols.length != 9) {
			throw new RuntimeException(line + "\ndoes not have 9 columns");
		}
		
		result.chr = cols[COLUMN.CHR.index()].replaceAll("chr", "");
		
		//set source
		switch(cols[COLUMN.SOURCE.index()]) {
		case "ENSEMBL":
			result.source = SOURCE.ENSEMBL;
			break;
		case "HAVANA":
			result.source = SOURCE.HAVANA;
			break;
			default:
				result.source = SOURCE.MISSING;
			
		}
		
		//set type
		String type = cols[COLUMN.TYPE.index()];
		switch(type) {
		case "gene":
			result.type = TYPE.GENE;
			break;
		case "transcript":
			result.type = TYPE.TRANSCRIPT;
			break;
		case "exon":
			result.type = TYPE.EXON;
			break;
		case "CDS":
			result.type = TYPE.CDS;
			break;
		case "UTR":
			result.type = TYPE.UTR;
			break;
		case "start_codon":
			result.type = TYPE.START_CODON;
			break;
		case "stop_codon":
			result.type = TYPE.STOP_CODON;
			break;
		case "Selenocysteine":
			result.type = TYPE.Selenocysteine;
			break;
			default:
				throw new RuntimeException(type + "\nUnknown type");
		}
		
		
		result.start = Integer.parseInt(cols[COLUMN.START.index()]);
		result.end = Integer.parseInt(cols[COLUMN.END.index()]);
		
		//set strand
		switch(cols[COLUMN.STRAND.index()]) {
		case "+":
			result.strand = STRAND.PLUS;
			break;
		case "-":
			result.strand = STRAND.MINUS;
			break;
			default:
				throw new RuntimeException("Strand is not  + or minus");
			
		}
		
		//set phase
		switch(cols[COLUMN.PHASE.index()]) {
		case "0":
			result.phase = PHASE.ZERO;
			break;
		case "1":
			result.phase = PHASE.ONE;
			break;
		case "2":
			result.phase = PHASE.TWO;
			break;
		default:
			result.phase = PHASE.NA;
		}
		
		
		String info = cols[COLUMN.INFO.index()];
		
		String[] fields = info.split(";");
		
		for(String field : fields) {
			String[] keyvals = field.trim().split("[ \t]+");
			if(keyvals.length != 2) {
				throw new RuntimeException(field + "\n not correct format");
			}
			
			String key = keyvals[0];
			String val = keyvals[1].replaceAll("\"", "");
			
			result.info.put(key, val);
		}
		
		
		result.gene_id = result.info.get("gene_id");
		result.transcript_id = result.info.get("transcript_id");
		result.gene_type = result.info.get("gene_type");
		result.gene_status = result.info.get("gene_status");
		result.gene_name = result.info.get("gene_name");
		result.transcript_type = result.info.get("transcript_type");
		result.transcript_status = result.info.get("transcript_status");
		result.transcript_name = result.info.get("transcript_name");
		result.exon_number = result.info.get("exon_number");
		result.exon_id = result.info.get("exon_id");
		result.level = result.info.get("level");
		
		return result;
	}
	
	
	
	public String getChr() {
		return chr;
	}


	public SOURCE getSource() {
		return source;
	}


	public TYPE getType() {
		return type;
	}


	public int getStart() {
		return start;
	}


	public int getEnd() {
		return end;
	}


	public STRAND getStrand() {
		return strand;
	}


	public PHASE getPhase() {
		return phase;
	}


	public Map<String, String> getInfo() {
		return info;
	}


	public String getGene_id() {
		return gene_id;
	}


	public String getTranscript_id() {
		return transcript_id;
	}


	public String getGene_type() {
		return gene_type;
	}


	public String getGene_status() {
		return gene_status;
	}


	public String getGene_name() {
		return gene_name;
	}


	public String getTranscript_name() {
		return transcript_name;
	}


	public String getTranscript_type() {
		return transcript_type;
	}


	public String getTranscript_status() {
		return transcript_status;
	}


	public String getExon_number() {
		return exon_number;
	}


	public String getExon_id() {
		return exon_id;
	}


	public String getLevel() {
		return level;
	}


	public Interval<GTFEntry> toInterval() {
		
		Interval<GTFEntry> interval = new IntervalImpl<>(this.start, this.end, this);
		
		return interval;
	}
	
	public static String toStringHeader() {
		return "CHR\tFEATURE_TYPE\tSTART\tEND\tSTRAND\tPHASE\tGENE_ID\tTRANSCRIPT_ID\tGENE_TYPE\tGENE_STATUS\tGENE_NAME\tTRANSCRIPT_TYPE\tTRANSCRIPT_STATUS\tTRANSCRIPT_NAME\tEXON_NUMBER\tEXON_ID";
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.chr);
		sb.append("\t");
		
		sb.append(this.type);
		sb.append("\t");
		
		sb.append(this.start);
		sb.append("\t");
		
		sb.append(this.end);
		sb.append("\t");
		
		sb.append(this.strand);
		sb.append("\t");
		
		sb.append(this.phase);
		sb.append("\t");
		
		sb.append(this.gene_id);
		sb.append("\t");
		
		sb.append(this.transcript_id);
		sb.append("\t");
		
		sb.append(this.gene_type);
		sb.append("\t");
		
		sb.append(this.gene_status);
		sb.append("\t");
		
		sb.append(this.gene_name);
		sb.append("\t");
		
		sb.append(this.transcript_type);
		sb.append("\t");
		
		sb.append(this.transcript_status);
		sb.append("\t");
		
		sb.append(this.transcript_name);
		sb.append("\t");
		
		sb.append(this.exon_number);
		sb.append("\t");
		
		sb.append(this.exon_id);
		
		
		return sb.toString();
	}
}
