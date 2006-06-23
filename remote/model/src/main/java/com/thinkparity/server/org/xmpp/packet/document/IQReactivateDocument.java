/*
 * Created On: Jun 22, 2006 6:23:22 PM
 * $Id$
 */
package com.thinkparity.server.org.xmpp.packet.document;

import org.xmpp.packet.IQ;

import com.thinkparity.server.org.dom4j.NamespaceName;
import com.thinkparity.server.org.xmpp.packet.IQParity.Action;

/**
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class IQReactivateDocument extends IQDocument {

    /** Create IQReactivateDocument. */
    public IQReactivateDocument() {
        super(Action.DOCUMENTREACTIVATE, NamespaceName.IQ_DOCUMENT_REACTIVATE);
        setType(IQ.Type.set);  
    }
}
