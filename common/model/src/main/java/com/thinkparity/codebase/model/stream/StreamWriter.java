/*
 * Created On: Oct 22, 2006 3:46:10 PM
 */
package com.thinkparity.codebase.model.stream;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class StreamWriter {

    private OutputStream output;

    /** A stream <code>Session</code>. */
    private final Session session;

    /**
     * Create StreamWriter.
     * 
     * @param session
     *            A stream <code>Session</code>.
     */
    public StreamWriter(final Session session) {
        super();
        this.session = session;
    }

    public void close() {
    }

    public void open() {
    }

    public void write(final String streamId, final InputStream input) {
        final Float completion = session.getCompletion(streamId);
        if (1.0F == completion) {
            return;
        } else {
            seek(input, calculatePosition(input, completion));
            write(input);
        }
    }

    private Long calculatePosition(final InputStream input, final Float completion) {
        return 0L;
    }

    private void seek(final InputStream input, final Long position) {
    }

    private void write(final InputStream input) {}
}
