/*
 * Nov 10, 2005
 */
package com.thinkparity.ophelia.model.document;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Vector;

import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;

import com.thinkparity.ophelia.OpheliaTestUser;

/**
 * Test the document model create api.
 * 
 * @author raymond@raykroeker.com
 * @version 1.1.2.2
 */
public class CreateTest extends DocumentTestCase {

    /** The test name. */
    private static final String NAME = "[CREATE TEST]";

    /** Test data. */
	private Vector<Fixture> data;

	/** Create a CreateTest. */
	public CreateTest() { super(NAME); }

	/**
	 * Test the document model create api.
	 * 
	 */
	public void testCreate() {
        Document document;
        List<DocumentVersion> versions;
	    for(final Fixture datum : data) {
            document = datum.documentModel.create(datum.name, datum.content);

            assertNotNull(NAME, document);
            versions = datum.documentModel.readVersions(document.getId());
            assertEquals(NAME + "[VERSIONS SHOULD NOT EXIST]", 0, versions.size());
        }
	}

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		final InternalDocumentModel documentModel = getDocumentModel(OpheliaTestUser.JUNIT);
		data = new Vector<Fixture>(4);

        InputStream content;
		for(final File inputFile : getInputFiles()) {
            content = new FileInputStream(inputFile);
            data.add(new Fixture(content, documentModel, inputFile.getName()));
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
	private class Fixture extends DocumentTestCase.Fixture {
		private final InputStream content;
		private final InternalDocumentModel documentModel;
		private final String name;
		private Fixture(final InputStream content,
                final InternalDocumentModel documentModel, final String name) {
		    this.content = content;
			this.documentModel = documentModel;
			this.name = name;
		}
	}
}
