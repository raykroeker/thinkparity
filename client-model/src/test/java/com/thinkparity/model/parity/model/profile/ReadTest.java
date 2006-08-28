/*
 * Created On: Jul 20, 2006 8:28:11 AM
 */
package com.thinkparity.model.parity.model.profile;

import com.thinkparity.model.parity.model.profile.Profile;
import com.thinkparity.model.parity.model.profile.ProfileModel;


/**
 * @author raymond@thinkparity.com
 * @version
 */
public class ReadTest extends ProfileTestCase {

    /** The test name. */
    private static final String NAME = "[LMODEL] [PROFILE] [READ TEST]";

    /** The test datum. */
    private Fixture datum;

    /** Create ReadTest. */
    public ReadTest() { super(NAME); }

    public void testRead() {
        final Profile profile = datum.pModel.read();
        assertNotNull(NAME, profile);
    }

    /**
     * @see com.thinkparity.model.parity.model.ModelTestCase#setUp()
     * 
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        login();
        final ProfileModel pModel = ProfileModel.getModel();
        datum = new Fixture(pModel);
    }

    /**
     * @see com.thinkparity.model.parity.model.ModelTestCase#tearDown()
     * 
     */
    @Override
    protected void tearDown() throws Exception {
        logout();
        datum = null;
        super.tearDown();
    }

    /** The test datum definition. */
    private class Fixture {
        private final ProfileModel pModel;
        private Fixture(final ProfileModel pModel) {
            this.pModel = pModel;
        }
    }
}
