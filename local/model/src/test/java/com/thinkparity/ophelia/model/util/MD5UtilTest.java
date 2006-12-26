/*
 * Created On:  25-Dec-06 3:52:04 PM
 */
package com.thinkparity.ophelia.model.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

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
     * Test the md5Hex api.
     *
     */
    public void testMD5Hex() {
        String md5Checksum;
        InputStream inputFileStream;
        for (int i = 0; i < datum.inputFiles.length; i++) {
            try {
                inputFileStream = new FileInputStream(datum.inputFiles[i]);
                try {
                    md5Checksum = MD5Util.md5Hex(inputFileStream);
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
