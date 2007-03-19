/*
 * Created On:  22-Feb-07 10:59:54 PM
 */
package com.thinkparity.codebase.model.stream;

import java.io.IOException;
import java.io.InputStream;

/**
 * <b>Title:</b>thinkParity Stream Opener<br>
 * <b>Description:</b>A simple interface used to delegate responsibility from
 * the stream provider to the stream upload implementaiton.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface StreamOpener {

    /**
     * Open an input stream.
     * 
     * @param stream
     *            An <code>InputStream</code>.
     */
    public void open(final InputStream stream) throws IOException;
}
