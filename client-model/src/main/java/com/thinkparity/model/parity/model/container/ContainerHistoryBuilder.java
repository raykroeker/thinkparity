/*
 * Created On: Jul 6, 2006 3:10:10 PM
 */
package com.thinkparity.model.parity.model.container;

import java.util.ArrayList;
import java.util.List;

import com.thinkparity.codebase.l10n.L18n;

import com.thinkparity.model.parity.model.audit.HistoryItemBuilder;
import com.thinkparity.model.parity.model.audit.event.AuditEvent;
import com.thinkparity.model.parity.model.audit.event.AuditVersionEvent;

/**
 * <b>Title:</b>thinkParity Container History Builder<br>
 * <b>Description:</b>A history builder for the container module.
 * 
 * @author raymond@thinkparity.com
 * @see {@link HistoryItemBuilder}
 */
class ContainerHistoryBuilder extends HistoryItemBuilder<ContainerHistoryItem> {

    /** The internal container model. */
    private final InternalContainerModel cModel;

    /** Create ContainerHistoryBuilder. */
    ContainerHistoryBuilder(final InternalContainerModel cModel, final L18n l18n) {
        super(l18n);
        this.cModel = cModel;
    }

    /**
     * Create a container history item.
     * 
     * @param event
     *            An audit event.
     * @return A container history item.
     */
    @Override
    protected ContainerHistoryItem createItem(final AuditEvent event) {
        final ContainerHistoryItem item = new ContainerHistoryItem();
        customize(item, event);
        item.setContainerId(event.getArtifactId());
        if(event instanceof AuditVersionEvent)
            item.setVersionId(((AuditVersionEvent) event).getArtifactVersionId());
        return item;
    }

    /**
     * Create the container history.
     * 
     * @param containerId
     *            A container id.
     * 
     * @return The container history.
     */
    List<ContainerHistoryItem> createHistory(final Long containerId) {
        final List<AuditEvent> auditEvents = cModel.readAuditEvents(containerId);
        final List<ContainerHistoryItem> history = new ArrayList<ContainerHistoryItem>(auditEvents.size());
        for(int i = 0; i < auditEvents.size(); i++) {
            history.add(createItem(
                    auditEvents.listIterator(i + 1), auditEvents.get(i)));
        }
        return history;
    }
}
