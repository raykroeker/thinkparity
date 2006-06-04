/*
 * Sun Apr 30 11:34:18 PDT 2006
 */
package com.thinkparity.browser.platform.action;

import javax.swing.ImageIcon;

import com.thinkparity.browser.platform.Platform;

/**
 * The platform login action.
 *
 * @author raymond@thinkparity.com
 * @version 1.1
 */
public class SaveProfile extends AbstractAction {

    private static final ImageIcon ICON;

    private static final ActionId ID;

    private static final String NAME;

//    private static Object saveDialogue;

    static {
        ICON = null;
        ID = ActionId.PLATFORM_SAVE_PROFILE;
        NAME = "SaveProfile";
    }

    /** The platform. */
//    private final Platform platform;

    /**
     * Create SaveProfile.
     * 
     * @param platform
     *            The platform.
     */
    public SaveProfile(final Platform platform) {
        super(NAME, ID, NAME, ICON);
//        this.platform = platform;
    }

    /** @see com.thinkparity.browser.platform.action.AbstractAction#invoke(com.thinkparity.browser.platform.action.Data) */
    public void invoke(final Data data) throws Exception {
        // display the save profile dialogue
//        saveDialogue = new Object();
        //saveDialogue.display();
        //saveDialogue.

//        final UserModel uModel = UserModel.getModel();
//        final User user = uModel.read(jabberId);
//        user.setFirstName(saveDialogue.getFirstName());
//        user.setOrganization(saveDialoge.getOrganization());
    }
}
