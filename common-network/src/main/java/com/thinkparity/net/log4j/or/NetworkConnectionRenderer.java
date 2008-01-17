/*
 * Created On:  29-Aug-07 2:20:21 PM
 */
package com.thinkparity.net.log4j.or;

import com.thinkparity.common.StringUtil;
import com.thinkparity.common.StringUtil.Separator;

import org.apache.log4j.or.ObjectRenderer;

import com.thinkparity.net.NetworkAddress;
import com.thinkparity.net.NetworkConnection;


/**
 * <b>Title:</b>thinkParity Network Connection Log4J Renderer<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class NetworkConnectionRenderer implements ObjectRenderer {

    /** A network address renderer. */
    private final NetworkAddressRenderer addressRenderer;

    /**
     * Create NetworkConnectionRenderer.
     *
     */
    public NetworkConnectionRenderer() {
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
            final NetworkConnection o2 = (NetworkConnection) o;
            return StringUtil.toString(o2.getClass(),
                    "getAddress()", doRender(o2.getAddress()),
                    "getId()", o2.getId(),
                    "getSocket()", o2.getSocket());
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
