/*
 * Created On:  25-Jul-07 8:56:19 PM
 */
package com.thinkparity.codebase.bzip2;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;

import com.thinkparity.codebase.BytesFormat;
import com.thinkparity.codebase.CodebaseTestCase;
import com.thinkparity.codebase.StringUtil.Separator;

/**
 * <b>Title:</b>thinkParity Codebase BZip2 Compression Test<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class CompressionTest extends CodebaseTestCase {

    /** A byte format. */
    private static final BytesFormat BYTES_FORMAT = new BytesFormat();

    /** A test name. */
    private static final String NAME = "Compression test";

    /** Test datum. */
    private Fixture datum;

    /**
     * Create CompressionTest.
     *
     */
    public CompressionTest() {
        super(NAME);
    }

    /**
     * Test compressing/decompressing files.
     *
     */
    public void testCompression() {
        TEST_LOGGER.logTraceId();
        final CompressFile compressor = new CompressFile();
        File compressed;
        File inflated;
        long compressBegin, compressDuration;
        long inflateBegin, inflateDuration;
        for (final File file : datum.files) {
            TEST_LOGGER.logInfo("Compressing file {0}...", file.getName());
            compressed = new File(file.getParent(), System.currentTimeMillis() + file.getName());
            try {
                if (!compressed.createNewFile()) {
                    fail("Could not create new file {0}.", compressed);
                }
            } catch (final IOException iox) {
                fail(iox, "Could not create new file {0}.", compressed);
            }
            synchronized (getBufferLock()) {
                try {
                    compressBegin = System.currentTimeMillis();
                    compressor.compress(file, compressed, getBuffer());
                    compressDuration = System.currentTimeMillis() - compressBegin;
                    TEST_LOGGER.logInfo(
                            "File {0} compressed.{1}\tOriginal Size:{2}{1}\tCompressed Size:{3}{1}\tCompression:{4}{1}\tTime:{5}",
                            file.getName(), Separator.SystemNewLine,
                            BYTES_FORMAT.format(file.length()),
                            BYTES_FORMAT.format(compressed.length()),
                            calculateCompression(file.length(), compressed.length()),
                            compressDuration);
                } catch (final IOException iox) {
                    fail(iox, "Could not compress file {0}.", file);
                }
            }
            TEST_LOGGER.logInfo("Inflating file {0}...", compressed.getName());
            inflated = new File(file.getParent(), System.currentTimeMillis() + file.getName());
            try {
                if (!inflated.createNewFile()) {
                    fail("Could not create new file {0}.", inflated);
                }
            } catch (final IOException iox) {
                fail(iox, "Could not create new file {0}.", inflated);
            }
            synchronized (getBufferLock()) {
                final InflateFile inflator = new InflateFile(getBuffer());
                try {
                    inflateBegin = System.currentTimeMillis();
                    inflator.inflate(compressed, inflated);
                    inflateDuration = System.currentTimeMillis() - inflateBegin;
                    TEST_LOGGER.logInfo(
                            "File {0} inflated.{1}\tCompressed Size:{2}{1}\tInflated Size:{3}{1}\tCompression:{4}{1}\tTime:{5}",
                            compressed.getName(), Separator.SystemNewLine,
                            BYTES_FORMAT.format(compressed.length()),
                            BYTES_FORMAT.format(inflated.length()),
                            calculateCompression(inflated.length(), compressed.length()),
                            inflateDuration);
                } catch (final IOException iox) {
                    fail(iox, "Could not inflate file {0}.", file);
                }
            }
            try {
                assertEquals("File does not match compressed/inflated file.", file, inflated);
            } catch (final IOException iox) {
                fail(iox, "Could not determine file equivalence.");
            }
        }
        TEST_LOGGER.logTraceId();
    }

    /**
     * @see com.thinkparity.codebase.junitx.TestCase#setUp()
     *
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();

        datum = new Fixture(getInputFiles());
    }

    /**
     * @see com.thinkparity.codebase.junitx.TestCase#tearDown()
     *
     */
    @Override
    protected void tearDown() throws Exception {
        datum = null;

        super.tearDown();
    }

    /**
     * Calculate the compression ratio.
     * 
     * @param inflated
     *            The inflated number of bytes.
     * @param compressed
     *            The compressed number of bytes.
     * @return A compression percentage formatted as a string.
     */
    private String calculateCompression(final long inflated, final long compressed) {
        final float compression = ((float) inflated / (float) compressed) * 100;
        return MessageFormat.format("{0,number,0.00}%", compression);
    }

    private class Fixture {

        private File[] files;

        private Fixture(final File[] files) {
            super();
            this.files = files;
        }
    }
}
