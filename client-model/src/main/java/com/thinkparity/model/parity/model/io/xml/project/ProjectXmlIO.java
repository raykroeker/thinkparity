/*
 * Feb 20, 2005
 */
package com.thinkparity.model.parity.model.io.xml.project;

import java.io.File;
import java.io.IOException;

import com.thinkparity.model.parity.model.io.xml.AbstractXmlIO;
import com.thinkparity.model.parity.model.project.Project;
import com.thinkparity.model.xstream.XStreamUtil;

/**
 * ProjectXmlIO
 * @author raykroeker@gmail.com
 * @version 1.2
 */
public class ProjectXmlIO extends AbstractXmlIO {

	/**
	 * Create a ProjectXmlIO [Singleton]
	 */
	private ProjectXmlIO() { super(); }

	public static Project readXml(final File projectMetaDataFile)
			throws IOException {
		final Project project = (Project) XStreamUtil.read(projectMetaDataFile);
		return project;
	}

	public static void writeCreationXml(final Project project) throws IOException {
		XStreamUtil.write(project);
	}

	public static void writeUpdateXml(final Project project) throws IOException {
		final boolean didDelete = XStreamUtil.delete(project);
		if(false == didDelete)
			throw new IOException("Could not delete project xml.");
		XStreamUtil.write(project);
	}
}
