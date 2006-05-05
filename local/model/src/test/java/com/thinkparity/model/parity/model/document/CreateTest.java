/*
 * Nov 10, 2005
 */
package com.thinkparity.model.parity.model.document;

import java.io.File;
import java.util.Collection;
import java.util.Set;
import java.util.Vector;

import com.thinkparity.codebase.FileUtil;

import com.thinkparity.model.parity.util.MD5Util;
import com.thinkparity.model.parity.model.artifact.ArtifactModel;
import com.thinkparity.model.xmpp.user.User;

/**
 * Test the document model create api.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1.2.2
 */
public class CreateTest extends DocumentTestCase {
	
	/**
	 * Test data.
	 */
	private Vector<Fixture> data;

	/**
	 * Create a CreateTest.
	 */
	public CreateTest() { super("testCreate"); }

	/**
	 * Test the document model create api.
	 * 
	 */
	public void testCreate() {
		try {
			Document document;
			DocumentContent content;
			Collection<DocumentVersion> versions;
            Set<User> team;
			for(Fixture datum : data) {
				document = datum.documentModel.create(datum.name,
						datum.description, datum.file);
				assertNotNull(document);

				content = datum.documentModel.getContent(document.getId());
				assertNotNull(content);
				assertEquals(content.getChecksum(), datum.documentContentChecksum);

				versions = datum.documentModel.listVersions(document.getId());
				assertNotNull(versions);
				assertEquals("Number of versions does not match expectation.", datum.expectedVersionsSize, versions.size());

                team = datum.aModel.readTeam(document.getId());
                assertNotNull("[LMODEL] [DOCUMENT] [CREATE TEST] [TEAM IS NULL]", team);
                assertEquals("[LMODEL] [DOCUMENT] [CREATE TEST] [UNEXPECTED TEAM SIZE]", datum.expectedTeamSize, team.size());
                assertTrue("[LMODEL] [DOCUMENT] [CREATE TEST] [UNEXPECTED TEAM COMPOSITION]", team.contains(getModelTestUser().getUser()));
			}
		}
		catch(final Throwable t) { fail(createFailMessage(t)); }
	}

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
        final ArtifactModel aModel = getArtifactModel();
		final DocumentModel documentModel = getDocumentModel();
		data = new Vector<Fixture>(4);
		String name, description;
		String documentContentChecksum;

		for(File testFile : getInputFiles()) {
			name = testFile.getName();
			description = name;
			documentContentChecksum = MD5Util.md5Hex(FileUtil.readBytes(testFile));
			data.add(new Fixture(aModel, description, testFile,
					documentContentChecksum, documentModel, 1, 1, name));
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

	/**
	 * Test data fixture.
	 * 
	 * @see CreateTest#setUp()
	 * @see CreateTest#tearDown()
	 */
	private class Fixture {
        private final ArtifactModel aModel;
		private final String description;
		private final String documentContentChecksum;
		private final DocumentModel documentModel;
        private final int expectedTeamSize;
		private final int expectedVersionsSize;
		private final File file;
		private final String name;
		private Fixture(final ArtifactModel aModel, final String description,
                final File file, final String documentContentChecksum,
                final DocumentModel documentModel, final int expectedTeamSize,
                final int expectedVersionsSize, final String name) {
            this.aModel = aModel;
			this.description = description;
			this.file = file;
			this.documentContentChecksum = documentContentChecksum;
			this.documentModel = documentModel;
            this.expectedTeamSize = expectedTeamSize;
			this.expectedVersionsSize = expectedVersionsSize;
			this.name = name;
		}
	}
}
