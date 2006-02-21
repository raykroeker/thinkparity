/*
 * Feb 20, 2006
 */
package com.thinkparity.model.parity.model.session;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import com.thinkparity.model.parity.model.ModelTestCase;
import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.document.DocumentModel;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class SendKeyRequestTest extends ModelTestCase {

	private List<Fixture> data;

	/**
	 * Create a SendKeyRequestTest.
	 * 
	 */
	public SendKeyRequestTest() { super("Send Key Request Test"); }

	public void testSendKeyRequest() {
		try {
			for(final Fixture datum : data) {
				datum.sessionModel.sendKeyRequest(datum.artifactId);
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
		login();
		data = new LinkedList<Fixture>();
		final DocumentModel documentModel = getDocumentModel();
		final SessionModel sessionModel = getSessionModel();

		Document d;
		for(final File file : getInputFiles()) {
			d = documentModel.create(file.getName(), file.getName(), file);
			data.add(new Fixture(d.getId(), sessionModel));
		}
	}

	/**
	 * @see com.thinkparity.model.parity.model.ModelTestCase#tearDown()
	 * 
	 */
	protected void tearDown() throws Exception {
		data.clear();
		data = null;
		logout();
		super.tearDown();
	}

	private class Fixture {
		private final Long artifactId;
		private final SessionModel sessionModel;
		private Fixture(final Long artifactId, final SessionModel sessionModel) {
			super();
			this.artifactId = artifactId;
			this.sessionModel = sessionModel;
		}
	}
}
