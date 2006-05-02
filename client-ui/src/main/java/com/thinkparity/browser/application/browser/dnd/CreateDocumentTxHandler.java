/*
 * Apr 7, 2006
 */
package com.thinkparity.browser.application.browser.dnd;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.TransferHandler;

import org.apache.log4j.Logger;

import com.thinkparity.browser.application.browser.Browser;
import com.thinkparity.browser.javax.swing.dnd.TxUtils;
import com.thinkparity.browser.platform.Platform.Connection;
import com.thinkparity.browser.platform.util.log4j.LoggerFactory;

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
    private static final String IMPORT_X = "[BROWSER2] [APP] [B2] [IMPORT DATA] [ERROR]";

    /** An apache logger error statement. */
    private static final String IMPORT_UFX = "[BROWSER2] [APP] [B2] [IMPORT DATA] [UNSUPPORTED DATA FORMAT]";

    /** @see java.io.Serializable */
    private static final long serialVersionUID = 1;

    /** An apache logger. */
    protected final Logger logger;

    /** The browser application. */
    private final Browser browser;

    /**
     * Create a CreateDocumentTxHandler.
     * 
     */
    public CreateDocumentTxHandler(final Browser browser) {
        super();
        this.browser = browser;
        this.logger = LoggerFactory.getLogger(getClass());
    }

    /**
     * @see javax.swing.TransferHandler#canImport(javax.swing.JComponent, java.awt.datatransfer.DataFlavor[])
     * 
     */
    public boolean canImport(final JComponent comp,
            final DataFlavor[] transferFlavors) {
        logger.info("[LBROWSER] [APPLICATION] [BROWSER] [DND] [CREATE TX] [CAN IMPORT]");
        logger.debug(comp.getClass().getSimpleName());
        if(TxUtils.containsJavaFileList(transferFlavors)) {
            if(Connection.ONLINE == browser.getConnectionStatus()) {
                return true;
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

        File[] files = null;
        try { files = TxUtils.extractFiles(t); }
        catch(final IOException iox) {
            logger.error(IMPORT_IOX, iox);
        }
        catch(final UnsupportedFlavorException ufx) {
            logger.error(IMPORT_UFX, ufx);
            return false;
        }

        // create documents for each file transferred
        final List<File> fileList = new LinkedList<File>();

        for(final File file : files) { fileList.add(file); }

        try { browser.runCreateDocuments(fileList); }
        catch(final Exception x) {
            logger.error(IMPORT_X, x);
            return false;
        }

        return true;
    }
}
