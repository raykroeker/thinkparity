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

    public void testRun() {
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
        final StreamServer streamServer = startStreamServer(DesdemonaTestUser.JUNIT, workingDirectory);
        final File streamFile = getInputFiles()[0];
        final StreamSession streamSession = createStreamSession(DesdemonaTestUser.JUNIT, streamServer);
        final String streamId = System.currentTimeMillis() + streamFile.getName();
        seedServer(streamServer, streamSession, streamId, streamFile);
        final OutputStream output = new FileOutputStream(new File(getOutputDirectory(), streamId));
        datum = new Fixture(new DownstreamHandler(streamServer, streamSession, streamId, output));
    }

    /**
     * @see com.thinkparity.desdemona.model.stream.StreamTestCase#tearDown()
     *
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    private final class Fixture extends StreamTestCase.Fixture {
        private final DownstreamHandler handler;
        private Fixture(final DownstreamHandler handler) {
            super();
            this.handler = handler;
        }
    }
}
