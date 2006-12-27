/*
 * Created On: Sun Oct 22 2006 11:00 PDT
 */
package com.thinkparity.desdemona.model.stream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.codebase.model.stream.StreamReader;
import com.thinkparity.codebase.model.stream.StreamSession;

import com.thinkparity.ophelia.model.util.MD5Util;

import com.thinkparity.desdemona.DesdemonaTestUser;

public final class StreamReaderTest extends StreamTestCase {

    /** Test name <code>String</code>. */
    private static final String NAME = "Stream reader test";

    /** Test datum <code>Fixture</code>. */
    private Fixture datum;

    /**
     * Create StreamReaderTest.
     *
     */
    public StreamReaderTest() {
        super(NAME);
    }

    /**
     * Test the stream reader.
     *
     */
    public void testReader() {
        final StreamReader reader = new StreamReader(datum.streamSession);
        try {
            final OutputStream outputStream = new FileOutputStream(datum.outputFile);
            try {
                reader.open();
                reader.read(datum.streamId, outputStream, 0L);
            } catch (final IOException iox) {
                fail(createFailMessage(iox));
            } finally {
                try {
                    outputStream.close();
                    reader.close();
                } catch (final IOException iox2) {
                    fail(createFailMessage(iox2));
                }
            }
        } catch (final IOException iox) {
            fail(createFailMessage(iox));
        }

        
        // verify sizes
        assertEquals(datum.outputFile.length(), datum.inputFile.length());

        // verify stream content
        try {
            final InputStream downloadInputStream = new FileInputStream(datum.outputFile);
            String downloadChecksum;
            try {
                downloadChecksum = MD5Util.md5Hex(downloadInputStream);
            } catch (final Throwable t) {
                downloadChecksum = null;
            } finally {
                downloadInputStream.close();
            }

            final InputStream uploadInputStream = new FileInputStream(datum.inputFile);
            String uploadChecksum;
            try {
                uploadChecksum = MD5Util.md5Hex(uploadInputStream);
            } catch (final Throwable t) {
                uploadChecksum = null;
            } finally {
                uploadInputStream.close();
            }

            assertEquals(downloadChecksum, uploadChecksum);
        } catch (final IOException iox) {
            fail(createFailMessage(iox));
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.stream.StreamTestCase#setUp()
     *
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        final File workingDirectory = new File(getOutputDirectory(), "working");
        Assert.assertTrue(workingDirectory.mkdir(), "Could not create directory {0}.", workingDirectory);
        final File inputFile = getInputFiles()[0];
        final File outputFile = new File(getOutputDirectory(), inputFile.getName());

        final StreamServer server = startStreamServer(DesdemonaTestUser.JUNIT, workingDirectory);
        final StreamSession session = createSession(DesdemonaTestUser.JUNIT, server);

        final String streamId = createStream(server, session, inputFile.getName());

        seedServer(server, session, streamId, 0L, inputFile);
        datum = new Fixture(server, session, streamId, inputFile, outputFile);
    }

    /**
     * @see com.thinkparity.desdemona.model.stream.StreamTestCase#tearDown()
     *
     */
    @Override
    protected void tearDown() throws Exception {
        datum.streamServer.stop(Boolean.TRUE);
        datum = null;
        super.tearDown();
    }

    /**
     * <b>Title:</b>thinkParity Stream Reader Test Datum Definition<br>
     * <b>Description:</b><br>
     */
    private final class Fixture extends StreamTestCase.Fixture {
        private final String streamId;
        private final StreamServer streamServer;
        private final StreamSession streamSession;
        private final File inputFile, outputFile;
        private Fixture(final StreamServer streamServer,
                final StreamSession streamSession, final String streamId,
                final File inputFile, final File outputFile) {
            super();
            this.streamServer = streamServer;
            this.streamId = streamId;
            this.streamSession = streamSession;
            this.inputFile = inputFile;
            this.outputFile = outputFile;
        }
    }
}
