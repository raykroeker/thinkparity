/*
 * Created On:  21-Jun-07 9:15:50 PM
 */
package com.thinkparity.amazon.s3.service.client.rest;

import java.io.IOException;
import java.io.OutputStream;

/**
 * <b>Title:</b>thinkParity Amazon S3 Service Rest Client Content Writer<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface RestRequestContentSerializer {

    /**
     * Obtain the content length.
     * 
     * @return A content length <code>long</code>.
     */
    public long getContentLength();

    /**
     * Determine the content type.
     * 
     * @return A content type <code>String</code>.
     */
    public String getContentType();

    /**
     * Serialize the content.
     * 
     * @param stream
     *            An <code>OutputStream</code>.
     */
    public void write(final OutputStream stream) throws IOException;
}
