/*
 * Created On:  22-Jun-07 4:23:01 PM
 */
package com.thinkparity.amazon.s3.service.client.rest;

import java.io.IOException;
import java.io.InputStream;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface ResponseBodySerializer {

    /**
     * Serialize the response body.
     * 
     * @param stream
     *            An <code>InputStream</code>.
     */
    public void read(final InputStream stream) throws IOException;
}
