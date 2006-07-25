/*
 * Apr 7, 2006
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

import com.thinkparity.browser.application.browser.Browser;
import com.thinkparity.browser.application.browser.display.avatar.BrowserContainersModel;
import com.thinkparity.browser.application.browser.display.avatar.container.CellContainer;
import com.thinkparity.browser.application.browser.display.avatar.container.CellDocument;
import com.thinkparity.browser.application.browser.display.avatar.main.MainCell;
import com.thinkparity.browser.javax.swing.dnd.TxUtils;
import com.thinkparity.browser.platform.Platform.Connection;
import com.thinkparity.browser.platform.util.SwingUtil;
import com.thinkparity.browser.platform.util.log4j.LoggerFactory;

import com.thinkparity.model.parity.ParityException;
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
    private static final String IMPORT_IOX = "[BROWSER2] [APP] [B2]  [DND] [IMPORT DATA] [IO ERROR]";

    /** An apache error message. */
    private static final String IMPORT_PX = "[BROWSER2] [APP] [B2] [DND] [IMPORT DATA] [PARITY LMODEL ERROR]";

    /** An apache logger error statement. */
    private static final String IMPORT_X = "[BROWSER2] [APP] [B2]  [DND] [IMPORT DATA] [ERROR]";

    /** An apache logger error statement. */
    private static final String IMPORT_UFX = "[BROWSER2] [APP] [B2]  [DND] [IMPORT DATA] [UNSUPPORTED DATA FORMAT]";
    
    /** An apache logger error statement. */
    private static final String IMPORT_MX = "[BROWSER2] [APP] [B2]  [DND] [IMPORT DATA] [MOUSE INFO ERROR]";

    /** @see java.io.Serializable */
    private static final long serialVersionUID = 1;

    /** An apache logger. */
    protected final Logger logger;

    /** The browser application. */
    private final Browser browser;
    
    /** The parity document interface. */
    private final DocumentModel dModel;
    
    /** The list we are droping on. */
    private final JList jList;
    
    /** The model */
    private BrowserContainersModel containersModel;

    /**
     * Create a CreateDocumentTxHandler.
     * 
     */
    public CreateDocumentTxHandler(final Browser browser, final JList jList,
            final BrowserContainersModel containersModel) {
        super();
        this.browser = browser;
        this.dModel = browser.getDocumentModel();
        this.jList = jList;
        this.containersModel = containersModel;
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
        Operation operation = Operation.NONE;
        CellContainer cellContainer = null;
        CellDocument cellDocument = null;
        final MainCell mc = (MainCell) jList.getSelectedValue();
        
        if (jList.getSelectedIndex() == -1) {   // No entries in the JList
            operation = Operation.CREATE_PACKAGE;
        }
        else if (pointBelowLastJListEntry(jList)) {
            operation = Operation.CREATE_PACKAGE;            
        }
        else if(mc instanceof CellContainer) {
            cellContainer = (CellContainer) mc;
            operation = Operation.CREATE_DOCUMENT;
        }
        else if (mc instanceof CellDocument) {
            cellDocument = (CellDocument) mc;
            cellContainer = cellDocument.getContainer();
            operation = Operation.UPDATE_DOCUMENT;
        }
        else {
            return false;
        }

        if (operation == Operation.CREATE_PACKAGE) {
            return( createPackage(t) );
        }
        else if (operation == Operation.CREATE_DOCUMENT) {
            return( createDocument(t, cellContainer) );
        }
        else if (operation == Operation.UPDATE_DOCUMENT){
            return( updateDocument(t, cellContainer, cellDocument) );
        }
        else {
            return false;
        }
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
            browser.userError("ErrorCreatePackageIsFolder");
        }
        
        // Create a package and add documents.
        if (foundFiles) {
            browser.runCreateContainer(fileList);
        }
        
        return true;
    }

    private boolean createDocument(final Transferable t, CellContainer cellContainer) {
        
        // Error (and stop) if the user doesn't have the key for this
        // container
        if (!cellContainer.isKeyHolder()) {
            browser.userError("ErrorCreateDocumentLackKey",
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
        final List <String> existingDocuments = containersModel.getDocumentNames(cellContainer);
        
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
            browser.userError("ErrorCreateDocumentIsFolder");
        }

        // Add one or more documents.
        if (foundFilesToAdd) {
            try {
                browser.runCreateDocuments(cellContainer.getId(), addFileList);
            } catch (final Exception x) {
                logger.error(IMPORT_X, x);
                return false;
            }
        }
        
        // Update one or more documents. Note that file.getName() is identical to the name
        // of the document since we used existingDocuments.contains() above.
        if (foundFilesToUpdate) {
            for (final File file : updateFileList) {
                final Long documentId = containersModel.getDocumentId(cellContainer, file.getName());
                if (documentId != null) {
                    updateOneDocument(file, cellContainer.getId(), documentId, file.getName());                   
                }
            }
        }
        
        return true;
    }
    
    private boolean updateDocument(final Transferable t,
            CellContainer cellContainer, CellDocument cellDocument) {

        // Error (and stop) if the user doesn't have the key for this
        // container
        if (!cellContainer.isKeyHolder()) {
            browser.userError("ErrorUpdateDocumentLackKey",
                    new Object[] { cellContainer.getName(), cellDocument.getName() });
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
            browser.userError("ErrorUpdateDocumentIsFolder");
            return false;
        }
        
        // Report an error (and stop) if the user tries to drag more than one file.
        if (foundFiles>1) {
            browser.userError("ErrorUpdateDocumentMultipleFiles");
            return false;            
        }
        
        // Report an error (and stop) if the file extensions are different.
        String ext1 = getExtension(cellDocument.getName());
        String ext2 = getExtension(updateFile.getName());
        if (!ext1.equals(ext2)) {
            browser.userError("ErrorUpdateDocumentDifferentExtension");
            return false;    
        }
        
        // If the file names are different then ask the user if the file should be replaced.
        // The content of the new file will be used, but the name of the document won't change.
        if (!cellDocument.getName().equals(updateFile.getName())) {
            if (!browser.confirm("ConfirmUpdateDocumentDifferentName",
                    new Object[] { cellDocument.getName(), updateFile.getName() })) {
                return false;
            }
        }
        
        // Replace the document
        updateOneDocument(updateFile, cellContainer.getId(), cellDocument.getId(), cellDocument.getName());    
        
        return true;        
    }
    
    private void updateOneDocument(File updateFile, Long idContainer, Long idDoc, String name) {
        try {
            if (!dModel.isWorkingVersionEqual(idDoc)) {
                if (browser.confirm("ConfirmOverwriteWorking", new Object[] { name })) {
                    dModel.updateWorkingVersion(idDoc, updateFile);
                    browser.fireDocumentUpdated(idContainer,idDoc);
                }
            } else {
                dModel.updateWorkingVersion(idDoc, updateFile);
                browser.fireDocumentUpdated(idContainer,idDoc);
            }
        } catch (final ParityException px) {
            logger.error(IMPORT_PX, px);
        }             
    }
    
    private String getExtension(final String string) {
        String extension = "";
        int dotIndex = string.lastIndexOf((int)'.');
        if (dotIndex>=0) {
            extension = string.substring(dotIndex+1);
        }
        return extension;
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
    
    private enum Operation { CREATE_PACKAGE, CREATE_DOCUMENT, UPDATE_DOCUMENT, NONE }
}
