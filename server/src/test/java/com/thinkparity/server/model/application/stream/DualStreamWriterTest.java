/*
 * Created On: Sun Oct 22 2006 11:00 PDT
 */
package com.thinkparity.desdemona.model.stream;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.codebase.model.stream.StreamSession;
import com.thinkparity.codebase.model.stream.StreamWriter;

import com.thinkparity.desdemona.DesdemonaTestUser;

public final class DualStreamWriterTest extends StreamTestCase {
    private static final String NAME = "Stream Writer Test";
    private Fixture datum;
    public DualStreamWriterTest() {
        super(NAME);
    }
    public void testDualWriter() {
        for (final Thread streamWriter : datum.streamWriters) {
            streamWriter.start();
        }
        synchronized (DualStreamWriterTest.this) {
            try {
                DualStreamWriterTest.this.wait();
            } catch (final InterruptedException ix) {
                fail(createFailMessage(ix));
            }
        }
    }
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        final File workingDirectory = new File(getOutputDirectory(), "working");
        Assert.assertTrue(workingDirectory.mkdir(), "Could not create directory {0}.", workingDirectory);
        final File streamFile = getInputFiles()[8];
        final StreamServer server = startStreamServer(DesdemonaTestUser.JUNIT, workingDirectory);
        
        final int streamCount = 2;
        final List<Thread> streamWriters = new ArrayList<Thread>();
        for (int i = 0; i < streamCount; i++) {
            streamWriters.add(new Thread(new Runnable() {
                    public void run() {
                        try {
                            final StreamSession session = createSession(DesdemonaTestUser.JUNIT, server);
                            final StreamWriter streamWriter = new StreamWriter(session);
                            final InputStream stream = new FileInputStream(streamFile);
                            try {
                                streamWriter.open();
                                streamWriter.write(createStream(server, session,
                                    streamFile.getName()), stream, streamFile.length());
                            } finally {
                                stream.close();
                                streamWriter.close();
                                datum.completedCount++;
                                if (datum.completedCount == streamCount) {
                                    synchronized (DualStreamWriterTest.this) {
                                        DualStreamWriterTest.this.notifyAll();
                                    }
                                }
                            }
                        } catch (final Exception x) {
                            fail(createFailMessage(x));
                        }
                    }
                }, "Stream Writer Thread" + String.valueOf(i)));
        }
        datum = new Fixture(server, streamWriters);
    }
    @Override
    protected void tearDown() throws Exception {
        datum.streamServer.stop(Boolean.TRUE);
        datum = null;
        super.tearDown();
    }
    private final class Fixture extends StreamTestCase.Fixture {
        private Integer completedCount;
        private final StreamServer streamServer;
        private final List<Thread> streamWriters;
        private Fixture(final StreamServer streamServer,
                final List<Thread> streamWriters) {
            super();
            this.streamServer = streamServer;
            this.streamWriters = streamWriters;
            this.completedCount = 0;
        }
    }
}
