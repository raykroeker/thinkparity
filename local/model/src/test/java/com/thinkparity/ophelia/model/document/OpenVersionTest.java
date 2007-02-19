/*
 * Nov 18, 2005
 */
package com.thinkparity.ophelia.model.document;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.thinkparity.codebase.FileUtil;

import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;

import com.thinkparity.ophelia.model.util.MD5Util;
import com.thinkparity.ophelia.model.util.Opener;

import com.thinkparity.ophelia.OpheliaTestUser;

/**
 * Test the document model open version api.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1.2.1
 */
public class OpenVersionTest extends DocumentTestCase {

	/** Test name <code>String</code>. */
    private static final String NAME = "Open version test";

	/** Test datum <code>Fixture</code>. */
	private Fixture datum;

	/**
     * Create OpenVersionTest.
     *
     */
    public OpenVersionTest() {
        super(NAME);
    }

    /**
     * Test the document model open version api.
     *
     */
    public void testOpenVersion() {
        final Container c = createContainer(datum.junit, NAME);
        final Document d = addDocument(datum.junit, c.getId(), "JUnitTestFramework.doc");
        publish(datum.junit, c.getId(), "JUnit.X thinkParity");
        datum.waitForEvents();

        final DocumentVersion dv = getDocumentModel(datum.junit).readLatestVersion(d.getId());
        final File file = getOutputFile(d.getName(), Boolean.TRUE);
        try {
            try {
                // test open using the stream
                InputStream is = getDocumentModel(datum.junit).openVersion(dv.getArtifactId(), dv.getVersionId());
                try {
                    FileUtil.write(is, file);
                } finally {
                    is.close();
                }
                is = new FileInputStream(file);
                try {
                    final String checksum = MD5Util.md5Hex(is);
                    assertEquals("Open version checksum does not match expectation.", getInputFileMD5Checksum("JUnitTestFramework.doc"), checksum);
                } finally {
                    is.close();
                }
                // test open using the opener
                getDocumentModel(datum.junit).openVersion(dv.getArtifactId(), dv.getVersionId(), new Opener() {
                    public void open(final File file) {
                        try {
                            final InputStream is = new FileInputStream(file);
                            try {
                                final String checksum = MD5Util.md5Hex(is);
                                assertEquals("Open version checksum does not match expectation.", getInputFileMD5Checksum("JUnitTestFramework.doc"), checksum);
                            } finally {
                                is.close();
                            }
                        } catch (final IOException iox) {
                            fail(createFailMessage("Could not test open version.", iox));
                        }
                    }
                });
            } finally {
                if (!file.delete())
                    fail("Could not test open version.");
            }
        } catch (final IOException iox) {
            fail(createFailMessage("Could not test open version.", iox));
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
