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

/**
 * <b>Title:</b>thinkParity Multi Stream Writer Test<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class BrokenStreamWriterTest extends StreamTestCase {

    /** The test name <code>String</code>. */
    private static final String NAME = "Broken stream writer test.";

    /** The test datum <code>Fixture</code>. */
    private Fixture datum;

    /**
     * Create MultiStreamWriterTest.
     *
     */
    public BrokenStreamWriterTest() {
        super(NAME);
    }

    /**
     * Test broken upstream simultaneous writers.
     *
     */
    public void testBrokenWriter() {
        for (final Thread streamWriter : datum.streamWriters) {
            streamWriter.start();
        }
        synchronized (BrokenStreamWriterTest.this) {
            try {
                BrokenStreamWriterTest.this.wait();
            } catch (final InterruptedException ix) {
                fail(createFailMessage(ix));
            }
        }
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
        final File streamFile = getInputFiles()[8];
        final StreamServer server = startStreamServer(DesdemonaTestUser.JUNIT, workingDirectory);

        final int streamCount = 1;
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
                                    synchronized (BrokenStreamWriterTest.this) {
                                        BrokenStreamWriterTest.this.notifyAll();
                                    }
                                }
                            }
                        } catch (final Exception x) {
                            fail(createFailMessage(x));
                        }
                    }
                }, "Stream Writer Thread:  " + String.valueOf(i)));
        }
        
        datum = new Fixture(server, streamWriters);
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

    /**
     * <b>Title:</b>thinkParity Multi Stream Writer Test Fixture<br>
     * <b>Description:</b>Test datum fixture for the test.<br>
     * 
     */
    private final class Fixture extends StreamTestCase.Fixture {
        private final List<Thread> streamWriters;
        private Integer completedCount;
        private final StreamServer streamServer;
        private Fixture(final StreamServer streamServer,
                final List<Thread> streamWriters) {
            super();
            this.streamServer = streamServer;
            this.streamWriters = streamWriters;
            this.completedCount = 0;
        }
    }
}
