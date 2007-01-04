/**
 * Created On: Dec 25, 2006 11:29:54 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.action.container;

import java.awt.event.ActionEvent;
import java.io.File;

import org.jdesktop.jdic.desktop.DesktopException;

import com.thinkparity.ophelia.browser.BrowserException;
import com.thinkparity.ophelia.browser.platform.action.LinkAction;
import com.thinkparity.ophelia.browser.util.jdic.DesktopUtil;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class ExportFileLink implements LinkAction {

    /** The file */
    private File file;
    
    /** The intro text */
    private String introText;
    
    public ExportFileLink(final String introText) {
        super();
        this.introText = introText;
    }
    
    /**
     * Set the file.
     * 
     * @param file
     *            The file.
     */
    public void setFile(final File file) {
        this.file = file;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.LinkAction#getAction()
     */
    public javax.swing.Action getAction() {
        return new javax.swing.AbstractAction() {
            public void actionPerformed(final ActionEvent e) {
                try {
                    DesktopUtil.open(file);
                } catch (final DesktopException dx) {
                    throw new BrowserException("Cannot open file.", dx);
                }
            }
        };
    }
    
    /**
     * @see com.thinkparity.ophelia.browser.platform.action.LinkAction#getActionName()
     */
    public String getActionName() {
        return ("ExportFileLink");
    }
    
    /**
     * @see com.thinkparity.ophelia.browser.platform.action.LinkAction#getIntroText()
     */
    public String getIntroText() {
        return introText;
    }
    
    /**
     * @see com.thinkparity.ophelia.browser.platform.action.LinkAction#getLinkType()
     */
    public LinkType getLinkType() {
        return LinkType.SHOW_ONCE;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.LinkAction#getLinkText()
     */
    public String getLinkText() {
        return file.getAbsolutePath();
    }   
}
