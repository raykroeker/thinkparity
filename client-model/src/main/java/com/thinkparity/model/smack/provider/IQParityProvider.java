/*
 * Dec 7, 2005
 */
package com.thinkparity.model.smack.provider;

import org.apache.log4j.Logger;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;

import org.xmlpull.v1.XmlPullParser;

/**
 * Parse parity iq request and convert into IQ objects.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class IQParityProvider implements IQProvider {

    /**
     * An apache logger.
     * 
     */
    protected final Logger logger;

    /**
	 * Create a IQParityProvider.
	 */
	public IQParityProvider() {
        super();
        this.logger = Logger.getLogger(getClass());
	}

	/**
	 * @see org.jivesoftware.smack.provider.IQProvider#parseIQ(org.xmlpull.v1.XmlPullParser)
	 */
	public IQ parseIQ(XmlPullParser parser) throws Exception {
		int eventType = parser.getEventType();
		while(XmlPullParser.END_DOCUMENT != eventType) {
			


			eventType = parser.getEventType();
		}
		return null;
	}
}
