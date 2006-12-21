/*
 * Created On:  20-Dec-06 10:59:54 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.view;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.thinkparity.codebase.model.artifact.Artifact;
import com.thinkparity.codebase.model.document.Document;

import com.thinkparity.ophelia.model.container.ContainerDraft;

/**
 * <b>Title:</b>thinkParity Container Tab Draft View<br>
 * <b>Description:</b>The draft view represents the data displayed for a draft
 * for each container within the container panel.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class DraftView {

    /** The <code>ContainerDraft</code>. */
    private ContainerDraft draft;

    /** The draft artifact's first published on <code>Calendar</code>. */
    private final Map<Artifact, Calendar> firstPublishedOn;

    /**
     * Create DraftView.
     *
     */
    public DraftView() {
        super();
        this.firstPublishedOn = new HashMap<Artifact, Calendar>();
    }

    /**
     * Obtain a list of the draft documents.
     * @return A <code>List</code> of <code>Document</code>s.
     */
    public List<Document> getDocuments() {
        return draft.getDocuments();
    }

    /**
     * Obtain draft.
     *
     * @return A ContainerDraft.
     */
    public ContainerDraft getDraft() {
        return draft;
    }

    /**
     * Obtain the first published on date for an artifact.
     * 
     * @param artifact
     *            An <code>Artifact</code>.
     * @return The first published on <code>Calendar</code>.
     */
    public Calendar getFirstPublishedOn(final Artifact artifact) {
        return firstPublishedOn.get(artifact);
    }

    /**
     * Set draft.
     *
     * @param draft
     *		A ContainerDraft.
     */
    public void setDraft(final ContainerDraft draft) {
        this.draft = draft;
    }

    /**
     * Set the first published on date for an artifact.
     * 
     * @param artifact
     *            An <code>Artifact</code>.
     * @param firstPublishedOn
     *            The first published on date <code>Calendar</code>.
     */
    public void setFirstPublishedOn(final Artifact artifact,
            final Calendar firstPublishedOn) {
        this.firstPublishedOn.put(artifact, firstPublishedOn);
    }
}
