/*
 * Nov 10, 2005
 */
package com.thinkparity.ophelia.model.document;

import java.io.File;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

import com.thinkparity.codebase.model.artifact.ArtifactVersion;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;

import com.thinkparity.ophelia.OpheliaTestUser;
import com.thinkparity.ophelia.model.util.sort.ComparatorBuilder;

/**
 * Test the document model listVersions api.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1.2.2
 */
public class ReadVersionsTest extends DocumentTestCase {
	
	/** Test name. */
    private static final String NAME = "[TEST READ VERSIONS]";

	/** Test data. */
	private List<Fixture> data;

	/** Create ListVersionsTest. */
	public ReadVersionsTest() { super(NAME); }

    /**
	 * Test the document model listVersions api.
	 *
	 */
	public void testReadVersions() {
		try {
			Collection<DocumentVersion> versions;
			for(Fixture datum : data) {
				versions =
					datum.documentModel.readVersions(datum.document.getId());

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
					datum.documentModel.readVersions(datum.document.getId(), descendingVersionId);

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
        final Integer VERSION_COUNT = 3;
		final DocumentModel documentModel = getDocumentModel(OpheliaTestUser.JUNIT);

        data = new Vector<Fixture>(getInputFilesLength());
		for(final File testFile : getInputFiles()) {
			final Document document = createDocument(OpheliaTestUser.JUNIT, testFile);
            for(int i = 0; i < VERSION_COUNT; i++) {
                modifyDocument(OpheliaTestUser.JUNIT, document.getId());
                documentModel.createVersion(document.getId());
            }
			data.add(new Fixture(document, documentModel, VERSION_COUNT));
		}
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		data.clear();
		data = null;
		super.tearDown();
	}

	/** Test datum definition. */
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
}
