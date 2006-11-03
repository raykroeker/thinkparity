/*
 * Created On: Sun Oct 22 2006 10:58 PDT
 */
package com.thinkparity.desdemona.model.stream;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.Charset;

import com.thinkparity.codebase.JVMUniqueId;
import com.thinkparity.codebase.NetworkUtil;
import com.thinkparity.codebase.StreamUtil;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.stream.StreamSession;

import com.thinkparity.desdemona.DesdemonaTestUser;
import com.thinkparity.desdemona.model.ModelTestCase;
import com.thinkparity.desdemona.util.MD5Util;

abstract class StreamTestCase extends ModelTestCase {
    protected StreamTestCase(final String name) {
        super(name);
    }
    protected StreamSession createSession(final DesdemonaTestUser testUser,
            final StreamServer server) throws UnknownHostException {
        final ServerSession session = new ServerSession();
        session.setBufferSize(1024);
        session.setCharset(Charset.forName("ISO-8859-1"));
        session.setEnvironment(testUser.getEnvironment());
        session.setId(createSessionId(testUser.getId()));
        session.setInetAddress(InetAddress.getByName(NetworkUtil.getMachine()));
        server.initialize(session);
        return session;
    }
    protected String createSessionId(final JabberId userId) {
        /*
         * NOTE A stream session id is unique per user per timestamp
         */
        final String hashString = new StringBuffer(userId.toString())
                .append(System.currentTimeMillis())
                .toString();
        return MD5Util.md5Hex(hashString.getBytes());
    }
    protected String createStream(final StreamServer server,
            final StreamSession session, final String streamIdSeed) {
        final String streamId = JVMUniqueId.nextId().getId() + "-" + streamIdSeed;
        server.initialize(session, streamId);
        return streamId;
    }
    protected StreamServer createStreamServer(final DesdemonaTestUser testUser,
            final File workingDirectory) {
        return new StreamServer(workingDirectory, testUser.getEnvironment());
    }
    protected StreamServer createSecureStreamServer(
            final DesdemonaTestUser testUser, final File workingDirectory) {
        return new StreamServer(workingDirectory, testUser.getEnvironment());
    }
    protected void seedServer(final StreamServer server,
            final StreamSession session, final String streamId, final File file)
            throws IOException {
        final OutputStream output = server.openOutputStream(session, streamId);
        try {
            StreamUtil.copy(new FileInputStream(file), output);
        } finally {
            output.close();
        }
    }
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }
    protected StreamServer startStreamServer(final DesdemonaTestUser testUser,
            final File workingDirectory) throws IOException {
        final StreamServer streamServer = createStreamServer(testUser, workingDirectory);
        streamServer.start();
        return streamServer;
    }
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    protected abstract class Fixture {
        protected Fixture() {
            super();
        }
    }
}
