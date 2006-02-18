/*
 * Feb 17, 2006
 */
package com.thinkparity.model.parity.model.session;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import com.thinkparity.model.parity.model.ModelTestCase;
import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.document.DocumentModel;

/**
 * Test the get session keys api.  Note that this test might fail with a NPE
 * in the session model if the remote server database and the local datbase
 * are out of synch.
 * 
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class GetArtifactKeysTest extends ModelTestCase {

	private List<Fixture> data;

	/**
	 * Create a GetArtifactKeysTest.
	 * 
	 */
	public GetArtifactKeysTest() { super("Test Get Artifact Keys"); }

	public void testGetArtifactKeys() {
		try {
			List<Long> keys;
			for(final Fixture datum : data) {
				keys = datum.sessionModel.getArtifactKeys();

				assertNotNull("Artifact keys are null.", keys);
				for(final Long expectedKey : datum.expectedKeys) {
					assertTrue(
							"Artifact key list does not contain expected key:  <" + expectedKey + ">",
							keys.contains(expectedKey));
				}
			}
		}
		catch(final Throwable t) { fail(createFailMessage(t)); }
	}

	/**
	 * @see com.thinkparity.model.parity.model.ModelTestCase#setUp()
	 * 
	 */
	protected void setUp() throws Exception {
		super.setUp();
		final DocumentModel documentModel = getDocumentModel();
		final SessionModel sessionModel = getSessionModel();
		data = new LinkedList<Fixture>();
		final List<Long> expectedKeys = new LinkedList<Long>();

		login();
		Document d;
		for(final File file : getInputFiles()) {
			d = documentModel.create(file.getName(), file.getName(), file);
			expectedKeys.add(d.getId());
		}
		data.add(new Fixture(expectedKeys, sessionModel));
	}

	/**
	 * @see com.thinkparity.model.parity.model.ModelTestCase#tearDown()
	 * 
	 */
	protected void tearDown() throws Exception {
		data.clear();
		data = null;
		super.tearDown();
	}

	private class Fixture {
		private final List<Long> expectedKeys;
		private final SessionModel sessionModel;
		private Fixture(final List<Long> expectedKeys,
				final SessionModel sessionModel) {
			super();
			this.expectedKeys = expectedKeys;
			this.sessionModel = sessionModel;
		}
	}
}
