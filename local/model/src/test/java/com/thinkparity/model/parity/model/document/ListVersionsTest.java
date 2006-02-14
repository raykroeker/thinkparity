/*
 * Nov 10, 2005
 */
package com.thinkparity.model.parity.model.document;

import java.io.File;
import java.util.Collection;
import java.util.Comparator;
import java.util.Vector;

import com.thinkparity.model.parity.model.ModelTestCase;
import com.thinkparity.model.parity.model.artifact.ArtifactVersion;
import com.thinkparity.model.parity.model.artifact.ComparatorBuilder;

/**
 * Test the document model listVersions api.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1.2.2
 */
public class ListVersionsTest extends ModelTestCase {
	
	/**
	 * Test data fixture.
	 * 
	 * @see ListVersionsTest#setUp()
	 * @see ListVersionsTest#tearDown()
	 */
	private class Fixture {
		private final Document document;
		private final DocumentModel documentModel;
		private final Integer numberOfVersions;
		private Fixture(final Document document,
				final DocumentModel documentModel, final Integer numberOfVersions) {
			this.document = document;
			this.documentModel = documentModel;
			this.numberOfVersions = numberOfVersions;
		}
	}

	/**
	 * Test data.
	 */
	private Vector<Fixture> data;

	/**
	 * Create a ListVersionsTest.
	 */
	public ListVersionsTest() { super("testListVersions"); }

	/**
	 * Test the document model listVersions api.
	 *
	 */
	public void testListVersions() {
		try {
			Collection<DocumentVersion> versions;
			for(Fixture datum : data) {
				versions =
					datum.documentModel.listVersions(datum.document.getId());

				// ensure the list is the correct size
				assertNotNull(versions);
				assertEquals(datum.numberOfVersions.intValue(), versions.size());

				// ensure the versions are sorted in an ascending order
				int previousVersion = 0;
				int currentVersion;
				for(DocumentVersion v : versions) {
					currentVersion = v.getVersionId().intValue();
					assertEquals(previousVersion + 1, currentVersion);
					previousVersion = v.getVersionId().intValue();
				}

				final Comparator<ArtifactVersion> descendingVersionId =
					new ComparatorBuilder().createVersionById(Boolean.FALSE);
				versions =
					datum.documentModel.listVersions(datum.document.getId(), descendingVersionId);

				// ensure the list is the correct size
				assertNotNull(versions);
				assertEquals(datum.numberOfVersions.intValue(), versions.size());

				// ensure the versions are sorted in an descending order
				previousVersion = datum.numberOfVersions + 1;
				for(DocumentVersion v : versions) {
					currentVersion = v.getVersionId().intValue();
					assertEquals(previousVersion - 1, currentVersion);
					previousVersion = v.getVersionId().intValue();
				}
			}
		}
		catch(Throwable t) { fail(createFailMessage(t)); }
	}

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		data = new Vector<Fixture>(getInputFilesLength());
		final DocumentModel documentModel = getDocumentModel();
		String name, description;
		Document document;
		for(File testFile : getInputFiles()) {
			name = testFile.getName();
			description = "Document:  " + name;
			document =
				documentModel.create(name, description, testFile);
			documentModel.createVersion(document.getId());
			documentModel.createVersion(document.getId());
			documentModel.createVersion(document.getId());
			data.add(new Fixture(document, documentModel, 4));
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
