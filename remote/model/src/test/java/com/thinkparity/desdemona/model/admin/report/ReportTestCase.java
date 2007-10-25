/*
 * Created On:  9-Oct-07 1:46:54 PM
 */
package com.thinkparity.desdemona.model.admin.report;

import com.thinkparity.desdemona.model.ModelTestCase;

/**
 * <b>Title:</b>thinkParity Desdemona Admin Model Report Test Case Abstraction<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
abstract class ReportTestCase extends ModelTestCase {

    /**
     * Create ReportTestCase.
     *
     * @param name
     */
    protected ReportTestCase(String name) {
        super(name);
    }

    /** <b>Title:</b>Report Test Case Fixture Abstraction<br> */
    protected abstract class Fixture extends ModelTestCase.Fixture {}
}
