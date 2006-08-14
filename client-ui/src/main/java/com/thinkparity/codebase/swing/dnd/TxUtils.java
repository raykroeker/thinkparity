/*
 * Apr 7, 2006
 */
package com.thinkparity.codebase.swing.dnd;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

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

    /**
     * Extract a list of files from the transferable.
     * 
     * @param t
     *            The dnd transferable.
     * @return A list of files
     * @throws IOException
     * @throws UnsupportedFlavorException
     */
    public static File[] extractFiles(final Transferable t) throws IOException,
            UnsupportedFlavorException {
        return SINGLETON.doExtractFiles(t);
    }

    /** An apache logger. */
    protected final Logger logger;

    /** Create a TxUtils [Singleton] */
    private TxUtils() {
        super();
        this.logger = Logger.getLogger(getClass());
    }

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

    /**
     * Extract a list of files from the transferable.
     * 
     * @param t
     *            The dnd transferable.
     * @return A list of files
     * @throws IOException
     * @throws UnsupportedFlavorException
     */
    private File[] doExtractFiles(final Transferable t) throws IOException,
            UnsupportedFlavorException {
        final List data = (List) t.getTransferData(DataFlavor.javaFileListFlavor);
        final List<File> fileData = new LinkedList<File>();
        File file;
        for(final Object datum : data) {
            file = (File) datum;
            if(!file.exists()) {
                logger.warn("[BROWSER2] [SWINGX] [DND] [EXTRACT FILES] [FILE DOESN'T EXIST]");
                try { Thread.sleep(500); }
                catch(final InterruptedException ix) { /* Do nothing. */ }
                if(!file.exists()) {
                    logger.warn("[BROWSER2] [SWINGX] [DND] [EXTRACT FILES] [FILE DOESN'T EXIST]");
                }
            }
            fileData.add(file);
        }
        return fileData.toArray(new File[] {});
    }
}
