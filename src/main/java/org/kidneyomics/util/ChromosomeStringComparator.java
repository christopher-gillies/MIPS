package org.kidneyomics.util;

import java.util.Comparator;

import org.apache.commons.lang3.StringUtils;

public class ChromosomeStringComparator implements Comparator<String> {

	
	@Override
	public int compare(String o1, String o2) {
		
		if(StringUtils.isNumeric(o1) && StringUtils.isNumeric(o2)) {
			Integer o1Int = Integer.parseInt(o1);
			Integer o2Int = Integer.parseInt(o2);
			return o1Int.compareTo(o2Int);
		} else if(StringUtils.isNumeric(o1) && !StringUtils.isNumeric(o2)) {
			return -1;
		} else if(!StringUtils.isNumeric(o1) && StringUtils.isNumeric(o2)) {
			return 1;
		} else {
			return o1.compareTo(o2);
		}
	}

}
