/**
 * Created On: Apr 1, 2007 11:31:43 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.firstrun;

import com.thinkparity.codebase.model.session.Credentials;

import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarFactory;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.ophelia.browser.platform.application.window.WindowPanel;
import com.thinkparity.ophelia.browser.platform.application.window.WindowTitle;
import com.thinkparity.ophelia.browser.util.swing.OpheliaJFrame;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class SignupWindow extends OpheliaJFrame {

    /** The signup avatar. */
    private SignupAvatar signupAvatar;

    /** The signup data. */
    private SignupData signupData;

    /** The panel onto which all displays are dropped. */
    private WindowPanel windowPanel;

    /** Creates new form LoginWindow */
    public SignupWindow() {
        super(null);
        initSignupAvatar();
        windowPanel = new WindowPanel();
        windowPanel.getWindowTitle().setBorderType(WindowTitle.BorderType.WINDOW_BORDER2);
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        initComponents(signupAvatar);
        pack();
    }

    /**
     * Get the credentials.
     * 
     * @return The <code>Credentials</code>.
     */
    public Credentials getCredentials() {
        return (Credentials)signupData.get(SignupData.DataKey.CREDENTIALS);
    }

    /**
     * Determine if signup has been completed (the button pressed).
     * 
     * @return true if the signup has been completed.
     */
    public Boolean isSignupCompleted() {
        return signupAvatar.isSignupCompleted();
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
        windowPanel.addPanel(avatar, Boolean.TRUE);
        add(windowPanel);
    }

    /**
     * Initialize the signup avatar pages.
     */
    private void initSignupAvatar() {
        signupData = new SignupData();
        signupAvatar = (SignupAvatar)getAvatar(AvatarId.DIALOG_PLATFORM_SIGNUP);
        signupAvatar.setInput(signupData);
        signupAvatar.registerPage(getSignupPage(AvatarId.DIALOG_PLATFORM_SIGNUP_AGREEMENT));
        signupAvatar.registerPage(getSignupPage(AvatarId.DIALOG_PLATFORM_SIGNUP_ACCOUNT));
        signupAvatar.registerPage(getSignupPage(AvatarId.DIALOG_PLATFORM_SIGNUP_PROFILE));
        signupAvatar.reload();
    }
}
