/**
 * Created On: 29-Jul-07 3:25:55 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.profile;


/**
 * @author robert@thinkparity.com
 * @version $Revision$
 */
public interface UpgradeAccountPage {

    /**
     * Get the text key of the next button.
     * 
     * @return The text key of the next button..
     */
    public String getNextButtonTextKey();

    /**
     * Get the name of the next page.
     * 
     * @return The name of the next page.
     */
    public String getNextPageName();

    /**
     * The name of this page.
     * 
     * @return The name of this page.
     */
    public String getPageName();

    /**
     * Get the name of the previous page.
     * 
     * @return The name of the previous page.
     */
    public String getPreviousPageName();

    /**
     * Determine if this is the first page.
     * 
     * @return true if this is the first page, false otherwise.
     */
    public Boolean isFirstPage();

    /**
     * Determine if this is the last page.
     * 
     * @return true if this is the last page, false otherwise.
     */
    public Boolean isLastPage();

    /**
     * Final check if it is OK to go to the next page.
     */
    public Boolean isNextOk();

    /**
     * Refresh. This is done when the page is shown.
     */
    public void refresh();

    /**
     * Reload.
     */
    public void reload();

    /**
     * Set the default focus.
     */
    public void setDefaultFocus();

    /**
     * Set the upgrade account delegate.
     * 
     * @param upgradeAccountDelegate
     *            The <code>UpgradeAccountDelegate</code>.
     */
    public void setUpgradeAccountDelegate(final UpgradeAccountDelegate upgradeAccountDelegate);

    /**
     * Validate input.
     */
    public void validateInput();
}
