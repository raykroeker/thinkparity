/*
 * Created On: Sun Oct 22 2006 11:00 PDT
 */
package com.thinkparity.desdemona.model.stream;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.codebase.model.stream.StreamReader;
import com.thinkparity.codebase.model.stream.StreamSession;

import com.thinkparity.desdemona.DesdemonaTestUser;

public final class StreamReaderTest extends StreamTestCase {
    private static final String NAME = "Stream Reader Test";
    private Fixture datum;
    public StreamReaderTest() {
        super(NAME);
    }
    public void testReader() {
        final StreamReader reader = new StreamReader(datum.streamSession);
        try {
            reader.open();
            reader.read(datum.streamId, datum.stream);
        } catch (final IOException iox) {
            fail(createFailMessage(iox));
        } finally {
            try {
                datum.stream.close();
                reader.close();
            } catch (final IOException iox2) {
                fail(createFailMessage(iox2));
            }
        }
    }
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        final File workingDirectory = new File(getOutputDirectory(), "working");
        Assert.assertTrue(workingDirectory.mkdir(), "Could not create directory {0}.", workingDirectory);
        final File streamFile = getInputFiles()[0];
        final StreamServer server = startStreamServer(DesdemonaTestUser.JUNIT, workingDirectory);
        final StreamSession session = createSession(DesdemonaTestUser.JUNIT, server);
        final String streamId = createStream(server, session, streamFile.getName());
        final OutputStream stream = new FileOutputStream(new File(getOutputDirectory(), streamFile.getName()));
        seedServer(server, session, streamId, 0L, streamFile);
        datum = new Fixture(server, stream, streamId, session);
    }
    @Override
    protected void tearDown() throws Exception {
        datum.streamServer.stop(Boolean.TRUE);
        datum = null;
        super.tearDown();
    }
    private final class Fixture extends StreamTestCase.Fixture {
        private final OutputStream stream;
        private final String streamId;
        private final StreamServer streamServer;
        private final StreamSession streamSession;
        private Fixture(final StreamServer streamServer,
                final OutputStream stream, final String streamId,
                final StreamSession streamSession) {
            super();
            this.streamServer = streamServer;
            this.stream = stream;
            this.streamId = streamId;
            this.streamSession = streamSession;
        }
    }
}
