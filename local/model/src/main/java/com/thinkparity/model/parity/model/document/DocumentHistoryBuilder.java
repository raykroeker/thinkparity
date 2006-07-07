/*
 * Created On: Feb 22, 2006
 */
package com.thinkparity.model.parity.model.document;

import java.util.ArrayList;
import java.util.List;

import com.thinkparity.codebase.l10n.L18n;

import com.thinkparity.model.parity.model.audit.HistoryItemBuilder;
import com.thinkparity.model.parity.model.audit.event.AuditEvent;
import com.thinkparity.model.parity.model.audit.event.AuditVersionEvent;

/**
 * <b>Title:</b>thinkParity Document History Builder<br>
 * <b>Description:</b>A history builder for the document module.
 * 
 * @author raymond@thinkparity.com
 * @see {@link HistoryItemBuilder}
 */
class DocumentHistoryBuilder extends HistoryItemBuilder<DocumentHistoryItem> {

    /** The internal document model. */
    private final InternalDocumentModel dModel;

    /**
     * Create DocumentHistoryBuilder.
     * 
     * @param l18n
     *            The parity model localization.
     */
	DocumentHistoryBuilder(final InternalDocumentModel dModel, final L18n l18n) {
        super(l18n);
        this.dModel = dModel;
    }

    /**
     * Create the document history item.
     * 
     * @param event
     *            An audit event.
     * @return A new history item.
     */
    @Override
    protected DocumentHistoryItem createItem(final AuditEvent event) {
        final DocumentHistoryItem item = new DocumentHistoryItem();
        item.setDocumentId(event.getArtifactId());
        if(event instanceof AuditVersionEvent)
            item.setVersionId(((AuditVersionEvent) event).getArtifactVersionId());
        return item;
    }

    /**
     * Create the document history.
     * 
     * @param documentId
     *            A document id.
     * @return The document history.
     */
	List<DocumentHistoryItem> createHistory(final Long documentId) {
	    final List<AuditEvent> auditEvents = dModel.readAuditEvents(documentId);
        final List<DocumentHistoryItem> history = new ArrayList<DocumentHistoryItem>(auditEvents.size());
        for(int i = 0; i < auditEvents.size(); i++) {
            history.add(createItem(
                    auditEvents.listIterator(i + 1), auditEvents.get(i)));
        }
        return history;
    }
}
