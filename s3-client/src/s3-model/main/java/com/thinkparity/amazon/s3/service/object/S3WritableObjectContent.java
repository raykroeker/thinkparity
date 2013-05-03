/*
 * Created On:  19-Jun-07 4:07:55 PM
 */
package com.thinkparity.amazon.s3.service.object;

import java.io.IOException;
import java.nio.channels.WritableByteChannel;

/**
 * <b>Title:</b>thinkParity Amazon S3 Writable Object Content<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface S3WritableObjectContent {

    /**
     * Open a channel to write the object content.
     * 
     * @return A <code>WritableByteChannel</code>.
     */
    public WritableByteChannel openWriteChannel() throws IOException;
}
