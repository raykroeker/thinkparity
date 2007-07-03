/*
 * Created On:  19-Jun-07 8:35:12 PM
 */
package com.thinkparity.amazon.s3.service.client.rest.xml;

import java.io.Reader;

/**
 * <b>Title:</b>thinkParity Amazon S3 Client Rest Xml Parser<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface Parser<T extends Object> {

    /**
     * Parse a rest response.
     * 
     * @param reader
     *            A <code>Reader</code>.
     * @param result
     *            A <code>T</code>.
     * @throws Exception
     */
    public void parse(final Reader reader, final T result)
            throws ParseException;
}
