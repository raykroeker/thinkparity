/*
 * Created On:  Apr 8, 2006
 * $Id$
 */
package com.thinkparity.ophelia.model.util.smackx.packet.artifact;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.ophelia.model.util.smackx.packet.IQArtifact;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class IQConfirmArtifactReceipt extends IQArtifact {

    /** The artifact version id. */
    private Long artifactVersionId;

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
     * @return Returns the artifactVersionId.
     */
    public Long getArtifactVersionId() {
        return artifactVersionId;
    }

    /**
     * @see com.thinkparity.ophelia.model.util.smackx.packet.IQArtifact#getChildElementXML()
     * 
     */
    public String getChildElementXML() {
        return new StringBuffer(startQueryXML())
            .append(getArtifactUUIDXML())
            .append(getArtifactVersionIdXML())
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
     * @param artifactVersionId The artifactVersionId to set.
     */
    public void setArtifactVersionId(Long artifactVersionId) {
        this.artifactVersionId = artifactVersionId;
    }

    /**
     * @param recievedFrom The recievedFrom to set.
     */
    public void setRecievedFrom(JabberId recievedFrom) {
        this.recievedFrom = recievedFrom;
    }

    private String getArtifactVersionIdXML() {
        return new StringBuffer("<versionid>")
            .append(artifactVersionId)
            .append("</versionid>")
            .toString();
    }

    private String getReceivedFromXML() {
        return new StringBuffer("<jid>")
            .append(recievedFrom.getQualifiedJabberId())
            .append("</jid>")
            .toString();
    }
}
