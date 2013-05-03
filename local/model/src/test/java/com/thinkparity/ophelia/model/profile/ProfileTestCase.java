/*
 * Created On: Jul 20, 2006 8:25:39 AM
 */
package com.thinkparity.ophelia.model.profile;

import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.profile.ProfileEMail;

import com.thinkparity.ophelia.OpheliaTestUser;
import com.thinkparity.ophelia.model.ModelTestCase;

/**
 * <b>Title:</b>thinkParity Profile Test Case<br>
 * <b>Description:</b>
 *
 * @author raymond@thinkparity.com
 * @version 1.1.2.2
 */
abstract class ProfileTestCase extends ModelTestCase {

    /**
     * Assert the profile and all of its required fields are not null.
     * 
     * @param profile
     *            The profile.
     */
    protected static void assertNotNull(final Profile profile) {
        assertNotNull("Profile reference is null.", (Object) profile);
        assertNotNull("Profile id reference is null.", profile.getId());
        assertNotNull("Profile local id reference is null.", profile.getLocalId());
        assertNotNull("Profile name reference is null.", profile.getName());
    }

    /**
     * Assert the profile email and all of its required fields are not null.
     * 
     * @param email
     *      A <code>ProfileEMail</code>.
     */
    protected static void assertNotNull(final ProfileEMail email) {
        assertNotNull("Profile email reference is null.", (Object) email);
        assertNotNull("Profile email address reference is null.", email.getEmail());
        assertNotNull("Profile email id reference is null.", email.getEmailId());
        assertNotNull("Profile email profile id reference is null.", email.getProfileId());
        assertNotNull("Profile email verified reference is null.", email.isVerified());
    }

    /**
     * Create ProfileTestCase.
     *
     * @param name
     *      A test name <code>String</code>.
     */
    protected ProfileTestCase(final String name) {
        super(name);
    }

    /**
     * Obtain a profile model.
     *
     * @param testUser
     *      A <code>OpheliaTestUser</code>.
     * @return An instance of <code>InternalProfileModel</code>.
     */
    protected InternalProfileModel getModel(final OpheliaTestUser testUser) {
        return getProfileModel(testUser);
    }

    /**
     * Set up the profile test case.
     *
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * Tear down the profile test case.
     *
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * <b>Title:</b>Profile Test Fixture Abstraction<br>
     * <b>Description:</b><br>
     */
    protected abstract class Fixture extends ModelTestCase.Fixture {}
}