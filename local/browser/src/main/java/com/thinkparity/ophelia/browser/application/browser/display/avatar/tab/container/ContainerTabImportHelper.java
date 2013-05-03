/**
 * Created On: Feb 4, 2007 10:11:34 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.container;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.util.List;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.swing.dnd.TxUtils;

import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.document.DocumentConstraints;

import com.thinkparity.ophelia.browser.BrowserException;
import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanel;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.ContainerPanel;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class ContainerTabImportHelper {

    /** The application. */
    private final Browser browser;

    /** An instance of <code>DocumentConstraints</code>. */
    private final DocumentConstraints documentConstraints;

    /** The model. */
    private final ContainerTabModel model;

    /**
     * Create a ContainerTabImportHelper.
     */
    ContainerTabImportHelper(final ContainerTabModel model, final Browser browser) {
        super();
        this.model = model;
        this.browser = browser;
        this.documentConstraints = DocumentConstraints.getInstance();
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
            return ((ContainerPanel) tabPanel).isLocalDraft().booleanValue();
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
        browser.moveToFront();

        // Check if the user is trying to import a folder.
        // If so, report an error and stop.
        if (containsDirectory(transferableFiles)) {
            browser.displayErrorDialog("ErrorCreatePackageIsFolder");
            return;
        }

        // Check if the user is trying to import a file with file
        // name too long. If so, report an error and stop.
        if (containsFileNameTooLong(transferableFiles)) {
            browser.displayErrorDialog("ErrorCreatePackageFileNameTooLong",
                    new Object[] {getMaxDocumentNameLength()});
            return;
        }
        
        // Create a container and add documents.
        if (0 < transferableFiles.size()) {
            browser.runCreateContainer(transferableFiles);
        }
    }

    /**
     * Import data into a container (ie. add and/or update documents).
     * 
     * @param container
     *            A <code>Container</code>.
     * @param transferable
     *            Import data <code>Transferable</code>.
     */
    protected void importData(final Container container,
            final Transferable transferable) {
        final List<File> transferableFiles = extractFiles(transferable);
        browser.moveToFront();

        // Check if the user is trying to import a folder.
        // If so, report an error and stop.
        if (containsDirectory(transferableFiles)) {
            browser.displayErrorDialog("ErrorAddDocumentIsFolder");
            return;
        }

        // Check if the user is trying to import a file with file
        // name too long. If so, report an error and stop.
        if (containsFileNameTooLong(transferableFiles)) {
            browser.displayErrorDialog("ErrorAddDocumentFileNameTooLong",
                    new Object[] {getMaxDocumentNameLength()});
            return;
        }

        // Add and/or update documents to the container.
        if (0 < transferableFiles.size()) {
            showPanel(container.getId());
            browser.runAddContainerDocuments(container.getId(), transferableFiles
                    .toArray(new File[] {}));
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
     * Determine if the list of files includes a file name that is too long.
     * 
     * @param files
     *            A list of <code>File</code>.
     * @return true if the list of files includes a file name that is too long.
     */
    private Boolean containsFileNameTooLong(final List<File> files) {
        final int maxLength = getMaxDocumentNameLength();
        for (final File file : files) {
            if (!file.isDirectory()  && file.getName().length() > maxLength) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    /**
     * Get the maximum allowed document name length.
     * 
     * @return The <code>int</code> maximum allowed document name length.
     */
    private int getMaxDocumentNameLength() {
        return documentConstraints.getDocumentName().getMaxLength();
    }

    /**
     * Expand the panel, select it, and select the draft.
     * 
     * @param containerId
     *            A <code>Long</code> container id.
     */
    private void showPanel(final Long containerId) {
        model.selectPanel(containerId);
        model.expandPanel(containerId, Boolean.FALSE);
        model.scrollPanelToVisible(containerId);
        model.setDraftSelection(containerId);
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
