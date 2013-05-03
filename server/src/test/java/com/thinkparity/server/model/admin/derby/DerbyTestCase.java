/*
 * Created On:  9-Oct-07 1:46:54 PM
 */
package com.thinkparity.desdemona.model.admin.derby;

import com.thinkparity.desdemona.model.ModelTestCase;

/**
 * <b>Title:</b>thinkParity Desdemona Admin Model Derby Test Case Abstraction<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
abstract class DerbyTestCase extends ModelTestCase {

    /**
     * Create DerbyTestCase.
     *
     * @param name
     */
    protected DerbyTestCase(String name) {
        super(name);
    }

    /** <b>Title:</b>Derby Test Case Fixture Abstraction<br> */
    protected abstract class Fixture extends ModelTestCase.Fixture {}
}
