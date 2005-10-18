/*
 * Aug 21, 2005
 */
package com.thinkparity.model.parity.model.document;

import java.util.Stack;
import java.util.Vector;

import com.thinkparity.model.ModelTestCase;
import com.thinkparity.model.ModelTestFile;
import com.thinkparity.model.parity.model.project.Project;

/**
 * DocumentModelTest
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class DocumentModelTest extends ModelTestCase {

	/**
	 * Test data definition for the get path test.
	 * @see DocumentModelTest#setUpGetPath()
	 * @see DocumentModelTest#testGetPath()
	 */
	private class GetPathData {
		private final StringBuffer expectedPath;
		private final StringBuffer path;
		private GetPathData(final StringBuffer expectedPath,
				final StringBuffer path) {
			this.expectedPath = expectedPath;
			this.path = path;
		}
	}

	private Vector<GetPathData> getPathData;

	/**
	 * Create a DocumentModelTest.
	 */
	public DocumentModelTest() { super("Test:  Document model"); }

	/**
	 * Test the paths generated for all of the documents attached to the
	 * root project.
	 *
	 */
	public void testGetPath() {
		try {
			for(GetPathData data : getPathData) {
				DocumentModelTest.assertNotNull(data.path);
				DocumentModelTest.assertEquals(data.path.toString(), data.expectedPath.toString());
			}
		}
		catch(Throwable t) { fail(getFailMessage(t)); }
	}

	/**
	 * @see com.thinkparity.model.ModelTestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		setUpGetPath();
	}

	/**
	 * Set up the testGetPath data.
	 * 
	 * @throws Exception
	 */
	protected void setUpGetPath() throws Exception {
		final Project testProject = createTestProject("testGetPath");
		String name, description;
		Document document;

		getPathData = new Vector<GetPathData>(10);
		for(ModelTestFile testFile : getJUnitTestFiles()) {
			name = testFile.getName();
			description = name;
			document = getDocumentModel().createDocument(testProject, name, description, testFile.getFile());
			getPathData.add(
					new GetPathData(getExpectedPath(document), document.getPath()));
		}
	}

	/**
	 * @see com.thinkparity.model.ModelTestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		tearDownGetPath();
	}

	/**
	 * Clean up the get path data.
	 * 
	 */
	protected void tearDownGetPath() {
		getPathData.clear();
		getPathData = null;
	}

	/**
	 * Build an expected path for a given document.  This uses the document to
	 * obtain the project to obtain the project, etc.
	 * 
	 * @param document
	 *            The document to build the expected path for.
	 * @return The expected path.
	 */
	private StringBuffer getExpectedPath(final Document document) {
		final Stack<Project> projectStack = new Stack<Project>();
		Project parentProject = document.getParent();
		while(parentProject.isSetParent()) {
			projectStack.push(parentProject);
			parentProject = parentProject.getParent();
		}
		projectStack.add(parentProject);

		final StringBuffer expectedPath =
			new StringBuffer(projectStack.pop().getCustomName());
		while(!projectStack.isEmpty())
			expectedPath.append("/")
				.append(projectStack.pop().getCustomName());
		expectedPath.append("/")
			.append(document.getCustomName());
		return expectedPath;
	}
}
