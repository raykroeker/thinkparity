/*
 * Apr 8, 2006
 */
package com.thinkparity.server.org.xmpp.packet.artifact;

import java.util.UUID;

import com.thinkparity.server.JabberId;
import com.thinkparity.server.model.artifact.ArtifactModel;
import com.thinkparity.server.org.dom4j.ElementBuilder;
import com.thinkparity.server.org.dom4j.ElementName;
import com.thinkparity.server.org.dom4j.NamespaceName;
import com.thinkparity.server.org.xmpp.packet.IQParity.Action;

/**
 * The confirmation receipt sent to the orginal sender.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 * 
 * @see ArtifactModel#confirmReceipt(UUID, JabberId)
 */
public class IQConfirmReceipt extends IQArtifact {

    /**
     * Create a IQConfirmReceipt.
     * 
     * @param uniqueId
     *            The artifact unique id.
     */
    public IQConfirmReceipt() {
        super(Action.ARTIFACTCONFIRMRECEIPT, NamespaceName.IQ_ARTIFACT_CONFIRM_RECEIPT);
    }

    /**
     * Set the confirmed by jabber id.
     * 
     * @param confirmedBy
     *            The confirmed by jabber id.
     */
    public void setConfirmedBy(final JabberId confirmedBy) {
        ElementBuilder.addElement(rootElement, ElementName.JID, confirmedBy);
    }
}
