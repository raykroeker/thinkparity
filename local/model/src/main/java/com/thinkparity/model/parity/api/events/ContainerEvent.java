/*
 * Created On: Jun 29, 2006 8:58:06 AM
 */
package com.thinkparity.model.parity.api.events;

import com.thinkparity.model.parity.model.container.Container;
import com.thinkparity.model.parity.model.container.ContainerDraft;
import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.user.TeamMember;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.4.3
 */
public class ContainerEvent {

    /** The event source. */
    private final Container container;

    /** A document. */
    private final Document document;

    /** A draft. */
    private final ContainerDraft draft;

    /** The event source. */
    private final Source source;

    /** Team member info. */
    private final TeamMember teamMember;

    /**
     * Create ContainerEvent.
     * 
     * @param source
     *            The event source
     * @param container
     *            The container.
     */
    public ContainerEvent(final Source source, final Container container) {
        this(source, container, null, null, null);
    }

    /**
     * Create ContainerEvent.
     * 
     * @param source
     *            The event source.
     * @param container
     *            The container.
     * @param draft
     *            The draft.
     * @param document
     *            The document.
     */
    public ContainerEvent(final Source source, final Container container,
            final ContainerDraft draft, final Document document) {
        this(source, container, draft, null, document);
    }

    /**
     * Create ContainerEvent.
     * 
     * @param source
     *            The event source
     * @param container
     *            The container.
     * @param teamMember
     *            The team member.
     */
    public ContainerEvent(final Source source, final Container container,
            final TeamMember teamMember) {
        this(source, container, null, teamMember, null);
    }

    /**
     * Create ContainerEvent.
     * 
     * @param source
     *            The event source.
     * @param draft
     *            The draft.
     */
    public ContainerEvent(final Source source, final ContainerDraft draft) {
        this(source, null, draft, null, null);
    }

    /**
     * Create ContainerEvent.
     * 
     * @param source
     *            The event source
     * @param container
     *            The container.
     * @param draft
     *            The draft.
     * @param teamMember
     *            The teamMember.
     * @param document
     *            A document.
     */
    private ContainerEvent(final Source source, final Container container,
            final ContainerDraft draft, final TeamMember teamMember,
            final Document document) {
        super();
        this.source = source;
        this.container = container;
        this.draft = draft;
        this.document = document;
        this.teamMember = teamMember;
    }

    /**
     * Obtain the container
     *
     * @return The Container.
     */
    public Container getContainer() { return container; }

    /**
     * Obtain the document
     *
     * @return The Document.
     */
    public Document getDocument() { return document; }

    /**
     * Obtain the draft.
     * 
     * @return The draft.
     */
    public ContainerDraft getDraft() { return draft; }

    /**
     * Obtain the user
     *
     * @return The User.
     */
    public TeamMember getTeamMember() { return teamMember; }

    /**
     * Determine if the event is a local event.
     * 
     * @return True if the event is a local event.
     */
    public Boolean isLocal() { return Source.LOCAL == source; }

    /**
     * Determine if the event is a remote event.
     * 
     * @return True if the event is a remote event.
     */
    public Boolean isRemote() { return Source.REMOTE == source; }

    /** A container event source. */
    public enum Source { LOCAL, REMOTE }
}
