/*
 * Created On: Jun 29, 2006 8:58:06 AM
 */
package com.thinkparity.ophelia.model.events;

import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.Document;

import com.thinkparity.ophelia.model.container.ContainerDraft;
import com.thinkparity.ophelia.model.user.TeamMember;

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

    /** A container version. */
    private final ContainerVersion version;

    /**
     * Create ContainerEvent.
     * 
     * @param source
     *            The <code>Source</code>.
     * @param container
     *            A <code>Container</code>.
     * @param teamMember
     *            A <code>TeamMember</code>.
     * @param version
     *            A <code>ContainerVersion</code>.
     */
    public ContainerEvent(final Source source, final Container container,
            final TeamMember teamMember, final ContainerVersion version) {
        this(source, container, null, version, teamMember, null);
    }

    /**
     * Create ContainerEvent.
     * 
     * @param source
     *            The event source
     * @param container
     *            The container.
     */
    public ContainerEvent(final Source source, final Container container) {
        this(source, container, null, null, null, null);
    }

    /**
     * Create ContainerEvent.
     * 
     * @param source
     *            The event source
     * @param container
     *            A container.
     * @param draft
     *            A container draft.
     */
    public ContainerEvent(final Source source, final Container container,
            final ContainerDraft draft) {
        this(source, container, draft, null, null, null);
    }

    /**
     * Create ContainerEvent.
     * 
     * @param source
     *            The event source
     * @param container
     *            A container.
     * @param draft
     *            A container draft.
     * @param version
     *            A container version.
     */
    public ContainerEvent(final Source source, final Container container,
            final ContainerDraft draft, final ContainerVersion version) {
        this(source, container, draft, version, null, null);
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
        this(source, container, draft, null, null, document);
    }

    /**
     * Create ContainerEvent.
     * 
     * @param source
     *            The event source
     * @param container
     *            The <code>Container</code>.
     * @param version
     *            The <code>ContainerVersion</code>.
     */
    public ContainerEvent(final Source source, final Container container,
            final ContainerVersion version) {
        this(source, container, null, version, null, null);
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
        this(source, container, null, null, teamMember, null);
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
        this(source, null, draft, null, null, null);
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
     * @param version
     *            The version.
     * @param teamMember
     *            The teamMember.
     * @param document
     *            A document.
     */
    private ContainerEvent(final Source source, final Container container,
            final ContainerDraft draft, final ContainerVersion version,
            final TeamMember teamMember, final Document document) {
        super();
        this.source = source;
        this.container = container;
        this.draft = draft;
        this.document = document;
        this.teamMember = teamMember;
        this.version = version;
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
     * Obtain the version
     *
     * @return The ContainerVersion.
     */
    public ContainerVersion getVersion() { return version; }

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
