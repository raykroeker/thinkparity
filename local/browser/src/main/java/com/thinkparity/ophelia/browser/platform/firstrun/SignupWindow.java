/**
 * Created On: Apr 1, 2007 11:31:43 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.firstrun;

import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarFactory;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.ophelia.browser.platform.application.window.WindowPanel;
import com.thinkparity.ophelia.browser.platform.application.window.WindowTitle;
import com.thinkparity.ophelia.browser.util.swing.OpheliaJFrame;

/**
 * <b>Title:</b>thinkParity OpheliaUI Sign-Up Window<br>
 * <b>Description:</b><br>
 * 
 * @author robert@thinkparity.com
 * @version $Revision$
 */
public final class SignupWindow extends OpheliaJFrame {

    /** The signup avatar. */
    private SignupAvatar signupAvatar;

    /** The signup data. */
    private SignupData signupData;

    /** The panel onto which all displays are dropped. */
    private WindowPanel windowPanel;

    /**
     * Create SignupWindow.
     * 
     */
    public SignupWindow() {
        super("SignupWindow");
        windowPanel = new WindowPanel();
        windowPanel.getWindowTitle().setBorderType(WindowTitle.BorderType.WINDOW_BORDER2);
        setTitle(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SignupWindow.Title"));
        initSignupAvatar();
        initComponents(signupAvatar);
        pack();
    }

    /**
     * Determine if signup has been cancelled.
     * 
     * @return true if the signup has been cancelled.
     */
    public Boolean isCancelled() {
        return signupAvatar.isCancelled();
    }

    /**
     * Get the requested avatar.
     * 
     * @param id
     *            The <code>AvatarId</code>.
     * @return The <code>Avatar</code>.
     */
    private Avatar getAvatar(final AvatarId id) {
        return AvatarFactory.create(id);
    }

    /**
     * Get the signup page.
     * 
     * @param id
     *            The <code>AvatarId</code>.
     * @return The <code>SignupPage</code>.
     */
    private SignupPage getSignupPage(final AvatarId id) {
        final Avatar avatar = getAvatar(id);
        avatar.setInput(signupData);
        ((SignupPage)avatar).setSignupDelegate(signupAvatar);
        return (SignupPage)avatar;
    }

    /**
     * Initialize the swing components on the window.
     * 
     * @param avatar
     *            The <code>Avatar</code>.
     */
    private void initComponents(final Avatar avatar) {
        windowPanel.addPanel(avatar);
        add(windowPanel);
    }

    /**
     * Initialize the signup avatar pages.
     */
    private void initSignupAvatar() {
        signupData = new SignupData();
        signupAvatar = (SignupAvatar)getAvatar(AvatarId.DIALOG_PLATFORM_SIGNUP);
        signupAvatar.setInput(signupData);
        signupAvatar.setWindowTitle(windowPanel.getWindowTitle());
        signupAvatar.registerPage(getSignupPage(AvatarId.DIALOG_PLATFORM_SIGNUP_INTRO));
        signupAvatar.registerPage(getSignupPage(AvatarId.DIALOG_PLATFORM_SIGNUP_AGREEMENT));
        signupAvatar.registerPage(getSignupPage(AvatarId.DIALOG_PLATFORM_SIGNUP_PROFILE));
        signupAvatar.registerPage(getSignupPage(AvatarId.DIALOG_PLATFORM_SIGNUP_ACCOUNT));
        signupAvatar.registerPage(getSignupPage(AvatarId.DIALOG_PLATFORM_SIGNUP_PAYMENT));
        signupAvatar.registerPage(getSignupPage(AvatarId.DIALOG_PLATFORM_SIGNUP_PAYMENT_PLAN));
        signupAvatar.registerPage(getSignupPage(AvatarId.DIALOG_PLATFORM_SIGNUP_SUMMARY));
        signupAvatar.registerPage(getSignupPage(AvatarId.DIALOG_PLATFORM_SIGNUP_FINISH));
        signupAvatar.registerPage(getSignupPage(AvatarId.DIALOG_PLATFORM_SIGNUP_CREDENTIALS));
        signupAvatar.registerPage(getSignupPage(AvatarId.DIALOG_PLATFORM_SIGNUP_LOGIN));
        signupAvatar.registerPage(getSignupPage(AvatarId.DIALOG_PLATFORM_SIGNUP_UPDATE_CONFIGURATION));
        signupAvatar.reload();
    }
}
