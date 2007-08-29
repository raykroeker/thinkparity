/*
 * Created On:  29-Aug-07 2:20:21 PM
 */
package com.thinkparity.network.log4j.or;

import com.thinkparity.codebase.StringUtil;
import com.thinkparity.codebase.StringUtil.Separator;

import org.apache.log4j.or.ObjectRenderer;

import com.thinkparity.network.NetworkAddress;

/**
 * <b>Title:</b>thinkParity Network Address Log4J Renderer<br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class NetworkAddressRenderer implements ObjectRenderer {

    /**
     * Create NetworkLog4JRenderer.
     *
     */
    public NetworkAddressRenderer() {
        super();
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
            final NetworkAddress o2 = (NetworkAddress) o;
            return StringUtil.toString(o2.getClass(),
                    "getHost()", o2.getHost(),
                    "getPort()", o2.getPort());
        }
    }
}
