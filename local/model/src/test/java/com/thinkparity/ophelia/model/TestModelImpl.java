/*
 * Created On: Sep 26, 2006 8:38:34 AM
 */
package com.thinkparity.ophelia.model;

import com.thinkparity.codebase.model.Context;


/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
final class TestModelImpl extends AbstractModelImpl {

    /** Create TestModelImpl. */
    TestModelImpl() {
        super(null, null);
    }

    Context getTestContext() {
        return super.getContext();
    }
}
