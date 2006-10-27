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
        final StreamServer streamServer = startStreamServer(DesdemonaTestUser.JUNIT, workingDirectory);
        final InputStream stream = new FileInputStream(getInputFiles()[3]);
        final String streamId = System.currentTimeMillis() + getInputFiles()[3].getName();
        datum = new Fixture(stream, streamId, createStreamSession(
                DesdemonaTestUser.JUNIT, streamServer));
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
