/**
 * Created On: 13-Jul-06 1:19:09 PM
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
import com.thinkparity.browser.application.browser.display.avatar.main.border.DocumentDefault;

import com.thinkparity.model.parity.model.artifact.ArtifactFlag;
import com.thinkparity.model.parity.model.artifact.ArtifactState;
import com.thinkparity.model.parity.model.container.Container;
import com.thinkparity.model.parity.model.container.ContainerModel;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class CellContainer extends Container implements MainCell  {

    /** The cell's text foreground color. */
    private static final Color TEXT_FG;

    /** The cell's text foreground colour for closed containers. */
    private static final Color TEXT_FG_CLOSED;

    /** Maximum length of a container cell's text. */
    private static final Integer TEXT_MAX_LENGTH;

    static {
        TEXT_FG = Color.BLACK;
        TEXT_FG_CLOSED = new Color(127, 131, 134, 255);

        TEXT_MAX_LENGTH = 60;
    }

    /** A flag indicating whether or not the container is closed. */
    private Boolean closed = Boolean.FALSE;
    
    /** A flag indicating whether or not the cell is urgent. */
    private Boolean urgent = Boolean.FALSE;
    
    /** A flag indicating the expand\collapse status. */
    private Boolean expanded = Boolean.FALSE; 
    
    /** A flag indicating whether or not the cell has been seen. */
    private Boolean seen = Boolean.FALSE;
    
    /** A flag indicating whether or not the user is the key holder. */
    private Boolean keyHolder = Boolean.FALSE;
    
    /** The parity container interface. */
    private ContainerModel ctrModel;
    
    /** The parity document interface. */
    //private DocumentModel dModel;

    /** An image cache. */
    private final MainCellImageCache imageCache;

    /** The container's key requests. */
    //private final List<KeyRequest> keyRequests;

    /** The container team. */
    //private final Set<User> team;


    /**
     * Create a CellContainer.
     */
    public CellContainer(final ContainerModel ctrModel, final Container c) {
        super();
        setCreatedBy(c.getCreatedBy());
        setCreatedOn(c.getCreatedOn());
        add(c.getFlags());
        setUniqueId(c.getUniqueId());
        setName(c.getName());
        setUpdatedBy(c.getUpdatedBy());
        setUpdatedOn(c.getUpdatedOn());
        setId(c.getId());
        setRemoteInfo(c.getRemoteInfo());
        setState(c.getState());
        
        this.ctrModel = ctrModel;
        this.imageCache = new MainCellImageCache();
        
        this.closed = getState() == ArtifactState.CLOSED;
        this.urgent = Boolean.FALSE;
        this.seen = contains(ArtifactFlag.SEEN);        
        this.keyHolder = contains(ArtifactFlag.KEY);        
        
        /*
        this.dModel = dModel;
        this.keyRequests = new LinkedList<KeyRequest>();
        this.team = team;
*/
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#canImport()
     * 
     */
    public boolean canImport() { return isKeyHolder(); }

    /**
     * @see com.thinkparity.model.parity.model.artifact.Artifact#equals(java.lang.Object)
     * 
     */
    public boolean equals(final Object obj) { return super.equals(obj); }

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
        if(isClosed()) { return imageCache.read(DocumentImage.BG_CLOSED); }
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
        if(isClosed()) { return imageCache.read(DocumentImage.BG_SEL_CLOSED); }
        else if(isUrgent()) { return imageCache.read(DocumentImage.BG_URGENT); }
        else { return imageCache.read(DocumentImage.BG_SEL_DEFAULT); }
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#getBorder()
     * 
     */
    public Border getBorder() { return new DocumentDefault(); }

    /**
     * Obtain an info icon.
     * 
     * 
     * @return An image icon.
     */
    public ImageIcon getInfoIcon() {
        if(isKeyHolder()) { return imageCache.read(DocumentIcon.INFO_IS_KEYHOLDER); }
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
        if(isExpanded()) { return imageCache.read(DocumentIcon.NODE_SEL_EXPANDED); }
        else { return imageCache.read(DocumentIcon.NODE_SEL_DEFAULT); }
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
        if(isClosed()) { return TEXT_FG_CLOSED; }
        else { return TEXT_FG; }
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#getTextInsetFactor()
     * 
     */
    public Float getTextInsetFactor() { return 1.0F; }

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
    //public int hashCode() { return super.hashCode(); }

    /**
     * Determine whether or not the container is closed.
     * 
     * @return True if the container is closed; false otherwise.
     */
    public Boolean isClosed() { return closed; }
    
    /**
     * Determine whether or not the container is urgent.
     * 
     * @return True if the container is urgent.
     */
    public Boolean isUrgent() { return urgent; }    

    /**
     * Determine whether or not the cell is expanded.
     * 
     * @return True if the cell is expanded.
     */
    public Boolean isExpanded() { return expanded; }
    
    /**
     * Determine whether or not the container cell has been seen.
     * 
     * @return True if the container has been seen; false otherwise.
     */
    public Boolean isSeen() { return seen; }
    
    /**
     * Determine whether or not the user is the container's key holder.
     * 
     * @return True if the user is the key holder.
     */
    public Boolean isKeyHolder() { return keyHolder; }

    /**
     * Determine whether or not the document has been distributed.
     * 
     * @return True if the document has been distributed.
     */
    //public Boolean isDistributed() { return dModel.isDistributed(getId()); }

    /**
     * Determine whether or not the working version has been modified.
     *
     * @return True if the working version has not been modified; false
     * otherwise.
     */
//    public Boolean isWorkingVersionEqual() {
//        try { return dModel.isWorkingVersionEqual(getId()); }
//        catch(final ParityException px) { throw new RuntimeException(px); }
//    }

    /**
     * Set the expanded flag.
     * 
     * @param expanded
     *            The expanded flag.
     */
    public void setExpanded(final Boolean expanded) { this.expanded = expanded; }

    /**
     * Set the document's key requests. This will affect the urgent status of
     * the document.
     * 
     * @param keyRequests
     *            The document's key request.
     */
/*    public void setKeyRequests(final List<KeyRequest> keyRequests) {
        this.keyRequests.clear();
        this.keyRequests.addAll(keyRequests);
    }*/
}
