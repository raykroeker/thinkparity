/*
 * Created On:  19-Jun-07 4:07:55 PM
 */
package com.thinkparity.amazon.s3.service.object;

import java.io.IOException;
import java.nio.channels.ReadableByteChannel;

/**
 * <b>Title:</b>thinkParity Amazon S3 Readable Object Content<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface S3ReadableObjectContent {

    /**
     * Open a channel to read the object content.
     * 
     * @return A <code>ReadableByteChannel</code>.
     */
    public ReadableByteChannel openReadChannel() throws IOException;
}
