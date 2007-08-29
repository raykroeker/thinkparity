/*
 * Created On:  19-Aug-07 2:41:15 PM
 */
package com.thinkparity.network.io;

import java.io.FilterOutputStream;
import java.io.OutputStream;

/**
 * <b>Title:</b>thinkParity Network Proxy OutputStream<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ProxyOutputStream extends FilterOutputStream {

    /**
     * Create ProxyOutputStream.
     * 
     * @param proxy
     *            An <code>OutputStream</code>.
     */
    public ProxyOutputStream(final OutputStream proxy) {
        super(proxy);
    }
}
