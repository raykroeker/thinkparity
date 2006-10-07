/*
 * Created On: Apr 7, 2006
 */
package com.thinkparity.ophelia.browser.application.browser.dnd;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.TransferHandler;

import org.apache.log4j.Logger;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.swing.SwingUtil;
import com.thinkparity.codebase.swing.dnd.TxUtils;
import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.container.ContainerModel;
import com.thinkparity.ophelia.browser.platform.Platform.Connection;

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

    /** An apache logger error statement. */
    private static final String IMPORT_IOX = "[BROWSER2] [APP] [B2]  [DND] [IMPORT DATA] [IO ERROR]";

    /** An apache logger error statement. */
    private static final String IMPORT_MX = "[BROWSER2] [APP] [B2]  [DND] [IMPORT DATA] [MOUSE INFO ERROR]";

    /** An apache logger error statement. */
    private static final String IMPORT_UFX = "[BROWSER2] [APP] [B2]  [DND] [IMPORT DATA] [UNSUPPORTED DATA FORMAT]";
    
    /** An apache logger error statement. */
    private static final String IMPORT_X = "[BROWSER2] [APP] [B2]  [DND] [IMPORT DATA] [ERROR]";

    /** @see java.io.Serializable */
    private static final long serialVersionUID = 1;

    /** An apache logger. */
    protected final Logger logger;

    /** The browser application. */
    private final Browser browser;
    
    /** The model */
    private ContainerModel containerModel;
    
    /** The list we are droping on. */
    private final JList jList;

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
        this.containerModel = containerModel;
        this.jList = jList;
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

    private boolean createPackage(final Transferable t) {
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
        final List<File> fileList = new LinkedList<File>();
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
        
        // Report an error if the user tries to drag folders. If the user drags
        // a mix of files and folders then we will still create a package.
        if (foundFolders) {
            browser.displayErrorDialog("ErrorCreatePackageIsFolder");
        }
        
        // Create a package and add documents.
        if (foundFiles) {
            browser.runCreateContainer(fileList);
        }
        
        return true;
    }
    
    private Boolean pointBelowLastJListEntry(final JList jList) {
        Boolean below = Boolean.FALSE;
        try {
            // These points are in screen coordinate space.
            Point pointJListTopLeft = jList.getLocationOnScreen();            
            Point pointScreen = MouseInfo.getPointerInfo().getLocation();
            Point pointJListRelative = new Point(
                    (int) (pointScreen.getX() - pointJListTopLeft.getX()),
                    (int) (pointScreen.getY() - pointJListTopLeft.getY()));
            
            final Integer listIndex = jList.locationToIndex(pointJListRelative);
            final Rectangle cellBounds = jList.getCellBounds(listIndex, listIndex);
            if (!SwingUtil.regionContains(cellBounds,pointJListRelative)) {
                below = Boolean.TRUE;
            }
        }
        catch(Exception mx) {
            logger.error(IMPORT_MX, mx);
        }
        
        return below;    
    }

    /**
     * Run the update document draft action via the browser.
     * 
     * @param containerId
     *          A container id.
     * @param documentId
     *          A document id.
     * @param file
     *		A file.
     */
    private void runUpdateDocumentDraft(final Long containerId, final Long documentId, final File file) {
    	Assert.assertNotYetImplemented("runUpdateDocumentDraft");
    }
}
