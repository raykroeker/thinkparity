/*
 * Nov 10, 2005
 */
package com.thinkparity.model.parity.model.document;

import java.util.Collection;
import java.util.Vector;

import com.thinkparity.model.ModelTestCase;
import com.thinkparity.model.ModelTestFile;
import com.thinkparity.model.parity.model.project.Project;
import com.thinkparity.model.parity.model.project.ProjectModel;

/**
 * Test the document model move api.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1.2.3
 */
public class MoveTest extends ModelTestCase {

	/**
	 * Test data fixture.
	 * 
	 * @see MoveTest#setUp()
	 * @see MoveTest#tearDown()
	 */
	private class Fixture {
		private final Project destination;
		private final Document document;
		private final DocumentModel documentModel;
		private final Project source;
		private Fixture(final Project destination, final Document document, final DocumentModel documentModel, final Project source) {
			this.destination = destination;
			this.document = document;
			this.documentModel = documentModel;
			this.source = source;
		}
	}

	/**
	 * Test data.
	 */
	private Vector<Fixture> data;

	/**
	 * Create a MoveTest.
	 */
	public MoveTest() { super("testMove"); }

	/**
	 * Test the document model move api.
	 */
	public void testMove() {
		try {
			Collection<Document> sourceDocuments;
			Collection<Document> destinationDocuments;
			Document document;
			for(Fixture datum : data) {
				datum.documentModel.move(datum.document, datum.destination);

				// ensure the document is missing from the source project
				sourceDocuments = datum.documentModel.list(datum.source);
				assertNotNull(sourceDocuments);
				assertNotContains(sourceDocuments, datum.document);

				// ensure the document is listed in the destination project
				destinationDocuments = datum.documentModel.list(datum.destination);
				assertNotNull(destinationDocuments);
				assertContains(destinationDocuments, datum.document);

				// ensure the moved document is correct
				document = datum.documentModel.get(datum.document.getId());
				assertNotNull(document);
			}
		}
		catch(Throwable t) { fail(getFailMessage(t)); }
	}

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		data = new Vector<Fixture>(getJUnitTestFilesSize());
		final Project testProject = createTestProject("testMove");
		final ProjectModel projectModel = getProjectModel();
		final DocumentModel documentModel = getDocumentModel();
		String name, description;
		Project destination, source;
		Document document;

		name = "S";
		description = name;
		source = projectModel.create(testProject, name, description);

		name = "D";
		description = name;
		destination = projectModel.create(testProject, name, description);
		for(ModelTestFile testFile : getJUnitTestFiles()) {
			name = testFile.getName();
			description = name;
			document = documentModel.create(source, name, description, testFile.getFile());

			data.add(new Fixture(destination, document, documentModel, source));
		}
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		data.clear();
		data = null;
	}

}
