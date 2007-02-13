/**
 * Created On: 1-Sep-06 3:03:28 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.action.document;

import java.io.File;

import org.jdesktop.jdic.desktop.DesktopException;

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
public class PrintVersion extends AbstractAction {

    /** The browser application. */
    private final Browser browser;

    /**
     * Create PrintVersion.
     * 
     * @param browser
     *            The thinkParity browser application.
     */
    public PrintVersion(final Browser browser) {
        super(ActionId.DOCUMENT_PRINT_VERSION);
        this.browser = browser;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
     */
    @Override
    public void invoke(final Data data) {
        final Long documentId = (Long) data.get(DataKey.DOCUMENT_ID);
        final Long versionId = (Long) data.get(DataKey.VERSION_ID);
        final DocumentModel documentModel = getDocumentModel();

        if (DesktopUtil.isPrinter()) {
            documentModel.printVersion(documentId, versionId, new Printer() {
                public void print(final File file, final String documentName) {
                    if (DesktopUtil.isPrintable(file)) {
                        try {
                            DesktopUtil.print(file);
                        } catch (final DesktopException dx) {
                            throw translateError(dx);
                        }
                    } else {
                        browser.displayErrorDialog("ErrorPrintDocumentNotPrintable", new Object[] {documentName});         
                    }
                }
            });
        } else {
            browser.displayErrorDialog("ErrorPrintDocumentNoPrinter");
        }
    }

    public enum DataKey { DOCUMENT_ID, VERSION_ID }
}
