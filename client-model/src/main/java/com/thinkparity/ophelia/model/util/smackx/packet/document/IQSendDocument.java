/*
 * Apr 5, 2006
 */
package com.thinkparity.ophelia.model.util.smackx.packet.document;

import java.util.HashSet;
import java.util.Set;

import org.jivesoftware.smack.packet.IQ;

import com.thinkparity.codebase.jabber.JabberId;



/**
 * An xmpp internet query for sending a document to a set of sendTo.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class IQSendDocument extends IQDocument {

    /**
     * The sendTo to send to.
     * 
     */
    private final Set<JabberId> sendTo;

    /**
     * Create an IQSendDocument.
     * 
     */
    public IQSendDocument() {
        super(Action.DOCUMENTSEND);
        this.sendTo = new HashSet<JabberId>();
        setType(IQ.Type.SET);
    }

    /**
     * Add all sendTo in the set.
     * 
     * @param sendTo
     *            A set of sendTo.
     */
    public void addAllSendTo(final Set<JabberId> jabberIds) {
        this.sendTo.addAll(jabberIds);
    }

    /**
     * Add a jabber id.
     * 
     * @param jabberId
     *            The jabberId to send the document to.
     * @return <tt>true</tt> if this set did not already contain the specified
     *         element.
     */
    public boolean addSendTo(final JabberId jabberId) {
        return this.sendTo.add(jabberId);
    }

    /**
     * Clear all sendTo.
     *
     */
    public void clearSendTo() {
        this.sendTo.clear();
    }

    /**
     * @see com.thinkparity.ophelia.model.util.smackx.packet.IQParity#getChildElementXML()
     * 
     */
    public String getChildElementXML() {
        final String xml = new StringBuffer(startQueryXML())
            .append(getSendToXML())
            .append(getDocumentXML())
            .append(finishQueryXML())
            .toString();
        logger.debug(xml);
        return xml;
    }

    /**
     * Remove a jabber id.
     * 
     * @param jabberId
     *            The jabber idto remove.
     * @return true if the set contained the specified element.
     */
    public boolean removeSendTo(final JabberId jabberId) {
        return this.sendTo.remove(jabberId);
    }

    /**
     * Obtain the sendTo xml.
     * 
     * @return An xml string.
     */
    private String getSendToXML() {
        final StringBuffer xml = new StringBuffer("<jids>");
        for(final JabberId sendToId : sendTo) {
            xml.append("<jid>").append(sendToId.getQualifiedJabberId()).append("</jid>");
        }
        return xml.append("</jids>").toString();
    }
}
