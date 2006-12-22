/*
 * Created On: Sun Oct 22 2006 11:00 PDT
 */
package com.thinkparity.desdemona.model.stream;

import java.io.File;
import java.net.InetAddress;
import java.nio.charset.Charset;

import com.thinkparity.codebase.NetworkUtil;
import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.desdemona.DesdemonaTestUser;

public final class StreamServerTest extends StreamTestCase {
    private static final String NAME = "Stream Server Test";
    private Fixture datum;
    public StreamServerTest() {
        super(NAME);
    }
    public void testServer() {
        datum.streamServer.start();
        
        final ServerSession session = new ServerSession();
        session.setBufferSize(1024);
        session.setCharset(Charset.forName("ISO-8859-1"));
        session.setEnvironment(datum.testUser.getEnvironment());
        session.setId(createSessionId(datum.testUser.getId()));
        session.setInetAddress(datum.inetAddress);

        datum.streamServer.initialize(session);

        datum.streamServer.stop(Boolean.TRUE);
    }
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        final File workingDirectory = new File(getOutputDirectory(), "working");
        Assert.assertTrue(workingDirectory.mkdir(), "Could not create directory {0}.", workingDirectory);
        final StreamServer streamServer = createStreamServer(DesdemonaTestUser.JUNIT, workingDirectory);
        datum = new Fixture(InetAddress.getByName(NetworkUtil.getMachine()),
                streamServer, DesdemonaTestUser.JUNIT);
    }
    @Override
    protected void tearDown() throws Exception {
        datum = null;
        super.tearDown();
    }
    private final class Fixture extends StreamTestCase.Fixture {
        private final InetAddress inetAddress;
        private final StreamServer streamServer;
        private final DesdemonaTestUser testUser;
        private Fixture(final InetAddress inetAddress,
                final StreamServer streamServer,
                final DesdemonaTestUser testUser) {
            super();
            this.inetAddress = inetAddress;
            this.streamServer = streamServer;
            this.testUser = testUser;
        }
    }
}
