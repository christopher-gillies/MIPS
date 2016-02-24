package org.kidneyomics.util;

import org.apache.commons.cli.ParseException;

public interface OptionProcessor {
	void processInputs(String[] args) throws ParseException;
}
