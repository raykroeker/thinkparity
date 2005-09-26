/*
 * Feb 20, 2005
 */
package com.thinkparity.model.parity.api.project.xml;

import java.io.File;
import java.io.IOException;


import com.thinkparity.model.parity.model.project.Project;
import com.thinkparity.model.parity.xml.XmlUtil;

/**
 * ProjectXml
 * @author raykroeker@gmail.com
 * @version 1.2
 */
public class ProjectXml {

	/**
	 * Register a project converter.  The project converter is responsible for
	 * translating a project to xml, and vice-versa.
	 */
	static { XmlUtil.registerTranslator(new ProjectXmlTranslator()); }

	/**
	 * Create a ProjectXml [Singleton]
	 */
	private ProjectXml() { super(); }

	public static Project readXml(final File projectMetaDataFile)
			throws IOException {
		final Project project = (Project) XmlUtil.read(projectMetaDataFile);
		return project;
	}

	public static void writeCreationXml(final Project project) throws IOException {
		XmlUtil.write(project);
	}

	public static void writeUpdateXml(final Project project) throws IOException {
		final boolean didDelete = XmlUtil.delete(project);
		if(false == didDelete)
			throw new IOException("Could not delete project xml.");
		XmlUtil.write(project);
	}

	public static StringBuffer toXml(final Project project) {
		return XmlUtil.toXml(project);
	}
}
