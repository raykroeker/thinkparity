/*
 * Nov 10, 2005
 */
package com.thinkparity.model.parity.model.document;

import java.util.Vector;

import com.thinkparity.model.ModelTestCase;
import com.thinkparity.model.ModelTestFile;
import com.thinkparity.model.parity.model.note.Note;
import com.thinkparity.model.parity.model.project.Project;

/**
 * Test the document model addNote api.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class AddNoteTest extends ModelTestCase {

	/**
	 * Test data fixture.
	 * 
	 * @see AddNoteTest#setUp()
	 * @see AddNoteTest#tearDown()
	 */
	private class Fixture {
		private final String content;
		private final Document document;
		private final DocumentModel documentModel;
		private final String subject;
		private Fixture(final String content, final Document document,
				final DocumentModel documentModel, final String subject) {
			this.content = content;
			this.document = document;
			this.documentModel = documentModel;
			this.subject = subject;
		}
	}

	/**
	 * Test data.
	 */
	private Vector<Fixture> data;

	/**
	 * Create a AddNoteTest.
	 */
	public AddNoteTest() { super("testAddNote"); }

	/**
	 * Test the document model addNote api.
	 *
	 */
	public void testAddNote() {
		try {
			Note note;
			for(Fixture datum : data) {
				note = datum.documentModel.addNote(datum.document, datum.subject, datum.content);

				assertNotNull(note);
				assertEquals(note.getSubject(), datum.subject);
				assertEquals(note.getContent(), datum.content);
			}
		}
		catch(Throwable t) { fail(getFailMessage(t)); }
	}

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		data = new Vector<Fixture>(5);
		final Project testProject = createTestProject(getName());
		final DocumentModel documentModel = getDocumentModel();
		String name, description;
		Document document;
		String subject, content;

		for(ModelTestFile testFile : getJUnitTestFiles()) {
			name = testFile.getName();
			description = name;
			document = documentModel.create(testProject, name,
					description, testFile.getFile());
			subject = "New note.";
			content = subject;

			data.add(new Fixture(content, document, documentModel, subject));
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
