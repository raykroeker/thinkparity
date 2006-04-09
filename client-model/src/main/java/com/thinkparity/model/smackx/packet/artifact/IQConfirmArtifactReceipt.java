/*
 * Apr 8, 2006
 */
package com.thinkparity.model.smackx.packet.artifact;

import com.thinkparity.model.smackx.packet.IQArtifact;
import com.thinkparity.model.xmpp.JabberId;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class IQConfirmArtifactReceipt extends IQArtifact {

    private JabberId recievedFrom;

    /**
     * Create a IQConfirmArtifactReceipt.
     * 
     * @param receivedFrom
     *            From whom the artifact was recieved.
     * @param uniqueId
     *            The artifact unique id.
     */
    public IQConfirmArtifactReceipt() {
        super(Action.ARTIFACTCONFIRMRECEIPT, null);
    }

    /**
     * @see com.thinkparity.model.smackx.packet.IQArtifact#getChildElementXML()
     * 
     */
    public String getChildElementXML() {
        return new StringBuffer(startQueryXML())
            .append(getArtifactUUIDXML())
            .append(getReceivedFromXML())
            .append(finishQueryXML())
            .toString();
    }

    /**
     * @return Returns the recievedFrom.
     */
    public JabberId getRecievedFrom() {
        return recievedFrom;
    }

    /**
     * @param recievedFrom The recievedFrom to set.
     */
    public void setRecievedFrom(JabberId recievedFrom) {
        this.recievedFrom = recievedFrom;
    }

    private String getReceivedFromXML() {
        return new StringBuffer("<jid>")
            .append(recievedFrom.getQualifiedJabberId())
            .append("</jid>")
            .toString();
    }
}
