/**
 * Created On: 29-Jul-07 3:22:31 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.profile;

import java.awt.Color;
import java.awt.Graphics;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.ophelia.browser.Constants.Images;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.ophelia.browser.platform.util.State;

/**
 * @author robert@thinkparity.com
 * @version $Revision$
 */
public abstract class DefaultUpgradeAccountPage extends Avatar implements
        UpgradeAccountPage {

    /** The <code>UpgradeAccountDelegate</code>. */
    protected UpgradeAccountDelegate upgradeAccountDelegate;

    /**
     * Create a DefaultUpgradeAccountPage.
     * 
     * @param l18nContext
     *            The localiztaion context.
     * @param background
     *            The avatar background colour.
     */
    public DefaultUpgradeAccountPage(final String l18nContext, final Color background) {
        super(l18nContext, background);
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.profile.UpgradeAccountPage#getNextButtonTextKey()
     */
    public String getNextButtonTextKey() {
        return null;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.profile.UpgradeAccountPage#getPageName()
     */
    public String getPageName() {
        return getPageName(getId());
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#getState()
     */
    @Override
    public State getState() {
        throw Assert.createNotYetImplemented("UpgradeAccountAvatar.DefaultUpgradeAccountAvatar#getState");
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#isAvatarBackgroundImage()
     */
    @Override
    public Boolean isAvatarBackgroundImage() {
        return Boolean.FALSE;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.profile.UpgradeAccountPage#isFirstPage()
     */
    public Boolean isFirstPage() {
        return Boolean.FALSE;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.profile.UpgradeAccountPage#isLastPage()
     */
    public Boolean isLastPage() {
        return Boolean.FALSE;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.profile.UpgradeAccountPage#isNextOk()
     */
    public Boolean isNextOk() {
        return isInputValid();
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.profile.UpgradeAccountPage#refresh()
     */
    public void refresh() {
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.profile.UpgradeAccountPage#setDefaultFocus()
     */
    public void setDefaultFocus() {
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#setState(com.thinkparity.ophelia.browser.platform.util.State)
     */
    @Override
    public void setState(final State state) {
        throw Assert.createNotYetImplemented("UpgradeAccountAvatar.DefaultUpgradeAccountAvatar#setState");
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.profile.UpgradeAccountPage#setUpgradeAccountDelegate(com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.profile.UpgradeAccountDelegate)
     */
    public void setUpgradeAccountDelegate(final UpgradeAccountDelegate upgradeAccountDelegate) {
        this.upgradeAccountDelegate = upgradeAccountDelegate;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#validateInput()
     */
    @Override
    public void validateInput() {
        super.validateInput();
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
     * Determine if the upgrade account delegate has been initialized yet.
     * 
     * @return true if the upgrade account delegate has been initialized.
     */
    protected Boolean isUpgradeAccountDelegateInitialized() {
        return (null != upgradeAccountDelegate);
    }

    /**
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        final Graphics g2 = g.create();
        try {
            // Draw the logo.
            g2.drawImage(Images.BrowserTitle.LOGO_LARGE,
                    (getWidth() - Images.BrowserTitle.LOGO_LARGE.getWidth()) / 2, 35,
                    Images.BrowserTitle.LOGO_LARGE.getWidth(),
                    Images.BrowserTitle.LOGO_LARGE.getHeight(), this);
        }
        finally { g2.dispose(); }
    }
}
