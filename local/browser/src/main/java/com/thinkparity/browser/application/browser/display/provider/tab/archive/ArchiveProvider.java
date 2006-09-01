/*
 * Created On: Sep 1, 2006 8:17:27 AM
 */
package com.thinkparity.browser.application.browser.display.provider.tab.archive;

import com.thinkparity.browser.application.browser.display.provider.ContentProvider;

import com.thinkparity.model.parity.model.profile.Profile;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ArchiveProvider extends ContentProvider {

    /**
     * Create ArchiveProvider.
     * 
     * @param profile
     *            The local user profile.
     */
    public ArchiveProvider(final Profile profile) {
        super(profile);
    }
}
