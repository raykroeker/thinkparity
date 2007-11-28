/*
 * Created On:  27-Nov-07 4:09:39 PM
 */
package com.thinkparity.ophelia.model.container.delegate;

import java.util.List;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.artifact.DraftExistsException;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.user.TeamMember;

import com.thinkparity.ophelia.model.container.ContainerDelegate;
import com.thinkparity.ophelia.model.container.ContainerDraft;

/**
 * <b>Title:</b>thinkParity Ophelia Model Handle Draft Created Container
 * Delegate<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class HandleDraftCreated extends ContainerDelegate {

    /** The container. */
    private Container container;

    /** A container id. */
    private Long containerId;

    /** A created by user id. */
    private JabberId createdBy;

    /** The new draft. */
    private ContainerDraft draft;

    /**
     * Create HandleDraftCreated.
     *
     */
    public HandleDraftCreated() {
        super();
    }

    /**
     * Obtain the container.
     *
     * @return A <code>Container</code>.
     */
    public Container getContainer() {
        return container;
    }

    /**
     * Obtain the draft.
     *
     * @return A <code>ContainerDraft</code>.
     */
    public ContainerDraft getDraft() {
        return draft;
    }

    /**
     * Handle the draft created event.
     * 
     * @throws DraftExistsException
     */
    public void handleDraftCreated() throws DraftExistsException {
        final ContainerDraft existingDraft = containerIO.readDraft(containerId);
        if (null != existingDraft && existingDraft.isRemote()) {
            containerIO.deleteDraft(containerId);
        } else {
            /* wow; we have a local draft and are receiving a draft created
             * event.  this should never in a million years happen */
            throw new DraftExistsException();
        }

        container = read(containerId);

        draft = new ContainerDraft();
        draft.setContainerId(containerId);
        draft.setLocal(Boolean.FALSE);
        final List<TeamMember> team = readTeam(containerId);
        draft.setOwner(team.get(indexOf(team, createdBy)));
        containerIO.createDraft(draft);
    }

    /**
     * Set the container id.
     * 
     * @param containerId
     *            A <code>Long</code>.
     */
    public void setContainerId(final Long containerId) {
        this.containerId = containerId;
    }

    /**
     * Set the createdBy.
     *
     * @param createdBy
     *		A <code>JabberId</code>.
     */
    public void setCreatedBy(final JabberId createdBy) {
        this.createdBy = createdBy;
    }
}
