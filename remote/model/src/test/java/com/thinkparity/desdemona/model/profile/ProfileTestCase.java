/*
 * Created On:  20-Sep-07 9:25:10 AM
 */
package com.thinkparity.desdemona.model.profile;

import com.thinkparity.desdemona.model.ModelTestCase;

/**
 * <b>Title:</b>thinkParity Desdemona Model Profile Test Case Abstraction<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
abstract class ProfileTestCase extends ModelTestCase {

    /**
     * Create ProfileTestCase.
     * 
     * @param name
     *            A <code>String</code>.
     */
    public ProfileTestCase(final String name) {
        super(name);
    }

    /** <b>Title:</b>Profile Test Case Fixture Abstraction<br> */
    protected abstract class Fixture extends ModelTestCase.Fixture {}
}
