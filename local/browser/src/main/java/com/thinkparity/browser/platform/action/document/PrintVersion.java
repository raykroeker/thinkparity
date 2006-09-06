/**
 * Created On: 1-Sep-06 3:03:28 PM
 * $Id$
 */
package com.thinkparity.browser.platform.action.document;

import java.io.File;

import org.jdesktop.jdic.desktop.DesktopException;

import com.thinkparity.codebase.jidc.DesktopUtil;

import com.thinkparity.browser.application.browser.Browser;
import com.thinkparity.browser.platform.action.AbstractAction;
import com.thinkparity.browser.platform.action.ActionId;
import com.thinkparity.browser.platform.action.Data;

import com.thinkparity.model.Printer;
import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.document.DocumentModel;
import com.thinkparity.model.parity.model.document.DocumentVersion;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class PrintVersion extends AbstractAction {

    /**
     * Create Export.
     * 
     * @param browser
     *            The thinkParity browser application.
     */
    public PrintVersion(final Browser browser) {
        super(ActionId.DOCUMENT_PRINT_VERSION);
    }

    /**
     * @see com.thinkparity.browser.platform.action.AbstractAction#invoke(com.thinkparity.browser.platform.action.Data)
     */
    @Override
    public void invoke(final Data data) {
        final Long documentId = (Long) data.get(DataKey.DOCUMENT_ID);
        final Long versionId = (Long) data.get(DataKey.VERSION_ID);
        final DocumentModel documentModel = getDocumentModel();
        final Document document = documentModel.get(documentId);
        final DocumentVersion version = documentModel.getVersion(documentId, versionId);

        final Browser browser = getBrowserApplication();
        if(browser.confirm(getId().toString(), new Object[] {
            document.getName(), version.getCreatedOn().getTime() })) {
            documentModel.printVersion(documentId, versionId, new Printer() {
                public void print(final File file) {
                    try {
                        DesktopUtil.print(file);
                    } catch (final DesktopException dx) {
                        throw translateError(dx);
                    }
                }
            });
        }
    }

    public enum DataKey { DOCUMENT_ID, VERSION_ID }
}
