/*
 * Created On: Aug 8, 2006 2:02:21 PM
 */
package com.thinkparity.ophelia.browser.platform.action.document;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.platform.action.AbstractAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class UpdateDraft extends AbstractAction {

    /** A thinkParity browser application. */
    private final Browser browser;

    /** Create UpdateDraftDocument. */
    public UpdateDraft(final Browser browser) {
        super(ActionId.DOCUMENT_UPDATE_DRAFT);
        this.browser = browser;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
     */
    @Override
    public void invoke(final Data data) {
        final Long containerId = (Long) data.get(DataKey.CONTAINER_ID);
        final Long documentId = (Long) data.get(DataKey.DOCUMENT_ID);
        final File file = (File) data.get(DataKey.FILE);

        try {
            final InputStream content = new FileInputStream(file);
            try {
                getDocumentModel().updateDraft(documentId, content);
                browser.fireDocumentDraftUpdated(containerId, documentId);
            } finally {
                content.close();
            }
        }
        catch(final IOException iox) { throw translateError(iox); }
    }

    public enum DataKey { CONTAINER_ID, DOCUMENT_ID, FILE }
}
