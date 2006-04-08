/*
 * Apr 7, 2006
 */
package com.thinkparity.browser.javax.swing.dnd;

import java.awt.datatransfer.DataFlavor;

/**
 * Swing drag'n'drop transfer utilities.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class TxUtils {

    /** A singleton instance. */
    private static final TxUtils SINGLETON;

    static { SINGLETON = new TxUtils(); }

    /**
     * Determine if the transfer flavors contains a java file list transfer
     * flavor.
     * 
     * @param transferFlavors
     *            A list of transfer flavors.
     * @return True if the java file list transfer flavor is contained in the
     *         list.
     */
    public static boolean containsJavaFileList(
            final DataFlavor[] transferFlavors) {
        return SINGLETON.doesContainJavaFileList(transferFlavors);
    }

    /** Create a TxUtils [Singleton] */
    private TxUtils() { super(); }

    /**
     * Determine if the transfer flavors contains a java file list transfer
     * flavor.
     * 
     * @param transferFlavors
     *            A list of transfer flavors.
     * @return True if the java file list transfer flavor is contained in the
     *         list.
     */
    private boolean doesContainJavaFileList(final DataFlavor[] transferFlavors) {
        for(final DataFlavor transferFlavor : transferFlavors) {
            if(DataFlavor.javaFileListFlavor.equals(transferFlavor)) {
                return true;
            }
        }
        return false;
    }
}
