/**
 * Created On: Apr 2, 2007 11:01:22 AM
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.firstrun;

/**
 * @author robert@thinkparity.com
 * @version $Revision$
 */
public interface SignupPage {

    /**
     * Get the key for the next button name.
     * 
     * @return The <code>String</code> key for the next button name.
     */
    public String getKeyNextButton();

    /**
     * Get the key for the previous button name.
     * 
     * @return The <code>String</code> key for the previous button name.
     */
    public String getKeyPreviousButton();

    /**
     * Get the key for the special next button name.
     * 
     * @return The <code>String</code> key for the special next button name.
     */
    public String getKeySpecialNextButton();

    /**
     * Get the name of the next page.
     * 
     * @return The <code>String</code> name of the next page.
     */
    public String getNextPageName();

    /**
     * The name of this page.
     * 
     * @return The <code>String</code> name of this page.
     */
    public String getPageName();

    /**
     * Get the name of the previous page.
     * 
     * @return The <code>String</code> name of the previous page.
     */
    public String getPreviousPageName();

    /**
     * Invoke the action associated with the special next button.
     */
    public void invokeSpecialNextButtonAction();

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
     * Determine if there is a 'special' next button.
     * 
     * @return True if there is a 'special' next button.
     */
    public Boolean isSpecialNextButton();

    /**
     * Reload.
     */
    public void reload();

    /**
     * Reload data.
     */
    public void reloadData();

    /**
     * Save data.
     */
    public void saveData();

    /**
     * Set the default focus.
     */
    public void setDefaultFocus();

    /**
     * Set the signup delegate.
     * 
     * @param signupDelegate
     *            The <code>SignupDelegate</code>.
     */
    public void setSignupDelegate(final SignupDelegate signupDelegate);

    /**
     * Validate input.
     */
    public void validateInput();
}
