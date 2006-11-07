/*
 * Created On: Dec 1, 2005
 * $Id$
 */
package com.thinkparity.codebase.xmpp;

import org.apache.log4j.or.ObjectRenderer;
import org.xmpp.packet.IQ;

/**
 * A log4j renderer that knows how to display jive wildfire's iq objects.
 * 
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public final class IQRenderer implements ObjectRenderer {

    /** Create IQRenderer. */
	public IQRenderer() { super(); }

	/**
     * @see org.apache.log4j.or.ObjectRenderer#doRender(java.lang.Object)
     * 
     */
	public String doRender(Object o) {
		if (null == o) {
			return "null";
		} else {
            return ((IQ) o).toXML();
        }

	}
}
