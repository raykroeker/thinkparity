/*
 * Created On:  21-Jun-07 9:35:01 PM
 */
package com.thinkparity.amazon.s3.service.client.rest.xml;

import java.io.IOException;
import java.io.Writer;

/**
 * <b>Title:</b>thinkParity Amazon S3 Service Rest Xml Writer<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface Printer {

    /**
     * Write the object to the stream.
     * 
     * @param writer
     *            A <code>Writer</code>.
     * @throws IOException
     */
    public void write(final Writer writer) throws IOException;
}
