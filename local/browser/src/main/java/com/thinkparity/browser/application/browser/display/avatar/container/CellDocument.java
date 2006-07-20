/**
 * Created On: 17-Jul-06 1:57:42 PM
 * $Id$
 */
package com.thinkparity.browser.application.browser.display.avatar.container;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.border.Border;

import com.thinkparity.browser.application.browser.BrowserConstants;
import com.thinkparity.browser.application.browser.display.avatar.main.MainCell;
import com.thinkparity.browser.application.browser.display.avatar.main.MainCellImageCache;
import com.thinkparity.browser.application.browser.display.avatar.main.MainCellImageCache.DocumentIcon;
import com.thinkparity.browser.application.browser.display.avatar.main.MainCellImageCache.DocumentImage;

import com.thinkparity.model.parity.model.artifact.ArtifactFlag;
import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.document.DocumentModel;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class CellDocument extends Document implements MainCell  {
    
    /** The document's container. */
    private final CellContainer container;

    /** The cell's text foreground color. */
    private static final Color TEXT_FG;

    /** The cell's text foreground colour for closed documents. */
    private static final Color TEXT_FG_CLOSED;

    /** Maximum length of a document cell's text. */
    private static final Integer TEXT_MAX_LENGTH;

    static {
        TEXT_FG = Color.BLACK;
        TEXT_FG_CLOSED = new Color(127, 131, 134, 255);

        TEXT_MAX_LENGTH = 60;
    }

    /** A flag indicating whether or not the cell is urgent. */
    private Boolean urgent = Boolean.FALSE;    
    
    /** A flag indicating whether or not the cell has been seen. */
    private Boolean seen = Boolean.FALSE;    

    /** The parity document interface. */
    private DocumentModel dModel;

    /** An image cache. */
    private final MainCellImageCache imageCache;

    /**
     * Create a CellDocument.
     */
    public CellDocument(final CellContainer container, final DocumentModel dModel, final Document d ) {
        super(d.getCreatedBy(), d.getCreatedOn(), d.getDescription(),
                d.getFlags(), d.getUniqueId(), d.getName(), d.getUpdatedBy(),
                d.getUpdatedOn());
        setId(d.getId());
        setRemoteInfo(d.getRemoteInfo());
        setState(d.getState());
        
        this.container = container;
        //this.dModel = dModel;
        this.imageCache = new MainCellImageCache();        

        this.urgent = Boolean.FALSE;
        this.seen = contains(ArtifactFlag.SEEN);        
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#canImport()
     * 
     */
    // TO DO is this right?
    public boolean canImport() { return false; }

    /**
     * @see com.thinkparity.model.parity.model.artifact.Artifact#equals(java.lang.Object)
     * 
     */
    public boolean equals(final Object obj) {
        return super.equals(obj);
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#fireSelection()
     * 
     */
    //public void fireSelection() {}
    
    /**
     * Obtain the background image for a cell.
     * 
     * 
     * @return A buffered image.
     */
    public BufferedImage getBackground() {
        if(container.isClosed()) { return imageCache.read(DocumentImage.BG_CLOSED); }
        else if(isUrgent()) { return imageCache.read(DocumentImage.BG_URGENT); }
        else { return imageCache.read(DocumentImage.BG_DEFAULT); }
    }
    
    /**
     * Obtain the background image for a selected cell.
     * 
     * 
     * @return A buffered image.
     */
    public BufferedImage getBackgroundSelected() {
        if(container.isClosed()) { return imageCache.read(DocumentImage.BG_SEL_CLOSED); }
        else if(isUrgent()) { return imageCache.read(DocumentImage.BG_URGENT); }
        else { return imageCache.read(DocumentImage.BG_SEL_DEFAULT); }
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#getBorder()
     * 
     */
    // TO DO is this right?
    public Border getBorder() { return null; }

    /**
     * Obtain an info icon.
     * 
     * 
     * @return An image icon.
     */
    public ImageIcon getInfoIcon() {
        if(container.isKeyHolder()) { return imageCache.read(DocumentIcon.INFO_IS_KEYHOLDER); }
        else { return imageCache.read(DocumentIcon.INFO_IS_NOT_KEYHOLDER); }
    }

    /**
     * Obtain the document cell's key requests.
     * 
     * @return The document cell's key requests.
     */
    //public List<KeyRequest> getKeyRequests() { return keyRequests; }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#getNodeIcon()
     * 
     */
    public ImageIcon getNodeIcon() {
        return imageCache.read(DocumentIcon.NODE_DEFAULT);
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#getNodeIconSelected()
     * 
     */
    public ImageIcon getNodeIconSelected() {
        return imageCache.read(DocumentIcon.NODE_SEL_DEFAULT);
    }

    /**
     * Obtain the team.
     *
     * @return A set of users.
     */
    //public Set<User> getTeam() { return team; }
    
    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#getText()
     * 
     */
    public String getText() {
        if(TEXT_MAX_LENGTH < getName().length()) {
            return getName().substring(0, TEXT_MAX_LENGTH - 1 - 3) + "...";
        }
        else { return getName(); }
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#getTextFont()
     * 
     */
    public Font getTextFont() {
        if(isSeen()) { return BrowserConstants.Fonts.DefaultFont; }
        else { return BrowserConstants.Fonts.DefaultFontBold; }
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#getTextForeground()
     * 
     */
    public Color getTextForeground() {
        if(container.isClosed()) { return TEXT_FG_CLOSED; }
        else { return TEXT_FG; }
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#getTextInsetFactor()
     * 
     */
    public Float getTextInsetFactor() { return 3.0F; }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#getToolTip()
     * 
     */
    public String getToolTip() {
        if(TEXT_MAX_LENGTH < getName().length()) { return getName(); }
        else { return null; }
    }

    /**
     * @see com.thinkparity.model.parity.model.artifact.Artifact#hashCode()
     * 
     */
    public int hashCode() { return super.hashCode(); }

    /**
     * Determine whether or not the document has been distributed.
     * 
     * @return True if the document has been distributed.
     */
    //public Boolean isDistributed() { return dModel.isDistributed(getId()); }

    /**
     * Determine whether or not the document cell has been seen.
     * 
     * @return True if the document has been seen; false otherwise.
     */
    public Boolean isSeen() { return seen; }

    /**
     * Determine whether or not the document is urgent.
     * 
     * @return True if the document is urgent.
     */
    public Boolean isUrgent() { return urgent; }

    /**
     * Determine whether or not the working version has been modified.
     *
     * @return True if the working version has not been modified; false
     * otherwise.
     */
/*    public Boolean isWorkingVersionEqual() {
        try { return dModel.isWorkingVersionEqual(getId()); }
        catch(final ParityException px) { throw new RuntimeException(px); }
    }*/
}
