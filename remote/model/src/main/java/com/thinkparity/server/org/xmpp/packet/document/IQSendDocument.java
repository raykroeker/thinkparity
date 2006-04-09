/*
 * Apr 5, 2006
 */
package com.thinkparity.server.org.xmpp.packet.document;

import org.xmpp.packet.IQ;

import com.thinkparity.server.org.dom4j.NamespaceName;
import com.thinkparity.server.org.xmpp.packet.IQParity.Action;

/**
 * The xmpp internet query to send a document to a user.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class IQSendDocument extends IQDocument {

    /**
     * Create a IQSendDocument.
     * 
     */
    public IQSendDocument() {
        super(Action.DOCUMENTSEND, NamespaceName.IQ_DOCUMENT_SEND);
        setType(IQ.Type.set);
    }
}
