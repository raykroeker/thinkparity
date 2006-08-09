/*
 * Created On: Aug 8, 2006 2:02:21 PM
 */
package com.thinkparity.browser.platform.action.document;

import java.io.File;

import com.thinkparity.browser.application.browser.Browser;
import com.thinkparity.browser.platform.action.AbstractAction;
import com.thinkparity.browser.platform.action.ActionId;
import com.thinkparity.browser.platform.action.Data;

import com.thinkparity.model.parity.model.document.DocumentModel;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class UpdateDocumentDraft extends AbstractAction {

    /** The thinkParity browser application. */
    private final Browser browser;

    /** A thinkParity document interface. */
    private final DocumentModel documentModel;

    /** Create UpdateDraftDocument. */
    public UpdateDocumentDraft(final Browser browser) {
        super(null, ActionId.DOCUMENT_UPDATE_DRAFT, null, null);
        this.browser = browser;
        this.documentModel = browser.getDocumentModel();
    }

    /**
     * @see com.thinkparity.browser.platform.action.AbstractAction#invoke(com.thinkparity.browser.platform.action.Data)
     */
    @Override
    public void invoke(final Data data) throws Exception {
        final Long documentId = (Long) data.get(DataKey.DOCUMENT_ID);
        final File file = (File) data.get(DataKey.FILE);

        documentModel.updateWorkingVersion(documentId, file);
        browser.fireDocumentDraftUpdated(documentId);
    }

    public enum DataKey { DOCUMENT_ID, FILE }
}
