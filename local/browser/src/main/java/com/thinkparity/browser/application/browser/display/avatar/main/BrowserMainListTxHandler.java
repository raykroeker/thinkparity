/*
 * Apr 6, 2006
 */
package com.thinkparity.browser.application.browser.display.avatar.main;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.TransferHandler;

import org.apache.log4j.Logger;

import com.thinkparity.browser.application.browser.display.avatar.BrowserMainAvatar;
import com.thinkparity.browser.model.ModelFactory;
import com.thinkparity.browser.platform.util.log4j.LoggerFactory;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.document.DocumentModel;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class BrowserMainListTxHandler extends TransferHandler {

    /** @see java.io.Serializable */
    private static final long serialVersionUID = 1;

    /** The browser main avatar. */
    private final BrowserMainAvatar browserMain;

    /** The supported file transfer. **/
    private final DataFlavor fileFlavor;

    /** An apache logger. **/
    protected final Logger logger;

    /** Create a BrowserMainListTxHandler. */
    public BrowserMainListTxHandler(final BrowserMainAvatar browserMain) {
        super();
        this.browserMain = browserMain;
        this.logger = LoggerFactory.getLogger(getClass());
        this.fileFlavor = DataFlavor.javaFileListFlavor;
    }

    /**
     * @see javax.swing.TransferHandler#canImport(javax.swing.JComponent, java.awt.datatransfer.DataFlavor[])
     * 
     */
    public boolean canImport(final JComponent comp,
            final DataFlavor[] transferFlavors) {
        logger.debug(comp.getClass().getSimpleName());
        if(containsFile(transferFlavors)) { return true; }
        return false;
    }

    /**
     * @see javax.swing.TransferHandler#importData(javax.swing.JComponent, java.awt.datatransfer.Transferable)
     * 
     */
    public boolean importData(final JComponent comp, final Transferable t) {
        if(!canImport(comp, t.getTransferDataFlavors())) { return false; }

        // do the work
        try {
            final java.util.List data =
                (java.util.List) t.getTransferData(fileFlavor);
            File file;
            Document[] documents;
            for(final Object o : data) {
                // try to find a document with the same name
                file = (File) o;
                documents = find(file.getName());
                if(null != documents) {
                    if(1 == documents.length) {
                        // update document
                    }
                    else {
                        // update documents
                    }
                }
                else {
                    // create document
                    browserMain.getController().runCreateDocument(file);
                }
            }
            return true;
        }
        catch(final Exception x) {
            logger.error("[BROWSER2] [APP] [B2] [DND DROP] [UNEXPECTED ERROR]", x);
        }
        return false;
    }

    private Document[] find(final String name) { return null; }

    /**
     * Check if the data flavors contains the file flavor.
     * 
     * @param flavors
     *            The data flavors to iterate.
     * @return True if the flavors contains the file flavor.
     */
    private boolean containsFile(final DataFlavor[] flavors) {
        for(final DataFlavor flavor : flavors) {
            if(fileFlavor.equals(flavor)) { return true; }
        }
        return false;
    }

    /**
     * @see javax.swing.TransferHandler#createTransferable(javax.swing.JComponent)
     * 
     */
    protected Transferable createTransferable(final JComponent c) {
        if(c instanceof JList) {
            final ListItem li = (ListItem) ((JList) c).getSelectedValue();
            final List<File> archiveFiles = new LinkedList<File>();
            if(li instanceof DocumentListItem) {
                archiveFiles.add(archive((Long) ((DocumentListItem) li).getProperty("documentId")));
            }
            if(0 < archiveFiles.size()) {
                return new BrowserMainListTx(archiveFiles);
            }
            else { return null; }
        }
        else { return null; }
    }

    private File archive(final Long documentId) {
        // TODO Provider?
        final DocumentModel dModel = ModelFactory.getInstance().getDocumentModel(getClass());
        try { return dModel.archive(documentId); }
        catch(final ParityException px) {
            throw new RuntimeException(px);
        }
    }

    /**
     * @see javax.swing.TransferHandler#getSourceActions(javax.swing.JComponent)
     * 
     */
    public int getSourceActions(final JComponent c) {
        if(c instanceof JList) { return COPY_OR_MOVE; }
        else { return NONE; }
    }
}
