/*
 * Apr 6, 2006
 */
package com.thinkparity.browser.application.browser.display.avatar.main;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.List;

import com.thinkparity.codebase.assertion.Assert;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class BrowserMainListTx implements Transferable {

    /** The transfer data */
    private final List data;

    /** Create a BrowserMainListTx */
    public BrowserMainListTx(final List data) {
        super();
        this.data = data;
    }

    /**
     * @see java.awt.datatransfer.Transferable#getTransferData(java.awt.datatransfer.DataFlavor)
     */
    public Object getTransferData(final DataFlavor flavor)
            throws UnsupportedFlavorException, IOException {
        Assert.assertTrue(
                "[BROWSER2] [APP] [B2] [LIST TX DATA NOT SUPPORTED]",
                isDataFlavorSupported(flavor));
        return data;
    }

    /**
     * @see java.awt.datatransfer.Transferable#getTransferDataFlavors()
     */
    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[] {DataFlavor.javaFileListFlavor};
    }

    /**
     * @see java.awt.datatransfer.Transferable#isDataFlavorSupported(java.awt.datatransfer.DataFlavor)
     */
    public boolean isDataFlavorSupported(final DataFlavor flavor) {
        if(DataFlavor.javaFileListFlavor.equals(flavor)) { return true; }
        return false;
    }

}
