/*
 * Created On: Sun Oct 22 2006 11:00 PDT
 */
package com.thinkparity.desdemona.model.stream;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.codebase.model.stream.StreamReader;
import com.thinkparity.codebase.model.stream.StreamSession;
import com.thinkparity.codebase.model.stream.StreamWriter;

import com.thinkparity.desdemona.DesdemonaTestUser;

public final class StreamTest extends StreamTestCase {

    /** Test name <code>String</code>. */
    private static final String NAME = "Stream reader test";

    /** Test datum <code>Fixture</code>. */
    private Fixture datum;

    /**
     * Create StreamReaderTest.
     *
     */
    public StreamTest() {
        super(NAME);
    }

    /**
     * Test the stream reader.
     *
     */
    public void test() {
        StreamSession downloadSession, uploadSession;
        String streamId;
        StreamReader reader;
        StreamWriter writer;
        File downloadFile;
        try {
            for (final File uploadFile : getInputFiles()) {
                try {
                    uploadSession = createSession(datum.junit, datum.streamServer);
                    writer = new StreamWriter(uploadSession);
                    streamId = createStream(datum.streamServer, uploadSession, uploadFile.getName());
                    upload(writer, streamId, uploadFile);
        
                    downloadSession = createSession(datum.junit_x, datum.streamServer);
                    reader = new StreamReader(downloadSession);
                    downloadFile = download(reader, streamId);
                    assertEquals("Upload file size does not match download file size.",
                            uploadFile.length(), downloadFile.length());
                    // verify content
                    final InputStream uploadStream = new FileInputStream(uploadFile);
                    try {
                        final InputStream downloadStream = new FileInputStream(downloadFile);
                        try {
                            assertEquals("Upload file does not match download file.",
                                    uploadStream, downloadStream);
                        } finally {
                            downloadStream.close();
                        }
                    } finally {
                        uploadStream.close();
                    }
                } catch (final IOException iox) {
                    fail("Could not upload/download file {0}.", uploadFile);
                }
                
            }
        } catch (final IOException iox) {
            fail("Could not test stream server file.");
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.stream.StreamTestCase#setUp()
     *
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        final File workingDirectory = new File(getOutputDirectory(), "StreamTest");
        Assert.assertTrue(workingDirectory.mkdir(), "Could not create directory {0}.", workingDirectory);
        final StreamServer server = startStreamServer(DesdemonaTestUser.JUNIT, workingDirectory);

        datum = new Fixture(server, DesdemonaTestUser.JUNIT, DesdemonaTestUser.JUNIT_X);
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

    private File download(final StreamReader reader, final String streamId)
            throws IOException {
        final File downloadFile = new File(getOutputDirectory(), streamId);
        final OutputStream stream = new FileOutputStream(downloadFile);
        try {
            reader.open();
            reader.read(streamId, stream, 0L);
            return downloadFile;
        } finally {
            try {
                stream.close();
            } finally {
                reader.close();
            }
        }
    }

    private void upload(final StreamWriter writer, final String streamId,
            final File file) throws IOException {
        final InputStream stream = new BufferedInputStream(
                new FileInputStream(file), getDefaultBufferSize());
        try {
            writer.open();
            writer.write(streamId, stream, file.length());
        } finally {
            try {
                stream.close();
            } finally {
                writer.close();
            }
        }
    }

    /**
     * <b>Title:</b>thinkParity Stream Reader Test Datum Definition<br>
     * <b>Description:</b><br>
     */
    private final class Fixture extends StreamTestCase.Fixture {
        private final DesdemonaTestUser junit;
        private final DesdemonaTestUser junit_x;
        private final StreamServer streamServer;
        private Fixture(final StreamServer streamServer,
                final DesdemonaTestUser junit, final DesdemonaTestUser junit_x) {
            super();
            this.streamServer = streamServer;
            this.junit = junit;
            this.junit_x = junit_x;
        }
    }
}
