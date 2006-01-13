/*
 * Nov 10, 2005
 */
package com.thinkparity.model.parity.model.document;

import java.util.Collection;
import java.util.UUID;
import java.util.Vector;

import com.thinkparity.model.JUnitTestFile;
import com.thinkparity.model.ModelTestCase;
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
		private final UUID destinationId;
		private final UUID documentId;
		private final DocumentModel documentModel;
		private final UUID sourceId;
		private Fixture(final UUID destinationId, final UUID documentId,
				final DocumentModel documentModel, final UUID sourceId) {
			this.destinationId = destinationId;
			this.documentId = documentId;
			this.documentModel = documentModel;
			this.sourceId = sourceId;
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
				datum.documentModel.move(datum.documentId, datum.destinationId);
				// ensure the moved document is correct
				document = datum.documentModel.get(datum.documentId);
				assertNotNull(document);

				// ensure the document is missing from the source project
				sourceDocuments = datum.documentModel.list(datum.sourceId);
				assertNotNull(sourceDocuments);
				assertNotContains(sourceDocuments, document);

				// ensure the document is listed in the destination project
				destinationDocuments = datum.documentModel.list(datum.destinationId);
				assertNotNull(destinationDocuments);
				assertContains(destinationDocuments, document);
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
		source = projectModel.create(testProject.getId(), name, description);

		name = "D";
		description = name;
		destination = projectModel.create(testProject.getId(), name, description);
		for(JUnitTestFile testFile : getJUnitTestFiles()) {
			name = testFile.getName();
			description = name;
			document = documentModel.create(source.getId(), name, description, testFile.getFile());

			data.add(new Fixture(destination.getId(), document.getId(), documentModel, source.getId()));
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
