/**
 * Created On: 22-Sep-06 1:27:39 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container;

import javax.swing.ImageIcon;
import javax.swing.border.Border;

import com.thinkparity.ophelia.browser.Constants.InsetFactors;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.main.MainCellImageCacheTest.TabCellIconTest;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.DefaultTabCell;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell;
import com.thinkparity.ophelia.browser.util.localization.MainCellL18n;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class ContainerVersionDocumentFolderCell extends DefaultTabCell {
    
    /** The version. */
    private final ContainerVersionCell version;
    
    /** The draft cell localization. */
    private final MainCellL18n localization; 

    /** Create ContainerVersionDocumentFolderCell. */
    public ContainerVersionDocumentFolderCell(final ContainerVersionCell version) {      
        super();
        this.version = version;
        this.localization = new MainCellL18n("MainCellContainerVersionDocumentFolder");
        setExpanded(Boolean.TRUE);  // Default expanded
    }
    
    /**
     * @see com.thinkparity.codebase.model.artifact.Artifact#equals(java.lang.Object)
     * 
     */
    public boolean equals(final Object obj) {
        if (null != obj && obj instanceof ContainerVersionDocumentFolderCell) {
            return ((ContainerVersionDocumentFolderCell) obj).version.equals(version);
        }
        return false;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getBorder(int)
     * 
     */
    public Border getBorder(final int index, final Boolean isFirstInGroup, final Boolean lastCell) {
        return getParent().getBorder(index, isFirstInGroup, lastCell);
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getParent()
     * 
     */
    public TabCell getParent() {
        return version;
    }
    
    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getSecondNodeIcon()
     */
    public ImageIcon getSecondNodeIcon() {
        if (isExpanded()) {
            return imageCacheTest.read(TabCellIconTest.FOLDER_OPEN); 
        } else {
            return imageCacheTest.read(TabCellIconTest.FOLDER_CLOSED); 
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getTextNoClipping(com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell.TextGroup)
     *
     */
    public String getTextNoClipping(TextGroup textGroup) {
        if (textGroup == TextGroup.WEST) {
            return localization.getString("Text");
        } else {
            return null;
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getTextInsetFactor()
     * 
     */
    public Float getTextInsetFactor() {
        return InsetFactors.LEVEL_3;
    }

    /**
     * @see com.thinkparity.codebase.model.artifact.Artifact#hashCode()
     * 
     */
    @Override
    public int hashCode() {
        return toString().hashCode();
    }
    
    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return new StringBuffer(getClass().getName()).append("//")
            .append(version.getArtifactId()).append("/")
            .append(version.getVersionId())
            .toString();
    }
    
    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#isChildren()
     */
    public Boolean isChildren() {
        return (Boolean.TRUE);
    }  
}
