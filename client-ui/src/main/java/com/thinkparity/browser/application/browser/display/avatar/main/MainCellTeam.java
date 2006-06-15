/*
 * Created On: Jun 9, 2006 8:41:36 AM
 * $Id$
 */
package com.thinkparity.browser.application.browser.display.avatar.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.border.Border;

import com.thinkparity.browser.application.browser.BrowserConstants;
import com.thinkparity.browser.application.browser.display.avatar.main.MainCellImageCache.HistoryItemIcon;
import com.thinkparity.browser.application.browser.display.avatar.main.MainCellImageCache.HistoryItemImage;
import com.thinkparity.browser.platform.util.l10n.MainCellL18n;

import com.thinkparity.model.xmpp.user.User;

/**
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class MainCellTeam implements MainCell {

    /** The history item's document. */
    private final MainCellDocument document;

    /** An image cache. */
    private final MainCellImageCache imageCache;

    /** Localisation access. */
    private final MainCellL18n l18n;

    /** The the team. */
    private final List<MainCellUser> team;

    /**
     * Create a MainCellHistoryItem.
     * 
     */
    public MainCellTeam(final MainCellDocument document, final List<MainCellUser> team) {
        super();
        this.document = document;
        this.imageCache = new MainCellImageCache();
        this.l18n = new MainCellL18n("Team");
        this.team = team;
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#canImport()
     * 
     */
    public boolean canImport() { return false; }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     * 
     */
    public boolean equals(final Object obj) {
        if(null != obj && obj instanceof MainCellTeam) {
            return ((MainCellTeam) obj).document.equals(document) &&
                    ((MainCellTeam) obj).team.equals(team);
        }
        return false;
    }


    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#getBackground()
     * 
     */
    public BufferedImage getBackground() {
        if(document.isClosed()) { return imageCache.read(HistoryItemImage.BG_CLOSED); }
        else if(document.isUrgent()) { return imageCache.read(HistoryItemImage.BG_URGENT); }
        else { return imageCache.read(HistoryItemImage.BG_DEFAULT); }
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#getBackgroundSelected()
     * 
     */
    public BufferedImage getBackgroundSelected() {
        if(document.isClosed()) { return imageCache.read(HistoryItemImage.BG_SEL_CLOSED); }
        else if(document.isUrgent()) { return imageCache.read(HistoryItemImage.BG_SEL_URGENT); }
        else { return imageCache.read(HistoryItemImage.BG_SEL_DEFAULT); }
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#getBorder()
     * 
     */
    public Border getBorder() { return null; }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#getBorderSelected()
     *
     */
     public Border getBorderSelected() { return BorderFactory.createLineBorder(BrowserConstants.Colours.MAIN_CELL_DEFAULT_BORDER2); }

    /**
     * Obtain the document.
     * 
     * @return The main cell document.
     */
    public MainCellDocument getDocument() { return document; }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#getInfoIcon()
     * 
     */
    public ImageIcon getInfoIcon() { return imageCache.read(HistoryItemIcon.INFO_DEFAULT); }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#getNodeIcon()
     * 
     */
    public ImageIcon getNodeIcon() {
        return imageCache.read(HistoryItemIcon.NODE_DEFAULT);
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#getNodeIconSelected()
     * 
     */
    public ImageIcon getNodeIconSelected() {
        return imageCache.read(HistoryItemIcon.NODE_DEFAULT_SELECTED);
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#getText()
     * 
     */
    public String getText() {
        final StringBuffer text = new StringBuffer(l18n.getString("Team"));
        String localKey;
        User teamMember;
        for(int i = 0; i < team.size(); i++) {
            teamMember = team.get(i);
            if(isSetOrganization(teamMember)) {
                if(0 == i) { localKey = "TeamMemberOrg.0"; }
                else { localKey = "TeamMemberOrg.N"; }
            }
            else {
                if(0 == i) { localKey = "TeamMember.0"; }
                else { localKey = "TeamMember.N"; }
            }
            
            text.append(l18n.getString(localKey, new Object[] {teamMember.getName(), teamMember.getOrganization()}));
        }
        return text.toString();
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#getTextFont()
     * 
     */
    public Font getTextFont() { return BrowserConstants.Fonts.SmallFont; }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#getTextForeground()
     * 
     */
    public Color getTextForeground() { return document.getTextForeground(); }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#getTextInsetFactor()
     * 
     */
    public Float getTextInsetFactor() { return 3.0F; }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#getToolTip()
     */
    public String getToolTip() { return null; }

    /**
     * @see java.lang.Object#hashCode()
     * 
     */
    public int hashCode() { return document.hashCode() & "Team".hashCode(); }

    protected Boolean isSetOrganization(final User teamMember) {
        return null != teamMember.getOrganization() && 0 < teamMember.getOrganization().length();
    }
}
