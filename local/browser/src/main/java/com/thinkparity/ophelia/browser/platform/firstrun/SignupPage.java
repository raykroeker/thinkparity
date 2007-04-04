/**
 * Created On: Apr 2, 2007 11:01:22 AM
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.firstrun;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public interface SignupPage {

    /**
     * The name of this page.
     * 
     * @return The name of this page.
     */
    public String getPageName();

    /**
     * Get the name of the next page.
     * 
     * @return The name of the next page.
     */
    public String getNextPageName();

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
     * Reload.
     */
    public void reload();

    /**
     * Save data.
     */
    public void saveData();

    /**
     * Set the signup delegate.
     */
    public void setSignupDelegate(final SignupDelegate signupDelegate);

    /**
     * Validate input.
     */
    public void validateInput();
}
