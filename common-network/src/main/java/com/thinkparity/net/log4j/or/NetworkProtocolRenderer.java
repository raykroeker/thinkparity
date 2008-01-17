/*
 * Created On:  29-Aug-07 2:20:21 PM
 */
package com.thinkparity.net.log4j.or;

import com.thinkparity.common.StringUtil;
import com.thinkparity.common.StringUtil.Separator;

import org.apache.log4j.or.ObjectRenderer;

import com.thinkparity.net.NetworkProtocol;


/**
 * <b>Title:</b>thinkParity Network Protocol Log4J Renderer<br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class NetworkProtocolRenderer implements ObjectRenderer {

    /**
     * Create NetworkProtocolRenderer.
     *
     */
    public NetworkProtocolRenderer() {
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
            final NetworkProtocol o2 = (NetworkProtocol) o;
            return StringUtil.toString(o2.getClass(),
                    "getName()", o2.getName());
        }
    }
}
