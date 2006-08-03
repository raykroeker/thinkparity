/*
 * Created On: Jun 29, 2006 8:56:42 AM
 * $Id$
 */
package com.thinkparity.model.parity.model.container;


import com.thinkparity.model.parity.api.events.ContainerEvent;
import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.user.TeamMember;

/**
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
class ContainerEventGenerator {

    /** The source to use when generating events. */
    private final ContainerEvent.Source source;

    /**
     * Create ContainerEventGenerator.
     * 
     * @param source
     *            The container event source.
     */
    ContainerEventGenerator(final ContainerEvent.Source source) {
        super();
        this.source = source;
    }

    /**
     * Generate a container event for a container.
     * 
     * @param container
     *            A container.
     * @return A container event.
     */
    ContainerEvent generate(final Container container) {
        return new ContainerEvent(source, container);
    }

    /**
     * Generate a container event for a container and a document.
     * 
     * @param container
     *            A container.
     * @param draft
     *            A draft.
     * @param document
     *            A document.
     * @return A container event.
     */
    ContainerEvent generate(final Container container,
            final ContainerDraft draft, final Document document) {
        return new ContainerEvent(source, container, draft, document);
    }

    /**
     * Generate a container event for a container and a user.
     * 
     * @param container
     *            A container.
     * @param user
     *            A user.
     * @return A container event.
     */
    ContainerEvent generate(final Container container, final TeamMember teamMember) {
        return new ContainerEvent(source, container, teamMember);
    }

    /**
     * Generate a container event for a draft.
     * 
     * @param draft
     *            A draft.
     * @return A container event.
     */
    ContainerEvent generate(final ContainerDraft draft) {
        return new ContainerEvent(source, draft);
    }
}
