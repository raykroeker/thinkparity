/*
 * Dec 1, 2005
 */
package com.thinkparity.migrator.util;

import java.util.List;

import org.apache.log4j.or.ObjectRenderer;
import org.xmpp.packet.IQ;

import com.thinkparity.migrator.Constants;

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

    private static final String PREFIX =
		"[RMIGRATOR] [MESSENGER IQ] [";

    private static final String TO = ",to:";

    /** Create IQRenderer. */
	public IQRenderer() { super(); }

	/**
	 * @see org.apache.log4j.or.ObjectRenderer#doRender(java.lang.Object)
	 */
	public String doRender(Object o) {
		if(null == o) {
			return new StringBuffer(PREFIX)
				.append(Constants.Log4J.NULL)
				.append(Constants.Log4J.SUFFIX).toString();
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
			return buffer.append(Constants.Log4J.SUFFIX).toString();
		}
	}
}
