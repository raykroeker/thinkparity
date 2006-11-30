/*
 * Created On: Apr 7, 2006
 */
package com.thinkparity.ophelia.browser.application.browser.dnd;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.TransferHandler;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.swing.dnd.TxUtils;

import com.thinkparity.codebase.model.container.Container;

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
    
    /** An apache logger error statement. */
    private static final String IMPORT_IOX = "Browser Drag and Drop IO Error";

    /** An apache logger error statement. */
    private static final String IMPORT_UFX = "Browser Drag and Drop Unsupported Data Format Error";
    
    /** An apache logger. */
    protected final Logger logger;

    /** The browser application. */
    private final Browser browser;
    
    /** The container. */
    private final Container container;
    
    /**
     * Create a transfer handler that creates containers and imports documents into it.
     * 
     * @param browser
     *            The browser application.
     * @param containerModel
     *            The model backing the container avatar.
     */
    public ImportTxHandler(final Browser browser, final ContainerModel containerModel) {
        super();
        this.browser = browser;
        this.container = null;
        this.logger = Logger.getLogger(getClass());
    }
    
    /**
     * Create a transfer handler that imports documents to a container.
     * 
     * @param browser
     *            The browser application.
     * @param containerModel
     *            The model backing the container avatar.
     * @param container
     *            The container.       
     */
    public ImportTxHandler(final Browser browser, final ContainerModel containerModel, final Container container) {
        super();
        this.browser = browser;
        this.container = container;
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
            // If the user is trying to update a container then he needs to
            // have the draft, or alternatively if nobody has the draft then he
            // needs to be online in order to create a new draft.
            if (null == container) {
                // Creating new container is always allowed.
                return true;
            } else if (container.isLocalDraft()) {
                // Updating a container is allowed if the user has the local draft.
                return true;                
            } else if (!container.isDraft() && (Connection.ONLINE == browser.getConnection())) {
                // Updating a container is allowed if online and nobody has the draft.
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
        
        // Determine the operation.
        // - If the user drops on a container (eg. on a panel) then add or
        //   update documents.
        // - If the user is not dropping on a container (eg. on the blank area
        //   above or below container panels) then create a container and
        //   add documents.
        if (null == container) {
            return createContainer(t);
        } else {
            throw Assert.createUnreachable("Import has been moved.");
        }
    }
    
    /**
     * Create a container and then add documents from the transferable files.
     * 
     * @param t
     *            The java DND transferable.
     */
    private boolean createContainer(final Transferable t) {
        File[] files = null;
        try {
            files = TxUtils.extractFiles(t);
        } catch (final IOException iox) {
            logger.error(IMPORT_IOX, iox);
        } catch (final UnsupportedFlavorException ufx) {
            logger.error(IMPORT_UFX, ufx);
            return false;
        }
        
        // Determine the list of files to add to a new package. Check if the user
        // is trying to drag folders.
        final List<File> fileList = new ArrayList<File>();
        Boolean foundFolders = Boolean.FALSE;
        Boolean foundFiles = Boolean.FALSE;
        for (final File file : files) {
            if (file.isDirectory()) {
                foundFolders = Boolean.TRUE;
            } else {
                foundFiles = Boolean.TRUE;
                fileList.add(file);
            }
        }
        
        // Report an error if the user tries to drag folders. Otherwise
        // create a package and add documents.
        if (foundFolders) {
            browser.displayErrorDialog("ErrorCreatePackageIsFolder");
        } else if (foundFiles) {
            browser.runCreateContainer(fileList);
        }
        
        return true;
    }
}
