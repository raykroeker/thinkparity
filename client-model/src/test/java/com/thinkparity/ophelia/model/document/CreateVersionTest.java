/*
 * Nov 19, 2005
 */
package com.thinkparity.ophelia.model.document;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;

import com.thinkparity.codebase.StreamUtil;
import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.util.codec.MD5Util;

import com.thinkparity.ophelia.OpheliaTestUser;

/**
 * Test the document model create version api.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class CreateVersionTest extends DocumentTestCase {

    /** Test name. */
    private static final String NAME = "[TEST CREATE VERSION]";
    
	/** Test datum. */
	private Fixture datum;

	/**
	 * Create a CreateVersionTest.
	 */
	public CreateVersionTest() { super(NAME); }

	/**
	 * Test the document model create version api.
	 */
	public void testCreateVersion() {
	    final Container c = createContainer(datum.junit, NAME);
        final Document d = addDocument(datum.junit, c.getId(), "JUnitTestFramework.doc");

        final Calendar createdOn = currentDateTime();
        DocumentVersion dv = null;
        try {
            final InputStream dv_is = new FileInputStream(
                    getInputFile("JUnitTestFrameworkMod.doc"));
            try {
                 dv = getDocumentModel(datum.junit).createVersion(d.getId(), dv_is, getDefaultBufferSize(), createdOn);
            } finally {
                dv_is.close();
            }
        } catch (final IOException iox) {
            fail(iox, "Could not test create version.");
        }

        assertNotNull("Document version is null.", dv);
		assertEquals("Document version artifact id does not match expectation.", d.getId(), dv.getArtifactId());
		assertEquals("Document version artifact type does not match expectation.", d.getType(), dv.getArtifactType());
		assertEquals("Document version unique id does not match expectation.", d.getUniqueId(), dv.getArtifactUniqueId());
		assertEquals("Document version created by does not match expectation.", d.getCreatedBy(), dv.getCreatedBy());
		assertEquals("Document version name does not match expectation.", d.getName(), dv.getName());
		assertEquals("Document version version id does not match expectation.", d.getUpdatedBy(), dv.getUpdatedBy());
        assertEquals("Document version checksum does not match expectation.", getInputFileMD5Checksum("JUnitTestFramework.doc"), dv.getChecksum());

        final InputStream stream = getDocumentModel(datum.junit).openVersion(dv.getArtifactId(), dv.getVersionId());
        final File file = new File(getOutputDirectory(), d.getName());
        try {
            try {
                final OutputStream os = new FileOutputStream(file);
                try {
                    StreamUtil.copy(stream, os, getDefaultBufferSize());
                } finally {
                    os.close();
                }
    
                final InputStream is = new FileInputStream(file);
                try {
                    final String checksum = MD5Util.md5Hex(is, getDefaultBufferSize());
                    assertEquals("Open version calculated checksum does not match expectation.", dv.getChecksum(), checksum);
                } finally {
                    is.close();
                }
            } finally {
                Assert.assertTrue("Could not delete file {0}.", file.delete());
            }
        } catch (final IOException iox) {
            fail(iox, "Could not test create version.");
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.test.ticket.TicketTestCase#setUp()
     *
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        datum = new Fixture(OpheliaTestUser.JUNIT, OpheliaTestUser.JUNIT_X);
        login(datum.junit);
        login(datum.junit_x);
    }

    /**
     * @see com.thinkparity.ophelia.model.test.ticket.TicketTestCase#tearDown()
     *
     */
    @Override
    protected void tearDown() throws Exception {
        logout(datum.junit);
        logout(datum.junit_x);
        datum = null;
        super.tearDown();
    }

    /** Test datum fixture. */
    private class Fixture extends DocumentTestCase.Fixture {
        private final OpheliaTestUser junit;
        private final OpheliaTestUser junit_x;
        private Fixture(final OpheliaTestUser junit,
                final OpheliaTestUser junit_x) {
            this.junit = junit;
            this.junit_x = junit_x;
            addQueueHelper(junit);
            addQueueHelper(junit_x);
        }
    }
}
