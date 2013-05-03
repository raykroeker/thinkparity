/*
 * Created On:  22-Jun-07 3:06:46 PM
 */
package com.thinkparity.amazon.s3.service.client.rest.http;

import org.apache.commons.httpclient.Header;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface HeaderReader<T extends Object> {

    /**
     * Read the headers into the result.
     * 
     * @param headers
     *            A <code>Header[]</code>.
     * @param result
     *            A <code>T</code>.
     */
    public void read(final Header[] headers, final T result);
}
