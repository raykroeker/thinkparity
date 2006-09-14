/*
 * Created On: Aug 23, 2006 6:13:44 PM
 */
package com.thinkparity.ophelia.model.profile;

import com.thinkparity.codebase.model.profile.Profile;

import com.thinkparity.ophelia.OpheliaTestUser;


/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class UpdateTest extends ProfileTestCase {

    /** Test name. */
    private static final String NAME = "TEST UPDATE";

    /** Test datum. */
    private Fixture datum;

    /** Create UpdateTest. */
    public UpdateTest() { super(NAME); }

    /** Test the profile model update api. */
    public void testUpdate() {
        datum.profileModel.update(datum.profile);
    }

    /**
     * @see com.thinkparity.ophelia.model.profile.ProfileTestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        login(OpheliaTestUser.JUNIT);
        final InternalProfileModel profileModel = getProfileModel(OpheliaTestUser.JUNIT);
        final Profile profile = profileModel.read();
        datum = new Fixture(profile, profileModel);
    }

    /**
     * @see com.thinkparity.ophelia.model.profile.ProfileTestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        datum = null;
        logout(OpheliaTestUser.JUNIT);
        super.tearDown();
    }

    /** Test datum definition. */
    private class Fixture extends ProfileTestCase.Fixture {
        private final Profile profile;
        private final InternalProfileModel profileModel;
        private Fixture(final Profile profile,
                final InternalProfileModel profileModel) {
            this.profile = profile;
            this.profileModel = profileModel;
        }
    }
}
