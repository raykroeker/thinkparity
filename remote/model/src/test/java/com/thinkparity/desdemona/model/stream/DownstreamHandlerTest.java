/*
 * Created On:  27-Oct-06 9:12:51 AM
 */
package com.thinkparity.desdemona.model.stream;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.codebase.model.stream.StreamSession;

import com.thinkparity.desdemona.DesdemonaTestUser;


/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class DownstreamHandlerTest extends StreamTestCase {

    private static final String NAME = "Downstream Handler Test";

    private Fixture datum;

    /**
     * Create DownstreamHandlerTest.
     *
     * @param name
     */
    public DownstreamHandlerTest() {
        super(NAME);
    }

    public void testDownstreamHandler() {
        datum.handler.run();
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
        final File streamFile = getInputFiles()[0];
        final StreamServer server = startStreamServer(DesdemonaTestUser.JUNIT, workingDirectory);
        final StreamSession session = createSession(DesdemonaTestUser.JUNIT, server);
        final String streamId = createStream(server, session, streamFile.getName());
        seedServer(server, session, streamId, 0L, streamFile);
        final OutputStream output = new FileOutputStream(new File(getOutputDirectory(), streamId));
        datum = new Fixture(server, new DownstreamHandler(server, session, streamId,
                0L, streamFile.length(), output));
    }

    /**
     * @see com.thinkparity.desdemona.model.stream.StreamTestCase#tearDown()
     *
     */
    @Override
    protected void tearDown() throws Exception {
        datum.streamServer.stop(Boolean.TRUE);
        super.tearDown();
    }

    private final class Fixture extends StreamTestCase.Fixture {
        private final DownstreamHandler handler;
        private final StreamServer streamServer;
        private Fixture(final StreamServer streamServer,
                final DownstreamHandler handler) {
            super();
            this.streamServer = streamServer;
            this.handler = handler;
        }
    }
}
