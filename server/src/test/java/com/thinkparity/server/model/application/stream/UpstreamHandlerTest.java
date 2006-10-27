/*
 * Created On: Sun Oct 22 2006 11:00 PDT
 */
package com.thinkparity.desdemona.model.stream;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.codebase.model.stream.StreamHeader;
import com.thinkparity.codebase.model.stream.StreamSession;

import com.thinkparity.desdemona.DesdemonaTestUser;

public final class UpstreamHandlerTest extends StreamTestCase {
    private static final String NAME = "Server Stream Reader Test";
    private Fixture datum;
    private StreamServer streamServer;
    public UpstreamHandlerTest() {
        super(NAME);
    }
    public void testRun() {
        datum.handler.run();
    }
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        final File workingDirectory = new File(getOutputDirectory(), "working");
        Assert.assertTrue(workingDirectory.mkdir(), "Could not create directory {0}.", workingDirectory);

        streamServer = startStreamServer(DesdemonaTestUser.JUNIT, workingDirectory);

        final File streamFile = getInputFiles()[0];
        final StreamSession streamSession = createStreamSession(DesdemonaTestUser.JUNIT, streamServer);
        final String streamId = System.currentTimeMillis() + streamFile.getName();

        // a simple representation of the input stream the reader would see from
        // a socket input stream - will present the header first; then the
        // file content
        final InputStream stream = new InputStream() {
            final InputStream file = new FileInputStream(streamFile);
            final byte[] header = new StringBuffer()
                .append(new StreamHeader(StreamHeader.Type.SESSION_BEGIN, streamSession.getId()))
                .append(new StreamHeader(StreamHeader.Type.STREAM_BEGIN, streamId))
                .toString().getBytes(streamSession.getCharset().name());
            int headerIndex = 0;
            @Override
            public void close() throws IOException {
                file.close();
            }
            @Override
            public int read() throws IOException {
                while(headerIndex < header.length)
                    return header[headerIndex++];
                return file.read();
            }
        };

        datum = new Fixture(new UpstreamHandler(streamServer, streamSession, streamId, stream));
    }
    @Override
    protected void tearDown() throws Exception {
        streamServer.stop(Boolean.TRUE);
        streamServer = null;
        datum = null;
        super.tearDown();
    }
    private final class Fixture extends StreamTestCase.Fixture {
        private final UpstreamHandler handler;
        private Fixture(final UpstreamHandler handler) {
            super();
            this.handler = handler;
        }
    }
}
