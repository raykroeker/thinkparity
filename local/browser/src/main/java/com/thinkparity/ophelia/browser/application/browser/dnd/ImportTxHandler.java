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

import org.apache.log4j.Logger;

import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;
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

    /** @see java.io.Serializable */
    private static final long serialVersionUID = 1;
    
    /** An apache logger error statement. */
    private static final String IMPORT_IOX = "Browser Drag and Drop IO Error";

    /** An apache logger error statement. */
    private static final String IMPORT_UFX = "Browser Drag and Drop Unsupported Data Format Error";
    
    /** An apache logger error statement. */
    private static final String IMPORT_X = "Browser Drag and Drop Error";

    /** An apache logger. */
    protected final Logger logger;

    /** The browser application. */
    private final Browser browser;
    
    /** The container model. */
    private final ContainerModel containerModel;
    
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
        this.containerModel = containerModel;
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
        this.containerModel = containerModel;
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
            return updateContainer(t);
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
    
    /**
     * Update a container by adding and updating documents from the transferable files.
     * 
     * @param t
     *            The java DND transferable.
     */
    private boolean updateContainer(final Transferable t) {
        List<Document> draftDocuments = null;
        List<DocumentVersion> versionDocuments = null;
        
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
        final List <String> existingDocuments = new ArrayList<String>();
        if (container.isLocalDraft()) {
            draftDocuments = containerModel.getDraftDocuments(container);
            for (final Document document : draftDocuments) {
                existingDocuments.add(document.getName());
            }
        } else {
            versionDocuments = containerModel.getLatestVersionDocuments(container);
            for (final DocumentVersion document : versionDocuments) {
                existingDocuments.add(document.getName());
            }
        }
        
        // Determine the list of files to add and/or update. Check if the user
        // is trying to drag folders. Create two lists, one for adding and one
        // for updating, depending on whether there is a document of the same
        // name found in the package.
        final List<File> addFileList = new ArrayList<File>();
        final List<File> updateFileList = new ArrayList<File>();
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
        
        // Report an error if the user tries to drag folders.
        if (foundFolders) {
            browser.displayErrorDialog("ErrorAddDocumentIsFolder");
            return false;
        }
        
        // If the draft is required, attempt to get it. This should succeed
        // unless somebody managed to get the draft, or the system went offline,
        // since the call to canImport() above.
        if (foundFilesToUpdate || foundFilesToAdd) {
            if (!container.isLocalDraft() && !container.isDraft() &&
                    (Connection.ONLINE == browser.getConnection())) {
                browser.runCreateContainerDraft(container.getId());
            }
            
            if (!container.isLocalDraft()) {
                browser.displayErrorDialog("ErrorAddDocumentLackDraft",
                        new Object[] { container.getName() });
                return false;
            }
            draftDocuments = containerModel.getDraftDocuments(container);
        }
        
        // Add one or more documents.
        if (foundFilesToAdd) {
            try {
                browser.runAddContainerDocuments(container.getId(), addFileList.toArray(new File[] {}));
            } catch (final Exception x) {
                logger.error(IMPORT_X, x);
                return false;
            }
        }
        
        // Update one or more documents.
        if ((foundFilesToUpdate) && (null != draftDocuments)) {
            for (final File file : updateFileList) {
                final Document document = findDocument(file.getName(), draftDocuments);
                if (null != document) {
                    runUpdateDocumentDraft(container.getId(), document.getId(), file);
                }
            }
        }
        
        return true;
    }

    /**
     * Run the update document draft action via the browser.
     * 
     * @param containerId
     *          A container id.
     * @param documentId
     *          A document id.
     * @param file
     *      A file.
     */
    private void runUpdateDocumentDraft(final Long containerId, final Long documentId, final File file) {
        if (containerModel.isDraftDocumentModified(documentId)) {
            if (browser.confirm("ConfirmOverwriteWorking",
            new Object[] { file.getName() })) {
                browser.runUpdateDocumentDraft(documentId, file);
            }
        } else {
            browser.runUpdateDocumentDraft(documentId, file);
        }             
    }
    
    /**
     * Find the Document with matching name from the list.
     * 
     * @param name
     *          A document name.
     * @param documents
     *          A list of documents
     * @return A document.    
     */
    private Document findDocument(final String name, final List<Document> documents) {
        for (final Document document : documents) {
            if (document.getName().equals(name)) {
                return document;
            }
        }
        return null;
    }
}
