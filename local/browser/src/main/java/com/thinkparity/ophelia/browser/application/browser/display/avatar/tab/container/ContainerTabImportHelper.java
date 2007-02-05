/**
 * Created On: Feb 4, 2007 10:11:34 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.container;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.swing.dnd.TxUtils;

import com.thinkparity.ophelia.browser.BrowserException;
import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanel;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.ContainerPanel;
import com.thinkparity.ophelia.browser.util.DocumentUtil;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class ContainerTabImportHelper {

    /** The application. */
    private final Browser browser;

    /** A <code>ContainerModel</code>. */
    private final ContainerTabModel model;

    /**
     * Create a ContainerTabImportHelper.
     */
    ContainerTabImportHelper(final ContainerTabModel model, final Browser browser) {
        super();
        this.browser = browser;
        this.model = model;
    }

    /**
     * Check if the user can import data (ie. create a container and add documents).
     * 
     * @param transferFlavors
     *            An array of <code>DataFlavor</code>.
     * @return true if the import is allowed; false otherwise.        
     */
    protected boolean canImportData(final DataFlavor[] transferFlavors) {
        return TxUtils.containsJavaFileList(transferFlavors);
    }

    /**
     * Check if the user can import data into a container (ie. add and/or
     * update documents).
     * 
     * @param tabPanel
     *            A <code>TabPanel</code>.
     * @param transferFlavors
     *            An array of <code>DataFlavor</code>.
     * @return true if the import is allowed; false otherwise.  
     */
    protected boolean canImportData(final TabPanel tabPanel,
            final DataFlavor[] transferFlavors) {
        if (TxUtils.containsJavaFileList(transferFlavors)) {
            return canImportData(((ContainerPanel) tabPanel).getContainer());
        }
        return false;
    }

    /**
     * Import data (ie. create a container and add documents).
     * 
     * @param transferable
     *            Import data <code>Transferable</code>.
     */
    protected void importData(final Transferable transferable) {
        Assert.assertTrue(canImportData(transferable.getTransferDataFlavors()),
                "Cannot import data {0}.", transferable);
        final List<File> transferableFiles = extractFiles(transferable);
        
        // Check if the user is trying to import a folder.
        // If so, report an error and stop.
        if (containsDirectory(transferableFiles)) {
            browser.displayErrorDialog("ErrorCreatePackageIsFolder");
            return;
        }
        
        // Create a package and add documents.
        if (0 < transferableFiles.size()) {
            browser.runCreateContainer(transferableFiles);
        }
    }

    /**
     * Import data into a container (ie. add and/or update documents).
     * 
     * @param c
     *            A <code>Container</code>.
     * @param transferable
     *            Import data <code>Transferable</code>.
     */
    protected void importData(final Container container,
            final Transferable transferable) {
        final List<File> transferableFiles = extractFiles(transferable);
        
        // Check if the user is trying to import a folder.
        // If so, report an error and stop.
        if (containsDirectory(transferableFiles)) {
            browser.displayErrorDialog("ErrorAddDocumentIsFolder");
            return;
        }

        // If the draft is required, attempt to get it. This should succeed
        // unless somebody managed to get the draft, or the system went offline,
        // since the call to canImport() above.
        Boolean isLocalDraft = container.isLocalDraft();
        if (!container.isLocalDraft() && !container.isDraft() && model.isOnline()) {
            final Long containerId = container.getId();
            browser.runCreateContainerDraft(containerId);
            
            // If all is well, the container is out of date because syncContainer()
            // was called. Get the updated one.
            final TabPanel tabPanel = model.lookupPanel(containerId);
            Assert.assertNotNull(tabPanel, "Error creating draft during import data.");
            isLocalDraft = ((ContainerPanel) tabPanel).getContainer().isLocalDraft();
        }
        
        if (!isLocalDraft) {
            browser.displayErrorDialog("ErrorAddDocumentLackDraft",
                    new Object[] { container.getName() });
            return;
        }

        // Determine which files are for adding and which are for updating.
        // A file is added to the 'update' list if the draft contains a
        // document of the same name; otherwise, the file is added to the 'add' list.
        final DocumentUtil documentUtil = DocumentUtil.getInstance();
        final List<Document> draftDocuments = model.readDraftDocuments(container.getId());
        final List<File> addFileList = new ArrayList<File>();
        final List<File> updateFileList = new ArrayList<File>();
        for (final File transferableFile : transferableFiles) {
            if (documentUtil.contains(draftDocuments, transferableFile)) {
                updateFileList.add(transferableFile);
            } else {
                addFileList.add(transferableFile); 
            }
        }

        // Add one or more documents.
        if (0 < addFileList.size()) {
            browser.runAddContainerDocuments(container.getId(), addFileList
                    .toArray(new File[] {}));
        }

        // Update one or more documents.
        if (0 < updateFileList.size()) {
            for (final File file : updateFileList) {
                final Document document =
                    draftDocuments.get(documentUtil.indexOf(draftDocuments, file));
                if (model.readIsDraftDocumentModified(document.getId())) {
                    if (browser.confirm("ConfirmOverwriteWorking",
                            new Object[] { file.getName() })) {
                        browser.runUpdateDocumentDraft(document.getId(), file);
                    }
                } else {
                    browser.runUpdateDocumentDraft(document.getId(), file);
                } 
            }
        }
    }

    /**
     * Determine if an import can be performed on a container. The user needs to
     * have the draft, or alternatively if nobody has the draft then he needs to
     * be online in order to create a new draft.
     * 
     * @param container
     *            A <code>Container</code>.
     * @return True if an import can be performed; false otherwise.
     */
    private boolean canImportData(final Container container) {
        if (container.isLocalDraft()) {
            return true;                
        } else if (!container.isDraft() && model.isOnline()) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Determines if the list of files contains at least one directory.
     * 
     * @param files
     *            A list of <code>File</code>.
     * @return true if there is at least one directory; false otherwise.
     */
    private Boolean containsDirectory(final List<File> files) {
        for (final File file : files) {
            if (file.isDirectory()) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    /**
     * Extract a list of files from a transferable.
     * 
     * @param transferable
     *            A <code>Transferable</code>.
     * @return A <code>List</code> of <code>File</code>s.
     */
    private List<File> extractFiles(final Transferable transferable) {
        try {
            return TxUtils.extractFiles(transferable);
        } catch (final Exception x) {
            throw new BrowserException("Cannot extract files from transferable.", x);
        }
    }
}
