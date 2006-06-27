/*
 * Created On: Jun 27, 2006 2:29:18 PM
 * $Id$
 */
package com.thinkparity.codebase;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

/**
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class StreamUtilTest extends CodebaseTestCase {

    private static final String NAME = "[CODEBASE] [TEST STREAM UTIL]";

    private List<Fixture> data;

    /** Create StreamUtilTest. */
    public StreamUtilTest() { super(NAME); }

    public void testRead() {
        for(final Fixture datum : data) {
            byte[] bytes = null;
            try { bytes = StreamUtil.read(datum.inputStream); }
            catch(final IOException iox) { fail(createFailMessage(iox)); }
    
            assertEquals(NAME, bytes, datum.eBytes);
        }
    }

    /**
     * @see junit.framework.TestCase#setUp()
     * 
     */
    protected void setUp() throws Exception {
        super.setUp();
        data = new LinkedList<Fixture>();

        for(final File file : getInputFiles()) {
            data.add(new Fixture(FileUtil.readBytes(file), new FileInputStream(file)));
        }
    }

    /**
     * @see junit.framework.TestCase#tearDown()
     * 
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    private class Fixture {
        private final byte[] eBytes;
        private final InputStream inputStream;
        private Fixture(final byte[] eBytes, final InputStream inputStream) {
            this.inputStream = inputStream;
            this.eBytes = eBytes;
        }
    }
}
