/*
 * Created On: Jun 29, 2006 8:58:06 AM
 */
package com.thinkparity.ophelia.model.events;

import java.util.Collections;
import java.util.List;

import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.contact.OutgoingEMailInvitation;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.user.TeamMember;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.container.ContainerDraft;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.4.3
 */
public class ContainerEvent {

    private static final List<ArtifactReceipt> EMPTY_ARTIFACT_RECEIPTS;

    private static final List<OutgoingEMailInvitation> EMPTY_OUTGOING_EMAIL_INVITATIONS;

    static {
        EMPTY_OUTGOING_EMAIL_INVITATIONS = Collections.emptyList();
        EMPTY_ARTIFACT_RECEIPTS = Collections.emptyList();
    }

    /** The event source. */
    private final Container container;

    /** A document. */
    private final Document document;

    /** A draft. */
    private final ContainerDraft draft;

    /** The next <code>ContainerVersion</code>. */
    private final ContainerVersion nextVersion;

    /** An <code>OutgoingEMailInvitation</code>. */
    private final List<OutgoingEMailInvitation> outgoingEMailInvitations;

    /** The previous <code>ContainerVersion</code>. */
    private final ContainerVersion previousVersion;

    /** A list of artifact receipts. */
    private final List<ArtifactReceipt> receipts;

    /** The event source. */
    private final Source source;

    /** Team member info. */
    private final TeamMember teamMember;

    /** User info. */
    private final User user;

    /** A container version. */
    private final ContainerVersion version;

    /**
     * Create ContainerEvent.
     * 
     * @param source
     *            The event source
     * @param container
     *            The container.
     */
    public ContainerEvent(final Source source, final Container container) {
        this(source, container, null, null, null, null, null, null,
                EMPTY_OUTGOING_EMAIL_INVITATIONS, null, EMPTY_ARTIFACT_RECEIPTS);
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
        this(source, container, draft, null, null, null, null, null,
                EMPTY_OUTGOING_EMAIL_INVITATIONS, null, EMPTY_ARTIFACT_RECEIPTS);
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
            final ContainerDraft draft, final ContainerVersion previousVersion,
            final ContainerVersion version, final ContainerVersion nextVersion,
            final TeamMember teamMember,
            final List<OutgoingEMailInvitation> outgoingEMailInvitations) {
        this(source, container, draft, previousVersion, version, nextVersion,
                teamMember, null, outgoingEMailInvitations, null,
                EMPTY_ARTIFACT_RECEIPTS);
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
        this(source, container, draft, null, null, null, null, document,
                EMPTY_OUTGOING_EMAIL_INVITATIONS, null, EMPTY_ARTIFACT_RECEIPTS);
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
        this(source, container, null, null, version, null, null,
                EMPTY_OUTGOING_EMAIL_INVITATIONS);
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
            final ContainerVersion previousVersion,
            final ContainerVersion version, final ContainerVersion nextVersion,
            final User user) {
        this(source, container, null, previousVersion, version, nextVersion,
                null, null, EMPTY_OUTGOING_EMAIL_INVITATIONS, user,
                EMPTY_ARTIFACT_RECEIPTS);
    }

    /**
     * Create ContainerEvent.
     * 
     * @param source
     *            The event <code>Source</code>.
     * @param version
     *            A <code>ContainerVersion</code>.
     * @param receipts
     *            A <code>List<ArtifactReceipt></code>.
     */
    public ContainerEvent(final Source source, final Container container,
            final ContainerVersion version, final List<ArtifactReceipt> receipts) {
        this(source, container, null, null, version, null, null, null,
                EMPTY_OUTGOING_EMAIL_INVITATIONS, null, receipts);
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
        this(source, container, null, null, null, null, teamMember, null,
                EMPTY_OUTGOING_EMAIL_INVITATIONS, null, EMPTY_ARTIFACT_RECEIPTS);
    }

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
        this(source, container, null, null, version, null, teamMember, null,
                EMPTY_OUTGOING_EMAIL_INVITATIONS, null, EMPTY_ARTIFACT_RECEIPTS);
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
        this(source, null, draft, null, null, null, null,
                EMPTY_OUTGOING_EMAIL_INVITATIONS);
    }

    /**
     * Create ContainerEvent.
     * 
     * @param source
     *            The event <code>Source</code>.
     * @param version
     *            A <code>ContainerVersion</code>.
     */
    public ContainerEvent(final Source source, final ContainerVersion version) {
        this(source, null, null, null, version, null, null, null,
                EMPTY_OUTGOING_EMAIL_INVITATIONS, null, EMPTY_ARTIFACT_RECEIPTS);
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
            final ContainerDraft draft, final ContainerVersion previousVersion,
            final ContainerVersion version, final ContainerVersion nextVersion,
            final TeamMember teamMember, final Document document,
            final List<OutgoingEMailInvitation> outgoingEMailInvitations,
            final User user, final List<ArtifactReceipt> receipts) {
        super();
        this.source = source;
        this.container = container;
        this.draft = draft;
        this.document = document;
        this.nextVersion = nextVersion;
        this.outgoingEMailInvitations = outgoingEMailInvitations;
        this.previousVersion = previousVersion;
        this.teamMember = teamMember;
        this.version = version;
        this.user = user;
        this.receipts = receipts;
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
     * Obtain next container version.
     * 
     * @return A <code>ContainerVersion</code>.
     */
    public ContainerVersion getNextVersion() {
        return nextVersion;
    }

    /**
     * Obtain outgoingEMailInvitation.
     *
     * @return A OutgoingEMailInvitation.
     */
    public List<OutgoingEMailInvitation> getOutgoingEMailInvitations() {
        // HACK - ContainerEvent#getOutgoingEMailInvitation()
        return outgoingEMailInvitations;
    }

    /**
     * Obtain the previous container version.
     *
     * @return A <code>ContainerVersion</code>.
     */
    public ContainerVersion getPreviousVersion() {
        return previousVersion;
    }

    /**
     * Obtain the receipts.
     * 
     * @return A <code>List<ArtifactReceipt></code>.
     */
    public List<ArtifactReceipt> getReceipts() {
        return receipts;
    }

    /**
     * Obtain the user
     *
     * @return The User.
     */
    public TeamMember getTeamMember() { return teamMember; }

    /**
     * Obtain user.
     *
     * @return A User.
     */
    public User getUser() {
        return user;
    }

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
