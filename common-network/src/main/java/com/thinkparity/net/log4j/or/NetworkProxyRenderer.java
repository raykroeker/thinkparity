/*
 * Created On:  29-Aug-07 2:20:21 PM
 */
package com.thinkparity.net.log4j.or;

import com.thinkparity.common.StringUtil;
import com.thinkparity.common.StringUtil.Separator;


import org.apache.log4j.or.ObjectRenderer;

import com.thinkparity.net.NetworkAddress;
import com.thinkparity.net.NetworkProxy;

/**
 * <b>Title:</b>thinkParity Network Proxy Log4J Renderer<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class NetworkProxyRenderer implements ObjectRenderer {

    /** A network address renderer. */
    private final NetworkAddressRenderer addressRenderer;

    /**
     * Create NetworkProxyRenderer.
     *
     */
    public NetworkProxyRenderer() {
        super();
        this.addressRenderer = new NetworkAddressRenderer();
    }

    /**
     * @see org.apache.log4j.or.ObjectRenderer#doRender(java.lang.Object)
     *
     */
    @Override
    public String doRender(final Object o) {
        if(null == o) {
            return Separator.Null.toString();
        }
        else {
            final NetworkProxy o2 = (NetworkProxy) o;
            return StringUtil.toString(o2.getClass(),
                    "getAddress()", doRender(o2.getAddress()));
        }
    }

    /**
     * Render the network address.
     * 
     * @param address
     *            A <code>NetworkAddress</code>.
     * @return A <code>String</code>.
     */
    private String doRender(final NetworkAddress address) {
        return addressRenderer.doRender(address);
    }
}
