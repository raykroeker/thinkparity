/*
 * Created On:  22-Feb-07 10:59:54 PM
 */
package com.thinkparity.codebase.model.stream;

import java.io.IOException;
import java.io.InputStream;

/**
 * <b>Title:</b>thinkParity Stream Uploader<br>
 * <b>Description:</b>A simple interface used to delegate responsibility from
 * the stream provider to the stream upload implementaiton.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface StreamUploader {

    /**
     * Upload an input stream.
     * 
     * @param stream
     *            An <code>InputStream</code>.
     */
    public void upload(final InputStream stream) throws IOException;
}
