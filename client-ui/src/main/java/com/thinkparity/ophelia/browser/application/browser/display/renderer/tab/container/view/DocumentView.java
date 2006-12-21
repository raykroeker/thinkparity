/*
 * Created On:  20-Dec-06 9:31:40 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.view;

import java.util.Calendar;

import com.thinkparity.codebase.model.container.ContainerVersionArtifactVersionDelta.Delta;
import com.thinkparity.codebase.model.document.DocumentVersion;

/**
 * <b>Title:</b>thinkParity Container Tab Document View<br>
 * <b>Description:</b>The document view represents the data displayed for a
 * document for each container version within the container panel.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class DocumentView {

    /** The document version's <code>Delta</code>. */
    private Delta delta;

    /** The <code>Calendar</code> the document was first published. */
    private Calendar firstPublishedOn;

    /** The view's <code>DocumentVersion</code>. */
    private DocumentVersion version;

    /**
     * Create DocumentView.
     *
     */
    public DocumentView() {
        super();
    }

    /**
     * Obtain delta.
     *
     * @return A Delta.
     */
    public Delta getDelta() {
        return delta;
    }

    /**
     * Obtain the document id.
     * 
     * @return A document id <code>Long</code>.
     */
    public Long getDocumentId() {
        return version.getArtifactId();
    }

    /**
     * Obtain firstPublishedOn.
     *
     * @return A Calendar.
     */
    public Calendar getFirstPublishedOn() {
        return firstPublishedOn;
    }

    /**
     * Obtain version.
     *
     * @return A DocumentVersion.
     */
    public DocumentVersion getVersion() {
        return version;
    }

    /**
     * Set delta.
     *
     * @param delta
     *		A Delta.
     */
    public void setDelta(final Delta delta) {
        this.delta = delta;
    }

    /**
     * Set firstPublishedOn.
     *
     * @param firstPublishedOn
     *		A Calendar.
     */
    public void setFirstPublishedOn(final Calendar firstPublishedOn) {
        this.firstPublishedOn = firstPublishedOn;
    }

    /**
     * Set version.
     *
     * @param version
     *		A DocumentVersion.
     */
    public void setVersion(final DocumentVersion version) {
        this.version = version;
    }
}
