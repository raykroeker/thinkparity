/*
 * 2005-05-23 18:17
 */
package com.thinkparity.model.parity;

import org.apache.log4j.Logger;

import com.thinkparity.codebase.Version;
import com.thinkparity.codebase.log4j.LoggerFormatter;
import com.thinkparity.model.parity.util.LoggerFactory;

/**
 * Parity3rdParty
 * @author raykroeker@gmail.com
 * @version 1.0
 */
public class Parity3rdParty {

	private static final Logger logger =
		LoggerFactory.createInstance(Parity3rdParty.class);

	private static final LoggerFormatter loggerFormatter =
		new LoggerFormatter();

	public static void display() {		
		logger.debug(loggerFormatter.format(
				Version.getName(),
				Version.getVersion()));
	}
}
