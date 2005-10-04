/*
 * Aug 21, 2005
 */
package com.thinkparity.model.parity.model.document;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import com.thinkparity.model.parity.ParityTestCase;
import com.thinkparity.model.parity.model.project.Project;

/**
 * DocumentModelTest
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class DocumentModelTest extends ParityTestCase {

	private Project testGetPathRootProject;
	private Collection<Document> testGetPathDocuments;

	/**
	 * Create a DocumentModelTest.
	 */
	public DocumentModelTest() {
		super("Test:  Document model.");
	}

	/**
	 * @see com.thinkparity.model.parity.ParityTestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		testGetPathRootProject = projectModel.getRootProject(workspace);
		testGetPathDocuments = testGetPathRootProject.getDocuments();
	}

	/**
	 * @see com.thinkparity.model.parity.ParityTestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testGetPath() {
		StringBuffer testDocumentPath;
		Document testDocument;
		for(Iterator<Document> i = testGetPathDocuments.iterator(); i.hasNext();) {
			testDocument = i.next();
			testDocumentPath = testDocument.getPath();
			DocumentModelTest.assertNotNull(testDocumentPath);
			DocumentModelTest.assertEquals(testDocumentPath, getExpectedDocumentPath(testDocument));
		}

	}

	private StringBuffer getExpectedDocumentPath(final Document document) {
		final LinkedList<Project> parentQueue = new LinkedList<Project>();
		Project parentProject = document.getParent();
		while(parentProject.isSetParent()) {
			parentQueue.add(parentProject);
			parentProject = parentProject.getParent();
		}
		final StringBuffer expectedDocumentPath =
			new StringBuffer(parentQueue.poll().getName());
		while(!parentQueue.isEmpty())
			expectedDocumentPath.append("/")
				.append(parentQueue.poll().getName());
		return expectedDocumentPath;
	}
}
