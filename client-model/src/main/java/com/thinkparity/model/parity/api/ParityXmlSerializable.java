/*
 * May 7, 2005
 */
package com.thinkparity.model.parity.api;

import java.io.File;

import com.thinkparity.model.xstream.XStreamSerializable;

/**
 * ParityXmlSerializable
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public interface ParityXmlSerializable extends XStreamSerializable {

	public File getMetaDataFile(final File parentDirectory);
}
