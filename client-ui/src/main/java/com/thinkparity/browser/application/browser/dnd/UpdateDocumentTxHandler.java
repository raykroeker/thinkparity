/*
 * Apr 7, 2006
 */
package com.thinkparity.browser.application.browser.dnd;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.TransferHandler;

import org.apache.log4j.Logger;

import com.thinkparity.browser.application.browser.Browser;
import com.thinkparity.browser.application.browser.display.avatar.main.MainCell;
import com.thinkparity.browser.application.browser.display.avatar.main.MainCellDocument;
import com.thinkparity.browser.javax.swing.dnd.TxUtils;
import com.thinkparity.browser.platform.Platform.Connection;
import com.thinkparity.browser.platform.util.log4j.LoggerFactory;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.document.DocumentModel;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class UpdateDocumentTxHandler extends TransferHandler {

    /** An apache error message. */
    private static final String IMPORT_IOX =
        "[BROWSER2] [APP] [B2] [DND] [IMPORT DATA] [IO ERROR]";

    /** An apache error message. */
    private static final String IMPORT_PX =
        "[BROWSER2] [APP] [B2] [DND] [IMPORT DATA] [PARITY LMODEL ERROR]";

    /** An apache error message. */
    private static final String IMPORT_UFX =
        "[BROWSER2] [APP] [B2] [DND] [IMPORT DATA] [UNSUPPORTED DROP DATA ERROR]";

    /** @see java.io.Serializable */
    private static final long serialVersionUID = 1;

    /** An apache logger. */
    protected final Logger logger;

    /** The browser application. */
    private final Browser application;

    /** The parity document interface. */
    private final DocumentModel dModel;

    /** The list we are droping on. */
    private final JList jList;

    /**
     * Create an UpdateDocumentTxHandler.
     * 
     * @param jList
     *            The list we are dropping on.
     * @param application
     *            The browser application.
     */
    public UpdateDocumentTxHandler(final Browser application, final JList jList) {
        this.application = application;
        this.dModel = application.getDocumentModel();
        this.jList = jList;
        this.logger = LoggerFactory.getLogger(getClass());
    }

    /**
     * @see javax.swing.TransferHandler#canImport(javax.swing.JComponent,
     *      java.awt.datatransfer.DataFlavor[])
     * 
     */
    public boolean canImport(final JComponent comp,
            final DataFlavor[] transferFlavors) {
        logger.debug(comp.getClass().getSimpleName());
        // when updating a document; only 1 file must be imported
        if(1 == transferFlavors.length) {
            if(TxUtils.containsJavaFileList(transferFlavors)) {
                if(!jList.isSelectionEmpty()) {
                    if(Connection.ONLINE == application.getConnection()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * @see javax.swing.TransferHandler#importData(javax.swing.JComponent,
     *      java.awt.datatransfer.Transferable)
     * 
     */
    public boolean importData(final JComponent comp, final Transferable t) {
        if(!canImport(comp, t.getTransferDataFlavors())) { return false; }

        List data = null;
        try { data = (List) t.getTransferData(DataFlavor.javaFileListFlavor); }
        catch(final UnsupportedFlavorException ufx) {
            logger.error(IMPORT_UFX, ufx);
            return false;
        }
        catch(final IOException iox) {
            logger.error(IMPORT_IOX, iox);
            return false;
        }

        final MainCell mc = (MainCell) jList.getSelectedValue();
        if(mc instanceof MainCellDocument) {
            final MainCellDocument mcd = (MainCellDocument) mc;
            try {
                if(!dModel.isWorkingVersionEqual(mcd.getId())) {
                    if(application.confirm("ConfirmOverwriteWorking")) {
                        dModel.updateWorkingVersion(mcd.getId(), (File) data.iterator().next());
                        application.fireDocumentUpdated(mcd.getId());
                        return true;
                    }
                }
                else {
                    dModel.updateWorkingVersion(mcd.getId(), (File) data.iterator().next());
                    application.fireDocumentUpdated(mcd.getId());
                    return true;
                }
            }
            catch(final ParityException px) {
                logger.error(IMPORT_PX, px);
            }       
        }
        return false;
    }
}
