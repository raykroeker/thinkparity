/*
 * Apr 7, 2006
 */
package com.thinkparity.browser.application.browser.dnd;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.TransferHandler;

import org.apache.log4j.Logger;

import com.thinkparity.browser.application.browser.Browser;
import com.thinkparity.browser.javax.swing.dnd.TxUtils;
import com.thinkparity.browser.platform.util.log4j.LoggerFactory;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.document.DocumentModel;

/**
 * A document transfer handler for drag'n'drop. The tx handler has the ability
 * to import a list of files as documents. Simply call
 * {@link JComponent#setTransferHandler(javax.swing.TransferHandler)} passing a
 * new instance of {@link CreateDocumentTxHandler} to the method.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class CreateDocumentTxHandler extends TransferHandler {

    /** An apache logger error statement. */
    private static final String IMPORT_IOX = "[BROWSER2] [APP] [B2] [IMPORT DATA] [IO ERROR]";

    /** An apache logger error statement. */
    private static final String IMPORT_PX = "[BROWSER2] [APP] [B2] [IMPORT DATA] [PARITY ERROR]";

    /** An apache logger error statement. */
    private static final String IMPORT_UFX = "[BROWSER2] [APP] [B2] [IMPORT DATA] [UNSUPPORTED DATA FORMAT]";

    /** An apache logger error statement. */
    private static final String IMPORT_UNDO_PX = "[BROWSER2] [APP] [B2] [IMPORT DATA] [UNDO PARITY ERROR]";

    /** @see java.io.Serializable */
    private static final long serialVersionUID = 1;

    /** An apache logger. */
    protected final Logger logger;

    /** The browser application. */
    private final Browser browser;

    /** The parity document interface. */
    private final DocumentModel dModel;

    /** The transfer data flavor. */
    private final DataFlavor javaFileListFlavor;

    /**
     * Create a CreateDocumentTxHandler.
     * 
     */
    public CreateDocumentTxHandler(final Browser browser) {
        super();
        this.browser = browser;
        this.dModel = browser.getDocumentModel();
        this.javaFileListFlavor = DataFlavor.javaFileListFlavor;
        this.logger = LoggerFactory.getLogger(getClass());
    }

    /**
     * @see javax.swing.TransferHandler#canImport(javax.swing.JComponent, java.awt.datatransfer.DataFlavor[])
     * 
     */
    public boolean canImport(final JComponent comp,
            final DataFlavor[] transferFlavors) {
        logger.debug(comp.getClass().getSimpleName());
        if(TxUtils.containsJavaFileList(transferFlavors)) { return true; }
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
        try { data = (List) t.getTransferData(javaFileListFlavor); }
        catch(final UnsupportedFlavorException ufx) {
            logger.error(IMPORT_UFX, ufx);
            return false;            
        }
        catch(final IOException iox) {
            logger.error(IMPORT_IOX, iox);
            return false;
        }

        // create documents for each file transferred
        final Set<Long> createdIds = new HashSet<Long>();
        boolean didPass = true;
        for(final Object datum : data) {
            final File file = (File) datum;
            try {
                final Document document =
                    dModel.create(file.getName(), null, file);
                createdIds.add(document.getId());
            }
            catch(final ParityException px) {
                logger.error(IMPORT_PX, px);
                didPass = false;
            }
        }
        if(didPass) {
            browser.fireDocumentsCreated(createdIds);
            browser.setInfoMessage("Document.Created");
            return true;
        }
        else {
            // try to undo the created docs
            for(final Long id : createdIds) {
                try { dModel.delete(id); }
                catch(final ParityException px) {
                    logger.error(IMPORT_UNDO_PX, px);
                }
            }
            return false;
        }
    }

    /**
     * @see javax.swing.TransferHandler#getVisualRepresentation(java.awt.datatransfer.Transferable)
     */
    @Override
    public Icon getVisualRepresentation(Transferable t) {
        return null;
    }
}
