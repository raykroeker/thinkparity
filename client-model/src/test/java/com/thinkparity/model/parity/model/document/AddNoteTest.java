/*
 * Nov 10, 2005
 */
package com.thinkparity.model.parity.model.document;

import java.util.Iterator;
import java.util.UUID;
import java.util.Vector;

import com.thinkparity.codebase.StringUtil.Separator;

import com.thinkparity.model.JUnitTestFile;
import com.thinkparity.model.ModelTestCase;
import com.thinkparity.model.parity.model.note.Note;
import com.thinkparity.model.parity.model.project.Project;

/**
 * Test the document model addNote api.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1.2.2
 */
public class AddNoteTest extends ModelTestCase {

	/**
	 * Test data fixture.
	 * 
	 * @see AddNoteTest#setUp()
	 * @see AddNoteTest#tearDown()
	 */
	private class Fixture {
		private final UUID documentId;
		private final DocumentModel documentModel;
		private final String note;
		private Fixture(final UUID documentId,
				final DocumentModel documentModel, final String note) {
			this.documentId = documentId;
			this.documentModel = documentModel;
			this.note = note;
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
			Document document;
			Note note;
			for(Fixture datum : data) {
				datum.documentModel.addNote(datum.documentId, datum.note);

				document = datum.documentModel.get(datum.documentId);
				note = document.getNotes().iterator().next();
				assertNotNull(note);
				assertEquals(note.getNote(), datum.note);
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
		final Iterator<JUnitTestFile> testFileIterator =
			getJUnitTestFiles().iterator();

		String note;
		note = "Simple note.";

		JUnitTestFile testFile = testFileIterator.next();
		String name = testFile.getName();
		String description = name;
		Document testDocument =
			documentModel.create(testProject.getId(), name, description, testFile.getFile());
		data.add(new Fixture(testDocument.getId(), documentModel, note));

		note = "More complex note.  Contains some windows line feed characters." +
			Separator.WindowsNewLine + " <-  Windows new line." +
			Separator.NixNewLine + " <- Unix new line." +
			Separator.SystemNewLine + " <- System new line.";
		testFile = testFileIterator.next();
		name = testFile.getName();
		description = name;
		testDocument =
			documentModel.create(testProject.getId(), name, description, testFile.getFile());
		data.add(new Fixture(testDocument.getId(), documentModel, note));

		note = new StringBuffer("I Am the Very Model of a Modern Major-General")
			.append(Separator.SystemNewLine)
			.append(Separator.SystemNewLine).append("I am the very model of a modern Major-General,")
			.append(Separator.SystemNewLine).append("I've information vegetable, animal, and mineral,")
			.append(Separator.SystemNewLine).append("I know the kings of England, and I quote the fights historical,")
			.append(Separator.SystemNewLine).append("From Marathon to Waterloo, in order categorical;")
			.append(Separator.SystemNewLine).append("I'm very well acquainted too with matters mathematical,")
			.append(Separator.SystemNewLine).append("I understand equations, both the simple and quadratical,")
			.append(Separator.SystemNewLine).append("About binomial theorem I'm teeming with a lot o' news---")
			.append(Separator.SystemNewLine).append("With many cheerful facts about the square of the hypotenuse.")
			.append(Separator.SystemNewLine)
			.append(Separator.SystemNewLine).append("I'm very good at integral and differential calculus,")
			.append(Separator.SystemNewLine).append("I know the scientific names of beings animalculous;")
			.append(Separator.SystemNewLine).append("In short, in matters vegetable, animal, and mineral,")
			.append(Separator.SystemNewLine).append("I am the very model of a modern Major-General.")
			.append(Separator.SystemNewLine)
			.append(Separator.SystemNewLine).append("I know our mythic history, King Arthur's and Sir Caradoc's,")
			.append(Separator.SystemNewLine).append("I answer hard acrostics, I've a pretty taste for paradox,")
			.append(Separator.SystemNewLine).append("I quote in elegiacs all the crimes of Heliogabalus,")
			.append(Separator.SystemNewLine).append("In conics I can floor peculiarities parablous.")
			.append(Separator.SystemNewLine).append("I can tell undoubted Raphaels from Gerard Dows and Zoffanies,")
			.append(Separator.SystemNewLine).append("I know the croaking chorus from the Frogs of Aristophanes,")
			.append(Separator.SystemNewLine).append("Then I can hum a fugue of which I've heard the music's din afore,")
			.append(Separator.SystemNewLine).append("And whistle all the airs from that infernal nonsense Pinafore.")
			.append(Separator.SystemNewLine)
			.append(Separator.SystemNewLine).append("Then I can write a washing bill in Balylonic cuneiform,")
			.append(Separator.SystemNewLine).append("And tell you every detail of Caractacus's uniform;")
			.append(Separator.SystemNewLine).append("In short, in matters vegetable, animal, and mineral,")
			.append(Separator.SystemNewLine).append("I am the very model of a modern Major-General.")
			.append(Separator.SystemNewLine)
			.append(Separator.SystemNewLine).append("In fact, when I know what is meant by \"mamelon\" and \"ravelin\",")
			.append(Separator.SystemNewLine).append("When I can tell at sight a chassepôt rifle from a javelin,")
			.append(Separator.SystemNewLine).append("When such affairs as sorties and surprises I'm more wary at,")
			.append(Separator.SystemNewLine).append("And when I know precisely what is meant by \"commissariat\",")
			.append(Separator.SystemNewLine).append("When I have learnt what progress has been made in modern gunnery,")
			.append(Separator.SystemNewLine).append("When I know more of tactics than a novice in a nunnery:")
			.append(Separator.SystemNewLine).append("In short, when I've a smattering of elemental strategy,")
			.append(Separator.SystemNewLine).append("You'll say a better Major-General has never sat a gee---")
			.append(Separator.SystemNewLine)
			.append(Separator.SystemNewLine).append("For my military knowledge, though I'm plucky and adventury,")
			.append(Separator.SystemNewLine).append("Has only been brought down to the beginning of the century;")
			.append(Separator.SystemNewLine).append("But still in matters vegetable, animal, and mineral,")
			.append(Separator.SystemNewLine).append("I am the very model of a modern Major-General.").toString();
		testFile = testFileIterator.next();
		name = testFile.getName();
		description = name;
		testDocument =
			documentModel.create(testProject.getId(), name, description, testFile.getFile());
		data.add(new Fixture(testDocument.getId(), documentModel, note));
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		data.clear();
		data = null;
	}
}
