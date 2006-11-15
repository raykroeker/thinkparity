/**
 * Created On: 14-Nov-06 11:01:11 PM
 * $Id$
 */
package com.thinkparity.codebase.swing;

import java.io.File;

import javax.swing.JFileChooser;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class JFileChooserUtil {
    
    /** A singleton instance. */
    private static final JFileChooserUtil SINGLETON;

    static {
        final JFileChooser jFileChooser = new JFileChooser();
        SINGLETON = new JFileChooserUtil(jFileChooser);
    }
    
    /**
     * Get a JFileChooser.
     * 
     * @return The JFileChooser.
     */
    public static JFileChooser getJFileChooser() {
        return SINGLETON.jFileChooser;
    }
    
    /**
     * Get a JFileChooser.
     * 
     * @param fileSelectionMode
     *            The JFileChooser file selection mode.
     * @param multiSelectionEnabled
     *            true if multi selection is enabled.
     * @param dialogTitle
     *            The dialog title.            
     * @param currentDirectory
     *            The JFileChooser current directory.                      
     * @return The JFileChooser.
     */
    public static JFileChooser getJFileChooser(final Integer fileSelectionMode, final Boolean multiSelectionEnabled,
            final String dialogTitle, final File currentDirectory) {
        SINGLETON.jFileChooser.setFileSelectionMode(fileSelectionMode);
        SINGLETON.jFileChooser.setMultiSelectionEnabled(multiSelectionEnabled);
        if (null != dialogTitle) {
            SINGLETON.jFileChooser.setDialogTitle(dialogTitle); 
        }
        if (null != currentDirectory) {
            SINGLETON.jFileChooser.setCurrentDirectory(currentDirectory);
        }

        return SINGLETON.jFileChooser;
    }
    
    /** The JFileChooser. */
    private final JFileChooser jFileChooser;
    
    /**
     *  Create JFileChooserUtil.
     */
    private JFileChooserUtil(final JFileChooser jFileChooser) {
        super();
        this.jFileChooser = jFileChooser;
    }
}
