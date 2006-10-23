/*
 * Created On: Oct 22, 2006 3:45:30 PM
 */
package com.thinkparity.codebase.model.stream;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class StreamReader {

    /** The backing <code>InputStream</code>. */
    private InputStream input;

    /** The stream <code>Session</code>. */
    private final Session session;

    /**
     * Create StreamReader.
     * 
     * @param session
     *            A stream <code>Session</code>.
     */
    public StreamReader(final Session session) {
        super();
        this.session = session;
    }

    public void open() {}

    public void read(final String streamId, final OutputStream output) {}

    public void close() {}
}
