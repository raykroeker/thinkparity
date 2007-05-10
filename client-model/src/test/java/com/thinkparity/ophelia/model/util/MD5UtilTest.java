/*
 * Created On:  25-Dec-06 3:52:04 PM
 */
package com.thinkparity.ophelia.model.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

import com.thinkparity.codebase.model.util.codec.MD5Util;

import com.thinkparity.ophelia.model.ModelTestCase;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class MD5UtilTest extends ModelTestCase {

    private static final String NAME = "MD5 util test.";

    private Fixture datum;

    /**
     * Create MD5UtilTest.
     * 
     */
    public MD5UtilTest() {
        super(NAME);
    }

    /**
     * Test the md5 hex channel api.
     *
     */
    public void testMD5HexChannel() {
        String md5Checksum;
        FileChannel fileChannel;
        for (int i = 0; i < datum.inputFiles.length; i++) {
            try {
                fileChannel = new RandomAccessFile(datum.inputFiles[i], "r").getChannel();
                try {
                    synchronized (getBufferLock()) {
                        md5Checksum = MD5Util.md5Hex(fileChannel, getBufferArray());
                    }
                } catch (final Throwable t) {
                    md5Checksum = null;
                } finally {
                    fileChannel.close();
                }
                assertEquals(md5Checksum, datum.inputFileMD5Checksums[i]);
            } catch (final IOException iox) {
                fail(createFailMessage(iox));
            }
        }
    }

    /**
     * Test the md5 hex stream api.
     *
     */
    public void testMD5HexStream() {
        String md5Checksum;
        InputStream inputFileStream;
        for (int i = 0; i < datum.inputFiles.length; i++) {
            try {
                inputFileStream = new FileInputStream(datum.inputFiles[i]);
                try {
                    synchronized (getBufferLock()) {
                        md5Checksum = MD5Util.md5Hex(inputFileStream, getBufferArray());
                    }
                } catch (final Throwable t) {
                    md5Checksum = null;
                } finally {
                    inputFileStream.close();
                }
                assertEquals(md5Checksum, datum.inputFileMD5Checksums[i]);
            } catch (final IOException iox) {
                fail(createFailMessage(iox));
            }
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.ModelTestCase#setUp()
     *
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        datum = new Fixture(getInputFileMD5Checksums(), getInputFiles());
    }

    /**
     * @see com.thinkparity.ophelia.model.ModelTestCase#tearDown()
     *
     */
    @Override
    protected void tearDown() throws Exception {
        datum = null;
        super.tearDown();
    }

    private class Fixture extends ModelTestCase.Fixture {
        private final String[] inputFileMD5Checksums;
        private final File[] inputFiles;
        private Fixture(final String[] inputFileMD5Checksums,
                final File[] inputFiles) {
            this.inputFileMD5Checksums = inputFileMD5Checksums;
            this.inputFiles = inputFiles;
        }
    }
}
