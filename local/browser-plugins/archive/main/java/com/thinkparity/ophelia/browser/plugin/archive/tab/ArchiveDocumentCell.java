/*
 * Created On: Sep 1, 2006 10:59:14 AM
 */
package com.thinkparity.ophelia.browser.plugin.archive.tab;

import com.thinkparity.codebase.model.document.Document;

import com.thinkparity.ophelia.browser.Constants.InsetFactors;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
final class ArchiveDocumentCell extends ArchiveArtifactCell {

    /**
     * Create ArchiveDocumentCell.
     * 
     */
    ArchiveDocumentCell() {
        super();
    }

    /**
     * Obtain the artifact
     *
     * @return The Artifact.
     */
    Document getDocument() {
        return (Document) getArtifact();
    }

    /**
     * Set artifact.
     *
     * @param artifact The Artifact.
     */
    void setDocument(final Document document) {
        setArtifact(document);
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.DefaultTabCell#getTextInsetFactor()
     */
    @Override
    public Float getTextInsetFactor() {
        return InsetFactors.LEVEL_2;
    }
}
