/*
 * Created On:  22-Jun-07 1:05:44 PM
 */
package com.thinkparity.amazon.s3.service.client.rest;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import com.thinkparity.amazon.s3.service.S3Context;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public abstract class XmlSerializer implements RestRequestContentSerializer {

    /**
     * Create a stream writer. Buffer the stream.
     * 
     * @param context
     *            A <code>S3Context</code>.
     * @param stream
     *            A <code>OutputStream</code>.
     * @return A <code>Writer</code>.
     * @throws IOException
     */
    protected static Writer newStreamWriter(final S3Context context,
            final OutputStream stream) throws IOException {
        return new BufferedWriter(new OutputStreamWriter(stream,
                context.getCharset()));
    }

    /**
     * Create XmlSerializer.
     *
     */
    public XmlSerializer() {
        super();
    }

    /**
     * @see com.thinkparity.amazon.s3.service.client.rest.RestRequestContentSerializer#getContentType()
     *
     */
    public String getContentType() {
        return "text/xml";
    }
}
