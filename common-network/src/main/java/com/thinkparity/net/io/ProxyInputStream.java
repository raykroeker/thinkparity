/*
 * Created On:  19-Aug-07 2:39:26 PM
 */
package com.thinkparity.net.io;

import java.io.FilterInputStream;
import java.io.InputStream;

/**
 * <b>Title:</b>thinkParity Network Proxy Input Stream<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ProxyInputStream extends FilterInputStream {

    /**
     * Create ProxyInputStream.
     * 
     * @param proxy
     *            An <code>InputStream</code>.
     */
    public ProxyInputStream(final InputStream proxy) {
        super(proxy);
    }
}
