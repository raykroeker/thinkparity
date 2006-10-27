/*
 * Created On: Sun Oct 22 2006 11:00 PDT
 */
package com.thinkparity.desdemona.model.stream;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.codebase.model.stream.StreamSession;
import com.thinkparity.codebase.model.stream.StreamWriter;

import com.thinkparity.desdemona.DesdemonaTestUser;

public final class StreamWriterTest extends StreamTestCase {
    private static final String NAME = "Stream Writer Test";
    private Fixture datum;
    public StreamWriterTest() {
        super(NAME);
    }
    public void testWriter() {
        final StreamWriter writer = new StreamWriter(datum.streamSession);
        try {
            writer.open();
            writer.write(datum.streamId, datum.stream);
        } finally {
            writer.close();
        }
    }
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        final File workingDirectory = new File(getOutputDirectory(), "working");
        Assert.assertTrue(workingDirectory.mkdir(), "Could not create directory {0}.", workingDirectory);
        final File streamFile = getInputFiles()[3];
        final StreamServer server = startStreamServer(DesdemonaTestUser.JUNIT, workingDirectory);
        final StreamSession session = createSession(DesdemonaTestUser.JUNIT, server);
        final String streamId = createStream(server, session, streamFile.getName());
        final InputStream stream = new FileInputStream(streamFile);
        datum = new Fixture(stream, streamId,  session);
    }
    @Override
    protected void tearDown() throws Exception {
        datum = null;
        super.tearDown();
    }
    private final class Fixture extends StreamTestCase.Fixture {
        private final InputStream stream;
        private final String streamId;
        private final StreamSession streamSession;
        private Fixture(final InputStream stream, final String streamId,
                final StreamSession streamSession) {
            super();
            this.stream = stream;
            this.streamId = streamId;
            this.streamSession = streamSession;
        }
    }
}
