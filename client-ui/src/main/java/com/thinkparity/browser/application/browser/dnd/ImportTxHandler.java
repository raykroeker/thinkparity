/*
 * Created On: Apr 7, 2006
 */
package com.thinkparity.browser.application.browser.dnd;

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

import com.thinkparity.codebase.FileUtil;
import com.thinkparity.codebase.swing.dnd.TxUtils;

import com.thinkparity.browser.application.browser.Browser;
import com.thinkparity.browser.application.browser.display.avatar.tab.container.ContainerAvatarModel;
import com.thinkparity.browser.application.browser.display.renderer.tab.TabCell;
import com.thinkparity.browser.application.browser.display.renderer.tab.container.ContainerCell;
import com.thinkparity.browser.application.browser.display.renderer.tab.container.DraftDocumentCell;
import com.thinkparity.browser.platform.Platform.Connection;
import com.thinkparity.browser.platform.util.SwingUtil;
import com.thinkparity.browser.platform.util.model.ArtifactUtil;

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
    private ContainerAvatarModel containerAvatarModel;
    
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
            final ContainerAvatarModel containerAvatarModel) {
        super();
        this.browser = browser;
        this.containerAvatarModel = containerAvatarModel;
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
        if(!canImport(comp, t.getTransferDataFlavors())) { return false; }
        
        // Determine the operation. If the user drops onto a container then
        // create documents. If the user drops onto a document then update
        // the document. If the user drops into the area below the last entry
        // or there are no jList entries, create a package.
        final ContainerCell cellContainer;
        final DraftDocumentCell cellDocument;

        if (jList.getSelectedIndex() == -1) {
            // No entries in the JList
            return createPackage(t);
        } else {
            if (pointBelowLastJListEntry(jList)) {
                return createPackage(t);
            } else {
                final TabCell tabCell = (TabCell) jList.getSelectedValue();
                if (tabCell instanceof ContainerCell) {
                    return createDocument(t, (ContainerCell) tabCell);
                } else if (tabCell instanceof DraftDocumentCell) {
                    cellDocument = (DraftDocumentCell) tabCell;
                    cellContainer = (ContainerCell) cellDocument.getParent().getParent();
                    return updateDocument(t, cellContainer, cellDocument);
                } else {
                    return false;
                }
            }
        }
    }

    /**
     * Create documents from the transferable files.
     * 
     * @param t
     *            The java DND transferable.
     * @param cellContainer
     *            The container tab container cell.
     * @return Success fail condition of the creation.
     */
    private boolean createDocument(final Transferable t,
            final ContainerCell cellContainer) {
        // Error (and stop) if the user doesn't have the key for this
        // container
        if (!cellContainer.isKeyHolder()) {
            browser.displayErrorDialog("ErrorCreateDocumentLackKey",
                    new Object[] { cellContainer.getName() });
            return false;
        }

        File[] files = null;
        try {
            files = TxUtils.extractFiles(t);
        } catch (final IOException iox) {
            logger.error(IMPORT_IOX, iox);
        } catch (final UnsupportedFlavorException ufx) {
            logger.error(IMPORT_UFX, ufx);
            return false;
        }
        
        // Get the list of documents in this package.
        final List <String> existingDocuments = containerAvatarModel.getDocumentNames(cellContainer);
        
        // Determine the list of files to add and/or update. Check if the user
        // is trying to drag folders. Create two lists, one for adding and one
        // for updating, depending on whether there is a document of the same
        // name found in the package.
        final List<File> addFileList = new LinkedList<File>();
        final List<File> updateFileList = new LinkedList<File>();
        Boolean foundFolders = Boolean.FALSE;
        Boolean foundFilesToAdd = Boolean.FALSE;
        Boolean foundFilesToUpdate = Boolean.FALSE;
        for (final File file : files) {
            if (file.isDirectory()) {
                foundFolders = Boolean.TRUE;
            } else {
                if (existingDocuments.contains(file.getName())) {
                    foundFilesToUpdate = Boolean.TRUE;
                    updateFileList.add(file);
                }
                else {
                    foundFilesToAdd = Boolean.TRUE;
                    addFileList.add(file);
                }
            }
        }

        // Report an error if the user tries to drag folders. If the user drags
        // a mix of files and folders then the files will import successfully.
        if (foundFolders) {
            browser.displayErrorDialog("ErrorCreateDocumentIsFolder");
        }

        // Add one or more documents.
        if (foundFilesToAdd) {
            try {
                browser.runAddContainerDocuments(cellContainer.getId(), addFileList.toArray(new File[] {}));
            } catch (final Exception x) {
                logger.error(IMPORT_X, x);
                return false;
            }
        }
        
        // Update one or more documents. Note that file.getName() is identical to the name
        // of the document since we used existingDocuments.contains() above.
        if (foundFilesToUpdate) {
            for (final File file : updateFileList) {
                final Long documentId = containerAvatarModel.getDocumentId(cellContainer, file.getName());
                if (documentId != null) {
                    runUpdateDocumentDraft(documentId, file);
                }
            }
        }
        
        return true;
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
     * @param documentId
     *          A document id.
     * @param file
     *		A file.
     */
    private void runUpdateDocumentDraft(final Long documentId, final File file) {
        if (containerAvatarModel.isDraftModified(documentId)) {
            if (browser.confirm("ConfirmOverwriteWorking",
		    new Object[] { file.getName() })) {
                browser.runUpdateDocumentDraft(documentId, file);
            }
        } else {
            browser.runUpdateDocumentDraft(documentId, file);
        }             
    }
    
    private boolean updateDocument(final Transferable t,
	    ContainerCell cellContainer, DraftDocumentCell draftDocument) {

        // Error (and stop) if the user doesn't have the key for this
        // container
        if (!cellContainer.isKeyHolder()) {
            browser.displayErrorDialog("ErrorUpdateDocumentLackKey",
                    new Object[] { cellContainer.getName(), draftDocument.getName() });
            return false;
        }
        
        File[] files = null;
        try {
            files = TxUtils.extractFiles(t);
        } catch (final IOException iox) {
            logger.error(IMPORT_IOX, iox);
        } catch (final UnsupportedFlavorException ufx) {
            logger.error(IMPORT_UFX, ufx);
            return false;
        }

        // Determine the file to update. Check if the user is trying to drag
        // folders or multiple files.
        File updateFile = null;
        Boolean foundFolders = Boolean.FALSE;
        int foundFiles = 0;
        for (final File file : files) {
            if (file.isDirectory()) {
                foundFolders = Boolean.TRUE;
            } else {
                foundFiles++;
                updateFile = file;
            }
        }
        
        // Report an error (and stop) if the user tries to drag folders.
        if (foundFolders) {
            browser.displayErrorDialog("ErrorUpdateDocumentIsFolder");
            return false;
        }
        
        // Report an error (and stop) if the user tries to drag more than one file.
        if (foundFiles>1) {
            browser.displayErrorDialog("ErrorUpdateDocumentMultipleFiles");
            return false;            
        }
        
        // Report an error (and stop) if the file extensions are different.
        final String ext1 = ArtifactUtil.getNameExtension(draftDocument);
        final String ext2 = FileUtil.getExtension(updateFile);
        if (!ext1.equals(ext2)) {
            browser.displayErrorDialog("ErrorUpdateDocumentDifferentExtension");
            return false;    
        }
        
        // If the file names are different then ask the user if the file should be replaced.
        // The content of the new file will be used, but the name of the document won't change.
        if (!draftDocument.getName().equals(updateFile.getName())) {
            if (!browser.confirm("ConfirmUpdateDocumentDifferentName",
                    new Object[] { draftDocument.getName(), updateFile.getName() })) {
                return false;
            }
        }
        
        // Replace the document
        runUpdateDocumentDraft(draftDocument.getId(), updateFile);
        return true;        
    }
}
