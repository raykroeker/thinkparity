/**
 * Created On: 27-Jul-07 11:23:33 AM
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.action.profile;

import java.util.ArrayList;
import java.util.List;

import com.thinkparity.codebase.model.migrator.Feature;
import com.thinkparity.codebase.model.profile.Profile;

import com.thinkparity.ophelia.model.profile.ProfileModel;

import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.platform.action.AbstractBrowserAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class DowngradeAccount extends AbstractBrowserAction {

    /** The thinkParity browser application. */
    private final Browser browser;

    /**
     * Create DowngradeAccount.
     * 
     * @param browser
     *            The thinkParity browser application.
     */
    public DowngradeAccount(final Browser browser) {
        super(ActionId.PROFILE_DOWNGRADE_ACCOUNT);
        this.browser = browser;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
     */
    @Override
    public void invoke(final Data data) {
        if (browser.confirm("DowngradeAccount.ConfirmDowngradeAccountMessage")) {
            final ProfileModel profileModel = getProfileModel();
            final Profile profile = profileModel.read();
            List<Feature> features = new ArrayList<Feature>();
            profile.setFeatures(features);
            profileModel.update(profile);
        }
    }
}
