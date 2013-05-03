/*
 * Created On:  15-May-07 2:22:02 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.artifact.PublishedToEMail;

/**
 * <b>Title:</b>thinkParity OpheliaUI Browser Published To View<br>
 * <b>Description:</b>Contains the container tab published to display
 * information.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class PublishedToView {

    /** A <code>List<ArtifactReceipt></code>. */
    private final List<ArtifactReceipt> artifactReceipts;

    /** A <code>List<PublishedToEMail></code>. */
    private final List<PublishedToEMail> emails;

    /**
     * Create PublishedToView.
     *
     */
    public PublishedToView() {
        super();
        this.artifactReceipts = new ArrayList<ArtifactReceipt>();
        this.emails = new ArrayList<PublishedToEMail>();
    }

    /**
     * Obtain artifact receipts.
     * 
     * @return A <code>List<ArtifactReceipt></code>.
     */
    public List<ArtifactReceipt> getArtifactReceipts() {
        return Collections.unmodifiableList(artifactReceipts);
    }

    /**
     * Obtain emails.
     *
     * @return A List<PublishedToEMail>.
     */
    public List<PublishedToEMail> getEMails() {
        return Collections.unmodifiableList(emails);
    }

    /**
     * Set artifact receipts.
     * 
     * @param artifactReceipts
     *            A <code>List<ArtifactReceipt></code>.
     */
    public void setArtifactReceipts(final List<ArtifactReceipt> artifactReceipts) {
        this.artifactReceipts.clear();
        this.artifactReceipts.addAll(artifactReceipts);
    }

    /**
     * Set emails.
     *
     * @param emails
     *		A List<PublishedToEMail>.
     */
    public void setEMails(final List<PublishedToEMail> emails) {
        this.emails.clear();
        this.emails.addAll(emails);
    }
}
