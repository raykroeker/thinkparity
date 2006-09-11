/*
 * Created On: Jun 29, 2006 8:56:42 AM
 * $Id$
 */
package com.thinkparity.ophelia.model.container;


import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;


import com.thinkparity.ophelia.model.document.Document;
import com.thinkparity.ophelia.model.events.ContainerEvent;
import com.thinkparity.ophelia.model.user.TeamMember;

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
     * Generate a container event for a container and a container version.
     * 
     * @param container
     *            A <code>Container</code>.
     * @param version
     *            A <code>ContainerVersion</code>.
     * @return A container event.
     */
    ContainerEvent generate(final Container container,
            final ContainerVersion version) {
        return new ContainerEvent(source, container, version);
    }

    /**
     * Generate a container event for a container and a draft.
     * 
     * @param container
     *            A container.
     * @param draft
     *            A draft.
     * @return A container event.
     */
    ContainerEvent generate(final Container container, final ContainerDraft draft) {
        return new ContainerEvent(source, container, draft);
    }

    /**
     * Generate a container event for a container a document and a draft.
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
     * Generate a container event for a container; draft and version.
     * 
     * @param container
     *            A container.
     * @param draft
     *            A container draft.
     * @param version
     *            A container version.
     * @return A container event.
     */
    ContainerEvent generate(final Container container,
            final ContainerDraft draft, final ContainerVersion version) {
        return new ContainerEvent(source, container, draft, version);
    }

    /**
     * Generate a container event for a <code>Container</code> and a
     * <code>TeamMember</code>.
     * 
     * @param container
     *            A <code>Container</code>.
     * @param teamMember
     *            A <code>TeamMember</code>.
     * @return A <code>ContainerEvent</code>.
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
