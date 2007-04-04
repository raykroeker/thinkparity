/*
 * Created On: Aug 24, 2006 1:14:41 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.provider.dialog.profile;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.ophelia.model.profile.ProfileModel;

import com.thinkparity.ophelia.browser.application.browser.display.provider.SingleContentProvider;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class VerifyEMailProvider extends SingleContentProvider {

    /** An array of single content providers. */
    private final SingleContentProvider singleProvider;

    /**
     * Create VerifyEMailProvider.
     * 
     * @param profileModel
     *            A thinkParity profile interface.
     */
    public VerifyEMailProvider(final ProfileModel profileModel) {
        super(profileModel);
        this.singleProvider = new SingleContentProvider(profileModel) {
            @Override
            public Object getElement(final Object input) {
                Assert.assertNotNull("NULL INPUT", input);
                Assert.assertOfType("INPUT OF WRONG TYPE", Long.class, input);
                return profileModel.readEmail((Long) input);
            }
        };
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.provider.SingleContentProvider#getElement(java.lang.Object)
     */
    @Override
    public Object getElement(final Object input) {
        return singleProvider.getElement(input);
    }
}
