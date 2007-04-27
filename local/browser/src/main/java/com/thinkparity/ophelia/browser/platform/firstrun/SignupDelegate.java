/**
 * Created On: Apr 2, 2007 4:58:41 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.firstrun;

import com.thinkparity.ophelia.browser.util.localization.Localization;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public interface SignupDelegate {

    /**
     * Enable or disable the cancel button.
     * 
     * @param enable
     *            Enable <code>Boolean</code>.
     */
    public void enableCancelButton(final Boolean enable);

    /**
     * Enable or disable the next button.
     * 
     * @param enable
     *            Enable <code>Boolean</code>.
     */
    public void enableNextButton(final Boolean enable);

    /**
     * Get the shared localization. Used for generic messages that are
     * the same for all pages.
     */
    public Localization getSharedLocalization();

    /**
     * Determine if signup has been cancelled.
     * 
     * @return true if the signup has been cancelled, false otherwise.
     */
    public Boolean isCancelled();

    /**
     * Set the next page.
     * 
     * This method should be used only used when the page needs to change
     * without the user pressing the 'next' button.
     */
    public void setNextPage();
}
