/**
 * Created On: 1-Sep-06 3:03:28 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.action.document;

import java.io.File;

import org.jdesktop.jdic.desktop.DesktopException;

import com.thinkparity.codebase.model.document.Document;




import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.platform.action.AbstractAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.util.jdic.DesktopUtil;
import com.thinkparity.ophelia.model.document.DocumentModel;
import com.thinkparity.ophelia.model.util.Printer;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class PrintDraft extends AbstractAction {

    /**
     * Create Export.
     * 
     * @param browser
     *            The thinkParity browser application.
     */
    public PrintDraft(final Browser browser) {
        super(ActionId.DOCUMENT_PRINT_DRAFT);
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
     */
    @Override
    public void invoke(final Data data) {
        final Long documentId = (Long) data.get(DataKey.DOCUMENT_ID);
        final DocumentModel documentModel = getDocumentModel();
        final Document document = documentModel.get(documentId);

        final Browser browser = getBrowserApplication();
        if(browser.confirm(getId().toString(), new Object[] { document
                .getName() })) {
            documentModel.printDraft(documentId, new Printer() {
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

    public enum DataKey { DOCUMENT_ID }
}
