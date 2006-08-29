/*
 * Created On: Aug 29, 2006 11:58:35 AM
 */
package com.thinkparity.browser.application.browser.display.provider.dialog.profile;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.browser.application.browser.display.provider.CompositeSingleContentProvider;
import com.thinkparity.browser.application.browser.display.provider.SingleContentProvider;

import com.thinkparity.model.parity.model.profile.Profile;
import com.thinkparity.model.parity.model.profile.ProfileModel;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ResetPasswordProvider extends CompositeSingleContentProvider {

    /** A single content provider. */
    private final SingleContentProvider[] singleContentProviders;

    /**
     * Create ResetPasswordProvider.
     * 
     * @param profile
     *            The local user's profile.
     * @param profileModel
     *            A thinkParity profile interface.
     */
    public ResetPasswordProvider(final Profile profile,
            final ProfileModel profileModel) {
        super(profile);
        this.singleContentProviders = new SingleContentProvider[] {
                new SingleContentProvider(profile) {
                    @Override
                    public Object getElement(final Object input) {
                        return profileModel.readSecurityQuestion();
                    }
                },
                new SingleContentProvider(profile) {
                    @Override
                    public Object getElement(final Object input) {
                        return profileModel.readCredentials();
                    }
                }};
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.provider.SingleContentProvider#getElement(java.lang.Object)
     */
    @Override
    public Object getElement(final Integer index, final Object input) {
        Assert.assertNotNull("NULL INDEX", index);
        Assert.assertInBounds("INDEX OUT OF BOUNDS", index, singleContentProviders);
        return singleContentProviders[index].getElement(input);
    }
}
