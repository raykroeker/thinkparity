/*
 * Created On: Dec 1, 2005
 * $Id$
 */
package com.thinkparity.codebase.xmpp;

import java.util.List;

import org.apache.log4j.or.ObjectRenderer;

import org.xmpp.packet.IQ;

/**
 * A log4j renderer that knows how to display jive wildfire's iq objects.
 * 
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class IQRenderer implements ObjectRenderer {

	private static final String FROM = ",from:";
	private static final String ID = "id:";
	private static final String NAMES_0 = ",elementNames:";
	private static final String NAMES_1 = ",";
    private static final String NULL = "null";
    private static final String PREFIX = "[XMPP IQ] [";
    private static final String SUFFIX = "]";
    private static final String TO = ",to:";

    /** Create IQRenderer. */
	public IQRenderer() { super(); }

	/**
     * @see org.apache.log4j.or.ObjectRenderer#doRender(java.lang.Object)
     * 
     */
	public String doRender(Object o) {
		if(null == o) {
			return new StringBuffer(PREFIX)
				.append(NULL)
				.append(SUFFIX).toString();
		}
		else {
			final IQ iq = (IQ) o;
            final List<String> iqNames = new IQReader(iq).readNames();
			final StringBuffer buffer = new StringBuffer(PREFIX)
				.append(ID).append(iq.getID())
				.append(FROM).append(iq.getFrom())
				.append(TO).append(iq.getTo());
            final int iqNamesSize = iqNames.size();
            for(int i = 0; i < iqNamesSize; i++) {
                if(0 == i) { buffer.append(NAMES_0); }
                else { buffer.append(NAMES_1); }
                buffer.append(iqNames.get(i));
            }
			return buffer.append(SUFFIX).toString();
		}
	}

    /** A private iq reader for rendering a log4j statement. */
	private class IQReader extends com.thinkparity.codebase.xmpp.IQReader {
	    private IQReader(final IQ iq) { super(iq); }
    }
}
