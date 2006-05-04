/*
 * Apr 11, 2006
 */
package com.thinkparity.browser.platform.application.window;

import java.awt.Color;
import java.util.Hashtable;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import org.apache.log4j.Logger;

import com.thinkparity.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.browser.javax.swing.border.ImageBorder;
import com.thinkparity.browser.platform.util.log4j.LoggerFactory;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class WindowBorder {

    private static final Map<AvatarId, Object> borderMap;

    private static final Object defaultBorder;

    static {
        borderMap = new Hashtable<AvatarId, Object>(1, 1.0F);

        // BORDER Add New Team Member
        borderMap.put(AvatarId.ADD_TEAM_MEMBER,
                new ImageBorder("AddNewTeamMemberDialogBorderTop.png",
                        "AddNewTeamMemberDialogBorderLeft.png",
                        "AddNewTeamMemberDialogBorderBottom.png",
                        "AddNewTeamMemberDialogBorderRight.png"));

        // BORDER Confirm Dialogue
        borderMap.put(AvatarId.CONFIRM_DIALOGUE,
                new ImageBorder("ConfirmDialogueBorderTop.png",
                        "ConfirmDialogueBorderLeft.png",
                        "ConfirmDialogueBorderBottom.png",
                        "ConfirmDialogueBorderRight.png"));

        // BORDER Login Dialogue
        borderMap.put(AvatarId.PLATFORM_LOGIN,
                new ImageBorder("LoginDialogueBorderTop.png",
                        "LoginDialogueBorderLeft.png",
                        "LoginDialogueBorderBottom.png",
                        "LoginDialogueBorderRight.png"));

        // BORDER Send Dialogue
        borderMap.put(AvatarId.SESSION_SEND_FORM,
                new ImageBorder("SendDialogBorderTop.png",
                        "SendDialogBorderLeft.png",
                        "SendDialogBorderBottom.png",
                        "SendDialogBorderRight.png"));

        // BORDER Manage Contacts Dialogue
        borderMap.put(AvatarId.SESSION_MANAGE_CONTACTS,
                new ImageBorder("ManageContactsDialogueBorderTop.png",
                        "ManageContactsDialogueBorderLeft.png",
                        "ManageContactsDialogueBorderBottom.png",
                        "ManageContactsDialogueBorderRight.png"));

        // BORDER Search Partner Dialogue
        borderMap.put(AvatarId.SESSION_SEARCH_PARTNER,
                new ImageBorder("SearchPartnerDialogueBorderTop.png",
                        "SearchPartnerDialogueBorderLeft.png",
                        "SearchPartnerDialogueBorderBottom.png",
                        "SearchPartnerDialogueBorderRight.png"));

        // BORDER Invite Partner Dialogue
        borderMap.put(AvatarId.SESSION_INVITE_PARTNER,
                new ImageBorder("InvitePartnerDialogueBorderTop.png",
                        "InvitePartnerDialogueBorderLeft.png",
                        "InvitePartnerDialogueBorderBottom.png",
                        "InvitePartnerDialogueBorderRight.png"));

        // BORDER Default Avatar Border
        final Color highlight = new Color(153, 153, 153, 255);
        final Color shadow = new Color(153, 153, 153, 255);
        defaultBorder = BorderFactory.createEtchedBorder(
                EtchedBorder.RAISED, highlight, shadow);
    }

    /** An apache logger. */
    protected final Logger logger;

    /**
     * Create a WindowBorder.
     * 
     */
    WindowBorder() {
        super();
        this.logger = LoggerFactory.getLogger(getClass());
    }

    /**
     * Obtain the window border for an avatar.
     * 
     * @param avatarId
     *            The avatar id.
     * @return The window border.
     */
    Border get(final AvatarId avatarId) {
        if(borderMap.containsKey(avatarId)) {
            return (Border) borderMap.get(avatarId);
        }
        else {
            logger.warn("[BROWSER2] [PLATFORM] [WINDOW UTILS] [NO BORDER DEFINED " + avatarId + "]");
            return (Border) defaultBorder;
        }
    }
}
