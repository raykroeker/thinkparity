/*
 * Created On: Apr 7, 2006
 */
package com.thinkparity.ophelia.browser.application.browser.dnd;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.TransferHandler;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.swing.dnd.TxUtils;

import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.container.ContainerModel;
import com.thinkparity.ophelia.browser.platform.Platform.Connection;

import org.apache.log4j.Logger;

/**
 * An import transfer handler for drag'n'drop. The tx handler has the ability to
 * import a list of files as documents as well as to update draft documents.
 * Simply call
 * {@link JComponent#setTransferHandler(javax.swing.TransferHandler)} passing a
 * new instance of {@link ImportTxHandler} to the method.
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ImportTxHandler extends TransferHandler {

    /** @see java.io.Serializable */
    private static final long serialVersionUID = 1;

    /** An apache logger. */
    protected final Logger logger;

    /** The browser application. */
    private final Browser browser;
    
    /**
     * Create CreateDocumentTxHandler.
     * 
     * @param browser
     *            The browser application.
     * @param jList
     *            The swing JList representing the containers.
     * @param containerModel
     *            The model backing the container avatar.
     */
    public ImportTxHandler(final Browser browser, final JList jList,
            final ContainerModel containerModel) {
        super();
        this.browser = browser;
        this.logger = Logger.getLogger(getClass());
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
            if(Connection.ONLINE == browser.getConnection()) {
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
    	throw Assert.createNotYetImplemented("importData()");
    }
}
