/*
 * Created On: 13-Oct-06 12:08:45 PM
 */
package com.thinkparity.ophelia.model.util.smack;

import com.thinkparity.ophelia.model.util.xmpp.XMPPCore;



/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class SmackErrorTranslator {

    private static final SmackErrorTranslator SINGLETON;

    static { SINGLETON = new SmackErrorTranslator(); }

    public static SmackException translate(final XMPPCore xmppCore,
            final String errorId, final Throwable t) {
        return SINGLETON.doTranslate(errorId, t);
    }

    /**
     * Create SmackErrorTranslator.
     *
     */
    private SmackErrorTranslator() {
        super();
    }

    private SmackException doTranslate(final String errorId, final Throwable t) {
        return new SmackException(errorId, t);
    }
}
