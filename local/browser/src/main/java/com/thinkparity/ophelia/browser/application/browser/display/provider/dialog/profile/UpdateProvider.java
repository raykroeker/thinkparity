/*
 * Created On: Aug 24, 2006 1:14:41 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.provider.dialog.profile;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.profile.ProfileEMail;


import com.thinkparity.ophelia.browser.application.browser.display.provider.CompositeFlatSingleContentProvider;
import com.thinkparity.ophelia.browser.application.browser.display.provider.FlatContentProvider;
import com.thinkparity.ophelia.browser.application.browser.display.provider.SingleContentProvider;
import com.thinkparity.ophelia.model.profile.ProfileModel;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class UpdateProvider extends CompositeFlatSingleContentProvider {

    /** An array of flat content providers. */
    private final FlatContentProvider[] flatProviders;

    /** An array of single content providers. */
    private final SingleContentProvider[] singleProviders;

    /**
     * Create UpdateProvider.
     * 
     * @param profile
     *            The user's profile.
     */
    public UpdateProvider(final Profile profile, final ProfileModel profileModel) {
        super(profile);
        this.flatProviders = new FlatContentProvider[] {
                new FlatContentProvider(profile) {
                    @Override
                    public Object[] getElements(final Object input) {
                        return profileModel.readEmails().toArray(new ProfileEMail[] {});
                    }
                }
        };
        this.singleProviders = new SingleContentProvider[] {
                new SingleContentProvider(profile) {
                    @Override
                    public Object getElement(final Object input) {
                        return profileModel.read();
                    }
                }
        };
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.provider.CompositeFlatSingleContentProvider#getElement(java.lang.Integer, java.lang.Object)
     */
    @Override
    public Object getElement(final Integer index, final Object input) {
        Assert.assertNotNull("INDEX CANNOT BE NULL", index);
        Assert.assertInBounds("INDEX OUT OF BOUNDS", index, singleProviders);
        return singleProviders[index].getElement(input);
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.provider.CompositeFlatSingleContentProvider#getElements(java.lang.Integer, java.lang.Object)
     */
    @Override
    public Object[] getElements(final Integer index, final Object input) {
        Assert.assertNotNull("INDEX CANNOT BE NULL", index);
        Assert.assertInBounds("INDEX OUT OF BOUNDS", index, flatProviders);
        return flatProviders[index].getElements(input);
    }
}
