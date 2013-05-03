/*
 * Created On: Aug 30, 2006 10:51:00 AM
 */
package com.thinkparity.codebase.swing;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;

import javax.swing.JTextArea;


/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ClipboardUtils implements ClipboardOwner {

    /** A singleton instance. */
    private static final ClipboardUtils SINGLETON;

    static {
        SINGLETON = new ClipboardUtils();
    }

    /**
     * Copy the text of the text area into the clipboard.
     * 
     * @param jTextArea
     *            A <code>JTextArea</code>.
     */
    public static void copy(final JTextArea jTextArea) {
        synchronized (SINGLETON) {
            SINGLETON.doCopy(jTextArea);
        }
    }

    /**
     * Copy a string into the clipboard.
     * 
     * @param string
     *            A <code>String</code>.
     */
    public static void copy(final String string) {
        synchronized (SINGLETON) {
            SINGLETON.doCopy(string);
        }
    }

    /** The system clipboard. */
    private final Clipboard clipboard;

    /** Create ClipboardUtils. */
    private ClipboardUtils() {
        super();
        this.clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    }

    /**
     * @see java.awt.datatransfer.ClipboardOwner#lostOwnership(java.awt.datatransfer.Clipboard, java.awt.datatransfer.Transferable)
     */
    public void lostOwnership(final Clipboard clipboard,
            final Transferable contents) {
        // NOTE Do nothing if the clipboard contents are lost.
    }

    /**
     * Copy the contents of the text area to the clipboard.
     * 
     * @param jTextArea
     *            A text area.
     */
    private void doCopy(final JTextArea jTextArea) {
        final String text = SwingUtil.extract(jTextArea);
        if (null != text)
            doCopy(text);
    }

    /**
     * Copy a string into the clipboard.
     * 
     * @param string
     *            A <code>String</code>.
     */
    private void doCopy(final String string) {
        final StringSelection stringSelection = new StringSelection(string);
        clipboard.setContents(stringSelection, this);
    }
}
