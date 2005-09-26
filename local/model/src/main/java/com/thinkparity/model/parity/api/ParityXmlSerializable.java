/*
 * May 7, 2005
 */
package com.thinkparity.model.parity.api;

import java.io.File;

import com.thinkparity.codebase.log4j.Loggable;

/**
 * ParityXmlSerializable
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public interface ParityXmlSerializable extends Loggable {

	public File getMetaDataFile(final File parentDirectory);
}
