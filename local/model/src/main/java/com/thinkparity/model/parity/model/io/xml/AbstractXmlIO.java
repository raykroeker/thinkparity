/*
 * 25-Oct-2005
 */
package com.thinkparity.model.parity.model.io.xml;

import java.io.File;
import java.util.Stack;

import org.apache.log4j.Logger;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.log4j.ModelLoggerFactory;
import com.thinkparity.model.parity.IParityConstants;
import com.thinkparity.model.parity.api.ParityObject;
import com.thinkparity.model.parity.model.project.Project;
import com.thinkparity.model.parity.model.workspace.Workspace;

/**
 * This is an abstraction of the xml input\output routines used by the parity
 * xml input\output classes.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1.2.1
 */
public abstract class AbstractXmlIO {

	/**
	 * Handle to an apache logger.
	 */
	protected final Logger logger =
		ModelLoggerFactory.getLogger(getClass());

	/**
	 * Parity workspace to work within.
	 */
	private final Workspace workspace;

	/**
	 * Create a AbstractXmlIO.
	 */
	protected AbstractXmlIO(final Workspace workspace) {
		super();
		this.workspace = workspace;
	}

	/**
	 * Build an xml file for a parity object.
	 * 
	 * @param parityObject
	 *            The parity object to build the xml file for.
	 * @return The xml file.
	 * @see AbstractXmlIO#getXmlFileChild(ParityObject)
	 * @see AbstractXmlIO#getXmlFileParent(ParityObject)
	 */
	protected File getXmlFile(final ParityObject parityObject) {
		logger.info("buildXmlFile(ParityObject)");
		logger.debug(parityObject);
		final String child = getXmlFileChild(parityObject);
		logger.debug(child);
		final String parent = getXmlFileParent(parityObject);
		logger.debug(parent);
		return new File(parent, child);
	}

	/**
	 * Build the child portion(name) of an xml file for a given parity object.
	 * 
	 * @param parityObject
	 *            The parity object to build the child portion(name) of the xml
	 *            file for.
	 * @return The child portion(name) of an xml file.
	 * @see AbstractXmlIO#getXmlFile(ParityObject)
	 * @see AbstractXmlIO#getXmlFileParent(ParityObject)
	 */
	private String getXmlFileChild(final ParityObject parityObject) {
		return new StringBuffer(parityObject.getName())
			.append(".")
			.append(parityObject.getClass().getSimpleName().toLowerCase())
			.toString();
	}

	/**
	 * Build the parent portion(path) of an xml file for a given parity object.
	 * 
	 * @param parityObject
	 *            The parity object to build the parent portion(path) of the xml
	 *            file for.
	 * @return The parent portion(path) of an xml file.
	 * @see AbstractXmlIO#getXmlFile(ParityObject)
	 * @see AbstractXmlIO#getXmlFileChild(ParityObject)
	 */
	private String getXmlFileParent(final ParityObject parityObject) {
		final Stack<ParityObject> parentStack = new Stack<ParityObject>();
		ParityObject parityObjectParent = parityObject.getParent();
		while(null != parityObjectParent) {
			Assert.assertTrue(
					"getXmlFile(ParityObject)",
					parityObjectParent instanceof Project);
			parentStack.push(parityObjectParent);
			parityObjectParent = parityObjectParent.getParent();
		}
		final StringBuffer path = new StringBuffer()
			.append(workspace.getDataURL().getFile());
		parentStack.pop();	// the root project doesn't exist in the path
		while(!parentStack.isEmpty()) {
			path.append(File.separatorChar)
				.append(parentStack.pop().getName());
		}
		return path.append(File.separatorChar)
			.append(IParityConstants.META_DATA_DIRECTORY_NAME)		
			.toString();
	}
}
