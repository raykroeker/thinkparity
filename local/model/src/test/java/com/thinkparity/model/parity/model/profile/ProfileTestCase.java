/*
 * Created On: Jul 20, 2006 8:25:39 AM
 */
package com.thinkparity.model.parity.model.profile;

import com.thinkparity.model.parity.model.ModelTestCase;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
abstract class ProfileTestCase extends ModelTestCase {

    /**
     * Assert the profile and all of its required fields are not null.
     * 
     * @param assertion
     *            The assertion.
     * @param profile
     *            The profile.
     */
    protected static void assertNotNull(final String assertion,
            final Profile profile) {
        assertNotNull(assertion + " [PROFILE IS NULL]", (Object) profile);
        assertNotNull(assertion + " [PROFILE ID IS NULL]", profile.getId());
        assertNotNull(assertion + " [PROFILE LOCAL ID IS NULL]", profile.getLocalId());
        assertNotNull(assertion + " [PROFILE NAME IS NULL]", profile.getName());
        assertNotNull(assertion + " [PROFILE VCARD IS NULL]", profile.getVCard());
    }

    /** Create ProfileTestCase. */
    protected ProfileTestCase(final String name) { super(name); }

    /**
     * @see com.thinkparity.model.parity.model.ModelTestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * @see com.thinkparity.model.parity.model.ModelTestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /** A datum definition abstraction. */
    protected abstract class Fixture {}
}