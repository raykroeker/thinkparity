/**
 * Created On: 7-Apr-07 11:35:47 AM
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.firstrun;

import java.awt.Color;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.ophelia.browser.platform.BrowserPlatform;
import com.thinkparity.ophelia.browser.platform.Platform;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.ophelia.browser.platform.util.State;

/**
 * <b>Title:</b>thinkParity Ophelia UI Platform Abstract First Run Page<br>
 * <b>Description:</b>An abstraction of a first run wizard page.<br>
 * 
 * @author robert@thinkparity.com
 * @version $Revision$
 */
abstract class DefaultSignupPage extends Avatar implements SignupPage {

    /** The <code>Platform</code>. */
    protected final Platform platform;

    /** The  <code>SignupDelegate</code>. */
    protected SignupDelegate signupDelegate;

    /** A <code>SignupHelper</code>. */
    private SignupHelper signupHelper;

    /**
     * Create a DefaultSignupPage.
     * 
     * @param l18nContext
     *            The localiztaion context.
     * @param background
     *            The avatar background colour.
     */
    public DefaultSignupPage(final String l18nContext, final Color background) {
        super(l18nContext, background);
        this.platform = BrowserPlatform.getInstance();
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.SignupPage#getPageName()
     */
    public String getPageName() {
        return getPageName(getId());
    }

    /** @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#getState() */
    public State getState() {
        throw Assert.createNotYetImplemented("SignupAvatar.DefaultSignupPage#getState");
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#isAvatarBackgroundImage()
     */
    @Override
    public Boolean isAvatarBackgroundImage() {
        return Boolean.FALSE;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.SignupPage#isFirstPage()
     */
    public Boolean isFirstPage() {
        return Boolean.FALSE;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.SignupPage#isLastPage()
     */
    public Boolean isLastPage() {
        return Boolean.FALSE;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.SignupPage#isNextOk()
     */
    public Boolean isNextOk() {
        return isInputValid();
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.SignupPage#reloadData()
     */
    public void reloadData() {
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.SignupPage#setDefaultFocus()
     */
    public void setDefaultFocus() {
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.SignupPage#setSignupDelegate(com.thinkparity.ophelia.browser.platform.firstrun.SignupDelegate)
     */
    public void setSignupDelegate(final SignupDelegate signupDelegate) {
        this.signupDelegate = signupDelegate;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#setState(com.thinkparity.ophelia.browser.platform.util.State)
     * 
     */
    public void setState(final State state) {
        throw Assert.createNotYetImplemented("SignupAvatar.DefaultSignupPage#setState");
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#validateInput()
     *
     */
    @Override
    public void validateInput() {
        super.validateInput();
    }

    /**
     * Get a shared localization string. Use this method to get strings that are
     * shared between all pages of the signup wizard.
     * 
     * @param localKey
     *            A localization key <code>String</code>.
     * @return A localized <code>String</code>.
     */
    protected String getSharedString(final String localKey) {
        return signupDelegate.getSharedLocalization().getString(localKey);
    }

    /**
     * Get the page name.
     * 
     * @param id
     *            A <code>AvatarId</code>.
     * @return The page name <code>String</code>.
     */
    protected String getPageName(final AvatarId id) {
        return id.toString();
    }

    /**
     * Get a SignupHelper
     * 
     * @return A <code>SignupHelper</code>.
     */
    protected SignupHelper getSignupHelper() {
        if (null == signupHelper) {
            signupHelper = new SignupHelper((SignupProvider) contentProvider, (Data) input);
        }
        return signupHelper;
    }

    /**
     * Determine if the string is empty.
     * 
     * @param text
     *            A <code>String</code>.
     * @return true if the string is null or blank; false otherwise.
     */
    protected Boolean isEmpty(final String text) {
        return (null==text || 0==text.length() ? Boolean.TRUE : Boolean.FALSE);
    }

    /**
     * Determine if the signup delegate has been initialized yet.
     * 
     * @return true if the signup delegate has been initialized.
     */
    protected Boolean isSignupDelegateInitialized() {
        return (null != signupDelegate);
    }
}
