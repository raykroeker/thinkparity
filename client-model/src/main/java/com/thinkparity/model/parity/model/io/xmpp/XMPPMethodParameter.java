/*
 * Created On: Aug 25, 2006 2:20:05 PM
 */
package com.thinkparity.model.parity.model.io.xmpp;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class XMPPMethodParameter {

    final Class javaType;

    final Object javaValue;

    final String name;

    XMPPMethodParameter(final String name, final Class javaType,
            final Object javaValue) {
        this.name = name;
        this.javaType = javaType;
        this.javaValue = javaValue;
    }
}
