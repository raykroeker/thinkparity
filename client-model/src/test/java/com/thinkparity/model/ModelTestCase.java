/*
 * Feb 13, 2006
 */
package com.thinkparity.model;

import java.io.File;
import java.io.IOException;

import com.thinkparity.model.parity.model.document.DocumentModel;

import com.raykroeker.junitx.TestCase;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class ModelTestCase extends TestCase {

	static {
		// set non ssl mode
		System.setProperty("parity.insecure", "true");
	}

	private DocumentModel documentModel;

	/**
	 * Create a ModelTestCase.
	 * 
	 * @param name
	 *            The test name
	 */
	protected ModelTestCase(final String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	protected DocumentModel getDocumentModel() {
		if(null == documentModel) {
			documentModel = DocumentModel.getModel();
		}
		return documentModel;
	}

	/**
	 * @see com.raykroeker.junitx.TestCase#getInputFiles()
	 */
	protected File[] getInputFiles() throws IOException {
		final File[] inputFiles = new File[5];
		System.arraycopy(super.getInputFiles(), 0, inputFiles, 0, 5);
		return inputFiles;
	
	}

}
